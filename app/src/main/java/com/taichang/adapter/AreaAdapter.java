package com.taichang.adapter;

import java.util.List;

import com.taichang.R;
import com.taichang.bean.Area;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AreaAdapter extends BaseAdapter
{
	private Context mContext;
	private List<Area> mAreaList;
	private LayoutInflater mInflater;

	public AreaAdapter(Context context, List<Area> areaList)
	{
		mContext = context;
		mAreaList = areaList;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return mAreaList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mAreaList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.lv_area, null);
			holder.item = (TextView)convertView.findViewById(R.id.area_item);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		holder.item.setText(mAreaList.get(position).getAreaName());
		return convertView;
	}

	public final class ViewHolder
	{
		public TextView item;
	}

}
