package dmitry.ru.infocall;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dmitry.ru.infocall.service.CallReceiver;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.infocall.utils.contact.ContactUtil;
import dmitry.ru.myapplication.R;

import static dmitry.ru.myapplication.R.id.callList;

/**
 * Created by Dmitry on 12.06.2016.
 */
public class CallLogFragment extends Fragment {


    LayoutInflater inflater;
    Context context;
    List<LinkedHashMap<String, String>> list;
    private TextView txt_jorny;
    private TextView txt_no_records;

    public CallLogFragment(List<LinkedHashMap<String, String>> l) {
        list = l;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calllog, container, false);
        this.inflater = inflater;
        this.context = CallLogFragment.this.inflater.getContext();
        Setting.init(inflater.getContext());


        //************************************* Переделать
        final ListView lw = (ListView) rootView.findViewById(callList);
        txt_jorny = (TextView) rootView.findViewById(R.id.txt_jorny);
        txt_no_records = (TextView) rootView.findViewById(R.id.txt_no_records);

        if(list.size() != 0){
            txt_no_records.setVisibility(View.GONE);
        }


        final  String[] strs = new String[list.size()];
        int i = 0;
        for (Map<String, String> e : list) {
            for (String e2 : e.keySet()) {
                strs[list.size() - 1 - i] = e.get(e2);
            }

            i++;
        }

//        list.get(0);


        final CallLogAdapter adapter;


        adapter = new CallLogAdapter(context, list);

        lw.setAdapter(adapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {


//
                String phone = list.get(position).get("number");
//                String phone = ((TextView) itemClicked).getText().toString();
                phone = phone.replaceAll("\\+|\\-", "");
//                Toast.makeText(context, "123467 " + phone + "  23",
//                        Toast.LENGTH_SHORT).show();
                MyAlert.show2(phone);
//                Log.d("CallLogAdapter"," 1234 ");
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
