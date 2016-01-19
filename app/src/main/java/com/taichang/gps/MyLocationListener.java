package com.taichang.gps;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.taichang.interf.MyLocationObserver;

@EBean(scope = Scope.Singleton)
public class MyLocationListener implements BDLocationListener
{

	private List<MyLocationObserver> observers = new ArrayList<MyLocationObserver>();

	@Override
	public void onReceiveLocation(BDLocation location)
	{
		updateAllMyLocations(location);
	}

	public void registerObserver(MyLocationObserver observer)
	{
		observers.add(observer);
	}

	public void unregisterObserver(MyLocationObserver observer)
	{
		observers.remove(observer);
	}

	private void updateAllMyLocations(BDLocation location)
	{
		for (MyLocationObserver observer : observers)
		{
			observer.updateMyLocation(location);
		}
	}
}
