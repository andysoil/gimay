package com.example.soil.biogi.healthCheck.suggest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.soil.biogi.ApplicationController;
import com.example.soil.biogi.R;

import java.util.List;

/**
 * Created by soil on 2016/6/19.
 */
public class rowAdapter extends  RecyclerView.Adapter<rowAdapter.MyviewHolder>{
    private List<suggestModel> followList;
    ImageLoader imageLoader = ApplicationController.getInstance().getImageLoader();

    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggest_list_row, parent, false);

        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        suggestModel movie = followList.get(position) ;
            holder.image.setImageUrl(movie.getImage(),imageLoader);
            holder.name.setText(movie.getName());
            holder.price.setText(movie.getPrice());
            holder.decri.setText(movie.getDecri());
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView name,price, decri;
        NetworkImageView  image;
        public MyviewHolder(View view){
            super(view);
            if (imageLoader == null)
                imageLoader = ApplicationController.getInstance().getImageLoader();
            image = (NetworkImageView)view.findViewById(R.id.productImage);
            name = (TextView)view.findViewById(R.id.name) ;
            price = (TextView)view.findViewById(R.id.price) ;
            decri = (TextView)view.findViewById(R.id.decri) ;
        }

    }
    public rowAdapter(List<suggestModel> followList){
        this.followList = followList;
    }
}
