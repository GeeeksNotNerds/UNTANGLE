package com.android.sgms_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.OutputStream;

public class ClickPhotoActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    String currentUserID,PostKey,url;
    ImageView Download;
    DatabaseReference ClickPostRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_photo);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.90),(int) (height*.80));
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.60f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        ZoomableImageView imViewedImage = findViewById(R.id.post_pic);


        PostKey=getIntent().getExtras().get("PostKey").toString();
        url=getIntent().getExtras().get("URL").toString();


        Download=findViewById(R.id.download_pic);
        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Picasso.with(ClickPhotoActivity.this)
                        .load(dataSnapshot.child("PostImage").getValue().toString())

                        .placeholder(R.drawable.loader1)
                        .into(imViewedImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
               startActivity(intent);


            }
        });

    }
}