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

package tvtests.com.mstar.android.tv;

import android.util.Log;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.atv.vo.EnumAtvManualTuneMode;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.vo.EnumDtvScanStatus;
import com.mstar.android.tvapi.common.vo.ProgramInfo;

import tvtests.com.mstar.android.tv.tuning.DtmbScan;

public class DtmbChannelTest extends TvChannelTestBase {

    private static final String TAG = "DtmbChannelTest";

    // atv scan start frequency
    private static int ATV_MIN_FREQ = 48250;

    // atv scan end frequency
    private static int ATV_MAX_FREQ = 877250;

    // every 500ms to show
    private static int ATV_EVENTINTERVAL = 500 * 1000;

    // dtv scan rf channel
    private static int DTV_SCAN_RF_CH = 22;

    private TvChannelManager mTvChannelManager = null;

    private TvCommonManager mTvCommonManager = null;

    private DtmbScan mDtmbScan = null;

    private int mDtmbRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    private int mDtvScanPercent = 0;

    private int mAtvScanPercent = 0;

    private int mDtvServiceCount = 0;

    public DtmbChannelTest() {
        if (null == mTvChannelManager) {
            mTvChannelManager = TvChannelManager.getInstance();
        }
        if (null == mTvCommonManager) {
            mTvCommonManager = TvCommonManager.getInstance();
        }
        if (null == mDtmbScan) {
            mDtmbScan = DtmbScan.getInstance();
        }
        if (TvChannelManager.TV_ROUTE_NONE == mDtmbRouteIndex) {
            mDtmbRouteIndex = mTvChannelManager.getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);
        }
    }

    public void setUp() throws Exception {
        Log.d(TAG, "setUp");
        super.setUp();
    }

    public void tearDown() throws Exception {
        Log.d(TAG, "tearDown");
        super.tearDown();
    }

    /**
     * DTV auto scan test
     *
     * pre-step : Register Scan Listener for listening ScanInfo.
     * check item : dtv Channel Number after tuning
     * pass: dtv Channel Number is more than zero.
     */
    @Override
    public void testDtvAutoScan() {
        Log.d(TAG, "testDtvAutoScan");
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleep(5000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV,
                mTvCommonManager.getCurrentTvInputSource());
        registerScanListener();
        TvDvbChannelManager.getInstance().setDtvAntennaType(mDtmbRouteIndex);
        mTvChannelManager.startDtvAutoScan();
        while (mDtvScanPercent < 100) {
            /* mDtvScanPercent is set as 100 while auto tuning done */
            sleep(500);
        }
        unRegisterScanListener();
        int dtvChCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        Log.d(TAG, "scaned dtv channel count is:" + dtvChCount);
        assertTrue("dtv channel count should be larger than zero after dtv autotuning!!",
                dtvChCount > 0);
    }

    /**
     * DTV program ManualTuning
     *
     * pre-step : RF has at least one channel or above, Register Scan Listener for listening ScanInfo.
     * check item : program can be changed by {@see com.mstar.android.tv.TvChannelManager#setDtvManualScanByRF()}
     * pass: {@see com.mstar.android.tv.TvChannelManager#setDtvManualScanByRF()} returns no error
     * fail: {@see com.mstar.android.tv.TvChannelManager#setDtvManualScanByRF()} returns error
     * check item : program can be changed by {@see com.mstar.android.tv.TvChannelManager#startDtvManualScan()}
     * pass: {@see com.mstar.android.tv.TvChannelManager#startDtvManualScan()} returns no error
     * fail: {@see com.mstar.android.tv.TvChannelManager#startDtvManualScan()} returns error
     */
    @Override
    public void testDtvManualTuning() {
        Log.d(TAG, "testDtvManuelTuning");
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        registerScanListener();
        mTvChannelManager.setDtvManualScanByRF(DTV_SCAN_RF_CH);
        mTvChannelManager.startDtvManualScan();
        /* add delay: impersonate the user operation */
        sleep(5000);
        unRegisterScanListener();
        int dtvChCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        Log.d(TAG, "dtvChCount:" + dtvChCount);
        assertTrue("channel list < 1, No DTV channel scanned", dtvChCount >= 1);
    }

    /**
     * DTV auto scan, test scan pause and stop command is workable.
     *
     *    pause command: {@see com.mstar.android.tv.TvChannelManager#pauseDtvScan()}
     *    stop command: {@see com.mstar.android.tv.TvChannelManager#stopDtvScan()}
     *
     * pre-step : change list has no channels
     * check item : channel scan status
     * pass: channel scan status is correctly
     * fail: channel scan status is not correctly
     */
    @Override
    public void testDtvAutoScanPauseStop() {
        Log.d(TAG, "testDtvAutoScanPauseStop");
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleep(5000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV,
                mTvCommonManager.getCurrentTvInputSource());
        registerScanListener();
        TvDvbChannelManager.getInstance().setDtvAntennaType(mDtmbRouteIndex);
        mTvChannelManager.startDtvAutoScan();
        while (10 == mDtvScanPercent) {
            /* mDtvScanPercent is set as 100 while auto tuning done */
            sleep(500);
        }
        unRegisterScanListener();
        assertTrue("Fail to pauseDtvScan!", mTvChannelManager.pauseDtvScan());
        Log.d(TAG, "DtvTuning: stop DTV tuning ");
        assertTrue("Fail to pauseDtvScan!", mTvChannelManager.stopDtvScan());
    }

    /**
     * ATV auto scan test
     *
     * pre-step : Register Scan Listener for listening ScanInfo.
     * check item : channel count of channel list
     * pass: channel count is not zero
     * fail: channel count is zero
     */
    @Override
    public void testAtvAutoScan() {
        Log.d(TAG, "testAtvAutoScan");
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }
        /* add delay: impersonate the user operation */
        sleep(5000);
        assertEquals(TvCommonManager.INPUT_SOURCE_ATV,
                mTvCommonManager.getCurrentTvInputSource());
        registerScanListener();
        mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
        mAtvScanPercent = 0;
        while (mAtvScanPercent < 100) {
            /* mAtvScanDone is set as true while auto tuning done */
            sleep(500);
        }
        unRegisterScanListener();
        int atvChCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
        Log.d(TAG, "scaned atv channel count is:" + atvChCount);
        assertTrue("atv channel count should be larger than zero after atv autotuning!!",
                atvChCount > 0);
    }

    /**
     * ATV manual tuning test.
     * <p>
     *  Step 1: try to change frequency (fine tune frequency, direction = up/down)
     * <p>
     *  Step 2: try to change frequency (search one channel, direction = up)
     * <p>
     * pre-step:
     * <ul>
     * <li> channel list has channels
     * </ul>
     * <p>
     * check item :
     * <ul>
     * <li> the result of fine tune mode
     * <li> the result of search mode
     * </ul>
     * pass: all of following cases are passed
     * <ul>
     * <li> fine tune mode can be performed with no errors
     * <li> search mode can be performed with no errors
     * </ul>
     * fail: one of following cases is failed
     * <ul>
     * <li> fine tune mode can be performed
     * <li> search mode can be performed
     * </ul>
     */
    @Override
    public void testAtvManualTuning() {
        boolean ret;
        int nCurrentFrequency;
        int curChannelNumber;
        int atvChCount;
        int setting;
        ProgramInfo programInfo = null;

        Log.d(TAG, "testAtvManualTuning");

        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }

        /* switch to first channel */
        programInfo = mTvChannelManager.getProgramInfoByIndex(0);
        mTvChannelManager.selectProgram(programInfo.number, TvChannelManager.SERVICE_TYPE_ATV);

        /** Step 1: change frequency */
        Log.d(TAG, "Manual tuning test -> change srequency");
        nCurrentFrequency = mTvChannelManager.getAtvCurrentFrequency();
        curChannelNumber = mTvChannelManager.getCurrentChannelNumber();
        assertTrue("check channel number failed", (curChannelNumber == programInfo.number));
        ret = mTvChannelManager.startAtvManualTuning(3 * 1000, nCurrentFrequency, EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_FINE_TUNE_UP);
        assertTrue("failed to start ATV manual tuning", (ret == true));
        /* add delay: impersonate the user operation */
        sleep(500);
        ret = mTvChannelManager.startAtvManualTuning(3 * 1000, nCurrentFrequency, EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_FINE_TUNE_DOWN);
        assertTrue("failed to start ATV manual tuning", (ret == true));
        /* add delay: impersonate the user operation */
        sleep(500);
        ret = mTvChannelManager.startAtvManualTuning(3 * 1000, nCurrentFrequency, EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_FINE_TUNE_UP);
        assertTrue("failed to start ATV manual tuning", (ret == true));
        /* add delay: impersonate the user operation */
        sleep(500);
        mTvChannelManager.stopAtvManualTuning();
        ret = mTvChannelManager.saveAtvProgram(curChannelNumber);
        assertTrue("failed to save ATV program", (ret == true));
        mTvChannelManager.selectProgram(curChannelNumber, TvChannelManager.SERVICE_TYPE_ATV);

        /** Step 2: search one channel */
        Log.d(TAG, "Manual tuning test -> search");
        nCurrentFrequency = mTvChannelManager.getAtvCurrentFrequency();
        curChannelNumber = mTvChannelManager.getCurrentChannelNumber();
        ret = mTvChannelManager.startAtvManualTuning(5 * 1000, nCurrentFrequency, EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_SEARCH_ONE_TO_UP);
        assertTrue("failed to start ATV manual tuning", (ret == true));
        /* add delay: impersonate the user operation */
        sleep(1000);
        mTvChannelManager.stopAtvManualTuning();
        ret = mTvChannelManager.saveAtvProgram(curChannelNumber);
        assertTrue("failed to save ATV program", (ret == true));
    }

    /**
     * ATV auto scan, test scan pause and stop command is workable.
     *
     *    pause command: {@see com.mstar.android.tv.TvChannelManager#pauseAtvAutoTuning()}
     *    stop command: {@see com.mstar.android.tv.TvChannelManager#stopAtvAutoTuning()}
     *
     * pre-step : change list has no channels
     * check item : channel scan status
     * pass: channel scan status is correctly
     * fail: channel scan status is not correctly
     */
    @Override
    public void testAtvAutoScanPauseStop() {
        Log.d(TAG, "testAtvAutoScanPauseStop");

        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }
        /* add delay: impersonate the user operation */
        sleep(5000);
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            assertTrue("Fatal error! input source incorrect!", false);
        }
        registerScanListener();
        mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
        mAtvScanPercent = 0;
        while (10 == mAtvScanPercent) {
            /* mAtvScanPercent is set as 100 while auto tuning done */
            sleep(500);
        }
        unRegisterScanListener();
        if (mTvChannelManager.pauseAtvAutoTuning() != true) {
            assertTrue("Fail to pauseAtvScan!", false);
        }
        if (mTvChannelManager.stopAtvAutoTuning() != true) {
            assertTrue("Fail to stopAtvScan!", false);
        }
    }

    /**
     * DTV program up 10 times test
     *
     * pre-step : channel list has two channels or above
     * check item : program can be changed by {@see com.mstar.android.tv.TvChannelManager#programUp()}
     * pass: {@see com.mstar.android.tv.TvChannelManager#programUp()} returns no error and program can be changed
     * fail: {@see com.mstar.android.tv.TvChannelManager#programUp()} returns error or program switching failed
     */
    public void testDtvChannelUp() {
        Log.d(TAG, "testDtvProgramUp10Times");
        boolean ret;
        ProgramInfo programInfo = null;
        ProgramInfo currentProgramInfo = null;

        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleep(5000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV,
                mTvCommonManager.getCurrentTvInputSource());

        assertTrue("no channel exist!", (checkDtvChannelCount() >= 2));
        Log.d(TAG, "channel switching test -> test TvChannelManager.programUp()");
        for (int i = 0; i < 10; i++) {
            programInfo = mTvChannelManager.getCurrentProgramInfo();
            Log.d(TAG, "DTV program up test, count = " + (i+1));
            ret = mTvChannelManager.programUp();
            assertTrue("failed to execure programUp", (ret == true));
            /* add delay: impersonate the user operation */
            sleep(1000);
            currentProgramInfo = mTvChannelManager.getCurrentProgramInfo();
            assertTrue("programUp() failed, the channel is not changed!", (currentProgramInfo.number != programInfo.number));
        }
    }

    /**
     * DTV program down 10 times test
     *
     * pre-step : channel list has two channels or above
     * check item : program can be changed by {@see com.mstar.android.tv.TvChannelManager#programDown()}
     * pass: {@see com.mstar.android.tv.TvChannelManager#programDown()} returns no error and program can be changed
     * fail: {@see com.mstar.android.tv.TvChannelManager#programDown()} returns error or program switching failed
     */
    public void testDtvChannelDown() {
        Log.d(TAG, "testDtvProgramDown10Times");
        boolean ret;
        ProgramInfo programInfo = null;
        ProgramInfo currentProgramInfo = null;

        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleep(5000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV,
                mTvCommonManager.getCurrentTvInputSource());

        assertTrue("no channel exist!", (checkDtvChannelCount() >= 2));
        Log.d(TAG, "channel switching test -> test TvChannelManager.programDown()");
        for (int i = 0; i < 10; i++) {
            programInfo = mTvChannelManager.getCurrentProgramInfo();
            Log.d(TAG, "DTV program down test, count = " + (i+1));
            ret = mTvChannelManager.programDown();
            assertTrue("failed to execure programDown", (ret == true));
            /* add delay: impersonate the user operation */
            sleep(1000);
            currentProgramInfo = mTvChannelManager.getCurrentProgramInfo();
            assertTrue("programDown() failed, the channel is not changed!", (currentProgramInfo.number != programInfo.number));
        }
    }

    /**
     * To check the channel lsit is empty or not.
     *
     * @return boolean true: channel list is empty, false: channle list has channels
     */
    private int checkDtvChannelCount() {
        Log.d(TAG, "checking Dtv Channel Count");
        boolean ret = false;
        int dtvChCount = 0;
        dtvChCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        Log.d(TAG, "scaned dtv channel count is:" + dtvChCount);
        if (dtvChCount < 2) {
            mDtmbScan.doDtvAutoScan();
            /* add delay: impersonate the user operation */
            sleep(1000);
            dtvChCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            Log.d(TAG, "scaned dtv channel count is:" + dtvChCount);
        }
        return dtvChCount;
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
