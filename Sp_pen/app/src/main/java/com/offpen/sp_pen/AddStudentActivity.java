package com.offpen.sp_pen;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.offpen.sp_pen.helper.DatabaseHelper;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {


    TextInputEditText f_name, l_name, DOB, f_lang, t_lang, passcode;
    AppCompatButton add_button, cancel_button;
    String radioGender;
    RadioGroup rg;
    char g;
    String orgId, imagepath;
    int arstatus, login_out, examlock, pen_sno, success = 0;

    RadioButton m, f, o, n;
    DatePickerDialog.OnDateSetListener mdateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        initViews();
        initListeners();

    }

    private void initViews() {
        f_name = findViewById(R.id.textInputEditTextfName);
        l_name = findViewById(R.id.textInputEditTextLName);
        DOB = findViewById(R.id.textInputEditTextDOB);
        f_lang = findViewById(R.id.textInputEditTextflang);
        t_lang = findViewById(R.id.textInputEditTexttlang);
        passcode = findViewById(R.id.textInputEditTextPassc);
        add_button = findViewById(R.id.appCompatButtonAddStudent);
        cancel_button = findViewById(R.id.appCompatButtonCancelStudent);

        m = findViewById(R.id.radioMale);
        f = findViewById(R.id.radioFemale);
        o = findViewById(R.id.radioOthers);
        n = findViewById(R.id.radioDNS);
        rg = findViewById(R.id.radioGroup);
    }

    private void initListeners() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (m.isChecked()) {
                    radioGender = m.getText().toString();
                } else if (f.isChecked()) {
                    radioGender = f.getText().toString();
                } else if (o.isChecked()) {
                    radioGender = o.getText().toString();
                } else if (n.isChecked()) {
                    radioGender = n.getText().toString();
                }
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                Toast.makeText(AddStudentActivity.this, "you selected " + radioGender, Toast.LENGTH_SHORT).show();
            }
        });

        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddStudentActivity.this, R.style.DialogTheme,
                        mdateListner, year, month, day);
            //Calender
          //      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.show();
            }
        });
        mdateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("PRINT", "ON dATESET:  DATE: " + year + "/" + +month + "/" + dayOfMonth);

                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                DOB.setText(date);

            }
        };
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStudentSql add_student_sql = new AddStudentSql();
                add_student_sql.execute();

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class AddStudentSql extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHelper db= new DatabaseHelper();
         alertDialog();

        }
        public void alertDialog(){





            AlertDialog.Builder dialog=new AlertDialog.Builder(AddStudentActivity.this);
            dialog.setMessage("Please Select any option");
            dialog.setTitle("Dialog Box");
            dialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                        }
                    });
            dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog=dialog.create();
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddStudentActivity.this);
                orgId = preferences.getString("orgId", "");
                System.out.println("ORG ID CHECK THIS.." + orgId);
                g = radioGender.charAt(0);
                System.out.println("Inserting records into the table...");
                arstatus = 0; 
                login_out = 0;
                examlock = 0;
                imagepath = "null";
                pen_sno = 1;
                String sql = "INSERT INTO studentmaster(orgId,fname,lname,passcode,flanguage,tlanguage,dob,arstatus,login_out,examlock,imagepath,penserialnumber,gender) " +
                        "VALUES ( '" + orgId + "', '" + f_name.getText() + "', '" + l_name.getText() + "', '" + passcode.getText() + "', '" + f_lang.getText() + "', '" + t_lang.getText() + "', '" + DOB.getText() + "', '" + arstatus + "' , '" + login_out + "', '" + examlock + "', '" + imagepath + "', '" + pen_sno + "', '" + g + "')";
                st.executeUpdate(sql);
                success = 1;
                System.out.println("Inserted records into the table...");


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (success == 1) {
                Toast.makeText(getApplicationContext(), "Inserted Successfully ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please re-insert again", Toast.LENGTH_SHORT).show();
            }
            //      Toast.makeText(getApplicationContext(), "f_name is " + f_name.getText() + " l_name " + l_name.getText() + " passcode", Toast.LENGTH_SHORT).show();


        }

    }


}