package com.offpen.sp_pen;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.offpen.sp_pen.helper.InputValidation;
import com.offpen.sp_pen.model.RegisterMainModel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class RegisterActivityMain extends AppCompatActivity {

    Spinner staticSpinner;
    String spinnerValue;
    String userType = "";
    AppCompatButton ButtonCreate, appCompatButtonRegister, ButtonCancelRegister;
    private InputValidation inputValidation;
    TextInputLayout textInputLayoutOrgcode, LayoutTitle, layout_f_name, layout_l_name, layout_email, layout_pass, layout_cnf_pass, layout_passcode;

    TextInputEditText textInputEditTextOrgcode, textInputEditTextName, textInputEditTextLastName,
            textInputEditTextEmail, textInputEditTextPassword, textInputEditTextcnfPassword,
            textInputEditTextContact, textInputEditTextAddress, textInputEditTextPasscode, textInputEditTextTitle;
    String img_path = "null";
    String f_name, l_name, email, password, cnf_password, mobile, address, passcode, org_code, radioGender, orgId, title, acc_type1, acc_type, randomOrg;
    int ar_status, login_status;
    char g;
    // l_name  = "null";
    RadioGroup rg;
    RadioButton m, f, o, n;

    RegisterMainModel mainModel = new RegisterMainModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);
        initViews();
        initObjects();
        Bundle bundle = getIntent().getExtras();
        acc_type1 = bundle.getString("Clicked_Acc");
        acc_type = acc_type1;
        orgId = bundle != null ? bundle.getString("orgId") : CreateOrgActivity.getValue();

        mainModel.setAcc_type(acc_type);

        if (acc_type.equals("Individual")) {
            textInputLayoutOrgcode.setHint("Reference Code");
            ButtonCreate.setText("Generate");
        } else if (acc_type.equals("Org")) {
        }
        textInputLayoutOrgcode.setHint("Organisation Code");


        //      Bundle bundle = getIntent().getExtras();
//        orgId = bundle != null ? bundle.getString("orgId") : "";
       /* Toast.makeText(RegisterActivityMain.this,"hoho"+ orgId,Toast.LENGTH_SHORT).show();
        textInputEditTextOrgcode.setText(orgId);*/

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        staticSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        Object item = parent.getItemAtPosition(pos);
                        if (item.toString().equals("Owner")) {
                            userType = "Owner";
                            ButtonCreate.setVisibility(View.VISIBLE);
                            textInputEditTextOrgcode.setEnabled(false);
                            ButtonCreate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (acc_type.equals("Individual")) {
                                        Random ran = new Random();
                                        int r = ran.nextInt();
                                        f_name = textInputEditTextName.getText().toString().trim();
                                        randomOrg = f_name + r;
                                        orgId = randomOrg;
                                        textInputEditTextOrgcode.setText(randomOrg);
                                        try {
                                            Class.forName("com.mysql.jdbc.Driver");
                                            Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                                            System.out.println("Database connection success");
                                            String result = "Database Connection Successful\n";
                                            Statement st = con.createStatement();

                                            System.out.println("Inserting records into the table...");
                                            String sql = "INSERT INTO organization_master VALUES ( '" + randomOrg + "', '" + f_name + "', 'null', '" + acc_type + "', 'false', 'null' )";
                                            st.executeUpdate(sql);
                                            System.out.println("Inserted records into the table...");

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Intent intent = new Intent(RegisterActivityMain.this, CreateOrgActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("type", "Org");
                                        intent.putExtras(b);
                                        //onPause();
                                        startActivityForResult(intent, 1000);
                                    }
                                }
                            });
                        }


                        if (item.toString().equals("Admin")) {
                            userType = "Admin";
                            ButtonCreate.setVisibility(View.GONE);
                            textInputEditTextOrgcode.setEnabled(true);
                        }
                        if (item.toString().equals("Type")) {
                            ButtonCreate.setVisibility(View.GONE);
                            textInputEditTextOrgcode.setEnabled(false);
                        }

                        System.out.println(item.toString());     //prints the text in spinner item.

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //todo check
        if (m.isChecked()) {
            radioGender = m.getText().toString();
        } else if (f.isChecked()) {
            radioGender = f.getText().toString();
        } else if (o.isChecked()) {
            radioGender = o.getText().toString();
        } else if (n.isChecked()) {
            radioGender = n.getText().toString();
        }
        //       Toast.makeText(RegisterActivityMain.this, "you selected "+radioGender, Toast.LENGTH_SHORT).show();

        ButtonCancelRegister.setOnClickListener(v -> finish());

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
                Toast.makeText(RegisterActivityMain.this, "you selected " + radioGender, Toast.LENGTH_SHORT).show();
            }
        });

        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                validate();
            }


        });

    }

    private void initObjects() {
        inputValidation = new InputValidation(RegisterActivityMain.this);
    }

    private void validate() {

        Toast.makeText(RegisterActivityMain.this,"clicked register now button",Toast.LENGTH_SHORT).show();
        //Validations for empty fields
        if (!inputValidation.isInputEditTextFilled(textInputEditTextTitle, LayoutTitle, getString(R.string.error_message_title_empty))) {
            return;
        }
        String value = textInputEditTextTitle.getText().toString().trim();
        if (value.equals(".")) {
            LayoutTitle.setError(getString(R.string.error_title_dot));
        }


        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, layout_f_name, getString(R.string.error_message_fname_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextLastName, layout_l_name, getString(R.string.error_message_lname_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, layout_email, getString(R.string.error_message_email_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, layout_email, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, layout_pass, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextcnfPassword, layout_cnf_pass, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputPasswordPatternMatch(textInputEditTextPassword, layout_pass, getString(R.string.error_message_password_length))) {
            return;
       }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPasscode, layout_passcode, getString(R.string.error_message_passcode_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextOrgcode, textInputLayoutOrgcode, getString(R.string.error_message_orgcode_empty))) {
            return;
        }

        if (!inputValidation.isInputEditTextMatches(textInputEditTextcnfPassword,textInputEditTextPassword, layout_cnf_pass, getString(R.string.error_message_password_not_match))) {
            return;
        }

//        if(textInputEditTextTitle.getText().toString().matches(""))
//        {
//            Toast.makeText(RegisterActivityMain.this,"enter title",Toast.LENGTH_SHORT).show();
//        }
       /* Drawable orignal =textInputEditTextTitle.getBackground();
        if(textInputEditTextTitle.getText().toString().isEmpty()){
            textInputEditTextTitle.setError("Enter Title");
           // textInputEditTextTitle.setBackgroundResource(R.drawable.err_back);
        }else {
            textInputEditTextTitle.setBackgroundDrawable(orignal);


        }*/


//        else{
//            Registersql connectMySql = new Registersql();
//            connectMySql.execute("");
//        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        mainModel.setAcc_type(acc_type);

    }

    /*@Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();

         Toast.makeText(RegisterActivityMain.this, "on Resume " + orgId, Toast.LENGTH_SHORT).show();
        textInputEditTextOrgcode.setText(orgId);
        acc_type = bundle.getString("Clicked_Acc");


    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String orgId = data.getStringExtra("orgId");
        Toast.makeText(RegisterActivityMain.this, "on activity result .... " + orgId, Toast.LENGTH_SHORT).show();
        textInputEditTextOrgcode.setText(orgId);
    }



   /* @Override
    public void onRestart() {
        super.onRestart();
        Bundle bundle = getIntent().getExtras();
        orgId = bundle != null ? bundle.getString("orgId") : "";
        Toast.makeText(RegisterActivityMain.this,"hello"+ orgId,Toast.LENGTH_SHORT).show();
        textInputEditTextOrgcode.setText(orgId);
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }*/

   /* @Override
    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode, resCode, data);
        Bundle bundle = data.getExtras();
        orgId = bundle != null ? bundle.getString("orgId") : "";
        Toast.makeText(RegisterActivityMain.this,"hello"+ orgId,Toast.LENGTH_SHORT).show();
        textInputEditTextOrgcode.setText(orgId);
    }*/

    private void initViews() {
        staticSpinner = findViewById(R.id.static_spinner);
        ButtonCreate = findViewById(R.id.appCompatButtonCreate);
        textInputEditTextOrgcode = findViewById(R.id.textInputEditTextOrgcode);
        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);
        ButtonCancelRegister = findViewById(R.id.appCompatButtonCancelRegister);
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextLastName = findViewById(R.id.textInputEditTextLastName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextcnfPassword = findViewById(R.id.textInputEditTextcnfPassword);
        textInputEditTextContact = findViewById(R.id.textInputEditTextContact);
        textInputEditTextAddress = findViewById(R.id.textInputEditTextAddress);
        textInputEditTextPasscode = findViewById(R.id.textInputEditTextPasscode);
        textInputEditTextTitle = findViewById(R.id.textInputEditTextTitle);

        textInputLayoutOrgcode = findViewById(R.id.textInputLayoutOrgcode);
        LayoutTitle = findViewById(R.id.textInputLayoutTitle);
        layout_f_name = findViewById(R.id.textInputLayoutName);
        layout_l_name = findViewById(R.id.textInputLayoutLastName);
        layout_email = findViewById(R.id.textInputLayoutEmail);
        layout_pass = findViewById(R.id.textInputLayoutPassword);
        layout_cnf_pass = findViewById(R.id.textInputLayoutcnfPassword);

        layout_passcode = findViewById(R.id.textInputLayoutPasscode);



        m = findViewById(R.id.radioMale);
        f = findViewById(R.id.radioFemale);
        o = findViewById(R.id.radioOthers);
        n = findViewById(R.id.radioDNS);
        rg = findViewById(R.id.radioGroup);
    }

    //todo for Admin

    //For Admin
    private class Registersql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(RegisterActivityMain.this, "Please wait... register sql", Toast.LENGTH_SHORT)
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
                email = textInputEditTextEmail.getText().toString().trim();
                f_name = textInputEditTextName.getText().toString().trim();
                l_name = textInputEditTextLastName.getText().toString().trim();
                password = textInputEditTextPassword.getText().toString().trim();
                passcode = textInputEditTextPasscode.getText().toString().trim();
                orgId = textInputEditTextOrgcode.getText().toString().trim();
                ar_status = 0;
                login_status = 0;
                g = radioGender.charAt(0);
                title = textInputEditTextTitle.getText().toString().trim();

                if (userType.equals("Owner") && (acc_type.equals("Org"))) {
                    System.out.println("Inserting records into the table...");
                    String sql = "INSERT INTO user_master VALUES ( '" + email + "', '" + orgId + "', '" + password + "', '" + passcode + "', '" + userType + "', '" + acc_type + "', '" + ar_status + "', '" + login_status + "', '" + title + "' , '" + f_name + "', '" + l_name + "', 'null', '" + g + "')";
                    st.executeUpdate(sql);
                    System.out.println("Inserted records into the table...");


                } else if (userType.equals("Admin")) {

                    ResultSet rs = st.executeQuery("select orgid from organization_master where orgid='" + orgId + "'");
                    if (rs.next()) {
                        System.out.println("Inserting records into the table...");
                        String sql = "INSERT INTO user_master VALUES ( '" + email + "', '" + orgId + "', '" + password + "', '" + passcode + "', '" + userType + "', '" + acc_type + "', '" + ar_status + "', '" + login_status + "', '" + title + "' , '" + f_name + "', '" + l_name + "', 'null', '" + g + "')";
                        st.executeUpdate(sql);
                        System.out.println("Inserted records into the table...");
                        result = "Registered Successfully";
                    } else {
                        System.out.println("Organisation Id not present");
                        result = "Organisation Id not present";
                        //  Toast.makeText(RegisterActivityMain.this,"Organisation Id not present",Toast.LENGTH_SHORT).show();

                    }
                } else if ((userType.equals("Owner")) && (acc_type.equals("Individual"))) {
                    //      Toast.makeText(RegisterActivityMain.this, "Inside indi admin", Toast.LENGTH_SHORT).show();


                    System.out.println("Inserting records into the table...");
                    String sql = "INSERT INTO user_master VALUES ( '" + email + "', '" + randomOrg + "', '" + password + "', '" + passcode + "', '" + userType + "', '" + acc_type + "', '" + ar_status + "', '" + login_status + "', '" + title + "' , '" + f_name + "', '" + l_name + "', 'null', '" + g + "')";
                    st.executeUpdate(sql);
                    System.out.println("Inserted records into the table...");
                    result = "Registered Successfully";

                }



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
            Toast.makeText(RegisterActivityMain.this, "on post execute register sql  " + res, Toast.LENGTH_SHORT)
                    .show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(RegisterActivityMain.this, LoginActivity.class);
                    startActivity(i);
                }
            }, 2000);
        }
    }

}