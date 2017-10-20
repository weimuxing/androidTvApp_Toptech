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

package com.mstar.android.tv.inputservice;

import android.content.Context;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.media.tv.TvInputHardwareInfo;
import android.media.tv.TvContract;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.HdmiTvClient;
import android.hardware.hdmi.HdmiPlaybackClient;
import android.hardware.hdmi.HdmiTvClient.InputChangeListener;
import android.hardware.hdmi.HdmiTvClient.SelectCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.SparseArray;
import android.util.Log;
import android.view.KeyEvent;

import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tv.inputservice.data.WindowControl;

import org.xmlpull.v1.XmlPullParserException;

import java.util.List;
import java.io.IOException;

/**
 * HdmiInputService
 */
public class HdmiInputService extends BaseTvInputService {
    private static final String TAG = "HdmiInputService";
    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);
    public static final int PORT1 = 1;
    public static final int PORT2 = 2;
    public static final int PORT3 = 3;
    public static final int PORT4 = 4;
    protected static HdmiTvClient mHdmiTvClient;
    protected static HdmiPlaybackClient mHdmiPlaybackClient;
    private InputChangeListenerImpl mInputChangeListener = null;

    @Override
    public String onHdmiDeviceRemoved(HdmiDeviceInfo deviceInfo) {
        TvInputManager manager = (TvInputManager) this.getSystemService(Context.TV_INPUT_SERVICE);
        List<TvInputInfo> tvInputList = manager.getTvInputList();
        String inputId = null;
        for (TvInputInfo info : tvInputList) {
            if (info.getType() == TvInputInfo.TYPE_HDMI && info.getHdmiDeviceInfo() != null) {
                if (info.getHdmiDeviceInfo().getId() == deviceInfo.getId()) {
                    inputId = info.getId();
                    break;
                }
            }
        }
        return inputId;
    }

    protected TvInputInfo createTvInputInfoForHardware(ResolveInfo resInfo, TvInputHardwareInfo hardwareInfo) {
        TvInputInfo tvInfo = null;
        try {
            tvInfo= TvInputInfo.createTvInputInfo(this, resInfo, hardwareInfo, null, null);
            if (mInputChangeListener == null) {
                if (DEBUG) Log.d(TAG, "createTvInputInfoForHardware: register InputChangeListener");
                HdmiControlManager hdmiManager = (HdmiControlManager) getSystemService(Context.HDMI_CONTROL_SERVICE);
                mHdmiTvClient = hdmiManager.getTvClient();
                mHdmiPlaybackClient = hdmiManager.getPlaybackClient();
                if (mHdmiTvClient != null) {
                    mInputChangeListener = new InputChangeListenerImpl();
                    mHdmiTvClient.setInputChangeListener(mInputChangeListener);
                }
            }
        } catch (XmlPullParserException | IOException e) {
        }
        return tvInfo;
    }

    protected TvInputInfo createTvInputInfoForDevice(ResolveInfo resInfo, HdmiDeviceInfo deviceInfo, String parentId) {
        TvInputInfo tvInfo = null;
        try {
            tvInfo= TvInputInfo.createTvInputInfo(this, resInfo, deviceInfo, parentId, null, null);
        } catch (XmlPullParserException | IOException e) {
        }
        return tvInfo;
    }

    private class InputChangeListenerImpl implements InputChangeListener {
        @Override
        public void onChanged(HdmiDeviceInfo info) {
            if (DEBUG) {
                Log.d(TAG, "InputChangeListenerImpl::onChanged: id = " + info.getId() +
                        ", port id = " + info.getPortId() +
                        ", device type = " + info.getDeviceType() +
                        ", logical addr = " + info.getLogicalAddress());
            }
            TvInputManager manager = (TvInputManager) getSystemService(Context.TV_INPUT_SERVICE);
            List<TvInputInfo> tvInputList = manager.getTvInputList();
            for (TvInputInfo tvInputInfo : tvInputList) {
                if (tvInputInfo.getType() == TvInputInfo.TYPE_HDMI && tvInputInfo.getHdmiDeviceInfo() != null) {
                    if (tvInputInfo.getHdmiDeviceInfo().getId() == info.getId()) {
                        Log.d(TAG, "onChanged: start intent uri " +
                                TvContract.buildChannelUriForPassthroughInput(tvInputInfo.getId()));
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                TvContract.buildChannelUriForPassthroughInput(tvInputInfo.getId()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                }
            }
        }
    }

    static class HdmiSessionImpl extends SessionImpl {
        private static SelectCallbackImpl mSelectCallback = new SelectCallbackImpl();
        private TvMhlManager mTvMhlManager;

        HdmiSessionImpl(Context context, String inputId) {
            super(context, inputId);
            mTvMhlManager = TvMhlManager.getInstance();
        }

        @Override
        public void onSetMain(boolean isMain) {
            if (DEBUG) Log.d(TAG, "onSetMain: mInputId = " + mInputId + ", isMain = " + isMain);
            TvInputInfo info = mTvInputManager.getTvInputInfo(mInputId);
            if (mHdmiTvClient != null) {
                if (isMain && info.getHdmiDeviceInfo() != null) {
                    int logicalAddress = info.getHdmiDeviceInfo().getLogicalAddress();
                    mHdmiTvClient.deviceSelect(logicalAddress, mSelectCallback);
                } else {
                    mHdmiTvClient.deviceSelect(0, mSelectCallback);
                }
            }
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (DEBUG) Log.d(TAG, "onKeyDown: " + event);
            if (WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_MAIN) {
                if (mHdmiTvClient != null) {
                    // This is because we don't have DPAD_CENTER key, the key code send from our IR driver is ENTER,
                    // But actually, the confirm key should be DPAD_CENTER, but not ENTER.
                    // It will cause we cant not use confirm key to controll the CEC device, because we don't have confirm key (DPAD_CENTER).
                    // So we need to transform KEYCODE_ENTER to KEYCODE_DPAD_CENTER to send to CEC device.
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        keyCode = KeyEvent.KEYCODE_DPAD_CENTER;
                    }
                    mHdmiTvClient.sendKeyEvent(keyCode, true);
                }
                if (mTvMhlManager.CbusStatus() &&
                        mTvMhlManager.IsMhlPortInUse()) {
                    if (mTvMhlManager.IRKeyProcess(keyCode, false)) {
                        SystemClock.sleep(140);
                        return true;
                    }
                }
            }
            return super.onKeyDown(keyCode, event);
        }

        private static class SelectCallbackImpl implements SelectCallback {
            @Override
            public void onComplete(int result) {
                Log.d(TAG, "SelectCallbackImpl::onComplete: result = " + result);
            }
        }
    }
}
