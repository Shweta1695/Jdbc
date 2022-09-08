package com.offpen.sp_pen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.offpen.sp_pen.helper.InputValidation;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateOrgActivity extends AppCompatActivity {


    TextInputEditText OrgName, OrgCnt, OrgAdd;
    AppCompatButton ButtonCreateOrg;
    String random, org_name, org_cnt, org_add, count, type;
    private InputValidation inputValidation;
    private static String value;
    private TextInputLayout OrgNameLayout;
    private final AppCompatActivity activity = CreateOrgActivity.this;


    public static String getValue() {
        return value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_org);
        initViews();
        initObjects();
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");


        ButtonCreateOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Org")) {

                    validate();


                }
            }
        });

    }

    private void initViews() {
        OrgName = findViewById(R.id.textInputEditTextOrgName);
        OrgNameLayout = findViewById(R.id.textInputLayoutOrgName);
        OrgAdd = findViewById(R.id.textInputEditTextOrgAdd);
        OrgCnt = findViewById(R.id.textInputEditTextOrgCnt);
        ButtonCreateOrg = findViewById(R.id.appCompatButtonCreateOrg);

    }

    private void initObjects() {
        inputValidation = new InputValidation(getApplicationContext());
    }

    private void validate() {
        if (!inputValidation.isInputEditTextFilled(OrgName, OrgNameLayout, getString(R.string.error_message_orgname_empty))) {
            return;
        }

        else {
            CreateOrgSql co = new CreateOrgSql();
            co.execute();
        }

    }


    //TODO
    private class CreateOrgSql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CreateOrgActivity.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();

                org_name = OrgName.getText().toString().trim();
                org_add = OrgAdd.getText().toString().trim();
                org_cnt = OrgCnt.getText().toString().trim();

                count = org_name.concat("1");
                value = count;

                System.out.println("Inserting records into the table...");
                String sql = "INSERT INTO organization_master VALUES ( '" + count + "', '" + org_name + "', '" + org_add + "', '" + type + "', 'false', '" + org_cnt + "' )";
                st.executeUpdate(sql);
                System.out.println("Inserted records into the table...");



                /*ResultSet rs = st.executeQuery("select * from organization_master");
                ResultSetMetaData rsmd = rs.getMetaData();

                if (rs.next()) {
                    result += rs.getString(2).toString() + "\n";
                }
                else
                {
                    result += "No data found"+ "\n";
                }*/
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {


            Toast.makeText(CreateOrgActivity.this, "on post execute create org", Toast.LENGTH_SHORT)
                    .show();

            Intent intent = new Intent(CreateOrgActivity.this, RegisterActivityMain.class);
            Bundle b = new Bundle();
            b.putString("orgId", count);
            intent.putExtras(b);
            // startActivity(intent);
            setResult(1000, intent);
            finish();
//            startActivity(intent);
        }
    }
}