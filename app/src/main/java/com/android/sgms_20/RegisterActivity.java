package com.android.sgms_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText mail, password, password2;
    ProgressBar progressBar;
    String currUserId;
    String token;
    DatabaseReference userRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mail = findViewById(R.id.email);
        password = findViewById(R.id.password);

  //      mAuth = FirebaseAuth.getInstance();
        //fab = findViewById(R.id.fab_cam);
//        currUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        password2 = findViewById(R.id.confirm_password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.login_text).setOnClickListener(this);
    }

    private void RegisterUser()
    {
        String Mail = mail.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Password2 = password2.getText().toString().trim();



        if (Mail.isEmpty())
        {
            mail.setError("Email is required!");
            mail.requestFocus();
            return;
        }
        if (!(Mail.endsWith("iitism.ac.in") || Mail.endsWith("ism.ac.in"))) {
            mail.setError("Please enter College ID");
            mail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Mail).matches()) {
            mail.setError("Enter a valid Email address!");
            mail.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if (!Password.equals(Password2)) {
            password2.setError("Passwords do not match!");
            password2.requestFocus();
            return;
        }

        if (Password.length() < 6) {
            password.setError("Minimum length of Password is 6 !");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(Mail, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    /*token= FirebaseInstanceId.getInstance().getToken();
                                    int pos1=Mail.indexOf('.');
                                    String userName=Mail.substring(0,pos1);
                                    int pos2=Mail.indexOf('@',pos1+1);
                                    String admissionNo=Mail.substring(pos1+1,pos2);
                                    int pos3=Mail.indexOf('.',pos2+1);
                                    String branch=Mail.substring(pos2+1,pos3);

                                    HashMap user1 = new HashMap();
                                    user1.put("username", userName);
                                    user1.put("department", branch);
                                    user1.put("email", Mail);
                                    user1.put("admission_number", admissionNo);
                                    user1.put("device_token",token);

                                    userRef.child(currUserId).updateChildren(user1).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task)
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {

                                                //Toast.makeText(RegisterActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            } else
                                                {
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });*/

                                    Toast.makeText(RegisterActivity.this, "A verification link has been sent to your Email.Please verify your account", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();

                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "This Email has already been registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                RegisterUser();
                break;
            case R.id.login_text:

                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }
}


