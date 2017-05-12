package trippin.trippinapp.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.prefs.Preferences;

import trippin.trippinapp.R;

/**
 * Created by tacco on 5/12/17.
 */

public class Settings {
    static SharedPreferences preferences;
    static private String KEY = "trippin.PREFERENCE_FILE_KEY";

    public Settings(Context context) {
        Settings.preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

//    public static Boolean SaveSettings(String key, Object value)
//    {
//        SharedPreferences.Editor editor = preferences.edit();
//
//        if (value instanceof Integer)
//        {
//            editor.putInt(key, (int)value);
//        }
//        else if (value instanceof String)
//        {
//            editor.putString(key, value.toString());
//        }
//        else if (value instanceof Boolean)
//        {
//            editor.putBoolean(key, (boolean)value);
//        }
//        else if (value instanceof Float)
//        {
//            editor.putFloat(key, (float)value);
//        }
//        else
//        {
//            return false;
//        }
//
//        editor.commit();
//        return true;
//    }

    public static Integer GetIntegerSettings(String key)
    {
        return preferences.getInt(key, -999);
    }

    public static Float GetFloatSettings(String key)
    {
        return preferences.getFloat(key, -999);
    }

    public static Boolean GetBooleanSettings(String key)
    {
        return preferences.getBoolean(key, false);
    }

    public static String GetStringSettings(String key)
    {
        return preferences.getString(key, null);
    }
}
