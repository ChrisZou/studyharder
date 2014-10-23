/**
 * MonitorService.java
 *
 * Created by zouyong on Oct 8, 2014,2014
 */
package com.chriszou.studyharder;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.chriszou.androidlibs.Prefs;

import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zouyong
 *
 */
public class MonitorService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppMonitor.getInstance(this).startMonitoring();
        return START_STICKY;
    }

    /* (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(AppMonitor.shouldMonitor()) {
            Intent i = new Intent();
            i.setAction(getString(R.string.keep_service));
            sendBroadcast(i);
        } else {
            AppMonitor.getInstance(this).stopMonitoring();
        }
    }

}
