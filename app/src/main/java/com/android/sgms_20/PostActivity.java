package com.android.sgms_20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private static final String TAG ="";
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;
    private EditText PostDescription;
    private FloatingActionButton UpdatePostButton;
    private String[] mAdmin= new String[]{"AkX6MclvgrXpN8oOGI5v37dn7eb2"};
    String downloadUrl="";
    TextView title;
    int check=1;
    ImageView Media;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;
    RadioGroup rg_mode,rg_mode_opt,rg_cat,rg_cat_off,rg_cat_per,rg_cat_oth;
    CardView cv2,cv4,cv5,cv6;
    String UserInfo_show="",UsersRefid;
    String cat1,cat2;
    int Gall=8;
    StorageReference PostImageRef;
    String Mode,category,Sub_Category;
    private Uri resultUri=null;
    ImageView Image;

    private String description,checker="",myUrl;
    private Uri myUri;
    private StorageTask uploadTask;
    private String current_user_id,saveCurrentDate,saveCurrentTime,postRandomName;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        title=findViewById(R.id.select);
        cv2=findViewById(R.id.cv2);
        Media=findViewById(R.id.media);
        //cv4=findViewById(R.id.cv4);
       // cv5=findViewById(R.id.cv5);
        //cv6=findViewById(R.id.cv6);
        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        bottomNav.getMenu().findItem(R.id.nav_post).setChecked(true);

        rg_mode=findViewById(R.id.rg1);
        rg_mode_opt=findViewById(R.id.rg2);
        //rg_cat=findViewById(R.id.rg3);
        //rg_cat_off=findViewById(R.id.rg4);
        //rg_cat_per=findViewById(R.id.rg5);
        //rg_cat_oth=findViewById(R.id.rg6);
        Image=findViewById(R.id.ima);




        Media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallInt=new Intent();
                gallInt.setAction(Intent.ACTION_GET_CONTENT);
                gallInt.setType("image/*");
                startActivityForResult(gallInt,Gall);

            }
        });

        rg_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.post_public){

                         cv2.setVisibility(View.VISIBLE);
                         rg_mode_opt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                             @Override
                             public void onCheckedChanged(RadioGroup group, int checkedId) {
                                 if(checkedId==R.id.post_no){
                                     UserInfo_show="no";
                                     Mode="Public";
                                 }
                                 else if(checkedId==R.id.post_yes){
                                     UserInfo_show="yes";
                                     Mode="Public";
                                 }
                             }
                         });



                }else{

                    cv2.setVisibility(View.GONE);
                    Mode="Private";
                    UserInfo_show="yes";
                }
            }
        });



        Spinner spinner1=(Spinner)findViewById(R.id.spinner1);
        final Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(PostActivity.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.categories));
        final ArrayAdapter<String> adapter2=new ArrayAdapter<String>(PostActivity.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.sub1));
        final ArrayAdapter<String> adapter3=new ArrayAdapter<String>(PostActivity.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.sub2));
        final ArrayAdapter<String> adapter4=new ArrayAdapter<String>(PostActivity.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.sub3));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label=parent.getItemAtPosition(position).toString();
                cat1=label;

                if(label.equals("Official"))
                {
                    spinner2.setAdapter(adapter2);
                }
                else if(label.equals("Personal"))
                {
                    spinner2.setAdapter(adapter3);
                }
                else {
                    spinner2.setAdapter(adapter4);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat2=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PostImageRef=FirebaseStorage.getInstance().getReference();

        /*rg_cat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.post_official) {
                    cv4.setVisibility(View.VISIBLE);
                    cv5.setVisibility(View.GONE);
                    cv6.setVisibility(View.GONE);

                    rg_cat_off.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.post_official_academic) {
                                category = "officialGrievance";
                                Sub_Category = "academic";
                            } else if (checkedId == R.id.post_official_admission) {
                                category = "officialGrievance";
                                Sub_Category = "admission";

                            } else if (checkedId == R.id.post_official_finance) {
                                category = "officialGrievance";
                                Sub_Category = "finance";
                            }
                        }

                        ;

                    });
                }
                else if(checkedId==R.id.post_personal){
                    cv4.setVisibility(View.GONE);
                    cv5.setVisibility(View.VISIBLE);
                    cv6.setVisibility(View.GONE);

                    rg_cat_per.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.post_personal_health) {
                                category = "personalIssue";
                                Sub_Category = "health";
                            } else if (checkedId == R.id.post_personal_housing) {
                                category = "personalIssue";
                                Sub_Category = "housing";

                            } else if (checkedId == R.id.post_personal_rightviolation) {
                                category = "personalIssue";
                                Sub_Category = "rightViolation";
                            }
                        }

                        ;

                    });


                }
                else if(checkedId==R.id.post_other){
                    cv4.setVisibility(View.GONE);
                    cv5.setVisibility(View.GONE);
                    cv6.setVisibility(View.VISIBLE);


                    rg_cat_oth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.post_other_announcement) {
                                category = "other";
                                Sub_Category = "announcement";
                            } else if (checkedId == R.id.post_other_comp) {
                                category = "other";
                                Sub_Category = "competetion";

                            } else if (checkedId == R.id.post_other_intern) {
                                category = "other";
                                Sub_Category = "intern";
                            } else if (checkedId == R.id.post_other_placements) {
                                category = "other";
                                Sub_Category = "placements";
                            }
                        }

                        ;

                    });



                }

            }
        });*/




        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");



        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");




        int c=1;
        for(int i=0;i<1;i++){
            if(current_user_id.equals(mAdmin[i])){
                c=0;
                break;
            }
        }

        if(c==0){
            title.setText("Select Your Announcement Category");
        }


        PostDescription=(EditText)findViewById(R.id.post_description);
        UpdatePostButton=findViewById(R.id.update_post_button);
        loadingBar = new ProgressDialog(this);

       // mToolbar=(Toolbar)findViewById(R.id.update_post_page_toolbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setTitle("Update Post");
        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidatePostInfo();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gall && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            Log.d(TAG, "onActivityResult: CHOOSE IMAGE : OK >> " + imageuri);
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }



        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
           // progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "onActivityResult: CROP IMAGE");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                 resultUri = result.getUri();

                if(resultUri!=null){
                    Image.setVisibility(View.VISIBLE);
                    Image.setImageURI(resultUri);
                    PostDescription.setHint("Enter your Caption");

                }



                Log.d(TAG, "onActivityResult: CROP IMAGE : OK >> " + resultUri);

            } else {
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(PostActivity.this, "Error Occured!....Image Can't be cropped....try again!", Toast.LENGTH_SHORT).show();
            }
        }




    }

    private void ValidatePostInfo()
    {
        //edit text is not empty
        description=PostDescription.getText().toString();


       if(resultUri!=null){
           storingImageToFirebaseStorage();
       }



        if(TextUtils.isEmpty(description)&& resultUri==null)
        {
            Toast.makeText(this, "Post cannot be left empty..", Toast.LENGTH_SHORT).show();
        }
        else if(UserInfo_show.isEmpty()){
        Toast.makeText(this, "Please Select the mode of posting(public or private), and if the mode is public...select if you wish to post anonymously or no!", Toast.LENGTH_SHORT).show();
           }
        else
        {
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Please wait, while we are updating your new post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            saveCurrentTime = currentTime.format(calFordDate.getTime());

            postRandomName = saveCurrentDate + saveCurrentTime;

            SavingPostInformationToDatabase();

        }
    }

    private void storingImageToFirebaseStorage() {

        StorageReference filePath=PostImageRef.child("Post Images").child(resultUri.getLastPathSegment()+postRandomName+".jpg");





        filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl = uri.toString();
                        PostsRef.child(postRandomName+current_user_id).child("PostImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PostActivity.this, "Image Stored", Toast.LENGTH_SHORT).show();
                                    check=0;
                                   // progressBar.setVisibility(View.GONE);

                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(PostActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                   // progressBar.setVisibility(View.GONE);
                                }

                            }
                        });

                    }
                });

            }
        });



    }


    private void SavingPostInformationToDatabase()
    {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {



                        String userFullName = dataSnapshot.child("username").getValue().toString();
                        String userProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                        String userEmail=dataSnapshot.child("email").getValue().toString();


                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", description);
                    postsMap.put("mode", Mode);
                    postsMap.put("category", cat1);
                    postsMap.put("subCategory", cat2);
                    postsMap.put("profileImage", userProfileImage);
                    postsMap.put("username", userFullName);
                    postsMap.put("email",userEmail);
                    postsMap.put("showInformation",UserInfo_show);
                    postsMap.put("PostKey",postRandomName+current_user_id);
                    postsMap.put("status","Unresolved");


                   if(check==1){
                       postsMap.put("PostImage","null");
                   }



                    postsMap.put("likes","0");
                    PostsRef.child(postRandomName+current_user_id ).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        SendUserToMainActivity();
                                        Toast.makeText(PostActivity.this, "New Post is updated successfully.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(PostActivity.this, "Error Occured while updating your post.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);

    }

    private void SendUserToMainActivity()
    {

        Intent intent=new Intent(PostActivity.this,MainActivity.class);
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
                            Intent intent=new Intent(PostActivity.this,MainActivity.class);

                            startActivity(intent);
                            finish();

                            break;

                        case R.id.nav_profile:
                            Intent Pintent=new Intent(PostActivity.this,ProfileActivity.class);

                            startActivity(Pintent);
                            finish();

                            break;
                    }

                    return true;
                }
            };
}
