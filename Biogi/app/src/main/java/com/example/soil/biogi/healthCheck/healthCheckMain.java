package com.example.soil.biogi.healthCheck;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soil.biogi.MainActivity;
import com.example.soil.biogi.R;
import com.example.soil.biogi.healthCheck.classItem.itemReport;
import com.example.soil.biogi.healthCheck.suggest.healthSuggest;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

/**
 * Created by soil on 2016/6/8.
 */

public class healthCheckMain extends Fragment {
    View view ;
    public static Fragment fragment;
    public static Fragment nextfragment ;
    private BottomBar mBottomBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_main, container, false);


        mBottomBar = BottomBar.attach(view.findViewById(R.id.mainFragment), savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.show();
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                MainActivity.toggleClass(true) ;
                if (menuItemId == R.id.healthCheck) {
                    fragment = new healthCheck();
                    MainActivity.toolbar.setTitle("健檢報告");

                } else if (menuItemId == R.id.itemReport) {
                    fragment = new itemReport();
                    MainActivity.toolbar.setTitle("分項報告");
                } else if (menuItemId == R.id.followSupport) {
                    fragment = new followUp();

                    MainActivity.toolbar.setTitle("後續追蹤");

                } else if (menuItemId == R.id.goods) {
                    fragment = new healthSuggest();
                    MainActivity.toolbar.setTitle("營養建議");

                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFragment, fragment)
                        .addToBackStack("tradeCustomer")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }

        });
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        MainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment();
            }
        });
        return view ;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    public void switchFragment() {
        if(healthCheckMain.fragment!=null){
            MainActivity.toolbar.setTitle("分項報告");
            MainActivity.toggleClass(true) ;
            getFragmentManager().beginTransaction().hide(nextfragment)
                    .show(healthCheckMain.fragment).commit();
        }
    }
}
