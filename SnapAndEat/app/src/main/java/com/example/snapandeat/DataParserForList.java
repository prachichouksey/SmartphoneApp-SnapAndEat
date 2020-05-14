package com.example.snapandeat;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DataParserForList {
    private static Location currentLocation;

    public DataParserForList(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    private HashMap<String, String> getSingleNearbyPlace(JSONObject googlePlaceJSON)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String photo_reference = "";
        String rating = "0";
        String distance = "0";
        String timing = "Open Now";
        String pricing = "-NA-";

        try
        {
            if (!googlePlaceJSON.isNull("name"))
            {
                NameOfPlace = googlePlaceJSON.getString("name");
            }
            if (!googlePlaceJSON.isNull("vicinity"))
            {
                vicinity = googlePlaceJSON.getString("vicinity");
            }

            if (!googlePlaceJSON.isNull("rating"))
            {
                rating = googlePlaceJSON.getString("rating");
            }

            if(!googlePlaceJSON.isNull("photos")){
                JSONArray photos = googlePlaceJSON.getJSONArray("photos");

                photo_reference = ((JSONObject)photos.get(0)).getString("photo_reference");
            }
            if(!googlePlaceJSON.getJSONObject("opening_hours").getBoolean("open_now")){
                timing = "Closed";
            }
            if (!googlePlaceJSON.isNull("price_level"))
            {
                pricing = googlePlaceJSON.getString("price_level");
            }
            distance = googlePlaceJSON.getString("distance");
            googlePlaceMap.put("place_name", NameOfPlace);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("photo_reference", photo_reference);
            googlePlaceMap.put("distance", distance);
            googlePlaceMap.put("timing", timing);
            googlePlaceMap.put("pricing", pricing);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }



    private List<HashMap<String, String>> getAllNearbyPlaces(JSONArray jsonArray)
    {
        int counter = jsonArray.length();

        List<HashMap<String, String>> NearbyPlacesList = new ArrayList<>();

        HashMap<String, String> NearbyPlaceMap = null;

        for (int i=0; i<counter; i++)
        {

            try
            {
                JSONObject googlePlaceJSON = (JSONObject) jsonArray.get(i);
                if(googlePlaceJSON.isNull("permanently_closed")){

                        NearbyPlaceMap = getSingleNearbyPlace( (JSONObject) jsonArray.get(i) );
                        NearbyPlacesList.add(NearbyPlaceMap);

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return NearbyPlacesList;
    }



    public List<HashMap<String, String>> parse(String jSONdata)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(jSONdata);
            jsonArray = jsonObject.getJSONArray("results");
            jsonArray = getSortedList(jsonArray);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return getAllNearbyPlaces(jsonArray);
    }

//    public static JSONArray getSortedList(JSONArray array) throws JSONException {
//        List<JSONObject> list = new ArrayList<JSONObject>();
//        for (int i = 0; i < array.length(); i++) {
//            list.add(array.getJSONObject(i));
//        }
//        Collections.sort(list, new SortCafesByRating());
//
//        JSONArray resultArray = new JSONArray(list);
//
//        return resultArray;
//    }

    public static JSONArray getSortedList(JSONArray array) throws JSONException {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject googlePlaceJSON = array.getJSONObject(i);
            String latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            float newLat = Float.parseFloat(latitude);
            float newLng = Float.parseFloat(longitude);
            float currLat = (float) currentLocation.getLatitude();
            float currLng = (float) currentLocation.getLongitude();
            String radius = getMilesFromLatLong(currLat, currLng, newLat, newLng);
            googlePlaceJSON.put("distance",radius);
            list.add(googlePlaceJSON);
        }
        Collections.sort(list, new SortCafesByDistance());

        JSONArray resultArray = new JSONArray(list);

        return resultArray;
    }

    public static String getMilesFromLatLong(float lat1, float lng1, float lat2, float lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        return df.format(distanceInMeters * 0.000621371192);
    }
}
