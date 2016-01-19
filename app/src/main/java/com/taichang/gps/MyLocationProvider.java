package com.taichang.gps;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.EBean.Scope;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

@EBean(scope = Scope.Singleton)
public class MyLocationProvider
{
	/**
	 * @Fields mLocationMode : 定位精度
	 */
	private final LocationMode locationMode = LocationMode.Hight_Accuracy;
	/**
	 * @Fields scanSpan : 定位请求间隔时间
	 */
	private final int scanSpan = 5000;
	
	/** 
	 * @Fields coordinateType : 定位坐标系
	 */ 
	private final String coordinateType = "bd09ll";

	@RootContext
	Context mContext;

	@Bean
	static MyLocationListener mLocationListener;

	private LocationClient mLocClient;
	
	@AfterInject
	void init()
	{
		initMyLocationListener();
	}

	public void exitLocationService()
	{
		mLocClient.unRegisterLocationListener(mLocationListener);
		mLocClient.stop();
	}

	private void initMyLocationListener()
	{
		SDKInitializer.initialize(mContext.getApplicationContext());
		mLocClient = new LocationClient(mContext);
		setLocationOption();
		mLocClient.registerLocationListener(mLocationListener);
		mLocClient.start();
	}

	private void setLocationOption()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(locationMode);
		option.setCoorType(coordinateType);
		option.setScanSpan(scanSpan);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
	}

}
