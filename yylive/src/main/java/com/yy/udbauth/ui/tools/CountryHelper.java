package com.yy.udbauth.ui.tools;

import android.content.Context;
import android.util.Log;

import com.yy.live.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 概述：国家得力助手
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年4月13日 下午7:36:25
 */
public class CountryHelper {
	private static final String TAG = CountryHelper.class.getSimpleName();

	/** 显示在右边的快速定位索引符*/
	public static final String INDEX = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/** 分隔符*/
	public static final String SEPERATE = "-----123---321-----";

	/** 返回一个国家列表*/
	public static List<CountryInfo> getAllCountryList(Context context) throws IOException {

		if(context == null){
			return new ArrayList<CountryInfo>();
		}
		
		//读取文件列表 
		InputStream is = context.getResources().openRawResource(R.raw.country_new);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		List<CountryInfo> results = new ArrayList<CountryInfo>();
		String line = null;

		while ((line = br.readLine()) != null) {
			CountryInfo info = CountryInfo.parseString(line);
			if (info != null) {
				results.add(info);
			}
		}
		br.close();
		is.close();

		return results;
	}

	/** 判断两个字母是否相同，不区分大小写*/
	public static boolean matchIgnoreCase(char a, char b) {
		boolean match = a == b || a - (97 - 65) == b || a + (97 - 65) == b;
		return match;
	}

	/**
	 * 概述：国家区号信息
	 * 
	 * @version 1.0
	 * @author weitianpeng@yy.com
	 * @time 2016年4月7日 上午11:27:11
	 */
	public static class CountryInfo implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		/** 国家名称*/
		public String name = "";
		/** 国家英文名称*/
		public String englishName = "";
		/** 国家名称拼音*/
		public String pinyin = "";
		/** 国家名称拼音首字母*/
		public String pinyinShouzimu = "";
		/** 国家区号*/
		public String number = "";

		/**
		 * 按格式“名称\t英文名\t拼音\t拼音首字母\t区号” 构建一个对象
		 * @param content
		 * @return	失败则返回空
		 */
		public static CountryInfo parseString(String content) {
			if (content == null || content.length() <= 0) {
				Log.e(TAG, "获取国家区号出错：content为空");
				return null;
			}

			String[] fileds = content.split("\t");

			if (fileds == null || fileds.length <= 0) {
				Log.e(TAG, "获取国家区号出错：无字段");
				return null;
			}

			if (fileds[0].contains(SEPERATE)) {
				//这里是分隔符
				CountryInfo info = new CountryInfo();
				info.name = fileds[0];
				return info;
			}

			if (fileds.length >= 5) {
				CountryInfo info = new CountryInfo();
				//这里是正常的国家信息
				info.name = fileds[0];
				info.englishName = fileds[1];
				info.pinyin = fileds[2];
				info.pinyinShouzimu = fileds[3];
				info.number = fileds[4];
				return info;
			}

			Log.e(TAG, "获取国家区号出错：" + content);
			return null;
		}

		/**
		 * 按格式“名称\t英文名\t拼音\t拼音首字母\t区号”拼成字符串
		 */
		@Override
		public String toString() {
			return String.format("%s\t%s\t%s\t%s\t%s", name, englishName, pinyin, pinyinShouzimu, number);
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof CountryInfo))
				return false; //不同类型（包括null）

			String name = ((CountryInfo) object).name;
			if (this.name == null && name != null)
				return false;//当前name为空

			return this.name.equals(name);//字符串比较
		}
	}
}
