package dmitry.ru.infocall.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dmitry.ru.infocall.SettingServers;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;

/**
 * Created by User on 23.02.2017.
 */

public class DialogLogin extends Dialog implements
        android.view.View.OnClickListener {

    private final String tag;
    private final CheckBox checkbox_service;
    public Context context;
    public Dialog d;
    public Button yes, no;
    private EditText et_pass;
    private EditText et_login;
    private Button btn_reg;

    public DialogLogin(Context a, String tag, CheckBox checkbox_service) {
        super(a);
        this.context = a;
        this.tag = tag;
        this.checkbox_service = checkbox_service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_login);


        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_cancel);
        btn_reg = (Button) findViewById(R.id.btn_reg);

        et_login = (EditText) findViewById(R.id.et_login);
        et_pass = (EditText) findViewById(R.id.et_pass);

        TextView txt_server_token = (TextView) findViewById(R.id.txt_server_token);
        txt_server_token.setText("Введите логин и пароль для " + tag);



        String login = Setting.getString("login_" + tag);
        String pass = Setting.getString("pass_" + tag);

        if(login == null|| login.isEmpty()){
            String default_login = SettingServers.getDefaultLogin(tag);
            et_login.setText(default_login);
        }
        if(pass == null|| pass.isEmpty()){
            String default_pass = SettingServers.getDefaultPass(tag);
            et_pass.setText(default_pass);
        }

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        btn_reg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_yes:
                String login = et_login.getText().toString();
                String pass = et_pass.getText().toString();

                if(login == null|| login.isEmpty()){
                    Toast.makeText(context,"Вы должны ввести логин", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass == null|| pass.isEmpty()){
                    Toast.makeText(context,"Вы должны ввести  пароль ", Toast.LENGTH_LONG).show();
                    return;
                }
                Setting.setString("login_" + tag,login);
                Setting.setString("pass_" + tag,pass);
                checkbox_service.setChecked(true);
                Setting.setBool(tag, true);
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.btn_reg:
                String url = SettingServers.getUrlByTag(tag);


                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);




                break;
            default:
                break;
        }
        dismiss();
    }


}