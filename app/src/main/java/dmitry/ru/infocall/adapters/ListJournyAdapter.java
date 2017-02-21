package dmitry.ru.infocall.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import dmitry.ru.infocall.view.DialogInfo;
import dmitry.ru.infocall.tasks.DownloadAvatarTask;
import dmitry.ru.infocall.tasks.OnAvatarGetListner;
import dmitry.ru.myapplication.R;

/**
 * Created by User on 21.02.2017.
 */

public class ListJournyAdapter extends BaseAdapter {


    private List<LinkedHashMap<String, String>> list;
    private final Context mContext;

    int getValueRecord(String str) {
        String[] arrs;
        if ((arrs = str.split(":")).length == 2) {
            str = arrs[0];
        }
        ;

        switch (str) {
            case "number":
                return 100;
            case "city":
                return 50;
            case "organization":
                return 10;
        }
        return 0;
    }


    public ListJournyAdapter(Context mContext, List<LinkedHashMap<String, String>> data) {


        list = data;


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

            someView = inflater.inflate(R.layout.item_journy, parent, false);

            final LinkedHashMap<String, String> map = list.get(position);

            String number = map.get("number");
            String urlAvatar = map.get("avatar");


            TextView wordTV = (TextView) someView.findViewById(R.id.txt_number);
            final ImageView imageView = (ImageView) someView.findViewById(R.id.image_journy);
            final Button btn_call = (Button) someView.findViewById(R.id.btn_call);
            final Button btn_sms = (Button) someView.findViewById(R.id.btn_sms);
            final CardView card_number_info_small = (CardView) someView.findViewById(R.id.card_number_info_small);

            card_number_info_small.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInfo di = new DialogInfo(mContext,map);
                    di.show();
//                    Toast.makeText(mContext,"123",Toast.LENGTH_LONG).show();
                }
            });


            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = "79081906207";
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mContext.startActivity(intent);
                }
            });


            btn_sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String outCipherText= "";//editTextSMSCipherText.getText().toString();
                    String phoneNumber=  "79081906207";

                    String uri= "smsto:"+phoneNumber;
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
                    intent.putExtra("sms_body", outCipherText);
                    intent.putExtra("compose_mode", true);
                    mContext.startActivity(intent);
//                    finish();
                }
            });

            DownloadAvatarTask dat = new DownloadAvatarTask(mContext);
            dat.setOnFinishDownloadListner(new OnAvatarGetListner() {
                @Override
                public void OnFinishDownload(Bitmap bm) {
                    if(bm != null){

//                        int w = 240;
//                        int h = 240;
//
//                        Bitmap bm2 = Bitmap.createScaledBitmap(bm, w,
//                                h, false);
//                        mContext.
                        imageView.setImageBitmap(bm);
                    }
                }
            });
            if(urlAvatar!= null){
                dat.execute(urlAvatar);
            }


            if(number != null)
                 wordTV.setText(number);
        }


        return someView;


    }


}