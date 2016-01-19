package com.taichang.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.taichang.bean.Car;

public class CarDao
{
	private boolean writable;

	private DatabaseHelper mHelper;
	private SQLiteDatabase mDatabase;

	private final String tableName = DatabaseHelper.MAIN_TABLE;

	public CarDao(Context context)
	{
		this(context, false);
	}

	/** 
	 * Title:
	 * Description: 
	 * @param context 
	 * @param writable 是否需要更改数据库数据
	 */
	public CarDao(Context context, boolean writable)
	{
		this.writable = writable;
		mHelper = DatabaseHelper.getDatabaseHelper(context);
		if (writable)
		{
			mDatabase = mHelper.getWritableDatabase();
		} else
		{
			mDatabase = mHelper.getReadableDatabase();
		}
	}

	// public void addMultiple(List<Car> cars)
	// {
	// if (!writable)
	// {
	// return;// TODO 抛异常
	// }
	// mDatabase.beginTransaction();
	// try
	// {
	// mDatabase.delete(tableName, null, null);
	// for (Car car : cars)
	// {
	// add(car);
	// }
	// mDatabase.setTransactionSuccessful();
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// } finally
	// {
	// mDatabase.endTransaction();
	// }
	// }

	public void updateMultiple(List<Car> cars)
	{
		if (!writable)
		{
			return;// TODO 抛异常
		}
		mDatabase.beginTransaction();
		try
		{
			for (Car car : cars)
				update(car);
			mDatabase.setTransactionSuccessful();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			mDatabase.endTransaction();
		}
	}

//	/**
//	 * @Title: add
//	 * @Description: 添加车辆信息
//	 * @return: void
//	 * @param car
//	 * @author: Psyche
//	 * @date: 2015年8月15日
//	 */
//	private void add(Car car)
//	{
//		ContentValues values = new ContentValues();
//		values.put("carNo", car.getCarNo());
//		values.put("dateTime", car.getDateTime());
//		values.put("lat", car.getLat());
//		values.put("lon", car.getLon());
//		mDatabase.insert(tableName, null, values);
//	}

	/**
	 * @Title: update
	 * @Description: 更新车辆数据，但是是根据车号来更新的，车号必须唯一
	 * @return: void
	 * @param car
	 * @author: Psyche
	 * @date: 2015年8月15日
	 */
	private void update(Car car)
	{
		ContentValues values = new ContentValues();
		values.put("carNo", car.getCarNo());
		values.put("dateTime", car.getDateTime());
		values.put("lat", car.getLat());
		values.put("lon", car.getLon());
		mDatabase.insertWithOnConflict(tableName, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
	}

	/**
	 * @Title: getByNo
	 * @Description: 根据车号精确查询车辆信息
	 * @return: Car
	 * @param carNo
	 * @author: Psyche
	 * @date: 2015年8月15日
	 */
	public Car getByNo(String carNo)
	{
		Car car = null;
		Cursor cursor = mDatabase.query(tableName, null, "carNo = ?",
				new String[]
				{ carNo }, null, null, null);
		if (cursor.moveToFirst())
		{
			long dateTime = cursor.getLong(cursor.getColumnIndex("dateTime"));
			double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
			double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
			car = new Car(carNo, lat, lon, dateTime);
		}
		return car;
	}

	/**
	 * @Title: getLikeNo
	 * @Description: 根据车号模糊查询车辆信息
	 * @return: List<Car>
	 * @param carNoLike
	 * @author: Psyche
	 * @date: 2015年8月15日
	 */
	public List<Car> getLikeNo(String carNoLike)
	{
		List<Car> cars = null;
		Cursor cursor = mDatabase.query(tableName, null, "carNo LIKE ?",
				new String[]
				{ "%" + carNoLike + "%" }, null, null, null);
		if (cursor.moveToFirst())
		{
			cars = new ArrayList<Car>(cursor.getCount());
			do
			{
				String carNo = cursor.getString(cursor.getColumnIndex("carNo"));
				long dateTime = cursor.getLong(cursor
						.getColumnIndex("dateTime"));
				double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
				double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
				Car car = new Car(carNo, lat, lon, dateTime);
				cars.add(car);
			} while (cursor.moveToNext());
		} else
		{
			cars = new ArrayList<Car>();
		}
		return cars;
	}
	
	public List<Car> getByDateTimeLikeNo(String carNoLike,
			long startDateTime, long endDateTime)
	{
		List<Car> cars = null;
		String sql = "SELECT * FROM " + tableName
				+ " WHERE dateTime >= ? AND dateTime <= ? AND carNo LIKE ?";
		Cursor cursor = mDatabase.rawQuery(sql, new String[]
		{ startDateTime + "", endDateTime + "", "%" + carNoLike + "%" });
		if (cursor.moveToFirst())
		{
			cars = new ArrayList<Car>(cursor.getCount());
			do
			{
				String carNo = cursor.getString(cursor.getColumnIndex("carNo"));
				long dateTime = cursor.getLong(cursor
						.getColumnIndex("dateTime"));
				double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
				double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
				Car car = new Car(carNo, lat, lon, dateTime);
				cars.add(car);
			} while (cursor.moveToNext());
		} else
		{
			cars = new ArrayList<Car>();
		}
		return cars;
	}

	/**
	 * @Title: getAll
	 * @Description: 返回所有车辆信息
	 * @return: List<Car>
	 * @author: Psyche
	 * @date: 2015年8月15日
	 */
	public List<Car> getAll()
	{
		List<Car> cars = null;
		Cursor cursor = mDatabase.query(tableName, null, null, null, null,
				null, null);
		if (cursor.moveToFirst())
		{
			cars = new ArrayList<Car>(cursor.getCount());
			do
			{
				String carNo = cursor.getString(cursor.getColumnIndex("carNo"));
				long dateTime = cursor.getLong(cursor
						.getColumnIndex("dateTime"));
				double lat = cursor.getDouble(cursor.getColumnIndex("lat"));
				double lon = cursor.getDouble(cursor.getColumnIndex("lon"));
				Car car = new Car(carNo, lat, lon, dateTime);
				cars.add(car);
			} while (cursor.moveToNext());
		} else
		{
			cars = new ArrayList<Car>();
		}
		return cars;
	}
}
