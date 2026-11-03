package com.ethtether.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("EthTether", "Boot completed, starting tether...");
        // 延迟 15 秒等系统就绪
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(
                () -> new TetherReceiver().onReceive(context, intent), 15000);
    }
}
