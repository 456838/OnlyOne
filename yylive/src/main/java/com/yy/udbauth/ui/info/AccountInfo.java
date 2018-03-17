package com.yy.udbauth.ui.info;

import java.io.Serializable;

public class AccountInfo implements Serializable {

	/** 序列号 */
	private static final long serialVersionUID = -6718992225749735884L;

	public String uid;
	public String yyid;
	public String passport;
	public String credit;
	public String yycookies;
	public String tooken;
	public String webtoken;
	public boolean used;
	//add by newsalton
	public String name ;
	public long loginTime;




}
