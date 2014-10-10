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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zouyong
 * 
 */
public class MonitorService extends Service implements MonitoringApp.OnAppsChangedListener{
	private static final int MSG_CHECK_ACTIVITY = 0;

	/**
	 * The target app and activity to start  when the foreground activity is the one we are monitoring
	 */
	private static final String TARGET_PACKAGE = "com.chriszou.words";
	private static final String TARGET_ACTIVITY = TARGET_PACKAGE+".QuickReviewActivity_";

	private boolean mInited = false;
//	private Map<String, String> mTargetApps;

	/**
	 * We have to prevent the case that the same app showing all the time, and we check it continuously.
	 */
	private String mPrevousApp;
	private String mCurrerntApp;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHECK_ACTIVITY:
				checkActivity();
				mHandler.sendEmptyMessageDelayed(MSG_CHECK_ACTIVITY, 1000);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * The foreground activity is one of the target activity
	 */
	private void onHit(String packageName) {
		if(!packageName.equals(mPrevousApp)) {
			Intent startIntent = new Intent();
			ComponentName componentName = new ComponentName(TARGET_PACKAGE, TARGET_ACTIVITY);
			startIntent.setComponent(componentName);
			//startIntent.setClassName("com.chriszou.words", "ReviewActivity_.class");
			startIntent.putExtra("extra_bool_quick_review", true);
			startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			startActivity(startIntent);
		}
	}

	/**
	 * Check the foreground activity
	 */
	private void checkActivity() {
		ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
		String pckName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        //L.l("current app: "+pckName);

		//If the current app is this app, then skip
		if(pckName.equals(getPackageName())) {
			return;
		}

		//If the current app is the target app, then skip
		if(pckName.equals(TARGET_PACKAGE)) {
			return;
		}

		mPrevousApp = mCurrerntApp;
		mCurrerntApp = pckName;

		if(mMonitoredApps.contains(mCurrerntApp)) {
			onHit(mCurrerntApp);
		}
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!mInited) {
			init();
			startMonitoring();
			mInited = true;
		}
		return START_STICKY;
	}

	private void init() {
        loadMonitoredApps();
        MonitoringApp.registerOnAppsChangedListener(this);
	}

	private void loadMonitoredApps() {
        mMonitoredApps.clear();
        List<MonitoringApp> apps = MonitoringApp.all();
        for(MonitoringApp app : apps) {
            mMonitoredApps.add(app.pkgName);
        }
	}

	private void startMonitoring() {
		mHandler.sendEmptyMessageDelayed(MSG_CHECK_ACTIVITY, 1000);
	}

    @Override
    public void onDestroy() {
        MonitoringApp.unregisterOnAppsChangedListener(this);
        startService(new Intent(this, MonitorService.class));
        super.onDestroy();
    }

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

    List<String> mMonitoredApps = new ArrayList<String>();
    @Override
    public void onAppsChanged() {
        loadMonitoredApps();
    }
}
