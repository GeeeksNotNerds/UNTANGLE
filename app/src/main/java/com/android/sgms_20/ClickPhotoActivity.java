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
//        imViewedImage.setImageResource(R.drawable.art_freaks);

        PostKey=getIntent().getExtras().get("PostKey").toString();
        url=getIntent().getExtras().get("URL").toString();
    //    Image=findViewById(R.id.postImage_c);

        Download=findViewById(R.id.download_pic);
        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //imViewedImage.setIma(dataSnapshot.child("").getValue().toString());
                Picasso.with(ClickPhotoActivity.this)
                        .load(dataSnapshot.child("PostImage").getValue().toString())
                        //.fit()
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
      /*  Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                /*BitmapDrawable drawable = (BitmapDrawable) imViewedImage.getDrawable();
                Bitmap icon = drawable.getBitmap();
               // Bitmap icon = mBitmap;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        values);


                OutputStream outstream;
                try {
                    outstream = getContentResolver().openOutputStream(uri);
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));*/
                /*Uri imageUri = Uri.parse(pictureFile.getAbsolutePath());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
//Send to Whattssap
                shareIntent.setPackage("com.whatsapp");
//Even you can add text to the image
                shareIntent.putExtra(Intent.EXTRA_TEXT, picture_text);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
                //ImageView content = (ImageView)mView.findViewById(R.id.imageViewy);
                ClickPostRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ImageView content=findViewById(R.id.share_pic1);
                        Picasso.with(ClickPhotoActivity.this)
                                .load(dataSnapshot.child("PostImage").getValue().toString())
                                //.fit()
                                .placeholder(R.drawable.loader1)
                                .into(content);

                        //content.setDrawingCacheEnabled(true);

                        content.buildDrawingCache();
                        Bitmap icon = content.getDrawingCache();

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "title");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);


                        OutputStream outstream;
                        try {
                            outstream = getContentResolver().openOutputStream(uri);
                            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                            outstream.close();
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }

                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(share, "Share Image"));

                        /*Uri imageUri= null;
                        try {
                            imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), String.valueOf(content), "title", "discription"));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        shareIntent.setType("image/*");
                        startActivity(Intent.createChooser(shareIntent,"Share"));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });*/

    }
}