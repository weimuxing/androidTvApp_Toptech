//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

package mstar.tvsetting.factory.ui.designmenu;

import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.MarqueeTextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.android.server.NetworkManagementSocketTagger;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tvapi.dtv.vo.DtvDemodVersion;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.SystemProperties;

public class DesignMenuViewHolder {
	private DesignMenuActivity factoryactivity;

	protected LinearLayout linear_factory_picture;

	protected LinearLayout linear_factory_sound;

	protected LinearLayout linear_factory_panel_info;

	protected LinearLayout linear_factory_user;

	protected LinearLayout linear_factory_upgrade;

	protected LinearLayout linear_factory_other_option;

	protected LinearLayout linear_factory_reset;

	protected LinearLayout linear_factory_name;

	protected LinearLayout linear_factory_build;

	protected LinearLayout linear_factory_changelist;

	private MarqueeTextView text_factory_name;

	private MarqueeTextView text_factory_build;

	private MarqueeTextView text_factory_changelist;

	protected MarqueeTextView text_factory_board;

	protected TextView text_factory_panel;

	protected TextView text_factory_compile_time;

	protected TextView plaName;

	protected TextView cusName;

	protected TextView proName;

	protected TextView preVersion;

	private String str_board = new String();

	private String str_panel = new String();

	private String str_compile_time = new String();

	private String str_date = new String();

	private String str_time = new String();

	private String str_name;

	private String str_build;

	private String str_changelist;

	private TvFactoryManager mTvFactoryManager = null;

	public DesignMenuViewHolder(DesignMenuActivity activity) {
		factoryactivity = activity;
	}

	void findView() {
		linear_factory_picture = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_picture);
		linear_factory_sound = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_sound);
		linear_factory_panel_info = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_panel_Info);
		linear_factory_user = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_User);
		linear_factory_upgrade = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_Upgrade);
		linear_factory_other_option = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_Other);
		linear_factory_reset = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_Reset);
		linear_factory_name = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_Name);
		linear_factory_build = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_Build);
		linear_factory_changelist = (LinearLayout) factoryactivity.findViewById(R.id.linearlayout_factory_Changelist);

		text_factory_name = (MarqueeTextView) factoryactivity.findViewById(R.id.textview_factory_Name2);
		text_factory_build = (MarqueeTextView) factoryactivity.findViewById(R.id.textview_factory_Build2);
		text_factory_changelist = (MarqueeTextView) factoryactivity.findViewById(R.id.textview_factory_Changelist2);
		text_factory_board = (MarqueeTextView) factoryactivity.findViewById(R.id.textview_factory_Board2);
		text_factory_panel = (TextView) factoryactivity.findViewById(R.id.textview_factory_Panel2);
		text_factory_compile_time = (TextView) factoryactivity.findViewById(R.id.textview_factory_Compile_time2);

		plaName = (TextView) factoryactivity.findViewById(R.id.factory_ota_info_praname_val);
		cusName = (TextView) factoryactivity.findViewById(R.id.factory_ota_info_cusname_val);
		proName = (TextView) factoryactivity.findViewById(R.id.factory_ota_info_proname_val);
		preVersion = (TextView) factoryactivity.findViewById(R.id.factory_ota_info_preversion_val);

		mTvFactoryManager = TvFactoryManager.getInstance();
		int curDtvRoute = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
		int curDtvDemodType = TvFactoryManager.DEMOD_DVB_C;
		switch (curDtvRoute) {
		case 0:
			curDtvDemodType = TvFactoryManager.DEMOD_DTMB;
			break;
		case 1:
			curDtvDemodType = TvFactoryManager.DEMOD_DVB_C;
			break;
		case 2:
			curDtvDemodType = TvFactoryManager.DEMOD_DVB_T;
			break;
		default:
			break;
		}
		DtvDemodVersion dtvDemodVersion = new DtvDemodVersion();
		dtvDemodVersion = mTvFactoryManager.getDtvDemodVersion(curDtvDemodType);
		str_name = new String(dtvDemodVersion.name);
		if ("".equals(str_name) || str_name == null) {
			linear_factory_name.setVisibility(View.GONE);
		} else {
			text_factory_name.setText(str_name);
		}
		str_build = new String(dtvDemodVersion.build);
		if ("".equals(str_build) || str_build == null) {
			linear_factory_build.setVisibility(View.GONE);
		} else {
			text_factory_build.setText(str_build);
		}
		str_changelist = new String(dtvDemodVersion.changelist);
		if ("".equals(str_changelist) || str_changelist == null) {
			linear_factory_changelist.setVisibility(View.GONE);
		} else {
			text_factory_changelist.setText(str_changelist);
		}

		str_board = mTvFactoryManager.getBoardType();
		str_panel = mTvFactoryManager.getPanelType();
		Long timestamp = Long.parseLong(SystemProperties.get("ro.build.date.utc", "1420008801")) * 1000;
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		String date = df.format(new java.util.Date(timestamp));
		String[] date_string = date.split(" ");
		str_date = date_string[1];
		str_time = date_string[2];
		str_compile_time = str_date + " " + str_time;
		text_factory_board.setText(str_board);
		text_factory_panel.setText(str_panel);
		text_factory_compile_time.setText(str_compile_time);

		plaName.setText(SystemProperties.get("ro.build.id", "unknow"));
		cusName.setText(SystemProperties.get("ro.build.cus.id", "unknow"));
		proName.setText(SystemProperties.get("ro.build.cus.pro.id", "unknow"));
		preVersion.setText(SystemProperties.get("ro.build.version.incremental", "unknow"));
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int currentid = -1;
		View view = factoryactivity.getCurrentFocus();
		if (null != view) {
			currentid = view.getId();
		}
		return false;
	}
}
