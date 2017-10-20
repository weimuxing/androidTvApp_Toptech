package com.toptech.launcherkorea2.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.dao.DaoService;
import com.toptech.launcherkorea2.dock.PackageInformation;
import com.toptech.launcherkorea2.model.AppBean;
import com.toptech.launcherkorea2.utils.Funs;
import com.toptech.launcherkorea2.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;

/**
 * �������
 * @author calvin
 *
 */
public class MainService extends Service implements Runnable{

	private final static String tag = "MainService";
	public static boolean isrun = false;
	private static ArrayList<Task> allTask = new ArrayList<Task>();
	private static ArrayList<BaseActivity> allActivity = new ArrayList<BaseActivity>();
	private DaoService dao;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//���activity��������
	public static void addActivity(BaseActivity ac) {
		allActivity.add(ac);
	}
	//�Ӽ������Ƴ�activity
	public static void removeActivity(BaseActivity ac) {
		allActivity.remove(ac);
	}

	public static BaseActivity getActivityByName(String aname) {
		for (BaseActivity ac : allActivity) {
			if (ac.getClass().getName().indexOf(aname) >= 0) {
				return ac;
			}

		}
		return null;
	}

	// �������
	public static void newTask(Task ts) {
		allTask.add(ts);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isrun = false;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		isrun = true;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (isrun) {
			try {
				if (allTask.size() > 0) {
					// ִ������
					doTask(allTask.get(0));
				} else {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				if (allTask.size() > 0)
					allTask.remove(allTask.get(0));
			}
		}
	}

	//ִ�����񷽷�
	private void doTask(Task ts) throws Exception{
		Message message = hand.obtainMessage();
		message.what = ts.getTaskID();
		switch (ts.getTaskID()) {
			case TaskType.FIND_APP_RECORD:
				dao = new DaoService(this);
				List<AppBean> list = dao.getAllData();
				List<PackageInformation> allApkList = new ArrayList<PackageInformation>();
				//���Ӧ���б�apkΪ�գ�������е�Ӧ�ö���ӽ�ȥ
				
				//lyp 2014.11.06
				boolean app_load_flag =  SystemProperties.getBoolean("persist.sys.launcher_loaded", false);
				Log.d("lyp","_________ app_load_flag="+app_load_flag);
				if(!app_load_flag/*list == null || list.size() <= 0*/){
					SystemProperties.set("persist.sys.launcher_loaded", "true");
					
					//allApkList = Funs.getAllApks(this);
					allApkList = Funs.getDefaultApks(this);
					//������app��Ϣ���浽��ݿ�
					for(PackageInformation info : allApkList){
						dao.save(new AppBean(info.getPackageName()));
					}
				}else{
					for(AppBean bean : list){
						PackageInformation p = Funs.getTargetApk(this, bean.getPkg());
						if(p != null){
							allApkList.add(p);
						}else{
							//����apk�Ѿ���ж�أ���ͬʱɾ����ݿ�ļ�¼
							dao.delete(bean.getAid());
						}
					}
				}
				message.obj = allApkList;
				break;
			case TaskType.DEL_APP_RECORD:
				String pkgname = (String) ts.getTaskParam().get("pkg");
				dao.delete(pkgname);
				List<AppBean> list2 = dao.getAllData();
				List<PackageInformation> allApkList2 = new ArrayList<PackageInformation>();
				for(AppBean bean : list2){
					PackageInformation p = Funs.getTargetApk(this, bean.getPkg());
					if(p != null){
						allApkList2.add(p);
					}else{
						//����apk�Ѿ���ж�أ���ͬʱɾ����ݿ�ļ�¼
						dao.delete(bean.getAid());
					}
				}
				message.obj = allApkList2;
				break;
			case TaskType.UPDATE_APP_RECORD:
				List<PackageInformation> apps = (ArrayList<PackageInformation>)ts.getTaskParam().get("data");
				System.out.println(tag +"------>"+apps.size());
				dao.deleteAll();
				for(PackageInformation p : apps){
					dao.save(new AppBean(p.getPackageName()));
				}
				message.obj = apps;
				break;
		/*	case TaskType.UPDATE_WEATHER:
				if(!Utils.isConnectInternet(this)){
					message.what = TaskType.NETWORK_ERROR;
				}else{
					HashMap<String,Object> map = ts.getTaskParam();
					RSSService rss = new RSSService();
					RSSFeed rssFeed = rss.getFeed("http://weather.yahooapis.com/forecastrss?w="+
							map.get("w")+"&u="+map.get("c"));
					Log.d(tag, rssFeed.toString());
					message.obj = rssFeed;
				}
				break;*/
		}
		allTask.remove(ts);
		// ֪ͨ���̸߳���UI
		hand.sendMessage(message);
	}
	
	private Handler hand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case TaskType.FIND_APP_RECORD:
				case TaskType.DEL_APP_RECORD:
				case TaskType.UPDATE_APP_RECORD:
					MainService.getActivityByName("Launcher").refresh(msg.what, msg.obj);
					break;
				case TaskType.UPDATE_WEATHER:
					if(msg.obj != null){
						MainService.getActivityByName("Launcher").refresh(msg.what, msg.obj);
					}else{
						MainService.getActivityByName("Launcher").refresh(TaskType.UPDATE_WEATHER_ERROR, msg.obj);
					}
					break;
				case TaskType.NETWORK_ERROR:
					MainService.getActivityByName("Launcher").refresh(msg.what, msg.obj);
					break;
			}
		}
	};

}
