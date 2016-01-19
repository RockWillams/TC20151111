package com.taichang.ui.component;

import com.taichang.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class IsOnlinePopupWindow extends PopupWindow
{
	protected TextView all;
	protected TextView online;
	protected TextView offline;

	public IsOnlinePopupWindow(Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.pop_is_online, null);
		this.setContentView(v);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(new ColorDrawable(0xFF));
		all = (TextView) v.findViewById(R.id.all);
		online = (TextView) v.findViewById(R.id.online);
		offline = (TextView) v.findViewById(R.id.offline);
	}

	public void setAllOnClickListener(OnClickListener listener)
	{
		all.setOnClickListener(listener);
	};

	public void setOnlineOnClickListener(OnClickListener listener)
	{
		online.setOnClickListener(listener);
	};

	public void setOfflineOnClickListener(OnClickListener listener)
	{
		offline.setOnClickListener(listener);
	};
}
