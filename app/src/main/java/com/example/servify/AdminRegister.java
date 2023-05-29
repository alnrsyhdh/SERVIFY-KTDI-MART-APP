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

import com.example.servify.admin.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class AdminRegister extends AppCompatActivity {

    private TextView tv_register_movelogin;
    private EditText et_resgister_email, et_register_pwd, et_register_role;
    private Button bt_register_submit;
    ProgressDialog progressDialog;

    FirebaseAuth nAuth;
    FirebaseUser nUser;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        tv_register_movelogin=findViewById(R.id.tv_register_movelogin);
        et_resgister_email=findViewById(R.id.et_resgister_email);
        et_register_pwd=findViewById(R.id.et_register_pwd);
        bt_register_submit=findViewById(R.id.bt_register_submit);
        et_register_role=findViewById(R.id.et_register_role);
        progressDialog = new ProgressDialog(this);
        nAuth=FirebaseAuth.getInstance();
        nUser=nAuth.getCurrentUser();

       tv_register_movelogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(AdminRegister.this, AdminLogin.class));
           }
       });

       bt_register_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               PerforAuth();
           }
       });

    }

    private void PerforAuth() {
        String email = et_resgister_email.getText().toString();
        String password = et_register_pwd.getText().toString();
        String role = et_register_role.getText().toString();

        if(!email.matches(emailPattern))
        {
            et_resgister_email.setError("Enter Correct Email!");
        }

        else if(password.isEmpty() || password.length()<8)
        {
            et_register_pwd.setError("Enter Proper Password!");
        }

        else if(role.isEmpty())
        {
            et_register_role.setError("What's your role?");
        }
        else
        {
            progressDialog.setMessage("Please wait while registeration...");
            progressDialog.setTitle("Registeration...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            nAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        FirebaseDatabase.getInstance().getReference("servify/admin/"+nAuth.getCurrentUser().getUid()).setValue(new Admin(email,password,role,""));
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(AdminRegister.this, "Registeration Succesfull", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(AdminRegister.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }




    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(AdminRegister.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
