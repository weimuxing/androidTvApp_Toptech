//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

package com.toptech.factorytools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.factory.FactoryManager;
import com.mstar.android.tvapi.factory.vo.PanelVersionInfo;

public class PanelSettingActivity  extends Activity {

    protected TextView text_panel_info_changelist_val;


    private FactoryManager fm = TvManager.getInstance().getFactoryManager();

    private String panelInfoChangelistVal = "0F0F0F";
 
    //zb20140922 add
	private TextView text_factory_ti_mode_val;
	private TextView text_factory_mirror_mode_val;
	private TextView text_factory_swap_mode_val;
	private TextView text_factory_panel_bit_nums_val;
	private TextView text_factory_bPolPWM_val;
	private TextView text_factory_bPeriodPWM_val;
	private TextView text_factory_bDutyPWM_val;
	private TextView text_factory_bDivPWM_val;
	private TextView text_factory_bMaxDutyPWM_val;
	private TextView text_factory_bMinDutyPWM_val;
	private String m_ucTiBitMode_UIShow = "8";
	private String m_pCustomerName="\"/config/model/Customer_1.ini\"";
	private String m_pPanelName = "\"/config/panel/FullHD_CMO216_H1L01.ini\"";
	
	private String strPreTiMode=null;
	private String strCurTiMode=null;
	private String strPreSwapPort=null;
	private String strCurSwapPort=null;
	private String strPreMirrorMode=null;
	private String strCurMirrorMode=null;
	private String strPreTiBitNum=null;
	private String strCurTiBitNum=null;
	private String strPreMaxPWMDuty=null;
	private String strCurMaxPWMDuty=null;
	private String strPreMinPWMDuty=null;
	private String strCurMinPWMDuty=null;
	private String strPrePWMPeriod=null;
	private String strCurPWMPeriod=null;
	private String strPrePWMDuty=null;
	private String strCurPWMDuty=null;
	private String strPrePWMDiv=null;
	private String strCurPWMDiv=null;
	private String strPrePWMPol=null;
	private String strCurPWMPol=null;
	private int tmpVal=0;
	private List<Map<String, String>> pnlParamsList=new ArrayList<Map<String, String>>();
	private Map<String, String> pnlParamsItem=null;
	
	private static int tmpPWMDuty=0;
	private static int tmpPWMPeriod=0;
	private static int tmpPWMMaxDuty=0;
	private static int tmpPWMMinDuty=0;
	private static int tmpPWMDiv=0;
	private static int mirror=0;
	private static String mirrorModeFlag="";
    //end
	
	@Override
	protected void onCreate(Bundle arg0) {
				super.onCreate(arg0);
				
				setContentView(R.layout.panelinfo);
				getPanenFileName();
				getDataFromIni();
				findView();
				loadData2UI();
				pnlParamsList.clear();
	}
	
    public void getPanenFileName() {
        try {
        	m_pCustomerName=IniFileReadWrite
					.getProfileString("/config/sys.ini", "model",
							"gModelName",
							"/config/model/Customer_1.ini");
        	Log.d("lyp","m_pCustomerName==="+m_pCustomerName);
        	m_pCustomerName = m_pCustomerName.replace("\"", "");
			m_pPanelName = IniFileReadWrite
					.getProfileString(m_pCustomerName, "panel",
							"m_pPanelName",
							"/config/panel/FullHD_CMO216_H1L01.ini");
		} catch (IOException e) {
			e.printStackTrace();
		}
		m_pPanelName = m_pPanelName.replace("\"", "");
		Log.d("lyp","m_pCustomerName 2==="+m_pCustomerName);
        Log.d("lyp","m_pPanelName ==="+m_pPanelName);
    }

    public void findView() {
        text_panel_info_changelist_val = (TextView)findViewById(R.id.textview_panel_info_changelist_val);
        //zb20140922 add
		text_factory_ti_mode_val = (TextView)findViewById(R.id.textview_factory_ti_mode_val);
		text_factory_mirror_mode_val = (TextView)findViewById(R.id.textview_factory_mirror_mode_val);
		text_factory_swap_mode_val = (TextView)findViewById(R.id.textview_factory_swap_mode_val);
		text_factory_panel_bit_nums_val = (TextView)findViewById(R.id.textview_factory_panel_bit_nums_val);
		text_factory_bPolPWM_val = (TextView)findViewById(R.id.textview_factory_bPolPWM_val);
		text_factory_bPeriodPWM_val = (TextView)findViewById(R.id.textview_factory_bPeriodPWM_val);
		text_factory_bDutyPWM_val = (TextView)findViewById(R.id.textview_factory_bDutyPWM_val);
		text_factory_bDivPWM_val = (TextView)findViewById(R.id.textview_factory_bDivPWM_val);
		text_factory_bMaxDutyPWM_val = (TextView)findViewById(R.id.textview_factory_bMaxDutyPWM_val);
		text_factory_bMinDutyPWM_val = (TextView)findViewById(R.id.textview_factory_bMinDutyPWM_val);
		//end
    }

    public void onCreate() {
        PanelVersionInfo panelInfo = new PanelVersionInfo();
        try {
            panelInfo = fm.panelGetVersionInfo();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }

        if (null != panelInfo) {
            panelInfoChangelistVal = Long.toHexString(panelInfo.u32Changelist);
        }
        text_panel_info_changelist_val.setText(panelInfoChangelistVal);
        
        //zb20140922 add
        getDataFromIni();
		loadData2UI();
		pnlParamsList.clear();
		//end
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	Log.d("lyp","#12.12<#> sss keyCode = "+keyCode );
        boolean bRet = true;
        int currentid = getCurrentFocus().getId();//zb20140922 add
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
            	if(updateData())
            		reboot();//zb20140922 add
            	finish();
                break;
           //zb20140922 add
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            	switch (currentid) {
            	case R.id.linearlayout_panel_info_changelist:
            		Log.d("lyp","change ");
            		

            		Log.d("lyp","change ok");
            		break;
    			case R.id.linearlayout_factory_ti_mode:
    				strCurTiMode = valAdd_0_1(strCurTiMode);
    				EnumReg.TiMode.setRegVal(strCurTiMode);
    				setReg(EnumReg.TiMode);
    				text_factory_ti_mode_val.setText(strCurTiMode);
    				break;

    			case R.id.linearlayout_factory_mirror_mode:
    				strCurMirrorMode = valAdd_0_1(strCurMirrorMode);
    				text_factory_mirror_mode_val.setText(strCurMirrorMode);
    				break;

    			case R.id.linearlayout_factory_swap_mode:
    				strCurSwapPort = valAdd_0_1(strCurSwapPort);
    				EnumReg.Swap.setRegVal(strCurSwapPort);
    				setReg(EnumReg.Swap);
    				text_factory_swap_mode_val.setText(strCurSwapPort);
    				break;

    			case R.id.linearlayout_factory_panel_bit_nums:
    				strCurTiBitNum = val_add_bitmode_SetReg(strCurTiBitNum);
    				if (strCurTiBitNum.equals("2")) {
    					m_ucTiBitMode_UIShow = "8";
    				} else if (strCurTiBitNum.equals("3")){
    					m_ucTiBitMode_UIShow = "6";
    				}else{
        					m_ucTiBitMode_UIShow = "10";
        			} 
    				text_factory_panel_bit_nums_val.setText(m_ucTiBitMode_UIShow);
    				break;

    			case R.id.linearlayout_factory_bPolPWM:
    				strCurPWMPol = valAdd_0_1(strCurPWMPol);
    				EnumReg.PWMPol.setRegVal(strCurPWMPol);
    				setReg(EnumReg.PWMPol);
    				text_factory_bPolPWM_val.setText(strCurPWMPol);
    				break;
    			case R.id.linearlayout_factory_bPeriodPWM:
    				if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)
    				{
    					tmpPWMPeriod +=10;;
    					if(tmpPWMPeriod>0xFFFF)
    						tmpPWMPeriod=0;
    				}
    				else {
						tmpPWMPeriod -=10;
						if(tmpPWMPeriod<0)
    						tmpPWMPeriod=0xFFFF;
					}
    				strCurPWMPeriod="0x"+Integer.toHexString(tmpPWMPeriod).toUpperCase();
    				updatePWMreg("PWMPeriod", "10320A",tmpPWMPeriod&0xFF );
    				updatePWMreg("PWMPeriod", "10320B",(tmpPWMPeriod>>8)&0xFF );
    				text_factory_bPeriodPWM_val.setText(strCurPWMPeriod);
    				break;
    			case R.id.linearlayout_factory_bDutyPWM:
    				if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)
    				{
    					tmpPWMDuty +=10;;
    					if(tmpPWMDuty>0xFFFF)
    						tmpPWMDuty=0;
    				}
    				else {
						tmpPWMDuty -=10;
						if(tmpPWMDuty<0)
    						tmpPWMDuty=0xFFFF;
					}
    				strCurPWMDuty="0x"+Integer.toHexString(tmpPWMDuty).toUpperCase();
    				updatePWMreg("PWMDuty", "10320C",tmpPWMDuty&0xFF );
    				updatePWMreg("PWMPeriod", "10320D",(tmpPWMDuty>>8)&0xFF );
    				text_factory_bDutyPWM_val.setText(strCurPWMDuty);
    				break;
    			case R.id.linearlayout_factory_bDivPWM:
    				if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)
    				{
    					++tmpPWMDiv;
    					if(tmpPWMDiv>0xFF)
    						tmpPWMDiv=0;
    				}
    				else {
						--tmpPWMDiv;
						if(tmpPWMDiv<0)
    						tmpPWMDiv=0xFF;
					}
    				strCurPWMDiv="0x"+Integer.toHexString(tmpPWMDiv).toUpperCase();
    				updatePWMreg("PWMDiv", "10320E",tmpPWMDiv&0xFF );
    				text_factory_bDivPWM_val.setText(strCurPWMDiv);
    				break;
    			case R.id.linearlayout_factory_bMaxDutyPWM:
    				if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)
    				{
    					tmpPWMMaxDuty +=10;
    					if(tmpPWMMaxDuty>0xFFFF)
    						tmpPWMMaxDuty=0;
    				}
    				else {
						tmpPWMMaxDuty -=10;
						if(tmpPWMMaxDuty<0)
    						tmpPWMMaxDuty=0xFFFF;
					}
    				strCurMaxPWMDuty="0x"+Integer.toHexString(tmpPWMMaxDuty).toUpperCase();
    				updatePWMreg("PWMDuty", "10320C",tmpPWMMaxDuty&0xFF );
    				updatePWMreg("PWMPeriod", "10320D",(tmpPWMMaxDuty>>8)&0xFF );
    				text_factory_bMaxDutyPWM_val.setText(strCurMaxPWMDuty);
    				break;
    			case R.id.linearlayout_factory_bMinDutyPWM:
    				if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)
    				{
    					tmpPWMMinDuty +=10;
    					if(tmpPWMMinDuty>0xFFFF)
    						tmpPWMMinDuty=0;
    				}
    				else {
						tmpPWMMinDuty -=10;
						if(tmpPWMMinDuty<0)
							tmpPWMMinDuty=0xFFFF;
					}
    				strCurMinPWMDuty="0x"+Integer.toHexString(tmpPWMMinDuty).toUpperCase();
    				updatePWMreg("PWMDuty", "10320C",tmpPWMMinDuty&0xFF );
    				updatePWMreg("PWMPeriod", "10320D",(tmpPWMMinDuty>>8)&0xFF );
    				text_factory_bMinDutyPWM_val.setText(strCurMinPWMDuty);
    				break;
    			default:
    				break;
    			}
            	break;
            //end
            default:
                bRet = false;
                break;
        }
        return bRet;
    }
    
    //zb20140922 add
    private int stringToint(String val)
    {
    	String tmp=val.replace("0x", "");
    	char[] charX=tmp.toCharArray();
    	int[] tmps=new int[charX.length];//{0,0,0,0};
    	
    	for(int i=0;i<charX.length;i++)
    	{
    		if(charX[i]>='0' && charX[i]<='9')
    			tmps[i] =  charX[i] - '0';
    		else if (charX[i]>='A' && charX[i]<='F')
    			tmps[i] =  charX[i] - 'A' +10;
    		else if	(charX[i]>='a' && charX[i]<='f')
    			tmps[i] =  charX[i] - 'a' +10;
    		else 
    			tmps[i] = 0xFF;
    	}
    	switch(tmps.length)
    	{
    	case 1:
    		tmpVal=tmps[0];
    		break;
    	case 2:
    		tmpVal=tmps[0]*16+tmps[1];
    		break;
    	case 3:
    		tmpVal=tmps[0]*256+tmps[1]*16+tmps[2];
    		break;
    	case 4:
    		tmpVal=tmps[0]*256*16+tmps[1]*256+tmps[2]*16+tmps[3];
    		break;
    	}
    	return tmpVal;
    }
 
	public void loadData2UI() {
		text_factory_ti_mode_val.setText(strCurTiMode);
		if(mirrorModeFlag.equals("False"))
			text_factory_mirror_mode_val.setText("0"/*(PanelInfoActivity.getResources()
					.getStringArray(R.array.mirror_mode))[0]*/);
		else
			text_factory_mirror_mode_val.setText("1"/*(PanelInfoActivity.getResources()
				.getStringArray(R.array.mirror_mode))[mirror]*/);
		
		text_factory_swap_mode_val.setText(strCurSwapPort);
		text_factory_bPolPWM_val.setText(strCurPWMPol);
		text_factory_bDivPWM_val.setText(strCurPWMDiv);
		text_factory_bPeriodPWM_val.setText(strCurPWMPeriod);
		text_factory_bDutyPWM_val.setText(strCurPWMDuty);
		text_factory_bMaxDutyPWM_val.setText(strCurMaxPWMDuty);
		text_factory_bMinDutyPWM_val.setText(strCurMinPWMDuty);
		//modify by wxy
		
		if (strCurTiBitNum.equals("2")){
			m_ucTiBitMode_UIShow = "8";
		} else if(strCurTiBitNum.equals("3")){
			m_ucTiBitMode_UIShow = "6";
		}else{
			m_ucTiBitMode_UIShow = "10";
		}
		text_factory_panel_bit_nums_val.setText(m_ucTiBitMode_UIShow);
		//modify end
	}
    public void getDataFromIni() {
		try {
			strPreTiMode=strCurTiMode = IniFileReadWrite.getProfileString(
					m_pPanelName, "panel", "m_bPanelLVDS_TI_MODE", "0");
			strPreMirrorMode=strCurMirrorMode = IniFileReadWrite.getProfileString(m_pCustomerName,
					"MISC_MIRROR_CFG", "MIRROR_OSD_TYPE", "0");
			mirrorModeFlag=IniFileReadWrite.getProfileString(m_pCustomerName,
					"MISC_MIRROR_CFG", "MIRROR_OSD", "False");
			
			strPreSwapPort=strCurSwapPort = IniFileReadWrite.getProfileString(m_pPanelName,
					"panel", "m_bPanelSwapPort", "0");
			strPreTiBitNum=strCurTiBitNum = IniFileReadWrite.getProfileString(m_pPanelName,
					"panel", "m_ucTiBitMode", "0");
			strPrePWMPeriod=strCurPWMPeriod = IniFileReadWrite.getProfileString(m_pPanelName,
					"panel", "u32PeriodPWM", "0");
			strPrePWMDuty=strCurPWMDuty = IniFileReadWrite.getProfileString(m_pPanelName,
					"panel", "u32DutyPWM", "0");
			strPrePWMPol=strCurPWMPol = IniFileReadWrite.getProfileString(m_pPanelName, "panel",
					"bPolPWM", "0");
			strPrePWMDiv=strCurPWMDiv = IniFileReadWrite.getProfileString(m_pPanelName, "panel",
					"u16DivPWM", "0");
			strPreMaxPWMDuty=strCurMaxPWMDuty = IniFileReadWrite.getProfileString(m_pPanelName,
					"panel", "u16MaxPWMvalue", "0");
			strPreMinPWMDuty=strCurMinPWMDuty = IniFileReadWrite.getProfileString(m_pPanelName,
					"panel", "u16MinPWMvalue", "0");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tmpPWMDiv=stringToint(strCurPWMDiv);
		tmpPWMDuty=stringToint(strCurPWMDuty);
		tmpPWMMaxDuty=stringToint(strCurMaxPWMDuty);
		tmpPWMMinDuty=stringToint(strCurMinPWMDuty);
		tmpPWMPeriod=stringToint(strCurPWMPeriod);
		mirror=Integer.parseInt(strCurMirrorMode);
	}

    private void updatePWMreg(String id,String reg,int val)
    {
    	String cmd=id+"/12" + reg + Integer.toHexString(val).toUpperCase();
    	Log.d("lyp","#12.12<#> updatePWMreg - cmd = "+cmd );
		try {
			TvManager.getInstance().setTvosCommonCommand(cmd);
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public void setReg(EnumReg currentReg) {
		String regCMD = "11" + currentReg.address + currentReg.currentBit
				+ currentReg.regVal;
    	Log.d("lyp","#12.12<#> setReg - regCMD = "+regCMD );
		try {
			TvManager.getInstance().setTvosCommonCommand(regCMD);
		} catch (TvCommonException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * regTiMode = "103280"+"2"; 
	 * regSwap = "103294"+"0"; 
	 * regMirror ="10FFFF"+"7" 
	 * regBitNums7 = "103296"+"1";     //modify by wxy  103296bit0 bit1 for bitnums
	 * regBitNums6 ="103296"+"0";		 //modify by wxy
	 */
	enum EnumReg {
		//modify by wxy
		TiMode("103280", "2", "0"), Swap("103294", "0", "0"), Mirror("10FFFF",
				"7", "0"), BitNum7("103296", "1", "0"), BitNum6("103296", "0",
				"0"),PWMPol("10320F","1","0");
		//modify end

		private EnumReg(String add, String bit, String regVal) {
			this.address = add;
			this.currentBit = bit;
			this.regVal = regVal;
		}

		public void setRegVal(String regVal) {
			this.regVal = regVal;
		}

		public String toString() {
			return super.toString() + "(add=" + address + ",bit=" + currentBit
					+ ",regval=" + regVal + ")";
		}

		private String address;
		private String currentBit;
		private String regVal;
	}

	public String valAdd_0_1(String val) {
		String retVal = "0";
		if (val.equals("0")) {
			retVal = "1";
		} else {
			retVal = "0";
		}
		return retVal;
	}

	public String val_add_bitmode_SetReg(String val) {
		// #TI_10BIT_MODE = 0,#TI_8BIT_MODE = 2,#TI_6BIT_MODE = 3,
		//10->8bit	11->6bit other->10bit
		//modify by wxy
		if (val.equals("0")) {
			strCurTiBitNum = "2"; // 1,0

			EnumReg.BitNum7.setRegVal("1");
			setReg(EnumReg.BitNum7);

			EnumReg.BitNum6.setRegVal("0");
			setReg(EnumReg.BitNum6);

		} else if (val.equals("2")) {
			strCurTiBitNum = "3";// 1,1
			EnumReg.BitNum7.setRegVal("1");
			setReg(EnumReg.BitNum7);

			EnumReg.BitNum6.setRegVal("1");
			setReg(EnumReg.BitNum6);

		} else if (val.equals("3")) {
			strCurTiBitNum = "0";// 0,0
			EnumReg.BitNum7.setRegVal("0");
			setReg(EnumReg.BitNum7);

			EnumReg.BitNum6.setRegVal("0");
			setReg(EnumReg.BitNum6);
		}
		return strCurTiBitNum;
		//modify end
	}
	private boolean updateMirrorMode()
	{
		boolean ret=false;
		if((!strPreMirrorMode.equals(strCurMirrorMode))&&(!strCurMirrorMode.equals("0")))
		{
			List<Map<String, String>> mirrorParamsList=new ArrayList<Map<String, String>>();
			Map<String, String> mirrorMap =new HashMap<String, String>();
			mirrorMap.put("id", "MIRROR_OSD");
			mirrorMap.put("val", "True");
			mirrorParamsList.add(mirrorMap);
			
//			mirrorMap =new HashMap<String, String>();
//			mirrorMap.put("id", "MIRROR_OSD_TYPE");
//			mirrorMap.put("val", strCurMirrorMode);
//			mirrorParamsList.add(mirrorMap);
			
			mirrorMap =new HashMap<String, String>();
			mirrorMap.put("id", "MIRROR_VIDEO");
			mirrorMap.put("val", "True");
			mirrorParamsList.add(mirrorMap);
			
//			mirrorMap =new HashMap<String, String>();
//			mirrorMap.put("id", "MIRROR_VIDEO_TYPE");
//			mirrorMap.put("val", strCurMirrorMode);
//			mirrorParamsList.add(mirrorMap);
			
			try {
				ret=IniFileReadWrite.setProfileString(m_pCustomerName, "MISC_MIRROR_CFG", mirrorParamsList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(strCurMirrorMode.equals("0"))
		{
			List<Map<String, String>> mirrorParamsList=new ArrayList<Map<String, String>>();
			Map<String, String> mirrorMap =new HashMap<String, String>();
			mirrorMap.put("id", "MIRROR_OSD");
			mirrorMap.put("val", "False");
			mirrorParamsList.add(mirrorMap);
			
//			mirrorMap =new HashMap<String, String>();
//			mirrorMap.put("id", "MIRROR_OSD_TYPE");
//			mirrorMap.put("val", strCurMirrorMode);
//			mirrorParamsList.add(mirrorMap);
			
			mirrorMap =new HashMap<String, String>();
			mirrorMap.put("id", "MIRROR_VIDEO");
			mirrorMap.put("val", "False");
			mirrorParamsList.add(mirrorMap);
			
//			mirrorMap =new HashMap<String, String>();
//			mirrorMap.put("id", "MIRROR_VIDEO_TYPE");
//			mirrorMap.put("val", strCurMirrorMode);
//			mirrorParamsList.add(mirrorMap);
			
			try {
				ret=IniFileReadWrite.setProfileString(m_pCustomerName, "MISC_MIRROR_CFG", mirrorParamsList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	private boolean updateData()
	{
		boolean ret= false;
		
		if(updateMirrorMode())
			ret =  true;
		
		pnlParamsList.clear();
		if(!strPreSwapPort.equals(strCurSwapPort))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "m_bPanelSwapPort");
			pnlParamsItem.put("val", strCurSwapPort);
			pnlParamsList.add(pnlParamsItem);
		}
		if(!strPreTiMode.equals(strCurTiMode))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "m_bPanelLVDS_TI_MODE");
			pnlParamsItem.put("val", strCurTiMode);
			pnlParamsList.add(pnlParamsItem);
		}
		if(!strPreTiBitNum.equals(strCurTiBitNum))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "m_ucTiBitMode");
			pnlParamsItem.put("val", strCurTiBitNum);
			pnlParamsList.add(pnlParamsItem);
		}
		
		if(!strPrePWMPeriod.equals(strCurPWMPeriod))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "u32PeriodPWM");
			pnlParamsItem.put("val", strCurPWMPeriod);
			pnlParamsList.add(pnlParamsItem);
		}
		if(!strPrePWMDuty.equals(strCurPWMDuty))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "u32DutyPWM");
			pnlParamsItem.put("val", strCurPWMDuty);
			pnlParamsList.add(pnlParamsItem);
		}
		if(!strPrePWMDiv.equals(strCurPWMDiv))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "u16DivPWM");
			pnlParamsItem.put("val", strCurPWMDiv);
			pnlParamsList.add(pnlParamsItem);
		}
		
		if(!strPrePWMPol.equals(strCurPWMPol))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "bPolPWM");
			pnlParamsItem.put("val", strCurPWMPol);
			pnlParamsList.add(pnlParamsItem);
		}
		
		if(!strPreMaxPWMDuty.equals(strCurMaxPWMDuty))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "u16MaxPWMvalue");
			pnlParamsItem.put("val", strCurMaxPWMDuty);
			pnlParamsList.add(pnlParamsItem);
		}
		if(!strPreMinPWMDuty.equals(strCurMinPWMDuty))
		{
			pnlParamsItem=new HashMap<String, String>();
			pnlParamsItem.put("id", "u16MinPWMvalue");
			pnlParamsItem.put("val", strCurMinPWMDuty);
			pnlParamsList.add(pnlParamsItem);
		}
		if(pnlParamsList.size()>0)
		{
			try {
				ret=IniFileReadWrite.setProfileString(m_pPanelName, "panel", pnlParamsList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	public void reboot() {
		if(strPreMirrorMode.equals(strCurMirrorMode)
		   &&strPreTiBitNum.equals(strCurTiBitNum)
		   &&strPreTiMode.equals(strCurTiMode)
		   &&strPreSwapPort.equals(strCurSwapPort)
		   &&strPrePWMDiv.equals(strCurPWMDiv)
		   &&strPrePWMPeriod.equals(strCurPWMPeriod)
		   &&strPrePWMPol.equals(strCurPWMPol)
		   &&strPrePWMDuty.equals(strCurPWMDuty)
		   &&strPreMaxPWMDuty.equals(strCurMaxPWMDuty)
		   &&strPreMinPWMDuty.equals(strCurMinPWMDuty)
			)
			return ;
		
		try {
			TvManager.getInstance().setEnvironment("db_table", "0");
			TvManager.getInstance().setEnvironment("top_auto_pwr_on", "1");
		} catch (TvCommonException e) {
			e.printStackTrace();
		}
		TvCommonManager.getInstance().rebootSystem("reboot");
	}
}
