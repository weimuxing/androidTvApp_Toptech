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

package com.mstar.tv.tvplayer.ui.pvr;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.text.format.Time;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.PvrManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.vo.PvrFileInfo;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.tv.tvplayer.ui.component.PasswordCheckDialog;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.util.Constant;

public class PVRFullPageBrowserActivity extends MstarBaseActivity {
    private static final String TAG = "PVRFullPageBrowserActivity";

    private Activity mActivity = null;

    private ArrayList<listViewHolder> mPvrList = new ArrayList<listViewHolder>();

    private Bitmap[] mBitmapList = null;

    private Bitmap mBitmap = null;

    private boolean mIsAscend = true;

    private boolean mIsItemClicked = false;

    private boolean mIsPreviewSetted = false;

    private boolean mIsRecordingItem = false;

    private boolean mIsToPromptPassword = false;

    private boolean mIsTransparency = false;

    private Handler mHandler = new Handler();

    private int mCurrentPosition = 0;

    private int mCurrentRecording = -1;

    private int mCurSortKey = PvrManager.PVR_FILE_INFO_SORT_FILENAME;

    private ListView mListview;

    private TvPlayerEventListener mTvPlayerEventListener = null;

    private PasswordCheckDialog mPasswordLock = null;

    private PVRListViewAdapter mPvrAdapter;

    private SurfaceView mPVRPlaybackView = null;

    private TextProgressBar mPVRPlaybackProgress = null;

    private TextView mTextViewBlueKey;

    private TextView mTextViewGreenKey;

    private TextView mTitle;

    private TextView mTotalTime;

    private TextView mTextLcnDataTime;

    private TvPvrManager mPvrManager = null;

    private TvAudioManager mAudioManager = null;

    private TvTimerManager mTimeManager = null;

    private USBDiskSelecter mUsbSelecter = null;

    private UsbReceiver mUsbReceiver = null;

    public class PVRListViewAdapter extends BaseAdapter {
        ArrayList<listViewHolder> mData = null;

        private Context mContext;

        public PVRListViewAdapter(Context context, ArrayList<listViewHolder> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pvr_listview_item, null);
            ImageView tmpImage = (ImageView) convertView.findViewById(R.id.player_recording_file);
            if (mCurrentRecording == position) {
                tmpImage.setVisibility(View.VISIBLE);
                mIsRecordingItem = true;
            } else {
                tmpImage.setVisibility(View.INVISIBLE);
                mIsRecordingItem = false;
            }
            TextView tmpText = (TextView) convertView.findViewById(R.id.pvr_listview_item_lcn);
            tmpText.setText(mData.get(position).getLcn());
            tmpText = (TextView) convertView.findViewById(R.id.pvr_listview_item_channel);
            tmpText.setText(mData.get(position).getChannel());
            tmpText = (TextView) convertView.findViewById(R.id.pvr_listview_item_program);
            tmpText.setText(mData.get(position).getProgramService());
            return convertView;
        }
    }

    private class listViewHolder {
        private String fileName = null;

        private String logicalChannelNum = null;

        private String channel = null;

        private String programService = null;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String strfileName) {
            fileName = strfileName;
        }

        public String getLcn() {
            return logicalChannelNum;
        }

        public void setLcn(String pvrTextViewLcn) {
            logicalChannelNum = pvrTextViewLcn;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String pvrTextViewChannel) {
            channel = pvrTextViewChannel;
        }

        public String getProgramService() {
            return programService;
        }

        public void setProgramService(String pvrTextViewProgramService) {
            programService = pvrTextViewProgramService;
        }
    }

    private void settingPasswardDialog() {
        mPasswordLock = new PasswordCheckDialog(this, R.layout.password_check_dialog_4_digits) {
            @Override
            public String onCheckPassword() {
                return String.format("%04d", TvParentalControlManager.getInstance()
                        .getParentalPassword());
            }

            @Override
            public void onPassWordCorrect() {
                Toast.makeText(mActivity,
                        mActivity.getResources().getString(R.string.str_check_password_pass),
                        Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPvrManager.unlockPlayback();
                    }
                }).start();
                dismiss();
            }

            @Override
            public void onKeyDown(int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_ENTER != keyCode) {
                    mActivity.onKeyDown(keyCode, keyEvent);
                }
            }

            @Override
            public void onBackPressed() {
                Toast.makeText(mActivity,
                        mActivity.getResources().getString(R.string.str_check_password_invalid),
                        Toast.LENGTH_SHORT).show();
                dismiss();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pvr_full_page_browser);
        mActivity = this;
        mListview = (ListView) findViewById(R.id.pvr_listview);
        mPVRPlaybackView = (SurfaceView) findViewById(R.id.pvr_lcn_img);
        mPVRPlaybackProgress = (TextProgressBar) findViewById(R.id.pvr_progressBar);
        mTextViewGreenKey = (TextView) findViewById(R.id.pvr_tips_green);
        mTextViewBlueKey = (TextView) findViewById(R.id.pvr_tips_blue);
        createPVRPlaybackView();
        mPvrManager = TvPvrManager.getInstance();
        mAudioManager = TvAudioManager.getInstance();
        mTimeManager = TvTimerManager.getInstance();
        mTitle = (TextView) findViewById(R.id.pvr_lcn_title);
        mTotalTime = (TextView) findViewById(R.id.total_time);
        mTextLcnDataTime = (TextView) findViewById(R.id.pvr_lcn_data);
        mTextLcnDataTime.setText("");
        settingPasswardDialog();
        if (mTvPlayerEventListener == null) {
            mTvPlayerEventListener = new TvPlayerEventListener();
            TvChannelManager.getInstance().registerOnTvPlayerEventListener(mTvPlayerEventListener);
        }

        mUsbSelecter = new USBDiskSelecter(this) {
            @Override
            public void onItemChosen(int position, String diskLabel, String diskPath) {
                String strMountPath = diskPath + "/_MSTPVR";
                if (diskPath.isEmpty()) {
                    Log.e(TAG, " USB Disk Path is NULL !!!");
                    return;
                }
                Log.d(TAG, "USB Disk Path = " + diskPath);
                mPvrManager.clearMetadata();
                mPvrManager.setPvrParams(diskPath, (short) 2);
                mPvrManager.createMetadata(strMountPath);
                init();
            }
        };

        /* Not support when input source not DTV */
        if (TvCommonManager.INPUT_SOURCE_DTV != TvCommonManager.getInstance()
                .getCurrentTvInputSource()) {
            Log.d(TAG, "input source not DTV");
            finish();
            return;
        }
        mAudioManager.enableAudioMute(TvAudioManager.AUDIO_MUTE_BYUSER);

        int usbDriverCount = mUsbSelecter.getDriverCount();
        String bestPath = mUsbSelecter.getBestDiskPath();

        Log.e(TAG, "bestPath::" + bestPath);
        if (usbDriverCount <= 0) {
            Toast.makeText(this, R.string.str_pvr_insert_usb, Toast.LENGTH_SHORT).show();
            return;
        }
        if (USBDiskSelecter.NO_DISK.equals(bestPath)) {
            Toast.makeText(this, R.string.str_pvr_insert_usb, Toast.LENGTH_SHORT).show();
            return;
        }
        if (USBDiskSelecter.CHOOSE_DISK.equals(bestPath)) {
            mUsbSelecter.start();
            return;
        } else {
            String strMountPath = bestPath + "/_MSTPVR";
            if (bestPath.isEmpty()) {
                Log.e(TAG, "USB Disk Path is NULL !!!");
                return;
            }
            Log.d(TAG, "USB Disk Path = " + bestPath);
            mPvrManager.clearMetadata();
            mPvrManager.setPvrParams(bestPath, (short) 2);
            mPvrManager.createMetadata(strMountPath);
            init();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        Intent intent = new Intent(TvIntent.ACTION_PVR_ENTER_FULL_BROWSER);
        LocalBroadcastManager.getInstance(this).sendBroadcastSync(intent);

        Handler focusHandler = new Handler();
        if (mListview != null && mListview.isInTouchMode()) {
            focusHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListview.setFocusableInTouchMode(true);
                    mListview.requestFocusFromTouch();
                    mListview.requestFocus();
                    mListview.setSelection(0);
                }
            }, 500);
        } else if (mListview != null) {
            mListview.setSelection(0);
        }
        showPasswordLock(mIsToPromptPassword);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");

        Intent intent = new Intent(TvIntent.ACTION_PVR_LEAVE_FULL_BROWSER);
        LocalBroadcastManager.getInstance(this).sendBroadcastSync(intent);

        SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
        int subtitlePos = settings.getInt("subtitlePos", 0);
        TvChannelManager.getInstance().closeSubtitle();
        if (subtitlePos > 0) {
            TvChannelManager.getInstance().openSubtitle((subtitlePos - 1));
        }
        /*
         * When press home key will 1. BrowserActivity onPause 2. RootActivity
         * onStop (change input source) 3. BrowserActivity onStop SetWindow
         * before change input source, prevent dtv resource be killed after
         * change input source
         */
        stopPlaybacking();
        scaleToFullScreen();
        super.onPause();
        showPasswordLock(false);
    }

    private void init() {
        if (!isPVRAvailable()) {
            return;
        }

        mPvrAdapter = new PVRListViewAdapter(this, mPvrList);

        mListview.setAdapter(mPvrAdapter);
        mListview.setDividerHeight(0);
        mListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                mCurrentPosition = pos;
                mIsItemClicked = true;
                finish();
            }
        });
        mListview.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mCurrentPosition = pos;
                playFile(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setSortAscending(mIsAscend);
        constructRecorderList();
        String fileName = getFileNameByIndex(0);
        int total = mPvrManager.getRecordedFileDurationTime(fileName);
        mTotalTime.setText(getTimeString(total));
        new PlayBackProgress().start();
    }

    private boolean isPVRAvailable() {
        Log.d(TAG, "mPvrManager.getPvrFileNumber() = " + mPvrManager.getPvrFileNumber());
        if (mPvrManager.getPvrFileNumber() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        registerUSBDetector();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
        mAudioManager.disableAudioMute(TvAudioManager.AUDIO_MUTE_BYUSER);
        if (null != mUsbSelecter) {
            mUsbSelecter.dismiss();
            unregisterReceiver(mUsbReceiver);
            mUsbReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        if (null != mTvPlayerEventListener) {
            TvChannelManager.getInstance()
                    .unregisterOnTvPlayerEventListener(mTvPlayerEventListener);
            mTvPlayerEventListener = null;
        }
        if (TvCommonManager.INPUT_SOURCE_DTV != TvCommonManager.getInstance()
                .getCurrentTvInputSource()) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                /*
                 * check input source, prevent goto PVRActivity input source not
                 * DTV
                 */
                if (true == mIsItemClicked) {
                    Log.d(TAG, "ItemClicked start PVRActivity");
                    Intent intent = new Intent(TvIntent.ACTION_PVR_ACTIVITY);
                    intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_PLAYBACK_FROM_BROWSER);
                    intent.putExtra(Constant.PVR_FILENAME, getFileNameByIndex(mCurrentPosition));
                    if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                        mActivity.startActivity(intent);
                    }
                    return;
                }
                if (true == mPvrManager.isRecording()) {
                    Log.d(TAG, "isRecording start PVRActivity");
                    Intent intent = new Intent(TvIntent.ACTION_PVR_ACTIVITY);
                    intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_RECORD_START);
                    if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                        mActivity.startActivity(intent);
                    }
                    return;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bRet = false;
        if (true == isPVRAvailable()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_PROG_RED: {
                    if (mCurrentRecording == mCurrentPosition) {
                        /* cannot delete current recording file */
                        Toast.makeText(this, R.string.str_pvr_is_recording, Toast.LENGTH_SHORT)
                                .show();
                        break;
                    }
                    String fileName = getFileNameByIndex(mCurrentPosition);
                    Log.i(TAG, "get fileName:" + fileName);
                    if ((null == fileName) || (true == fileName.isEmpty())) {
                        return false;
                    }
                    stopPlaybacking();
                    mPvrManager.deletefile(0, fileName);
                    constructRecorderList();

                    mTextLcnDataTime.setText("");
                    int size = mListview.getAdapter().getCount();
                    if (0 < size) {
                        if (size <= mCurrentPosition) {
                            mCurrentPosition--;
                        }
                        mListview.setSelection(mCurrentPosition);
                        playFile(mCurrentPosition);
                    }
                    bRet = true;
                    break;
                }
                case KeyEvent.KEYCODE_PROG_BLUE: {
                    stopPlaybacking();
                    if (mCurSortKey < PvrManager.PVR_FILE_INFO_SORT_PROGRAM) {
                        mCurSortKey++;
                    } else {
                        mCurSortKey = PvrManager.PVR_FILE_INFO_SORT_TIME;
                    }
                    setSortKey(mCurSortKey);
                    constructRecorderList();
                    int nIdx = mListview.getSelectedItemPosition();
                    playRecordedFile(nIdx);
                    bRet = true;
                    break;
                }
                case KeyEvent.KEYCODE_PROG_GREEN: {
                    stopPlaybacking();
                    setSortAscending(!mIsAscend);
                    constructRecorderList();
                    int nIdx = mListview.getSelectedItemPosition();
                    playRecordedFile(nIdx);
                    bRet = true;
                    break;
                }
            }
        }
        if (bRet == false) {
            bRet = super.onKeyDown(keyCode, event);
        }
        return bRet;
    }

    private void setSortKey(final int key) {
        boolean iskeyAvailable = true;
        mCurSortKey = key;
        if (key == PvrManager.PVR_FILE_INFO_SORT_TIME) {
            mTextViewBlueKey.setText(getResources().getString(R.string.str_time_time));
        } else if (key == PvrManager.PVR_FILE_INFO_SORT_LCN) {
            mTextViewBlueKey.setText(getResources().getString(R.string.str_pvr_lcn_textview));
        } else if (key == PvrManager.PVR_FILE_INFO_SORT_CHANNEL) {
            mTextViewBlueKey.setText(getResources().getString(
                    R.string.str_schedule_list_channel_name));
        } else if (key == PvrManager.PVR_FILE_INFO_SORT_PROGRAM) {
            mTextViewBlueKey.setText(getResources().getString(
                    R.string.str_schedule_list_programmer_title));
        } else {
            mTextViewBlueKey.setText(getResources().getString(
                    R.string.str_pvr_program_service_tips_blue));
            iskeyAvailable = false;
        }
        if (iskeyAvailable)
            mPvrManager.setMetadataSortKey(mCurSortKey);
    }

    private void setSortAscending(final boolean isAscend) {
        mPvrManager.setMetadataSortAscending(isAscend);
        mIsAscend = isAscend;
        if (isAscend) {
            mTextViewGreenKey.setText(getResources().getString(
                    R.string.str_pvr_program_service_tips_green));
        } else {
            mTextViewGreenKey.setText(getResources().getString(
                    R.string.str_pvr_program_service_tips_descend));
        }
    }

    private void registerUSBDetector() {
        mUsbReceiver = new UsbReceiver();
        IntentFilter iFilter;
        iFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        iFilter.addDataScheme("file");
        registerReceiver(mUsbReceiver, iFilter);
        iFilter = new IntentFilter(Intent.ACTION_MEDIA_EJECT);
        iFilter.addDataScheme("file");
        registerReceiver(mUsbReceiver, iFilter);
    }

    private void createPVRPlaybackView() {
        Callback callback = new Callback() {

            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    Log.d(TAG, "surfaceCreated!");
                    TvManager.getInstance().getPlayerManager().setDisplay(holder);
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
            }
        };
        mPVRPlaybackView.getHolder().addCallback(callback);
    }

    private VideoWindowType getPreviewWindowType() {
        VideoWindowType videoWindowType = new VideoWindowType();
        int[] location = new int[2];
        mPVRPlaybackView.getLocationOnScreen(location);
        videoWindowType.x = location[0];
        videoWindowType.y = location[1];
        if (null != mPVRPlaybackView) {
            videoWindowType.height = mPVRPlaybackView.getHeight();
            videoWindowType.width = mPVRPlaybackView.getWidth();
        }
        return videoWindowType;
    }

    private boolean isPreviewWindowAvaliable() {
        VideoWindowType videoWindowType = getPreviewWindowType();
        Log.e(TAG, "the [x,y][w,h] => [" + videoWindowType.x + "," + videoWindowType.y + "]["
                + videoWindowType.width + "," + videoWindowType.height + "]");
        if ((0 == videoWindowType.width) || (0 == videoWindowType.height)) {
            return false;
        }
        return true;
    }

    private boolean setPreviewWindow() {
        if (true != mIsPreviewSetted) {
            if (true == isPreviewWindowAvaliable()) {
                VideoWindowType videoWindowType = getPreviewWindowType();
                mPvrManager.setPlaybackWindow(videoWindowType, 1920, 1080);
                mIsPreviewSetted = true;
                return true;
            }
        }
        return false;
    }

    private void toastErrorCode(int errorCode) {
        String[] dispInfo = getResources().getStringArray(R.array.str_arr_pvr_error_code);
        try {
            Toast.makeText(this, dispInfo[errorCode], Toast.LENGTH_SHORT).show();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaybacking() {
        if (true == mPvrManager.isPlaybacking()) {
            mPvrManager.stopPlayback();
        }
        mAudioManager.enableAudioMute(TvAudioManager.AUDIO_MUTE_BYUSER);
        mTitle.setText("");
        mTotalTime.setText(getTimeString(0));
        setTransparency(false);
    }

    private void playRecordedFile(int pos) {
        String fileName = getFileNameByIndex(pos);
        if ((null == fileName) || (true == fileName.isEmpty())) {
            return;
        }
        if (false == isPreviewWindowAvaliable()) {
            return;
        }
        Log.d(TAG, "current playback fileName = " + fileName);

        PvrFileInfo fileInfo = mPvrManager.getPvrFileInfo(pos, mPvrManager.getMetadataSortKey());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy/MM/dd EEE");
        mTextLcnDataTime.setText(sdf.format(new Date((((long)fileInfo.recordStartTime)*1000))));

        if (mPvrManager.getCurPlaybackingFileName().equals(fileName))
            return;
        stopPlaybacking();
        int playbackStatus = mPvrManager.startPvrPlayback(fileName);
        int total = mPvrManager.getRecordedFileDurationTime(fileName);
        mPVRPlaybackProgress.setMax(total);
        mTotalTime.setText(getTimeString(total));
        if (TvPvrManager.PVR_STATUS_SUCCESS != playbackStatus) {
            Log.e(TAG, "playRecordedFile error code:" + playbackStatus);
            toastErrorCode(playbackStatus);
            return;
        }
        mAudioManager.disableAudioMute(TvAudioManager.AUDIO_MUTE_BYUSER);
        setPreviewWindow();
        setTransparency(true);
        TvChannelManager.getInstance().closeSubtitle();
    }

    private String getTimeString(int seconds) {
        String hour = "00";
        String minute = "00";
        String second = "00";
        if (seconds % 60 < 10)
            second = "0" + seconds % 60;
        else
            second = "" + seconds % 60;

        int offset = seconds / 60;
        if (offset % 60 < 10)
            minute = "0" + offset % 60;
        else
            minute = "" + offset % 60;

        offset = seconds / 3600;
        if (offset < 10)
            hour = "0" + offset;
        else
            hour = "" + offset;
        return hour + ":" + minute + ":" + second;
    }

    private void scaleToFullScreen() {
        if (false == mIsPreviewSetted) {
            return;
        }
        setTransparency(false);
        VideoWindowType videoWindowType = new VideoWindowType();
        videoWindowType.height = 0;
        videoWindowType.width = 0;
        videoWindowType.x = 0xFFFF;
        videoWindowType.y = 0xFFFF;
        mPvrManager.setPlaybackWindow(videoWindowType, 0, 0);
        mIsPreviewSetted = false;
    }

    private void constructRecorderList() {
        final int pvrFileNumber = mPvrManager.getPvrFileNumber();
        final String strFileName = mPvrManager.getCurRecordingFileName();
        final int nSortKey = mPvrManager.getMetadataSortKey();
        PvrFileInfo fileInfo = new PvrFileInfo();
        String pvrFileLcn = null;
        String pvrFileServiceName = null;
        String pvrFileEventName = null;
        mPvrList.clear();

        for (int i = 0; i < pvrFileNumber; i++) {
            listViewHolder vh = new listViewHolder();
            fileInfo = mPvrManager.getPvrFileInfo(i, nSortKey);
            pvrFileLcn = "CH " + mPvrManager.getFileLcn(i);
            pvrFileServiceName = mPvrManager.getFileServiceName(fileInfo.filename);
            pvrFileEventName = mPvrManager.getFileEventName(fileInfo.filename);
            if (strFileName.equals(fileInfo.filename)) {
                mCurrentRecording = i;
            }
            vh.setFileName(fileInfo.filename);
            vh.setLcn(pvrFileLcn);
            vh.setChannel(pvrFileServiceName);
            vh.setProgramService(pvrFileEventName);
            mPvrList.add(vh);
        }

        mPvrAdapter.notifyDataSetChanged();
        mListview.invalidate();
    }

    private String getEventNameByIndex(int index) {
        final String emptyString = "";
        listViewHolder listItem = null;
        try {
            listItem = mPvrList.get(index);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (listItem == null) {
            return emptyString;
        }
        final String name = listItem.getProgramService();
        if (name != null) {
            return name;
        } else {
            return emptyString;
        }
    }

    private String getFileNameByIndex(int index) {
        final String emptyString = "";
        listViewHolder listItem = null;
        try {
            listItem = mPvrList.get(index);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        if (listItem == null) {
            return emptyString;
        }
        final String name = listItem.getFileName();
        if (name != null) {
            return name;
        } else {
            return emptyString;
        }
    }

    private String getSelectedFileName() {
        if (mPvrList.isEmpty()) {
            return null;
        }
        PvrFileInfo fileInfo = new PvrFileInfo();
        fileInfo = mPvrManager.getPvrFileInfo(mListview.getSelectedItemPosition(),
                mPvrManager.getMetadataSortKey());
        return fileInfo.filename;
    }

    private Bitmap decodeFile(String filePath) {
        Log.d(TAG, "filepath in decode file .. " + filePath);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // scale size
        final int REQUIRED_SIZE = 100;
        final int H = 50;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < H) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Log.d(TAG, "decode file ........... " + filePath);
        mBitmap = BitmapFactory.decodeFile(filePath, o2);
        return mBitmap;
    }

    /*
     * setVisibility/setBackgroundColor will do in background it will not be
     * setted immediately.
     */
    private void setTransparency(boolean transparencyOnOff) {
        if (mIsTransparency == transparencyOnOff) {
            return;
        }
        mIsTransparency = transparencyOnOff;
        if (transparencyOnOff)
            mPVRPlaybackView.setVisibility(View.VISIBLE);
        else
            mPVRPlaybackView.setVisibility(View.INVISIBLE);
    }

    private void showPasswordLock(boolean bShow) {
        if (true == bShow) {
            mPasswordLock.show();
        } else {
            mPasswordLock.dismiss();
        }
    }

    private class PlayBackProgress extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while (!isFinishing()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            int currentTime = mPvrManager.getCurPlaybackTimeInSecond();
                            if (mIsRecordingItem) {
                                int total = (mPvrManager.isRecording()) ? TvPvrManager
                                        .getInstance().getCurRecordTimeInSecond() : TvPvrManager
                                        .getInstance().getCurPlaybackTimeInSecond();
                                mTotalTime.setText(getTimeString(total));
                                mPVRPlaybackProgress.setMax(total);
                            }
                            Log.e(TAG, "current time = " + currentTime);
                            mPVRPlaybackProgress.setTextProgress(getTimeString(currentTime),
                                    currentTime);
                        }
                    });
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class UsbReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Uri uri = intent.getData();
            String path = uri.getPath();
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {

            } else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                String mountPath = null;
                mountPath = mPvrManager.getPvrMountPath();
                Log.e(TAG, "mountPath:" + mountPath);
                if (path.equals(mountPath)) {
                    mPvrManager.clearMetadata();
                    finish();
                }
            }
        }
    }

    private void playFile(int pos) {
        mTitle.setText(getEventNameByIndex(pos));
        if (mCurrentRecording == pos) {
            mIsRecordingItem = true;
        } else {
            mIsRecordingItem = false;
        }
        playRecordedFile(pos);
    }

    private class TvPlayerEventListener implements OnTvPlayerEventListener {

        @Override
        public boolean onScreenSaverMode(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onHbbtvUiEvent(int what, HbbtvEventInfo eventInfo) {
            return false;
        }

        @Override
        public boolean onPopupDialog(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackTime(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackSpeedChange(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordTime(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordSize(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordStop(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackBegin(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesBefore(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesAfter(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyOverRun(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyUsbRemoved(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyCiPlusProtection(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackStop(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyParentalControl(int what, int arg1) {
            final boolean isLock = (arg1 == 1);
            mIsToPromptPassword = isLock;
            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    showPasswordLock(isLock);
                }
            });
            return true;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyCiPlusRetentionLimitUpdate(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onTvProgramInfoReady(int what) {
            return false;
        }

        @Override
        public boolean onSignalLock(int what) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            return false;
        }

        @Override
        public boolean onEpgUpdateList(int what, int arg1) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePip(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePop(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableDualView(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableTravelingMode(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onDtvPsipTsUpdate(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onEmerencyAlert(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onDtvChannelInfoUpdate(int what, int info, int arg2) {
            return false;
        }
    }
}
