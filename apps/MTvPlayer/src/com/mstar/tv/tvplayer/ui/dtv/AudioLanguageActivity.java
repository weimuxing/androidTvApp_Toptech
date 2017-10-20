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

package com.mstar.tv.tvplayer.ui.dtv;

import java.util.Arrays;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TimeOutHelper;
import com.mstar.util.Utility;

public class AudioLanguageActivity extends MstarBaseActivity {
    private static final String TAG = "AudioLanguageActivity";

    private final int DUAL_MONO_CHANNEL = 1;

    private ListView mAudioLanguageListView = null;

    private short mAudioLangCount = 0;

    private TimeOutHelper mTimeoutHelper;

    private TvChannelManager mTvChannelManager = null;

    private DtvAudioInfo mAudioInfo = null;

    private boolean mIsDualMono = false;

    private AudioAdapter mAudioAdapter = null;

    private final int[] mNextAudioModeLL = {
            TvAudioManager.DTV_SOUND_MODE_LEFT, TvAudioManager.DTV_SOUND_MODE_LEFT,
            TvAudioManager.DTV_SOUND_MODE_STEREO
    };

    private final int[] mNextAudioModeRR = {
            TvAudioManager.DTV_SOUND_MODE_RIGHT, TvAudioManager.DTV_SOUND_MODE_STEREO,
            TvAudioManager.DTV_SOUND_MODE_RIGHT
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == mTimeoutHelper.getTimeOutMsg()) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_language_list_view);
        mAudioLanguageListView = (ListView) findViewById(R.id.audio_language_list_view);
        TextView title = (TextView) findViewById(R.id.audio_language_title);
        String str = getResources().getString(R.string.str_audio_language);
        title.setText(str);
        mTvChannelManager = TvChannelManager.getInstance();

        mTimeoutHelper = new TimeOutHelper(mHandler, this);

        loadUI(false);

        mAudioLanguageListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                Log.d(TAG, "set audio language = " + postion);
                mTvChannelManager.switchAudioTrack(postion);
            }
        });

        TvCommonManager.getInstance().speakTtsDelayed(
            getString(R.string.str_audio_language_title)
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
    }

    private void loadUI(boolean isReload) {
        mAudioInfo = mTvChannelManager.getAudioInfo_ex();

        if (mAudioInfo.audioLangNum == 0) {
            // show unknown
            mAudioLangCount = 1;
        } else {
            mAudioLangCount = mAudioInfo.audioLangNum;
            Log.d(TAG, "currentChannelMode = " + mAudioInfo.currentChannelMode);
            if (false == isReload) {
                if (mAudioInfo.currentChannelMode == DUAL_MONO_CHANNEL) {
                    mIsDualMono = true;
                }
            }
        }

        String[] audioTypeIsoDisplayString = getResources().getStringArray(
                R.array.str_arr_setting_audio_type_iso_text);
        String[] audioLang = new String[mAudioLangCount];
        String[] audioType = new String[mAudioLangCount];
        int[] audioMode = new int[mAudioLangCount];
        String[] audioTypeIso = new String[mAudioLangCount];

        Arrays.fill(audioType, "");
        Arrays.fill(audioMode, 0);
        Arrays.fill(audioTypeIso, "");

        if (mAudioInfo.audioLangNum == 0) {
            audioLang[0] = getResources().getString(R.string.str_sound_format_unknown);
        }

        for (short i = 0; i < mAudioInfo.audioLangNum; i++) {
            int lang = mTvChannelManager.getLanguageIdByCode(String
                    .valueOf(mAudioInfo.audioInfos[i].isoLangInfo.isoLangInfo));
            audioLang[i] = TvLanguage.getName(lang);
            audioType[i] = Utility.getAudioTypeString(mAudioInfo.audioInfos[i].audioType,
                    mAudioInfo.audioInfos[i].aacProfileAndLevel);
            audioMode[i] = mAudioInfo.audioInfos[i].isoLangInfo.audioMode;
            if (mAudioInfo.audioInfos[i].isoLangInfo.audioType < audioTypeIsoDisplayString.length) {
                audioTypeIso[i] = audioTypeIsoDisplayString[mAudioInfo.audioInfos[i].isoLangInfo.audioType];
            }
        }

        mAudioAdapter = new AudioAdapter(audioLang, audioType, audioMode,
                audioTypeIso, mIsDualMono);
        mAudioLanguageListView.setAdapter(mAudioAdapter);
        mAudioLanguageListView.setDividerHeight(0);
        int index = mAudioInfo.currentAudioIndex;
        if (index < 0 || index >= mAudioInfo.audioLangNum) {
            index = 0;
        }
        mAudioLanguageListView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mAudioAdapter.ttsSpeakFocusItem(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mAudioLanguageListView.setSelection(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimeoutHelper.start();
        mTimeoutHelper.init();
    };

    @Override
    protected void onPause() {
        super.onPause();
        mTimeoutHelper.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mTimeoutHelper.reset();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_PROG_RED:
                finish();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if ((mAudioInfo.audioLangNum > 0) && (true == mIsDualMono)) {
                    setAudioMode(mNextAudioModeLL);
                    loadUI(true);
                    mAudioLanguageListView.invalidate();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if ((mAudioInfo.audioLangNum > 0) && (true == mIsDualMono)) {
                    setAudioMode(mNextAudioModeRR);
                    loadUI(true);
                    mAudioLanguageListView.invalidate();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setAudioMode(final int[] nextMode) {
        int selPos = mAudioLanguageListView.getSelectedItemPosition();
        int audioMode = mAudioInfo.audioInfos[selPos].isoLangInfo.audioMode;
        if (nextMode.length > audioMode) {
            TvAudioManager.getInstance().setAudioMode(nextMode[audioMode]);
        }
    }

    private class AudioAdapter extends BaseAdapter {

        private final String DUAL_MONO_STR = "DualMono";

        private String[] audioLang = null;

        private String[] audioTypeText = null;

        private int[] audioModeText = null;

        private String[] audioTypeIsoText = null;

        private boolean isDualMonoChannel = false;

        public AudioAdapter(String[] audioName, String[] audioSort, int[] audioMode,
                String[] audioTypeIso, boolean isDualMono) {
            audioLang = audioName;
            audioTypeText = audioSort;
            audioModeText = audioMode;
            audioTypeIsoText = audioTypeIso;
            isDualMonoChannel = isDualMono;
        }

        public void ttsSpeakFocusItem(int pos) {
            TvCommonManager.getInstance().speakTtsDelayed(
                audioLang[pos]
                , TvCommonManager.TTS_QUEUE_FLUSH
                , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
                , TvCommonManager.TTS_DELAY_TIME_100MS);
        }

        @Override
        public int getCount() {
            return mAudioLangCount;
        }

        @Override
        public Object getItem(int arg0) {
            return audioLang[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater layout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layout.inflate(R.layout.mts_info_list_view_item, parent, false);
            }

            /* audio mode */
            TextView audioMode = (TextView) view.findViewById(R.id.mts_audio_mode);
            if (false == isDualMonoChannel) {
                audioMode.setVisibility(View.GONE);
            } else {
                audioMode.setText(DUAL_MONO_STR);
                audioMode.setVisibility(View.VISIBLE);
            }

            /* audio type */
            TextView audioType = (TextView) view.findViewById(R.id.mts_audio_type);
            if (audioTypeText[position].isEmpty()) {
                audioType.setVisibility(View.INVISIBLE);
            } else {
                audioType.setText(audioTypeText[position]);
                audioType.setVisibility(View.VISIBLE);
            }

            /* audio type iso */
            TextView audioTypeIso = (TextView) view.findViewById(R.id.mts_audio_info_type_iso);
            if (audioTypeIsoText[position].isEmpty()) {
                audioTypeIso.setVisibility(View.INVISIBLE);
            } else {
                audioTypeIso.setText(audioTypeIsoText[position]);
                audioTypeIso.setVisibility(View.VISIBLE);
            }

            ImageView audioModeImageL = (ImageView) view.findViewById(R.id.mts_audio_info_left);
            ImageView audioModeImageR = (ImageView) view.findViewById(R.id.mts_audio_info_right);
            switch (audioModeText[position]) {
                case TvAudioManager.DTV_SOUND_MODE_STEREO:
                    audioModeImageL.setImageDrawable(getResources().getDrawable(
                            R.drawable.mts_audio_left));
                    audioModeImageR.setImageDrawable(getResources().getDrawable(
                            R.drawable.mts_audio_right));
                    break;

                case TvAudioManager.DTV_SOUND_MODE_LEFT:
                    audioModeImageL.setImageDrawable(getResources().getDrawable(
                            R.drawable.mts_audio_left));
                    audioModeImageR.setImageDrawable(getResources().getDrawable(
                            R.drawable.mts_audio_left));
                    break;

                case TvAudioManager.DTV_SOUND_MODE_RIGHT:
                    audioModeImageL.setImageDrawable(getResources().getDrawable(
                            R.drawable.mts_audio_right));
                    audioModeImageR.setImageDrawable(getResources().getDrawable(
                            R.drawable.mts_audio_right));
                    break;

                default:
                    break;

            }

            TextView text = (TextView) view.findViewById(R.id.mts_audio_info_text);
            text.setText(audioLang[position]);
            return view;
        }
    }
}
