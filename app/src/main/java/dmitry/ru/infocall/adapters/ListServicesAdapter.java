package dmitry.ru.infocall.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;

/**
 * Created by User on 21.02.2017.
 */
public class ListServicesAdapter  extends BaseAdapter {


    private final Context mContext;
    private List<String> list;

    public ListServicesAdapter(Context mContext) {
        list = Arrays.asList( Setting.APP_LIST_SERVICE);
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View someView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (someView == null) {

            someView = inflater.inflate(R.layout.item_service, parent, false);

            String tag = list.get(position);

            String name_service = "";

            TextView txt_service_name = (TextView) someView.findViewById(R.id.txt_service_name);
            CheckBox checkbox_service = (CheckBox) someView.findViewById(R.id.checkbox_service);
            ImageView img_service = (ImageView) someView.findViewById(R.id.img_service);

            name_service = getNameByTag(tag);
            if(name_service != null){
                txt_service_name.setText(name_service);

            }


            Drawable drawable = getImageByTag(tag);

            if(drawable != null) {
                img_service.setImageDrawable(drawable);
            }else{
                img_service.setVisibility(View.INVISIBLE);
            }
           // wordTV.setText(value);
    }


        return someView;


    }
    public final static int[] services_string = {
            R.string.drawer_item_service_sp,
            R.string.drawer_item_service_htmlweb,
            R.string.drawer_item_service_numbester
    };

    int[] DRAWABLES = {R.drawable.sp, R.drawable.htmlweb, R.drawable.numbuster};


    private String getNameByTag(String tag) {
        int type = 0;
        switch (tag.toUpperCase()){
            case "SP":   type = 0;break;
            case "HTMLWEB":type = 1;break;
            case "NUMBUSTER":type = 2;break;
        }
        return  mContext.getString(services_string[type]);
    }

    private Drawable getImageByTag(String tag) {
        int type = 0;
        Drawable dr = null;
        switch (tag.toUpperCase()){
            case "SP": type = 0; break;
            case "HTMLWEB":type = 1;break;
            case "NUMBUSTER":type = 2;break;
        }
        int id = DRAWABLES[type];
        dr= mContext.getResources().getDrawable(id);

        return dr;
    }
}