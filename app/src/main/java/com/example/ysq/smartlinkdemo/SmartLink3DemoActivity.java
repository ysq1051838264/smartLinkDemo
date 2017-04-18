package com.example.ysq.smartlinkdemo;

import com.hiflying.smartlink.v3.SnifferSmartLinkerActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SmartLink3DemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smartlink3_demo);
	}
	
	public void startSmartLinkerActivity(View view) {
		startActivity(new Intent(this, SnifferSmartLinkerActivity.class));
	}
	
	public void startSmartLinkerFragment(View view) {
		
		Intent intent = new Intent(this, SmartLinkerFragmentActivity.class);
		intent.putExtra(SmartLinkerFragmentActivity.EXTRA_SMARTLINK_VERSION, 3);
		startActivity(intent);
	}
	
	public void startCustomizedActivity(View view) {
		
		Intent intent = new Intent(this, CustomizedActivity.class);
		intent.putExtra(CustomizedActivity.EXTRA_SMARTLINK_VERSION, 3);
		startActivity(intent);
	}
}
