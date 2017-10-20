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

package mstar.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemProperties;

import com.mstar.android.tv.TvCommonManager;

public class Tools {
    public static void toastShow(int resId, Context context) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean isBox() {
        return SystemProperties.getBoolean("mstar.build.for.stb", false);
    }

    public static String getHdmiDispStr(int inputSource) {
        final int FUNCTION_DISABLED = 0;
        int[] sourceList = TvCommonManager.getInstance().getSourceList();
        int hdmiPortNum = 0;
        int typeCPortNum = 0;
        String srcName = "";
        boolean isTypeC = false;
        if ((TvCommonManager.INPUT_SOURCE_HDMI <= inputSource)
            && (TvCommonManager.INPUT_SOURCE_HDMI4 >= inputSource)) {
            for (int i = TvCommonManager.INPUT_SOURCE_HDMI; i < TvCommonManager.INPUT_SOURCE_HDMI_MAX; i++) {
                if (FUNCTION_DISABLED != sourceList[i]) {
                    hdmiPortNum++;
                }
            }
            boolean[] hdmiPortStatus = TvCommonManager.getInstance().getHdmiTypeCPort();
            if (null != hdmiPortStatus) {
                for (int i = 0; i < hdmiPortStatus.length; i++) {
                    if (true == hdmiPortStatus[i]) {
                        typeCPortNum++;
                    }
                }
            }
            hdmiPortNum = hdmiPortNum - typeCPortNum;
            final int idx = inputSource - TvCommonManager.INPUT_SOURCE_HDMI;
            if ((null != hdmiPortStatus) && (hdmiPortStatus[idx])) {
                srcName = "TYPE-C";
                if (1 < typeCPortNum) {
                    int count = 0;
                    for (int i = 0; i < hdmiPortStatus.length; i++) {
                        if (true == hdmiPortStatus[i]) {
                            count++;
                        }
                        if (idx == i) {
                            break;
                        }
                    }
                    srcName = "TYPE-C " + count;
                }
                isTypeC = true;
            }
            if (false == isTypeC) {
                srcName = "HDMI";
                if (1 < hdmiPortNum) {
                    int count = 0;
                    for (int i = TvCommonManager.INPUT_SOURCE_HDMI; i < TvCommonManager.INPUT_SOURCE_HDMI_MAX; i++) {
                        if (FUNCTION_DISABLED != sourceList[i]) {
                            if (true == hdmiPortStatus[i - TvCommonManager.INPUT_SOURCE_HDMI]) {
                                continue;
                            }
                            count++;
                            if (inputSource == i) {
                                break;
                            }
                        }
                    }
                    srcName = "HDMI" + count;
                }
            }
        }
        /*
         * HW: HDMI, HDMI, HDMI, HDMI
         * Displaying: HDMI1, HDMI2, HDMI3, HDMI4
         *
         * HW: HDMI, TYPE C, HDMI, HDMI
         * Displaying HDMI1, TYPE-C, HDMI2, HDMI3
         *
         * HW: HDMI, TYPE C, HDMI, TYPE C
         * Displaying: HDMI1, TYPE-C 1, HDMI2, TYPE-C 2
         *
         * HW: TYPE C, HDMI, HDMI, HDMI
         * Displaying: TYPE-C, HDMI1, HDMI2, HDMI3
         */
        return srcName;
    }
}
