package dmitry.ru.infocall.tasks;

import android.content.Context;
import android.os.AsyncTask;

import dmitry.ru.infocall.UserHandler;

/**
 * Created by User on 28.02.2017.
 */

public class LoginHtmlwebTask  extends AsyncTask<UserHandler, Void, String> {


    private static Context context = null;

    public LoginHtmlwebTask(Context context){
        this.context =  context;
    }

    @Override
    protected String doInBackground(UserHandler... params) {



        return null;
    }
}
