package com.ethtether.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TetherReceiver extends BroadcastReceiver {
    private static final String TAG = "EthTether";
    private static final int TETHERING_ETHERNET = 5;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast received: " + intent.getAction());
        new Thread(() -> doTether(context)).start();
    }

    private void doTether(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // 用动态代理创建 OnStartTetheringCallback
            Class<?> cbClass = Class.forName(
                    "android.net.ConnectivityManager$OnStartTetheringCallback");

            CountDownLatch latch = new CountDownLatch(1);
            AtomicBoolean ok = new AtomicBoolean(false);

            Object callback = java.lang.reflect.Proxy.newProxyInstance(
                    cbClass.getClassLoader(),
                    new Class<?>[]{cbClass},
                    (proxy, method, args) -> {
                        Log.d(TAG, "Callback: " + method.getName());
                        if ("onTetheringStarted".equals(method.getName())) {
                            ok.set(true);
                        }
                        latch.countDown();
                        return null;
                    });

            // 找 startTethering 方法
            Method m = null;
            for (Method mm : cm.getClass().getDeclaredMethods()) {
                if ("startTethering".equals(mm.getName())
                        && mm.getParameterTypes().length == 4) {
                    m = mm;
                    break;
                }
            }

            if (m == null) {
                Log.e(TAG, "startTethering method not found!");
                return;
            }

            Log.d(TAG, "Calling startTethering(5)...");
            m.invoke(cm, TETHERING_ETHERNET, false, callback,
                    new Handler(Looper.getMainLooper()));

            latch.await(10, TimeUnit.SECONDS);
            Log.d(TAG, ok.get() ? "SUCCESS" : "NO_CALLBACK");

        } catch (Throwable e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
    }
}
