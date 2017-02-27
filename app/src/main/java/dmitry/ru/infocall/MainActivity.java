package dmitry.ru.infocall;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmitry.ru.infocall.fragment.JourneyFragment;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.view.DialogSearch;
import dmitry.ru.infocall.view.DrawPanel;
import dmitry.ru.myapplication.R;


public class MainActivity extends AppCompatActivity {


    public static Boolean  isShown = false;
    static FragmentManager fragmentManager;
    long start;

    String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,

            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS
            };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }


    public final static int REQUEST_CODE = 2006;



    @TargetApi(Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            // ** if so check once again if we have permission */
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted

                Toast.makeText(this,"permission was granted",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        fragmentManager = getFragmentManager();

//        MainActivity.setFragment(new JourneyFragment(list2), false);

        setCurrentFragment(new JourneyFragment(), false);
        Setting.init(this);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawPanel.prepare(this, toolbar);
        MyAlert.prepare(this);
        DrawPanel.start();
        start = System.currentTimeMillis();


        int PERMISSION_ALL = 1;

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        if (android.os.Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M){
            checkDrawOverlayPermission();
        }


    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        switch (id){
            case R.id.action_about:  aboutProgram();break;
            case R.id.action_search:  beginSearch();break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void beginSearch() {
        DialogSearch ds = new DialogSearch(this);
        ds.show();
    }

    private void aboutProgram() {



        String version = getResources().getString(R.string.app_name) + "\n" +  getResources().getString(R.string.version_text) + " " + getVersion();
        String about = getResources().getString(R.string.about);
        new SweetAlertDialog(MainActivity.this)
                .setTitleText(about)
                .setContentText(
                        version + "\n"
                )
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

    }


    public String getVersion() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        return version;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }


        if( MyDrawer.isShowen()){
            MyDrawer.closeWindow();
            return  false;
        }

        if (DrawPanel.getDrawer().isDrawerOpen()) {
            DrawPanel.getDrawer().closeDrawer();
            return  false;
        }


        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            return  false;
        }

        if (isShown) {
            isShown = false;
            return false;
        }



        if((System.currentTimeMillis() - start) > 3000 ){
            Toast.makeText(getApplicationContext(), "Нажмите снова, чтобы выйти ", Toast.LENGTH_SHORT).show();
            start = System.currentTimeMillis();
            return false;
        }


        return super.onKeyDown(keyCode, event);
    }


    private static void setCurrentFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.MainBox, fragment);
        if (addToBackStack) transaction.addToBackStack("test");
        transaction.commit();
    }

    public static void setFragment(Fragment fragment,boolean addToBackStack) {
        setCurrentFragment(fragment, addToBackStack);
    }


}
