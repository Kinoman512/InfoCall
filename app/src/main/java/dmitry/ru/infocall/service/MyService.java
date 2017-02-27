package dmitry.ru.infocall.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created by User on 23.02.2017.
 */

public class MyService extends Service
{
    private static final String TAG = "MyService";
    private static CallReceiver callReceiver;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerReceiver();
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(callReceiver);
        callReceiver = null;
    }

    private void registerReceiver()
    {

        IntentFilter filter = new IntentFilter(Intent.ACTION_CALL);
        registerReceiver(callReceiver, filter);
    }
}