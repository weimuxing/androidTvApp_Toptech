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

package com.mstar.miscsetting.activity.display.util;

import android.util.Log;
import android.view.View;

import com.mstar.miscsetting.activity.display.vo.MDisplayConstants;
import com.mstar.miscsetting.activity.display.vo.ReproduceRate;
import com.mstar.miscsetting.activity.display.vo.ResolutionVO;
import com.mstar.miscsetting.activity.display.PictureSkin;

import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tvapi.common.PipManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumPipMode;
import com.mstar.android.tvapi.common.vo.EnumPipModes;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import android.os.SystemProperties;

public class TvDispAreaUtil {
    private static final String TAG = "TvDispAreaUtil";

    private PictureSkin mPictureSkin;

    private TvPictureManager mTvPictureManager;

    private ReproduceRate mReproduceRate = null;

    private static final int REPRODUCE_ADJUST_NONE = 0;

    private static final int REPRODUCE_ADJUST_TOP = 1;

    private static final int REPRODUCE_ADJUST_BOTTOM = 2;

    private static final int REPRODUCE_ADJUST_LEFT = 3;

    private static final int REPRODUCE_ADJUST_RIGHT = 4;

    private static final int REPRODUCE_ADJUST_ALL = 5;

    public TvDispAreaUtil() {
        mPictureSkin = PictureSkin.getInstance();
        mTvPictureManager = TvPictureManager.getInstance();
        mReproduceRate = new ReproduceRate(mTvPictureManager.GetReproduce());
    }

    public ReproduceRate getReproduceRate() {
        if (mReproduceRate == null) {
            mReproduceRate = new ReproduceRate(mTvPictureManager.GetReproduce());
        }
        return mReproduceRate;
    }

    private ResolutionVO getResolutionInfo() {
        int resolution = mTvPictureManager.GetResloution();

        Log.i(TAG, "resolution = " + resolution);

        if (resolution == MDisplayConstants.E_MAPI_RES_1920x1080p_50Hz) {
            return new ResolutionVO(1920, 1080, 192, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_1920x1080p_60Hz) {
            return new ResolutionVO(1920, 1080, 192, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_1920x1080i_50Hz) {
            return new ResolutionVO(1920, 1080, 192, 1);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_1920x1080i_60Hz) {
            return new ResolutionVO(1920, 1080, 192, 1);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_4K2Kp_30Hz) {
            return new ResolutionVO(3840, 2160, 343, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_4K2Kp_25Hz) {
            return new ResolutionVO(3840, 2160, 128, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_1280x720p_50Hz) {
            return new ResolutionVO(1280, 720, 260, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_1280x720p_60Hz) {
            return new ResolutionVO(1280, 720, 260, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_720x576p) {
            return new ResolutionVO(720, 576, 132, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_720x576i) {
            return new ResolutionVO(720, 576, 132, 1);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_720x480p) {
            return new ResolutionVO(720, 480, 122, 0);
        } else if (resolution == MDisplayConstants.E_MAPI_RES_720x480i) {
            return new ResolutionVO(720, 480, 122, 1);
        } else {
            return new ResolutionVO(1920, 1080, 192, 0);
        }
    }

    public void setReproduceRateTop(int value) {
        mReproduceRate._reduceRate.topRate = value;
        setReproduceRate(mReproduceRate, REPRODUCE_ADJUST_TOP);
    }

    public void setReproduceRateBottom(int value) {
        mReproduceRate._reduceRate.bottomRate = value;
        setReproduceRate(mReproduceRate, REPRODUCE_ADJUST_BOTTOM);
    }

    public void setReproduceRateLeft(int value) {
        mReproduceRate._reduceRate.leftRate = value;
        setReproduceRate(mReproduceRate, REPRODUCE_ADJUST_LEFT);
    }

    public void setReproduceRateRight(int value) {
        mReproduceRate._reduceRate.rightRate = value;
        setReproduceRate(mReproduceRate, REPRODUCE_ADJUST_RIGHT);
    }

    private void setReproduceRate(ReproduceRate mReproduceRate, int direction) {
        ResolutionVO resolutionVo = getResolutionInfo();
        int scaleval = (mReproduceRate._reduceRate.topRate << 24)
                + (mReproduceRate._reduceRate.bottomRate << 16)
                + (mReproduceRate._reduceRate.leftRate << 8)
                + (mReproduceRate._reduceRate.rightRate);

        Log.d(TAG, "resolutionVo.getWidth(), resolutionVo.getHeight() : "
                + resolutionVo.getWidth() + ", " + resolutionVo.getHeight());
        Log.d(TAG,
                "resolutionVo.getHstart(), mReproduceRate._reduceRate.interleave : "
                        + resolutionVo.getHstart() + ", "
                        + mReproduceRate._reduceRate.interleave);
        Log.d(TAG, "direction, scaleval : " + direction + ", " + scaleval);

        mPictureSkin.Connect();
        mPictureSkin.setSurfaceResolutionMode(resolutionVo.getWidth(),
                resolutionVo.getHeight(), resolutionVo.getHstart(),
                mReproduceRate._reduceRate.interleave, direction, scaleval);
        mTvPictureManager.SetReproduce(scaleval);
    }

    public void setReproduceRateDefault() {
        mReproduceRate._reduceRate.topRate = 0;
        mReproduceRate._reduceRate.bottomRate = 0;
        mReproduceRate._reduceRate.leftRate = 0;
        mReproduceRate._reduceRate.rightRate = 0;
        setReproduceRate(mReproduceRate, REPRODUCE_ADJUST_ALL);
    }
}
