package com.offpen.sp_pen.Tag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.offpen.sp_pen.PenFragment;
import com.offpen.sp_pen.R;
import com.offpen.sp_pen.helper.InputValidation;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TagPenActivity extends AppCompatActivity {


    Spinner spinner_tagpen, spinner_color, spinner_name;
    TextInputLayout mTextViewdetails;
    EditText mEditText;
    TextInputEditText edit_custom;
    Button btncancel, btnadd, customtag_button;
    String type, details;
    InputValidation inputValidation;
    String orgId;
    TextInputLayout textcustomtag;
    TextView select_pen, select_color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_pen);
        Bundle bundle = getIntent().getExtras();
        int TabPosition = bundle.getInt("TabPosition");
        Toast.makeText(TagPenActivity.this, TabPosition + " pos", Toast.LENGTH_SHORT).show();
        //viewpenAdapter v = new viewpenAdapter();
        initViews();
        initListeners();
        if (TabPosition == 1) {
            select_pen.setText("Select Student ID");
        }

        customtag_button.setTag(0);
        customtag_button.setText("Custom Tag");

        viewpendetails ob = new viewpendetails(TabPosition);
        ob.execute();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TagPenActivity.this);
        orgId = preferences.getString("orgId", "");


        ArrayAdapter staticAdaptercolor = ArrayAdapter.createFromResource(this, R.array.color_tagpen
                , android.R.layout.simple_spinner_item);
        staticAdaptercolor
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_color.setAdapter(staticAdaptercolor);

        //Display of no of pens in drop down in tag pen
        //  spinner_tagpen.setOnItemSelectedListener(getApplicationContext());
/*        ArrayAdapter<CharSequence> staticAdapter = new ArrayAdapter(this, R.layout.spinner_tagpen,
                R.id.texttagpen
                , arrlist_name);*/

        // Apply the adapter to the spinner

        System.out.println("ORG ID CHECK THIS.." + orgId);

        spinner_tagpen.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);

                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                        Toast.makeText(getApplicationContext(), "js " + spinner_tagpen.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        if (spinner_name.getVisibility() == View.VISIBLE) {

            spinner_name.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);

                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));
                            Toast.makeText(getApplicationContext(), "js ", Toast.LENGTH_SHORT).show();
                            details = spinner_tagpen.getSelectedItem().toString().trim();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    }

            );

        }
    }

    private void initViews() {
        spinner_tagpen = findViewById(R.id.static_spinner_pen);
        spinner_color = findViewById(R.id.static_spinner_color);
        mTextViewdetails = findViewById(R.id.textdetails);
        mEditText = findViewById(R.id.textedit);
        btncancel = findViewById(R.id.ButtonCanceltagpen);
        btnadd = findViewById(R.id.ButtonCreatePenTag);
        spinner_name = findViewById(R.id.static_spinner_name);
        customtag_button = findViewById(R.id.customtag_button);
        edit_custom = findViewById(R.id.edit_custom);
        textcustomtag = findViewById(R.id.textcustomtag);

        select_pen = findViewById(R.id.selectpen);
        select_color = findViewById(R.id.selectcolor);
    }

    private void initListeners() {

        customtag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();

                // spinner_name.setVisibility(View.GONE);
                //  textcustomtag.setVisibility(View.VISIBLE);

                textcustomtag.setHint("Enter here");

                if (status == 1) {
                    edit_custom.setText("");
                    customtag_button.setText("Custom Tag");
                    spinner_name.setVisibility(View.VISIBLE);
                    textcustomtag.setVisibility(View.GONE);
                    v.setTag(0); //pause
                } else {
                    customtag_button.setText(" Preset Tag");
                    spinner_name.setVisibility(View.GONE);
                    textcustomtag.setVisibility(View.VISIBLE);
                    v.setTag(1); //orignal text
                }
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clicked = true;
                String det = postDataToSQLite(); // validation
                insertTagPen itp = new insertTagPen(det);
                itp.execute("");

            }


        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PenFragment.class);
                //startActivity(i);
                finish();

            }
        });
    }

    private String postDataToSQLite() {
        if (edit_custom.isShown()) {
            if (edit_custom.length() == 0) {
                edit_custom.setError("This field is required");
                return "false";
            } else if (edit_custom.length() != 0) {
                details = String.valueOf(edit_custom.getText());
                System.out.println("Length " + details);
            }

        } else if (spinner_name.isShown()) {
            if (spinner_name != null && spinner_name.getSelectedItem() != null) {
                details = spinner_name.getSelectedItem().toString().trim();
                System.out.println("spinner has   " + details);
            } else {
                System.out.println("Nothing   " + details);
            }
        }


        return details;
    }

    public class insertTagPen extends AsyncTask<String, Void, String> {

        String details;

        public insertTagPen(String details) {
            this.details = details;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();
                System.out.println("Inserting records into the table...");
                System.out.println("*******************************************************************************************************");
                System.out.println("details" + mEditText.getText() + "++++++++***" + spinner_color.getSelectedItem().toString().trim() + "***" + type);

                //todo if loop for fragment tab selection
                //spinner_name.getSelectedItem().toString().trim()
                String sql = "INSERT INTO tag_master(orgid,tagname,details,colour,type) VALUES ( '" + orgId + "', '" + details + "', '" + mEditText.getText() + "', '" + spinner_color.getSelectedItem().toString().trim() + "', '" + type + "')";
                st.executeUpdate(sql);
                con.close();

                //     String sql = "INSERT INTO pen_master VALUES ( '" + serialedit.getText() + "', '" + orgId + "', '" + type + "', '" + regStatus + "', '" + CurrStatus + "', '" + Oldtype + "', '" + factorytype + "', '" + batteryPer + "', '" + batteryHealth + "' , '" + firmUpdate + "', '" + Arstatus + "', '" + firmVersion + "')";


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(TagPenActivity.this, "Tag Assigned", Toast.LENGTH_SHORT).show();
        }

    }

    public class viewpendetails extends AsyncTask<String, Void, String> {
        String res = "";
        int tb;
        ArrayList<String> arrlist_penname = new ArrayList<String>();
        ArrayList<String> arrlist_name = new ArrayList<String>();
        ArrayList<Integer> arrlist_stdId = new ArrayList<>();


        List<String> arrlist_type = new ArrayList<>();

        public viewpendetails(int tb) {
            this.tb = tb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();

                if (tb == 1) {
                    type = "student";
                    ResultSet rs2 = st.executeQuery("select studentid from studentmaster");
                    while (rs2.next()) {
                        int sdtId = rs2.getInt(1);
                        arrlist_stdId.add(sdtId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Student ID" + arrlist_stdId, Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> staticAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item
                                        , arrlist_stdId);
                                staticAdapter
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_tagpen.setAdapter(staticAdapter);
                            }
                        });
                    }
                    ResultSet rs3 = st.executeQuery("select tagname from tag_master where orgid='" + orgId + "'");
                    while (rs3.next()) {
                        String tagname = rs3.getString(1);
                        arrlist_name.add(tagname);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "tag names " + arrlist_name, Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> staticAdapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item
                                        , arrlist_name);
                                staticAdapter1
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_name.setAdapter(staticAdapter1);
                            }
                        });
                    }
                }
                if (tb == 0) {
                    type = "Pen";

                    //TODO

                    /* if (customtag_button. == 1){

                    }
*/
                    ResultSet rs = st.executeQuery("select serialnumber from pen_master");

                    while (rs.next()) {
                        String pennamedb = rs.getString(1);
                        arrlist_penname.add(pennamedb);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "text" + arrlist_penname, Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> staticAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item
                                        , arrlist_penname);

                                staticAdapter
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_tagpen.setAdapter(staticAdapter);
                            }
                        });
                    }

                    ResultSet rs1 = st.executeQuery("select tagname from tag_master where orgid='" + orgId + "'");
                    while (rs1.next()) {
                        String tagname = rs1.getString(1);
                        arrlist_name.add(tagname);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "text" + arrlist_name, Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> staticAdapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item
                                        , arrlist_name);

                                staticAdapter1
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_name.setAdapter(staticAdapter1);
                            }
                        });

                    }

                } else {
                    System.out.println("no data");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return arrlist_penname.toString();
        }

        private void insertmethod() {


        }

        @Override
        protected void onPostExecute(String result) {


        }
    }

}