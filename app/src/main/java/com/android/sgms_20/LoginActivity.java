package com.android.sgms_20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

   static final int GOOGLE_SIGN=123;

    EditText email,password;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private boolean checker;
    private String TAG;
    ImageView google;
    GoogleSignInClient mGooglesignInClient;
    TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        findViewById(R.id.login_button).setOnClickListener(this);
        pass=findViewById(R.id.pass);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_bar);
        google=findViewById(R.id.google_signin_button);
        findViewById(R.id.register_account_link).setOnClickListener(this);

        if(!haveNetworkConnection()){
            Toast.makeText(LoginActivity.this,"You are not Online....Please switch on your interner connection!",Toast.LENGTH_SHORT).show();
        }




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder()
                                                                        .requestIdToken(getString(R.string.default_web_client_id))
                                                                        .requestEmail()
                                                                        .build();
        mGooglesignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinGoogle();
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,password.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


    }


    private void UserLogin(){
        String Email=email.getText().toString().trim();
        String Password=password.getText().toString().trim();
        if(Email.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter a valid Email address!");
            email.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(Password.length()<6 ){
            password.setError("Minimum length of Password is 6 !");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);



        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    VerifyWithEmail();


                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.register_account_link:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.login_button:
                UserLogin();
                break;
        }
    }


    private void VerifyWithEmail(){
        FirebaseUser user = mAuth.getCurrentUser();
        checker=user.isEmailVerified();
        if (checker) {
            finish();
            Toast.makeText(this,"Account is verified.",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        }else{
            Toast.makeText(this,"Verify your account first",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    void signinGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent=mGooglesignInClient.getSignInIntent();
        startActivityForResult(signInIntent,GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GOOGLE_SIGN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                if(account!=null){
                    firebaseAuthWithGoogleAccount(account);
                }

            }catch (ApiException e){
                e.printStackTrace();
            }

        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential=GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    FirebaseUser user =mAuth.getCurrentUser();



                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"SignIn Successgul",Toast.LENGTH_LONG).show();


                }

            }
        });


    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
