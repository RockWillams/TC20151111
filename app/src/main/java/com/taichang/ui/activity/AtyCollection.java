package com.taichang.ui.activity;

import static com.taichang.util.GeneralUtil.showToast;

import java.io.Serializable;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import com.taichang.R;
import com.taichang.adapter.CollectionAdapter;
import com.taichang.adapter.WarningAdapter;
import com.taichang.bean.Car;
import com.taichang.bean.Collection;
import com.taichang.db.CarDao;
import com.taichang.db.CollectionDao;
import com.taichang.ui.activity.AtyMain.ResultCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@WindowFeature(
{ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.aty_collection)
public class AtyCollection extends Activity
{
	@ViewById
	TextView title;

	@ViewById(R.id.collections)
	ListView listviewCols;

	private CollectionDao colDao;
	private CarDao carDao;
	private List<Collection> cols;

	private CollectionAdapter mAdapter;

	private AlertDialog ad;

	@AfterViews
	void init()
	{
		title.setText(R.string.aty_collection_title);
		colDao = new CollectionDao(this);
		carDao = new CarDao(this);
		updateCollectionData();
		cols = colDao.getAll();
		mAdapter = new CollectionAdapter(this, cols);
		listviewCols.setAdapter(mAdapter);
	}

	@ItemClick(R.id.collections)
	void onBackWithOneCar(int position)
	{
		Collection col = cols.get(position);
		String carNo = col.getCarNo();
		Intent intent = new Intent();
		intent.putExtra("carNo", carNo);
		setResult(ResultCode.ONE_CAR, intent);
		finish();
	}

	@ItemLongClick(R.id.collections)
	void delCollection(final int position)
	{
		ad = new AlertDialog.Builder(this).setTitle("是否要删除该条收藏？").setPositiveButton("删除", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ad.dismiss();
				String carNo = cols.get(position).getCarNo();
				colDao.delByNo(carNo);
				showToast(AtyCollection.this, "已取消收藏\t" + carNo, Toast.LENGTH_SHORT);
				cols = colDao.getAll();
				mAdapter.notifyDataSetChanged(cols);
			}
		}).setNegativeButton("取消", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ad.dismiss();
			}
		}).show();
	}

	/**
	 * @Title: updateCollectionData
	 * @Description: 更新一下收藏数据库的信息，有可能会出现车辆已经在主表中删除了，但是收藏里面还有
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年9月24日
	 */
	private void updateCollectionData()
	{
		List<Collection> oldCols = colDao.getAll();
		StringBuilder deletedCars = new StringBuilder("");
		for (Collection oldCol : oldCols)
		{
			String carNo = oldCol.getCarNo();
			if(carDao.getByNo(carNo)==null){
				colDao.delByNo(carNo);
				deletedCars.append(carNo+" ");
			}
		}
		if(!"".equals(deletedCars.toString()))
			showToast(this, "您收藏的车辆：\""+deletedCars+"\"已不存在", Toast.LENGTH_LONG);
	}

	@Click(R.id.back)
	void onBack()
	{
		finish();
	}
}
