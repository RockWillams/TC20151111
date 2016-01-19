package com.taichang.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.taichang.bean.TrackPoint;

public class TrackPointDao
{
	private DatabaseHelper mHelper;
	private SQLiteDatabase mDatabase;

	private final String tableName;

	public TrackPointDao(Context context, String tableName)
	{
		this.tableName = tableName;
		mHelper = DatabaseHelper.getDatabaseHelper(context);
		mDatabase = mHelper.getWritableDatabase();
		String createTable = "CREATE TABLE IF NOT EXISTS `"
				+ tableName
				+ "` (`dateTime` BIGINT , `lat` DOUBLE PRECISION , `lon` DOUBLE PRECISION , `id` INTEGER PRIMARY KEY AUTOINCREMENT )";
		mDatabase.execSQL(createTable);
	}

	public void addMultiple(List<TrackPoint> tps)
	{
		mDatabase.beginTransaction();
		try
		{
			for (TrackPoint tp : tps)
			{
				add(tp);
			}
			mDatabase.setTransactionSuccessful();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			mDatabase.endTransaction();
		}

	}

	/**
	 * @Title: add
	 * @Description: 添加轨迹点
	 * @return: void
	 * @param tp
	 * @author: Psyche
	 * @date: 2015年8月15日
	 */
	private void add(TrackPoint tp)
	{
		ContentValues values = new ContentValues();
		values.put("dateTime", tp.getDateTime());
		values.put("lat", tp.getLat());
		values.put("lon", tp.getLon());
		mDatabase.insert(tableName, null, values);
	}

	/**
	 * @Title: getByDateTime
	 * @Description: 根据时间段查询轨迹点
	 * @return: List<TrackPoint>
	 * @param startDateTime
	 * @param endDateTime
	 * @author: Psyche
	 * @date: 2015年8月15日
	 */
	public List<TrackPoint> getByDateTime(long startDateTime, long endDateTime)
	{
		List<TrackPoint> tps = null;
		String sql = "SELECT * FROM " + tableName
				+ " WHERE dateTime >= ? AND dateTime <= ?";
		Cursor cursor = mDatabase.rawQuery(sql, new String[]
		{ startDateTime + "", endDateTime + "" });
		if (cursor.moveToFirst())
		{
			tps = new ArrayList<TrackPoint>(cursor.getCount());
			do
			{
				long dateTime = cursor.getLong(cursor
						.getColumnIndex("dateTime"));
				double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
				double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
				TrackPoint tp = new TrackPoint(dateTime, lat, lon);
				tps.add(tp);
			} while (cursor.moveToNext());
		} else
		{
			tps = new ArrayList<TrackPoint>();
		}
		return tps;
	}

	/**
	 * @Title: getLast
	 * @Description: 获取最后一个轨迹点
	 * @return: TrackPoint
	 * @return
	 * @author: Psyche
	 * @date: 2015年8月15日
	 */
	public TrackPoint getLast()
	{
		TrackPoint tp = null;
		String sql = "SELECT * FROM " + tableName
				+ " ORDER BY id DESC LIMIT 0,1";
		Cursor cursor = mDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst())
		{
			long dateTime = cursor.getLong(cursor
					.getColumnIndex("dateTime"));
			double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
			double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
			tp = new TrackPoint(dateTime, lat, lon);
		}
		return tp;
	}
}
