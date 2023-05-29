package com.example.servify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLogin extends AppCompatActivity {

    private TextView tv_login_movesignup;
    private EditText et_login_email, et_login_password;
    private Button bt_login_login;
    private ProgressDialog progressDialog;

    FirebaseAuth nAuth;
    FirebaseUser nUser;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

//        tv_login_movesignup=findViewById(R.id.tv_login_movesignup);
        bt_login_login=findViewById(R.id.bt_login_login);
        et_login_email=findViewById(R.id.et_login_email);
        et_login_password=findViewById(R.id.et_login_password);
        progressDialog = new ProgressDialog(this);
        nAuth=FirebaseAuth.getInstance();
        nUser=nAuth.getCurrentUser();

//        tv_login_movesignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AdminLogin.this, AdminRegister.class));
//            }
//        });

        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforLog();
            }
        });


    }

    private void PerforLog() {

        String email = et_login_email.getText().toString();
        String password = et_login_password.getText().toString();

        if(!email.matches(emailPattern))
        {
            et_login_email.setError("Enter Correct Email!");
        }

        else if(password.isEmpty() || password.length()<8)
        {
            et_login_password.setError("Enter Correct Password!");
        }

        else
        {
            progressDialog.setMessage("Please wait while login...");
            progressDialog.setTitle("Login...");
            progressDialog.show();

            nAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(AdminLogin.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(AdminLogin.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


    }
}

    private void sendUserToNextActivity() {
        startActivity(new Intent(AdminLogin.this, AdminMainActivity.class));
    }

    }