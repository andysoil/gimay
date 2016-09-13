package com.example.soil.biogi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Response;

/**
 * Created by soil on 2016/5/5.
 */
public class Dialog {
    private static ProgressDialog pDialog;
    public static void Dialog(Context ctx){
        pDialog = new ProgressDialog(ctx);
        pDialog.setMessage("請稍後...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    public static void dissDialog(){
        pDialog.dismiss();
    }


}
