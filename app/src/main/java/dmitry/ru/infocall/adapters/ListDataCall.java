package dmitry.ru.infocall.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import dmitry.ru.myapplication.R;


/**
 * Created by User on 20.02.2017.
 */
public class ListDataCall extends BaseAdapter {


    private final LinkedHashMap<String, String> data;
    private final Context mContext;
    private List<String> keys = new ArrayList<>();

    int  getValueRecord(String str){
        String[] arrs;
        if ((arrs = str.split(":")).length == 2){
            str = arrs[0];
        };

        switch (str){
            case  "number" : return  100;
            case  "city" : return  50;
            case  "organization" : return  10;
        }
        return 0;
    }


    public ListDataCall(Context mContext, LinkedHashMap<String, String> map) {



        Iterator myVeryOwnIterator = map.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            String key=(String)myVeryOwnIterator.next();
            String value=(String)map.get(key);

            if(key .equals( "name")){
                continue;
            }
            keys.add(key);
        }

        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int val1 = getValueRecord(o1);
                int val2 = getValueRecord(o2);




                return (int) (val2 - val1);
            }
        });


        this.mContext = mContext;
        this.data = map;
    }


    @Override
    public int getCount() {
        return keys.size();
    }

    @Override
    public Object getItem(int position) {
            return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View someView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (someView == null) {

            someView = inflater.inflate(R.layout.item_data_call, parent, false);

            String key = keys.get(position);
            String value = data.get(key);



            TextView wordTV = (TextView) someView.findViewById(R.id.txt_info_call);
            ImageView imageView = (ImageView) someView.findViewById(R.id.image_info_calll);

            String[] arrs;
            String tag = "";
            if ((arrs = key.split(":")).length == 2){
                tag = arrs[1];

                Drawable drawable = getImageByTag(tag);

                if(drawable != null)
                     imageView.setImageDrawable(drawable);

            }else{
                imageView.setVisibility(View.INVISIBLE);
            }
            wordTV.setText(value);
        }


        return someView;


    }

    int[] DRAWABLES = {R.drawable.sp, R.drawable.htmlweb, R.drawable.numbuster};


    private Drawable getImageByTag(String tag) {
        int type = 0;
        Drawable dr = null;
        switch (tag){
            case "SP": type = 0; break;
            case "HTMLWEB":type = 1;break;
        }
        int id = DRAWABLES[type];
        dr= mContext.getResources().getDrawable(id);

        return dr;
    }
}