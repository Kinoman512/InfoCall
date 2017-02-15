package dmitry.ru.infocall.utils.net.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.net.helper.NetJson;

/**
 * Created by Dmitry on 13.02.2016.
 */
public class Sp2All  extends AsyncTask<UserHandler, Void, String> {

    public static final String baseurl = "http://api.sp2all.ru/";

    public static final String[] listFuild = new String[]{
            "first_name@@name", "vk:old_user_id@@vk", "country_name@@country", "city_name@@city", "avatar:100@@avatar", "org_title@@organization", "birthday@@birthday"
    };

    public static final long waitResponse = 3000;

    private static void login(String username, String password, Handler handler) {
        String url = baseurl + "login";
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", username));
        params.add(new BasicNameValuePair("password", password));
        //					params.add(new BasicNameValuePair("authType", "mobile"));
        try {
            NetJson.requestJSONAsync(url, params, null, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getUserByPhone(String phone, String token, Handler handler) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("m", "getUserInfo"));
        params.add(new BasicNameValuePair("phone", phone));

        NetJson.requestJSONAsync(baseurl, params, token, handler);
    }

    public static void getInfo(UserHandler params) {
        UserHandler handler = params;
        String phone = handler.phone;
        getUserByPhone(phone, handler.getTokenSp2all(), handler.sphandler);
    }



    protected String doInBackground(UserHandler... params) {
        UserHandler handler = params[0];

        String username = handler.user;
        String password = handler.pass;
        String phone = handler.phone;


        if (handler.getLog() == null) {

            login(username, password, handler.loginhandler);
//            while (handler.getLog() == null) {
//                    Log.d("Sp2AllTask", "wait my auth for user " + phone + handler.loginSp2AllData);
//                    if (System.currentTimeMillis() - waitResponse > startTime) return null;
//
//            }
        }

        Log.d("Sp2AllTask", "i try to get data from  sp2all for " + phone);
        return null;
    }
}



