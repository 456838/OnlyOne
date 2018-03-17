package com.yy.udbauth.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * 概述：一个可以自由绑定【清空编辑框内容】按钮的编辑框
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月21日 下午5:46:57
 */
public class UdbEditText extends EditText {

	View mBindCleanButton;
	int mBindResId = Integer.MIN_VALUE;

	public UdbEditText(Context context) {
		super(context);
	}

	public UdbEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UdbEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		updateCleanButtonState();
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		if (mBindCleanButton != null) {
			mBindCleanButton.setVisibility(getText().toString().length() > 0 ? View.VISIBLE : View.INVISIBLE);
		}
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

	/**
	 * 绑定一个按钮，点击该按钮将会清空文本框的内容，同时使文本框获取焦点。<br/>
	 * 文本框内容（或焦点）发生变化时，按钮的可视化也跟着变化
	 */
	public void bindCleanButton(View v) {
		if (v != null) {
			mBindCleanButton = v;
			mBindResId = Integer.MIN_VALUE;
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setText("");
					requestFocus();
				}
			});

			updateCleanButtonState();
		}
	}

	/**
	 * 绑定一个按钮，点击该按钮将会清空文本框的内容，同时使文本框获取焦点。<br/>
	 * 文本框内容（或焦点）发生变化时，按钮的可视化也跟着变化
	 */
	public void bindCleanButton(int resId) {
		mBindResId = resId;
		mBindCleanButton = null;
		updateCleanButtonState();
	}

	/**
	 * 更新清空按钮的状态
	 */
	private void updateCleanButtonState() {
		if (mBindCleanButton == null && mBindResId == Integer.MIN_VALUE) {
			return;
		}

		if (mBindCleanButton == null && mBindResId != Integer.MIN_VALUE) {
			mBindCleanButton = getRootView().findViewById(mBindResId);

			if (mBindCleanButton != null) {
				mBindCleanButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setText("");
						requestFocus();
					}
				});
			} else {
				mBindResId = Integer.MIN_VALUE;
			}
		}

		if (mBindCleanButton != null) {
			mBindCleanButton.setVisibility((isFocused() && getText().toString().length() > 0) ? View.VISIBLE
					: View.INVISIBLE);
		}
	}
}
