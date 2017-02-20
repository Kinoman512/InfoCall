package dmitry.ru.infocall.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dmitry.ru.myapplication.R;

/**
 * Created by Dmitry on 22.02.2016.
 */
public class CallListAdapter extends BaseAdapter {

    Context context;
    LinkedHashMap<String, String> map;

    static  Map<String,String>  mapName  = new HashMap<>() ;
    // 0 - широкий
    // 1 - узкий
    // 2 - портрет
    int mode = 0;
    int padding = 80;
    int size;
    String name;
    String number;
    Map< String, Integer> count;
    Map<String,String>  tempMap;

    ArrayList <Map.Entry<String, String>> listKeys = new ArrayList <Map.Entry<String, String>>();

    public CallListAdapter(Context context, LinkedHashMap<String, String> linkedHashMap, int m) {
        this.context = context;
         map = linkedHashMap;

        if(map == null){
            map = new LinkedHashMap<>();
        }
        this.mode = m;


        if(map.size() == 0){
            int x =0;
        }
        map.remove("country");
        if(m != 4){
            map.remove("number");
        }



        listKeys = new ArrayList(map.entrySet());
        Collections.sort(listKeys, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> a, Map.Entry<String, String> b) {
//                if(a.getKey().equals(b.getKey())){
//                    int y =0;
//                }
                if(a.getKey() .equals( "name")) return  -1000;
                if(b.getKey() .equals( "name")) return  10000;

               if(a.getKey() .equals( "number")) return  1000;
                if(b.getKey() .equals( "number")) return  -10000;

                int x = a.getKey().compareTo(b.getKey());
  //              if(x > 5000) x = 4999;
                return x;
            }
        });
        if (mode == 2) {
            Map.Entry<String, String> e = listKeys.get(0);
            listKeys.clear();
            listKeys = new ArrayList<>();
            listKeys.add(e);

        }

        number = map.get("number");

        if(mapName.get(number) == null  ){
            name =listKeys.get(0).getValue();
            mapName.put(number, map.get("number"));
        }else{
            name =mapName.get(number);
        }

        count = new  HashMap<>();

        Map.Entry<String, String> temp = null;
        List<Map.Entry<String, String>> removedlist = new ArrayList<>();
        tempMap = new HashMap<>();

        for(Map.Entry<String, String> e : listKeys){
            String key = e.getKey();
            String[] arr = key.split(":");
            if(arr.length == 1){
                continue;
            }

            if (temp != null && e.getValue().equals(temp.getValue())) {
                tempMap.put(arr[0],e.getValue());
                removedlist.add(temp);

            }
            temp = e;


        };
        for(Map.Entry<String, String> e : removedlist){
            listKeys.remove(e);
        }


        size = listKeys.size();


    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int pos) {
        return listKeys.get(pos).getValue();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View someView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (someView == null) {
            someView = inflater.inflate(R.layout.item_list, parent, false);
        }
        TextView text = (TextView) someView.findViewById(R.id.ItemList);
        ImageView img = (ImageView) someView.findViewById(R.id.sourceImage);


        String key =  listKeys.get(pos).getKey();
        String[] arr = key.split(":");

        String temp = tempMap.get(arr[0]);
       ;

        if(arr.length != 1 &&  temp == null){
            String val = arr[1];
            Bitmap bm = null;


            switch (val){
                case "SP":  bm = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.sp); break;
                case  "HTMLWEB":    bm = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.htmlweb);break;
                default:    bm = null;

            }
            img.setImageBitmap(bm);
        }


        text.setTextSize(17);


        switch (pos) {
            case 0:
                if (mode == 1) {
                    text.setPadding(padding, 0, 0, 0);
                }
                if (mode == 2) {
                    text.setTextSize(25);
                }


                //if(map.get(nameKey) == null || map.get(nameKey).isEmpty()) return text;


                String str  =  listKeys.get(pos).getValue();

                if(str!= null && str.length() > this.name.length()){
                    mapName.put(number, map.get("name"));
                    name = str;
                }


                text.setText(name);

                break;
            default:


                String str2 =  listKeys.get(pos).getValue();

                text.setText(str2);
                int x = text.getHeight();
                Log.d("CallListAdapter", "  str  " + str2);


        }

        return someView;
    }
}
