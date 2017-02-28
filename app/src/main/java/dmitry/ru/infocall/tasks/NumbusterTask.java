package dmitry.ru.infocall.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import dmitry.ru.infocall.RunMainThread;
import dmitry.ru.infocall.SettingServers;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.net.helper.NetJson;

/**
 * Created by Dmitry on 07.03.2016.
 */
public class NumbusterTask extends AsyncTask<UserHandler, Void, String> {

    public static final String baseurl = "https://api.numbuster.com/api/person/by_phone/";
//    public static final String token = "4hai9edgtokkckw4kc08sgw0o000skswk008cgw408goo0404w";



    public static final String[] listFuild = new String[]{
            "averageProfile:firstName@@name", "averageProfile:lastName@@lastName", "averageProfile:avatar:link@@avatar"
    };
    private final Context context;

    public NumbusterTask(Context context){
        this.context = context;
    }


    private  void getUserByPhone(String phone, String token, Handler handler) {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("access_token", token));
        String url = baseurl + phone;
        NetJson.requestJSONAsync(url, params, null, handler,context);
    }
    protected String doInBackground(UserHandler... params) {
        UserHandler uh = params[0];
        String phone = uh.phone;
        String tag = SettingServers.APP_LIST_SERVICE[2];

        String token = SettingServers.getDefaultToken( tag);//Setting.getString("token" + );

        if(token == null || token.isEmpty()){
            RunMainThread.runOnUiThread(context, new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,"Нет ключа для Numbuster",Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        Handler handler =   uh.getTaskBeanByName(SettingServers.APP_LIST_SERVICE[2]).getHandler();
        getUserByPhone(phone, token, handler);

        Log.d("NumBuster", "i try to get data from  NumBuster for " + phone);
        return null;
    }
}



