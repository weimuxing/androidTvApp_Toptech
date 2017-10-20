//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.vo.EnumVideoItem;
import com.mstar.android.tvapi.common.vo.EnumPictureMode;
import com.mstar.android.tvapi.common.vo.EnumColorTemperature;
import com.mstar.android.tvapi.common.vo.EnumVideoArcType;
import com.mstar.android.tvapi.common.vo.NoiseReduction.EnumNoiseReduction;
import com.mstar.android.tvapi.common.vo.MpegNoiseReduction.EnumMpegNoiseReduction;
import com.mstar.android.tvapi.common.vo.MweType.EnumMweType;
import com.mstar.android.tvapi.common.vo.Film.EnumFilm;

public class PictureTest extends TestBase {

    private static final String TAG = "PictureTest";

    private TvPictureManager mTvPictureManager;

    public void setUp() throws Exception {
        super.setUp();
        mTvPictureManager = TvPictureManager.getInstance();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDemoMode() {
         mTvPictureManager.setDemoMode(EnumMweType.E_OFF);
         assertEquals(EnumMweType.E_OFF, mTvPictureManager.getDemoMode());
    }

    public void testDlcStatus() {
        mTvPictureManager.disableDlc();
        assertFalse(mTvPictureManager.isDlcEnabled());
        mTvPictureManager.enableDlc();
        assertTrue(mTvPictureManager.isDlcEnabled());
    }

    public void testDbcStatus() {
        mTvPictureManager.disableDbc();
        assertFalse(mTvPictureManager.isDbcEnabled());
        mTvPictureManager.enableDbc();
        assertTrue(mTvPictureManager.isDbcEnabled());
    }

    public void testDccStatus() {
        mTvPictureManager.disableDcc();
        assertFalse(mTvPictureManager.isDccEnabled());
        mTvPictureManager.enableDcc();
        assertTrue(mTvPictureManager.isDccEnabled());
    }

    public void testNRStatus() {
        for (int i = 0; i < EnumNoiseReduction.E_NR_NUM.getValue(); i++) {
            mTvPictureManager.setNR(EnumNoiseReduction.values()[i]);
            assertEquals(EnumNoiseReduction.values()[i].getValue(), mTvPictureManager.getNR()
                    .getValue());
        }
    }

    public void testMpegNRStatus() {
        for (int i = 0; i < EnumMpegNoiseReduction.E_MPEG_NR_OFF.getValue(); i++) {
             mTvPictureManager.setMpegNR(EnumMpegNoiseReduction.values()[i]);
             assertEquals(EnumMpegNoiseReduction.values()[i].getValue(),
             mTvPictureManager.getMpegNR().getValue());
        }
    }

    public void testVideoItem_sequential() {
        EnumVideoItem eVitem = EnumVideoItem.MS_VIDEOITEM_BRIGHTNESS;
        for (int i = eVitem.ordinal(); i < EnumVideoItem.MS_VIDEOITEM_NUM.ordinal(); i++) {
            mTvPictureManager.setVideoItem(EnumVideoItem.values()[i], i * 10);
            assertEquals(i * 10, mTvPictureManager.getVideoItem(EnumVideoItem.values()[i]));
        }
    }

    public void testVideoItem_getAfterSetAll() {
        EnumVideoItem eVitem = EnumVideoItem.MS_VIDEOITEM_BRIGHTNESS;
        for (int i = eVitem.ordinal(); i < EnumVideoItem.MS_VIDEOITEM_NUM.ordinal(); i++) {
            mTvPictureManager.setVideoItem(EnumVideoItem.values()[i], i * 10);
        }
        for (int j = eVitem.ordinal(); j < EnumVideoItem.MS_VIDEOITEM_NUM.ordinal(); j++) {
            assertEquals(j * 10, mTvPictureManager.getVideoItem(EnumVideoItem.values()[j]));
        }
    }

    public void testPictureMode_sequential() {
        EnumPictureMode ePitem = EnumPictureMode.PICTURE_DYNAMIC;
        for (int i = ePitem.ordinal(); i < EnumPictureMode.PICTURE_NUMS.ordinal(); i++) {
            assertTrue(mTvPictureManager.setPictureModeIdx(EnumPictureMode.values()[i]));
            assertEquals(EnumPictureMode.values()[i], mTvPictureManager.getPictureModeIdx());
        }
    }

    public void testPictureMode_getAfterSetAll() {
        EnumPictureMode ePitem = EnumPictureMode.PICTURE_DYNAMIC;
        int i;
        for (i = ePitem.ordinal(); i < EnumPictureMode.PICTURE_NUMS.ordinal(); i++) {
            mTvPictureManager.setPictureModeIdx(EnumPictureMode.values()[i]);
        }
        for (int j = ePitem.ordinal(); j < EnumPictureMode.values().length; j++) {
            assertEquals(EnumPictureMode.values()[i - 1], mTvPictureManager.getPictureModeIdx());
        }
    }

    public void testBacklight_setValue() {
        for (int i = 0; i <= 50; i++) {
            assertTrue(mTvPictureManager.setBacklight(i));
            assertEquals(i, mTvPictureManager.getBacklight());
        }

        int j;
        for (j = 51; j <= 100; j++) {
            assertTrue(mTvPictureManager.setBacklight(j));
        }
        assertEquals(j - 1, mTvPictureManager.getBacklight());
    }

    public void testBacklight_onOff() {
        // stress test backlight on/off
        for (int i = 0; i <= 100; i++) {
            mTvPictureManager.disableBacklight();
            mTvPictureManager.enableBacklight();
        }
    }

    public void testColorTemp_sequential() {
        for (int i = EnumColorTemperature.E_COLOR_TEMP_MIN.ordinal(); i < EnumColorTemperature.E_COLOR_TEMP_NUM
                .ordinal(); i++) {
            assertTrue(mTvPictureManager.setColorTempIdx((EnumColorTemperature.values()[i])));
            assertEquals(EnumColorTemperature.values()[i].getValue(), mTvPictureManager
                    .getColorTempIdx().getValue());
        }
    }

    public void testColorTemp_getAfterSetAll() {
        for (int i = EnumColorTemperature.E_COLOR_TEMP_MIN.ordinal(); i < EnumColorTemperature.E_COLOR_TEMP_NUM
                .ordinal(); i++) {
            assertTrue(mTvPictureManager.setColorTempIdx((EnumColorTemperature.values()[i])));
        }
        assertEquals(EnumColorTemperature.E_COLOR_TEMP_MAX.getValue(), mTvPictureManager
                .getColorTempIdx().getValue());
    }

    public void testVideoArc_sequential() {
        int preSuccessRatio = EnumVideoArcType.E_DEFAULT.ordinal();
        for (int i = EnumVideoArcType.E_DEFAULT.ordinal(); i < EnumVideoArcType.E_MAX.ordinal(); i++) {
            boolean ret = mTvPictureManager.setVideoArc(EnumVideoArcType.values()[i]);
            if (ret) {
                assertEquals(EnumVideoArcType.values()[i], mTvPictureManager.getVideoArc());
                preSuccessRatio = i;
            } else {
                assertEquals(EnumVideoArcType.values()[preSuccessRatio],
                        mTvPictureManager.getVideoArc());
            }
        }
    }

    public void testVideoArc_getAfterSetAll() {
        int maxRatio = EnumVideoArcType.E_MAX.ordinal() - 1;
        int preSuccessRatio = EnumVideoArcType.E_DEFAULT.ordinal();
        for (int i = EnumVideoArcType.E_DEFAULT.ordinal(); i < maxRatio; i++) {
            if (mTvPictureManager.setVideoArc(EnumVideoArcType.values()[i])) {
                preSuccessRatio = i;
            }
        }
        assertEquals(EnumVideoArcType.values()[preSuccessRatio], mTvPictureManager.getVideoArc());
    }

    public void testPCHPosition() {
        for (int i = 0; i <= 500; i++) {
            assertTrue(mTvPictureManager.setPCHPos(i));
            assertEquals(i, mTvPictureManager.getPCHPos());
        }
    }

    public void testPCVPosition() {
        for (int i = 0; i <= 500; i++) {
            assertTrue(mTvPictureManager.setPCVPos(i));
            assertEquals(i, mTvPictureManager.getPCVPos());
        }
    }

    public void testPCClock() {
        for (int i = 0; i <= 500; i++) {
            assertTrue(mTvPictureManager.setPCClock(i));
            assertEquals(i, mTvPictureManager.getPCClock());
        }
    }

    public void testPCPhase() {
        for (int i = 0; i <= 500; i++) {
            assertTrue(mTvPictureManager.setPCPhase(i));
            assertEquals(i, mTvPictureManager.getPCPhase());
        }
    }

    public void testFilmMode() {
        Log.d(TAG, "testing setFilmMode");
        EnumFilm eFilm = EnumFilm.E_OFF;
        // FIXME: waitting mantis 0478674 done.
        assertTrue("setFilmMode failed", mTvPictureManager.setFilmMode(eFilm));
        assertEquals("getFilmMode not equals to setted value", eFilm,
                mTvPictureManager.getFilmMode());
    }
}
