package com.chriszou.studyharder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zouyong on 10/23/14.
 */
public class ServiceDestroyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MonitorService.class);
        context.startService(serviceIntent);
    }
}
