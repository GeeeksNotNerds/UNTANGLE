package com.android.sgms_20;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.util.HashMap;

public class ClickPostActivity extends AppCompatActivity {

    private TextView PostDescription,postStatus,postStatus_heading;
    private Button DeletePostButton,EditPostButton,statusButton;
    ImageView Share;
    private String PostKey,currentUserID,databaseUSerID,description,Status,message,ReceiverUid;
    private DatabaseReference ClickPostRef,NotificationRef,UserRef;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);
        //getSupportActionBar().hide();


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
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users");
        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);
        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ReceiverUid=dataSnapshot.child("uid").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        NotificationRef=FirebaseDatabase.getInstance().getReference().child("Notification");

        PostDescription=(TextView)findViewById(R.id.click_post_description);
        PostDescription.setMovementMethod(LinkMovementMethod.getInstance());
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
                  String mode=dataSnapshot.child("mode").getValue().toString();
                  postStatus.setText(Status);

                  PostDescription.setText(description);
                  databaseUSerID=dataSnapshot.child("uid").getValue().toString();
                  if(mode.equals("Private")){

                      postStatus.setVisibility(View.VISIBLE);
                      postStatus_heading.setVisibility(View.VISIBLE);

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

                                  ClickPostRef.child("status").setValue(inputField.getText().toString())
                                          .addOnCompleteListener(task -> {

                                              if(task.isSuccessful()){

                                                  HashMap<String,String> NotificationMap= new HashMap<>();
                                                  NotificationMap.put("from",currentUserID);
                                                  NotificationMap.put("type","StatusChange");

                                                  NotificationRef.child(ReceiverUid).push()
                                                          .setValue(NotificationMap)
                                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                  Toast.makeText(ClickPostActivity.this, "Status Changed.", Toast.LENGTH_SHORT).show();

                                                                  SendUserToMainActivity();

                                                              }
                                                          });




                                              }

                                          });


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
                //UserRef.child("description").setValue(inputField.getText().toString());
                /*UserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                          if(dataSnapshot1.child("star").hasChild(PostKey))
                          {
                            dataSnapshot1.child("star").child(PostKey).child("description").getRef().setValue(inputField.getText().toString());
                          }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

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

        //public Iterable<DataSnapshot> getChildren();
        //for(DataSnapshot child:)
        //void getData(DataSnapshot dataSnapshot){
        /*UserRef.addValueEventListener(new ValueEventListener()
        {
            public void getData(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                        Toast.makeText(ClickPostActivity.this, "Working", Toast.LENGTH_SHORT).show();
                        UserRef.child("pHpCnW14v9cUKXLLB4eySHHSmlG3").child("star").child(PostKey).getRef().removeValue();
                    }
            }


            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.hasChild("star"))
                    {

                        //Toast.makeText(ClickPostActivity.this, PostKey, Toast.LENGTH_SHORT).show();
                        //dataSnapshot1.child("star").child(PostKey).getRef().removeValue();
                        //if(dataSnapshot1.child("star").hasChild(PostKey))
                            //Toast.makeText(ClickPostActivity.this, "Working", Toast.LENGTH_SHORT).show();
                        UserRef.child("pHpCnW14v9cUKXLLB4eySHHSmlG3").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2)
                            {
                                if(dataSnapshot2.child("star").hasChild(PostKey))
                                {
                                    //Toast.makeText(ClickPostActivity.this, "Working", Toast.LENGTH_SHORT).show();
                                    dataSnapshot2.child("star").child(PostKey).getRef().removeValue();
                                }
                                else
                                {
                                    //Toast.makeText(ClickPostActivity.this, "Not a child", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

       // ClickPostRef.removeValue();
        //UserRef.removeValue();
        //UserRef.child("pHpCnW14v9cUKXLLB4eySHHSmlG3").child("star").child(PostKey).getRef().removeValue();
        //UserRef.child("FU5r1KMEvOeQqCU5D8V7FQ4MGQW2").child("department").setValue("lfof");
      // new Handler().postDelayed(new Runnable() {
        //    @Override
          //  public void run() {
                //ClickPostRef.removeValue();
                //SendUserToMainActivity();
                //Toast.makeText(ClickPostActivity.this, "Post has been deleted..", Toast.LENGTH_SHORT).show();
            //}
        //},5000);
        MaterialDialog mDialog = new MaterialDialog.Builder(ClickPostActivity.this)
                .setTitle("Delete..")
                .setMessage("Are you sure you want to delete this post?")
                .setCancelable(false)
                .setPositiveButton("Yes,Delete It!", R.drawable.ic_baseline_delete_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                                 ClickPostRef.removeValue();
                                 SendUserToMainActivity();
                                 Toast.makeText(ClickPostActivity.this, "Post has been deleted..", Toast.LENGTH_SHORT).show();

                        dialogInterface.dismiss();
                    }


                })
                .setNegativeButton("Don't Delete", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
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
    private void getData(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
        {
            if(dataSnapshot1.hasChild("star")){
                Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
            //UserRef.child("pHpCnW14v9cUKXLLB4eySHHSmlG3").child("star").child(PostKey).getRef().removeValue();
        }}
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
