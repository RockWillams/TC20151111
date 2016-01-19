package com.taichang.ui.activity;

import static com.taichang.util.GeneralUtil.OFFLINE_TIME;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import com.taichang.R;
import com.taichang.adapter.CarAdapter;
import com.taichang.bean.Car;
import com.taichang.db.CarDao;
import com.taichang.ui.activity.AtyMain.ResultCode;
import com.taichang.ui.component.IsOnlinePopupWindow;
import com.taichang.util.GeneralUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@WindowFeature(
{ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.aty_car_search)
public class AtyCarSearch extends Activity
{
	@ViewById
	TextView title;

	@ViewById(R.id.cars)
	ListView listviewCars;

	@ViewById(R.id.search_is_online)
	Button isOnlineBT;

	@ViewById(R.id.search_car_no)
	EditText searchET;

	CarDao carDao;
	private List<Car> cars;
	private int isOnline;
	private String searchNo;

	private CarAdapter mAdapter;

	private interface OnlineCondition
	{
		final int ALL = 0;
		final int ONLINE = 1;
		final int OFFLINE = 2;
	}

	@AfterViews
	void init()
	{
		title.setText(R.string.aty_car_list_title);
		isOnline = OnlineCondition.ALL;
		searchNo = "";
		carDao = new CarDao(this);
		cars = carDao.getAll();
		mAdapter = new CarAdapter(this, cars);
		listviewCars.setAdapter(mAdapter);
		initSearch();
	}

	private void initSearch()
	{
		searchET.setOnEditorActionListener(new OnEditorActionListener()
		{

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					search();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		searchET.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				search();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
	}

	@Click(R.id.search_is_online)
	void onIsOnlineChanged()
	{
		final IsOnlinePopupWindow popWindowIsOnline = new IsOnlinePopupWindow(this);
		popWindowIsOnline.setAllOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				popWindowIsOnline.dismiss();
				isOnline = OnlineCondition.ALL;
				isOnlineBT.setText(R.string.aty_car_list_search_isonline_all);
				search();
			}
		});
		popWindowIsOnline.setOnlineOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				popWindowIsOnline.dismiss();
				isOnline = OnlineCondition.ONLINE;
				isOnlineBT.setText(R.string.aty_car_list_search_isonline_online);
				search();
			}
		});
		popWindowIsOnline.setOfflineOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				popWindowIsOnline.dismiss();
				isOnline = OnlineCondition.OFFLINE;
				isOnlineBT.setText(R.string.aty_car_list_search_isonline_offline);
				search();
			}
		});
		popWindowIsOnline.showAsDropDown(findViewById(R.id.search_is_online));
	}

	@Click(R.id.search)
	void onCarNoChanged()
	{
		search();
	}

	private void search()
	{
		searchNo = searchET.getText().toString();
		if (isOnline == OnlineCondition.ALL)
		{
			cars = carDao.getLikeNo(searchNo);
			mAdapter.notifyDataSetChanged(cars);
		} else if (isOnline == OnlineCondition.ONLINE)
		{
			long currentTime = System.currentTimeMillis();
			cars = carDao.getByDateTimeLikeNo(searchNo, currentTime - GeneralUtil.OFFLINE_TIME, currentTime);
			mAdapter.notifyDataSetChanged(cars);
		} else if (isOnline == OnlineCondition.OFFLINE)
		{
			long currentTime = System.currentTimeMillis();
			cars = carDao.getByDateTimeLikeNo(searchNo, 0, currentTime - GeneralUtil.OFFLINE_TIME);
			mAdapter.notifyDataSetChanged(cars);
		}
	}

	@ItemClick(R.id.cars)
	void onItemClicked(int position)
	{
		onBackWithOneCar(position);
	}

	@Click(R.id.display_on_map)
	void onDisplayButtonClicked()
	{
		if (cars.isEmpty())
			finish();
		else if (cars.size() == 1)
			onBackWithOneCar(0);
		else
			onBackWithMultipleCars();
	}

	private void onBackWithOneCar(int position)
	{
		Intent intent = new Intent();
		Car car = cars.get(position);
		intent.putExtra("car", (Serializable) car);
		setResult(ResultCode.ONE_CAR, intent);
		finish();
	}

	private void onBackWithMultipleCars()
	{
		Intent intent = new Intent();
		intent.putExtra("cars", (Serializable) cars);
		setResult(ResultCode.MULTIPLE_CARS, intent);
		finish();
	}

	@Click(R.id.back)
	void onBack()
	{
		finish();
	}

}
