package dmitry.ru.infocall.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import dmitry.ru.infocall.MySweetAlertDialog;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.myapplication.R;

public class DrawPanel {

    public final static String tagService = "service";
    public final static String tagAction = "action";


    public final static int[] services = {
            R.string.drawer_item_service_sp,
            R.string.drawer_item_service_htmlweb,
            R.string.drawer_item_service_numbester
    };

    public final static String  saveContact = "saveContact";

    public final static int[] iconServices = {
           R.drawable.sp,
           R.drawable.htmlweb,
           R.drawable.numbuster,
    };


    public static AppCompatActivity activity = null;
    public static Toolbar toolbar = null;


    private static Drawer result = null;
    private static DrawerBuilder db = null;
    private static int positionActionService = -1;

    public static Drawer getDrawer(){
        return result;
    }
    public static void prepare(AppCompatActivity activity, Toolbar toolbar) {
        DrawPanel.activity = activity;
        DrawPanel.toolbar = toolbar;
    }

    private static DrawerBuilder add(DrawerBuilder drawer) {

        drawer.addDrawerItems(
                new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(144),
                new DividerDrawerItem()
        );

        for (int i = 0; i < services.length; i++) {

            boolean bool = false;
            try {
                bool = Setting.getServiceState(i, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Drawable bd;
            int id =  iconServices[i];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bd = activity.getResources().getDrawable(id, activity.getTheme());
            } else {
                bd = activity.getResources().getDrawable(id);
            }


            drawer.addDrawerItems(
                    new SecondarySwitchDrawerItem().withName(services[i]).withIcon(bd).withLevel(1).withTag(tagService).withIdentifier(i).withChecked(bool).withOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                            if (drawerItem instanceof SecondarySwitchDrawerItem) {
                                SecondarySwitchDrawerItem sd = (SecondarySwitchDrawerItem) drawerItem;

//                                result.updateItem(sd);
                                Setting.saveServiceState(sd.getIdentifier(), 0, isChecked);
                                Log.d("drawerItem", "add setting  for " + sd.getIdentifier() + isChecked);


                            }


                        }
                    })
            );

    }
//        drawer.addDrawerItems(
//                new DividerDrawerItem(),
//                new SecondaryDrawerItem().withName("Сохранить контакт")
//                        .withIcon(FontAwesome.Icon.faw_star)
//                        .withTag(saveContact).withOnDrawerItemClickListener(
//                        new Drawer.OnDrawerItemClickListener() {
//                            @Override
//                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//
//                                new MySweetAlertDialog(activity, MySweetAlertDialog.WARNING_TYPE)
//                                        .setTitleText("Введите номер?")
//                                        .setConfirmText("Сохранить!")
//
//                                        .setConfirmClickListener(new MySweetAlertDialog.OnSweetClickListener() {
//
//                                            public void onClick(MySweetAlertDialog sDialog) {
//                                                UserHandler uh = new UserHandler(sDialog.getEditText(), activity);
//                                                uh.isSave = true;
//                                                Log.d("MySweetAlert", sDialog.getEditText());
//                                                ContactService.startServicesToGetInfo(uh);
//
//
//                                                sDialog
//                                                        .setTitleText("Сохранено!")
//                                                        .setConfirmText("OK")
//                                                        .setConfirmClickListener(null)
//                                                        .invisibleEditText()
//                                                        .changeAlertType(MySweetAlertDialog.SUCCESS_TYPE);
//                                            }
//                                        })
//                                        .show();
//                                return true;
//                            }
//                        }
//                )
//        );

//        drawer.addDrawerItems(
//                new DividerDrawerItem()
//        );

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
                        switch ((String) drawerItem.getTag()) {
                            case saveContact: {

                                if (drawerItem instanceof SecondaryDrawerItem) {
                                    SecondaryDrawerItem sd = (SecondaryDrawerItem) drawerItem;




                                }


                            }
//                            case tagAction: {
//                                if (drawerItem instanceof PrimaryDrawerItem) {
//                                    PrimaryDrawerItem pi = (PrimaryDrawerItem) drawerItem;
//
//                                    boolean value = false;
//                                    if (pi.getBadge().getText() == "-") {
//                                        value = true;
//                                        pi.withBadge("+");
//                                    } else {
//                                        pi.withBadge("-");
//                                    }
//                                    result.updateItem(pi);
//                                    Setting.saveServiceState(selectedService.getIdentifier(), list.indexOf(pi), value);
//                                    Log.d("drawerItem", "add setting  for " + selectedService.getName().getText() + pi.getName().getText() + value);
//
//
//                                }
//                            }
//                        if (drawerItem instanceof SecondaryDrawerItem) {
//                            SecondaryDrawerItem pi = (SecondaryDrawerItem) drawerItem;
//
//
//
//                        }
                            Log.d("CallService", "drawerItem  = " + position);

                            return true;
                        }
                        return false;
                    }


                });

        db = add(db);

        result = db.build();
    }
}