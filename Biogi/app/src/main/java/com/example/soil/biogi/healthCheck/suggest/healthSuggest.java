package com.example.soil.biogi.healthCheck.suggest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.soil.biogi.AllUrl;
import com.example.soil.biogi.ApplicationController;
import com.example.soil.biogi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soil on 2016/6/8.
 */
public class healthSuggest extends Fragment{
    View view ;
    ImageButton imageRow,imageSmall ;
    private RecyclerView recyclerView;
    private rowAdapter rowAdapter;
    private smallAdapter smallAdapter;
    private GridLayoutManager gLayout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_suggest, container, false);

        initSuggest() ;

            imageRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageRow.setImageResource(R.drawable.ic_product_row_click);
                    imageSmall.setImageResource(R.drawable.ic_product_small);
                    recyclerViewRow() ;
                }


            });

            imageSmall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageSmall.setImageResource(R.drawable.ic_product_small_ck);
                    imageRow.setImageResource(R.drawable.ic_product_row);
                    recyclerSmall();

                }


            });

        return view ;
    }
    public void initSuggest(){
        imageRow = (ImageButton)view.findViewById(R.id.row) ;
        imageSmall= (ImageButton)view.findViewById(R.id.small) ;
        imageRow.setImageResource(R.drawable.ic_product_row_click);
        recyclerView = (RecyclerView) view.findViewById(R.id.produce_suggest);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager) ;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerViewRow() ;
    }
    public void recyclerViewRow(){
        suggestProduct(true) ;

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    public void recyclerSmall(){
        suggestProduct(false) ;

        recyclerView.setAdapter(smallAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    public void suggestProduct(final Boolean chance){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AllUrl.product, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("產品", response.toString());

                try {
                    JSONArray name = response.getJSONArray("product_name") ;
                    JSONArray description = response.getJSONArray("product_description") ;
                    JSONArray price = response.getJSONArray("product_price") ;
                    JSONArray image = response.getJSONArray("product_image") ;
                    List<suggestModel> movieList = new ArrayList<>();

                    for (int i = 0; i < name.length(); i++) {
                        suggestModel model = new suggestModel(image.getString(i),name.getString(i),"NT$"+price.getString(i),description.getString(i)) ;
                        movieList.add(model) ;
                    }

                    if(chance){
                        gLayout = new GridLayoutManager(getActivity(), 1);

                        rowAdapter = new rowAdapter(movieList) ;
                        rowAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(rowAdapter);
                    }else{

                        gLayout = new GridLayoutManager(getActivity(), 2);
                        smallAdapter = new smallAdapter(movieList) ;
                        smallAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(smallAdapter);
                    }

                    recyclerView.setLayoutManager(gLayout);


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("產品", "Error: " + error.getMessage());

            }
        });

        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public interface ClickListener{
        void onClick(View view, int position) ;
        void onLongClick(View view, int position) ;

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector ;
        private healthSuggest.ClickListener clickListener ;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final healthSuggest.ClickListener clickListener){
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
