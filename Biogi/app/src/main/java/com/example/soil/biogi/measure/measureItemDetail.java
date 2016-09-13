package com.example.soil.biogi.measure;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.soil.biogi.MainActivity;
import com.example.soil.biogi.R;


public class measureItemDetail extends Fragment {
    TextView tvName,tvValue,tvDate,tvUnit ;
    Button btnChart ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        View view = inflater.inflate(R.layout.fragment_measure_item_detail, container, false); //find all view

        btnChart = (Button)view.findViewById(R.id.chart) ;
        tvName = (TextView) view.findViewById(R.id.itemname);
        tvValue = (TextView) view.findViewById(R.id.itemvalue);
        tvDate = (TextView) view.findViewById(R.id.itemdate);
        tvUnit = (TextView)view.findViewById(R.id.itemunit) ;

        final Bundle bundle = getArguments();

        tvName.setText(bundle.getString("name"));
        tvValue.setText(bundle.getString("value"));
        tvDate.setText(bundle.getString("date"));
        tvUnit.setText(bundle.getString("unit"));

        btnChart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), measureChart.class);
                intent.putExtra("name",bundle.getString("name"));
                intent.putExtra("inid",bundle.getString("inid"));
                startActivity(intent);
            }
        });

        return view ;
    }


}
