package mstar.tvsetting.factory.ui.user;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tvapi.common.TvManager;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.FactoryIntent;
import mstar.tvsetting.factory.ui.IniFileReadWrite;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;
import mstar.tvsetting.factory.until.CopyFileTask;
import mstar.tvsetting.factory.until.USBDiskSelecter;

public class UserViewHolder {
	private DesignMenuActivity mUserActivity;
	private IFactoryDesk factoryManager;

	private TvFactoryManager mTvFactoryManager = null;

	private TextView textview_user_mute_color;
	private TextView textview_user_volumecurve;
	private TextView textview_user_power_on_mode;
	private TextView textview_user_display_logo;
	private TextView textview_user_initial_channel;
	private TextView textview_user_pvr_record_all;
	private TextView textview_user_dtv_preset;
	private LinearLayout linearlayout_user_dtv_preset;
	private LinearLayout linearlayout_user_aging_mode;
	private TextView textview_user_run_time;
	private TextView textView_record_miracast;
	private LinearLayout linear_copy_to_usb;
	private TextView textView_user_scrolling_display;
	private LinearLayout linearlayout_scrolling_display;
	private String[] mStr_mute_color = null;
	private int mMute_color;

	private String[] mStr_volumecure;
	protected int mVolumecurve_Index;

	private String[] mStr_power_on_mode;
	private int mPower_on_mode_index = 0;

	private String[] mStr_display_logo;
	private int mDisplay_logo_index = 0;

	private String[] mStr_Initial_channel;
	private int mInitial_channel_index = 0;

	private String[] mStr_dtv_type;

	private int uartenableindex = 0;
	protected TextView text_factory_otheroption_uartenable_val;
	private String[] uartenable;
	private USBDiskSelecter USBDiskSelecter;

	private String filename = null;
	private String choicePath = "";
	private File fileSour;
	private File fileTar;

	public UserViewHolder(DesignMenuActivity designMenuActivity, IFactoryDesk factoryManager) {
		mUserActivity = designMenuActivity;
		this.factoryManager = factoryManager;
	}

	public void findView() {
		textview_user_mute_color = (TextView) mUserActivity.findViewById(R.id.textview_user_Mute_Color2);
		textview_user_volumecurve = (TextView) mUserActivity.findViewById(R.id.textview_user_VolumeCurve2);
		textview_user_power_on_mode = (TextView) mUserActivity.findViewById(R.id.textview_user_power_on_mode2);
		textview_user_display_logo = (TextView) mUserActivity.findViewById(R.id.textview_user_Display_logo2);
		textview_user_initial_channel = (TextView) mUserActivity.findViewById(R.id.textview_user_intitial_channel2);
		textview_user_pvr_record_all = (TextView) mUserActivity.findViewById(R.id.textview_user_pvr_record_all2);
		textview_user_dtv_preset = (TextView) mUserActivity.findViewById(R.id.textview_user_dtv_preset2);
		linearlayout_user_dtv_preset = (LinearLayout) mUserActivity.findViewById(R.id.linearlayout_user_dtv_preset);
		linearlayout_user_aging_mode = (LinearLayout) mUserActivity.findViewById(R.id.linearlayout_user_Aging_mode);
		text_factory_otheroption_uartenable_val = (TextView) mUserActivity
				.findViewById(R.id.textview_factory_otheroption_uartenable_val);
		textview_user_run_time = (TextView) mUserActivity.findViewById(R.id.textview_factory_running_time);
		textView_record_miracast = (TextView) mUserActivity.findViewById(R.id.textview_factory_record_miracast_val);
		linear_copy_to_usb = (LinearLayout) mUserActivity.findViewById(R.id.linearlayout_factory_copy_miracast);
		textView_user_scrolling_display = (TextView) mUserActivity.findViewById(R.id.textview_factory_scrolling_display_val);
		linearlayout_scrolling_display = (LinearLayout) mUserActivity.findViewById(R.id.linearlayout_factory_scrolling_display);
		boolean flag=SystemProperties.getBoolean("mstar.cus.onida", false);
		if (flag) {
			linearlayout_scrolling_display.setVisibility(View.VISIBLE);
			boolean isOpen = SystemProperties.getBoolean("persist.sys.onidaview_onoff", true);
			if (isOpen) {
				SystemProperties.set("persist.sys.onidaview_onoff", "true");
			}
			textView_user_scrolling_display.setText(isOpen ? R.string.str_textview_factory_record_miracast_text_on
					: R.string.str_textview_factory_record_miracast_text_off);
		} else {
			linearlayout_scrolling_display.setVisibility(View.GONE);
			SystemProperties.set("persist.sys.onidaview_onoff", "false");
		}
		USBDiskSelecter = new USBDiskSelecter(mUserActivity) {

			@Override
			public void onItemChosen(int i, String str, String str2) {
				choicePath = str2;
				copyToUsb(str2);
			}
		};
	}

	private void copyToUsb(String usbPath) {
		createFile();
		CopyFileTask copyFileTask = new CopyFileTask(mUserActivity);
		copyFileTask.execute(fileSour, fileTar);
	}

	private void createFile() {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String format = sFormat.format(new Date());

		String fileName = "-" + format;
		String path = choicePath + "/Miracast" + fileName + ".ts";
		fileTar = new File(path);
		if (!fileTar.exists()) {
			try {
				fileTar.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setStatus() {
		int isOpen = SystemProperties.getInt("net.wfd.dump", 0);
		if (isOpen == 1) {
			SystemProperties.set("net.wfd.dump", "0");
			textView_record_miracast.setText(R.string.str_textview_factory_record_miracast_text_off);
		} else {
			SystemProperties.set("net.wfd.dump", "1");
			textView_record_miracast.setText(R.string.str_textview_factory_record_miracast_text_on);
		}
	}
	
	public void setScrollingDisplayStatus() {
		if (SystemProperties.getBoolean("persist.sys.onidaview_onoff", false)) {
			SystemProperties.set("persist.sys.onidaview_onoff", "false");
			SystemProperties.set("persist.sys.showonidaview", "false");
			textView_user_scrolling_display.setText(R.string.str_textview_factory_record_miracast_text_off);
		} else {
			SystemProperties.set("persist.sys.onidaview_onoff", "true");
			textView_user_scrolling_display.setText(R.string.str_textview_factory_record_miracast_text_on);
		}
	}

	public void onCreate() {
		filename = "/license/runruntime.ini";
		String displaytime = null;
		try {
			displaytime = IniFileReadWrite.getProfileString(filename, "rt", "runningtime", "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// add by chenwenlong, 20161222
		Log.e("UserViewHolder", "displaytime -->>> " + displaytime);
		try {
			Float.parseFloat(displaytime);
		} catch (NumberFormatException e) {
			displaytime = "0";
		}
		// end add
		mTvFactoryManager = TvFactoryManager.getInstance();
		displaytime = String.valueOf(Float.parseFloat(displaytime) / 2f);
		textview_user_run_time.setText(
				displaytime + mUserActivity.getResources().getString(R.string.str_textview_factory_total_run_time));
		mMute_color = TvFactoryManager.VIDEO_MUTE_COLOR_BLACK;
		mMute_color = mTvFactoryManager.getVideoMuteColor();
		mStr_mute_color = mUserActivity.getResources().getStringArray(R.array.str_arr_mute_colors);

		if ((0 <= mMute_color) && (mStr_mute_color.length > mMute_color)) {
			textview_user_mute_color.setText(mStr_mute_color[mMute_color]);
		} else {
			textview_user_mute_color.setText(mStr_mute_color[TvFactoryManager.VIDEO_MUTE_COLOR_BLACK]);
		}

		mStr_volumecure = mUserActivity.getResources().getStringArray(R.array.str_arr_volumecurve_select);
		mStr_power_on_mode = mUserActivity.getResources().getStringArray(R.array.str_arr_user_poweronmode);
		mStr_display_logo = mUserActivity.getResources().getStringArray(R.array.str_arr_user_display_logo);
		mStr_Initial_channel = mUserActivity.getResources().getStringArray(R.array.str_arr_user_initial_channel);
		mStr_dtv_type = mUserActivity.getResources().getStringArray(R.array.str_arr_user_dtv_type);

		try {
			mInitial_channel_index = mTvFactoryManager.getFactoryPreSetFeature();
			if (mInitial_channel_index >= mStr_Initial_channel.length) {
				mInitial_channel_index = 0;
				mTvFactoryManager.setFactoryPreSetFeature(mInitial_channel_index);
			}
		} catch (Exception e) {
			System.out.println("------------");
		}

		textview_user_initial_channel.setText(mStr_Initial_channel[mInitial_channel_index]);

		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
		mVolumecurve_Index = retval[4];

		if (mVolumecurve_Index >= mStr_volumecure.length)
			textview_user_volumecurve.setText("undefined");
		else
			textview_user_volumecurve.setText(mStr_volumecure[mVolumecurve_Index]);

		mPower_on_mode_index = factoryManager.getPowerOnMode();
		textview_user_power_on_mode.setText(mStr_power_on_mode[mPower_on_mode_index]);

		boolean logo_flag = false;
		try {
			logo_flag = TvManager.getInstance().getEnvironment("display_logo").equals("on");
		} catch (Exception e) {
			// TODO: handle exception
		}
		mDisplay_logo_index = logo_flag ? 1 : 0;
		textview_user_display_logo.setText(mStr_display_logo[mDisplay_logo_index]);

		if (true == TvFactoryManager.getInstance().getPvrRecordAll()) {
			textview_user_pvr_record_all
					.setText(mUserActivity.getString(R.string.str_textview_factory_otheroption_text_on));
		} else {
			textview_user_pvr_record_all
					.setText(mUserActivity.getString(R.string.str_textview_factory_otheroption_text_off));
		}

		uartenable = mUserActivity.getResources().getStringArray(R.array.str_arr_user_display_logo);
		uartenableindex = factoryManager.getUartOnOff() ? 1 : 0;
		text_factory_otheroption_uartenable_val.setText(uartenable[uartenableindex]);
		registerListeners();
	}

	private void registerListeners() {
		linearlayout_user_dtv_preset.setOnClickListener(listener);
		linearlayout_user_aging_mode.setOnClickListener(listener);
		linear_copy_to_usb.setOnClickListener(listener);
	}

	private void changePVRRecordAll() {
		if (true == TvFactoryManager.getInstance().getPvrRecordAll()) {
			TvFactoryManager.getInstance().setPvrRecordAll(false);
			textview_user_pvr_record_all
					.setText(mUserActivity.getString(R.string.str_textview_factory_otheroption_text_off));
		} else {
			TvFactoryManager.getInstance().setPvrRecordAll(true);
			textview_user_pvr_record_all
					.setText(mUserActivity.getString(R.string.str_textview_factory_otheroption_text_on));
		}
	}

	private void doDtvPreset(int which) {// 1 is for dvbc, 0 is for dtmb
		System.out.println("\n==>restoreFactoryDtvProgramTable from java");
		textview_user_dtv_preset.setText(R.string.str_factory_otheroption_dtv_preset_hint_msg1);
		factoryManager.restoreFactoryDtvProgramTable((short) 0, which);
		textview_user_dtv_preset.setText(R.string.str_factory_otheroption_dtv_preset_hint_msg2);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int currentid = mUserActivity.getCurrentFocus().getId();
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentid) {
			case R.id.linearlayout_user_Mute_Color:
				int newVideoMuteColor = mMute_color + 1;
				if ((0 > newVideoMuteColor) || (mStr_mute_color.length <= newVideoMuteColor)) {
					newVideoMuteColor = 0;
				}
				if (true == mTvFactoryManager.setVideoMuteColor(newVideoMuteColor)) {
					textview_user_mute_color.setText(mStr_mute_color[newVideoMuteColor]);
					mMute_color = newVideoMuteColor;
				}
				break;
			case R.id.linearlayout_user_VolumeCurve: {
				mVolumecurve_Index++;
				if (mVolumecurve_Index > (mStr_volumecure.length - 1))
					mVolumecurve_Index = 0;
				TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/volumecure*" + mVolumecurve_Index);
				int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
				textview_user_volumecurve.setText(mStr_volumecure[retval[4]]);
			}
				break;
			case R.id.linearlayout_user_power_on_mode:
				if (mPower_on_mode_index != IFactoryDesk.EN_POWER_MODE_DIRECT)
					mPower_on_mode_index++;
				else
					mPower_on_mode_index = 0;
				textview_user_power_on_mode.setText(mStr_power_on_mode[mPower_on_mode_index]);
				factoryManager.setPowerOnMode(mPower_on_mode_index);
				break;
			case R.id.linearlayout_user_Display_logo:
				if (mDisplay_logo_index == 0) {
					mDisplay_logo_index = 1;
					// factoryManager.setDisplayLogoOnOff(true);
					try {
						TvManager.getInstance().setEnvironment("display_logo", "on");
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					mDisplay_logo_index = 0;
					// factoryManager.setDisplayLogoOnOff(false);
					try {
						TvManager.getInstance().setEnvironment("display_logo", "off");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				textview_user_display_logo.setText(mStr_display_logo[mDisplay_logo_index]);
				break;
			case R.id.linearlayout_user_intitial_channel:
				if (mInitial_channel_index != 1)
					mInitial_channel_index++;
				else
					mInitial_channel_index = 0;
				textview_user_initial_channel.setText(mStr_Initial_channel[mInitial_channel_index]);
				mTvFactoryManager.setFactoryPreSetFeature(mInitial_channel_index);
				break;
			case R.id.linearlayout_user_pvr_record_all:
				changePVRRecordAll();
				break;
			case R.id.linearlayout_factory_otheroption_uartenable:
				if (uartenableindex == 0) {
					uartenableindex = 1;
					factoryManager.setUartOnOff(true);
				} else {
					uartenableindex = 0;
					factoryManager.setUartOnOff(false);
				}
				text_factory_otheroption_uartenable_val.setText(uartenable[uartenableindex]);
				break;
			case R.id.linearlayout_factory_record_miracast:
				setStatus();
				break;
			case R.id.linearlayout_factory_scrolling_display:
				setScrollingDisplayStatus();
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentid) {
			case R.id.linearlayout_user_Mute_Color:
				int newVideoMuteColor = mMute_color - 1;
				if ((0 > newVideoMuteColor) || (mStr_mute_color.length <= newVideoMuteColor)) {
					newVideoMuteColor = mStr_mute_color.length - 1;
				}
				if (true == mTvFactoryManager.setVideoMuteColor(newVideoMuteColor)) {
					textview_user_mute_color.setText(mStr_mute_color[newVideoMuteColor]);
					mMute_color = newVideoMuteColor;
				}
				break;
			case R.id.linearlayout_user_VolumeCurve: {
				if (mVolumecurve_Index < 1)
					mVolumecurve_Index = (mStr_volumecure.length - 1);
				else
					mVolumecurve_Index--;
				TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/volumecure*" + mVolumecurve_Index);
				int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
				textview_user_volumecurve.setText(mStr_volumecure[retval[4]]);
			}
				break;
			case R.id.linearlayout_user_power_on_mode:
				if (mPower_on_mode_index != IFactoryDesk.EN_POWER_MODE_DIRECT)
					mPower_on_mode_index++;
				else
					mPower_on_mode_index = 0;
				textview_user_power_on_mode.setText(mStr_power_on_mode[mPower_on_mode_index]);
				factoryManager.setPowerOnMode(mPower_on_mode_index);
				break;
			case R.id.linearlayout_user_Display_logo:
				if (mDisplay_logo_index == 0) {
					mDisplay_logo_index = 1;
					// factoryManager.setDisplayLogoOnOff(true);
					try {
						TvManager.getInstance().setEnvironment("display_logo", "on");
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					mDisplay_logo_index = 0;
					// factoryManager.setDisplayLogoOnOff(false);
					try {
						TvManager.getInstance().setEnvironment("display_logo", "off");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				textview_user_display_logo.setText(mStr_display_logo[mDisplay_logo_index]);
				break;
			case R.id.linearlayout_user_intitial_channel:
				if (mInitial_channel_index != 0)
					mInitial_channel_index--;
				else
					mInitial_channel_index = 1;
				textview_user_initial_channel.setText(mStr_Initial_channel[mInitial_channel_index]);
				mTvFactoryManager.setFactoryPreSetFeature(mInitial_channel_index);
				break;
			case R.id.linearlayout_user_pvr_record_all:
				changePVRRecordAll();
				break;
			case R.id.linearlayout_factory_otheroption_uartenable:
				if (uartenableindex == 1) {
					uartenableindex = 0;
					factoryManager.setUartOnOff(false);
				} else {
					uartenableindex = 1;
					factoryManager.setUartOnOff(true);
				}
				text_factory_otheroption_uartenable_val.setText(uartenable[uartenableindex]);
				break;
			case R.id.linearlayout_factory_record_miracast:
				setStatus();
				break;
			case R.id.linearlayout_factory_scrolling_display:
				setScrollingDisplayStatus();
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_ENTER:
			switch (currentid) {
			case R.id.linearlayout_user_factory_program_preset_function:
				Intent intent = new Intent();
				intent.setAction(FactoryIntent.ACTION_FACTORY_PROGRAM_PRESET);
				mUserActivity.startActivity(intent);
				break;
			default:
				break;
			}
			break;
		default:
			return false;
		}
		return true;
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			int currentid = mUserActivity.getCurrentFocus().getId();
			switch (currentid) {
			case R.id.linearlayout_user_Aging_mode: {
				/* release mut status when doing aging mode.lxk 20150126 */
				if (SystemProperties.getInt("persist.sys.istvmute", 0) == 1) {
					AudioManager mAudioManager = (AudioManager) mUserActivity.getApplicationContext()
							.getSystemService(Context.AUDIO_SERVICE);
					mAudioManager.setMasterMute(false);
				}
				/* end */
				PackageManager pm = mUserActivity.getPackageManager();
				ComponentName componentName = new ComponentName("com.toptech.factorytools",
						"com.toptech.factorytools.AgingActivity");
				pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
						PackageManager.DONT_KILL_APP);
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setComponent(componentName);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				mUserActivity.startActivity(intent);
				mUserActivity.finish();
			}
				// end
				break;
			case R.id.linearlayout_user_dtv_preset:
				new AlertDialog.Builder(mUserActivity)
						// build AlertDialog
						.setTitle(R.string.str_dtv_preset_title)
						// title
						.setItems(mStr_dtv_type, new DialogInterface.OnClickListener() { // content
							@Override
							public void onClick(DialogInterface dialog, int which) {
								doDtvPreset(which);
							}
						}).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss(); // 关闭alertDialog
							}
						}).show();
				break;
			case R.id.linearlayout_factory_copy_miracast:
				int usbDriverCount = USBDiskSelecter.getDriverCount();
				fileSour = new File("/data/wfd.ts");
				boolean exists = fileSour.exists();
				if (!exists) {
					Toast.makeText(mUserActivity, "please use miracast first ", 0).show();
					return;
				}
				if (usbDriverCount > 0) {
					USBDiskSelecter.start();
				} else {
					Toast.makeText(mUserActivity, R.string.str_please_plug_USB, Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	};

}
