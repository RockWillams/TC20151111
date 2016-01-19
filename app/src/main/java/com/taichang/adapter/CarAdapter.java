package com.taichang.adapter;
import static com.taichang.util.GeneralUtil.OFFLINE_TIME;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.SimpleFormatter;

import com.taichang.R;
import com.taichang.bean.Car;
import com.taichang.util.GeneralUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CarAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<Car> items;

	public CarAdapter(Context context, List<Car> itemList)
	{
		super();
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.items = itemList;
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.lv_car, null);

			holder = new ViewHolder();
			holder.is_online = (ImageView) convertView
					.findViewById(R.id.is_online);
			holder.carNo = (TextView) convertView.findViewById(R.id.car_no);
			holder.datetime = (TextView) convertView
					.findViewById(R.id.datetime);
			holder.location = (TextView) convertView
					.findViewById(R.id.location);

			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Car item = items.get(position);
		long currentTime = System.currentTimeMillis();
		if (currentTime - item.getDateTime() > OFFLINE_TIME)//TODO 离线
			holder.is_online
					.setImageResource(R.drawable.ic_directions_car_grey_36dp);
		else//TODO 在线
			holder.is_online
					.setImageResource(R.drawable.ic_directions_car_blue_36dp);
		holder.carNo.setText(item.getCarNo());
		SimpleDateFormat sdf = new SimpleDateFormat(GeneralUtil.TIME_FORMAT);
		String dateTime = sdf.format(item.getDateTime());
		holder.datetime.setText(dateTime);
		holder.location.setText(item.getLat() + ", " + item.getLon());

		return convertView;
	}

	private static class ViewHolder
	{
		ImageView is_online;
		TextView carNo;
		TextView datetime;
		TextView location;
	}

	/**
	 * 刷新列表
	 * 
	 * @param newList
	 */
	public void notifyDataSetChanged(List<Car> newList)
	{
		this.items = newList;
		notifyDataSetChanged();
	}

}
