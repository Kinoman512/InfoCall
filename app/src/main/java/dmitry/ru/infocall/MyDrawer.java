package dmitry.ru.infocall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;

import dmitry.ru.infocall.adapters.ListDataCall;
import dmitry.ru.infocall.service.CallReceiver;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;

import static dmitry.ru.infocall.DrawView.DRAW_VIEW_POX_X;
import static dmitry.ru.infocall.DrawView.DRAW_VIEW_POX_Y;


/**
 * Created by Dmitry on 22.02.2016.
 */
public class MyDrawer {

    static DrawView myView;
    static ImageView img;
    private static WindowManager windowManager;
    private static ViewGroup windowLayout;
    private static boolean  ishown = false;
    private static LinkedHashMap<String, String> data;
    private static ListView listNumberData;
    private static Context context;
    private static TextView txt_name;
    private static int mPosX;
    private static int mPosY;
    private static String  MyDrawerTag = "MyDrawerTag";
    private static String  My_TIPS_TAG = "TIPS_DRAWER";
    private static View tips_overlay_info_window;


    public static void rewrite(LinkedHashMap<String, String> map) {
        MyDrawer.data = map;
//        myView.setData(map,false);
        listNumberData.setAdapter(new ListDataCall(context, map) );

        String name = data.get("name");

        if(name != null && !name.isEmpty()){
            txt_name.setText(name);
            txt_name.setVisibility(View.VISIBLE);

        }
    }
    public static void showWindow(final Context context,  LinkedHashMap<String, String> map, boolean isLockedScreen){
        showWindow(context,map,  isLockedScreen, false);
    }


    public static void showWindow(final Context context,  LinkedHashMap<String, String> map, boolean isLockedScreen, boolean needBtns) {
        if(map == null || map.isEmpty()){
            return;
        }
        ishown = true;
        Log.d("DrawView", "add  content " + map);
        if (windowLayout !=null){
            rewrite(map);
            return;
        }

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CallReceiver.context = context;
        MyDrawer.context = context;
        int paramForScreen;
        if(isLockedScreen){
             paramForScreen = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//             paramForScreen = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        }else{
             paramForScreen = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
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
        tips_overlay_info_window = (View) windowLayout.findViewById(R.id.tips_overlay_info_window);
        View content_info = (View) windowLayout.findViewById(R.id.content_info);
        View root_info = (View) windowLayout.findViewById(R.id.root_info);

        boolean skipTips = Setting.getBool(My_TIPS_TAG);
        if(needBtns && !skipTips ){
            root_info.setBackgroundColor(Color.WHITE);
            content_info.setAlpha(1);
            tips_overlay_info_window.setVisibility(View.VISIBLE);
            tips_overlay_info_window.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    tips_overlay_info_window.setVisibility(View.GONE);
                    Setting.setBool(My_TIPS_TAG,true);
                    return false;
                }
            });
        }else {
            tips_overlay_info_window.setVisibility(View.GONE);
        }
        img = (ImageView)  windowLayout.findViewById(R.id.imageView);
         listNumberData = (ListView)  windowLayout.findViewById( R.id.list_number_data ) ;

//        ListView lv = (ListView)  windowLayout.findViewById(R.id.callList);
        //lv.setAdapter(new CallListAdapter(context, map));

//        Log.d("DrawView", "width  is" + lv.getWidth());


        myView = (DrawView)  windowLayout.findViewById(R.id.MyView);
        txt_name = (TextView)  windowLayout.findViewById(R.id.txt_name);

        Button btn_save = (Button) windowLayout.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) windowLayout.findViewById(R.id.btn_cancel);




        txt_name.setVisibility(View.GONE);

        final LinearLayout frame_content = (LinearLayout)  windowLayout.findViewById(R.id.frame_content);

        int left = (int) DrawView.getInitX();
        int top = (int) DrawView.getInitY();

        RelativeLayout.LayoutParams paramsContent = new RelativeLayout.LayoutParams (
                FrameLayout.LayoutParams .MATCH_PARENT,
                FrameLayout.LayoutParams .WRAP_CONTENT
        );
        paramsContent.setMargins( 0, top, 0, 0);
        frame_content.setLayoutParams(paramsContent);

        myView.setMyView(frame_content);
        myView.setOnMoveListner(new DrawView.IListner() {
            @Override
            public void onMove(float x, float y) {

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (
                        FrameLayout.LayoutParams .MATCH_PARENT,
                        FrameLayout.LayoutParams .WRAP_CONTENT
                );
                 mPosX =  (int) x;
                 mPosY =  (int) y;
                params.setMargins( 0, mPosY, 0, 0);
                frame_content.setLayoutParams(params);

            }
        });

        if(!needBtns){
            btn_cancel.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting.setFuild(DRAW_VIEW_POX_X, mPosX);
                Setting.setFuild(DRAW_VIEW_POX_Y, mPosY);
                closeWindow();

                Toast.makeText(context,"Сохранено", Toast.LENGTH_LONG).show();
            }
        });





        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);





//        myView.setContent(lv,windowManager, map,false);

//        myView.setBack(fl);

        


        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                return false;
            }


        });



        rewrite(map);
        Log.d(MyDrawerTag, "1!!!");
        RunMainThread.runOnUiThread(context, new Runnable() {
            @Override
            public void run() {
                closeWindow();
                Log.d(MyDrawerTag, " get listner!!!");
                windowManager.addView(windowLayout, params);
            }
        });
    }

    public static void setAvatar( Bitmap bm) {
        //ImageView img = (ImageView)  windowLayout.findViewById(R.id.imageView);
        if(ishown){


            img.setImageBitmap(bm);

//            myView.setAvatar(img);
        }

        //windowManager.removeView(myView);
    }






    public static boolean isShowen() {
        if(windowLayout == null){
            return false;
        }else{
            return  true;
        }
    }

    public static void closeWindow() {

        try{
            if (windowLayout !=null){
                ishown= false;
                windowManager.removeView(windowLayout);
                windowLayout =null;
            }
        }catch (Exception e){

        }

    }
}
