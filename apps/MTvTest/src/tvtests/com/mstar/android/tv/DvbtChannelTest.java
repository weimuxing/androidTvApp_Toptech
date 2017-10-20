package tvtests.com.mstar.android.tv;

import android.util.Log;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.vo.EnumDtvScanStatus;

import tvtests.com.mstar.android.tv.tuning.DtmbScan;

public class DvbtChannelTest extends TvChannelTestBase {

    private static final String TAG = "DvbtChannelTest";

    // atv scan start frequency
    private static int ATV_MIN_FREQ = 48250;

    // atv scan end frequency
    private static int ATV_MAX_FREQ = 877250;

    // every 500ms to show
    private static int ATV_EVENTINTERVAL = 500 * 1000;

    // dtv scan rf channel
    private static int DTV_SCAN_RF_CH = 32;

    private TvChannelManager mTvChannelManager = null;

    private TvCommonManager mTvCommonManager = null;

    private int mDvbtRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    private int mDtvScanPercent = 0;

    private int mAtvScanPercent = 0;

    private int mDtvServiceCount = 0;

    public DvbtChannelTest() {
        if (null == mTvChannelManager) {
            mTvChannelManager = TvChannelManager.getInstance();
        }
        if (null == mTvCommonManager) {
            mTvCommonManager = TvCommonManager.getInstance();
        }
        if (TvChannelManager.TV_ROUTE_NONE == mDvbtRouteIndex) {
            mDvbtRouteIndex = mTvChannelManager.getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT);
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
        TvDvbChannelManager.getInstance().setDtvAntennaType(mDvbtRouteIndex);
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
        TvDvbChannelManager.getInstance().setDtvAntennaType(mDvbtRouteIndex);
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
     *  Step 1: try to change color system value
     * <ul>
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_PAL_BGHI}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_NTSC_M}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_SECAM}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_NTSC_44}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_PAL_M}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_PAL_N}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_PAL_60}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_NOTSTANDARD}
     * <li> {@see com.mstar.android.tv.TvChannelManager#AVD_VIDEO_STANDARD_AUTO}
     * </ul>
     * <p>
     *  Step 2: try to change sound system value
     * <ul>
     * <li> {@see com.mstar.android.tv.TvChannelManager#ATV_AUDIO_STANDARD_BG}
     * <li> {@see com.mstar.android.tv.TvChannelManager#ATV_AUDIO_STANDARD_DK}
     * <li> {@see com.mstar.android.tv.TvChannelManager#ATV_AUDIO_STANDARD_I}
     * <li> {@see com.mstar.android.tv.TvChannelManager#ATV_AUDIO_STANDARD_L}
     * <li> {@see com.mstar.android.tv.TvChannelManager#ATV_AUDIO_STANDARD_M}
     * </ul>
     * <p>
     *  Step 3: try to change frequency (fine tune frequency, direction = up/down)
     * <p>
     *  Step 4: try to change frequency (search one channel, direction = up)
     * <p>
     * pre-step:
     * <ul>
     * <li> channel list has channels
     * </ul>
     * <p>
     * check item :
     * <ul>
     * <li> the value of color system
     * <li> the value of sound system
     * <li> the result of fine tune mode
     * <li> the result of search mode
     * </ul>
     * pass: all of following cases are passed
     * <ul>
     * <li> the value of color system can be changed
     * <li> the value of sound system can be changed
     * <li> fine tune mode can be performed with no errors
     * <li> search mode can be performed with no errors
     * </ul>
     * fail: one of following cases is failed
     * <ul>
     * <li> the value of color system is not changed correctly
     * <li> the value of sound system is not changed correctly
     * <li> fine tune mode can be performed
     * <li> search mode can be performed
     * </ul>
     */
    @Override
    public void testAtvManualTuning() {
        Log.d(TAG, "testAtvManualTuning");
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
