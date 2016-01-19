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
	 * @param writable �Ƿ���Ҫ�������ݿ�����
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
	// return;// TODO ���쳣
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
			return;// TODO ���쳣
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
//	 * @Description: ��ӳ�����Ϣ
//	 * @return: void
//	 * @param car
//	 * @author: Psyche
//	 * @date: 2015��8��15��
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
	 * @Description: ���³������ݣ������Ǹ��ݳ��������µģ����ű���Ψһ
	 * @return: void
	 * @param car
	 * @author: Psyche
	 * @date: 2015��8��15��
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
	 * @Description: ���ݳ��ž�ȷ��ѯ������Ϣ
	 * @return: Car
	 * @param carNo
	 * @author: Psyche
	 * @date: 2015��8��15��
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
	 * @Description: ���ݳ���ģ����ѯ������Ϣ
	 * @return: List<Car>
	 * @param carNoLike
	 * @author: Psyche
	 * @date: 2015��8��15��
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
	 * @Description: �������г�����Ϣ
	 * @return: List<Car>
	 * @author: Psyche
	 * @date: 2015��8��15��
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
