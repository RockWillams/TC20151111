package com.taichang.bean;

import java.io.Serializable;

public class Car implements Serializable
{

	/**
	 * @Fields serialVersionUID : –Ú¡–ªØid
	 */
	private static final long serialVersionUID = 99L;

	private String carNo;

	private double lat;

	private double lon;

	private long dateTime;

	public Car(String carNo, double lat, double lon, long dateTime)
	{
		this.carNo = carNo;
		this.lat = lat;
		this.lon = lon;
		this.dateTime = dateTime;
	}

	public String getCarNo()
	{
		return carNo;
	}

	public double getLat()
	{
		return lat;
	}

	public double getLon()
	{
		return lon;
	}

	public long getDateTime()
	{
		return dateTime;
	}

}
