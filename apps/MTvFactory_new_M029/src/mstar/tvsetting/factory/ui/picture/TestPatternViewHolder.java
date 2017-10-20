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

package mstar.tvsetting.factory.ui.picture;

import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mstar.android.tvapi.common.vo.TestPattern;
import com.mstar.android.tv.TvPictureManager;

public class TestPatternViewHolder {
    private static final String TAG = "TestPatternViewHolder";

    private DesignMenuActivity mTestpatternActivity;

    private final static int ADC_TYPE_VALUE_MAX = 255;

    private final static int RAMP_VALUE_MAX = 1023;

    private final static int IPMUX_RCR_DATA_MAX = 1023;

    private final static int IPMUX_GY_DATA_MAX = 1023;

    private final static int IPMUX_BCB_DATA_MAX = 1023;

    private final static int XC_IP_ENABLE_VALUE_MAX = 255;

    private final static int XC_IP_PATTERN_TYPE_VALUE_MAX = 65535;

    private final static int XC_IP_WINDOW_TYPE_VALUE_MAX = 3;

    private final static int XC_OP_MIULINE_BUFF_VALUE_MAX = 1;

    private final static int XC_OP_LINE_BUFF_HVSP_VALUE_MAX = 1;

    private final static int XC_VOP_ENABLE_VALUE_MAX = 1;

    private final static int XC_VOP2_ENABLE_VALUE_MAX = 1;

    private final static int XC_VOP2_R_DATA_VALUE_MAX = 1023;

    private final static int XC_VOP2_G_DATA_VALUE_MAX = 1023;

    private final static int XC_VOP2_B_DATA_VALUE_MAX = 1023;

    private final static int XC_MOD_ENABLE_VALUE_MAX = 1;

    private final static int XC_MOD_R_DATA_VALUE_MAX = 1023;

    private final static int XC_MOD_G_DATA_VALUE_MAX = 1023;

    private final static int XC_MOD_B_DATA_VALUE_MAX = 1023;

    private final static int XC_WHITE_BALANCE_ENABLE_VALUE_MAX = 1;

    private final static int XC_WHITE_BALANCE_RATIO_VALUE_MAX = 100;

    private TvPictureManager mTvPictureManager = null;

    private TestPattern mTestPattern = null;

    protected LinearLayout mLinear_factory_mvop_test_pattern = null;

    protected LinearLayout mLinear_factory_adc_test_pattern = null;

    protected LinearLayout mLinear_factory_ip_mux_test_pattern = null;

    protected LinearLayout mLinear_factory_xc_ip_test_pattern = null;

    protected LinearLayout mLnear_factory_xc_op_test_pattern = null;

    protected LinearLayout mLinear_factory_xc_vop_test_pattern = null;

    protected LinearLayout mLinear_factory_xc_vop2_test_pattern = null;

    protected LinearLayout mLinear_factory_xc_mod_test_pattern = null;

    protected LinearLayout mLinear_factory_white_balance_test_pattern = null;

    public TestPatternViewHolder(DesignMenuActivity mstarActivity) {
        mTestpatternActivity = mstarActivity;
        mTvPictureManager = TvPictureManager.getInstance();
        mTestPattern = new TestPattern();
    }

    public void findView() {
        mLinear_factory_mvop_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_mvop_test_pattern);
        mLinear_factory_adc_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_adc_test_pattern);
        mLinear_factory_ip_mux_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_ip_mux_test_pattern);
        mLinear_factory_xc_ip_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_xc_ip_test_pattern);
        mLnear_factory_xc_op_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_xc_op_test_pattern);
        mLinear_factory_xc_vop_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_xc_vop_test_pattern);
        mLinear_factory_xc_vop2_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_xc_vop2_test_pattern);
        mLinear_factory_xc_mod_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_xc_mod_test_pattern);
        mLinear_factory_white_balance_test_pattern = (LinearLayout) mTestpatternActivity
                .findViewById(R.id.linearlayout_factory_white_balance_test_pattern);
    }

    public void onCreate() {
        mLinear_factory_adc_test_pattern.setOnClickListener(listener);
        mLinear_factory_mvop_test_pattern.setOnClickListener(listener);
        mLinear_factory_ip_mux_test_pattern.setOnClickListener(listener);
        mLinear_factory_xc_ip_test_pattern.setOnClickListener(listener);
        mLnear_factory_xc_op_test_pattern.setOnClickListener(listener);
        mLinear_factory_xc_vop_test_pattern.setOnClickListener(listener);
        mLinear_factory_xc_vop2_test_pattern.setOnClickListener(listener);
        mLinear_factory_xc_mod_test_pattern.setOnClickListener(listener);
        mLinear_factory_white_balance_test_pattern.setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {

        EditText adc_enable_type_value = null;

        EditText adc_ramp_value = null;

        View adcLayout = null;

        EditText ip_mux_enable_value = null;

        EditText ip_mux_r_cr_data_value = null;

        EditText ip_mux_g_y_data_value = null;

        EditText ip_mux_b_cb_data_value = null;

        View ipMuxLayout = null;

        EditText xc_ip_enable_value = null;

        EditText xc_ip_pattern_type_value = null;

        EditText xc_ip_window_type_value = null;

        View xcIpLayout = null;

        EditText xc_op_bMiuLineBuff_value = null;

        EditText xc_op_bLineBuffHVSP_value = null;

        View xcOpLayout = null;

        EditText xc_vop_enable_value = null;

        View xcVopLayout = null;

        EditText xc_vop2_enable_value = null;

        EditText xc_vop2_r_data_value = null;

        EditText xc_vop2_g_data_value = null;

        EditText xc_vop2_b_data_value = null;

        View xcVop2Layout = null;

        EditText xc_mod_enable_value = null;

        EditText xc_mod_r_data_value = null;

        EditText xc_mod_g_data_value = null;

        EditText xc_mod_b_data_value = null;

        View xcModLayout = null;

        EditText xc_white_balance_enable_value = null;

        EditText xc_white_balance_radio_value = null;

        View xcWhiteBalanceLayout = null;

        @Override
        public void onClick(View view) {
            int currentid = mTestpatternActivity.getCurrentFocus().getId();
            switch (currentid) {
                case R.id.linearlayout_factory_mvop_test_pattern:
                    String[] mvop_pattern_type = mTestpatternActivity.getResources()
                            .getStringArray(R.array.str_arr_mvop_pattern_type);
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_mvop_tip_info))
                            .setItems(mvop_pattern_type, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mTestPattern.mMvopPattern = which;
                                    if (mTvPictureManager != null) {
                                        mTvPictureManager.generateTestPattern(
                                                TvPictureManager.MVOP_PATTERN_MODE, mTestPattern);
                                    }
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                case R.id.linearlayout_factory_adc_test_pattern:
                    adcLayout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_adc, null);
                    adc_enable_type_value = (EditText) adcLayout
                            .findViewById(R.id.adc_EnableADCType_val);
                    adc_ramp_value = (EditText) adcLayout.findViewById(R.id.adc_Ramp_val);
                    if ((ViewGroup) adcLayout.getParent() != null) {
                        ((ViewGroup) adcLayout.getParent()).removeView(adcLayout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_adc_tip_info))
                            .setView(adcLayout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == adc_enable_type_value.getText().toString()
                                                    .isEmpty()
                                                    && false == adc_ramp_value.getText().toString()
                                                            .isEmpty()) {
                                                int adc_Type = 0;
                                                int ramp = 0;
                                                adc_Type = Integer.parseInt(adc_enable_type_value
                                                        .getText().toString());
                                                ramp = Integer.parseInt(adc_ramp_value.getText()
                                                        .toString());
                                                if ((adc_Type >= 0 && adc_Type <= ADC_TYPE_VALUE_MAX)
                                                        && (ramp >= 0 && ramp <= RAMP_VALUE_MAX)) {
                                                    mTestPattern.mAdcType = adc_Type;
                                                    mTestPattern.mRamp = ramp;
                                                    if (mTvPictureManager != null) {
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_ADC_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                case R.id.linearlayout_factory_ip_mux_test_pattern:
                    ipMuxLayout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_ip_mux, null);
                    ip_mux_enable_value = (EditText) ipMuxLayout
                            .findViewById(R.id.ip_mux_enable_val);
                    ip_mux_r_cr_data_value = (EditText) ipMuxLayout
                            .findViewById(R.id.ip_mux_r_cr_data_val);
                    ip_mux_g_y_data_value = (EditText) ipMuxLayout
                            .findViewById(R.id.ip_mux_g_y_data_val);
                    ip_mux_b_cb_data_value = (EditText) ipMuxLayout
                            .findViewById(R.id.ip_mux_b_cb_data_val);
                    if ((ViewGroup) ipMuxLayout.getParent() != null) {
                        ((ViewGroup) ipMuxLayout.getParent()).removeView(ipMuxLayout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_ip_mux_tip_info))
                            .setView(ipMuxLayout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == ip_mux_enable_value.getText().toString()
                                                    .isEmpty()
                                                    && false == ip_mux_r_cr_data_value.getText()
                                                            .toString().isEmpty()
                                                    && false == ip_mux_g_y_data_value.getText()
                                                            .toString().isEmpty()
                                                    && false == ip_mux_b_cb_data_value.getText()
                                                            .toString().isEmpty()) {
                                                int isIpmuxEnable = 0;
                                                int ipmuxRCR_Data = 0;
                                                int ipmuxGY_Data = 0;
                                                int ipmuxBCB_Data = 0;
                                                isIpmuxEnable = Integer
                                                        .parseInt(ip_mux_enable_value.getText()
                                                                .toString());
                                                ipmuxRCR_Data = Integer
                                                        .parseInt(ip_mux_r_cr_data_value.getText()
                                                                .toString());
                                                ipmuxGY_Data = Integer
                                                        .parseInt(ip_mux_g_y_data_value.getText()
                                                                .toString());
                                                ipmuxBCB_Data = Integer
                                                        .parseInt(ip_mux_b_cb_data_value.getText()
                                                                .toString());
                                                if ((isIpmuxEnable == 1 || isIpmuxEnable == 0)
                                                        && (ipmuxRCR_Data >= 0 && ipmuxRCR_Data <= IPMUX_RCR_DATA_MAX)
                                                        && (ipmuxGY_Data >= 0 && ipmuxGY_Data <= IPMUX_GY_DATA_MAX)
                                                        && (ipmuxBCB_Data >= 0 && ipmuxBCB_Data <= IPMUX_BCB_DATA_MAX)) {
                                                    mTestPattern.mIsIpMuxEnable = isIpmuxEnable == 1 ? true
                                                            : false;
                                                    mTestPattern.mIpMuxRcrData = ipmuxRCR_Data;
                                                    mTestPattern.mIpMuxGyData = ipmuxGY_Data;
                                                    mTestPattern.mIpMuxBcbData = ipmuxBCB_Data;
                                                    if (mTvPictureManager != null) {
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_IPMUX_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                case R.id.linearlayout_factory_xc_ip_test_pattern:
                    xcIpLayout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_xc_ip, null);
                    xc_ip_enable_value = (EditText) xcIpLayout.findViewById(R.id.xc_ip_enable_val);
                    xc_ip_pattern_type_value = (EditText) xcIpLayout
                            .findViewById(R.id.xc_ip_pattern_type_val);
                    xc_ip_window_type_value = (EditText) xcIpLayout
                            .findViewById(R.id.xc_ip_window_type_val);
                    if ((ViewGroup) xcIpLayout.getParent() != null) {
                        ((ViewGroup) xcIpLayout.getParent()).removeView(xcIpLayout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_xc_ip_tip_info))
                            .setView(xcIpLayout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == xc_ip_enable_value.getText().toString()
                                                    .isEmpty()
                                                    && false == xc_ip_pattern_type_value.getText()
                                                            .toString().isEmpty()
                                                    && false == xc_ip_window_type_value.getText()
                                                            .toString().isEmpty()) {
                                                int isIpEnable = 0;
                                                int pattern_type = 0;
                                                int window = 0;
                                                isIpEnable = Integer.parseInt(xc_ip_enable_value
                                                        .getText().toString());
                                                pattern_type = Integer
                                                        .parseInt(xc_ip_pattern_type_value
                                                                .getText().toString());
                                                window = Integer.parseInt(xc_ip_window_type_value
                                                        .getText().toString());
                                                if ((isIpEnable >= 0 && isIpEnable <= XC_IP_ENABLE_VALUE_MAX)
                                                        && (pattern_type >= 0 && pattern_type <= XC_IP_PATTERN_TYPE_VALUE_MAX)
                                                        && (window >= 0 && window <= XC_IP_WINDOW_TYPE_VALUE_MAX)) {
                                                    mTestPattern.mIsIpEnable = isIpEnable;
                                                    mTestPattern.mPatternType = pattern_type;
                                                    mTestPattern.mWindow = window;
                                                    if (mTvPictureManager != null) {
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_IP_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                case R.id.linearlayout_factory_xc_op_test_pattern:
                    xcOpLayout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_xc_op, null);
                    xc_op_bMiuLineBuff_value = (EditText) xcOpLayout
                            .findViewById(R.id.xc_op_bMiuLineBuff_val);
                    xc_op_bLineBuffHVSP_value = (EditText) xcOpLayout
                            .findViewById(R.id.xc_op_bLineBuffHVSP_val);
                    if ((ViewGroup) xcOpLayout.getParent() != null) {
                        ((ViewGroup) xcOpLayout.getParent()).removeView(xcOpLayout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_xc_op_tip_info))
                            .setView(xcOpLayout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == xc_op_bMiuLineBuff_value.getText()
                                                    .toString().isEmpty()
                                                    && false == xc_op_bLineBuffHVSP_value.getText()
                                                            .toString().isEmpty()) {
                                                int miuLineBuff = 0;
                                                int lineBuffHVSP = 0;
                                                miuLineBuff = Integer
                                                        .parseInt(xc_op_bMiuLineBuff_value
                                                                .getText().toString());
                                                lineBuffHVSP = Integer
                                                        .parseInt(xc_op_bLineBuffHVSP_value
                                                                .getText().toString());

                                                if ((miuLineBuff >= 0 && miuLineBuff <= XC_OP_MIULINE_BUFF_VALUE_MAX)
                                                        && (lineBuffHVSP >= 0 && lineBuffHVSP <= XC_OP_LINE_BUFF_HVSP_VALUE_MAX)) {
                                                    mTestPattern.mMiuLineBuff = miuLineBuff == 1 ? true
                                                            : false;
                                                    mTestPattern.mLineBuffHvsp = lineBuffHVSP == 1 ? true
                                                            : false;
                                                    if (mTvPictureManager != null) {
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_OP_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                case R.id.linearlayout_factory_xc_vop_test_pattern:
                    xcVopLayout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_xc_vop, null);
                    xc_vop_enable_value = (EditText) xcVopLayout
                            .findViewById(R.id.xc_vop_enable_val);
                    if ((ViewGroup) xcVopLayout.getParent() != null) {
                        ((ViewGroup) xcVopLayout.getParent()).removeView(xcVopLayout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_xc_vop_tip_info))
                            .setView(xcVopLayout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == xc_vop_enable_value.getText().toString()
                                                    .isEmpty()) {
                                                int isVopEnable = 0;
                                                isVopEnable = Integer.parseInt(xc_vop_enable_value
                                                        .getText().toString());
                                                if ((isVopEnable >= 0 && isVopEnable <= XC_VOP_ENABLE_VALUE_MAX)) {
                                                    mTestPattern.mIsVopEnable = isVopEnable == 1 ? true
                                                            : false;
                                                    if (mTvPictureManager != null) {
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_VOP_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                case R.id.linearlayout_factory_xc_vop2_test_pattern:
                    xcVop2Layout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_xc_vop2, null);
                    xc_vop2_enable_value = (EditText) xcVop2Layout
                            .findViewById(R.id.xc_vop2_enable_val);
                    xc_vop2_r_data_value = (EditText) xcVop2Layout
                            .findViewById(R.id.xc_vop2_r_data_val);
                    xc_vop2_g_data_value = (EditText) xcVop2Layout
                            .findViewById(R.id.xc_vop2_g_data_val);
                    xc_vop2_b_data_value = (EditText) xcVop2Layout
                            .findViewById(R.id.xc_vop2_b_data_val);
                    if ((ViewGroup) xcVop2Layout.getParent() != null) {
                        ((ViewGroup) xcVop2Layout.getParent()).removeView(xcVop2Layout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_xc_vop2_tip_info))
                            .setView(xcVop2Layout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == xc_vop2_enable_value.getText().toString()
                                                    .isEmpty()
                                                    && false == xc_vop2_r_data_value.getText()
                                                            .toString().isEmpty()
                                                    && false == xc_vop2_g_data_value.getText()
                                                            .toString().isEmpty()
                                                    && false == xc_vop2_b_data_value.getText()
                                                            .toString().isEmpty()) {
                                                int isVop2Enable = 0;
                                                int vop2R_Data = 0;
                                                int vop2G_Data = 0;
                                                int vop2B_Data = 0;
                                                isVop2Enable = Integer
                                                        .parseInt(xc_vop2_enable_value.getText()
                                                                .toString());
                                                vop2R_Data = Integer.parseInt(xc_vop2_r_data_value
                                                        .getText().toString());
                                                vop2G_Data = Integer.parseInt(xc_vop2_g_data_value
                                                        .getText().toString());
                                                vop2B_Data = Integer.parseInt(xc_vop2_b_data_value
                                                        .getText().toString());
                                                if ((isVop2Enable >= 0 && isVop2Enable <= XC_VOP2_ENABLE_VALUE_MAX)
                                                        && (vop2R_Data >= 0 && vop2R_Data <= XC_VOP2_R_DATA_VALUE_MAX)
                                                        && (vop2G_Data >= 0 && vop2G_Data <= XC_VOP2_G_DATA_VALUE_MAX)
                                                        && (vop2B_Data >= 0 && vop2B_Data <= XC_VOP2_B_DATA_VALUE_MAX)) {
                                                    mTestPattern.mIsVop2Enable = isVop2Enable == 1 ? true
                                                            : false;
                                                    mTestPattern.mVop2RData = vop2R_Data;
                                                    mTestPattern.mVop2GData = vop2G_Data;
                                                    mTestPattern.mVop2BData = vop2B_Data;
                                                    if (mTvPictureManager != null) {
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_VOP2_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                case R.id.linearlayout_factory_xc_mod_test_pattern:
                    xcModLayout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_xc_mod, null);
                    xc_mod_enable_value = (EditText) xcModLayout
                            .findViewById(R.id.xc_mod_enable_val);
                    xc_mod_r_data_value = (EditText) xcModLayout
                            .findViewById(R.id.xc_mod_r_data_val);
                    xc_mod_g_data_value = (EditText) xcModLayout
                            .findViewById(R.id.xc_mod_g_data_val);
                    xc_mod_b_data_value = (EditText) xcModLayout
                            .findViewById(R.id.xc_mod_b_data_val);
                    if ((ViewGroup) xcModLayout.getParent() != null) {
                        ((ViewGroup) xcModLayout.getParent()).removeView(xcModLayout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_xc_mod_tip_info))
                            .setView(xcModLayout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == xc_mod_enable_value.getText().toString()
                                                    .isEmpty()
                                                    && false == xc_mod_r_data_value.getText()
                                                            .toString().isEmpty()
                                                    && false == xc_mod_g_data_value.getText()
                                                            .toString().isEmpty()
                                                    && false == xc_mod_b_data_value.getText()
                                                            .toString().isEmpty()) {
                                                int isModEnable = 0;
                                                int modR_Data = 0;
                                                int modG_Data = 0;
                                                int modB_Data = 0;
                                                isModEnable = Integer.parseInt(xc_mod_enable_value
                                                        .getText().toString());
                                                modR_Data = Integer.parseInt(xc_mod_r_data_value
                                                        .getText().toString());
                                                modG_Data = Integer.parseInt(xc_mod_g_data_value
                                                        .getText().toString());
                                                modB_Data = Integer.parseInt(xc_mod_b_data_value
                                                        .getText().toString());
                                                if ((isModEnable >= 0 && isModEnable <= XC_MOD_ENABLE_VALUE_MAX)
                                                        && (modR_Data >= 0 && modR_Data <= XC_MOD_R_DATA_VALUE_MAX)
                                                        && (modG_Data >= 0 && modG_Data <= XC_MOD_G_DATA_VALUE_MAX)
                                                        && (modB_Data >= 0 && modB_Data <= XC_MOD_B_DATA_VALUE_MAX)) {
                                                    mTestPattern.mIsModEnable = isModEnable == 1 ? true
                                                            : false;
                                                    mTestPattern.mModRData = modR_Data;
                                                    mTestPattern.mModGData = modG_Data;
                                                    mTestPattern.mModBData = modB_Data;
                                                    if (mTvPictureManager != null) {
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_MOD_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                case R.id.linearlayout_factory_white_balance_test_pattern:
                    xcWhiteBalanceLayout = LayoutInflater.from(mTestpatternActivity).inflate(
                            R.layout.test_pattern_xc_white_balance, null);
                    xc_white_balance_enable_value = (EditText) xcWhiteBalanceLayout
                            .findViewById(R.id.xc_whitebalance_enable_val);
                    xc_white_balance_radio_value = (EditText) xcWhiteBalanceLayout
                            .findViewById(R.id.xc_white_balance_ratio_val);
                    if ((ViewGroup) xcWhiteBalanceLayout.getParent() != null) {
                        ((ViewGroup) xcWhiteBalanceLayout.getParent()).removeView(xcWhiteBalanceLayout);
                    }
                    new AlertDialog.Builder(mTestpatternActivity)
                            .setTitle(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_xc_white_balance_tip_info))
                            .setView(xcWhiteBalanceLayout)
                            .setPositiveButton(
                                    mTestpatternActivity.getResources().getString(R.string.str_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (false == xc_white_balance_enable_value.getText().toString()
                                                    .isEmpty()
                                                    && false == xc_white_balance_radio_value.getText()
                                                            .toString().isEmpty()) {
                                                int isWhiteBalanceEnable = 0;
                                                int mWhiteBalanceRadio = 0;
                                                isWhiteBalanceEnable = Integer.parseInt(xc_white_balance_enable_value
                                                        .getText().toString());
                                                mWhiteBalanceRadio = Integer.parseInt(xc_white_balance_radio_value
                                                        .getText().toString());
                                                if ((isWhiteBalanceEnable >= 0 && isWhiteBalanceEnable <= XC_WHITE_BALANCE_ENABLE_VALUE_MAX)
                                                        && (mWhiteBalanceRadio >= 0 && mWhiteBalanceRadio <= XC_WHITE_BALANCE_RATIO_VALUE_MAX)) {
                                                    mTestPattern.mIsWhiteBalanceEnable = isWhiteBalanceEnable == 1 ? true
                                                            : false;
                                                    mTestPattern.mWhiteFieldRatio = mWhiteBalanceRadio;
                                                    if (mTvPictureManager != null) {
                                                        Log.i(TAG, "mTestPattern.mIsWhiteBalanceEnable = " + isWhiteBalanceEnable);
                                                        Log.i(TAG, "mTestPattern.mWhiteFieldRatio = " + mWhiteBalanceRadio);
                                                        mTvPictureManager
                                                                .generateTestPattern(
                                                                        TvPictureManager.XC_WHITE_BALANCE_PATTERN_MODE,
                                                                        mTestPattern);
                                                    }
                                                } else {
                                                    new AlertDialog.Builder(mTestpatternActivity)
                                                            .setTitle(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_warning))
                                                            .setMessage(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_invalid_config))
                                                            .setPositiveButton(
                                                                    mTestpatternActivity
                                                                            .getResources()
                                                                            .getString(
                                                                                    R.string.str_ok),
                                                                    null).show();
                                                }
                                            } else {
                                                new AlertDialog.Builder(mTestpatternActivity)
                                                        .setTitle(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_warning))
                                                        .setMessage(
                                                                mTestpatternActivity
                                                                        .getResources()
                                                                        .getString(
                                                                                R.string.str_no_empty))
                                                        .setPositiveButton(
                                                                mTestpatternActivity.getResources()
                                                                        .getString(R.string.str_ok),
                                                                null).show();
                                            }
                                        }
                                    })
                            .setNegativeButton(
                                    mTestpatternActivity.getResources().getString(
                                            R.string.str_cancle), null).show();
                    break;
                default:
                    break;
            }
        }
    };
}
