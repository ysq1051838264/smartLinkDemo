package com.example.ysq.smartlinkdemo;

import com.hiflying.smartlink.v3.SnifferSmartLinkerFragment;
import com.hiflying.smartlink.v7.MulticastSmartLinkerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class SmartLinkerFragmentActivity extends FragmentActivity {
	
	public static final String EXTRA_SMARTLINK_VERSION = "EXTRA_SMARTLINK_VERSION";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		int smartLinkVersion = getIntent().getIntExtra(EXTRA_SMARTLINK_VERSION, 3);
		Fragment fragment = new SnifferSmartLinkerFragment();
		setTitle("SnifferSmartLinkerFragment");
		if (smartLinkVersion == 7) {
			fragment = new MulticastSmartLinkerFragment();
			setTitle("MulticastSmartLinkerFragment");
		}
		
		setContentView(R.layout.activity_smart_linker_fragment);
		getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_01, fragment, null).commit();
	}
}
