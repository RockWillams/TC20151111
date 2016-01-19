package com.taichang.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class MapStatusDao
{
	private String prefName = "MapStatus";
	private String key_lat = "lat", key_lon = "lon", key_city = "city", key_zoom = "zoom";

	private SharedPreferences mPref;

	public MapStatusDao(Context context)
	{
		mPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
	}

	public void setLat(double lat)
	{
		mPref.edit().putLong(key_lat, Double.doubleToRawLongBits(lat)).apply();
	}

	public double getLat()
	{
		return Double.longBitsToDouble(mPref.getLong(key_lat, 30l));
	}

	public void setLon(double lon)
	{
		mPref.edit().putLong(key_lon, Double.doubleToRawLongBits(lon)).apply();
	}

	public double getLon()
	{
		return Double.longBitsToDouble(mPref.getLong(key_lon, 104l));
	}

	public void setCity(String city)
	{
		mPref.edit().putString(key_city, city).apply();
	}

	public String getCity()
	{
		return mPref.getString(key_city, "±±¾©");
	}

	public void setZoom(float zoom)
	{
		mPref.edit().putFloat(key_zoom, zoom).apply();
	}

	public float getZoom()
	{
		return mPref.getFloat(key_zoom, 10F);
	}
}
