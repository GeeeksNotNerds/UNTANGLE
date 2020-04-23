package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TextView userName,userEmail,userBranch;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    Button edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        edit_profile=findViewById(R.id.edit_button);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userName=(TextView) findViewById(R.id.name);

        userBranch=(TextView)findViewById(R.id.dept);
        userEmail=(TextView)findViewById(R.id.email);
        userProfileImage= findViewById(R.id.pro_pic);

        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));


            }
        });

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String myProfileImage=dataSnapshot.child("ProfileImage").getValue().toString();
                    String myUserName=dataSnapshot.child("username").getValue().toString();

                    String myBranch=dataSnapshot.child("department").getValue().toString();
                    String myEmail=dataSnapshot.child("email").getValue().toString();

                    Picasso.get()
                            .load(myProfileImage)
                            .placeholder(R.drawable.ic_account_circle_24px)
                            .into(userProfileImage);


                    userName.setText("UserName :"+myUserName);

                    userBranch.setText("Department : "+myBranch);
                    userEmail.setText("Email : "+myEmail);

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
                            startActivity(new Intent(ProfileActivity.this,MainActivity.class));

                            break;
                        case R.id.nav_post:
                            startActivity(new Intent(ProfileActivity.this,PostActivity.class));

                            break;
                        case R.id.nav_profile:
                            startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));

                            break;
                    }

                    return true;
                }
            };
}

