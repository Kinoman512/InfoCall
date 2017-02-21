package dmitry.ru.infocall.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Dmitry on 07.03.2016.
 */
public class DownloadAvatarTask extends AsyncTask<String, Void, Void> {

    public static String TAG = "DownloadAvatarTaskTAG";

    private final Context context;

    public DownloadAvatarTask(Context context){
        this.context = context;
    }


    OnAvatarGetListner  listner;

    public void setOnFinishDownloadListner(OnAvatarGetListner listner){
        this.listner = listner;
    }

    @Override
    protected Void doInBackground(String... params) {

        //UserHandler uh = params[0];
        String avatarUrl = params[0];


        Log.d(TAG, "try to donaload avatar");

        try {


          //  Log.d("DownloadAvatarTask", "try to download image from  " + uh.avatarUrl);
           final Bitmap bitmap;// = acheImage.getBitmapFromMemCache(phone);
          //  if (bitmap == null) {
                Log.d(TAG, "try to download image from  " + avatarUrl);
                bitmap = Picasso.with(context)
                        .load(avatarUrl)
                        .get();

           // }

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listner.OnFinishDownload(bitmap);
                }
            });

//            Message.obtain(uh.avatarHandler, UserHandler.OK, bitmap).sendToTarget();

            Log.d(TAG, "the avatar is donwloaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }


        return null;
    }


}