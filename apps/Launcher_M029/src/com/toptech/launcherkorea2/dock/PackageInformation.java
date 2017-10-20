package com.toptech.launcherkorea2.dock;

import android.graphics.drawable.Drawable;

/** 
 * @author calvin
 * @date 2013-1-10
 */
public class PackageInformation {

	private String mAppName = null;

	private String mPackageName = null;

	private String mActivityName = null;

	private Drawable mIcon = null;

	private String mVersionName = null;

	private int mVersionCode = 0;

	public void setAppName(String name) {
		mAppName = name;
	}

	public void setPackageName(String packageName) {
		mPackageName = packageName;
	}

	public void setActivityName(String name) {
		mActivityName = name;
	}

	public void setIcon(Drawable drawable) {
		mIcon = drawable;
	}

	public void setVersionName(String name) {
		mVersionName = name;
	}

	public void setVersionCode(int version) {
		mVersionCode = version;
	}

	public String getAppName() {
		return mAppName;
	}

	public String getPackageName() {
		return mPackageName;
	}

	public String getActivityName() {
		return mActivityName;
	}

	public Drawable getIcon() {
		return mIcon;
	}

	public String getVersionName() {
		return mVersionName;
	}

	public int getVersionCode() {
		return mVersionCode;
	}
}