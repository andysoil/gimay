package com.example.soil.biogi.measure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soil.biogi.AllUrl;
import com.example.soil.biogi.MainActivity;
import com.example.soil.biogi.R;
import com.example.soil.biogi.SaveText;
import com.example.soil.biogi.healthCheck.healthCheckMain;
import com.example.soil.biogi.memberSet.editData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soil on 2016/4/8.
 */
public class measureClass extends Fragment {
    private View view ;
    private List<measureClassModel> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private measureClassAdapter mAdapter;
    private SaveText db;
    private GridLayoutManager lLayout;

    RequestQueue requestQueue;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        view = inflater.inflate(R.layout.fragment_measure_show, container, false); //find all view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new measureClassAdapter(movieList) ;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager) ;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        db = new SaveText(getActivity());


        lLayout = new GridLayoutManager(getActivity(), 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);


        final HashMap<String, String> user = db.getUserDetails();
        getAlldata(user.get("_id")) ;


// set the adapter
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MainActivity.nextFragment = new measureItem();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .hide(MainActivity.fragment)
                        .add(R.id.homepageLayout, MainActivity.nextFragment)
                        .addToBackStack("measureItem")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                MainActivity.toggleClass(false);

                measureClassModel movie = movieList.get(position);
                MainActivity.toolbarname = movie.getName() ;
                Log.d("n1", movie.get_id());
                Bundle bundle = new Bundle();
                bundle.putString("toolbarname",movie.getName()) ;
                bundle.putString("_id", user.get("_id"));
                bundle.putString("id", movie.get_id());
                MainActivity.nextFragment.setArguments(bundle);
                Toast.makeText(getActivity(), movie.getName() + " is selected!", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        MainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToggle();
            }
        });
        return view ;
    }

    private void getAlldata(final String id) {

        StringRequest measuredate = new StringRequest(Request.Method.POST, AllUrl.category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jobj = new JSONObject(response) ;
                            JSONArray id= jobj.getJSONArray("id");
                            JSONArray name = jobj.getJSONArray("name");
                            for(int i =0;i<id.length();i++) {

                                measureClassModel movie = new measureClassModel( name.getString(i),id.getString(i));
                                movieList.add(movie);
                            }
                            mAdapter.notifyDataSetChanged();
                        }catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String, String> parmater = new HashMap<>();
                parmater.put("_id",id );
                Log.d("n1", id);
                return parmater;
            }
        };
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(measuredate) ;
    }

    public interface ClickListener{
        void onClick(View view, int position) ;
        void onLongClick(View view, int position) ;

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector ;
        private measureClass.ClickListener clickListener ;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final measureClass.ClickListener clickListener){
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
    public void backToggle(){

                if(MainActivity.midfragment!=null){
                    MainActivity.toolbar.setTitle(MainActivity.toolbarname);
                    Log.d("mid", "mid");
                    getActivity().getSupportFragmentManager().beginTransaction().hide(MainActivity.midfragment)
                            .show(MainActivity.nextFragment).commit();
                    MainActivity.midfragment=null ;
                }else if(MainActivity.nextFragment!=null){
                    MainActivity.toolbar.setTitle("量測紀錄");
                    Log.d("next", "next");
                    MainActivity.toggleClass(true) ;
                    getActivity().getSupportFragmentManager().beginTransaction().hide(MainActivity.nextFragment)
                            .show(MainActivity.fragment).commit();
                }


}
}
