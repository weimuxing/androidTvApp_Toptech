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

package com.mstar.tv.ui.sidepanel.fragment.misc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Environment;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.net.Uri;
import android.util.Log;
import android.text.TextWatcher;
import android.text.Editable;
import android.text.TextUtils;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.dialog.SliderDialogFragment;
import com.mstar.tv.ui.dialog.OptionDialogFragment;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.tv.ui.sidepanel.fragment.parentalcontrols.RatingsFragment;
import com.mstar.android.tv.TvCaManager;
import com.mstar.android.tv.TvCcManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvHbbTVManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvGingaManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.storage.MStorageManager;
import com.mstar.android.tvapi.dtv.dvb.isdb.vo.GingaInfo;
import com.mstar.tv.util.Constant;
import com.mstar.tv.ui.activity.CimmiActivity;
import com.mstar.tv.ui.activity.ca.CaActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Shows version and optional license information.
 */
public class MiscFragment extends SideFragment {
    private final String TAG = "MiscFragment";

    private static Handler mHandler = new Handler();

    private static final int ID_GINGA = 0;

    private static final int ID_VCHIP = 1;

    private static final int ID_CC = 2;

    private static final int ID_STORE_COOKIES = 3;

    private static final int ID_SWITCH_MODE = 4;

    private static final int ID_SOURCE_IDENT = 5;

    private static final int ID_SOURCE_SWITCH = 6;

    private static final int ID_SOURCE_PREVIEW = 7;

    private static final int ID_HBBTV = 8;

    private static final int ID_LOCATION_CODE = 9;

    private static final int ID_NUMS = 10;

    // String arrays
    private String mStrCloseCaption[];

    private String mStrSwitchMode[];

    private String mStrColorRange[];

    private int mTvSystem = -1;

    private boolean mIsItemShow[];

    private ContentResolver mContentResolver = null;

    private void updateUI() {
        Arrays.fill(mIsItemShow, Boolean.FALSE);
        if (TvCommonManager.getInstance() == null) {
            return;
        }
        int inputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        int CountryID = TvChannelManager.getInstance().getSystemCountryId();
        Log.d(TAG, "inputSource: " + inputSource);
        Log.d(TAG, "CountryID:" + CountryID);

        // update CC
        if (true == isATSC()) {
            if (TvCommonManager.getInstance()
                    .isSupportModule(TvCommonManager.MODULE_ATSC_CC_ENABLE)
                    && TvCommonManager.getInstance().isSupportModule(
                            TvCommonManager.MODULE_NTSC_CC_ENABLE)) {
                // ATSC TV System using [DTV]MODULE_ATSC_CC_ENABLE +
                // [ATV]MODULE_NTSC_CC_ENABLE
                mIsItemShow[ID_CC] = true;
            }
        } else if (true == isISDB()) {
            if (TvCommonManager.getInstance()
                    .isSupportModule(TvCommonManager.MODULE_ISDB_CC_ENABLE)
                    && TvCommonManager.getInstance().isSupportModule(
                            TvCommonManager.MODULE_NTSC_CC_ENABLE)) {
                // ISDB TV System using [DTV]MODULE_ISDB_CC_ENABLE +
                // [ATV]MODULE_NTSC_CC_ENABLE
                mIsItemShow[ID_CC] = true;
            }
        } else {
            if (TvCommonManager.getInstance()
                    .isSupportModule(TvCommonManager.MODULE_NTSC_CC_ENABLE)) {
                // ASIA CC = [ATV]MODULE_NTSC_CC_ENABLE
                mIsItemShow[ID_CC] = true;
            }
        }

        // update Ginga
        if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            if (true == isISDB()) {
                mIsItemShow[ID_GINGA] = true;
            } else {
                mIsItemShow[ID_GINGA] = false;
            }
        }

        // update VChip
        if (true == isATSC()) {
            mIsItemShow[ID_VCHIP] = true;
        }

        // update Store Cookies
        if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            if (CountryID == TvCountry.SPAIN) {
                mIsItemShow[ID_STORE_COOKIES] = true;
            }
        }

        // update Switch mode
        if (true == isATSC()) {
            if (inputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                mIsItemShow[ID_SWITCH_MODE] = true;
            } else {
                mIsItemShow[ID_SWITCH_MODE] = false;
            }
        } else {
            if ((inputSource == TvCommonManager.INPUT_SOURCE_ATV)
                    || (inputSource == TvCommonManager.INPUT_SOURCE_DTV)) {
                mIsItemShow[ID_SWITCH_MODE] = true;
            } else {
                mIsItemShow[ID_SWITCH_MODE] = false;
            }
        }

        // update source ident and switch
        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
            mIsItemShow[ID_SOURCE_IDENT] = true;
            mIsItemShow[ID_SOURCE_SWITCH] = true;
        }

        // update source preview
        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_PREVIEW_MODE)) {
            mIsItemShow[ID_SOURCE_PREVIEW] = true;
        }

        // update HBBTV
        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_HBBTV)) {
            mIsItemShow[ID_HBBTV] = true;
        }

        // update Location Code
        if (TvCountry.INDONESIA == TvChannelManager.getInstance().getSystemCountryId()) {
            if (true == isDVBT()) {
                // The location code setting is for EWS and only enabled in
                // DVBT/DVBT2.
                mIsItemShow[ID_LOCATION_CODE] = true;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrCloseCaption = getActivity().getResources().getStringArray(
                R.array.str_arr_setting_cc_mode_vals);
        mStrSwitchMode = getActivity().getResources().getStringArray(
                R.array.str_arr_misc_switchmodetype);
        mStrColorRange = getActivity().getResources().getStringArray(
                R.array.str_arr_misc_colorrangetype);
        mIsItemShow = new boolean[ID_NUMS];
    }

    /**
     * Shows the Ginga setting fragment.
     */
    public class GingaItem extends SwitchItem {
        public final String DIALOG_TAG = GingaItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public GingaItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_ginga_on_off));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvGingaManager.getInstance() == null) {
                        return;
                    }
                    // Check if Ginga is enabled and then set the ui
                    if (TvGingaManager.getInstance().isGingaEnabled()) {
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
            if (TvGingaManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Ginga
            if (isChecked()) {
                TvGingaManager.getInstance().enableGinga();
            } else {
                TvGingaManager.getInstance().disableGinga();
            }
        }
    }

    /**
     * Shows the Ginga list setting fragment.
     */
    public class GingaListItem extends ActionItem {
        public final String DIALOG_TAG = GingaListItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private List<GingaInfo> apps;

        private CharSequence[] gingaApps;

        public GingaListItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_ginga_list));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        private String getUsbPath() {
            MStorageManager storageManager = MStorageManager.getInstance(getActivity());
            String[] volumes = storageManager.getVolumePaths();

            if (volumes == null || volumes.length < 2) {
                return "";
            }

            String state = storageManager.getVolumeState(volumes[1]);
            if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                return "";
            }
            String path = volumes[1];
            return path;
        }

        @Override
        public void onSelected() {
            String path = getUsbPath();
            if (!TextUtils.isEmpty(path)) {
                path += "/apps/";
            }
            apps = TvGingaManager.getInstance().getApps(path);
            int size = apps.size();
            gingaApps = new String[size];
            for (int j = 0; j < size; j++) {
                gingaApps[j] = apps.get(j).name;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ginga App")
                    .setItems(gingaApps, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final long aid = apps.get(which).aid;
                            final long oid = apps.get(which).oid;
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    TvGingaManager.getInstance().startApplication(aid, oid);
                                }
                            }, 1000);
                            getActivity().finish();
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    }).show();
        }
    }

    /**
     * Shows the Vchip setting fragment.
     */
    public class VCHIPItem extends SwitchItem {
        public final String DIALOG_TAG = VCHIPItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private boolean mVCHIPOn;

        public VCHIPItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_vchip_vchip));
            mMainActivity = mainActivity;
            mVCHIPOn = true;
            setChecked(mVCHIPOn);
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (isChecked()) {
                // To do: Set Vchip on here
            } else {
                // To do: Set Vchip off here
            }
        }
    }

    /**
     * Shows the Close Caption setting fragment.
     */
    public class CloseCaptionItem extends ActionItem {
        public final String DIALOG_TAG = CloseCaptionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public CloseCaptionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_closed_caption));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new CloseCaptionFragment());
        }
    }

    /**
     * Shows the Store Cookies setting fragment.
     */
    public class StoreCookiesItem extends SwitchItem {
        public final String DIALOG_TAG = StoreCookiesItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public StoreCookiesItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_store_cookies));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvHbbTVManager.getInstance() == null) {
                        return;
                    }
                    // Check if Store Cookies is enabled and then set the ui
                    if (TvHbbTVManager.getInstance().getStoreCookiesEnable()) {
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
            if (TvHbbTVManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Store Cookies
            if (isChecked()) {
                TvHbbTVManager.getInstance().setStoreCookiesEnable(true);
            } else {
                TvHbbTVManager.getInstance().setStoreCookiesEnable(false);
            }
        }
    }

    /**
     * Shows the Menu Keeping Time setting fragment.
     */
    public class MenuKeepingTimeItem extends ActionItem {
        public final String DIALOG_TAG = MenuKeepingTimeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public MenuKeepingTimeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_menutime));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new MenuKeepingTimeFragment());
        }
    }

    /**
     * Shows the Switch Mode setting fragment.
     */
    public class SwitchModeItem extends ActionItem {
        public final String DIALOG_TAG = SwitchModeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SwitchModeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_switchmode));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvChannelManager.getInstance() != null) {
                        int SwitchMode = TvChannelManager.getInstance().getAtvChannelSwitchMode();
                        setDescription(mStrSwitchMode[SwitchMode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SwitchModeFragment());
        }
    }

    /**
     * Shows the Source Identify setting fragment.
     */
    public class SourceIdentifyItem extends SwitchItem {
        public final String DIALOG_TAG = SourceIdentifyItem.class.getSimpleName();

        private static final int SOURCE_IDENTIFITY_OFF = 0;

        private static final int SOURCE_IDENTIFITY_ON = 1;

        private final MainActivity mMainActivity;

        public SourceIdentifyItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_autosourceidentify));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvCommonManager.getInstance() == null) {
                        return;
                    }
                    // Check if Store Cookies is enabled and then set the ui
                    if (TvCommonManager.getInstance().getSourceIdentState() == SOURCE_IDENTIFITY_ON) {
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
            if (TvCommonManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Source Identify
            if (isChecked()) {
                TvCommonManager.getInstance().setSourceIdentState(SOURCE_IDENTIFITY_ON);
            } else {
                TvCommonManager.getInstance().setSourceIdentState(SOURCE_IDENTIFITY_OFF);
            }
        }
    }

    /**
     * Shows the Source Preview setting fragment.
     */
    public class SourcePreviewItem extends SwitchItem {
        public final String DIALOG_TAG = SourcePreviewItem.class.getSimpleName();

        private static final int SOURCE_PREVIEW_OFF = 0;

        private static final int SOURCE_PREVIEW_ON = 1;

        private final MainActivity mMainActivity;

        public SourcePreviewItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_sourcepreview));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvCommonManager.getInstance() == null) {
                        return;
                    }
                    // Check if Store Cookies is enabled and then set the ui
                    if (TvCommonManager.getInstance().getSourcePreviewState() == SOURCE_PREVIEW_ON) {
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
            if (TvCommonManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Source Preview
            if (isChecked()) {
                TvCommonManager.getInstance().setSourcePreviewState(SOURCE_PREVIEW_ON);
            } else {
                TvCommonManager.getInstance().setSourcePreviewState(SOURCE_PREVIEW_OFF);
            }
        }
    }

    /**
     * Shows the Movie mode setting fragment.
     */
    public class MovieModeItem extends SwitchItem {
        public final String DIALOG_TAG = MovieModeItem.class.getSimpleName();

        private static final int FILM_MODE_OFF = 0;

        private static final int FILM_MODE_ON = 1;

        private final MainActivity mMainActivity;

        public MovieModeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_moviemode));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() == null) {
                        return;
                    }
                    // Check if Movide Mode (Film Mode) is enabled and then set
                    // the ui
                    if (TvPictureManager.getInstance().getFilm() == FILM_MODE_ON) {
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
            // Enable/Disable Movide Mode
            if (isChecked()) {
                TvPictureManager.getInstance().setFilm(FILM_MODE_ON);
            } else {
                TvPictureManager.getInstance().setFilm(FILM_MODE_OFF);
            }
        }
    }

    /**
     * Shows the Blue Screen Switch setting fragment.
     */
    public class BlueScreenSwitchModeItem extends SwitchItem {
        public final String DIALOG_TAG = BlueScreenSwitchModeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public BlueScreenSwitchModeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_bluescreenswitch));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvCommonManager.getInstance() == null) {
                        return;
                    }
                    // Check if Blue Screen Mode is enabled and then set the ui
                    if (TvCommonManager.getInstance().getBlueScreenMode()) {
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
            if (TvCommonManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Blue Screen Mode
            if (isChecked()) {
                TvCommonManager.getInstance().setBlueScreenMode(true);
            } else {
                TvCommonManager.getInstance().setBlueScreenMode(false);
            }
        }
    }

    /**
     * Shows the HBBTV setting fragment.
     */
    public class HBBTVOnOffItem extends SwitchItem {
        public final String DIALOG_TAG = HBBTVOnOffItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public HBBTVOnOffItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_hbbtv_switch));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvHbbTVManager.getInstance() == null) {
                        return;
                    }
                    // Check if HbbTV is enabled and then set the ui
                    if (TvHbbTVManager.getInstance().getHbbTVOnOff()) {
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
            if (TvHbbTVManager.getInstance() == null) {
                return;
            }
            // Enable/Disable HbbTv
            if (isChecked()) {
                TvHbbTVManager.getInstance().setHbbTVOnOff(true);
            } else {
                TvHbbTVManager.getInstance().setHbbTVOnOff(false);
            }
        }
    }

    /**
     * Shows the Location Code setting fragment.
     */
    public class LocationCodeItem extends ActionItem {
        public final String DIALOG_TAG = LocationCodeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private void showLocationCodeInputDialog(Activity activity) {
            class LocationCodeTextWatcher implements TextWatcher {
                private AlertDialog alertDialog;

                private final int MAX_TEXT_LEN = 5;

                public LocationCodeTextWatcher(AlertDialog d) {
                    alertDialog = d;
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    int count = arg0.length();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(
                            (count >= MAX_TEXT_LEN) ? true : false);
                }

                public void afterTextChanged(Editable arg0) {
                }
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.str_misc_location_code));
            final View view = activity.getLayoutInflater().inflate(
                    R.layout.misc_location_code_input_dialog_5_digits, null);
            EditText editText = (EditText) view.findViewById(R.id.input_dialog_edittext);
            builder.setView(view);
            builder.setPositiveButton(activity.getResources().getString(R.string.str_save),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            EditText editText = (EditText) view
                                    .findViewById(R.id.input_dialog_edittext);
                            TvDvbChannelManager.getInstance().setUserLocationCode(
                                    Integer.valueOf(editText.getText().toString()));
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton(activity.getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            editText.addTextChangedListener(new LocationCodeTextWatcher(alertDialog));
            alertDialog.show();
            String str = String.format("%05d", TvDvbChannelManager.getInstance()
                    .getUserLocationCode());
            editText.setText(str);

            editText.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN
                            && keyCode == KeyEvent.KEYCODE_BACK) {
                        EditText editText = (EditText) v.findViewById(R.id.input_dialog_edittext);
                        if (editText.getText().toString().length() > 0) {
                            editText.dispatchKeyEvent(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
                                    KeyEvent.KEYCODE_DEL, 0));
                            editText.dispatchKeyEvent(new KeyEvent(0, 0, KeyEvent.ACTION_UP,
                                    KeyEvent.KEYCODE_DEL, 0));
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        public LocationCodeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_location_code));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @Override
        public void onSelected() {
            showLocationCodeInputDialog(getActivity());
        }
    }

    /**
     * Shows the Restore To Default setting fragment.
     */
    public class RestoreToDefaultItem extends ActionItem {
        public final String DIALOG_TAG = RestoreToDefaultItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public RestoreToDefaultItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_restoretodefault));
            mMainActivity = mainActivity;
        }

        private void restoreToDefault() {
            TvCommonManager tTvCommonManager = TvCommonManager.getInstance();
            if (tTvCommonManager == null) {
                return;
            }
            if (TvFactoryManager.getInstance() == null) {
                return;
            }

            if (TvFactoryManager.getInstance().restoreToDefault() == true) {
                if (VERSION.SDK_INT >= TvCommonManager.API_LEVEL_LOLLIPOP_MR1) {
                    deleteTvDB();
                }
                int currInputSource = tTvCommonManager.getCurrentTvInputSource();
                if (currInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                    TvS3DManager.getInstance().setDisplayFormatForUI(
                            TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                    if ((tTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ISDB)
                            && (tTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ATSC)) {
                        // Because restore to factory default value,reset
                        // routeIndex to 0
                        TvDvbChannelManager.getInstance().setDtvAntennaType(0);
                    }
                }

                // Reset First Startup Input Source
                SharedPreferences preferencesSettings = getActivity().getSharedPreferences(
                        Constant.PREFERENCES_INPUT_SOURCE, Context.MODE_PRIVATE);
                preferencesSettings.edit().remove(Constant.PREFERENCES_PREVIOUS_INPUT_SOURCE)
                        .commit();

                preferencesSettings = getActivity().getSharedPreferences(
                        Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
                // Reset Setup Wizard
                preferencesSettings.edit().remove(Constant.PREFERENCES_IS_AUTOSCAN_LAUNCHED)
                        .commit();
                // Reset Setup Location Wizard
                preferencesSettings.edit().remove(Constant.PREFERENCES_IS_LOCATION_SELECTED)
                        .commit();
                preferencesSettings.edit().commit();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tTvCommonManager.rebootSystem("reboot");
            } else {
                Log.e(TAG, "restoreToDefault failed!");
            }

        }

        private ContentResolver getContentResolver() {
            if (mContentResolver == null) {
                mContentResolver = getActivity().getApplicationContext().getContentResolver();
            }
            return mContentResolver;
        }

        private void deleteTvDB() {
            long ret = -1;
            try {
                Log.d(TAG, "clean TV channel content");
                /**
                * Support TIF clean databases that create by Android TvProvider
                */
                ret = getContentResolver().delete(Uri.parse("content://android.media.tv/channel"), null, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ret == -1) {
                System.out.println("clean database ignored");
            }
        }

        @Override
        public void onSelected() {
            OptionDialogFragment dialog = new OptionDialogFragment(
                    mMainActivity.getString(R.string.str_misc_restoretodefault),
                    mMainActivity
                            .getString(R.string.str_misc_alert_dialog_message_restore_to_default),
                    mMainActivity.getString(R.string.str_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restoreToDefault();
                        }
                    }, mMainActivity.getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the Source Auto Switch setting fragment.
     */
    public class SourceAutoSwitchItem extends SwitchItem {
        public final String DIALOG_TAG = SourceAutoSwitchItem.class.getSimpleName();

        private static final int SOURCE_AUTO_SWITCH_OFF = 0;

        private static final int SOURCE_AUTO_SWITCH_ON = 1;

        private final MainActivity mMainActivity;

        public SourceAutoSwitchItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_set_autosourceswit));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvCommonManager.getInstance() == null) {
                        return;
                    }
                    // Check if Source Auto Switch is enabled and then set the
                    // ui
                    if (TvCommonManager.getInstance().getSourceSwitchState() == SOURCE_AUTO_SWITCH_OFF) {
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
            if (TvCommonManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Source Auto Switch
            if (isChecked()) {
                TvCommonManager.getInstance().setSourceSwitchState(SOURCE_AUTO_SWITCH_ON);
            } else {
                TvCommonManager.getInstance().setSourceSwitchState(SOURCE_AUTO_SWITCH_OFF);
            }
        }
    }

    /**
     * Shows the Signal Info setting fragment.
     */
    public class SignalInfoItem extends ActionItem {
        public final String DIALOG_TAG = SignalInfoItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSignalInfo;

        public SignalInfoItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_signalinfo));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SignalInfoFragment());
        }
    }

    /**
     * Shows the CI Info setting fragment.
     */
    public class CIInfoItem extends ActionItem {
        public final String DIALOG_TAG = CIInfoItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public CIInfoItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_cimmi_title));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            if (TvCiManager.getInstance() != null
                    && TvCommonManager.getInstance() != null) {
                int currInputSource = TvCommonManager.getInstance()
                        .getCurrentTvInputSource();
                if (currInputSource != TvCommonManager.INPUT_SOURCE_DTV) {
                    return;
                }

                int status = TvCiManager.CARD_STATE_NO;
                status = TvCiManager.getInstance().getCiCardState();

                if (status == TvCiManager.CARD_STATE_READY) {
                    TvCiManager.getInstance().enterMenu();
                    Intent intent = new Intent(mMainActivity, CimmiActivity.class);
                    mMainActivity.startActivity(intent);
                    mMainActivity.finish();
                }
            }
        }
    }

    /**
     * Shows the CA Info setting fragment.
     */
    public class CAInfoItem extends ActionItem {
        public final String DIALOG_TAG = CAInfoItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public CAInfoItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_cha_cainformation));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            if (TvCommonManager.getInstance() != null) {
                int currInputSource = TvCommonManager.getInstance()
                        .getCurrentTvInputSource();
                if (currInputSource != TvCommonManager.INPUT_SOURCE_DTV) {
                    return;
                }
                if (TvCaManager.getInstance().CaGetVer() == 0) {
                    return;
                }
                Intent intent = new Intent(mMainActivity, CaActivity.class);
                mMainActivity.startActivity(intent);
            }
        }
    }

    /**
     * Shows the CI operator profile setting fragment.
     */
    public class CIOperatorProfileListItem extends ActionItem {
        public final String DIALOG_TAG = CIOperatorProfileListItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private CIOperatorProfileFragment mFragment;

        public CIOperatorProfileListItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_ci_operator_list));
            mMainActivity = mainActivity;
            mFragment = new CIOperatorProfileFragment();
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(mFragment);
        }

        public boolean isVisible() {
            return mFragment.getOpCount()>0;
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_menu_misc);
    }

    private int getCurrentTvSystem() {
        if (mTvSystem < 0) {
            mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        }
        return mTvSystem;
    }

    private boolean isATSC() {
        return TvCommonManager.TV_SYSTEM_ATSC == getCurrentTvSystem();
    }

    private boolean isISDB() {
        return TvCommonManager.TV_SYSTEM_ISDB == getCurrentTvSystem();
    }

    private boolean isDVBT() {
        boolean ret = false;
        int currentSystem = getCurrentTvSystem();
        if ((TvCommonManager.TV_SYSTEM_ATSC != currentSystem)
                && (TvCommonManager.TV_SYSTEM_ISDB != currentSystem)) {
            int currentRoute = TvChannelManager.TV_ROUTE_NONE;
            TvTypeInfo tvinfo = TvCommonManager.getInstance().getTvInfo();
            int routeIdx = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
            currentRoute = tvinfo.routePath[routeIdx];
            if ((TvChannelManager.TV_ROUTE_DVBT == currentRoute)
                    || (TvChannelManager.TV_ROUTE_DVBT2 == currentRoute)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        updateUI();

        // add item to menu
        if (mIsItemShow[ID_GINGA] == true) {
            items.add(new GingaItem((MainActivity) getActivity()));
            items.add(new GingaListItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_VCHIP] == true) {
            items.add(new VCHIPItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_CC] == true) {
            items.add(new CloseCaptionItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_STORE_COOKIES] == true) {
            items.add(new StoreCookiesItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_SWITCH_MODE] == true) {
            items.add(new SwitchModeItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_SOURCE_IDENT] == true) {
            items.add(new SourceIdentifyItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_SOURCE_SWITCH] == true) {
            items.add(new SourceAutoSwitchItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_SOURCE_PREVIEW] == true) {
            items.add(new SourcePreviewItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_HBBTV] == true) {
            items.add(new HBBTVOnOffItem((MainActivity) getActivity()));
        }
        if (mIsItemShow[ID_LOCATION_CODE] == true) {
            items.add(new LocationCodeItem((MainActivity) getActivity()));
        }
        items.add(new MovieModeItem((MainActivity) getActivity()));
        items.add(new BlueScreenSwitchModeItem((MainActivity) getActivity()));
        items.add(new RestoreToDefaultItem((MainActivity) getActivity()));

        items.add(new SignalInfoItem((MainActivity) getActivity()));
        items.add(new CIInfoItem((MainActivity) getActivity()));
        items.add(new CAInfoItem((MainActivity) getActivity()));
        CIOperatorProfileListItem ciOperatorProfileListItem = new CIOperatorProfileListItem((MainActivity) getActivity());
        if (ciOperatorProfileListItem.isVisible()) {
            items.add(ciOperatorProfileListItem);
        }
        return items;
    }
}
