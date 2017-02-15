package dmitry.ru.infocall.utils.net.helper;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dmitry.ru.infocall.Cache;

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
    static Cache<String, String> cache = new Cache<>(NetHtml.cacheId);


    public static void requestHTMLAsync(final String url,
                                        final Map<String, String> params, final Handler handler) {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 8;


        pool.execute(new Runnable() {

            @Override
            public void run() {
                Document doc = null;
                try {
                    Log.i("NetworkUtil Html", "i try to get html  " + params.toString());
                    String cacheKey = url + params.toString();


                    String html = cache.get(cacheKey);

                    if (html != null)
                        doc = Jsoup.parse(html);

                    if (doc == null) {
                        doc = requestHTML(url, params);
                        String text = doc.html();
                        cache.put(cacheKey, text );
                    }

                    Message.obtain(handler, OK, doc).sendToTarget();

                    Element link = doc.select("div.box1").first().select("a").first();

                    Log.i("NetworkUtil Html", "  city is " + link.html());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static Document requestHTML(String url, Map<String, String> params) {

        try {


            Document doc = Jsoup.connect(url)
                    .data(params)
                    .timeout(3000)
                    .get();

            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
