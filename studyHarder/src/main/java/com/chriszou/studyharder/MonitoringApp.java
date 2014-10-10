package com.chriszou.studyharder;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.chriszou.androidlibs.AppInfo;
import com.chriszou.androidlibs.AppManager;
import com.chriszou.androidlibs.Prefs;

import org.androidannotations.annotations.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.SharedPreferences.*;

/**
 * Created by zouyong on 10/9/14.
 */
public class MonitoringApp extends AppInfo{
    private static final String PREF_STRING_SET_MONITORING_APPS = "pref_string_set_monitoring_apps";
    public MonitoringApp(String pkgName) {
        super(pkgName, MyApplication.getContext());
    }

    private static OnSharedPreferenceChangeListener sSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            notifyAppsChanged();
        }
    };
    static {
        staticInit();
    }
    private static void staticInit() {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).registerOnSharedPreferenceChangeListener(sSharedPreferenceChangeListener);
    }

    public static List<MonitoringApp> all() {
        Set<String> apps = Prefs.getStringSet(PREF_STRING_SET_MONITORING_APPS, Collections.EMPTY_SET);
        List<MonitoringApp> results = new ArrayList<MonitoringApp>();
        for(String pkg : apps) {
            if(pkg!=null) {
                results.add(new MonitoringApp(pkg));
            }
        }

        return results;
    }

    public static void add(MonitoringApp app) {
        Set<String> apps = Prefs.getStringSet(PREF_STRING_SET_MONITORING_APPS, Collections.EMPTY_SET);
        apps.add(app.pkgName);
        Prefs.putStringSet(PREF_STRING_SET_MONITORING_APPS, apps);
    }

    public static void add(String app) {
        //The set retrieved from preference is unchangable
        Set<String> apps = Prefs.getStringSet(PREF_STRING_SET_MONITORING_APPS, Collections.EMPTY_SET);
        Set<String> apps2 = new HashSet<String>();
        apps2.addAll(apps);
        apps2.add(app);
        Prefs.putStringSet(PREF_STRING_SET_MONITORING_APPS, apps2);
    }


    private static void notifyAppsChanged() {
        for(OnAppsChangedListener listener:sListeners) {
            listener.onAppsChanged();
        }
    }

    private static List<OnAppsChangedListener> sListeners = new ArrayList<OnAppsChangedListener>();
    public static void registerOnAppsChangedListener(OnAppsChangedListener listener) {
        sListeners.add(listener);
    }
    public static void unregisterOnAppsChangedListener(OnAppsChangedListener listener) {
        sListeners.remove(listener);
    }

    public static interface OnAppsChangedListener {
        public void onAppsChanged();
    }


}
