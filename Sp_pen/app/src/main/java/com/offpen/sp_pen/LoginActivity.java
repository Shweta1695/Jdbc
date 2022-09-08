package com.offpen.sp_pen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.offpen.sp_pen.helper.InputValidation;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String user = "user";
    private static final String pass = "user@123";
    private final AppCompatActivity activity = LoginActivity.this;
    private AppCompatTextView textViewLinkRegister;
    private AppCompatButton appCompatButtonLogin;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    private InputValidation inputValidation;
    // private DatabaseHelper databaseHelper;
    private ConstraintLayout linear_main_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // variables var= new variables();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        linear_main_login = findViewById(R.id.linear_main_login);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);

        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    private void initListeners() {
        textViewLinkRegister.setOnClickListener(this);
        textViewLinkRegister.setSelected(true);
        appCompatButtonLogin.setOnClickListener(this);
    }

    private void initObjects() {
        //   databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }


    @Override
    public void onClick(View v) {;
        switch (v.getId()) {
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.appCompatButtonLogin:

               /* InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); */
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);


                validations();

                Toast.makeText(this, "Login Button pressed", Toast.LENGTH_SHORT).show();
                //   verifyFromSQLite();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
     private void validations() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        } else {

            loginMySql connectMySql = new loginMySql();
            connectMySql.execute("");

        }

       /* else{
          //  Toast.makeText(LoginActivity.this, " Wrong Details", Toast.LENGTH_SHORT).show();

            // Snack Bar to show success message that record is wrong
         //   Snackbar.make(linear_main_login, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
       */


    }


    private class loginMySql extends AsyncTask<String, Void, String> {
        String res = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LoginActivity.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select userid,password,fname,orgid from user_master where userid = '" +
                        textInputEditTextEmail.getText().toString().trim() + "' and password = '" +
                        textInputEditTextPassword.getText().toString().trim() + "' ");


                //ResultSetMetaData rsmd = rs.getMetaData();
                if (rs.next()) {

                    String CurrUserId = rs.getString(1).toString().trim();
                    String orgId = rs.getString(4).toString().trim();
                    Intent intenthome = new Intent(getApplicationContext(), HomeActivity.class);
                    String getrec = textInputEditTextEmail.getText().toString().trim();

                    Bundle b = new Bundle();
                    String fname = rs.getString(3).toString().trim();
                    b.putString("fname", fname);
                    intenthome.putExtras(b);

                    //Code to put into Shared Preferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("orgId", orgId);
                    editor.putString("currentUserName", fname);
                    editor.putString("currentUserId", CurrUserId);
                    editor.apply();


                    startActivity(intenthome);
//                   result = rs.getString(3).toString() + "\n";
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Wrong Details Entered", Toast.LENGTH_SHORT).show();

                        }
                    });
        //            Snackbar.make(linear_main_login, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();

                    result += "No data found" + "\n";
                }
                res = result;

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });



                res = e.toString();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

           /* Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT)
                    .show();*/
            //  txtData.setText(result);
        }
    }


}
