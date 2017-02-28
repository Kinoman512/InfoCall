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
            case "SP":    return true;
            case "HTMLWEB": return true;
            case "NUMBUSTER": return false;
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

    public static String getUrlByTag( String tag) {
        switch (tag.toUpperCase()){
            case "SP":  return  "https://sp2all.ru/registration/";
            case "HTMLWEB": return "https://htmlweb.ru/user/signup.php?";
//            case "NUMBUSTER":type = 2;break;
        }
        return   "";
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

    static String login_sp = "Kinoman512";
    static String login_htmlweb = "Kinoman512";

    static String pass_sp = "074d0c3";
    static String pass_htmlweb = "Saboteur1";




    public static String getDefaultToken(String tag) {

        switch (tag.toUpperCase()) {
            case "HTMLWEB":
                return token_htmlweb;
            case "NUMBUSTER":
                return token_numbuster;
        }
        return "";

    }
    public static String getDefaultLogin(String tag) {

        switch (tag.toUpperCase()) {
            case "SP":
                return login_sp;
            case "HTMLWEB":
                return login_htmlweb;

        }
        return "";

    }
    public static String getDefaultPass(String tag) {

        switch (tag.toUpperCase()) {
            case "SP":
                return pass_sp;
            case "HTMLWEB":
                return pass_htmlweb;

        }
        return "";

    }
}
