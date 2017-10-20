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

//import com.toptech.factorytools.IniFileReadWrite;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.desk.IFactoryDesk.MS_NLA_SET_INDEX;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.lang.String;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.util.Log;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import java.io.IOException;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.AudioManager;
import com.mstar.android.tvapi.common.vo.EnumSoundGetParameterType;

import android.util.Log;

public class FactorySoundViewHolder {
	private final static String TAG = "FactorySoundViewHolder";

	private AudioManager am = TvManager.getInstance().getAudioManager();

	// jerry add 20160405
	private final static String CMD_SET_AVC_ENABLE = "SetAvcEnable";
	private final static String CMD_SET_AVC_THL = "SetAvcThrethold";
	private final static String CMD_SET_DRC_ENABLE = "SetDrcEnable";
	private final static String CMD_SET_DRC_THL = "SetDrcThrethold";
	private final static String CMD_SET_AUDIO_DELAY = "SetAudioDelay";
	private final static String CMD_SET_AUDIO_SPDIF_DELAY = "SetAudioSpdifDelay";
	private final static String CMD_SET_AUDIO_NR = "SetAudioNr";
	private final static String CMD_GET_AUDIO_INFO = "GetAudioInfo";
	// end

	private DesignMenuActivity SoundActivity;

	private IFactoryDesk factoryManager;

	protected LinearLayout linearlayout_factory_lineout;
	protected TextView textvalue_factory_lineout;

	protected LinearLayout linearlayout_factory_avc_enable;
	protected TextView textvalue_factory_avc_enable;

	protected LinearLayout linearlayout_factory_avc_threthold;
	protected TextView textvalue_factory_avc_threthold;

	protected LinearLayout linearlayout_factory_drc_enable;
	protected TextView textvalue_factory_drc_enable;

	protected LinearLayout linearlayout_factory_drc_threthold;
	protected TextView textvalue_factory_drc_threthold;

	protected LinearLayout linearlayout_factory_sound_effect;

	protected LinearLayout linearlayout_factory_sound_toneffect;

	protected LinearLayout linearlayout_factory_audio_delay;
	protected TextView textvalue_factory_audio_delay;

	protected LinearLayout linearlayout_factory_audio_nr;
	protected TextView textvalue_factory_audio_nr;

	protected TextView textvalue_factory_audio_sound_effect;

	protected TextView textvalue_factory_audio_sound_tone_effect;

	protected TextView textvalue_factory_speaker;

	protected TextView textvalue_factory_audio_spdif_delay;

	private int lineout_prescale = 0;
	private int speaker_volume = 0;
	private boolean avc_enable = false;
	private int avc_threthold = 0;
	private boolean drc_enable = false;
	private int drc_threthold = 0;
	private boolean sound_effect = false;
	private boolean sound_tone_effect = false;
	private int audio_delay = 0;
	private int audio_spdif_delay = 0;
	private int audio_nr = 0;

    public FactorySoundViewHolder(DesignMenuActivity activity, IFactoryDesk factoryDesk) {
        SoundActivity = activity;
        this.factoryManager = factoryDesk;
    }

	public void findView() {

		linearlayout_factory_lineout = (LinearLayout) SoundActivity.findViewById(R.id.linearlayout_factory_lineout);
		textvalue_factory_lineout = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_lineout);

		linearlayout_factory_avc_enable = (LinearLayout) SoundActivity
				.findViewById(R.id.linearlayout_factory_avc_enable);
		textvalue_factory_avc_enable = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_avc_enable);

		linearlayout_factory_avc_threthold = (LinearLayout) SoundActivity
				.findViewById(R.id.linearlayout_factory_avc_threthold);
		textvalue_factory_avc_threthold = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_avc_threthold);

		linearlayout_factory_drc_enable = (LinearLayout) SoundActivity
				.findViewById(R.id.linearlayout_factory_drc_enable);
		textvalue_factory_drc_enable = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_drc_enable);

		linearlayout_factory_drc_threthold = (LinearLayout) SoundActivity
				.findViewById(R.id.linearlayout_factory_drc_threthold);
		textvalue_factory_drc_threthold = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_drc_threthold);

		linearlayout_factory_sound_effect = (LinearLayout) SoundActivity
				.findViewById(R.id.linearlayout_factory_sound_effect);

		linearlayout_factory_sound_toneffect = (LinearLayout) SoundActivity
				.findViewById(R.id.linearlayout_factory_sound_toneffect);

		linearlayout_factory_audio_delay = (LinearLayout) SoundActivity
				.findViewById(R.id.linearlayout_factory_audio_delay);
		textvalue_factory_audio_delay = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_audio_delay);

		linearlayout_factory_audio_nr = (LinearLayout) SoundActivity.findViewById(R.id.linearlayout_factory_audio_nr);
		textvalue_factory_audio_nr = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_audio_nr);

		textvalue_factory_audio_sound_effect = (TextView) SoundActivity
				.findViewById(R.id.textvalue_factory_sound_effect);

		textvalue_factory_audio_sound_tone_effect = (TextView) SoundActivity
				.findViewById(R.id.textvalue_factory_sound_toneffect);

		textvalue_factory_speaker = (TextView) SoundActivity.findViewById(R.id.textvalue_factory_speaker);

		textvalue_factory_audio_spdif_delay = (TextView) SoundActivity
				.findViewById(R.id.textvalue_factory_audio_spdif_delay);

		// init the values here
		initValues();
		// set the value
		setValues();

	}

	private void initValues() {
		int[] retvalues;
		if (true) {
			retvalues = TvCommonManager.getInstance().setTvosCommonCommand(CMD_GET_AUDIO_INFO);
			avc_enable = (retvalues[1] > 0) ? true : false;
			avc_threthold = retvalues[2];
			drc_enable = (retvalues[3] > 0) ? true : false;
			drc_threthold = retvalues[4];
			audio_delay = retvalues[5];
			audio_nr = retvalues[6];
			sound_effect = retvalues[7] > 0 ? true : false;
			sound_tone_effect = retvalues[8] > 0 ? true : false;
			lineout_prescale = retvalues[9];
			speaker_volume = retvalues[10];
			audio_spdif_delay = retvalues[11];
		} else {
			// speaker_prescale =
			// am.getBasicSoundEffect(EnumSoundGetParameterType.E_PRESCALE);
			// avc_enable =
			// am.getBasicSoundEffect(EnumSoundGetParameterType.E_AVC_ONOFF)?true:false;
			// avc_threthold =
			// am.getBasicSoundEffect(EnumSoundGetParameterType.E_AVC_THRESHOLD);
			// drc_enable =
			// am.getBasicSoundEffect(EnumSoundGetParameterType.E_PRESCALE)?true:false;
			// drc_threthold =
			// am.getBasicSoundEffect(EnumSoundGetParameterType.E_DRC_THRESHOLD);
			// audio_delay =
			// am.getBasicSoundEffect(EnumSoundGetParameterType.E_PRESCALE);
			// audio_nr =
			// am.getBasicSoundEffect(EnumSoundGetParameterType.E_NR_THRESHOLD);
		}
	}

	private void setValues() {
		textvalue_factory_avc_enable.setText(avc_enable ? "true" : "false");
		textvalue_factory_avc_threthold.setText("0x" + Integer.toHexString(avc_threthold));
		textvalue_factory_drc_enable.setText(drc_enable ? "true" : "false");
		textvalue_factory_drc_threthold.setText("0x" + Integer.toHexString(drc_threthold));
		textvalue_factory_audio_delay.setText("0x" + Integer.toHexString(audio_delay));
		textvalue_factory_audio_nr.setText("0x" + Integer.toHexString(audio_nr));

        textvalue_factory_audio_sound_effect.setText(sound_effect?"true":"false");
        textvalue_factory_audio_sound_tone_effect.setText(sound_tone_effect?"true":"false");

        textvalue_factory_lineout.setText("0x"+Integer.toHexString(lineout_prescale));
        textvalue_factory_speaker.setText("0x"+Integer.toHexString(speaker_volume));
        textvalue_factory_audio_spdif_delay.setText("0x"+Integer.toHexString(audio_spdif_delay));
	}

	private int bti(boolean bval) {
		if (bval)
			return 1;

		return 0;
	}

	private boolean itb(int ival) {
		if (ival > 0)
			return true;

		return false;
	}

	public boolean onCreate() {

		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int[] ret_values;// [0] length, [1..] values
		int currentid = SoundActivity.getCurrentFocus().getId();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			SoundActivity.gotoPage(2);
			break;
		case KeyEvent.KEYCODE_MENU:
			this.SoundActivity.returnRoot(/* DesignMenuActivity.SOUND_PAGE */12);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: {
			switch (currentid) {
			case R.id.linearlayout_factory_avc_enable: {
				avc_enable = !avc_enable;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand(CMD_SET_AVC_ENABLE + "#" + bti(avc_enable));
				avc_enable = (ret_values[0] > 0) ? true : false;
			}
				break;
			case R.id.linearlayout_factory_avc_threthold: {
				if (avc_threthold > 0)
					avc_threthold -= 1;
				else
					avc_threthold = 0x50;

				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand(CMD_SET_AVC_THL + "#" + avc_threthold);
				avc_threthold = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_drc_enable: {
				drc_enable = !drc_enable;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand(CMD_SET_DRC_ENABLE + "#" + bti(drc_enable));
				drc_enable = (ret_values[0] > 0) ? true : false;
			}
				break;
			case R.id.linearlayout_factory_drc_threthold: {
				if (drc_threthold > 0)
					drc_threthold -= 1;
				else
					drc_threthold = 0x50;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand(CMD_SET_DRC_THL + "#" + drc_threthold);
				drc_threthold = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_audio_delay: {
				if (audio_delay > 0)
					audio_delay -= 1;
				else
					audio_delay = 0xFF;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand(CMD_SET_AUDIO_DELAY + "#" + audio_delay);
				audio_delay = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_audio_spdif_delay: {
				if (audio_spdif_delay > 0)
					audio_spdif_delay -= 1;
				else
					audio_spdif_delay = 0xFF;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand(CMD_SET_AUDIO_SPDIF_DELAY + "#" + audio_spdif_delay);
				audio_spdif_delay = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_audio_nr: {
				if (audio_nr > 0)
					audio_nr -= 1;
				else
					audio_nr = 0xFF;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand(CMD_SET_AUDIO_NR + "#" + audio_nr);
				audio_nr = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_sound_effect: {// in fact it is
															// hpf.........
				sound_effect = !sound_effect;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioEffect#" + bti(sound_effect));
				sound_effect = ret_values[0] > 0 ? true : false;

			}
				break;
			case R.id.linearlayout_factory_sound_toneffect: {
				sound_tone_effect = !sound_tone_effect;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand("SetAudioToneEffect#" + bti(sound_tone_effect));
				sound_tone_effect = ret_values[0] > 0 ? true : false;
			}
				break;
			case R.id.linearlayout_factory_lineout: {
				int tmpval = (lineout_prescale & 0x7FFF) >> 5;
				if (tmpval > 0)
					tmpval -= 1;
				else
					tmpval = (0x7f << 3 | 0x07);
				lineout_prescale = tmpval << 5;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioLineout#" + lineout_prescale);
				lineout_prescale = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_speaker: {
				int tmpval = (speaker_volume & 0x7FFF) >> 5;
				if (tmpval > 0)
					tmpval -= 1;
				else
					tmpval = (0x7f << 3 | 0x07);
				speaker_volume = tmpval << 5;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioVolume#" + speaker_volume);
				speaker_volume = ret_values[0];
			}
				break;
			default:
				bRet = false;
				break;
			}

			setValues();
		}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: {
			switch (currentid) {
			case R.id.linearlayout_factory_avc_enable: {
				avc_enable = !avc_enable;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAvcEnable#" + bti(avc_enable));
				avc_enable = (ret_values[0] > 0) ? true : false;
			}
				break;
			case R.id.linearlayout_factory_avc_threthold: {
				if (avc_threthold < 0x50)
					avc_threthold += 1;
				else
					avc_threthold = 0;

				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAvcThrethold#" + avc_threthold);
				avc_threthold = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_drc_enable: {
				drc_enable = !drc_enable;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetDrcEnable#" + bti(drc_enable));
				drc_enable = (ret_values[0] > 0) ? true : false;
			}
				break;
			case R.id.linearlayout_factory_drc_threthold: {
				if (drc_threthold < 0x50)
					drc_threthold += 1;
				else
					drc_threthold = 0;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetDrcThrethold#" + drc_threthold);
				drc_threthold = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_audio_delay: {
				if (audio_delay < 0xFF)
					audio_delay += 1;
				else
					audio_delay = 0;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioDelay#" + audio_delay);
				audio_delay = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_audio_spdif_delay: {
				if (audio_spdif_delay < 0xFF)
					audio_spdif_delay += 1;
				else
					audio_spdif_delay = 0;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand("SetAudioSpdifDelay#" + audio_spdif_delay);
				audio_spdif_delay = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_audio_nr: {
				if (audio_nr < 0xFF)
					audio_nr += 1;
				else
					audio_nr = 0;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioNr#" + audio_nr);
				audio_nr = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_sound_effect: {// in fact it is
															// hpf.........
				sound_effect = !sound_effect;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioEffect#" + bti(sound_effect));
				sound_effect = ret_values[0] > 0 ? true : false;

			}
				break;
			case R.id.linearlayout_factory_sound_toneffect: {
				sound_tone_effect = !sound_tone_effect;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand("SetAudioToneEffect#" + bti(sound_tone_effect));
				sound_tone_effect = ret_values[0] > 0 ? true : false;
			}
				break;
			case R.id.linearlayout_factory_lineout: {
				int tmpval = (lineout_prescale & 0x7FFF) >> 5;
				if (tmpval < (0x7f << 3 | 0x07))
					tmpval += 1;
				else
					tmpval = 0;
				lineout_prescale = tmpval << 5;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioLineout#" + lineout_prescale);
				lineout_prescale = ret_values[0];
			}
				break;
			case R.id.linearlayout_factory_speaker: {
				int tmpval = (speaker_volume & 0x7FFF) >> 5;
				if (tmpval < (0x7f << 3 | 0x07))
					tmpval += 1;
				else
					tmpval = 0;
				speaker_volume = tmpval << 5;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetAudioVolume#" + speaker_volume);
				speaker_volume = ret_values[0];
			}
				break;
			default:
				bRet = false;
				break;
			}

			setValues();
		}
			break;

		default:
			Log.d(TAG, "onkeydown = " + keyCode);
			bRet = false;
			break;
		}

		return bRet;
    }
}
