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
import android.widget.LinearLayout;
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

    private TextView userName,userEmail,userBranch,admin_no,categ,subCateg;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    Button edit_profile;
    private LinearLayout l1,l2;
    private Toolbar mToolbar;
    private String category,subCategory;
    TextDrawable mDrawableBuilder;
    DatabaseReference MyPostRef;

    private static String TAG;
    private String type;

    ImageView pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pro=(ImageView)findViewById(R.id.thumbnail1);
        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        categ=findViewById(R.id.category);
        subCateg=findViewById(R.id.sub_category);

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
        BottomNavigationView bottomNavigAdmin=findViewById(R.id.bottom_navigation_admin);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                type=dataSnapshot.child("type").getValue().toString();

        if(type.equals("Admin"))//if admin
        {
            bottomNavigAdmin.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.GONE);
            bottomNavigAdmin.setOnNavigationItemSelectedListener(navListner2);
            bottomNavigAdmin.getMenu().findItem(R.id.nav_profile_admin).setChecked(true);
        }
        else
        {
            bottomNav.setVisibility(View.VISIBLE);
            bottomNavigAdmin.setVisibility(View.GONE);
            bottomNav.setOnNavigationItemSelectedListener(navListner);
            bottomNav.getMenu().findItem(R.id.nav_profile).setChecked(true);
        }


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



                        if (type.equals("Admin")||type.equals("Club")||type.equals("SubAdmin"))
                        {
                            if(type.equals("Admin")||type.equals("SubAdmin"))
                            {

                                l1.setVisibility(View.VISIBLE);
                                l2.setVisibility(View.VISIBLE);
                                category=dataSnapshot.child("category").getValue().toString();
                                subCategory=dataSnapshot.child("subCategory").getValue().toString();
                                categ.setText("Complaint Category : "+category);
                                subCateg.setText("Sub-Category : "+subCategory);
                                edit_profile.setVisibility(View.VISIBLE);

                            }
                            //String myProfileImage="";
                             //if(dataSnapshot.child("ProfileImage").exists()) myProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();

                            String myUserName = dataSnapshot.child("username").getValue().toString();
                            String Designation = dataSnapshot.child("designation").getValue().toString();
                            String myBranch = dataSnapshot.child("department").getValue().toString();
                            String myEmail = dataSnapshot.child("email").getValue().toString();

                            /*if(!myProfileImage.isEmpty()) {
                                Picasso.with(ProfileActivity.this)
                                        .load(myProfileImage)
                                        .placeholder(R.drawable.ic_account_circle_24px)
                                        .into(userProfileImage);
                            }else
                                {
                                userProfileImage.setImageResource(R.drawable.profile);
                            }*/


                            userName.setText("UserName : " + myUserName);
                            admin_no.setText("Designation : " + Designation);

                            userBranch.setText("Department : " + myBranch);
                            userEmail.setText("Email : " + myEmail);

                            String myProfileName = dataSnapshot.child("username").getValue().toString();
                            char letter = myProfileName.charAt(0);
                            letter = Character.toUpperCase(letter);


                            mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(letter), R.color.colorAccent);

                            //pro.setImageDrawable(mDrawableBuilder);

                        } else if (!type.equals("Admin")&&(!type.equals("SubAdmin"))) {
                            //String myProfileImage="";
                              //if(dataSnapshot.child("ProfileImage").exists())  myProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();




                            String myUserName = dataSnapshot.child("username").getValue().toString();
                            String Adminno = dataSnapshot.child("admission_number").getValue().toString();
                            String myBranch = dataSnapshot.child("department").getValue().toString();
                            String myEmail = dataSnapshot.child("email").getValue().toString();

                            /*if(!myProfileImage.isEmpty()) {
                                Picasso.with(ProfileActivity.this)
                                        .load(myProfileImage)
                                        .placeholder(R.drawable.ic_account_circle_24px)
                                        .into(userProfileImage);
                            }
                            else
                                {
                                userProfileImage.setImageResource(R.drawable.profile);
                            }*/


                            userName.setText("UserName : " + myUserName);
                            admin_no.setText("Admission Number : " + Adminno);

                            userBranch.setText("Department : " + myBranch);
                            userEmail.setText("Email : " + myEmail);

                            /*String myProfileName = dataSnapshot.child("username").getValue().toString();
                            char letter = myProfileName.charAt(0);
                            letter = Character.toUpperCase(letter);


                            mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(letter), R.color.colorAccent);

                            pro.setImageDrawable(mDrawableBuilder);*/

                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            return true;

                        case R.id.nav_post:
                            Intent Lintent=new Intent(ProfileActivity.this,PostActivity.class);
                            Lintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(Lintent);
                            finish();

                            return true;
                        case R.id.nav_star:
                            Intent Lintent1=new Intent(ProfileActivity.this,StarActivity.class);
                            Lintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(Lintent1);
                            finish();

                            return true;





                    }

                    return false;
                }
            };
    private BottomNavigationView.OnNavigationItemSelectedListener navListner2=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.nav_home_admin:
                            Intent intent4=new Intent(ProfileActivity.this,MainActivity.class);
                            startActivity(intent4);
                            intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            break;
                        case R.id.nav_post_admin:
                            Intent intent=new Intent(ProfileActivity.this,PostActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        case R.id.nav_profile_admin:
                            Intent Pintent=new Intent(ProfileActivity.this,ProfileActivity.class);
                            Pintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent);
                            finish();
                            break;
                        case R.id.nav_star_admin:
                            Intent Pintent1=new Intent(ProfileActivity.this,StarActivity.class);
                            Pintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent1);
                            finish();
                            break;
                        case R.id.nav_add_admin:
                            Intent Pintent2=new Intent(ProfileActivity.this,AddAdmin.class);
                            Pintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent2);
                            finish();
                            break;

                    }

                    return true;
                }
            };


}

