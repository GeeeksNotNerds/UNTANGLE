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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

public class Admin_Login extends AppCompatActivity implements View.OnClickListener {

    EditText email,password;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    ArrayList<String> mylist = new ArrayList<String>();
    private String[] mAdmin;
    int c;
    private boolean checker;
    private String TAG;
    private  String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);



        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        mAdmin=getResources().getStringArray(R.array.admin_uid);
        c=1;
       // mylist=getResources().getStringArray(R.array.admin_uid);
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
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful())
                {
                    String current_user_id=mAuth.getCurrentUser().getUid();
                    DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
                    UserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {


                            if(!dataSnapshot.hasChild(current_user_id))
                            {
                                //if(Password.equals("12345678")||Password.equals("1234567"))
                                if(Password.equals("12345678"))
                                {
                                    type="Admin";
                                   // c++;
                                    //mAdmin[c]=current_user_id;
                                }
                                else if(Password.equals("1234567"))
                                {
                                    type="SubAdmin";
                                }
                                else
                                {
                                    type="Club";
                                }
                                SendToSetupActivity();

                                String token;
                                String id=mAuth.getCurrentUser().getUid().toString();
                                String Mail=mAuth.getCurrentUser().getEmail().toString();
                                token= FirebaseInstanceId.getInstance().getToken();
                                //int pos1=Mail.indexOf('.');
                                int Apos=Mail.indexOf('@');
                                String userName=Mail.substring(0,Apos);
                                String first=Mail.substring(0,1);
                                first=first.toUpperCase();
                                userName=first+Mail.substring(1,Apos);
                                //int pos2=Mail.indexOf('@',pos1+1);
                                //String admissionNo=Mail.substring(Apos-8,Apos);
                                //int pos3=Mail.indexOf('.',pos2+1);
                                //String branch=Mail.substring(pos2+1,pos3);*/

                                HashMap user1 = new HashMap();
                                user1.put("username", userName);
                                //user1.put("department", branch);
                                user1.put("email", Mail);
                                //user1.put("designation", admissionNo);
                                user1.put("device_token",token);
                                user1.put("type",type);

                                UserRef.child(id).updateChildren(user1).addOnCompleteListener(new OnCompleteListener()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task task)
                                    {
                                        //progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(Admin_Login.this, "Setup your profile ...", Toast.LENGTH_SHORT).show();

                                        }

                                        else
                                        {
                                            Toast.makeText(Admin_Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                //SendToSetupActivity();
                            }
                            else if(dataSnapshot.child(current_user_id).hasChild("subCategory"))
                            {
                                Intent intent=new Intent(Admin_Login.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            if(Password.equals("123456"))
                            {
                                Intent intent=new Intent(Admin_Login.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            //else if()

                           /* else
                            {
                                Intent intent=new Intent(Admin_Login.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }*/

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });

                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                   // Intent intent=new Intent(Admin_Login.this,MainActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(intent);
                    //finish();

                }
                else
                    {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    private void SendToSetupActivity()
    {
        Intent intent=new Intent(Admin_Login.this, adminSetup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
