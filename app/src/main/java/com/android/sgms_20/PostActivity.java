package com.android.sgms_20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emredavarci.noty.Noty;
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
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private static final String TAG ="";
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;
    private ProgressBar progressBar;
    private EditText PostDescription;
    private ImageButton UpdatePostButton;
    private String[] mAdmin= new String[]{"AkX6MclvgrXpN8oOGI5v37dn7eb2"};
    String downloadUrl="",downloadUrlp="";
    Uri fileUri=null;
    TextView title;
    int check=1,check1=1;
    private TextView mLoading;
    ImageView Media;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;
    RadioGroup rg_mode,rg_mode_opt,rg_cat,rg_cat_off,rg_cat_per,rg_cat_oth;
    CardView cv2,cv4,cv5,cv6,cv;
    String UserInfo_show="",UsersRefid;
    String cat1,cat2;
    int Gall=8;
    StorageReference PostImageRef;
    String Mode,category,Sub_Category;
    private Uri resultUri=null;
    ImageView Image;

    private String Checker="";

    TextView mSelect;
    Spinner mSpin;

    private RelativeLayout r;
    private ImageButton information;
    private String type;
    private String description,checker="",myUrl;
    private Uri myUri;
    private StorageTask uploadTask;
    private String current_user_id,saveCurrentDate,saveCurrentTime,postRandomName;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        cv=findViewById(R.id.cv);



        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        title=findViewById(R.id.select);
        mLoading=(TextView)findViewById(R.id.loading);
        cv2=findViewById(R.id.cv2);
        mSelect=findViewById(R.id.select);
        mSpin=findViewById(R.id.spinner1);
        Media=findViewById(R.id.media);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar1);

        //cv4=findViewById(R.id.cv4);
        // cv5=findViewById(R.id.cv5);
        //cv6=findViewById(R.id.cv6);
        //getSupportActionBar().hide();

        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        BottomNavigationView bottomNavigAdmin=findViewById(R.id.bottom_navigation_admin);

        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                type=dataSnapshot.child("type").getValue().toString();



       // if(current_user_id.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))//if admin
        if(type.equals("Admin"))
                {
            bottomNavigAdmin.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.GONE);
            bottomNavigAdmin.setOnNavigationItemSelectedListener(navListner2);
            bottomNavigAdmin.getMenu().findItem(R.id.nav_post_admin).setChecked(true);
        }
        else
        {
            bottomNav.setVisibility(View.VISIBLE);
            bottomNavigAdmin.setVisibility(View.GONE);
            bottomNav.setOnNavigationItemSelectedListener(navListner);
            bottomNav.getMenu().findItem(R.id.nav_post).setChecked(true);
        }

        rg_mode=findViewById(R.id.rg1);
        rg_mode_opt=findViewById(R.id.rg2);

        //rg_cat=findViewById(R.id.rg3);
        //rg_cat_off=findViewById(R.id.rg4);
        //rg_cat_per=findViewById(R.id.rg5);
        //rg_cat_oth=findViewById(R.id.rg6);
        Image=findViewById(R.id.ima);
        //r=(RelativeLayout)findViewById(R.id.r1);

        //if(current_user_id.equals("nO3l336v84OXDNCkR0aFNm0Es1w2"))
        if(type.equals("Club"))
                {
            cv.setVisibility(View.GONE);
            mSpin.setVisibility(View.GONE);
            mSelect.setVisibility(View.GONE);
            cat1="Activities";
            //rg_mode.setVisibility(View.GONE);
            UserInfo_show="yes";
            Mode="Public";

        }
        //else if(current_user_id.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
        if(type.equals("Admin")||type.equals("SubAdmin"))
                {
            cv.setVisibility(View.GONE);
           // rg_mode.setVisibility(View.GONE);
            UserInfo_show="yes";
            Mode="Public";

        }

        information=(ImageButton)findViewById(R.id.info);

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int c=1;
                for(int i=0;i<1;i++){
                    if(current_user_id.equals(mAdmin[i])){
                        c=0;
                        break;
                    }
                }

                if(!type.equals("Admin")&&(!type.equals("SubAdmin"))){
                    MaterialDialog mDialog = new MaterialDialog.Builder(PostActivity.this)
                            .setTitle("Info")
                            .setMessage("Posts visible to all except the admin will be shown in the public tab while the posts only to the admin will be visible in the official tab..")
                            .setCancelable(false)
                            .setPositiveButton("Okay,Got it!", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }


                            })
                            /*.setNegativeButton("Cancel", R.drawable.ic_arrow, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                                }
                            })*/
                            .build();

                    // Show Dialog
                    mDialog.show();

                }


                    //title.setText("Select Your Announcement Category");

               // RelativeLayout rl = (RelativeLayout) findViewById(R.id.myLayout) ;
                else {
                    MaterialDialog mDialog = new MaterialDialog.Builder(PostActivity.this)
                            .setTitle("Info")
                            .setMessage("Public posts will be visible to all,while the private posts will only be visible to you and the admin ")
                            .setCancelable(false)
                            .setPositiveButton("Okay,Got it!", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }


                            })
                            /*.setNegativeButton("Cancel", R.drawable.ic_arrow, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                                }
                            })*/
                            .build();

                    // Show Dialog
                    mDialog.show();
                }
            }
        });



        Media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CharSequence options[]= new CharSequence[]{

                        "Images",
                        "PDF Files"

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                 builder.setTitle("Select the type");
                 builder.setItems(options, new android.content.DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(android.content.DialogInterface dialog, int i) {

                         if(i == 0){

                             Checker="Image";

                             Intent gallInt=new Intent();
                             gallInt.setAction(Intent.ACTION_GET_CONTENT);
                             gallInt.setType("image/*");
                             startActivityForResult(gallInt,Gall);



                         }
                         else if(i==1){

                             Checker="PDF";


                             Intent gallInt=new Intent();
                             gallInt.setAction(Intent.ACTION_GET_CONTENT);
                             gallInt.setType("application/pdf");
                             startActivityForResult(gallInt.createChooser(gallInt,"Select PDF File"),Gall);



                         }

                     }
                 });

                 builder.show();





            }
        });

        rg_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.post_public)
                {

                         /*cv2.setVisibility(View.VISIBLE);
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
                         });*/

                    UserInfo_show="yes";
                    Mode="Public";



                }
                else
                    {

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
        final ArrayAdapter<String> adapter5=new ArrayAdapter<String>(PostActivity.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.clubs));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //if(current_user_id.equals("nO3l336v84OXDNCkR0aFNm0Es1w2"))
                if(type.equals("Club"))
        {
            spinner2.setAdapter(adapter5);
        }

        else {

            spinner1.setAdapter(adapter1);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String label = parent.getItemAtPosition(position).toString();
                    cat1 = label;


                    if (label.equals("Official")) {
                        spinner2.setAdapter(adapter2);
                    } else if (label.equals("Personal")) {
                        spinner2.setAdapter(adapter3);
                    } else {
                        spinner2.setAdapter(adapter4);
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                cat2=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PostImageRef=FirebaseStorage.getInstance().getReference();


        if(type.equals("Admin")||type.equals("SubAdmin")){

            title.setText("Select Your Announcement Category");
        }


        PostDescription=(EditText)findViewById(R.id.post_description);
        UpdatePostButton=findViewById(R.id.update_post_button);
        loadingBar = new ProgressDialog(PostActivity.this);

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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(Checker.equals("Image")) {


            if (requestCode == Gall && resultCode == RESULT_OK) {
                Uri imageuri = data.getData();
                Log.d(TAG, "onActivityResult: CHOOSE IMAGE : OK >> " + imageuri);
                CropImage.activity(imageuri)
                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1, 1)
                        .setActivityTitle("Select your image..")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
  //                      .setRequestedSize(400, 400)
                        .start(this);
            }



            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                // progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onActivityResult: CROP IMAGE");
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();

                    if (resultUri != null) {
                        Image.setVisibility(View.VISIBLE);
                        Image.setImageURI(resultUri);
                        PostDescription.setHint("Enter your Caption");

                    }


                    Log.d(TAG, "onActivityResult: CROP IMAGE : OK >> " + resultUri);

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PostActivity.this, "Error Occured!....Image Can't be cropped....try again!", Toast.LENGTH_SHORT).show();
                }
            }

        }else if(Checker.equals("PDF")) {

            if (requestCode == Gall && resultCode == RESULT_OK) {

                //progressBar.setVisibility(View.VISIBLE);


                fileUri = data.getData();

                if(fileUri != null){
                    Image.setVisibility(View.VISIBLE);
                    Image.setImageResource(R.drawable.pdf);
                    PostDescription.setHint("Enter your Caption");
                }
            }
        }



    }

    private void ValidatePostInfo()
    {
        //edit text is not empty
        description=PostDescription.getText().toString();

        if(UserInfo_show.isEmpty()){
            Toast.makeText(this, "Please Select the mode of posting (public or private), and if the mode is public...select if you wish to post anonymously or no!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)&& resultUri==null && fileUri==null)
        {
            Toast.makeText(this, "Post cannot be left empty..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(resultUri!=null){
                UpdatePostButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            storingImageToFirebaseStorage(); }

            else if(fileUri!= null){
                UpdatePostButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            stringFileToFirebaseStorage(); }

         /*   else if(TextUtils.isEmpty(description)&& resultUri==null && fileUri==null)
        {
            Toast.makeText(this, "Post cannot be left empty..", Toast.LENGTH_SHORT).show();
        }*/
         else
        {
            SavingPostInformationToDatabase();
            /*MaterialDialog mDialog = new MaterialDialog.Builder(PostActivity.this)
                    .setTitle("Post It..")
                    .setMessage("Be sure of the content you are posting..admin can scan your credentials in case of any spam!" +
                            "Are you sure you want to post this?")
                    .setCancelable(false)
                    .setPositiveButton("Yes,Post It", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
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
                            dialogInterface.dismiss();
                        }


                    })
                    .setNegativeButton("No,Leave It", R.drawable.ic_baseline_cancel_schedule_send_24, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            Toast.makeText(PostActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();

                        }
                    })
                    .build();

            // Show Dialog
            mDialog.show();*/


        }}



    }

    private void stringFileToFirebaseStorage() {


       //UpdatePostButton.setVisibility(View.INVISIBLE);
      //  mLoading.setVisibility(View.VISIBLE);
        StorageReference filePath1=PostImageRef.child("Post PDF").child(fileUri.getLastPathSegment()+postRandomName+".pdf");


        filePath1.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrlp = uri.toString();
                        check1=0;
                        Toast.makeText(PostActivity.this, "PDF Stored", Toast.LENGTH_SHORT).show();
                       // mLoading.setVisibility(View.GONE);
                        SavingPostInformationToDatabase();
                        progressBar.setVisibility(View.GONE);
                        Checker.equals("PDF");
                        //mLoading.setVisibility(View.GONE);
                       /* PostsRef.child(postRandomName+current_user_id).child("PostPDF").setValue(downloadUrlp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PostActivity.this, "PDF Stored", Toast.LENGTH_SHORT).show();
                                    check1=0;
                                    mLoading.setVisibility(View.GONE);
                                    UpdatePostButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(PostActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                    UpdatePostButton.setVisibility(View.VISIBLE);
                                    mLoading.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                }

                            }
                        });*/

                    }
                });

            }
        });



    }

    private void storingImageToFirebaseStorage() {

        //UpdatePostButton.setVisibility(View.INVISIBLE);
        //mLoading.setVisibility(View.VISIBLE);
        StorageReference filePath=PostImageRef.child("Post Images").child(resultUri.getLastPathSegment()+postRandomName+".jpg");





        filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl = uri.toString();
                        check=0;
                        SavingPostInformationToDatabase();
                        Toast.makeText(PostActivity.this, "Image Stored", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Checker="Image";
                        //mLoading.setVisibility(View.GONE);
                       // mLoading.setVisibility(View.GONE);
                        /*PostsRef.child(postRandomName+current_user_id).child("PostImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PostActivity.this, "Image Stored", Toast.LENGTH_SHORT).show();
                                    check=0;

                                    UpdatePostButton.setVisibility(View.VISIBLE);


                                   progressBar.setVisibility(View.GONE);

                                } else
                                    {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(PostActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                    UpdatePostButton.setVisibility(View.VISIBLE);
                                    mLoading.setVisibility(View.GONE);
                                   progressBar.setVisibility(View.GONE);
                                }

                            }
                        });*/

                    }
                });

            }
        });



    }


    private void SavingPostInformationToDatabase()
    {




        MaterialDialog mDialog = new MaterialDialog.Builder(PostActivity.this)
                .setTitle("Post It..")
                .setMessage("Be sure of the content you are posting..admin can scan your credentials in case of any spam!" +
                        "Are you sure you want to post this?")
                .setCancelable(false)
                .setPositiveButton("Yes,Post It", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which)
                    {
                        loadingBar.setTitle("Add New Post");
                        loadingBar.setMessage("Please wait, while we are updating your new post...");
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(true);


                        Calendar calFordDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        saveCurrentDate = currentDate.format(calFordDate.getTime());

                        Calendar calFordTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        saveCurrentTime = currentTime.format(calFordDate.getTime());

                        postRandomName = saveCurrentDate + saveCurrentTime;

                        //SavingPostInformationToDatabase();

                        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if(dataSnapshot.exists())
                                {
                                    type=dataSnapshot.child("type").getValue().toString();

                                    String userAdmissionNo;
                                    if(type.equals("Admin")||type.equals("SubAdmin")||type.equals("Club"))
                                    {
                                        userAdmissionNo=dataSnapshot.child("designation").getValue().toString();
                                    }
                                    else
                                    {
                                        userAdmissionNo=dataSnapshot.child("admission_number").getValue().toString();
                                    }
                                    //String userAdmissionNo=dataSnapshot.child("admission_number").getValue().toString();
                                    String userFullName = dataSnapshot.child("username").getValue().toString();
//                        String userProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                                    String userEmail=dataSnapshot.child("email").getValue().toString();
                                    String postType=dataSnapshot.child("type").getValue().toString();


                                    HashMap postsMap = new HashMap();
                                    postsMap.put("uid", current_user_id);
                                    postsMap.put("date", saveCurrentDate);
                                    postsMap.put("time", saveCurrentTime);
                                    postsMap.put("description", description);
                                    // postsMap.put("type",Checker);
                                    postsMap.put("mode", Mode);
                                    postsMap.put("admissionNo",userAdmissionNo);
                                    postsMap.put("category", cat1);
                                    postsMap.put("subCategory", cat2);
                                    //                  postsMap.put("profileImage", userProfileImage);
                                    postsMap.put("username", userFullName);
                                    postsMap.put("email",userEmail);
                                    postsMap.put("showInformation",UserInfo_show);
                                    postsMap.put("PostKey",postRandomName+current_user_id);
                                    postsMap.put("status","Unresolved");
                                    postsMap.put("postType",postType);


                                    if(check==1){
                                        postsMap.put("PostImage","null");
                                    }
                                    if(check==0)
                                    {
                                        postsMap.put("PostImage",downloadUrl);
                                    }
                                    if(check1==1) {
                                        postsMap.put("PostPDF","null");
                                    }
                                    if(check1==0)
                                    {
                                        postsMap.put("PostPDF",downloadUrlp);
                                    }



                                    // postsMap.put("star","no");
                                    postsMap.put("likes","0");
                                    PostsRef.child(postRandomName+current_user_id ).updateChildren(postsMap)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task)
                                                {
                                                    if(task.isSuccessful())
                                                    {

                                                        //loadingBar.dismiss();
                                                        SendUserToMainActivity();
                                                       // progressBar.setVisibility(View.VISIBLE);
                                                        Toast.makeText(PostActivity.this, "New Post is updated successfully.", Toast.LENGTH_LONG).show();
                                                        mLoading.setVisibility(View.GONE);

                                                    }
                                                    else
                                                    {
                                                        //loadingBar.dismiss();
                                                        Toast.makeText(PostActivity.this, "Error Occured while updating your post.", Toast.LENGTH_LONG).show();
                                                        mLoading.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        dialogInterface.dismiss();
                    }


                })
                .setNegativeButton("No,Leave It", R.drawable.ic_baseline_cancel_schedule_send_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which)
                    {
                        Toast.makeText(PostActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        UpdatePostButton.setVisibility(View.VISIBLE);
                        dialogInterface.dismiss();

                    }
                })
                .build();

        // Show Dialog
        mDialog.show();




        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;
        /*UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    type=dataSnapshot.child("type").getValue().toString();

                    String userAdmissionNo;
                        if(type.equals("Admin")||type.equals("SubAdmin")||type.equals("Club"))
                        {
                             userAdmissionNo=dataSnapshot.child("designation").getValue().toString();
                        }
                        else
                            {
                             userAdmissionNo=dataSnapshot.child("admission_number").getValue().toString();
                        }
                        //String userAdmissionNo=dataSnapshot.child("admission_number").getValue().toString();
                        String userFullName = dataSnapshot.child("username").getValue().toString();
//                        String userProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                        String userEmail=dataSnapshot.child("email").getValue().toString();
                        String postType=dataSnapshot.child("type").getValue().toString();


                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", description);
                   // postsMap.put("type",Checker);
                    postsMap.put("mode", Mode);
                    postsMap.put("admissionNo",userAdmissionNo);
                    postsMap.put("category", cat1);
                    postsMap.put("subCategory", cat2);
  //                  postsMap.put("profileImage", userProfileImage);
                    postsMap.put("username", userFullName);
                    postsMap.put("email",userEmail);
                    postsMap.put("showInformation",UserInfo_show);
                    postsMap.put("PostKey",postRandomName+current_user_id);
                    postsMap.put("status","Unresolved");
                    postsMap.put("postType",postType);


                   if(check==1){
                       postsMap.put("PostImage","null");
                   }
                   if(check==0)
                   {
                       postsMap.put("PostImage",downloadUrl);
                   }
                   if(check1==1) {
                       postsMap.put("PostPDF","null");
                   }



                   // postsMap.put("star","no");
                    postsMap.put("likes","0");
                    PostsRef.child(postRandomName+current_user_id ).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {


                                        SendUserToMainActivity();
                                        Toast.makeText(PostActivity.this, "New Post is updated successfully.", Toast.LENGTH_LONG).show();
                                        mLoading.setVisibility(View.GONE);

                                    }
                                    else
                                    {
                                        Toast.makeText(PostActivity.this, "Error Occured while updating your post.", Toast.LENGTH_LONG).show();
                                        mLoading.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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

        if(Checker.equals("Image")) {


            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PostActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 5000);
            progressBar.setVisibility(View.GONE);
        }else if(Checker.equals("PDF")){


            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PostActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 15000);
            progressBar.setVisibility(View.GONE);

        }else{

           /* progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {*/
                    Intent intent = new Intent(PostActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                //}
            //}, 1000);
            //progressBar.setVisibility(View.GONE);
        }


    }





    private BottomNavigationView.OnNavigationItemSelectedListener navListner2=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.nav_home_admin:
                            Intent intent4=new Intent(PostActivity.this,MainActivity.class);
                            startActivity(intent4);
                            intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            break;
                        case R.id.nav_post_admin:
                            Intent intent=new Intent(PostActivity.this,PostActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                             finish();
                            break;
                        case R.id.nav_profile_admin:
                            Intent Pintent=new Intent(PostActivity.this,ProfileActivity.class);
                            Pintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent);
                            finish();
                            break;
                        case R.id.nav_star_admin:
                            Intent Pintent1=new Intent(PostActivity.this,StarActivity.class);
                            Pintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent1);
                            finish();
                            break;
                        case R.id.nav_add_admin:
                            Intent Pintent2=new Intent(PostActivity.this,AddAdmin.class);
                            Pintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent2);
                            finish();
                            break;

                    }

                    return true;
                }
            };
    private BottomNavigationView.OnNavigationItemSelectedListener
            navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            Intent intent=new Intent(PostActivity.this,MainActivity.class);
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();

                            break;

                        case R.id.nav_profile:
                            Intent Pintent=new Intent(PostActivity.this,ProfileActivity.class);
                            Pintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent);
                            finish();

                            break;
                        case R.id.nav_star:
                            Intent Pintent1=new Intent(PostActivity.this,StarActivity.class);
                            Pintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent1);
                            finish();

                            break;

                    }

                    return true;
                }
            };
}
