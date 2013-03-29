package com.panzyma.nm;
 

import android.app.Application;
import android.content.res.Configuration;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NMApp extends Application
{ 
	@Override
	public void onConfigurationChanged(Configuration newConfig) { 
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() { 
		super.onCreate();   
	}

	@Override
	public void onLowMemory() { 
		super.onLowMemory();
	}

	@Override
	public void onTerminate() { 
		super.onTerminate();
	}
}
