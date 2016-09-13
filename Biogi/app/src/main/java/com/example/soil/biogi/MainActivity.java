package com.example.soil.biogi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.soil.biogi.chat.talkroom;
import com.example.soil.biogi.healthCheck.healthCheckMain;
import com.example.soil.biogi.goods.goods;
import com.example.soil.biogi.measure.measureClass;
import com.example.soil.biogi.memberSet.memberSetIn;
import com.example.soil.biogi.qanda.question_main;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtName, txtDate;
    static Context context ;
    static DrawerLayout drawer;
    private SaveText db;
    private SessionManger session;
    public static  ActionBarDrawerToggle toggle ;
    String fragmentArray[] = {"健檢報告","量測紀錄","聊天室","設定","保健商品","線上Q&A"} ;
    public static Toolbar toolbar ;
    public static FragmentManager fragmentManager;
    public static String toolbarname=null ;
    public static String isMenu = "follow";
    public static Fragment nextFragment = null;
    public static Fragment fragment = null;
    public static Fragment midfragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        db = new SaveText(getApplicationContext());
        session = new SessionManger(getApplicationContext()); //need before if session
        context = this.getApplicationContext() ;
        HashMap<String, String> user = db.getUserDetails();

        initToolbar() ;
        initFragment(savedInstanceState) ;
        //session check is login in ;
        if (!session.isLoggedIn()) {
          //  logout_check();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        txtName = (TextView) header.findViewById(R.id.TextName);
        txtDate = (TextView) header.findViewById(R.id.TextDate);
        txtName.setText( user.get("username"));
        txtDate.setText(user.get("created_at"));



    }

    public void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(fragmentArray[0]);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggleClass(true) ;
    }

        @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();

        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if(id == R.id.notice){



        }

        return super.onOptionsItemSelected(item);
    }
    @Override

    public boolean onPrepareOptionsMenu(Menu menu) {

       if(isMenu.equals("none")) {
            menu.findItem(R.id.notice).setVisible(false);

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        MainActivity.toggleClass(true) ;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.healthCheck) {
            fragment = new healthCheckMain();
            isMenu="follow" ;
            toolbar.setTitle(fragmentArray[0]);

        } else if (id == R.id.measureClass) {
            isMenu="chart" ;
            toolbar.setTitle(fragmentArray[1]);
            fragment = new measureClass();

        } else if (id == R.id.talkroom) {
            toolbar.setTitle(fragmentArray[2]);
            isMenu="none" ;
            fragment = new talkroom();
        } else if (id == R.id.memberSetIn) {
            toolbar.setTitle(fragmentArray[3]);
            isMenu="none" ;
            fragment = new memberSetIn();
        }
        else if(id==R.id.goods){
            toolbar.setTitle(fragmentArray[4]);
            isMenu="none" ;
            fragment = new goods();
        }
        else if(id==R.id.q_and_a){
            toolbar.setTitle(fragmentArray[5]);
            isMenu="none" ;
            fragment = new question_main();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.homepageLayout, fragment)
                .addToBackStack("tradeCustomer")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();


        invalidateOptionsMenu();
        drawer.closeDrawers();   //自動關閉選單
        return true;
    }
    public void logout_check() {
        session.setLogin(false);
        db.deleteUsers();
        startActivity(new Intent(MainActivity.this,
                LoginActivity.class));
        finish();
    }

    public static void toggleClass(Boolean todrawer) {
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(todrawer);
        toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp) ;
        toggle.syncState();

    }

    public void initFragment(Bundle savedInstanceState)
    {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if(savedInstanceState==null) {
            Fragment fragment = new healthCheckMain();
            fragmentManager.beginTransaction().replace(R.id.homepageLayout, fragment, "healthcheck")
                    .addToBackStack("healthcheck")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

        }
    }
}
