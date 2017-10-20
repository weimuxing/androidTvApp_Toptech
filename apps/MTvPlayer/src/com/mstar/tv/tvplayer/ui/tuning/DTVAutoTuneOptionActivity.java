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

package com.mstar.tv.tvplayer.ui.tuning;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;
import android.app.AlertDialog;
import android.app.AlarmManager;
import android.text.format.Time;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvChannelManager.DvbcScanParam;
import com.mstar.android.tv.TvCountry;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.MyButton;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;

public class DTVAutoTuneOptionActivity extends MstarBaseActivity {

    private static final String TAG = "DTVAutoTuneOptionActivity";

    private static final int FREQUENCY_MAX = 1000;

    private static final int SYMBOL_MAX = 10000;

    private static final int NETWORKID_MAX = 65535;

    private static final int INVALID_NETWORKID = 0;

    private static final int DEFAULT_SYMBOL_VALUE = 5957;

    private int mDvbcFrequency;

    private short mDvbcSymbol;

    private int mDvbcNetworkId;

    private String mStrFrequency = new String();

    private String mStrSymbol = new String();

    private String mStrNetworkId = new String();

    private ComboButton mComboBtnTitle;

    private ComboButton mComboBtnFrequency;

    private ComboButton mComboBtnModulation;

    private ComboButton mComboBtnSymbol;

    private ComboButton mComboBtnNetworkId;

    private ComboButton mComboBtnSignalStrength;

    private ComboButton mComboBtnSignalQuality;

    private MyButton mBtnSearch;

    private int mDvbcOperator = TvChannelManager.CABLE_OPERATOR_OTHER;

    private boolean mIsFrequencyAuto = true;

    private boolean mIsModulationAuto = true;

    private boolean mIsSymbolAuto = true;

    private boolean mIsNetworkIdAuto = true;

    private boolean mIsNetworkIdChanged = false;

    private boolean mIsPreviousFrequencyAuto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtv_auto_tune_option);
        SharedPreferences setting = getSharedPreferences(Constant.SAVE_SETTING_SELECT,
                Context.MODE_PRIVATE);
        mDvbcOperator = setting.getInt(Constant.PREFERENCES_DVBC_OPERATOR, TvChannelManager
                .getInstance().getCableOperator());
        Log.d(TAG, "onCreate: operator = " + mDvbcOperator);
        initUIComponents();
        loadDataToUI();
    }

    private void initUIComponents() {
        int currentDtvRouteIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
        final int dvbcDtvRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(
                TvChannelManager.TV_ROUTE_DVBC);
        if (dvbcDtvRouteIndex == currentDtvRouteIndex) {
            mComboBtnTitle = new ComboButton(this, getResources().getStringArray(
                    R.array.strs_cha_dtvautotuning_dvbc_tuning_mode),
                    R.id.linearlayout_cha_dtvautotuning_choose, 1, 2, ComboButton.DIRECT_SWITCH) {
                @Override
                public void doUpdate() {
                    ImageView frequencyImage = (ImageView) findViewById(R.id.imageview_cha_dtvautotuning_button_tips_frequency);
                    TextView frequencyText = (TextView) findViewById(R.id.textview_cha_dtvautotuning_button_tips_frequency);
                    if (0 == mComboBtnTitle.getIdx()) {
                        mIsPreviousFrequencyAuto = mIsFrequencyAuto;
                        if (!mIsFrequencyAuto) {
                            mIsFrequencyAuto = true;
                            mComboBtnFrequency.setTextInChild(2, getResources().getString(R.string.str_cha_dtvautotuning_auto));
                            mComboBtnFrequency.setEnable(false);
                        }
                        frequencyImage.setVisibility(View.GONE);
                        frequencyText.setVisibility(View.GONE);
                    } else {
                        if (!mIsPreviousFrequencyAuto) {
                            mIsFrequencyAuto = false;
                            mComboBtnFrequency.setTextInChild(2, Integer.toString(mDvbcFrequency));
                            mComboBtnFrequency.setEnable(true);
                        }
                        frequencyImage.setVisibility(View.VISIBLE);
                        frequencyText.setVisibility(View.VISIBLE);
                    }
                }
            };
            // FIXME: Do not hard-coding
            mComboBtnTitle.setIdx(1);
        } else {
            // TODO: to confirm tv route other than DVBC will not launch this activity
            mComboBtnTitle = new ComboButton(this, getResources().getStringArray(
                    R.array.strs_cha_dtvautotuning_dvbc_tuning_mode),
                    R.id.linearlayout_cha_dtvautotuning_choose, 1, 2, ComboButton.DIRECT_SWITCH) {
                @Override
                public void doUpdate() {
                    if (mComboBtnTitle.getIdx() == 1) {
                        setBelowTextEnable(true);
                    } else {
                        setBelowTextEnable(false);
                    }
                }
            };
        }

        mComboBtnFrequency = new ComboButton(this, null, R.id.linearlayout_cha_dtvautotuning_fre,
                1, 2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                if (mComboBtnFrequency.getIdx() < 0) {
                    mComboBtnFrequency.setIdx(FREQUENCY_MAX);
                }
                if (mComboBtnFrequency.getIdx() > FREQUENCY_MAX) {
                    mComboBtnFrequency.setIdx(0);
                }
            }
        };

        mComboBtnModulation = new ComboButton(this, getResources().getStringArray(
                R.array.strs_cha_dtvautotuning_demodulation_tbl),
                R.id.linearlayout_cha_dtvautotuning_mod, 1, 2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
            }
        };

        mComboBtnSymbol = new ComboButton(this, null, R.id.linearlayout_cha_dtvautotuning_sym, 1,
                2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                if (mComboBtnSymbol.getIdx() < 0) {
                    mComboBtnSymbol.setIdx(SYMBOL_MAX);
                }
                if (mComboBtnSymbol.getIdx() > SYMBOL_MAX) {
                    mComboBtnSymbol.setIdx(0);
                }
            }
        };

        mComboBtnNetworkId = new ComboButton(this, null,
                R.id.linearlayout_cha_dtvautotuning_networkid, 1, 2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                if (mComboBtnNetworkId.getIntIndex() < 0) {
                    mComboBtnNetworkId.setIdx(NETWORKID_MAX);
                }
                if (mComboBtnNetworkId.getIntIndex() > NETWORKID_MAX) {
                    mComboBtnNetworkId.setIdx(0);
                }
                mIsNetworkIdChanged = true;
            }
        };

        mComboBtnSignalStrength = new ComboButton(this, null,
                R.id.linearlayout_cha_dtvautotuning_len, 1, 2, false) {
            @Override
            public void doUpdate() {
            }
        };

        mComboBtnSignalQuality = new ComboButton(this, null,
                R.id.linearlayout_cha_dtvautotuning_qual, 1, 2, false) {
            @Override
            public void doUpdate() {
            }
        };

        mBtnSearch = new MyButton(this, R.id.linearlayout_cha_startscan);
        mBtnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!mIsNetworkIdAuto && 0 == mComboBtnNetworkId.getIdx() && TvChannelManager.CABLE_OPERATOR_STOFA != mDvbcOperator)
                        || (true == mIsNetworkIdAuto && TvChannelManager.CABLE_OPERATOR_UPC == mDvbcOperator)) {
                    // show invalid network id message
                    Log.d(TAG, "pop up network invalid...");
                    AlertDialog.Builder build = new AlertDialog.Builder(
                            DTVAutoTuneOptionActivity.this);
                    build.setMessage(R.string.str_cha_dtvautotuning_networkid_invalid);
                    build.setPositiveButton(R.string.str_cha_dtvautotuning_ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    build.create().show();
                    return;
                }
                int frequency;
                if (true == mIsFrequencyAuto) {
                    frequency = 0;
                } else {
                    frequency = mComboBtnFrequency.getIdx() * 1000;
                }
                int modulation;
                if (true == mIsModulationAuto) {
                    modulation = DvbcScanParam.DVBC_CAB_TYPE_INVALID;
                } else {
                    modulation = mComboBtnModulation.getIdx();
                }
                int networkId;
                if (true == mIsNetworkIdAuto) {
                    networkId = INVALID_NETWORKID;
                } else {
                    networkId = mComboBtnNetworkId.getIntIndex();
                    if (true == mIsNetworkIdChanged) {
                        TvDvbChannelManager.getInstance().setSystemNetworkId(networkId);
                    }
                }
                int scanType;
                if (0 == mComboBtnTitle.getIdx()) {
                    scanType = TvChannelManager.DVBC_FULL_SCAN;
                } else {
                    scanType = TvChannelManager.DVBC_QUICK_SCAN;
                }
                Log.d(TAG, "frequency = " + frequency + ", modulation = " + modulation
                        + ", symbol = " + mComboBtnSymbol.getIdx() + ", networkId = " + networkId
                        + ", scanType = " + scanType);
                SharedPreferences setting = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                        Context.MODE_PRIVATE);
                int country = setting.getInt(Constant.TUNING_COUNTRY, 0);
                TvChannelManager.getInstance().setSystemCountryId(country);
                setSystemTimeZone(country);
                Intent intent = new Intent();
                intent.setAction(TvIntent.ACTION_CHANNEL_TUNING);
                intent.putExtra("scanType", scanType);
                intent.putExtra("cableOperator", mDvbcOperator);
                intent.putExtra("symbol", (int)(mComboBtnSymbol.getIdx()));
                intent.putExtra("modulation", modulation);
                intent.putExtra("frequency", frequency);
                intent.putExtra("networkId", networkId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadDataToUI() {
        mDvbcFrequency = 0;
        mIsFrequencyAuto = true;
        mComboBtnFrequency.setTextInChild(2, getResources().getString(R.string.str_cha_dtvautotuning_auto));
        mComboBtnFrequency.setEnable(false);

        if (true == mIsModulationAuto) {
            mComboBtnModulation.setTextInChild(2, getResources().getString(R.string.str_cha_dtvautotuning_auto));
            mComboBtnModulation.setEnable(false);
        } else {
            mComboBtnModulation.setIdx(0);
            mComboBtnModulation.setEnable(true);
        }

        if (true == mIsSymbolAuto) {
            mDvbcSymbol = 0;
            mComboBtnSymbol.setTextInChild(2, getResources().getString(R.string.str_cha_dtvautotuning_auto));
            mComboBtnSymbol.setEnable(false);
        } else {
            mDvbcSymbol = DEFAULT_SYMBOL_VALUE;
            mComboBtnSymbol.setIdx(DEFAULT_SYMBOL_VALUE);
            mComboBtnSymbol.setEnable(true);
        }

        mDvbcNetworkId = INVALID_NETWORKID;
        int defaultNetworkId = TvDvbChannelManager.getInstance().getDefaultNetworkId();
        if (TvChannelManager.CABLE_OPERATOR_STOFA == mDvbcOperator
                || INVALID_NETWORKID != defaultNetworkId) {
            mDvbcNetworkId = defaultNetworkId;
            mIsNetworkIdAuto = false;
            mComboBtnNetworkId.setIdx(mDvbcNetworkId);
            mComboBtnNetworkId.setEnable(true);
        } else {
            mDvbcNetworkId = TvDvbChannelManager.getInstance().getSystemNetworkId();
            mComboBtnNetworkId.setTextInChild(2, Integer.toString(mDvbcNetworkId));
            mIsNetworkIdAuto = true;
            mComboBtnNetworkId.setEnable(false);
        }

        switch (mDvbcOperator) {
            case TvChannelManager.CABLE_OPERATOR_CDSMATV:
            case TvChannelManager.CABLE_OPERATOR_CABLEREADY:
            case TvChannelManager.CABLE_OPERATOR_OTHER:
                mComboBtnModulation.setVisibility(true);
                mComboBtnSymbol.setVisibility(true);
                mComboBtnNetworkId.setVisibility(true);
                break;
            default:
                mComboBtnModulation.setVisibility(true);
                mComboBtnSymbol.setVisibility(true);
                mComboBtnNetworkId.setVisibility(true);
                break;
        }
        mComboBtnSignalStrength.setVisibility(false);
        mComboBtnSignalQuality.setVisibility(false);
    }

    private void setBelowTextEnable(boolean b) {
        mComboBtnFrequency.setEnable(b);
        mComboBtnModulation.setEnable(b);
        mComboBtnSymbol.setEnable(b);
        mComboBtnNetworkId.setEnable(b);

        int textColor = b ? getResources().getColor(R.color.enable_text_color) : getResources()
                .getColor(R.color.disable_text_color);
        ((TextView) mComboBtnFrequency.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) mComboBtnModulation.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) mComboBtnSymbol.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) mComboBtnNetworkId.getLayout().getChildAt(1)).setTextColor(textColor);
    }

    private void setStrengthAndQualityTextEnable(boolean b) {
        mComboBtnSignalQuality.setEnable(b);
        mComboBtnSignalStrength.setEnable(b);

        int textColor = b ? getResources().getColor(R.color.enable_text_color) : getResources()
                .getColor(R.color.disable_text_color);
        ((TextView) mComboBtnSignalQuality.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) mComboBtnSignalStrength.getLayout().getChildAt(1)).setTextColor(textColor);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int currentid = getCurrentFocus().getId();
        switch (keyCode) {
            case KeyEvent.KEYCODE_PROG_BLUE:
                // frequency
                if (0 == mComboBtnTitle.getIdx()) {
                    return true;
                }
                if (true == mIsFrequencyAuto) {
                    mIsFrequencyAuto = false;
                    mComboBtnFrequency.setIdx(0);
                    mComboBtnFrequency.setEnable(true);
                    mStrFrequency = "";
                } else {
                    mIsFrequencyAuto = true;
                    mComboBtnFrequency.setTextInChild(2, getResources().getString(R.string.str_cha_dtvautotuning_auto));
                    mComboBtnFrequency.setEnable(false);
                }
                return true;
            case KeyEvent.KEYCODE_PROG_RED:
                // modulation
                if (true == mIsModulationAuto) {
                    mIsModulationAuto = false;
                    mComboBtnModulation.setIdx(0);
                    mComboBtnModulation.setEnable(true);
                } else {
                    mIsModulationAuto = true;
                    mComboBtnModulation.setTextInChild(2, getResources().getString(R.string.str_cha_dtvautotuning_auto));
                    mComboBtnModulation.setEnable(false);
                }
                return true;
            case KeyEvent.KEYCODE_PROG_GREEN:
                // symbol
                if (true == mIsSymbolAuto) {
                    mIsSymbolAuto = false;
                    mComboBtnSymbol.setIdx(0);
                    mComboBtnSymbol.setEnable(true);
                    mStrSymbol = "";
                } else {
                    mIsSymbolAuto = true;
                    mComboBtnSymbol.setTextInChild(2, getResources().getString(R.string.str_cha_dtvautotuning_auto));
                    mComboBtnSymbol.setEnable(false);
                }
                return true;
            case KeyEvent.KEYCODE_PROG_YELLOW:
                // network id
                if (true == mIsNetworkIdAuto) {
                    mIsNetworkIdAuto = false;
                    mComboBtnNetworkId.setIdx(mDvbcNetworkId);
                    mComboBtnNetworkId.setEnable(true);
                } else {
                    mIsNetworkIdAuto = true;
                    mComboBtnNetworkId.setEnable(false);
                }
                return true;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_9:
                switch (currentid) {
                    case R.id.linearlayout_cha_dtvautotuning_fre:
                        inputFrequencyNumber(keyCode - KeyEvent.KEYCODE_0);
                        break;
                    case R.id.linearlayout_cha_dtvautotuning_sym:
                        inputSymbolNumber(keyCode - KeyEvent.KEYCODE_0);
                        break;
                    case R.id.linearlayout_cha_dtvautotuning_networkid:
                        inputNetworkIdNumber(keyCode - KeyEvent.KEYCODE_0);
                        mIsNetworkIdChanged = true;
                        break;
                    default:
                        break;
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                if (!mIsNetworkIdAuto && true == mIsNetworkIdChanged) {
                    TvDvbChannelManager.getInstance().setSystemNetworkId(
                            mComboBtnNetworkId.getIntIndex());
                }
                Intent intent = new Intent(DTVAutoTuneOptionActivity.this,
                        AutoTuneOptionActivity.class);
                intent.putExtra(Constant.AUTO_TUNING_OPTION_FROM_SUBPAGE, true);
                startActivity(intent);
                finish();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void inputFrequencyNumber(int inputNumber) {
        int freq = 0;
        if (mStrFrequency.equals("0")) {
            mStrFrequency = "";
        }
        mStrFrequency = mStrFrequency + Integer.toString(inputNumber);
        try {
            freq = Integer.parseInt(mStrFrequency);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (mStrFrequency.length() > 4) {
            mStrFrequency = Integer.toString(inputNumber);
            mDvbcFrequency = inputNumber;
        } else if (freq > FREQUENCY_MAX) {
            mStrFrequency = Integer.toString(FREQUENCY_MAX);
            mDvbcFrequency = FREQUENCY_MAX;
        } else {
            mDvbcFrequency = freq;
        }
        mComboBtnFrequency.setIdx(mDvbcFrequency);
    }

    private void inputSymbolNumber(int inputNumber) {
        short symbol = 0;
        if (mStrSymbol.equals("0")) {
            mStrSymbol = "";
        }
        mStrSymbol = mStrSymbol + Integer.toString(inputNumber);
        try {
            symbol = (short) Integer.parseInt(mStrSymbol);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (mStrSymbol.length() > 5) {
            mStrSymbol = Integer.toString(inputNumber);
            mDvbcSymbol = (short) inputNumber;
        } else if (symbol > SYMBOL_MAX) {
            mStrSymbol = Integer.toString(SYMBOL_MAX);
            mDvbcSymbol = SYMBOL_MAX;
        } else {
            mDvbcSymbol = symbol;
        }
        mComboBtnSymbol.setIdx(mDvbcSymbol);
    }

    private void inputNetworkIdNumber(int inputNumber) {
        int networkId = 0;
        if (mStrNetworkId.equals("0")) {
            mStrNetworkId = "";
        }
        mStrNetworkId = mStrNetworkId + Integer.toString(inputNumber);
        try {
            networkId = Integer.parseInt(mStrNetworkId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (mStrNetworkId.length() > 5) {
            mStrNetworkId = Integer.toString(inputNumber);
            mDvbcNetworkId = inputNumber;
        } else if (networkId > NETWORKID_MAX) {
            mStrNetworkId = Integer.toString(NETWORKID_MAX);
            mDvbcNetworkId = NETWORKID_MAX;
        } else {
            mDvbcNetworkId = networkId;
        }
        mComboBtnNetworkId.setIdx(mDvbcNetworkId);
    }

    private void setSystemTimeZone(int country) {
        String timeZoneString = TvCountry.countryToTimeZone(country);
        Time time = new Time();
        time.setToNow();
        if (timeZoneString.compareTo(time.timezone) == 0) {
            return;
        }
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setTimeZone(timeZoneString);
    }
}
