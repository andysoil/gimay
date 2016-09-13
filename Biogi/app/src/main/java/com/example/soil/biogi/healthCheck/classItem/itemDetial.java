package com.example.soil.biogi.healthCheck.classItem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.soil.biogi.AllUrl;
import com.example.soil.biogi.ApplicationController;
import com.example.soil.biogi.R;
import com.example.soil.biogi.SaveText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class itemDetial extends Fragment {

    private SaveText db;
    String inspect_id ;
    TextView texthistory,textreason;
    View view ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SaveText(getActivity());
        Bundle bundle = getArguments();
        inspect_id = bundle.getString("inspect_id");

        getInspectDetial();

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_item_detial, container, false);
        texthistory = (TextView)view.findViewById(R.id.history_value) ;
        textreason = (TextView)view.findViewById(R.id.reason) ;
        getDetial() ;

        return view;
    }
    private void getInspectDetial(){

        StringRequest inspectDeital = new StringRequest(Request.Method.POST, AllUrl.itemDetial,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("asdasd in", "asdasdasdasd");
                            JSONObject jobj = new JSONObject(response) ;
                            JSONArray num= jobj.getJSONArray("num");
                            JSONArray date= jobj.getJSONArray("indate");
                            JSONArray inid= jobj.getJSONArray("inid");
                            JSONArray value= jobj.getJSONArray("result");
                            JSONArray reason= jobj.getJSONArray("reason");
                            Log.d("getInspectDetial in", reason.getString(0));
                            for(int i =0;i<num.length();i++) {
                                db.addHealthItemDetial(num.getString(i),date.getString(i),
                                        value.getString(i),reason.getString(0),inid.getString(i));
                                db.updateHealthItemDetial(num.getString(i),date.getString(i),
                                        value.getString(i),reason.getString(0),inid.getString(i));
                                Log.d("getInspectDetial in",  value.getString(i)+date.getString(i));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String, String> parmater = new HashMap<String, String>();
                SaveText db = new SaveText(getActivity().getApplicationContext());
                HashMap<String, String> user = db.getUserDetails();

                parmater.put("_id", user.get("_id"));
                parmater.put("inspect_id", inspect_id);
                parmater.put("sex",  user.get("sex"));
                Log.d("inid",inspect_id+ user.get("_id")+ user.get("sex") );

                return parmater;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(inspectDeital);

    }
    public void getDetial(){
        SaveText db = new SaveText(getActivity().getApplicationContext());
        List<String> lables = db.getHealthItemDetial(inspect_id);

        String detial = "";
        for(int i =0;i<lables.size();i++) {
            detial =detial+ lables.get(i++)+"        " + lables.get(i++) +"\n" ;
        }
        texthistory.setText(detial);
        textreason.setText(lables.get(2)) ;

    }
}
