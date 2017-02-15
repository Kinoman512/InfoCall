package dmitry.ru.infocall.utils.net.helper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dmitry.ru.infocall.Cache;
import dmitry.ru.infocall.Serializer;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.net.tasks.Sp2All;

/**
 * Created by Dmitry on 13.02.2016.
 */
public class NetJson {
        private static ExecutorService pool = Executors.newFixedThreadPool(20);
    private static Comparator<NameValuePair> comparator = new Comparator<NameValuePair>() {
        @Override
        public int compare(NameValuePair p1, NameValuePair p2) {
            int res = p1.getName().compareTo(p2.getName());
            if(res==0) {
                return p1.getValue().compareTo(p2.getValue());
            } else {
                return res;
            }
        }
    };

    static String cacheId = "NetJson";
    static Cache<String, Serializer> cache = new Cache<>(NetJson.cacheId);

    public static void requestJSONAsync(final String url,
                                        final ArrayList<NameValuePair> params, final String authtoken, final Handler handler) {
        pool.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject json = null;
                try {


                    String cacheKey = url + params.toString();
                    Serializer ser = cache.get(cacheKey);

                    if(ser != null)
                         json = new JSONObject(ser.getData());

                    if(json == null){
                        json = requestJSON(url, params, authtoken);

                        Serializer ser2 =  new Serializer(json.toString());
                        cache.put(cacheKey,ser2);
                    }

                    Log.i("NetworkUtil", "123" + json.toString());

                    if(!json.has("status") ){

                        Log.i("NetworkUtil", "requestJSONAsync() 2: succses!");
                        Message.obtain(handler, UserHandler.OK, json).sendToTarget();
                        return;


                    }

                    Log.d("NetworkUtil", "  json array " + json + json.isNull("status"));
                    if ("".equals(json.getString("status")) || json.isNull("status")  ) {
                        Log.i("NetworkUtil", "requestJSONAsync() 3: succses!");
                        Message.obtain(handler, UserHandler.OK, json).sendToTarget();
                        return;
                    }
                    if (json.getInt("status") == 200 || json.getInt("status") == 201) {
                        Log.i("NetworkUtil", "requestJSONAsync(): succses!");
                        Message.obtain(handler,  UserHandler.OK, json).sendToTarget();
                    } else if (json.getInt("status") == 204) {
                        Log.w("NetworkUtil", "requestJSONAsync(): no more items");
                        Message.obtain(handler,  UserHandler.NO_MORE, json).sendToTarget();
                    } else if (json.getInt("status") == 401) {
                        Log.w("NetworkUtil", "requestJSONAsync(): invalid auth token");
                        Message.obtain(handler,  UserHandler.AUTH, json).sendToTarget();
                    } else if (json.getInt("status") == 404) {
                        Log.w("NetworkUtil", "requestJSONAsync(): not found");
                        Message.obtain(handler,  UserHandler.NOT_FOUND, json).sendToTarget();
                    } else {
                        Log.w("NetworkUtil", "code " + json.getString("status") + " received");
                        Message.obtain(handler,  UserHandler.ERR, json).sendToTarget();
                    }

                } catch (Exception e) {
                    Log.e("ErrorJson" , e.toString());
                    Log.w("NetworkUtil", "requestJSONAsync(): " + e.toString());
                    Message.obtain(handler,  UserHandler.NOT_FOUND, json).sendToTarget();
                }
            }
        });
    }

    public static JSONObject requestJSON(String url,
                                         ArrayList<NameValuePair> params, String token) throws Exception {

        String text= "";
        if(url.equals(Sp2All.baseurl)){
             text = requestStringByPost(url, params, token);
        }else{
             text = requestStringByGet(url, params);

        }

        Log.d("NetworkUtil", "get: " + url + params.toString() + token);
        Log.d("NetworkUtil", "got: " + text);
        String isArray = "{values: _val_ , status: 200}";
        if (text.startsWith("[")){
            text = isArray.replace("_val_", text).replace("null", "");
        }
        JSONObject json = new JSONObject(text);

        return json;
    }

    public static String requestStringByGet(String url_string,
                                             ArrayList<NameValuePair> params ) throws Exception {
        String uid = null;
        String key = null;
        Log.i("NetworkUtil", "Get method");


        Log.d("NetworkUtil", url_string);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        String data = EntityUtils.toString(entity);
        Log.d("NetworkUtil", "params " + data);

        URL url = new URL(url_string + "?"+ data);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setConnectTimeout(1500);
            conn.setReadTimeout(2000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setUseCaches(false);


            conn.connect();

            Log.i("NetworkUtil", "conn.getRequestMethod() " + conn.getRequestMethod());

            if(conn.getResponseCode()!=HttpURLConnection.HTTP_OK) {
                throw new Exception("Received response code: " + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
            BufferedReader reader = new BufferedReader(in);

            String line;
            StringBuffer response = new StringBuffer();
            do {
                line = reader.readLine();
                response.append(line + "\n");
            } while (line != null);
            reader.close();
            conn.disconnect();
            Log.i("NetworkUtil", "4");
            return response.toString();
        } finally {
            conn.disconnect();
        }
    }

    public static String requestStringByPost(String url_string,
                                       ArrayList<NameValuePair> params, String autotoken) throws Exception {
        String uid = null;
        String key = null;
        Log.i("NetworkUtil", "77");
        if(url_string.equals(Sp2All.baseurl)){

            Log.i("NetworkUtil", "7");
            params.add(new BasicNameValuePair("noquote", "1"));
            for(NameValuePair param : params) {
                String value = param.getValue();
                Log.d("NetworkUtil", "param: " + param.getName() + "=" + value);
            }

            if(autotoken != null) {
                int i = autotoken.indexOf("|");
                if(i == -1) {
                    Log.e("NetworkUtil", "requestString: incorrect token");
                    return null;
                }
                uid = autotoken.substring(i+1);
                key = autotoken.substring(0, i);
            }
        }

        Log.d("NetworkUtil", url_string);
        URL url = new URL(url_string);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            if(autotoken != null   ) {
                String query = new String(key);
                params.add(new BasicNameValuePair("uid", uid));
                Collections.sort(params, comparator);

                for(NameValuePair param : params) {
                    query = query.concat("&" + param.getName() + "=" +
                            param.getValue());
                }
                Log.i("NetworkUtil", "3");
                String crc = MD5(query);
                Log.d("NetworkUtil", "crc of " + query + " is " + crc);
                params.add(new BasicNameValuePair("crc", crc));
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            String data = EntityUtils.toString(entity);
            Log.d("NetworkUtil", "params " + data);
            
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            Log.i("NetworkUtil", "conn.getRequestMethod() " + conn.getResponseMessage());

            if(conn.getResponseCode()!=HttpURLConnection.HTTP_OK) {
                throw new Exception("Received response code: " + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
            BufferedReader reader = new BufferedReader(in);

            String line;
            StringBuffer response = new StringBuffer();
            do {
                line = reader.readLine();
                response.append(line + "\n");
            } while (line != null);
            reader.close();
            conn.disconnect();
            Log.i("NetworkUtil", "4");
            return response.toString();
        } finally {
            conn.disconnect();
        }
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {}
        return null;
    }

//    public static String getError(Message msg){
//        String message = "error";
//        JSONObject jo = (JSONObject) msg.obj;
//        if(msg.obj instanceof ConnectTimeoutException || msg.obj instanceof SocketTimeoutException) {
//            message = "network error timeout";
//        } else if(msg.obj instanceof UnknownHostException) {
//            message = "network error host not found";
//        } else if(msg.obj instanceof ConnectException) {
//            message = "network error connect";
//        } else{
//            try {
//                if(jo.getInt("status") == 401){
//                    message = jo.getString("error");
//                }else {
//                    message = msg.toString();
//                }
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//            }
//        }
//        return message;
//    }
}
