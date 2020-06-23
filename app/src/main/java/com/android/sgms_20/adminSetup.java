package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class adminSetup extends AppCompatActivity
{
    String cat1;
    String cat2;
    String current_user_id;
    FirebaseAuth mAuth;
    Button mContinue;
    EditText mDep,mDesig;
    String department,designation;
    DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setup);
        //this.setFinishOnTouchOutside(false);

        mAuth=FirebaseAuth.getInstance();
        current_user_id=mAuth.getCurrentUser().getUid();
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users");
        mContinue=findViewById(R.id.con);
        mDep=findViewById(R.id.dept);
        mDesig=findViewById(R.id.desig);

        //department=mDep

        Spinner spinner1=(Spinner)findViewById(R.id.spin1);
        final Spinner spinner2=(Spinner)findViewById(R.id.spin2);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(adminSetup.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.categories));
        final ArrayAdapter<String> adapter2=new ArrayAdapter<String>(adminSetup.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.sub1));
        final ArrayAdapter<String> adapter3=new ArrayAdapter<String>(adminSetup.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.sub2));
        final ArrayAdapter<String> adapter4=new ArrayAdapter<String>(adminSetup.this,android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.sub3));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label=parent.getItemAtPosition(position).toString();
                cat1=label;

                if(label.equals("Official"))
                {
                    spinner2.setAdapter(adapter2);
                }
                else if(label.equals("Personal"))
                {
                    spinner2.setAdapter(adapter3);
                }
                else {
                    spinner2.setAdapter(adapter4);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat2=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RegisterUser();







    }

    private void RegisterUser()
    {
        department = mDep.getText().toString().trim();
        designation = mDesig.getText().toString().trim();
        //String Password2 = password2.getText().toString().trim();



        if (department.isEmpty())
        {
            mDep.setError("Department is required!");
            mDep.requestFocus();
            return;
        }
        if (designation.isEmpty())
        {
            mDesig.setError("Designation is required!");
            mDesig.requestFocus();
            return;
        }


        HashMap postsMap = new HashMap();
        //postsMap.put("department",);
        postsMap.put("department", department);
       // user1.put("email", Mail);
        postsMap.put("designation", designation);
        postsMap.put("category", cat1);
        postsMap.put("subCategory", cat2);

        UserRef.child(current_user_id).updateChildren(postsMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            SendUserToMainActivity();
                            Toast.makeText(adminSetup.this, "Details are updated successfully....", Toast.LENGTH_LONG).show();
                            //mLoading.setVisibility(View.GONE);

                        }
                        else
                        {
                            Toast.makeText(adminSetup.this, "Error occurred while updating your details....", Toast.LENGTH_LONG).show();
                            //mLoading.setVisibility(View.GONE);
                        }
                    }
                });
    }
        });

    }

    private void SendUserToMainActivity()
    {
        startActivity(new Intent(adminSetup.this,MainActivity.class));
        finish();
    }
}