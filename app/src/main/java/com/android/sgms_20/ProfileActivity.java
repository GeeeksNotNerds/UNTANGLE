package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName,userEmail,userBranch,admin_no;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    Button edit_profile;
    private Toolbar mToolbar;
    TextDrawable mDrawableBuilder;
    DatabaseReference MyPostRef;

    private static String TAG;

    ImageView pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pro=(ImageView)findViewById(R.id.thumbnail1);


        edit_profile=findViewById(R.id.edit_button);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        Log.d(TAG, "onCreate: "+currentUserId);
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userName=(TextView) findViewById(R.id.name);
        admin_no=findViewById(R.id.admin_no);
        userBranch=(TextView)findViewById(R.id.dept);
        userEmail=(TextView)findViewById(R.id.email);
        userProfileImage= findViewById(R.id.pro_pic);

        mToolbar=(Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);












        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        bottomNav.getMenu().findItem(R.id.nav_profile).setChecked(true);


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProfileActivity.this,SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }
        });

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {



                        if (currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")) {

                            String myProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                            String myUserName = dataSnapshot.child("username").getValue().toString();
                            String Designation = dataSnapshot.child("designation").getValue().toString();
                            String myBranch = dataSnapshot.child("department").getValue().toString();
                            String myEmail = dataSnapshot.child("email").getValue().toString();

                            Picasso.with(ProfileActivity.this)
                                    .load(myProfileImage)
                                    .placeholder(R.drawable.ic_account_circle_24px)
                                    .into(userProfileImage);


                            userName.setText("UserName :" + myUserName);
                            admin_no.setText("Designation :" + Designation);

                            userBranch.setText("Department : " + myBranch);
                            userEmail.setText("Email : " + myEmail);

                            String myProfileName = dataSnapshot.child("username").getValue().toString();
                            char letter = myProfileName.charAt(0);
                            letter = Character.toUpperCase(letter);


                            mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(letter), R.color.colorAccent);

                            pro.setImageDrawable(mDrawableBuilder);

                        } else if (!currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")) {

                            String myProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                            String myUserName = dataSnapshot.child("username").getValue().toString();
                            String Adminno = dataSnapshot.child("admission_number").getValue().toString();
                            String myBranch = dataSnapshot.child("department").getValue().toString();
                            String myEmail = dataSnapshot.child("email").getValue().toString();

                            Picasso.with(ProfileActivity.this)
                                    .load(myProfileImage)
                                    .placeholder(R.drawable.ic_account_circle_24px)
                                    .into(userProfileImage);


                            userName.setText("UserName :" + myUserName);
                            admin_no.setText("Admission Number :" + Adminno);

                            userBranch.setText("Department : " + myBranch);
                            userEmail.setText("Email : " + myEmail);

                            String myProfileName = dataSnapshot.child("username").getValue().toString();
                            char letter = myProfileName.charAt(0);
                            letter = Character.toUpperCase(letter);


                            mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(letter), R.color.colorAccent);

                            pro.setImageDrawable(mDrawableBuilder);


                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener
            navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            Intent intent=new Intent(ProfileActivity.this,MainActivity.class);

                            startActivity(intent);
                            finish();
                            return true;

                        case R.id.nav_post:
                            Intent Lintent=new Intent(ProfileActivity.this,PostActivity.class);

                            startActivity(Lintent);
                            finish();

                            return true;





                    }

                    return false;
                }
            };


}

