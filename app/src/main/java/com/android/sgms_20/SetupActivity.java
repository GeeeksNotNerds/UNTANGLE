package com.android.sgms_20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.net.Uri;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;

    EditText name, dept, email, admin_no;
    CircleImageView pro_pic;
    Button save;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    String currUserId,token;
    ProgressBar progressBar;
    FloatingActionButton fab;
    private String TAG,image="";
    private String[] mAdmin=new String[]{"AkX6MclvgrXpN8oOGI5v37dn7eb2"};;
    private StorageReference proPicRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        name = findViewById(R.id.name);
        dept = findViewById(R.id.dept);
        admin_no = findViewById(R.id.admin_no);
        mAdmin=getResources().getStringArray(R.array.admin_uid);

        email = findViewById(R.id.email);

        pro_pic = findViewById(R.id.pro_pic);
        progressBar = findViewById(R.id.progress_bar);
        save = findViewById(R.id.save_button);
        mAuth = FirebaseAuth.getInstance();
        fab = findViewById(R.id.fab_cam);
        currUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserId);
        proPicRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        int c=0;
        for(int i=0;i<1;i++){
        if (currUserId.equals(mAdmin[i])) {
           c=1;
           break;
        } }
        if (c==1) {
            admin_no.setHint("Designation");
            c=0;
        }
        else {
            admin_no.setHint("Admission Number");
            c=0;
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountInfo();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallint = new Intent();
                gallint.setType("image/*");
                gallint.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallint, "Select Profile Image"), CHOOSE_IMAGE);


            }
        });


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("ProfileImage")) {
                         image = dataSnapshot.child("ProfileImage").getValue().toString();
                        Log.d(TAG, "onActivityResult: CHOOSE IMAGE : OK >> " + image);

                        Picasso.with(SetupActivity.this)
                                .load(image)
                                .placeholder(R.drawable.ic_account_circle_24px)
                                .into(pro_pic);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            Log.d(TAG, "onActivityResult: CHOOSE IMAGE : OK >> " + imageuri);
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "onActivityResult: CROP IMAGE");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                Log.d(TAG, "onActivityResult: CROP IMAGE : OK >> " + resultUri);

                final StorageReference filePath = proPicRef.child(currUserId + ".jpg");


                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                userRef.child("ProfileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SetupActivity.this, "Image Stored", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);

                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(SetupActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }
                                });

                            }
                        });

                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SetupActivity.this, "Error Occured!....Image Can't be cropped....try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

                /*


                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(CreateProfile.this,"Profile Image has been successfully stored to Firebase storage!",Toast.LENGTH_SHORT).show();
                        final String downloadUrl=task.getResult().getStorage().getDownloadUrl().toString();
                        userRef.child("ProfileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);

                                if(task.isSuccessful()){
                                    Toast.makeText(CreateProfile.this,"Image Successfully stored to the database!",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    String message=task.getException().getMessage();
                                    Toast.makeText(CreateProfile.this,message,Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });
                */


    private void SaveAccountInfo() {

        if (!image.isEmpty()) {


            int l = 0;
            for (int i = 0; i < 1; i++) {
                if (currUserId.equals(mAdmin[i])) {
                    l = 1;
                    break;
                }
            }


            if (l == 0) {

                String Name = name.getText().toString();
                String Dept = dept.getText().toString();
                token= FirebaseInstanceId.getInstance().getToken();


                String Email = email.getText().toString();
                String AdmisiionNo = admin_no.getText().toString();

                if (Name.isEmpty()) {
                    name.setError("Enter Name!");
                    name.requestFocus();
                    return;
                }
                if (Dept.isEmpty()) {
                    dept.setError("Enter Department!");
                    dept.requestFocus();
                    return;
                }
                if (Email.isEmpty()) {
                    email.setError("Enter Email!");
                    email.requestFocus();
                    return;
                }
                if (AdmisiionNo.isEmpty()) {
                    email.setError("Enter Admission Number!");
                    email.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    HashMap user = new HashMap();
                    user.put("username", Name);
                    user.put("star",null);
                    user.put("department", Dept);
                    user.put("email", Email);
                    user.put("admission_number", AdmisiionNo);
                    user.put("device_token",token);

                    userRef.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                Toast.makeText(SetupActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(SetupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            } else if (l == 1) {
                l = 0;

                String Name = name.getText().toString();
                String Dept = dept.getText().toString();
                token= FirebaseInstanceId.getInstance().getToken();

                String Email = email.getText().toString();
                String Designation = admin_no.getText().toString();

                if (Name.isEmpty()) {
                    name.setError("Enter Name!");
                    name.requestFocus();
                    return;
                }
                if (Dept.isEmpty()) {
                    dept.setError("Enter Department!");
                    dept.requestFocus();
                    return;
                }
                if (Email.isEmpty()) {
                    email.setError("Enter Email!");
                    email.requestFocus();
                    return;
                }
                if (Designation.isEmpty()) {
                    email.setError("Enter your Designation!");
                    email.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    HashMap user = new HashMap();
                    user.put("username", Name);
                    user.put("department", Dept);
                    user.put("email", Email);
                    user.put("designation", Designation);
                    user.put("device_token",token);

                    userRef.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                Toast.makeText(SetupActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(SetupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        }else{

            Toast.makeText(SetupActivity.this,"Please Select your profile image ..",Toast.LENGTH_LONG).show();

        }
    }



}

