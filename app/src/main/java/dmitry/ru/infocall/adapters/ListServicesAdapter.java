package dmitry.ru.infocall.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import dmitry.ru.infocall.SettingServers;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.view.DialogLogin;
import dmitry.ru.myapplication.R;

import static dmitry.ru.infocall.SettingServers.getImageByTag;
import static dmitry.ru.infocall.SettingServers.getNameByTag;
import static dmitry.ru.infocall.SettingServers.isNeedAccesToken;

/**
 * Created by User on 21.02.2017.
 */
public class ListServicesAdapter  extends BaseAdapter {


    private final Context mContext;
    private List<String> list;

    public ListServicesAdapter(Context mContext) {
        list = Arrays.asList( SettingServers.APP_LIST_SERVICE);
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
    public View getView(final int position, View someView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (someView == null) {

            someView = inflater.inflate(R.layout.item_service, parent, false);

            final String tag = list.get(position);

            String name_service = "";

            TextView txt_service_name = (TextView) someView.findViewById(R.id.txt_service_name);
            final CheckBox checkbox_service = (CheckBox) someView.findViewById(R.id.checkbox_service);
            ImageView img_service = (ImageView) someView.findViewById(R.id.img_service);


            final boolean needAcces = isNeedAccesToken(tag);

            boolean isChecked = Setting.getBool(tag);

            checkbox_service.setChecked(isChecked);
            checkbox_service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked && needAcces){

                        String pass = Setting.getString("pass_" + tag);
                        String login = Setting.getString("login_" + tag);


                        if(pass == null || pass.isEmpty() || login == null || login.isEmpty() ){

                            checkbox_service.setChecked(false);
                            DialogLogin dl =new DialogLogin(mContext,tag, checkbox_service);
                            dl.show();
                           // Toast.makeText(mContext,"Вы должны установить ключ для этого сервиса", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    Setting.setBool(tag,isChecked);
                }
            });
            if(needAcces){
                someView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogLogin dst = new DialogLogin(mContext,tag, checkbox_service);
                        dst.show();
                    }
                });
            }


            name_service = getNameByTag(mContext, tag);
            if(name_service != null){
                txt_service_name.setText(name_service);

            }


            Drawable drawable = getImageByTag( mContext, tag);

            if(drawable != null) {
                img_service.setImageDrawable(drawable);
            }else{
                img_service.setVisibility(View.INVISIBLE);
            }
           // wordTV.setText(value);
    }


        return someView;


    }

}