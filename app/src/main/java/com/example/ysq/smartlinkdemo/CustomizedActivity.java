package com.example.ysq.smartlinkdemo;

import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinker;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomizedActivity extends Activity implements OnSmartLinkListener {

    public static final String EXTRA_SMARTLINK_VERSION = "EXTRA_SMARTLINK_VERSION";

    private static final String TAG = "CustomizedActivity";

    protected EditText mSsidEditText;
    protected EditText mPasswordEditText;
    protected Button mStartButton;
    protected ISmartLinker mSnifferSmartLinker;
    private boolean mIsConncting = false;
    protected Handler mViewHandler = new Handler();
    protected ProgressDialog mWaitingDialog;
    private BroadcastReceiver mWifiChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int smartLinkVersion = getIntent().getIntExtra(EXTRA_SMARTLINK_VERSION, 3);
        if (smartLinkVersion == 7) {
            mSnifferSmartLinker = MulticastSmartLinker.getInstance();
        } else {
            mSnifferSmartLinker = SnifferSmartLinker.getInstance();
        }

        mWaitingDialog = new ProgressDialog(this);
        mWaitingDialog.setMessage(getString(R.string.hiflying_smartlinker_waiting));
        mWaitingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mWaitingDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

                mSnifferSmartLinker.setOnSmartLinkListener(null);
                mSnifferSmartLinker.stop();
                mIsConncting = false;
            }
        });

        setContentView(R.layout.activity_customized);
        mSsidEditText = (EditText) findViewById(R.id.editText_hiflying_smartlinker_ssid);
        mPasswordEditText = (EditText) findViewById(R.id.editText_hiflying_smartlinker_password);
        mStartButton = (Button) findViewById(R.id.button_hiflying_smartlinker_start);
        mSsidEditText.setText(getSSid());

        mStartButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!mIsConncting) {

                    //设置要配置的ssid 和pswd
                    try {
                        mSnifferSmartLinker.setOnSmartLinkListener(CustomizedActivity.this);
                        //开始 smartLink
                        mSnifferSmartLinker.start(getApplicationContext(), mPasswordEditText.getText().toString().trim(),
                                mSsidEditText.getText().toString().trim());
                        mIsConncting = true;
                        mWaitingDialog.show();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        mWifiChangedReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (networkInfo != null && networkInfo.isConnected()) {
                    mSsidEditText.setText(getSSid());
                    mPasswordEditText.requestFocus();
                    mStartButton.setEnabled(true);
                } else {
                    mSsidEditText.setText(getString(R.string.hiflying_smartlinker_no_wifi_connectivity));
                    mSsidEditText.requestFocus();
                    mStartButton.setEnabled(false);
                    if (mWaitingDialog.isShowing()) {
                        mWaitingDialog.dismiss();
                    }
                }
            }
        };
        registerReceiver(mWifiChangedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mSnifferSmartLinker.setOnSmartLinkListener(null);
        try {
            unregisterReceiver(mWifiChangedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLinked(final SmartLinkedModule module) {
        // TODO Auto-generated method stub

        Log.w(TAG, "onLinked");
        mViewHandler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.hiflying_smartlinker_new_module_found)
                                + "  ip地址：" + module.getMac() + "  ip地址：" + module.getModuleIP(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onCompleted() {

        Log.w(TAG, "onCompleted");
        mViewHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), getString(R.string.hiflying_smartlinker_completed),
                        Toast.LENGTH_SHORT).show();
                mWaitingDialog.dismiss();
                mIsConncting = false;
            }
        });
    }


    @Override
    public void onTimeOut() {

        Log.w(TAG, "onTimeOut");
        mViewHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), getString(R.string.hiflying_smartlinker_timeout),
                        Toast.LENGTH_SHORT).show();
                mWaitingDialog.dismiss();
                mIsConncting = false;
            }
        });
    }

    private String getSSid() {

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String ssid = wi.getSSID();
                if (ssid.length() > 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    return ssid.substring(1, ssid.length() - 1);
                } else {
                    return ssid;
                }
            }
        }

        return "";
    }
}
