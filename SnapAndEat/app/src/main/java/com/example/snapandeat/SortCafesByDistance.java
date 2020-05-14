package com.example.snapandeat;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class SortCafesByDistance implements Comparator<JSONObject> {
    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     * lhs- 1st message in the form of json object. rhs- 2nd message in the form
     * of json object.
     */
    @Override
    public int compare(JSONObject lhs, JSONObject rhs) {
        try {
            Log.d("Comparator", "Comparing " + lhs.getDouble("rating"));
            return lhs.getDouble("distance") > rhs.getDouble("distance") ? 1 : (lhs
                    .getDouble("distance") < rhs.getDouble("distance") ? -1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
