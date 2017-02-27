package dmitry.ru.infocall.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dmitry.ru.infocall.SettingServers;
import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;

/**
 * Created by User on 23.02.2017.
 */

public class DialogSetToken extends Dialog implements
        android.view.View.OnClickListener {

    private final String tag;
    public Context context;
    public Dialog d;
    public Button yes, no;
    private EditText et_token;

    public DialogSetToken(Context a, String tag) {
        super(a);
        this.context = a;
        this.tag = tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_token);


        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_cancel);
        et_token = (EditText) findViewById(R.id.et_token);

        TextView txt_server_token = (TextView) findViewById(R.id.txt_server_token);
        txt_server_token.setText("Ключ для " + tag);


        String token = Setting.getString("token" + tag);

        if(token == null || token.isEmpty()){
            String key = SettingServers.getDefaultToken(tag);
            et_token.setText(key);
        }

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_yes:
                String token = et_token.getText().toString();

                if(token == null|| token.isEmpty()){
                    Toast.makeText(context,"Поле дожно быть заполнено", Toast.LENGTH_LONG).show();
                    return;
                }
                Setting.setString("token" + tag,token);
                break;
            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.btn_test:

                UserHandler uh = new UserHandler("79081906207", context ,false );
                uh.htmlWebHandler =  uh.new HtmlWebHandler();


                break;
            default:
                break;
        }
        dismiss();
    }


}