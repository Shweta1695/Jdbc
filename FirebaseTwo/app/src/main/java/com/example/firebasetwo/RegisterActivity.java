package com.example.firebasetwo;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;

import com.example.firebasetwo.helper.InputValidation;
import com.example.firebasetwo.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton registerUser;
    NestedScrollView nestedScrollView;
    LinearLayoutCompat linear_main;
    DatePickerDialog.OnDateSetListener mdateListner;
    TextInputEditText textInputEditTextName,
            textInputEditTextPassword, textInputEditTextLastName, textInputEditTextDOB,
            textInputEditTextContact, textInputEditTextAddress, textInputEditTextEmail;
    TextInputLayout textInputLayoutName,
            textInputLayoutPassword, textInputLayoutLastName, textInputLayoutEmail,
            textInputLayoutDOB, textInputLayoutContact, textInputLayoutAddress;
    String name, email, password, lastName, contact, address, dob, type;
    InputValidation inputValidation;
    Spinner staticSpinner;


     private FirebaseAuth auth;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        initViews();
        initListeners();
        initObjects();
        initSpinner();
        textInputEditTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this, R.style.DialogTheme,
                        mdateListner, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        mdateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("PRINT", "ON dATESET:  DATE: " + year + "/" + +month + "/" + dayOfMonth);
                String date = month + 1 + "/" + dayOfMonth + "/" + year;
                textInputEditTextDOB.setText(date);

            }
        };
        auth = FirebaseAuth.getInstance();

    }

    private void initViews() {
        registerUser = findViewById(R.id.appCompatButtonRegister);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        linear_main = findViewById(R.id.linear_main);
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextLastName = findViewById(R.id.textInputEditTextLastName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextDOB = findViewById(R.id.textInputEditTextDOB);
        textInputEditTextContact = findViewById(R.id.textInputEditTextContact);
        textInputEditTextAddress = findViewById(R.id.textInputEditTextAddress);

        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutLastName = findViewById(R.id.textInputLayoutLastName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutDOB = findViewById(R.id.textInputLayoutDOB);
        textInputLayoutContact = findViewById(R.id.textInputLayoutContact);
        textInputLayoutAddress = findViewById(R.id.textInputLayoutAddress);
    }

    private void initListeners() {
        registerUser.setOnClickListener(this);
        //  appCompatTextViewLoginLink.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(RegisterActivity.this);
    }

    private void initSpinner() {
        staticSpinner = findViewById(R.id.static_spinner);
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                validate_verify();

                name = textInputEditTextName.getText().toString().trim();
                password = textInputEditTextPassword.getText().toString().trim();
                lastName = textInputEditTextLastName.getText().toString().trim();
                email = textInputEditTextEmail.getText().toString().trim();
                dob = textInputEditTextDOB.getText().toString().trim();
                contact = textInputEditTextContact.getText().toString().trim();
                address = textInputEditTextAddress.getText().toString().trim();
                type = staticSpinner.getSelectedItem().toString();

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user = new User(name, email, password, lastName, dob, contact, address, type);
                                    FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(FirebaseAuth
                                                    .getInstance().getCurrentUser()
                                                    .getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this,"Added",Toast.LENGTH_SHORT).show();


                                            }
                                            else {
                                                Toast.makeText(RegisterActivity.this,"Fail",Toast.LENGTH_SHORT).show();

                                            }
                                            }
                                    });


                                    }
                                else{
                                    Toast.makeText(RegisterActivity.this,"Fail",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                break;
        }
    }

    private void validate_verify() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        } else if ((!inputValidation.isInputEditTextFilled(textInputEditTextLastName, textInputLayoutLastName, getString(R.string.error_message_lname))) ||
                (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email)))

        ) {
            return;

        } else if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextDOB, textInputLayoutDOB, getString(R.string.error_message_dob))) {
            return;
        }

        if (!inputValidation.isInputEditTextContact(textInputEditTextContact, textInputLayoutContact, getString(R.string.error_message_Contact))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextAddress, textInputLayoutAddress, getString(R.string.error_message_address))) {
            return;
        }



      /*  auth.createUserWithEmailAndPassword(textInputEditTextEmail.getText().toString().trim(),textInputEditTextPassword.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            user = new User(name, email, password, lastName, dob, contact, address, type);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,"User Registered successfully",Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this,"User Not Registered",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });



    }
*/

    }
}