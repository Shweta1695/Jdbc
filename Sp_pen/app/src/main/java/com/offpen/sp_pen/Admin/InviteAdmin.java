package com.offpen.sp_pen.Admin;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.offpen.sp_pen.LoginActivity;
import com.offpen.sp_pen.R;
import com.offpen.sp_pen.SuccessDialog;
import com.offpen.sp_pen.helper.InputValidation;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class InviteAdmin extends AppCompatActivity {

    TextInputLayout f_name1, l_name1, email1;
    TextInputEditText f_name, l_name, email;
    Spinner permission;
    SuccessDialog successDialog;
    Button btncancel, btninvite, btnexit;
    Thread t = null;
    private final AppCompatActivity activity = InviteAdmin.this;

    private InputValidation inputValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_admin);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        initViews();
        initListeners();

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputValidation = new InputValidation(activity);

        permission.setAdapter(staticAdapter);

        permission.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                      /*  if(position == 0)
                        {
                            permission.setBackgroundColor(Color.LTGRAY);

                        }
                        else
                        {
                            permission.setBackgroundColor(Color.TRANSPARENT);

                        }*/


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

    }

    private void initListeners() {
        btninvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();

            }

        });


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        btncancel.setOnClickListener(v -> finish());

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void validate() {

        if (!inputValidation.isInputEditTextFilled(email, email1, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(email, email1, getString(R.string.error_message_email))) {
            return;
        } else {

            successDialog = new SuccessDialog(InviteAdmin.this);
            runOnUiThread(new Runnable() {
                public void run() {
                    successDialog.startLoading();
                }
            });


            btninvite.setEnabled(false);
            btncancel.setEnabled(false);


         /*     final String username="shwetat@scanningpens.com" ;
//              String password ="cnjtqfdrngkdihpy";
              String password="nwgbhgktxtymhlnq";*/


                /*final String username="noreply@wizcomtech.com" ;
                String password="qzhjkrjkrmscmnwz";
                */
//code from here
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final String username = "noreply@scanningpens.com";
                    String password = "wyhtgmxdhngnnhnh";


                    String msg = "Hello,\n This is a test mail";
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.socketFactory.port", "587");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.host", "smtp.office365.com");
                    props.put("mail.smtp.port", "587");
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);

                                }
                            });
                    try {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getText().toString().trim()));
                        message.setSubject("Sending test email");
                        message.setText(msg);
                        Transport.send(message);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "email send succesfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (MessagingException e) {

                        // Toast.makeText(getApplicationContext(), "Invalid Address", Toast.LENGTH_SHORT).show();

                        throw new RuntimeException(e);
                    }


                }
            }, 1000);

            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        successDialog.dismissDialog();
                        email.setText("");
                        f_name.setText("");
                        l_name.setText("");
                        email.setEnabled(false);
                        f_name.setEnabled(false);
                        l_name.setEnabled(false);
                        permission.setEnabled(false);
                        btninvite.setVisibility(View.INVISIBLE);
                        btncancel.setVisibility(View.INVISIBLE);
                        btnexit.setVisibility(View.VISIBLE);

                         /*   try {
                                Thread.sleep(2000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                    }
                }, 6000);
            } catch (Exception e) {

                // Toast.makeText(getApplicationContext(), "Invalid Address", Toast.LENGTH_SHORT).show();

                throw new RuntimeException(e);
            }


        }
    }

    void initViews() {
        f_name = findViewById(R.id.textInputEditTextf_name);
        l_name = findViewById(R.id.textInputEditTextl_name);
        permission = findViewById(R.id.static_spinner_admin);
        email = findViewById(R.id.textInputEditTextemail);
        email1 = findViewById(R.id.textInputLayoutemail);
        btncancel = findViewById(R.id.ButtonCancelInvite);
        btninvite = findViewById(R.id.ButtonInviteAdmin);
        btnexit = findViewById(R.id.ButtonExit);


    }
}