package com.yy.udbauth.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.AuthEvent.RefreshPicEvent;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.fragment.VerifyFragment.OnTokenErrorListener;
import com.yy.udbauth.ui.tools.OnNextVerifyResultListener;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 
 * 概述：图片验证码页面
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年11月6日 下午4:29:03
 */
public class PictureVerifyFragment extends UdbAuthBaseFragment implements OnTokenErrorListener {

	View mMainView;
	ImageView mImageView;
	UdbEditText mEtToken;
	TextView mTvTitle;
	TextView mTvRefreshPic;
	Button mBtnSubmit;

	/** 经BASE64编译之后的验证码图片*/
	String mPictureString;
	//请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
	String mRequestContext = null;
	//二次验证的内容
	NextVerify mNextVerify;
	//用户输入的用户名
	String mUsername;

	public PictureVerifyFragment(NextVerify v, String username) {
		mNextVerify = v;
		mPictureString = mNextVerify.data;
		mUsername = username;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_picture_verify;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mImageView = (ImageView) mMainView.findViewById(R.id.ua_fragment_verify_img);
		mBtnSubmit = (Button) mMainView.findViewById(R.id.ua_fragment_verify_btn_ok);
		mTvRefreshPic = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_btn_refresh_pic);
		mEtToken = (UdbEditText) mMainView.findViewById(R.id.ua_fragment_verify_et_token);
		mTvTitle = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_txt_title);

		mTvTitle.setText(mNextVerify.promptTitle + " " + mNextVerify.promptContent);
		mImageView.setOnClickListener(onRefreshPictureClickListener);
		mTvRefreshPic.setOnClickListener(onRefreshPictureClickListener);
		mBtnSubmit.setOnClickListener(onSubmitClickListener);
		mEtToken.bindCleanButton(R.id.ua_fragment_verify_btn_clear_token);
		mEtToken.setHint(mNextVerify.selectTitle);

		//设置标题
		setTitleBarText(R.string.ua_title_second_verify);

		//显示验证码图片
		showPicture(mPictureString);

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {
		// 适配样式效果
		adjustButtonStyle(mBtnSubmit);
		adjustDefaultTextStyle(mTvRefreshPic);
		adjustDefaultTextStyle(mTvTitle);
	}

	/** 请求刷新验证码图片*/
	OnClickListener onRefreshPictureClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//调用刷新图片验证码接口
			mRequestContext = Long.toString(System.currentTimeMillis());
			AuthRequest.RefreshPicReq auth = new AuthRequest.RefreshPicReq(mUsername, mRequestContext);
			sendAuthRequest(auth);
		}
	};

	/** 提交token*/
	OnClickListener onSubmitClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String token = mEtToken.getText().toString().trim();

			if (TextUtils.isEmpty(token)) {
				showShortToast(R.string.ua_empty_picture_token);
				return;
			}

			if (getParentFragment() instanceof OnNextVerifyResultListener) {
				((OnNextVerifyResultListener) getParentFragment()).onVerifyResult(token, mNextVerify.strategy);
			}
		}
	};

	/** 转换图片并显示*/
	public void showPicture(String pic_base64) {
		if (pic_base64 == null) {
			showShortToast(R.string.ua_refresh_picture_failed_with_error);
			mImageView.setImageResource(R.drawable.ua_shape_rectangle);
			return;
		}

		AsyncTask<String, Integer, Bitmap> task = new AsyncTask<String, Integer, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				try {
					byte[] pic_data = Base64.decode(params[0], Base64.DEFAULT);
					Bitmap bitmap = BitmapFactory.decodeByteArray(pic_data, 0, pic_data.length);
					return bitmap;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				if (bitmap != null) {
					mImageView.setImageBitmap(bitmap);
				} else {
					showShortToast(R.string.ua_refresh_picture_failed_when_decode);
					mImageView.setImageResource(R.drawable.ua_shape_rectangle);
				}
				super.onPostExecute(bitmap);
			}
		};

		task.execute(pic_base64);
	}

	/** 处理刷新图片验证码回调结果*/
	@Override
	protected void onRefreshPicEvent(RefreshPicEvent et) {

		if (mRequestContext == null || !mRequestContext.equals(et.context)) {
			return;
		}

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			showPicture(et.pic);
		} else {
			//showShortToast(R.string.ua_refresh_picture_failed, et.description);
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 超时机制*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mRequestContext != null && mRequestContext.equals(et.context)) {
			showShortToast(R.string.ua_timeout_refresh_picture);
		}
	}

	@Override
	public void onTokenError() {
		showShortToast(R.string.ua_login_failed_with_err_piccode);
		mEtToken.setText("");
		mEtToken.requestFocus();
	}

}
