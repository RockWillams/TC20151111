package com.taichang.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkUtil
{
	private NetworkUtil()
	{
	}

	private static final String[][] HTTP_SERVERS =
	{
	{ "À×Æ½", "http://10.0.1.141:8024/android/" },
	{ "TC", "http://teeechina.xicp.net:8024/android/" },
	{ "TC", "http://teeechina.xicp.net:8025/android/" }};

	public static final String HTTP_SERVER = HTTP_SERVERS[0][1];

	public static final String SERVER_PAGE_LOGIN = "login.asp";
	public static final String SERVER_PAGE_ALL_CAR_STATUS = "carList.asp";
	public static final String SERVER_PAGE_CAR_TRACK = "carTrack.asp";
	public static final String SERVER_PAGE_ALL_CAR_WARNING = "carWarning.asp";

	private static RequestQueue mRequestQueue;

	public static RequestQueue getVolleyRequestQueue(Context context)
	{
		if (mRequestQueue == null)
		{
			synchronized (NetworkUtil.class)
			{
				if (mRequestQueue == null)
				{
					mRequestQueue = Volley.newRequestQueue(context
							.getApplicationContext());
					mRequestQueue.start();
				}
			}
		}
		return mRequestQueue;
	}
}
