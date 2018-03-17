package com.yy.udbauth.ui.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yy.live.R;


/**
 * 概述：通用对话框
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年4月12日 上午10:26:50
 */
public class UdbDialog extends Dialog {

	public UdbDialog(Context context) {
		super(context);
	}

	public UdbDialog(Context context, int theme) {
		super(context, theme);
	}

	public UdbDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public void setPositiveBtnText(CharSequence text) {
		((Button) findViewById(R.id.ua_udb_dialog_btn_positive)).setText(text);
	}

	public void setPositiveBtnText(int resid) {
		((Button) findViewById(R.id.ua_udb_dialog_btn_positive)).setText(resid);
	}

	public void setNegativeBtnText(CharSequence text) {
		((Button) findViewById(R.id.ua_udb_dialog_btn_negative)).setText(text);
	}

	public void setNegativeBtnText(int resid) {
		((Button) findViewById(R.id.ua_udb_dialog_btn_negative)).setText(resid);
	}

	public void setMessage(CharSequence content) {
		((TextView) findViewById(R.id.ua_udb_dialog_tv_message)).setText(content);
	}

	public void setMessage(int resid) {
		((TextView) findViewById(R.id.ua_udb_dialog_tv_message)).setText(resid);
	}

	public static class Builder {

		/** 按钮组合类型*/
		enum BUTTON_TYPE {
			/** 无按钮*/
			NONE,
			/** 只有确认按钮*/
			POSITIVE,
			/** 只有否定按钮*/
			NEGATIVE,
			/** 既有确认按钮也有否定按钮*/
			BOTH
		};

		private View mContentView;
		private Button mPositiveButton = null;
		private Button mNegativeButton = null;

		private CharSequence mTitleCharSequence;
		private CharSequence mMessageCharSequence;
		private CharSequence mPositiveCharSequence;
		private CharSequence mNegativeCharSequence;

		private int mMessageGravity = Gravity.NO_GRAVITY;
		private boolean mAutoLink = true;

		private OnClickListener mPositiveBtnOnClickListener;
		private OnClickListener mNegativeOnClickListener;

		private Context mContext;
		private boolean mCancelable = true;

		public Builder(Context mContext) {
			super();
			this.mContext = mContext;
		}

		public Builder setAutoLink(boolean autolink) {
			this.mAutoLink = autolink;
			return this;
		}

		public Builder setTitle(CharSequence title) {
			this.mTitleCharSequence = title;
			return this;
		}

		public Builder setTitle(int titleResId) {
			this.mTitleCharSequence = (String) mContext.getText(titleResId);
			return this;
		}

		public Builder setMessage(CharSequence message) {
			this.mMessageCharSequence = message;
			return this;
		}

		public Builder setMessage(int messageResId) {
			this.mMessageCharSequence = (String) mContext.getText(messageResId);
			return this;
		}

		public Builder setMessageGravity(int gravity) {
			mMessageGravity = gravity;
			return this;
		}

		public Builder setContentView(View v) {
			this.mContentView = v;
			return this;
		}

		public Builder setPositiveButton(CharSequence positiveText, OnClickListener listener) {
			this.mPositiveCharSequence = positiveText;
			this.mPositiveBtnOnClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(int positiveTextResId, OnClickListener listener) {
			this.mPositiveCharSequence = (String) mContext.getText(positiveTextResId);
			this.mPositiveBtnOnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(CharSequence negativeText, OnClickListener listener) {
			this.mNegativeCharSequence = negativeText;
			this.mNegativeOnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negitiveTextResId, OnClickListener listener) {
			this.mNegativeCharSequence = (String) mContext.getText(negitiveTextResId);
			this.mNegativeOnClickListener = listener;
			return this;
		}

		public Builder setCancelable(boolean cancelable) {
			this.mCancelable = cancelable;
			return this;
		}

		@SuppressLint("InflateParams")
		public UdbDialog create() {

			final UdbDialog dialog = new UdbDialog(mContext);

			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewGroup mainLayout = (ViewGroup) inflater.inflate(R.layout.ua_dialog, null);

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.addContentView(mainLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			TextView titleTextView = (TextView) mainLayout.findViewById(R.id.ua_udb_dialog_tv_title);
			TextView msgTextView = (TextView) mainLayout.findViewById(R.id.ua_udb_dialog_tv_message);
			ScrollView messageLayout = (ScrollView) mainLayout.findViewById(R.id.ua_udb_dialog_message_layout);
			FrameLayout customLayout = (FrameLayout) mainLayout.findViewById(R.id.ua_udb_dialog_custom_layout);

			mNegativeButton = (Button) mainLayout.findViewById(R.id.ua_udb_dialog_btn_negative);
			mPositiveButton = (Button) mainLayout.findViewById(R.id.ua_udb_dialog_btn_positive);

			//标题
			if (mTitleCharSequence != null) {
				titleTextView.setText(mTitleCharSequence);
			} else {
				mainLayout.removeView(titleTextView);
			}

			//内容
			if (mMessageCharSequence != null) {
				msgTextView.setText(mMessageCharSequence);
				if (mAutoLink) {
					msgTextView.setMovementMethod(LinkMovementMethod.getInstance());
				}
			} else {
				messageLayout.setVisibility(View.GONE);
			}

			//内容文字方向
			if (mMessageGravity != Gravity.NO_GRAVITY) {
				msgTextView.setGravity(mMessageGravity);
			}

			//设置按钮
			setupButton(mainLayout, dialog);

			//设置额外布局内容
			if (mContentView != null) {
				customLayout.addView(mContentView, new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
			} else {
				customLayout.setVisibility(View.GONE);
			}

			dialog.setContentView(mainLayout);
			dialog.setCancelable(mCancelable);
			dialog.setCanceledOnTouchOutside(mCancelable);
			return dialog;

		}

		/** 设置对话框按钮，有四种组合类型*/
		private void setupButton(ViewGroup mainLayout, final Dialog dialog) {
			View seperateHorizontal = mainLayout.findViewById(R.id.ua_udb_dialog_seperate_horizontal);
			View seperateVertical = mainLayout.findViewById(R.id.ua_udb_dialog_seperate_vertical);
			LinearLayout buttonLayout = (LinearLayout) mainLayout.findViewById(R.id.ua_udb_dialog_button_layout);

			BUTTON_TYPE btnType = getButtonType();

			View.OnClickListener listener = new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (v == mPositiveButton) {
						if (mPositiveBtnOnClickListener != null) {
							mPositiveBtnOnClickListener.onClick(dialog, BUTTON_POSITIVE);
						}
						dialog.dismiss();
					} else if (v == mNegativeButton) {
						if (mNegativeOnClickListener != null) {
							mNegativeOnClickListener.onClick(dialog, BUTTON_NEGATIVE);
						}
						dialog.dismiss();
					}
				}
			};

			switch (btnType) {
			case NONE: {
				buttonLayout.setVisibility(View.GONE);
				seperateHorizontal.setVisibility(View.GONE);
				break;
			}
			case POSITIVE: {
				buttonLayout.setVisibility(View.VISIBLE);
				seperateHorizontal.setVisibility(View.VISIBLE);
				seperateVertical.setVisibility(View.GONE);

				mNegativeButton.setVisibility(View.GONE);
				mPositiveButton.setVisibility(View.VISIBLE);
				mPositiveButton.setBackgroundResource(R.drawable.ua_selector_dialog_btn_left_right);
				mPositiveButton.setText(mPositiveCharSequence);
				mPositiveButton.setOnClickListener(listener);
				break;
			}
			case NEGATIVE: {
				buttonLayout.setVisibility(View.VISIBLE);
				seperateHorizontal.setVisibility(View.VISIBLE);
				seperateVertical.setVisibility(View.GONE);

				mPositiveButton.setVisibility(View.GONE);
				mNegativeButton.setVisibility(View.VISIBLE);
				mNegativeButton.setBackgroundResource(R.drawable.ua_selector_dialog_btn_left_right);
				mNegativeButton.setText(mNegativeCharSequence);
				mNegativeButton.setOnClickListener(listener);
				break;
			}
			case BOTH: {
				buttonLayout.setVisibility(View.VISIBLE);
				seperateHorizontal.setVisibility(View.VISIBLE);
				seperateVertical.setVisibility(View.VISIBLE);

				mPositiveButton.setVisibility(View.VISIBLE);
				mPositiveButton.setText(mPositiveCharSequence);
				mPositiveButton.setBackgroundResource(R.drawable.ua_selector_dialog_btn_right);
				mPositiveButton.setOnClickListener(listener);

				mNegativeButton.setVisibility(View.VISIBLE);
				mNegativeButton.setText(mNegativeCharSequence);
				mNegativeButton.setBackgroundResource(R.drawable.ua_selector_dialog_btn_left);
				mNegativeButton.setOnClickListener(listener);
				break;
			}
			}
		}

		/** 获取按钮组合类型*/
		private BUTTON_TYPE getButtonType() {
			boolean hasPosBtn = !TextUtils.isEmpty(mPositiveCharSequence);
			boolean hasNegBtn = !TextUtils.isEmpty(mNegativeCharSequence);
			if (hasPosBtn && hasNegBtn) {
				return BUTTON_TYPE.BOTH;
			} else if (hasPosBtn && !hasNegBtn) {
				return BUTTON_TYPE.POSITIVE;
			} else if (!hasPosBtn && hasNegBtn) {
				return BUTTON_TYPE.NEGATIVE;
			} else {
				return BUTTON_TYPE.NONE;
			}
		}
	}
}
