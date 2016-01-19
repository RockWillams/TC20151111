package com.taichang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper
{
	/**
	 * @Fields DATABASE_NAME : ����Ŀ���ݿ�����
	 */
	private static final String DATABASE_NAME = "SQLITE_TC";
	
	/** 
	 * @Fields MAIN_TABLE : ������
	 */ 
	static final String MAIN_TABLE = "Cars";

	private static DatabaseHelper singleInstance;

	static DatabaseHelper getDatabaseHelper(Context context)
	{
		if (singleInstance == null)
		{
			synchronized (DatabaseHelper.class)
			{
				if (singleInstance == null)
					singleInstance = new DatabaseHelper(
							context.getApplicationContext(), DATABASE_NAME,
							null, 1);
			}
		}
		return singleInstance;
	}

	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String createTable = "CREATE TABLE `"
				+ MAIN_TABLE
				+ "` (`carNo` VARCHAR PRIMARY KEY , `dateTime` BIGINT , `lat` DOUBLE PRECISION , `lon` DOUBLE PRECISION )";
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

}
