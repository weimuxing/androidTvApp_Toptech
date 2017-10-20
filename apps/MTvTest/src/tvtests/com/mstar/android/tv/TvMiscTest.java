
package tvtests.com.mstar.android.tv;

import android.util.Log;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tvapi.common.vo.EnumChannelSwitchMode;
import com.mstar.android.tvapi.common.vo.EnumPipReturn;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import tvtests.com.mstar.android.tv.TestBase;

/**
 * Test Cases of miscellaneous category
 */
public class TvMiscTest extends TestBase {

    private static final String TAG = "TvMiscTest";

    private TvCommonManager mTvCommonManager;

    private TvChannelManager mTvChannelManager;

    private TvPipPopManager mTvPipPopManager;

    /**
     * Input Sources used in the test
     *
     */
    public static final int[] sTestSources = {
            TvCommonManager.INPUT_SOURCE_ATV,
            TvCommonManager.INPUT_SOURCE_DTV,
            TvCommonManager.INPUT_SOURCE_HDMI,
            TvCommonManager.INPUT_SOURCE_HDMI2,
            TvCommonManager.INPUT_SOURCE_HDMI3,
            TvCommonManager.INPUT_SOURCE_HDMI4,
            TvCommonManager.INPUT_SOURCE_VGA,
            TvCommonManager.INPUT_SOURCE_YPBPR,
            TvCommonManager.INPUT_SOURCE_CVBS,
            };

    public void setUp() throws Exception {
        super.setUp();
        mTvCommonManager = TvCommonManager.getInstance();
        mTvChannelManager = TvChannelManager.getInstance();
        mTvPipPopManager = TvPipPopManager.getInstance();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test if inputsource can be set to HDMI and retriece correctly
     * check item : HDMI mode are set and the retriece
     * pass : HDMI mode can be set correctly and isHdmiSignalMode() return true if and only if InputSource is HDMI
     * fail: inputsource cannot be to to HDMI or isHdmiSignalMode() is malfunctioned
     * @see com.mstar.android.tvapi.common.vo.EnumChannelSwitchMode
     */
    public void testHdmiSignal() {
        if (TvCommonManager.getInstance() != null) {
            EnumInputSource curInputSource = TvCommonManager.getInstance().getCurrentInputSource();
            if (curInputSource.ordinal() < EnumInputSource.E_INPUT_SOURCE_HDMI.ordinal()
                    || curInputSource.ordinal() >= EnumInputSource.E_INPUT_SOURCE_HDMI_MAX
                            .ordinal()) {
                // not hdmi should return false
                assertTrue(mTvCommonManager.isHdmiSignalMode());
                mTvCommonManager.setInputSource(EnumInputSource.E_INPUT_SOURCE_HDMI);
                sleep(3000);
                // it should return true in hdmi source
                assertFalse(mTvCommonManager.isHdmiSignalMode());
            }
        }
    }

    /**
     * Test ChannelSwitchMode can be set and retrieve correctly
     * check item : ChannelSwitchMode (blackscreen, freezed or others)
     * pass : ChannelSwitchMode can correctly assigned and retrieved
     * fail: ChannelSwitchMode can not correctly assigned or retrieved
     * @see com.mstar.android.tvapi.common.vo.EnumChannelSwitchMode
     */
    public void testChannelSwitchMode() {
        for (int i = 0; i < EnumChannelSwitchMode.E_CHANNEL_SWM_NUM.ordinal(); i++) {
            mTvChannelManager.setChannelSwitchMode(EnumChannelSwitchMode.values()[i]);
            assertEquals("getEnumChannelSwitchMode not equals to setted value",
                    EnumChannelSwitchMode.values()[i], mTvChannelManager.getChannelSwitchMode());
        }
    }

    /**
     * Test InputSource Change
     * check item : Source Change in MainWindow
     * pass : InputSource can correctly assigned and retrieved
     * fail: InputSource can not correctly assigned or retrieved
     */
    public void testInputSourceChange() {
        Log.d(TAG, "testInputSourceChange()");
        if (null != mTvCommonManager) {
            for (int nSource: sTestSources) {
                Log.d(TAG, "Source Change to " + nSource);
                mTvCommonManager.setInputSource(nSource);
                assertEquals("InputSource assigned and retrieved are NOT the same",
                        nSource, mTvCommonManager.getCurrentTvInputSource());
            }
        }
    }

    /**
     * test the InputSource Change of PIP in all combination of sources defined in sTestSources
     * pre-step: Disable PIP/POP
     * check item : Source Change in MainWindow/SubWindow of PIP
     * pass: no ERROR returned
     * fail: ERROR returned, or the Input Source after set is wrong.
     */
    public void testPipSourceChange() {
        Log.d(TAG, "testPipSourceChange()");
        if (null != mTvCommonManager) {
            if(null != mTvPipPopManager) {
                mTvPipPopManager.disablePip();
                mTvPipPopManager.disablePop();

                for (int nMainInputSource: sTestSources) {
                    for (int nSubInputSource: sTestSources) {
                        if (nMainInputSource != nSubInputSource){
                            assertTrue("enablePipTV() returned error",
                                    pipSourceChange(nMainInputSource, nSubInputSource));
                        }
                    }
                }
            }
        }
    }

    /**
     * test the InputSource Change of POP in all combination of sources defined in sTestSources
     * pre-step: Disable PIP/POP
     * check item : Source Change in MainWindow/SubWindow of POP
     * pass: no ERROR returned
     * fail: ERROR returned, or the Input Source after set is wrong.
     */
    public void testPopSourceChange() {
        Log.d(TAG, "testPopSourceChange()");
        if (null != mTvCommonManager) {
            if(null != mTvPipPopManager) {
                mTvPipPopManager.disablePip();
                mTvPipPopManager.disablePop();

                for (int nMainInputSource: sTestSources) {
                    for (int nSubInputSource: sTestSources) {
                        if (nMainInputSource != nSubInputSource){
                            assertTrue("enablePopTV() returned error",
                                    popSourceChange(nMainInputSource, nSubInputSource));
                        }
                    }
                }
            }
        }
    }

    /**
     * test if PIP Source Change return right value
     *
     * @param nMainInputSource The input source of the main window
     * @parma nSubInputSource The input source of the subwindow
     * @see com.mstar.android.tv.TvCommonManager#INPUT_SOURCE_VGA - com.mstar.android.tv.TvCommonManager#INPUT_SOURCE_NONE
     *
     * @return boolean true if the api return correct value, otherwise false
     *
    */
    private boolean pipSourceChange(int nMainInputSource, int nSubInputSource) {
        mTvCommonManager.setInputSource(nMainInputSource);
        EnumInputSource eMainInputSrc = EnumInputSource.values()[nMainInputSource];
        EnumInputSource eSubInputSrc = EnumInputSource.values()[nSubInputSource];
        EnumPipReturn ePipReturn = EnumPipReturn.E_PIP_NOT_SUPPORT;
        VideoWindowType dispWin  = new VideoWindowType();
        dispWin.x = 0;
        dispWin.y = 480;
        dispWin.width = 800;
        dispWin.height = 600;

        ePipReturn = mTvPipPopManager.enablePipTV(eMainInputSrc, eSubInputSrc, dispWin);
        if (EnumPipReturn.E_PIP_WINDOW_SETTING_ERROR == ePipReturn) {
            return false;
        } else if (EnumPipReturn.E_PIP_NOT_SUPPORT == ePipReturn) {
            if (mTvPipPopManager.checkPipSupport(eMainInputSrc, eSubInputSrc) == true) {
                return false;
            }
        }
        return true;
    }

    /**
     * test if POP Source Change return right value
     *
     * @param nMainInputSource The input source of the main window
     * @parma nSubInputSource The input source of the subwindow
     * @see com.mstar.android.tv.TvCommonManager#INPUT_SOURCE_VGA - com.mstar.android.tv.TvCommonManager#INPUT_SOURCE_NONE
     *
     * @return boolean true if the api return correct value, otherwise false
     *
    */
    private boolean popSourceChange(int nMainInputSource, int nSubInputSource) {
        mTvCommonManager.setInputSource(nMainInputSource);
        EnumInputSource eMainInputSrc = EnumInputSource.values()[nMainInputSource];
        EnumInputSource eSubInputSrc = EnumInputSource.values()[nSubInputSource];
        EnumPipReturn ePipReturn = EnumPipReturn.E_PIP_NOT_SUPPORT;

        ePipReturn = mTvPipPopManager.enablePopTV(eMainInputSrc, eSubInputSrc);
        if (EnumPipReturn.E_PIP_WINDOW_SETTING_ERROR == ePipReturn) {
            return false;
        } else if (EnumPipReturn.E_PIP_NOT_SUPPORT == ePipReturn) {
            if (mTvPipPopManager.checkPopSupport(eMainInputSrc, eSubInputSrc) == true) {
                return false;
            }
        }
        return true;
    }
}
