package com.mark.stylish;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class StylishReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.mark.stylish.start")) {
            intent.setClass(context, StylishService.class);
            context.startService(intent);
        } else if (intent.getAction().equals("com.mark.stylish.stop")) {
            intent.setClass(context, StylishService.class);
            context.stopService(intent);
        }
    }
}
