package com.ethtether.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("EthTether - 以太网共享服务已启动\n点击返回键退出");
        setContentView(tv);
        // 手动触发一次
        new TetherReceiver().onReceive(this, new Intent("com.ethtether.MANUAL"));
    }
}
