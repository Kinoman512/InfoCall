package dmitry.ru.infocall.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.util.LruCache;

import java.util.LinkedHashMap;
import java.util.Map;

import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.net.helper.NetHtml;

/**
 * Created by Dmitry on 16.02.2016.
 */
public class HtmlwebTask   extends AsyncTask<UserHandler, Void, String> {

    public static final String baseurl = "http://htmlweb.ru/geo/api.php";
    public static final String API_key = "e183d75e129c958aa5320a099390eb90";

    public static final long waitResponse = 30000;


    private static void getRegionByPhone(String phone , Handler handler) {

        Map<String,String> params = new LinkedHashMap<>();
        params.put("short", "link");
        params.put("telcod", phone);
        params.put("API_key", API_key);
        NetHtml.requestHTMLAsync(baseurl, params, handler);
    }

    protected String doInBackground(UserHandler... params) {




        UserHandler handler = params[0];
        String phone = handler.phone;
        getRegionByPhone(phone, handler.htmlWebHandler);
        return null;
    }

}


