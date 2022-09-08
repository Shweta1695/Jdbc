package com.offpen.sp_pen.sideDrawer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.offpen.sp_pen.R;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class My_org extends AppCompatActivity {

    String orgId;
    private TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_org);
        initViews();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(My_org.this);
        orgId = preferences.getString("orgId", "");
        System.out.println("ORG ID CHECK THIS.." + orgId);
        org_sql connectMySql = new org_sql();
        connectMySql.execute("");


    }

    private void initViews() {
        {
            t1= findViewById(R.id.acc_text1);
        }

    }

    private class org_sql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Inside my org drawer..", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();


                ResultSet rs = st.executeQuery("select * from organization_master where orgid='" + orgId + "'");
                if (rs.next()) {
                    System.out.println("hie");
                    String orgIdone = rs.getString(4).toString().trim();

                    t1.setText(orgIdone);
                }
                else{
                    t1.setText("No data found");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}