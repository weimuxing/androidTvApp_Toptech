//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package mstar.tvsetting.factory.ui.upgrade;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mstar.android.storage.MStorageManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
//import com.toptech.factorytools.IniFileReadWrite;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.IniFileReadWrite;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

public class UpgradeViewHolder {
	private DesignMenuActivity mUpgradeActivity;

	private IFactoryDesk factoryManager;

	protected LinearLayout linearlayout_upgrade_pq;
	protected TextView textview_upgrade_pq;

	protected LinearLayout linearlayout_upgrade_mboot;
	protected TextView textview_upgrade_mboot;

	protected LinearLayout linearlayout_upgrade_main;
	protected TextView textview_upgrade_main;

	protected LinearLayout linearlayout_upgrade_6m30;
	protected TextView textview_upgrade_6m30;

	protected LinearLayout linearlayout_upgrade_dual_ursa;
	protected TextView textview_upgrade_dual_ursa;

	protected LinearLayout linearlayout_upgrade_ci_upgrade;
	protected TextView textview_upgrade_ci_upgrade;
	protected TextView textview_upgrade_ci_display;

	protected LinearLayout linearlayout_upgrade_hdcp_upgrade;
	protected TextView textview_upgrade_hdcp_upgrade;
	protected TextView textview_upgrade_hdcp_display;

	protected LinearLayout linearlayout_upgrade_hdcp2_upgrade;
	protected TextView textview_upgrade_hdcp2_upgrade;
	protected TextView textview_upgrade_hdcp2_display;

	protected LinearLayout linearlayout_upgrade_mac_upgrade;
	protected TextView textview_upgrade_mac_upgrade;
	protected TextView textview_upgrade_mac_display;

	protected LinearLayout linearlayout_upgrade_sn_upgrade;
	protected TextView textview_upgrade_sn_upgrade;
	protected TextView textview_upgrade_sn_display;

	private int upgrademboot = 0;

	private int upgrade6m30 = 0;

	private int upgradedualursa = 0;

	private static final byte DialogType = 0;

	private String[] upgradeStatus;

	private short commendResult[] = null;
	private String strresult = null;
	private String filename = null;
	private String filename2 = null;

	private boolean pq_update_state = false;

	private MStorageManager stm;

	public UpgradeViewHolder(DesignMenuActivity activity, IFactoryDesk factoryDesk) {
		mUpgradeActivity = activity;
		this.factoryManager = factoryDesk;
	}

	public void findView() {
		linearlayout_upgrade_pq = (LinearLayout) mUpgradeActivity.findViewById(R.id.linearlayout_upgrade_PQ);
		linearlayout_upgrade_mboot = (LinearLayout) mUpgradeActivity.findViewById(R.id.linearlayout_upgrade_mboot);
		linearlayout_upgrade_main = (LinearLayout) mUpgradeActivity.findViewById(R.id.linearlayout_upgrade_main);
		linearlayout_upgrade_6m30 = (LinearLayout) mUpgradeActivity.findViewById(R.id.linearlayout_upgrade_6m30);
		linearlayout_upgrade_dual_ursa = (LinearLayout) mUpgradeActivity
				.findViewById(R.id.linearlayout_upgrade_dual_ursa);
		textview_upgrade_pq = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_PQ2);
		textview_upgrade_mboot = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_mboot2);
		textview_upgrade_main = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_main2);
		textview_upgrade_6m30 = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_6m30_2);
		textview_upgrade_dual_ursa = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_dual_ursa2);
		linearlayout_upgrade_ci_upgrade = (LinearLayout) mUpgradeActivity
				.findViewById(R.id.linearlayout_upgrade_ci_upgrade);
		textview_upgrade_ci_upgrade = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_ci_upgrade2);
		textview_upgrade_ci_display = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_ci_displa2);

		linearlayout_upgrade_hdcp_upgrade = (LinearLayout) mUpgradeActivity
				.findViewById(R.id.linearlayout_upgrade_hdcp_upgrade);
		textview_upgrade_hdcp_upgrade = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_hdcp_upgrade2);
		textview_upgrade_hdcp_display = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_hdcp_display2);

		linearlayout_upgrade_hdcp2_upgrade = (LinearLayout) mUpgradeActivity
				.findViewById(R.id.linearlayout_upgrade_hdcp2_upgrade);
		textview_upgrade_hdcp2_upgrade = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_hdcp2_upgrade2);
		textview_upgrade_hdcp2_display = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_hdcp2_display2);

		linearlayout_upgrade_mac_upgrade = (LinearLayout) mUpgradeActivity
				.findViewById(R.id.linearlayout_upgrade_mac_upgrade);
		textview_upgrade_mac_upgrade = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_mac_upgrade2);
		textview_upgrade_mac_display = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_mac_display2);

		linearlayout_upgrade_sn_upgrade = (LinearLayout) mUpgradeActivity
				.findViewById(R.id.linearlayout_upgrade_serial_upgrade);
		textview_upgrade_sn_upgrade = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_serial_upgrade2);
		textview_upgrade_sn_display = (TextView) mUpgradeActivity.findViewById(R.id.textview_upgrade_sn_display2);
	}

	public boolean onCreate() {
		upgradeStatus = mUpgradeActivity.getResources().getStringArray(R.array.str_arr_upgradeStatus);
		filename = "/license/macandci.ini";
		filename2 = "/license/hdcp2.ini";
		// text_factory_ci_display
		textview_upgrade_ci_upgrade.setText(mUpgradeActivity.getString(R.string.str_click_to_upgrade));
		try {
			textview_upgrade_ci_display.setText(IniFileReadWrite.getProfileString(filename, "ci", "cidisplay",
					mUpgradeActivity.getString(R.string.str_please_update)));

		} catch (IOException e) {
			e.printStackTrace();
		}
		// text_factory_hdcp_display
		textview_upgrade_hdcp_upgrade.setText(mUpgradeActivity.getString(R.string.str_click_to_upgrade));
		try {
			textview_upgrade_hdcp_display.setText(IniFileReadWrite.getProfileString(filename, "hdcp", "hdcpdisplay",
					mUpgradeActivity.getString(R.string.str_please_update)));

		} catch (IOException e) {
			e.printStackTrace();
		}
		// text_factory_hdcp2_display
		textview_upgrade_hdcp2_upgrade.setText(mUpgradeActivity.getString(R.string.str_click_to_upgrade));
		try {
			textview_upgrade_hdcp2_display.setText(IniFileReadWrite.getProfileString(filename2, "hdcp2", "display",
					mUpgradeActivity.getString(R.string.str_please_update)));

		} catch (IOException e) {
			e.printStackTrace();
		}
		// text_factory_mac_display
		textview_upgrade_mac_upgrade.setText(mUpgradeActivity.getString(R.string.str_click_to_upgrade));
		try {
			textview_upgrade_mac_display.setText(IniFileReadWrite.getProfileString(filename, "mac", "macdisplay",
					mUpgradeActivity.getString(R.string.str_please_update)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// text_factory_sn_display
		textview_upgrade_sn_upgrade.setText(mUpgradeActivity.getString(R.string.str_click_to_upgrade));
		try {
			textview_upgrade_sn_display.setText(IniFileReadWrite.getProfileString(filename, "sn", "serialnumber",
					mUpgradeActivity.getString(R.string.str_please_update)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		registerListeners();
		return true;
	}

	private void openAndriodDialog(boolean flag) {
		AlertDialog.Builder builder = new Builder(mUpgradeActivity);
		if (flag == true) {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_are_you_sure_upgrade_mboot));
		} else {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_please_plug_USB));
		}
		// builder.setTitle("???");
		builder.setPositiveButton(mUpgradeActivity.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (CheckUsbIsExist() == true) {
							upgrademboot = UpgradeMbootFun();
							textview_upgrade_mboot.setText(upgradeStatus[upgrademboot]);
							// otheroptionActivity.finish();
						}
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(mUpgradeActivity.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						upgrademboot = 0;
						dialog.dismiss();
						textview_upgrade_mboot.setText(upgradeStatus[upgrademboot]);
					}
				});
		builder.create().show();
	}

	private void openAndriodDialogMain(boolean flag) {
		AlertDialog.Builder builder = new Builder(mUpgradeActivity);
		if (flag == true) {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_are_you_sure_upgrade_main));
		} else {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_please_plug_USB));
		}
		builder.setPositiveButton(mUpgradeActivity.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (CheckUsbIsExist() == true) {
							if (UpgradeMainFun()) {
								textview_upgrade_main
										.setText(mUpgradeActivity.getString(R.string.str_please_reboot_the_tv));
							} else {
								textview_upgrade_main.setText(mUpgradeActivity.getString(R.string.str_fail));
							}
							// otheroptionActivity.finish();
						}
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(mUpgradeActivity.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void openAndriodDialog6M30(boolean flag) {
		AlertDialog.Builder builder = new Builder(mUpgradeActivity);
		if (flag == true) {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_are_you_sure_upgrade_6M30));
		} else {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_please_plug_USB));
		}
		builder.setPositiveButton(mUpgradeActivity.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (CheckUsbIsExist() == true) {
							upgrade6m30 = Upgrade6M30Fun();
							textview_upgrade_6m30.setText(upgradeStatus[upgrade6m30]);
							// otheroptionActivity.finish();
						}
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(mUpgradeActivity.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						upgrade6m30 = 0;
						dialog.dismiss();
						textview_upgrade_6m30.setText(upgradeStatus[upgrade6m30]);
					}
				});
		builder.create().show();
	}

	private void openAndriodDialogDualUrsa(boolean flag) {
		AlertDialog.Builder builder = new Builder(mUpgradeActivity);
		if (flag == true) {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_are_you_sure_upgrade_dual_ursa));
		} else {
			builder.setMessage(mUpgradeActivity.getString(R.string.str_please_plug_USB));
		}
		builder.setPositiveButton(mUpgradeActivity.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (CheckUsbIsExist() == true) {
							upgradedualursa = UpgradeDualUrsaFun();
							textview_upgrade_dual_ursa.setText(upgradeStatus[upgradedualursa]);
							// otheroptionActivity.finish();
						}
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(mUpgradeActivity.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						upgradedualursa = 0;
						dialog.dismiss();
						textview_upgrade_dual_ursa.setText(upgradeStatus[upgradedualursa]);
					}
				});
		builder.create().show();
	}

	private boolean CheckUsbIsExist() {
		boolean ret = true;
		// TODO check usb exit function
		return ret;
	}

	private int UpgradeMbootFun() {
		int ret = 1;
		// TODO UpgradeMboot function
		return ret;
	}

	private boolean UpgradeMainFun() {
		boolean ret = false;
		// TODO UpgradeMain function
		return ret;
	}

	private int Upgrade6M30Fun() {
		int ret = 1;
		// TODO Upgrade6M30 function
		return ret;
	}

	private int UpgradeDualUrsaFun() {
		int ret = 1;
		// TODO UpgradeDualUrsaFun function
		return ret;
	}

	private void openLayoutDialog() {
		DialogMenu dlg = new DialogMenu(mUpgradeActivity, R.id.textview_upgrade_mboot2);
		/*
		 * dlg.setOnDismissListener(new OnDismissListener() { public void
		 * onDismiss(DialogInterface dialog) { dialog.dismiss(); } });
		 */
		dlg.show();
	}

	private void openLayoutDialogMain() {
		DialogMenu dlg = new DialogMenu(mUpgradeActivity, R.id.textview_upgrade_main2);
		/*
		 * dlg.setOnDismissListener(new OnDismissListener() { public void
		 * onDismiss(DialogInterface dialog) { dialog.dismiss(); } });
		 */
		dlg.show();
	}

	private void openLayoutDialog6M30() {
		DialogMenu dlg = new DialogMenu(mUpgradeActivity, R.id.textview_upgrade_6m30_2);
		/*
		 * dlg.setOnDismissListener(new OnDismissListener() { public void
		 * onDismiss(DialogInterface dialog) { dialog.dismiss(); } });
		 */
		dlg.show();
	}

	private void openLayoutDialogDualUrsa() {
		DialogMenu dlg = new DialogMenu(mUpgradeActivity, R.id.textview_upgrade_dual_ursa2);
		/*
		 * dlg.setOnDismissListener(new OnDismissListener() { public void
		 * onDismiss(DialogInterface dialog) { dialog.dismiss(); } });
		 */
		dlg.show();
	}

	private void registerListeners() {
		linearlayout_upgrade_ci_upgrade.setOnClickListener(listener);
		linearlayout_upgrade_hdcp_upgrade.setOnClickListener(listener);
		linearlayout_upgrade_hdcp2_upgrade.setOnClickListener(listener);
		linearlayout_upgrade_mac_upgrade.setOnClickListener(listener);
		linearlayout_upgrade_sn_upgrade.setOnClickListener(listener);
		linearlayout_upgrade_mboot.setOnClickListener(listener);
		linearlayout_upgrade_main.setOnClickListener(listener);
		linearlayout_upgrade_6m30.setOnClickListener(listener);
		linearlayout_upgrade_dual_ursa.setOnClickListener(listener);
		linearlayout_upgrade_pq.setOnClickListener(listener);

	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.linearlayout_upgrade_PQ:
				if (pq_update_state) {
					textview_upgrade_pq
							.setText(mUpgradeActivity.getString(R.string.str_factory_otheroption_pq_update_state));
					pq_update_state = false;
				} else {
					updatePqFile();
					pq_update_state = true;
				}
				break;
			case R.id.linearlayout_upgrade_mboot:
				if (DialogType == 1) {
					openAndriodDialog(CheckUsbIsExist());
				} else {
					openLayoutDialog();
				}
				break;
			case R.id.linearlayout_upgrade_main:
				if (DialogType == 1) {
					openAndriodDialogMain(CheckUsbIsExist());
				} else {
					openLayoutDialogMain();
				}
				break;
			case R.id.linearlayout_upgrade_6m30:
				if (DialogType == 1) {
					openAndriodDialog6M30(CheckUsbIsExist());
				} else {
					openLayoutDialog6M30();
				}
				break;
			case R.id.linearlayout_upgrade_dual_ursa:
				if (DialogType == 1) {
					openAndriodDialogDualUrsa(CheckUsbIsExist());
				} else {
					openLayoutDialogDualUrsa();
				}
				break;
			case R.id.linearlayout_upgrade_ci_upgrade:
				Log.d("wym", "click to upgrade ci!\n");
				try {
					commendResult = TvManager.getInstance()
							.setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_CIKEY_DIRECT);
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// update the textview_upgrade_ci_display
				// if success, get the value from inifile
				if (commendResult[0] == 0) {
					strresult = mUpgradeActivity.getString(R.string.str_small_sucess);
					String displayval = null;
					try {
						// IniReader IniFile = new
						// IniReader("/license/macandci.ini");
						// textview_upgrade_ci_display
						// .setText(IniFile.getValue("ci", "cidisplay"));
						// Log.d("wym","ci:"+IniFile.getValue("ci",
						// "cidisplay"));
						displayval = IniFileReadWrite.getProfileString(filename, "ci", "cidisplay",
								mUpgradeActivity.getString(R.string.str_please_update));
						textview_upgrade_ci_display.setText(displayval);
						Log.d("wym", "ci:" + displayval);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					strresult = mUpgradeActivity.getString(R.string.str_small_fail);

				textview_upgrade_ci_upgrade.setText(strresult);
				break;
			case R.id.linearlayout_upgrade_hdcp_upgrade:
				Log.d("wym", "click to upgrade hdcp!\n");
				try {
					commendResult = TvManager.getInstance()
							.setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_HDCPKEY_DIRECT);
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// update the textview_upgrade_hdcp_display
				// if success, get the value from inifile
				if (commendResult[0] == 0) {
					strresult = mUpgradeActivity.getString(R.string.str_small_sucess);
					String displayval = null;
					try {
						// IniReader IniFile = new
						// IniReader("/license/macandci.ini");
						// textview_upgrade_hdcp_display
						// .setText(IniFile.getValue("hdcp", "hdcpdisplay"));
						// Log.d("wym","hdcp:"+IniFile.getValue("hdcp",
						// "hdcpdisplay"));
						displayval = IniFileReadWrite.getProfileString(filename, "hdcp", "hdcpdisplay",
								mUpgradeActivity.getString(R.string.str_please_update));
						textview_upgrade_hdcp_display.setText(displayval);
						Log.d("wym", "hdcp:" + displayval);

					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					strresult = mUpgradeActivity.getString(R.string.str_small_fail);
				}

				textview_upgrade_hdcp_upgrade.setText(strresult);
				break;
			case R.id.linearlayout_upgrade_hdcp2_upgrade:
				Log.d("wym", "click to upgrade hdcp!\n");
				try {
					commendResult = TvManager.getInstance()
							.setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_HDCP2KEY_DIRECT);
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// update the textview_upgrade_hdcp_display
				// if success, get the value from inifile
				if (commendResult[0] == 0) {
					strresult = mUpgradeActivity.getString(R.string.str_small_sucess);
					String displayval = null;
					try {
						// IniReader IniFile = new
						// IniReader("/license/macandci.ini");
						// textview_upgrade_hdcp_display
						// .setText(IniFile.getValue("hdcp", "hdcpdisplay"));
						// Log.d("wym","hdcp:"+IniFile.getValue("hdcp",
						// "hdcpdisplay"));
						displayval = IniFileReadWrite.getProfileString(filename2, "hdcp2", "display",
								mUpgradeActivity.getString(R.string.str_please_update));
						textview_upgrade_hdcp2_display.setText(displayval);
						Log.d("wym", "hdcp2:" + displayval);

					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					strresult = mUpgradeActivity.getString(R.string.str_small_fail);
				}

				textview_upgrade_hdcp2_upgrade.setText(strresult);
				break;
			case R.id.linearlayout_upgrade_mac_upgrade:
				Log.d("wym", "click to upgrade mac!\n");
				try {
					commendResult = TvManager.getInstance()
							.setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_MAC_DIRECT);
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// update the textview_upgrade_mac_display
				// if success, get the value from inifile
				if (commendResult[0] == 0) {
					strresult = mUpgradeActivity.getString(R.string.str_small_sucess);
					String displayval = null;
					try {
						// IniReader IniFile = new
						// IniReader("/license/macandci.ini");
						// textview_upgrade_mac_display
						// .setText(IniFile.getValue("mac", "macdisplay"));
						// Log.d("wym","mac:"+IniFile.getValue("mac",
						// "macdisplay"));
						displayval = IniFileReadWrite.getProfileString(filename, "mac", "macdisplay",
								mUpgradeActivity.getString(R.string.str_please_update));
						textview_upgrade_mac_display.setText(displayval);
						Log.d("wym", "mac:" + displayval);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					strresult = mUpgradeActivity.getString(R.string.str_small_fail);

				textview_upgrade_mac_upgrade.setText(strresult);
				break;
			case R.id.linearlayout_upgrade_serial_upgrade:
				Log.d("wym", "click to upgrade sn!\n");
				try {
					commendResult = TvManager.getInstance()
							.setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_SN_DIRECT);
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// update the textview_upgrade_sn_display
				// if success, get the value from inifile
				if (commendResult[0] == 0) {
					strresult = mUpgradeActivity.getString(R.string.str_small_sucess);
					String displayval = null;
					try {
						// IniReader IniFile = new
						// IniReader("/license/macandci.ini");
						// textview_upgrade_sn_display
						// .setText(IniFile.getValue("sn", "serialnumber"));
						// Log.d("wym","mac:"+IniFile.getValue("sn",
						// "serialnumber"));
						displayval = IniFileReadWrite.getProfileString(filename, "sn", "serialnumber",
								mUpgradeActivity.getString(R.string.str_please_update));
						textview_upgrade_sn_display.setText(displayval);
						Log.d("wym", "sn:" + displayval);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					strresult = mUpgradeActivity.getString(R.string.str_small_fail);
				textview_upgrade_sn_upgrade.setText(strresult);
				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentid = mUpgradeActivity.getCurrentFocus().getId();
		switch (keyCode) {
		// case KeyEvent.KEYCODE_BACK:
		// case KeyEvent.KEYCODE_MENU:
		// this.mUpgradeActivity
		// .returnRoot(/* DesignMenuActivity.UPGRADE_SETTING_PAGE */11);
		// break;
		default:
			bRet = false;
			break;
		}

		return bRet;
	}

	public void updatePqFile() {
		int i = 0;
		File srcFile;
		File destFile;
		boolean ret = false;
		stm = MStorageManager.getInstance(mUpgradeActivity);
		String destPath = new String();
		String[] volumes = stm.getVolumePaths();
		String usbPath = new String();
		if (volumes == null) {
			return;
		}
		/* Find the first available mounted disk */
		boolean flag = false;
		for (i = 0; i < volumes.length; ++i) {
			String state = stm.getVolumeState(volumes[i]);
			if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
				continue;
			}
			textview_upgrade_pq.setText(mUpgradeActivity.getString(R.string.str_upgrade_ok));
			/* Copy all pq update files to destPath */
			File file = new File(volumes[i] + "/pqbin");
			if (file != null && file.exists()) {
				usbPath = volumes[i];
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_DLC_FILE);
				destPath = "/Customer/DLC/DLC.ini";
				Log.i("OtherOption", "....1....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/DLC.ini");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_COLOR_MATRiX_FILE);
				destPath = "/Customer/ColorMatrix/ColorMatrix.ini";
				Log.i("OtherOption", "....2....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/ColorMatrix.ini");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_BANDWIDTH_REG_TABLE_FILE);
				destPath = "/Customer/pq/Bandwidth_RegTable.bin";
				Log.i("OtherOption", "....3....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Bandwidth_RegTable.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_MAIN_FILE);
				destPath = "/Customer/pq/Main.bin";
				Log.i("OtherOption", "....4....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Main.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_MAIN_TEXT_FILE);
				destPath = "/Customer/pq/Main_Text.bin";
				Log.i("OtherOption", "....5....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Main_Text.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_MAIN_EX_FILE);
				destPath = "/Customer/pq/Main_Ex.bin";
				Log.i("OtherOption", "....6....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Main_Ex.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_MAIN_EX_TEXT_FILE);
				destPath = "/Customer/pq/Main_Ex_Text.bin";
				Log.i("OtherOption", "....7....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Main_Ex_Text.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_MAIN_EX_TEXT_FILE);
				destPath = "/Customer/pq/Main_Color.bin";
				Log.i("OtherOption", "....8....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Main_Color.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_MAIN_EX_TEXT_FILE);
				destPath = "/Customer/pq/Main_Color_Text.bin";
				Log.i("OtherOption", "....9....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Main_Color_Text.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_MSRV_PQ_SUB_FILE);
				destPath = "/Customer/pq/Sub.bin";
				Log.i("OtherOption", "....10....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Sub.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_SUB_TEXT_FILE);
				destPath = "/Customer/pq/Sub_Text.bin";
				Log.i("OtherOption", "....11....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Sub_Text.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_SUB_EX_FILE);
				destPath = "/Customer/pq/Sub_Ex.bin";
				Log.i("OtherOption", "....12....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Sub_Ex.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_PQ_SUB_EX_TEXT_FILE);
				destPath = "/Customer/pq/Sub_Ex_Text.bin";
				Log.i("OtherOption", "....13....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Sub_Ex_Text.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				destPath = "/Customer/pq/Sub_Color.bin";
				Log.i("OtherOption", "....14....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Sub_Color.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				destPath = "/Customer/pq/Sub_Color_Text.bin";
				Log.i("OtherOption", "....15....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/Sub_Color_Text.bin");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				// destPath =
				// factoryManager.getUpdatePqFilePath(EnumPqUpdateFile.E_GAMMA0_FILE);
				destPath = "/Customer/pq/gamma0.ini";
				Log.i("OtherOption", "....16....destPath is " + destPath + "..........");
				srcFile = new File(usbPath + "/pqbin/gamma0.ini");
				destFile = new File(destPath);
				try {
					if (copyFile(srcFile, destFile)) {
						ret = true;
					}
					chmodFile(destFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				factoryManager.updatePqIniFiles();
				Log.i("OtherOption", ".......updatePqIniFiles OK!!!!!!!!!!..........");
			} else {
				textview_upgrade_pq.setText(mUpgradeActivity.getString(R.string.str_upgrade_pq_no_file));
			}
		}
	}

	private void chmodFile(File destFile) {
		try {
			String command = "chmod 666 " + destFile.getAbsolutePath();
			Log.i("OtherOptionAdjustViewHolder", "command = " + command);
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(command);
		} catch (IOException e) {
			Log.i("OtherOptionAdjustViewHolder", "chmod fail!!!!");
			e.printStackTrace();
		}
	}

	private boolean copyFile(File srcFile, File destFile) throws IOException {
		if (!srcFile.exists()) {
			Log.i("OtherOption", "~src file not exits~");
			return false;
		}
		if (!srcFile.isFile()) {
			Log.i("OtherOption", "~src file is not a file~");
			return false;
		}
		if (!srcFile.canRead()) {
			Log.i("OtherOption", "~src file can  not read~");
			return false;
		}
		if (!destFile.exists()) {
			Log.i("OtherOption", "~dest file not exits~");
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			destFile.createNewFile();
		}
		if (!destFile.canRead()) {
			Log.i("OtherOption", "~dest file can  not read~");
			return false;
		}
		Log.i("OtherOption", "~src file OK~");
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
		// chmodFile(destFile);
		Log.i("OtherOption", "~chmod dest file OK~");
		return true;
	}
}
