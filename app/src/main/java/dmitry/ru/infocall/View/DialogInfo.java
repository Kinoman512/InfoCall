package dmitry.ru.infocall.View;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;

import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.adapters.ListDataCall;
import dmitry.ru.infocall.tasks.DownloadAvatarTask;
import dmitry.ru.infocall.tasks.OnAvatarGetListner;
import dmitry.ru.infocall.utils.ContactService;
import dmitry.ru.myapplication.R;

/**
 * Created by User on 21.02.2017.
 */

public class DialogInfo   extends Dialog implements
        android.view.View.OnClickListener {

    private final LinkedHashMap<String, String> data;
    public Context context;
    public Dialog d;
    public Button yes, no;
    public ListView listView;
    private TextView txt_name;

    public DialogInfo(Context a, LinkedHashMap<String, String> map) {
        super(a);
        this.context = a;
        this.data = map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_info);

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        txt_name = (TextView) findViewById(R.id.txt_name);
        listView = (ListView) findViewById(R.id.list_number_data);
        final ImageView image_avatar = (ImageView) findViewById(R.id.image_avatar);
        listView.setAdapter(new ListDataCall(context,data));


        DownloadAvatarTask dat = new DownloadAvatarTask(context);
        dat.setOnFinishDownloadListner(new OnAvatarGetListner() {
            @Override
            public void OnFinishDownload(Bitmap bm) {
                if(bm != null){
                    image_avatar.setImageBitmap(bm);
                }
            }
        });

        String avatarUrl = data.get("avatar");

        dat.execute(avatarUrl);

        String name = data.get("name");
        if(name != null){
            txt_name.setText(name);
        }

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_yes:
//                ((Activity) context).finish();
                String number = data.get("number");

                if(number != null){
                    UserHandler uh = new UserHandler(number, context, false);
                    uh.isSave = true;
                    ContactService.startServicesToGetInfo(uh);
                }else{
                    Toast.makeText(context,"Нет номера для сохранения!",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}