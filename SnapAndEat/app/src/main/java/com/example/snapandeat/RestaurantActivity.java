package com.example.snapandeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.snapandeat.ui.main.SectionsPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import com.example.snapandeat.ListViewFragment.OnFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RestaurantActivity extends AppCompatActivity implements MapViewFragment.OnLocationChangedListener,
        OnFragmentInteractionListener{
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    CoordinatorLayout coordinatorLayout;
    SectionsPagerAdapter sectionsPagerAdapter;
    String nearByPlacesJson = "";
    ViewPager viewPager;
    Location mLocation;
    GoogleMap mMap;
    String itemName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        coordinatorLayout =  findViewById(R.id.coordinatorLayout1);

        getSupportActionBar().hide();
        if(!checkPlayServices()){
            Snackbar.make(coordinatorLayout, "Google play service issue", Snackbar.LENGTH_LONG).show();
        }
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), nearByPlacesJson);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Bundle extras = getIntent().getExtras();
        itemName = extras.getString("itemName");
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
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
//            System.exit(0);
//            return true;
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
    public void onLocationChanged(Location location, GoogleMap map) {
        mLocation = location;
        mMap = map;
        if(isConnectedToNetwork(this)){
            mMap.clear();
            Object transferData[] = new Object[4];
            GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
            String url = constructUrl(mLocation.getLatitude(),mLocation.getLongitude());
            transferData[0] = mMap;
            transferData[1] = url;
            transferData[2] = sectionsPagerAdapter;

            getNearbyPlaces.execute(transferData);

            Snackbar.make(coordinatorLayout, "Getting and displaying nearby Cafes", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(coordinatorLayout, "No Active Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private String constructUrl(double latitude, double longitude)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + 3000);
        googleURL.append("&type=restaurant");
//        googleURL.append("&keyword=" + search_txt.getText() );
        googleURL.append("&keyword=" + itemName );
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyCD0GTdc64QDR_WojsuRpCYArwae25_f00");

        return googleURL.toString();
    }

    public boolean isConnectedToNetwork(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if (capabilities != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSaveReport(String restaurant) {
        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());

//        String date = java.text.DateFormat.getDateTimeInstance().format(new Date());

        SharedPreferences mSharedPreferences = getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
        int userId = mSharedPreferences.getInt("userId",0);
        String itemName = getIntent().getExtras().getString("itemName");
        String imageUri = getIntent().getExtras().getString("imageUri");
        String calories = getIntent().getExtras().getString("itemCalorie");
        String carbs =getIntent().getExtras().getString("itemCarbs");
        String fats = getIntent().getExtras().getString("itemFats");
        String proteins = getIntent().getExtras().getString("itemProteins");

        Report report = new Report(0, userId, restaurant, calories, carbs, fats, proteins, date, itemName, imageUri);

        long insertResult = dbHelper.addReport(report);

        if(insertResult == -1){
            Snackbar.make(coordinatorLayout,
                    "Database insertion error",
                    Snackbar.LENGTH_SHORT)
                    .show();
        } else {
//            Intent i = new Intent(this, LoginActivity.class);
//            startActivity(i);
//            finish();
            Snackbar.make(coordinatorLayout,
                    "Successfully added to report",
                    Snackbar.LENGTH_SHORT)
                    .show();
            Intent i = new Intent(this, ReportActivity.class);
            startActivity(i);
            finish();
        }
    }
}
