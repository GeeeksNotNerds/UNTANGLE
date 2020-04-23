package com.android.sgms_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;
    ProgressBar progressBar;
    private EditText userName,userDept,userEmail;
    Button UpdateAccountSettingsButton;
    private ImageView userProfImage;
    private DatabaseReference SettingsuserRef;
    final static int Gallery_Pick = 1;
    private StorageReference UserProfileImageRef;
    private FirebaseAuth mAuth;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadingBar=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_bar);

        currentUserId=mAuth.getCurrentUser().getUid();
        SettingsuserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");






        userName=(EditText)findViewById(R.id.settings_name);
        userEmail=findViewById(R.id.settings_email);

        userDept=(EditText)findViewById(R.id.settings_dept);

        userProfImage=(ImageView)findViewById(R.id.settings_pro_pic);
        UpdateAccountSettingsButton=(Button)findViewById(R.id.update_button);

        SettingsuserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String myProfileImage=dataSnapshot.child("ProfileImage").getValue().toString();
                        String myUserName=dataSnapshot.child("username").getValue().toString();

                        String myDept=dataSnapshot.child("department").getValue().toString();
                        String myEmail=dataSnapshot.child("email").getValue().toString();

                        Picasso.get()
                                .load(myProfileImage)
                                .placeholder(R.drawable.ic_account_circle_24px)
                                .into(userProfImage);





                        userName.setText(myUserName);

                        userDept.setText(myDept);
                        userEmail.setText(myEmail);
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
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
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



    private void ValidateAccountInfo()
    {
        String username=userName.getText().toString();

        String userdept= userDept.getText().toString();
        String useremail=userEmail.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(userdept))
        {
            Toast.makeText(this, "Please write your department", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(useremail))
        {
            Toast.makeText(this, "Please write your Email", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Profile Image");
            loadingBar.setMessage("Please wait, while we updating your profile image...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            UpdateAccountInfo(username,userdept,useremail);
        }

    }

    private void UpdateAccountInfo(String username, String userdept, String useremail) {

        HashMap useMap= new HashMap();
        useMap.put("username",username);

        useMap.put("department",userdept);
        useMap.put("email",useremail);
        SettingsuserRef.updateChildren(useMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    loadingBar.dismiss();
                    SendUserToMainActivity();
                    Toast.makeText(SettingsActivity.this, "Account Settings Updated Successfully..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(SettingsActivity.this, "Error Occured while update account setting info..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent intent=new Intent(SettingsActivity.this,ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }

}
