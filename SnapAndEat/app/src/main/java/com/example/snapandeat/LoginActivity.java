package com.example.snapandeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
    }

    public void submitLogin(View v){
        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);
        User user;

        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if(userEmail.equalsIgnoreCase("")){
            email.setError("Email is mandatory.");
        } else if(userPassword.equalsIgnoreCase("")){
            password.setError("Password is mandatory.");
        } else {
            Cursor cursor = dbHelper.validateUser(userEmail, userPassword);

            if(cursor.moveToFirst()){
                SharedPreferences mSharedPreferences = getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                mEditor.putInt("userId",Integer.parseInt(cursor.getString(0)));
                mEditor.putString("fname",cursor.getString(1));
                mEditor.putString("lname",cursor.getString(1));
                mEditor.apply();

                cursor.close();
                Intent i = new Intent(this, ReportActivity.class);
                startActivity(i);
                finish();
            } else {
                Snackbar.make(email,
                        "Wrong email and/or password",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void cancelLogin(View v){

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
