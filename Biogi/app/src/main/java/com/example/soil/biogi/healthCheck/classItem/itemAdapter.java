package com.example.soil.biogi.healthCheck.classItem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.soil.biogi.R;

import java.util.List;

/**
 * Created by soil on 2016/6/19.
 */
public class itemAdapter extends  RecyclerView.Adapter<itemAdapter.MyviewHolder>{
    private List<itemModel> followList;


    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemreport_list_row, parent, false);

        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        itemModel movie = followList.get(position) ;
            holder.name.setText(movie.getName());
            holder.range.setText(movie.getRange());
            holder.value.setText(movie.getValue());
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView name,range, value;

        public MyviewHolder(View view){
            super(view);

            name = (TextView)view.findViewById(R.id.price) ;
            range = (TextView)view.findViewById(R.id.name) ;
            value = (TextView)view.findViewById(R.id.decri) ;
        }

    }
    public itemAdapter(List<itemModel> followList){
        this.followList = followList;
    }
}
