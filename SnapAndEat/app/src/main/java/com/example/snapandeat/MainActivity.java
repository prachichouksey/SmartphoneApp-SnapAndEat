package com.example.snapandeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10;
    boolean permissionGranted;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        coordinatorLayout =  findViewById(R.id.coordinatorLayout);
        getSupportActionBar().hide();
        permissionGranted = checkAndRequestPermissions();
        if(!permissionGranted) {
            Snackbar.make(coordinatorLayout, "Permission required to run this app properly.", Snackbar.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_items, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_logout) {
//            clearSharedPreferences();
//            logout();
//            return true;
//        }
//        else if(id==R.id.action_exit){
//            clearSharedPreferences();
//            System.exit(0);
//            return true;
//        }
//        else if(id==R.id.action_report){
//            Intent i = new Intent(this, ReportActivity.class);
//            startActivity(i);
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void logout(){
//        Intent i = new Intent(this, LoginActivity.class);
//        startActivity(i);
//        finish();
//    }
//
//    private void clearSharedPreferences(){
//        SharedPreferences preferences =getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();
//    }

    public void handleLogin(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void handleSignup(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    private  boolean checkAndRequestPermissions() {
        int permissionWriteExSto = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (permissionWriteExSto != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        permissionGranted = true;
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                    permissionGranted = true;
                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }
                    // permissions list of don't granted permission
                }
                return;
            }
        }
    }
}
