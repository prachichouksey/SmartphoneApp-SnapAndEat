package com.example.snapandeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SnapAndEatDB";
    private static final String USER_TABLE_NAME = "Users";
    private static final String KEY_USERID = "id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String[] USER_COLUMNS = { KEY_USERID, KEY_FNAME, KEY_LNAME, KEY_EMAIL, KEY_PASSWORD};

    private static final String REPORT_TABLE_NAME = "Reports";
    private static final String KEY_REPORTID = "id";
    private static final String KEY_REPORTUSERID = "user_id";
    private static final String KEY_RESTAURANT = "restaurant";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_CARBS = "carbs";
    private static final String KEY_FATS = "fats";
    private static final String KEY_PROTEINS = "proteins";
    private static final String KEY_DATE = "date";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_IMAGE_URI = "image_uri";
    private static final String[] REPORT_COLUMNS = { KEY_REPORTID, KEY_REPORTUSERID, KEY_RESTAURANT, KEY_CALORIES,
            KEY_CARBS, KEY_FATS, KEY_PROTEINS, KEY_DATE, KEY_ITEM_NAME, KEY_IMAGE_URI };

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_USER_TABLE = "CREATE TABLE Users ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "fname TEXT, "
                + "lname TEXT, "
                + "email TEXT, "
                + "password TEXT)";

        db.execSQL(CREATION_USER_TABLE);

        String CREATION_REPORT_TABLE = "CREATE TABLE Reports ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER, "
                + "restaurant TEXT, "
                + "calories TEXT, "
                + "carbs TEXT, "
                + "fats TEXT, "
                + "proteins TEXT, "
                + "date TEXT, "
                + "item_name TEXT, "
                + "image_uri TEXT, "
                + "FOREIGN KEY(user_id) REFERENCES Users(id))";

        db.execSQL(CREATION_REPORT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REPORT_TABLE_NAME);
        this.onCreate(db);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, user.getFname());
        values.put(KEY_LNAME, user.getLname());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        // insert
        long insertResult = db.insertOrThrow(USER_TABLE_NAME,null, values);
        db.close();
        return insertResult;
    }

    public Cursor validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, // a. table
                USER_COLUMNS, // b. column names
                KEY_EMAIL + "=?" + " AND " + KEY_PASSWORD + "=?", // c. selections
                new String[] { email, password }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
//
//        User user = new User();
//        int rowId = 0;
//        String rowFname = null;
//        String rowLname = null;
//        String rowEmail = null;
//        String rowPassword = null;
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//            rowId = Integer.parseInt(cursor.getString(0));
//            rowFname = cursor.getString(1);
//            rowLname = cursor.getString(2);
//            rowEmail = cursor.getString(3);
//            rowPassword = cursor.getString(4);
//        }
//
//        user.setId(rowId);
//        user.setFname(rowFname);
//        user.setLname(rowLname);
//        user.setEmail(rowEmail);
//        user.setPassword(rowPassword);

        return cursor;
    }

    public long addReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REPORTUSERID, report.getUserId());
        values.put(KEY_RESTAURANT, report.getRestaurant());
        values.put(KEY_CALORIES, report.getCalories());
        values.put(KEY_CARBS, report.getCarbs());
        values.put(KEY_FATS, report.getFats());
        values.put(KEY_PROTEINS, report.getProteins());
        values.put(KEY_DATE, report.getDate());
        values.put(KEY_ITEM_NAME, report.getItemName());
        values.put(KEY_IMAGE_URI, report.getImageUri());
        // insert
        long insertResult = db.insertOrThrow(REPORT_TABLE_NAME,null, values);
        db.close();

        return insertResult;
    }

    public Cursor allReports(int userId) {

//        List<Report> reports = new LinkedList<Report>();
        String query = "SELECT  * FROM " + REPORT_TABLE_NAME + " WHERE " + KEY_REPORTUSERID + "=? ORDER BY " + KEY_DATE+ " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(userId) });
//        Report report;

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
//                reports.add(report);
//            } while (cursor.moveToNext());
//        }

        return cursor;
    }

}
