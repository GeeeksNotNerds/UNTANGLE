package com.android.sgms_20;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClickPostActivity extends AppCompatActivity {

    private TextView PostDescription,postStatus,postStatus_heading;
    private Button DeletePostButton,EditPostButton,statusButton;
    ImageView Share;
    private String PostKey,currentUserID,databaseUSerID,description,Status,message;
    private DatabaseReference ClickPostRef;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


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

        // FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId)
        //get it using known data from the card view
        //.child("posts").push().getKey()


        PostKey=getIntent().getExtras().get("PostKey").toString();
        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        PostDescription=(TextView)findViewById(R.id.click_post_description);
        DeletePostButton=(Button)findViewById(R.id.delete_post_button);
        EditPostButton=(Button)findViewById(R.id.edit_post_button);
        postStatus=findViewById(R.id.click_post_status);
        postStatus_heading=findViewById(R.id.status_heading);
        statusButton=findViewById(R.id.status_post_button);
        statusButton.setVisibility(View.INVISIBLE);
        DeletePostButton.setVisibility(View.INVISIBLE);
        EditPostButton.setVisibility(View.INVISIBLE);
        Share=findViewById(R.id.share);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
              if(dataSnapshot.exists())
              {
                  description=dataSnapshot.child("description").getValue().toString();
                  message=dataSnapshot.child("description").getValue().toString();
                  Status=dataSnapshot.child("status").getValue().toString();
                  postStatus.setText(Status);

                  PostDescription.setText(description);
                  databaseUSerID=dataSnapshot.child("uid").getValue().toString();
                  if(databaseUSerID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){

                      postStatus.setVisibility(View.INVISIBLE);
                      postStatus_heading.setVisibility(View.INVISIBLE);

                  }



                  if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")&& !databaseUSerID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){

                      statusButton.setVisibility(View.VISIBLE);
                      DeletePostButton.setVisibility(View.INVISIBLE);
                      EditPostButton.setVisibility(View.INVISIBLE);


                  }
                  else if(currentUserID.equals(databaseUSerID))
                  {
                      DeletePostButton.setVisibility(View.VISIBLE);
                      EditPostButton.setVisibility(View.VISIBLE);

                  }
                  EditPostButton.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          EditCurrentPost(description);
                      }
                  });


                  statusButton.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          //ClickPostRef.child("status").setValue("Reviewed...Corresponding Action will be taken as soon as possible");
                         // Toast.makeText(ClickPostActivity.this, "Status Changed", Toast.LENGTH_SHORT).show();
                          //SendUserToMainActivity();

                          AlertDialog.Builder builder=new AlertDialog.Builder(ClickPostActivity.this);
                          builder.setTitle("Change Status");

                          final EditText inputField = new EditText(ClickPostActivity.this);
                          inputField.setText(Status);
                          builder.setView(inputField);
                          builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which)
                              {

                                  ClickPostRef.child("status").setValue(inputField.getText().toString());
                                  notification();
                                  Toast.makeText(ClickPostActivity.this, "Status Changed.", Toast.LENGTH_SHORT).show();

                                  SendUserToMainActivity();
                              }
                          });
                          builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.cancel();
                              }
                          });
                          Dialog dialog=builder.create();
                          dialog.show();





                      }
                  });

              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DeletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DeleteCurrentPost();

            }
        });

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message2 = "New Post on UNTANGLE : "+message;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message2);

                startActivity(Intent.createChooser(share, "SHARE POST"));
            }
        });



    }

    private void EditCurrentPost(String description)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle("Edit Post");

        final EditText inputField = new EditText(ClickPostActivity.this);
        inputField.setText(description);
        builder.setView(inputField);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                ClickPostRef.child("description").setValue(inputField.getText().toString());
                Toast.makeText(ClickPostActivity.this, "Post updated..", Toast.LENGTH_SHORT).show();
                SendUserToMainActivity();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
        Dialog dialog=builder.create();
        dialog.show();

    }

    private void DeleteCurrentPost()
    {
        ClickPostRef.removeValue();
     SendUserToMainActivity();
        Toast.makeText(this, "Post has been deleted..", Toast.LENGTH_SHORT).show();

    }
    private void SendUserToMainActivity()
    {
        Intent intent=new Intent(ClickPostActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }


private void notification(){

    NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"n")
            .setContentText("UNTANGLED")
            .setSmallIcon(R.id.logo)
            .setAutoCancel(true)
            .setContentText("Status of your post has been changed!");
    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
    managerCompat.notify(1,builder.build());

}


}
