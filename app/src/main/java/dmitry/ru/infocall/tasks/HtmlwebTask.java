package dmitry.ru.infocall.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import dmitry.ru.infocall.Cache;
import dmitry.ru.infocall.RunMainThread;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.utils.net.helper.NetHtml;

/**
 * Created by Dmitry on 16.02.2016.
 */
public class HtmlwebTask extends AsyncTask<UserHandler, Void, String> {

    public static String TAG = "HtmlwebTaskTag";

    public static final String baseurl = "https://htmlweb.ru/geo/api.php";
    public static final String baseurl_user = "https://htmlweb.ru/user/api.php";
//    public static final String API_key = "e183d75e129c958aa5320a099390eb90";

    public static final long waitResponse = 30000;
    private static Context context;
    private static Cache cache;


    IFineshExecuteListner listner;
    private static String API_key;


    static String username = "Kinoman512";
    static String password = "Saboteur1";
    private String phone;
    private UserHandler userHandler;

    public void setFineshListner(IFineshExecuteListner listner) {
        this.listner = listner;
    }


    public HtmlwebTask(Context context) {
        this.context = context;
    }

    private static void getRegionByPhone(String phone, Handler handler) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("short", "link");
        params.put("telcod", phone);
        params.put("API_key", API_key);
        NetHtml.requestHTMLAsync(baseurl, params, handler, context);
    }


   abstract static class  MyTextHttpResponseHandler extends  TextHttpResponseHandler{
       String url;
       Handler handler;
       public void setConfig(Handler h, String url ){
           handler = h;
           this.url = url;
       }
   }


    static MyTextHttpResponseHandler txtHttpResponse = new MyTextHttpResponseHandler() {

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {



            if(statusCode == 0){
                Message.obtain(handler, UserHandler.TIME_OUT, responseString).sendToTarget();
                return;
            }
            JSONObject json = null;
            try {
                json = new JSONObject(responseString);
                Message.obtain(handler, UserHandler.ERR, json).sendToTarget();
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message.obtain(handler, UserHandler.ERR, responseString).sendToTarget();

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {


//            Serializer ser2 =  new Serializer(responseString);
            cache.put(url, responseString);
            JSONObject json = null;
            try {
                json = new JSONObject(responseString);

                Message.obtain(handler, UserHandler.OK, json).sendToTarget();
                return;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message.obtain(handler, UserHandler.ERR, responseString).sendToTarget();
        }
    };


    private static void getLogin(final Handler handler) {


        final AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000);
//        client.removeAllHeaders();
//        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");

        final String url = baseurl_user + "?login=" + username + "&password=" + password + "&json";
        JSONObject json = null;
        cache = null;
        try {
            cache = new Cache(TAG, context);
            String ser =  cache.get(url);

            if (ser != null && !ser.isEmpty())
                json = new JSONObject(ser);

            if (json == null) {
                txtHttpResponse.setConfig(handler, url);
                client.post(url,  txtHttpResponse);
            }
        } catch (Exception e) {

            Log.w(TAG, e.getMessage());
        }




    }

    public class HtmlwebLoginHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            if (msg.what == UserHandler.OK) {
                JSONObject data = (JSONObject) msg.obj;
                try {
                    API_key = data.getString("api_key");
                    Log.e(TAG, "api_key = " + API_key);
                    getRegionByPhone(phone, userHandler.htmlWebHandler);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (msg.what == UserHandler.TIME_OUT) {
                Toast.makeText(context, "Время ожидания истекло, проверьте подключение к интернету", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Ошибка при логировании HTMLWEB", Toast.LENGTH_SHORT).show();
            }

        }
    }


    protected String doInBackground(UserHandler... params) {


        userHandler = params[0];
        phone = userHandler.phone;

        API_key = Setting.getString("api_key_html" + username);

        if (API_key == null || API_key.isEmpty()) {
            // Toast.makeText(context,"Нет ключа для Htmlweb",Toast.LENGTH_LONG).show();
            RunMainThread.runOnUiThread(context, new Runnable() {
                @Override
                public void run() {
                    getLogin(new HtmlwebLoginHandler());
                }
            });
            return null;
        } else {
            getRegionByPhone(phone, userHandler.htmlWebHandler);
        }


        return null;
    }

}


