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

import android.util.Log;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.vo.EnumDtvScanStatus;

public class DtmbScan extends TvScanBase {

    private static final String TAG = "DtmbScan";

    static DtmbScan mInstance = null;

    // atv scan start frequency
    private static final int ATV_MIN_FREQ = 48250;

    // atv scan end frequency
    private static final int ATV_MAX_FREQ = 877250;

    // every 500ms to show
    private static final int ATV_EVENTINTERVAL = 500 * 1000;

    // dtv scan rf channel
    private static final int DTV_SCAN_RF_CH = 22;

    private int mDtvScanPercent = 0;

    private int mAtvScanPercent = 0;

    private int mDtvServiceCount = 0;

    private int mDtvManualScanRf = DTV_SCAN_RF_CH;

    private TvChannelManager mTvChannelManager = null;

    private TvCommonManager mTvCommonManager = null;

    private int mDtmbRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    protected DtmbScan() {
        mTvChannelManager = TvChannelManager.getInstance();
        mTvCommonManager = TvCommonManager.getInstance();
        mDtmbRouteIndex = mTvChannelManager.getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);
    }

    /**
     * Singleton getInstance function
     *
     * @return Instance of DtmbScan
     */
    public static DtmbScan getInstance() {
        /* Double-checked locking */
        if (mInstance == null) {
            synchronized (DtmbScan.class) {
                if (mInstance == null) {
                    mInstance = new DtmbScan();
                }
            }
        }
        return mInstance;
    }

    /**
     * Do Dtv Auto Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    @Override
    public void doDtvAutoScan() {
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleepMillis(5000);
        super.registerScanListener();
        TvDvbChannelManager.getInstance().setDtvAntennaType(mDtmbRouteIndex);
        mTvChannelManager.startDtvAutoScan();
        while (mDtvScanPercent < 100) {
            /* mDtvScanPercent is set as true while auto tuning done */
            sleepMillis(500);
        }
        super.unRegisterScanListener();
    }

    /**
     * Do Atv Auto Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    @Override
    public void doAtvAutoScan() {
        super.registerScanListener();
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }
        /* add delay: impersonate the user operation */
        sleepMillis(5000);
        mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
        while (mAtvScanPercent < 100) {
            /* mAtvScanDone is set as true while auto tuning done */
            sleepMillis(500);
        }
        super.unRegisterScanListener();
    }

    /**
     * Do Dtv Manual Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    @Override
    public void doDtvManualScan() {
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleepMillis(5000);
        super.registerScanListener();
        mTvChannelManager.setDtvManualScanByRF(mDtvManualScanRf);
        mTvChannelManager.startDtvManualScan();
        /* add delay: impersonate the user operation */
        sleepMillis(3000);
        super.unRegisterScanListener();
    }

    /**
     * Do Atv Manual Tuning Operation.
     *
     * @param none.
     * @return none.
     */
    @Override
    public void doAtvManualScan() {

    }

    /**
     * Setup Dtv Manual Tuning Rf Channel Number.
     *
     * @param none.
     * @return none.
     */
    @Override
    public void setDtvManualScanRf(int rf) {
        Log.d(TAG, "set Dtv Manual Scan Rf Number to " + rf);
        mDtvManualScanRf = rf;
    }

    @Override
    protected void updateDtvTuningScanInfo(DtvEventScan extra) {
        String str;
        int dtv = extra.dtvSrvCount;
        int radio = extra.radioSrvCount;
        int data = extra.dataSrvCount;
        int percent = extra.scanPercentageNum;
        int currRFCh = extra.currRFCh;
        int scan_status = extra.scanStatus;
        mDtvScanPercent = percent;

        str = "" + (dtv + radio + data);
        Log.d(TAG, "aridtv = " + str);
        str = "" + dtv;
        Log.d(TAG, "dtv program = " + str);
        str = "" + radio;
        Log.d(TAG, "audio program = " + str);
        str = "" + data;
        Log.d(TAG, "data program= " + str);
        str = "" + percent + '%';
        Log.d(TAG, "tuning progress = " + str);
        str = "" + currRFCh;
        Log.d(TAG, "tuning RF = " + str);

        if (scan_status == EnumDtvScanStatus.E_STATUS_SCAN_END.ordinal()) {
                if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL ||
                        mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV) {
                    mDtvServiceCount = dtv + radio + data;
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                    super.pauseChannelTuning();
                    super.stopChannelTuningAndPlayProgram();
                    /* force define scan percent to 100 to stop testDtvAutoScan */
                    mDtvScanPercent = 100;
                }
        }
    }

    @Override
    protected void updateAtvTuningScanInfo(AtvEventScan extra) {
        String str = new String();
        int percent = extra.percent;
        int frequencyKHz = extra.frequencyKHz;
        int scannedChannelNum = extra.scannedChannelNum;
        int curScannedChannel = extra.curScannedChannel;
        mAtvScanPercent = percent;

        str = "" + scannedChannelNum;
        Log.d(TAG, "scanned channel Number = " + str);

        str = "" + curScannedChannel;
        Log.d(TAG, "current Scan Channel = " + str);

        String sFreq = " " + (frequencyKHz / 1000) + "." + (frequencyKHz % 1000) / 10
                    + "Mhz";
        str = "" + percent + '%' + sFreq;
        Log.d(TAG, "scan progress = " + str);
        if ((percent >= 100) || (frequencyKHz > ATV_MAX_FREQ)) {
            /* force define scan percent to 100 to stop testAtvAutoScan */
            mAtvScanPercent = 100;
            mTvChannelManager.stopAtvAutoTuning();

            if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                if (mDtvServiceCount > 0) {
                    if (TvCommonManager.getInstance().getCurrentTvInputSource()
                            != TvCommonManager.INPUT_SOURCE_DTV) {
                        TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                    }
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                } else {
                    if (TvCommonManager.getInstance().getCurrentTvInputSource()
                            != TvCommonManager.INPUT_SOURCE_ATV) {
                        TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                    }
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
            } else {
                if (TvCommonManager.getInstance().getCurrentTvInputSource()
                        != TvCommonManager.INPUT_SOURCE_ATV) {
                    TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                }
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
            }
        }
    }

}
