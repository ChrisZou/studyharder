package com.chriszou.studyharder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startMonitoringService();

		findViewById(R.id.main_stopService).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopMonitoringService();
			}
		});
	}

	private void startMonitoringService() {
		Intent intent = new Intent(this, MonitorService.class);
		startService(intent);
	}

	private void stopMonitoringService() {
		Intent intent = new Intent(this, MonitorService.class);
		stopService(intent);
	}

}
