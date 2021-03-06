package trippin.trippinapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import trippin.trippinapp.model.Attraction;


public class MySQLHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "UserAttractionsDB";
    //Table Name
    private static final String ATTRACTIONS_TABLE_NAME = "UserAttractionsTable";
    //Keys name
    private static final String KEY_ID = "m_id";
    private static final String KEY_GoogleID = "m_googleID";
    private static final String KEY_Name = "m_name";
    private static final String KEY_Rate = "m_rate"; //int
    private static final String KEY_StartDate = "m_startDate"; //dateTime
    private static final String KEY_ENDDate = "m_endDate"; //dateTime
    private static final String KEY_AttractionLocationLat = "m_attractionLocationLat"; // double - both need to be one LanLng
    private static final String KEY_AttractionLocationLng = "m_attractionLocationLng"; // double
    private static final String KEY_Image = "m_image"; // BitmapDescriptor

    private static final String CREATE_ATTRCATIONS_DB = "CREATE TABLE " +
            ATTRACTIONS_TABLE_NAME + "(" + KEY_ID + " TEXT PRIMARY KEY," +
            KEY_GoogleID + " TEXT," +
            KEY_Name + " TEXT NOT NULL," +
            KEY_Rate + " INTEGER NOT NULL," +
            KEY_StartDate + " DATETIME," +
            KEY_ENDDate + " DATETIME," +
            KEY_AttractionLocationLat + " REAL NOT NULL," +
            KEY_AttractionLocationLng + " REAL NOT NULL," +
            KEY_Image + " TEXT NOT NULL" + ")";

    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ATTRCATIONS_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ATTRACTIONS_TABLE_NAME);

        // create new tables
        onCreate(db);
    }

    public void deleteDataFromTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ATTRACTIONS_TABLE_NAME);

        // create new tables
        onCreate(db);
    }

    public void addAttraction(Attraction attraction) {
        SQLiteDatabase db = this.getWritableDatabase();

        double lat = attraction.getAttractionLocation().latitude;
        double lng = attraction.getAttractionLocation().longitude;
        String startDate = attraction.getStartDate() != null ? attraction.getStartDate().toString() : null;
        String endDate = attraction.getEndDate() != null ? attraction.getEndDate().toString() : null;

        ContentValues values = new ContentValues();
        values.put(KEY_ID, attraction.getID());
        values.put(KEY_GoogleID, attraction.getM_googleID());
        values.put(KEY_Name, attraction.getName());
        values.put(KEY_Rate, attraction.getRate());
        values.put(KEY_StartDate,startDate);
        values.put(KEY_ENDDate, endDate);
        values.put(KEY_AttractionLocationLat, lat);
        values.put(KEY_AttractionLocationLng, lng);
        values.put(KEY_Image, attraction.getImage().toString());

        db.insert(ATTRACTIONS_TABLE_NAME, null, values);

    }

    public ArrayList<Attraction> getAttractions() {

        String getTable_attracations = "SELECT * FROM " + ATTRACTIONS_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getTable_attracations, null);
        ArrayList<Attraction> Arryattractions = new ArrayList<Attraction>();
        Attraction tempAttraction;


        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            do {
                try {
                    tempAttraction = readAttractionToObject(cursor);

                    Arryattractions.add(tempAttraction);
                }catch (Exception e){

                }

            } while (cursor.moveToNext());
        }

        return Arryattractions;
    }

    private Attraction readAttractionToObject(Cursor cursor) {

        Attraction convertObj = new Attraction();


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        String start = cursor.getString(cursor.getColumnIndex(KEY_StartDate));
        String end = cursor.getString(cursor.getColumnIndex(KEY_ENDDate));


        Date startDate = null;
        try {
           if (start != null) { startDate = format.parse(start); } ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date EndDate = null;
        try {
            if (end != null) { EndDate = format.parse(end); };
        } catch (Exception e) {
            e.printStackTrace();
        }

        double lat = cursor.getDouble((cursor.getColumnIndex(KEY_AttractionLocationLat)));
        double lng = cursor.getDouble((cursor.getColumnIndex(KEY_AttractionLocationLng)));
        LatLng location = new LatLng(lat, lng);
        String image = cursor.getString(cursor.getColumnIndex(KEY_Image));

        convertObj.setID(cursor.getString(cursor.getColumnIndex(KEY_ID)));
        convertObj.setM_googleID(cursor.getString(cursor.getColumnIndex(KEY_GoogleID)));
        convertObj.setName(cursor.getString(cursor.getColumnIndex(KEY_Name)));
        convertObj.setRate(cursor.getInt(cursor.getColumnIndex(KEY_Rate)));
        convertObj.setStartDate(startDate);
        convertObj.setEndDate(EndDate);
        convertObj.setAttractionLocation(location);
        convertObj.setImage(image);

        return convertObj;
    }

    private boolean updateTable(Attraction attraction) {

        ContentValues values = new ContentValues();
        values.put(KEY_StartDate, String.valueOf(attraction.getStartDate()));
        values.put(KEY_ENDDate, String.valueOf(attraction.getEndDate()));
        values.put(KEY_Rate, attraction.getRate());
        boolean update;

        try {
            // updating row
            SQLiteDatabase db = this.getWritableDatabase();
            db.update(ATTRACTIONS_TABLE_NAME, values, KEY_ID + " = ?", new String[]{attraction.getID()});
            update = true;
        } catch (Exception e) {
            update = false;
        }

        return update;
    }


}
