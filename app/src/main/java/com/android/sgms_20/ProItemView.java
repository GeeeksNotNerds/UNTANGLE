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

    TextView Name,Email;
    CircleImageView image;

    private String PostKey,currentUserID,databaseUSerID,description,Status,permission,ProfileImage;
    private DatabaseReference ClickPostRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_item_view);
        Name=findViewById(R.id.user_profile_name);
        Email=findViewById(R.id.user_profile_email);
        image=findViewById(R.id.user_profile_photo);


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.90),(int) (height*.45));
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.60f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        PostKey=getIntent().getExtras().get("PostKey").toString();
        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);


        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    permission=dataSnapshot.child("showInformation").getValue().toString();
                    ProfileImage = dataSnapshot.child("profileImage").getValue().toString();
                    if(permission.equals("yes")){

                        Picasso.with(ProItemView.this)
                                .load(ProfileImage)
                                .placeholder(R.drawable.ic_profile)
                                .into(image);

                     description = dataSnapshot.child("username").getValue().toString();
                     Status = dataSnapshot.child("email").getValue().toString();
                     Email.setText(Status);

                     Name.setText(description);
                    }
                    else if(permission.equals("no")){
                        Email.setText("-");

                        Name.setText("Anonymous");
                    }
                    databaseUSerID = dataSnapshot.child("uid").getValue().toString();

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                }


            });






        }
}
