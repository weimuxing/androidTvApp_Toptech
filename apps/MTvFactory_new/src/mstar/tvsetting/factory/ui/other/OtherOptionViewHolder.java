//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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

package mstar.tvsetting.factory.ui.other;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.ProgramInfo;

import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.FactoryDB;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.FactoryIntent;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;
import android.content.Intent;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.storage.MStorageManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvAudioManager;

@SuppressWarnings("unused")
public class OtherOptionViewHolder {

	private final static String TAG = "OtherOptionAdjustViewHolder";

	private DesignMenuActivity otheroptionActivity;

	private IFactoryDesk factoryManager;

	private MStorageManager stm;

	protected TextView text_factory_mountconfig_val;

	protected TextView text_factory_pq_table_update;

	protected LinearLayout linearlayout_otheroption_ursa_test;

	protected LinearLayout linearlayout_otheroption_ursa_info;

	protected LinearLayout linearlayout_otheroption_ip_enable_mapping;

	protected LinearLayout linearLayout_otheroption_execute_shell;

	protected LinearLayout linearlayout_otheroption_ci_setting;

	protected LinearLayout linearlayout_otheroption_enablesnapshotcreation;

	protected LinearLayout linearlayout_otheroption_learnmode;

	protected TextView text_factory_otheroption_watchdog_val;

	protected TextView text_factory_otheroption_str_enable_val;

	protected LinearLayout linearlayout_factory_otheroption_mstv_tool;

	protected TextView text_factory_otheroption_mstv_tool;

	protected TextView text_factory_otheroption_wake_on_lan_val;

	private EditText editText;

	private Builder editDialog;

	private String[] mountConfigStrs;

	private String[] pqTableUpdateStrs;

	protected int mountConfigIndex = 0;

	protected int pqTableUpdateIndex = 0;

	private static final int MOUNTCONFIGNUM = 2;

	private static final int PQTABLEUPDATENUM = 4;

	private int watchdogindex = 0;

	private int wakeonlanindex = 0;

	private int enableSTRidex = 0;

	private String[] watchdogenable;

	private String[] dtvavdelayenable;

	private String[] wakeonlan;

	private String[] enableSTR;

	private TvManager tvManager;

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			int currentid = otheroptionActivity.getCurrentFocus().getId();
			switch (currentid) {
			case R.id.linearlayout_other_option_ursa_test:
				Intent ursaIntent = new Intent(otheroptionActivity, UrsaTestActivity.class);
				otheroptionActivity.startActivity(ursaIntent);
				break;
			case R.id.linearlayout_other_option_ursa_info:
				otheroptionActivity.gotoPage(otheroptionActivity.URSA_INFO_PAGE);
				break;
			case R.id.linearlayout_other_option_ip_enable_mapping:
				otheroptionActivity.gotoPage(otheroptionActivity.IP_ENABLE_MAPPING_PAGE);
				break;
			case R.id.linearlayout_other_option_uart_debug:
				if (factoryManager.enableUartDebug()) {
					text_factory_otheroption_mstv_tool
							.setText(otheroptionActivity.getResources().getString(R.string.str_uart_debug_success));
				} else {
					text_factory_otheroption_mstv_tool
							.setText(otheroptionActivity.getResources().getString(R.string.str_uart_debug_failed));
				}
				break;
			case R.id.linearlayout_other_option_execute_shell:
				Intent executeIntent = new Intent(otheroptionActivity, ExecuteShellActivity.class);
				otheroptionActivity.startActivity(executeIntent);
				break;
			case R.id.linearlayout_other_option_ci_setting:
				otheroptionActivity.gotoPage(otheroptionActivity.CI_SETTING_PAGE);
				break;
			case R.id.linearlayout_other_option_snapshot_creation:
				Intent snapshotIntent = new Intent();
				snapshotIntent.setAction(FactoryIntent.ACTION_FACTORY_FALCON_QUICK_BOOT_SNAPSHOT);
				otheroptionActivity.startActivity(snapshotIntent);
				break;
			case R.id.linearlayout_other_option_learn_mode:
				Intent learnIntent = new Intent();
				learnIntent.setAction(FactoryIntent.ACTION_FACTORY_FALCON_QUICK_BOOT_LEARNING);
				otheroptionActivity.startActivity(learnIntent);
				break;
			default:
				break;
			}
		}
	};

	private TvFactoryManager tvFactoryManager;

	private TvCommonManager mTvCommonManager;

	private void registerListeners() {
		linearlayout_otheroption_ursa_test.setOnClickListener(listener);
		linearlayout_otheroption_ursa_info.setOnClickListener(listener);
		linearlayout_otheroption_ip_enable_mapping.setOnClickListener(listener);
		linearLayout_otheroption_execute_shell.setOnClickListener(listener);
		linearlayout_factory_otheroption_mstv_tool.setOnClickListener(listener);
		linearlayout_otheroption_ci_setting.setOnClickListener(listener);
		linearlayout_otheroption_enablesnapshotcreation.setOnClickListener(listener);
		linearlayout_otheroption_learnmode.setOnClickListener(listener);
	}

	public OtherOptionViewHolder(DesignMenuActivity mstarActivity, IFactoryDesk factoryManager) {
		otheroptionActivity = mstarActivity;
		this.factoryManager = factoryManager;
		tvManager = TvManager.getInstance();
	}

	public void findView() {

		text_factory_mountconfig_val = (TextView) otheroptionActivity
				.findViewById(R.id.textview_other_option_mountconfig2);

		text_factory_pq_table_update = (TextView) otheroptionActivity
				.findViewById(R.id.textview_other_option_pq_table_update2);

		linearlayout_otheroption_ursa_test = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_ursa_test);

		linearlayout_otheroption_ursa_info = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_ursa_info);

		linearlayout_otheroption_ip_enable_mapping = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_ip_enable_mapping);

		text_factory_otheroption_watchdog_val = (TextView) otheroptionActivity
				.findViewById(R.id.textview_other_option_watch_dog2);

		linearlayout_factory_otheroption_mstv_tool = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_uart_debug);

		linearLayout_otheroption_execute_shell = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_execute_shell);

		linearlayout_otheroption_ci_setting = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_ci_setting);

		linearlayout_otheroption_enablesnapshotcreation = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_snapshot_creation);

		linearlayout_otheroption_learnmode = (LinearLayout) otheroptionActivity
				.findViewById(R.id.linearlayout_other_option_learn_mode);

		text_factory_otheroption_mstv_tool = (TextView) otheroptionActivity
				.findViewById(R.id.textview_other_option_uart_debug2);

		text_factory_otheroption_str_enable_val = (TextView) otheroptionActivity
				.findViewById(R.id.textview_other_option_enable_str2);

		text_factory_otheroption_wake_on_lan_val = (TextView) otheroptionActivity
				.findViewById(R.id.textview_other_option_wake_on_lan2);

		mountConfigStrs = otheroptionActivity.getResources().getStringArray(R.array.str_factory_mountconfig_val);
		pqTableUpdateStrs = otheroptionActivity.getResources().getStringArray(R.array.str_factory_pqtableupdate_val);

		text_factory_mountconfig_val.setText(mountConfigStrs[mountConfigIndex]);
		text_factory_pq_table_update.setText(pqTableUpdateStrs[pqTableUpdateIndex]);
	}

	public boolean onCreate() {

		watchdogenable = otheroptionActivity.getResources().getStringArray(R.array.str_arr_other_watch_dog);
		dtvavdelayenable = otheroptionActivity.getResources().getStringArray(R.array.str_arr_on_off);
		wakeonlan = otheroptionActivity.getResources().getStringArray(R.array.str_arr_other_wake_on_lan);
		enableSTR = otheroptionActivity.getResources().getStringArray(R.array.str_arr_on_off);

		watchdogindex = factoryManager.getWatchDogMode();
		wakeonlanindex = factoryManager.getWOLEnableStatus() ? 1 : 0;
		text_factory_otheroption_watchdog_val.setText(watchdogenable[watchdogindex]);
		enableSTRidex = FactoryDB.getInstance(otheroptionActivity).queryEnableSTR();

		if (enableSTRidex <= 1) {
			text_factory_otheroption_str_enable_val.setText(enableSTR[enableSTRidex]);
		} else {
			text_factory_otheroption_str_enable_val.setText(enableSTR[1] + "  " + enableSTRidex
					+ otheroptionActivity.getString(R.string.str_other_option_enable_str_times));
		}

		text_factory_otheroption_wake_on_lan_val.setText(wakeonlan[wakeonlanindex]);
		registerListeners();
		return true;
	}

	private void showEditDialog() {
		if (editDialog == null) {
			editDialog = new AlertDialog.Builder(otheroptionActivity);
			editDialog.setTitle(otheroptionActivity.getString(R.string.str_other_option_enable_str_title));
			editDialog.setIcon(android.R.drawable.ic_dialog_info);
			editDialog.setPositiveButton(otheroptionActivity.getString(R.string.str_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							if (editText.getText().toString().isEmpty()) {
								enableSTRidex = 1;
							} else {
								enableSTRidex = Integer.parseInt(editText.getText().toString());
							}
							if (enableSTRidex <= 1 && enableSTRidex >= 0) {
								text_factory_otheroption_str_enable_val.setText(enableSTR[enableSTRidex]);
								if (enableSTRidex == 0) {
									try {
										tvManager.setEnvironment("str_crc", "2");
										Log.d("Environment", "Environment=" + tvManager.getEnvironment("str_crc"));
									} catch (TvCommonException e) {
										e.printStackTrace();
									}
								} else {
									try {
										tvManager.setEnvironment("str_crc", "1");
										Log.d("Environment", "Environment=" + tvManager.getEnvironment("str_crc"));
									} catch (TvCommonException e) {
										e.printStackTrace();
									}
								}
								setSTR(enableSTRidex);
							} else if (enableSTRidex < 0) {
								Toast.makeText(otheroptionActivity,
										otheroptionActivity.getString(R.string.str_input_invalite), Toast.LENGTH_SHORT)
										.show();
							} else {
								try {
									tvManager.setEnvironment("str_crc", "1");
									Log.d("Environment", "Environment=" + tvManager.getEnvironment("str_crc"));
								} catch (TvCommonException e) {
									e.printStackTrace();
								}
								text_factory_otheroption_str_enable_val.setText(enableSTR[1] + "  " + enableSTRidex
										+ otheroptionActivity.getString(R.string.str_other_option_enable_str_times));
								setSTR(enableSTRidex);
							}
						}
					});
			editDialog.setNegativeButton(otheroptionActivity.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							enableSTRidex = 1;
							text_factory_otheroption_str_enable_val.setText(enableSTR[enableSTRidex]);
							setSTR(enableSTRidex);

						}
					});
		}
		editText = new EditText(otheroptionActivity);
		editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		editDialog.setView(editText);
		editDialog.show();
	}

	private void setSTR(int times) {
		FactoryDB.getInstance(otheroptionActivity).updateEnableSTR(enableSTRidex);
		try {
			TvManager.getInstance().sendStrCommand(TvManager.STR_CMD_SET_MAX_CNT, times, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentid = otheroptionActivity.getCurrentFocus().getId();
		String str_val = new String();
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentid) {
			case R.id.linearlayout_other_optioon_mountconfig:
				mountConfigIndex = (mountConfigIndex + 1) % MOUNTCONFIGNUM;
				text_factory_mountconfig_val.setText(mountConfigStrs[mountConfigIndex]);
				break;
			case R.id.linearlayout_other_option_pq_table_update:
				pqTableUpdateIndex = (pqTableUpdateIndex + 1) % PQTABLEUPDATENUM;
				text_factory_pq_table_update.setText(pqTableUpdateStrs[pqTableUpdateIndex]);
				break;
			case R.id.linearlayout_other_option_watch_dog:
				if (watchdogindex != 1)
					watchdogindex++;
				else
					watchdogindex = 0;
				text_factory_otheroption_watchdog_val.setText(watchdogenable[watchdogindex]);
				factoryManager.setWatchDogMode((short) (watchdogindex));
				break;
			case R.id.linearlayout_other_option_enable_str:
				if (enableSTRidex == 0) {
					showEditDialog();
				}
				break;
			case R.id.linearlayout_other_option_wake_on_lan:
				if (wakeonlanindex != 1)
					wakeonlanindex++;
				else
					wakeonlanindex = 0;
				text_factory_otheroption_wake_on_lan_val.setText(wakeonlan[wakeonlanindex]);
				factoryManager.setWOLEnableStatus(wakeonlanindex == 1);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentid) {
			case R.id.linearlayout_other_optioon_mountconfig:
				if (mountConfigIndex != 0)
					mountConfigIndex--;
				else
					mountConfigIndex = MOUNTCONFIGNUM - 1;
				text_factory_mountconfig_val.setText(mountConfigStrs[mountConfigIndex]);
				break;
			case R.id.linearlayout_other_option_pq_table_update:
				if (pqTableUpdateIndex != 0)
					pqTableUpdateIndex--;
				else
					pqTableUpdateIndex = PQTABLEUPDATENUM - 1;
				text_factory_pq_table_update.setText(pqTableUpdateStrs[pqTableUpdateIndex]);
				break;
			case R.id.linearlayout_other_option_watch_dog:
				if (watchdogindex != 0)
					watchdogindex--;
				else
					watchdogindex = 1;
				text_factory_otheroption_watchdog_val.setText(watchdogenable[watchdogindex]);
				factoryManager.setWatchDogMode((short) (watchdogindex));
				break;
			case R.id.linearlayout_other_option_enable_str:
				if (enableSTRidex != 0) {
					enableSTRidex = 0;
					text_factory_otheroption_str_enable_val.setText(enableSTR[enableSTRidex]);
					try {
						tvManager.setEnvironment("str_crc", "2");
						Log.d("Environment", "Environment=" + tvManager.getEnvironment("str_crc"));
					} catch (TvCommonException e) {
						e.printStackTrace();
					}
					setSTR(enableSTRidex);
				}
				break;
			case R.id.linearlayout_other_option_wake_on_lan:
				if (wakeonlanindex != 0)
					wakeonlanindex--;
				else
					wakeonlanindex = 1;
				text_factory_otheroption_wake_on_lan_val.setText(wakeonlan[wakeonlanindex]);
				factoryManager.setWOLEnableStatus(wakeonlanindex == 1);
				break;
			default:
				break;
			}
			break;
		default:
			bRet = false;
			break;
		}
		return bRet;
	}

}
