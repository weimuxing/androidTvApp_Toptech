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

package com.mstar.tv.ui.sidepanel.fragment.demo;

import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvS3DManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows version and optional license information.
 */
public class DemoFragment extends SideFragment {
    private final String TAG = "DemoFragment";

    private static Handler mHandler = new Handler();

    // String arrays
    private String mStrMWE[];

    private String mStrNR[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrMWE = getActivity().getResources().getStringArray(R.array.str_arr_mwe_text);
        mStrNR = getActivity().getResources().getStringArray(
                R.array.str_arr_pic_imgnoisereduction_vals);
    }

    /**
     * Shows the MWE setting fragment.
     */
    public class MWEItem extends ActionItem {
        public final String DIALOG_TAG = MWEItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private void check3D() {
            int MWEIdx = TvPictureManager.getInstance().getMWEDemoMode();
            if ((TvS3DManager.getInstance().get3dDisplayFormat() != TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE)
                    && (MWEIdx != TvPictureManager.MWE_DEMO_MODE_OFF)) {
                TvS3DManager.getInstance().set3dDisplayFormat(
                        TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                Toast toast = Toast.makeText(getActivity(), R.string.str_demo_toast, 5);
                toast.show();
            }
        }

        public MWEItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_demo_mwe));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        int MWEIdx = TvPictureManager.getInstance().getMWEDemoMode();
                        // error case handling
                        check3D();

                        if (MWEIdx >= mStrMWE.length) {
                            Log.e(TAG, "MWEIdx out of bound : " + MWEIdx);
                            MWEIdx = mStrMWE.length - 1;
                        }
                        setDescription(mStrMWE[MWEIdx]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new MWEFragment());
        }
    }

    /**
     * Shows the DBC setting fragment.
     */
    public class DBCItem extends SwitchItem {
        public final String DIALOG_TAG = DBCItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public DBCItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_demo_dbc));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() == null) {
                        return;
                    }
                    // Check if DBC is enabled and then set the ui
                    if (TvPictureManager.getInstance().isDbcEnabled()) {
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
            if (TvPictureManager.getInstance() == null) {
                return;
            }
            // Enable/Disable DBC
            if (isChecked()) {
                TvPictureManager.getInstance().enableDbc();
            } else {
                TvPictureManager.getInstance().disableDbc();
            }
        }
    }

    /**
     * Shows the contrast setting fragment.
     */
    public class DLCItem extends SwitchItem {
        public final String DIALOG_TAG = DLCItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public DLCItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_demo_dlc));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() == null) {
                        return;
                    }
                    // Check if DLC is enabled and then set the ui
                    if (TvPictureManager.getInstance().isDlcEnabled()) {
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
            if (TvPictureManager.getInstance() == null) {
                return;
            }
            // Enable/Disable DLC
            if (isChecked()) {
                TvPictureManager.getInstance().enableDlc();
            } else {
                TvPictureManager.getInstance().disableDlc();
            }
        }
    }

    /**
     * Shows the DCC setting fragment.
     */
    public class DCCItem extends SwitchItem {
        public final String DIALOG_TAG = DCCItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public DCCItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_demo_dcc));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() == null) {
                        return;
                    }
                    // Check if DCC is enabled and then set the ui
                    if (TvPictureManager.getInstance().isDccEnabled()) {
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
            if (TvPictureManager.getInstance() == null) {
                return;
            }
            // Enable/Disable DCC
            if (isChecked()) {
                TvPictureManager.getInstance().enableDcc();
            } else {
                TvPictureManager.getInstance().disableDcc();
            }
        }
    }

    /**
     * Shows the NR setting fragment.
     */
    public class NRItem extends ActionItem {
        public final String DIALOG_TAG = NRItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public NRItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_demo_nr));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        int NRIdx = TvPictureManager.getInstance().getNoiseReduction();
                        setDescription(mStrNR[NRIdx]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new NRFragment());
        }
    }

    /**
     * Shows the UltraClear setting fragment.
     */
    public class UltraClearItem extends SwitchItem {
        public final String DIALOG_TAG = UltraClearItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public UltraClearItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_demo_uclear));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() == null) {
                        return;
                    }
                    // Check if Ultra Clear is enabled and then set the ui
                    if (TvPictureManager.getInstance().isUClearOn()) {
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
            if (TvPictureManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Ultra Clear
            if (isChecked()) {
                TvPictureManager.getInstance().setUClearStatus(true);
            } else {
                TvPictureManager.getInstance().setUClearStatus(false);
            }
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_menu_demo);
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        items.add(new MWEItem((MainActivity) getActivity()));
        items.add(new DBCItem((MainActivity) getActivity()));
        items.add(new DLCItem((MainActivity) getActivity()));
        items.add(new DCCItem((MainActivity) getActivity()));
        items.add(new NRItem((MainActivity) getActivity()));
        items.add(new UltraClearItem((MainActivity) getActivity()));
        return items;
    }
}
