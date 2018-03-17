package com.yy.udbauth.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.yy.live.R;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.adapter.CountryAdapter;
import com.yy.udbauth.ui.tools.CountryHelper;
import com.yy.udbauth.ui.tools.CountryHelper.CountryInfo;
import com.yy.udbauth.ui.widget.IndexableListView;
import com.yy.udbauth.ui.widget.UdbEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 概述：国家区域码选择页面
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年4月12日 下午4:29:03
 */
public class CountrySelectFragment extends UdbAuthBaseFragment {

	/** 表示传递一个国家信息*/
	public static final String EXTRA_COUNTRY_INFO = "country_info";

	private View mMainView;
	private UdbEditText mEtKeyWord;
	private List<CountryInfo> mAllItems;
	private IndexableListView mListView;
	private CountryAdapter mCountryAdapter;

	private long mKeywordChangeTime = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_country_select;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mEtKeyWord = (UdbEditText) mMainView.findViewById(R.id.ua_fragment_country_select_et_keyword);
		mListView = (IndexableListView) mMainView.findViewById(R.id.ua_fragment_country_select_listview);
		mListView.setFastScrollEnabled(true);

		//设置标题
		setTitleBarText(R.string.ua_title_country_select);

		mEtKeyWord.bindCleanButton(R.id.ua_fragment_country_select_btn_clear_keyword);
		mEtKeyWord.addTextChangedListener(onKeyWordChangeListener);
		mEtKeyWord.setFocusable(false);//相当于隐藏键盘（普通隐藏键盘方法不可用）

		mEtKeyWord.postDelayed(new Runnable() {
			@Override
			public void run() {
				//相当于恢复编辑框的焦点模式
				mEtKeyWord.setFocusableInTouchMode(true);
				mEtKeyWord.setFocusable(true);
			}
		}, 1000);

		//异步加载国家区号列表
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (getActivity() != null) {
						mAllItems = CountryHelper.getAllCountryList(getActivity());
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								showAllItems();
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return mMainView;
	}

	/** 显示所有条目*/
	private void showAllItems() {
		mCountryAdapter = new CountryAdapter(getContext(), mAllItems);
		mListView.setAdapter(mCountryAdapter);
		mListView.setOnItemClickListener(onItemClickListener);
	}

	/** 显示过滤条目*/
	private void showFilterItems(String keyword) {
		if (mAllItems == null)
			return;

		if (TextUtils.isEmpty(keyword)) {
			showAllItems();
			return;
		}

		Locale locale = Locale.getDefault();
		List<CountryInfo> filterInfos = new ArrayList<CountryInfo>();
		int size = mAllItems.size();

		for (int i = 0; i < size; i++) {

			CountryInfo info = mAllItems.get(i);

			//名字，拼音，拼音首字母三个匹配
			if ((info.name != null && info.name.toLowerCase(locale).startsWith(keyword.toLowerCase(locale)))
					|| (info.pinyin != null && info.pinyin.toLowerCase(locale).startsWith(keyword.toLowerCase(locale)))
					|| (info.pinyinShouzimu != null && info.pinyinShouzimu.toLowerCase(locale).startsWith(
							keyword.toLowerCase(locale)))) {

				if (filterInfos.contains(info)) {
					filterInfos.remove(info);//去重复，主要是常用国家搞鬼（重写了equals()）
				}
				filterInfos.add(info);
			}
		}

		mCountryAdapter = new CountryAdapter(getContext(), filterInfos);
		mListView.setAdapter(mCountryAdapter);
		mListView.setOnItemClickListener(onItemClickListener);
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 条目被点击
			if (!mCountryAdapter.isSeperateItem(position) && getActivity() != null) {
				Intent intent = new Intent();
				intent.putExtra(EXTRA_COUNTRY_INFO, (CountryInfo) mCountryAdapter.getItem(position));
				getActivity().setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
	};

	TextWatcher onKeyWordChangeListener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

			if (System.currentTimeMillis() - mKeywordChangeTime > 200) {
				//200毫秒才进行更新操作，避免更新太快
				showFilterItems(s.toString());
				mKeywordChangeTime = System.currentTimeMillis();
				return;
			}
		}
	};
}
