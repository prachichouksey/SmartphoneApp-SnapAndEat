package com.example.snapandeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class SignupActivity extends AppCompatActivity {

    EditText fname, lname, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fname = findViewById(R.id.user_firstName);
        lname = findViewById(R.id.user_lastName);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
    }

    public void submitSignup(View v){
        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);
        User user;

        String userFirstName = fname.getText().toString();
        String userLastName = lname.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if(userFirstName.equalsIgnoreCase("")){
            fname.setError("First Name is mandatory.");
        } else if(userLastName.equalsIgnoreCase("")){
            lname.setError("Last Name is mandatory.");
        } else if(userEmail.equalsIgnoreCase("")){
            email.setError("Email is mandatory.");
        } else if(userPassword.equalsIgnoreCase("")){
            password.setError("Password is mandatory.");
        } else {
            user = new User(0, userFirstName, userLastName, userEmail, userPassword);
            long insertResult = dbHelper.addUser(user);
            if(insertResult == -1){
                Snackbar.make(fname,
                        "Database insertion error",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    public void cancelSignup(View v){
        finish();
    }

}
