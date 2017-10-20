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

package com.mstar.tvsetting.switchinputsource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.database.SQLException;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.view.InputDevice;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.Gravity;

import com.mstar.tvsetting.R;
import com.mstar.android.MKeyEvent;
import com.mstar.android.MIntent;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.vo.EnumPipModes;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscMainListChannelInformation;
import com.mstar.tvsetting.hotkey.InputSourceItem;
import com.mstar.tvsetting.hotkey.PreviewSourceHandle;
import com.mstar.tvsetting.hotkey.PreviewWindowHandle;
import com.mstar.tvsetting.hotkey.ProgressActivity;
import com.mstar.tvsetting.hotkey.SwitchPageHelper;

public class PictrueChangeActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "PictrueChangeActivity";

    private ArrayList<InputSourceItem> mGalleryItemList = new ArrayList<InputSourceItem>();

    private InputSourceGalleryAdapter mInputSourceGalleryAdapter = null;

    private int mLastPosition = 0;

    private int mCurrentPosition = 0;

    private int mTotalsourceNum = 0;
    private int mCount = 0;

    private int[] mSourceListFlag = new int[TvCommonManager.INPUT_SOURCE_NUM];

    private PreviewWindowHandle mPreviewWindowHandle = null;

    private PreviewSourceHandle mPreviewSourceHandle = null;

    private static final int[] mSourceListInvisible = {
            TvCommonManager.INPUT_SOURCE_STORAGE, TvCommonManager.INPUT_SOURCE_KTV,
            TvCommonManager.INPUT_SOURCE_JPEG, TvCommonManager.INPUT_SOURCE_DTV2,
            TvCommonManager.INPUT_SOURCE_STORAGE2, TvCommonManager.INPUT_SOURCE_DIV3,
            TvCommonManager.INPUT_SOURCE_SCALER_OP, TvCommonManager.INPUT_SOURCE_RUV
    };

    private static final int[] mSourceSelectedResIds = {
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview,
    };

    private static final int[] mSourceUnselectedResIds = {
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview, R.drawable.no_preview,
            R.drawable.no_preview, R.drawable.no_preview,
    };

    private static final int[] mSourceNoSignalResIds = {
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
            R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal, R.drawable.no_signal,
    };

    private int mInputSource = TvCommonManager.INPUT_SOURCE_ATV;

    private int mAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_NONE;

    private ArrayList<Integer> mKeyQueue = new ArrayList<Integer>();

    private AudioManager audiomanager;

    private static int currentVolume = 0;

    private static final int KEYQUEUE_SIZE = 4;

    // the password of entrying Design menu
    private static final String GOODKEYCODES = String
            .valueOf(KeyEvent.KEYCODE_DPAD_UP)
            + String.valueOf(KeyEvent.KEYCODE_DPAD_UP)
            + String.valueOf(KeyEvent.KEYCODE_DPAD_DOWN)
            + String.valueOf(KeyEvent.KEYCODE_DPAD_DOWN);

    private static final String DESIGNMENUKEYCODES = String
            .valueOf(KeyEvent.KEYCODE_2)
            + String.valueOf(KeyEvent.KEYCODE_5)
            + String.valueOf(KeyEvent.KEYCODE_8)
            + String.valueOf(KeyEvent.KEYCODE_0);

    // the password of entrying factory menu
    private static final String FACTORYEYCODES = String
            .valueOf(KeyEvent.KEYCODE_DPAD_UP)
            + String.valueOf(KeyEvent.KEYCODE_DPAD_UP)
            + String.valueOf(KeyEvent.KEYCODE_DPAD_DOWN)
            + String.valueOf(KeyEvent.KEYCODE_DPAD_DOWN);

    private static final String FACTORYMENUKEYCODES = String
            .valueOf(KeyEvent.KEYCODE_1)
            + String.valueOf(KeyEvent.KEYCODE_9)
            + String.valueOf(KeyEvent.KEYCODE_7)
            + String.valueOf(KeyEvent.KEYCODE_9);

    private static final int SETIS_START = -100;

    private static final int SETIS_END_COMPLETE = -101;

    private final int TTS_SPEAK_FOCUS_ITEM = -102;

    private final int TTS_DELAY_TIME_100MS = 100;

    private final int TTS_DELAY_TIME_800MS = 800;

    private static final int FUNCTION_DISABLED = 0;

    private static final int CHANNELMANAGER_ATSC_ATV_ID = -1;

    private StaticSourceSettingDialog mStaticSourceDialog = null;

    private TvCommonManager mTvCommonmanager = null;

    private TvChannelManager mTvChannelManager = null;

    private TvS3DManager mTvS3DManager = null;

    private TvPvrManager mTvPvrManager = null;

    public TvPipPopManager mTvPipPopManager = null;

    private int mInputSourceOfRecording = 0;

    private AlertDialog mStopRecordDialog = null;

    private SourceViewBroadcastReceiver mSourceViewBroadcastReceiver = null;

    private ThreeDModeObserver mObserverThreeDMode = null;

    private boolean mIsSourceNeedSwitch = false;

    private boolean mIsChangingSource = false;

    private boolean mInitialPosition = false;

    private boolean mIsPreviewOn = false;

    private boolean haveStartPreview = false;

    private boolean mTvIsSuppportPreviewMode = false;

    private View mPreviewWindowView = null;

    private int mTvSystem = 0;

    private static final int IDX_POS_CABLE = 1;

    private static final int IDX_POS_AIR = 7;

    private static final int VIEW_PAGER_MARGIN = 105;

    private FrameLayout mInputSourceGalleryContainer;

    private ViewPager mViewPager;

    private int mIndexOfUserDefineItem = 0;

    private int mCurrentSource = TvCommonManager.INPUT_SOURCE_NONE;

    private boolean[] mSourceStatusList = null;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SETIS_START) {
                mIsChangingSource = true;
                if (mSourceViewBroadcastReceiver != null) {
                    PictrueChangeActivity.this.unregisterReceiver(mSourceViewBroadcastReceiver);
                    mSourceViewBroadcastReceiver = null;
                }
            } else if (msg.what == SETIS_END_COMPLETE) {
                Intent intent;

                intent = new Intent("mstar.tvsetting.ui.intent.action.RootActivity");
                intent.putExtra("task_tag", "input_source_changed");
                intent.putExtra("inputAntennaType", mAntennaType);

                startActivity(intent);
                finish();
                mIsChangingSource = false;
            } else if (msg.what == TTS_SPEAK_FOCUS_ITEM) {
                ttsSpeakFocusItem();
            }
        };
    };

    private final String TV_INPUT_TV_PREFIX = "com.mstar.android.tv.inputservice/.TunerInputService/HW";
    private final String TV_INPUT_COMPONENT_PREFIX = "com.mstar.android.tv.inputservice/.ComponentInputService/HW";
    private final String TV_INPUT_COMPOSITE_PREFIX = "com.mstar.android.tv.inputservice/.CompositeInputService/HW";
    private final String TV_INPUT_DISPLAYPORT_PREFIX = "com.mstar.android.tv.inputservice/.DisplayportInputService/HW";
    private final String TV_INPUT_DVI_PREFIX = "com.mstar.android.tv.inputservice/.DviInputService/HW";
    private final String TV_INPUT_HDMI_PREFIX = "com.mstar.android.tv.inputservice/.HdmiInputService/HW";
    private final String TV_INPUT_SCART_PREFIX = "com.mstar.android.tv.inputservice/.ScartInputService/HW";
    private final String TV_INPUT_SVIDEO_PREFIX = "com.mstar.android.tv.inputservice/.SvideoInputService/HW";
    private final String TV_INPUT_VGA_PREFIX = "com.mstar.android.tv.inputservice/.VgaInputService/HW";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupmenu);
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mTvCommonmanager = TvCommonManager.getInstance();
        mTvChannelManager = TvChannelManager.getInstance();
        mTvS3DManager = TvS3DManager.getInstance();
        mTvPvrManager = TvPvrManager.getInstance();
        mTvPipPopManager = TvPipPopManager.getInstance();
        mInputSource = mTvCommonmanager.getCurrentTvInputSource();
        mObserverThreeDMode = new ThreeDModeObserver(handler);
        mTvSystem = mTvCommonmanager.getCurrentTvSystem();

        mTvIsSuppportPreviewMode = TvCommonManager.getInstance().isSupportModule(
                TvCommonManager.MODULE_PREVIEW_MODE);
        Log.d(TAG, "mTvIsSuppportPreviewMode = " + mTvIsSuppportPreviewMode);
        mSourceStatusList = mTvCommonmanager.GetInputSourceStatus();

        TvCommonManager.getInstance().speakTtsDelayed(
                getResources().getString(R.string.str_input_source_title)
                , TvCommonManager.TTS_QUEUE_FLUSH
                , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
                , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
            handler.sendEmptyMessageDelayed(TTS_SPEAK_FOCUS_ITEM, TTS_DELAY_TIME_100MS);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout_popmenu_root);
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.x = 0;
        lp.y = 0;
        lp.width = ll.getLayoutParams().width;
        lp.height = ll.getLayoutParams().height;
        lp.gravity = Gravity.BOTTOM;
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        mIsChangingSource = false;
        getContentResolver().registerContentObserver(
                Uri.parse("content://mstar.tv.usersetting/systemsetting"), true,
                mObserverThreeDMode);
        initTvGallery();
        mSourceViewBroadcastReceiver = new SourceViewBroadcastReceiver();
        mKeyQueue.clear();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_STATUS_UPDATE");
        filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH");
        filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_PLUG_OUT");
        PictrueChangeActivity.this.registerReceiver(mSourceViewBroadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        getContentResolver().unregisterContentObserver(mObserverThreeDMode);
        if ((true == mTvIsSuppportPreviewMode) && (true == mIsPreviewOn)) {
            if ((null != mPreviewWindowHandle) && (null != mPreviewSourceHandle)) {
                mPreviewSourceHandle.stopPreview();
                mPreviewWindowHandle.stopSourcePreviewWindow();
            }
        }
        getContentResolver().unregisterContentObserver(mObserverThreeDMode);
        super.onPause();
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged()");
        if ((true == mTvIsSuppportPreviewMode) && (true == mIsPreviewOn)) {
            if (null != mPreviewWindowHandle) {
                mPreviewWindowHandle.addSelfToWindow();
            }
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        if (true == mTvIsSuppportPreviewMode) {
            if (true == mIsPreviewOn) {
                mIsPreviewOn = false;
                if (null != mPreviewSourceHandle) {
                    mPreviewSourceHandle.releasePreview();
                }
                if (null != mPreviewWindowHandle) {
                    mPreviewWindowHandle.fromWinRemoveView();
                }
            }
        }
        super.onStop();
    }

    private Handler previewhandler = new Handler();
    private Runnable runSourcePreview = new Runnable() {

        @Override
        public void run() {
            if ((true == mTvIsSuppportPreviewMode) && (true == mIsPreviewOn)) {
                if (null != mPreviewWindowHandle) {
                    Log.i(TAG, "startSourcePreviewWindow \n");
                    mPreviewWindowHandle.startSourcePreviewWindow(mPreviewWindowView, mCurrentPosition);
                    mPreviewWindowHandle.addSelfToWindow();
                    haveStartPreview = true;
               }
            }
        }
    };

    private void getInputSourcelist() {
        int[] sourceList;

        sourceList = mTvCommonmanager.getSourceList();

        if (sourceList != null) {
            for (int i = 0; (i < sourceList.length) && (TvCommonManager.INPUT_SOURCE_NUM > i); i++) {
                if (i == TvCommonManager.INPUT_SOURCE_ATV
                        && mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    mSourceListFlag[i] = FUNCTION_DISABLED;
                    continue;
                }
                mSourceListFlag[i] = sourceList[i];
            }
        }
        for (int i = 0; (i < mSourceListInvisible.length) && (i < mSourceListFlag.length); i++) {
            if (TvCommonManager.INPUT_SOURCE_NUM > mSourceListInvisible[i]) {
                mSourceListFlag[mSourceListInvisible[i]] = FUNCTION_DISABLED;
            }
        }
    }

    private void initTvGallery() {
        String[] tmpData = null;
        String[] tmpTtsData = null;

        int PreviewSourceFlag = 0;
        int focusPosition = 0;
        int currentSource = 0;
        int hdmiPortCounter = 0;
        int typeCPortCounter = 0;
        int hdmiPortNum = 0;
        int typeCPortNum = 0;
        getInputSourcelist();

        mInputSourceGalleryContainer = (FrameLayout) findViewById(R.id.gallery_container);
        mInputSourceGalleryContainer.setClipChildren(false);
        mInputSourceGalleryContainer.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.gallery_viewpager);
        mCurrentSource = mTvCommonmanager.getCurrentTvInputSource();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            tmpData = getResources().getStringArray(R.array.str_arr_atsc_input_source_vals);
            tmpTtsData = getResources().getStringArray(R.array.str_arr_atsc_tts_input_source_vals);
        } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            tmpData = getResources().getStringArray(R.array.str_arr_idsb_input_source_vals);
        } else {
            tmpData = getResources().getStringArray(R.array.str_arr_input_source_vals);
        }

        mGalleryItemList.clear();
        /*
         * Always display the ATV/DTV icon due to source detection function
         * cannot support to get ATV/DTV status.
         */
        mSourceStatusList[TvCommonManager.INPUT_SOURCE_ATV] = true;
        mSourceStatusList[TvCommonManager.INPUT_SOURCE_DTV] = true;

        for (int i = TvCommonManager.INPUT_SOURCE_HDMI; i < TvCommonManager.INPUT_SOURCE_HDMI_MAX; i++) {
            if (mSourceListFlag[i] == FUNCTION_DISABLED) {
                continue;
            }
            hdmiPortNum++;
        }
        final boolean[] hdmiPortStatus = mTvCommonmanager.getHdmiTypeCPort();
        if (null != hdmiPortStatus) {
            typeCPortNum = 0;
            for (int i = 0; i < hdmiPortStatus.length; i++) {
                if (true == hdmiPortStatus[i]) {
                    typeCPortNum++;
                }
            }
        }
        hdmiPortNum = hdmiPortNum - typeCPortNum;
        for (int subSource = 0; subSource < tmpData.length; subSource++) {
            if (TvCommonManager.INPUT_SOURCE_NUM <= subSource) {
                break;
            }
            if (mSourceListFlag[subSource] == FUNCTION_DISABLED) {
                continue;
            }

            if (mTvPipPopManager.isPipModeEnabled() == true) {
                if (subSource == mTvCommonmanager.getCurrentTvInputSource()) {
                    mInitialPosition = true;
                    continue;
                }
                if (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_PIP) {
                    if (mTvPipPopManager.checkPipSupportOnSubSrc(subSource) == false) {
                        continue;
                    }
                }
                if (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP) {
                    int formatType = mTvS3DManager.getCurrent3dType();
                    if (formatType == TvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW) {
                    } else {
                        if (mTvPipPopManager.checkPipSupportOnSubSrc(subSource) == false) {
                            continue;
                        }
                    }
                }
                if (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_TRAVELING) {
                    int curSubSource = mTvCommonmanager.getCurrentTvSubInputSource();

                    if (curSubSource == subSource) {
                        continue;
                    } else if (mTvPipPopManager.checkTravelingModeSupport(subSource, curSubSource) == false) {
                        continue;
                    }
                }
            }
            if (subSource == mCurrentSource) {
                focusPosition = PreviewSourceFlag;
            } else {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (TvCommonManager.INPUT_SOURCE_ATV == mCurrentSource) {
                        if (subSource == TvCommonManager.INPUT_SOURCE_DTV) {
                            focusPosition = PreviewSourceFlag;
                        }
                    }
                }
            }
            InputSourceItem inputSourceItem = new InputSourceItem();
            String srcName = tmpData[subSource];
            String ttsText = null;
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                ttsText = tmpTtsData[subSource];
            }
            if (subSource == TvCommonManager.INPUT_SOURCE_DTV
                    && mCurrentSource != TvCommonManager.INPUT_SOURCE_DTV) {
                srcName = "DTV2";
                inputSourceItem.setInputSourceName(srcName);
            } else {
                boolean isTypeC = false;
                if ((TvCommonManager.INPUT_SOURCE_HDMI <= subSource)
                    && (TvCommonManager.INPUT_SOURCE_HDMI4 >= subSource)) {
                    int idx = subSource - TvCommonManager.INPUT_SOURCE_HDMI;
                    if ((null != hdmiPortStatus) && (hdmiPortStatus[idx])) {
                        typeCPortCounter++;
                        srcName = "TYPE-C";
                        if (1 < typeCPortNum) {
                            srcName = "TYPE-C " + typeCPortCounter;
                        }
                        ttsText = srcName;
                        isTypeC = true;
                    }
                    if (false == isTypeC) {
                        hdmiPortCounter++;
                        srcName = "HDMI";
                        ttsText = "H D M I";
                        if (1 < hdmiPortNum) {
                            srcName = "HDMI" + hdmiPortCounter;
                            ttsText = "H D M I " + hdmiPortCounter;
                        }
                    }
                }
                /*
                 * HW: HDMI, HDMI, HDMI, HDMI
                 * Displaying: HDMI1, HDMI2, HDMI3, HDMI4
                 *
                 * HW: HDMI, TYPE C, HDMI, HDMI
                 * Displaying HDMI1, TYPE-C, HDMI2, HDMI3
                 *
                 * HW: HDMI, TYPE C, HDMI, TYPE C
                 * Displaying: HDMI1, TYPE-C 1, HDMI2, TYPE-C 2
                 *
                 * HW: TYPE C, HDMI, HDMI, HDMI
                 * Displaying: TYPE-C, HDMI1, HDMI2, HDMI3
                 */
                inputSourceItem.setInputSourceName(srcName);
            }
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                inputSourceItem.setTtsInputSourceName(ttsText);
            }
            inputSourceItem.setPositon(subSource);
            if (mSourceStatusList != null) {
                inputSourceItem.setSignalFlag(mSourceStatusList[subSource]);
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (mSourceStatusList[TvCommonManager.INPUT_SOURCE_ATV] == true) {
                        /*
                         * For ATSC system, soure list icon "TV" is used for ATV
                         * and DTV.
                         */
                        mSourceStatusList[TvCommonManager.INPUT_SOURCE_DTV] = true;
                    }
                }
            } else {
                inputSourceItem.setSignalFlag(false);
            }
            if (TvCommonManager.getInstance()
                    .isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
                if (mSourceStatusList[subSource] == true) {
                    inputSourceItem.setSelectedResId(mSourceSelectedResIds[subSource]);
                    inputSourceItem.setUnselectedResId(mSourceUnselectedResIds[subSource]);
                } else {
                    inputSourceItem.setSelectedResId(mSourceNoSignalResIds[subSource]);
                    inputSourceItem.setUnselectedResId(mSourceNoSignalResIds[subSource]);
                }
            } else {
                inputSourceItem.setSelectedResId(mSourceSelectedResIds[subSource]);
                inputSourceItem.setUnselectedResId(mSourceUnselectedResIds[subSource]);
            }

            mGalleryItemList.add(inputSourceItem);
            PreviewSourceFlag++;
        }

        appendUserDefineItems();

        PreviewSourceFlag = 0;
        if ((focusPosition < TvCommonManager.INPUT_SOURCE_VGA
                || focusPosition > TvCommonManager.INPUT_SOURCE_DTV || mInitialPosition)
                && (focusPosition != TvCommonManager.INPUT_SOURCE_VGA2)
                && (focusPosition != TvCommonManager.INPUT_SOURCE_VGA3)) {
            mInitialPosition = false;
            mViewPager.setCurrentItem(0);
            mCurrentPosition = 0;
        } else {
            int focusIdx;

            focusIdx = focusPosition;
            if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                int currentInputSource = mTvCommonmanager.getCurrentTvInputSource();
                int currentAntennaType = TvIsdbChannelManager.getInstance().getAntennaType();
                if (TvCommonManager.INPUT_SOURCE_ATV == mCurrentSource
                        || TvCommonManager.INPUT_SOURCE_DTV == mCurrentSource) {
                    if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE == currentAntennaType) {
                        focusIdx = IDX_POS_CABLE;
                    } else if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR == currentAntennaType) {
                        focusIdx = IDX_POS_AIR;
                    }
                }
            }
            mCurrentPosition = focusIdx;
        }

        /*
         * Workaround for mantis 0879640. It seems be frameworks issue that
         * system is in touch mode and mViewPager may NOT recieve first key
         * event from IR. The following code could be removed if framework fixes
         * this issue.
         */
        new Instrumentation().setInTouchMode(false);

        checkInputSourceForUserDefineItems();
        ViewGroup.LayoutParams lp = mViewPager.getLayoutParams();
        if ((true == mTvIsSuppportPreviewMode) && (true == isSourcePreviewSettingOn())) {
            if (true == isSupportSourcePreview()) {
                mIsPreviewOn = true;
            }
        }
        mInputSourceGalleryAdapter = new InputSourceGalleryAdapter(this, mGalleryItemList, mIsPreviewOn,
                lp.width, lp.height);
        mInputSourceGalleryAdapter.setPosition(mCurrentPosition);
        if ((true == mTvIsSuppportPreviewMode) && (true == isSourcePreviewSettingOn())) {
            if (true == isSupportSourcePreview()) {
                mPreviewSourceHandle = new PreviewSourceHandle(mGalleryItemList, mCurrentSource, mInputSourceGalleryAdapter);
                mPreviewWindowHandle = new PreviewWindowHandle(this, mPreviewSourceHandle);
            } else {
                Toast.makeText(PictrueChangeActivity.this, R.string.str_source_preview_warning,
                        Toast.LENGTH_SHORT).show();
            }
        }
        mViewPager.setAdapter(mInputSourceGalleryAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.setOffscreenPageLimit(mInputSourceGalleryAdapter.getCount());
        mViewPager.setPageMargin(VIEW_PAGER_MARGIN);
        mViewPager.setPageTransformer(true, new depthPageTransformer());
        changeViewPageScroller();
        setListener();
        mTotalsourceNum = mGalleryItemList.size();
    }

    public class depthPageTransformer implements ViewPager.PageTransformer {
        private static final int LEFT_POINT_POSITION = -1;
        private static final int CENTER_POINT_POSITION = 0;
        private static final int RIGHT_POINT_POSITION = 1;
        public void transformPage(View view, float position) {

            ++mCount;
            if (position < LEFT_POINT_POSITION) {
                view.setScaleX(1.1f);
                view.setScaleY(1.1f);
                view.setAlpha(0.6f);
            } else if (position <= CENTER_POINT_POSITION) {
                /* Fade the page out */
                view.setAlpha((0.4f * position) + 1);
                float scaleFactor = 0.5f * position + 1.6f;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else if (position <= RIGHT_POINT_POSITION) {
                /* Fade the page out */
                view.setAlpha((-0.4f * position) + 1);
                float scaleFactor = -0.5f * position + 1.6f;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else {
                /* This page is way off-screen to the right */
                view.setScaleX(1.1f);
                view.setScaleY(1.1f);
                view.setAlpha(0.6f);
            }

            if(mTotalsourceNum>0 && mTotalsourceNum<mCount && mCount<=mTotalsourceNum+mTotalsourceNum)
            {
               if(mCount-1 == mCurrentPosition + mTotalsourceNum )
               {
                   if ((true == mTvIsSuppportPreviewMode) && (true == mIsPreviewOn)) {
                       mPreviewWindowView = view;
                       Log.i(TAG, "run handler\n");
                       Log.i(TAG, "ready view\n");

                       handler.removeCallbacks(runSourcePreview);
                       handler.postDelayed(runSourcePreview, 5000);
                   }
               }

          }
        }
    }

    private void changeViewPageScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            fixedSpeedScroller scroller;
            scroller = new fixedSpeedScroller(this, new AccelerateDecelerateInterpolator());
            mField.set(mViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class fixedSpeedScroller extends Scroller {
        private static final int ANIMATION_DELAY_TIME = 300;
        private int animationDelayTime = ANIMATION_DELAY_TIME;
        public fixedSpeedScroller(Context context) {
            super(context);
        }

        public fixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, animationDelayTime);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, animationDelayTime);
        }

        public void setmDuration(int time) {
            animationDelayTime = time;
        }

        public int getmDuration() {
            return animationDelayTime;
        }
    };

    private void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mGalleryItemList.get(mLastPosition).setSelectFlag(false);
                mGalleryItemList.get(position).setSelectFlag(true);
                mLastPosition = position;
                mCurrentPosition = position;
                mInputSourceGalleryAdapter.setPosition(position);
                mInputSourceGalleryAdapter.notifyDataSetChanged();
                mViewPager.invalidate();

                mCount = 0;
                if(haveStartPreview == true){
                    haveStartPreview = false;
                    if ((true == mTvIsSuppportPreviewMode) && (true == mIsPreviewOn)) {
                        if ((null != mPreviewWindowHandle) && (null != mPreviewSourceHandle)) {
                            mPreviewSourceHandle.stopPreview();
                            mPreviewWindowHandle.stopSourcePreviewWindow();
                            mPreviewWindowHandle.fromWinRemoveView();
                        }
                    }
                }
                ttsSpeakFocusItem();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position;

        position = mInputSourceGalleryAdapter.getGalleryItemPositionFromView(v);
        mGalleryItemList.get(mLastPosition).setSelectFlag(false);
        mGalleryItemList.get(position).setSelectFlag(true);
        mLastPosition = position;
        mCurrentPosition = position;
        mInputSourceGalleryAdapter.setPosition(position);
        handleOnItemSelected();
    }

    @Override
    protected void onDestroy() {
        if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
            mStopRecordDialog.dismiss();
        }

        if (mSourceViewBroadcastReceiver != null) {
            this.unregisterReceiver(mSourceViewBroadcastReceiver);
            mSourceViewBroadcastReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mapKeyPadToIR(keyCode, event)) {
            return true;
        }

        // don't handle this key when changing source
        if (mIsChangingSource && keyCode == MKeyEvent.KEYCODE_TV_SETTING) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            handleOnItemSelected();
            return true;
        }

        if (keyCode == MKeyEvent.KEYCODE_LIST) {
            Intent intent = new Intent("com.mstar.tvsetting.ui.intent.action.ChannelActivity");
            intent.putExtra("ListId", 2);
            startActivity(intent);
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_S || keyCode == KeyEvent.KEYCODE_TV_INPUT) {
            finish();
            return true;
        }
        if (SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToSourceInfo(this, keyCode) == true) {
            return true;
        } else if (KeyEvent.KEYCODE_MENU == keyCode) {
            mStaticSourceDialog = new StaticSourceSettingDialog(PictrueChangeActivity.this,
                    mGalleryItemList.get(mCurrentPosition).getPositon(), mTvPipPopManager);
            mStaticSourceDialog.show();
            return true;
        }

        mKeyQueue.add(keyCode);
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mKeyQueue.size() == KEYQUEUE_SIZE) {
            String keystr = intArrayListToString(mKeyQueue);
            if (((currentVolume == 10) && (keystr.equals(GOODKEYCODES))) || (keystr.equals(DESIGNMENUKEYCODES))) {
                if (false == ActivityManager.isUserAMonkey()) {
                    Intent intent;

                    mKeyQueue.clear();
                    intent = new Intent("mstar.tvsetting.factory.intent.action.MainmenuActivity");
                    startActivity(intent);
                    finish();
                    return true;
                }
            } else if (((currentVolume == 0) && (keystr.equals(FACTORYEYCODES))) || (keystr.equals(FACTORYMENUKEYCODES))) {
                if (false == ActivityManager.isUserAMonkey()) {
                    Intent intent;

                    mKeyQueue.clear();
                    intent = new Intent("mstar.tvsetting.factory.intent.action.FactorymenuActivity");
                    startActivity(intent);
                    finish();
                    return true;
                }
            } else {
                mKeyQueue.remove(0);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private Boolean mapKeyPadToIR(int keyCode, KeyEvent event) {
        String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
        Log.d(TAG, "deviceName: " + deviceName);
        if (!deviceName.equals("MStar Smart TV Keypad")) {
            return false;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
                return true;
            default:
                return false;
        }
    }

    private void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }.start();
        }
    }

    private String intArrayListToString(ArrayList<Integer> al) {
        String str = "";
        for (int i = 0; i < al.size(); ++i) {
            str += al.get(i).toString();
        }
        return str;
    }

    private class OnStopRecordCancelClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
                mStopRecordDialog.dismiss();
            }
            mInputSourceOfRecording = mTvCommonmanager.getCurrentTvInputSource();
            Log.i(TAG, "OnStopRecordCancelClickListener onClick");
        }
    }

    private class OnStopRecordConfirmClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
                mStopRecordDialog.dismiss();
            }
            mTvPvrManager.stopRecord();

            if (mGalleryItemList.get(mInputSourceOfRecording).isUserDefineItem() == true) {
                if (mGalleryItemList.get(mInputSourceOfRecording).getUserDefineItemType() == InputSourceItem.USER_DEFINE_ITEM_TYPE_FILEBROWSER) {
                    startFileBrowser();
                }
            } else {
                changeInputSource(mInputSource, mInputSourceOfRecording);
            }
            dialog.dismiss();
        }
    }

    private boolean showmStopRecordDialog() {
        boolean bRet = true;

        do {
            if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
                Log.e(TAG, "mStopRecordDialog already exist");
                bRet = false;
                break;
            }
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder = dialogBuilder.setTitle(R.string.str_stop_record_dialog_title);
            dialogBuilder = dialogBuilder.setMessage(R.string.str_stop_record_dialog_message);
            dialogBuilder = dialogBuilder
                    .setPositiveButton(R.string.str_stop_record_dialog_confirm,
                            new OnStopRecordConfirmClickListener());
            dialogBuilder = dialogBuilder.setNegativeButton(R.string.str_stop_record_dialog_cancel,
                    new OnStopRecordCancelClickListener());
            if (dialogBuilder == null) {
                Log.e(TAG, "showmStopRecordDialog -- AlertDialog.Builder init fail");
                bRet = false;
                break;
            }
            mStopRecordDialog = dialogBuilder.create();
            if (mStopRecordDialog == null) {
                Log.e(TAG, "showmStopRecordDialog -- AlertDialog.Builder create dialog fail");
                bRet = false;
                break;
            }
            mStopRecordDialog.show();
        } while (false);
        return bRet;
    }

    // Mute Receiver Receive Broadcast form system
    private class SourceViewBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive(), action = " + action);
            // TODO: Do not use hard coded action name
            /*
             * The broadcasts are related to Source Auto Switch setting status:
             *
             * OFF: SN sends the only one notification to notify the cable status changed:
             *     COMMON_EVENT_SIGNAL_STATUS_UPDATE
             * ON: SN sends the two kinds of notification to indicate the cable status:
             *     COMMON_EVENT_SIGNAL_AUTO_SWITCH => for cable plug in
             *     COMMON_EVENT_SIGNAL_PLUG_OUT => for cable plug out
             */
            if (action.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_STATUS_UPDATE")
                    || action.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH")
                    || action.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_PLUG_OUT")) {
                int sourceIndex = intent.getIntExtra("SwitchSourceIndex",
                        TvCommonManager.INPUT_SOURCE_NONE);
                if (TvCommonManager.INPUT_SOURCE_NONE != sourceIndex) {
                    mSourceStatusList = mTvCommonmanager.GetInputSourceStatus();
                    if (null != mSourceStatusList) {
                        final int nSize = mGalleryItemList.size();
                        for (int index = 0; index < nSize; index++) {
                            if (mGalleryItemList.get(index).getPositon() == sourceIndex) {
                                mGalleryItemList.get(index).setSignalFlag(
                                        mSourceStatusList[sourceIndex]);

                                // Update status and icon of the given input
                                // source
                                if (TvCommonManager.getInstance().isSupportModule(
                                        TvCommonManager.MODULE_OFFLINE_DETECT)) {
                                    if (true == mSourceStatusList[sourceIndex]) {
                                        mGalleryItemList.get(index).setSelectedResId(
                                                mSourceSelectedResIds[sourceIndex]);
                                        mGalleryItemList.get(index).setUnselectedResId(
                                                mSourceUnselectedResIds[sourceIndex]);
                                    } else {
                                        mGalleryItemList.get(index).setSelectedResId(
                                                mSourceNoSignalResIds[sourceIndex]);
                                        mGalleryItemList.get(index).setUnselectedResId(
                                                mSourceNoSignalResIds[sourceIndex]);
                                    }
                                }

                                // Refresh Screen
                                mInputSourceGalleryAdapter.notifyDataSetChanged();
                                mViewPager.invalidate();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private class ThreeDModeObserver extends ContentObserver {
        private int systemAutoTime = 0;

        public ThreeDModeObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            if (!mIsSourceNeedSwitch) {
                return;
            }
            mIsSourceNeedSwitch = false;
            if (systemAutoTime > 0) {
                Settings.Global.putInt(getContentResolver(), Settings.Global.AUTO_TIME,
                        systemAutoTime);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // disable dualview
                    if ((mTvPipPopManager.isPipModeEnabled() == true)
                            && (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP)) {
                        int formatType = mTvS3DManager.getCurrent3dType();
                        if (formatType == TvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW) {
                            mTvPipPopManager.disable3dDualView();
                        }
                    }
                    if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                        try {
                            systemAutoTime = Settings.Global.getInt(getContentResolver(),
                                    Settings.Global.AUTO_TIME);
                        } catch (SettingNotFoundException e) {
                            systemAutoTime = 0;
                        }
                        if (systemAutoTime > 0) {
                            Settings.Global.putInt(getContentResolver(), Settings.Global.AUTO_TIME,
                                    0);
                        }
                    }
                    Log.i(TAG, "startActivity SOURCE_CHANGE intent: inputSource = " + mInputSource);
                    Intent intent = new Intent(MIntent.ACTION_START_TV_PLAYER);
                    intent.putExtra("inputSrc", mInputSource);
                    intent.putExtra("inputAntennaType", mAntennaType);
                    intent.addFlags(MIntent.FLAG_ACTIVITY_INHERIT_ATTRIBUTE);
                    startActivity(intent);
                    handler.sendEmptyMessage(SETIS_END_COMPLETE);
                }
            }).start();
        }
    }

    private boolean isSupportSourcePreview() {
        if (mTvPipPopManager.isPipModeEnabled() == true) {
            if ((mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_PIP)
                    || (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP)
                    || (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_TRAVELING)) {
                Log.d(TAG, "PIP is open now, isSupportSourcePreview return false");
                return false;
            }
        }
        Log.d(TAG, "isSupportSourcePreview return true");
        return true;
    }

    private boolean isSourcePreviewSettingOn() {
        int result = mTvCommonmanager.getSourcePreviewState();
        Log.d(TAG, "isSourcePreviewSettingOn return " + (result == 1 ? true : false));
        return (result == 1 ? true : false);
    }

    private void updateSourceInputType(int inputSourceTypeIdex) {
        long ret = -1;

        ContentValues vals = new ContentValues();
        vals.put("enInputSourceType", inputSourceTypeIdex);
        try {
            ret = getContentResolver().update(
                    Uri.parse("content://mstar.tv.usersetting/systemsetting"), vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            Log.i(TAG, "update tbl_PicMode_Setting ignored");
        }
    }

    private void executePreviousTask(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mTvS3DManager
                        .setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                int inputSource = mGalleryItemList.get(position).getPositon();
                Log.i(TAG, "startActivity SOURCE_CHANGE intent: inputSource = " + inputSource);
                if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                    if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                        TvIsdbChannelManager.getInstance().setAntennaType(
                                TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
                    }
                    if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                        AtscMainListChannelInformation info = TvAtscChannelManager.getInstance()
                                .getCurrentChannelInformation();
                        if (info != null) {
                            if (info.progId == CHANNELMANAGER_ATSC_ATV_ID) {
                                inputSource = TvCommonManager.INPUT_SOURCE_ATV;
                            }
                        }
                    }
                }
                Intent intent = new Intent(MIntent.ACTION_START_TV_PLAYER);
                intent.putExtra("inputSrc", inputSource);
                intent.putExtra("inputAntennaType", mAntennaType);
                startActivity(intent);
                try {
                    Intent targetIntent;

                    targetIntent = new Intent("mstar.tvsetting.ui.intent.action.RootActivity");
                    targetIntent.putExtra("task_tag", "input_source_changed");
                    /* DO NOT remove on_change_source extra!, it will cause mantis:1088498. */
                    targetIntent.putExtra("no_change_source", true);
                    if (targetIntent != null)
                        startActivity(targetIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void changeInputSource(int inpSource, int position) {
        int curIntSource = mTvCommonmanager.getCurrentTvInputSource();

        mInputSource = inpSource;
        if (true == mTvIsSuppportPreviewMode) {
            if (true == mIsPreviewOn) {
                mIsPreviewOn = false;
                mPreviewSourceHandle.stopPreview();
                mPreviewSourceHandle.releasePreview();
                if (null != mPreviewWindowHandle) {
                    mPreviewWindowHandle.fromWinRemoveView();
                }
            }
        }
        if ((curIntSource >= TvCommonManager.INPUT_SOURCE_STORAGE)
                && (curIntSource != TvCommonManager.INPUT_SOURCE_VGA2)
                && (curIntSource != TvCommonManager.INPUT_SOURCE_VGA3)) {
            Intent source_switch_from_storage = new Intent("source.switch.from.storage");
            sendBroadcast(source_switch_from_storage);
            handler.sendEmptyMessage(SETIS_START);
            executePreviousTask(position);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // to ensure input-source is switched after database has
                    // been saved, we'll do it later
                    handler.sendEmptyMessage(SETIS_START);
                    mIsSourceNeedSwitch = true;
                    updateSourceInputType(mInputSource);
                }
            }).start();
        }
    }

    private void appendUserDefineItems() {
        Intent intent;
        intent = new Intent(MIntent.ACTION_START_MEDIA_BROWSER);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (intent.resolveActivity(PictrueChangeActivity.this.getPackageManager()) != null) {
            mIndexOfUserDefineItem = mGalleryItemList.size();
            InputSourceItem inputSourceItem = new InputSourceItem();
            inputSourceItem.setInputSourceName(getResources().getString(R.string.str_media_browser));
            inputSourceItem.setTtsInputSourceName(getResources().getString(R.string.str_media_browser));

            mSourceListFlag = Arrays.copyOf(mSourceListFlag, mSourceListFlag.length + 1);
            mSourceListFlag[mSourceListFlag.length - 1] = 1;

            // Since it's not one of the TV input sources
            inputSourceItem.setPositon(TvCommonManager.INPUT_SOURCE_STORAGE);

            inputSourceItem.setUserDefineItemType(InputSourceItem.USER_DEFINE_ITEM_TYPE_FILEBROWSER);

            inputSourceItem.setSelectedResId(R.drawable.no_preview);
            inputSourceItem.setUnselectedResId(R.drawable.no_preview);

            mGalleryItemList.add(inputSourceItem);
        }
    }

    private void checkInputSourceForUserDefineItems() {
        if (mTvCommonmanager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE) {
            mViewPager.setCurrentItem(mIndexOfUserDefineItem);
            mCurrentPosition = mIndexOfUserDefineItem;
        }
    }

    private void handleOnItemSelected() {
        int position = mCurrentPosition;

        Log.d(TAG, "handleOnItemSelected(), position = " + position);

        if (mGalleryItemList.get(position).isUserDefineItem() == true) {
            if (mGalleryItemList.get(position).getUserDefineItemType() == InputSourceItem.USER_DEFINE_ITEM_TYPE_FILEBROWSER) {
                if (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC) {
                    boolean bRecordNow = mTvPvrManager.isRecording();

                    if (bRecordNow) {
                        mInputSourceOfRecording = position;
                        showmStopRecordDialog();
                        return;
                    }
                }
                startFileBrowser();
                return;
            }
        }
        mInputSource = mGalleryItemList.get(position).getPositon();
        Log.d(TAG, "handleOnItemSelected(), input source = " + mInputSource);

        Intent live_tv_intent = new Intent();
        live_tv_intent.setComponent(new ComponentName("com.android.tv",
                        "com.android.tv.MainActivity"));
        if (isIntentSupported(live_tv_intent)) {
            Uri channelUri = null;
            switch (mInputSource) {
                case TvCommonManager.INPUT_SOURCE_ATV:
                case TvCommonManager.INPUT_SOURCE_DTV:
                    channelUri = buildChannelsUriForInput();
                    break;
                case TvCommonManager.INPUT_SOURCE_VGA:
                case TvCommonManager.INPUT_SOURCE_VGA2:
                case TvCommonManager.INPUT_SOURCE_VGA3:
                    channelUri = buildChannelUriForPassthroughInput(TV_INPUT_VGA_PREFIX+mInputSource);
                    break;
                case TvCommonManager.INPUT_SOURCE_CVBS:
                case TvCommonManager.INPUT_SOURCE_CVBS2:
                case TvCommonManager.INPUT_SOURCE_CVBS3:
                case TvCommonManager.INPUT_SOURCE_CVBS4:
                case TvCommonManager.INPUT_SOURCE_CVBS5:
                case TvCommonManager.INPUT_SOURCE_CVBS6:
                case TvCommonManager.INPUT_SOURCE_CVBS7:
                case TvCommonManager.INPUT_SOURCE_CVBS8:
                    channelUri = buildChannelUriForPassthroughInput(TV_INPUT_COMPOSITE_PREFIX+mInputSource);
                    break;
                case TvCommonManager.INPUT_SOURCE_SVIDEO:
                case TvCommonManager.INPUT_SOURCE_SVIDEO2:
                case TvCommonManager.INPUT_SOURCE_SVIDEO3:
                case TvCommonManager.INPUT_SOURCE_SVIDEO4:
                    channelUri = buildChannelUriForPassthroughInput(TV_INPUT_SVIDEO_PREFIX+mInputSource);
                    break;
                case TvCommonManager.INPUT_SOURCE_SCART:
                case TvCommonManager.INPUT_SOURCE_SCART2:
                    channelUri = buildChannelUriForPassthroughInput(TV_INPUT_SCART_PREFIX+mInputSource);
                    break;
                case TvCommonManager.INPUT_SOURCE_HDMI:
                case TvCommonManager.INPUT_SOURCE_HDMI2:
                case TvCommonManager.INPUT_SOURCE_HDMI3:
                case TvCommonManager.INPUT_SOURCE_HDMI4:
                    channelUri = buildChannelUriForPassthroughInput(TV_INPUT_HDMI_PREFIX+mInputSource);
                    break;
                case TvCommonManager.INPUT_SOURCE_YPBPR:
                case TvCommonManager.INPUT_SOURCE_YPBPR2:
                case TvCommonManager.INPUT_SOURCE_YPBPR3:
                    channelUri = buildChannelUriForPassthroughInput(TV_INPUT_COMPONENT_PREFIX+mInputSource);
                    break;
                case TvCommonManager.INPUT_SOURCE_DVI:
                case TvCommonManager.INPUT_SOURCE_DVI2:
                case TvCommonManager.INPUT_SOURCE_DVI3:
                case TvCommonManager.INPUT_SOURCE_DVI4:
                    channelUri = buildChannelUriForPassthroughInput(TV_INPUT_DVI_PREFIX+mInputSource);
                    break;
            }
            if (channelUri != null) {
                live_tv_intent.setAction(Intent.ACTION_VIEW);
                live_tv_intent.setData(channelUri);
            }
            startActivity(live_tv_intent);
            return;
        }

        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            if (IDX_POS_AIR == mCurrentPosition) {
                mAntennaType = TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR;
            } else if (IDX_POS_CABLE == mCurrentPosition) {
                mAntennaType = TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE;
            }
        }

        if (mInputSource != mTvCommonmanager.getCurrentTvInputSource()) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                if ((mInputSource == TvCommonManager.INPUT_SOURCE_DTV)
                        && (mTvCommonmanager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV)) {
                    handler.sendEmptyMessage(SETIS_END_COMPLETE);
                } else {
                    if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                        AtscMainListChannelInformation info = TvAtscChannelManager.getInstance()
                                .getCurrentChannelInformation();
                        if (info != null) {
                            if (info.progId == CHANNELMANAGER_ATSC_ATV_ID) {
                                mInputSource = TvCommonManager.INPUT_SOURCE_ATV;
                            }
                        }
                    }
                    changeInputSource(mInputSource, position);
                }
            } else {
                boolean bRecordNow = false;
                bRecordNow = mTvPvrManager.isRecording();

                if (bRecordNow
                        && (mTvCommonmanager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV)) {
                    mInputSourceOfRecording = position;
                    showmStopRecordDialog();
                } else {
                    changeInputSource(mInputSource, position);
                }
            }
        } else {
            handler.sendEmptyMessage(SETIS_END_COMPLETE);
        }
    }

    private void startFileBrowser() {
        Intent intent;
        intent = new Intent(MIntent.ACTION_START_MEDIA_BROWSER);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.resolveActivity(PictrueChangeActivity.this.getPackageManager()) != null) {
            PictrueChangeActivity.this.startActivity(intent);
        } else {
            Log.e(TAG, "Failed to start media browser!");
        }
    }

    private boolean isIntentSupported(Intent intent) {
        boolean ret = false;
        List<ResolveInfo> resolveInfo = getApplicationContext().getPackageManager().queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            ret = true;
        }
        return ret;
    }

     /**
     * Build a special channel URI intended to be used with pass-through inputs. (e.g. HDMI)
     *
     * @param inputId The ID of the pass-through input to build a channels URI for.
     * @see TvInputInfo#isPassthroughInput()
     */
    private final Uri buildChannelUriForPassthroughInput(String inputId) {
        return new Uri.Builder().scheme("content").authority("android.media.tv")
                .appendPath("passthrough").appendPath(inputId).build();
    }

     /**
     * Builds a URI that points to all channels from a given TV input.
     */
    private final Uri buildChannelsUriForInput() {
        return Uri.parse("content://android.media.tv/channel");
    }

    private void ttsSpeakFocusItem() {
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            final String str = mGalleryItemList.get(mCurrentPosition).getTtsInputSourceName();
            if (null != str) {
                TvCommonManager.getInstance().speakTtsDelayed(
                    str
                    , TvCommonManager.TTS_QUEUE_FLUSH
                    , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
                    , TTS_DELAY_TIME_800MS);
            }
        }
    }
}
