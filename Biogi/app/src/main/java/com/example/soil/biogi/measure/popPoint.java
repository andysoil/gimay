package com.example.soil.biogi.measure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.soil.biogi.R;

public class popPoint extends AppCompatActivity {
    TextView date,value ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_pop_point);

        date =(TextView)findViewById(R.id.datepop) ;
        value = (TextView)findViewById(R.id.valuepop) ;


        Intent intent =getIntent();
                /*取出Intent中附加的数据*/
        date.setText(intent.getStringExtra("x"));
        value.setText(intent.getStringExtra("y"));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int height = dm.heightPixels ;

        getWindow().setLayout((int) (width * .8), (int) (height * .6)) ;



    }
}
