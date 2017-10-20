//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

package mstar.tvsetting.factory.ui.picture;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.FactoryDeskImpl;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.desk.IFactoryDesk.EN_MS_COLOR_TEMP;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

public class WBAdjustViewHolder {
	private static final String TAG = "WBAdjustViewHolder";

	private DesignMenuActivity wbActivity;

	private TvCommonManager mTvCommonmanager = TvCommonManager.getInstance();

	private IFactoryDesk factoryManager;

	protected TextView text_factory_wbadjust_source_val;

	protected TextView text_factory_wbadjust_rgain_val;

	protected TextView text_factory_wbadjust_ggain_val;

	protected TextView text_factory_wbadjust_bgain_val;

	protected TextView text_factory_wbadjust_roffset_val;

	protected TextView text_factory_wbadjust_goffset_val;

	protected TextView text_factory_wbadjust_boffset_val;

	protected TextView text_factory_wbadjust_colortemp_val;

	protected ProgressBar progress_factory_wbadjust_rgain;

	protected ProgressBar progress_factory_wbadjust_ggain;

	protected ProgressBar progress_factory_wbadjust_bgain;

	protected ProgressBar progress_factory_wbadjust_roffset;

	protected ProgressBar progress_factory_wbadjust_goffset;

	protected ProgressBar progress_factory_wbadjust_boffset;

	private int sourceindexWB = 0;

	private int currentSourceindex = 0;

	private int clortempindex = 0;

	private int rgainvalWB = 1024;

	private int ggainvalWB = 1024;

	private int bgainvalWB = 1024;

	private final int gainDisplayDivideWB = 8;// display:256=2048/8,step=8

	private final int gainStepWB = 8;// display:256=2048/8,step=8

	private static final int FUNCTION_DISABLED = 0;

	private int roffsetvalWB = 1024;

	private int goffsetvalWB = 1024;

	private int boffsetvalWB = 1024;

	private final int offsetDisplayDivideWB = 1;// Bar_step=1

	private final int offsetStepWB = 1;// Reg_step=1

	private int[] SourceListFlag = new int[TvCommonManager.INPUT_SOURCE_NUM];

	private int[] sourceType;

	int currentType = 0;

	private int WB_ADJUST_GAIN_MAX = 2047;

	private int WB_ADJUST_GAIN_MIN = 0;

	private int WB_ADJUST_OFFSET_MAX = 2047;

	private int WB_ADJUST_OFFSET_MIN = 0;

	// FIXME: move to resource file
	private static String[] sourcearrayWB;

	private static final int TYPE_AV = 0;
	private static final int TYPE_HDMI = 1;

	private static final int[] mSourceListInvisible = { TvCommonManager.INPUT_SOURCE_STORAGE,
			TvCommonManager.INPUT_SOURCE_KTV, TvCommonManager.INPUT_SOURCE_JPEG, TvCommonManager.INPUT_SOURCE_DTV2,
			TvCommonManager.INPUT_SOURCE_STORAGE2, TvCommonManager.INPUT_SOURCE_DIV3,
			TvCommonManager.INPUT_SOURCE_SCALER_OP, TvCommonManager.INPUT_SOURCE_RUV };

	private String[] colortemparray;

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		};
	};

	public WBAdjustViewHolder(DesignMenuActivity mstarActivity, IFactoryDesk factoryManager) {
		this.wbActivity = mstarActivity;
		this.factoryManager = factoryManager;
		factoryManager.setHandler(handler, 1);
	}

	public void findView() {
		text_factory_wbadjust_source_val = (TextView) wbActivity
				.findViewById(R.id.textview_factory_wbadjust_source_val);
		text_factory_wbadjust_rgain_val = (TextView) wbActivity.findViewById(R.id.textview_factory_wbadjust_val);
		text_factory_wbadjust_ggain_val = (TextView) wbActivity.findViewById(R.id.textview_factory_wbadjust_ggain_val);
		text_factory_wbadjust_bgain_val = (TextView) wbActivity.findViewById(R.id.textview_factory_wbadjust_bgain_val);
		text_factory_wbadjust_roffset_val = (TextView) wbActivity
				.findViewById(R.id.textview_factory_wbadjust_roffset_val);
		text_factory_wbadjust_goffset_val = (TextView) wbActivity
				.findViewById(R.id.textview_factory_wbadjust_goffset_val);
		text_factory_wbadjust_boffset_val = (TextView) wbActivity
				.findViewById(R.id.textview_factory_wbadjust_boffset_val);
		text_factory_wbadjust_colortemp_val = (TextView) wbActivity
				.findViewById(R.id.textview_factory_wbadjust_colortemp_val);
		progress_factory_wbadjust_rgain = (ProgressBar) wbActivity
				.findViewById(R.id.progressbar_facroty_wbadjust_rgain);
		progress_factory_wbadjust_ggain = (ProgressBar) wbActivity
				.findViewById(R.id.progressbar_facroty_wbadjust_ggain);
		progress_factory_wbadjust_bgain = (ProgressBar) wbActivity
				.findViewById(R.id.progressbar_facroty_wbadjust_bgain);
		progress_factory_wbadjust_roffset = (ProgressBar) wbActivity
				.findViewById(R.id.progressbar_facroty_wbadjust_roffset);
		progress_factory_wbadjust_goffset = (ProgressBar) wbActivity
				.findViewById(R.id.progressbar_facroty_wbadjust_goffset);
		progress_factory_wbadjust_boffset = (ProgressBar) wbActivity
				.findViewById(R.id.progressbar_facroty_wbadjust_boffset);
	}

	public void freshOffer() {
		rgainvalWB = factoryManager.getWbRedGain();
		ggainvalWB = factoryManager.getWbGreenGain();
		bgainvalWB = factoryManager.getWbBlueGain();
		roffsetvalWB = factoryManager.getWbRedOffset();
		goffsetvalWB = factoryManager.getWbGreenOffset();
		boffsetvalWB = factoryManager.getWbBlueOffset();
		if (factoryManager != null) {
			clortempindex = factoryManager.getColorTmpIdx();
			Log.v(TAG, "-freshOffer----clortempindex----" + clortempindex);
		}
		text_factory_wbadjust_rgain_val.setText(Integer.toString(rgainvalWB / gainDisplayDivideWB));
		text_factory_wbadjust_ggain_val.setText(Integer.toString(ggainvalWB / gainDisplayDivideWB));
		text_factory_wbadjust_bgain_val.setText(Integer.toString(bgainvalWB / gainDisplayDivideWB));
		text_factory_wbadjust_roffset_val.setText(Integer.toString(roffsetvalWB / offsetDisplayDivideWB));
		text_factory_wbadjust_goffset_val.setText(Integer.toString(goffsetvalWB / offsetDisplayDivideWB));
		text_factory_wbadjust_boffset_val.setText(Integer.toString(boffsetvalWB / offsetDisplayDivideWB));
		text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
	}

	public boolean onCreate() {
		sourcearrayWB = wbActivity.getResources().getStringArray(R.array.str_arr_picture_white_balance_vals);
		colortemparray = wbActivity.getResources().getStringArray(R.array.str_arr_picture_wb_colortemparray_vals);
		rgainvalWB = factoryManager.getWbRedGain();
		ggainvalWB = factoryManager.getWbGreenGain();
		bgainvalWB = factoryManager.getWbBlueGain();
		roffsetvalWB = factoryManager.getWbRedOffset();
		goffsetvalWB = factoryManager.getWbGreenOffset();
		boffsetvalWB = factoryManager.getWbBlueOffset();
		sourceindexWB = factoryManager.getWBIdx().ordinal();
		if (factoryManager != null) {
			clortempindex = factoryManager.getColorTmpIdx();
		}
		getSupportSourcelist();
		text_factory_wbadjust_rgain_val.setText(Integer.toString(rgainvalWB / gainDisplayDivideWB));
		text_factory_wbadjust_ggain_val.setText(Integer.toString(ggainvalWB / gainDisplayDivideWB));
		text_factory_wbadjust_bgain_val.setText(Integer.toString(bgainvalWB / gainDisplayDivideWB));
		text_factory_wbadjust_roffset_val.setText(Integer.toString(roffsetvalWB / offsetDisplayDivideWB));
		text_factory_wbadjust_goffset_val.setText(Integer.toString(goffsetvalWB / offsetDisplayDivideWB));
		text_factory_wbadjust_boffset_val.setText(Integer.toString(boffsetvalWB / offsetDisplayDivideWB));
		progress_factory_wbadjust_rgain.setProgress(rgainvalWB);
		progress_factory_wbadjust_ggain.setProgress(ggainvalWB);
		progress_factory_wbadjust_bgain.setProgress(bgainvalWB);
		progress_factory_wbadjust_roffset.setProgress(roffsetvalWB);
		progress_factory_wbadjust_goffset.setProgress(goffsetvalWB);
		progress_factory_wbadjust_boffset.setProgress(boffsetvalWB);
		text_factory_wbadjust_source_val.setText(sourcearrayWB[sourceindexWB]);
		text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
		// DisableSourceLinear();
		return true;
	}

	private void getSupportSourcelist() {
		int[] sourceList;
		int j = 0;
		sourceList = mTvCommonmanager.getSourceList();
		if (sourceList != null) {
			int[] count = new int[2];
			for (int i = 0; i < count.length; i++) {
				count[i] = 0;
			}
			for (int i = 0; (i < SourceListFlag.length) && (i < sourceList.length); i++) {
				SourceListFlag[i] = sourceList[i];
				if (SourceListFlag[i] > 0) {
					j++;
					switch (i) {
					case TvCommonManager.INPUT_SOURCE_CVBS:
					case TvCommonManager.INPUT_SOURCE_CVBS2:
					case TvCommonManager.INPUT_SOURCE_CVBS3:
					case TvCommonManager.INPUT_SOURCE_CVBS4:
					case TvCommonManager.INPUT_SOURCE_CVBS5:
					case TvCommonManager.INPUT_SOURCE_CVBS6:
					case TvCommonManager.INPUT_SOURCE_CVBS7:
					case TvCommonManager.INPUT_SOURCE_CVBS8:
						count[TYPE_AV] += 1;
						break;
					case TvCommonManager.INPUT_SOURCE_HDMI:
					case TvCommonManager.INPUT_SOURCE_HDMI2:
					case TvCommonManager.INPUT_SOURCE_HDMI3:
					case TvCommonManager.INPUT_SOURCE_HDMI4:
						count[TYPE_HDMI] += 1;
						break;
					}
				}
			}
			sourcearrayWB[TvCommonManager.INPUT_SOURCE_CVBS] = count[TYPE_AV] == 1
					? wbActivity.getResources().getString(R.string.picture_white_balance_AV_vals)
					: wbActivity.getResources().getString(R.string.picture_white_balance_AV1_vals);
			sourcearrayWB[TvCommonManager.INPUT_SOURCE_HDMI] = count[TYPE_HDMI] == 1
					? wbActivity.getResources().getString(R.string.picture_white_balance_HDMI_vals)
					: wbActivity.getResources().getString(R.string.picture_white_balance_HDMI1_vals);
			for (int i = 0; (i < mSourceListInvisible.length) && (i < SourceListFlag.length); i++) {
				if (TvCommonManager.INPUT_SOURCE_NUM > mSourceListInvisible[i]) {
					SourceListFlag[mSourceListInvisible[i]] = FUNCTION_DISABLED;
					if (mSourceListInvisible[i] < SourceListFlag.length
							&& mSourceListInvisible[i] < sourceList.length) {
						j--;
					}
				}
			}

			sourceType = new int[j];
			j = 0;
			for (int i = 0; (i < SourceListFlag.length) && (i < sourceList.length); i++) {
				if (SourceListFlag[i] > 0) {
					sourceType[j] = i;
					if (sourceType[j] == sourceindexWB) {
						currentSourceindex = j;
					}
					j++;
				}
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentid = wbActivity.getCurrentFocus().getId();
		String str_val = new String();
		int maxValue;
		int curSourceValue;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentid) {
			case R.id.linearlayout_factory_wbadjust_source:
				maxValue = sourceType.length;
				currentSourceindex++;
				if (currentSourceindex > maxValue - 1) {
					currentSourceindex = 0;
				}
				factoryManager.execSetInputSource(EnumInputSource.values()[sourceType[currentSourceindex]]);
				factoryManager.setWBIdx(EnumInputSource.values()[sourceType[currentSourceindex]]);
				onCreate();
				if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_ATV.ordinal()) {
					factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
					int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
				} else if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_DTV.ordinal()) {
					factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_DTV);
					TvChannelManager.getInstance().playDtvCurrentProgram();
				}
				break;
			case R.id.linearlayout_factory_wbadjust_colortemp:
				maxValue = EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal();
				if (clortempindex < (maxValue - 3))
					clortempindex++;
				else
					clortempindex = 0;

				text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
				if (factoryManager != null) {
					factoryManager.setColorTmpIdx(EN_MS_COLOR_TEMP.values()[clortempindex]);
				}
				onCreate();
				break;
			case R.id.linearlayout_factory_wbadjust_rgain:
				if (rgainvalWB < WB_ADJUST_GAIN_MAX) {
					rgainvalWB = rgainvalWB + gainStepWB;

					if (rgainvalWB > WB_ADJUST_GAIN_MAX)
						rgainvalWB = WB_ADJUST_GAIN_MAX;
				} else {
					rgainvalWB = WB_ADJUST_GAIN_MIN;
				}
				str_val = Integer.toString(rgainvalWB / gainDisplayDivideWB);
				text_factory_wbadjust_rgain_val.setText(str_val);
				progress_factory_wbadjust_rgain.setProgress(rgainvalWB);
				factoryManager.setWbRedGain((short) rgainvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_ggain:
				if (ggainvalWB < WB_ADJUST_GAIN_MAX) {
					ggainvalWB = ggainvalWB + gainStepWB;

					if (ggainvalWB > WB_ADJUST_GAIN_MAX)
						ggainvalWB = WB_ADJUST_GAIN_MAX;
				} else {
					ggainvalWB = WB_ADJUST_GAIN_MIN;
				}
				str_val = Integer.toString(ggainvalWB / gainDisplayDivideWB);
				text_factory_wbadjust_ggain_val.setText(str_val);
				progress_factory_wbadjust_ggain.setProgress(ggainvalWB);
				factoryManager.setWbGreenGain((short) ggainvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_bgain:
				if (bgainvalWB < WB_ADJUST_GAIN_MAX) {
					bgainvalWB = bgainvalWB + gainStepWB;

					if (bgainvalWB > WB_ADJUST_GAIN_MAX)
						bgainvalWB = WB_ADJUST_GAIN_MAX;
				} else {
					bgainvalWB = WB_ADJUST_GAIN_MIN;
				}
				str_val = Integer.toString(bgainvalWB / gainDisplayDivideWB);
				text_factory_wbadjust_bgain_val.setText(str_val);
				progress_factory_wbadjust_bgain.setProgress(bgainvalWB);
				factoryManager.setWbBlueGain((short) bgainvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_roffset:
				if (roffsetvalWB < WB_ADJUST_OFFSET_MAX) {
					roffsetvalWB = roffsetvalWB + offsetStepWB;

					if (roffsetvalWB > WB_ADJUST_OFFSET_MAX)
						roffsetvalWB = WB_ADJUST_OFFSET_MAX;
				} else {
					roffsetvalWB = WB_ADJUST_OFFSET_MIN;
				}
				str_val = Integer.toString(roffsetvalWB / offsetDisplayDivideWB);
				text_factory_wbadjust_roffset_val.setText(str_val);
				progress_factory_wbadjust_roffset.setProgress(roffsetvalWB);
				factoryManager.setWbRedOffset((short) roffsetvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_goffset:
				if (goffsetvalWB < WB_ADJUST_OFFSET_MAX) {
					goffsetvalWB = goffsetvalWB + offsetStepWB;

					if (goffsetvalWB > WB_ADJUST_OFFSET_MAX)
						goffsetvalWB = WB_ADJUST_OFFSET_MAX;
				} else {
					goffsetvalWB = WB_ADJUST_OFFSET_MIN;
				}
				str_val = Integer.toString(goffsetvalWB / offsetDisplayDivideWB);
				text_factory_wbadjust_goffset_val.setText(str_val);
				progress_factory_wbadjust_goffset.setProgress(goffsetvalWB);
				factoryManager.setWbGreenOffset((short) goffsetvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_boffset:
				if (boffsetvalWB < WB_ADJUST_OFFSET_MAX) {
					boffsetvalWB = boffsetvalWB + offsetStepWB;

					if (boffsetvalWB > WB_ADJUST_OFFSET_MAX)
						boffsetvalWB = WB_ADJUST_OFFSET_MAX;
				} else {
					boffsetvalWB = WB_ADJUST_OFFSET_MIN;
				}
				str_val = Integer.toString(boffsetvalWB / offsetDisplayDivideWB);
				text_factory_wbadjust_boffset_val.setText(str_val);
				progress_factory_wbadjust_boffset.setProgress(boffsetvalWB);
				factoryManager.setWbBlueOffset((short) boffsetvalWB);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentid) {
			case R.id.linearlayout_factory_wbadjust_source:
				maxValue = sourceType.length;
				currentSourceindex--;
				if (currentSourceindex < 0) {
					currentSourceindex = maxValue - 1;
				}
				factoryManager.execSetInputSource(EnumInputSource.values()[sourceType[currentSourceindex]]);
				factoryManager.setWBIdx(EnumInputSource.values()[sourceType[currentSourceindex]]);
				factoryManager.changeWBParaWhenSourceChange();
				onCreate();
				if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_ATV.ordinal()) {
					int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
				} else if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_DTV.ordinal()) {
					TvChannelManager.getInstance().playDtvCurrentProgram();
				}
				break;
			case R.id.linearlayout_factory_wbadjust_colortemp:
				maxValue = EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal();
				if (clortempindex > 0)
					clortempindex--;
				else
					clortempindex = maxValue - 3;
				text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
				if (factoryManager != null) {
					factoryManager.setColorTmpIdx(EN_MS_COLOR_TEMP.values()[clortempindex]);
				}
				onCreate();
				break;
			case R.id.linearlayout_factory_wbadjust_rgain:
				if (rgainvalWB > WB_ADJUST_GAIN_MIN) {
					rgainvalWB = rgainvalWB - gainStepWB;

					if (rgainvalWB < WB_ADJUST_GAIN_MIN)
						rgainvalWB = WB_ADJUST_GAIN_MIN;
				} else {
					rgainvalWB = WB_ADJUST_GAIN_MAX;
				}
				str_val = Integer.toString(rgainvalWB / gainDisplayDivideWB);
				text_factory_wbadjust_rgain_val.setText(str_val);
				progress_factory_wbadjust_rgain.setProgress(rgainvalWB);
				factoryManager.setWbRedGain((short) rgainvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_ggain:
				if (ggainvalWB > WB_ADJUST_GAIN_MIN) {
					ggainvalWB = ggainvalWB - gainStepWB;

					if (ggainvalWB < WB_ADJUST_GAIN_MIN)
						ggainvalWB = WB_ADJUST_GAIN_MIN;
				} else {
					ggainvalWB = WB_ADJUST_GAIN_MAX;
				}
				str_val = Integer.toString(ggainvalWB / gainDisplayDivideWB);
				text_factory_wbadjust_ggain_val.setText(str_val);
				progress_factory_wbadjust_ggain.setProgress(ggainvalWB);
				factoryManager.setWbGreenGain((short) ggainvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_bgain:
				if (bgainvalWB > WB_ADJUST_GAIN_MIN) {
					bgainvalWB = bgainvalWB - gainStepWB;

					if (bgainvalWB < WB_ADJUST_GAIN_MIN)
						bgainvalWB = WB_ADJUST_GAIN_MIN;
				} else {
					bgainvalWB = WB_ADJUST_GAIN_MAX;
				}
				str_val = Integer.toString(bgainvalWB / gainDisplayDivideWB);
				text_factory_wbadjust_bgain_val.setText(str_val);
				progress_factory_wbadjust_bgain.setProgress(bgainvalWB);
				factoryManager.setWbBlueGain((short) bgainvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_roffset:
				if (roffsetvalWB > WB_ADJUST_OFFSET_MIN) {
					roffsetvalWB = roffsetvalWB - offsetStepWB;

					if (roffsetvalWB < WB_ADJUST_OFFSET_MIN)
						roffsetvalWB = WB_ADJUST_OFFSET_MIN;
				} else {
					roffsetvalWB = WB_ADJUST_OFFSET_MAX;
				}
				str_val = Integer.toString(roffsetvalWB / offsetDisplayDivideWB);
				text_factory_wbadjust_roffset_val.setText(str_val);
				progress_factory_wbadjust_roffset.setProgress(roffsetvalWB);
				factoryManager.setWbRedOffset((short) roffsetvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_goffset:
				if (goffsetvalWB > WB_ADJUST_OFFSET_MIN) {
					goffsetvalWB = goffsetvalWB - offsetStepWB;

					if (goffsetvalWB < WB_ADJUST_OFFSET_MIN)
						goffsetvalWB = WB_ADJUST_OFFSET_MIN;
				} else {
					goffsetvalWB = WB_ADJUST_OFFSET_MAX;
				}
				str_val = Integer.toString(goffsetvalWB / offsetDisplayDivideWB);
				text_factory_wbadjust_goffset_val.setText(str_val);
				progress_factory_wbadjust_goffset.setProgress(goffsetvalWB);
				factoryManager.setWbGreenOffset((short) goffsetvalWB);
				break;
			case R.id.linearlayout_factory_wbadjust_boffset:
				if (boffsetvalWB > WB_ADJUST_OFFSET_MIN) {
					boffsetvalWB = boffsetvalWB - offsetStepWB;

					if (boffsetvalWB < WB_ADJUST_OFFSET_MIN)
						boffsetvalWB = WB_ADJUST_OFFSET_MIN;
				} else {
					boffsetvalWB = WB_ADJUST_OFFSET_MAX;
				}
				str_val = Integer.toString(boffsetvalWB / offsetDisplayDivideWB);
				text_factory_wbadjust_boffset_val.setText(str_val);
				progress_factory_wbadjust_boffset.setProgress(boffsetvalWB);
				factoryManager.setWbBlueOffset((short) boffsetvalWB);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			wbActivity.gotoPage(1);
			return bRet;
		case KeyEvent.KEYCODE_MENU:
			this.wbActivity.returnRoot(1);
			return bRet;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			catchInputNumber(keyCode);
			return true;

		case KeyEvent.KEYCODE_ENTER:
			if (isInputNumber) {
				if (mHandler.hasMessages(HANDLER_INPUT_NUMBER)) {
					mHandler.removeMessages(HANDLER_INPUT_NUMBER);
				}
				mHandler.sendEmptyMessage(HANDLER_INPUT_NUMBER);
				return true;
			}
			break;

		default:
			bRet = false;
			return bRet;
		}
		return bRet;
	}

	private boolean isInputNumber = false;
	private boolean isChangeNumber = false;
	private final static int HANDLER_INPUT_NUMBER = 5525;
	private int inputNumber = -1;

	private void catchInputNumber(int keyCode) {
		isInputNumber = true;
		isChangeNumber = true;
		if (mHandler.hasMessages(HANDLER_INPUT_NUMBER)) {
			mHandler.removeMessages(HANDLER_INPUT_NUMBER);
		}
		if (inputNumber != -1) {
			inputNumber = (inputNumber * 10) + (keyCode - KeyEvent.KEYCODE_0);
		} else {
			inputNumber = keyCode - KeyEvent.KEYCODE_0;
		}
		int currentid = wbActivity.getCurrentFocus().getId();
		switch (currentid) {
		case R.id.linearlayout_factory_wbadjust_rgain:
			currentType = 1;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_wbadjust_ggain:
			currentType = 2;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_wbadjust_bgain:
			currentType = 3;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_wbadjust_roffset:
			currentType = 4;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_wbadjust_goffset:
			currentType = 5;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_wbadjust_boffset:
			currentType = 6;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		default:
			inputNumber = -1;
			currentType = 0;
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_INPUT_NUMBER:
				isChangeNumber = false;
				break;
			}
			switch (currentType) {
			case 1:
				if (isChangeNumber) {
					text_factory_wbadjust_rgain_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > (int) (WB_ADJUST_GAIN_MAX / gainDisplayDivideWB)) {
					inputNumber = rgainvalWB / gainDisplayDivideWB;
				}
				rgainvalWB = inputNumber * gainDisplayDivideWB + 7;
				text_factory_wbadjust_rgain_val.setText(inputNumber + "");
				progress_factory_wbadjust_rgain.setProgress(rgainvalWB);
				factoryManager.setWbRedGain((short) rgainvalWB);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 2:
				if (isChangeNumber) {
					text_factory_wbadjust_ggain_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > (int) (WB_ADJUST_GAIN_MAX / gainDisplayDivideWB)) {
					inputNumber = ggainvalWB / gainDisplayDivideWB;
				}
				ggainvalWB = inputNumber * gainDisplayDivideWB + 7;
				text_factory_wbadjust_ggain_val.setText(inputNumber + "");
				progress_factory_wbadjust_ggain.setProgress(ggainvalWB);
				factoryManager.setWbGreenGain((short) ggainvalWB);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 3:
				if (isChangeNumber) {
					text_factory_wbadjust_bgain_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > (int) (WB_ADJUST_GAIN_MAX / gainDisplayDivideWB)) {
					inputNumber = bgainvalWB / gainDisplayDivideWB;
				}
				bgainvalWB = inputNumber * gainDisplayDivideWB + 7;
				text_factory_wbadjust_bgain_val.setText(inputNumber + "");
				progress_factory_wbadjust_bgain.setProgress(bgainvalWB);
				factoryManager.setWbBlueGain((short) bgainvalWB);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 4:
				if (isChangeNumber) {
					text_factory_wbadjust_roffset_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > (int) (WB_ADJUST_OFFSET_MAX / offsetDisplayDivideWB)) {
					inputNumber = roffsetvalWB / offsetDisplayDivideWB;
				}
				roffsetvalWB = inputNumber;
				text_factory_wbadjust_roffset_val.setText(inputNumber + "");
				progress_factory_wbadjust_roffset.setProgress(roffsetvalWB);
				factoryManager.setWbRedOffset((short) roffsetvalWB);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 5:
				if (isChangeNumber) {
					text_factory_wbadjust_goffset_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > (int) (WB_ADJUST_OFFSET_MAX / offsetDisplayDivideWB)) {
					inputNumber = goffsetvalWB / offsetDisplayDivideWB;
				}
				goffsetvalWB = inputNumber;
				text_factory_wbadjust_goffset_val.setText(inputNumber + "");
				progress_factory_wbadjust_goffset.setProgress(goffsetvalWB);
				factoryManager.setWbGreenOffset((short) goffsetvalWB);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 6:
				if (isChangeNumber) {
					text_factory_wbadjust_boffset_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > (int) (WB_ADJUST_OFFSET_MAX / offsetDisplayDivideWB)) {
					inputNumber = boffsetvalWB / offsetDisplayDivideWB;
				}
				boffsetvalWB = inputNumber;
				text_factory_wbadjust_boffset_val.setText(inputNumber + "");
				progress_factory_wbadjust_boffset.setProgress(boffsetvalWB);
				factoryManager.setWbBlueOffset((short) boffsetvalWB);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;
			default:
				break;
			}
		};
	};

	void DisableSourceLinear() {
		LinearLayout sourceLinear = (LinearLayout) wbActivity.findViewById(R.id.linearlayout_factory_wbadjust_source);
		((TextView) (sourceLinear.getChildAt(0))).setTextColor(Color.GRAY);
		((TextView) (sourceLinear.getChildAt(1))).setTextColor(Color.GRAY);
		sourceLinear.setEnabled(false);
		sourceLinear.setFocusable(false);
	}
}
