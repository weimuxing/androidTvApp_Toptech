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

package mstar.tvsetting.factory.ui.sound;

import com.mstar.android.tv.TvAudioManager;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

public class SoundModeViewHolder {
	private DesignMenuActivity soundModeActivity;

	protected TvAudioManager tvAudioManager = null;

	private IFactoryDesk factoryManager;

	protected TextView text_sound_mode_val;

	protected TextView text_sound_mode_bass_val;

	protected TextView text_sound_mode_treble_val;

	protected ProgressBar progress_sound_mode_bass;

	protected ProgressBar progress_sound_mode_treble;

	private int type;
	private int progressBass;
	private int progressTreble;

	private int updateFlag;

	private String[] soundmodetypearray;

	public SoundModeViewHolder(DesignMenuActivity activity, IFactoryDesk factoryDesk) {
		soundModeActivity = activity;
		this.factoryManager = factoryDesk;
	}

	public void findView() {
		text_sound_mode_val = (TextView) soundModeActivity.findViewById(R.id.textview_sound_mode_val);
		text_sound_mode_bass_val = (TextView) soundModeActivity.findViewById(R.id.textview_sound_mode_bass_val);
		text_sound_mode_treble_val = (TextView) soundModeActivity.findViewById(R.id.textview_sound_mode_treble_val);
		progress_sound_mode_bass = (ProgressBar) soundModeActivity.findViewById(R.id.progressbar_sound_mode_bass);
		progress_sound_mode_treble = (ProgressBar) soundModeActivity.findViewById(R.id.progressbar_sound_mode_treble);
	}

	public boolean onCreate() {
		tvAudioManager = TvAudioManager.getInstance();
		soundmodetypearray = soundModeActivity.getResources().getStringArray(R.array.str_arr_sound_soundmode_vals);
		progress_sound_mode_bass.setMax(100);
		progress_sound_mode_treble.setMax(100);
		freshDataToUIWhenSoundModeChange();
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentid = soundModeActivity.getCurrentFocus().getId();
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentid) {
			case R.id.linearlayout_sound_mode:
				if (type < 4)
					type++;
				else
					type = 0;
				tvAudioManager.setAudioSoundMode(type);
				freshDataToUIWhenSoundModeChange();
				return true;
			case R.id.linearlayout_sound_mode_bass:
				if (progressBass < 100)
					progressBass++;
				else
					progressBass = 0;
				updateFlag = 1;
				break;
			case R.id.linearlayout_sound_mode_treble:
				if (progressTreble < 100)
					progressTreble++;
				else
					progressTreble = 0;
				updateFlag = 2;
				break;
			}
			updateProgress();
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, 1000);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentid) {
			case R.id.linearlayout_sound_mode:
				if (type > 0)
					type--;
				else
					type = 4;
				tvAudioManager.setAudioSoundMode(type);
				freshDataToUIWhenSoundModeChange();
				return true;
			case R.id.linearlayout_sound_mode_bass:
				if (progressBass > 0)
					progressBass--;
				else
					progressBass = 100;
				updateFlag = 1;
				break;
			case R.id.linearlayout_sound_mode_treble:
				if (progressTreble > 0)
					progressTreble--;
				else
					progressTreble = 100;
				updateFlag = 2;
				break;
			}
			updateProgress();
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			handler.removeMessages(0);
			handler.sendEmptyMessage(0);
			return false;
		default:
			bRet = false;
			break;
		}
		handler.removeMessages(0);
		handler.sendEmptyMessageDelayed(0, 500);
		return bRet;
	}
	
	private void freshDataToUIWhenSoundModeChange() {
		type = tvAudioManager.getAudioSoundMode();
		progressBass = tvAudioManager.getBass();
		progressTreble = tvAudioManager.getTreble();
		text_sound_mode_val.setText(soundmodetypearray[type]);
		updateProgress();
	}
	
	private void updateProgress(){
		progress_sound_mode_bass.setProgress(progressBass);
		progress_sound_mode_treble.setProgress(progressTreble);
		text_sound_mode_bass_val.setText(progressBass + "");
		text_sound_mode_treble_val.setText(progressTreble + "");
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (updateFlag) {
			case 1:
				tvAudioManager.setBass(progressBass);
				break;
			case 2:
				tvAudioManager.setTreble(progressTreble);
				break;
			}
		};
	};

}
