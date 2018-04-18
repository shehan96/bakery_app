package com.adelmotechnology.bakery_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.app.ProgressDialog;


import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    Button loginBtn;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.usernametext);
        password = (EditText) findViewById(R.id.passwordtext);
        loginBtn = (Button) findViewById(R.id.loginbtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //User Login Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(username.getText().toString(), password.getText().toString());
            }
        });


        //updateUI(currentUser);

        //TextView datetext = (TextView) findViewById(R.id.datetext);
        //try{
            //datetext.setText(getDate());
        //}catch (Exception ex){
           //System.out.println("Error:"+ex);
        //}

    }

    //form validating
    private boolean validateForm() {
        boolean valid = true;

        String usernameString = username.getText().toString();
        if (TextUtils.isEmpty(usernameString)) {
            username.setError("Required.");
            valid = false;
        } else {
            username.setError(null);
        }

        String passwordString = password.getText().toString();
        if (TextUtils.isEmpty(passwordString)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    //user login
    private void signIn(String email, String password) {
        Log.d(TAG, "logIn:" + email);
        if (!validateForm()) {
            return;
        }

        String singIn = "Wait Until LogIn Process Complete";

        final ProgressDialog spinner = spinner(singIn);
        spinner.show();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "logInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "LogIn Success",
                                    Toast.LENGTH_SHORT).show();
                            directToControlPanel();
                            spinner.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "logInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "LogIn failed.",
                                    Toast.LENGTH_SHORT).show();
                            spinner.dismiss();
                        }

                        // [START_EXCLUDE]
                        //if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        //}
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    //direct to sales activity
    private void directToControlPanel(){
        Intent controlPanelActivityIntent = new Intent(this,controlPanel.class);
        startActivity(controlPanelActivityIntent);
    }

    //direct to sales activity
    private void directToRegisterActivity(){
        Intent registerActivityIntent = new Intent(this,RegisterUserActivity.class);
        startActivity(registerActivityIntent);
    }

    //progress spinner
    private ProgressDialog spinner(String status){
        ProgressDialog spinner = new ProgressDialog(MainActivity.this);
        spinner.setMessage(status); // Setting Message
        spinner.setTitle("Loading..."); // Setting Title
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        return spinner;
    }


    //private void createAccount

   //private String getDate(){
      //String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
      //return currentDate;
   //}
}
