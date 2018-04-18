package com.adelmotechnology.bakery_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterUserActivity extends AppCompatActivity {

    Button goBackBtn,singinBtn;
    EditText username,password;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        username = (EditText) findViewById(R.id.usernametext);
        password = (EditText) findViewById(R.id.passwordtext);
        goBackBtn = (Button) findViewById(R.id.goBackBtn);
        singinBtn = (Button) findViewById(R.id.singinbtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //loginBtn Click Event
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlActivityStart();
            }
        });

        singinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(username.getText().toString(),password.getText().toString());
            }
        });

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

    //create new user account
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // create spinner and start spinner
        String singIn = "Wait Until Register Process Complete";
        final ProgressDialog spinner = spinner(singIn);
        spinner.show();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterUserActivity.this, "Sing In Success.",
                                    Toast.LENGTH_SHORT).show();
                            spinner.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterUserActivity.this, "Sing In failed.",
                                    Toast.LENGTH_SHORT).show();
                            spinner.dismiss();
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    //direct to mainactivity
    private void controlActivityStart(){
        Intent controlIntent = new Intent(this,controlPanel.class);
        startActivity(controlIntent);
    }

    //progress spinner
    private ProgressDialog spinner(String status){
        ProgressDialog spinner = new ProgressDialog(RegisterUserActivity.this);
        spinner.setMessage(status); // Setting Message
        spinner.setTitle("Loading..."); // Setting Title
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        return spinner;
    }


}
