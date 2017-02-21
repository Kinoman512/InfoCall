package dmitry.ru.infocall;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import dmitry.ru.infocall.service.CallReceiver;
import dmitry.ru.infocall.utils.Setting;

/**
 * Created by User on 21.02.2017.
 */

public class InfoCallApplication extends Application {

    private static final String LIST_TAG = "InfoCallApplication";
    private static final String INIT_TAG = "InitKey";

    private static InfoCallApplication instance;


    public static Context getAppContext()

    {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();


        Setting.init(this);
        instance = this;

        boolean isInit = Setting.getBool(INIT_TAG);

        if (!isInit) {
            this.startService(
                    new Intent(this, CallReceiver.class));
            Toast.makeText(this, "Служба InfoCall запущена",
                    Toast.LENGTH_SHORT).show();

            Setting.setBool(INIT_TAG, true);
            Setting.setBool( Setting.START_INFOCALL_TAG, true);
        }




    }
}
