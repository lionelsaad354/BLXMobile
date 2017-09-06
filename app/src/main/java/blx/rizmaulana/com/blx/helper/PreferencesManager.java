package blx.rizmaulana.com.blx.helper;

import android.content.Context;
import android.content.SharedPreferences;

/*
* Created by : Rizki Maulana
* email : rizmaulana@live.com
* Class ini adalah class yang berisi fungsi untuk menyimpan dan mengambil value preference.
*
* */

public class PreferencesManager {

    public static void set(Context context, String pname, String str){
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(pname, str);
        editor.apply();
    }

    public static void set(Context context, String pname, boolean str){
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(pname, str);
        editor.apply();
    }


    public static String getString(Context context, String pname){
        String data = "";
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        data = sharedPref.getString(pname, "");
        return data;
    }

    public static boolean getBoolean(Context context, String pname){
        boolean data;
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        data = sharedPref.getBoolean(pname, false);
        return data;
    }

    public static void clear(Context context){
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
