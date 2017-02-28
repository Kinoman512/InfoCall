package dmitry.ru.infocall;

import android.content.Context;
import android.graphics.drawable.Drawable;

import dmitry.ru.myapplication.R;

/**
 * Created by User on 23.02.2017.
 */

public class SettingServers {

    public static final  String[] APP_LIST_SERVICE = {"sp", "htmlweb", "numbuster" };


    public final static int[] services_string = {
            R.string.drawer_item_service_sp,
            R.string.drawer_item_service_htmlweb,
            R.string.drawer_item_service_numbester
    };

    public static  int[] DRAWABLES = {R.drawable.sp, R.drawable.htmlweb, R.drawable.numbuster};


    public static  boolean isNeedAccesToken(String tag) {
        switch (tag.toUpperCase()){
            case "SP":    return false;
            case "HTMLWEB": return true;
            case "NUMBUSTER": return true;
        }
        return  false;
    }

    public static String getNameByTag(Context context, String tag) {
        int type = 0;
        switch (tag.toUpperCase()){
            case "SP":   type = 0;break;
            case "HTMLWEB":type = 1;break;
            case "NUMBUSTER":type = 2;break;
        }
        return  context.getString(services_string[type]);
    }



    public static Drawable getImageByTag(Context context, String tag) {
        int type = 0;
        Drawable dr = null;
        switch (tag.toUpperCase()){
            case "SP": type = 0; break;
            case "HTMLWEB":type = 1;break;
            case "NUMBUSTER":type = 2;break;
        }
        int id = DRAWABLES[type];
        dr= context.getResources().getDrawable(id);

        return dr;
    }

    static String token_numbuster = "4hai9edgtokkckw4kc08sgw0o000skswk008cgw408goo0404w";
    static String token_htmlweb = "3f735098616a745fdafd5d8e4ddcc366";

    public static String getDefaultToken(String tag) {

        switch (tag.toUpperCase()) {
            case "HTMLWEB":
                return token_htmlweb;
            case "NUMBUSTER":
                return token_numbuster;
        }
        return "";

    }
}
