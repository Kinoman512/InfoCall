package dmitry.ru.infocall.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dmitry.ru.infocall.MyDrawer;
import dmitry.ru.infocall.TaskBean;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.contact.ContactUtil;
import dmitry.ru.infocall.utils.net.tasks.HtmlwebTask;
import dmitry.ru.infocall.utils.net.tasks.NumbusterTask;
import dmitry.ru.infocall.utils.net.tasks.Sp2All;

/**
 * Created by Dmitry on 21.02.2016.
 * занимается решением проблемы, откуда брать инфу, как ее парсить, возвращает готовую мапу
 */


public class ContactService {

    public static List<TaskBean> listtask = new ArrayList<>();

    public static boolean startServicesToGetInfo(UserHandler uh) {
        uh.phone = uh.phone.replaceAll("\\+|\\-", "");

        Context con = uh.context;
        String phone = uh.phone;
        boolean isEmptyNumber;


        phone = "7" + phone.substring(1);
        Log.d("ContactService", phone);
        isEmptyNumber = ContactUtil.getContactInfo(con, phone).isEmpty();
        Log.d("ContactService", "isEmptyNumber = " + isEmptyNumber);

        if(isEmptyNumber){
            phone = "8" + phone.substring(1);
            Log.d("ContactService", phone);
            isEmptyNumber = ContactUtil.getContactInfo(con, phone).isEmpty();
            Log.d("ContactService", "isEmptyNumber = " + isEmptyNumber);
        }
        if(!isEmptyNumber){
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


        phone = "7" + phone.substring(1);


        Log.d("ContactService", "Setting.APP_LIST_SERVICE.length  " + Setting.APP_LIST_SERVICE.length);
            for (int i = 0; i < Setting.APP_LIST_SERVICE.length; i++) {
                TaskBean tb = new TaskBean();

                String tag  = Setting.APP_LIST_SERVICE[i];

                tb.setTag(tag);
                
                Setting.init(uh.context);
                boolean b0 = false;
                try {
                    b0 = Setting.getServiceState(i, 0);
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
                        tb.setTask(new Sp2All());
                        tb.setHandler(uh.new Sp2AllHandler());
                        listtask.add(tb);
                        break;
                    case 1:
                        tb.setTask(new HtmlwebTask());
                        tb.setHandler(uh. new HtmlWebHandler());
                        listtask.add(tb);
                        break;
                    case 2:
                        tb.setTask(new NumbusterTask());
                        tb.setHandler(uh. new CommmonJsonHandler(Setting.APP_LIST_SERVICE[2],NumbusterTask.listFuild, null));
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
