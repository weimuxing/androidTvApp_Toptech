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

package com.mstar.tv.ui.sidepanel.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.TextView;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.content.Context;
import android.util.Log;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.fragment.picture.PictureFragment;
import com.mstar.tv.ui.sidepanel.fragment.sound.SoundFragment;
import com.mstar.tv.ui.sidepanel.fragment.time.TimeFragment;
import com.mstar.tv.ui.sidepanel.fragment.demo.DemoFragment;
import com.mstar.tv.ui.sidepanel.fragment.misc.MiscFragment;
import com.mstar.tv.ui.sidepanel.fragment.threeD.Menu3DFragment;
import com.mstar.tv.ui.dialog.UsbDiskDialogFragment;
import com.mstar.tv.ui.sidepanel.fragment.language.LanguageFragment;
import com.mstar.tv.ui.sidepanel.fragment.pvr.PvrFragment;
import com.mstar.tv.ui.sidepanel.fragment.hdmi.HdmiFragment;
import com.mstar.tv.ui.sidepanel.fragment.power.PowerFragment;
import com.mstar.android.tv.TvCommonManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows advanced option.
 */
public class AdvancedOptionMenuFragment extends SideFragment {

    private final static String TAG = "AdvancedOptionMenuFragment";

    private static Intent inputsActivity = new Intent();

    /**
     * Shows the Inputs setting fragment.
     */
    public static class InputsActionItem extends ActionItem {
        public final static String DIALOG_TAG = InputsActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public InputsActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.system_inputs));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.startActivity(inputsActivity);
        }
    }

    /**
     * Shows the Picture setting fragment.
     */
    public static class PictureActionItem extends ActionItem {
        public final static String DIALOG_TAG = PictureActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public PictureActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_picture));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new PictureFragment());
        }
    }

    /**
     * Shows the Sound setting fragment.
     */
    public static class SoundActionItem extends ActionItem {
        public final static String DIALOG_TAG = SoundActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SoundActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_sound));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SoundFragment());
        }
    }

    /**
     * Shows the Language setting fragment.
     */
    public static class LanguageActionItem extends ActionItem {
        public final static String DIALOG_TAG = LanguageActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public LanguageActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_language));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new LanguageFragment());
        }
    }

    /**
     * Shows the PVR setting fragment.
     */
    public static class PVRActionItem extends ActionItem {
        public final static String DIALOG_TAG = PVRActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public PVRActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_pvr));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new PvrFragment());
        }
    }

    /**
     * Shows the HDMI setting fragment.
     */
    public static class HDMIActionItem extends ActionItem {
        public final static String DIALOG_TAG = HDMIActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public HDMIActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_hdmi));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new HdmiFragment());
        }
    }

    /**
     * Shows the Power setting fragment.
     */
    public static class PowerActionItem extends ActionItem {
        public final static String DIALOG_TAG = PowerActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public PowerActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_power));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new PowerFragment());
        }
    }

    /**
     * Shows the Time setting fragment.
     */
    public static class TimeActionItem extends ActionItem {
        public final static String DIALOG_TAG = TimeActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public TimeActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_time));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new TimeFragment());
        }
    }

    /**
     * Shows the Demo setting fragment.
     */
    public static class DemoActionItem extends ActionItem {
        public final static String DIALOG_TAG = DemoActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public DemoActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_demo));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new DemoFragment());
        }
    }

    /**
     * Shows the 3D setting fragment.
     */
    public static class Menu3DActionItem extends ActionItem {
        public final static String DIALOG_TAG = Menu3DActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public Menu3DActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_3d));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new Menu3DFragment());
        }
    }

    /**
     * Shows the Misc setting fragment.
     */
    public static class MiscActionItem extends ActionItem {
        public final static String DIALOG_TAG = MiscActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public MiscActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_menu_misc));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new MiscFragment());
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.advanced_option);
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();

        boolean isPVREnable = false;
        Cursor snconfig = getActivity()
                .getApplicationContext()
                .getContentResolver()
                .query(Uri.parse("content://mstar.tv.usersetting/snconfig"), null, null, null, null);
        if (snconfig.moveToFirst()) {
            isPVREnable = snconfig.getInt(snconfig.getColumnIndex("PVR_ENABLE")) == 1 ? true
                    : false;
        }
        snconfig.close();

        PackageManager packageManager = getActivity().getPackageManager();
        inputsActivity.setComponent(new ComponentName("com.android.tv.settings",
                        "com.android.tv.settings.system.InputsActivity"));
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(inputsActivity,
            PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            items.add(new InputsActionItem((MainActivity) getActivity()));
        }

        items.add(new PictureActionItem((MainActivity) getActivity()));
        items.add(new SoundActionItem((MainActivity) getActivity()));
        if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            items.add(new LanguageActionItem((MainActivity) getActivity()));
        }
        if (isPVREnable == true) {
            items.add(new PVRActionItem((MainActivity) getActivity()));
        }
        items.add(new HDMIActionItem((MainActivity) getActivity()));
        items.add(new PowerActionItem((MainActivity) getActivity()));
        items.add(new TimeActionItem((MainActivity) getActivity()));
        items.add(new DemoActionItem((MainActivity) getActivity()));
        items.add(new Menu3DActionItem((MainActivity) getActivity()));
        items.add(new MiscActionItem((MainActivity) getActivity()));
        return items;
    }
}
