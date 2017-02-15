package dmitry.ru.infocall;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmitry.ru.infocall.utils.ContactService;

/**
 * Created by Dmitry on 27.03.2016.
 */
public class MyAlert {

    public static Context activity = null;
    public static Toolbar  toolbar = null;
    static   SweetAlertDialog savingAlert;
    static String phone ;

    public static void prepare(Context activity) {
        MyAlert.activity = activity;


    }

    public  static void show(){

        new MySweetAlertDialog(activity, MySweetAlertDialog.WARNING_TYPE)
                .setTitleText("Введите номер")
                .setConfirmText("Сохранить!")

                .setConfirmClickListener(new MySweetAlertDialog.OnSweetClickListener() {

                    public void onClick(MySweetAlertDialog sDialog) {
                        phone = sDialog.getEditText();
                        UserHandler uh = new UserHandler(sDialog.getEditText(), activity,false);
                        uh.isSave = true;
                        Log.d("MySweetAlert", sDialog.getEditText());
                        ContactService.startServicesToGetInfo(uh);
                        sDialog.cancel();

                        savingAlert = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
                        savingAlert.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        savingAlert.setTitleText("Loading");
                        savingAlert.setCancelable(true);
                        savingAlert.show();
                    }
                })
                .show();
    }

    public  static void show2(final String p){
        phone = p;
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Сохранить " + phone + " ?")
                .setConfirmText("Да")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    public void onClick(SweetAlertDialog sDialog) {
                        UserHandler uh = new UserHandler(phone, activity,false);
                        uh.isSave = true;
                        Log.d("MySweetAlert", uh.phone);
                        sDialog.cancel();


                        Boolean hasAlrady = ContactService.startServicesToGetInfo(uh);
                        if (!hasAlrady) {
                            savingAlert = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
                            savingAlert.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            savingAlert.setTitleText("Loading");
                            savingAlert.setCancelable(true);
                            savingAlert.show();
                        }

                    }
                })
                .show();
    }

    public static  void closeSavingAlert(boolean isSaved){
        if (savingAlert != null)
                savingAlert.cancel();

        if(isSaved){
            SweetAlertDialog sa = new SweetAlertDialog(activity, MySweetAlertDialog.SUCCESS_TYPE);
            sa.setTitleText("Сохранено!");
            FileSave.delete(activity.getApplicationContext(), phone);

            MainActivity.setFragment(new MenuFragment(), false);
            MainActivity.setFragment(new CallLogFragment(FileSave.read(activity.getApplicationContext()) ), true);
            sa.show();

        }else{
            SweetAlertDialog sa = new SweetAlertDialog(activity, MySweetAlertDialog.ERROR_TYPE);
            sa.setTitleText("Ошибка сохранения!");
            sa.show();
        }

    }

}
