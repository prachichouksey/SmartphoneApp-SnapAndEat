package com.example.snapandeat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.snapandeat.env.Classifier;
import com.example.snapandeat.env.TensorFlowImageClassifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageActivity extends AppCompatActivity {
    ImageView imagePreview;
    TextView itemName, itemAccuracy, itemCalories, itemCarbs, itemFats, itemProteins;
    Button searchNearByRestaurants;
    Uri imageUri = null;

    String itemCalorieVal = null;
    String itemCarbsVal = null;
    String itemFatsVal = null;
    String itemProteinsVal = null;

    private Executor executor = Executors.newSingleThreadExecutor();
    public static final float PREDICTION_THRESHOLD = (float) 0.60;
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";
    private static final String NUTRI_STATS_FILE = "file:///android_asset/nutrition_stats.txt";

    private Classifier classifier;
    List<Classifier.Recognition> predResults = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imagePreview = findViewById(R.id.imgPreview);
        itemName = findViewById(R.id.itemName);
        itemAccuracy = findViewById(R.id.itemAccuracy);
        itemCalories = findViewById(R.id.itemCalories);
        itemCarbs = findViewById(R.id.itemCarbs);
        itemFats= findViewById(R.id.itemFats);
        itemProteins = findViewById(R.id.itemProteins);
        searchNearByRestaurants = findViewById(R.id.search_button);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initTensorFlowAndLoadModel();

        selectImage();
//        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));

    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image to classify");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                    finish();
                }
            }
        });
        builder.show();
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
        SharedPreferences preferences =getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && data != null){
                if(requestCode == 0){
                    imageUri = data.getData();
                } else if(requestCode == 1){
                    imageUri = data.getData();
                }
            }
        }

        try{
            Log.d("Test", "Inside try");
            InputStream imageStream = getContentResolver().openInputStream(imageUri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            imagePreview.setImageBitmap(selectedImage);
            Log.d("Test", "Image set");
            classifyImage(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Snackbar.make(imagePreview, "Something went wrong.", Snackbar.LENGTH_LONG).show();
        }
    }

    public void backPressed(View view) {
        finish();
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    classifier =
                            TensorFlowImageClassifier.create(
                                    getAssets(),
                                    MODEL_FILE,
                                    LABEL_FILE,
                                    INPUT_SIZE,
                                    IMAGE_MEAN,
                                    IMAGE_STD,
                                    INPUT_NAME,
                                    OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    public void classifyImage(Bitmap bitmap){
        Bitmap croppedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
        predResults = classifier.recognizeImage(croppedBitmap);

        if(predResults.get(0).getConfidence() > PREDICTION_THRESHOLD){
            setValues();
        } else {
//            itemName.setText("Unable to predict");
//            searchNearByRestaurants.setVisibility(View.INVISIBLE);
            showUnknownClassPopup();
        }
    }

    public void setValues() {
        itemName.setText(predResults.get(0).getTitle().toUpperCase());
        itemAccuracy.setText(predResults.get(0).getConfidence().toString());
        String actualFilename = NUTRI_STATS_FILE.split("file:///android_asset/")[1];
        BufferedReader br;
        try {
            JSONArray jsonArray;
            br = new BufferedReader(new InputStreamReader(getAssets().open(actualFilename)));
            String line;
            while ((line = br.readLine()) != null) {
                jsonArray = new JSONArray(line);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    if(predResults.get(0).getTitle().toLowerCase().contains(explrObject.getString("item").toLowerCase())){
                        itemCalorieVal = explrObject.getString("calories");
                        itemCalories.setText(itemCalorieVal);

                        itemCarbsVal = explrObject.getString("carbs");
                        itemCarbs.setText(itemCarbsVal + " gms");

                        itemFatsVal = explrObject.getString("fat");
                        itemFats.setText(itemFatsVal + " gms");

                        itemProteinsVal = explrObject.getString("protein");
                        itemProteins.setText(itemProteinsVal + " gms");
                        break;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Problem reading label file!" , e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void searchRestaurants(View view){
        Intent i = new Intent(this, RestaurantActivity.class);
        i.putExtra("itemName",itemName.getText());
        i.putExtra("imageUri", imageUri.toString());
        i.putExtra("itemCalorie", itemCalorieVal);
        i.putExtra("itemCarbs", itemCarbsVal);
        i.putExtra("itemFats", itemFatsVal);
        i.putExtra("itemProteins", itemProteinsVal);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void showUnknownClassPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_unknown_class, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.edit_unknown_class);

        dialogBuilder.setTitle("Item not found");
        dialogBuilder.setMessage("Unable to predict this item. \n\nTrying hard to make myself better.");
        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String editV = edt.getText().toString();
                if(editV.length() > 0){
                    String mailto = "mailto:wamique.ansari@sju.edu";

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse(mailto));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Add "+editV);

                    try {
                        startActivity(emailIntent);
                    } catch (ActivityNotFoundException e) {
                        //TODO: Handle case where no email app is available
                    }

                    Toast.makeText(getApplicationContext(),
                            "Thanks.", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Item name not added.", Toast.LENGTH_SHORT)
                            .show();
                    showUnknownClassPopup();
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                Toast.makeText(getApplicationContext(),"Ooops, it hurts!!", Toast.LENGTH_SHORT).show();
                setValues();
                finish();
            }
        });

        dialogBuilder.setNeutralButton("Retake", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                String lbls = MyUtility.getLabels(getApplicationContext());
//                AlertDialog alertDialog = new AlertDialog.Builder(CameraActivity.this).create();
//                alertDialog.setTitle("I can predict these items");
//                alertDialog.setMessage(lbls);
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//                finish();
                selectImage();
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
