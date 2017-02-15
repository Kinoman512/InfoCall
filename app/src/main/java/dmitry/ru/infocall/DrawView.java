package dmitry.ru.infocall;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import dmitry.ru.infocall.utils.CallListAdapter;
import dmitry.ru.infocall.utils.Setting;

/**
 * Created by Dmitry on 16.02.2016.
 */
public class DrawView extends View {

    private static final int INVALID_POINTER_ID = -1;
    public boolean hasAvatar;
    int posAvatar = 0;
    boolean hasName;

    String windowsX = "windowsX";
    String windowsY = "windowsY";
    String windowsScaleFactor = "windowsScaleFactor";
    int use = 0;
    private float mPosX;
    private float mPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private Paint mDrawPaint;
    private Paint mDrawText;
    private List<Bitmap> contentList = new ArrayList<>();
    WindowManager windowManager;
    static DisplayMetrics metrics;

    int MAX_HEIGHT_BOX_W = 350;
    int MAX_WIDTH_VERTICAL_BOX = 500;
    int MIN_HEIGHT_VERTICAL_BOX = 250;
    int MIN_HEIGHT_BOX = 150;

    int heightBox = 0;
    int widthBox = 0;
    private ListView contentInfo;
    private LinkedHashMap<String, String> mapInfo;
    private int MAX_HEIGHT_BOX = 400;
    int height = 0;

    public boolean isStatic = false;

    public DrawView(Context context) {
        this(context, null, 0);
    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        mDrawPaint = new Paint();
        mDrawPaint.setColor(Color.RED);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawText = new Paint();
        mPosX = Setting.getFuildFloat(windowsX);
        mPosY = Setting.getFuildFloat(windowsY);
        if(Math.abs(mPosX) > 300 || Math.abs(mPosY) > 300  ){
            mPosX = 0;
            mPosY = 0;
        }


        if (Setting.getFuildFloat(windowsScaleFactor) > 0) {
            mScaleFactor = Setting.getFuildFloat(windowsScaleFactor);

        }

        contentList.add(null);
        contentList.add(null);
        contentList.add(null);
        contentList.add(null);

        heightBox = (int) Setting.getLong("heightBox");

    }

    public static Bitmap getBitmapFromView(View v) {
        int w;
        int h;
        // if (v.getMeasuredHeight() <= 0) {
        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Log.e("DrawView5", "Width: " + v.getMeasuredWidth() + " Height: " + v.getMeasuredHeight());
        if (v.getMeasuredHeight() >=1000 || v.getMeasuredHeight() <= 0) {
            w = metrics.widthPixels;
            h = 380;
        } else {
            w = v.getMeasuredWidth();
            h = v.getMeasuredHeight();
        }
        Log.e("DrawView", "Width2: " + w + " Height2: " + h);
        Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        if(isStatic) return false;

        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;
                    Setting.setFuild(windowsX, mPosX);
                    Setting.setFuild(windowsY, mPosY);
                    Setting.setFuild(windowsScaleFactor, mScaleFactor);

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    void onDrawBoxH(@NonNull Canvas canvas) {
        int x = 0;
        int y = 0;
        int n = -1;
        int u = 0;

        //узкий контейнер
        for (Bitmap e : contentList) {
            Log.d("DrawView", "  count elements " + n + " " + contentList.size());
            if (e == null) continue;
            Log.d("DrawView", "  mScaleFactor " + mScaleFactor);
            //y = 5;
            n++;


            switch (n) {
                //фон
                case 0:
                    heightBox = (int) (e.getHeight() * mScaleFactor);
                    widthBox = (int) (e.getWidth() * mScaleFactor);
                    Log.d("DrawView", " heightBox  " + heightBox + " widthBox " + widthBox);
                    Bitmap bmp = Bitmap.createScaledBitmap(e, e.getWidth(), heightBox, false);
                    canvas.drawBitmap(bmp, y, x, mDrawText);
                    //x +=  5;
                    continue;
                case 1:
                    //аватар
                    y = metrics.widthPixels / 2 - e.getWidth() / 2;
                    Log.d("DrawView", "avatar widthPixels " + y);
                    x += 10;
                    canvas.drawBitmap(e, y, x, mDrawText);

                    x += e.getHeight() + 5;
                    continue;
                case 2:
                    //имя
                    Log.d("DrawView7", " text2 align " + y + "  metrics.widthPixels  " +  metrics.widthPixels + " e.getWidth()" +  e.getWidth());
                    //if ( (y + e.getWidth()) > metrics.widthPixels )
                    //y = metrics.widthPixels/2  - 200;
                    int n2 = mapInfo.get("name").length();
                    y =  metrics.widthPixels / 2 - (25 * n2)/ 2 ;
                    //else
                   //     y = metrics.widthPixels / 1000;
                    u++;
                    x += 3;
                    Log.d("DrawView7", " text align " + y + " u  " + u);
                    canvas.drawBitmap(e, y, x, mDrawText);
                    continue;
                case 3:
                    y = 410;
                    x = 45;
                    continue;
                default:
                    continue;
            }
        }
    }

    void onDrawBoxW(@NonNull Canvas canvas) {
        int x = 0;
        int y = 0;
        int n = -1;
        int u = 0;

        //широкий контейнер
        for (Bitmap e : contentList) {
            if (e == null) continue;

            Log.d("DrawView", "  MIN_HEIGHT_BOX " + MIN_HEIGHT_BOX);
            n++;
            switch (n) {
                //фон
                case 0:
                    heightBox = (int) (e.getHeight() * mScaleFactor);
                    widthBox = (int) (e.getWidth() * mScaleFactor);
                    Log.d("DrawView", "  h " + heightBox + " w " + widthBox);
                    Bitmap bmp = Bitmap.createScaledBitmap(e, e.getWidth(), heightBox, false);
                    canvas.drawBitmap(bmp, y, x, mDrawText);
                    x += 5;
                    continue;
                case 1:
                    //аватар

                    Log.d("DrawView", "avatar widthPixels " + y);
                    x += 10;
                    y += 10;
                    y += metrics.widthPixels / 5;
                    if(isStatic ) y = 20;
                    canvas.drawBitmap(e, y, x, mDrawText);
                    y += e.getWidth() + 10;
                    Log.d("DrawView6", " text2 align " + y );
                    continue;
                case 2:
                    //имя

                    //y+= 300;
                    int n2 = mapInfo.get("name").length();
                    //y =  metrics.widthPixels / 2 - (17 * n2)/ 2 ;

                    Log.d("DrawView6", " text align " + y + " u  " + u);
                    canvas.drawBitmap(e, y, x, mDrawText);
                    y = 0;
                    continue;
                case 3:
                    y = 110;
                    x = 45;
                    continue;
                default:
                    continue;
            }
        }
    }

    void onDrawBoxB(@NonNull Canvas canvas) {
        int x = 0;
        int y = 0;
        int n = -1;

        // портер  и надпись
        for (Bitmap e : contentList) {
            if (e == null) continue;
            n++;

            switch (n) {
                //фон
                case 0:
                    heightBox = (int) (e.getHeight() * mScaleFactor);
                    widthBox = (int) (e.getWidth() * mScaleFactor);
                    Log.d("DrawView", " back h " + heightBox + " w " + widthBox);
                    Bitmap bmp = Bitmap.createScaledBitmap(e, e.getWidth(), heightBox, false);
                    canvas.drawBitmap(bmp, y, x, mDrawText);
                    x += 5;
                    continue;
                case 1:
                    //аватар

                    Log.d("DrawView", "avatar widthPixels " + y);
                    x += 10;
                    Bitmap bmp2 = Bitmap.createScaledBitmap(e, e.getWidth() * 2, e.getHeight() * 2, false);
                    y = metrics.widthPixels / 2 - bmp2.getWidth() / 2 + 30;
                    canvas.drawBitmap(bmp2, y, x, mDrawText);

                    x += bmp2.getHeight() + 5;
                    continue;
                case 2:
                    //имя

                    x += 3;
                    int n2 = mapInfo.get("name").length();
                    y =  metrics.widthPixels / 2 - (25 * n2)/ 2 ;
                    canvas.drawBitmap(e, y, x, mDrawText);
                    continue;
                case 3:
                    y = 110;
                    x = 45;
                    continue;
                default:
                    continue;
            }
        }
    }

    int getHeightListView(ListView lv){
        ListAdapter mAdapter = lv.getAdapter();
        int listviewElementsheight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, contentInfo);
            mView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            listviewElementsheight += mView.getMeasuredHeight();
        }
        return  listviewElementsheight;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0, mPosY);

        contentInfo.setAdapter(new CallListAdapter(getContext(), mapInfo, 1));
        MIN_HEIGHT_BOX =  getHeightListView(contentInfo) + 30;


        contentInfo.setAdapter(new CallListAdapter(getContext(), mapInfo, 0));
        MIN_HEIGHT_VERTICAL_BOX =  getHeightListView(contentInfo)+ contentList.get(1).getHeight() + 40;


        contentInfo.setAdapter(new CallListAdapter(getContext(), mapInfo, 2));
        MAX_HEIGHT_BOX_W =  getHeightListView(contentInfo)+ contentList.get(1).getHeight() * 2 + 30;
//      //  contentInfo.removeAllViews();



        int backWidth = metrics.widthPixels;
        int mode = 0;

        if(isStatic){
            mode = 4;
            heightBox = height;
        }else{
            Setting.setLong("heightBox",heightBox);
        }

        Log.d("DrawView2", "heightBox " + heightBox);
        Log.d("DrawView2", "backWidth " + backWidth);
        Log.d("DrawView2", "mScaleFactor " + mScaleFactor);
        //портрет и надпись


        if(heightBox >= MAX_HEIGHT_BOX_W && !isStatic){
            Log.d("DrawView", "портрет и надпись  ");

            contentInfo.setAdapter(new CallListAdapter(getContext(), mapInfo, 2));
            Bitmap bm = DrawView.getBitmapFromView(contentInfo);
            contentList.set(2, bm);
            onDrawBoxB(canvas);
        }else{
            //шировая часть
            //если  высота  сокращается, рисуется как узкая часть
            if (!(backWidth <= MAX_WIDTH_VERTICAL_BOX) && heightBox <= MIN_HEIGHT_VERTICAL_BOX || isStatic) {

                contentInfo.setAdapter(new CallListAdapter(getContext(), mapInfo, mode));
                int y2 = contentInfo.getHeight();

                Bitmap bm = DrawView.getBitmapFromView(contentInfo);
                contentList.set(2, bm);
                onDrawBoxW(canvas);
                Log.d("DrawView", " широкая часть ");
            } else {
                //узкая часть


                contentInfo.setAdapter(new CallListAdapter(getContext(), mapInfo, 1));
                Bitmap bm = DrawView.getBitmapFromView(contentInfo);
                contentList.set(2, bm);

                Log.d("DrawView", "узкая часть ");
                onDrawBoxH(canvas);
            }
        }




        canvas.restore();
    }

    public void setContent(ListView v, WindowManager wm, LinkedHashMap<String, String> map, boolean moved) {
        use++;
        windowManager = wm;

        metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        contentInfo = v;

        mapInfo = map;
        isStatic = moved;

        if(isStatic ){
            mPosX = 0;
            mPosY = 0;
        }

        //Log.d("DrawView", "set content , used "+ use);

        invalidate();
    }



    public void setBack(View v) {
        use++;
        Bitmap bm = DrawView.getBitmapFromView(v);


        Log.d("DrawView4", "backWidth " + bm.getWidth() + " backHeight " + bm.getHeight() + "use " + use);

        contentList.set(0, bm);
        invalidate();


    }

    public void setAvatar(View v) {

        Bitmap bm = DrawView.getBitmapFromView(v);

        contentList.set(1, bm);
        invalidate();
        hasAvatar = true;


    }

    public void setHeight(int height) {
        this.height = height;
        mScaleFactor = 0.6534475f;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float mScaleFactor2 = mScaleFactor * detector.getScaleFactor();
            Log.d("DrawView", "mScaleFactor " + mScaleFactor);
            // Don't let the object get too small or too large.
            mScaleFactor2 = Math.max(0.1f, Math.min(mScaleFactor2, 10.0f));
            Log.d("DrawView", "mScaleFactor2 " + mScaleFactor);

            if((metrics.widthPixels <= MAX_WIDTH_VERTICAL_BOX && heightBox < MIN_HEIGHT_VERTICAL_BOX )){
                if (mScaleFactor2 > mScaleFactor) {
                    mScaleFactor = mScaleFactor2;
                }
            }
            else if (heightBox < MIN_HEIGHT_BOX) {
                if (mScaleFactor2 > mScaleFactor) {
                    mScaleFactor = mScaleFactor2;
                }
            } else{

                if (heightBox > MAX_HEIGHT_BOX) {
                    if (mScaleFactor2 < mScaleFactor) {
                        mScaleFactor = mScaleFactor2;
                    }
                }else{
                    mScaleFactor = mScaleFactor2;
                }
            }



            invalidate();
            return true;
        }
    }

}