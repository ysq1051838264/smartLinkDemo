package com.example.ysq.smartlinkdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		((TextView)findViewById(R.id.textView_version)).setText("Version: " + getVersionName()) ;
	}
	
	public void startSmartLink3Demo(View view) {
		startActivity(new Intent(this, SmartLink3DemoActivity.class));
	}
	
	public void startSmartLink7Demo(View view) {
		startActivity(new Intent(this, SmartLink7DemoActivity.class));
	}
	
	private String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "unknown";
	}
}
