package com.android.sgms_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText email,password;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private boolean checker;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        findViewById(R.id.login_button).setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_bar);
        findViewById(R.id.register_account_link).setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


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
        if(Password.length()<6 ){
            password.setError("Minimum length of Password is 6 !");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);



        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    VerifyWithEmail();


                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.register_account_link:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.login_button:
                UserLogin();
                break;
        }
    }


    private void VerifyWithEmail(){
        FirebaseUser user = mAuth.getCurrentUser();
        checker=user.isEmailVerified();
        if (checker) {
            finish();
            Toast.makeText(this,"Account is verified.",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        }else{
            Toast.makeText(this,"Verify your account first",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }
}
