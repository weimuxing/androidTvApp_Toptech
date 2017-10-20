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

package com.mstar.android.tv.tunersetup.tuning;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;

import com.mstar.android.tv.inputservice.R;
import com.mstar.android.tv.tunersetup.TvIntent;
import com.mstar.android.tv.tunersetup.TunerSetupActivity;
import com.mstar.android.tv.tunersetup.holder.ViewHolder;
import com.mstar.android.tv.framework.MstarBaseActivity;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tv.util.Constant;
import com.mstar.android.tv.util.Utility;

public class AutoTuneOptionActivity extends MstarBaseActivity {
    /** Called when the activity is first created. */

    private static final String TAG = "AutoTuneOptionActivity";

    private ViewHolder viewholder_autotune;

    private int mTvSystem = 0;

    private int mTuningType;

    private int mAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_NONE;

    private int mCurrntRoute = TvChannelManager.TV_ROUTE_NONE;

    private int page_index = 0;

    private int mCountryPageIndex = 0;

    private int mOperatorPageIndex = 0;

    private int mTotalCountryCount = 0;

    private int mTotalOperatorCount = 0;

    private int mCountryIndex = 0;

    private int mDvbcOperator = TvChannelManager.CABLE_OPERATOR_OTHER;

    private String[] mOptionTuningType = null;

    private String[] mOptionCountry = null;

    private String[] mDvbcOperatorNameArray = null;

    private ArrayList<Integer> mOperatorList = null;

    private String[] mOptionScanAntennaTypes = {
            "None", "Air", "Cable"
    };

    private final int ONEPINDEXS = 9;

    private TvChannelManager mTvChannelManager = null;

    private int MSG_ONCREAT = 1001;

    private int mCurrentRoute = TvChannelManager.TV_ROUTE_NONE;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_ONCREAT) {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (true == Utility.isSupportATV()) {
                        mTuningType = TvChannelManager.TV_SCAN_ALL;
                    } else {
                        mTuningType = TvChannelManager.TV_SCAN_DTV;
                    }
                    mTvChannelManager.setUserScanType(mTuningType);
                    viewholder_autotune.text_cha_autotuning_antenna_val
                            .setText(mOptionScanAntennaTypes[mAntennaType]);
                    viewholder_autotune.relative_country_choose.setVisibility(View.GONE);
                    viewholder_autotune.linear_4dots.setVisibility(View.INVISIBLE);
                    viewholder_autotune.linear_main
                            .setBackgroundResource(R.drawable.list_menu_img_bg);
                } else {
                    mTuningType = TvChannelManager.TV_SCAN_DTV;
                    mTvChannelManager.setUserScanType(mTuningType);
                    int currentRouteIndex = TvChannelManager.getInstance()
                            .getCurrentDtvRouteIndex();
                    TvTypeInfo tvInfo = TvCommonManager.getInstance().getTvInfo();
                    mCurrntRoute = tvInfo.routePath[currentRouteIndex];
                    if ((TvChannelManager.TV_ROUTE_DVBS == mCurrntRoute)
                        || (TvChannelManager.TV_ROUTE_DVBS2 == mCurrntRoute)) {
                        viewholder_autotune.text_cha_autotuning_antenna_val
                            .setText(mOptionScanAntennaTypes[mAntennaType]);
                        viewholder_autotune.relative_country_choose.setVisibility(View.GONE);
                        viewholder_autotune.linear_4dots.setVisibility(View.INVISIBLE);
                        viewholder_autotune.linear_main.setBackgroundResource(R.drawable.list_menu_img_bg);
                        viewholder_autotune.linear_cha_autotuning_antenna_type.setVisibility(View.GONE);
                    } else {
                        viewholder_autotune.linear_cha_autotuning_antenna_type.setVisibility(View.GONE);
                        viewholder_autotune.relative_search.setVisibility(View.GONE);
                        updateUiCoutrySelect();
                    }
                }
                viewholder_autotune.linear_cha_autotuning_tuningtype.requestFocus();
                viewholder_autotune.text_cha_autotuning_tuningtype_val
                        .setText(mOptionTuningType[mTuningType]);
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autotuning);

        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mTvChannelManager = TvChannelManager.getInstance();
        TvTypeInfo tvinfo = TvCommonManager.getInstance().getTvInfo();
        int currentRouteIndex = mTvChannelManager.getCurrentDtvRouteIndex();
        mCurrentRoute = tvinfo.routePath[currentRouteIndex];
        final boolean isFromSubpage = getIntent().getBooleanExtra(
                Constant.AUTO_TUNING_OPTION_FROM_SUBPAGE, false);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if (TvAtscChannelManager.getInstance()
                .getDtvAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                mAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_AIR;
            } else {
                mAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_CABLE;
            }
        }

        viewholder_autotune = new ViewHolder(AutoTuneOptionActivity.this);
        viewholder_autotune.findViewsForAutoTuning();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (true == isFromSubpage) {
                    SharedPreferences setting = getSharedPreferences(
                            Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
                    mCountryIndex = setting.getInt(Constant.TUNING_COUNTRY,
                            mTvChannelManager.getSystemCountryId());
                } else {
                    mCountryIndex = mTvChannelManager.getSystemCountryId();
                }
                mCountryPageIndex = mCountryIndex / ONEPINDEXS;
                mTuningType = mTvChannelManager.getUserScanType();
                mOptionCountry = getResources().getStringArray(R.array.str_arr_autotuning_country);
                mTotalCountryCount = mOptionCountry.length;
                registerListeners();
                Message msg = Message.obtain();
                msg.what = MSG_ONCREAT;
                if (TvChannelManager.TV_ROUTE_DVBC == mCurrentRoute) {
                    mOptionTuningType = getResources().getStringArray(
                            R.array.str_arr_dvbc_tuning_type);
                    mDvbcOperatorNameArray = getResources().getStringArray(
                            R.array.str_arr_dvbc_operator);
                    if (true == isFromSubpage) {
                        SharedPreferences setting = getSharedPreferences(Constant.SAVE_SETTING_SELECT,
                                Context.MODE_PRIVATE);
                        mDvbcOperator = setting.getInt(Constant.PREFERENCES_DVBC_OPERATOR,
                                mTvChannelManager.getCableOperator());
                    } else {
                        mDvbcOperator = mTvChannelManager.getCableOperator();
                    }
                } else {
                    mOptionTuningType = getResources().getStringArray(R.array.str_arr_tuning_type);
                }
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((TvChannelManager.TV_ROUTE_DVBS == mCurrentRoute)
            || (TvChannelManager.TV_ROUTE_DVBS2 == mCurrentRoute)) {
            viewholder_autotune.relative_country_choose.setVisibility(View.GONE);
        }
    }

    private void focusLastTuneCountry(int index) {
        index++;
        switch (index % ONEPINDEXS) {
            case 1:
                viewholder_autotune.button_cha_country_1.requestFocus();
                break;
            case 2:
                viewholder_autotune.button_cha_country_2.requestFocus();
                break;
            case 3:
                viewholder_autotune.button_cha_country_3.requestFocus();
                break;
            case 4:
                viewholder_autotune.button_cha_country_4.requestFocus();
                break;
            case 5:
                viewholder_autotune.button_cha_country_5.requestFocus();
                break;
            case 6:
                viewholder_autotune.button_cha_country_6.requestFocus();
                break;
            case 7:
                viewholder_autotune.button_cha_country_7.requestFocus();
                break;
            case 8:
                viewholder_autotune.button_cha_country_8.requestFocus();
                break;
            case 0:
                viewholder_autotune.button_cha_country_9.requestFocus();
                break;
            default:
                // do nothing
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int currentid = getCurrentFocus().getId();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_cha_autotuning_tuningtype:
                        if (false == Utility.isSupportATV()) {
                            break;
                        }
                        if (mTuningType == TvChannelManager.TV_SCAN_ALL) {
                            mTuningType = TvChannelManager.TV_SCAN_ATV;
                        } else {
                            mTuningType++;
                        }
                        viewholder_autotune.text_cha_autotuning_tuningtype_val
                                .setText(mOptionTuningType[mTuningType]);
                        mTvChannelManager.setUserScanType(mTuningType);
                        return true;
                    case R.id.linearlayout_cha_autotuning_antenna_type:
                        if (mAntennaType == TvChannelManager.DTV_ANTENNA_TYPE_CABLE) {
                            mAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_AIR;
                        } else {
                            mAntennaType++;
                        }
                        viewholder_autotune.text_cha_autotuning_antenna_val
                                .setText(mOptionScanAntennaTypes[mAntennaType]);
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            TvAtscChannelManager.getInstance().setDtvAntennaType(mAntennaType);
                        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            TvIsdbChannelManager.getInstance().setDtvAntennaType(mAntennaType);
                        }
                        return true;
                    case R.id.button_cha_autotuning_choosecountry_denmark:
                        if ((mCountryPageIndex + 1) * ONEPINDEXS < mTotalCountryCount) {
                            mCountryPageIndex++;
                            updateUiCoutrySelect();
                            viewholder_autotune.button_cha_country_1.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_choosecountry_finland:
                        if ((mCountryPageIndex + 1) * ONEPINDEXS < mTotalCountryCount) {
                            mCountryPageIndex++;
                            updateUiCoutrySelect();
                            viewholder_autotune.button_cha_country_2.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_choosecountry_france:
                        if ((mCountryPageIndex + 1) * ONEPINDEXS < mTotalCountryCount) {
                            mCountryPageIndex++;
                            updateUiCoutrySelect();
                            viewholder_autotune.button_cha_country_3.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_operator_6:
                        if ((mOperatorPageIndex + 1) * ONEPINDEXS < mTotalOperatorCount) {
                            mOperatorPageIndex++;
                            updateUiOperatorSelect();
                            viewholder_autotune.button_cha_operator_0.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_operator_7:
                        if ((mOperatorPageIndex + 1) * ONEPINDEXS < mTotalOperatorCount) {
                            mOperatorPageIndex++;
                            updateUiOperatorSelect();
                            viewholder_autotune.button_cha_operator_1.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_operator_8:
                        if ((mOperatorPageIndex + 1) * ONEPINDEXS < mTotalOperatorCount) {
                            mOperatorPageIndex++;
                            updateUiOperatorSelect();
                            viewholder_autotune.button_cha_operator_2.requestFocus();
                        }
                        return true;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_cha_autotuning_tuningtype:
                        if (false == Utility.isSupportATV()) {
                            break;
                        }
                        if (mTuningType == TvChannelManager.TV_SCAN_ATV) {
                            mTuningType = TvChannelManager.TV_SCAN_ALL;
                        } else {
                            mTuningType--;
                        }
                        viewholder_autotune.text_cha_autotuning_tuningtype_val
                                .setText(mOptionTuningType[mTuningType]);
                        mTvChannelManager.setUserScanType(mTuningType);
                        return true;
                    case R.id.linearlayout_cha_autotuning_antenna_type:
                        if (mAntennaType == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                            mAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_CABLE;
                        } else {
                            mAntennaType--;
                        }
                        viewholder_autotune.text_cha_autotuning_antenna_val
                                .setText(mOptionScanAntennaTypes[mAntennaType]);
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            TvAtscChannelManager.getInstance().setDtvAntennaType(mAntennaType);
                        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            TvIsdbChannelManager.getInstance().setDtvAntennaType(mAntennaType);
                        }
                        return true;
                    case R.id.button_cha_autotuning_choosecountry_australia:
                        if (mCountryPageIndex > 0) {
                            mCountryPageIndex--;
                            updateUiCoutrySelect();
                            viewholder_autotune.button_cha_country_7.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_choosecountry_austria:
                        if (mCountryPageIndex > 0) {
                            mCountryPageIndex--;
                            updateUiCoutrySelect();
                            viewholder_autotune.button_cha_country_8.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_choosecountry_beligum:
                        if (mCountryPageIndex > 0) {
                            mCountryPageIndex--;
                            updateUiCoutrySelect();
                            viewholder_autotune.button_cha_country_9.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_operator_0:
                        if (mOperatorPageIndex > 0) {
                            mOperatorPageIndex--;
                            updateUiOperatorSelect();
                            viewholder_autotune.button_cha_operator_6.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_operator_1:
                        if (mOperatorPageIndex > 0) {
                            mOperatorPageIndex--;
                            updateUiOperatorSelect();
                            viewholder_autotune.button_cha_operator_7.requestFocus();
                        }
                        return true;
                    case R.id.button_cha_autotuning_operator_2:
                        if (mOperatorPageIndex > 0) {
                            mOperatorPageIndex--;
                            updateUiOperatorSelect();
                            viewholder_autotune.button_cha_operator_8.requestFocus();
                        }
                        return true;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (R.id.linearlayout_cha_autotuning_tuningtype == currentid) {
                    if (View.VISIBLE == viewholder_autotune.relative_country_choose.getVisibility()) {
                        focusLastTuneCountry(mCountryIndex);
                        return true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                Intent intent = new Intent(AutoTuneOptionActivity.this, TunerSetupActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                finish();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerListeners() {
        viewholder_autotune.button_cha_country_1.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_2.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_3.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_4.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_5.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_6.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_7.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_8.setOnClickListener(countryListener);
        viewholder_autotune.button_cha_country_9.setOnClickListener(countryListener);

        viewholder_autotune.button_cha_operator_0.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_1.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_2.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_3.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_4.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_5.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_6.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_7.setOnClickListener(operatorListener);
        viewholder_autotune.button_cha_operator_8.setOnClickListener(operatorListener);

        viewholder_autotune.button_search.setOnClickListener(listenerSearchBtn);
    }

    private OnClickListener listenerSearchBtn = new OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean showDvbsUI = false;
            Intent intent = new Intent();
            if ((TvChannelManager.TV_ROUTE_DVBS == mCurrntRoute)
                || (TvChannelManager.TV_ROUTE_DVBS2 == mCurrntRoute)) {
                if ((TvChannelManager.TV_SCAN_ALL == mTuningType)
                    || (TvChannelManager.TV_SCAN_DTV == mTuningType)) {
                    showDvbsUI = true;
                }
            }
            if (true == showDvbsUI) {
                intent.setAction(TvIntent.ACTION_DVBSDTV_AUTOTUNING_OPTION);
            } else {
                intent.setClass(AutoTuneOptionActivity.this, ChannelTuning.class);
            }
            startActivity(intent);
            finish();
        }
    };

    private OnClickListener countryListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.button_cha_autotuning_choosecountry_australia:
                    countrySelected(0);
                    break;
                case R.id.button_cha_autotuning_choosecountry_austria:
                    countrySelected(1);
                    break;
                case R.id.button_cha_autotuning_choosecountry_beligum:
                    countrySelected(2);
                    break;
                case R.id.button_cha_autotuning_choosecountry_bulgaral:
                    countrySelected(3);
                    break;
                case R.id.button_cha_autotuning_choosecountry_croatia:
                    countrySelected(4);
                    break;
                case R.id.button_cha_autotuning_choosecountry_czech:
                    countrySelected(5);
                    break;
                case R.id.button_cha_autotuning_choosecountry_denmark:
                    countrySelected(6);
                    break;
                case R.id.button_cha_autotuning_choosecountry_finland:
                    countrySelected(7);
                    break;
                case R.id.button_cha_autotuning_choosecountry_france:
                    countrySelected(8);
                    break;
                default:
                    break;
            }
        }
    };

    private OnClickListener operatorListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_cha_autotuning_operator_0:
                    operatorSelected(0);
                    break;
                case R.id.button_cha_autotuning_operator_1:
                    operatorSelected(1);
                    break;
                case R.id.button_cha_autotuning_operator_2:
                    operatorSelected(2);
                    break;
                case R.id.button_cha_autotuning_operator_3:
                    operatorSelected(3);
                    break;
                case R.id.button_cha_autotuning_operator_4:
                    operatorSelected(4);
                    break;
                case R.id.button_cha_autotuning_operator_5:
                    operatorSelected(5);
                    break;
                case R.id.button_cha_autotuning_operator_6:
                    operatorSelected(6);
                    break;
                case R.id.button_cha_autotuning_operator_7:
                    operatorSelected(7);
                    break;
                case R.id.button_cha_autotuning_operator_8:
                    operatorSelected(8);
                    break;
                default:
                    break;
            }
        }
    };

    private void setSystemTimeZone(int country) {
        // set time zone according country selection
        String timeZoneString = TvCountry.countryToTimeZone(country);
        Time time = new Time();
        time.setToNow();
        if (timeZoneString.compareTo(time.timezone) == 0) {
            return;
        }
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setTimeZone(timeZoneString);
    }

    private void countrySelected(int index) {
        int countryIndex = mCountryPageIndex * ONEPINDEXS + index;
        if (countryIndex >= TvCountry.COUNTRY_NUM) {
            Log.e(TAG, "countrySelected: countryIndex >= TvCountry.COUNTRY_NUM!!");
            return;
        }
        mCountryIndex = countryIndex;
        SharedPreferences setting = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(Constant.TUNING_COUNTRY, mCountryIndex);
        editor.commit();
        if (TvChannelManager.TV_ROUTE_DVBC == mCurrentRoute
                && TvChannelManager.TV_SCAN_ATV != mTvChannelManager.getUserScanType()) {
            if (TvCountry.CHINA == mCountryIndex) {
                if (TvChannelManager.CABLE_OPERATOR_OTHER != mDvbcOperator) {
                    mDvbcOperator = TvChannelManager.CABLE_OPERATOR_OTHER;
                    TvChannelManager.getInstance().setCableOperator(mDvbcOperator, true);
                    setting = getSharedPreferences(Constant.SAVE_SETTING_SELECT,
                            Context.MODE_PRIVATE);
                    editor = setting.edit();
                    editor.putInt(Constant.PREFERENCES_DVBC_OPERATOR, mDvbcOperator);
                    editor.commit();
                }
                Intent intent = new Intent();
                intent.setClass(AutoTuneOptionActivity.this, ChooseCityForAutoTuneActivity.class);
                startActivity(intent);
                finish();
            } else {
                // FIXME: Modify after tvclient api getOperatorByCountry is
                // ready
                mOperatorList = getOperatorByCountry(mCountryIndex);
                mTotalOperatorCount = mOperatorList.size();
                if (mTotalOperatorCount > 0) {
                    updateUiOperatorSelect();
                    viewholder_autotune.relative_country_choose.setVisibility(View.GONE);
                    viewholder_autotune.relative_operator_choose.setVisibility(View.VISIBLE);
                    viewholder_autotune.button_cha_operator_0.requestFocus();
                } else {
                    if (TvChannelManager.CABLE_OPERATOR_OTHER != mDvbcOperator) {
                        mDvbcOperator = TvChannelManager.CABLE_OPERATOR_OTHER;
                        TvChannelManager.getInstance().setCableOperator(mDvbcOperator, true);
                        setting = getSharedPreferences(Constant.SAVE_SETTING_SELECT,
                                Context.MODE_PRIVATE);
                        editor = setting.edit();
                        editor.putInt(Constant.PREFERENCES_DVBC_OPERATOR, mDvbcOperator);
                        editor.commit();
                    }
                    Intent intent = new Intent();
                    intent.setClass(AutoTuneOptionActivity.this, DtvAutoTuneOptionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } else {
            mTvChannelManager.setSystemCountryId(mCountryIndex);
            setSystemTimeZone(mCountryIndex);
            Intent intent = new Intent();
            intent.setClass(AutoTuneOptionActivity.this, ChannelTuning.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateUiCoutrySelect() {
        int index = mCountryPageIndex * ONEPINDEXS;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_1.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_1.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_1.setText("");
            viewholder_autotune.button_cha_country_1.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_2.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_2.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_2.setText("");
            viewholder_autotune.button_cha_country_2.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_3.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_3.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_3.setText("");
            viewholder_autotune.button_cha_country_3.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_4.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_4.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_4.setText("");
            viewholder_autotune.button_cha_country_4.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_5.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_5.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_5.setText("");
            viewholder_autotune.button_cha_country_5.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_6.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_6.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_6.setText("");
            viewholder_autotune.button_cha_country_6.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_7.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_7.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_7.setText("");
            viewholder_autotune.button_cha_country_7.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_8.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_8.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_8.setText("");
            viewholder_autotune.button_cha_country_8.setFocusable(false);
        }
        index++;
        if (index < mTotalCountryCount) {
            viewholder_autotune.button_cha_country_9.setText(mOptionCountry[index]);
            viewholder_autotune.button_cha_country_9.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_country_9.setText("");
            viewholder_autotune.button_cha_country_9.setFocusable(false);
        }
    }

    private void operatorSelected(int index) {
        int operatorIndex = mOperatorPageIndex * ONEPINDEXS + index;
        if (operatorIndex >= mTotalOperatorCount) {
            return;
        }

        int operator = mOperatorList.get(operatorIndex);
        Log.d(TAG, "operatorSelected: operator = " + operator);
        if (operator != mDvbcOperator) {
            mDvbcOperator = operator;
            TvChannelManager.getInstance().setCableOperator(mDvbcOperator, true);
            SharedPreferences setting = getSharedPreferences(Constant.SAVE_SETTING_SELECT,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.putInt(Constant.PREFERENCES_DVBC_OPERATOR, mDvbcOperator);
            editor.commit();
        }

        Intent intent = new Intent();
        intent.setClass(AutoTuneOptionActivity.this, DtvAutoTuneOptionActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUiOperatorSelect() {
        int index = mOperatorPageIndex * ONEPINDEXS;
        if (null == mOperatorList) {
            return;
        }

        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_0.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_0.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_0.setText("");
            viewholder_autotune.button_cha_operator_0.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_1.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_1.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_1.setText("");
            viewholder_autotune.button_cha_operator_1.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_2.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_2.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_2.setText("");
            viewholder_autotune.button_cha_operator_2.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_3.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_3.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_3.setText("");
            viewholder_autotune.button_cha_operator_3.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_4.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_4.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_4.setText("");
            viewholder_autotune.button_cha_operator_4.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_5.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_5.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_5.setText("");
            viewholder_autotune.button_cha_operator_5.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_6.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_6.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_6.setText("");
            viewholder_autotune.button_cha_operator_6.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_7.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_7.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_7.setText("");
            viewholder_autotune.button_cha_operator_7.setFocusable(false);
        }
        index++;
        if (index < mTotalOperatorCount) {
            viewholder_autotune.button_cha_operator_8.setText(mDvbcOperatorNameArray[mOperatorList
                    .get(index)]);
            viewholder_autotune.button_cha_operator_8.setFocusable(true);
        } else {
            viewholder_autotune.button_cha_operator_8.setText("");
            viewholder_autotune.button_cha_operator_8.setFocusable(false);
        }
    }

    // FIXME: Remove after tvclient api getOperatorByCountry is ready
    private ArrayList<Integer> getOperatorByCountry(int country) {
        ArrayList<Integer> operatorList = new ArrayList<Integer>();
        switch (country) {
            case TvCountry.AUSTRIA:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_UPC);
                break;
            case TvCountry.DENMARK:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDSMATV);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDCABLE);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_YOUSEE);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_STOFA);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.FINLAND:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDSMATV);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDCABLE);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CABLEREADY);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.HUNGARY:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_UPC);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.IRELAND:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_UPC);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.NETHERLANDS:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_UPC);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_ZIGGO);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.NORWAY:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDSMATV);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDCABLE);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.POLAND:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_UPC);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.RUMANIA:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_UPC);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.SWEDEN:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDSMATV);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_CDCABLE);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_COMHEM);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            case TvCountry.SWITZERLAND:
                operatorList.add(TvChannelManager.CABLE_OPERATOR_UPC);
                operatorList.add(TvChannelManager.CABLE_OPERATOR_OTHER);
                break;
            default:
                break;
        }
        return operatorList;
    }
}
