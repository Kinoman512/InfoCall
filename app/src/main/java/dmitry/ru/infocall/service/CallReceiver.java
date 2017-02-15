package dmitry.ru.infocall.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.ContactService;

/**
 * Created by Dmitry on 04.02.2016.
 */


public class CallReceiver extends BroadcastReceiver {

    public static Context context;
    private static boolean incomingCall = false;
    private static WindowManager windowManager;
    private static ViewGroup windowLayout;
    String phoneNumber = "";
    WindowManager wm;

    public CallReceiver(){
        Log.d("CallService", "construct  call receiver");
    }

    public void onReceive(Context context, Intent intent) {

        Log.d("CallService", "start call receiver");
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            //получаем исходящий номер
            phoneNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");


        } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")){


            String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            //Log.d("CallService2", "123" + phone_state);

            if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //телефон звонит, получаем входящий номер
                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("CallService", "Show window: " + phoneNumber);
                incomingCall = true;
//                showWindow(context, phoneNumber);
                UserHandler uh = new UserHandler(phoneNumber,context , true);
                ContactService.startServicesToGetInfo(uh);

            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                //телефон находится в режиме звонка (набор номера / разговор)
                if (incomingCall) {
                    Log.d("CallService", "Close window.");
                    incomingCall = false;
                    ContactService.stopWindow();
                }
            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                //телефон находиться в ждущем режиме. Это событие наступает по окончанию разговора, когда мы уже знаем номер и факт звонка
                if (incomingCall) {
                    Log.d("CallService", "Close window.");
                    incomingCall = false;
                    ContactService.stopWindow();

                }
            }else{
                ContactService.stopWindow();
            }
        }else{
            ContactService.stopWindow();
        }
    }




}