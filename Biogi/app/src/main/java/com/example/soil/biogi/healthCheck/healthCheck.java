package com.example.soil.biogi.healthCheck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.soil.biogi.AllUrl;
import com.example.soil.biogi.ApplicationController;
import com.example.soil.biogi.Dialog;
import com.example.soil.biogi.MainActivity;
import com.example.soil.biogi.R;
import com.example.soil.biogi.SaveText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class healthCheck extends Fragment {

    View view ;
    Spinner spinner ;
    TextView comment,healthScore ;
    private SaveText db;
    String id,sex ;
    public static healthDateModel hm=new healthDateModel() ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SaveText(getActivity());
        final HashMap<String, String> user = db.getUserDetails();
        id = user.get("_id") ;
        sex = user.get("sex");
        Log.d("sex", user.get("sex") + id);
        gethealthdata(id, sex) ;

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        view = inflater.inflate(R.layout.fragment_health_check, container, false); //find all view

        comment = (TextView)view.findViewById(R.id.doctorComment) ;
        healthScore = (TextView)view.findViewById(R.id.healthScore) ;
        spinner = (Spinner)view.findViewById(R.id.spinner) ;
        hm.setmodelId(id);
        hm.setmodelSex(sex);

        MainActivity.toolbar.setTitle("健檢報告");
        loadSpinnerData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                 List<String> healthDateLabel = db.getHealthDatesDetial(spinner.getSelectedItem().toString());
                    hm.setmodelDate(spinner.getSelectedItem().toString()) ;
                    comment.setText(healthDateLabel.get(0));
                    healthScore.setText(healthDateLabel.get(1));
                 Log.d("spinner_date",healthDateLabel.get(1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        return view ;
    }

    private void gethealthdata(final String id, final String sex ){

        StringRequest measuredate = new StringRequest(Request.Method.POST, AllUrl.healthcheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jobj = new JSONObject(response) ;
                            JSONArray reportNum =  jobj.getJSONArray("reportNum") ;
                            JSONArray reportDate =  jobj.getJSONArray("reportDate") ;
                            JSONArray comment =  jobj.getJSONArray("personalComment") ;
                            JSONArray Score =  jobj.getJSONArray("personalScore") ;

                            //Save date and other data
                            for(int i=0; i<jobj.getJSONArray("reportDate").length();i++){

                                db.addHealthCheck(reportNum.getString(i),id, reportDate.getString(i), sex, comment.getString(i), Score.getString(i));
                                db.updateHealthDate(reportNum.getString(i),id, reportDate.getString(i), sex, comment.getString(i), Score.getString(i));
                                Log.d("healthReportData", reportNum.getString(i)+ id+ reportDate.getString(i)+ sex+ comment.getString(i)+ Score.getString(i));
                            }

                        }catch(JSONException e){

                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("not network", "Volley error: " + error.getMessage() + ", code: " + networkResponse);

            }
        }) {
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String, String> parmater = new HashMap<>();
                parmater.put("_id",id );
                Log.d("gethealthdata", id);
                return parmater;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(measuredate);

    }
    private void loadSpinnerData() {
        // database handler
        SaveText db = new SaveText(getActivity().getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getHealthDates();
        Log.d("lables", String.valueOf(lables));
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(spinnerAdapter);

    }

}
