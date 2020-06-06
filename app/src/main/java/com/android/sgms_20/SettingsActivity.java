package com.android.sgms_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;
    ProgressBar progressBar;
    private EditText userName,userDept,userEmail,userAdminNo;
    Button UpdateAccountSettingsButton;
    private CircleImageView userProfImage;
    private DatabaseReference SettingsuserRef;
    final static int Gallery_Pick = 1;
    private StorageReference UserProfileImageRef;
    private FirebaseAuth mAuth;
    String currentUserId;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        loadingBar=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        bottomNav.getMenu().findItem(R.id.nav_profile).setChecked(true);
        progressBar=findViewById(R.id.progress_bar);
        mToolbar=(Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }
        });


        currentUserId=mAuth.getCurrentUser().getUid();
        SettingsuserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");




        userName=(EditText)findViewById(R.id.settings_name);
        userEmail=findViewById(R.id.settings_email);
        userAdminNo=findViewById(R.id.settings_admin_no);
        userDept=(EditText)findViewById(R.id.settings_dept);


        if(currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){
            userAdminNo.setHint("Designation");
        }else if(!currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){
            userAdminNo.setHint("Admisiion Number");
        }



        userProfImage=findViewById(R.id.settings_pro_pic);
        UpdateAccountSettingsButton=(Button)findViewById(R.id.update_button);

        SettingsuserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {

                        if(currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")) {
                            String myProfileImage="";
                            if(dataSnapshot.child("ProfileImage").exists()) myProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();



                            String myUserName = dataSnapshot.child("username").getValue().toString();
                            String myDesignation=dataSnapshot.child("designation").getValue().toString();
                            String myDept = dataSnapshot.child("department").getValue().toString();
                            String myEmail = dataSnapshot.child("email").getValue().toString();

                            if(!myProfileImage.isEmpty()) {
                                Picasso.with(SettingsActivity.this)
                                        .load(myProfileImage)
                                        .placeholder(R.drawable.ic_account_circle_24px)
                                        .into(userProfImage);
                            }else{
                                userProfImage.setImageResource(R.drawable.profile);
                            }

                            userName.setText(myUserName);
                            userAdminNo.setText(myDesignation);
                            userDept.setText(myDept);
                            userEmail.setText(myEmail);
                        }else if(!currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){
                            String myProfileImage="";
                            if(dataSnapshot.child("ProfileImage").exists()) myProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();



                            String myUserName = dataSnapshot.child("username").getValue().toString();
                            String myAdminNo=dataSnapshot.child("admission_number").getValue().toString();
                            String myDept = dataSnapshot.child("department").getValue().toString();
                            String myEmail = dataSnapshot.child("email").getValue().toString();

                        //    Picasso.with(SettingsActivity.this)
                          //          .load(myProfileImage)
                            //        .placeholder(R.drawable.ic_account_circle_24px)
                              //      .into(userProfImage);

                            if(!myProfileImage.isEmpty()) {
                                Picasso.with(SettingsActivity.this)
                                        .load(myProfileImage)
                                        .placeholder(R.drawable.ic_account_circle_24px)
                                        .into(userProfImage);
                            }else{
                                userProfImage.setImageResource(R.drawable.profile);
                            }

                            userName.setText(myUserName);
                            userAdminNo.setText(myAdminNo);
                            userDept.setText(myDept);
                            userEmail.setText(myEmail);



                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        UpdateAccountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateAccountInfo();

            }
        });
        userProfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent galleryIntent = new Intent();
                //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                //galleryIntent.setType("image/*");
                //startActivityForResult(galleryIntent, Gallery_Pick);


                Intent gallint = new Intent();
                gallint.setType("image/*");
                gallint.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallint, "Select Profile Image"), Gallery_Pick);


            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            Uri imageuri = data.getData();

            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }

        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile image...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();


                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserId+ ".jpg");


                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                loadingBar.dismiss();
                                SettingsuserRef.child("ProfileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Image Stored", Toast.LENGTH_SHORT).show();


                                            progressBar.setVisibility(View.GONE);

                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(SettingsActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }
                                });

                            }
                        });

                    }
                });



                /*



                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(SettingsActivity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            SettingsuserRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Intent selfIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(SettingsActivity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SettingsActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });


                */
            }
            else
            {
                Toast.makeText(this, "Error Occured: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }



    private void ValidateAccountInfo() {
        if (currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){


            String username = userName.getText().toString();
            String userDesignation=userAdminNo.getText().toString();
        String userdept = userDept.getText().toString();
        String useremail = userEmail.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userdept)) {
            Toast.makeText(this, "Please write your department", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(useremail)) {
            Toast.makeText(this, "Please write your Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userDesignation)) {
            Toast.makeText(this, "Please write your Designation", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Profile");
            loadingBar.setMessage("Please wait, while we updating your profile ...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            UpdateAccountInfo(username, userdept, useremail,userDesignation);
        }
        } else if (!currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")) {


            String username = userName.getText().toString();
            String userDesignation=userAdminNo.getText().toString();
            String userdept = userDept.getText().toString();
            String useremail = userEmail.getText().toString();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userdept)) {
                Toast.makeText(this, "Please write your department", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(useremail)) {
                Toast.makeText(this, "Please write your Email", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userDesignation)) {
                Toast.makeText(this, "Please write your Admission Number", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile ...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();
                UpdateAccountInfo(username, userdept, useremail,userDesignation);
            }


        }

    }

    private void UpdateAccountInfo(String username, String userdept, String useremail,String userDesignation) {

        if(currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")) {

            MaterialDialog mDialog = new MaterialDialog.Builder(SettingsActivity.this)
                    .setTitle("Info")
                    .setMessage("Public posts will be visible to all,while the private posts will only be visible to you and the other admins ")
                    .setCancelable(false)
                    .setPositiveButton("Okay,Got it!", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which)
                        {
                            HashMap useMap = new HashMap();
                            useMap.put("username", username);
                            useMap.put("designation",userDesignation);
                            useMap.put("department", userdept);
                            useMap.put("email", useremail);
                            SettingsuserRef.updateChildren(useMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        loadingBar.dismiss();
                                        SendUserToMainActivity();
                                        Toast.makeText(SettingsActivity.this, "Account Settings Updated Successfully..", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Error Occured while update account setting info..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            dialogInterface.dismiss();
                        }


                    })
                    .setNegativeButton("Cancel", R.drawable.ic_arrow, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which)
                        {
                            dialogInterface.dismiss();

                        }
                    })
                    .build();

            // Show Dialog
            mDialog.show();




        }
        else if(!currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
        {
            MaterialDialog mDialog = new MaterialDialog.Builder(SettingsActivity.this)
                    .setTitle("Update Post..")
                    .setMessage("Are you sure you want update your details?")
                    .setCancelable(false)
                    .setPositiveButton("Yes,Update!", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which)
                        {
                            HashMap useMap = new HashMap();
                            useMap.put("username", username);
                            useMap.put("admission_number",userDesignation);
                            useMap.put("department", userdept);
                            useMap.put("email", useremail);
                            SettingsuserRef.updateChildren(useMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        loadingBar.dismiss();
                                        SendUserToMainActivity();
                                        Toast.makeText(SettingsActivity.this, "Account Settings Updated Successfully..", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Error Occured while update account setting info..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialogInterface.dismiss();
                        }


                    })
                    .setNegativeButton("Cancel", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                        }
                    })
                    .build();

            // Show Dialog
            mDialog.show();




        }
    }

    private void SendUserToMainActivity()
    {
        Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }
    private BottomNavigationView.OnNavigationItemSelectedListener
            navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            Intent intent=new Intent(SettingsActivity.this,MainActivity.class);

                            startActivity(intent);
                            finish();
                            return true;

                        case R.id.nav_post:
                            Intent Lintent=new Intent(SettingsActivity.this,PostActivity.class);

                            startActivity(Lintent);
                            finish();

                            return true;
                        case R.id.nav_star:
                            Intent Lintent1=new Intent(SettingsActivity.this,StarActivity.class);

                            startActivity(Lintent1);
                            finish();

                            return true;





                    }

                    return false;
                }
            };

}
