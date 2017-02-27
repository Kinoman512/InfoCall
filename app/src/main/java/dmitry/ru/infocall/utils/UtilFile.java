package dmitry.ru.infocall.utils;

import android.content.Context;

/**
 * Created by User on 23.02.2017.
 */

public class UtilFile{

public static String getPath(Context context){
    return   context.getFilesDir().getPath().toString();
}
}