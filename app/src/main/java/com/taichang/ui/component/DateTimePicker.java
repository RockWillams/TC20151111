package com.taichang.ui.component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.taichang.R;
import com.taichang.interf.OnDateTimePickListener;
import com.taichang.util.GeneralUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePicker
{
	private Activity mActivity;

	private DatePicker datePicker;
	private TimePicker timePicker;
	private Spinner lastPicker;
	private Spinner typePicker;
	private long dateTime;
	private long lastTime;
	private boolean needDay;
	private Calendar cal;
	private String title;
	private OnDateTimePickListener listener;
	private AlertDialog ad;
	private int typePosition = 0;
	private ArrayAdapter<Integer> adapterLast;

	public DateTimePicker(Activity activity, OnDateTimePickListener listener,
			long dateTime, long lastTime, boolean needDay, String title)
	{
		mActivity = activity;
		this.listener = listener;
		this.dateTime = dateTime;
		this.lastTime = lastTime;
		this.needDay = needDay;
		this.title = title;
		init();
	}

	private void init()
	{
		//据说某些手机会弹出键盘，所以不管有没有先隐藏了
		InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		
		LinearLayout dateTimeLayout = (LinearLayout) mActivity
				.getLayoutInflater().inflate(R.layout.pop_date_time, null);
		initDatePicker(dateTimeLayout);
		initTimePicker(dateTimeLayout);
		initLastTimePicker(dateTimeLayout);

		ad = new AlertDialog.Builder(mActivity).setTitle(title)
				.setView(dateTimeLayout)
				.setPositiveButton("确定", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dateTime = cal.getTimeInMillis();
						listener.onDateTimePick(dateTime, lastTime);
						ad.dismiss();
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

	private void initDatePicker(LinearLayout layout)
	{
		datePicker = (DatePicker) layout.findViewById(R.id.datepicker);
		cal = Calendar.getInstance(Locale.CHINA);
		cal.setTimeInMillis(dateTime);
		
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener()
				{

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth)
					{
						cal.set(year, monthOfYear, dayOfMonth);
					}
				});
	}

	private void initTimePicker(LinearLayout layout)
	{
		timePicker = (TimePicker) layout.findViewById(R.id.timepicker);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener()
		{

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
			{
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
			}
		});

	}

	private void initLastTimePicker(LinearLayout layout)
	{
		lastPicker = (Spinner) layout.findViewById(R.id.last_timepicker);
		typePicker = (Spinner) layout.findViewById(R.id.type);
		String[] itemsType;
		if (needDay)
		{
			itemsType = new String[2];
			itemsType[0] = "小时";
			itemsType[1] = "天";
		} else
		{
			itemsType = new String[1];
			itemsType[0] = "小时";
		}
		ArrayAdapter<String> adapterType = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_dropdown_item, itemsType);
		typePicker.setAdapter(adapterType);
		typePicker.setSelection(0, true);

		final Integer[][] itemsLast =
		{
				{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 },
				{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
						18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 } };
		adapterLast = new ArrayAdapter<Integer>(mActivity,
				android.R.layout.simple_spinner_dropdown_item, itemsLast[0]);
		lastPicker.setAdapter(adapterLast);
		lastPicker.setSelection(0, true);

		typePicker.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				typePosition = position;
				adapterLast = new ArrayAdapter<Integer>(mActivity,
						android.R.layout.simple_spinner_dropdown_item,
						itemsLast[position]);
				lastPicker.setAdapter(adapterLast);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
		lastPicker.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				long i = (Integer) parent.getItemAtPosition(position);
				if (typePosition == 0)
				{
					lastTime = 60 * 60 * 1000 * i;
					Log.d("volley", "lastTime=" + lastTime);
				} else if (typePosition == 1)
				{
					lastTime = 60 * 60 * 24 * 1000 * i;
					Log.d("volley", "lastTime=" + lastTime);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}
}
