package com.offpen.sp_pen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    ProgressBar p1;

    LoadingDialog(Activity myactivity) {
        activity = myactivity;
    }

    void startLoading() {
        builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.addpen_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.findViewById(R.id.progressBar1).setVisibility(View.INVISIBLE);
        dialog.findViewById(R.id.img1).setVisibility(View.VISIBLE);

        dialog.findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
        dialog.findViewById(R.id.img2).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();


            }
        }, 4000);


    }
}