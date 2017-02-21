package dmitry.ru.infocall.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import dmitry.ru.infocall.adapters.ListServicesAdapter;
import dmitry.ru.myapplication.R;

/**
 * Created by User on 21.02.2017.
 */

public class ServicesFragment  extends Fragment {


    LayoutInflater inflater;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_services, container, false);
        this.inflater = inflater;
        this.context = inflater.getContext();

        ListView lv_services = (ListView) rootView.findViewById(R.id.lv_services);

        lv_services.setAdapter(new ListServicesAdapter(context));



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}