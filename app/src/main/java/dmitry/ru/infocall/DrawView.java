package dmitry.ru.infocall;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import dmitry.ru.infocall.utils.Setting;

/**
 * Created by Dmitry on 16.02.2016.
 */
public class DrawView extends View {

    private static final int INVALID_POINTER_ID = -1;
    public boolean hasAvatar;
    int posAvatar = 0;
    boolean hasName;

    public static String DRAW_VIEW_POX_X = "windowsX";
    public static String DRAW_VIEW_POX_Y = "windowsY";

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

        mPosX += getInitX();
        mPosY += getInitY();


        if (Setting.getFuildFloat(windowsScaleFactor) > 0) {
            mScaleFactor = Setting.getFuildFloat(windowsScaleFactor);

        }

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

    public static float getInitX() {
        return  Setting.getFuildFloat(DRAW_VIEW_POX_X);
    }

    public static float getInitY() {
        return  Setting.getFuildFloat(DRAW_VIEW_POX_Y);
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
                    onMoveListner.onMove(mPosX, mPosY);

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


    View view;

    IListner onMoveListner;



    interface IListner{
        public void onMove(float x, float y);
    }

    public void setOnMoveListner(IListner listner){
        onMoveListner = listner;
    }
    void setMyView(View v){
        this.view = v;
    }
    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0, mPosY);


       // Bitmap bmp = getBitmapFromView(view);
        //canvas.drawBitmap(bmp, 0, 0, mDrawText);




        canvas.restore();
    }

    public void setData( LinkedHashMap<String, String> map, boolean moved) {
        use++;

        isStatic = moved;

        if(isStatic ){
            mPosX = 0;
            mPosY = 0;
        }

        //Log.d("DrawView", "set content , used "+ use);

        invalidate();
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