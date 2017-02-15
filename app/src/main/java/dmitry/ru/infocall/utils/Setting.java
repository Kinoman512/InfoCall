package dmitry.ru.infocall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Dmitry on 01.03.2016.
 */
public class Setting {
    public static final String APP_PREFERENCES = "mysettings3";

    public static final  String[] APP_LIST_SERVICE = {"sp", "html", "numbuster" };



    public static final String APP_ACTION_SAVE = "2";
    public static final String APP_ACTION_SHOW =  "1";
    public static final String APP_ACTION_PRIMARY = "0";


    private static  SharedPreferences mSettings;
    private static Context c;

    public static SharedPreferences init(Context con){
        c = con;
        return mSettings = c.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void saveServiceState(long service, int key, Boolean value){
        if(mSettings == null)  {
            Log.w("mysettings", "You need instal your setting");
            return;
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(service + "" + key, value);
        editor.apply();
        Log.d("mysettings", "add setting  for " + service + " " + key + " v =  " + value);

    }

    public static boolean getServiceState(long service, int key) throws Exception {
        if(mSettings == null)  {
            Log.w("mysettings", "You need instal your setting");
            throw  new Exception("You need instal your setting");
        }

        Log.d("mysettings", "get setting  for "  + service + " " + key );
        return  mSettings.getBoolean(service +"" + key,  true );
    }



    public static long getLong( String key){
        if(mSettings == null)  {
            Log.w("mysettings", "You need instal your setting");
            return 0;
        }

        return  mSettings.getLong(key, 0);
    }

    public static void setLong(String key, long value){
        if(mSettings == null)  {
            Log.w("mysettings", "You need instal your setting");
            return  ;
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static float getFuildFloat( String key){
        if(mSettings == null)  {
            Log.e("mysettings", "You need instal your setting");
            return 0;
        }

        Log.d("mysettings", "get setting  for "    + key );
        return  mSettings.getFloat(key, 0);
    }

    public static void setFuild(String key, float value){
        if(mSettings == null)  {
            Log.w("mysettings", "You need instal your setting");
            return  ;
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putFloat(key, value);
        editor.apply();
        Log.d("mysettings", "add setting  for " + key + " v =  " + value);
    }


}
