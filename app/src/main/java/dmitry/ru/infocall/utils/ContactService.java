package dmitry.ru.infocall.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dmitry.ru.infocall.MyDrawer;
import dmitry.ru.infocall.SettingServers;
import dmitry.ru.infocall.TaskBean;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.tasks.HtmlwebTask;
import dmitry.ru.infocall.tasks.NumbusterTask;
import dmitry.ru.infocall.tasks.Sp2All;
import dmitry.ru.infocall.utils.contact.ContactUtil;
import dmitry.ru.myapplication.BuildConfig;

/**
 * Created by Dmitry on 21.02.2016.
 * занимается решением проблемы, откуда брать инфу, как ее парсить, возвращает готовую мапу
 */


public class ContactService {

    public static List<TaskBean> listtask;

    public static boolean startServicesToGetInfo(UserHandler uh) {
        return  startServicesToGetInfo(uh,false, true, false);
    }

    public static boolean startServicesToGetInfo(UserHandler uh, boolean needSaveInJourney, boolean needShowWindow, boolean needBtns) {

        listtask = new ArrayList<>();
        uh.phone = uh.phone.replaceAll("\\+|\\-", "");

        if (BuildConfig.DEBUG) {
            //uh.phone = "79044404193";
        }
        uh.needSaveInJourney = needSaveInJourney;
        uh.needShowWindow = needShowWindow;
        uh.needBtns = needBtns;

        Context con = uh.context;
        String phone = uh.phone;
        boolean isEmptyNumber;


        phone = "7" + phone.substring(1);
        Log.d("ContactService", phone);
        uh.phone = phone;
        isEmptyNumber = ContactUtil.getContactInfo(con, phone).isEmpty();
        Log.d("ContactService", "isEmptyNumber = " + isEmptyNumber);

        if(isEmptyNumber || needShowWindow || !needSaveInJourney){
            phone = "8" + phone.substring(1);
            Log.d("ContactService", phone);
            isEmptyNumber = ContactUtil.getContactInfo(con, phone).isEmpty();
            Log.d("ContactService", "isEmptyNumber = " + isEmptyNumber);
        }
        if(!isEmptyNumber && !needShowWindow && !needSaveInJourney){
            Log.d("ContactService", "there is number, dont need a running tasks");
            if(uh.isSave) {
                //MyAlert.closeSavingAlert(false);
                Toast.makeText(con, "Контакт уже был сохранен!",
                        Toast.LENGTH_SHORT).show();
                return true;
            }

            Toast.makeText(con, "Номер есть в контактах!!",  Toast.LENGTH_SHORT).show();
            return true;
        }




        Log.d("ContactService", "Setting.APP_LIST_SERVICE.length  " + SettingServers.APP_LIST_SERVICE.length);
            for (int i = 0; i < SettingServers.APP_LIST_SERVICE.length; i++) {
                TaskBean tb = new TaskBean();

                String tag  = SettingServers.APP_LIST_SERVICE[i];

                tb.setTag(tag);
                
                Setting.init(uh.context);
                boolean b0 = false;
                try {
                    b0 = Setting.getBool(tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("ContactService", "check the service state = " + b0);

                if(!b0){
                    continue;
                }
                tb.setIsWork(b0);
                switch (i) {
                    case 0:
                        tb.setTask(new Sp2All(con));
                        tb.setHandler(uh.new Sp2AllHandler());
                        listtask.add(tb);
                        break;
                    case 1:
                        tb.setTask(new HtmlwebTask(con));
                        tb.setHandler(uh. new HtmlWebHandler());
                        listtask.add(tb);
                        break;
                    case 2:
                        tb.setTask(new NumbusterTask(con));
                        tb.setHandler(uh. new NumbusterJsonHandler(SettingServers.APP_LIST_SERVICE[2],NumbusterTask.listFuild, null));
                        listtask.add(tb);
                        break;
                }



                Log.d("ContactService", "add new taskBean " + tb.toString());

            }
            UserHandler.listtask = listtask;

            boolean isStarted = false;
            for (TaskBean e : listtask) {

                Log.d("ContactService", " TaskBean task .getStatus()=" + e.getTask().getStatus());

                if (e.getTask().getStatus() == AsyncTask.Status.RUNNING || e.getTask().getStatus() == AsyncTask.Status.FINISHED){
                    e.getTask().cancel(true);
                }

                if (e.getIsWork()) {
                    if(e.getTask() != null){
                        Log.d("ContactServiceTask", "TaskBean e.getStatus() " + e.getTask().getStatus());

                    }
                    if (!e.getTask().isCancelled()){
                        e.getTask().execute(uh);
                        isStarted = true;
                    }

                }
            }
            Log.d("ContactService", "isStarted  = " + isStarted);
        return false;
    }

    public static void stopWindow() {
        MyDrawer.closeWindow();
    }


}
