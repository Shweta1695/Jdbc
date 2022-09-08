package com.offpen.sp_pen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.offpen.sp_pen.model.RegisterMainModel;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterMain";
    AppCompatButton org_acc, ind_acc;
    Bundle b = new Bundle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initViews();
        RegisterMainModel mainModel = new RegisterMainModel();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //org_acc = findViewById(R.id.org_acc);
        org_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RegisterActivity.this, "Organization account clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, RegisterActivityMain.class);
                b.putString("Clicked_Acc", "Org");
                mainModel.setAcc_type("Org");
                editor.putString(TAG, mainModel.getAcc_type());
                editor.commit();
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        ind_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Individual account clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, RegisterActivityMain.class);
                b.putString("Clicked_Acc", "Individual");
                mainModel.setAcc_type("Individual");
                editor.putString(TAG, mainModel.getAcc_type());
                editor.commit();
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    private void initViews() {
        org_acc = findViewById(R.id.org_acc);
        ind_acc = findViewById(R.id.ind_acc);
    }

}