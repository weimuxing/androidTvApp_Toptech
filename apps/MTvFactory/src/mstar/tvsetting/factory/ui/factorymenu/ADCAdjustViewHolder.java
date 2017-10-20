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

package mstar.tvsetting.factory.ui.factorymenu;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvFactoryManager;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.FactoryDeskImpl;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.desk.IFactoryDesk.E_ADC_SET_INDEX;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

public class ADCAdjustViewHolder {
    private FactoryMenuActivity adcActivity;

    private IFactoryDesk factoryManager;

    protected TextView text_factory_adc_source_val;

    protected TextView text_factory_adc_rgain_val;

    protected TextView text_factory_adc_ggain_val;

    protected TextView text_factory_adc_bgain_val;

    protected TextView text_factory_adc_roffset_val;

    protected TextView text_factory_adc_goffset_val;

    protected TextView text_factory_adc_boffset_val;

    protected TextView text_factory_adc_phase_val;

    private int sourceIndexAdc = 0;

    private int rgainvalADC = TvFactoryManager.ADC_ADJUST_GAIN_MAX;

    private int ggainvalADC = TvFactoryManager.ADC_ADJUST_GAIN_MAX;

    private int bgainvalADC = TvFactoryManager.ADC_ADJUST_GAIN_MAX;

    private int roffsetvalADC = TvFactoryManager.ADC_ADJUST_OFFSET_MAX;

    private int goffsetvalADC = TvFactoryManager.ADC_ADJUST_OFFSET_MAX;

    private int boffsetvalADC = TvFactoryManager.ADC_ADJUST_OFFSET_MAX;

    private int phasevalADC = 50;

    private final int FUNCTION_DISABLED = 0;

    private final String[] mADCSourceItems = {
            "VGA", "YPbPr(SD)", "YPbPr(HD)"
    };

    private boolean[] mADCSourceItemEnableFlags = new boolean[mADCSourceItems.length];

    private ProgressDialog progressDialog = null;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == IFactoryDesk.AUTOTUNE_START) {
                progressDialog = getProgressDialog();
                progressDialog.show();
            } else if (msg.what == IFactoryDesk.AUTOTUNE_END_SUCESSED) {
                if (null != progressDialog) {
                    progressDialog.dismiss();
                }
                onCreate();
                Toast.makeText(
                        adcActivity,
                        adcActivity.getResources()
                                .getString(R.string.str_factory_adc_autoadjust_ok),
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == IFactoryDesk.AUTOTUNE_END_FAILED) {
                if (null != progressDialog) {
                    progressDialog.dismiss();
                }
                onCreate();
                Toast.makeText(
                        adcActivity,
                        adcActivity.getResources().getString(
                                R.string.str_factory_adc_autoadjust_failed), Toast.LENGTH_SHORT)
                        .show();
            } else if (msg.what == IFactoryDesk.AUTOTUNE_TIMING_MODE_ERR) {
                if (null != progressDialog) {
                    progressDialog.dismiss();
                }
                onCreate();
                Toast.makeText(
                        adcActivity,
                        adcActivity.getResources().getString(
                                R.string.str_factory_adc_autoadjust_timing_mode_err),
                        Toast.LENGTH_SHORT).show();
            }
        };
    };

    public ADCAdjustViewHolder(FactoryMenuActivity mstarActivity, IFactoryDesk factoryManager) {
        adcActivity = mstarActivity;
        this.factoryManager = factoryManager;

        int[] sourceList = TvCommonManager.getInstance().getSourceList();
        mADCSourceItemEnableFlags[TvFactoryManager.ADC_SET_VGA] = (sourceList[TvCommonManager.INPUT_SOURCE_VGA] == FUNCTION_DISABLED) ? false : true;
        mADCSourceItemEnableFlags[TvFactoryManager.ADC_SET_YPBPR_SD] = (sourceList[TvCommonManager.INPUT_SOURCE_YPBPR] == FUNCTION_DISABLED) ? false : true;
        mADCSourceItemEnableFlags[TvFactoryManager.ADC_SET_YPBPR_HD] = (sourceList[TvCommonManager.INPUT_SOURCE_YPBPR] == FUNCTION_DISABLED) ? false : true;
        sourceIndexAdc = factoryManager.getAdcIdx().ordinal();
        factoryManager.setHandler(handler, 1);
    }

    public void findView() {
        text_factory_adc_source_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_source_val);
        text_factory_adc_rgain_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_rgain_val);
        text_factory_adc_ggain_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_ggain_val);
        text_factory_adc_bgain_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_bgain_val);
        text_factory_adc_roffset_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_roffset_val);
        text_factory_adc_goffset_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_goffset_val);
        text_factory_adc_boffset_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_boffset_val);
        text_factory_adc_phase_val = (TextView) adcActivity
                .findViewById(R.id.textview_factory_adc_phase_val);
    }

    public boolean onCreate() {
        FactoryDeskImpl factoryDesk = FactoryDeskImpl.getInstance(adcActivity);
        factoryDesk.loadCurAdcDataFromDB(factoryManager.getAdcIdx().ordinal());
        rgainvalADC = factoryManager.getADCRedGain();
        ggainvalADC = factoryManager.getADCGreenGain();
        bgainvalADC = factoryManager.getADCBlueGain();
        roffsetvalADC = factoryManager.getADCRedOffset();
        goffsetvalADC = factoryManager.getADCGreenOffset();
        boffsetvalADC = factoryManager.getADCBlueOffset();
        phasevalADC = factoryManager.getADCPhase();
        sourceIndexAdc = factoryManager.getAdcIdx().ordinal();
        text_factory_adc_rgain_val.setText(Integer.toString(rgainvalADC));
        text_factory_adc_ggain_val.setText(Integer.toString(ggainvalADC));
        text_factory_adc_bgain_val.setText(Integer.toString(bgainvalADC));
        text_factory_adc_roffset_val.setText(Integer.toString(roffsetvalADC));
        text_factory_adc_goffset_val.setText(Integer.toString(goffsetvalADC));
        text_factory_adc_boffset_val.setText(Integer.toString(boffsetvalADC));
        text_factory_adc_phase_val.setText(Integer.toString(phasevalADC));
        text_factory_adc_source_val.setText(mADCSourceItems[sourceIndexAdc]);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bRet = true;
        int currentid = adcActivity.getCurrentFocus().getId();
        String str_val = new String();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_factory_adc_source:
                        sourceIndexAdc = increaseIdx(sourceIndexAdc);
                        factoryManager.setAdcIdx(E_ADC_SET_INDEX.values()[sourceIndexAdc]);
                        onCreate();
                        if (factoryManager.getAdcIdx() == E_ADC_SET_INDEX.ADC_SET_VGA) {
                            factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_VGA);
                        } else {
                            factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_YPBPR);
                        }
                        break;
                    case R.id.linearlayout_factory_adc_rgain:
                        if (rgainvalADC < TvFactoryManager.ADC_ADJUST_GAIN_MAX) {
                            rgainvalADC++;
                        } else {
                            rgainvalADC = 0;
                        }
                        str_val = Integer.toString(rgainvalADC);
                        text_factory_adc_rgain_val.setText(str_val);
                        factoryManager.setADCRedGain(rgainvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_ggain:
                        if (ggainvalADC < TvFactoryManager.ADC_ADJUST_GAIN_MAX) {
                            ggainvalADC++;
                        } else {
                            ggainvalADC = 0;
                        }
                        str_val = Integer.toString(ggainvalADC);
                        text_factory_adc_ggain_val.setText(str_val);
                        factoryManager.setADCGreenGain(ggainvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_bgain:
                        if (bgainvalADC < TvFactoryManager.ADC_ADJUST_GAIN_MAX) {
                            bgainvalADC++;
                        } else {
                            bgainvalADC = 0;
                        }
                        str_val = Integer.toString(bgainvalADC);
                        text_factory_adc_bgain_val.setText(str_val);
                        factoryManager.setADCBlueGain(bgainvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_roffset:
                        if (roffsetvalADC < TvFactoryManager.ADC_ADJUST_OFFSET_MAX) {
                            roffsetvalADC++;
                        } else {
                            roffsetvalADC = 0;
                        }
                        str_val = Integer.toString(roffsetvalADC);
                        text_factory_adc_roffset_val.setText(str_val);
                        factoryManager.setADCRedOffset(roffsetvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_goffset:
                        if (goffsetvalADC < TvFactoryManager.ADC_ADJUST_OFFSET_MAX) {
                            goffsetvalADC++;
                        } else {
                            goffsetvalADC = 0;
                        }
                        str_val = Integer.toString(goffsetvalADC);
                        text_factory_adc_goffset_val.setText(str_val);
                        factoryManager.setADCGreenOffset(goffsetvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_boffset:
                        if (boffsetvalADC < TvFactoryManager.ADC_ADJUST_OFFSET_MAX) {
                            boffsetvalADC++;
                        } else {
                            boffsetvalADC = 0;
                        }
                        str_val = Integer.toString(boffsetvalADC);
                        text_factory_adc_boffset_val.setText(str_val);
                        factoryManager.setADCBlueOffset(boffsetvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_phase:
                        if (phasevalADC != 255) {
                            phasevalADC++;
                        } else {
                            phasevalADC = 0;
                        }
                        str_val = Integer.toString(phasevalADC);
                        text_factory_adc_phase_val.setText(str_val);
                        factoryManager.setADCPhase(phasevalADC);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_factory_adc_source:
                        sourceIndexAdc = decreaseIdx(sourceIndexAdc);
                        factoryManager.setAdcIdx(E_ADC_SET_INDEX.values()[sourceIndexAdc]);
                        onCreate();
                        if (factoryManager.getAdcIdx() == E_ADC_SET_INDEX.ADC_SET_VGA) {
                            factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_VGA);
                        } else {
                            factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_YPBPR);
                        }
                        break;
                    case R.id.linearlayout_factory_adc_rgain:
                        if (rgainvalADC != TvFactoryManager.ADC_ADJUST_GAIN_MIN) {
                            rgainvalADC--;
                        } else {
                            rgainvalADC = TvFactoryManager.ADC_ADJUST_GAIN_MAX;
                        }
                        str_val = Integer.toString(rgainvalADC);
                        text_factory_adc_rgain_val.setText(str_val);
                        factoryManager.setADCRedGain(rgainvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_ggain:
                        if (ggainvalADC != TvFactoryManager.ADC_ADJUST_GAIN_MIN) {
                            ggainvalADC--;
                        } else {
                            ggainvalADC = TvFactoryManager.ADC_ADJUST_GAIN_MAX;
                        }
                        str_val = Integer.toString(ggainvalADC);
                        text_factory_adc_ggain_val.setText(str_val);
                        factoryManager.setADCGreenGain(ggainvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_bgain:
                        if (bgainvalADC != TvFactoryManager.ADC_ADJUST_GAIN_MIN) {
                            bgainvalADC--;
                        } else {
                            bgainvalADC = TvFactoryManager.ADC_ADJUST_GAIN_MAX;
                        }
                        str_val = Integer.toString(bgainvalADC);
                        text_factory_adc_bgain_val.setText(str_val);
                        factoryManager.setADCBlueGain(bgainvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_roffset:
                        if (roffsetvalADC != TvFactoryManager.ADC_ADJUST_OFFSET_MIN) {
                            roffsetvalADC--;
                        } else {
                            roffsetvalADC = TvFactoryManager.ADC_ADJUST_OFFSET_MAX;
                        }
                        str_val = Integer.toString(roffsetvalADC);
                        text_factory_adc_roffset_val.setText(str_val);
                        factoryManager.setADCRedOffset(roffsetvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_goffset:
                        if (goffsetvalADC != TvFactoryManager.ADC_ADJUST_OFFSET_MIN) {
                            goffsetvalADC--;
                        } else {
                            goffsetvalADC = TvFactoryManager.ADC_ADJUST_OFFSET_MAX;
                        }
                        str_val = Integer.toString(goffsetvalADC);
                        text_factory_adc_goffset_val.setText(str_val);
                        factoryManager.setADCGreenOffset(goffsetvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_boffset:
                        if (boffsetvalADC != TvFactoryManager.ADC_ADJUST_OFFSET_MIN) {
                            boffsetvalADC--;
                        } else {
                            boffsetvalADC = TvFactoryManager.ADC_ADJUST_OFFSET_MAX;
                        }
                        str_val = Integer.toString(boffsetvalADC);
                        text_factory_adc_boffset_val.setText(str_val);
                        factoryManager.setADCBlueOffset(boffsetvalADC);
                        break;
                    case R.id.linearlayout_factory_adc_phase:
                        if (phasevalADC != 0) {
                            phasevalADC--;
                        } else {
                            phasevalADC = 255;
                        }
                        str_val = Integer.toString(phasevalADC);
                        text_factory_adc_phase_val.setText(str_val);
                        factoryManager.setADCPhase(phasevalADC);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                this.adcActivity.returnRoot(0);
                factoryManager.releaseHandler(1);
                break;
            case KeyEvent.KEYCODE_ENTER:
                if (currentid == R.id.linearlayout_factory_adc_autoadjust) {
                    factoryManager.ExecAutoADC(sourceIndexAdc);
                }
                break;
            default:
                bRet = false;
                break;
        }
        return bRet;
    }

    private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(adcActivity);
            progressDialog.setMessage(adcActivity
                    .getString(R.string.str_factory_adc_autoadjust_val));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        return progressDialog;
    }

    private int increaseIdx(int currIndex) {
        int retIdx = currIndex;
        for (int idx = currIndex + 1; idx < currIndex + mADCSourceItemEnableFlags.length; idx++) {
            if (true == mADCSourceItemEnableFlags[idx % mADCSourceItemEnableFlags.length]) {
                retIdx = idx % mADCSourceItemEnableFlags.length;
                break;
            }
        }
        return retIdx;
    }

    private int decreaseIdx(int currIndex) {
        int retIdx = currIndex;
        for (int idx = currIndex + mADCSourceItemEnableFlags.length - 1; idx > currIndex; idx--) {
            if (true == mADCSourceItemEnableFlags[idx % mADCSourceItemEnableFlags.length]) {
                retIdx = idx % mADCSourceItemEnableFlags.length;
                break;
            }
        }
        return retIdx;
    }
}
