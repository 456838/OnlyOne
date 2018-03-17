package com.duowan.liveshow.entity.contacts;

public class SortModel extends Contact {

	public SortToken sortToken = new SortToken();

	public SortModel(String name, String number, String sortKey) {
		super(name, number, sortKey);
	}

	@Override
	public String toString() {
		return "SortModel{" +
				"sortToken=" + sortToken +
				'}';
	}
}
