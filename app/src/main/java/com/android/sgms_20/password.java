package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class password extends AppCompatActivity {

    EditText Email;
    Button enter;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Email=findViewById(R.id.password_email);
        enter=findViewById(R.id.enter_button);
        mAuth=FirebaseAuth.getInstance();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(password.this, "Enter your Email ID", Toast.LENGTH_SHORT).show();

                }else{
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(password.this,"A Link has been sent to your Email ID to change your password",Toast.LENGTH_LONG).show();

                                Intent intent=new Intent(password.this,LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }else{
                                Toast.makeText(password.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });

    }
}
