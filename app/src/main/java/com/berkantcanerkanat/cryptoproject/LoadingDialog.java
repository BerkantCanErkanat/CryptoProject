package com.berkantcanerkanat.cryptoproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity activity){
        this.activity = activity;
    }
    void startLoadingDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        alertDialog.setView(layoutInflater.inflate(R.layout.custom_loading_dialog,null));
        alertDialog.setCancelable(false);
        dialog = alertDialog.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}
