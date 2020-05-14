package com.example.snapandeat;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.snapandeat.ui.main.ReportsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {
    ReportsPagerAdapter reportsPagerAdapter;
    ViewPager viewPager;
    boolean doubleBackToExitPressedOnce=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initSetup();
    }

    public void initSetup() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startImageActivity();
            }
        });

//        Spinner spinner = findViewById(R.id.spinner);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("JAVA");
//        arrayList.add("ANDROID");
//        arrayList.add("C Language");
//        arrayList.add("CPP Language");
//        arrayList.add("Go Language");
//        arrayList.add("AVN SYSTEMS");
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(arrayAdapter);

        reportsPagerAdapter = new ReportsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.reports_view_pager);
        viewPager.setAdapter(reportsPagerAdapter);
        TabLayout tabs = findViewById(R.id.reports_tabs);
//        tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
//        tabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        tabs.setupWithViewPager(viewPager);

//        totalItemsView = findViewById(R.id.totalItems);
//        totalCaloriesView = findViewById(R.id.totalCalories);
//
//        SharedPreferences mSharedPreferences = getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
//        int userId = mSharedPreferences.getInt("userId",0);
//
//        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);
//
//        Cursor cursor = dbHelper.allReports(userId);
//        ArrayList<Report> reports = new ArrayList<>();
//        Report report;
//        int totalItems = 0;
//        int totalCalories = 0;
//
//        if (cursor.moveToFirst()) {
//            do {
//                report = new Report();
//                report.setId(Integer.parseInt(cursor.getString(0)));
//                report.setUserId(Integer.parseInt(cursor.getString(1)));
//                report.setRestaurant(cursor.getString(2));
//                report.setCalories(cursor.getString(3));
//                report.setCarbs(cursor.getString(4));
//                report.setFats(cursor.getString(5));
//                report.setProteins(cursor.getString(6));
//                report.setDate(cursor.getString(7));
//                report.setItemName(cursor.getString(8));
//                report.setImageUri(cursor.getString(9));
//                reports.add(report);
//
//                totalItems += 1;
//                totalCalories += Integer.parseInt(cursor.getString(3));
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        totalItemsView.setText(Integer.toString(totalItems));
//        totalCaloriesView.setText(Integer.toString(totalCalories));
//
//        ReportListFragment reportListFragment = ReportListFragment.newInstance(reports);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.report_container,reportListFragment)
//                .commit();

    }

    public void startImageActivity() {
        Intent i = new Intent(this, ImageActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onResume(){
        super.onResume();

        SharedPreferences mSharedPreferences = getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
        int userId = mSharedPreferences.getInt("userId",0);

        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);

        Cursor cursor = dbHelper.allReports(userId);
        ArrayList<Report> reports = new ArrayList<>();
        Report report;
        int totalItems = 0;
        float totalCalories = 0.0f;
        float totalCarbs = 0.0f;
        float totalFats = 0.0f;
        float totalProteins = 0.0f;
        if (cursor.moveToFirst()) {
            do {
                report = new Report();
                report.setId(Integer.parseInt(cursor.getString(0)));
                report.setUserId(Integer.parseInt(cursor.getString(1)));
                report.setRestaurant(cursor.getString(2));
                report.setCalories(cursor.getString(3));
                report.setCarbs(cursor.getString(4));
                report.setFats(cursor.getString(5));
                report.setProteins(cursor.getString(6));
                String date =cursor.getString(7);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date parsedDate = sdf.parse(date);
                    date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(parsedDate);
                    report.setDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                report.setItemName(cursor.getString(8));
                report.setImageUri(cursor.getString(9));
                reports.add(report);

                totalItems += 1;
                totalCalories += Float.parseFloat(cursor.getString(3));
                totalCarbs += Float.parseFloat(cursor.getString(4));
                totalFats += Float.parseFloat(cursor.getString(5));
                totalProteins += Float.parseFloat(cursor.getString(6));

            } while (cursor.moveToNext());
        }

        cursor.close();

        Log.d("totalitems --", Integer.toString(totalItems));

        reportsPagerAdapter.setData(reports, totalItems, totalCalories, totalCarbs, totalFats, totalProteins);
        reportsPagerAdapter.notifyDataSetChanged();
//        ReportListFragment reportListFragment = ReportListFragment.newInstance(reports);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.report_container,reportListFragment)
//                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            clearSharedPreferences();
            logout();
            return true;
        }
        else if(id==R.id.action_exit){
            clearSharedPreferences();
            //System.exit(0);
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }
        else if(id==R.id.action_report){
            Intent i = new Intent(this, ReportActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        else if(id==R.id.action_image){
            Intent i = new Intent(this, ImageActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void clearSharedPreferences(){
        SharedPreferences preferences =getSharedPreferences("loginPreference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            clearSharedPreferences();
//
////            System.exit(0);
////            finish();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ReportActivity.this.doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
