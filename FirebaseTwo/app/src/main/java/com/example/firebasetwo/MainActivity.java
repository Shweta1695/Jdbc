package com.example.firebasetwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.firebasetwo.helper.InputValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    private InputValidation inputValidation;
    private LinearLayout linear_main_login;
    String email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        initObjects();
        auth =FirebaseAuth.getInstance();

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
        inputValidation = new InputValidation(MainActivity.this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verify_validate();
                email = textInputEditTextEmail.getText().toString().trim();
                password = textInputEditTextPassword.getText().toString().trim();

                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }


    }

    private void verify_validate() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }
//        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
//                , textInputEditTextPassword.getText().toString().trim())) {
//
//            Intent accountsIntent = new Intent(getApplicationContext(), UsersListActivity.class);
//            // Toast.makeText(MainActivity.this, " Welcome", Toast.LENGTH_SHORT).show();
//            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
//            emptyInputEditText();
//            startActivity(accountsIntent);
        // }
        else {
        //   Toast.makeText(MainActivity.this, " Wrong Details", Toast.LENGTH_SHORT).show();
            // Snack Bar to show success message that record is wrong
        //    Snackbar.make(linear_main_login, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);

    }

}