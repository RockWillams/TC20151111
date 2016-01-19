package com.taichang.ui.activity;

import static com.taichang.util.GeneralUtil.showToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.OnActivityResult;
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
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.taichang.R;
import com.taichang.adapter.AreaAdapter;
import com.taichang.bean.Area;
import com.taichang.bean.Car;
import com.taichang.dao.AccountDao;
import com.taichang.dao.MapStatusDao;
import com.taichang.dao.SettingsDao;
import com.taichang.db.CarDao;
import com.taichang.gps.MyLocationListener;
import com.taichang.interf.MyLocationObserver;
import com.taichang.util.DoubleClickExitHelper;
import com.taichang.util.GeneralUtil;
import com.taichang.util.NetworkUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.CursorJoiner.Result;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;


@WindowFeature({ Window.FEATURE_NO_TITLE })
@Fullscreen
@EActivity(R.layout.aty_main)
public class AtyMain extends Activity implements MyLocationObserver
{

	private interface RequestCode
	{
		final int CAR_SEARCH = 0;
		final int CAR_TRACK = 1;
		final int CAR_WARNING = 2;
		final int CAR_COLLECTION = 3;
		final int CAR_SETTINGS = 4;
	}

	/**
	 * @ClassName: ResultCode
	 * @Description: 默认ResultCode是0？
	 * @author: Psyche
	 * @date: 2015年8月19日
	 */
	public interface ResultCode
	{
		final int ONE_CAR = 1;
		final int MULTIPLE_CARS = 2;
	}

	private DoubleClickExitHelper mDoubleClickExit;
	private RequestQueue mQueue;
	private long updateGap = 60 * 1000;
	private Timer timer;

	@Bean
	MyLocationListener mLocationListener;

	@Bean
	SettingsDao settingsDao;
	
	@Bean
	AccountDao dao;

	@ViewById(R.id.mapView)
	MapView mMapView;
	private BaiduMap mBaiduMap;

	@ViewById(R.id.search_content)
	AutoCompleteTextView searchTV;
	private PoiSearch mPoiSearch;

	private MapStatusDao mapStatusDao;

	private final int[] scopes =
	{ 2000000, 1000000, 500000, 200000, 100000, 50000, 25000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20 };// 单位：米

	private float zoom;// 缩放级别，最大放大至19，最小缩小至3

	private LatLng centerLl;
	
	private BitmapDescriptor onlineMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_car_online);
	private BitmapDescriptor offlineMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_car_offline);

	private CarDao carDao;
	private List<Car> cars;
	private Marker[] markers;



	@AfterViews
	void init()
	{
		removeBaiduMark();
		initRequiredComponents();
		initMyLocationService();
		initDatas();
		moveToCenterAndZoom();
		drawMarkers();
		updateDataFromServer();
		setMarkerListener();

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
		mMapView.showZoomControls(false);
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
		carDao = new CarDao(this, true);
		timer = new Timer();
		mapStatusDao = new MapStatusDao(this);
		mDoubleClickExit = new DoubleClickExitHelper(this);
		initSearchComponents();
	}

	private void initMyLocationService()
	{
		mLocationListener.registerObserver(this);
		mBaiduMap.setMyLocationEnabled(true);//设置定位图层为不激活
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.FOLLOWING, false, null));
	}

	ArrayAdapter<String> sugAdapter;
	SuggestionSearch suggestionSearch;

	private void initSearchComponents()
	{
		// searchTV = (AutoCompleteTextView) findViewById(R.id.search_content);
		searchTV.setOnEditorActionListener(new OnEditorActionListener()
		{

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					searchPosition();
				}
				return false;
			}
		});
		sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
		searchTV.setAdapter(sugAdapter);
		suggestionSearch = SuggestionSearch.newInstance();
		suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener()
		{

			@Override
			public void onGetSuggestionResult(SuggestionResult result)
			{
				if (result == null || result.getAllSuggestions() == null)
				{
					return;
				}
				sugAdapter.clear();
				for (SuggestionResult.SuggestionInfo info : result.getAllSuggestions())
				{
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();
			}
		});
		searchTV.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence cs, int start, int before, int count)
			{
				if (cs.length() <= 0)
					return;
				suggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(
						mapStatusDao.getCity()));
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

		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener()
		{

			@Override
			public void onGetPoiResult(PoiResult result)
			{
				if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND)
				{
					Toast.makeText(AtyMain.this, "未找到结果，请检查您输入的地址或扩大搜索范围", Toast.LENGTH_LONG).show();
					return;
				}
				if (result.error == SearchResult.ERRORNO.NO_ERROR)
				{
					PoiOverlay overlay = new PoiOverlay(mBaiduMap)
					{
						public boolean onPoiClick(int index)
						{
							super.onPoiClick(index);
							return true;
						}
					};
					mBaiduMap.setOnMarkerClickListener(overlay);
					overlay.setData(result);
					overlay.addToMap();
					overlay.zoomToSpan();
					return;
				}
			}

			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0)
			{
			}
		});
	}

	@Click(R.id.search)
	void search()
	{
		searchPosition();
	}

	private void searchPosition()
	{
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(mapStatusDao.getCity())
				.keyword(searchTV.getText().toString()).pageNum(0));
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive())
		{
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * @Title: initDatas
	 * @Description: 初始化上次退出时的数据
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年8月14日
	 */
	private void initDatas()
	{
		// TODO 车的数据从上次取
		cars = new ArrayList<Car>();
		cars.addAll(carDao.getAll());
		centerLl = new LatLng(mapStatusDao.getLat(), mapStatusDao.getLon());
		zoom = mapStatusDao.getZoom();
		moveToCenterAndZoom();
		updateGap = settingsDao.getUpdateGap();
	}

	@Click(R.id.area)
	void locationSelect()
	{
		// TODO >>>>>测试代码，后期移植到数据库
		final List<Area> areaList = new ArrayList<Area>();
		Area chengDu = new Area("成铁局", 30.663534, 104.072340);
		Area guangZhou = new Area("广铁局", 23.146635, 113.272165);
		areaList.add(chengDu);
		areaList.add(guangZhou);
		// >>>>>
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("请选择区域");
		builder.setSingleChoiceItems(new AreaAdapter(this, areaList), 0, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				showToast(AtyMain.this, areaList.get(which).getAreaName(), Toast.LENGTH_LONG);
				// TODO 显示周围多少距离的车辆？
				centerLl = new LatLng(areaList.get(which).getLat(), areaList.get(which).getLon());
				moveToCenterAndZoom();
			}
		});
		builder.show();
	}

	private void updateDataFromServer()
	{
		Request<JSONObject> request = new JsonObjectRequest(NetworkUtil.HTTP_SERVER
				+ NetworkUtil.SERVER_PAGE_ALL_CAR_STATUS/*
														 * +"?userid="+ userid+
														 * "&which=" +which
														 */, null, new Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject jsObject)    //jsObject example:{"result":[{"carNo":"guangzhou1","lat":"30.548977748754","lon":"104.07759108986","datetime":"1448431152"},{"carNo":"guangzhou2","lat":"32.271858364636","lon":"104.20808784603","datetime":"1495823933"},{"carNo":"chengdu0","lat":"30.4772442804383","lon":"104.03386541711","datetime":"1449038741"},{"carNo":"chengdu1","lat":"30.476682598687","lon":"104.033417611045","datetime":"1448980595"},{"carNo":"chengdu2","lat":"30.4729922093886","lon":"104.031862621554","datetime":"1449039689"},{"carNo":"beijing0","lat":"30.5553611","lon":"104.0812345","datetime":"1448956923"},{"carNo":"beijing1","lat":"30.5553611","lon":"104.0812345","datetime":"1448956923"},{"carNo":"beijing2","lat":"","lon":"","datetime":""},{"carNo":"beijing3","lat":"","lon":"","datetime":""},{"carNo":"beijing4","lat":"","lon":"","datetime":""},{"carNo":"beijing5","lat":"","lon":"","datetime":""},{"carNo":"beijing6","lat":"","lon":"","datetime":""}]}
			{
				Log.d("volley", jsObject.toString());
				try
				{
					String result = jsObject.getString("result");
					JSONArray jsArray = new JSONArray(result);
					List<Car> newCars = convertData(jsArray);
					carDao.updateMultiple(newCars);
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
				// 数据库更新后刷新车辆
				
				cars.clear();
				cars.addAll(carDao.getAll());
				drawMarkers();
				new Handler().postDelayed(new Runnable()
				{
					
					@Override
					public void run()
					{
						updateDataFromServer();
					}
				}, updateGap);
//				timer.schedule(new TimerTask()
//				{
//
//					@Override
//					public void run()
//					{
//						updateDataFromServer();
//					}
//				}, updateGap);
				// TODO 是否应该刷新位置？
			}
		}, new ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError arg0)
			{
				// Log.d("volley", "carlist error:"+arg0.toString());
				showToast(AtyMain.this, R.string.common_error_net, Toast.LENGTH_LONG);
				new Handler().postDelayed(new Runnable()
				{
					
					@Override
					public void run()
					{
						updateDataFromServer();
					}
				}, updateGap);
			}
		});
		request.setTag(this);
		mQueue.add(request);
	}

	private List<Car> convertData(JSONArray jsArray)
	{
		List<Car> newCars = new ArrayList<Car>(jsArray.length());
		for (int i = 0; i < jsArray.length(); i++)
		{
			try
			{
				JSONObject jsObject = jsArray.getJSONObject(i);
				String carNo = jsObject.getString("carNo");
				String lat = jsObject.getString("lat");
				String lon = jsObject.getString("lon");
				String dateTime = jsObject.getString("datetime");
				if ("".equals(carNo) || "".equals(lat) || "".equals(lon) || "".equals(dateTime))
					continue;
				Car newCar = new Car(carNo, Double.parseDouble(lat), Double.parseDouble(lon), Long.parseLong(dateTime)
						* 1000 - GeneralUtil.TIME_DIF_UTC);
				newCars.add(newCar);
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return newCars;
	}

	private void clearCarMarkers()
	{
		if(markers!=null)
		for (Marker marker : markers)
			marker.remove();
	}

	/**
	 * @Title: drawMarkers
	 * @Description: 根据表中车的数据在地图上生成车辆图标
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年8月14日
	 */
	private void drawMarkers()
	{
		// mBaiduMap.clear();
		clearCarMarkers();
		markers = new Marker[cars.size()];
		for (int i = 0; i < markers.length; i++)
		{
			long currentTime = System.currentTimeMillis();
			Car car = cars.get(i);
			Bundle bundle = new Bundle();
			bundle.putInt("position", i);
			if (currentTime - car.getDateTime() <= 60000)
			{
				OverlayOptions oo = new MarkerOptions().position(new LatLng(car.getLat(), car.getLon()))
						.icon(onlineMarker).zIndex(5).extraInfo(bundle);

				markers[i] = (Marker) (mBaiduMap.addOverlay(oo));
			} else
			{
				OverlayOptions oo = new MarkerOptions().position(new LatLng(car.getLat(), car.getLon()))
						.icon(offlineMarker).zIndex(5).extraInfo(bundle);
				markers[i] = (Marker) (mBaiduMap.addOverlay(oo));
			}
		}
	}

	private void setMarkerListener()
	{
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener()
		{

			@Override
			public boolean onMarkerClick(Marker marker)
			{
				// LayoutInflater inflater = LayoutInflater.from(AtyMain.this);
				// View v = inflater.inflate(R.layout.pop_marker, null);
				// TextView carNoTv = (TextView) v.findViewById(R.id.carNo);
				Button carNoBT = new Button(getApplicationContext());
				carNoBT.setBackgroundResource(R.drawable.bg_pop_marker);
				int i = marker.getExtraInfo().getInt("position");
				final Car car = cars.get(i);
				carNoBT.setText(car.getCarNo());
				carNoBT.setTextColor(getResources().getColor(R.color.button_text_black));
				// carNoBT.setOnClickListener(new View.OnClickListener()
				// {
				//
				// @Override
				// public void onClick(View v)
				// {
				// Intent intent = AtyCarTrack_.intent(AtyMain.this).get();
				// intent.putExtra("carNo", car.getCarNo());
				// intent.putExtra("lat", car.getLat());
				// intent.putExtra("lon", car.getLon());
				// startActivity(intent);
				// mBaiduMap.hideInfoWindow();
				// }
				// });
				// infoWindow = new InfoWindow(carNoBT, marker.getPosition(),
				// -47);
				// Log.d("volley", car.getCarNo());
				InfoWindow infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(carNoBT), marker.getPosition(),
						-67, new OnInfoWindowClickListener()
						{

							@Override
							public void onInfoWindowClick()
							{
								Intent intent = AtyCarTrack_.intent(AtyMain.this).get();
								intent.putExtra("carNo", car.getCarNo());
								intent.putExtra("lat", car.getLat());
								intent.putExtra("lon", car.getLon());
								intent.putExtra("dateTime", car.getDateTime());
								startActivity(intent);
								mBaiduMap.hideInfoWindow();
							}
						});
				mBaiduMap.showInfoWindow(infoWindow);
				return true;
			}
		});
	}

	@Click(R.id.to_car_search)
	void toCarSearch()
	{
		startActivityForResult(AtyCarSearch_.intent(this).get(), RequestCode.CAR_SEARCH);
//		startActivity(ActivityMain_.intent(this).get());
	}

	/**
	 * @Title: onCarListResult
	 * @Description: 从车辆列表页面返回时取出搜索的结果，更新表
	 * @return: void
	 * @param resultCode
	 * @param intent
	 * @author: Psyche
	 * @date: 2015年8月14日
	 */
	@SuppressWarnings("unchecked")
	@OnActivityResult(RequestCode.CAR_SEARCH)
	void onCarSearchResult(int resultCode, Intent intent)
	{
		if (resultCode == ResultCode.ONE_CAR)
		{
			if (cars!=null)
			{
				cars.clear();
				Car car = (Car) intent.getSerializableExtra("car");
				cars.add(car);
				drawMarkers();
				setCenterAndZoom();
				moveToCenterAndZoom();
			}

		} else if (resultCode == ResultCode.MULTIPLE_CARS)
		{
			cars.clear();
			cars.addAll((List<Car>) intent.getSerializableExtra("cars"));
			drawMarkers();
			setCenterAndZoom();
			moveToCenterAndZoom();
		}
	}

	@Click(R.id.to_collection)
	void toCollection()
	{
		startActivityForResult(AtyCollection_.intent(this).get(), RequestCode.CAR_COLLECTION);
	}

	@OnActivityResult(RequestCode.CAR_COLLECTION)
	void onCarCollectionResult(int resultCode, Intent intent)
	{
		if (resultCode == ResultCode.ONE_CAR)
		{
			cars.clear();
			String carNo = intent.getStringExtra("carNo");
			Car car = carDao.getByNo(carNo);
			cars.add(car);
			drawMarkers();
			setCenterAndZoom();
			moveToCenterAndZoom();
		}
	}

	@Click(R.id.to_car_warning)
	void toCarWarning()
	{
		startActivity(AtyWarning_.intent(this).get());
	}

	@Click(R.id.to_settings)
	void toSettings()
	{
		startActivityForResult(AtySettings_.intent(this).get(), RequestCode.CAR_SETTINGS);
	}

	@OnActivityResult(RequestCode.CAR_SETTINGS)
	void onSettingsResult()
	{
		updateGap = settingsDao.getUpdateGap();
	}

	/**
	 * @Title: setCenterAndZoom
	 * @Description: 设置地图中心点和地图倍数
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年8月14日
	 */
	private void setCenterAndZoom()
	{
		// 计算中心点，重复计算了第一辆车，懒得修改了
		Car firstCar = cars.get(0);
		double maxLat = firstCar.getLat(), minLat = firstCar.getLat();
		double maxLon = firstCar.getLon(), minLon = firstCar.getLon();
		for (Car car : cars)
		{
			if (car.getLat() > maxLat)
				maxLat = car.getLat();
			if (car.getLat() < minLat)
				minLat = car.getLat();
			if (car.getLon() > maxLon)
				maxLon = car.getLon();
			if (car.getLon() < minLon)
				minLon = car.getLon();
		}
		centerLl = new LatLng((maxLat + minLat) / 2, (maxLon + minLon) / 2);

		// 计算缩放倍数，大于10都以10算。
		double difLat = maxLat - minLat;
		double difLon = maxLon - minLon;
		// Point point = new Point();
		// getWindowManager().getDefaultDisplay().getRealSize(point);
		// DisplayMetrics dm = getResources().getDisplayMetrics();
		// double x = Math.pow(point.x/dm.xdpi, 2);
		int scope = (int) ((difLat) > (difLon) ? difLat * 111000 : difLon * 111000);
		scope /= 7;// TODO 一般手机大概7公分宽，暂时不计算屏幕物理尺寸，用大概值代替，后期再优化
		int i = scopes.length - 1;
		for (; i >= 0; i--)
		{
			if (scope < scopes[i])
				break;
		}
		zoom = i + 3;
		if (zoom > 10)
			zoom = 10;
	}

	@Override
	public void updateMyLocation(BDLocation myLocation)
	{
		MyLocationData locData = new MyLocationData.Builder().latitude(myLocation.getLatitude())
				.longitude(myLocation.getLongitude()).build();
		mBaiduMap.setMyLocationData(locData);
		mapStatusDao.setLat(myLocation.getLatitude());
		mapStatusDao.setLon(myLocation.getLongitude());
		mapStatusDao.setCity(myLocation.getCity());
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
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		mBaiduMap.setMyLocationEnabled(false);
		mLocationListener.unregisterObserver(this);
		mMapView.onDestroy();
		mMapView = null;
		mQueue.cancelAll(this);
//		saveStatus();
		super.onDestroy();
	}

	/**
	 * @Title: saveStatus
	 * @Description: 存储地图状态信息
	 * @return: void
	 * @author: Psyche
	 * @date: 2015年8月13日
	 */
//	private void saveStatus()
//	{
//		mapStatusDao.setZoom(mBaiduMap.getMapStatus().zoom);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
//			saveStatus();
			return mDoubleClickExit.onKeyDown(keyCode);
		}
		return super.onKeyDown(keyCode, event);
	}

	
	
	@Click(R.id.exit)
	void exit(){
		Intent intent = new Intent();
		intent.setClass(AtyMain.this, AtyLogin_.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		dao.clearData();
		startActivity(intent);
		finish();
	}
}
