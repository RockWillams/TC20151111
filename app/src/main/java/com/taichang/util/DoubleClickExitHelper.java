package com.taichang.util;

import static com.taichang.util.GeneralUtil.showToast;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

/** 
 * @ClassName: DoubleClickExitHelper 
 * @Description: 双击退出
 * @author: Psyche
 * @date: 2015年9月25日
 */
public class DoubleClickExitHelper
{
	/** 
	 * @Fields exitTime : 双击间隔时间
	 */ 
	private long exitTime = 2000;
	
	private boolean mIsExit = false;
	private Context mContext;

	public DoubleClickExitHelper(Context context)
	{
		mContext = context;
	}

	/**
	 * @Title: onKeyDown
	 * @Description: 双击退出
	 * @return: boolean
	 * @param keyCode
	 * @author: Psyche
	 * @date: 2015年9月25日
	 */
	public boolean onKeyDown(int keyCode)
	{
		if(keyCode != KeyEvent.KEYCODE_BACK)
			return false;
		if (mIsExit)
		{
			System.exit(0);
		} else
		{
			mIsExit = true;
			showToast(mContext, "再按一次退出应用", Toast.LENGTH_SHORT);
			new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					mIsExit = false;
				}
			}, exitTime);
		}
		return true;
	}
}
