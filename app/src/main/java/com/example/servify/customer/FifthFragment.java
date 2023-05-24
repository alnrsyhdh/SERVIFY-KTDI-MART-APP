package com.example.servify.customer;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servify.AdminLogin;
import com.example.servify.AdminMainActivity;
import com.example.servify.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.servify.R;


public class FifthFragment extends Fragment {

    private TextView tv_login_movesignup;
    private EditText et_login_email, et_login_password;
    private Button bt_login_login;
    private ProgressDialog progressDialog;

    FirebaseAuth nAuth;
    FirebaseUser nUser;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public FifthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fifth, container, false);

        bt_login_login=rootView.findViewById(R.id.bt_login_login);
        et_login_email=rootView.findViewById(R.id.et_login_email);
        et_login_password=rootView.findViewById(R.id.et_login_password);
        progressDialog = new ProgressDialog(getActivity());
        nAuth=FirebaseAuth.getInstance();
        nUser=nAuth.getCurrentUser();

        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforLog();
            }
        });
        return rootView;
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
                        Toast.makeText(getActivity(), "Login Successfull", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getActivity(), AdminMainActivity.class);
        startActivity(intent);
    }
}