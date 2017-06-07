package trippin.trippinapp.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tacco on 5/12/17.
 */

public class Settings {
    static SharedPreferences preferences;
    static private String KEY = "trippin.PREFERENCE_FILE_KEY";

    public Settings(Context context) {
        Settings.preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static Integer getIntegerSettings(String key) {
        return preferences.getInt(key, -999);
    }

    public static Float getFloatSettings(String key) {
        return preferences.getFloat(key, -999);
    }

    public static Boolean getBooleanSettings(String key) {
        return preferences.getBoolean(key, false);
    }

    public static String getStringSettings(String key) {
        return preferences.getString(key, null);
    }
}
