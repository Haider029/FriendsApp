package com.example.lastattempt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,password,name;
    private Button signin,signup;
    FirebaseUser user;
    static String LoggedIn_User_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // OneSignal Initialization
        OneSignal.startInit(this).init();
        mAuth=FirebaseAuth.getInstance();
        signin =(Button) findViewById(R.id.signin);
        signup =(Button) findViewById(R.id.signup);
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        name = (EditText) findViewById(R.id.etName);
        //Checking if user is already logged in or not
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
        user = mAuth.getCurrentUser();
        LoggedIn_User_Email=user.getEmail();
        OneSignal.sendTag("UserID:",LoggedIn_User_Email);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail = email.getText().toString().trim();
                String getepassword = password.getText().toString().trim();
                callsignin(getemail,getepassword);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail= email.getText().toString().trim();
                String getepassword = password.getText().toString().trim();
                callsignup(getemail,getepassword);

            }
        });

    }
    //Creating Account
    private void callsignup(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Testing","Sign Up SuccessFul"+task.isSuccessful());
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"SignUp Fail",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                             userProfile();
                             Toast.makeText(MainActivity.this,"Created Account",Toast.LENGTH_SHORT).show();
                             Log.d("Testing","Created Account");
                        }

                    }
                });

    }
    //Set User Display
    private void userProfile(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            UserProfileChangeRequest profileupdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString().trim()).build();
            user.updateProfile(profileupdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Testing","User Profile Updated");
                            }
                        }
                    });
        }
    }
    private void callsignin(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Testing","Sign In Successful" + task.isSuccessful());
                        if(!task.isSuccessful()){
                            Log.w("Testing","signInWithEmail:Failed",task.getException());
                            Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent i = new Intent(MainActivity.this,SignIn.class);
                            finish();
                            startActivity(i);
                        }

                    }
                });
    }
}
