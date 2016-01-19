package com.taichang.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.taichang.R;
import com.taichang.bean.Warning;
import com.taichang.util.GeneralUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WarningAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<Warning> items;

	public WarningAdapter(Context context, List<Warning> itemList)
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
			convertView = mInflater.inflate(R.layout.lv_warning, null);
			holder = new ViewHolder();
			holder.carNo = (TextView) convertView.findViewById(R.id.car_no);
			holder.datetime = (TextView) convertView
					.findViewById(R.id.datetime);
			holder.location = (TextView) convertView
					.findViewById(R.id.location);
			holder.person = (TextView) convertView.findViewById(R.id.person);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Warning item = items.get(position);
		holder.carNo.setText(item.getCarNo());
		SimpleDateFormat sdf = new SimpleDateFormat(GeneralUtil.TIME_FORMAT);
		String dateTime = sdf.format(item.getDateTime());
		holder.datetime.setText(dateTime);
		holder.location.setText(item.getLat() + ", " + item.getLon());
		holder.person.setText(item.getPerson());
		holder.phone.setText(item.getPhone());

		return convertView;
	}

	private static class ViewHolder
	{
		TextView carNo;
		TextView datetime;
		TextView location;
		TextView person;
		TextView phone;
	}

	/**
	 * Ë¢ÐÂÁÐ±í
	 * 
	 * @param newList
	 */
	public void notifyDataSetChanged(List<Warning> newList)
	{
		this.items = newList;
		notifyDataSetChanged();
	}
}
