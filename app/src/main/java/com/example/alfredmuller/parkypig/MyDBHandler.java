package com.example.alfredmuller.parkypig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


public class MyDBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PastParking";
    public static final String TABLE_NAME = "coordinates";

    public static final String KEY_ID = "id";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    String LONGI = "longitude";
    String LATI = "latitude";

    Context context;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MyDBHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, String.valueOf(location.getLatitude()));
        values.put(KEY_LONGITUDE, String.valueOf(location.getLongitude()));

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<Location>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setID(Integer.parseInt(cursor.getString(0)));
                System.out.println(Integer.parseInt(cursor.getString(0)));
                location.setLatitude(Double.parseDouble(cursor.getString(1)));
                System.out.println(Integer.parseInt(cursor.getString(1)));
                location.setLongitude(Double.parseDouble(cursor.getString(2)));
                System.out.println(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        // return location list
        return locationList;
    }

    Location getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_LATITUDE, KEY_LONGITUDE }, KEY_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Location location = new Location();
        location.setID(Integer.parseInt(cursor.getString(0)));
        location.setLatitude(Double.parseDouble(cursor.getString(1)));
        location.setLongitude(Double.parseDouble(cursor.getString(2)));

        return location;
    }


    public Location findProduct(int ID) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " =  \"" + ID + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Location location = new Location();

        if (cursor.moveToFirst()) {
            System.out.println("made it");
            cursor.moveToFirst();
            location.setID(Integer.parseInt(cursor.getString(0)));
            location.setLatitude(Double.parseDouble(cursor.getString(1)));
            location.setLongitude(Double.parseDouble(cursor.getString(2)));
            cursor.close();
        } else {
            System.out.println("Bummer");
            location = null;
        }
        db.close();
        return location;
    }

    public boolean deleteProduct(String productId) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " =  \"" + productId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Location location = new Location();

        if (cursor.moveToFirst()) {
            location.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, KEY_ID + " = ?",
                    new String[] { String.valueOf(location.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public int getLocationsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

}