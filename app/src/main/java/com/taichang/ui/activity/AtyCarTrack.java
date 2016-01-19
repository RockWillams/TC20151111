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
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.taichang.R;
import com.taichang.bean.Car;
import com.taichang.bean.Collection;
import com.taichang.bean.TrackPoint;
import com.taichang.db.CollectionDao;
import com.taichang.db.TrackPointDao;
import com.taichang.interf.OnDateTimePickListener;
import com.taichang.ui.component.DateTimePicker;
import com.taichang.util.GeneralUtil;
import com.taichang.util.NetworkUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@WindowFeature(
{ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.aty_car_track)
public class AtyCarTrack extends Activity
{
	public static final long MAX_TIME_DIFF = 60 * 60 * 12 * 1000;

	private RequestQueue mQueue;

	@ViewById
	TextView title;

	@ViewById(R.id.collect)
	ImageView collect;

	@ViewById(R.id.mapView)
	MapView mMapView;
	private BaiduMap mBaiduMap;

	private TrackPointDao tpDao;
	private CollectionDao colDao;
	private List<TrackPoint> tps;

	private String carNo;
	private Double lat;
	private Double lon;
	private Long dateTime;
	private LatLng centerLl;
	private float zoom;
	private long startDateTime;
	private long lastTime;

	private Marker marker;

	private BitmapDescriptor onlineMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_car_online);
	private BitmapDescriptor offlineMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_car_offline);

	@AfterViews
	void init()
	{
		initDatas();
		removeBaiduMark();
		initRequiredComponents();
		setCollection();
		moveToCenterAndZoom();
		initDateTime();
		search();
		drawTracks();
		updateDataFromServer();
	}

	private void initDatas()
	{
		Intent intent = getIntent();
		carNo = intent.getStringExtra("carNo");
		centerLl = new LatLng(intent.getDoubleExtra("lat", 30), intent.getDoubleExtra("lon", 104));
		zoom = 10;
		title.setText(carNo);
		tps = new ArrayList<TrackPoint>();
	}

	/**
	 * @Title: removeBaiduMark
	 * @Description: 去除左下角SB百度标志
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年8月13日
	 */


	private void removeBaiduMark()
	{
		mMapView.getChildAt(1).setVisibility(View.GONE);
	}

	/**
	 * @Title: initRequiredComponents
	 * @Description: 初始化必要组件
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年8月13日
	 */
	private void initRequiredComponents()
	{
		mBaiduMap = mMapView.getMap();
		mQueue = NetworkUtil.getVolleyRequestQueue(this);
		tpDao = new TrackPointDao(this, carNo);
		colDao = new CollectionDao(this);
	}

	private void setCollection()
	{
		if (colDao.getByNo(carNo) == null)// TODO
			collect.setImageResource(R.drawable.ic_star_grey_36dp);
		else
			collect.setImageResource(R.drawable.ic_star_yellow_36dp);
	}

	private void drawTracks()
	{
		mBaiduMap.clear();
		if (tps.size() < 2 || tps.size() > 9999)
		{
			return;
		}
		List<LatLng> tracks = new ArrayList<LatLng>(tps.size());
		for (TrackPoint tp : tps)
		{
			tracks.add(new LatLng(tp.getLat(), tp.getLon()));
			Log.d("volley", tp.getLat() + "," + tp.getLon());
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - tps.get(tps.size()-1).getDateTime() <= 60000)
		{
			OverlayOptions oo = new MarkerOptions().position(new LatLng(tps.get(tps.size()-1).getLat(), tps.get(tps.size()-1).getLon())).icon(onlineMarker).zIndex(5);
			mBaiduMap.addOverlay(oo);
		} else
		{
			OverlayOptions oo = new MarkerOptions().position(new LatLng(tps.get(tps.size()-1).getLat(), tps.get(tps.size()-1).getLon())).icon(offlineMarker).zIndex(5);
			mBaiduMap.addOverlay(oo);
		}
		
		OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(tracks);
		mBaiduMap.addOverlay(ooPolyline);
	}

	private void updateDataFromServer()
	{
		TrackPoint tp = tpDao.getLast();
		String startDateString = "";
		if (tp != null)
		{
			long startDate = tp.getDateTime();
			startDateString = startDate / 1000 + "";
		}
		Log.d("volley", startDateString);
		Request<JSONObject> request = new JsonObjectRequest(NetworkUtil.HTTP_SERVER + NetworkUtil.SERVER_PAGE_CAR_TRACK + "?carNo=" + carNo + "&startDate=" + startDateString,
				null, new Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject jsObject)
					{
						try
						{
							String result = jsObject.getString("result");
							// if ("1".equals(result))
							// return;
							JSONArray jsArray = new JSONArray(result);
							List<TrackPoint> newTps = convertData(jsArray);
							tpDao.addMultiple(newTps);
						} catch (JSONException e)
						{
							e.printStackTrace();
						}
						initDateTime();
						TrackPoint lastTp = tpDao.getLast();
						centerLl = new LatLng(lastTp.getLat(), lastTp.getLon());
						search();
						drawTracks();
						moveToCenterAndZoom();
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
						showToast(AtyCarTrack.this, R.string.common_error_net, Toast.LENGTH_LONG);
					}
				});
		request.setTag(this);
		mQueue.add(request);
	}

	private List<TrackPoint> convertData(JSONArray jsArray)
	{
		List<TrackPoint> tps = new ArrayList<TrackPoint>(jsArray.length());
		for (int i = 0; i < jsArray.length(); i++)
		{
			try
			{
				JSONObject jsObject = jsArray.getJSONObject(i);
				String lat = jsObject.getString("lat");
				String lon = jsObject.getString("lon");
				String dateTime = jsObject.getString("dateTime");
				TrackPoint tp = new TrackPoint(Long.parseLong(dateTime) * 1000 - GeneralUtil.TIME_DIF_UTC, Double.parseDouble(lat), Double.parseDouble(lon));
				tps.add(tp);
			} catch (JSONException e)
			{
				e.printStackTrace();
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		return tps;
	}

	public void initDateTime()
	{
		lastTime = 1000 * 60 * 60;
		TrackPoint lastTp = tpDao.getLast();
		if (lastTp != null)
			startDateTime = lastTp.getDateTime() - lastTime;
		else
			startDateTime = System.currentTimeMillis() - MAX_TIME_DIFF;
	}

	private void search()
	{
		tps = tpDao.getByDateTime(startDateTime, startDateTime + lastTime);
	}

	@Click(R.id.start_time)
	void onStartDateTimeClicked()
	{
		new DateTimePicker(this, new OnDateTimePickListener()
		{

			@Override
			public void onDateTimePick(long dateTime, long lastTime)
			{
				startDateTime = dateTime;
				AtyCarTrack.this.lastTime = lastTime;
				search();
				drawTracks();
			}
		}, startDateTime, lastTime, false, "设置查询开始时间");
	}

	@Click(R.id.collect)
	void collect()
	{
		if (colDao.getByNo(carNo) == null)
		{
			colDao.add(new Collection(carNo));
			showToast(this, "已收藏\t" + carNo, Toast.LENGTH_SHORT);
			setCollection();
		} else
		{
			colDao.delByNo(carNo);
			showToast(this, "已取消收藏\t" + carNo, Toast.LENGTH_SHORT);
			setCollection();
		}
	}

	/**
	 * @Title: moveToCenterAndZoom
	 * @Description: 将地图中心移到centerLl，并把地图缩放至zoom
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年8月13日
	 */
	private void moveToCenterAndZoom()
	{
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(centerLl, zoom);
		mBaiduMap.animateMapStatus(u);
	}

	@Override
	protected void onResume()
	{
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		mMapView.onPause();
		mQueue.cancelAll(this);
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Click(R.id.back)
	void onBack()
	{
		finish();
	}
}
