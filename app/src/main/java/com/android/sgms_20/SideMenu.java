package com.android.sgms_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SideMenu extends AppCompatActivity {
    private TextView userName,userFullName;
    private ImageView thumbImage;
    private GoogleApiClient mGoogleApiClient;
    private Button UpdateProfile;
    private TextDrawable mDrawableBuilder;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private TextView Profile,Support,Logout;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int) (height*.33));

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.RIGHT|Gravity.TOP;

        getWindow().setAttributes(params);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        thumbImage=(ImageView)findViewById(R.id.thumb);
        userName=(TextView) findViewById(R.id.user);
        userFullName=(TextView)findViewById(R.id.name);
        UpdateProfile=(Button)findViewById(R.id.update);
        Profile=(TextView)findViewById(R.id.profile_page);
        Support=(TextView)findViewById(R.id.support_page);
        Logout=(TextView)findViewById(R.id.logout);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {

                    String myUserName=dataSnapshot.child("username").getValue().toString();
                    String myProfileName=dataSnapshot.child("fullname").getValue().toString();
                    char letter=myProfileName.charAt(0);


                    mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(letter),R.color.white );
                    thumbImage.setImageDrawable(mDrawableBuilder);



                    userName.setText("@"+myUserName);
                    userFullName.setText(myProfileName);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SideMenu.this,SettingsActivity.class));
                finish();
            }
        });
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SideMenu.this,ProfileActivity.class));
                finish();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                sendUserToLoginActivity();

            }
        });
    }

    private void sendUserToLoginActivity() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }


}

