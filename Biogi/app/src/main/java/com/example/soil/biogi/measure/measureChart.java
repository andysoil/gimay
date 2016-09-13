package com.example.soil.biogi.measure;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class measureChart extends AppCompatActivity {
    EditText in_first,in_second ;
    RequestQueue requestQueue ;
    Button input ;
    private Calendar myCalendar,myCalendar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_chart);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.chart_toolbar);
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        final String inid = intent.getStringExtra("inid");
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        in_first = (EditText)findViewById(R.id.firstDate);
        in_second = (EditText)findViewById(R.id.secondDate) ;
        input = (Button)findViewById(R.id.btnCreate) ;
        myCalendar = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();

        in_first.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(measureChart.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        in_second.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(measureChart.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        input.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String first = in_first.getText().toString().trim();
                String second = in_second.getText().toString().trim();
                chart(first,second,inid);
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.changepasmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();

        }

    };
    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            // TODO Auto-generated method stub
            myCalendar2.set(Calendar.YEAR, year);
            myCalendar2.set(Calendar.MONTH, monthOfYear);
            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();

        }

    };
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        in_first.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel2() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        in_second.setText(sdf.format(myCalendar2.getTime()));
    }
    public void chart(final String first, final String second, final String inid){
        SaveText db = new SaveText(this);
        final HashMap<String, String> user = db.getUserDetails();

        StringRequest chart = new StringRequest(Request.Method.POST, AllUrl.measureChart , new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (!jObj.getBoolean("error")) {

                                JSONArray name = jObj.getJSONArray("inName");
                                JSONArray dates = jObj.getJSONArray("lisDate");
                                JSONArray value = jObj.getJSONArray("value");

                                final String mename = name.getString(0) ;
                                LineChart chart = (LineChart) findViewById(R.id.chart_one);

                                ArrayList<Entry> yVals2 = new ArrayList<>();

                                for (int i = 0; i < value.length(); i++) {

                                    yVals2.add(new Entry(Integer.parseInt(value.getString(i)), i));
                                }
                                final LineDataSet dataSet2 = new LineDataSet(yVals2, mename);
                                dataSet2.setLineWidth(100);
                                dataSet2.setCircleSize(10);
                                dataSet2.setValueTextSize(15);
                                List<LineDataSet> dataSetList = new ArrayList<>();
                                dataSetList.add(dataSet2);

                                final List<String> xVals = new ArrayList<>();
                                for (int i = 0; i < value.length(); i++) {
                                    xVals.add(dates.getString(i));
                                }

                                chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                                        if (e == null) return;
                                        Intent intent = new Intent(measureChart.this,
                                                popPoint.class);
                                        intent.putExtra("x",xVals.get(e.getXIndex())) ;
                                        intent.putExtra("y", String.valueOf(e.getVal())) ;
                                        startActivity(intent);
                                        MainActivity.toolbar.setTitle(mename);

                                        Toast.makeText(measureChart.this,
                                                xVals.get(e.getXIndex()) +"="+e.getVal()+"%",
                                                Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                    }
                                });

                                LineData data = new LineData(xVals, dataSetList);
                                chart.setData(data);
                                chart.invalidate();

                            }
                        }catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> parmater = new HashMap<>() ;

                parmater.put("_id", user.get("_id")) ;
                parmater.put("inid",  inid) ;
                parmater.put("f_year",first) ;
                parmater.put("s_year",second) ;

                Log.d("mchart", first + second);
                return  parmater ;
            }


        };
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(chart) ;
    }

}


