package com.yy.udbauth.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yy.live.R;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.fragment.UdbAuthBaseFragment;
import com.yy.udbauth.ui.style.PageStyle;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.IUdbAuthActivityCallback;

import java.util.Stack;

/**
 * 概述：唯一的一个Activity，用于包装各种各样的Fragment并进行显示
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月18日 下午5:38:52
 */
public class UdbAuthActivity extends AppCompatActivity implements IUdbAuthActivityCallback {

	/** 表示传递一个Fragment类型（Class<? extends Fragment>）*/
	public static final String EXTRA_FRAGMENT_CLASS = "extra_fragment_class";
	/** 表示传递一个PageStyle类型*/
	public static final String EXTRA_PAGE_STYLE = "extra_page_style";

	private static Stack<UdbAuthActivity> mStack = new Stack<UdbAuthActivity>();

	/** 当前使用的Fragment*/
	public Fragment mCurrFragment;
	/** 表示传入的Fragment类名*/
	public Class<? extends Fragment> mFragmentClassName;
	/** 表示传入的页面样式*/
	public PageStyle mPageStyle;

	/** 主布局*/
	ViewGroup mMainLayout;
	/** 进度对话框*/
	ProgressDialog mProgressDialog;

	/** 关闭所有Activity*/
	public static void finishAll() {
		if (mStack != null) {
			for (UdbAuthActivity a : mStack) {
				if (a != null && a.isFinishing() == false) {
					a.finish();
				}
			}
		}
	}

	/** 获取当前存在的Activity的数目*/
	public static int getExistActivityNumber() {
		return mStack.size();
	}

	/** 
	 * 获取当前活动栈中是否包含了某个Fragment的Activity，仅支持根fragment
	 * @return 如果没有则返回null
	 */
	public static UdbAuthActivity getContainActivity(Class<? extends Fragment> cls) {
		if (mStack != null) {
			for (UdbAuthActivity a : mStack) {
				if (a.mFragmentClassName == cls) {
					return a;
				}
			}
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle bundle) {
		//增加到栈中
		if (mStack != null && !mStack.contains(this)) {
			mStack.push(this);
		}
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
		super.onCreate(bundle);
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_activity_udbauth;
		setContentView(layout);
		initComponent(); //初始化
	}

	@Override
	protected void onDestroy() {
		//从栈中移除
		if (mStack != null && mStack.contains(this)) {
			mStack.remove(this);
		}
		super.onDestroy();
	}

	/** 初始化*/
	@SuppressWarnings("unchecked")
	private void initComponent() {
		//关联布局元素
		mMainLayout = (ViewGroup) findViewById(R.id.ua_udbauth_main_layout);

		//获取外部参数
		mPageStyle = (PageStyle) getIntent().getSerializableExtra(EXTRA_PAGE_STYLE);
		mFragmentClassName = (Class<? extends Fragment>) getIntent().getSerializableExtra(EXTRA_FRAGMENT_CLASS);
		Fragment fragment = FragmentHelper.generateFragmentInstance(mFragmentClassName, getIntent().getExtras());

		if (fragment != null) {
			showFragment(fragment);//显示内容页
		}

		adjustStyle(); //适配页面样式
	}

	@Override
	public void onBackPressed() {
		if (mCurrFragment instanceof UdbAuthBaseFragment) {
			//  传递返回事件 
			if (((UdbAuthBaseFragment) mCurrFragment).onBackPressed()) {
				return;
			}
		}
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mCurrFragment != null) {
			//  传递结果事件 
			mCurrFragment.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 显示或替换当前Fragment*/
	public void showFragment(Fragment fragment) {
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.replace(R.id.ua_udbauth_content_layout, fragment);
		trans.commit();

		mCurrFragment = fragment;
	}

	/** 适配页面样式*/
	@SuppressWarnings("deprecation")
	private void adjustStyle() {
		if (mPageStyle == null) {
			//如果没有指定，那么使用全局样式表
			mPageStyle = AuthUI.getInstance().getGlobalPageStyle();

			if (mPageStyle == null)
				return;
		}

		findViewById(R.id.ua_titlebar_layout).setBackgroundColor(mPageStyle.titlebarColor);
		findViewById(R.id.ua_titlebar_back).setVisibility(mPageStyle.showBackButton ? View.VISIBLE : View.GONE);
		((TextView) findViewById(R.id.ua_titlebar_title)).setTextColor(mPageStyle.titlebarTextColor);

		if (!mPageStyle.showTitlebar) {
			findViewById(R.id.ua_titlebar_layout).setVisibility(View.GONE);
		} else {
			findViewById(R.id.ua_titlebar_layout).setVisibility(View.VISIBLE);
		}

		if (mPageStyle.backgroundBitmap != null) {
			mMainLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), mPageStyle.backgroundBitmap));
		} else if (mPageStyle.backgroundResId != 0) {
			mMainLayout.setBackgroundResource(mPageStyle.backgroundResId);
		} else {
			mMainLayout.setBackgroundColor(mPageStyle.backgroundColor);
		}
	}

	/**
	 * 标题栏按钮点击事件，根据view的ID来判断是哪个按钮<br/>
	 */
	public void onTitleBarClicked(View v) {
		if (v.getId() == R.id.ua_titlebar_back) {
			// 传递返回事件
			onBackPressed();
		}
	}

	/**
	 * 设置标题栏文字
	 * 
	 * @param title
	 *            标题文字
	 */
	@Override
	public void setTitleBarText(String title) {
		((TextView) findViewById(R.id.ua_titlebar_title)).setText(title);
	}

	/**
	 * 设置标题栏文字
	 * 
	 * @param resId
	 *            文字资源id
	 */
	@Override
	public void setTitleText(int resId) {
		setTitleBarText(getString(resId));
	}

	/**
	 * 设置标题栏按钮的显示状态
	 * 
	 * @param resId
	 *            按钮的资源ID，默认为“R.id.titlebar”开头
	 * @param visible
	 *            true表示显示，false表示隐藏
	 */
	public void setTitleBarButton(int resId, boolean visible) {
		if (findViewById(resId) != null)
			findViewById(resId).setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	/**
	 * 显示标题栏进度条
	 * 
	 * @param show
	 *            true表示显示，false表示隐藏
	 */
	public void showTitleBarProgress(boolean show) {
		findViewById(R.id.ua_titlebar_progress).setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * 显示进度条
	 * 
	 * @param msg
	 *            不为null时，表示显示内容；为null时表示隐藏
	 * @param listener
	 *            当用户按返回键取消时所触发的监听器
	 */
	@Override
	public void showProgressDialog(final String msg, final OnCancelListener listener) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (msg == null) {
					if (mProgressDialog != null && mProgressDialog.isShowing())
						mProgressDialog.dismiss();
				} else {
					if (mProgressDialog == null) {
						mProgressDialog = new ProgressDialog(UdbAuthActivity.this);
					} else if (mProgressDialog.isShowing()) {
						mProgressDialog.dismiss();
					}

					mProgressDialog.setMessage(msg);
					mProgressDialog.setCanceledOnTouchOutside(false);
					mProgressDialog.setCancelable(true);
					mProgressDialog.setOnCancelListener(listener);
					mProgressDialog.show();
				}
			}
		});
	}

	/**
	 * 获取页面配置样式表
	 */
	@Override
	public PageStyle getPageStyle() {
		return mPageStyle;
	}

}
