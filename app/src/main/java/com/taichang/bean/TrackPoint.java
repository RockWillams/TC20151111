package com.taichang.bean;

public class TrackPoint
{
	private long dateTime;

	private double lat;

	private double lon;

	public TrackPoint(long dateTime, double lat, double lon)
	{
		this.dateTime = dateTime;
		this.lat = lat;
		this.lon = lon;
	}

	public long getDateTime()
	{
		return dateTime;
	}

	public double getLat()
	{
		return lat;
	}

	public double getLon()
	{
		return lon;
	}

}
