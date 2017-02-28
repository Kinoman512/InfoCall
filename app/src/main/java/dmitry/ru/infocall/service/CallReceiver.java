package dmitry.ru.infocall.service;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.LinkedHashMap;

import dmitry.ru.infocall.MyDrawer;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.ContactService;
import dmitry.ru.infocall.utils.Setting;

/**
 * Created by Dmitry on 04.02.2016.
 */


public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {

//                showWindow(context, phoneNumber);
//                UserHandler uh = new UserHandler(phoneNumber,context , true);

//                UserHandler uh = new UserHandler("79044404193",context,false );


//        Toast.makeText(ctx, number + "",Toast.LENGTH_LONG).show();
//        number = "79044404193";

        //Toast.makeText(context, number , Toast.LENGTH_LONG).show();

        boolean isNeedStart = Setting.getBool( Setting.START_INFOCALL_TAG);

        if(!isNeedStart){
            return;
        }

        UserHandler uh = new UserHandler( number ,context,false );
        uh.isLockedScreen = true;
        uh.setGetDatalistner(new UserHandler.OnGetDataListner() {
            @Override
            public void OnGetData(LinkedHashMap<String, String> map) {
                if(map.size() <= 2){
                    String number = map.get("number");
                }

            }
        });
        ContactService.startServicesToGetInfo(uh, true, true, false);
        Log.d("1234",number);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        //
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
        MyDrawer.closeWindow();

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        //

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        MyDrawer.closeWindow();
    }







}