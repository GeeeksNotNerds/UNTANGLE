package com.android.sgms_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class The_First extends AppCompatActivity {
    RadioButton yes,no;
    Button next;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the__first);
        mAuth=FirebaseAuth.getInstance();
      yes=findViewById(R.id.yes);
      no=findViewById(R.id.no);
      next=findViewById(R.id.next);
      next.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if(yes.isChecked()){
                  Intent intent = new Intent(The_First.this, LoginActivity.class);
                  startActivity(intent);
              }else if(no.isChecked()){
                  Intent intent = new Intent(The_First.this, Admin_Login.class);
                  startActivity(intent);
              }

          }
      });





    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)//if already logged in
        {
            SendUserToMainActivity();
        }




    }

    private void SendUserToMainActivity() {
        Intent intent=new Intent(The_First.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
