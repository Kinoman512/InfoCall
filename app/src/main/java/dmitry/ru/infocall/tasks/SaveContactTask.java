package dmitry.ru.infocall.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.contact.ContactUtil;

import static dmitry.ru.infocall.UserHandler.OK;

/**
 * Created by Dmitry on 22.02.2016.
 */
public class SaveContactTask  extends AsyncTask<UserHandler, Void, Void> {



    OnAvatarGetListner  listner;

    public void setOnFinishDownloadListner(OnAvatarGetListner listner){
        this.listner = listner;
    }

    @Override
    protected Void doInBackground(UserHandler... params) {
        UserHandler uh = params[0];
        String phone = uh.phone;
        Log.d("SaveContactTask", "try to add new contact");

        try {

            Bitmap bitmap = Picasso.with(uh.context)
                    .load(uh.forSave.get("avatar")).get();


//            Message.obtain(uh.avatarHandler, OK, bitmap).sendToTarget();
            //listner.OnFinishDownload(bitmap);

            ContactUtil.addContact(uh.context, uh.forSave, phone, bitmap);
            Log.d("SaveContactTask", "the contact is added");
            Message.obtain( uh.sweethandler, OK, null).sendToTarget();

        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }
}
