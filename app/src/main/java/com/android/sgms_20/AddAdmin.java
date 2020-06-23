package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAdmin extends AppCompatActivity
{
    private EditText mEmail,mDesignation,mDepartment;
    private Button mAdd;
    private String email,designation,department,cat,sub;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    String currentUserId;
    private String[] mAdmin;
    String Mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");

        mEmail=findViewById(R.id.email_admin);
        mAdmin=getResources().getStringArray(R.array.admin_uid);
         Mail=mAuth.getCurrentUser().getEmail().toString();

        mAdd=findViewById(R.id.add_admin);

        //department=mDepartment.getText().toString().trim();
        //designation=mDesignation.getText().toString().trim();
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RegisterAdmin();

            }
        });




        BottomNavigationView bottomNavigAdmin=findViewById(R.id.bottom_navigation_admin);


        //if(currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))//if admin

            bottomNavigAdmin.setVisibility(View.VISIBLE);

            bottomNavigAdmin.setOnNavigationItemSelectedListener(navListner2);
            bottomNavigAdmin.getMenu().findItem(R.id.nav_add_admin).setChecked(true);


    }

    private void RegisterAdmin()
    {
        email=mEmail.getText().toString().trim();
        if(email.isEmpty())
        {
            mEmail.setError("Email is required!");
            mEmail.requestFocus();
            return;
        }
        mAuth.signOut();

        mAuth.createUserWithEmailAndPassword(email,"1234567").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    //add admin in list
                    Toast.makeText(AddAdmin.this, "Admin added successfully", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    mAuth.signInWithEmailAndPassword(Mail,"12345678").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            SendUserToMainActivity();
                        }
                    });
                }


            }
        });
    }

    private void SendUserToMainActivity()
    {
        startActivity(new Intent(AddAdmin.this,MainActivity.class));
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner2=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.nav_home_admin:
                            Intent intent4=new Intent(AddAdmin.this,MainActivity.class);
                            startActivity(intent4);
                            intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            break;
                        case R.id.nav_post_admin:
                            Intent intent=new Intent(AddAdmin.this,PostActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        case R.id.nav_profile_admin:
                            Intent Pintent=new Intent(AddAdmin.this,ProfileActivity.class);
                            Pintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent);
                            finish();
                            break;
                        case R.id.nav_star_admin:
                            Intent Pintent1=new Intent(AddAdmin.this,StarActivity.class);
                            Pintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent1);
                            finish();
                            break;
                        case R.id.nav_add_admin:
                            Intent Pintent2=new Intent(AddAdmin.this,AddAdmin.class);
                            Pintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent2);
                            finish();
                            break;

                    }

                    return true;
                }
            };
}
