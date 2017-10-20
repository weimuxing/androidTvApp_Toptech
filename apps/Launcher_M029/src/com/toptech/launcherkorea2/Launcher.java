package com.toptech.launcherkorea2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser.NewPermissionInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.Enum3dType;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceInputType;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceType;
import com.mstar.android.tvapi.common.vo.EnumPipModes;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.VideoWindowType;

import com.toptech.launcherkorea2.adapter.ImageAdapter;
import com.toptech.launcherkorea2.dock.PackageInformation;
import com.toptech.launcherkorea2.logic.BaseActivity;
import com.toptech.launcherkorea2.logic.MainService;
import com.toptech.launcherkorea2.logic.Task;
import com.toptech.launcherkorea2.logic.TaskType;
import com.toptech.launcherkorea2.shortcut.AppGallery;
import com.toptech.launcherkorea2.shortcut.AppPopupDialog;
import com.toptech.launcherkorea2.shortcut.AppPopupWindow;
import com.toptech.launcherkorea2.shortcut.ShortCutAdapter;
import com.toptech.launcherkorea2.utils.FileUtil;
import com.toptech.launcherkorea2.utils.Funs;
import com.toptech.launcherkorea2.utils.Tools;
import com.toptech.launcherkorea2.utils.Utils;
import com.toptech.launcherkorea2.view.CoverFlow;
import com.mstar.android.MKeyEvent;
import android.net.wifi.WifiInfo;

@SuppressWarnings("deprecation")
public class Launcher extends BaseActivity implements View.OnClickListener {

	private final Context context = Launcher.this;

	// -----------------------------------涓昏彍鍗�------------------------------
	private ImageView cf_next;
	private ImageView cf_prev;
	private LinearLayout main_image;
	private Button main_media;
	private Button main_source;
	private ImageView  main_pic;

	private CoverFlow cf;
	private ImageAdapter imageAdapter;
	private final static int DEFAULT_FOCUS = (int) (Integer.MAX_VALUE / 2);
	public static Integer[] mImageIds;
	private String[] packages;
	private String[] activitys;
	private boolean hasSkype;
	private boolean hasYouTube;
	private int currpos = -1;// 涓昏彍鍗曞綋鍓嶉�変腑position
	private AppGallery appGallery = null;
	private ImageView add_app = null;
	private static int currItem = -1;
	private static int selectedPosition = -1;
	private ShortCutAdapter shortCutAdapter = null;

	private static List<PackageInformation> selectedApkList = null;// 褰撳墠閫変腑apk鍒楄〃闆嗗悎

	// 娣诲姞搴旂敤寮圭獥
	//private AppPopupWindow appPopup = null;
	private AppPopupDialog appPopup = null;
	//zb20141024 add
	private boolean isBootFinish=false;
	private final BroadcastReceiver mWallpaperReceiver = new WallpaperIntentReceiver();
    
	private  boolean isMain_image = false;
	private  boolean is_Main_source = false;
	private LinearLayout surfaceViewLayout;
	
	private int currentImg = 0;  
    private boolean threadrun = false;
	
	
	private static final int SHOW_NEXT_IMAGE = 5;
	private static final int DELETE_SDCARD_GALLERY = 4;
	private static final int ONE_SECOND = 1000;

	ArrayList<String> usbImagePathList = new ArrayList<String>();
	ArrayList<String> sdCardImagePathList = new ArrayList<String>();

	private File sdCardGallery = null;
	
	private Bitmap defaultBitmap;
	private static BitmapFactory.Options options;
	private FileInputStream mFileInputStream = null;
	private static final long UPPER_BOUND_PIX = 1280 * 8 * 720 * 8;
	private static final double UPPER_BOUND_WIDTH_PIX = 1280.0f;
	private static final double UPPER_BOUND_HEIGHT_PIX = 720.0f;
	private SynchronizedImage synchronizedImage;
	private Thread putImageThread;
	private Thread outImageThread;
	private boolean put_flag = false;
	private boolean get_flag = false;
	private LinkedList<String> imageList = new LinkedList<String>();
	private Toast mtoast=null;
	private String curShowPictureName="";
	private WifiManager mWifiManager;
	private int offertWidth = 3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE); //为啥报错
		setContentView(R.layout.main);
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LOW_PROFILE);
		
		try {
			str_m_wPanelWidth = TvManager.getInstance().getEnvironment("m_wPanelWidth");
			str_m_wPanelHeight = TvManager.getInstance().getEnvironment("m_wPanelHeight");
			} catch (TvCommonException e) {
			}
		// ----------------------------涓昏彍鍗昒I-------------------------
		cf_next = (ImageView) findViewById(R.id.cf_right);
		cf_next.setOnClickListener(this);
		cf_prev = (ImageView) findViewById(R.id.cf_left);
		cf_prev.setOnClickListener(this);
		//改变图标
		main_source= (Button) findViewById(R.id.main_source);
		Drawable[]  drawables = main_source.getCompoundDrawables();
		int size = getResources().getDimensionPixelSize(R.dimen.x40);
		drawables[1].setBounds(offertWidth, offertWidth, size+(3*offertWidth), size+(offertWidth*2));
		main_source.setCompoundDrawables(null, drawables[1], null, null);
		main_source.setPadding(0, 0, 5, 0);
		main_source.setOnClickListener(this);
		main_media = (Button) findViewById(R.id.main_media);
		Drawable[]  drawables2 = main_media.getCompoundDrawables();
		//add by hz 20170808 for mantis:0033984
		if(SystemProperties.getBoolean("mstar.cus.onida", false)){
			drawables2[1] = this.getResources().getDrawable(R.drawable.multimedia_icon_onida);
		}
		//end hz 
		int size2 = getResources().getDimensionPixelOffset(R.dimen.x40);
		drawables2[1].setBounds(offertWidth, offertWidth, size2+offertWidth*2, size2+offertWidth*2);
		main_media.setCompoundDrawables(null, drawables2[1], null, null);
		main_source.setOnClickListener(this);
		//end
		
		main_media.setOnClickListener(this);
		main_image = (LinearLayout) findViewById(R.id.main_image);
		//main_image.setOnClickListener(this);
		main_pic = (ImageView)findViewById(R.id.main_pic);
		
		cf = (CoverFlow) findViewById(R.id.coverflow);
		
		TextView mGallery_App_Text = (TextView) findViewById(R.id.gallery_app_text);
		
		packages = getResources().getStringArray(R.array.apppkgs);
		TypedArray cfnames = getAvailableAppName(packages);
		
		imageAdapter = new ImageAdapter(this, R.layout.coverflow_item,
				new String[] { "icon", "name" }, new int[] { R.id.cf_icon,
						R.id.cf_name }, mImageIds, cfnames,mGallery_App_Text);
		imageAdapter.setSelectItem(DEFAULT_FOCUS);

		cf.setAdapter(imageAdapter);
		cf.setFadingEdgeLength(5);
		cf.setAnimationDuration(5);//skye 201503
		// cf.setSpacing(10);
		if (currpos == -1) {
			cf.setSelection(DEFAULT_FOCUS, true);
			currpos = DEFAULT_FOCUS;
		} else {
			cf.setSelection(currpos, true);
		}
		cf.setOnItemClickListener(new CoverFlowOnItemClickListener());
		cf.setOnItemSelectedListener(new CoverFlowOnSelectLisener());
		cf.setOnTouchListener(new CoverFlowOnTouchLisener());
		initAppUI();
		MainService.newTask(new Task(TaskType.FIND_APP_RECORD, null));
		stopOtherMusic();

		mcontent = (RelativeLayout) findViewById(R.id.mainlayout);
		mcontent.setVisibility(View.INVISIBLE);
		updateWallpaperVisibility(false);
		new Thread(inputSourceThread).start();
		
		creatGalleryInSdcard();
		defaultBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.onida_advetisement_logo);
		if(sdCardGallery != null)
		synchronizedImage = new SynchronizedImage(5, sdCardGallery.toString());
		IntentFilter removedFilter=new IntentFilter();
		removedFilter.addAction("removed_package");
		this.registerReceiver(mRemovedBroadcast, removedFilter);
		

		IntentFilter usbFilter = new IntentFilter();
		usbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		usbFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		usbFilter.addDataScheme("file");
		this.registerReceiver(mUsb_Receiver, usbFilter);
		
	}
	
	private TypedArray getAvailableAppName(String[] pkgs){
		TypedArray array = null;
		
		try {
			hasSkype = getPackageManager().getPackageInfo(pkgs[5], 0) != null;
		} catch (NameNotFoundException e) {
			hasSkype = false;
			e.printStackTrace();
		}
		
		try {
			hasYouTube = getPackageManager().getPackageInfo(pkgs[6], 0) != null;
		} catch (NameNotFoundException e) {
			hasYouTube = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!hasSkype && !hasYouTube){
			array = getResources().obtainTypedArray(R.array.appnames_noytb_nosy);
			if(mImageIds != null){
				mImageIds = null;
			}
			mImageIds = new Integer[]{R.drawable.dropbox_icon,
					// R.drawable.app_store_icon,
					 R.drawable.app_library_icon,
					 R.drawable.app_manager_icon,
					 R.drawable.setting,
					// R.drawable.gallery_big,
					 R.drawable.browser
					// R.drawable.skype_img_1,
					// R.drawable.youtube
					 };
		} else if (!hasYouTube) {
			array = getResources().obtainTypedArray(R.array.appnames_noytb);
			if(mImageIds != null){
				mImageIds = null;
			}
			mImageIds = new Integer[]{R.drawable.dropbox_icon,
					// R.drawable.app_store_icon,
					 R.drawable.app_library_icon,
					 R.drawable.app_manager_icon,
					 R.drawable.setting,
					// R.drawable.gallery_big,
					 R.drawable.browser,
					 R.drawable.skype_img_1
					// R.drawable.youtube
					 };
		} else if (!hasSkype) {
			array = getResources().obtainTypedArray(R.array.appnames_nosy);
			if(mImageIds != null){
				mImageIds = null;
			}
			mImageIds = new Integer[]{R.drawable.dropbox_icon,
					// R.drawable.app_store_icon,
					 R.drawable.app_library_icon,
					 R.drawable.app_manager_icon,
					 R.drawable.setting,
					// R.drawable.gallery_big,
					 R.drawable.browser,
					// R.drawable.skype_img_1,
					 R.drawable.youtube};
		} else {
			array = getResources().obtainTypedArray(R.array.appnames);
			if(mImageIds != null){
				mImageIds = null;
			}
			mImageIds = new Integer[]{R.drawable.dropbox_icon,
					// R.drawable.app_store_icon,
					 R.drawable.app_library_icon,
					 R.drawable.app_manager_icon,
					 R.drawable.setting,
					// R.drawable.gallery_big,
					 R.drawable.browser,
					 R.drawable.skype_img_1,
					 R.drawable.youtube};
		}
		return array;
	}
	
	class SynchronizedImage {
		private int index = 0;
		private int size = 10;
		private String sdCardGalleryPath = "/mnt/sdcard/gallery";

		public SynchronizedImage(int size, String galleryPath) {
			Log.d(TAG, "The queue has been created!");
			this.size = size;
			this.sdCardGalleryPath = galleryPath;
		}

		/**
		 * ������
		 * 
		 * @param c
		 */
		public synchronized void push(String usbImagePath) {
			boolean isCopyFinished = false;
			if (imageList.size() == size) {
				Log.d(TAG, "The queue is full!");
				return;
			}
			String imagePath = usbImagePath.substring(
					usbImagePath.lastIndexOf(File.separator) + 1,
					usbImagePath.length());
			String sdCardPath = null;
			if (sdCardGalleryPath.endsWith(File.separator)) {
				sdCardPath = sdCardGalleryPath + imagePath;
			} else {
				sdCardPath = sdCardGalleryPath + File.separator + imagePath;
			}
			isCopyFinished = copyImageFromUsbToSDcard(usbImagePath);
			if (isCopyFinished) {
				imageList.add(sdCardPath);
				Log.d(TAG, "-------------imageList.size()=" + imageList.size());
				this.notify();		
			}

		}


		public synchronized Bitmap pop() {
			if (imageList.size() == 0) {
				Log.d("wangdy", "The queue is null !");
				return defaultBitmap;
			}
			String imagePath = imageList.get(0);
			String[] str=imagePath.split("/");
			curShowPictureName=str[str.length-1];
			Bitmap bitmap = decodeImage(imagePath);
			if (bitmap != null) {
				if (delFile(imagePath)) {
					imageList.remove(0);
					Log.d("wangdy", "-------------imageList.size()="
							+ imageList.size());
				}
			}
			this.notify(); 	
			return bitmap;
		}

		public synchronized void print() {
			for (int i = 0; i < imageList.size(); i++) {
				Log.d("wangdy", "-------------imageList.get(" + i + ")="
						+ imageList.get(i));
			}
			System.out.println();
			this.notify(); // ֪ͨ�����߳���ʾ��ջ����
		}

		public boolean copyImageFromUsbToSDcard(String usbPath) {
			Log.d(TAG,
					"copyImageFromUsbToSDcard-----image path in usb-------path:"
							+ usbPath);
			String sdCardPath = null;
			if (sdCardGalleryPath.toString().endsWith(File.separator)) {
				sdCardPath = sdCardGalleryPath.toString()
						+ usbPath.substring(
								usbPath.lastIndexOf(File.separator) + 1,
								usbPath.length());
			} else {
				sdCardPath = sdCardGalleryPath.toString()
						+ File.separator
						+ usbPath.substring(
								usbPath.lastIndexOf(File.separator) + 1,
								usbPath.length());
			}
			Log.d(TAG,
					"copyImageFromUsbToSDcard-------image path in sdCard------>imagePath:"
							+ sdCardPath);
			boolean copied = copy(usbPath, sdCardPath);
			Log.d(TAG, "copyImageFromUsbToSDcard->copy->copied:" + copied);
			return copied;
		}

		public boolean copy(String start, String end) {
			Log.d(TAG, "copyImageFromUsbToSDcard->copy()");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(new FileInputStream(new File(
						start)));
				bos = new BufferedOutputStream(new FileOutputStream(new File(
						end)));
				int val = -1;
				byte[] buffer = new byte[1024];
				while ((val = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, val);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) {
					try {
						bos.flush();
						bos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return false;
		}

	}

	class PutImageIntoSdcard implements Runnable {

		private SynchronizedImage synchronizedImage;

		// private ArrayList<String> imagePath = new ArrayList<String>();

		public PutImageIntoSdcard(SynchronizedImage synchronizedImage) {
			this.synchronizedImage = synchronizedImage;
			//Log.d(TAG, "Creat PutImageIntoSdcard thread!!!!!!!!!");
			// this.imagePath = imagePath;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//Log.d(TAG, "---------------------put_flag=" + put_flag);
			while (put_flag) {
				String path;
				if (usbImagePathList.size() > 0) {
					if (currentImg >= usbImagePathList.size()) {
						currentImg = 0;
					}
					path = usbImagePathList.get(currentImg++);
					//Log.d(TAG,"the PutImageIntoSdcard thread is running--PutImageIntoSdcard_path---------:" + path);
					synchronizedImage.push(path);
					try {
						Thread.sleep((int) (4 * ONE_SECOND));
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	class GetImageFromSdcard implements Runnable {

		private SynchronizedImage synchronizedImage;

		private Bitmap bitmap = null;

		public GetImageFromSdcard(SynchronizedImage synchronizedImage) {
			this.synchronizedImage = synchronizedImage;
			//Log.d(TAG, "Creat GetImageFromSdcard thread!!!!!!!!!");
		}

		@Override
		public void run() {
			//Log.d(TAG, "---------------------get_flag=" + get_flag);
			while (get_flag) {
				//Log.d(TAG, "the GetImageFromSdcard thread is running");
				bitmap = synchronizedImage.pop();
				Message msg = new Message();
				msg.what = SHOW_NEXT_IMAGE;
				msg.obj = bitmap;
				//Log.d(TAG,"the GetImageFromSdcard thread is running,the bitmap has get from sdcard!!!!");
				scaleWinHandler.sendMessage(msg);
				// main_pic.setImageBitmap(bitmap);
				try {
					// 姣忎骇鐢熶竴涓瓧绗︾嚎绋嬪氨鐫＄湢涓�涓�
					Thread.sleep((int) (5 * ONE_SECOND));
				} catch (InterruptedException e) {
				}
			}
		}

	}

	private static int picsky = 3;
	private Handler signaHandler = new Handler() {
		public void handleMessage(Message signalMessage) {
			switch (signalMessage.what) {
			case 0:

				break;
			case 1:

				break;
			case 3:
				main_pic.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(newPath)));

				//Log.d("yy", "*****wwwwwwwwwwwwwwwwww**********");
				break;
			default:
				break;
			}
		}

	};

		
	public void stopOtherMusic() {

		Intent intent_stop = new Intent();
		intent_stop.setAction("com.android.music.musicservicecommand.stop");
		sendBroadcast(intent_stop);
	}

	@Override
	public void init() {

	}
	
	@Override
	protected void onResume() {
		Log.d(TAG, " --------- Launcher -onResume---------curFocusLine-------"+curFocusLine);
		super.onResume();
		TypedArray names = getAvailableAppName(packages);
		imageAdapter.setImageIds(mImageIds);
		imageAdapter.setNames(names);
		imageAdapter.notifyDataSetChanged();
		stopOtherMusic();
		if (!issearch) {
			fileIsExists();
			if (put_flag && get_flag ) {
				main_pic.setBackgroundColor(Color.BLACK);
			}
		}


		initTipSFunction();
		register_sysUI();
		
		// 娉ㄥ唽瀹氭椂鏃ュ巻/澶╂皵鏇存柊骞挎挱
		/*IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(DateReceiver, filter, null, null);*/

		// zb20140521 add for tv
		Intent hide_intent = new Intent();
		hide_intent.setAction("android.intent.action.HIDE_STATUS_BAR");
		hide_intent.putExtra("hide_status_bar", false);
		sendBroadcast(hide_intent);

		/******************** for tv **********************/
			{
			//Log.i(TAG, "<<<<<<----------enter onResume---------->>>>>");
			super.onResume();
			SystemProperties.set("mstar.str.storage", "0");
			try {
				if (TvManager.getInstance() != null) {
					//Log.i(TAG,"<<<<<<----------TvManager.getInstance() != null ---------->>>>>hammm");
					EnumPipModes mode = TvManager.getInstance().getPipManager()
							.getPipMode();
					if (mode == EnumPipModes.E_PIP_MODE_PIP) {
						//Log.i(TAG,"<<<<<<----------mode == EnumPipModes.E_PIP_MODE_PIP ---------->>>>>");
						TvPipPopManager.getInstance().disablePip();
						TvPipPopManager.getInstance().setPipOnFlag(false);
					} else if (mode == EnumPipModes.E_PIP_MODE_POP) {
						//Log.i(TAG,"<<<<<<----------mode == EnumPipModes.E_PIP_MODE_POP ---------->>>>>");
						Enum3dType formatType = Enum3dType.EN_3D_TYPE_NUM;
						try {
							formatType = TvManager.getInstance()
									.getThreeDimensionManager()
									.getCurrent3dFormat();
							//Log.i(TAG,"<<<<<<----------formatType ---------->>>>>");
						} catch (TvCommonException e) {
							e.printStackTrace();
				}

						if (formatType == Enum3dType.EN_3D_DUALVIEW) {
							//Log.i(TAG,"<<<<<<----------formatType == Enum3dType.EN_3D_DUALVIEW ---------->>>>>");
							TvPipPopManager.getInstance().disable3dDualView();
						} else {
							//Log.i(TAG,"<<<<<<----------formatType != Enum3dType.EN_3D_DUALVIEW ---------->>>>>");
							TvPipPopManager.getInstance().disablePop();
						}
					}
				}
			} catch (TvCommonException e) {
				e.printStackTrace();
			}
			if (isPowerOn() == true) {
				Log.i(TAG, "<<<<<<----------PowerOn == true ---------->>>>>");
				if(SystemProperties.getBoolean("persist.sys.aging.Running", false)){
					ComponentName componentName = new ComponentName(
							"com.toptech.factorytools",
							"com.toptech.factorytools.AgingActivity");
					Intent intent = new Intent();
					intent.setComponent(componentName);
					Launcher.this.startActivity(intent);
				} 
				else {
				ComponentName componentName = new ComponentName(
						"com.mstar.tv.tvplayer.ui",
						"com.mstar.tv.tvplayer.ui.RootActivity");
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setComponent(componentName);
				intent.putExtra("isPowerOn", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				Launcher.this.startActivity(intent);
				}
		} else {
				/*if(SystemProperties.getBoolean("persist.sys.aging.Running", false)){
					ComponentName componentName = new ComponentName(
							"com.toptech.factorytools",
							"com.toptech.factorytools.AgingActivity");
					Intent intent = new Intent();
					intent.setComponent(componentName);
					Launcher.this.startActivity(intent);
				} */
				
			 try {
		        	if("1".equals(TvManager.getInstance().getEnvironment("env_aging_Running"))){
		    			//Log.d("sky","--------persist.sys.aging.running----20150302-----");
		    				ComponentName componentName = new ComponentName(
		    						"com.toptech.factorytools",
		    						"com.toptech.factorytools.AgingActivity");
		    				Intent intent = new Intent();
		    				intent.setComponent(componentName);
		    				startActivity(intent);
		    		 }
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			 	isBootFinish=false;
				new Thread(new Runnable() {
					@Override
					public void run() {
						while(!isBootFinish)
						{
							if("stopped".equals(SystemProperties.get("init.svc.bootanim", "0")))
							{
								isBootFinish=true;
								if(SystemProperties.getBoolean("persist.sys.isfirstttpw", false))
							   	{
							   		//Log.d("tvos","--------unmuteAfterPowerOn---------");
//									SystemProperties.set("persist.sys.isfirstttpw","false");
									TvCommonManager.getInstance().setTvosCommonCommand("unmuteAfterPowerOn");
							   	}
							}
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			
			/**
				 * "0": Normal flow (Not in STR flow or STR is not in Suspending
				 * or Resuming) "1": STR is Suspending "2": STR is Resuming
				 * (Before launcher does the 2nd onPause) if
				 * (STR_STATUS_NONE.equals(SystemProperties.get(
				 * "mstar.str.suspending", "0"))) {
				 */
				//Log.i(TAG,"<<<<<<----------mstar.str.suspending == STR_STATUS_NONE---------->>>>>");
				Intent it = new Intent("com.biaoqi.stb.launcherk.onresume");
					sendBroadcast(it);
						if (surfaceView == null) {
					//Log.i(TAG,"<<<<<<---------- surfaceView == null---------->>>>>");
							mcontent.setVisibility(View.VISIBLE);
							updateWallpaperVisibility(true);
							handlertv.postDelayed(handlerRuntv, 300);
					BackHomeSource();
				} else {
					updateWallpaperVisibilityForResetWallPaper(true);
					BackHomeSource();
					
					if (STR_STATUS_NONE.equals(SystemProperties.get(
							"mstar.str.suspending", "0"))) {
						Message tmpMsg = new Message();
						tmpMsg.what = SCALE_SMALL;
						scaleWinHandler.sendMessageDelayed(tmpMsg, 10);
					}
				}
				//Log.i(TAG,"Launcher (Home Key):  ========== Display Launcher =========="); // add

			}
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LOW_PROFILE);
			//Log.i(TAG, "<<<<<<----------end onResume---------->>>>>");
		}
		if (curFocusLine==1) {
			imageTV.clearFocus();
			curFocusLine = 1;
			cf.requestFocus();
			cf.setSelection(currpos);
			imageAdapter.setCurFocus(currpos);
			imageAdapter.notifyDataSetChanged();
			
			//Log.i(TAG, "******************cf.isFocused()***********************");
		} else if(curFocusLine==2){
			updateApp();//skye 201503
			//Log.i(TAG, "******************2B.isFocused()***********************");
		}else if(curFocusLine==4){
			if(is_Main_source){
				main_source.requestFocus();
			}else{
			    main_media.requestFocus();
			}
		}
		else {
			//Log.i(TAG, "******************imageTV.isFocused()***********************");
			cf.clearFocus();
			imageAdapter.setCurFocus(-1);
			curFocusLine = 0;
			if(isMain_image){
				main_image.requestFocus();
			}else{
			    imageTV.requestFocus();
			}
			imageAdapter.notifyDataSetChanged();
		}	
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		currpos = savedInstanceState.getInt("currpos");
		currItem = savedInstanceState.getInt("currItem");
		selectedPosition = savedInstanceState.getInt("selectedPosition");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("currpos", currpos);
		outState.putInt("currItem", currItem);
		outState.putInt("selectedPosition", selectedPosition);
		super.onSaveInstanceState(outState);
	}
	
	
	
	 private void registerIntentReceivers() {
	        IntentFilter filter = new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED);
	        registerReceiver(mWallpaperReceiver, filter);
	    }
	 
	 private void unregisterIntentReceivers(){
		 unregisterReceiver(mWallpaperReceiver);
	 }

	    private class WallpaperIntentReceiver extends BroadcastReceiver {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            updateWallpaperVisibility(true);
	        }
	    }
	
	   /* private void startWallpaper() {
	        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
	        startActivity(Intent.createChooser(pickWallpaper, getString(R.string.wallpaper)));
	    }*/

		void updateWallpaperVisibility(boolean visible) {
			int wpflags = visible ? WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER
					: 0;
			int curflags = getWindow().getAttributes().flags
					& WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
			if (wpflags != curflags) {
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
				WallpaperManager.getInstance(this).suggestDesiredDimensions(
						metrics.widthPixels, metrics.heightPixels);
				getWindow().setFlags(wpflags,
						WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
			}
		}

		void updateWallpaperVisibilityForResetWallPaper(boolean visible) {
			int wpflags = visible ? WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER
					: 0;
			int curflags = getWindow().getAttributes().flags
					& WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER;
			if (wpflags != curflags) {
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
				WallpaperManager.getInstance(this).suggestDesiredDimensions(
						metrics.widthPixels, metrics.heightPixels);
				getWindow().setFlags(wpflags,
						WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
			}
		}
	
	/*
		private void setDefaultWallpaper() {
			if (!mWallpaperChecked) {
				Drawable wallpaper = peekWallpaper();
				if (wallpaper == null) {
					try {
						clearWallpaper();
					} catch (IOException e) {
						Log.e(TAG, "Failed to clear wallpaper " + e);
					}
				} else {
					getWindow().setBackgroundDrawable(
							new ClippedDrawable(wallpaper));
				}
				mWallpaperChecked = true;
			}
		}*/
	
	/*	private class ClippedDrawable extends Drawable {

	        private final Drawable mWallpaper;

	        public ClippedDrawable(Drawable wallpaper) {
	            mWallpaper = wallpaper;
	        }

			@Override
			public void setBounds(int left, int top, int right, int bottom) {
				super.setBounds(left, top, right, bottom);
				mWallpaper.setBounds(left, top,
						left + mWallpaper.getIntrinsicWidth(),
						top + mWallpaper.getIntrinsicHeight());
			}

	        @Override
	        public void draw(Canvas canvas) {
	            mWallpaper.draw(canvas);
	        }

	        @Override
	        public void setAlpha(int alpha) {
	            mWallpaper.setAlpha(alpha);
	        }

	        @Override
	        public void setColorFilter(ColorFilter cf) {
	            mWallpaper.setColorFilter(cf);
	        }

	        @Override
	        public int getOpacity() {
	            return mWallpaper.getOpacity();
	        }
	    }*/
	

	@Override
	protected void onPause() {
		super.onPause();
		//Log.i(TAG, "---Launcher-------onPause--==--------");
		//unregisterReceiver(DateReceiver);

		
		//Log.i(TAG, "----------onPause----------");
				handlertv.removeCallbacks(pip_thread);
		super.onPause();
		destoryTipSFunction();
		// delay2LaunchAppWhenSupernovaNotOkHandler.removeMessages(SET_WIN_SCALE);
					if (surfaceView != null) {
						if (createsurface) {
				// surfaceView.setVisibility(View.INVISIBLE);
						}
						if (bSystemShutdown == false) {

				if (STR_STATUS_NONE.equals(SystemProperties.get(
						"mstar.str.suspending", "0"))) {
				}
			} else {
						bSystemShutdown = false;
					}

				} else {
					handlertv.removeCallbacks(handlerRuntv);
				}
	}

	// 涓昏彍鍗曠偣鍑讳簨浠剁洃鍚櫒
	private class CoverFlowOnItemClickListener implements
			android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			currpos = position;
			currItem = -1 ;
	        shortCutAdapter.setSelectItem(currItem);
	        curFocusLine = 1 ;
			switch (position % mImageIds.length) {
			case 0:// Dropbox
				changeInputSource("change to app");
				startCoverFlowApp("com.dropbox.android");
				break;
//			case 1://  appstore
//				changeInputSource("change to app");
//				startCoverFlowApp("com.android.vending");
//				break;
			case 1:// applibrary
				changeInputSource("change to app");
				Funs.startApplication(context);
				break;
			case 2:// app manager
				changeInputSource("change to app");
				startCoverFlowApp("com.cv.apk_manager");
				break;
			case 3://  setting
				changeInputSource("change to app");
				startCoverFlowApp("com.android.tv.settings");
				break;
			case 4:// Browser
				changeInputSource("change to app");
				//Funs.startBrower(context);
				startCoverFlowApp("com.android.browser");
				break;
			default:
				if (!hasSkype) {
					switch (position % mImageIds.length) {
					case 5:  // youtube
						changeInputSource("change to app");
						startCoverFlowApp("com.google.android.youtube.tv");
						break;
					}
				} else {
					switch (position % mImageIds.length) {
					case 5://  skype
						changeInputSource("change to app");
						startCoverFlowApp("com.skype.rover");
					 	break;
					case 6: // youtube
						changeInputSource("change to app");
						startCoverFlowApp("com.google.android.youtube.tv");
						break;
					}
				}
				break;

			}
		}

	}

	private void startCoverFlowApp(String packageName){
		PackageManager packageManager = Launcher.this.getPackageManager(); 
		Intent intent=new Intent(); 
		    intent =packageManager.getLaunchIntentForPackage(packageName);
		    try {
		    	if(intent!=null)
		    	startActivity(intent); 
			} catch (Exception e) {
				// TODO: handle exception
				Log.d(TAG, "startCoverFlowApp intent="+intent);
				mtoast = Toast.makeText(Launcher.this, "You have to install the Application first ,tks",
						Toast.LENGTH_SHORT);
				if (mtoast==null) mtoast.show(); else mtoast.cancel();
			}
		
		((Activity)context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	private class CoverFlowOnTouchLisener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			 scaleWinHandler.removeMessages(IMAGEADAPTER_REFRESH);
			 scaleWinHandler.sendEmptyMessageDelayed(IMAGEADAPTER_REFRESH, 350);
			return false;
		}
		
	}
	
	// 涓昏彍鍗曠偣鍑讳簨浠堕�夋嫨鐩戝惉鍣�
	private class CoverFlowOnSelectLisener implements
			android.widget.AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			currpos = position;
			scaleWinHandler.removeMessages(IMAGEADAPTER_REFRESH);
			scaleWinHandler.sendEmptyMessageDelayed(IMAGEADAPTER_REFRESH, 350);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}
	
	protected void inputSource() {
	Intent intent666 = new Intent(
			"com.mstar.tvsetting.switchinputsource.intent.action.PictrueChangeActivity");
	intent666.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	Launcher.this.startActivity(intent666);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//Log.d(TAG, "onKeyDown:" + selectedApkList);
    	Log.d(TAG, "onKeyDown:" + keyCode);
		// appGallery.requestFocus();
		isMain_image = false ;
		
		switch (keyCode) {
		
		
		case KeyEvent.KEYCODE_0:
			 WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			 if (!manager.isWifiEnabled()) {
				manager.setWifiEnabled(true);
			}
			break;
		
		case KeyEvent.KEYCODE_TV_INPUT:
			inputSource();
			break;
		case KeyEvent.KEYCODE_MENU:
			if (add_app.isFocused()) {
				currItem = -1;
			}
		     break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (curFocusLine == 0) {
				if (imageTV.isFocused()) {
					curFocusLine = 1;
					cf.requestFocus();
					cf.setSelection(currpos);
					imageAdapter.setCurFocus(currpos);
					imageAdapter.notifyDataSetChanged();
					return true;
				} 
				if (main_image.isFocused()) {
					imageTV.requestFocus();
				}
				
				
			} else if (curFocusLine == 1) {
				currpos -= 1;
				if (currpos < 0)
					currpos = DEFAULT_FOCUS;
				cf.setSelection(currpos);
			} else if (curFocusLine == 2) {
				if (currItem > 0) {
					currItem -= 1;// 閫変腑鍥炬爣鍚庨��涓�涓�
					if (currItem < 0) {
						currItem = 0;

					}
					preFocus = SHORTCUT_APP;
					shortCutAdapter.setSelectItem(currItem);
					if (currItem < appGallery.getFirstVisiblePosition()) {
						appGallery.setSelection(appGallery
								.getSelectedItemPosition() - 1);
					}
					return true;
				}
				if (currItem == 0) {
					if (shortCutAdapter.getSelectedItem() == 0
							&& !add_app.isFocused()) {
						shortCutAdapter.setSelectItem(-1);
						//Log.d(TAG, "add app focus");
						add_app.requestFocus();
						preFocus = ADD_APP;
						return true;
					}

					if (add_app.isFocused()) {
						//Log.d(TAG, "all app focus");
						//all_app_show.requestFocus();
						//preFocus = ALL_APP_SHOW;
						return true;
					}

				}
				if (currItem == -1&&add_app.isFocused()) {
					//Log.d(TAG, "all app focus-------------------");
					//all_app_show.requestFocus();
					//preFocus = ALL_APP_SHOW;
					return true;
				}
			}else if (curFocusLine == 4) {
				//Log.d(TAG, "*****line=4*****keydown left***");
				if (main_source.isFocused()) {
					main_media.requestFocus();
				} else {
					main_source.requestFocus();
				}
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			//Log.d(TAG, "***KEYCODE_DPAD_RIGHT***"+curFocusLine);
			if (curFocusLine == 4) {//curFocusLine == 0
				if (main_source.isFocused()) {
					main_media.requestFocus();
					return true;
				} else {
					curFocusLine = 1;
					cf.requestFocus();
					cf.setSelection(currpos);
					imageAdapter.setCurFocus(currpos);
					imageAdapter.notifyDataSetChanged();
					return true;
				}
				
			} else if (curFocusLine == 1) {
				currpos += 1;
				cf.setSelection(currpos);
			} else if (curFocusLine == 2) {
				if (selectedApkList.size()!=0) {//skye 20141125
					
				if (add_app.isFocused()) {
					add_app.setFocusable(false);
					currItem = 0;
					shortCutAdapter.setSelectItem(currItem);
					add_app.setFocusable(true);
					preFocus = SHORTCUT_APP;
					return true;
				}
				currItem += 1;// 閫変腑apk鍓嶈繘涓�涓�
				if (currItem >= selectedApkList.size()) {
					currItem = selectedApkList.size() - 1;
				}

				shortCutAdapter.setSelectItem(currItem);
				if (currItem > appGallery.getLastVisiblePosition()) {
					appGallery.setSelection(appGallery
							.getSelectedItemPosition() + 1);
				}
			  }
			}else if (curFocusLine == 0) {//skye 201503
				if (imageTV.isFocused()) {
					main_image.requestFocus();
					//Log.d(TAG, "****curFocusLine == 0*********main_image.requestFocus()");
				} else {
					imageTV.requestFocus();
				}
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if (curFocusLine == 0) {//curFocusLine == 2
				imageAdapter.setCurFocus(currpos);
				curFocusLine = 1;
				currItem = -2;// lib 20140703
				//Log.d(TAG, "line===2");
//				if (shortCutAdapter.getSelectedItem() != -1) {
//					shortCutAdapter.setSelectItem(-1);
//				}
				cf.requestFocus();
				cf.setSelection(currpos);
				imageAdapter.notifyDataSetChanged();
				//shortCutAdapter.notifyDataSetChanged();
				return true;
			}
			if (curFocusLine == 2) {//curFocusLine == 1
				if (shortCutAdapter.getSelectedItem() != -1) {
					shortCutAdapter.setSelectItem(-1);
				}
				shortCutAdapter.notifyDataSetChanged();
				//imageAdapter.setCurFocus(-1);
				//imageAdapter.notifyDataSetChanged();
				curFocusLine = 0;
				imageTV.requestFocus();
				//Log.d(TAG, "line ====0");

				return true;
			}
			if (curFocusLine == 1) {
				curFocusLine =4;
				imageAdapter.setCurFocus(-1);
				imageAdapter.notifyDataSetChanged();
				
				main_source.requestFocus();
				
				//Log.d(TAG, "line ====11=====44444");
				return true;
			}
			if (curFocusLine == 4) {
				//Log.d(TAG, "line ====444444444");
				return true;
			}

			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (curFocusLine == 4) {//curFocusLine == 0
				curFocusLine = 1;
				cf.requestFocus();
				cf.setSelection(currpos);
				imageAdapter.setCurFocus(currpos);
				imageAdapter.notifyDataSetChanged();
				return true;
			}
			if (curFocusLine == 0) {//curFocusLine == 1
				//Log.d(TAG, "prefocus===" + preFocus);
				//imageAdapter.setCurFocus(-1);
				//imageAdapter.notifyDataSetChanged();
				curFocusLine = 2;
				// if(preFocus==ALL_APP_SHOW) //lib 20140703
				// all_app_show.requestFocus();
				// else if(preFocus==ADD_APP)
				// add_app.requestFocus();
				// else if(preFocus==SHORTCUT_APP)
				// {
				// shortCutAdapter.setSelectItem(currItem);
				// }

				add_app.requestFocus();// lib 20141122
				currItem=0;
				shortCutAdapter.notifyDataSetChanged();
				return true;
			}
			if (curFocusLine == 1) {
				curFocusLine =0;
				imageAdapter.setCurFocus(-1);
				imageAdapter.notifyDataSetChanged();
				imageTV.requestFocus();
				//Log.d(TAG, "line===down----11-----00000000000");
				
			}

			break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
            //Log.d("fujia", "==urFocusLine ===KEYCODE_ENTER="+curFocusLine);

			if (curFocusLine == 0) {

				return onClickItem();
			}
			if (curFocusLine == 1) {

				return changeToAPP();
			}
			if (curFocusLine ==4 ) {
                //Log.d("fujia", "==urFocusLine ==4=KEYCODE_ENTER=");
				return onClickItem();//skye 201503
			}
			if (curFocusLine == 2) {
				if (preFocus == SHORTCUT_APP) {
					if(selectedApkList.get(currItem).getAppName()==null)
					return true;
					//zb20141105 add
					changeInputSource("change to app");
					//end
					Funs.startTargetApk(Launcher.this,
							selectedApkList.get(currItem));
					return true;
				} else
					return super.onKeyDown(keyCode, event);
			}
			
			 case KeyEvent.KEYCODE_VOLUME_UP:
			 case KeyEvent.KEYCODE_VOLUME_DOWN:
			 case KeyEvent.KEYCODE_VOLUME_MUTE:
			/* case MKeyEvent.KEYCODE_F_FREQUENC:
			// case MKeyEvent.KEYCODE_F_AGIN_MODE:
			 case MKeyEvent.KEYCODE_F_RESET:
                     case MKeyEvent.KEYCODE_F_VERSION:
                     case MKeyEvent.KEYCODE_F_ETHERNET:
                     case  MKeyEvent.KEYCODE_F_WIFI:*/
			return super.onKeyDown(keyCode, event);
		}

		return true;
	}

	@Override
	public void onClick(View v) {
        currItem = -1 ;
        if(selectedApkList == null)
            Log.d(TAG,"-----------selectedApkList:null--------------");
        shortCutAdapter.setSelectItem(currItem);
        isMain_image = false ;
		switch (v.getId()) {
		case R.id.cf_left: // 搴曠洏鍚戝乏
			currpos -= 1;
			if (currpos < 0)
				currpos = DEFAULT_FOCUS;
			cf.setSelection(currpos);
			curFocusLine = 1 ;
			break;
		case R.id.cf_right:// 搴曠洏鍚戝彸
			currpos += 1;
			cf.setSelection(currpos);
			curFocusLine = 1 ;
			break;
		case R.id.add_app: // 鎵撳紑娣诲姞搴旂敤绐楀彛
			// 澶嶄綅閫変腑浣嶇疆
			currItem = -1;
			selectedPosition = -1;

			if (appPopup != null) {
				appPopup = null;
			}
			/*appPopup = new AppPopupWindow(this, findViewById(R.id.mainlayout),
					selectedApkList);*/
			appPopup = new AppPopupDialog(this, selectedApkList);
			if (!appPopup.isShowing()) {
				appPopup.show();
			}

			// openShortcutAppPopup();
			Funs.hideStatusBar(this);// 闅愯棌鐘舵�佹爮
			curFocusLine = 2 ;
			break;
		case R.id.imagetv:
			//Log.d(TAG, "222222222222222222222-----iamge tv");
			{
				//Log.d(TAG, "Start execute TV APK ~ ");
				surfaceView.setBackgroundColor(Color.BLACK);
				changeInputSource("com.mstar.tv.tvplayer.ui");
			ComponentName componentName = new ComponentName(
						"com.mstar.tv.tvplayer.ui",
						"com.mstar.tv.tvplayer.ui.RootActivity");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(componentName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				Launcher.this.startActivity(intent);
				//Log.d(TAG, "End execute TV APK ~ ");
			}
			imageTV.setFocusable(true);
			imageTV.requestFocus();
			curFocusLine = 0 ;
			break;
		case R.id.main_source:
			is_Main_source =  true;
			inputSource();
			curFocusLine = 4 ;
			break;
		case R.id.main_media:
			is_Main_source =  false;
			Funs.startLocalVideo(context);
			curFocusLine = 4 ;
			break;
		
		case R.id.main_image:
			break;
			/*boolean bRet = false;
		    String file;
		    File[] files = new File("/mnt/usb/").listFiles();

		    for (int i = 0; i < files.length; i++) {
			file = files[i].getAbsolutePath();
			Log.d("lxk", "#9.1<#>  file = " + file);
			if (file.contains("/mnt/usb/sd") && files[i].exists()&&files[i].canRead()) {
				bRet = true;
				break;
			}
                    bRet = false;
		    }
		    Log.d("lxk", "GalleryImgClickListener on click ");
            if(bRet == false||imageList.size()==0){
            	Toast.makeText(getApplicationContext(),getResources().getString(R.string.gallery_info), Toast.LENGTH_SHORT).show();
                return;
            }
   		 	changeInputSource("com.android.settings");
   		 	ComponentName componentName = new ComponentName("com.jrm.localmm","com.jrm.localmm.ui.photo.ImagePlayerActivity");
   	   		Intent intent = new Intent(Intent.ACTION_MAIN);
   	   		//intent.addCategory(Intent.CATEGORY_LAUNCHER);
   	   		intent.putExtra("curPicName", curShowPictureName);
   	   		Log.d("lxk", "curShowPictureName:   "+curShowPictureName);
   	   		intent.putExtra("fromLauncher", 1);
   	   		intent.setComponent(componentName);
   	   		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
   	   				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
   	   		startActivity(intent);
   			break;
   			*/
		}

	}

	// 鍒濆鍖栧揩鎹锋柟寮忓簲鐢║I
	private void initAppUI() {
		add_app = (ImageView) findViewById(R.id.add_app);
		add_app.setOnClickListener(this);
		appGallery = (AppGallery) findViewById(R.id.show_app);
		//appGallery.setGravity(Gravity.LEFT);
		appGallery.setSpacing(getResources().getDimensionPixelSize(R.dimen.x17));
		
		imageTV = (Button) findViewById(R.id.imagetv);
		imageTV.setOnClickListener(this);

		appGallery
				.setOnItemClickListener(new AppGallery.IOnItemClickListener() {

					@Override
					public void onItemClick(int position) {
						appGallery.requestFocus();
						curFocusLine = 2;
						currItem = position;
						shortCutAdapter.setSelectItem(currItem);
						if(selectedApkList.get(currItem).getAppName()==null)
						return;
						//zb20141105 add
						changeInputSource("change to app");
						//end
						Funs.startTargetApk(Launcher.this,
								selectedApkList.get(position));
					}

				});

/*		appGallery
				.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						currItem = position;
						shortCutAdapter.setSelectItem(currItem);
						ImageView iv_image = (ImageView) view;
						Drawable drawable = iv_image.getDrawable();
						BitmapDrawable bd = (BitmapDrawable) drawable;
						Bitmap bm = bd.getBitmap();
						Bitmap bg = Utils.GeneratorBitmap(context, bm);
						iv_image.setImageBitmap(bg);

						delShortcutDailog(currItem);
						Funs.hideStatusBar(Launcher.this);
						return false;
					}
				});*/

	}

	// 鍒犻櫎蹇嵎鏂瑰紡搴旂敤绐楀彛鎻愮ず
	public void delShortcutDailog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.tips);
		builder.setMessage(R.string.delmsg);
		// 纭畾鎸夐挳
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 澶嶄綅閫変腑浣嶇疆
						currItem = -1;
						selectedPosition = -1;

						PackageInformation p = selectedApkList.get(position);
						String pkg = p.getPackageName();
						// 鏇存柊蹇嵎鏂瑰紡搴旂敤
						/*
						 * selectedApkList.remove(position);
						 * shortCutAdapter.notifyDataSetChanged();
						 */
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("pkg", pkg);
						MainService.newTask(new Task(TaskType.DEL_APP_RECORD,
								params));
					}
				});
		// 鍙栨秷鎸夐挳
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						shortCutAdapter.notifyDataSetChanged();
					}
				});
		builder.create().show();
	}

	// 鏇存柊蹇嵎鏂瑰紡apk
	private void updateApp() {
		//Log.d(TAG,"---updateApp----");

		if (shortCutAdapter == null) {
			shortCutAdapter = new ShortCutAdapter(this, R.layout.shortcut_app,
					new String[] { "icon" }, new int[] { R.id.app_img },
					selectedApkList);

			appGallery.setAdapter(shortCutAdapter);
			appGallery.setFadingEdgeLength(5);
			
		} else {
			shortCutAdapter.setOnDataChanged(selectedApkList);
		}

		// 璁剧疆褰撳墠閫変腑榛勬item
		/*if (currItem == -1) {
			if (preFocus == SHORTCUT_APP)
				currItem = selectedApkList.size() / 2;
		}*/

		shortCutAdapter.setSelectItem(currItem);

		// 璁剧疆褰撳墠涓棿浣嶇疆item
		/*if (selectedPosition == -1) {
			if (preFocus == SHORTCUT_APP)
				selectedPosition = selectedApkList.size() / 2;
		}*/

		/*if (preFocus == SHORTCUT_APP)
			appGallery.setSelection(selectedPosition);
		else
			appGallery.setSelection(4);*/
		appGallery.setSelection(5);
		//Log.d(TAG, "updateApp()+++++++++++++++++++++++++"+selectedPosition+"==="+currItem);
		appGallery.setAnimationDuration(1000);
		// shortCutAdapter.notifyDataSetChanged();
	}

	/**
	 * UI鍒锋柊
	 */
	@Override
	public void refresh(Object... param) {
		//Log.d(TAG, "--------------refresh()+++++++++++++++++++++++++");
		int type = Integer.parseInt(param[0].toString());
		switch (type) {
		/*case TaskType.UPDATE_WEATHER:// 鏇存柊澶╂皵棰勬姤
			RSSFeed rssFeed = (RSSFeed) param[1];
			weatherWidget.refreshWeahterUI(rssFeed, TaskType.UPDATE_WEATHER);
			break;
		case TaskType.UPDATE_WEATHER_ERROR:
			weatherWidget.refreshWeahterUI(null, TaskType.UPDATE_WEATHER_ERROR);
			break;
		case TaskType.NETWORK_ERROR:
			weatherWidget.refreshWeahterUI(null, TaskType.NETWORK_ERROR);
			break;*/
		case TaskType.FIND_APP_RECORD:
		case TaskType.DEL_APP_RECORD:
		case TaskType.UPDATE_APP_RECORD:
			selectedApkList = (List<PackageInformation>) param[1];
		       //Log.d(TAG, "-----------selectedApkList:" + selectedApkList + " is null:" + (selectedApkList == null));
			updateApp();
			break;

		}
	}

	/**
	 * 鏃ュ巻/澶╂皵瀹氭椂鏇存柊骞挎挱
	 
	private BroadcastReceiver DateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// Log.d(TAG, "鏃ユ湡鍙樺寲鐩戝惉....	");
			handler.sendEmptyMessage(0);
		}
	};*/

	/**
	 * 澶勭悊鏃ュ巻/澶╂皵鏇存柊
	 
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// Log.d(TAG, "鎺ユ敹鍒板箍鎾紝杩涜鏃ュ巻/澶╂皵鏇存柊");
			// 鏇存柊鏃ュ巻
			dateWidget.updateDate();
			// 鏇存柊澶╂皵棰勬姤
			weatherWidget.updateWeather();
		}
	};*/



	private boolean onClickItem() {
		switch (getCurrentFocus().getId()) {
		case R.id.imagetv:
			//Log.d(TAG, "Start execute TV APK ~ ");
			surfaceView.setBackgroundColor(Color.BLACK);
			changeInputSource("com.mstar.tv.tvplayer.ui");
			ComponentName componentName = new ComponentName(
					"com.mstar.tv.tvplayer.ui",
					"com.mstar.tv.tvplayer.ui.RootActivity");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(componentName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			Launcher.this.startActivity(intent);
			break;
		case R.id.main_source:
			is_Main_source =  true ;
			inputSource();
			//Log.d(TAG, "**************main_source ~ ");
			break;
		case R.id.main_media:
			is_Main_source =  false ;
			Funs.startLocalVideo(context);
			//Log.d(TAG, "**************main_media ~ ");
			break;
		
		case R.id.main_image:
		      break;
			/*isMain_image = true;
			Log.d(TAG, "**************main_media ~ ");
			String url = "http://www.onida.com"; // web address
			Intent intent123 = new Intent(Intent.ACTION_VIEW);
			intent123.setData(Uri.parse(url));
			startActivity(intent123);
		break;*/
	}
		return true;
	}

	private boolean changeToAPP() {
		switch (currpos % mImageIds.length) {
			case 0:// Dropbox
				changeInputSource("change to app");
                startCoverFlowApp("com.dropbox.android");
				break;
//			case 1://  appstore
//				changeInputSource("change to app");
//              startCoverFlowApp("com.android.vending");
//				break;
			case 1:// applibrary
				changeInputSource("change to app");
				Funs.startApplication(context);
				break;
			case 2:// app manager
				changeInputSource("change to app");
				startCoverFlowApp("com.cv.apk_manager");
				break;
			case 3://  setting
				changeInputSource("change to app");
				//Funs.startSetting(context);
				startCoverFlowApp("com.android.tv.settings");
				break;
			case 4:// Browser
				changeInputSource("change to app");
				startCoverFlowApp("com.android.browser");
				break;
			case 5://  skype
				changeInputSource("change to app");
				startCoverFlowApp("com.skype.rover");
			 break;
			case 6: // youtube
				changeInputSource("change to app");
				startCoverFlowApp("com.google.android.youtube.tv");
				break;

		}

		return true;
	}

	@Override
	protected void onStop() {
		{
			Log.i(TAG, "----------onStop----------");
			if (surfaceView != null
					&& surfaceView.getVisibility() == View.VISIBLE) {
			    surfaceView.setVisibility(View.INVISIBLE);
				surfaceView.setBackgroundColor(Color.BLACK);
				Log.i(TAG, "----------onStop-----callback-----");
			}
		super.onStop();
		}

	}
	@Override
	protected void onDestroy() {
		//Log.i(TAG, "----------onDestroy----------");
		unregisterReceiver(mUsb_Receiver);
		unregisterReceiver(systenUIrecReceiver);
		
		if (surfaceView != null) {
			surfaceView.getHolder().removeCallback(callback);
			surfaceView = null;
		}
		put_flag = false;
		get_flag = false;
		//registerIntentReceivers();
		super.onDestroy();
		bExitThread = true;
		this.unregisterReceiver(mRemovedBroadcast);
	}




	/********************************** tv **************************************/
	/************************************************************************/
	private Button imageTV;
	public  int curFocusLine = 0; // lib 20140703 2-1
	private static final int ALL_APP_SHOW = 0;
	private static final int ADD_APP = 1;
	private static final int SHORTCUT_APP = 2;
	private static int preFocus = ALL_APP_SHOW;

	public static float tmp_x = 0;
	public static float tmp_y = 0;
	public static float tmp_width = 0;
	public static float tmp_height = 0;
	public static String str_m_wPanelWidth = null ;
	public static String str_m_wPanelHeight = null;
	private static final String TAG = "Launcher";
	private static final String STR_STATUS_NONE = "0";
	private static final String STR_STATUS_SUSPENDING = "1";
	final float W_1920 = 1920f;
	final float H_1080 = 1080f;
	LayoutInflater mInflater;
	RelativeLayout mcontent;
	public static Context msContext;
	private int currentpageidx = 1;
	private static final int PAGE2 = 1;
	private Boolean bExitThread = false;
	private Boolean bSystemShutdown = false;
	private SurfaceView surfaceView = null;
	android.view.SurfaceHolder.Callback callback;
	private boolean createsurface = false;
	private int toChangeInputSource = TvCommonManager.INPUT_SOURCE_NONE;
	private InputSourceThread inputSourceThread = new InputSourceThread();
	private Boolean bSync = true;
	private static final int SCALE_NONE = 0;
	private static final int SCALE_FULL = 1;
	private static final int SCALE_SMALL = 2;
	private static final int IMAGEADAPTER_REFRESH = 3;
	private int fullScale = SCALE_NONE;
	private final String IS_POWER_ON_PROPERTY = "mstar.launcher.1stinit";

	private int dimens2px(int id) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (getResources().getInteger(id) * scale + 0.5f);

	}

	private Handler handlertv = new Handler();
	private Runnable handlerRuntv = new Runnable() {

		@Override
		public void run() {
			try {
				 surfaceViewLayout = (LinearLayout) Launcher.this
						.findViewById(R.id.tv_surfaceview_layout);
				surfaceViewLayout.removeAllViews();
				surfaceView = new SurfaceView(Launcher.this);
				surfaceView.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.FILL_PARENT));
				surfaceViewLayout.addView(surfaceView);
				surfaceView.setBackgroundResource(R.drawable.onida_bg);
				//Log.d(TAG, "openSurfaceView");
				openSurfaceView();
				setPipscale();
				if (surfaceView != null)
					surfaceView.setBackgroundColor(Color.TRANSPARENT);
				if (surfaceView.getVisibility() == View.VISIBLE
						&& currentpageidx != PAGE2) {
					surfaceView.setVisibility(View.INVISIBLE);
				} else if (currentpageidx == PAGE2) {
					surfaceView.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			handlertv.removeCallbacks(handlerRuntv);
		}
	};

	private Runnable pip_thread = new Runnable() {
		@Override
		public void run() {
			try {
				if (surfaceView.getVisibility() != View.VISIBLE
						&& currentpageidx == PAGE2) {
					surfaceView.setVisibility(View.VISIBLE);
				}
				//Log.i(TAG, "..PipThread..");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private void openSurfaceView() {
		final SurfaceHolder mSurfaceHolder = surfaceView.getHolder();

		callback = new android.view.SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				//Log.v(TAG, "===surfaceDestroyed===");
	}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (holder == null || holder.getSurface() == null
							|| holder.getSurface().isValid() == false)
						return;
					//Log.v(TAG, "===surfaceCreated===");
					if (TvManager.getInstance() != null) {
						TvManager.getInstance().getPlayerManager()
								.setDisplay(mSurfaceHolder);
	}

				} catch (TvCommonException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				//Log.v(TAG, "===surfaceChanged===");
				createsurface = true;
			}
		};

		mSurfaceHolder.addCallback(callback);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	private void setFullscale() {
		try {
			VideoWindowType videoWindowType = new VideoWindowType();
			videoWindowType.height = 0xffff;
			videoWindowType.width = 0xffff;
			videoWindowType.x = 0xffff;
			videoWindowType.y = 0xffff;
			if (TvManager.getInstance() != null) {
				TvManager.getInstance().getPictureManager()
						.selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
				TvManager.getInstance().getPictureManager()
						.setDisplayWindow(videoWindowType);
				TvManager.getInstance().getPictureManager().scaleWindow();
			}
		} catch (TvCommonException e) {
			e.printStackTrace();
		}
		//Log.i(TAG, "setFullscale");
	}
	
	 /**
     * 获取相对坐标
     * @param view
     * @return
     */
    public  AbsLocal getAbsLocal(View view) {
        int num[] = new int[2];
        AbsLocal absLocal =  new AbsLocal();
        view.getLocationOnScreen(num);
        absLocal.x = num[0];
        absLocal.y = num[1];
        return absLocal;
    }
    
    class AbsLocal{
    	 int x;
    	 int y;
		public AbsLocal() {
			super();
		}
		@Override
		public String toString() {
			return "AbsLocal [x=" + x + ", y=" + y + "]";
		}
    	
		
    }
	
	private void setPipscale() {
		//Log.i(TAG, "setPipscale");
		int[] iArr = new int[2];
		try {
			if (STR_STATUS_SUSPENDING.equals(SystemProperties.get(
					"mstar.lvds-off", "1"))) {
				//Log.i(TAG, "power off!!!");
			} else {
			VideoWindowType videoWindowType = new VideoWindowType();
			surfaceViewLayout.getLocationOnScreen(iArr);
				{
					//Log.d(TAG,"str_m_wPanelWidth = 1920 = "+str_m_wPanelWidth);
					if (true)//(str_m_wPanelWidth.equals("1920")&&str_m_wPanelHeight.equals("1080"))
					{
						AbsLocal absLocal = getAbsLocal(surfaceView);
						if (surfaceView.getWidth() == 0 && surfaceView.getHeight() == 0) {
							
							int l = videoWindowType.x = absLocal.x;
							int t = videoWindowType.y = absLocal.y;
							int r = videoWindowType.width = getResources().getDimensionPixelSize(R.dimen.x400);
							int b = videoWindowType.height = getResources().getDimensionPixelSize(R.dimen.y270);
						}else{
							int l = videoWindowType.x = absLocal.x;
							int t = videoWindowType.y = absLocal.y;
							int r = videoWindowType.width = surfaceView.getWidth();
							int b = videoWindowType.height = surfaceView.getHeight();
						}
						
					//	Log.d("zsr", "l,t,width,height: " + l+" "+t+" "+r+" "+b+" "+surfaceView.getMeasuredWidth()+" "+getResources().getDimensionPixelSize(R.dimen.x400));
					}else {
						//Log.d(TAG,"str_m_wPanelWidth = else");
						tmp_x = Integer.parseInt(str_m_wPanelWidth)*155/1920;
						tmp_y = Integer.parseInt(str_m_wPanelHeight)*403/1080;
						tmp_width = Integer.parseInt(str_m_wPanelWidth)*718/1920;
						tmp_height = Integer.parseInt(str_m_wPanelHeight)*444/1080;
						videoWindowType.x = (int)tmp_x;
						videoWindowType.y = (int)tmp_y;
						videoWindowType.width = (int)tmp_width;
						videoWindowType.height = (int)tmp_height;	
					}
					
				}
				if (TvManager.getInstance() != null) {
					if (TvManager.getInstance().getPictureManager() != null) {
						TvManager.getInstance().getPictureManager()
								.selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
					}
				}
				if (TvManager.getInstance() != null) {
					if (TvManager.getInstance().getPictureManager() != null) {
						//Log.i(TAG, " videoWindowType x : " + videoWindowType.x
						//		+ "   y : " + videoWindowType.y + "       w : "
						//		+ videoWindowType.width + "       h : "
						//		+ videoWindowType.height);
						TvManager.getInstance().getPictureManager()
								.setDisplayWindow(videoWindowType);
					}
				}
				if (TvManager.getInstance() != null) {
					if (TvManager.getInstance().getPictureManager() != null) {
						TvManager.getInstance().getPictureManager()
								.scaleWindow();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Log.i(TAG, "***************>>>>>>>>>>>>>setPipscale");
		}
	
	
	
	 

	
	public void changeInputSource(String packName) {
		//Log.i(TAG, "changeInputSource------------" + packName);
		if (packName != null) {
			if (packName.contentEquals("com.mstar.tv.tvplayer.ui")
					|| packName.contentEquals("mstar.factorymenu.ui")
					|| packName.contentEquals("com.tvos.pip")
					|| packName.contentEquals("com.mstar.tvsetting.hotkey")
					|| packName.contentEquals("com.babao.tvju")
					|| packName.contentEquals("com.babao.socialtv")
					|| packName.contentEquals("com.toptech.factorytools")					
					|| packName.contentEquals("com.mstar.appdemo")) {
				//Log.i(TAG, "------------TV AP");
				synchronized (bSync) {
					fullScale = SCALE_FULL;
				}
			} else {
				synchronized (bSync) {
					if (STR_STATUS_SUSPENDING.equals(SystemProperties.get(
							"mstar.str.suspending", "0"))) {
						SystemProperties.set("mstar.str.storage", "1");
					}
					toChangeInputSource = TvCommonManager.INPUT_SOURCE_STORAGE;
	}
			}
		}
	}
	public void BackHomeSource() {
		synchronized (bSync) {
			if (this.getEnablePipFlagIndex() == 0) {
				toChangeInputSource = TvCommonManager.INPUT_SOURCE_ATV;
			} else {
				toChangeInputSource = TvCommonManager.INPUT_SOURCE_STORAGE;
		}
		}
	}
	@SuppressWarnings("deprecation")
	private int getEnablePipFlagIndex() {
		SharedPreferences pre = getSharedPreferences("temp_sms",
				MODE_WORLD_READABLE);
		String index = pre.getString("sms_content", "");
		//Log.d("check", "   --> : getEnablePipFlagIndex : " + index);
		return isParserNumber(index);
	}
	@SuppressWarnings("deprecation")
	private void setEnablePipFlagIndex(int enablePipFlagIndex) {
		//Log.d(TAG, "   --> : setEnablePipFlagIndex : " + enablePipFlagIndex);
		SharedPreferences.Editor editor = getSharedPreferences("temp_sms",
				MODE_WORLD_WRITEABLE).edit();
		editor.putString("sms_content", String.valueOf(enablePipFlagIndex));
		editor.commit();
	}
	private int isParserNumber(String input) {
		try {
			return Integer.parseInt(input);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	/**
	 * Implementation of the method from SysShutDownReceiver.Callbacks.
	 */
	public void setSysShutdown() {
		//Log.v(TAG, "------------setSysShutdown=");
		bSystemShutdown = true;
	}
	private Handler scaleWinHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 琛ㄦ槑娑堟伅鏄绋嬪簭鍙戝嚭鐨�  
            if (msg.what == 0x110) {  
                // 灞曠ず涓嬩竴寮犲浘鐗�  
                dispalyNextImage();  
            }
			switch (msg.what) {
			case SCALE_FULL:
				synchronized (bSync) {
					fullScale = SCALE_FULL;
				}
				break;
			case SCALE_SMALL:
				synchronized (bSync) {
					fullScale = SCALE_SMALL;
				}
				handlertv.postDelayed(pip_thread, 500);
				surfaceView.setBackgroundColor(Color.TRANSPARENT);
				break;
			case IMAGEADAPTER_REFRESH:
				imageAdapter.setCurFocus(currpos);
				imageAdapter.setSelectItem(currpos); // 褰撴粦鍔ㄦ椂锛屼簨浠跺搷搴旓紝璋冪敤閫傞厤鍣ㄤ腑鐨勮繖涓柟娉曘��
				break;
			case SHOW_NEXT_IMAGE:
				// 灞曠ず涓嬩竴寮犲浘鐗�
				// dispalyNextImage();
				// BitmapDrawable bitmapDrawable = (BitmapDrawable) main_pic
				// .getDrawable();
				//
				// // 濡傛灉鍥剧墖杩樻湭鍥炴敹锛屽厛寮哄埗鍥炴敹璇ュ浘鐗�
				// if (bitmapDrawable != null
				// && !bitmapDrawable.getBitmap().isRecycled()) {
				// Log.d(TAG, "recycle bitmap");
				// bitmapDrawable.getBitmap().recycle();
				// }
				Bitmap bitmap = (Bitmap) msg.obj;
				if (bitmap != null && !bitmap.isRecycled()) {
					main_pic.setImageBitmap(bitmap);
				}
				Log.d(TAG, "reset bitmap on main_pic");
				break;
			case DELETE_SDCARD_GALLERY:
				//Log.d(TAG, "delete all image in sdcard!!!!!");
				delAllFile(sdCardGallery.toString());
				usbImagePathList.clear();
				imageList.clear();
				// sdCardImagePathList.clear();
				break;

			}
		}
	};
	public boolean isPowerOn() {
	
             /*
		//zb20141024 add	
		if(STR_STATUS_NONE.equals(SystemProperties.get("persist.sys.firstpwron", "0")))
		{
			SystemProperties.set("persist.sys.firstpwron", "1");
			//if(SystemProperties.get("persist.sys.pwr_to_android", "false").equals("true"))
			//{
			//	return false;
			//}
			//else {
			//	return true;
			//}
			return true;
		}
		else //if(STR_STATUS_SUSPENDING.equals(SystemProperties.get("mstar.str.suspending", "0")))
		{
			return false;
		}
		
		//end
		*/
		//Log.d(TAG,"Is Fist Power On: "+ (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY,false)));
		if (!SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)) {
			SystemProperties.set(IS_POWER_ON_PROPERTY, "true");
			return true;
		} else {
			return false;
		}
	}

	class InputSourceThread implements Runnable {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			int tmpInputSource;
			int tmpScale;
			while (!bExitThread) {
				synchronized (bSync) {
					tmpInputSource = toChangeInputSource;
					toChangeInputSource = TvCommonManager.INPUT_SOURCE_NONE;
					tmpScale = fullScale;
					fullScale = SCALE_NONE;
				}
				if ((tmpInputSource != TvCommonManager.INPUT_SOURCE_NONE)) {
					//Log.v(TAG, "\n  change source=====" + tmpInputSource);
					int currentSource;
					if (tmpInputSource == TvCommonManager.INPUT_SOURCE_ATV
							|| (Tools.isBox() && tmpInputSource == TvCommonManager.INPUT_SOURCE_DTV)) {
						currentSource = TvCommonManager.getInstance().getCurrentTvInputSource();
						//Log.v(TAG, "currentSource=====" + currentSource);
						if (currentSource==TvCommonManager.INPUT_SOURCE_STORAGE) {
							int curSource = TvCommonManager.getInstance()
									.getPowerOnSource().ordinal();
							//Log.v(TAG, "minidatabase=====" + curSource);
							if ((curSource >= 0)
									&& (curSource <= EnumInputSource.E_INPUT_SOURCE_NONE
											.ordinal())) {
								//Log.v(TAG, "setInputSource====="+ EnumInputSource.values()[curSource]);
								TvCommonManager.getInstance().setInputSource(curSource);
								if (curSource == TvCommonManager.INPUT_SOURCE_ATV) {
									int channel = TvChannelManager
											.getInstance()
											.getCurrentChannelNumber();
									if ((channel < 0) || (channel > 255)) {
										channel = 0;
			}
									TvChannelManager.getInstance().selectProgram(channel,
											TvChannelManager.SERVICE_TYPE_ATV);
								} else if (curSource == TvCommonManager.INPUT_SOURCE_DTV) {
									TvChannelManager.getInstance().selectProgram(TvChannelManager.getInstance().getCurrentChannelNumber(),
											TvChannelManager.SERVICE_TYPE_DTV);
		}
	}
						}
				//zb20141107 add
						else {
							//Log.d(TAG,"currentSource======"+currentSource);
							TvCommonManager.getInstance().setInputSource(
									currentSource);
						}
				//end
					} else {
						TvCommonManager.getInstance().setInputSource(
								tmpInputSource);
					}
				}
				if (tmpScale == SCALE_SMALL) {
					setPipscale();
				} else if (tmpScale == SCALE_FULL) {
					setFullscale();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/********************************/
	private ImageView tips_USB = null;
    private ImageView tips_Wifi = null;
    private ImageView tips_Eth = null;
    private final static int CHECK_DELAY_TIME = 4000;

    private Handler mUSB_Net_Handler = new Handler();
    
    private void initTipSFunction(){
    	tips_USB = (ImageView)findViewById(R.id.tips_usb);
    	tips_Wifi = (ImageView)findViewById(R.id.tips_wifi);
    	tips_Eth = (ImageView)findViewById(R.id.tips_eth);
    	
    	getNetStatus();
    	getUSBStatus();
    	register_NET_Action();
    	register_Pic_Action();
    }
    
    private void destoryTipSFunction(){
    	unRegister_NET_Action();
    	unregister_Pic_Action();
    }

	private void getNetStatus() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

		// msg.arg1鍖哄垎鏈夋棤缃戠粶锛�0->鏃犵綉缁滐紝1->鏈夌綉缁� ; arg2鍖哄垎鏄疻IFI杩樻槸ETH锛�1->wifi,2->ETH
		
		if (netInfo == null) {
			//Log.d(TAG, "getNetStatus ->no net connected");
			tips_Eth.setImageResource(R.drawable.tips_eth_dark);
			tips_Wifi.setImageResource(R.drawable.tips_wifi_dark);
		} else {
		
			//Log.d(TAG, "getNetStatus ==>current net  : " + netInfo.getTypeName());// ETH
			if (netInfo.getTypeName().equals("WIFI")) {
				mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
				 WifiInfo currentConnection = mWifiManager.getConnectionInfo();
				 int signalLevel = WifiManager.calculateSignalLevel(
	                                currentConnection.getRssi(), 4);
				Log.d("chenl","signalLevel===="+signalLevel);
				Log.d("chenl","Rssi：    "+currentConnection.getRssi());
				int resourceId=R.drawable.tips_eth_dark;
				 switch (signalLevel) {
	                case 0:
	                    resourceId = R.drawable.tips_wifi_1;
	                    break;
	                case 1:
	                    resourceId = R.drawable.tips_wifi_2;
	                    break;
	                case 2:
	                    resourceId = R.drawable.tips_wifi_3;
	                    break;
	                case 3:
	                    resourceId = R.drawable.tips_wifi_4;
	                    break;
					default:
						break;
	            }
				tips_Wifi.setImageResource(resourceId);
				tips_Eth.setImageResource(R.drawable.tips_eth_dark);
			} else if (netInfo.getTypeName().equals("ETHERNET") || netInfo.getTypeName().equals("Ethernet")) {
				tips_Eth.setImageResource(R.drawable.tips_eth);
				tips_Wifi.setImageResource(R.drawable.tips_wifi_dark);
			}
		}
	}	
    
	private void getUSBStatus() {
		boolean bRet = false;
		String file;
		File[] files = new File("/mnt/usb").listFiles();

		for (int i = 0; i < files.length; i++) {
			file = files[i].getAbsolutePath();
			//Log.d(TAG, "#9.1<#>  file = " + file);
			if (file.contains("/mnt/usb/") && files[i].exists()&&files[i].canRead()) {
				bRet = true;
				break;
			}
		}
		
		//Log.d(TAG, "#9.1<#>  bRet = " + bRet);

		if (bRet) {
			tips_USB.setImageResource(R.drawable.tips_usb);
		} else {
			tips_USB.setImageResource(R.drawable.tips_usb_dark);
		}
	}

	// public void setTime() {
	// Time t = new Time(); // or Time t=new Time("GMT+8"); 鍔犱笂Time Zone璧勬枡銆�
	// t.normalize(true);
	// t.setToNow(); // 鍙栧緱绯荤粺鏃堕棿銆�
	// String timeStr = t.hour + ":" + t.minute;
	// Log.i(TAG, "timeStr=" + timeStr);
	// timeTextView.setText(timeStr);
	// }

	public ArrayList<String> getUsbImagePath() {
		ArrayList<String> filePaths = new ArrayList<String>();
		String usbPath;
		File[] files = new File("/mnt/usb/").listFiles();
		String[] filePath = null;
		for (int i = 0; i < files.length; i++) {
			usbPath = files[i].getAbsolutePath();
			Log.d(TAG, "#9.1<#>  file = " + usbPath);
			if (usbPath.contains("/mnt/usb/sd") && files[i].exists()
					&& files[i].canRead() && files[i].listFiles().length > 0) {
				filePath = com.toptech.launcherkorea2.utils.FileUtil.getImagePaths(usbPath);
				break;
			}
		}

		if (filePath != null) {
			if (filePath.length > 0) {
				for (int i = 0; i < filePath.length; i++) {
					Log.d(TAG, "filePath[" + i + "]=" + filePath[i]);
					filePaths.add(filePath[i]);
				}
			}
			return filePaths;
		} else {
			return null;
		}
	}

	public ArrayList<String> getSdCardImagePath() {
		ArrayList<String> filePaths = new ArrayList<String>();

		String[] imagePath = null;
		if (sdCardGallery.exists()) {
			imagePath = com.toptech.launcherkorea2.utils.FileUtil.getImagePaths(sdCardGallery.toString());
		}
		if (imagePath != null) {
			for (int i = 0; i < imagePath.length; i++) {
				//Log.d("wangdaoyun","wangdaoyun----getSdCardImagePath()----imagePath[" + i + "]:" + imagePath[i]);
				filePaths.add(imagePath[i]);
			}
		}
		return filePaths;
	}

	private void creatGalleryInSdcard() {
		boolean isSdExit = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		String mDir = null;
		if (isSdExit) {
			mDir = Environment.getExternalStorageDirectory().toString();// ??��??��????
		}else{
			mDir = Environment.getDownloadCacheDirectory().toString();
		}
		if (mDir.endsWith(File.separator)) {
			sdCardGallery = new File(mDir + "gallery");
		} else {
				sdCardGallery = new File(mDir + File.separator + "gallery");
		}
		if (!sdCardGallery.exists()) {
				sdCardGallery.mkdir();
		}
		Log.d("wangdaoyun", "sdCardGallery:" + sdCardGallery.toString());
	}

	public boolean copyImageFromUsbToSDcard() {
		boolean flag = false;
		ArrayList<String> usbImagePathList = getUsbImagePath();
		// setsdCardImagePath(usbImagePathList);
		//Log.d("wangdaoyun","wangdaoyun------copyImageFromUsbToSDcard()-------sdCardGallery.exists():" + sdCardGallery.exists());
		if (sdCardGallery.exists() && (usbImagePathList.size() > 0)) {
			InputStream inputStream = null;
			FileOutputStream outputStream = null;
			for (int i = 0; i < 10 /* usbImagePathList.size() */; i++) {
				//Log.d("wangdaoyun","wangdaoyun------copyImageFromUsbToSDcard()-------usbImagePathList.get("+ i + "):" + usbImagePathList.get(i));

				String imagePath = null;
				String str = usbImagePathList.get(i);
				if (sdCardGallery.toString().endsWith(File.separator)) {
					imagePath = sdCardGallery.toString()
							+ str.substring(
									str.lastIndexOf(File.separator) + 1,
									str.length());
				} else {
					imagePath = sdCardGallery.toString()
							+ File.separator
							+ str.substring(
									str.lastIndexOf(File.separator) + 1,
									str.length());
				}
				//Log.d("wangdaoyun", "wangdaoyun------------->imagePath:" + imagePath);
				try {
					outputStream = new FileOutputStream(new File(imagePath));
					inputStream = new FileInputStream(new File(
							usbImagePathList.get(i)));
					int len = 0;
					byte[] buffer = new byte[1024];
					while ((len = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, len);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (outputStream != null) {
						try {
							outputStream.flush();
							outputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			flag = true;
		}

		return flag;
	}

	public void setsdCardImagePath(ArrayList<String> list) {
		String sdCardPath = null;
		String usbPath = null;
		String imageName = null;
		for (int i = 0; i < list.size(); i++) {
			usbPath = list.get(i);
			imageName = usbPath.substring(
					usbPath.lastIndexOf(File.separator) + 1, usbPath.length());
			if (sdCardGallery.toString().endsWith(File.separator)) {
				sdCardPath = sdCardGallery.toString() + imageName;
			} else {
				sdCardPath = sdCardGallery + File.separator + imageName;
			}
			//Log.d("wangdaoyun","wangdoayun-----setsdCardImagePath-----sdCardPath:" + sdCardPath);
			sdCardImagePathList.add(sdCardPath);
		}
	}

	public static boolean Copy(String start, String end) {
		try {
			// 瑕佹嫹璐濈殑鍥剧墖
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(new File(start)));
			// 鐩爣鐨勫湴鍧�
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(new File(end)));
			try {
				int val = -1;
				while ((val = bis.read()) != -1) {

					bos.write(val);
				}
				bos.flush();
				bos.close();
				bis.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean delFile(String filePath) {
		boolean flag = false;
		File file = new File(filePath);
		if (!file.exists()) {
			flag = true;
		}
		if (file.isFile()) {
			flag = file.delete();
		}
		return flag;
	}
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			// if (temp.isDirectory()) {
			// delAllFile(path + "/" + tempList[i]);//鍏堝垹闄ゆ枃浠跺す閲岄潰鐨勬枃浠�
			// delFolder(path + "/" + tempList[i]);//鍐嶅垹闄ょ┖鏂囦欢澶�
			// flag = true;
			// }
		}
		return flag;
	}

	BroadcastReceiver mUsb_Receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			
			switch (action) {

			case Intent.ACTION_MEDIA_MOUNTED:

				ArrayList<String> imagePathList = new ArrayList<String>();
				Log.d(TAG, "the usb has mounted!!!");
				StorageVolume volume = intent
						.getParcelableExtra(StorageVolume.EXTRA_STORAGE_VOLUME);
				String mountPath = volume.getPath();
				Log.d(TAG, "------------------mount volume------------------>"
						+ mountPath);
				imagePathList = getUsbImagePaths(mountPath);
				if (imagePathList != null && imagePathList.size() > 0) {
					usbImagePathList.addAll(imagePathList);
					FileOutputStream fos=null;
					File saveFile = null;
					boolean isSdExit = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
					if(isSdExit)
						saveFile = new File("/mnt/sdcard/gallery/imagelist.txt");
					else
						saveFile =new File("/cache/gallery/imagelist.txt");
					String tmpImage=null;
					if(saveFile.exists())
					{
						saveFile.delete();
					}
					
					try {
						fos= new FileOutputStream(saveFile,true);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for (int i = 0; i < usbImagePathList.size(); i++) {
						Log.d(TAG, "-----------usbImagePathList.size()="
								+ usbImagePathList.size()
								+ "//////////usbImagePathList.get(" + i + ")="
								+ usbImagePathList.get(i));
						tmpImage=usbImagePathList.get(i)+"\r\n";
						try {
							 byte[] data=tmpImage.getBytes();
							 if(fos !=null)
							 {
								 fos.write(data);
								 fos.flush();
								 
							 }
							 						 
						} 
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					try {
						if(fos != null)
							fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (usbImagePathList != null && usbImagePathList.size() > 0
						&& !threadrun) {
					threadrun = true;
					startPlayer();
				}
				break;
			case Intent.ACTION_MEDIA_EJECT:
				if (threadrun) {
					threadrun = false;
					stopPlayer();
				}
				break;
				
			case Intent.ACTION_MEDIA_REMOVED:
				break;
			default:
				break;
			}
			
			if(action.equals(Intent.ACTION_MEDIA_MOUNTED)
					||action.equals(Intent.ACTION_MEDIA_EJECT)){
				mUSB_Net_Handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						getUSBStatus();
					}
				}, CHECK_DELAY_TIME);
			}
		}
	};
	
	BroadcastReceiver mNet_Receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.d("TAG", "#9.1#  " + intent.toString());

			String act = intent.getAction();

	        //if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {   
			if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
				String packageName = intent.getDataString();
				for (int i = 0; i < selectedApkList.size(); i++) {
					if (selectedApkList.get(i).getPackageName() != null) {
						if(packageName.contains(selectedApkList.get(i).getPackageName())){
							selectedApkList.remove(i);
							ShortCutAdapter.inputSize = ShortCutAdapter.inputSize -1 ;
						}
					}
				}
				
				shortCutAdapter.setOnDataChanged(selectedApkList);

				System.out.println("unistalled " + packageName + "(package name)");

			}
			            		
			if(act.equals("mlauncher_SetScanFullFilter")){
				if(surfaceView != null)
				{
					surfaceView.setBackgroundColor(Color.BLACK);
					changeInputSource("com.mstar.tv.tvplayer.ui");
					Log.i(TAG,"finish Launcher Activity by sourcehotkey");
					//onStop();
					//finish();
				}
			}
			

			if (//act.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)
					act.equals(ConnectivityManager.CONNECTIVITY_ACTION)
					//||act.equals(ConnectivityManager.CONNECTIVITY_ACTION_IMMEDIATE)
					||act.equals(ConnectivityManager.INET_CONDITION_ACTION)
					|| act.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)
					|| act.equals(WifiManager.RSSI_CHANGED_ACTION)) {
				mUSB_Net_Handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						getNetStatus();
					}
				}, CHECK_DELAY_TIME);
			}
		}
	};

	public void register_NET_Action() {
             IntentFilter netFilter1 = new IntentFilter();
		netFilter1.addAction(Intent.ACTION_PACKAGE_REMOVED);
		netFilter1.addDataScheme("package");
		this.registerReceiver(mNet_Receiver, netFilter1);
		IntentFilter netFilter = new IntentFilter();
		//netFilter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION); //
		netFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		//netFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION_IMMEDIATE);
		netFilter.addAction(ConnectivityManager.INET_CONDITION_ACTION);
		netFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); // wifi
		netFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		netFilter.addAction("mlauncher_SetScanFullFilter");
		this.registerReceiver(mNet_Receiver, netFilter);
		
		
	}
	
	public void unRegister_NET_Action(){
		this.unregisterReceiver(mNet_Receiver);
		
	}
	
	
	
	private static final String PIC_Action="PIC_FROM_LOCALMM";
	private static final String newPath="/mnt/sdcard/skypic.jpg";
	private String skypicpath=null;
	private boolean issearch=false;
	BroadcastReceiver mmReceiver =new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			skypicpath=intent.getStringExtra("skyepic");
			//Log.d("yy", "picture from localMM path= "+skypicpath);
			copyFile(skypicpath,newPath);
			signaHandler.sendEmptyMessage(picsky);
		}
	};
	public void register_Pic_Action() {
		IntentFilter picFilter = new IntentFilter();
		picFilter.addAction(PIC_Action);
		this.registerReceiver(mmReceiver, picFilter);
	}
	
	public void unregister_Pic_Action(){
		this.unregisterReceiver(mmReceiver);
	}
	
	 public void copyFile(String oldPath, String newPath) {   
	       try {   
	           int bytesum = 0;   
	           int byteread = 0;   
	           File oldfile = new File(oldPath);   
	           if (oldfile.exists()) { //file 
	               InputStream inStream = new FileInputStream(oldPath);   
	               FileOutputStream fs = new FileOutputStream(newPath);   
	               byte[] buffer = new byte[1024];   
	              // int length;   
	               while ( (byteread = inStream.read(buffer)) != -1) {   
	                   bytesum += byteread;   
	                   System.out.println(bytesum);   
	                   fs.write(buffer, 0, byteread);   
	               }   
	               inStream.close();   
	           }   
	       }   
	       catch (Exception e) {   
	           System.out.println("COPY ERROR.....");   
	           e.printStackTrace();   
	       }   
	   } 
	 
	public void fileIsExists(){
         try{
                 File f=new File("/mnt/sdcard/skypic.jpg");
                 if(!f.exists()){
                	 main_pic.setBackgroundResource(R.drawable.onida_advetisement_logo);
                	 //Log.d("yy", "SdCard no this file skypic.jpg ");
                 }else {
                	 main_pic.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(newPath)));
                	 issearch=true;
                	 //Log.d("yy", "set onidapic skypic.jpg ");
                 }
         }catch (Exception e) {
         }
     }
	
	private String[] getImages()throws Exception{
		String[] titles = com.toptech.launcherkorea2.utils.FileUtil.getImagePaths("/mnt/usb/sda1/");
		//Log.d("fujia", "======>>>>>>ddd>a>aa>>>>"+titles.length);
		String[] imagePaths = new String[titles.length];
		for (int i = 0; i < titles.length; i++) {
			imagePaths[i]="/mnt/usb/sda1/"+titles[i];
			//Log.d("fujia", "======>>>>dd>>>>>>>>"+imagePaths[i]);
		}
		 return imagePaths;
	} 
	
	private String[] getImagesb() throws Exception{
		String[] titles = com.toptech.launcherkorea2.utils.FileUtil.getImagePaths("/mnt/usb/sdb1/");
		//Log.d("fujia", "======>>>>>>ddd>>b>>>>"+titles.length);
		String[] imagePaths = new String[titles.length];
		for (int i = 0; i < titles.length; i++) {
			imagePaths[i]="/mnt/usb/sdb1/"+titles[i];
			//Log.d("fujia", "======>>>>>>>>>>>>"+imagePaths[i]);
		}
		 return imagePaths;
	} 
	private String[] getImagesc()throws Exception{
		String[] titles = com.toptech.launcherkorea2.utils.FileUtil.getImagePaths("/mnt/usb/sdc1/");
		//Log.d("fujia", "======>>>>>>ddd>>>>c>>"+titles.length);
		String[] imagePaths = new String[titles.length];
		for (int i = 0; i < titles.length; i++) {
			imagePaths[i]="/mnt/usb/sdc1/"+titles[i];
			//Log.d("fujia", "======>>>>>>>>>>>>"+imagePaths[i]);
		}
		 return imagePaths;
	} 
	
	public ArrayList<String> getUsbImagePaths(String usbPath) {
		ArrayList<String> filePath = new ArrayList<String>();
		File usb = new File(usbPath);
		if (usb.exists() && usb.canRead() && usb.listFiles().length > 0) {
			filePath = FileUtil.getImagePaths(usbPath,false);
		}

		if (filePath.size() > 0) {
			return filePath;
		} else {
			return null;
		}
	}
	
	// 灞曠ず涓嬩竴寮犲浘鐗�  
	private void dispalyNextImage() {
		//System.out.println("wangdaoyun--------->dispalyNextImage()");
		// 濡傛灉鍙戠敓鏁扮粍瓒婄晫
		if (currentImg >= sdCardImagePathList.size()) {
			currentImg = 0;
		}

		BitmapDrawable bitmapDrawable = (BitmapDrawable) main_pic.getDrawable();

		// 濡傛灉鍥剧墖杩樻湭鍥炴敹锛屽厛寮哄埗鍥炴敹璇ュ浘鐗�
		if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
			bitmapDrawable.getBitmap().recycle();
		}
		String imagePath = sdCardImagePathList.get(currentImg++);
		curShowPictureName=imagePath;
		Bitmap bitmap = decodeImage(imagePath);
		main_pic.setImageBitmap(bitmap);
	}
	
	public Bitmap decodeImage(String imagePath) {
		// file no found
		if (!isFileExist(imagePath)) {
			return defaultBitmap;
		}
		Bitmap bitmap = null;
		/* BitmapFactory.Options */
		if (threadrun) {
			options = new BitmapFactory.Options();
			try {
				closeSilently(mFileInputStream);
				mFileInputStream = new FileInputStream(imagePath);
				FileDescriptor fd = mFileInputStream.getFD();
				if (fd == null) {
					closeSilently(mFileInputStream);
					return defaultBitmap;
				}
				// Plug disk, the following must be set to false.
				options.inPurgeable = false;
				options.inInputShareable = true;
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fd, null, options);
				//Log.d(TAG, "options " + options.outHeight + " " + options.outWidth);

				if (isLargerThanLimit(options)) {
					closeSilently(mFileInputStream);
					//Log.d(TAG, "**show default photo**");
					return defaultBitmap;
				}
				if (isErrorPix(options)) {
					closeSilently(mFileInputStream);
					return defaultBitmap;
				}
				// options.forceNoHWDoecode = false;
				// According to the 1920 * 1080 high-definition format picture
				// as
				// the restriction condition
				options.inSampleSize = computeSampleSizeLarger(
						options.outWidth, options.outHeight);

				//Log.d(TAG, "options.inSampleSize : " + options.inSampleSize);
				options.inJustDecodeBounds = false;
				if (fd != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fd, null,
							options);
					// jpeg png gif use the open source third-party library閿涘異mp
					// is
					// decoded by skia
					// Open source third-party library have default exception
					// handling methods閿涘湜n the exit will interrupt
					// analytic閿涘eturn
					// null
					if (bitmap != null) {
						bitmap = resizeDownIfTooBig(bitmap, 400, true);
					} else {
						return defaultBitmap;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					closeSilently(mFileInputStream);
					mFileInputStream = new FileInputStream(imagePath);
					bitmap = BitmapFactory.decodeStream(mFileInputStream, null,
							options);
					if (bitmap == null) {
						return defaultBitmap;
					}
				} catch (Exception error) {
					error.printStackTrace();
					return defaultBitmap;
				} finally {
					closeSilently(mFileInputStream);
				}
			} finally {
				closeSilently(mFileInputStream);
			}
			// ARGB_8888 is flexible and offers the best quality
			if (options != null
					&& options.inPreferredConfig != Config.ARGB_8888) {
				return ensureGLCompatibleBitmap(bitmap);
			}
		} else {
			bitmap = defaultBitmap;
		}

		return bitmap;
	}

	public boolean isFileExist(String imagePath) {
		File file = new File(imagePath);
		if (file == null) {
			return false;
		}
		return file.exists();
	}

	private void closeSilently(final Closeable c) {
		if (c == null) {
			return;
		}
		try {
			c.close();
		} catch (Throwable t) {
		}
	}

	private boolean isLargerThanLimit(BitmapFactory.Options options) {
		long pixSize = options.outWidth * options.outHeight;
		if (pixSize <= UPPER_BOUND_PIX) {
			return false;
		}
		return true;
	}

	private boolean isErrorPix(BitmapFactory.Options options) {
		if (options.outWidth <= 0 || options.outHeight <= 0) {
			return true;
		}
		return false;
	}

	// This computes a sample size which makes the longer side at least
	// minSideLength long. If that's not possible, return 1.
	private int computeSampleSizeLarger(double w, double h) {
		double initialSize = Math.max(w / UPPER_BOUND_WIDTH_PIX, h
				/ UPPER_BOUND_HEIGHT_PIX);
		if (initialSize <= 2.0f) {
			return 1;
		} else if (initialSize < 3.0f) {
			return 2;
		} else if (initialSize < 4.0f) {
			return 3;
		} else if (initialSize < 5.0f) {
			return 4;
		} else if (initialSize < 6.0f) {
			return 5;
		} else if (initialSize < 7.0f) {
			return 6;
		} else if (initialSize < 8.0f) {
			return 7;
		} else {
			return 8;
		}
	}

	// Resize the bitmap if each side is >= targetSize * 2
	private Bitmap resizeDownIfTooBig(Bitmap bitmap, int targetSize,
			boolean recycle) {
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();
		float scale = Math.max((float) targetSize / srcWidth,
				(float) targetSize / srcHeight);
		//Log.d(TAG, "srcWidth : " + srcWidth + " srcHeight : " + srcHeight + " scale : " + scale);
		if (scale > 0.5f) {
			return bitmap;
		}
		return resizeBitmapByScale(bitmap, scale, recycle);
	}

	private Bitmap resizeBitmapByScale(Bitmap bitmap, float scale,
			boolean recycle) {
		int width = Math.round(bitmap.getWidth() * scale);
		int height = Math.round(bitmap.getHeight() * scale);
		if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
			return bitmap;
		}
		Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
		Canvas canvas = new Canvas(target);
		canvas.scale(scale, scale);
		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		if (recycle) {
			bitmap.recycle();
		}
		return target;
	}

	private Bitmap.Config getConfig(Bitmap bitmap) {
		Bitmap.Config config = bitmap.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}
		return config;
	}

	private Bitmap ensureGLCompatibleBitmap(Bitmap bitmap) {
		//Log.i(TAG, "***is**" + (bitmap == null) + (bitmap.getConfig() != null));
		if (bitmap == null || bitmap.getConfig() != null) {
			return bitmap;
		}
		Bitmap newBitmap = bitmap.copy(Config.ARGB_8888, false);
		bitmap.recycle();
		System.gc();
		//Log.i(TAG, "***bitmap**" + (bitmap == null) + " " + (newBitmap == null));
		return newBitmap;
	}

	private void startPlayer() {
		Log.i(TAG, "startPlayer");
		main_pic.setBackgroundColor(Color.BLACK);
		putImageThread = new Thread(new PutImageIntoSdcard(synchronizedImage));
		outImageThread = new Thread(new GetImageFromSdcard(synchronizedImage));
		put_flag = true;
		try {
			Thread.sleep(ONE_SECOND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		putImageThread.start();
		get_flag = true;
		outImageThread.start();
		// if (thread == null) {
		// thread = new Thread() {
		// @Override
		// public void run() {
		// Thread curThread = Thread.currentThread();
		// while (thread != null && thread == curThread) {
		// try {
		// Thread.sleep(5000);
		// Message msg = new Message();
		// msg.what = SHOW_NEXT_IMAGE;
		// scaleWinHandler.sendMessage(msg);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// };
		// thread.start();
		// }
	}

	private void stopPlayer() {
		if (outImageThread != null) {
			//Log.d(TAG, "set get_flag false!!!");
			get_flag = false;
			//Log.d(TAG, "Interrupting thread outImageThread............. ");
			Thread temp = outImageThread;
			temp.interrupt();
			outImageThread = null;
			// outImageThread.interrupt();
		}
		if (putImageThread != null) {
			//Log.d(TAG, "set put_flag false!!!");
			put_flag = false;
			Thread temp = putImageThread;
			temp.interrupt();
			putImageThread = null;
			//Log.d(TAG, "Interrupting thread putImageThread............. ");
		}

		BitmapDrawable bitmapDrawable = null;
		Drawable drawable = main_pic.getDrawable();
		if (drawable instanceof BitmapDrawable) {
			bitmapDrawable = (BitmapDrawable) drawable;
		}

		// 濡傛灉鍥剧墖杩樻湭鍥炴敹锛屽厛寮哄埗鍥炴敹璇ュ浘鐗�
		if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
			bitmapDrawable.getBitmap().recycle();
		}
		main_pic.setImageDrawable(new ColorDrawable(0));
		//Log.d(TAG, "reset main_pic bitmap after eject");
		if (defaultBitmap != null && !defaultBitmap.isRecycled()) {
			main_pic.setImageBitmap(defaultBitmap);
		}else{
			main_pic.setImageResource(R.drawable.onida_advetisement_logo);
		}
		Message msg = Message.obtain();
		msg.what = DELETE_SDCARD_GALLERY;
		scaleWinHandler.sendMessageDelayed(msg, ONE_SECOND);
	}
	BroadcastReceiver mRemovedBroadcast=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("removed_package")){
				String packageName=intent.getStringExtra("pn");
				for (int i = 0; i < selectedApkList.size(); i++) {
					Log.d("cl=>", selectedApkList.size()+"");
					if (selectedApkList.get(i).getPackageName() != null) {
						String pn=selectedApkList.get(i).getPackageName();
						if(packageName!=null && pn!=null){
						if(packageName.contains(pn)){
							selectedApkList.remove(i);
							ShortCutAdapter.inputSize = ShortCutAdapter.inputSize -1 ;
							}
						}
						}
					}
				shortCutAdapter.setOnDataChanged(selectedApkList);
			
				}
				}
	};
	
	public void register_sysUI(){
		IntentFilter sysuiFilter =new IntentFilter();
		sysuiFilter.addAction("com.toptech.setsourcefromsui.ChangeToUSB");
		this.registerReceiver(systenUIrecReceiver, sysuiFilter);
	}
	
	
	BroadcastReceiver systenUIrecReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("com.toptech.setsourcefromsui.ChangeToUSB")){
				toChangeInputSource = TvCommonManager.INPUT_SOURCE_STORAGE;
				Log.d("skk", "com.toptech.setsourcefromsui.ChangeToUSB 1!");
			}
		}
	};
}
