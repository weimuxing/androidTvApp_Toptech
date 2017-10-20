package com.toptech.factorytools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class HotKeyReceiver extends BroadcastReceiver {
	
	private final static String TAG = "lyp";
	
	private Context context;

	String fileName = "/config/model/Customer_1.ini";
	final String str_panel = "panel";
	final String str_m_pPanelName = "m_pPanelName";

	final String Panel_1920x1080 = "\"/config/panel/FullHD_CMO216_H1L01.ini\"";
	final String Panel_1440x900 = "\"/config/panel/M190A1.ini\"";
	final String Panel_1366x768 = "\"/config/panel/AU20_T200XW02.ini\"";
	final String Panel_1280x800 = "\"/config/panel/Panel_1280x800.ini\"";
	final String Panel_1680x1050 = "\"/config/panel/Panel_1680x1050.ini\"";
	final static String file_not_exist = "This file is not existed in U disk!";

	@Override
	public void onReceive(Context ctx, Intent it) {
		context = ctx;
		int cmd = it.getIntExtra("hotkey_cmd", 0);
		Log.d(TAG, "2014.12.16    onReceive---cmd="+cmd);
		try {
			changeLogoPannelIni(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// zb20140912 add
	public void changeLogoPannelIni(int iChangeCode) throws IOException {
//		fileName = IniFileReadWrite.getProfileString("/config/sys.ini",
//				"model", "gModelName", "/config/model/Customer_1.ini");
//		fileName = fileName.replace("\"", "");

		switch (iChangeCode) {
		case 7551920:
			Log.d(TAG, Panel_1920x1080);
			IniFileReadWrite.setProfileString(fileName, str_panel,
					str_m_pPanelName, Panel_1920x1080);
			reboot();
			break;
		case 7551440:
			Log.d(TAG, Panel_1440x900);
			IniFileReadWrite.setProfileString(fileName, str_panel,
					str_m_pPanelName, Panel_1440x900);
			Log.d(TAG,
					"IniFileReadWrite.setProfileString");
			reboot();
			break;
		case 7551366:
			Log.d(TAG, Panel_1366x768);
			IniFileReadWrite.setProfileString(fileName, str_panel,
					str_m_pPanelName, Panel_1366x768);
			Log.d(TAG,
					"IniFileReadWrite.setProfileString");
			reboot();
			break;
		case 7551280:
			Log.d(TAG, Panel_1280x800);
			IniFileReadWrite.setProfileString(fileName, str_panel,
					str_m_pPanelName, Panel_1280x800);
			Log.d(TAG,
					"IniFileReadWrite.setProfileString");
			reboot();
			break;
		case 7551680:
			Log.d(TAG, Panel_1680x1050);
			IniFileReadWrite.setProfileString(fileName, str_panel,
					str_m_pPanelName, Panel_1680x1050);
			Log.d(TAG,
					"IniFileReadWrite.setProfileString");
			reboot();
			break;

		case 7551010: {
			Log.d(TAG, "change logo");

			File srcFile = new File("/mnt/usb/sda1/boot0.jpg");
			if (!srcFile.exists() || !srcFile.isFile() || !srcFile.canRead()) {
				Log.e(TAG, fileName
						+ "is not exist!");
				Toast.makeText(context, file_not_exist, Toast.LENGTH_LONG)
						.show();
				iChangeCode = 0;
				return;
			}

			copyIniFile("/mnt/usb/sda1/boot0.jpg", "/tvconfig/boot0.jpg");
			reboot();
			break;
		}

		case 7552020: {
			Log.d(TAG, "change Panel_user.ini");
			File srcFile = new File("/mnt/usb/sda1/Panel_user.ini");
			if (!srcFile.exists() || !srcFile.isFile() || !srcFile.canRead()) {
				Log.e(TAG, fileName
						+ "is not exist!");
				Toast.makeText(context, file_not_exist, Toast.LENGTH_LONG)
						.show();
				iChangeCode = 0;
				return;
			}

			copyIniFile("/mnt/usb/sda1/Panel_user.ini",
					"/config/panel/Panel_user.ini");
			IniFileReadWrite.setProfileString(fileName, str_panel,
					str_m_pPanelName, "\"/config/panel/Panel_user.ini\"");
			reboot();
			break;
		}

		default:
			break;
		}

	}

	public void reboot() {
		try {
			TvManager.getInstance().setEnvironment("db_table", "0");
			TvManager.getInstance().setEnvironment("top_auto_pwr_on", "1");
		} catch (TvCommonException e) {
			e.printStackTrace();
		}

		TvCommonManager.getInstance().rebootSystem("reboot");
	}

	public void copyIniFile(String srcFileStr, String destFileStr) {
		File srcFile = new File(srcFileStr);
		File destFile = new File(destFileStr);
		try {
			copyFile(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean copyFile(File srcFile, File destFile) throws IOException {
		if (!srcFile.exists()) {
			Log.i(TAG, "~src file not exits~");
			return false;
		}
		if (!srcFile.isFile()) {
			Log.i(TAG, "~src file is not a file~");
			return false;
		}
		if (!srcFile.canRead()) {
			Log.i(TAG, "~src file can  not read~");
			return false;
		}

		if (!destFile.exists()) {
			Log.i(TAG, "~dest file not exits~");
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			destFile.createNewFile();
		}
		if (!destFile.canRead()) {
			Log.i(TAG,"~dest file can  not read~");
			return false;
		}

		Log.i(TAG, "~src copy file OK~");
		FileInputStream input = new FileInputStream(srcFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);
		FileOutputStream output = new FileOutputStream(destFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);
		byte[] b = new byte[1024 * 2000];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		outBuff.flush();
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
		return true;
	}
}
