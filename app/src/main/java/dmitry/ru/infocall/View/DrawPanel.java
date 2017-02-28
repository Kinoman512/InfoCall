package dmitry.ru.infocall.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import dmitry.ru.infocall.MainActivity;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.fragment.JourneyFragment;
import dmitry.ru.infocall.fragment.ServicesFragment;
import dmitry.ru.infocall.utils.ContactService;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;

public class DrawPanel {

    public final static String SERVICE_TAG = "service";
    public final static String SETTING_TAG = "setting_window";
    public final static String SERVICE_JOURNY = "journyes";


    public static AppCompatActivity activity = null;
    public static Toolbar toolbar = null;


    private static Drawer result = null;
    private static DrawerBuilder db = null;
    private static int positionActionService = -1;

    public static Drawer getDrawer() {
        return result;
    }

    public static void prepare(AppCompatActivity activity, Toolbar toolbar) {
        DrawPanel.activity = activity;
        DrawPanel.toolbar = toolbar;
    }

    private static DrawerBuilder add(DrawerBuilder drawer) {


        boolean check = Setting.getBool( Setting.START_INFOCALL_TAG);
        drawer.addDrawerItems(
                new SecondarySwitchDrawerItem().withName("Старт").withLevel(2).withIdentifier(0).withChecked(check).withOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                        if (drawerItem instanceof SecondarySwitchDrawerItem) {
                            SecondarySwitchDrawerItem sd = (SecondarySwitchDrawerItem) drawerItem;

//                                result.updateItem(sd);
//
//                            if(isChecked){
//
//                                Intent service = new Intent(activity, MyService.class);
//                                activity.startService(service);
//
//                            }else {
//
//                                Intent service = new Intent(activity, MyService.class);
//                                activity.stopService(service);
//                            }


                            Setting.setBool( Setting.START_INFOCALL_TAG, isChecked);
//                            Setting.saveServiceState(sd.getIdentifier(), 0, isChecked);
//                            Log.d("drawerItem", "add setting  for " + sd.getIdentifier() + isChecked);


                        }


                    }
                })
        );


        drawer.addDrawerItems(
                new PrimaryDrawerItem().withTag(SERVICE_TAG).withName(R.string.services).withIdentifier(144).withLevel(2),
                new DividerDrawerItem()
        );

        drawer.addDrawerItems(
                new PrimaryDrawerItem().withTag(SERVICE_JOURNY).withName(R.string.journy).withIdentifier(144).withLevel(2),
                new DividerDrawerItem()
        );

        drawer.addDrawerItems(
                new PrimaryDrawerItem().withTag(SETTING_TAG).withName(R.string.setting_window).withIdentifier(144).withLevel(2),
                new DividerDrawerItem()
        );


        return drawer;

    }


    public static void start() {


        db = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .withDrawerWidthDp(300)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        // positionActionService
                        Object tag = drawerItem.getTag();
                        if (tag == null) {
                            return false;
                        }
                        switch ((String) drawerItem.getTag()) {

                            case  SERVICE_JOURNY:
                                MainActivity.setFragment(new JourneyFragment(), true);
                                return false;
                            case SERVICE_TAG:
                                MainActivity.setFragment(new ServicesFragment(), true);
                                return false;
                            case SETTING_TAG:

                                UserHandler uh = new UserHandler("79081906207", activity,false );
                                ContactService.startServicesToGetInfo(uh, false, true, true);
                                return false;

                        }
                        return false;
                    }


                });

        db = add(db);

        result = db.build();
    }
}