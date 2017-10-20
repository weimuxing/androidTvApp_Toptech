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

import tvtests.com.mstar.android.tv.TestBase;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tvapi.common.vo.EnumSoundMode;
import com.mstar.android.tvapi.common.vo.EnumSpdifType;
import com.mstar.android.tvapi.common.vo.EnumSurroundMode;

public class AudioTest extends TestBase {
    private static final String TAG = "AudioTest";

    private TvAudioManager mTvAudioManager;

    public void setUp() throws Exception {
        super.setUp();
        mTvAudioManager = TvAudioManager.getInstance();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    private void validateSoundMode(EnumSoundMode expect) {
        Log.d(TAG, "SoundMode set to " + expect.name());
        assertTrue("setSoundMode failed", mTvAudioManager.setSoundMode(expect));
        assertEquals(expect, mTvAudioManager.getSoundMode());

        Log.d(TAG, " LOW  PITCH : " + mTvAudioManager.getBass());
        Log.d(TAG, " HIGH PITCH : " + mTvAudioManager.getTreble());
        Log.d(TAG, " BALANCE : " + mTvAudioManager.getBalance());

        Log.d(TAG, " AVC      MODE : " + mTvAudioManager.getAvcMode());
        Log.d(TAG, " SURROUND MODE : " + mTvAudioManager.getSurroundMode());
        Log.d(TAG, " SPDF     MODE : " + mTvAudioManager.getSpdifOutMode());
    }

    public void testSoundMode() {
        EnumSoundMode soundModeOriginal = mTvAudioManager.getSoundMode();
        Log.d(TAG, "Getting original Sound Mode :" + soundModeOriginal.toString());
        mTvAudioManager.setSoundMode(EnumSoundMode.E_STANDARD);
        validateSoundMode(EnumSoundMode.E_STANDARD);
        validateSoundMode(EnumSoundMode.E_MOVIE);
        validateSoundMode(EnumSoundMode.E_MUSIC);
        validateSoundMode(EnumSoundMode.E_SPORTS);
        validateSoundMode(EnumSoundMode.E_ONSITE1);
        validateSoundMode(EnumSoundMode.E_ONSITE2);
        validateSoundMode(EnumSoundMode.E_USER);
    }

    public void testEqBand120() {
        Log.d(TAG, "Testing testEqBand");
        int rval = mTvAudioManager.getEqBand120();
        Log.d(TAG, "getEqBand120 : " + rval);
        assertTrue("getEqBand120 failed", rval >= 0);
        assertTrue("SetEqBand120 failed", mTvAudioManager.setEqBand120(50));
        assertEquals(50, mTvAudioManager.getEqBand120());
    }

    public void testEqBand500() {
        Log.d(TAG, "Testing testEqBand");
        int rval = mTvAudioManager.getEqBand500();
        Log.d(TAG, "getEqBand500 : " + rval);
        assertTrue("getEqBand500 failed", rval >= 0);
        assertTrue("SsetEqBand500 failed", mTvAudioManager.setEqBand500(50));
        assertEquals(50, mTvAudioManager.getEqBand500());
    }

    public void testEqBand1500() {
        Log.d(TAG, "Testing testEqBand");
        int rval = mTvAudioManager.getEqBand1500();
        Log.d(TAG, "getEqBand1500 : " + rval);
        assertTrue("getEqBand1500 failed", rval >= 0);
        assertTrue("SsetEqBand1500 failed", mTvAudioManager.setEqBand1500(50));
        assertEquals(50, mTvAudioManager.getEqBand1500());
    }

    public void testEqBand5k() {
        Log.d(TAG, "Testing testEqBand");
        int rval = mTvAudioManager.getEqBand5k();
        Log.d(TAG, "getEqBand5k : " + rval);
        assertTrue("getEqBand5k failed", rval >= 0);
        assertTrue("SsetEqBand5k failed", mTvAudioManager.setEqBand5k(40));
        assertEquals(40, mTvAudioManager.getEqBand5k());
    }

    public void testEqBand10k() {
        Log.d(TAG, "Testing testEqBand");
        int rval = mTvAudioManager.getEqBand10k();
        Log.d(TAG, "getEqBand10k : " + rval);
        assertTrue("getEqBand10k failed", rval >= 0);
        assertTrue("SetEqBand10k failed", mTvAudioManager.setEqBand10k(40));
        assertEquals(40, mTvAudioManager.getEqBand10k());
    }

    public void testBalance() {
        Log.d(TAG, "testing getBalance and SetBalance");
        int rval = mTvAudioManager.getBalance();
        assertTrue("getBalance", rval > 0);
        assertTrue("setBalance failed", mTvAudioManager.setBalance(30));
        assertEquals(30, mTvAudioManager.getBalance());
    }

    public void testSurroundMode() {
        Log.d(TAG, "testing SurroundMode");
        EnumSurroundMode retv = mTvAudioManager.getSurroundMode();
        assertNotNull("getSurrountMode failed", retv);
        assertTrue("setSurround mode on failed",
                mTvAudioManager.setSurroundMode(EnumSurroundMode.E_SURROUND_MODE_ON));
        // after setSurroundMode wait > 300ms
        sleep(1000);
        assertEquals(EnumSurroundMode.E_SURROUND_MODE_ON, mTvAudioManager.getSurroundMode());
    }

    private void validateSpdifOutMode(EnumSpdifType expect) {
        assertTrue(TAG, mTvAudioManager.setSpdifOutMode(expect));
        EnumSpdifType rval = mTvAudioManager.getSpdifOutMode();
        Log.d(TAG, "getSpdifOutMode : " + rval.name());
        assertEquals("SpdifType set to " + expect.name(), expect, rval);
    }

    public void testSpdifOutModeOFF() {
        Log.d(TAG, "testing SpdifOutMode");
        EnumSpdifType rval = mTvAudioManager.getSpdifOutMode();
        assertNotNull("getSpdifOutMode returns null", rval);
        Log.d(TAG, "getSpdifOutMode : " + rval.name());

        validateSpdifOutMode(EnumSpdifType.E_OFF);
    }

    public void testSpdifOutModePCM() {
        Log.d(TAG, "testing SpdifOutMode");
        EnumSpdifType rval = mTvAudioManager.getSpdifOutMode();
        assertNotNull("getSpdifOutMode returns null", rval);
        Log.d(TAG, "getSpdifOutMode : " + rval.name());

        validateSpdifOutMode(EnumSpdifType.E_PCM);
    }

    public void testSpdifOutModeNONPCM() {
        Log.d(TAG, "testing SpdifOutMode");
        EnumSpdifType rval = mTvAudioManager.getSpdifOutMode();
        assertNotNull("getSpdifOutMode returns null", rval);
        Log.d(TAG, "getSpdifOutMode : " + rval.name());

        validateSpdifOutMode(EnumSpdifType.E_NONPCM);
    }

    public void testEarPhoneVolume() {
        Log.d(TAG, "testing Ear Phone Volume");
        int inVolume = mTvAudioManager.getEarPhoneVolume();
        int testVolume = 100;
        boolean bRet = mTvAudioManager.setEarPhoneVolume(testVolume);
        assertTrue("set Ear Phone volume 100 failure", bRet);
        assertEquals(testVolume, mTvAudioManager.getEarPhoneVolume());

        testVolume = 50;
        bRet = mTvAudioManager.setEarPhoneVolume(testVolume);
        assertTrue("set Ear Phone volume 50 failure", bRet);
        assertEquals(testVolume, mTvAudioManager.getEarPhoneVolume());

        testVolume = 0;
        bRet = mTvAudioManager.setEarPhoneVolume(testVolume);
        assertTrue("set Ear Phone volume 0 failure", bRet);
        assertEquals(testVolume, mTvAudioManager.getEarPhoneVolume());

        testVolume = inVolume;
        bRet = mTvAudioManager.setEarPhoneVolume(testVolume);
        assertTrue("set Ear Phone volume 100 failure", bRet);
        assertEquals(testVolume, mTvAudioManager.getEarPhoneVolume());
    }

    public void testBass() {
        Log.d(TAG, "testing Bass");
        int orgBassSetting = mTvAudioManager.getBass();
        int testBass = 100;
        boolean bRet = mTvAudioManager.setBass(testBass);
        assertTrue("set Bass 100 failure", bRet);
        assertEquals(testBass, mTvAudioManager.getBass());

        testBass = 50;
        bRet = mTvAudioManager.setBass(testBass);
        assertTrue("set Bass 50 failure", bRet);
        assertEquals(testBass, mTvAudioManager.getBass());

        testBass = 0;
        bRet = mTvAudioManager.setBass(testBass);
        assertTrue("set Bass 0 failure", bRet);
        assertEquals(testBass, mTvAudioManager.getBass());

        testBass = orgBassSetting;
        bRet = mTvAudioManager.setBass(testBass);
        assertTrue("set Bass to original setting failure", bRet);
        assertEquals(testBass, mTvAudioManager.getBass());
    }

    public void testTreble() {
        Log.d(TAG, "testing Treble");
        int orgTrebleSetting = mTvAudioManager.getTreble();
        int testTreble = 100;
        boolean bRet = mTvAudioManager.setTreble(testTreble);
        assertTrue("set Treble 100 failure", bRet);
        assertEquals(testTreble, mTvAudioManager.getTreble());

        testTreble = 50;
        bRet = mTvAudioManager.setTreble(testTreble);
        assertTrue("set Treble 50 failure", bRet);
        assertEquals(testTreble, mTvAudioManager.getTreble());

        testTreble = 0;
        bRet = mTvAudioManager.setTreble(testTreble);
        assertTrue("set Treble 0 failure", bRet);
        assertEquals(testTreble, mTvAudioManager.getTreble());

        testTreble = orgTrebleSetting;
        bRet = mTvAudioManager.setTreble(testTreble);
        assertTrue("set Treble to original setting failure", bRet);
        assertEquals(testTreble, mTvAudioManager.getTreble());
    }

    public void testAvcMode() {
        Log.d(TAG, "testing AVC Mode");
        boolean orgAvcSetting = mTvAudioManager.getAvcMode();
        boolean bRet = false;

        bRet = mTvAudioManager.setAvcMode(false);
        assertTrue("set AVC false failure", bRet);
        assertEquals(false, mTvAudioManager.getAvcMode());

        bRet = mTvAudioManager.setAvcMode(true);
        assertTrue("set AVC true failure", bRet);
        assertEquals(true, mTvAudioManager.getAvcMode());

        bRet = mTvAudioManager.setAvcMode(orgAvcSetting);
        assertTrue("set AVC orignal setting failure", bRet);
        assertEquals(orgAvcSetting, mTvAudioManager.getAvcMode());
    }
}
