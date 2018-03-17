package com.yy.udbauth.ui.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yy.udbauth.ui.activity.UdbAuthActivity;
import com.yy.udbauth.ui.fragment.CountrySelectFragment;
import com.yy.udbauth.ui.fragment.FindMyPasswordFragment;
import com.yy.udbauth.ui.fragment.LoginFragment;
import com.yy.udbauth.ui.fragment.ModifyPasswordFragment;
import com.yy.udbauth.ui.fragment.RegisterFragment;
import com.yy.udbauth.ui.fragment.SmsLoginFragment;
import com.yy.udbauth.ui.fragment.VerifyFragment;
import com.yy.udbauth.ui.fragment.WebViewFragment;

public class FragmentHelper {

	/**
	 * 将指定的{@code Fragment}嵌入到{@code UdbAuthActivity}中，并启动
	 * @param activity	上下文对象
	 * @param fragmentClass	{@code Fragment}类，如{@code UdbLoginFragment.class}
	 */
	public static boolean startFragment(Activity activity, Class<? extends Fragment> fragmentClass) {
		return startFragment(activity, fragmentClass, null);
	}

	/**
	 * 将指定的{@code Fragment}嵌入到{@code UdbAuthActivity}中，并启动
	 * @param activity	上下文对象
	 * @param fragmentClass	{@code Fragment}类，如{@code UdbLoginFragment.class}
	 * @param bundle 传递给{@code UdbAuthActivity}的参数（同时会传递给对应的Fragment）
	 */
	public static boolean startFragment(Activity activity, Class<? extends Fragment> fragmentClass, Bundle bundle) {
		if (activity == null) {
			return false;
		}

		Intent intent = new Intent(activity, UdbAuthActivity.class);
		intent.putExtra(UdbAuthActivity.EXTRA_FRAGMENT_CLASS, fragmentClass);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		activity.startActivity(intent);

		return true;
	}

	/**
	 * 将指定的{@code Fragment}嵌入到{@code UdbAuthActivity}中，并启动
	 * @param activity	上下文对象
	 * @param fragmentClass	{@code Fragment}类，如{@code UdbLoginFragment.class}
	 * @param requestCode	请求码
	 */
	public static boolean startFragmentForResult(Activity activity, Class<? extends Fragment> fragmentClass,
			int requestCode) {
		return startFragmentForResult(activity, fragmentClass, requestCode, null);
	}

	/**
	 * 将指定的{@code Fragment}嵌入到{@code UdbAuthActivity}中，并启动
	 * @param activity	上下文对象
	 * @param fragmentClass	{@code Fragment}类，如UdbLoginFragment.class
	 * @param requestCode	请求码
	 * @param bundle 传递给{@code UdbAuthActivity}的参数（同时会传递给对应的Fragment）
	 */
	public static boolean startFragmentForResult(Activity activity, Class<? extends Fragment> fragmentClass,
			int requestCode, Bundle bundle) {

		if (activity == null) {
			return false;
		}

		Intent intent = new Intent(activity, UdbAuthActivity.class);
		intent.putExtra(UdbAuthActivity.EXTRA_FRAGMENT_CLASS, fragmentClass);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		activity.startActivityForResult(intent, requestCode);

		return true;
	}

	/**
	 * 停止所有UdbAuthActivity
	 */
	public static void finishAllActivity() {
		try {
			UdbAuthActivity.finishAll();
		} catch (Exception e) {
			;
		}
	}

	/** 
	 * 获取当前活动栈中是否包含了某个Fragment的Activity
	 * @return 如果没有则返回null
	 */
	public static UdbAuthActivity getContainActivity(Class<? extends Fragment> cls) {
		return UdbAuthActivity.getContainActivity(cls);
	}

	/**
	 * 获取一个内部Fragment实例
	 * 
	 * @param fragmentClass	{@code Fragment}类，如UdbLoginFragment.class
	 * @param bundle	参数，直接传给Fragment使用
	 * @return	返回一个与类名对应的新实例，并向实例传递bundle；如果找不到对应的类名，那么返回null
	 */
	public static Fragment generateFragmentInstance(Class<? extends Fragment> fragmentClass, Bundle bundle) {

		Fragment fragment = null;

		/** TODO-这里增加映射关系 */
		if (fragmentClass == LoginFragment.class) {
			fragment = new LoginFragment();//账号密码登录

		} else if (fragmentClass == SmsLoginFragment.class) {
			fragment = new SmsLoginFragment();//手机短信登录

		} else if (fragmentClass == FindMyPasswordFragment.class) {
			fragment = new FindMyPasswordFragment();//找回密码

		} else if (fragmentClass == ModifyPasswordFragment.class) {
			fragment = new ModifyPasswordFragment();//修改密码

		} else if (fragmentClass == RegisterFragment.class) {
			fragment = new RegisterFragment();//注册账号

		} else if (fragmentClass == VerifyFragment.class) {
			fragment = new VerifyFragment();//二次验证

		} else if (fragmentClass == WebViewFragment.class) {
			fragment = new WebViewFragment();//web页面

		} else if (fragmentClass == CountrySelectFragment.class) {
			fragment = new CountrySelectFragment();//国家区域选择

		}

		if (fragment != null && bundle != null)
			fragment.setArguments(bundle);

		return fragment;
	}
}
