package dmitry.ru.infocall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedHashMap;

import dmitry.ru.infocall.service.CallReceiver;
import dmitry.ru.infocall.utils.CallListAdapter;
import dmitry.ru.infocall.utils.contact.ContactUtil;
import dmitry.ru.myapplication.R;

/**
 * Created by Dmitry on 22.02.2016.
 */
public class MyDrawer {

    static DrawView myView;
    static ImageView img;
    private static WindowManager windowManager;
    private static ViewGroup windowLayout;
    private static boolean  ishown = false;



    public static void rewrite(Context context, String phone, LinkedHashMap<String, String> map) {




        ListView lv = (ListView)  windowLayout.findViewById(R.id.callList);
        //lv.setAdapter(new CallListAdapter(context, map));
        myView.setContent(lv, windowManager,map,false);






    }
    public static void showWindow(Context context, String phone, LinkedHashMap<String, String> map, boolean isLockedScreen) {
        if(map == null || map.isEmpty()){
            return;
        }
        ishown = true;
        Log.d("DrawView", "add  content " + map);
        if (windowLayout !=null){
            rewrite(context,phone,map);
            return;
        }
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CallReceiver.context = context;
        int paramForScreen;
        if(isLockedScreen){
             paramForScreen = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        }else{
             paramForScreen = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                paramForScreen,WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;




//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
///* | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON */,
//                PixelFormat.RGBA_8888);



        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.info, null);
        img = (ImageView)  windowLayout.findViewById(R.id.imageView);
        ListView lv = (ListView)  windowLayout.findViewById(R.id.callList);
        //lv.setAdapter(new CallListAdapter(context, map));

        Log.d("DrawView", "width  is" + lv.getWidth());


        myView = (DrawView)  windowLayout.findViewById(R.id.MyView);
        ImageView fl = (ImageView) windowLayout.findViewById(R.id.myBack);

        fl.setBackgroundColor(121);
        Bitmap bmp_back = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.back);
        fl.setImageBitmap(bmp_back);

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);





        int height = bmp_back.getHeight() ;
        Bitmap bmp_back2 = Bitmap.createScaledBitmap(bmp_back, metrics.widthPixels , height, false) ;


        Log.d("MyDrawer3", "backWidth " + bmp_back2.getWidth() + " backHeight " + bmp_back2.getHeight());
        Log.d("MyDrawer3", "width Metrics " + metrics.widthPixels + " Height " + metrics.heightPixels);

        fl.setImageBitmap(bmp_back2) ;
        Log.d("MyDrawer3", "width View " + fl.getWidth() + " Height " + fl.getHeight());





        myView.setContent(lv,windowManager, map,false);

        myView.setBack(fl);

        
        Bitmap img2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_callscreen);
        replaceAvatar(context, img2);


        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("MyListner", " get listner!!!");

                return false;
            }


        });




        windowManager.addView(windowLayout, params);
    }

    public static void replaceAvatar(Context c, Bitmap bm) {
        //ImageView img = (ImageView)  windowLayout.findViewById(R.id.imageView);
        if(ishown){

            int w = 120;
            int h = 120;

// Выводим уменьшенную в два раза картинку во втором ImageView
            Bitmap bm2 = Bitmap.createScaledBitmap(bm, w,
                    h, false);

            Log.d("DrawView", "show avatar !");
            img.setImageBitmap(bm2);

            myView.setAvatar(img);
        }

        //windowManager.removeView(myView);
    }








    public static void closeWindow() {
        if (windowLayout !=null){
            ishown= false;
            windowManager.removeView(windowLayout);
            windowLayout =null;
        }
    }
}
