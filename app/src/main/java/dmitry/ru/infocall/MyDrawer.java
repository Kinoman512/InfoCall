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


    public static void rewrite(LinkedHashMap<String, String> map) {
        MyDrawer.data = map;
//        myView.setData(map,false);
        listNumberData.setAdapter(new ListDataCall(context, map) );

        String name = data.get("name");

        if(name != null){
            txt_name.setText(name);
            txt_name.setVisibility(View.VISIBLE);

        }
    }


    public static void showWindow(final Context context,  LinkedHashMap<String, String> map, boolean isLockedScreen) {
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

        
        Bitmap img2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_callscreen);

        setAvatar(img2);


        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("MyListner", " get listner!!!");

                return false;
            }


        });



        rewrite(map);
        windowManager.addView(windowLayout, params);
    }

    public static void setAvatar( Bitmap bm) {
        //ImageView img = (ImageView)  windowLayout.findViewById(R.id.imageView);
        if(ishown){

            int w = 240;
            int h = 240;

            Bitmap bm2 = Bitmap.createScaledBitmap(bm, w,
                    h, false);
            Log.d("DrawView", "show avatar !");
            img.setImageBitmap(bm2);

//            myView.setAvatar(img);
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
