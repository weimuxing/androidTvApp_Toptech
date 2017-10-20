//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.ui.sidepanel.fragment.threeD;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.dialog.SliderDialogFragment;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoLrViewSwitch;
import com.mstar.android.tvapi.common.vo.VideoInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Shows version and optional license information.
 */
public class Menu3DFragment extends SideFragment {
    private final String TAG = "3DFragment";

    private static final int FRAME_PACKING_720P_VSIZE = 1470; // temp enum, wait
                                                              // add enum in
                                                              // VideoInfo

    private static final int SELF_DETECT_3DTO2D_RIGHT_NOW = 1; // temp enum

    private static final int ID_SELF_DETECT = 0;

    private static final int ID_3D_TO_2D_SELF_DETECT = 1;

    private static final int ID_CONVERSION = 2;

    private static final int ID_3D_TO_2D = 3;

    private static final int ID_DEPTH = 4;

    private static final int ID_OFFSET = 5;

    private static final int ID_OUTPUT_ASPECT = 6;

    private static final int ID_LR_SWITCH = 7;

    private static Handler mHandler = new Handler();

    // String arrays
    private String mStrSelfDetect[];

    private String mStr3Dto2DSelfDetect[];

    private String mStr3DConversion[];

    private String mStr3Dto2D[];

    private String mStr3DOutputAspect[];

    private boolean mIsItemShow[];

    private SelfAdaptiveDetectItem mSelfAdaptiveDetectItem;

    private Menu3Dto2DSelfAdaptiveDetectItem mMenu3Dto2DSelfAdaptiveDetectItem;

    private Menu3DCconversion mMenu3DCconversion;

    private Menu3Dto2D mMenu3Dto2D;

    private Menu3DDdepth mMenu3DDdepth;

    private Menu3DOffset mMenu3DOffset;

    private Menu3DOutputAspect mMenu3DOutputAspect;

    private Menu3DLRSwitch mMenu3DLRSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrSelfDetect = getActivity().getResources().getStringArray(
                R.array.str_arr_3d_self_adaptive_detecttriple_vals);
        mStr3Dto2DSelfDetect = getActivity().getResources().getStringArray(
                R.array.str_arr_3dto2d_self_adaptive_detecttriple_vals);
        mStr3DConversion = getActivity().getResources().getStringArray(
                R.array.str_arr_3d_3dconversion_vals);
        mStr3Dto2D = getActivity().getResources().getStringArray(R.array.str_arr_3d_3dto2d_vals);
        mStr3DOutputAspect = getActivity().getResources().getStringArray(
                R.array.str_arr_3d_3doutputaspect_vals);
        mIsItemShow = new boolean[8];
    }

    private void update2dMode() {
        mMenu3DDdepth.setEnabled(false);
        mMenu3DOffset.setEnabled(false);
        mMenu3DLRSwitch.setEnabled(false);
    }

    private void update3dMode(boolean is2dTo3D) {
        mMenu3DDdepth.setEnabled(true);
        mMenu3DOffset.setEnabled(false);
        mMenu3DLRSwitch.setEnabled(true);
    }

    private void updateSelfDetectBtn() {
        if (TvChannelManager.getInstance().isSignalStabled()) {
            // comboBtn3DSelfAdaptiveDetect.setEnable(true);
            mIsItemShow[ID_SELF_DETECT] = true;
        } else {
            // comboBtn3DSelfAdaptiveDetect.setEnable(false);
            mIsItemShow[ID_SELF_DETECT] = false;
        }
    }

    private void checkMWE() {
        int mode = TvS3DManager.getInstance().get3dDisplayFormat();
        if (TvPictureManager.getInstance().getMWEDemoMode() != TvPictureManager.MWE_DEMO_MODE_OFF) {
            TvPictureManager.getInstance().setMWEDemoMode(TvPictureManager.MWE_DEMO_MODE_OFF);
            Toast toast = Toast.makeText(getActivity(), R.string.str_3d_toast, 5);
            toast.show();
        }
    }

    private void updateUI() {
        Arrays.fill(mIsItemShow, Boolean.TRUE);
        mIsItemShow[ID_OUTPUT_ASPECT] = false;

        final int selfDetect = TvS3DManager.getInstance().getSelfAdaptiveDetectMode();
        final int displayFormat3d = TvS3DManager.getInstance().get3dDisplayFormat();
        final int displayFormat3dTo2d = TvS3DManager.getInstance().get3DTo2DDisplayMode();
        final boolean is2dTo3d = (TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D == displayFormat3d) ? true
                : false;
        final int current3dType = TvS3DManager.getInstance().getCurrent3dType();
        final VideoInfo videoInfo = TvPictureManager.getInstance().getVideoInfo();
        Log.d(TAG, "selfDetect: " + selfDetect);
        Log.d(TAG, "displayFormat3d: " + displayFormat3d);
        Log.d(TAG, "displayFormat3dTo2d: " + displayFormat3dTo2d);
        Log.d(TAG, "is2dTo3d: " + is2dTo3d);
        Log.d(TAG, "current3dType: " + current3dType);
        Log.d(TAG, "vResolution:" + videoInfo.vResolution);

        if (TvPictureManager.getInstance().getMWEDemoMode() == TvPictureManager.MWE_DEMO_MODE_OFF) {
            if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE
                    && current3dType == TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P
                    && videoInfo.vResolution == FRAME_PACKING_720P_VSIZE) {
                mIsItemShow[ID_SELF_DETECT] = false;
                mIsItemShow[ID_CONVERSION] = false;
            } else if (selfDetect != TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_OFF) {
                updateSelfDetectBtn();
                mIsItemShow[ID_3D_TO_2D_SELF_DETECT] = false;
                mIsItemShow[ID_CONVERSION] = false;
                mIsItemShow[ID_3D_TO_2D] = false;
            } else if (displayFormat3d != TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
                mIsItemShow[ID_SELF_DETECT] = false;
                mIsItemShow[ID_3D_TO_2D_SELF_DETECT] = false;
                mIsItemShow[ID_CONVERSION] = true;
                mIsItemShow[ID_3D_TO_2D] = false;
            } else if (displayFormat3dTo2d != TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_NONE) {
                if (displayFormat3dTo2d != TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_AUTO) {
                    mIsItemShow[ID_3D_TO_2D_SELF_DETECT] = false;
                    mIsItemShow[ID_3D_TO_2D] = true;
                } else {
                    mIsItemShow[ID_3D_TO_2D_SELF_DETECT] = true;
                    mIsItemShow[ID_3D_TO_2D] = false;
                }
                mIsItemShow[ID_SELF_DETECT] = false;
                mIsItemShow[ID_CONVERSION] = false;
            } else {
                updateSelfDetectBtn();
                mIsItemShow[ID_3D_TO_2D_SELF_DETECT] = true;
                mIsItemShow[ID_CONVERSION] = true;
                mIsItemShow[ID_3D_TO_2D] = true;
            }
            if (displayFormat3dTo2d == SELF_DETECT_3DTO2D_RIGHT_NOW) {
                mIsItemShow[ID_SELF_DETECT] = false;
                mIsItemShow[ID_3D_TO_2D_SELF_DETECT] = true;
                mIsItemShow[ID_CONVERSION] = false;
                mIsItemShow[ID_3D_TO_2D] = false;
            }
            if (displayFormat3d == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE
                    && current3dType == TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) {
                update2dMode();
            } else if ((displayFormat3d == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO)
                    && (current3dType == TvS3DManager.THREE_DIMENSIONS_TYPE_NONE)) {
                update2dMode();
            } else {
                update3dMode(is2dTo3d);
            }
        } else {
            Log.d(TAG, "disable all items in demo mode");
            Arrays.fill(mIsItemShow, Boolean.FALSE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkMWE();
        updateUI();
    }

    /**
     * Shows the SelfAdaptiveDetect setting fragment.
     */
    public class SelfAdaptiveDetectItem extends ActionItem {
        public final String DIALOG_TAG = SelfAdaptiveDetectItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SelfAdaptiveDetectItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3d_self_adaptive_detecttriple));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() != null) {
                        int mode = TvS3DManager.getInstance().getSelfAdaptiveDetectMode();
                        setDescription(mStrSelfDetect[mode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SelfAdaptiveDetectFragment());
        }
    }

    /**
     * Shows the 3dto2d_self_adaptive_detecttriple setting fragment.
     */
    public class Menu3Dto2DSelfAdaptiveDetectItem extends ActionItem {
        public final String DIALOG_TAG = Menu3Dto2DSelfAdaptiveDetectItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public Menu3Dto2DSelfAdaptiveDetectItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3dto2d_self_adaptive_detecttriple));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() != null) {
                        int mode = TvS3DManager.getInstance().get3DTo2DDisplayMode();
                        mode = (mode == TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_AUTO) ? 1 : 0;
                        setDescription(mStr3Dto2DSelfDetect[mode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new Menu3Dto2DSelfAdaptiveDetectFragment());
        }
    }

    /**
     * Shows the 3d conversion setting fragment.
     */
    public class Menu3DCconversion extends ActionItem {
        public final String DIALOG_TAG = Menu3DCconversion.class.getSimpleName();

        private final MainActivity mMainActivity;

        public Menu3DCconversion(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3d_conversion));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() != null) {
                        int mode = TvS3DManager.getInstance().get3dDisplayFormat();
                        // error case handling
                        checkMWE();
                        setDescription(mStr3DConversion[mode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new Menu3DConversionFragment());
        }
    }

    /**
     * Shows the s3d_3dto2d setting fragment.
     */
    public class Menu3Dto2D extends ActionItem {
        public final String DIALOG_TAG = Menu3Dto2D.class.getSimpleName();

        private final MainActivity mMainActivity;

        public Menu3Dto2D(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3d_3dto2d));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() != null) {
                        int Mode = TvS3DManager.getInstance().get3DTo2DDisplayMode();
                        setDescription(mStr3Dto2D[Mode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new Menu3Dto2DFragment());
        }
    }

    /**
     * Shows the 3D depth setting fragment.
     */
    public class Menu3DDdepth extends ActionItem {
        public final String DIALOG_TAG = Menu3DDdepth.class.getSimpleName();

        private static final int DEPTH_MAX = 31;

        private static final int DEPTH_MIN = 0;

        private final MainActivity mMainActivity;

        private int m3DDepth;

        public Menu3DDdepth(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3d_3ddepth));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() == null) {
                        return;
                    }
                    // Get 3D depth value frome system and then set the ui
                    m3DDepth = TvS3DManager.getInstance().get3DDepthMode();
                    if ((DEPTH_MIN > m3DDepth) || (m3DDepth > DEPTH_MAX)) {
                        m3DDepth = DEPTH_MIN;
                    }
                    setDescription(Integer.toString(m3DDepth));
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.str_3d_3doffset), DEPTH_MIN, DEPTH_MAX,
                    m3DDepth, new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvS3DManager.getInstance() == null) {
                                return;
                            }
                            m3DDepth = value;
                            // Set 3D depth value to system
                            TvS3DManager.getInstance().set3DDepthMode(m3DDepth);
                            setDescription(Integer.toString(m3DDepth));
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the s3DOIffset setting fragment.
     */
    public class Menu3DOffset extends ActionItem {
        public final String DIALOG_TAG = Menu3DOffset.class.getSimpleName();

        private static final int OFFSET_MAX = 31;

        private static final int OFFSET_MIN = 0;

        private final MainActivity mMainActivity;

        private int m3DOffset;

        private Handler mHandler = new Handler();

        public Menu3DOffset(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3d_3doffset));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() == null) {
                        return;
                    }
                    // Get 3D offset value frome system and then set the ui
                    m3DOffset = TvS3DManager.getInstance().get3DOffsetMode();
                    if ((OFFSET_MIN > m3DOffset) || (m3DOffset > OFFSET_MAX)) {
                        m3DOffset = OFFSET_MIN;
                    }
                    setDescription(Integer.toString(m3DOffset));
                }
            });
        }

        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.str_3d_3doffset), OFFSET_MIN, OFFSET_MAX,
                    m3DOffset, new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvS3DManager.getInstance() == null) {
                                return;
                            }
                            m3DOffset = value;
                            // Set 3D offset value to system
                            TvS3DManager.getInstance().set3DOffsetMode(m3DOffset);
                            setDescription(Integer.toString(m3DOffset));
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the 3D outputaspect setting fragment.
     */
    public class Menu3DOutputAspect extends ActionItem {
        public final String DIALOG_TAG = Menu3DOutputAspect.class.getSimpleName();

        private final MainActivity mMainActivity;

        public Menu3DOutputAspect(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3d_3doutputaspect));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() != null) {
                        int mode = TvS3DManager.getInstance().getThreeDVideoOutputAspectMode();
                        setDescription(mStr3DOutputAspect[mode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new Menu3DOutputAspectFragment());
        }
    }

    /**
     * Shows the 3d LRSwitch setting fragment.
     */
    public class Menu3DLRSwitch extends SwitchItem {
        public final String DIALOG_TAG = Menu3DLRSwitch.class.getSimpleName();

        private final MainActivity mMainActivity;

        private Handler mHandler = new Handler();

        public Menu3DLRSwitch(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_3d_lrswitch));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvS3DManager.getInstance() == null) {
                        return;
                    }
                    // Check if LR switch is enabled and then set the ui
                    if (TvS3DManager.getInstance().getLrViewSwitch() == EnumThreeDVideoLrViewSwitch.E_ThreeD_Video_LRVIEWSWITCH_EXCHANGE) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvS3DManager.getInstance() == null) {
                return;
            }
            // Enable/Disable LR switch
            if (isChecked()) {
                TvS3DManager.getInstance().setLrViewSwitch(
                        EnumThreeDVideoLrViewSwitch.E_ThreeD_Video_LRVIEWSWITCH_EXCHANGE);
            } else {
                TvS3DManager.getInstance().setLrViewSwitch(
                        EnumThreeDVideoLrViewSwitch.E_ThreeD_Video_LRVIEWSWITCH_NOTEXCHANGE);
            }
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_menu_3d);
    }

    @Override
    protected List<Item> getItemList() {
        mSelfAdaptiveDetectItem = new SelfAdaptiveDetectItem((MainActivity) getActivity());
        mMenu3Dto2DSelfAdaptiveDetectItem = new Menu3Dto2DSelfAdaptiveDetectItem((MainActivity) getActivity());
        mMenu3DCconversion = new Menu3DCconversion((MainActivity) getActivity());
        mMenu3Dto2D = new Menu3Dto2D((MainActivity) getActivity());
        mMenu3DDdepth = new Menu3DDdepth((MainActivity) getActivity());
        mMenu3DOffset = new Menu3DOffset((MainActivity) getActivity());
        mMenu3DOutputAspect = new Menu3DOutputAspect((MainActivity) getActivity());
        mMenu3DLRSwitch = new Menu3DLRSwitch((MainActivity) getActivity());
        List<Item> items = new ArrayList<>();

        // error case handling
        checkMWE();

        updateUI();

        if (mIsItemShow[ID_SELF_DETECT] == true) {
            items.add(mSelfAdaptiveDetectItem);
        }
        if (mIsItemShow[ID_3D_TO_2D_SELF_DETECT] == true) {
            items.add(mMenu3Dto2DSelfAdaptiveDetectItem);
        }
        if (mIsItemShow[ID_CONVERSION] == true) {
            items.add(mMenu3DCconversion);
        }
        if (mIsItemShow[ID_3D_TO_2D] == true) {
            items.add(mMenu3Dto2D);
        }
        if (mIsItemShow[ID_DEPTH] == true) {
            items.add(mMenu3DDdepth);
        }
        if (mIsItemShow[ID_OFFSET] == true) {
            items.add(mMenu3DOffset);
        }
        if (mIsItemShow[ID_OUTPUT_ASPECT] == true) {
            items.add(mMenu3DOutputAspect);
        }
        if (mIsItemShow[ID_LR_SWITCH] == true) {
            items.add(mMenu3DLRSwitch);
        }
        return items;
    }
}
