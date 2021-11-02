package com.mark.stylish;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
