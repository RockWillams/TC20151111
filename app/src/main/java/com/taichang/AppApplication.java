package com.taichang;

import android.app.Application;

import com.taichang.gps.MyLocationProvider;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

@EApplication
public class AppApplication extends Application
{
	@Bean
	MyLocationProvider mMyLocationProvider;
	
//	@AfterInject
//	void init()
//	{
//		SDKInitializer.initialize(this);
//	}
	
	@Override
	public void onTerminate()
	{
		mMyLocationProvider.exitLocationService();
		super.onTerminate();
	}
}
