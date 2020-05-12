package com.stx.xhb.DCAPlatform.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.stx.xhb.DCAPlatform.utils.NetUtils;


public class NetStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //判断过来的广播，是否是我们想要的
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            if (NetUtils.isWiFi(context)) {
                Toast.makeText(context, "连接上wifi了", Toast.LENGTH_SHORT).show();
            } else if (NetUtils.isMobile(context)) {
                Toast.makeText(context, "连接上手机网络了", Toast.LENGTH_SHORT).show();
            } else if (!NetUtils.isNetworkAvailable(context)){
                Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
