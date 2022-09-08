package com.offpen.sp_pen.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.offpen.sp_pen.Adapter.viewpenAdapter;
import com.offpen.sp_pen.LoginActivity;
import com.offpen.sp_pen.PenFragment;
import com.offpen.sp_pen.R;
import com.offpen.sp_pen.model.viewpenmodel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends AppCompatActivity {
    private static final String url = "jdbc:mysql://192.168.1.9:3306/ScanPen_1?characterEncoding=utf8";
    private static final String user = "user";
    private static final String pass = "user@123";
    String orgId;
    //TextView pentexttab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //ConnectMySql connectMySql = new ConnectMySql();
        //connectMySql.execute("");

        // pentexttab = findViewById(R.id.pentexttab);


        //AsyncTask<String, Void, String> res= connectMySql.execute("");
        //pentexttab.setText(res.toString());
    }


    public class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";
        String result = "qwe";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //    Toast.makeText(null, "Please wait...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();

               /* System.out.println("Inserting records into the table...");
                String sql = "INSERT INTO persons VALUES (2, 'Offpen1', 'offpen1', 'add1','city1')";
                st.executeUpdate(sql);
                System.out.println("Inserted records into the table...");
*/
                ResultSet rs = st.executeQuery("select orgid from organization_master where " + orgId);
                ResultSetMetaData rsmd = rs.getMetaData();
                val1();
                if (rs.next()) {
                    result += rs.getString(1).toString() + "\n";
                } else {
                    result += "No data found" + "\n";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        private void val1() {
        }


        @Override
        protected void onPostExecute(String result) {
            //pentexttab.setText(res.toString());
        }
    }





}