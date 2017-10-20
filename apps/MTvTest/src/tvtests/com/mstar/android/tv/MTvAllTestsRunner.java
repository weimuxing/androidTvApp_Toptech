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

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import android.util.Log;


import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;

public class MTvAllTestsRunner extends InstrumentationTestRunner {

    private static final String TAG = "MTvAllTestsRunner";

    protected static int sTvSystem = TvCommonManager.TV_SYSTEM_DVBT;

    private InstrumentationTestSuite mSuite = null;

    @Override
    public TestSuite getAllTests() {
        sTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mSuite = new InstrumentationTestSuite(this);
        switch (sTvSystem) {
            case TvCommonManager.TV_SYSTEM_ATSC:
                Log.d(TAG, "add atsc system test suite");
                mSuite.addTestSuite(AtscChannelTest.class);
                break;
            case TvCommonManager.TV_SYSTEM_ISDB:
                Log.d(TAG, "add isdb system test suite");
                mSuite.addTestSuite(IsdbChannelTest.class);
                break;
            case TvCommonManager.TV_SYSTEM_DVBT:
            case TvCommonManager.TV_SYSTEM_DVBT2:
            case TvCommonManager.TV_SYSTEM_DVBS:
            case TvCommonManager.TV_SYSTEM_DVBS2:
            case TvCommonManager.TV_SYSTEM_DVBC:
            case TvCommonManager.TV_SYSTEM_DTMB:
                Log.d(TAG, "add dvb system test suite");
                addChannelTestSuiteByDvbRoute();
                break;
        }
        // mSuite.addTestSuite(PictureTest.class);
        // mSuite.addTestSuite(TimerTest.class);
        // mSuite.addTestSuite(TvMiscTest.class);
        return mSuite;
    }

    @Override
    public ClassLoader getLoader() {
        return MTvAllTestsRunner.class.getClassLoader();
    }

    private void addChannelTestSuiteByDvbRoute() {
        TvTypeInfo tvinfo = TvCommonManager.getInstance().getTvInfo();
        for (int i = 0; i < tvinfo.routePath.length; i++) {
            if (TvChannelManager.TV_ROUTE_NONE != tvinfo.routePath[i]) {
                switch (tvinfo.routePath[i]) {
                    case TvChannelManager.TV_ROUTE_DVBT:
                    case TvChannelManager.TV_ROUTE_DVBT2:
                        Log.d(TAG, "add dvbt system test suite");
                        // mSuite.addTestSuite(DvbtChannelTest.class);
                        break;
                    case TvChannelManager.TV_ROUTE_DVBS:
                    case TvChannelManager.TV_ROUTE_DVBS2:
                        Log.d(TAG, "add dvbs system test suite");
                        // mSuite.addTestSuite(DvbsChannelTest.class);
                        break;
                    case TvChannelManager.TV_ROUTE_DVBC:
                        Log.d(TAG, "add dvbc system test suite");
                        // mSuite.addTestSuite(DvbcChannelTest.class);
                        break;
                    case TvChannelManager.TV_ROUTE_DTMB:
                        Log.d(TAG, "add dtmb system test suite");
                        mSuite.addTestSuite(DtmbChannelTest.class);
                        break;
                }
            }
        }
    }
}
