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
package com.jrm.localmm.ui.video;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Handler;
import com.jrm.localmm.R;
import com.jrm.localmm.business.adapter.AudioTrackListAdapter;
import com.jrm.localmm.util.Tools;
import com.jrm.localmm.util.Constants;
import com.mstar.android.media.MMediaPlayer;

public class PlaySettingAudioTrackDialog extends Dialog {
    private static final String TAG = "PlaySettingAudioTrackDialog";
    private VideoPlayerActivity context;
    private ListView audioTrackListView;
    private AudioTrackListAdapter adapter;
    private VideoPlaySettingDialog videoPlaySettingDialog;
    private Toast mToast = null;
    private int viewId = 1;
    private boolean isResponseKey = true;
    private long mLastTimeMillis = 0;
    private int mAudioTrackCount;
    private int[] trackDuration;
    private int duration = 1;
    private String strFormatDuration;
    private String[] trackLanguage;
    // Audio track Settings item name
    public static int[] audioTrackSettingName = { R.string.audio_track_number,
            R.string.audio_track_count,
            // R.string.audio_track_name,
            R.string.audio_track_duration, 
            R.string.audio_track_language,
            R.string.audio_track_codec,
            R.string.audio_track_channel,
            R.string.audio_track_name};

    public PlaySettingAudioTrackDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
        viewId = context.getVideoPlayHolder().getViewId();
    }

    public PlaySettingAudioTrackDialog(VideoPlayerActivity context, int theme,
            VideoPlaySettingDialog videoPlaySettingDialog) {
        super(context, theme);
        this.context = context;
        this.videoPlaySettingDialog = videoPlaySettingDialog;
        viewId = context.getVideoPlayHolder().getViewId();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isResponseKey = true;
        }

    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoPlaySettingDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_setting_audiotrack_dialog);
        // instantiation new window
        Window w = getWindow();
        // for the default display data
        Display display = w.getWindowManager().getDefaultDisplay();
        MMediaPlayer.MsTrackInfo trackInfo = null;
        if (Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
            trackInfo =  context.getVideoPlayHolder().
                    getPlayerView(context.getVideoPlayHolder().getViewId()).getMsTrackInfo(0);
            if (trackInfo != null) {
                AudioTrackManager.setAudioTackSettingOpt(context, 3, trackInfo.getLanguage(), viewId);
            }
        } else {
            AudioTrackManager.setAudioTackSettingOpt(context, 3,
                    AudioTrackManager.getInstance().getAudioTrackLanguage(getContext(),
                            context.getVideoPlayHolder().getPlayerView().getMMediaPlayer()),
                    viewId);
        }

        audioTrackListView = (ListView) this
                .findViewById(R.id.playsetting_audiotrack_list);
        adapter = new AudioTrackListAdapter(context, audioTrackSettingName,
                AudioTrackManager.initAudioTackSettingOpt(context, viewId), PlaySettingAudioTrackDialog.this);
        audioTrackListView.setDivider(null);
        audioTrackListView.setAdapter(adapter);
        setListeners();
        // window's title is empty
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.48);
        int height = (int) (display.getHeight() * 0.52);
        // Settings window size
        w.setLayout(width, height);
        // Settings window display position
        w.setGravity(Gravity.CENTER);
        // Settings window properties
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        if (Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
            mAudioTrackCount = context.getVideoPlayHolder().getPlayerView(context.getVideoPlayHolder().
                    getViewId()).getMsAudioTrackCount();
        } else {
            mAudioTrackCount = AudioTrackManager.getInstance().getAudioTrackCount(
                    context.getVideoPlayHolder().getPlayerView().getMMediaPlayer()
                    ) ;
        }

        AudioTrackManager.setAudioTackSettingOpt(context, 1, "" + mAudioTrackCount, viewId);
        if (mAudioTrackCount > 0) {
            trackDuration = new int[mAudioTrackCount];
            if (Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
                trackDuration[0] = context.getVideoPlayHolder().getPlayerView(context.getVideoPlayHolder().getViewId()).getDuration();
            } else {
                trackDuration[0] = AudioTrackManager.getInstance().getAudioTrackDuration(
                       context.getVideoPlayHolder().getPlayerView().getMMediaPlayer());
            }
            if (trackDuration[0] >= 1000) {
                duration = trackDuration[0] / 1000;
                strFormatDuration = String.format("%02d:%02d:%02d",
                        duration / 3600, duration % 3600 / 60,
                        duration % 60);
                AudioTrackManager.setAudioTackSettingOpt(context, 2, strFormatDuration, viewId);
            }

            trackLanguage = new String[mAudioTrackCount];
            if (Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
                if (trackInfo != null) {
                    trackLanguage[0] = trackInfo.getLanguage();
                }
            } else {
                trackLanguage[0] = AudioTrackManager.getInstance().getAudioTrackLanguage(
                        context.getBaseContext(),context.getVideoPlayHolder().getPlayerView().getMMediaPlayer());
            }
            Log.i(TAG, "trackLanguage[0]:" + trackLanguage[0]);
            AudioTrackManager.setAudioTackSettingOpt(context, 3, trackLanguage[0], viewId);
            String ads[] = new String[3];
            DivxInfoDialog.getDivxAudioInfo(ads,context);
            for (int i=0;i<3;i++) {
                AudioTrackManager.setAudioTackSettingOpt(context,4+i,ads[i],viewId);
            }
        }
    }

    ResponseTask mResponseTask = new ResponseTask();
    Handler mHandler = new Handler();

    class ResponseTask {
        private volatile boolean mResponseAble = true;
        private long mToastShowTime = 0;
        private Thread mRunner = null;
        public synchronized boolean responseAble() {
            return mResponseAble;
        }
        public synchronized void runTask(final int step) {
            if (1 == mAudioTrackCount) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis-mLastTimeMillis < 1000) {
                mLastTimeMillis = currentTimeMillis;
                showToastTip(context.getResources().getString(R.string.video_audio_track_switch_info));
                return;
            }
            mLastTimeMillis = currentTimeMillis;

            if (!mResponseAble && mRunner.isAlive()) {
                long now = System.currentTimeMillis();
                if ((now - mToastShowTime) > 3600) {
                    mToastShowTime = now;
                    Toast.makeText(context,R.string.busy_tip, Toast.LENGTH_SHORT).show();
                }
                return;
            } else {
                mResponseAble = false;
            }
            mRunner = new Thread() {
                public void run() {
                    changeAudioTrack(step);
                    mHandler.post(new Runnable() {
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    // Sleep to avoid set audio track too often, just need 998 !
                    try {
                        Thread.sleep(998);
                    }catch(Exception e){}
                    mResponseAble = true;
                }
                };
            mRunner.start();
        }
    }
    private void setListeners() {
        audioTrackListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.i(TAG, "------onItemClick ---------- position:" + position);
                handleMidClick(position);
            }
        });
        audioTrackListView.setOnKeyListener(onkeyListenter);
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i(TAG, "------onkeyListenter ---------- keyCode:" + keyCode + " getAction:" + event.getAction());
            if(!isResponseKey){
                return false;
            }
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN: {
                    isResponseKey = false;
                    handler.sendEmptyMessageAtTime(0, 300);
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT: {
                            int position = audioTrackListView.getSelectedItemPosition();
                            handleImageClick(position, true);
                            break;
                        }
                        case KeyEvent.KEYCODE_DPAD_RIGHT: {
                            int position = audioTrackListView.getSelectedItemPosition();
                            handleImageClick(position, false);
                            break;
                        }
                    }
                }
            }
            return false;
        }
    };

    public void handleImageClick(int position, boolean bLeftImageClick) {
        Log.i(TAG, "------ handleImageClick -------- position:" + position + " bLeftImageClick:" + bLeftImageClick);
        if (bLeftImageClick) {
            if (position == 0) {
                mResponseTask.runTask(-1);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            if (position == 0) {
                mResponseTask.runTask(1);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void handleMidClick(int position) {
        Log.i(TAG, "----- handleMidClick ----- position:" + position);
        /*
        switch (position) {
        case 0:
            changeAudioTrack(1);
            adapter.notifyDataSetChanged();
            // dismiss();
            // SubtitleListDialog subtitleListDialog = new
            // SubtitleListDialog(context,R.style.dialog);
            // subtitleListDialog.show();
            break;
        case 1:
            break;
        case 2:
            break;
        case 3:
            break;
        default:
            break;
        }
        */
        if (0 == position) {
            mResponseTask.runTask(1);
        }
    }

    private void changeAudioTrack(int step) {
        Log.i(TAG, "---changeAudioTrack--- step:" + step);

        if (step == 1 || step == -1) {
            //VideoPlayerActivity videoPlayActivity = (VideoPlayerActivity) context;
            //String count = Tools.getAudioTackSettingOpt(1, viewId);
            //int iCount = Integer.parseInt(count);//videoPlayActivity.getAudioTrackCount();
            Log.d(TAG, "Track number: " + mAudioTrackCount);
            if (mAudioTrackCount == 0) {
                return;
            }
            String audio = AudioTrackManager.getAudioTackSettingOpt(0, viewId);
            String value = context.getString(R.string.audio_track_setting_0_value_1);
            int numberAudioTrack = Integer.parseInt(audio.substring(
                    value.length(), audio.length())) - 1;
            if (mAudioTrackCount == 1) {
                AudioTrackManager.setAudioTackSettingOpt(
                        context,
                        0,
                        context.getString(R.string.audio_track_setting_0_value_1)
                                + "1", viewId);
                return;
            }
            numberAudioTrack = (numberAudioTrack + step) % mAudioTrackCount;
            if (numberAudioTrack<0) {
                numberAudioTrack = mAudioTrackCount - 1;
            }
            if (Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
                context.getVideoPlayHolder().getPlayerView(context.getVideoPlayHolder().
                        getViewId()).setMsTrackIndex(MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_AUDIO, numberAudioTrack);
            } else {
                AudioTrackManager.getInstance().setAudioTrack(
                        context.getVideoPlayHolder().getPlayerView().getMMediaPlayer()
                        , numberAudioTrack);
            }
            if (mAudioTrackCount > 0) {
                if (Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
                    MMediaPlayer.MsTrackInfo trackInfo =  context.getVideoPlayHolder().
                            getPlayerView(context.getVideoPlayHolder().getViewId()).getMsTrackInfo(numberAudioTrack);
                    if (trackInfo != null) {
                        trackLanguage[numberAudioTrack] = trackInfo.getLanguage();
                        Log.i(TAG, "trackLanguage[numberAudioTrack]:" + trackLanguage[numberAudioTrack]);
                    }
                }

                if (trackDuration[numberAudioTrack] <= 0) {
                    if (Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
                        trackDuration[numberAudioTrack] = context.getVideoPlayHolder().getPlayerView(context.getVideoPlayHolder().getViewId()).getDuration();
                    } else {
                        trackDuration[numberAudioTrack] = AudioTrackManager.getInstance().getAudioTrackDuration(
                                context.getVideoPlayHolder().getPlayerView().getMMediaPlayer());
                    }

                }
                if (trackDuration[numberAudioTrack] >= 1000) {
                    duration = trackDuration[numberAudioTrack] / 1000;
                    strFormatDuration = String.format("%02d:%02d:%02d",
                            duration / 3600, duration % 3600 / 60,
                            duration % 60);
                    AudioTrackManager.setAudioTackSettingOpt(context, 2, strFormatDuration, viewId);
                }
                if (!Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
                    trackLanguage[numberAudioTrack] = AudioTrackManager.getInstance().getAudioTrackLanguage(
                           context.getBaseContext(),context.getVideoPlayHolder().getPlayerView().getMMediaPlayer());
                }

                if (trackLanguage[numberAudioTrack].equals("undefined")) {
                    trackLanguage[numberAudioTrack] = "Undetermined";
                }
                AudioTrackManager.setAudioTackSettingOpt(context, 3, trackLanguage[numberAudioTrack], viewId);
            }
            numberAudioTrack++;
            value = value + numberAudioTrack;
            AudioTrackManager.setAudioTackSettingOpt(context, 0, value, viewId);
            String ads[] = new String[3];
            DivxInfoDialog.getDivxAudioInfo(ads,context);
            for (int i=0;i<3;i++) {
                AudioTrackManager.setAudioTackSettingOpt(context,4+i,ads[i],viewId);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void showToastTip(String strMessage) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, strMessage, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
