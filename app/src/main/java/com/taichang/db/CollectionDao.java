package com.taichang.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.taichang.bean.Collection;

public class CollectionDao
{
	private DatabaseHelper mHelper;
	private SQLiteDatabase mDatabase;

	private final String tableName = "Collections";

	public CollectionDao(Context context)
	{
		mHelper = DatabaseHelper.getDatabaseHelper(context);
		mDatabase = mHelper.getWritableDatabase();
		String createTable = "CREATE TABLE IF NOT EXISTS `" + tableName
				+ "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `carNo` VARCHAR)";
		mDatabase.execSQL(createTable);
	}

	public void add(Collection col)
	{
		ContentValues values = new ContentValues();
		values.put("carNo", col.getCarNo());
		mDatabase.insert(tableName, null, values);
	}

	public void delByNo(String carNo)
	{
		Collection col = getByNo(carNo);
		if (col != null)
		{
			mDatabase.delete(tableName, "carNo=?", new String[]
			{ carNo });
		}
	}

	public Collection getByNo(String carNo)
	{
		Collection col = null;
		Cursor cursor = mDatabase.query(tableName, null, "carNo=?",
				new String[]
				{ carNo }, null, null, null);
		if (cursor.moveToFirst())
		{
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			col = new Collection(carNo, id);
		}
		return col;
	}

	public List<Collection> getAll()
	{
		List<Collection> cols = null;
		Cursor cursor = mDatabase.query(tableName, null, null, null, null,
				null, null);
		if (cursor.moveToFirst())
		{
			cols = new ArrayList<Collection>(cursor.getCount());
			do
			{
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String carNo = cursor.getString(cursor.getColumnIndex("carNo"));
				Collection col = new Collection(carNo, id);
				cols.add(col);
			} while (cursor.moveToNext());
		} else
		{
			cols = new ArrayList<Collection>();
		}
		return cols;
	}
}
