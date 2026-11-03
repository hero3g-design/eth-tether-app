package com.ethtether.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("EthTether", "Boot completed, starting tether...");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new TetherReceiver().onReceive(context, new Intent("com.ethtether.MANUAL"));
            }
        }, 15000);
    }
}
