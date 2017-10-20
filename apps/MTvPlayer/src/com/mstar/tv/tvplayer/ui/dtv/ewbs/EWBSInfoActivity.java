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
//        of any kind whatsoever, or any information;or
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

package com.mstar.tv.tvplayer.ui.dtv.ewbs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.dtv.dvb.isdb.vo.EwbsInfo;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.dtv.ewbs.AreaCodeObject;
import com.mstar.tv.tvplayer.ui.dtv.ewbs.AreaNameList;
import com.mstar.tv.tvplayer.ui.dtv.ewbs.EWBSNameSelectingDialog;
import com.mstar.tv.tvplayer.ui.dtv.ewbs.EWBSNameSelectingDialog.OnAreaCodeSelectedListener;
import com.mstar.tv.tvplayer.ui.tuning.AutoTuneOptionActivity;
import com.mstar.tv.tvplayer.ui.tuning.ChannelTuning;
import com.mstar.tv.tvplayer.ui.tuning.DTVManualTuning;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;

import java.util.ArrayList;

public class EWBSInfoActivity extends MstarBaseActivity {
    private static final String TAG = "EWBSInfoActivity";

    private final int IDX_ON = 1;

    private final int LAUNCH_MODE_NORMAL = 0;

    private final int LAUNCH_MODE_FOR_AUTO_TUNING = 1;

    private final int LAUNCH_MODE_FOR_MANUAL_TUNING = 2;

    private final AreaNameList mAreaNameList = new AreaNameList();

    private int mLaunchMode = LAUNCH_MODE_NORMAL;

    private LinearLayout mLinearRoot = null;

    private LinearLayout mLinearAreaCode = null;

    private TextView mTextviewAreaCode = null;

    private ComboButton mComboAutoStartupScreen = null;

    private OnAreaCodeSelectedListener mOnAreaCodeSelectedListener = null;

    private OnFocusChangeListener mFocuschangesListener = null;

    private Dialog mDigitAndNameDialog = null;

    private AlertDialog mDigitInputDialog = null;

    private ArrayList<EWBSNameSelectingDialog> mNameSelDlgList = new ArrayList<EWBSNameSelectingDialog>();

    private int mNumOfDlg = 0;

    private TvChannelManager mTvChannelManager = null;

    private TvIsdbChannelManager mTvIsdbChannelManager = null;

    private int mCountryId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isForAutoTuning = getIntent().getBooleanExtra("forAutoTuning", false);
        getIntent().removeExtra("forAutoTuning");
        if (true == isForAutoTuning) {
            mLaunchMode = LAUNCH_MODE_FOR_AUTO_TUNING;
        }
        boolean isForManualTuning = getIntent().getBooleanExtra("forManualTuning", false);
        getIntent().removeExtra("forManualTuning");
        if (true == isForManualTuning) {
            mLaunchMode = LAUNCH_MODE_FOR_MANUAL_TUNING;
        }
        Log.d(TAG, "mLaunchMode = " + mLaunchMode);

        /* Load view */
        setContentView(R.layout.ewbs_info);
        mLinearRoot = (LinearLayout) findViewById(
                R.id.linearlayout_root);
        mLinearAreaCode = (LinearLayout) findViewById(
                R.id.linearlayout_cha_area_code);
        mTextviewAreaCode = (TextView) findViewById(
                R.id.textview_cha_area_code_val);

        mTvChannelManager = TvChannelManager.getInstance();
        mTvIsdbChannelManager = TvIsdbChannelManager.getInstance();
        mCountryId = mTvChannelManager.getSystemCountryId();

        updateDispDigitText();

        /* Add auto startup combo button */
        mComboAutoStartupScreen = new ComboButton(this, getResources()
                .getStringArray(R.array.str_arr_ewbs_auto_startup_screen_val),
                R.id.linearlayout_cha_auto_startup_screen, 1, 2, false) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                mTvIsdbChannelManager.setEWBSOpInfo(TvIsdbChannelManager.EWBS_GET_AUTOSTARTUP_FLAG,
                        (getIdx() == IDX_ON) ? TvIsdbChannelManager.EWBS_AUTOSTARTUP_ON : TvIsdbChannelManager.EWBS_AUTOSTARTUP_OFF);
            }
        };
        mComboAutoStartupScreen.setIdx((mTvIsdbChannelManager.getEWBSOpInfo(TvIsdbChannelManager.EWBS_GET_AUTOSTARTUP_FLAG).value == TvIsdbChannelManager.EWBS_AUTOSTARTUP_ON)
                ? TvIsdbChannelManager.EWBS_AUTOSTARTUP_ON : TvIsdbChannelManager.EWBS_AUTOSTARTUP_OFF);

        //FIXME: This item is currently disabled
        LinearLayout linear = (LinearLayout) findViewById(R.id.linearlayout_cha_auto_startup_screen);
        linear.setVisibility(View.GONE);

        /* Prepare area code name selection listener */
        mOnAreaCodeSelectedListener = new EWBSNameSelectingDialog.OnAreaCodeSelectedListener() {
            @Override
            public void onSelected(int code) {
                Log.d(TAG, "onSelected(), code = " + code);
                showNameSelectingDialog(code);
            }
        };

        /* Prepare and set focus change listener */
        mFocuschangesListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (true == hasFocus) {
                    /* Set the item to "selected" status to enable text scrolling */
                    LinearLayout container = (LinearLayout) v;
                    container.getChildAt(1).setSelected(true);
                }
            }
        };
        mLinearAreaCode.setOnFocusChangeListener(mFocuschangesListener);

        /* Prepare and set on click listener for list view */
        OnClickListener listenerAreaCode = new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDigitAndNameDialog();
            }
        };
        mLinearAreaCode.setOnClickListener(listenerAreaCode);

        if ((LAUNCH_MODE_FOR_AUTO_TUNING == mLaunchMode)
                || (LAUNCH_MODE_FOR_MANUAL_TUNING == mLaunchMode)) {
            mLinearRoot.setVisibility(View.INVISIBLE);
            showDigitAndNameDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        cancelDialog();
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                if (LAUNCH_MODE_FOR_AUTO_TUNING == mLaunchMode) {
                    startAutoTuneOptionActivity();
                } else if (LAUNCH_MODE_FOR_MANUAL_TUNING == mLaunchMode) {
                    startDtvManualTuningActivity();
                } else {
                    Intent intent = new Intent(TvIntent.MAINMENU);
                    intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                finish();
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void cancelDigitAndNameDialog() {
        if (null != mDigitAndNameDialog) {
            mDigitAndNameDialog.dismiss();
            mDigitAndNameDialog = null;
        }
    }

    private void cancelDigitInputDialog() {
        if (null != mDigitInputDialog) {
            mDigitInputDialog.dismiss();
            mDigitInputDialog = null;
        }
    }

    private void cancelNameSelectingDialog() {
        if (0 < mNumOfDlg) {
            for (int idx = (mNumOfDlg - 1); idx >= 0; idx--) {
                EWBSNameSelectingDialog ewbsNameSelectingDialog = mNameSelDlgList.get(idx);
                ewbsNameSelectingDialog.dismiss();
                mNameSelDlgList.remove(idx);
            }
        }
        mNumOfDlg = 0;
    }

    private void cancelDialog() {
        cancelDigitAndNameDialog();
        cancelDigitInputDialog();
        cancelNameSelectingDialog();
    }

    private void startAutoTuneOptionActivity() {
        Intent i = new Intent();
        i.setClass(EWBSInfoActivity.this, AutoTuneOptionActivity.class);
        startActivity(i);
    }

    private void startDtvManualTuningActivity() {
        Intent i = new Intent();
        i.setClass(EWBSInfoActivity.this, DTVManualTuning.class);
        startActivity(i);
    }

    private void showDigitAndNameDialog() {
        mDigitAndNameDialog = new Dialog(EWBSInfoActivity.this);
        mDigitAndNameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDigitAndNameDialog.setContentView(R.layout.ewbs_area_code_dialog);
        mDigitAndNameDialog.show();

        Button digitBtn = (Button) mDigitAndNameDialog.findViewById(R.id.btn_digit);
        digitBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Show digi input style dialog */
                showDigitInputDialog();
            }
        });

        Button nameBtn = (Button) mDigitAndNameDialog.findViewById(R.id.btn_name);
        nameBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Show area mane selection dialog */
                showNameSelectingDialog(TvIsdbChannelManager.EWBS_ROOT_PH_MENU);
            }
        });

        if ((LAUNCH_MODE_FOR_AUTO_TUNING == mLaunchMode)
                || (LAUNCH_MODE_FOR_MANUAL_TUNING == mLaunchMode)) {
            mDigitAndNameDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (LAUNCH_MODE_FOR_AUTO_TUNING == mLaunchMode) {
                        startAutoTuneOptionActivity();
                    }
                    if (LAUNCH_MODE_FOR_MANUAL_TUNING == mLaunchMode) {
                        startDtvManualTuningActivity();
                    }
                }
            });
        }
    }

    private void showDigitInputDialog() {
        class AreaCodeTextWatcher implements TextWatcher {
            private final int MAX_TEXT_LEN = 4;
            private AlertDialog alertDialog;

            public AreaCodeTextWatcher(AlertDialog d) {
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(EWBSInfoActivity.this);
        builder.setTitle(getResources().getString(R.string.str_ewbs_area_code));
        final View view = getLayoutInflater().inflate(
                R.layout.code_input_dialog_4_digits, null);
        EditText editText = (EditText) view.findViewById(R.id.input_dialog_edittext);
        builder.setView(view);
        builder.setPositiveButton(
                getResources().getString(R.string.str_ews_location_code_save),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText editText = (EditText) view
                                .findViewById(R.id.input_dialog_edittext);
                        /* update location code to SN */
                        mTvChannelManager.setUserLocationCode(Integer.valueOf(editText.getText().toString()));
                        saveSharedPreference();
                        dialog.cancel();
                        cancelDigitAndNameDialog();
                        updateDispDigitText();
                        if (LAUNCH_MODE_FOR_AUTO_TUNING == mLaunchMode) {
                            Intent intent = new Intent();
                            intent.setClass(EWBSInfoActivity.this, ChannelTuning.class);
                            startActivity(intent);
                            finish();
                        }
                        if (LAUNCH_MODE_FOR_MANUAL_TUNING == mLaunchMode) {
                            startDtvManualTuningActivity();
                        }
                    }
                });
        builder.setNegativeButton(
                getResources().getString(R.string.str_ews_location_code_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        mDigitInputDialog = builder.create();
        mDigitInputDialog.getWindow().setLayout(view.getWidth(), view.getHeight());
        editText.addTextChangedListener(new AreaCodeTextWatcher(mDigitInputDialog));
        mDigitInputDialog.show();

        /* set init value for digit input */
        editText.setText("" + mTvChannelManager.getUserLocationCode());
        editText.setSelection(editText.getText().length());


        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
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

    private void showNameSelectingDialog(final int areaCode) {
        final EwbsInfo ewbsInfo = mTvIsdbChannelManager.getEWBSInfo(areaCode);
        if (TvIsdbChannelManager.EWBS_ERR_OK == ewbsInfo.returnCode) {
            /* Show list */
            String titleStr = "";
            ArrayList<AreaCodeObject> itemList = new ArrayList<AreaCodeObject>();
            final int listSize = ewbsInfo.dataLen;
            Log.d(TAG, "listSize = " + listSize);
            titleStr = getTitleName(ewbsInfo.titleType);
            for (int idx = 0; idx < listSize; idx++) {
                final int code = ewbsInfo.codeList[idx];
                Log.d(TAG, "Code = " + code);
                AreaCodeObject obj;
                obj = new AreaCodeObject(code, getAreaName(code));
                itemList.add(obj);
            }

            EWBSNameSelectingDialog ewbsNameSelectingDialog = null;
            ewbsNameSelectingDialog = new EWBSNameSelectingDialog(EWBSInfoActivity.this, R.style.listmenuDialg, titleStr, itemList);
            ewbsNameSelectingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.d(TAG, "onCancel!!");
                    removeOneDlg();
                }
            });
            ewbsNameSelectingDialog.registerOnAreaCodeSelectedListener(mOnAreaCodeSelectedListener);
            ewbsNameSelectingDialog.show();
            mNameSelDlgList.add(ewbsNameSelectingDialog);
            mNumOfDlg++;
        } else if (TvIsdbChannelManager.EWBS_ERR_NO_NEXT_SECTION == ewbsInfo.returnCode) {
            mTvIsdbChannelManager.setEWBSOpInfo(TvIsdbChannelManager.EWBS_OP_GET_AREA_CODE, areaCode);
            saveSharedPreference();
            updateDispDigitText();
            cancelDialog();
            if (LAUNCH_MODE_FOR_AUTO_TUNING == mLaunchMode) {
                Intent intent = new Intent();
                intent.setClass(EWBSInfoActivity.this, ChannelTuning.class);
                startActivity(intent);
                finish();
            }
            if (LAUNCH_MODE_FOR_MANUAL_TUNING == mLaunchMode) {
                startDtvManualTuningActivity();
                finish();
            }
        } else if (TvIsdbChannelManager.EWBS_ERR_NOT_SUPPORT_ENUM == ewbsInfo.returnCode) {
            Log.w(TAG, "not support code!");
        } else if (TvIsdbChannelManager.EWBS_ERR_UNKOWN_ERROR == ewbsInfo.returnCode) {
            Log.w(TAG, "unknown error!");
        }
    }

    private void removeOneDlg() {
        Log.d(TAG, "removeOneDlg(), before mNumOfDlg = " + mNumOfDlg);
        EWBSNameSelectingDialog ewbsNameSelectingDialog = mNameSelDlgList.get(mNumOfDlg - 1);
        ewbsNameSelectingDialog.dismiss();
        mNameSelDlgList.remove(mNumOfDlg - 1);
        mNumOfDlg--;
        Log.d(TAG, "removeOneDlg(), after mNumOfDlg = " + mNumOfDlg);
    }

    private void updateDispDigitText() {
        SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
        if (true == settings.getBoolean(Constant.PREFERENCES_IS_EWBS_AREA_CODE_SELECTED, false)) {
            if (TvCountry.PHILIPPINES == mCountryId) {
                final int areaCode = mTvIsdbChannelManager.getEWBSOpInfo(TvIsdbChannelManager.EWBS_OP_GET_AREA_CODE).value;
                if (0 == areaCode) {
                    mTextviewAreaCode.setText("" + mTvChannelManager.getUserLocationCode());
                } else {
                    mTextviewAreaCode.setText(getAreaName(areaCode));
                }
            } else {
                mTextviewAreaCode.setText("" + mTvChannelManager.getUserLocationCode());
            }
        } else {
            mTextviewAreaCode.setText(getResources().getString(R.string.str_ewbs_area_code_undefined));
        }
    }

    private String getAreaName(final int areaCode) {
        String codeText = "";
        boolean noData = true;

        final int resId = mAreaNameList.getNameResId(areaCode);
        if (AreaNameList.NO_RESOURCE_ID != resId) {
            codeText = getResources().getString(resId);
            noData = false;
        }
        if (true == noData) {
            codeText = new String(" ");
        }
        return codeText;
    }

    private String getTitleName(int type) {
        String text;
        int resId = 0;

        if (TvIsdbChannelManager.EWBS_MENU_ZONE == type) {
            resId = R.string.str_ewbs_title_type_zone;
        } else if (TvIsdbChannelManager.EWBS_MENU_REGION == type) {
            resId = R.string.str_ewbs_title_type_region;
        } else if (TvIsdbChannelManager.EWBS_MENU_CITY == type) {
            resId = R.string.str_ewbs_title_type_city;
        }

        if (0 != resId) {
            text = getResources().getString(resId);
        } else {
            text = new String(" ");
        }

        return text;
    }

    private void saveSharedPreference() {
        SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
        if (false == settings.getBoolean(Constant.PREFERENCES_IS_EWBS_AREA_CODE_SELECTED, false)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(Constant.PREFERENCES_IS_EWBS_AREA_CODE_SELECTED, true);
            editor.commit();
        }
    }
}
