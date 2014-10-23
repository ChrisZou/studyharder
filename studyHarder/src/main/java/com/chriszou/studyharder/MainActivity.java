package com.chriszou.studyharder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.chriszou.androidlibs.AppInfo;
import com.chriszou.androidlibs.DefaultListAdapter;
import com.chriszou.androidlibs.L;
import com.chriszou.androidlibs.Prefs;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends Activity implements MonitoringApp.OnAppsChangedListener{

    private static final int REQUEST_SELECT_APP = 1;
    @ViewById(R.id.main_listview)
    ListView mListView;

    @AfterViews
    void loadData() {
        MonitoringApp.registerOnAppsChangedListener(this);
        loadMonitoringApps();
        startMonitoringService();
    }

    @Override
    protected void onDestroy() {
        MonitoringApp.unregisterOnAppsChangedListener(this);
        super.onDestroy();

    }

    @OptionsItem(R.id.action_add)
    void addMonitoringApp() {
        L.l("add clicked");
        Intent intent = new Intent(this, AppSelectorActivity_.class);
        startActivityForResult(intent, REQUEST_SELECT_APP);
    }

    private void loadMonitoringApps() {
        List<MonitoringApp> apps = MonitoringApp.all();
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for(MonitoringApp app: apps) {
            if(app!=null) {
                appInfos.add(app);
            }
        }
        AppsAdapter adapter = new AppsAdapter(this, appInfos);
        mListView.setAdapter(adapter);
    }

    @OnActivityResult(REQUEST_SELECT_APP)
    void onResult(int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            String pkg = data.getStringExtra(AppSelectorActivity.RESULT_PACKAGE);
            MonitoringApp.add(pkg);
        }
    }

    void startMonitoringService() {
        AppMonitor.setShouldMonitor(true);
		Intent intent = new Intent(this, MonitorService.class);
		startService(intent);
	}

    @Click(R.id.main_stopService)
    void stopMonitoringService() {
        AppMonitor.setShouldMonitor(false);
        AppMonitor.getInstance(this).stopMonitoring();
        Intent intent = new Intent(this, MonitorService.class);
        stopService(intent);
    }

    @Override
    public void onAppsChanged() {
        loadMonitoringApps();
    }



}
