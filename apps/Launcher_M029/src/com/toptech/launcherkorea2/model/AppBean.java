package com.toptech.launcherkorea2.model;

/**
 * 数据库实体类
 * @author calvin
 * @date 2013-1-10
 */
public class AppBean {
	
    private Integer aid;
    private String pkg;//包名
    
	public AppBean() {
		super();
	}
	
	public AppBean(String pkg) {
		super();
		this.pkg = pkg;
	}
	
	public AppBean(Integer aid, String pkg) {
		super();
		this.aid = aid;
		this.pkg = pkg;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	@Override
	public String toString() {
		return "AppBean [aid=" + aid + ", pkg=" + pkg + "]";
	}

}
