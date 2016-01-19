package com.taichang.ui.activity;

import static com.taichang.util.GeneralUtil.showToast;

import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.taichang.R;
import com.taichang.dao.AccountDao;
import com.taichang.util.GeneralUtil;
import com.taichang.util.NetworkUtil;
import com.taichang.util.SecurityUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

@WindowFeature(
{ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.aty_login)
public class AtyLogin extends Activity
{
	@ViewById(R.id.username)
	EditText usernameET;

	@ViewById(R.id.password)
	EditText passwordET;
	
	@Bean
	AccountDao dao;

	private RequestQueue mQueue;

	private ProgressDialog pd;

	@AfterInject
	void init()
	{
		mQueue = NetworkUtil.getVolleyRequestQueue(this);
	}

	@Click
	void login()
	{
//		AtyLogin.this.startActivity(AtyMain_.intent(AtyLogin.this).get());
		final String username = getUsername();
		final String password = getPassword();
		if (username != null && !"".equals(username) && password != null)
		{
			pd = ProgressDialog.show(this, null, getResources().getString(R.string.aty_login_progress_dialog), false, true);
			mQueue.add(new StringRequest(Method.POST, NetworkUtil.HTTP_SERVER + NetworkUtil.SERVER_PAGE_LOGIN, new Response.Listener<String>()
			{
				@Override
				public void onResponse(String response)
				{
					pd.dismiss();
					JSONObject jObject;
					try
					{
						jObject = new JSONObject(response);
						String code = jObject.getString("code");// （0，成功；1，用户名不存在；2，密码错误）
						if ("0".equals(code))
						{
							String userid = jObject.getString("userid");
							showToast(AtyLogin.this, R.string.aty_login_success, Toast.LENGTH_SHORT);
							AtyLogin.this.startActivity(AtyMain_.intent(AtyLogin.this).get());
							AtyLogin.this.finish(); // TODO 进入主页面，存储userid
							dao.setUserid(userid);
						} else if ("1".equals(code))
						{
							showToast(AtyLogin.this, R.string.aty_login_error_username, Toast.LENGTH_LONG);
						} else if ("2".equals(code))
						{
							showToast(AtyLogin.this, R.string.aty_login_error_password, Toast.LENGTH_LONG);
						} // TODO 其它错误
					} catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			
				}
			}, new Response.ErrorListener()
			{

				@Override
				public void onErrorResponse(VolleyError error)
				{
					pd.dismiss();
					showToast(AtyLogin.this, R.string.common_error_net, Toast.LENGTH_LONG);
				}
			})
			{

				@Override
				protected Map<String, String> getParams() throws AuthFailureError
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", GeneralUtil.UTF8Encode(username));
					map.put("password", SecurityUtil.MD5Encode(password));
					return map;
				}
			});

		} else
		{
			showToast(AtyLogin.this, "账号或密码不能为空", Toast.LENGTH_LONG);
		}

	}

	private String getUsername()
	{
		return usernameET.getText().toString();
	}

	private String getPassword()
	{
		return passwordET.getText().toString();
	}
}
