package com.android.sgms_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class EntryActivity extends AppCompatActivity {
    Button mGot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.90),(int) (height*.28));
        mGot=findViewById(R.id.got);
        mGot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendUserToMainActivity();
            }
        });
    }

    private void SendUserToMainActivity()
    {
        //Intent myIntent2 = new Intent(getApplicationContext(), EntryActivity.class);
        EntryActivity.this.finish();
    }
}