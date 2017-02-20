package dmitry.ru.infocall;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.utils.net.tasks.DownloadAvatarTask;
import dmitry.ru.infocall.utils.net.helper.NetHtml;
import dmitry.ru.infocall.utils.net.tasks.SaveContactTask;
import dmitry.ru.infocall.utils.net.tasks.Sp2All;

/**
 * Created by Dmitry on 13.02.2016.
 */

//обработчик данных, получаемых с интернета
public class UserHandler {
    public static final int OK = 0;
    public static final int ERR = 1;
    public static final int AUTH = 2;
    public static final int NO_MORE = 3;
    public static final int CONFLICT = 4;
    public static final int NOT_FOUND = 5;
    public static List<TaskBean> listtask = new ArrayList<>();

    public final AvatarHandler avatarHandler = new AvatarHandler();
    public final LoginHandler loginhandler = new LoginHandler();
    public final Sp2AllHandler sphandler = new Sp2AllHandler();
    public final HtmlWebHandler htmlWebHandler = new HtmlWebHandler();
    public final SweetAlertHandler sweethandler = new SweetAlertHandler();

    public final LinkedHashMap<String, String> data = new LinkedHashMap<>();
    public LinkedHashMap<String, String> forSave = new LinkedHashMap<>();
    public JSONObject loginSp2AllData;

    public String user = "Kinoman512";
    public String pass = "074d0c3";
    public String avatarUrl = "";
    public String phone;
    public String userName = "";
    public Context context;
    public boolean isLockedScreen = false;

    public Boolean isSave = false;
    int x = 0;
    public boolean needSaveInJourney;


    public UserHandler(String phone, Context context, boolean isLockedScreen) {
        this.phone = phone;
        this.context = context;
        this.isLockedScreen = isLockedScreen;
    }

    public static LinkedHashMap<String, String> jsonToMap(String[] listFuild, JSONObject json) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (json == null) {
            Log.d("UserHandler", "There is no json object ");
            return map;
        }
        int length = listFuild.length;
        for (int i = 0; i <= length - 1; i++) {
            JSONObject jObj = json;
            String key = listFuild[i];
            String value = "Nothing";
            String trueKey;
            String[] arr = key.split(":");
            try {
                if (arr.length != 1) {
                    JSONObject splitObj = jObj;
                    for (int j = 0; j < arr.length - 1; j++) {
                        splitObj = splitObj.getJSONObject(arr[j]);
                    }
                    Log.d("UserHandler", " arr 1 = " + " " + arr[arr.length - 1] + " arr.length " + arr.length);


                    Log.d("UserHandler", " splitObj is " + splitObj);
                    Log.d("UserHandler", " key is " + key);
//                    Log.d("UserHandler", " trueKey is " + trueKey);
                    Log.d("UserHandler", " value is " + value);
                    jObj = splitObj;

                    String[] arrkey = arr[arr.length - 1].split("@@");


                    if (arrkey.length != 1) {
                        trueKey = arrkey[0];
                        key = arrkey[1];
                    } else {
                        key = arr[arr.length - 1];
                        trueKey = key;
                    }

                } else {
                    Log.d("UserHandler", " key is " + key);
                    Log.d("UserHandler", " value is " + value);

                    String[] arrkey = arr[0].split("@@");
                    if (arrkey.length != 1) {
                        trueKey = arrkey[0];
                        key = arrkey[1];
                    } else {
                        key = arr[arr.length - 1];
                        trueKey = key;
                    }
                }


                Log.d("UserHandler", " jObj is " + jObj);
                Log.d("UserHandler", " key2 is " + key);
                Log.d("UserHandler", " trueKey2 is " + trueKey);

                value = jObj.getString(trueKey);
                Log.d("UserHandler", " value2 is " + value);

                map.put(key, value);

            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }


        return map;
    }

    public JSONObject getLog() {
        Log.d("UserHandler", "X = " + x);
        x++;
        return loginSp2AllData;
    }

    void rewrite() {
    }


    public String getTokenSp2all() {
        if (loginSp2AllData == null) {
            Log.d("UserHandler", "i dont have Sp2all data login");

            return null;
        }
        String autotoken = "nothing";
        try {

            autotoken = loginSp2AllData.getString("key") + "|" + loginSp2AllData.get("id");
            Log.d("UserHandler", "build  autotoken for user " + loginSp2AllData.getString("login") + "   " + autotoken);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return autotoken;
    }

    public TaskBean getTaskBeanByName(String name) {
        for (TaskBean e : listtask) {
            if (e.getTask().getStatus() == AsyncTask.Status.RUNNING || e.getTask().getStatus() == AsyncTask.Status.FINISHED) {
                e.getTask().cancel(true);
            }

            if (e.getTag().equals(name)) {
                return e;
            }
        }
        return null;
    }

    void extraOptions(String tag, LinkedHashMap<String, String> map) {
        if (isSave) {
            Log.d("UserHandler", "is save work! " + listtask.size() + map.toString());

            data.putAll(map);
            if (listtask.size() <= 1) {


                if ("".equals(map.get("name")) || null == map.get("name")) {
                    Log.d("UserHandler", "  there is'nt save  contact !" + data.toString());
                    MyAlert.closeSavingAlert(false);

                    Iterator it = listtask.iterator();
                    while (it.hasNext()) {
                        TaskBean e = (TaskBean) it.next();

                        if (e.getTag().equals(tag)) {
                            listtask.remove(e);
                            break;
                        }
                    }

                    return;
                }
                Log.d("UserHandler", "  save  contact !" + data.toString());
                forSave.putAll(data);
                forSave.put("phone", this.phone);
                SaveContactTask sc = new SaveContactTask();
                sc.execute(this);


            }
            Iterator it = listtask.iterator();
            while (it.hasNext()) {
                TaskBean e = (TaskBean) it.next();

                if (e.getTag().equals(tag)) {
                    listtask.remove(e);
                    break;
                }
            }


            return;
        }

        for (TaskBean e : listtask) {
            if (e.getTag().equals(tag)) {
                Log.d("UserHandler", "extra options for the task  with tag " + tag);
//

                if (e.getIsWork()) {
                    Log.d("UserHandler", "the task for show");

                    // map.remove("vk");
                    map.remove("company");
                    map.remove("country");
                    map.remove("birthday");
                    map.remove("city");

                    String name = map.get("name");
                    if (name == null) {
                        name = "";
                    }
                    int u1= userName.length();
                    if (userName == "" ||
                            userName.length() < name.length())
                        userName = name;
                    map.put("name", userName);

                    Log.d("UserHandler2", "name " + map.get("name"));
                    map.remove("vk");


                    if (map.get("avatar") != null) {
                        avatarUrl = map.get("avatar");
                        new DownloadAvatarTask().execute(this);
                    }
                    map.remove("avatar");
                    map.put("number", phone);
                    data.putAll(map);
                    Log.d("UserHandler", "show windows with  data");

                    if (listtask.size() == 1) {
                        LinkedHashMap<String, String> data2 = new LinkedHashMap<>();
                        data2.putAll(data);

                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");

                        data2.put("date",  format.format(now));
                        boolean u = true;
                        List<LinkedHashMap<String, String>> list = FileSave.read(context);
                        for (Map<String, String> e2 : list) {
                            if (e2.get("number").equals( phone)) {
                                list.add( data2);
                                list.remove(e2);
                                u = false;
                                break;
                            }
                        }
                        if (u) {
                            list.add(data2);
                        }
                        if(needSaveInJourney){
                            FileSave.save(context, list);
                        }
                    }

                    MyDrawer.showWindow(context, phone, data ,this.isLockedScreen);
                }

                listtask.remove(e);
                break;
            }
        }
    }

    public class SweetAlertHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            Log.d("UserHandler", "SweetAlertHandler ");
            if (msg.what == OK) {
                MyAlert.closeSavingAlert(true);
            } else {
                MyAlert.closeSavingAlert(false);
            }

        }
    }

    public class CommmonJsonHandler extends Handler {
        String tag;
        String[] listFields;
        String[] removeOnShow;

        public CommmonJsonHandler(String t, String[] list, String[] removed) {
            tag = t;
            listFields = list;
            removeOnShow = removed;
        }

        public void handleMessage(Message msg) {
            Log.d("UserHandler", "CommmonJsonHandler");

            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            if (msg.what == OK) {

                JSONObject obj = (JSONObject) msg.obj;


                map.putAll(UserHandler.jsonToMap(listFields, obj));
                map.put("name", map.get("name") + " " + map.get("lastName"));
                map.remove("lastName");
                Log.d("UserHandler", "CommmonJsonHandler" + map);
            }
            extraOptions(tag, map);
        }
    }



    // класс для обработки информации о логине с сайта sp2all
    public class LoginHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            Log.d("UserHandler", "login from Sp2AllData ");
            if (msg.what == OK) {
                JSONObject data = (JSONObject) msg.obj;
                loginSp2AllData = data;
                try {
                    Log.d("UserHandler", "loginSp2AllData key " + getLog().getString("key"));
                    Sp2All.getInfo(UserHandler.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
//                String message = NetJson.getError(msg);
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class AvatarHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == OK) {
                Bitmap obj = (Bitmap) msg.obj;
                if (obj != null) {

                    CacheImage.addBitmapToMemoryCache(phone,obj);

                    Log.d("UserHandler", "Try to replace avatar  ");
                    MyDrawer.replaceAvatar(UserHandler.this.context, obj);
                }
            }
        }
    }

    public class Sp2AllHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            if (msg.what == OK) {
                JSONObject obj = (JSONObject) msg.obj;
                //sp2AllData = obj;
                map.putAll(UserHandler.jsonToMap(Sp2All.listFuild, obj));

                map.put("organization", "Должность : " + map.get("organization"));
                map.put("city:SP", "Город : " + map.get("city"));
                map.put("vk", "VK : " + "vk.com/" + map.get("vk"));
                map.put("avatar", Sp2All.baseurl + map.get("avatar"));
                map.put("company", "Sp2All");
                Log.d("Sp2AllData", "Data " + map);


            }
            extraOptions(Setting.APP_LIST_SERVICE[0], map);


        }
    }

    public class HtmlWebHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            if (msg.what == NetHtml.OK) {
                Document doc = (Document) msg.obj;
                if (doc == null) {
                    return;
                }
                Elements es = doc.select("div.box1 a");
                for (int i = 0; i < es.size(); i++) {
                    org.jsoup.nodes.Element e = es.get(i);
                    switch (i) {
                        case 0:
                            map.put("city:HTMLWEB", "Город : " + e.text());
                            break;
                        case 2:
                            map.put("country", "Страна :" + e.text());
                            break;
                    }

                    Log.d("UserHandler", "HtmlWeb " + e.text());

                }
                extraOptions(Setting.APP_LIST_SERVICE[1], map);
//                map.put("city_name", doc.select("d");
//                map.put("country_name", Sp2All.baseurl + map.get("avatar"));


            } else {
//                String message = NetJson.getError(msg);
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

        }
    }


}
