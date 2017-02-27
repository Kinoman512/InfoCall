package dmitry.ru.infocall;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.util.Arrays;
import java.util.List;

import dmitry.ru.infocall.service.MyService;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.BuildConfig;

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
            Setting.setBool(INIT_TAG, true);
            Setting.setBool( Setting.START_INFOCALL_TAG, true);

            Intent service = new Intent(this, MyService.class);
            this.startService(service);


            List<String> list = Arrays.asList(SettingServers.APP_LIST_SERVICE);

            int i = 0;
            for(String e : list){
                boolean bl = SettingServers.isNeedAccesToken(e);
                Setting.setBool(e,!bl);
                i++;
            }

        }




        test();

    }

    private void test() {

        if (!BuildConfig.DEBUG) {
            return;
        }

    }
}
