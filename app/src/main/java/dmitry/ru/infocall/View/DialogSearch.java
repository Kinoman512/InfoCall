package dmitry.ru.infocall.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashMap;

import dmitry.ru.infocall.UserHandler;
import dmitry.ru.infocall.utils.ContactService;
import dmitry.ru.myapplication.R;

/**
 * Created by User on 21.02.2017.
 */

public class DialogSearch   extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    public Dialog d;
    public Button yes, no;
    private EditText et_number;

    public DialogSearch(Context a) {
        super(a);
        this.context = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_search);

        yes = (Button) findViewById(R.id.btn_find);
        no = (Button) findViewById(R.id.btn_cancel);

        et_number = (EditText) findViewById(R.id.et_number);


        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_find:
                String number = String.valueOf(et_number.getText());

                if(number != null && !number.isEmpty()){
                    Toast.makeText(context, number + "",Toast.LENGTH_LONG).show();

                    UserHandler uh = new UserHandler(number, context, false);
                    uh.setGetDatalistner(new UserHandler.OnGetDataListner() {
                        @Override
                        public void OnGetData(LinkedHashMap<String, String> map) {
                            dismiss();
                            DialogInfo di = new DialogInfo(context,map);
                            di.show();
                        }
                    });
                    ContactService.startServicesToGetInfo(uh, false, false);

                }else{
                    Toast.makeText(context,"Поле не должно быть пустым",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}