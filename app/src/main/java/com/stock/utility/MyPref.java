package com.stock.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.stock.R;




public class MyPref {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyPref(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setData(Keys keys, String value) {
        editor.putString(keys.name(), value);
        editor.commit();
    }

    public void setData(Keys keys, boolean value) {
        editor.putBoolean(keys.name(), value);
        editor.commit();
    }

    public String getData(Keys keys) {
        return sharedPreferences.getString(keys.name(), "");
    }

    public boolean getData(Keys keys, boolean flag) {
        return sharedPreferences.getBoolean(keys.name(), flag);
    }

    public enum Keys {
        ISLOGIN, IMAGEURL, USERDATA, TOKEN, PROFILE, NOTIFICATIONDATA
    }
}
