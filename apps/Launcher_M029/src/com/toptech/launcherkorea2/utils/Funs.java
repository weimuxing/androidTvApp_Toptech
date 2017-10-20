package com.toptech.launcherkorea2.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.dock.ApplicationActivity;
import com.toptech.launcherkorea2.dock.PackageInformation;


/**
 * apk执行动作函数类
 * @author calvin
 * @date 2013-1-10
 */
public class Funs {
	public static final String[] defaultApkPackageName={
		//"com.android.vending",//"com.toptech.playstore",
	};
	
	/**
	 * 打开button快捷方式
	 */
	public static void startHoppin(Context context){
		PackageInformation pinfo = getTargetApk(context,"com.sktelecom.hoppin.tablet");
		if(pinfo == null){
			Toast.makeText(context, "com.sktelecom.hoppin.tablet doesn't exist", Toast.LENGTH_SHORT).show();
		}else{
			startTargetApk(context, pinfo);
		}
	}
	
	/**
	 * 打开搜索
	 */
	public static void startQuicksearch(Context context){
		PackageInformation pinfo = getTargetApk(context,"com.android.quicksearchbox");
		if(pinfo == null){
			Toast.makeText(context, "com.android.quicksearchbox doesn't exist", Toast.LENGTH_SHORT).show();
		}else{
			startTargetApk(context,pinfo);
		}
	}

	/**
	 * 打开设置
	 */
	public static void startSetting(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		ComponentName componentName = new ComponentName("com.android.settings",
				"com.android.settings.MainSettings");
		intent.setComponent(componentName);

		context.startActivity(intent);
	}
	/**
	 * Skype
	 */
	public static void startSkype(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		ComponentName componentName = new ComponentName("com.skype.rover",
				"com.skype.raider.Main");
		intent.setComponent(componentName);

		context.startActivity(intent);
	}

	/**
	 * 打开浏览器
	 */
	public static void startBrower(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.setComponent(new ComponentName("com.android.browser",
				"com.android.browser.BrowserActivity"));
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	/**
	 * 打开apk列表页面
	 */
	public static void startApplication(Context context) {
		Intent intent = new Intent(context, ApplicationActivity.class);
		context.startActivity(intent);
		//this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	/**
	 * 打开视频应用 -谷歌商店
	 */
	public static void startGoogleStore(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		ComponentName componentName = new ComponentName(
				"com.android.vending",
				"com.android.vending.AssetBrowserActivity");
		intent.setComponent(componentName);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	/**
	 * 打开视频应用 
	 */
	public static void startLocalVideo(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		ComponentName componentName = new ComponentName(
			"com.jrm.localmm",
			"com.jrm.localmm.ui.main.FileBrowserActivity");
		//add by hz 20170808 for mantis:0033984
		if(SystemProperties.getBoolean("mstar.cus.onida", false)){
			componentName = new ComponentName(
				"com.toptech.localmm",
				"com.toptech.localmm.ui.RootActivity");
		}
		//end 
		intent.setComponent(componentName);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	/**
	 * 打开音乐应用
	 * cmp=com.apk.good.ApkMainActivity
	 * cmp=aMao.apkManager/aMao.manager.doshboardActivity
        * cmp=com.cv.apk_manager/.MainActivity
	 */
	public static void startAppmanager(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		ComponentName componentName = new ComponentName("com.cv.apk_manager",
				"com.cv.apk_manager.MainActivity");
		intent.setComponent(componentName);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	/**
	 * 打开音乐应用
	 */
	public static void startDropboxBrowser(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		ComponentName componentName = new ComponentName("com.dropbox.android",
				"com.dropbox.android.activity.DropboxBrowser");
		intent.setComponent(componentName);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	/**
	 * 打开图片应用 com.google.android.youtube.googletv/.MainActivity
	 * com.skype.rover/com.skype.raider.Main
	 * com.android.vending/.AssetBrowserActivity
	 * cmp=com.dropbox.android/.activity.DropboxBrowser
	 */
	public static void startLocalPicture(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		ComponentName componentName = new ComponentName("com.google.android.youtube.googletv",
				"com.google.android.youtube.googletv.MainActivity");
		intent.setComponent(componentName);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	public static void startTV(Context context) {
		
	}
	/**
	 * 根据指定包名，获取相应的apk信息
	 * @param pkname 包名
	 * @return
	 */
	public static PackageInformation getTargetApk(Context context, String pkname){
		Intent intent = new Intent(Intent.ACTION_MAIN,null);
    	intent.addCategory(Intent.CATEGORY_LAUNCHER);
    	intent.setPackage(pkname);

		PackageManager pm = context.getPackageManager();
	    // 查询含有CATEGORY_LAUNCHER和ACTION_MAIN的apk,即应用的主activity
		List<ResolveInfo> resolve = pm.queryIntentActivities(intent,0);
		//Collections.sort(resolve, new ResolveInfo.DisplayNameComparator(pm));
		if(resolve == null || resolve.size() <=0) {
			return null;
		}
		PackageInformation apkInfor = new PackageInformation();
		//for(int i = 0; i < resolve.size(); i++){
			ResolveInfo res = resolve.get(0);
			String packageName = res.activityInfo.packageName;   
			if(packageName.compareTo(pkname) == 0){
				apkInfor.setAppName(res.loadLabel(pm).toString());
				apkInfor.setPackageName(packageName);
				apkInfor.setIcon(res.loadIcon(pm));
				apkInfor.setActivityName(res.activityInfo.name);
				return apkInfor;
			}
		//}
		return null;	
	}
	
	/**
	 * 跳转一个Activity
	 * @param pinfo apk信息
	 */
	public static void startTargetApk(Context context, PackageInformation pinfo){
		if(pinfo == null) return;
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		if(pinfo.getPackageName()==null) return;
		ComponentName componentName = new ComponentName(pinfo.getPackageName(),pinfo.getActivityName());
		intent.setComponent(componentName);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	/**
	 * 获取系统所有的apk信息
	 * @param context 上下文
	 * @return apk应用列表
	 */
	public static ArrayList<PackageInformation> getAllApks(Context context) {
		
		ArrayList<PackageInformation> list = new ArrayList<PackageInformation>();
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolve=null;
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		// 查询含有CATEGORY_LAUNCHER和ACTION_MAIN的apk,即应用的主activity
		 resolve = pm.queryIntentActivities(intent, 0);
		Collections.sort(resolve, new ResolveInfo.DisplayNameComparator(pm));

		for (int i = 0; i < resolve.size(); i++) {
			ResolveInfo res = resolve.get(i);
			String packageName = res.activityInfo.packageName; // 包名
			//Log.d("ApplicationActivity","packageName = " + packageName);
			if(packageName.compareTo("com.mstar.tv.tvplayer.ui") == 0){
				continue;
			}
			PackageInformation apkInfor = new PackageInformation();
			apkInfor.setAppName(res.loadLabel(pm).toString());
			apkInfor.setPackageName(packageName);
			//apkInfor.setVersionCode(res.activityInfo. versionCode);
			//apkInfor.setVersionName(res.activityInfo.versionName);
			apkInfor.setIcon(res.loadIcon(pm));
			apkInfor.setActivityName(res.activityInfo.name);
			list.add(apkInfor);
		}
		//search TV APP
		Intent intentTV = new Intent(Intent.ACTION_MAIN, null);
		intentTV.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

		List<ResolveInfo> resolveTV = pm.queryIntentActivities(intentTV, 0);
		Collections.sort(resolveTV, new ResolveInfo.DisplayNameComparator(pm));

		for (int i = 0; i < resolveTV.size(); i++) {
			ResolveInfo resTV = resolveTV.get(i);
			String packageNameTV = resTV.activityInfo.packageName; // 包名
			if (resolve.toString().contains(packageNameTV)) {
				//Log.d("ApplicationActivity","repeat packageName = " + packageNameTV);
				continue;
			}
			PackageInformation apkInforTV = new PackageInformation();
			apkInforTV.setAppName(resTV.loadLabel(pm).toString());
			apkInforTV.setPackageName(packageNameTV);
			apkInforTV.setIcon(resTV.loadIcon(pm));
			apkInforTV.setActivityName(resTV.activityInfo.name);
			list.add(apkInforTV);
		}
		return list;
	}
	
	
	//显示状态栏
	public static void displayStatusBar(Context context){
		Intent i = new Intent();
		i.setAction("android.intent.action.HIDE_STATUS_BAR");
		i.putExtra("hide_status_bar", true);
		context.sendBroadcast(i);
	}
	
	//隐藏状态栏
	public static void hideStatusBar(Context context){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.HIDE_STATUS_BAR");
		intent.putExtra("hide_status_bar", false);
		context.sendBroadcast(intent); 
	}

	public static ArrayList<PackageInformation> getDefaultApks(Context context) {
		
		ArrayList<PackageInformation> list = new ArrayList<PackageInformation>();
		
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		PackageManager pm = context.getPackageManager();
		// 查询含有CATEGORY_LAUNCHER和ACTION_MAIN的apk,即应用的主activity
		List<ResolveInfo> resolve = pm.queryIntentActivities(intent, 0);
		Collections.sort(resolve, new ResolveInfo.DisplayNameComparator(pm));

		for (int i = 0; i < resolve.size(); i++) {
			ResolveInfo res = resolve.get(i);

			String packageName = res.activityInfo.packageName; // 包名
			//Log.d("ApplicationActivity","packageName = " + packageName);
			/*
			if(res.activityInfo.name.compareTo("com.android.vending") == 0){
				continue;
			}
			*/
			for(int j=0;j<defaultApkPackageName.length;j++)
			{
				if(packageName.equals(defaultApkPackageName[j]))
				{
					PackageInformation apkInfor = new PackageInformation();
					apkInfor.setAppName(res.loadLabel(pm).toString());
					apkInfor.setPackageName(packageName);
					//apkInfor.setVersionCode(res.activityInfo. versionCode);
					//apkInfor.setVersionName(res.activityInfo.versionName);
					apkInfor.setIcon(res.loadIcon(pm));
					apkInfor.setActivityName(res.activityInfo.name);
					list.add(apkInfor);
				}
			}
		}
		return list;
	}
}
