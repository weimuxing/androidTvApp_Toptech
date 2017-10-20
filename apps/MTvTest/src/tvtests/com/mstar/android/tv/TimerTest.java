
package tvtests.com.mstar.android.tv;

import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumTimeZone;

public class TimerTest extends TestBase {

    private static final String TAG = "TvTimerTest";

    private TvTimerManager mTvTimerManager;

    public void setUp() throws Exception {
        super.setUp();
        mTvTimerManager = TvTimerManager.getInstance();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTimezoneUsage() {
        EnumTimeZone timezone = EnumTimeZone.E_TIMEZONE_GMT_0_START;
        mTvTimerManager.setTimeZone(timezone, true);
        // get timezone back to check
        EnumTimeZone timezone_check = mTvTimerManager.getTimeZone();
        assertEquals(timezone.getValue(), timezone_check.getValue());
    }
}
