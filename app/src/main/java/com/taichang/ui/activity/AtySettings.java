package com.taichang.ui.activity;

import static com.taichang.util.GeneralUtil.showToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import com.taichang.R;
import com.taichang.dao.SettingsDao;

import android.app.Activity;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@WindowFeature(
{ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.aty_settings)
public class AtySettings extends Activity
{
	@ViewById
	TextView title;

	@ViewById(R.id.update_gap)
	Spinner updateGap;

	@Bean
	SettingsDao settingsDao;

	private final String[] updateItem =
	{ "30秒", "1分钟", "2分钟", "5分钟", "10分钟", "60分钟" };

	@AfterViews
	void init()
	{
		title.setText(R.string.aty_settings_title);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, updateItem);
		updateGap.setAdapter(adapter);
		updateGap.setSelection(1, true);
	}

	@ItemSelect(R.id.update_gap)
	void selectUpdateGap(boolean selected, int position)
	{
		switch (position)
		{
		case 0:
			settingsDao.setUpdateGap(30000);
			showToast(this, "数据更新时间已设置为每30秒更新一次，每小时大约消耗流量2.5M",
					Toast.LENGTH_SHORT);
			break;
		case 1:
			settingsDao.setUpdateGap(60000);
			showToast(this, "数据更新时间已设置为每1分钟更新一次，每小时大约消耗流量1.3M",
					Toast.LENGTH_SHORT);
			break;
		case 2:
			settingsDao.setUpdateGap(120000);
			showToast(this, "数据更新时间已设置为每2分钟更新一次，每小时大约消耗流量600K",
					Toast.LENGTH_SHORT);
			break;
		case 3:
			settingsDao.setUpdateGap(300000);
			showToast(this, "数据更新时间已设置为每5分钟更新一次，每小时大约消耗流量240K",
					Toast.LENGTH_SHORT);
			break;
		case 4:
			settingsDao.setUpdateGap(600000);
			showToast(this, "数据更新时间已设置为每10分钟更新一次，每小时大约消耗流量120K",
					Toast.LENGTH_SHORT);
			break;
		case 5:
			settingsDao.setUpdateGap(1800000);
			showToast(this, "数据更新时间已设置为每60分钟更新一次，每小时大约消耗流量20K",
					Toast.LENGTH_SHORT);
			break;
		default:
			break;
		}
	}

	@Click(R.id.back)
	void onBack()
	{
		finish();
	}

}
