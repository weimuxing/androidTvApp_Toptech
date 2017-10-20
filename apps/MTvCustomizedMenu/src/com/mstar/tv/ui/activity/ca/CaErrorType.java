//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2016 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.ui.activity.ca;

public class CaErrorType {
    public static enum CA_NOTIFY {
        ST_CA_MESSAGE_CANCEL_TYPE, ST_CA_MESSAGE_BADCARD_TYPE, ST_CA_MESSAGE_EXPICARD_TYPE, ST_CA_MESSAGE_INSERTCARD_TYPE, ST_CA_MESSAGE_NOOPER_TYPE, ST_CA_MESSAGE_BLACKOUT_TYPE, ST_CA_MESSAGE_OUTWORKTIME_TYPE, ST_CA_MESSAGE_WATCHLEVEL_TYPE, ST_CA_MESSAGE_PAIRING_TYPE, ST_CA_MESSAGE_NOENTITLE_TYPE, ST_CA_MESSAGE_DECRYPTFAIL_TYPE, ST_CA_MESSAGE_NOMONEY_TYPE, ST_CA_MESSAGE_ERRREGION_TYPE, ST_CA_MESSAGE_NEEDFEED_TYPE, ST_CA_MESSAGE_ERRCARD_TYPE, ST_CA_MESSAGE_UPDATE_TYPE, ST_CA_MESSAGE_LOWCARDVER_TYPE, ST_CA_MESSAGE_VIEWLOCK_TYPE, ST_CA_MESSAGE_MAXRESTART_TYPE, ST_CA_MESSAGE_FREEZE_TYPE, ST_CA_MESSAGE_CALLBACK_TYPE, ST_CA_MESSAGE_CURTAIN_TYPE, ST_CA_MESSAGE_CARDTESTSTART_TYPE, ST_CA_MESSAGE_CARDTESTFAILD_TYPE, ST_CA_MESSAGE_CARDTESTSUCC_TYPE, ST_CA_MESSAGE_NOCALIBOPER_TYPE, ST_CA_MESSAGE_STBLOCKED_TYPE, ST_CA_MESSAGE_STBFREEZE_TYPE,
    }

    public enum RETURN_CODE {
        ST_CA_RC_OK(0), ST_CA_RC_UNKNOWN(1), ST_CA_RC_POINTER_INVALID(2), ST_CA_RC_CARD_INVALID(3), ST_CA_RC_PIN_INVALID(
                4), ST_CA_RC_DATASPACE_SMALL(6), ST_CA_RC_CARD_PAIROTHER(7), ST_CA_RC_DATA_NOT_FIND(
                8), ST_CA_RC_PROG_STATUS_INVALID(9), ST_CA_RC_CARD_NO_ROOM(10), ST_CA_RC_WORKTIME_INVALID(
                11), ST_CA_RC_IPPV_CANNTDEL(12), ST_CA_RC_CARD_NOPAIR(13), ST_CA_RC_WATCHRATING_INVALID(
                14), ST_CA_RC_CARD_NOTSUPPORT(15), ST_CA_RC_DATA_ERROR(16), ST_CA_RC_FEEDTIME_NOT_ARRIVE(
                17), ST_CA_RC_CARD_TYPEERROR(18), ST_CA_RC_CAS_FAILED(32), ST_CA_RC_OPER_FAILED(33);

        private int retCode;

        private RETURN_CODE(int value) {
            this.retCode = value;
        }

        public int getRetCode() {
            return retCode;
        }
    }

    public enum CA_CARD_STATE {
        ST_CA_SC_OUT, ST_CA_SC_REMOVING, ST_CA_SC_INSERTING, ST_CA_SC_IN, ST_CA_SC_ERROR, ST_CA_SC_UPDATE, ST_CA_SC_UPDATE_ERR,
    }

}
