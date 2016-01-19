package com.taichang.ui.activity;

import static com.taichang.util.GeneralUtil.showToast;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taichang.R;
import com.taichang.adapter.WarningAdapter;
import com.taichang.bean.Warning;
import com.taichang.db.WarningDao;
import com.taichang.interf.OnDateTimePickListener;
import com.taichang.ui.component.DateTimePicker;
import com.taichang.util.GeneralUtil;
import com.taichang.util.NetworkUtil;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

@WindowFeature(
{ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.aty_warning)
public class AtyWarning extends Activity
{

	private RequestQueue mQueue;

	@ViewById
	TextView title;

	@ViewById(R.id.warnings)
	ListView listviewWarnings;

	@ViewById(R.id.search_car_no)
	EditText searchET;

	private long startDateTime;
	private long lastTime;
	private String searchNo;

	private WarningDao wnDao;
	private List<Warning> wns;

	private WarningAdapter mAdapter;

	@AfterViews
	void init()
	{
		title.setText(R.string.aty_warning_title);
		lastTime = 60 * 60 * 24 * 1000;
		startDateTime = System.currentTimeMillis() - lastTime;
		searchNo = "";
		wnDao = new WarningDao(this);
		mAdapter = new WarningAdapter(this, wns);
		search();
		listviewWarnings.setAdapter(mAdapter);
		mQueue = NetworkUtil.getVolleyRequestQueue(this);
		initSearch();
		updateDataFromServer();
	}

	private void initSearch()
	{
		searchET.setOnEditorActionListener(new OnEditorActionListener()
		{

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event)
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
	}

	@Click(R.id.search_time)
	void onDateTimeClicked()
	{
		new DateTimePicker(this, new OnDateTimePickListener()
		{

			@Override
			public void onDateTimePick(long dateTime, long lastTime)
			{
				startDateTime = dateTime;
				AtyWarning.this.lastTime = lastTime;
				search();
			}

		}, startDateTime, lastTime, true, "设置查询开始时间");
	}

	@Click(R.id.search)
	void onCarNoChanged()
	{
		search();
	}

	private void search()
	{
		searchNo = searchET.getText().toString();
		wns = wnDao.getByDateTimeLikeNo(searchNo, startDateTime, startDateTime
				+ lastTime);
		// wns = wnDao.getAll();
		// Log.d("volley", wns.get(0).getDateTime()+"");
		mAdapter.notifyDataSetChanged(wns);
	}

	private void updateDataFromServer()
	{
		Warning tp = wnDao.getLast();
		int serverId = 0;
		if (tp != null)
		{
			serverId = tp.getServerId();
		}
		Request<JSONObject> request = new JsonObjectRequest(
				NetworkUtil.HTTP_SERVER
						+ NetworkUtil.SERVER_PAGE_ALL_CAR_WARNING + "?id="
						+ serverId/*
								 * +"?userid="+userid+ "&which=" +which
								 */, null, new Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject jsObject)
					{
						
						try
						{
							String result = jsObject.getString("result");
							if ("1".equals(result))
								return;
							JSONArray jsArray = new JSONArray(result);
							List<Warning> newWns = convertData(jsArray);
							wnDao.addMultiple(newWns);
						} catch (JSONException e)
						{
							e.printStackTrace();
						}
						search();
						// tps = tpDao.getByDateTime(new Date(
						// System.currentTimeMillis() - 86400 * 20 * 1000),
						// new Date(System.currentTimeMillis()));
						// for (TrackPoint tp : tps)
						// {
						// Log.d("volley", tp.getDateTime() + "," + tp.getLat()
						// + ","
						// + tp.getLon());
						// }
					}
				}, new ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError arg0)
					{
						// if (arg0.toString().equals("1"))
						// return;
						 Log.d("volley", arg0.toString());
						showToast(AtyWarning.this, R.string.common_error_net,
								Toast.LENGTH_LONG);
					}
				});
		request.setTag(this);
		mQueue.add(request);
	}

	private List<Warning> convertData(JSONArray jsArray)
	{
		List<Warning> wns = new ArrayList<Warning>(jsArray.length());
		for (int i = 0; i < jsArray.length(); i++)
		{
			try
			{
				JSONObject jsObject = jsArray.getJSONObject(i);
				String lat = jsObject.getString("lat");
				String lon = jsObject.getString("lon");
				String serverId = jsObject.getString("id");
				String dateTime = jsObject.getString("dateTime");
				String carNo = jsObject.getString("carNo");
				String person = jsObject.getString("person");
				String phone = jsObject.getString("phone");
				Warning wn = new Warning(
						Integer.parseInt(serverId),
						carNo,
						Double.parseDouble(lat),
						Double.parseDouble(lon),
						(Long.parseLong(dateTime) * 1000 - GeneralUtil.TIME_DIF_UTC),
						person, phone);
				wns.add(wn);
			} catch (JSONException e)
			{
				e.printStackTrace();
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		return wns;
	}

	@Override
	protected void onPause()
	{
		mQueue.cancelAll(this);
		super.onPause();
	}
	
	@Click(R.id.back)
	void onBack()
	{
		finish();
	}
}
