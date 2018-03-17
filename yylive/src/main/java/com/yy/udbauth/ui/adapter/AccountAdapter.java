package com.yy.udbauth.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yy.live.R;

import java.util.List;


/**
 * 概述：账号适配器
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年9月24日 上午10:24:18
 */
public class AccountAdapter extends BaseAdapter {
	Context mContext;
	List<String> mAccountInfos;
	LayoutInflater mLayoutInflater;
	OnDeleteAccountListener mOnDeleteAccountListener;

	/**
	 * @param context	上下文
	 * @param accounts	账号列表
	 * @param listener	当账号被用户点击删除时触发该回调
	 */
	public AccountAdapter(Context context, List<String> accounts, OnDeleteAccountListener listener) {
		this.mContext = context;
		this.mOnDeleteAccountListener = listener;
		this.mAccountInfos = accounts;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mAccountInfos == null ? 0 : mAccountInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mAccountInfos == null ? null : mAccountInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.ua_item_account, parent, false);
			holder = new ViewHolder();
			holder.btnDelete = (ImageButton) convertView.findViewById(R.id.ua_item_account_btn_delete);
			holder.txtName = (TextView) convertView.findViewById(R.id.ua_item_account_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String account = (String) getItem(position);
		if (account != null) {
			holder.txtName.setText(account);
			holder.btnDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mOnDeleteAccountListener != null)
						mOnDeleteAccountListener.onDelete(account);
				}
			});
		} else {
			holder.txtName.setText("");
			holder.btnDelete.setOnClickListener(null);
		}

		return convertView;
	}

	public interface OnDeleteAccountListener {
		public void onDelete(String account);
	}

	class ViewHolder {
		TextView txtName;
		ImageButton btnDelete;
	}
}
