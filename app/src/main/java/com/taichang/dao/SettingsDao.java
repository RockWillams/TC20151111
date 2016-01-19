package com.taichang.dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import com.taichang.bean.SettingsPref_;

@EBean
public class SettingsDao
{
	@Pref
	SettingsPref_ pref;

	public long getUpdateGap()
	{
		return pref.updateGap().get();
	}
	
	public void setUpdateGap(int gap)
	{
		pref.edit().updateGap().put(gap).apply();
	}
}
