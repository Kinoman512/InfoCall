package dmitry.ru.infocall.utils.net.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import dmitry.ru.infocall.Cache;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.utils.net.helper.NetJson;

/**
 * Created by Dmitry on 07.03.2016.
 */
public class NumbusterTask extends AsyncTask<UserHandler, Void, String> {

    public static final String baseurl = "https://api.numbuster.com/api/person/by_phone/";
    public static final String token = "4hai9edgtokkckw4kc08sgw0o000skswk008cgw408goo0404w";



    public static final String[] listFuild = new String[]{
            "averageProfile:firstName@@name", "averageProfile:lastName@@lastName", "averageProfile:avatar:link@@avatar"
    };


    private static void getUserByPhone(String phone, String token, Handler handler) {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("access_token", token));
        String url = baseurl + phone;
        NetJson.requestJSONAsync(url, params, null, handler);
    }
    protected String doInBackground(UserHandler... params) {
        UserHandler uh = params[0];

        String phone = uh.phone;

        Handler handler =   uh.getTaskBeanByName(Setting.APP_LIST_SERVICE[2]).getHandler();
        getUserByPhone(phone, token, handler);

        Log.d("NumBuster", "i try to get data from  NumBuster for " + phone);
        return null;
    }
}



