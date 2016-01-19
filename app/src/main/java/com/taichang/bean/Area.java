package com.taichang.bean;

public class Area
{
	private String areaName;

	private double lat;

	private double lon;

	public Area(String areaName, double lat, double lon)
	{
		this.areaName = areaName;
		this.lat = lat;
		this.lon = lon;
	}

	public String getAreaName()
	{
		return areaName;
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
