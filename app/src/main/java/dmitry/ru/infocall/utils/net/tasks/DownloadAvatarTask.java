package dmitry.ru.infocall.utils.net.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.io.IOException;

import dmitry.ru.infocall.CacheImage;
import dmitry.ru.infocall.UserHandler;

/**
 * Created by Dmitry on 07.03.2016.
 */
public class DownloadAvatarTask extends AsyncTask<UserHandler, Void, Void> {

    @Override
    protected Void doInBackground(UserHandler... params) {

        UserHandler uh = params[0];
        String phone = uh.phone;
        Log.d("DownloadAvatarTask", "try to donaload avatar");

        try {


            Log.d("DownloadAvatarTask", "try to download image from  " + uh.avatarUrl);
            Bitmap bitmap = CacheImage.getBitmapFromMemCache(phone);
            if (bitmap == null) {
                Log.d("DownloadAvatarTask", "try to download image from  " + uh.avatarUrl);
                bitmap = Picasso.with(uh.context)
                        .load(uh.avatarUrl)
                        .get();

            }




            Message.obtain(uh.avatarHandler, UserHandler.OK, bitmap).sendToTarget();

            Log.d("DownloadAvatarTask", "the avatar is donwloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}