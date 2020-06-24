package com.android.sgms_20;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProItemView extends AppCompatActivity {

    TextView Name,Email,Depatment,AdNo;
    CircleImageView image;
    String type;
    private String PostKey,currentUserID,databaseUSerID,description,Status,permission,ProfileImage,Pro,admissionNo,department;
    private DatabaseReference ClickPostRef,ProRef,UserRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_item_view);
        Name=findViewById(R.id.name);
        Email=findViewById(R.id.email);
        Depatment=findViewById(R.id.dept);
        AdNo=findViewById(R.id.admin_no);


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.90),(int) (height*.65));
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.60f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        PostKey=getIntent().getExtras().get("PostKey").toString();
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);


        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                if (dataSnapshot1.exists()) {

                Pro=dataSnapshot1.child("uid").getValue().toString();
                    permission=dataSnapshot1.child("showInformation").getValue().toString();

                    databaseUSerID = dataSnapshot1.child("uid").getValue().toString();




                    ProRef=FirebaseDatabase.getInstance().getReference().child("Users").child(Pro);//pro is uid of the one who is posting

                    ProRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                               //if(dataSnapshot.child("ProfileImage").exists()) ProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                               // ProfileImage="";


                               //if(permission.equals("yes")){

                                  //  Picasso.with(ProItemView.this)
                                    //        .load(ProfileImage)
                                      //      .placeholder(R.drawable.ic_profile)
                                        //    .into(image);

                                   /* if(!ProfileImage.isEmpty()) {
                                        Picasso.with(ProItemView.this)
                                                .load(ProfileImage)
                                                .placeholder(R.drawable.ic_account_circle_24px)
                                                .into(image);
                                    }else{
                                        image.setImageResource(R.drawable.profile);
                                    }*/

                                    description = dataSnapshot.child("username").getValue().toString();
                                    Status = dataSnapshot.child("email").getValue().toString();
                                    department=dataSnapshot.child("department").getValue().toString();
                                    String postType=dataSnapshot.child("type").getValue().toString();


                                   // if(Pro.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))//if post is from admin
                                 /*  if(postType.equals("Admin"))
                                    {
                                        admissionNo=dataSnapshot.child("designation").getValue().toString();
                                        AdNo.setText("Designation : ");

                                    }
                                    else
                                        {
                                        admissionNo = dataSnapshot.child("admission_number").getValue().toString();
                                    }*/
                                    Email.setText("Email : " +Status);

                                    Name.setText("Name : "+description);

                                    Depatment.setText("Department : "+department);

                                   //if(Pro.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
                                   if(postType.equals("Admin")||postType.equals("Club"))
                                   {
                                       admissionNo=dataSnapshot.child("designation").getValue().toString();
                                       AdNo.setText("Designation : "+admissionNo);

                                   }
                                   else
                                       {
                                       admissionNo = dataSnapshot.child("admission_number").getValue().toString();
                                       AdNo.setText("Admission Number : "+admissionNo);
                                   }
                                //}

                                 /*if(permission.equals("no") && !currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){
                                    Email.setText("-");
                                    AdNo.setText("-");
                                    Depatment.setText("-");

                                    Name.setText("Anonymous");
                                }*/
                                /*else if(permission.equals("no") && currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){

                                   description = dataSnapshot.child("username").getValue().toString();
                                   Status = dataSnapshot.child("email").getValue().toString();
                                   department=dataSnapshot.child("department").getValue().toString();

                                   if(Pro.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){
                                       admissionNo=dataSnapshot.child("designation").getValue().toString();


                                   }*/

                                   /*else
                                       {
                                       admissionNo = dataSnapshot.child("admission_number").getValue().toString();
                                   }
                                   Email.setText("Email : "+Status);

                                   Name.setText("Name : "+description);

                                   Depatment.setText("Department : "+department);
                                   if(Pro.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){
                                       admissionNo=dataSnapshot.child("designation").getValue().toString();
                                       AdNo.setText("Designation : "+admissionNo);

                                   }
                                   else
                                       {
                                       admissionNo = dataSnapshot.child("admission_number").getValue().toString();
                                       AdNo.setText("Admission Number : "+admissionNo);
                                   }

                               }*/
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                }


            });






        }
}
