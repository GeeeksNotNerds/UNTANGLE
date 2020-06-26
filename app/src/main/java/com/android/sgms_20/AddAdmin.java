package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

public class AddAdmin extends AppCompatActivity
{
    private EditText mEmail,mDesignation,mDepartment;
    private Button mAdd;
    private String email,designation,department,cat,sub;
    ProgressBar mProgressBar;
    private String username;
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
         mProgressBar=findViewById(R.id.progress_bar);

        mAdd=findViewById(R.id.add_admin);

        //department=mDepartment.getText().toString().trim();
        //designation=mDesignation.getText().toString().trim();
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mProgressBar.setVisibility(View.VISIBLE);
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
      //  mAuth.signOut();

        mAuth.createUserWithEmailAndPassword(email,"1234567").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    mProgressBar.setVisibility(View.GONE);
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
                else
                {
                    mProgressBar.setVisibility(View.GONE);
                    //Toast.makeText(AddAdmin.this, "Try Again...something went wrong!", Toast.LENGTH_SHORT).show();
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(AddAdmin.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                    }

                   /* mAuth.signInWithEmailAndPassword(Mail,"12345678").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            Toast.makeText(AddAdmin.this, "enter valid and unregistered email.", Toast.LENGTH_SHORT).show();

                        }
                    });*/



                }


            }
        });
    }

    private void SendUserToMainActivity()
    {
        int p=email.indexOf('@');
        username=email.substring(0,p);

        MaterialDialog mDialog = new MaterialDialog.Builder(AddAdmin.this)
                .setTitle("Notify the Admin ")
                .setMessage("Do you want to notify "+username+" that he/she has been added as an admin ? ")
                .setCancelable(false)
                .setPositiveButton("Yes,Send!", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which)
                    {
                        String senderMail=Mail;
                        String toMail=email;
                        String subject="Untangle : Admin Confirmation";
                        String body="You have been added as an admin by " +Mail+". Please login and setup your details.";

                        //String mailto = "mailto:"+toMail +"&subject=" + Uri.encode(subject) +"&body=" + Uri.encode(body);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        //intent.setData(Uri.parse(mailto));
                        //intent.setData(Uri.parse())
                        intent.putExtra(Intent.EXTRA_EMAIL,toMail);
                        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                        intent.putExtra(Intent.EXTRA_TEXT,body);
                       // intent.setType("plain/text");
                        intent.setType("message/rfc822");
                        startActivity(Intent.createChooser(intent,"Choose an email client"));
                        finish();
                        //startActivity(new Intent(AddAdmin.this,MainActivity.class));
                        //finish();
                        dialogInterface.dismiss();
                    }


                })
                .setNegativeButton("No,Leave it!", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which)
                    {
                        startActivity(new Intent(AddAdmin.this,MainActivity.class));
                        finish();
                        dialogInterface.dismiss();

                    }
                })
                .build();

        // Show Dialog
        mDialog.show();


     //   startActivity(new Intent(AddAdmin.this,MainActivity.class));
      //  finish();
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
