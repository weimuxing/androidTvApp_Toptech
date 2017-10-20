package mstar.tvsetting.factory.ui.sound;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAudioManager;

public class SoundViewHolder {
	private DesignMenuActivity mSoundActivity;
	private IFactoryDesk factoryManager;
	private LinearLayout linearlayout_sound_mode;
	private LinearLayout linearlayout_sound_adjust;
	private LinearLayout linearlayout_sound_prescale;
	private LinearLayout linearlayout_sound_speaker_audio_delay;
	private LinearLayout linearlayout_sound_peq_adjust;
	private TextView textview_sound_prescale;
	private TextView textview_sound_speaker_audio_delay;

	private int speaker_prescale = 0;
	private int audiodelayvalue = 0;

	private final static String CMD_SET_PRESCALE = "SetPrescale";
	private final static String CMD_SET_AUDIO_DELAY = "SetAudioDelay";
	private final static String CMD_GET_AUDIO_INFO = "GetAudioInfo";

	public SoundViewHolder(DesignMenuActivity designMenuActivity1, IFactoryDesk factoryManager) {
		mSoundActivity = designMenuActivity1;
		this.factoryManager = factoryManager;
	}

	public void findView() {
		linearlayout_sound_mode = (LinearLayout) mSoundActivity.findViewById(R.id.linearlayout_sound_mode);
		linearlayout_sound_adjust = (LinearLayout) mSoundActivity.findViewById(R.id.linearlayout_sound_Adjust);
		linearlayout_sound_prescale = (LinearLayout) mSoundActivity.findViewById(R.id.linearlayout_sound_Prescale);
		linearlayout_sound_speaker_audio_delay = (LinearLayout) mSoundActivity
				.findViewById(R.id.linearlayout_sound_Speaker_Audio_Delay);
		textview_sound_prescale = (TextView) mSoundActivity.findViewById(R.id.textview_sound_Prescale2);
		textview_sound_speaker_audio_delay = (TextView) mSoundActivity
				.findViewById(R.id.textview_sound_Speaker_Audio_Delay2);
		linearlayout_sound_peq_adjust = (LinearLayout) mSoundActivity.findViewById(R.id.linearlayout_sound_PEQ_Adjust);
		initValues();
		setValues();
		linearlayout_sound_adjust.setOnClickListener(listener);
		linearlayout_sound_mode.setOnClickListener(listener);
		linearlayout_sound_peq_adjust.setOnClickListener(listener);
	}

	private void initValues() {
		int[] retvalues;
		if (true) {
			retvalues = TvCommonManager.getInstance().setTvosCommonCommand(CMD_GET_AUDIO_INFO);
			speaker_prescale = retvalues[0];
			audiodelayvalue = TvAudioManager.getInstance().getSoundSpeakerDelay();
		}
	}

	private void setValues() {
		textview_sound_prescale.setText("0x" + Integer.toHexString(speaker_prescale));
		textview_sound_speaker_audio_delay.setText(
				Integer.toString(audiodelayvalue) + mSoundActivity.getString(R.string.str_sound_audio_delay_unit));
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.linearlayout_sound_mode:
				mSoundActivity.gotoPage(mSoundActivity.SOUND_MODE_PAGE);
				break;
			case R.id.linearlayout_sound_Adjust:
				mSoundActivity.gotoPage(mSoundActivity.SOUND_ADJUST_PAGE);
				break;
			case R.id.linearlayout_sound_PEQ_Adjust:
				mSoundActivity.gotoPage(mSoundActivity.PEQ_ADJUST_PAGE);
				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int[] ret_values;// [0] length, [1..] values
		String str_val = new String();
		int currentid = mSoundActivity.getCurrentFocus().getId();
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT: {
			switch (currentid) {
			case R.id.linearlayout_sound_Prescale: {
				if (speaker_prescale > 0)
					speaker_prescale -= 1;
				else
					speaker_prescale = 0xFF;
				// set then readback...
				ret_values = TvCommonManager.getInstance()
						.setTvosCommonCommand(CMD_SET_PRESCALE + "#" + speaker_prescale);
			}
				break;
			case R.id.linearlayout_sound_Speaker_Audio_Delay: {
				if (audiodelayvalue >= 10) {
					audiodelayvalue -= 10;
				} else {
					audiodelayvalue = 0;
				}
				TvAudioManager.getInstance().setSoundSpeakerDelay(audiodelayvalue);
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
			case R.id.linearlayout_sound_Prescale: {
				if (speaker_prescale < 0xFF)
					speaker_prescale += 1;
				else
					speaker_prescale = 0;
				// set then readback...
				ret_values = TvCommonManager.getInstance().setTvosCommonCommand("SetPrescale#" + speaker_prescale);
			}
				break;
			case R.id.linearlayout_sound_Speaker_Audio_Delay: {
				if (audiodelayvalue != 0xFFFF) {
					audiodelayvalue += 10;
				} else {
					audiodelayvalue = 0;
				}
				TvAudioManager.getInstance().setSoundSpeakerDelay(audiodelayvalue);
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
			bRet = false;
			break;
		}

		return bRet;
	}
}
