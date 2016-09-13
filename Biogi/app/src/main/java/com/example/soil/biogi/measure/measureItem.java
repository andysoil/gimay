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
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.soil.biogi.LoginActivity;
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

/**
 * Created by soil on 2016/4/30.
 */

public class measureItem extends Fragment {
    View view ;
    RequestQueue requestQueue;
    private List<measureItemModel>movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private measureItemAdapter mAdapter;
    private GridLayoutManager lLayout;
    String id,_id ;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        view = inflater.inflate(R.layout.fragment_measureitem, container, false); //find all view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new measureItemAdapter(movieList) ;

        Bundle bundle = getArguments();

        MainActivity.toolbar.setTitle(bundle.getString("toolbarname"));
        if( getArguments()!=null) {
            id = bundle.getString("id");
            _id = bundle.getString("_id");
            getAlldata(id, _id);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager) ;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        lLayout = new GridLayoutManager(getActivity(), 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);

        recyclerView.addItemDecoration(new DividerClassDecoration(getActivity(), LinearLayoutManager.VERTICAL));
// set the adapter
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {

            public void onClick(View view, int position) {
                MainActivity.midfragment = new measureItemDetail();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .hide(MainActivity.nextFragment)
                        .add(R.id.homepageLayout, MainActivity.midfragment)
                        .addToBackStack("measureItem")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                measureItemModel movie = movieList.get(position);

                MainActivity.toolbar.setTitle(movie.getName());
                Bundle bundle = new Bundle();
                bundle.putString("name", movie.getName()) ;
                bundle.putString("date", movie.getYear());
                bundle.putString("value", movie.getValue());
                bundle.putString("unit",movie.getUnit()) ;
                bundle.putString("inid",movie.getId()) ;
                MainActivity.midfragment .setArguments(bundle);

                MainActivity.toggleClass(false) ;


                Toast.makeText(getActivity(), movie.getName() + " is selected!", Toast.LENGTH_LONG).show();
                Log.d("mi", movie.getName()+movie.getYear()+ movie.getValue());
            }


            public void onLongClick(View view, int position) {

            }
        }));

        return view ;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.changepasmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public interface ClickListener{
        void onClick(View view, int position) ;
        void onLongClick(View view,int position) ;

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector ;
        private measureItem.ClickListener clickListener ;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final measureItem.ClickListener clickListener) {
            this.clickListener =  clickListener;
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

        @Override
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
    private void getAlldata(final String getId, final String get_id) {


        StringRequest measuredate = new StringRequest(Request.Method.POST, AllUrl.inspect,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jobj = new JSONObject(response) ;
                            JSONArray date= jobj.getJSONArray("indate");
                            JSONArray name = jobj.getJSONArray("inname");
                            JSONArray value = jobj.getJSONArray("hv");
                            JSONArray inspectId = jobj.getJSONArray("inid");
                            JSONArray unit = jobj.getJSONArray("unit");
                            for(int i =0;i<date.length();i++) {
                                measureItemModel movie = new measureItemModel( name.getString(i),value.getString(i)
                                        ,  date.getString(i),inspectId.getString(i),unit.getString(i));
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

                parmater.put("category_id",getId );
                parmater.put("_id",get_id );
                Log.d("n1", " 1023601");
                return parmater;
            }
        };
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(measuredate) ;
    }

}
