package dmitry.ru.infocall;

import android.content.Context;
import android.os.Handler;

/**
 * Created by User on 21.02.2017.
 */

public class RunMainThread {

    private static Handler handler;


    public static void runOnUiThread(Context context , Runnable r) {
        if(handler == null){
            handler = new Handler(context.getMainLooper());
        }

        handler.post(r);
    }
}
