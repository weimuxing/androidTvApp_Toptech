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

package mstar.tvsetting.factory.ui.picture;

import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.desk.IFactoryDesk.MS_NLA_SET_INDEX;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NonLinearAdjustViewHolder {
	private DesignMenuActivity nonLinearActivity;

	private IFactoryDesk factoryManager;

	protected TextView text_factory_nonlinear_curvetype_val;

	protected TextView text_factory_nonlinear_osd0_val;

	protected TextView text_factory_nonlinear_osd25_val;

	protected TextView text_factory_nonlinear_osd50_val;

	protected TextView text_factory_nonlinear_osd75_val;

	protected TextView text_factory_nonlinear_osd100_val;

	protected ProgressBar progress_factory_nonlinear_osd0;

	protected ProgressBar progress_factory_nonlinear_osd25;

	protected ProgressBar progress_factory_nonlinear_osd50;

	protected ProgressBar progress_factory_nonlinear_osd75;

	protected ProgressBar progress_factory_nonlinear_osd100;

	private int curvetypeindex = 0;

	private int osd0val = 50;

	private int osd25val = 50;

	private int osd50val = 50;

	private int osd75val = 50;

	private int osd100val = 50;

	private int VolumeMaxVol = 100;

	private int BrightnessMaxVol = 255;

	private int ContrastMaxVol = 255;

	private int SaturationMaxVol = 255;

	private int SharpnessMaxVol = 63;

	private int HueMaxVol = 100;

	private int BackLightMaxVol = 255;

	private int[] curvemaxarray = { VolumeMaxVol, BrightnessMaxVol, ContrastMaxVol, SaturationMaxVol, SharpnessMaxVol,
			HueMaxVol, BackLightMaxVol };

	private String[] curvetypearray;

	private final static int HANDLER_INPUT_NUMBER = 5525;
	
	private int inputNumber = -1;

	private int currentType = 0;
	
	private boolean isInputNumber = false;
	
	private boolean isChangeNumber = false;

	public NonLinearAdjustViewHolder(DesignMenuActivity activity, IFactoryDesk factoryDesk) {
		nonLinearActivity = activity;
		this.factoryManager = factoryDesk;
	}

	public void findView() {
		text_factory_nonlinear_curvetype_val = (TextView) nonLinearActivity
				.findViewById(R.id.textview_factory_nonlinear_curvetype_val);
		text_factory_nonlinear_osd0_val = (TextView) nonLinearActivity
				.findViewById(R.id.textview_factory_nonlinear_osd0_val);
		text_factory_nonlinear_osd25_val = (TextView) nonLinearActivity
				.findViewById(R.id.textview_factory_nonlinear_osd25_val);
		text_factory_nonlinear_osd50_val = (TextView) nonLinearActivity
				.findViewById(R.id.textview_factory_nonlinear_osd50_val);
		text_factory_nonlinear_osd75_val = (TextView) nonLinearActivity
				.findViewById(R.id.textview_factory_nonlinear_osd75_val);
		text_factory_nonlinear_osd100_val = (TextView) nonLinearActivity
				.findViewById(R.id.textview_factory_nonlinear_osd100_val);
		progress_factory_nonlinear_osd0 = (ProgressBar) nonLinearActivity
				.findViewById(R.id.progressbar_facroty_nonlinear_osd0);
		progress_factory_nonlinear_osd25 = (ProgressBar) nonLinearActivity
				.findViewById(R.id.progressbar_facroty_nonlinear_osd25);
		progress_factory_nonlinear_osd50 = (ProgressBar) nonLinearActivity
				.findViewById(R.id.progressbar_facroty_nonlinear_osd50);
		progress_factory_nonlinear_osd75 = (ProgressBar) nonLinearActivity
				.findViewById(R.id.progressbar_facroty_nonlinear_osd75);
		progress_factory_nonlinear_osd100 = (ProgressBar) nonLinearActivity
				.findViewById(R.id.progressbar_facroty_nonlinear_osd100);
	}

	public boolean onCreate() {
		curvetypearray = nonLinearActivity.getResources().getStringArray(R.array.str_arr_picture_non_linear_vals);
		curvetypeindex = factoryManager.getCurveType().ordinal();
		osd0val = factoryManager.getOsdV0Nonlinear();
		osd25val = factoryManager.getOsdV25Nonlinear();
		osd50val = factoryManager.getOsdV50Nonlinear();
		osd75val = factoryManager.getOsdV75Nonlinear();
		osd100val = factoryManager.getOsdV100Nonlinear();
		text_factory_nonlinear_osd0_val.setText(Integer.toString(osd0val));
		text_factory_nonlinear_osd25_val.setText(Integer.toString(osd25val));
		text_factory_nonlinear_osd50_val.setText(Integer.toString(osd50val));
		text_factory_nonlinear_osd75_val.setText(Integer.toString(osd75val));
		text_factory_nonlinear_osd100_val.setText(Integer.toString(osd100val));
		progress_factory_nonlinear_osd0.setProgress((osd0val * 255) / curvemaxarray[curvetypeindex]);
		progress_factory_nonlinear_osd25.setProgress((osd25val * 255) / curvemaxarray[curvetypeindex]);
		progress_factory_nonlinear_osd50.setProgress((osd50val * 255) / curvemaxarray[curvetypeindex]);
		progress_factory_nonlinear_osd75.setProgress((osd75val * 255) / curvemaxarray[curvetypeindex]);
		progress_factory_nonlinear_osd100.setProgress((osd100val * 255) / curvemaxarray[curvetypeindex]);
		text_factory_nonlinear_curvetype_val.setText(curvetypearray[curvetypeindex]);
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentid = nonLinearActivity.getCurrentFocus().getId();
		String str_val = new String();
		int maxValue;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentid) {
			case R.id.linearlayout_factory_nonlinear_curvetype:
				maxValue = MS_NLA_SET_INDEX.EN_NLA_NUMS.ordinal();
				if (curvetypeindex < (maxValue - 1))
					curvetypeindex++;
				else
					curvetypeindex = 0;
				text_factory_nonlinear_curvetype_val.setText(curvetypearray[curvetypeindex]);
				factoryManager.setCurveType(MS_NLA_SET_INDEX.values()[curvetypeindex]);
				onCreate();
				break;
			case R.id.linearlayout_factory_nonlinear_osd0:
				if (osd0val != curvemaxarray[curvetypeindex]) {
					osd0val++;
				} else {
					osd0val = 0;
				}
				str_val = Integer.toString(osd0val);
				text_factory_nonlinear_osd0_val.setText(str_val);
				progress_factory_nonlinear_osd0.setProgress((osd0val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV0Nonlinear(osd0val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd25:
				if (osd25val != curvemaxarray[curvetypeindex]) {
					osd25val++;
				} else {
					osd25val = 0;
				}
				str_val = Integer.toString(osd25val);
				text_factory_nonlinear_osd25_val.setText(str_val);
				progress_factory_nonlinear_osd25.setProgress((osd25val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV25Nonlinear(osd25val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd50:
				if (osd50val != curvemaxarray[curvetypeindex]) {
					osd50val++;
				} else {
					osd50val = 0;
				}
				str_val = Integer.toString(osd50val);
				text_factory_nonlinear_osd50_val.setText(str_val);
				progress_factory_nonlinear_osd50.setProgress((osd50val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV50Nonlinear(osd50val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd75:
				if (osd75val != curvemaxarray[curvetypeindex]) {
					osd75val++;
				} else {
					osd75val = 0;
				}
				str_val = Integer.toString(osd75val);
				text_factory_nonlinear_osd75_val.setText(str_val);
				progress_factory_nonlinear_osd75.setProgress((osd75val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV75Nonlinear(osd75val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd100:
				if (osd100val != curvemaxarray[curvetypeindex]) {
					osd100val++;
				} else {
					osd100val = 0;
				}
				str_val = Integer.toString(osd100val);
				text_factory_nonlinear_osd100_val.setText(str_val);
				progress_factory_nonlinear_osd100.setProgress((osd100val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV100Nonlinear(osd100val);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentid) {
			case R.id.linearlayout_factory_nonlinear_curvetype:
				maxValue = MS_NLA_SET_INDEX.EN_NLA_NUMS.ordinal();
				if (curvetypeindex > 0)
					curvetypeindex--;
				else
					curvetypeindex = maxValue - 1;
				text_factory_nonlinear_curvetype_val.setText(curvetypearray[curvetypeindex]);
				factoryManager.setCurveType(MS_NLA_SET_INDEX.values()[curvetypeindex]);
				onCreate();
				break;
			case R.id.linearlayout_factory_nonlinear_osd0:
				if (osd0val != 0) {
					osd0val--;
				} else {
					osd0val = curvemaxarray[curvetypeindex];
				}
				str_val = Integer.toString(osd0val);
				text_factory_nonlinear_osd0_val.setText(str_val);
				progress_factory_nonlinear_osd0.setProgress((osd0val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV0Nonlinear(osd0val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd25:
				if (osd25val != 0) {
					osd25val--;
				} else {
					osd25val = curvemaxarray[curvetypeindex];
				}
				str_val = Integer.toString(osd25val);
				text_factory_nonlinear_osd25_val.setText(str_val);
				progress_factory_nonlinear_osd25.setProgress((osd25val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV25Nonlinear(osd25val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd50:
				if (osd50val != 0) {
					osd50val--;
				} else {
					osd50val = curvemaxarray[curvetypeindex];
				}
				str_val = Integer.toString(osd50val);
				text_factory_nonlinear_osd50_val.setText(str_val);
				progress_factory_nonlinear_osd50.setProgress((osd50val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV50Nonlinear(osd50val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd75:
				if (osd75val != 0) {
					osd75val--;
				} else {
					osd75val = curvemaxarray[curvetypeindex];
				}
				str_val = Integer.toString(osd75val);
				text_factory_nonlinear_osd75_val.setText(str_val);
				progress_factory_nonlinear_osd75.setProgress((osd75val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV75Nonlinear(osd75val);
				break;
			case R.id.linearlayout_factory_nonlinear_osd100:
				if (osd100val != 0) {
					osd100val--;
				} else {
					osd100val = curvemaxarray[curvetypeindex];
				}
				str_val = Integer.toString(osd100val);
				text_factory_nonlinear_osd100_val.setText(str_val);
				progress_factory_nonlinear_osd100.setProgress((osd100val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV100Nonlinear(osd100val);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			nonLinearActivity.gotoPage(1);

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
		int currentId = nonLinearActivity.getCurrentFocus().getId();
		switch (currentId) {
		case R.id.linearlayout_factory_nonlinear_osd0:
			currentType = 1;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_nonlinear_osd25:
			currentType = 2;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_nonlinear_osd50:
			currentType = 3;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_nonlinear_osd75:
			currentType = 4;
			mHandler.sendEmptyMessage(0);
			mHandler.sendEmptyMessageDelayed(HANDLER_INPUT_NUMBER, 4000);
			break;

		case R.id.linearlayout_factory_nonlinear_osd100:
			currentType = 5;
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
					text_factory_nonlinear_osd0_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > curvemaxarray[curvetypeindex]) {
					inputNumber = factoryManager.getOsdV0Nonlinear();
				}
				osd0val = inputNumber;
				text_factory_nonlinear_osd0_val.setText(osd0val + "");
				progress_factory_nonlinear_osd0.setProgress((osd0val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV0Nonlinear(osd0val);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 2:
				if (isChangeNumber) {
					text_factory_nonlinear_osd25_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > curvemaxarray[curvetypeindex]) {
					inputNumber = factoryManager.getOsdV25Nonlinear();
				}
				osd25val = inputNumber;
				text_factory_nonlinear_osd25_val.setText(osd25val + "");
				progress_factory_nonlinear_osd25.setProgress((osd25val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV25Nonlinear(osd25val);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 3:
				if (isChangeNumber) {
					text_factory_nonlinear_osd50_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > curvemaxarray[curvetypeindex]) {
					inputNumber = factoryManager.getOsdV50Nonlinear();
				}
				osd50val = inputNumber;
				text_factory_nonlinear_osd50_val.setText(osd50val + "");
				progress_factory_nonlinear_osd50.setProgress((osd50val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV50Nonlinear(osd50val);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 4:
				if (isChangeNumber) {
					text_factory_nonlinear_osd75_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > curvemaxarray[curvetypeindex]) {
					inputNumber = factoryManager.getOsdV75Nonlinear();
				}
				osd75val = inputNumber;
				text_factory_nonlinear_osd75_val.setText(osd75val + "");
				progress_factory_nonlinear_osd75.setProgress((osd75val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV75Nonlinear(osd75val);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			case 5:
				if (isChangeNumber) {
					text_factory_nonlinear_osd100_val.setText(inputNumber + "");
					break;
				}
				if (inputNumber > curvemaxarray[curvetypeindex]) {
					inputNumber = factoryManager.getOsdV100Nonlinear();
				}
				osd100val = inputNumber;
				text_factory_nonlinear_osd100_val.setText(osd100val + "");
				progress_factory_nonlinear_osd100.setProgress((osd100val * 255) / curvemaxarray[curvetypeindex]);
				factoryManager.setOsdV100Nonlinear(osd100val);
				inputNumber = -1;
				isInputNumber = false;
				currentType = 0;
				break;

			default:
				break;
			}
		};
	};
}
