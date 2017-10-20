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

package com.mstar.android.tv.util;

import android.content.Context;
import android.content.Intent;

import android.os.storage.StorageManager;
import android.util.Log;
import android.database.Cursor;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.storage.StorageVolume;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tvapi.common.vo.EnumPvrStatus;
import com.mstar.android.tvapi.common.vo.PvrPlaybackSpeed;
import com.mstar.android.tv.TvPvrManager.OnPlaybackEventListener;
import com.mstar.android.tvapi.common.PvrManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager.OnPvrEventListener;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.lang.Long;
import java.lang.Integer;
import java.lang.Throwable;

public class PVRControlUnit {
    private String TAG = "PVRControlUnit";

    private TvPvrManager mTvPvrManager;

    private TvChannelManager mTvChannelManager;

    private StorageManager mStorageManager;

    private static PVRControlUnit mInstance = null;

    private final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    private int MILLIS_UNIT = 1000;

    private OnPvrEventListener mPvrEventListener = null;

    private boolean isPVREnabled = false;

    public static long PVR_JUMP_NEXT_MS_DEFAULT = Long.MAX_VALUE;

    public static long PVR_JUMP_PREVIOUS_MS_DEFAULT = 0;

    protected Context mContext;

    private BroadcastReceiver mReceiver = null;

    public static PVRControlUnit getInstance(Context context) {
        if (mInstance == null)
            mInstance = new PVRControlUnit(context);
        return mInstance;
    }

    private PVRControlUnit(Context context) {
        mContext = context;
        mTvPvrManager = TvPvrManager.getInstance();
        mStorageManager = context.getSystemService(StorageManager.class);
        mPvrEventListener = new PvrEventListener();
        mTvChannelManager = TvChannelManager.getInstance();
        mTvChannelManager.registerOnPvrEventListener(mPvrEventListener);
        updatePVREnabledFlag(context);
        registerReceiver();
    }

    protected void finalize() throws Throwable {
        mContext.unregisterReceiver(mReceiver);
        mPvrEventListener = null;
    }

    private void registerReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED) ||
                           intent.getAction().equals(Intent.ACTION_MEDIA_EJECT)) {
                    if (DEBUG) Log.d(TAG, "Receive ACTION_MEDIA_UNMOUNTED");
                    if (isPVRsupported()) {
                        stopPVR();
                        stopAlwaysTimeShiftRecord();
                    }
                } else if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){
                    if (DEBUG) Log.d(TAG, "Receive ACTION_MEDIA_MOUNTED");
                    if (isPVRsupported()) {
                        stopAlwaysTimeShiftRecord();
                        StorageVolume vol = getExternalStorageVolumeInfo(getFsUuid(intent));
                        if (vol != null) {
                            if (DEBUG) Log.d(TAG, "vol.path=" + vol.getPath());
                            enableAlwaysTimeShift();
                            startAlwaysTimeShiftRecord(vol);
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        mContext.registerReceiver(mReceiver, filter);
    }

    public EnumPvrStatus startAlwaysTimeShiftRecord(StorageVolume vol) {
        if (vol == null) {
            return EnumPvrStatus.E_ERROR;
        }

        if (DEBUG)
            Log.d(TAG, "startAlwaysTimeShiftRecord, vol.getPath()=" + vol.getPath() + " vol.getFsLabel()=" + vol.getFsLabel());

        if (!mTvPvrManager.setPvrParams(vol.getPath(), (short)0)) {
            Log.d(TAG, "startAlwaysTimeShiftRecord,, set pvr parameters, path:" + vol.getPath());
            return EnumPvrStatus.E_ERROR;
        }

        if (mTvPvrManager.isAlwaysTimeShiftRecording()) {
            return EnumPvrStatus.E_SUCCESS;
        }

        short retShort = mTvPvrManager.startAlwaysTimeShiftRecord();
        if (DEBUG)
            Log.d(TAG, "startAlwaysTimeShiftRecord, retShort=" + retShort);

        return EnumPvrStatus.E_SUCCESS;
    }

    public boolean stopAlwaysTimeShiftRecord() {
        if (DEBUG)
            Log.d(TAG, "stopAlwaysTimeShiftRecord");
        mTvPvrManager.stopAlwaysTimeShiftRecord();
        return true;
    }

    public String getFsUuid(Intent intent) {
        String uuid = null;

        if (intent != null) {
            String storageVolume = intent.getExtra("storage_volume").toString();
            if (storageVolume != null) {
                String[] arrayList = storageVolume.split("\\s+");
                List<String> valueSets = new ArrayList<String>(Arrays.asList(arrayList));
                for (String valueSet : valueSets) {
                    if (valueSet.startsWith("mFsUuid")) {
                        uuid = valueSet.split("=")[1];
                    }
                }
            }
        }

        return uuid;
    }

    public StorageVolume getExternalStorageVolumeInfo(String recommandFsUuid) {
        StorageVolume[] storageVolumes = mStorageManager.getVolumeList();
        StorageVolume firstVolFound = null;
        int length = storageVolumes.length;
        for (final StorageVolume vol : storageVolumes) {
            StorageVolume storageVolume = vol;
            if (firstVolFound == null) {
                firstVolFound = vol;
            }else if (vol.getUuid().equals(recommandFsUuid)){
                if (DEBUG)
                    Log.d(TAG,
                        "getExternalStorageVolumeInfo, getUuid():"
                            + vol.getUuid() + " getPath():" + vol.getPath());
                return vol;
            }
        }
        if (DEBUG)
            Log.d(TAG,
                "getExternalStorageVolumeInfo, getUuid():"
                    + firstVolFound.getUuid() + " getPath():" + firstVolFound.getPath());
        return firstVolFound;
    }

    public EnumPvrStatus pause() {
        EnumPvrStatus ret = EnumPvrStatus.E_SUCCESS;

        if (DEBUG)
            Log.d(TAG,
                    "pause, isAlwaysTimeShiftRecording:"
                            + mTvPvrManager.isAlwaysTimeShiftRecording()
                            + ", isAlwaysTimeShiftPlaybackPaused:"
                            + mTvPvrManager.isAlwaysTimeShiftPlaybackPaused());

        if (mTvPvrManager.isAlwaysTimeShiftRecording()) {
            if (!mTvPvrManager.isAlwaysTimeShiftPlaybackPaused()) {
                if (DEBUG)
                    Log.d(TAG, "pause, call pauseAlwaysTimeShiftPlayback");
                ret = mTvPvrManager.pauseAlwaysTimeShiftPlayback(true);
                if (ret != EnumPvrStatus.E_SUCCESS) {
                    if (DEBUG)
                        Log.d(TAG,
                                "pause, PVR manager pauseAlwaysTimeShiftPlayback FAIL, error code:"
                                        + ret);
                }
            } else if (!mTvPvrManager.isPlaybackPaused()) {
                if (DEBUG)
                    Log.d(TAG, "pause, call pausePlayback");
                mTvPvrManager.pausePlayback();
                return EnumPvrStatus.E_SUCCESS;
            } else {
                //ignore; example. pause->pause->pause
            }
        }

        return ret;
    }

    public EnumPvrStatus play() {
        EnumPvrStatus ret = EnumPvrStatus.E_SUCCESS;

        if (DEBUG)
            Log.d(TAG,
                    "play, isAlwaysTimeShiftRecording:" + mTvPvrManager.isAlwaysTimeShiftRecording()
                            + ", isAlwaysTimeShiftPlaybackPaused:"
                            + mTvPvrManager.isAlwaysTimeShiftPlaybackPaused());

        if (mTvPvrManager.isAlwaysTimeShiftRecording()) {
            if (!mTvPvrManager.isPlaybacking()) {
                if (DEBUG)
                    Log.d(TAG, "play, call startAlwaysTimeShiftPlayback");
                short retShort = mTvPvrManager.startAlwaysTimeShiftPlayback();
                ret = EnumPvrStatus.values()[retShort];
                if (ret != EnumPvrStatus.E_SUCCESS) {
                    if (DEBUG)
                        Log.d(TAG,
                                "play, PVR manager startAlwaysTimeShiftPlayback FAIL, error code:"
                                        + ret);
                }
            } else {
                if (DEBUG)
                    Log.d(TAG, "play, call resumePlayback");
                mTvPvrManager.resumePlayback();
            }
        }
        return ret;
    }

    public EnumPvrStatus seekTo(long timeMs) {
        int intTimeMS = (int)(timeMs/MILLIS_UNIT);
        if (mTvPvrManager.isAlwaysTimeShiftRecording() && mTvPvrManager.isPlaybacking()) {
            if (timeMs == PVR_JUMP_NEXT_MS_DEFAULT) { // jump to the end of the
                                                      // pvr record
                if (DEBUG)
                    Log.d(TAG, "seekTo, call jumpPlaybackTimeInMS("
                            + mTvPvrManager.getCurRecordTimeInSecond() + ")");
                mTvPvrManager.jumpPlaybackTimeInMS(mTvPvrManager.getCurRecordTimeInSecond());
            } else if (timeMs == PVR_JUMP_PREVIOUS_MS_DEFAULT) { // jump to the
                                                                 // start of the
                                                                 // pvr record
                if (DEBUG)
                    Log.d(TAG, "seekTo, call jumpPlaybackTimeInMS(0)");
                mTvPvrManager.jumpPlaybackTimeInMS(0);
            } else { //jump to the specified time
                if (DEBUG)
                    Log.d(TAG, "seekTo, else, call jumpPlaybackTimeInMS(" + intTimeMS + ")");
                mTvPvrManager.jumpPlaybackTimeInMS(intTimeMS);
            }
            return EnumPvrStatus.E_SUCCESS;
        }
        return EnumPvrStatus.E_ERROR;
    }

    public long getStartPosition() {
        long startTime = mTvPvrManager.getTimeshiftStartTimeInMiliSecSinceEpoch();
        if (DEBUG)
            Log.d(TAG, "getStartPosition, getTimeshiftStartTimeInMiliSecSinceEpoch:" + startTime);
        return startTime;
    }

    public long getCurrentPosition() {
        long currTime = mTvPvrManager.getCurTimeshiftTimeInMiliSecSinceEpoch();
        if (DEBUG)
            Log.d(TAG, "getCurrentPosition, getCurTimeshiftTimeInMiliSecSinceEpoch:" + currTime);
        return currTime;
    }

    public void setPlaybackSpeed(float speed) {
        PvrPlaybackSpeed.EnumPvrPlaybackSpeed enumSpeed = mTvPvrManager.getPlaybackSpeed();
        if (DEBUG)
            Log.d(TAG,
                    "setPlaybackSpeed, speed=" + speed + ", current speed=" + enumSpeed.getValue());
        if ((speed > 0) && ( enumSpeed.getValue() < 0)) {
            if (DEBUG)
                Log.d(TAG, "setPlaybackSpeed, call setPlaybackSpeedInFloat(1)");
            mTvPvrManager.setPlaybackSpeedInFloat(1);
        } else if ((speed < 0) && ( enumSpeed.getValue() > 0)) {
            if (DEBUG)
                Log.d(TAG, "setPlaybackSpeed, call setPlaybackSpeedInFloat(1)");
            mTvPvrManager.setPlaybackSpeedInFloat(1);
        } else {
            //do nothing
        }
        if (DEBUG)
            Log.d(TAG, "setPlaybackSpeed, call setPlaybackSpeedInFloat(" + speed + ")");
        mTvPvrManager.setPlaybackSpeedInFloat(speed);

    }

    private short convertFsType(String fsTypeString) {
        if (fsTypeString == null) {
            Log.e(TAG, "invalid fsType name!!");
            return -1;
        } else if (fsTypeString.equals("jffs2")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_JFFS2;
        } else if (fsTypeString.equals("vfat")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_VFAT;
        } else if (fsTypeString.equals("ext2")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_EXT2;
        } else if (fsTypeString.equals("ext3")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_EXT3;
        } else if (fsTypeString.equals("msdos")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_MSDOS;
        } else if (fsTypeString.equals("ntfs")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_NTFS;
        } else if (fsTypeString.equals("exfat")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_EXFAT;
        } else if (fsTypeString.equals("ext4")) {
            return (short)PvrManager.E_FILE_SYSTEM_TYPE_EXT4;
        } else {
            Log.e(TAG, "no fsType can mapped!!");
            return -1;
        }
    }

    public void enableAlwaysTimeShift() {
        if (DEBUG)
            Log.d(TAG, "enableAlwaysTimeShift");
        mTvPvrManager.setAlwaysTimeShift(true);
    }

    public void doPlaybackJumpForward() {
        if (DEBUG)
            Log.d(TAG, "doPlaybackJumpForward");
        mTvPvrManager.doPlaybackJumpForward();
    }

    public void doPlaybackJumpBackward() {
        if (DEBUG)
            Log.d(TAG, "doPlaybackJumpBackward");
        mTvPvrManager.doPlaybackJumpBackward();
    }

    public void stopPVR() {
        if (DEBUG)
            Log.d(TAG, "stopPVR");
        mTvPvrManager.stopPvr();
    }

    private void updatePVREnabledFlag(Context context) {
        Cursor snconfig = context.getContentResolver().query(
                Uri.parse("content://mstar.tv.usersetting/snconfig"), null,
                null, null, null);
        if (snconfig != null) {
            if (snconfig.moveToFirst()) {
                isPVREnabled = snconfig.getInt(snconfig.getColumnIndex("PVR_ENABLE")) == 1 ? true : false;
            }
            if (DEBUG)
                Log.d(TAG, "set PVR enabled flag:" + isPVREnabled);
        }
    }

    public boolean isPVRsupported() {
        return isPVREnabled;
    }

    public boolean start() {
        enableAlwaysTimeShift();
        StorageVolume vol = getExternalStorageVolumeInfo(null);
        if (vol == null) {
            if (DEBUG)
                Log.d(TAG, "start fail, vol == null");
            return false;
        }
        EnumPvrStatus status = startAlwaysTimeShiftRecord(vol);
        if (status != EnumPvrStatus.E_SUCCESS) {
            if (DEBUG)
                Log.d(TAG, "start fail");
            return false;
        }
        return true;
    }

    public void stop() {
        stopPVR();
    }

    private class PvrEventListener implements OnPvrEventListener {
        @Override
        public boolean onPvrEvent(int what, int arg1, int arg2, Object obj) {
            if (DEBUG)
                Log.d(TAG, "PvrEventListener, onPvrEvent, what=" + what);
            switch (what) {
                case TvChannelManager.TVPLAYER_PVR_PLAYBACK_STOP: {
                    if (DEBUG)
                        Log.d(TAG, "PvrEventListener, onPvrEvent, get TVPLAYER_PVR_PLAYBACK_STOP");
                    //exit playback and back to live
                    mTvPvrManager.stopAlwaysTimeShiftPlayback();
                    break;
                }
                case TvChannelManager.TVPLAYER_PVR_PLAYBACK_BEGIN: {
                    if (DEBUG)
                        Log.d(TAG, "PvrEventListener, onPvrEvent, get TVPLAYER_PVR_PLAYBACK_BEGIN");
                    //set speed to 1x for normal play
                    mTvPvrManager.setPlaybackSpeed(
                            PvrPlaybackSpeed.EnumPvrPlaybackSpeed.E_PVR_PLAYBACK_SPEED_1X);
                    break;
                }
                case TvChannelManager.TVPLAYER_PVR_ALWAYS_TIMESHIFT_PROGRAM_READY: {
                    if (DEBUG)
                        Log.d(TAG, "PvrEventListener, onPvrEvent, get TVPLAYER_PVR_ALWAYS_TIMESHIFT_PROGRAM_READY");
                    //start alwaystimeshiftrecordstart
                    enableAlwaysTimeShift();
                    StorageVolume vol = getExternalStorageVolumeInfo(null);
                    startAlwaysTimeShiftRecord(vol);
                    break;
                    }
                default:
                    break;
            }
            return true;
        }
    }
}
