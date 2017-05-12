package trippin.trippinapp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String KEY_AttractionLocationX = "m_attractionLocationX"; // NEED TO CHANGE -LatLng
    private static final String KEY_AttractionLocationY = "m_attractionLocationY";  // NEED TO CHANGE - LatLng
    private static final String KEY_Image = "m_image"; // NEED TO CHANGE - BitmapDescriptor


    private static final String CREATE_ATTRCATIONS_DB="CREATE TABLE " +
            ATTRACTIONS_TABLE_NAME + "(" + KEY_ID + " TEXT PRIMARY KEY," +
            KEY_GoogleID + " TEXT NOT NULL," +
            KEY_Name + " TEXT NOT NULL," +
            KEY_Rate + " INTEGER NOT NULL," +
            KEY_StartDate + " DATETIME NOT NULL," +
            KEY_ENDDate + " DATETIME NOT NULL," +
            KEY_AttractionLocationX  + " ," +
            KEY_AttractionLocationY + " ," +
            KEY_Image + " " + ")";

    public MySQLHelper(Context context){
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

    public void AddAttraction(Attraction attraction){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,attraction.getID());
        values.put(KEY_GoogleID,attraction.getM_googleID());
        values.put(KEY_Name,attraction.getName());
        values.put(KEY_Rate,attraction.getRate());
        values.put(KEY_StartDate,attraction.getStartDate().toString());
        values.put(KEY_ENDDate,attraction.getEndDate().toString());
        //NEED TO ADD 3 MORE !!!!

    }
}
