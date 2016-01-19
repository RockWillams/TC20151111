package com.taichang.bean;

public class Warning
{
	private int id;

	private int serverId;

	private String carNo;

	private double lat;

	private double lon;

	private long dateTime;

	private String person;

	private String phone;

	public Warning(int serverId, String carNo, double lat, double lon,
			long dateTime, String person, String phone)
	{
		this.serverId = serverId;
		this.carNo = carNo;
		this.lat = lat;
		this.lon = lon;
		this.dateTime = dateTime;
		this.person = person;
		this.phone = phone;
	}

	public Warning(int id, int serverId, String carNo, double lat, double lon,
			long dateTime, String person, String phone)
	{
		this(serverId, carNo, lat, lon, dateTime, person, phone);
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getServerId()
	{
		return serverId;
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

	public String getPerson()
	{
		return person;
	}

	public String getPhone()
	{
		return phone;
	}

}
