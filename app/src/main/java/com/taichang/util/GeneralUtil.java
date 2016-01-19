package com.taichang.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class GeneralUtil
{
	private GeneralUtil()
	{
	}

	private static Toast mCurrentToast;
	
	/**
	 * @Fields ONLINE_TIME : 查询时汽车在该时间内未上传数据则判定为离线
	 */
	public static final long OFFLINE_TIME = 60000;

	/**
	 * @Fields TIME_DIF_UTC : 与格林威治标准时间差（东八区）
	 */
	public static final long TIME_DIF_UTC = 8 * 60 * 60 * 1000;

	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String UTF8Encode(String origin)
	{
		String result = null;
		try
		{
			result = URLEncoder.encode(origin, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static final void showToast(Context context, CharSequence text,
			int duration)
	{
		if (mCurrentToast != null)
		{
			mCurrentToast.cancel();
		}
		mCurrentToast = Toast.makeText(context, text, duration);
		mCurrentToast.show();
	}

	public static final void showToast(Context context, int resId, int duration)
	{
		showToast(context, context.getResources().getText(resId), duration);
	}

}
