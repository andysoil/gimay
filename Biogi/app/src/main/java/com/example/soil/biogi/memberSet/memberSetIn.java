package com.example.soil.biogi.memberSet;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soil.biogi.AllUrl;
import com.example.soil.biogi.LoginActivity;
import com.example.soil.biogi.MainActivity;
import com.example.soil.biogi.R;
import com.example.soil.biogi.SaveText;
import com.example.soil.biogi.SessionManger;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class memberSetIn extends Fragment {

    private SaveText db;
    private SessionManger session;
    private View view;
    Button  changeData,changePas ,logout ,logining ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        view = inflater.inflate(R.layout.fragment_member_set, container, false); //find all view
        changeData = (Button) view.findViewById(R.id.changedata);
        changePas = (Button) view.findViewById(R.id.changebtn);
        logout = (Button)view.findViewById(R.id.logout) ;
        logining = (Button)view.findViewById(R.id.btnLogining) ;

        session = new SessionManger(getActivity());
        db = new SaveText(getActivity()) ;
        HashMap<String, String> checkDate = db.getHealthDate();
        logining.setText("登入中                   " + checkDate.get("id"));

        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),
                        editData.class));
            }
        });
        changePas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity( new Intent(getActivity(),
                        change_psw.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                session.setLogin(false);
                db.deleteUsers();
                startActivity( new Intent(getActivity(),
                        LoginActivity.class));
            }
        });
        return view ;
    }


}