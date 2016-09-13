package com.example.soil.biogi.healthCheck.classItem;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soil.biogi.AllUrl;
import com.example.soil.biogi.ApplicationController;
import com.example.soil.biogi.MainActivity;
import com.example.soil.biogi.R;
import com.example.soil.biogi.SaveText;
import com.example.soil.biogi.healthCheck.healthCheck;
import com.example.soil.biogi.healthCheck.healthCheckMain;
import com.example.soil.biogi.healthCheck.healthModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soil on 2016/6/8.
 */
public class itemReport extends Fragment {

    View view ;
    static Spinner spinnerClass ;
    private SaveText db;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private RecyclerView recyclerView;
    private itemAdapter fAdapter ;
    private GridLayoutManager gLayout;
    List<itemModel> itemList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SaveText(getActivity());


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        view = inflater.inflate(R.layout.fragment_item_report, container, false); //find all view
        spinnerClass =(Spinner)view.findViewById(R.id.itemClass) ;
        recyclerView = (RecyclerView) view.findViewById(R.id.helath_recycler_view);

        getCategory() ;
        loadSpinnerData() ;
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager) ;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        gLayout = new GridLayoutManager(getActivity(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gLayout);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String stringSpinner = spinnerClass.getSelectedItem().toString();
                postInspect(stringSpinner.substring(0, stringSpinner.indexOf("：")));
                recycleCategory(stringSpinner.substring(0, stringSpinner.indexOf("："))) ;
                Log.d("spinner", stringSpinner.substring(0, stringSpinner.indexOf("：")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                itemModel itemMovie = itemList.get(position);

                MainActivity.toolbar.setTitle(itemMovie.getName());
                MainActivity.toggleClass(false);

                healthCheckMain.nextfragment = new itemDetial();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .hide(healthCheckMain.fragment)
                        .add(R.id.mainFragment, healthCheckMain.nextfragment)
                        .addToBackStack("tradeCustomer")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                Bundle itemBundle = new Bundle();
                itemBundle.putString("inspect_id", itemMovie.getId());
                healthCheckMain.nextfragment.setArguments(itemBundle);

            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        return view ;
    }

    private void getCategory(){
        StringRequest requestCategory = new StringRequest(Request.Method.POST, AllUrl.category, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response) ;
                            JSONArray num = jobj.getJSONArray("num") ;
                            JSONArray id= jobj.getJSONArray("id");
                            JSONArray name= jobj.getJSONArray("name");
                            for(int i =0;i<id.length();i++) {
                                db.addHealthItem(num.getString(i),id.getString(i) + "：" + name.getString(i),healthCheck.hm.getmodelDate());
                                db.updateHealthItemReport(num.getString(i),id.getString(i) + "：" + name.getString(i),healthCheck.hm.getmodelDate());
                                Log.d("getCategory", num.getString(i)+id.getString(i) + "：" +name.getString(i)+healthCheck.hm.getmodelDate());
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
                parmater.put("_id",  healthCheck.hm.getmodelId());
                parmater.put("date",  healthCheck.hm.getmodelDate());
                Log.d("itemRe", healthCheck.hm.getmodelId() + healthCheck.hm.getmodelDate());

                return parmater;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(requestCategory);

      }
    private void loadSpinnerData() {
        // database handler
        SaveText db = new SaveText(getActivity().getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getHealthItemReport( healthCheck.hm.getmodelDate());
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerClass.setAdapter(spinnerAdapter);
    }

    private void postInspect(final String categoryChoose){

            StringRequest inspect = new StringRequest(Request.Method.POST, AllUrl.itemReport,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jobj = new JSONObject(response) ;
                                JSONArray num= jobj.getJSONArray("num");
                                JSONArray inspect_name= jobj.getJSONArray("inspect_name");
                                JSONArray inspect_id= jobj.getJSONArray("inspect_id");
                                JSONArray category_id= jobj.getJSONArray("category_id");
                                JSONArray inspect_range= jobj.getJSONArray("inspect_range") ;
                                JSONArray member_value= jobj.getJSONArray("member_value") ;

                                for(int k=0;k<category_id.length();k++) {
                                    db.addHealthInspect(num.getString(k),inspect_name.getString(k),inspect_id.getString(k),
                                            inspect_name.getString(k),inspect_range.getString(k),
                                            member_value.getString(k),categoryChoose);
                                    db.updateHealthItemReportInspect(num.getString(k),inspect_name.getString(k),inspect_id.getString(k),
                                            inspect_name.getString(k),inspect_range.getString(k),
                                            member_value.getString(k),categoryChoose);
                                    Log.d("postInspect", String.valueOf( inspect_id.getString(k)));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                protected Map<String,String> getParams()throws AuthFailureError {
                    Map<String, String> parmater = new HashMap<String, String>();
                    parmater.put("_id", healthCheck.hm.getmodelId());
                    parmater.put("sex", healthCheck.hm.getmodelSex());
                    parmater.put("category_id", categoryChoose);
                    parmater.put("date", healthCheck.hm.getmodelDate());
                    Log.d("follow2", healthCheck.hm.getmodelId()+healthCheck.hm.getmodelDate());

                    return parmater;
                }
            };
            ApplicationController.getInstance().addToRequestQueue(inspect);

    }
    public void recycleCategory(String category){
        SaveText db = new SaveText(getActivity().getApplicationContext());
        List<String> lables = db.getHealthItemInspect(category);
        List<itemModel> movieList = new ArrayList<>();
        fAdapter = new itemAdapter(movieList) ;
        for(int i=0;i<lables.size();i++){
        itemModel movie = new itemModel(lables.get(i++),
               lables.get(i++),lables.get(i++),lables.get(i));
        movieList.add(movie);
        Log.d("recycleCategory", lables.get(i)) ;
        }
        itemList = movieList ;
        fAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(fAdapter);
    }
    public interface ClickListener{
        void onClick(View view, int position) ;
        void onLongClick(View view, int position) ;
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector ;
        private itemReport.ClickListener clickListener ;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final itemReport.ClickListener clickListener){
            this.clickListener = clickListener ;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                public boolean onSingleTapUp(MotionEvent e){
                    return true ;
                }
                public void onLongPress(MotionEvent e){
                    View show = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(show !=null&&clickListener !=null){
                        clickListener.onLongClick(show, recyclerView.getChildPosition(show));
                    }
                }

            });
        }
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }

    }

