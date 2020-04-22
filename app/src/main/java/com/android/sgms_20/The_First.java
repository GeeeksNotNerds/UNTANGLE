package com.android.sgms_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class The_First extends AppCompatActivity {
    RadioButton yes,no;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the__first);

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
}
