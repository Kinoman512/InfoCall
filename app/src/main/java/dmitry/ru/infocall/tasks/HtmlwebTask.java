package dmitry.ru.infocall.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

import dmitry.ru.infocall.SettingServers;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.utils.net.helper.NetHtml;

/**
 * Created by Dmitry on 16.02.2016.
 */
public class HtmlwebTask   extends AsyncTask<UserHandler, Void, String> {

    public static final String baseurl = "http://htmlweb.ru/geo/api.php";
//    public static final String API_key = "e183d75e129c958aa5320a099390eb90";

    public static final long waitResponse = 30000;
    private static Context context;


    IFineshExecuteListner listner;
    private static String API_key;

    public void setFineshListner(IFineshExecuteListner listner){
        this.listner = listner;
    }


    public HtmlwebTask(Context context){
        this.context = context;
    }

    private static void getRegionByPhone(String phone , Handler handler) {

        Map<String,String> params = new LinkedHashMap<>();
        params.put("short", "link");
        params.put("telcod", phone);
        params.put("API_key", API_key);
        NetHtml.requestHTMLAsync(baseurl, params, handler,context);
    }

    protected String doInBackground(UserHandler... params) {




        UserHandler handler = params[0];
        String phone = handler.phone;

         API_key = Setting.getString("token" + SettingServers.APP_LIST_SERVICE[2] );

        if(API_key == null || API_key.isEmpty()){
            Toast.makeText(context,"Нет ключа для Htmlweb",Toast.LENGTH_LONG).show();
            return null;
        }

        getRegionByPhone(phone, handler.htmlWebHandler);



        return null;
    }

}


