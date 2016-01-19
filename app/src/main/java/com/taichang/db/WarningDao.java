package com.taichang.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.taichang.bean.Warning;

public class WarningDao
{
	private DatabaseHelper mHelper;
	private SQLiteDatabase mDatabase;

	private final String tableName = "Warnings";

	public WarningDao(Context context)
	{
		mHelper = DatabaseHelper.getDatabaseHelper(context);
		mDatabase = mHelper.getWritableDatabase();
		String createTable = "CREATE TABLE IF NOT EXISTS `"
				+ tableName
				+ "` (`serverId` INTEGER, `carNo` VARCHAR, `dateTime` BIGINT , `lat` DOUBLE PRECISION , `lon` DOUBLE PRECISION , `id` INTEGER PRIMARY KEY AUTOINCREMENT, `person` VARCHAR, `phone` VARCHAR)";
		mDatabase.execSQL(createTable);
	}

	public void addMultiple(List<Warning> warnings)
	{
		mDatabase.beginTransaction();
		try
		{
			mDatabase.delete(tableName, null, null);
			for (Warning warning : warnings)
			{
				add(warning);
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

	public List<Warning> getByDateTimeLikeNo(String carNoLike,
			long startDateTime, long endDateTime)
	{
		List<Warning> warnings = null;
		String sql = "SELECT * FROM " + tableName
				+ " WHERE dateTime >= ? AND dateTime <= ? AND carNo LIKE ?";
		Cursor cursor = mDatabase.rawQuery(sql, new String[]
		{ startDateTime + "", endDateTime + "", "%" + carNoLike + "%" });
		if (cursor.moveToFirst())
		{
			warnings = new ArrayList<Warning>(cursor.getCount());
			do
			{
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int serverId = cursor.getInt(cursor.getColumnIndex("serverId"));
				String carNo = cursor.getString(cursor.getColumnIndex("carNo"));
				long dateTime = cursor.getLong(cursor
						.getColumnIndex("dateTime"));
				double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
				double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
				String person = cursor.getString(cursor
						.getColumnIndex("person"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				Warning warning = new Warning(id, serverId, carNo, lat, lon,
						dateTime, person, phone);
				warnings.add(warning);
			} while (cursor.moveToNext());
		} else
		{
			warnings = new ArrayList<Warning>();
		}
		return warnings;
	}

	public List<Warning> getAll()
	{
		List<Warning> warnings = null;
		Cursor cursor = mDatabase.query(tableName, null, null, null, null,
				null, null);
		if (cursor.moveToFirst())
		{
			warnings = new ArrayList<Warning>(cursor.getCount());
			do
			{
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int serverId = cursor.getInt(cursor.getColumnIndex("serverId"));
				String carNo = cursor.getString(cursor.getColumnIndex("carNo"));
				long dateTime = cursor.getLong(cursor
						.getColumnIndex("dateTime"));
				double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
				double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
				String person = cursor.getString(cursor
						.getColumnIndex("person"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				Warning warning = new Warning(id, serverId, carNo, lat, lon,
						dateTime, person, phone);
				warnings.add(warning);
			} while (cursor.moveToNext());
		} else
		{
			warnings = new ArrayList<Warning>();
		}
		return warnings;
	}

	public Warning getLast()
	{
		Warning warning = null;
		String sql = "SELECT * FROM " + tableName
				+ " ORDER BY id DESC LIMIT 0,1";
		Cursor cursor = mDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst())
		{
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int serverId = cursor.getInt(cursor.getColumnIndex("serverId"));
			String carNo = cursor.getString(cursor.getColumnIndex("carNo"));
			long dateTime = cursor.getLong(cursor.getColumnIndex("dateTime"));
			double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
			double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
			String person = cursor.getString(cursor.getColumnIndex("person"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			warning = new Warning(id, serverId, carNo, lat, lon, dateTime,
					person, phone);
		}
		return warning;
	}

	private void add(Warning warning)
	{
		ContentValues values = new ContentValues();
		values.put("serverId", warning.getServerId());
		values.put("carNo", warning.getCarNo());
		values.put("dateTime", warning.getDateTime());
		values.put("lat", warning.getLat());
		values.put("lon", warning.getLon());
		values.put("person", warning.getPerson());
		values.put("phone", warning.getPhone());
		mDatabase.insert(tableName, null, values);
	}
}
