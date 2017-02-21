package dmitry.ru.infocall;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import dmitry.ru.infocall.service.CallReceiver;
import dmitry.ru.infocall.utils.ContactService;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;

/**
 * Created by Dmitry on 12.06.2016.
 */
public class MenuFragment extends Fragment {


    LayoutInflater inflater;
    Context context;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        this.inflater = inflater;
        this.context = MenuFragment.this.inflater.getContext();
        Setting.init(inflater.getContext());


        final Button btnStart = (Button)rootView.findViewById(R.id.btn_start);
        final Button btnStop  = (Button)rootView.findViewById(R.id.btn_stop);
        final Button btnTest  = (Button)rootView.findViewById(R.id.btn_test);
        final Button btnJ  = (Button)rootView.findViewById(R.id.btn_j);
        final Button btnCont  = (Button)rootView.findViewById(R.id.btnCont);


        final TextView txtView = (TextView) rootView.findViewById(R.id.textView2);
        final TextView txtView2 = (TextView) rootView.findViewById(R.id.textView3);
        final TextView txtView3 = (TextView) rootView.findViewById(R.id.textView4);







        //create the drawer and remember the `Drawer` result object
        // запуск службы
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // используем явный вызов службы

                Log.d("CallService", "start call service");
                MenuFragment.this.inflater.getContext().startService(
                        new Intent(MenuFragment.this.inflater.getContext(), CallReceiver.class));
                Toast.makeText(context, "Служба запущена",
                                Toast.LENGTH_SHORT).show();
            }
        });

        //контаты
        btnCont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // используем явный вызов службы
                Log.d("CallService", "start contact service");
                //MyDrawer.closeWindow();
                MyAlert.show();
                //ContactUtil.getContactCursor(MainActivity.this,"MedleyMedley");
            }
        });

        //test
        btnTest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("CallService", "test call service");
//                UserHandler uh = new UserHandler("Kinoman512", "074d0c3" ); 79044404193


//                Sp2AllTask task = new Sp2AllTask();
                // HtmlwebTask task = new HtmlwebTask();
//                UserHandler uh = new UserHandler("79044404193", MenuFragment.this.inflater.getContext() );
//                UserHandler uh = new UserHandler("79081906207", MenuFragment.this.inflater.getContext(),false );

//                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//                map.put("number", "123asd");
//                MyDrawer.showWindow(context, "79044404193", map ,true);


                UserHandler uh = new UserHandler("79081906207", MenuFragment.this.inflater.getContext(),false );
                ContactService.startServicesToGetInfo(uh, false);
//                List<LinkedHashMap<String, String>> list = ContactUtil.getCallLog( MenuFragment.this.inflater.getContext());


//                List<LinkedHashMap<String, String>> list =  new ArrayList<LinkedHashMap<String, String>>();
//
//                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//                map.put("city", "Rostov");
//                map.put("phone", "123");
//                map.put("name", "Dima");
//
//
//                LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
//                map2.put("phone", "123222");
//                map2.put("city", "Rostov2");
//                map2.put("name", "Dima2");
//
//                list.add(map);
//                list.add(map2);




                //FileSave.save(context, list);

//                List<LinkedHashMap<String, String>> list2 = FileSave.read(context);
//
//                Log.d("CallService2", "tt" + list2.toString());
//
//                MainActivity.setFragment(new CallLogFragment(list2));
                MainActivity.isShown  = true;




            }});

        // остановка
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
//                Log.d("CallService", "stop call service");
//                context.stopService(
//                        new Intent(context, CallReceiver.class));
//                MyDrawer.closeWindow();


            }
        });

        btnJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<LinkedHashMap<String, String>> list2 = FileSave.read(context);
                List<LinkedHashMap<String, String>> list2  = new ArrayList<LinkedHashMap<String, String>>();//FileSave.read(context);
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("number", "79081906207");
                map.put("name", "Dima Gribkov");
                map.put("city:SP", "Город : Москва");
                map.put("city:HTMLWEB", "Город : Ростов-на-Дону");
                map.put("organization", "Руководитель");

                map.put("avatar", "http://api.sp2all.ru//images/avatar/a100_115219.jpg?v=1479989942");

                list2.add(map);

                MainActivity.setFragment(new CallLogFragment(list2), true);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
