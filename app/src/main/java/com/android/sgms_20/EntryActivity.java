package com.android.sgms_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class EntryActivity extends AppCompatActivity {
    Button mGot;
    String user_id;
    TextView text;
    CircleImageView image;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.90),(int) (height*.28));
        mGot=findViewById(R.id.got);

        user_id=getIntent().getExtras().get("ID").toString();
        mGot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendUserToMainActivity();
            }
        });
        text=findViewById(R.id.click);
        image=findViewById(R.id.enIcon);

        usersRef=FirebaseDatabase.getInstance().getReference().child("Users");

        usersRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("type").getValue().toString()).equals("Student") || (dataSnapshot.child("type").getValue().toString()).equals("Club")){

                    image.setVisibility(View.GONE);
                    text.setText("Switch on the notification button in the profile activiy to receive notifications for admin and club announcements");


                }else{

                    image.setVisibility(View.VISIBLE);
                    text.setText("Click this in the posts to check the credential of students");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void SendUserToMainActivity()
    {
        //Intent myIntent2 = new Intent(getApplicationContext(), EntryActivity.class);
        EntryActivity.this.finish();
    }
}