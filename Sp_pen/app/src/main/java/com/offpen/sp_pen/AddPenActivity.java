package com.offpen.sp_pen;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.offpen.sp_pen.Admin.InviteAdmin;
import com.offpen.sp_pen.helper.InputValidation;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AddPenActivity extends AppCompatActivity {


    String type, regStatus, CurrStatus, Oldtype, factorytype, batteryPer, batteryHealth, firmVersion;
    int firmUpdate, Arstatus;
    LoadingDialog loadingDialog;
    TextInputLayout textdetails;
    private AppCompatButton ButtonCancel, ButtonAddPen, ButtonExit;
    private TextInputEditText serialedit;
    private final AppCompatActivity activity = AddPenActivity.this;
    private InputValidation inputValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pen);
        initViews();
        initListeners();


    }

    private void initViews() {
        ButtonAddPen = findViewById(R.id.ButtonAddPen);
        ButtonCancel = findViewById(R.id.ButtonCancel);
        serialedit = findViewById(R.id.serialedit);
        textdetails = findViewById(R.id.textdetails);
        loadingDialog = new LoadingDialog(AddPenActivity.this);
        ButtonExit = findViewById(R.id.ButtonExit);
        inputValidation = new InputValidation(activity);

    }

    private void initListeners() {
        ButtonAddPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();


            }
        });

        ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                //startActivity(i);
                finish();

            }
        });

        ButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), HomeActivity.class);
                finish();
            }
        });
    }

    private void validate() {
        if (!inputValidation.isInputEditTextFilled(serialedit, textdetails, getString(R.string.error_message_serialNumber))) {
            return;
        }
        else
        {
            AddPenSql connectMySql = new AddPenSql();
            connectMySql.execute("");

        }

    }

    private class AddPenSql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingDialog.startLoading();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddPenActivity.this);
                String orgId = preferences.getString("orgId", "");
                System.out.println("ORG ID CHECK THIS.." + orgId);

                type = "Basic";
                regStatus = "new";
                CurrStatus = "Off";
                Oldtype = "Basic";
                factorytype = "Basic";
                batteryPer = "30%";
                batteryHealth = "Good";
                firmUpdate = 0;
                Arstatus = 0;
                firmVersion = "23.0.1";

                System.out.println("Inserting records into the table...");

                String sql = "INSERT INTO pen_master VALUES ( '" + serialedit.getText() + "', '" + orgId + "', '" + type + "', '" + regStatus + "', '" + CurrStatus + "', '" + Oldtype + "', '" + factorytype + "', '" + batteryPer + "', '" + batteryHealth + "' , '" + firmUpdate + "', '" + Arstatus + "', '" + firmVersion + "')";
                st.executeUpdate(sql);
                System.out.println("Inserted records into the table...");

            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {


            /*Toast.makeText(AddPenActivity.this, "on post execute register sql  " + res, Toast.LENGTH_SHORT)
                    .show();*/
            //  animation();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismissDialog();
                    ButtonAddPen.setVisibility(View.GONE);
                    ButtonCancel.setVisibility(View.GONE);
                    ButtonExit.setVisibility(View.VISIBLE);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 6000);
        }
    }
}