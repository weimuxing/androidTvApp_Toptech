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

package tvtests.com.mstar.android.tv.tuning;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;

public class TvScanBase {

    private static final String TAG = "TvScanBase";

    private Handler mAtvEventHandler = null;

    private Handler mDtvEventHandler = null;

    private TvChannelManager mTvChannelManager = null;

    private MockDtvEventListener mMockDtvEventListener;

    private MockAtvEventListener mMockAtvEventListener;

    protected TvScanBase() {
        mTvChannelManager = TvChannelManager.getInstance();
        mDtvEventHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                updateDtvTuningScanInfo((DtvEventScan)msg.obj);
            }
        };
        mAtvEventHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                updateAtvTuningScanInfo((AtvEventScan)msg.obj);
            }
        };
    }

    /**
     * Sleep function for Scan Class
     * @param millis
     */
    static void sleepMillis(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.w(TAG, "Warning, sleep was interrupted!" + e.getMessage());
        }
    }

    /**
     * Do Dtv Auto Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    public void doDtvAutoScan() {
        Log.w(TAG, "implement in child class");
    }

    /**
     * Do Atv Auto Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    public void doAtvAutoScan() {
        Log.w(TAG, "implement in child class");
    }

    /**
     * Do Dtv Manual Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    public void doDtvManualScan() {
        Log.w(TAG, "implement in child class");
    }

    /**
     * Do Atv Manual Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    public void doAtvManualScan() {
        Log.w(TAG, "implement in child class");
    }

    /**
     * Setup Dtv Manual Tuning Rf Channel Number.
     *
     * @param none.
     * @return none.
     */
    public void setDtvManualScanRf(int rf) {
        Log.w(TAG, "implement in child class");
    }

    public void registerScanListener() {
        Log.d(TAG, "register Scan Event Listener");
        mMockDtvEventListener = new MockDtvEventListener();
        mMockAtvEventListener = new MockAtvEventListener();
        // register event listener
        mTvChannelManager.registerOnDtvPlayerEventListener(mMockDtvEventListener);
        mTvChannelManager.registerOnAtvPlayerEventListener(mMockAtvEventListener);
    }

    public void unRegisterScanListener() {
        Log.d(TAG, "unregister Scan Event Listener");
        // unregister event listener
        mTvChannelManager.unregisterOnDtvPlayerEventListener(mMockDtvEventListener);
        mTvChannelManager.unregisterOnAtvPlayerEventListener(mMockAtvEventListener);
        mMockDtvEventListener = null;
        mMockAtvEventListener = null;
    }

    // This is DtvPlayerEventListener, listen for event from native code.
    protected class MockDtvEventListener implements OnDtvPlayerEventListener {
        @Override
        public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
            Message msg = mDtvEventHandler.obtainMessage(what, extra);
            mDtvEventHandler.sendMessage(msg);
            return true;
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
        public boolean onAudioModeChange(int arg0, boolean arg1) {
            return false;
        }

        @Override
        public boolean onChangeTtxStatus(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onCiLoadCredentialFail(int what) {
            return false;
        }

        @Override
        public boolean onDtvAutoUpdateScan(int what) {
            return false;
        }

        @Override
        public boolean onDtvChannelNameReady(int what) {
            return false;
        }

        @Override
        public boolean onDtvPriComponentMissing(int what) {
            return false;
        }

        @Override
        public boolean onDtvProgramInfoReady(int what) {
            return false;
        }

        @Override
        public boolean onEpgTimerSimulcast(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onGingaStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onHbbtvStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onMheg5EventHandler(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onMheg5ReturnKey(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onMheg5StatusMode(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadDownload(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadHandler(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onOadTimeout(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogFrequencyChange(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogLossSignal(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogNewMultiplex(int what) {
            return false;
        }

        @Override
        public boolean onRctPresence(int what) {
            return false;
        }

        @Override
        public boolean onTsChange(int what) {
            return false;
        }

        @Override
        public boolean onUiOPRefreshQuery(int arg0) {
            return false;
        }

        @Override
        public boolean onUiOPServiceList(int arg0) {
            return false;
        }

        @Override
        public boolean onUiOPExitServiceList(int arg0) {
            return false;
        }
    }

    // This is AtvPlayerEventListener, listen for event from native code.
    protected class MockAtvEventListener implements OnAtvPlayerEventListener {
        @Override
        public boolean onAtvAutoTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvEventHandler.obtainMessage(what, extra);
            mAtvEventHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onAtvManualTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvEventHandler.obtainMessage(what, extra);
            mAtvEventHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onAtvProgramInfoReady(int what) {
            Log.v(TAG, "onAtvProgramInfoReady called");
            return false;
        }

        @Override
        public boolean onSignalLock(int what) {
            Log.v(TAG, "onSignalLock called");
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            Log.v(TAG, "onSignalUnLock called");
            return false;
        }
    }

    protected void updateDtvTuningScanInfo(DtvEventScan extra) {
        Log.w(TAG, "Basic function of updating tuning scanInfo in TvScanBase");
        Log.w(TAG, "Please implment in your Scan class with respect to your tvsystem");
    }

    protected void updateAtvTuningScanInfo(AtvEventScan extra) {
        Log.w(TAG, "Basic function of updating tuning scanInfo in TvScanBase");
        Log.w(TAG, "Please implment in your Scan class with respect to your tvsystem");
    }

    protected void pauseChannelTuning() {
        switch (mTvChannelManager.getTuningStatus()) {
            case TvChannelManager.TUNING_STATUS_ATV_AUTO_TUNING:
                mTvChannelManager.pauseAtvAutoTuning();
                break;
            case TvChannelManager.TUNING_STATUS_DTV_AUTO_TUNING:
            case TvChannelManager.TUNING_STATUS_DTV_FULL_TUNING:
                mTvChannelManager.pauseDtvScan();
                break;
            default:
                break;
        }
    }

    protected void stopChannelTuningAndPlayProgram() {
        switch (mTvChannelManager.getTuningStatus()) {
            case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                mTvChannelManager.stopAtvAutoTuning();
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                break;
            case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                mTvChannelManager.stopDtvScan();
                if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                    boolean res = mTvChannelManager.stopAtvAutoTuning();
                    if (res == false) {
                        Log.e(TAG, "atvSetAutoTuningStart Error!!!");
                    }
                } else {
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
                break;
            default:
                break;
        }
    }
}
