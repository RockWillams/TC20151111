package com.taichang.dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import com.taichang.bean.AccountPref_;

@EBean
public class AccountDao
{
	@Pref
	AccountPref_ pref;
	
	public String getUserid()
	{
		return pref.userid().get();
	}
	
	public void setUserid(String userid)
	{
		pref.edit().userid().put(userid).apply();
	}
	
	// Êý¾ÝÇå¿Õ
	public void clearData()
	{
		pref.edit().clear().apply();
	}
}
