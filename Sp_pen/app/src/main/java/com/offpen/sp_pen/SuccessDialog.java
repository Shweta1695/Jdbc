package com.offpen.sp_pen;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class SuccessDialog {

    Activity activity;
    AlertDialog dialog1;
    AlertDialog.Builder builder;
    ProgressBar p1;

    public SuccessDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoading() {
        builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.success_dialog , null));
        builder.setCancelable(true);
        dialog1 = builder.create();
        dialog1.show();
    }

    public void dismissDialog() {
        dialog1.findViewById(R.id.progressBar1).setVisibility(View.INVISIBLE);
        dialog1.findViewById(R.id.img1).setVisibility(View.VISIBLE);

     /*   dialog.findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
        dialog.findViewById(R.id.img2).setVisibility(View.VISIBLE);
*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog1.dismiss();

            }
        }, 6000);

    }

}
