package tvtests.com.mstar.android.tv;

import android.util.Log;

import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.vo.EnumDtvScanStatus;

import tvtests.com.mstar.android.tv.tuning.DtmbScan;

public class AtscChannelTest extends TvChannelTestBase {

    private static final String TAG = "AtscChannelTest";

    // atv scan start frequency
    private static int ATV_MIN_FREQ = 48250;

    // atv scan end frequency
    private static int ATV_MAX_FREQ = 877250;

    // every 500ms to show
    private static int ATV_EVENTINTERVAL = 500 * 1000;

    private TvChannelManager mTvChannelManager = null;

    private TvCommonManager mTvCommonManager = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private int mDtvScanPercent = 0;

    private int mAtvScanPercent = 0;

    private int mDtvServiceCount = 0;

    public AtscChannelTest() {
        if (null == mTvChannelManager) {
            mTvChannelManager = TvChannelManager.getInstance();
        }
        if (null == mTvCommonManager) {
            mTvCommonManager = TvCommonManager.getInstance();
        }
        if (null == mTvAtscChannelManager) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
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
        mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_DTV);
        mTvAtscChannelManager.deleteDtvMainList();
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV, mTvCommonManager.getCurrentTvInputSource());

        mTvAtscChannelManager.setDtvAntennaType(TvChannelManager.DTV_ANTENNA_TYPE_AIR);
        /* add delay: impersonate the user operation */
        sleep(3000);

        registerScanListener();
        mTvChannelManager.startDtvAutoScan();
        while (mDtvScanPercent < 100) {
            /* mDtvScanPercent is set as true while auto tuning done */
            sleep(500);
        }
        unRegisterScanListener();

        int dtvChCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        Log.d(TAG, "scaned dtv channel count is:" + dtvChCount);
        assertTrue("dtv channel count should be larger than zero after dtv autotuning!!",
            dtvChCount > 0);
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
        mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_DTV);
        mTvAtscChannelManager.deleteDtvMainList();
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV, mTvCommonManager.getCurrentTvInputSource());

        mTvAtscChannelManager.setDtvAntennaType(TvChannelManager.DTV_ANTENNA_TYPE_AIR);
        /* add delay: impersonate the user operation */
        sleep(3000);

        registerScanListener();
        mTvChannelManager.startDtvAutoScan();
        while (10 == mDtvScanPercent) {
            /* mDtvScanPercent is set as 100 while auto tuning done */
            sleep(500);
        }
        unRegisterScanListener();
        Log.d(TAG, "DtvTuning: pause DTV tuning ");
        assertTrue("Fail to pauseDtvScan!", mTvChannelManager.pauseDtvScan());
        Log.d(TAG, "DtvTuning: stop DTV tuning ");
        assertTrue("Fail to stopDtvScan!", mTvChannelManager.stopDtvScan());
    }

    /**
     * DTV auto scan, test scan pause and resume command is workable.
     *
     *    pause command: {@see com.mstar.android.tv.TvChannelManager#pauseDtvScan()}
     *    resume command: {@see com.mstar.android.tv.TvChannelManager#resumeDtvScan()}
     *
     * pre-step : change list has no channels
     * check item : channel scan status
     * pass: channel scan status is correctly
     * fail: channel scan status is not correctly
     */
    public void testDtvAutoScanPauseResume() {
        Log.d(TAG, "testDtvAutoScanPauseResume");
        mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_DTV);
        mTvAtscChannelManager.deleteDtvMainList();
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        /* add delay: impersonate the user operation */
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV, mTvCommonManager.getCurrentTvInputSource());

        mTvAtscChannelManager.setDtvAntennaType(TvChannelManager.DTV_ANTENNA_TYPE_AIR);
        /* add delay: impersonate the user operation */
        sleep(3000);

        registerScanListener();
        mTvChannelManager.startDtvAutoScan();
        while (20 == mAtvScanPercent) {
            /* mAtvScanPercent is set as 100 while auto tuning done */
            switch(mDtvScanPercent) {
                case 5:
                case 8:
                case 10:
                case 15:
                    assertTrue("Fail to pauseDtvScan!", mTvChannelManager.pauseDtvScan());
                    sleep(1000);
                    assertTrue("Fail to resumeDtvScan!", mTvChannelManager.resumeDtvScan());
                    break;
                default:
                    sleep(500);
                    break;
            }
        }
        Log.d(TAG, "DtvTuning: pause DTV tuning ");
        assertTrue("Fail to pauseDtvScan!", mTvChannelManager.pauseDtvScan());
        Log.d(TAG, "DtvTuning: stop DTV tuning ");
        assertTrue("Fail to stopDtvScan!", mTvChannelManager.stopDtvScan());
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
        mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_ATV);
        mTvAtscChannelManager.deleteAtvMainList();
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }
        /* add delay: impersonate the user operation */
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_ATV, mTvCommonManager.getCurrentTvInputSource());

        mTvAtscChannelManager.setDtvAntennaType(TvChannelManager.DTV_ANTENNA_TYPE_AIR);
        /* add delay: impersonate the user operation */
        sleep(3000);

        registerScanListener();
        mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
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
        mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_ATV);
        mTvAtscChannelManager.deleteAtvMainList();
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }
        /* add delay: impersonate the user operation */
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_ATV, mTvCommonManager.getCurrentTvInputSource());

        mTvAtscChannelManager.setDtvAntennaType(TvChannelManager.DTV_ANTENNA_TYPE_AIR);
        /* add delay: impersonate the user operation */
        sleep(3000);

        registerScanListener();
        mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
        while (10 == mAtvScanPercent) {
            /* mAtvScanDone is set as true while auto tuning done */
            sleep(500);
        }
        unRegisterScanListener();

        Log.d(TAG, "DtvTuning: pause ATV tuning ");
        assertTrue("Fail to pauseAtvScan!", mTvChannelManager.pauseAtvAutoTuning());
        Log.d(TAG, "DtvTuning: stop ATV tuning ");
        assertTrue("Fail to stopAtvScan!", mTvChannelManager.stopAtvAutoTuning());
    }

    /**
     * ATV auto scan, test scan pause and resume command is workable.
     *
     *    pause command: {@see com.mstar.android.tv.TvChannelManager#pauseAtvAutoTuning()}
     *    resume command: {@see com.mstar.android.tv.TvChannelManager#resumeAtvAutoTuning()}
     *
     * pre-step : change list has no channels
     * check item : channel scan status
     * pass: channel scan status is correctly
     * fail: channel scan status is not correctly
     */
    @Override
    public void testAtvAutoScanPauseResume() {
        Log.d(TAG, "testAtvAutoScanPauseResume");
        mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_ATV);
        mTvAtscChannelManager.deleteAtvMainList();
        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }
        /* add delay: impersonate the user operation */
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_ATV, mTvCommonManager.getCurrentTvInputSource());

        mTvAtscChannelManager.setDtvAntennaType(TvChannelManager.DTV_ANTENNA_TYPE_AIR);
        /* add delay: impersonate the user operation */
        sleep(3000);

        registerScanListener();
        mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
        while (20 == mAtvScanPercent) {
            /* mAtvScanPercent is set as 100 while auto tuning done */
            switch(mAtvScanPercent) {
                case 5:
                case 8:
                case 10:
                case 15:
                    assertTrue("Fail to pauseAtvScan!", mTvChannelManager.pauseAtvAutoTuning());
                    sleep(1000);
                    assertTrue("Fail to stopAtvScan!", mTvChannelManager.resumeAtvAutoTuning());
                    break;
                default:
                    sleep(500);
                    break;
            }
        }
        unRegisterScanListener();

        Log.d(TAG, "DtvTuning: pause ATV tuning ");
        assertTrue("Fail to pauseAtvScan!", mTvChannelManager.pauseAtvAutoTuning());
        Log.d(TAG, "DtvTuning: stop ATV tuning ");
        assertTrue("Fail to stopAtvScan!", mTvChannelManager.stopAtvAutoTuning());

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
