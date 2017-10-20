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

package mstar.tvsetting.factory.ui.designmenu;

//import com.toptech.factorytools.IniFileReadWrite;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.desk.IFactoryDesk.MS_NLA_SET_INDEX;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.lang.String;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.util.Log;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import java.io.IOException;

import mstar.tvsetting.factory.ui.IniFileReadWrite;

public class UpgradeViewHolder {
    private DesignMenuActivity UpgradeActivity;

	private IFactoryDesk factoryManager;
	
    protected LinearLayout  factory_ci_upgrade;
    protected TextView text_factory_ci_upgrade;
    protected TextView text_factory_ci_display_val;

    protected LinearLayout  factory_hdcp_upgrade;
    protected TextView text_factory_hdcp_upgrade;
    protected TextView text_factory_hdcp_display_val;

    protected LinearLayout  factory_mac_upgrade;
    protected TextView text_factory_mac_upgrade;
    protected TextView text_factory_mac_display_val;

	protected LinearLayout  factory_sn_upgrade;
    protected TextView text_factory_sn_upgrade;
    protected TextView text_factory_sn_display_val;

    protected LinearLayout  factory_hdcp2_upgrade;
    protected TextView text_factory_hdcp2_upgrade;
    protected TextView text_factory_hdcp2_display_val;

    private short commendResult[] = null;
	private String strresult = null;
	private String filename = null;
	private String filename2 = null;

    public UpgradeViewHolder(DesignMenuActivity activity, IFactoryDesk factoryDesk) {
        UpgradeActivity = activity;
        this.factoryManager = factoryDesk;
    }

    void findView() {
        factory_ci_upgrade = (LinearLayout) UpgradeActivity
                .findViewById(R.id.linearlayout_factory_ci_upgrade);
        text_factory_ci_upgrade = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_ci_upgrade_val);
        text_factory_ci_display_val = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_ci_display_val);

        factory_hdcp_upgrade = (LinearLayout) UpgradeActivity
                .findViewById(R.id.linearlayout_factory_hdcp_upgrade);
        text_factory_hdcp_upgrade = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_hdcp_upgrade_val);
        text_factory_hdcp_display_val = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_hdcp_display_val);

        factory_mac_upgrade = (LinearLayout) UpgradeActivity
                .findViewById(R.id.linearlayout_factory_mac_upgrade);
        text_factory_mac_upgrade = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_mac_upgrade_val);
        text_factory_mac_display_val = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_mac_display_val);

		factory_sn_upgrade = (LinearLayout) UpgradeActivity
                .findViewById(R.id.linearlayout_factory_sn_upgrade);
        text_factory_sn_upgrade = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_sn_upgrade_val);
        text_factory_sn_display_val = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_sn_display_val);

        factory_hdcp2_upgrade = (LinearLayout) UpgradeActivity
                .findViewById(R.id.linearlayout_factory_hdcp2_upgrade);
        text_factory_hdcp2_upgrade = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_hdcp2_upgrade_val);
        text_factory_hdcp2_display_val = (TextView) UpgradeActivity
                .findViewById(R.id.textview_factory_hdcp2_display_val);
    }

    public boolean onCreate() {
    	filename = "/license/macandci.ini";
    	filename2 = "/license/hdcp2.ini";
        //text_factory_ci_display
        text_factory_ci_upgrade
                .setText("Click to Upgrade");
        try {
        text_factory_ci_display_val
                .setText(IniFileReadWrite.getProfileString(filename,
												"ci",
												"cidisplay",
												"please update"));

        } catch (IOException e) {
			e.printStackTrace();
		}
		//text_factory_hdcp_display
        text_factory_hdcp_upgrade
                .setText("Click to Upgrade");
        try {
        text_factory_hdcp_display_val
                .setText(IniFileReadWrite.getProfileString(filename,
												"hdcp",
												"hdcpdisplay",
												"please update"));

        } catch (IOException e) {
			e.printStackTrace();
		}
        //text_factory_mac_display
        text_factory_mac_upgrade
                .setText("Click to Upgrade");
        try {
        text_factory_mac_display_val
                .setText(IniFileReadWrite.getProfileString(filename,
												"mac",
												"macdisplay",
												"please update"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//text_factory_sn_display
        text_factory_sn_upgrade
                .setText("Click to Upgrade");
        try {
        text_factory_sn_display_val
                .setText(IniFileReadWrite.getProfileString(filename,
												"sn",
												"serialnumber",
												"please update"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//text_factory_hdcp2_display
        text_factory_hdcp2_upgrade
                .setText("Click to Upgrade");
        try {
        text_factory_hdcp2_display_val
                .setText(IniFileReadWrite.getProfileString(filename2,
												"hdcp2",
												"display",
												"please update"));

        } catch (IOException e) {
			e.printStackTrace();
		}
		
        registerListeners();
        return true;
    }

    private void registerListeners() {
        this.factory_ci_upgrade.setOnClickListener(listener);
        this.factory_hdcp_upgrade.setOnClickListener(listener);
        this.factory_mac_upgrade.setOnClickListener(listener);
		this.factory_sn_upgrade.setOnClickListener(listener);
		this.factory_hdcp2_upgrade.setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.linearlayout_factory_ci_upgrade:
                    Log.d("wym", "click to upgrade ci!\n");
                    try {
						commendResult = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_CIKEY_DIRECT);
					} catch (TvCommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                    //update the text_factory_ci_display_val
                    //if success, get the value from inifile
                    if (commendResult[0] == 0)
                    {
                    	strresult = "success";
                    	String displayval = null;
                    	try {
	                    	//IniReader IniFile = new IniReader("/license/macandci.ini");
							//text_factory_ci_display_val
	                		//	.setText(IniFile.getValue("ci", "cidisplay"));
	                		//Log.d("wym","ci:"+IniFile.getValue("ci", "cidisplay"));
	                		displayval = IniFileReadWrite.getProfileString(filename,
												"ci",
												"cidisplay",
												"please update");
							text_factory_ci_display_val
	                					.setText(displayval);
	                		Log.d("wym","ci:"+displayval);
                		} catch (IOException e) {
                			e.printStackTrace();
                		}
                    }
					else
						strresult = "fail";

					text_factory_ci_upgrade
                    		.setText(strresult);
                    break;
                case R.id.linearlayout_factory_hdcp_upgrade:
                    Log.d("wym", "click to upgrade hdcp!\n");
					try {
						commendResult = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_HDCPKEY_DIRECT);
					} catch (TvCommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    //update the text_factory_hdcp_display_val
                    //if success, get the value from inifile
                    if(commendResult[0] == 0)
                    {
                    	strresult = "success";
                    	String displayval = null;
                    	try {
	                    	//IniReader IniFile = new IniReader("/license/macandci.ini");
							//text_factory_hdcp_display_val
	                		//	.setText(IniFile.getValue("hdcp", "hdcpdisplay"));
	                		//Log.d("wym","hdcp:"+IniFile.getValue("hdcp", "hdcpdisplay"));
	                		displayval = IniFileReadWrite.getProfileString(filename,
												"hdcp",
												"hdcpdisplay",
												"please update");
							text_factory_hdcp_display_val
	                					.setText(displayval);
	                		Log.d("wym","hdcp:"+displayval);

                		} catch (IOException e) {
                			e.printStackTrace();
                		}
                    }
					else
					{
                    	strresult = "fail";
					}

					text_factory_hdcp_upgrade
                    		.setText(strresult);
                    break;
                case R.id.linearlayout_factory_mac_upgrade:
                    Log.d("wym", "click to upgrade mac!\n");
                    try {
						commendResult = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_MAC_DIRECT);
					} catch (TvCommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    //update the text_factory_mac_display_val
                    //if success, get the value from inifile
                    if (commendResult[0] == 0)
                    {
                    	strresult = "success";
                    	String displayval = null;
                    	try {
	                    	//IniReader IniFile = new IniReader("/license/macandci.ini");
							//text_factory_mac_display_val
	                		//	.setText(IniFile.getValue("mac", "macdisplay"));
	                		//Log.d("wym","mac:"+IniFile.getValue("mac", "macdisplay"));
	                		displayval = IniFileReadWrite.getProfileString(filename,"mac","macdisplay","please update");
							text_factory_mac_display_val
	                				.setText(displayval);
	                		Log.d("wym","mac:"+displayval);
                		} catch (IOException e) {
                			e.printStackTrace();
                		}
                    }
					else
						strresult = "fail";
					
					text_factory_mac_upgrade
                    		.setText(strresult);
                    break;
				case R.id.linearlayout_factory_sn_upgrade:
                    Log.d("wym", "click to upgrade sn!\n");
                    try {
						commendResult = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_SN_DIRECT);
					} catch (TvCommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                    //update the text_factory_sn_display_val
                    //if success, get the value from inifile
                    if (commendResult[0] == 0)
                    {
                    	strresult = "success";
                    	String displayval = null;
                    	try {
	                    	//IniReader IniFile = new IniReader("/license/macandci.ini");
							//text_factory_sn_display_val
	                		//	.setText(IniFile.getValue("sn", "serialnumber"));
	                		//Log.d("wym","mac:"+IniFile.getValue("sn", "serialnumber"));
	                		displayval = IniFileReadWrite.getProfileString(filename,"sn","serialnumber","please update");
							text_factory_sn_display_val
	                				.setText(displayval);
	                		Log.d("wym","sn:"+displayval);
                		} catch (IOException e) {
                			e.printStackTrace();
                		}
                    }
					else
						strresult = "fail";
					text_factory_sn_upgrade
                    		.setText(strresult);
					break;
			    case R.id.linearlayout_factory_hdcp2_upgrade:
                    Log.d("wym", "click to upgrade hdcp2!\n");
					try {
						commendResult = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_HDCP2KEY_DIRECT);
					} catch (TvCommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    //update the text_factory_hdcp_display_val
                    //if success, get the value from inifile
                    if(commendResult[0] == 0)
                    {
                    	strresult = "success";
                    	String displayval = null;
                    	try {
	                		displayval = IniFileReadWrite.getProfileString(filename2,
												"hdcp2",
												"display",
												"please update");
							text_factory_hdcp2_display_val
	                					.setText(displayval);
	                		Log.d("wym","hdcp2:"+displayval);

                		} catch (IOException e) {
                			e.printStackTrace();
                		}
                    }
					else
					{
                    	strresult = "fail";
					}

					text_factory_hdcp2_upgrade
                    		.setText(strresult);
                    break;
                default:
                    break;
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
        int currentid = UpgradeActivity.getCurrentFocus().getId();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                this.UpgradeActivity.returnRoot(/*DesignMenuActivity.UPGRADE_SETTING_PAGE*/11);
                break;
            default:
                bRet = false;
                break;
        }

		return bRet;
    }
}
