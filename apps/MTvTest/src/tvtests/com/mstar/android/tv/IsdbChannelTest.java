package tvtests.com.mstar.android.tv;

import android.util.Log;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.dtv.vo.EnumDtvScanStatus;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;

public class IsdbChannelTest extends TvChannelTestBase {
    private static final String TAG = "IsdbChannelTest";

    // atv scan start frequency
    private static int ATV_MIN_FREQ = 48250;

    // atv scan end frequency
    private static int ATV_MAX_FREQ = 877250;

    // every 500ms to show
    private static int ATV_EVENTINTERVAL = 500 * 1000;

    private TvChannelManager mTvChannelManager = null;

    private TvCommonManager mTvCommonManager = null;

    private int mDtvScanPercent = 0;

    private int mAtvScanPercent = 0;

    private int mAtvFrequencyKHz = 0;

    private int mDtvServiceCount = 0;

    private int mIsdbRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    public IsdbChannelTest() {
        if (null == mTvChannelManager) {
            mTvChannelManager = TvChannelManager.getInstance();
        }
        if (null == mTvCommonManager) {
            mTvCommonManager = TvCommonManager.getInstance();
        }
        if (TvChannelManager.TV_ROUTE_NONE == mIsdbRouteIndex) {
            mIsdbRouteIndex = mTvChannelManager.getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_ISDB);
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
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_DTV, mTvCommonManager.getCurrentTvInputSource());

        TvIsdbChannelManager.getInstance().setDtvAntennaType(mIsdbRouteIndex);
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
        sleep(3000);
        assertEquals(TvCommonManager.INPUT_SOURCE_ATV, mTvCommonManager.getCurrentTvInputSource());

        registerScanListener();
        mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);

        while ((mAtvScanPercent < 100) && (mAtvFrequencyKHz < ATV_MAX_FREQ)) {
            sleep(500);
            Log.d(TAG, "AtvScanPercent: " + mAtvScanPercent + " AtvFrequencyKHz: " + mAtvFrequencyKHz);
            Log.d(TAG, "waiting atv auto scan done");
        }
        unRegisterScanListener();

        int atvChCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
        Log.d(TAG, "scaned atv channel count is:" + atvChCount);
        assertTrue("atv channel count should be larger than zero after atv autotuning!!",
                atvChCount > 0);
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
            if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                mDtvServiceCount = dtv + radio + data;
                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ,
                        ATV_MAX_FREQ);
            } else if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV) {
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                super.pauseChannelTuning();
                super.stopChannelTuningAndPlayProgram();
                //force define scan percent to 100 to stop testDtvAutoScan
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
        mAtvFrequencyKHz = frequencyKHz;

        str = "" + scannedChannelNum;
        Log.d(TAG, "scanned channel Number = " + str);

        str = "" + curScannedChannel;
        Log.d(TAG, "current Scan Channel = " + str);

        String sFreq = " " + (frequencyKHz / 1000) + "." + (frequencyKHz % 1000) / 10
                    + "Mhz";
        str = "" + percent + '%' + sFreq;
        Log.d(TAG, "scan progress = " + str);
        if ((percent >= 100) || (frequencyKHz > ATV_MAX_FREQ)) {
            //force define scan percent to 100 to stop testAtvAutoScan
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
            super.pauseChannelTuning();
            super.stopChannelTuningAndPlayProgram();
        }
    }

}
