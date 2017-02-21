package dmitry.ru.infocall;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.LinkedHashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmitry.ru.infocall.view.DialogSearch;
import dmitry.ru.infocall.fragment.MenuFragment;
import dmitry.ru.infocall.view.DrawPanel;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;


public class MainActivity extends AppCompatActivity {


    public static Boolean  isShown = false;
    static FragmentManager fragmentManager;
    static String filedir;
    long start;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        filedir =  getFilesDir().getPath().toString();
        fragmentManager = getFragmentManager();

        List<LinkedHashMap<String, String>> list2 = FileSave.read(this);
//        MainActivity.setFragment(new CallLogFragment(list2), false);

        setCurrentFragment(new MenuFragment(), false);
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

        MyDrawer.closeWindow();

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
