package dmitry.ru.infocall;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;
import java.util.Map;

import dmitry.ru.infocall.service.CallReceiver;
import dmitry.ru.infocall.utils.ContactService;
import dmitry.ru.infocall.utils.DrawPanel;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.utils.contact.ContactUtil;
import dmitry.ru.myapplication.R;

import cn.pedant.SweetAlert.SweetAlertDialog;


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
        setCurrentFragment(new MenuFragment(), false);
        Setting.init(this);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawPanel.prepare(this, toolbar);
        MyAlert.prepare(this);
        DrawPanel.start();
        start = System.currentTimeMillis();


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
