package com.chriszou.studyharder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.chriszou.androidlibs.Prefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zouyong on 10/23/14.
 */
public class AppMonitor implements MonitoringApp.OnAppsChangedListener {

	private static final int MSG_CHECK_ACTIVITY = 0;

	/**
	 * The target app and activity to start  when the foreground activity is the one we are monitoring
	 */
	private static final String TARGET_PACKAGE = "com.chriszou.words";
	private static final String TARGET_ACTIVITY = TARGET_PACKAGE+".QuickReviewActivity_";
    private static final String PREF_BOOL_SHOULD_MONITOR = "pref_bool_should_monitor";

    private boolean mMonitoring = false;

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
                if(mMonitoring) {
                    mHandler.sendEmptyMessageDelayed(MSG_CHECK_ACTIVITY, 1000);
                }
				break;
			default:
				break;
			}
		}
	};

    private Context mContext;
    private AppMonitor(Context context) {
        mContext = context;
    }

    private static AppMonitor sAppMonitor;
    public static AppMonitor getInstance(Context context) {
        if (sAppMonitor == null) {
            sAppMonitor = new AppMonitor(context);
        }
        return sAppMonitor;
    }

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
			mContext.startActivity(startIntent);
		}
	}

	/**
	 * Check the foreground activity
	 */
	private void checkActivity() {
		ActivityManager am = (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);
		String pckName = am.getRunningTasks(1).get(0).topActivity.getPackageName();

		//If the current app is this app, then skip
		if(pckName.equals(mContext.getPackageName())) {
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

	public void startMonitoring() {
        if(!mMonitoring) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_ACTIVITY, 1000);
            mMonitoring = true;
        }
	}

    public void stopMonitoring() {
        mMonitoring = false;
    }

    List<String> mMonitoredApps = new ArrayList<String>();
    @Override
    public void onAppsChanged() {
        loadMonitoredApps();
    }


    public static boolean shouldMonitor() {
        return Prefs.getBoolean(PREF_BOOL_SHOULD_MONITOR, true);
    }

    public static void setShouldMonitor(boolean shouldMonitor) {
        Prefs.putBoolean(PREF_BOOL_SHOULD_MONITOR, shouldMonitor);
    }
}
