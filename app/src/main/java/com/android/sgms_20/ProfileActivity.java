package com.android.sgms_20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName,userFullName,userBranch,userAdmission;
    private ImageView userProfileImage;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userName=(TextView) findViewById(R.id.my_user_name);
        userFullName=(TextView)findViewById(R.id.my_profile_full_name);
        userBranch=(TextView)findViewById(R.id.my_branch);
        userAdmission=(TextView)findViewById(R.id.my_admission);
        userProfileImage=(ImageView) findViewById(R.id.my_profile_pic);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String myProfileImage=dataSnapshot.child("ProfileImage").getValue().toString();
                    String myUserName=dataSnapshot.child("email").getValue().toString();
                    String myProfileName=dataSnapshot.child("username").getValue().toString();
                    String myBranch=dataSnapshot.child("department").getValue().toString();
                    //String myAdmissionNo=dataSnapshot.child("admissionNo").getValue().toString();

                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userName.setText("@"+myUserName);
                    userFullName.setText(myProfileName);
                    userBranch.setText("Branch : "+myBranch);
                    //userAdmission.setText("Admission No. : "+myAdmissionNo);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

