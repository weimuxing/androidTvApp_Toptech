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

package mstar.tvsetting.factory.ui.designmenu;

import android.R.integer;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tvapi.factory.vo.PanelVersionInfo;

import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;
import mstar.factorymenu.ui.R;
import mstar.factorymenu.ui.R.array;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;

public class PanelInfoViewHolder {
    private static final String TAG = "PanelInfoViewHolder";

    protected TextView text_panel_info_changelist_val;

    protected TextView text_panel_info_timode_val;
    protected TextView text_panel_info_bitmode_val;
    protected TextView text_panel_info_swap_val;
    protected TextView text_panel_info_dualport_val;
    protected TextView text_panel_info_mirror_val;
    protected TextView text_panel_info_volumecurve;
    protected TextView text_factory_otheroption_poweronmode_val;
    protected TextView textview_panel_info_display_logo_val;
    protected int m_timode_val;
    protected int m_bitmode_val;
    protected int m_swap_val;
    protected int m_dualport_val;
    protected int m_mirror_val;
    protected int m_volumecurve_val;
    private int displaylogoenableindex = 0;
    private int poweronmodeindex = 0;
    
    private String[] volumecure;

    private String[] displaylogoenable = {
            "Off", "On"
    };
    
    private String[] powerOnMode = {
            "Secondary", "Memory", "Direct"
    };
    
    private String[] mBitModeStrArray;

    private DesignMenuActivity mPanelInfoActivity;

    private String mPanelInfoChangelistVal = "0F0F0F";

    private TvFactoryManager mTvFactoryManager = null;
    
    private IFactoryDesk factoryManager;
    
    public PanelInfoViewHolder(DesignMenuActivity activity, IFactoryDesk factoryManager) {
        mPanelInfoActivity = activity;
        this.factoryManager = factoryManager;
    }

    public void findView() {
        text_panel_info_changelist_val = (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_panel_info_changelist_val);
        text_panel_info_timode_val = (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_panel_info_timode_val);
        text_panel_info_bitmode_val = (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_panel_info_bitmode_val);
        text_panel_info_swap_val = (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_panel_info_swap_val);
        text_panel_info_dualport_val = (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_panel_info_dualport_val);
        text_panel_info_mirror_val = (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_panel_info_mirror_val);
        text_panel_info_volumecurve = (TextView) mPanelInfoActivity
        		.findViewById(R.id.textview_panel_info_valumecurve_select);
        text_factory_otheroption_poweronmode_val = (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_factory_otheroption_poweronmode_val);
        textview_panel_info_display_logo_val =  (TextView) mPanelInfoActivity
                .findViewById(R.id.textview_panel_info_display_logo_val);
        
    }

    public void onCreate() {
        mTvFactoryManager = TvFactoryManager.getInstance();

        PanelVersionInfo panelInfo = mTvFactoryManager.getPanelVersionInfo();
        
        poweronmodeindex = factoryManager.getPowerOnMode();
        text_factory_otheroption_poweronmode_val.setText(powerOnMode[poweronmodeindex]);
        
        boolean logo_flag = false;
        try {
        	logo_flag = TvManager.getInstance().getEnvironment("display_logo").equals("on");
		} catch (Exception e) {
			// TODO: handle exception
		}
        displaylogoenableindex = logo_flag ? 1 : 0;
        textview_panel_info_display_logo_val.setText(displaylogoenable[displaylogoenableindex]);
        
        if (null != panelInfo) {
            mPanelInfoChangelistVal = Long.toHexString(panelInfo.u32Changelist);
        }
        volumecure = mPanelInfoActivity.getResources().getStringArray(R.array.str_arr_volumecurve_select);
        int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
        mBitModeStrArray = mPanelInfoActivity.getResources().getStringArray(R.array.str_arr_factory_panelinfo_text_bitmode);
        m_timode_val = retval[0];
        m_swap_val = retval[1];
        m_bitmode_val = retval[2];
        m_mirror_val = retval[3];
        m_volumecurve_val = retval[4];
        m_dualport_val = retval[5];
        
        text_panel_info_changelist_val.setText(mPanelInfoChangelistVal);
        if (m_volumecurve_val >= volumecure.length)
        text_panel_info_volumecurve.setText("undefined");
        else
        text_panel_info_volumecurve.setText(volumecure[m_volumecurve_val]);
        if (m_timode_val > 0)
            text_panel_info_timode_val.setText(R.string.str_textview_factory_otheroption_text_on);
        else
            text_panel_info_timode_val.setText(R.string.str_textview_factory_otheroption_text_off);
        text_panel_info_bitmode_val.setText(mBitModeStrArray[m_bitmode_val]);
        if (m_swap_val > 0)
            text_panel_info_swap_val.setText(R.string.str_textview_factory_otheroption_text_on);
        else
            text_panel_info_swap_val.setText(R.string.str_textview_factory_otheroption_text_off);
        if (m_dualport_val > 0)
            text_panel_info_dualport_val.setText(R.string.str_textview_factory_otheroption_text_on);
        else
            text_panel_info_dualport_val.setText(R.string.str_textview_factory_otheroption_text_off);
        if (m_mirror_val > 0)
            text_panel_info_mirror_val.setText(R.string.str_textview_factory_otheroption_text_on);
        else
            text_panel_info_mirror_val.setText(R.string.str_textview_factory_otheroption_text_off);

        
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bRet = true;
        int currentid = mPanelInfoActivity.getCurrentFocus().getId();
        Log.d("wym", "currentid="+currentid);
        //TVOS_COMMON_CMD_GET_PANEL_INFO    "GetPanelInfo"
        //TVOS_COMMON_CMD_SET_PANEL_INFO    "SetPanelInfo"
        //int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                	case R.id.linearlayout_panel_info_volumeselect:
                	{
                		m_volumecurve_val++;
                		if(m_volumecurve_val > (volumecure.length -1))
                			m_volumecurve_val = 0;
                		TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/volumecure*"+m_volumecurve_val);
                		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
                		Log.i("wxy","=====retval="+retval[4]+";"+retval[3]+";"+retval[2]);
                		text_panel_info_volumecurve.setText(volumecure[retval[4]]);
                	}
                	break;
                    case R.id.linearlayout_panel_info_timode:
                    {
                        m_timode_val = (m_timode_val>0)?0:1;
                        if (m_timode_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/timode*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/timode*1");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_timode_val = retval[0];
						
                        if (m_timode_val > 0)
                            text_panel_info_timode_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_timode_val.setText(R.string.str_textview_factory_otheroption_text_off);

                        break;
                    }
                    case R.id.linearlayout_panel_info_bitmode:
                    {
                        if (m_bitmode_val < 2) m_bitmode_val++;
                        else m_bitmode_val = 0;
                        
                        if (m_bitmode_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/bitmode*0");
                        else if (m_bitmode_val == 1)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/bitmode*1");
                        else
                        	TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/bitmode*2");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_bitmode_val = retval[2];
						
                        text_panel_info_bitmode_val.setText(mBitModeStrArray[m_bitmode_val]);

                        break;
                    }
                    case R.id.linearlayout_panel_info_swap:
                    {
                        m_swap_val = (m_swap_val>0)?0:1;
                        if (m_swap_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/swapmode*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/swapmode*1");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_swap_val = retval[1];
						
                        if (m_swap_val > 0)
                            text_panel_info_swap_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_swap_val.setText(R.string.str_textview_factory_otheroption_text_off);

                        break;
                    }
                    case R.id.linearlayout_panel_info_dualport:
                    {
                        m_dualport_val = (m_dualport_val>0)?0:1;
                        if (m_dualport_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/dualport*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/dualport*1");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_dualport_val = retval[5];
						
                        if (m_dualport_val > 0)
                            text_panel_info_dualport_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_dualport_val.setText(R.string.str_textview_factory_otheroption_text_off);

                        break;
                    }
                    case R.id.linearlayout_panel_info_mirror:
                    {
                        m_mirror_val = (m_mirror_val>0)?0:1;
                        if (m_mirror_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/mirrormode*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/mirrormode*1");

                        int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_mirror_val = retval[3];

						if (m_mirror_val > 0)
                            text_panel_info_mirror_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_mirror_val.setText(R.string.str_textview_factory_otheroption_text_off);
                        break;
                    }
                    case R.id.linearlayout_factory_otheroption_poweronmode:
                        if (poweronmodeindex != IFactoryDesk.EN_POWER_MODE_DIRECT)
                            poweronmodeindex++;
                        else
                            poweronmodeindex = 0;
                        text_factory_otheroption_poweronmode_val
                                .setText(powerOnMode[poweronmodeindex]);
                        factoryManager.setPowerOnMode(poweronmodeindex);
                        break;
                    case R.id.linearlayout_panel_info_display_logo:
                        if (displaylogoenableindex == 0) {
                        	displaylogoenableindex = 1;
//                            factoryManager.setDisplayLogoOnOff(true);
                    		try {
                    			TvManager.getInstance().setEnvironment("display_logo", "on");
                    		} catch (Exception e) {
                    			// TODO: handle exception
                    		}
                        } else {
                        	displaylogoenableindex = 0;
//                            factoryManager.setDisplayLogoOnOff(false);
                        	try {
                    			TvManager.getInstance().setEnvironment("display_logo", "off");
                    		} catch (Exception e) {
                    			// TODO: handle exception
                    		}
                        }
                        textview_panel_info_display_logo_val
                                .setText(displaylogoenable[displaylogoenableindex]);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                case R.id.linearlayout_panel_info_volumeselect:
            	{
            		if(m_volumecurve_val<1)
            			m_volumecurve_val = (volumecure.length - 1);
            		else
            		m_volumecurve_val--;
            		TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/volumecure*"+m_volumecurve_val);
            		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
            		text_panel_info_volumecurve.setText(volumecure[retval[4]]);
            	}
            	break;
                    case R.id.linearlayout_panel_info_timode:
                    {
                        m_timode_val = (m_timode_val>0)?0:1;
                        if (m_timode_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/timode*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/timode*1");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_timode_val = retval[0];
						
                        if (m_timode_val > 0)
                            text_panel_info_timode_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_timode_val.setText(R.string.str_textview_factory_otheroption_text_off);

                        break;
                    }
                    case R.id.linearlayout_panel_info_bitmode:
                    {
                        if (m_bitmode_val > 0) m_bitmode_val--;
                        else m_bitmode_val = 2;
                        
                        if (m_bitmode_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/bitmode*0");
                        else if (m_bitmode_val == 1)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/bitmode*1");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/bitmode*2");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_bitmode_val = retval[2];
						
                        text_panel_info_bitmode_val.setText(mBitModeStrArray[m_bitmode_val]);
                        break;
                    }
                    case R.id.linearlayout_panel_info_swap:
                    {
                        m_swap_val = (m_swap_val>0)?0:1;
                        if (m_swap_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/swapmode*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/swapmode*1");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_swap_val = retval[1];
                        if (m_swap_val > 0)
                            text_panel_info_swap_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_swap_val.setText(R.string.str_textview_factory_otheroption_text_off);

                        break;
                    }
                    case R.id.linearlayout_panel_info_dualport:
                    {
                        m_dualport_val = (m_dualport_val>0)?0:1;
                        if (m_dualport_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/dualport*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/dualport*1");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_dualport_val = retval[5];
                        if (m_dualport_val > 0)
                            text_panel_info_dualport_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_dualport_val.setText(R.string.str_textview_factory_otheroption_text_off);

                        break;
                    }
                    case R.id.linearlayout_panel_info_mirror:
                    {
                        m_mirror_val = (m_mirror_val>0)?0:1;
                        if (m_mirror_val == 0)
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/mirrormode*0");
                        else
                            TvCommonManager.getInstance().setTvosCommonCommand("SetPanelInfo/mirrormode*1");

						int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPanelInfo");
						m_mirror_val = retval[3];
                        if (m_mirror_val > 0)
                            text_panel_info_mirror_val.setText(R.string.str_textview_factory_otheroption_text_on);
                        else
                            text_panel_info_mirror_val.setText(R.string.str_textview_factory_otheroption_text_off);
                        break;
                    }
                    case R.id.linearlayout_factory_otheroption_poweronmode:
                        if (poweronmodeindex != IFactoryDesk.EN_POWER_MODE_DIRECT)
                            poweronmodeindex++;
                        else
                            poweronmodeindex = 0;
                        text_factory_otheroption_poweronmode_val
                                .setText(powerOnMode[poweronmodeindex]);
                        factoryManager.setPowerOnMode(poweronmodeindex);
                        break;
                    case R.id.linearlayout_panel_info_display_logo:
                        if (displaylogoenableindex == 0) {
                        	displaylogoenableindex = 1;
//                            factoryManager.setDisplayLogoOnOff(true);
                    		try {
                    			TvManager.getInstance().setEnvironment("display_logo", "on");
                    		} catch (Exception e) {
                    			// TODO: handle exception
                    		}
                        } else {
                        	displaylogoenableindex = 0;
//                            factoryManager.setDisplayLogoOnOff(false);
                        	try {
                    			TvManager.getInstance().setEnvironment("display_logo", "off");
                    		} catch (Exception e) {
                    			// TODO: handle exception
                    		}
                        }
                        textview_panel_info_display_logo_val
                                .setText(displaylogoenable[displaylogoenableindex]);
                        break;
                    default:
                        break;
                }
                break;
            default:
                bRet = false;
                break;
        }
        return bRet;
    }
}
