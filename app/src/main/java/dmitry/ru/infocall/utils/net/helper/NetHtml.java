package dmitry.ru.infocall.utils.net.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dmitry.ru.infocall.Cache;
import dmitry.ru.infocall.UserHandler;

/**
 * Created by Dmitry on 16.02.2016.
 */
public class NetHtml {
    public static final int OK = 0;
    public static final int ERR = 1;
    public static final int AUTH = 2;
    public static final int NO_MORE = 3;
    public static final int CONFLICT = 4;
    public static final int NOT_FOUND = 5;
    private static ExecutorService pool = Executors.newFixedThreadPool(20);

    static String cacheId = "NetHtml";
    static Cache cache;


    public static void requestHTMLAsync(final String url,
                                        final Map<String, String> params, final Handler handler, final Context context) {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 8;
        cache = new Cache(NetHtml.cacheId, context);

        pool.execute(new Runnable() {

            @Override
            public void run() {
                Document doc = null;
                try {
                    Log.i("NetworkUtil Html", "i try to get html  " + params.toString());
                    String cacheKey = url + params.toString();

                    String html = null;
                    if(cache!= null)
                         html = cache.get(cacheKey);

                    if (html != null && !html.isEmpty())
                        doc = Jsoup.parse(html);

                    if (doc == null) {
                        doc = requestHTML(url, params);
                        String text = doc.html();
                        cache.put(cacheKey, text );
                    }

                    Message.obtain(handler, OK, doc).sendToTarget();

                    Element link = doc.select("div.box1").first().select("a").first();

                    Log.i("NetworkUtil Html", "  city is " + link.html());


                }
                catch (UnknownHostException  e){
                    Log.w("NetworkUtil", " NetHtml: " + e.toString());
                    Message.obtain(handler,  UserHandler.TIME_OUT, doc).sendToTarget();
                }
                catch (SocketTimeoutException  e){
                    Log.w("NetworkUtil", " NetHtml: " + e.toString());
                    Message.obtain(handler,  UserHandler.TIME_OUT, doc).sendToTarget();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Message.obtain(handler, ERR , doc).sendToTarget();
                }
            }
        });
    }

    private static Document requestHTML(String url, Map<String, String> params) throws IOException {


            Document doc = Jsoup.connect(url)
                    .data(params)
                    .timeout(3000)
                    .get();

            return doc;

    }
}
