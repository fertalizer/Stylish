package com.mark.stylish;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

// Singleton class
public class UserManager {
    private static UserManager mInstance;
    private SharedPreferences mSharedPreferences;

    private UserManager() {

    }

    public static UserManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserManager();
        }
        return mInstance;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public void setSharedPreferences(String accessToken) {
        mSharedPreferences = MyApp.getContext().getSharedPreferences("access_token", MODE_PRIVATE);
        mSharedPreferences.edit().putString("HashKey", accessToken).commit();
    }

    public String getStylishToken() {
        String stylishToken = MyApp.getContext().getSharedPreferences("access_token", MODE_PRIVATE).getString("HashKey", "");
        return stylishToken;
    }

}
