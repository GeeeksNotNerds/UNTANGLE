package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Admin_Login extends AppCompatActivity implements View.OnClickListener {

    EditText email,password;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private boolean checker;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);


        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        findViewById(R.id.login_button).setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {


        switch(v.getId()){

            case R.id.login_button:
                UserLogin();
                break;
        }
    }


    private void UserLogin(){
        String Email=email.getText().toString().trim();
        String Password=password.getText().toString().trim();
        if(Email.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter a valid Email address!");
            email.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);



        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Admin_Login.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
