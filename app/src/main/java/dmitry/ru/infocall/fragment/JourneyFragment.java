package dmitry.ru.infocall.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import dmitry.ru.infocall.FileSave;
import dmitry.ru.infocall.adapters.ListJournyAdapter;
import dmitry.ru.infocall.utils.Setting;
import dmitry.ru.myapplication.R;

import static dmitry.ru.myapplication.R.id.callList;

/**
 * Created by Dmitry on 12.06.2016.
 */
public class JourneyFragment extends Fragment {


    LayoutInflater inflater;
    Context context;
    List<LinkedHashMap<String, String>> list;
    private TextView txt_jorny;
    private TextView txt_no_records;
    private ListView lw;

//    public JourneyFragment(List<LinkedHashMap<String, String>> l) {
//        list = l;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journey, container, false);
        this.inflater = inflater;
        this.context = JourneyFragment.this.inflater.getContext();
        Setting.init(inflater.getContext());


        //************************************* Переделать
        lw = (ListView) rootView.findViewById(callList);
        txt_jorny = (TextView) rootView.findViewById(R.id.txt_jorny);
        txt_no_records = (TextView) rootView.findViewById(R.id.txt_no_records);



        return rootView;
    }

    @Override
    public void onResume() {
        List<LinkedHashMap<String, String>> list = FileSave.read(context);
        if(list.size() != 0){
            txt_no_records.setVisibility(View.GONE);
        }

        Collections.reverse(list);
        final ListJournyAdapter adapter;
        adapter = new ListJournyAdapter(context, list);
        lw.setAdapter(adapter);

        super.onResume();
    }
}
