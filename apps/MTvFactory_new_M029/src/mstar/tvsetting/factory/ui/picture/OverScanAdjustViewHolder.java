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

import java.util.ArrayList;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

import android.os.SystemProperties;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

@SuppressWarnings("unused")
public class OverScanAdjustViewHolder {
    protected TextView text_factory_overscan_source_val;

    protected TextView text_factory_overscan_hsize_val;

    protected TextView text_factory_overscan_hposition_val;

    protected TextView text_factory_overscan_vsize_val;

    protected TextView text_factory_overscan_vpositon_val;

    protected ProgressBar progress_factory_overscan_hsize;

    protected ProgressBar progress_factory_overscan_hposition;

    protected ProgressBar progress_factory_overscan_vsize;

    protected ProgressBar progress_factory_overscan_vpositon;

    private DesignMenuActivity overScanActivity;

    private IFactoryDesk factoryManager;

    private int sourceindex = 0;

    private int hsizeval = 50;

    private int hpositionval = 50;

    private int vsizeval = 50;

    private int vpositionval = 50;

    private String[] sourcearray;
    
	private static final int TYPE_AV = 0;
	private static final int TYPE_HDMI = 1;

    private ArrayList<EnumInputSource> validSources;

    private int[] sourceflags;
    
    private TvPlayerEventListener listener = null;

    public OverScanAdjustViewHolder(DesignMenuActivity mstarActivity, IFactoryDesk factoryManager) {
        overScanActivity = mstarActivity;
        this.factoryManager = factoryManager;
        validSources = new ArrayList<EnumInputSource>();
		sourcearray=overScanActivity.getResources().getStringArray(R.array.str_arr_picture_over_vals);
        try {
            sourceflags = TvManager.getInstance().getSourceList();
            //VGA don't support overscan, start with atv
            int[] count = new int[2];
			for (int i = 0; i < count.length; i++) {
				count[i] = 0;
			}
            for (int i = 1; (i < sourcearray.length) && (i < sourceflags.length); i++) {
				if(i == TvCommonManager.INPUT_SOURCE_DTV) {
					if(SystemProperties.getBoolean("persist.sys.no_dtv",false)) {
                		sourceflags[i] = 0;
                		continue;
                	}
                }
                if (sourceflags[i] != 0){
                    validSources.add(EnumInputSource.values()[i]);
                    switch (i) {
					case TvCommonManager.INPUT_SOURCE_CVBS:
					case TvCommonManager.INPUT_SOURCE_CVBS2:
					case TvCommonManager.INPUT_SOURCE_CVBS3:
					case TvCommonManager.INPUT_SOURCE_CVBS4:
					case TvCommonManager.INPUT_SOURCE_CVBS5:
					case TvCommonManager.INPUT_SOURCE_CVBS6:
					case TvCommonManager.INPUT_SOURCE_CVBS7:
					case TvCommonManager.INPUT_SOURCE_CVBS8:
						count[TYPE_AV] += 1;
						break;
					case TvCommonManager.INPUT_SOURCE_HDMI:
					case TvCommonManager.INPUT_SOURCE_HDMI2:
					case TvCommonManager.INPUT_SOURCE_HDMI3:
					case TvCommonManager.INPUT_SOURCE_HDMI4:
						count[TYPE_HDMI] += 1;
						break;
					}
				}
            }
            sourcearray[TvCommonManager.INPUT_SOURCE_CVBS] = count[TYPE_AV] == 1 ? overScanActivity.getResources().getString(R.string.picture_white_balance_AV_vals) : overScanActivity.getResources().getString(R.string.picture_white_balance_AV1_vals);
            sourcearray[TvCommonManager.INPUT_SOURCE_HDMI] = count[TYPE_HDMI] == 1 ? overScanActivity.getResources().getString(R.string.picture_white_balance_HDMI_vals) : overScanActivity.getResources().getString(R.string.picture_white_balance_HDMI1_vals);
        } catch (TvCommonException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ;
    }

    public void findView() {
        text_factory_overscan_source_val = (TextView) overScanActivity
                .findViewById(R.id.textview_factory_overscan_source_val);
        text_factory_overscan_hsize_val = (TextView) overScanActivity
                .findViewById(R.id.textview_factory_overscan_hsize_val);
        text_factory_overscan_hposition_val = (TextView) overScanActivity
                .findViewById(R.id.textview_factory_overscan_hposition_val);
        text_factory_overscan_vsize_val = (TextView) overScanActivity
                .findViewById(R.id.textview_factory_overscan_vsize_val);
        text_factory_overscan_vpositon_val = (TextView) overScanActivity
                .findViewById(R.id.textview_factory_overscan_vposition_val);
        progress_factory_overscan_hsize = (ProgressBar) overScanActivity
                .findViewById(R.id.progressbar_facroty_overscan_hsize);
        progress_factory_overscan_hposition = (ProgressBar) overScanActivity
                .findViewById(R.id.progressbar_facroty_overscan_hposition);
        progress_factory_overscan_vsize = (ProgressBar) overScanActivity
                .findViewById(R.id.progressbar_facroty_overscan_vsize);
        progress_factory_overscan_vpositon = (ProgressBar) overScanActivity
                .findViewById(R.id.progressbar_facroty_overscan_vposition);
        listener = new TvPlayerEventListener();
        TvChannelManager.getInstance().registerOnTvPlayerEventListener(listener);
    }

    public boolean onCreate() {
        // VGA don't support overscan. If current source is VGA, switch to ATV
        if (factoryManager.getOverScanSourceType() != EnumInputSource.E_INPUT_SOURCE_VGA) {
            text_factory_overscan_source_val.setText(sourcearray[factoryManager.getOverScanSourceType()
                    .ordinal()]);
            sourceindex = validSources.indexOf(factoryManager.getOverScanSourceType());
        } else {
            text_factory_overscan_source_val.setText(sourcearray[validSources.get(
                    sourceindex).ordinal()]);
            factoryManager.setOverScanSourceType(validSources.get(sourceindex));

            int curChannelNumber = TvChannelManager.getInstance()
                    .getCurrentChannelNumber();
            if (curChannelNumber > 0xFF) {
                curChannelNumber = 0;
            }
            TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
        }

        updateUi();
        return true;
    }
    
    private class TvPlayerEventListener implements OnTvPlayerEventListener{

		@Override
		public boolean on4k2kHDMIDisableDualView(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisablePip(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisablePop(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisableTravelingMode(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean onDtvChannelInfoUpdate(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean onDtvPsipTsUpdate(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean onEmerencyAlert(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean onEpgUpdateList(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onHbbtvUiEvent(int arg0, HbbtvEventInfo arg1) {
			return false;
		}

		@Override
		public boolean onPopupDialog(int arg0, int arg1, int arg2) {
			return false;
		}

		@Override
		public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyCiPlusProtection(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyCiPlusRetentionLimitUpdate(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onPvrNotifyOverRun(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyParentalControl(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onPvrNotifyPlaybackBegin(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyPlaybackSpeedChange(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyPlaybackStop(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyPlaybackTime(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onPvrNotifyRecordSize(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onPvrNotifyRecordStop(int arg0) {
			return false;
		}

		@Override
		public boolean onPvrNotifyRecordTime(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onPvrNotifyTimeShiftOverwritesAfter(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onPvrNotifyTimeShiftOverwritesBefore(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onPvrNotifyUsbRemoved(int arg0, int arg1) {
			return false;
		}

		@Override
		public boolean onScreenSaverMode(int arg0, int arg1) {
			updateUi();
			return false;
		}

		@Override
		public boolean onSignalLock(int arg0) {
			// TODO Auto-generated method stub
			updateUi();
			return false;
		}

		@Override
		public boolean onSignalUnLock(int arg0) {
			return false;
		}

		@Override
		public boolean onTvProgramInfoReady(int arg0) {
			return false;
		}
    	
    }

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentid = overScanActivity.getCurrentFocus().getId();
		String str_val = new String();
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentid) {
			case R.id.linearlayout_factory_overscan_source:
				if (sourceindex == (validSources.size() - 1))
					sourceindex = 0;
				else if (sourceindex < (validSources.size() - 1))
					sourceindex++;
				text_factory_overscan_source_val.setText(sourcearray[validSources.get(sourceindex).ordinal()]);
				factoryManager.setOverScanSourceType(validSources.get(sourceindex));
				
                updateUi();

				if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV) {
					int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
				} else if (TvCommonManager.getInstance()
						.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
					TvChannelManager.getInstance().playDtvCurrentProgram();
				}
				break;
			case R.id.linearlayout_factory_overscan_hsize:
				if (hsizeval != 100) {
					hsizeval++;
				}
				str_val = Integer.toString(hsizeval);
				text_factory_overscan_hsize_val.setText(str_val);
				progress_factory_overscan_hsize.setProgress(hsizeval);
				factoryManager.setOverScanHsize((short) hsizeval);
				break;
			case R.id.linearlayout_factory_overscan_hposition:
				if (hpositionval != 100) {
					hpositionval++;
				}
				str_val = Integer.toString(hpositionval);
				text_factory_overscan_hposition_val.setText(str_val);
				progress_factory_overscan_hposition.setProgress(hpositionval);
				factoryManager.setOverScanHposition((short) hpositionval);
				break;
			case R.id.linearlayout_factory_overscan_vsize:
				if (vsizeval != 100) {
					vsizeval++;
				}
				str_val = Integer.toString(vsizeval);
				text_factory_overscan_vsize_val.setText(str_val);
				progress_factory_overscan_vsize.setProgress(vsizeval);
				factoryManager.setOverScanVsize((short) vsizeval);
				break;
			case R.id.linearlayout_factory_overscan_vposition:
				if (vpositionval != 100) {
					vpositionval++;
				}
				str_val = Integer.toString(vpositionval);
				text_factory_overscan_vpositon_val.setText(str_val);
				progress_factory_overscan_vpositon.setProgress(vpositionval);
				factoryManager.setOverScanVposition((short) vpositionval);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentid) {
			case R.id.linearlayout_factory_overscan_source:
				if (sourceindex == 0)
					sourceindex = validSources.size() - 1;
				else if (sourceindex > 0)
					sourceindex--;
				text_factory_overscan_source_val.setText(sourcearray[validSources.get(sourceindex).ordinal()]);
				factoryManager.setOverScanSourceType(validSources.get(sourceindex));

				updateUi();

				if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV) {
					int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
				} else if (TvCommonManager.getInstance()
						.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
					TvChannelManager.getInstance().playDtvCurrentProgram();
				}
				break;
			case R.id.linearlayout_factory_overscan_hsize:
				if (hsizeval != 0) {
					hsizeval--;
				}
				str_val = Integer.toString(hsizeval);
				text_factory_overscan_hsize_val.setText(str_val);
				progress_factory_overscan_hsize.setProgress(hsizeval);
				factoryManager.setOverScanHsize((short) hsizeval);
				break;
			case R.id.linearlayout_factory_overscan_hposition:
				if (hpositionval != 0) {
					hpositionval--;
				}
				str_val = Integer.toString(hpositionval);
				text_factory_overscan_hposition_val.setText(str_val);
				progress_factory_overscan_hposition.setProgress(hpositionval);
				factoryManager.setOverScanHposition((short) hpositionval);
				break;
			case R.id.linearlayout_factory_overscan_vsize:
				if (vsizeval != 0) {
					vsizeval--;
				}
				str_val = Integer.toString(vsizeval);
				text_factory_overscan_vsize_val.setText(str_val);
				progress_factory_overscan_vsize.setProgress(vsizeval);
				factoryManager.setOverScanVsize((short) vsizeval);
				break;
			case R.id.linearlayout_factory_overscan_vposition:
				if (vpositionval != 0) {
					vpositionval--;
				}
				str_val = Integer.toString(vpositionval);
				text_factory_overscan_vpositon_val.setText(str_val);
				progress_factory_overscan_vpositon.setProgress(vpositionval);
				factoryManager.setOverScanVposition((short) vpositionval);
				break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			overScanActivity.gotoPage(1);
			TvChannelManager.getInstance().unregisterOnTvPlayerEventListener(listener);
			listener = null;
			return bRet;
		case KeyEvent.KEYCODE_MENU:
			overScanActivity.returnRoot(2);
			TvChannelManager.getInstance().unregisterOnTvPlayerEventListener(listener);
			listener = null;
			return bRet;
		default:
			bRet = false;
			return bRet;
		}
		return bRet;
	}

    public void updateUi() {
        hsizeval = factoryManager.getOverScanHsize();
        hpositionval = factoryManager.getOverScanHposition();
        vsizeval = factoryManager.getOverScanVsize();
        vpositionval = factoryManager.getOverScanVposition();

        text_factory_overscan_hsize_val.setText(Integer.toString(hsizeval));
        text_factory_overscan_hposition_val.setText(Integer.toString(hpositionval));
        text_factory_overscan_vsize_val.setText(Integer.toString(vsizeval));
        text_factory_overscan_vpositon_val.setText(Integer.toString(vpositionval));
        progress_factory_overscan_hsize.setProgress(hsizeval);
        progress_factory_overscan_hposition.setProgress(hpositionval);
        progress_factory_overscan_vsize.setProgress(vsizeval);
        progress_factory_overscan_vpositon.setProgress(vpositionval);
    }
}
