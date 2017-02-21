package dmitry.ru.infocall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import dmitry.ru.myapplication.R;

/**
 * Created by Dmitry on 20.06.2016.
 */
public class CallLogAdapter extends BaseAdapter {

    Context context;
    int size;
    ImageView img;

    List< LinkedHashMap<String, String>> list;

    public CallLogAdapter(Context context, List<LinkedHashMap<String, String>>  myList) {
        size=myList.size();
        list = myList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View someView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (someView == null) {
            someView = inflater.inflate(R.layout.info, parent, false);
        }

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        img = (ImageView)  someView.findViewById(R.id.imageView);

//        ListView lv = (ListView)  someView.findViewById(R.id.callList);
        //lv.setAdapter(new CallListAdapter(context, map));

//        Log.d("DrawView", "width  is" + lv.getWidth());



        DrawView myView = (DrawView)  someView.findViewById(R.id.MyView);

        Bitmap bmp_back = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.back);
        TextView date = (TextView) someView.findViewById(R.id.textView2);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);





        int height = bmp_back.getHeight() ;
        Bitmap bmp_back2 = Bitmap.createScaledBitmap(bmp_back, metrics.widthPixels , height, false) ;


        Log.d("MyDrawer3", "backWidth " + bmp_back2.getWidth() + " backHeight " + bmp_back2.getHeight());
        Log.d("MyDrawer3", "width Metrics " + metrics.widthPixels + " Height " + metrics.heightPixels);


//        myView.setContent(lv, windowManager, list.get(pos), true);

//        myView.setBack(fl);
        myView.setHeight(36 * list.get(pos).size());



        String phone = list.get(pos).get("number");
        if(phone!= null){
            Bitmap bitmap = CacheImage.getBitmapFromMemCache(phone);
            if(bitmap == null){
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.default_callscreen);
            }
            replaceAvatar(context,myView,bitmap);
        }else{
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_callscreen);
            replaceAvatar(context,myView,bitmap);
        }



        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("MyListner", " get listner!!!");

                return false;
            }


        });
        String dateStr = list.get(pos).get("date");
        list.get(pos).remove("date");
        if(dateStr != null){
            date.setText(dateStr);
            date.setVisibility(View.VISIBLE);
        }

        int x = date.getWidth();
        int y = date.getHeight();

        //date.setY(10);//metrics.heightPixels - date.getHeight());

        ViewGroup.LayoutParams p2 = someView.getLayoutParams();
        p2.height = 36 * list.get(pos).size() + 50;
        someView.requestLayout();


        return someView;
    }

    public  void replaceAvatar(Context c, DrawView myView, Bitmap bm) {


            int w = 120;
            int h = 120;

            Bitmap bm2 = Bitmap.createScaledBitmap(bm, w,
                    h, false);

            img.setImageBitmap(bm2);

//            myView.setAvatar(img);
        }



}
