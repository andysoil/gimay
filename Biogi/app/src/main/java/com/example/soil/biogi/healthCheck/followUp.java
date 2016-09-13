package com.example.soil.biogi.healthCheck;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soil.biogi.AllUrl;
import com.example.soil.biogi.ApplicationController;
import com.example.soil.biogi.MainActivity;
import com.example.soil.biogi.R;
import com.example.soil.biogi.SaveText;
import com.example.soil.biogi.healthCheck.classItem.itemAdapter;
import com.example.soil.biogi.healthCheck.classItem.itemModel;
import com.example.soil.biogi.measure.measureClassModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class followUp extends Fragment {
    View view ;
    TextView  tvclass1,tvtime1,tvreason1 ;
    TableLayout fu ;
    TableRow lastExpensesTableRow ;
    private SaveText db;
    healthDateModel hm ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_follow_up, container, false);

        db = new SaveText(getActivity());
        followData() ;
        fu = (TableLayout)view.findViewById(R.id.fuTable) ;
        fu.setColumnStretchable(0,true);
        fu.setColumnStretchable(1,true);
        fu.setColumnStretchable(2, true);
        followList() ;
        return view ;
    }

    public void followData(){
        StringRequest follow = new StringRequest(Request.Method.POST, AllUrl.follow, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject joct = new JSONObject(response);
                            JSONArray num = joct.getJSONArray("num") ;
                            JSONArray classFollow = joct.getJSONArray("name") ;
                            JSONArray timeFollow = joct.getJSONArray("often");
                            JSONArray reasonFollow = joct.getJSONArray("reason") ;

                            for(int i =0;i<classFollow.length();i++) {
                                db.addHealthFollow(num.getString(i),healthCheck.hm.getmodelDate(),
                                        classFollow.getString(i),timeFollow.getString(i),reasonFollow.getString(i));
                                db.updateFollowUp(num.getString(i),healthCheck.hm.getmodelDate(),
                                        classFollow.getString(i),timeFollow.getString(i),reasonFollow.getString(i)) ;
                                Log.d("follow", num.getString(i)+healthCheck.hm.getmodelDate()+
                                        classFollow.getString(i)+timeFollow.getString(i)+reasonFollow.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> parmater = new HashMap<>();
                parmater.put("_id",  healthCheck.hm.getmodelId());
                parmater.put("date", healthCheck.hm.getmodelDate());
                Log.d("follow",  healthCheck.hm.getmodelId());
                return parmater;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(follow);

    }
    public void followList(){
        List<String> healthDateLabel = db.getHealthFollow(healthCheck.hm.getmodelDate());


            for(int k=0;k<healthDateLabel.size();k++) {

                lastExpensesTableRow = new TableRow(getActivity());
                View v = new View(getActivity());
                v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
                v.setBackgroundColor(Color.rgb(51, 51, 51));
                tvclass1 = new TextView(getActivity());
                tvtime1 = new TextView(getActivity());
                tvreason1 = new TextView(getActivity());
                if (!"".equals(healthDateLabel.get(0))) {
                    tvclass1.setText(healthDateLabel.get(k++));
                    tvtime1.setText(healthDateLabel.get(k++));
                    tvreason1.setText(healthDateLabel.get(k));
                    Log.d("k2", String.valueOf(k));
                }
                tvtime1.setTextSize(15);
                tvclass1.setTextSize(15);
                tvreason1.setTextSize(15);

                tvtime1.setGravity(Gravity.CENTER);
                tvclass1.setGravity(Gravity.CENTER);
                tvreason1.setGravity(Gravity.CENTER);

                lastExpensesTableRow.addView(tvclass1);
                lastExpensesTableRow.addView(tvtime1);
                lastExpensesTableRow.addView(tvreason1);
                fu.addView(v);
                fu.addView(lastExpensesTableRow);
            }

    }
}
