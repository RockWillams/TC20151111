package com.taichang.bean;

public class Collection
{
	private int id;

	private String carNo;

	public Collection(String carNo)
	{
		this.carNo = carNo;
	}
	public Collection(String carNo, int id)
	{
		this(carNo);
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

	public String getCarNo()
	{
		return carNo;
	}
}
