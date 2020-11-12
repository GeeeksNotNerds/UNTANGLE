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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ClickPostActivity extends AppCompatActivity {

    private TextView PostDescription,postStatus,postStatus_heading;
    private Button DeletePostButton,EditPostButton,statusButton;
    ImageView Share;
    private String postTypeDelete;
    String type,postType;
    ImageView Image;
    private String PostKey,currentUserID,databaseUSerID,description,Status,message,ReceiverUid;
    private DatabaseReference ClickPostRef,NotificationRef,UserRef,multiNoteRef;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        Image=findViewById(R.id.postImage_c);
        PostKey=getIntent().getExtras().get("PostKey").toString();
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                type=dataSnapshot.child("type").getValue().toString();

        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);
        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                ReceiverUid=dataSnapshot.child("uid").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        NotificationRef=FirebaseDatabase.getInstance().getReference().child("Notification");
        multiNoteRef=FirebaseDatabase.getInstance().getReference().child("MultiNotifications");

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
                  postTypeDelete=dataSnapshot.child("postType").getValue().toString();
                  message=dataSnapshot.child("description").getValue().toString();
                  Status=dataSnapshot.child("status").getValue().toString();
                  String mode=dataSnapshot.child("mode").getValue().toString();
                  postStatus.setText(Status);

                  PostDescription.setText(description);
                  databaseUSerID=dataSnapshot.child("uid").getValue().toString();
                  postType=dataSnapshot.child("postType").getValue().toString();

                  if(mode.equals("Private"))
                  {

                      postStatus.setVisibility(View.VISIBLE);
                      postStatus_heading.setVisibility(View.VISIBLE);

                  }

                  if(!(dataSnapshot.child("PostImage").getValue().toString()).equals("null")){


                      Image.setVisibility(View.VISIBLE);

                      Picasso.with(ClickPostActivity.this)
                              .load(dataSnapshot.child("PostImage").getValue().toString())
                              //.fit()
                              .placeholder(R.drawable.loader1)
                              .into(Image);

                  }else{
                      Image.setVisibility(View.GONE);
                  }



                 if((type.equals("Admin")||type.equals("SubAdmin"))&& !postType.endsWith("Admin")&& !postType.endsWith("SubAdmin") &&mode.equals("Private")){
                      statusButton.setVisibility(View.VISIBLE);
                      DeletePostButton.setVisibility(View.INVISIBLE);
                      EditPostButton.setVisibility(View.INVISIBLE);


                  }
                  else if(currentUserID.equals(databaseUSerID)&&(!postType.equals("StudentOnly")))
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

                                                                  MaterialDialog mDialog = new MaterialDialog.Builder(ClickPostActivity.this)
                                                                          .setTitle("Status of grievance....")
                                                                          .setMessage("The grievance is..")
                                                                          .setCancelable(false)
                                                                          .setPositiveButton("Solved!", R.drawable.ic_baseline_thumb_up_24, new MaterialDialog.OnClickListener() {
                                                                              @Override
                                                                              public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                                                                  ClickPostRef.child("postType").setValue("StudentOnly");
                                                                                  SendUserToMainActivity();
                                                                                  dialogInterface.dismiss();
                                                                              }
                                                                          })
                                                                          .setNegativeButton("Pending..", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                                                                              @Override
                                                                              public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                                                                  SendUserToMainActivity();
                                                                                  dialogInterface.dismiss();
                                                                              }


                                                                          })
                                                                          .build();

                                                                  mDialog.show();




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
                DeleteCurrentPost(postTypeDelete);

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

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

                ClickPostRef.child("description").setValue("(edited) "+inputField.getText().toString());


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

    private void DeleteCurrentPost(String deletePost)
    {

        MaterialDialog mDialog = new MaterialDialog.Builder(ClickPostActivity.this)
                .setTitle("Delete..")
                .setMessage("Are you sure you want to delete this post?")
                .setCancelable(false)
                .setPositiveButton("Yes,Delete It!", R.drawable.ic_baseline_delete_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                        if(deletePost.endsWith("Admin")||deletePost.endsWith("SubAdmin"))
                        {
                            ClickPostRef.removeValue();
                        }
                        else
                        {
                            ClickPostRef.child("postType").setValue("delete" + deletePost);
                            ClickPostRef.child("PostPDF").setValue("null");
                            ClickPostRef.child("PostImage").setValue("null");
                            ClickPostRef.child("description").setValue("The message has been deleted...");
                        }

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


        mDialog.show();


    }
    private void getData(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
        {
            if(dataSnapshot1.hasChild("star")){
                Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
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
