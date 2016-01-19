package com.taichang.ui.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.SupposeUiThread;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.WindowFeature;
import com.taichang.R;
import com.taichang.dao.AccountDao;

import android.app.Activity;
import android.view.Window;
@WindowFeature({ Window.FEATURE_NO_TITLE })
@Fullscreen
@EActivity(R.layout.aty_start)
public class AtyStart extends Activity
{
	private final long gapTime = 2000;
	
	@Bean
	AccountDao dao;
	
	@AfterInject
	@UiThread(delay = gapTime)
	void toNext() {
		
		if (dao.getUserid().equals(""))
		{
			toLogin();
		} else
		{
			toMain();
		}
	}
	
	@SupposeUiThread
	public void toLogin()
	{
		startActivity(AtyLogin_.intent(this).get());
		finish();
	}

	@SupposeUiThread
	void toMain()
	{
		// TODO
		startActivity(AtyMain_.intent(this).get());
		finish();
	}
}
