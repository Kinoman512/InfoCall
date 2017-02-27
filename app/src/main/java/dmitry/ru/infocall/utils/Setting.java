package dmitry.ru.infocall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Dmitry on 01.03.2016.
 */
public class Setting {
    public static final String APP_PREFERENCES = "mysettings3";




    public static final String APP_ACTION_SAVE = "2";
    public static final String APP_ACTION_SHOW =  "1";
    public static final String APP_ACTION_PRIMARY = "0";

    public static final String START_INFOCALL_TAG =  "START_INFOCALL_TAG";


    private static  SharedPreferences mSettings;
    private static Context c;

    public static SharedPreferences init(Context con){
        c = con;
        return mSettings = c.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }


    public static void setBool(String key, Boolean value) {

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean( key, value);
        editor.apply();
    }

    public static boolean getBool(String key) {
        return  mSettings.getBoolean( key,  false );
    }






    public static long getLong( String key){
        if(mSettings == null)  {
//            Log.w("mysettings", "You need instal your setting");
            return 0;
        }

        return  mSettings.getLong(key, 0);
    }

    public static void setLong(String key, long value){
        if(mSettings == null)  {
//            Log.w("mysettings", "You need instal your setting");
            return  ;
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static float getFuildFloat( String key){
        if(mSettings == null)  {
//            Log.e("mysettings", "You need instal your setting");
            return 0;
        }

//        Log.d("mysettings", "get setting  for "    + key );
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
//        Log.d("mysettings", "add setting  for " + key + " v =  " + value);
    }

    public static String getString( String key){
        return  mSettings.getString(key,"");
    }

    public static void setString(String key, String value){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(key, value);
        editor.apply();
    }


}
