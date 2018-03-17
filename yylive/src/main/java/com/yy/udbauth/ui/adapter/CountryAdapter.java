package com.yy.udbauth.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.ui.tools.CountryHelper;
import com.yy.udbauth.ui.tools.CountryHelper.CountryInfo;

import java.util.List;

/**
 * 概述：国家列表适配器
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年4月6日 下午8:37:43
 */
public class CountryAdapter extends BaseAdapter implements SectionIndexer {

	private static final String INDEX = CountryHelper.INDEX;
	private static final String SEPERATE = CountryHelper.SEPERATE;

	private LayoutInflater mInflater;
	private List<CountryInfo> mInfos;
	private float mDensity;

	public CountryAdapter(Context context, List<CountryInfo> infos) {
		mInflater = LayoutInflater.from(context);
		mInfos = infos;
		mDensity = context.getResources().getDisplayMetrics().density;
	}

	@Override
	public int getCount() {
		return mInfos == null ? 0 : mInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mInfos == null ? null : mInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 判断某一行是否是分隔符栏*/
	public boolean isSeperateItem(int position) {
		return mInfos.get(position).name.contains(SEPERATE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//数据与View绑定
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ua_item_country, parent, false);
		}

		TextView tvname = (TextView) convertView.findViewById(R.id.ua_item_country_name);
		TextView tvnumber = (TextView) convertView.findViewById(R.id.ua_item_country_number);
		CountryInfo info = mInfos.get(position);

		if (!info.name.contains(SEPERATE)) {
			//不是分隔符栏
			int paddingLeftRight = (int) (mDensity * 15); //dip转px
			int height = (int) (mDensity * 45); //dip转px

			convertView.setPadding(paddingLeftRight, 0, paddingLeftRight, 0);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
			convertView.setBackgroundResource(R.drawable.ua_selector_list_item);

			tvname.setText(info.name);
			tvname.setTextColor(0xFF333333);
			tvnumber.setVisibility(View.VISIBLE);
			tvnumber.setText(info.number);

		} else {
			//分隔符栏
			int paddingLeftRight = (int) (mDensity * 17); //dip转px
			int height = (int) (mDensity * 30); //dip转px

			convertView.setPadding(paddingLeftRight, 0, paddingLeftRight, 0);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
			convertView.setBackgroundColor(0xFFf6f6f6);

			tvname.setText(info.name.replace(SEPERATE, ""));
			tvname.setTextColor(0xFF767676);
			tvnumber.setVisibility(View.GONE);

		}
		return convertView;
	}

	@Override
	public Object[] getSections() {
		//返回用于在列表右侧显示的索引列表
		String[] sections = new String[INDEX.length()];
		for (int i = 0; i < INDEX.length(); i++)
			sections[i] = String.valueOf(INDEX.charAt(i));
		return sections;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		//获取某个索引值所对应的列表的位置
		if (sectionIndex == 0) {
			//针对“#”索引值的特殊处理
			return 0;
		}

		//针对字母“A-Z”索引值的处理
		char section = ((String) getSections()[sectionIndex]).charAt(0);

		for (int i = getCount() - 1; i > 0; i--) {
			CountryInfo ci = mInfos.get(i);
			if (ci.name.contains(SEPERATE) && CountryHelper.matchIgnoreCase(section, ci.name.charAt(0))) {
				//返回分隔符栏的位置
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		//获取某个列表位置所对应的索引值
		return 0;
	}
}
