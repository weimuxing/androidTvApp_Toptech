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

package mstar.tvsetting.factory.ui.factorymenu;

import java.util.Arrays;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.desk.FactoryDeskImpl;
import mstar.tvsetting.factory.desk.IFactoryDesk.EN_MS_COLOR_TEMP;
import mstar.tvsetting.factory.desk.IFactoryDesk.EN_MS_COLOR_TEMP_INPUT_SOURCE;
import android.graphics.Color;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvFactoryManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import mstar.util.Tools;

public class WBAdjustViewHolder {
    private static final String TAG = "WBAdjustViewHolder";

    private FactoryMenuActivity wbActivity;

    private TvCommonManager mTvCommonmanager = TvCommonManager.getInstance();

    private IFactoryDesk factoryManager;

    private FactoryDeskImpl factoryDeskManager;

    protected TextView text_factory_wbadjust_source_val;

    protected TextView text_factory_wbadjust_rgain_val;

    protected TextView text_factory_wbadjust_ggain_val;

    protected TextView text_factory_wbadjust_bgain_val;

    protected TextView text_factory_wbadjust_roffset_val;

    protected TextView text_factory_wbadjust_goffset_val;

    protected TextView text_factory_wbadjust_boffset_val;

    protected TextView text_factory_wbadjust_colortemp_val;

    protected ProgressBar progress_factory_wbadjust_rgain;

    protected ProgressBar progress_factory_wbadjust_ggain;

    protected ProgressBar progress_factory_wbadjust_bgain;

    protected ProgressBar progress_factory_wbadjust_roffset;

    protected ProgressBar progress_factory_wbadjust_goffset;

    protected ProgressBar progress_factory_wbadjust_boffset;

    private int sourceindexWB = 0;

    private int currentSourceindex = 0;

    private int clortempindex = 0;

    private int rgainvalWB = 1024;

    private int ggainvalWB = 1024;

    private int bgainvalWB = 1024;

    private final int gainDisplayDivideWB = 8;// display:256=2048/8,step=8

    private final int gainStepWB = 8;// display:256=2048/8,step=8

    private static final int FUNCTION_DISABLED = 0;

    private int roffsetvalWB = 1024;

    private int goffsetvalWB = 1024;

    private int boffsetvalWB = 1024;

    private final int offsetDisplayDivideWB = 1;// Bar_step=1

    private final int offsetStepWB = 1;// Reg_step=1

    private int[] SourceListFlag = new int[TvCommonManager.INPUT_SOURCE_NUM];

    private int[] sourceType;

    // FIXME: move to resource file
    private static final String[] sourcearrayWB = {
            "VGA", "ATV", "AV1", "AV2", "AV3", "AV4", "AV5", "AV6", "AV7", "AV8", "AVMAX", "SV1",
            "SV2", "SV3", "SV4", "SVMAX", "YPbPr1", "YPbPr2", "YPbPr3", "YPbPrMAX", "SCART1",
            "SCART2", "SCARTMAX", "HDMI1", "HDMI2", "HDMI3", "HDMI4", "HDMIMAX", "DTV", "DVI1",
            "DVI2", "DVI3", "DVI4", "DVIMAX", "STORAGE", "KTV", "JPEG", "DTV2", "STORAGE2",
            "DVI3", "SCALER_OP", "RVU", "VGA2", "VGA3"
    };

    private static final int[] mSourceListInvisible = {
        TvCommonManager.INPUT_SOURCE_STORAGE,
        TvCommonManager.INPUT_SOURCE_JPEG, TvCommonManager.INPUT_SOURCE_DTV2,
        TvCommonManager.INPUT_SOURCE_STORAGE2, TvCommonManager.INPUT_SOURCE_DIV3,
        TvCommonManager.INPUT_SOURCE_SCALER_OP, TvCommonManager.INPUT_SOURCE_RUV
};

    private String[] colortemparray = {
            "Cool", "Nature", "Warm"
    };

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        };
    };

    public WBAdjustViewHolder(FactoryMenuActivity mstarActivity, IFactoryDesk factoryManager) {
        this.wbActivity = mstarActivity;
        this.factoryManager = factoryManager;
        factoryManager.setHandler(handler, 1);
    }

    public void findView() {
        text_factory_wbadjust_source_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_source_val);
        text_factory_wbadjust_rgain_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_val);
        text_factory_wbadjust_ggain_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_ggain_val);
        text_factory_wbadjust_bgain_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_bgain_val);
        text_factory_wbadjust_roffset_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_roffset_val);
        text_factory_wbadjust_goffset_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_goffset_val);
        text_factory_wbadjust_boffset_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_boffset_val);
        text_factory_wbadjust_colortemp_val = (TextView) wbActivity
                .findViewById(R.id.textview_factory_wbadjust_colortemp_val);
        progress_factory_wbadjust_rgain = (ProgressBar) wbActivity
                .findViewById(R.id.progressbar_facroty_wbadjust_rgain);
        progress_factory_wbadjust_ggain = (ProgressBar) wbActivity
                .findViewById(R.id.progressbar_facroty_wbadjust_ggain);
        progress_factory_wbadjust_bgain = (ProgressBar) wbActivity
                .findViewById(R.id.progressbar_facroty_wbadjust_bgain);
        progress_factory_wbadjust_roffset = (ProgressBar) wbActivity
                .findViewById(R.id.progressbar_facroty_wbadjust_roffset);
        progress_factory_wbadjust_goffset = (ProgressBar) wbActivity
                .findViewById(R.id.progressbar_facroty_wbadjust_goffset);
        progress_factory_wbadjust_boffset = (ProgressBar) wbActivity
                .findViewById(R.id.progressbar_facroty_wbadjust_boffset);
    }

    public void freshOffer() {
        rgainvalWB = factoryManager.getWbRedGain();
        ggainvalWB = factoryManager.getWbGreenGain();
        bgainvalWB = factoryManager.getWbBlueGain();
        roffsetvalWB = factoryManager.getWbRedOffset();
        goffsetvalWB = factoryManager.getWbGreenOffset();
        boffsetvalWB = factoryManager.getWbBlueOffset();
        if (factoryManager != null) {
            clortempindex = factoryManager.getColorTmpIdx();
            Log.v(TAG, "-freshOffer----clortempindex----" + clortempindex);
        }
        text_factory_wbadjust_rgain_val.setText(Integer.toString(rgainvalWB / gainDisplayDivideWB));
        text_factory_wbadjust_ggain_val.setText(Integer.toString(ggainvalWB / gainDisplayDivideWB));
        text_factory_wbadjust_bgain_val.setText(Integer.toString(bgainvalWB / gainDisplayDivideWB));
        text_factory_wbadjust_roffset_val.setText(Integer.toString(roffsetvalWB
                / offsetDisplayDivideWB));
        text_factory_wbadjust_goffset_val.setText(Integer.toString(goffsetvalWB
                / offsetDisplayDivideWB));
        text_factory_wbadjust_boffset_val.setText(Integer.toString(boffsetvalWB
                / offsetDisplayDivideWB));
        text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
    }

    public boolean onCreate() {
        rgainvalWB = factoryManager.getWbRedGain();
        ggainvalWB = factoryManager.getWbGreenGain();
        bgainvalWB = factoryManager.getWbBlueGain();
        roffsetvalWB = factoryManager.getWbRedOffset();
        goffsetvalWB = factoryManager.getWbGreenOffset();
        boffsetvalWB = factoryManager.getWbBlueOffset();
        // sourceindexWB = factoryManager.getCurrentInputSource().ordinal();
        sourceindexWB = factoryManager.getWBIdx().ordinal();
        if (factoryManager != null) {
            clortempindex = factoryManager.getColorTmpIdx();
        }
        text_factory_wbadjust_rgain_val.setText(Integer.toString(rgainvalWB / gainDisplayDivideWB));
        text_factory_wbadjust_ggain_val.setText(Integer.toString(ggainvalWB / gainDisplayDivideWB));
        text_factory_wbadjust_bgain_val.setText(Integer.toString(bgainvalWB / gainDisplayDivideWB));
        text_factory_wbadjust_roffset_val.setText(Integer.toString(roffsetvalWB
                / offsetDisplayDivideWB));
        text_factory_wbadjust_goffset_val.setText(Integer.toString(goffsetvalWB
                / offsetDisplayDivideWB));
        text_factory_wbadjust_boffset_val.setText(Integer.toString(boffsetvalWB
                / offsetDisplayDivideWB));
        progress_factory_wbadjust_rgain.setProgress(rgainvalWB);
        progress_factory_wbadjust_ggain.setProgress(ggainvalWB);
        progress_factory_wbadjust_bgain.setProgress(bgainvalWB);
        progress_factory_wbadjust_roffset.setProgress(roffsetvalWB);
        progress_factory_wbadjust_goffset.setProgress(goffsetvalWB);
        progress_factory_wbadjust_boffset.setProgress(boffsetvalWB);
        String dispStr = sourcearrayWB[sourceindexWB];
        if ((TvCommonManager.INPUT_SOURCE_HDMI <= sourceindexWB)
            && (TvCommonManager.INPUT_SOURCE_HDMI4 >= sourceindexWB)) {
            dispStr = Tools.getHdmiDispStr(sourceindexWB);
        }
        text_factory_wbadjust_source_val.setText(dispStr);
        text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
        getSupportSourcelist();
        // DisableSourceLinear();
        return true;
    }

    private void getSupportSourcelist() {
        int[] sourceList;
        int j = 0;
        sourceList = mTvCommonmanager.getSourceList();
        if (sourceList != null) {
            for (int i = 0; (i < SourceListFlag.length) && (i < sourceList.length); i++) {
                SourceListFlag[i] = sourceList[i];
            }

            for (int i = 0; (i < mSourceListInvisible.length) && (i < SourceListFlag.length); i++) {
                if (TvCommonManager.INPUT_SOURCE_NUM > mSourceListInvisible[i]) {
                    SourceListFlag[mSourceListInvisible[i]] = FUNCTION_DISABLED;
                }
            }

            sourceType = new int[SourceListFlag.length];
            j = 0;
            for (int i = 0; (i < SourceListFlag.length) && (i < sourceList.length); i++) {
                if (SourceListFlag[i] > 0) {
                    sourceType[j] = i;
                    if (sourceType[j] == sourceindexWB) {
                        currentSourceindex = j;
                    }
                    j++;
                }
            }
            sourceType = Arrays.copyOf(sourceType, j);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bRet = true;
        int currentid = wbActivity.getCurrentFocus().getId();
        String str_val = new String();
        int maxValue;
        int curSourceValue;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_factory_wbadjust_source:
                        maxValue = sourceType.length;
                        currentSourceindex++;
                        if (currentSourceindex > maxValue - 1) {
                            currentSourceindex = 0;
                        }
                        factoryManager.execSetInputSource(EnumInputSource.values()[sourceType[currentSourceindex]]);
                        factoryManager.setWBIdx(EnumInputSource.values()[sourceType[currentSourceindex]]);
                        onCreate();
                        if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_ATV.ordinal()) {
                            factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
                            int curChannelNumber = TvChannelManager.getInstance()
                                    .getCurrentChannelNumber();
                            if (curChannelNumber > 0xFF) {
                                curChannelNumber = 0;
                            }
                            TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
                        } else if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_DTV.ordinal()) {
                            factoryManager.execSetInputSource(EnumInputSource.E_INPUT_SOURCE_DTV);
                            TvChannelManager.getInstance().playDtvCurrentProgram();
                        }
                        break;
                    case R.id.linearlayout_factory_wbadjust_colortemp:
                        maxValue = EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal();
                        if (clortempindex < (maxValue - 3))
                            clortempindex++;
                        else
                            clortempindex = 0;

                        text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
                        if (factoryManager != null) {
                            factoryManager.setColorTmpIdx(EN_MS_COLOR_TEMP.values()[clortempindex]);
                        }
                        onCreate();
                        break;
                    case R.id.linearlayout_factory_wbadjust_rgain:
                        if (rgainvalWB < TvFactoryManager.WB_ADJUST_GAIN_MAX) {
                            rgainvalWB = rgainvalWB + gainStepWB;

                            if (rgainvalWB > TvFactoryManager.WB_ADJUST_GAIN_MAX)
                                rgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MAX;
                        } else {
                            rgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MIN;
                        }
                        str_val = Integer.toString(rgainvalWB / gainDisplayDivideWB);
                        text_factory_wbadjust_rgain_val.setText(str_val);
                        progress_factory_wbadjust_rgain.setProgress(rgainvalWB);
                        factoryManager.setWbRedGain((short) rgainvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_ggain:
                        if (ggainvalWB < TvFactoryManager.WB_ADJUST_GAIN_MAX) {
                            ggainvalWB = ggainvalWB + gainStepWB;

                            if (ggainvalWB > TvFactoryManager.WB_ADJUST_GAIN_MAX)
                                ggainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MAX;
                        } else {
                            ggainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MIN;
                        }
                        str_val = Integer.toString(ggainvalWB / gainDisplayDivideWB);
                        text_factory_wbadjust_ggain_val.setText(str_val);
                        progress_factory_wbadjust_ggain.setProgress(ggainvalWB);
                        factoryManager.setWbGreenGain((short) ggainvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_bgain:
                        if (bgainvalWB < TvFactoryManager.WB_ADJUST_GAIN_MAX) {
                            bgainvalWB = bgainvalWB + gainStepWB;

                            if (bgainvalWB > TvFactoryManager.WB_ADJUST_GAIN_MAX)
                                bgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MAX;
                        } else {
                            bgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MIN;
                        }
                        str_val = Integer.toString(bgainvalWB / gainDisplayDivideWB);
                        text_factory_wbadjust_bgain_val.setText(str_val);
                        progress_factory_wbadjust_bgain.setProgress(bgainvalWB);
                        factoryManager.setWbBlueGain((short) bgainvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_roffset:
                        if (roffsetvalWB < 2047) {
                            roffsetvalWB = roffsetvalWB + offsetStepWB;

                            if (roffsetvalWB > 2047)
                                roffsetvalWB = 2047;
                        } else {
                            roffsetvalWB = 0;
                        }
                        str_val = Integer.toString(roffsetvalWB / offsetDisplayDivideWB);
                        text_factory_wbadjust_roffset_val.setText(str_val);
                        progress_factory_wbadjust_roffset.setProgress(roffsetvalWB);
                        factoryManager.setWbRedOffset((short) roffsetvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_goffset:
                        if (goffsetvalWB < 2047) {
                            goffsetvalWB = goffsetvalWB + offsetStepWB;

                            if (goffsetvalWB > 2047)
                                goffsetvalWB = 2047;
                        } else {
                            goffsetvalWB = 0;
                        }
                        str_val = Integer.toString(goffsetvalWB / offsetDisplayDivideWB);
                        text_factory_wbadjust_goffset_val.setText(str_val);
                        progress_factory_wbadjust_goffset.setProgress(goffsetvalWB);
                        factoryManager.setWbGreenOffset((short) goffsetvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_boffset:
                        if (boffsetvalWB < 2047) {
                            boffsetvalWB = boffsetvalWB + offsetStepWB;

                            if (boffsetvalWB > 2047)
                                boffsetvalWB = 2047;
                        } else {
                            boffsetvalWB = 0;
                        }
                        str_val = Integer.toString(boffsetvalWB / offsetDisplayDivideWB);
                        text_factory_wbadjust_boffset_val.setText(str_val);
                        progress_factory_wbadjust_boffset.setProgress(boffsetvalWB);
                        factoryManager.setWbBlueOffset((short) boffsetvalWB);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_factory_wbadjust_source:
                        maxValue = sourceType.length;
                        currentSourceindex--;
                        if (currentSourceindex < 0) {
                            currentSourceindex = maxValue - 1;
                        }
                        factoryManager.execSetInputSource(EnumInputSource.values()[sourceType[currentSourceindex]]);
                        factoryManager.setWBIdx(EnumInputSource.values()[sourceType[currentSourceindex]]);
                        factoryManager.changeWBParaWhenSourceChange();
                        onCreate();
                        if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_ATV.ordinal()) {
                            int curChannelNumber = TvChannelManager.getInstance()
                                    .getCurrentChannelNumber();
                            if (curChannelNumber > 0xFF) {
                                curChannelNumber = 0;
                            }
                            TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
                        } else if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_DTV.ordinal()) {
                            TvChannelManager.getInstance().playDtvCurrentProgram();
                        }
                        break;
                    case R.id.linearlayout_factory_wbadjust_colortemp:
                        maxValue = EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal();
                        if (clortempindex > 0)
                            clortempindex--;
                        else
                            clortempindex = maxValue - 3;
                        text_factory_wbadjust_colortemp_val.setText(colortemparray[clortempindex]);
                        if (factoryManager != null) {
                            factoryManager.setColorTmpIdx(EN_MS_COLOR_TEMP.values()[clortempindex]);
                        }
                        onCreate();
                        break;
                    case R.id.linearlayout_factory_wbadjust_rgain:
                        if (rgainvalWB > TvFactoryManager.WB_ADJUST_GAIN_MIN) {
                            rgainvalWB = rgainvalWB - gainStepWB;

                            if (rgainvalWB < TvFactoryManager.WB_ADJUST_GAIN_MIN)
                                rgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MIN;
                        } else {
                            rgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MAX;
                        }
                        str_val = Integer.toString(rgainvalWB / gainDisplayDivideWB);
                        text_factory_wbadjust_rgain_val.setText(str_val);
                        progress_factory_wbadjust_rgain.setProgress(rgainvalWB);
                        factoryManager.setWbRedGain((short) rgainvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_ggain:
                        if (ggainvalWB > TvFactoryManager.WB_ADJUST_GAIN_MIN) {
                            ggainvalWB = ggainvalWB - gainStepWB;

                            if (ggainvalWB < TvFactoryManager.WB_ADJUST_GAIN_MIN)
                                ggainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MIN;
                        } else {
                            ggainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MAX;
                        }
                        str_val = Integer.toString(ggainvalWB / gainDisplayDivideWB);
                        text_factory_wbadjust_ggain_val.setText(str_val);
                        progress_factory_wbadjust_ggain.setProgress(ggainvalWB);
                        factoryManager.setWbGreenGain((short) ggainvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_bgain:
                        if (bgainvalWB > TvFactoryManager.WB_ADJUST_GAIN_MIN) {
                            bgainvalWB = bgainvalWB - gainStepWB;

                            if (bgainvalWB < TvFactoryManager.WB_ADJUST_GAIN_MIN)
                                bgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MIN;
                        } else {
                            bgainvalWB = TvFactoryManager.WB_ADJUST_GAIN_MAX;
                        }
                        str_val = Integer.toString(bgainvalWB / gainDisplayDivideWB);
                        text_factory_wbadjust_bgain_val.setText(str_val);
                        progress_factory_wbadjust_bgain.setProgress(bgainvalWB);
                        factoryManager.setWbBlueGain((short) bgainvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_roffset:
                        if (roffsetvalWB > 0) {
                            roffsetvalWB = roffsetvalWB - offsetStepWB;

                            if (roffsetvalWB < 0)
                                roffsetvalWB = 0;
                        } else {
                            roffsetvalWB = 2047;
                        }
                        str_val = Integer.toString(roffsetvalWB / offsetDisplayDivideWB);
                        text_factory_wbadjust_roffset_val.setText(str_val);
                        progress_factory_wbadjust_roffset.setProgress(roffsetvalWB);
                        factoryManager.setWbRedOffset((short) roffsetvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_goffset:
                        if (goffsetvalWB > 0) {
                            goffsetvalWB = goffsetvalWB - offsetStepWB;

                            if (goffsetvalWB < 0)
                                goffsetvalWB = 0;
                        } else {
                            goffsetvalWB = 2047;
                        }
                        str_val = Integer.toString(goffsetvalWB / offsetDisplayDivideWB);
                        text_factory_wbadjust_goffset_val.setText(str_val);
                        progress_factory_wbadjust_goffset.setProgress(goffsetvalWB);
                        factoryManager.setWbGreenOffset((short) goffsetvalWB);
                        break;
                    case R.id.linearlayout_factory_wbadjust_boffset:
                        if (boffsetvalWB > 0) {
                            boffsetvalWB = boffsetvalWB - offsetStepWB;

                            if (boffsetvalWB < 0)
                                boffsetvalWB = 0;
                        } else {
                            boffsetvalWB = 2047;
                        }
                        str_val = Integer.toString(boffsetvalWB / offsetDisplayDivideWB);
                        text_factory_wbadjust_boffset_val.setText(str_val);
                        progress_factory_wbadjust_boffset.setProgress(boffsetvalWB);
                        factoryManager.setWbBlueOffset((short) boffsetvalWB);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                this.wbActivity.returnRoot(1);
                break;
            default:
                bRet = false;
                break;
        }
        return bRet;
    }

    void DisableSourceLinear() {
        LinearLayout sourceLinear = (LinearLayout) wbActivity
                .findViewById(R.id.linearlayout_factory_wbadjust_source);
        ((TextView) (sourceLinear.getChildAt(0))).setTextColor(Color.GRAY);
        ((TextView) (sourceLinear.getChildAt(1))).setTextColor(Color.GRAY);
        sourceLinear.setEnabled(false);
        sourceLinear.setFocusable(false);
    }
}
