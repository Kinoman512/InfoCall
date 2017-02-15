package dmitry.ru.infocall;

import android.os.AsyncTask;
import android.os.Handler;

/**
 * Created by Dmitry on 06.03.2016.
 */
public class TaskBean {

    Boolean isWork = false;

    Handler handler;

    String  tag = "";

    AsyncTask<UserHandler, Void, String> task ;

    public Boolean getIsWork() {
        return isWork;
    }

    public void setIsWork(Boolean isWork) {
        this.isWork = isWork;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public AsyncTask<UserHandler, Void, String> getTask() {
        return task;
    }

    public void setTask(AsyncTask<UserHandler, Void, String> task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "isWork=" + isWork +
                ", handler=" + handler +
                ", tag='" + tag + '\'' +
                ", task=" + task +
                '}';
    }
}

