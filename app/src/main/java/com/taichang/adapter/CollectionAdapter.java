package com.taichang.adapter;

import java.util.List;

import com.taichang.R;
import com.taichang.bean.Collection;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CollectionAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<Collection> items;

	public CollectionAdapter(Context context, List<Collection> itemList)
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
			convertView = mInflater.inflate(R.layout.lv_collection, null);
			holder = new ViewHolder();
			holder.carNo = (TextView) convertView.findViewById(R.id.car_no);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Collection item = items.get(position);
		holder.carNo.setText(item.getCarNo());

		return convertView;
	}

	private static class ViewHolder
	{
		TextView carNo;
	}

	/**
	 * Ë¢ÐÂÁÐ±í
	 * 
	 * @param newList
	 */
	public void notifyDataSetChanged(List<Collection> newList)
	{
		this.items = newList;
		notifyDataSetChanged();
	}
}
