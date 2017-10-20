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

package com.mstar.tv.tvplayer.ui.holder;

import java.util.Locale;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tvapi.common.vo.DTVSpecificProgInfo;
import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEitInfo;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.tv.tvplayer.ui.component.PasswordChangeDialog;
import com.mstar.tv.tvplayer.ui.component.PasswordCheckDialog;
import com.mstar.tv.tvplayer.ui.dtv.parental.dvb.BlockProgramActivity;
import com.mstar.tv.tvplayer.ui.dtv.parental.dvb.ParentalContentLockActivity;
import com.mstar.tv.tvplayer.ui.dtv.parental.dvb.ParentalGuidanceActivity;
import com.mstar.tv.tvplayer.ui.dtv.parental.dvb.SetParentalPwdActivity;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.util.Constant;
import com.mstar.util.Utility;
import android.os.SystemProperties;

public class ParentalControlViewHolder {
    private static final String TAG = "ParentalControlViewHolder";

    protected LinearLayout linear_lock_system;

    protected LinearLayout linear_set_pwd;

    protected LinearLayout linear_block_program;

    protected LinearLayout linear_parental_contentlock;

    protected LinearLayout linear_parental_guidance;

    protected LinearLayout linear_parental_cipincode;

    protected TextView text_lock_system;

    protected TextView textt_set_pwd;

    protected TextView text_block_program;

    protected TextView text_parental_guidance;

    private TextView text_parental_guidance_value;

    protected TextView text_parental_contentlock;

    protected TextView lock_status;

    private MainMenuActivity mainMenuActivity;

    private int focusedid = 0x00000000;

    private EditText mEditPin1;

    private EditText mEditPin2;

    private EditText mEditPin3;

    private EditText mEditPin4;

    private PasswordCheckDialog mPasswordLock = null;

    private PasswordChangeDialog mPasswordChange = null;

    private boolean mIsControlPermitted = false;

    private int mCountry = TvCountry.OTHERS;

    public ParentalControlViewHolder(MainMenuActivity activity) {
        this.mainMenuActivity = activity;
    }

    private OnClickListener listener = null ;
    public void findViews() {
        mCountry = TvChannelManager.getInstance().getSystemCountryId();
        linear_lock_system = (LinearLayout) mainMenuActivity
                .findViewById(R.id.linearlayout_lock_system);
        linear_set_pwd = (LinearLayout) mainMenuActivity.findViewById(R.id.linearlayout_set_pwd);
        linear_block_program = (LinearLayout) mainMenuActivity
                .findViewById(R.id.linearlayout_block_program);
        linear_parental_guidance = (LinearLayout) mainMenuActivity
                .findViewById(R.id.linearlayout_parental_guidance);
        linear_parental_contentlock = (LinearLayout) mainMenuActivity
                .findViewById(R.id.linearlayout_parental_contentlock);
        linear_parental_cipincode = (LinearLayout) mainMenuActivity
                .findViewById(R.id.linearlayout_parental_cipincode);

        text_lock_system = (TextView) mainMenuActivity.findViewById(R.id.textview_lock_system);
        textt_set_pwd = (TextView) mainMenuActivity.findViewById(R.id.textview_set_pwd);
        text_block_program = (TextView) mainMenuActivity.findViewById(R.id.textview_block_program);
        text_parental_guidance = (TextView) mainMenuActivity
                .findViewById(R.id.textview_parental_guidance);
        text_parental_guidance_value = (TextView) mainMenuActivity
                .findViewById(R.id.textview_parental_guidance_value);
        text_parental_contentlock = (TextView) mainMenuActivity
                .findViewById(R.id.textview_parental_contentlock);
        lock_status = (TextView) mainMenuActivity.findViewById(R.id.textview_lock_system_statue);

        if (TvCountry.SINGAPORE == mCountry) {
            int currentRate = TvParentalControlManager.getInstance().getParentalControlRating();
            text_parental_guidance_value.setText(Utility.getParentalGuideAgeString(currentRate, mCountry));
        }

        if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB) {
            linear_parental_contentlock.setVisibility(View.VISIBLE);
        } else {
            linear_parental_contentlock.setVisibility(View.GONE);
        }
		
        if(SystemProperties.getBoolean("persist.sys.no_dtv",false))
        linear_parental_guidance.setVisibility(View.GONE);
		
        setOnClickLisenters();
        //setOnFocusChangeListeners();
        setOnTouchListeners();
        findViewCiPinCode();

        mPasswordLock = new PasswordCheckDialog(mainMenuActivity, R.layout.password_check_dialog_4_digits) {
            @Override
            public String onCheckPassword() {
                // Zero Filled to make a 4-digit String
                return String.format(Locale.ENGLISH,"%04d", TvParentalControlManager.getInstance().getParentalPassword());
            }

            @Override
            public void onPassWordCorrect() {
                mToast.cancel();
                mToast = Toast.makeText(mainMenuActivity, mainMenuActivity.getResources().getString(R.string.str_check_password_pass) , Toast.LENGTH_SHORT);
                mToast.show();
                cancel();

                if (mainMenuActivity.getIntent() == null) {
                    Intent intent = new Intent(TvIntent.MAINMENU);
                    intent.putExtra("currentPage", MainMenuActivity.LOCK_PAGE);
                    intent.putExtra(Constant.PARENTAL_CONTROL_MENU_PERMITTED, true);
                    mainMenuActivity.setIntent(intent);
                } else {
                    mainMenuActivity.getIntent().putExtra(Constant.PARENTAL_CONTROL_MENU_PERMITTED, true);
                }
                setControlPermitted(true);
                updateUi();
            }

            @Override
            public void onKeyDown(int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_MENU == keyCode) {
                    cancel();
                }
            }

            @Override
            public void onShow() {
                LittleDownTimer.pauseMenu();
                View view = mainMenuActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate().alpha(0f)
                            .setDuration(mainMenuActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onCancel() {
                View view = mainMenuActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate().alpha(1f)
                            .setDuration(mainMenuActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onDismiss() {
                LittleDownTimer.resetMenu();
                LittleDownTimer.resumeMenu();
            }
        };

        mPasswordChange = new PasswordChangeDialog(mainMenuActivity, R.layout.password_change_dialog_4_digits) {
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_MENU == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
                    cancel();
                    return true;
                }
                return false;
            }

            @Override
            public String onCheckPassword() {
                // Zero Filled to make a 4-digit String
                return String.format(Locale.ENGLISH,"%04d", TvParentalControlManager.getInstance().getParentalPassword());
            }
            @Override
            public void setPassword(String newPassword) {
                TvParentalControlManager.getInstance().setParentalPassword(Integer.valueOf(newPassword));
            }

            @Override
            public void onShow() {
                LittleDownTimer.pauseMenu();
                View view = mainMenuActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate().alpha(0f)
                            .setDuration(mainMenuActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onCancel() {
                View view = mainMenuActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate().alpha(1f)
                            .setDuration(mainMenuActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onDismiss() {
            	//add by fch 20170721
            	View view = mainMenuActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate().alpha(1f)
                            .setDuration(mainMenuActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
                //end fch
                LittleDownTimer.resetMenu();
                LittleDownTimer.resumeMenu();
            }
        };
    }

    public boolean isControlPermitted() {
        return mIsControlPermitted;
    }

    public void setControlPermitted(boolean bPermission) {
        mIsControlPermitted = bPermission;
    }

    public void closeDialogs() {
        if (null != mPasswordLock) {
            mPasswordLock.dismiss();
        }
        if (null != mPasswordChange) {
            mPasswordChange.dismiss();
        }
    }

    public void updateUi() {
        final int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        /*if (currInputSource != TvCommonManager.INPUT_SOURCE_DTV
		&&currInputSource != TvCommonManager.INPUT_SOURCE_ATV) {//enable channel lock in ATV.lxk 20141210
            //text_lock_system.setTextColor(Color.GRAY);//enable the visiable of lock system&pwd setting item.lxk 20141229
            //textt_set_pwd.setTextColor(Color.GRAY);
            text_block_program.setTextColor(Color.GRAY);
            text_parental_guidance.setTextColor(Color.GRAY);
            text_parental_contentlock.setTextColor(Color.GRAY);
            //lock_status.setTextColor(Color.GRAY);
            //linear_lock_system.setFocusable(false);
            //linear_set_pwd.setFocusable(false);
            linear_block_program.setFocusable(false);
            linear_parental_guidance.setFocusable(false);
            linear_parental_contentlock.setFocusable(false);
            if (false == isControlPermitted()) {
	            lock_status.setText("");
	            linear_set_pwd.setFocusable(false);
            }
        } else */if (false == isControlPermitted()) {
            lock_status.setText("");
            linear_set_pwd.setFocusable(false);
            linear_block_program.setFocusable(false);
            linear_parental_guidance.setFocusable(false);
            linear_parental_contentlock.setFocusable(false);
            textt_set_pwd.setTextColor(Color.GRAY);
            text_block_program.setTextColor(Color.GRAY);
            text_parental_guidance.setTextColor(Color.GRAY);
            text_parental_contentlock.setTextColor(Color.GRAY);
        } else {
            if (TvParentalControlManager.getInstance().isSystemLock()) {
                lock_status.setText(mainMenuActivity.getResources().getString(R.string.str_set_on));
                linear_set_pwd.setFocusable(true);
                linear_block_program.setFocusable(true);
                linear_parental_guidance.setFocusable(true);
                linear_parental_contentlock.setFocusable(true);
                textt_set_pwd.setTextColor(Color.WHITE);

                boolean isDtv = (currInputSource == TvCommonManager.INPUT_SOURCE_DTV);
                boolean isAtv = (currInputSource == TvCommonManager.INPUT_SOURCE_ATV);
                
                linear_block_program.setFocusable((isDtv||isAtv));
                text_block_program.setTextColor((isDtv||isAtv)?Color.WHITE:Color.GRAY);
                
                linear_parental_guidance.setFocusable(isDtv);
                text_parental_guidance.setTextColor(isDtv?Color.WHITE:Color.GRAY);
            } else {
                lock_status.setText(mainMenuActivity.getResources().getString(R.string.str_set_off));
                linear_set_pwd.setFocusable(false);
                linear_block_program.setFocusable(false);
                linear_parental_guidance.setFocusable(false);
                linear_parental_contentlock.setFocusable(false);
                textt_set_pwd.setTextColor(Color.GRAY);
                text_block_program.setTextColor(Color.GRAY);
                text_parental_guidance.setTextColor(Color.GRAY);
                text_parental_contentlock.setTextColor(Color.GRAY);
            }
        }
        //Utility.refreshFocusOrder(linear_lock_system.getParent()); //skye 201503 for M029
    }
    public static void sendKeyEvent(final int KeyCode) {
        new Thread() {     
             public void run() {
                 try {
                     Instrumentation inst = new Instrumentation();
                     inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                     e.printStackTrace();
                 }
              }
   
         }.start();
  }
    
    private void setOnClickLisenters() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentid = mainMenuActivity.getCurrentFocus().getId();
                Intent intent = new Intent();
                if (focusedid != currentid) {
                    MainMenuActivity.getInstance().setParentalControlSelectStatus(0x00000000);
                }
                switch (currentid) {
                    case R.id.linearlayout_lock_system:
                        if (isControlPermitted() == true) {
                            MainMenuActivity.getInstance().setParentalControlSelectStatus(0x00000001);
                            focusedid = R.id.linearlayout_lock_system;
                           // linear_lock_system.getChildAt(0).setVisibility(View.VISIBLE);
                           // linear_lock_system.getChildAt(3).setVisibility(View.VISIBLE);
                            
                            setMyonClickListener(R.id.linearlayout_lock_system, 0x00000001, linear_lock_system);
                        } else {
                            mPasswordLock.show();
                        }
                        break;
                    case R.id.linearlayout_set_pwd:
                        mPasswordChange.show();
                        break;
                    case R.id.linearlayout_block_program:
                        intent.setClass(mainMenuActivity, BlockProgramActivity.class);
                        mainMenuActivity.startActivity(intent);
                        mainMenuActivity.finish();
                        break;
                    case R.id.linearlayout_parental_guidance:
                        intent.setClass(mainMenuActivity, ParentalGuidanceActivity.class);
                        mainMenuActivity.startActivity(intent);
                        mainMenuActivity.finish();
                        break;
                    case R.id.linearlayout_parental_contentlock:
                        intent.setClass(mainMenuActivity, ParentalContentLockActivity.class);
                        if (intent.resolveActivity(mainMenuActivity.getPackageManager()) != null) {
                            mainMenuActivity.startActivity(intent);
                            mainMenuActivity.finish();
                        }

                    case R.id.linearlayout_parental_cipincode:
                        ShowEditTextDialog(true);
                        break;
                    default:
                        break;
                }
            }
        };
        linear_lock_system.setOnClickListener(listener);
        linear_set_pwd.setOnClickListener(listener);
        linear_block_program.setOnClickListener(listener);
        linear_parental_guidance.setOnClickListener(listener);
        linear_parental_contentlock.setOnClickListener(listener);
        linear_parental_cipincode.setOnClickListener(listener);
        linear_lock_system.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				if(event.getAction()==KeyEvent.ACTION_UP)return false;
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				Log.d(TAG, "====setOnClickLisenters====" + keyCode);
				int currentid = -1;
				if (false == isControlPermitted()) {
					Log.e(TAG, "onKeyDown: isControlPermitted == false!!!");
					return false;
				}
				if (mainMenuActivity.getCurrentFocus() != null) {
					currentid = mainMenuActivity.getCurrentFocus().getId();
				}
				if (focusedid != currentid) {
					MainMenuActivity.getInstance()
							.setParentalControlSelectStatus(0x00000000);
				}
				switch (keyCode) {
				case KeyEvent.KEYCODE_ENTER:
				case KeyEvent.KEYCODE_DPAD_RIGHT:
				case KeyEvent.KEYCODE_DPAD_LEFT:
					if(event.getRepeatCount()==0){
					if (currentid == R.id.linearlayout_lock_system) {
						/*if (MainMenuActivity.getInstance()
								.getParentalControlSelectStatus() == 0x00000001)*/ {
							boolean lockstatus = TvParentalControlManager
									.getInstance().isSystemLock();
							lockstatus = !lockstatus;
							TvParentalControlManager.getInstance()
									.setSystemLock(lockstatus);
							// add for system lock operation with immediate
							// process.lxk 20141217
							ProgramInfo currentProInfo = TvChannelManager
									.getInstance().getCurrentProgramInfo();
							Log.i(TAG, "curProInfo(num:"
									+ currentProInfo.number + ",ser:"
									+ currentProInfo.serviceType + ",Lock:"
									+ currentProInfo.isLock);
							if (currentProInfo.isLock == true /*&& lockstatus*/ ) {// zb20150106 add
								if(TvCommonManager.INPUT_SOURCE_DTV == TvCommonManager.getInstance().getCurrentTvInputSource() 
									|| TvCommonManager.INPUT_SOURCE_ATV == TvCommonManager.getInstance().getCurrentTvInputSource()){
					            if(lockstatus){
									TvChannelManager
										.getInstance()
										.setProgramAttribute(
												TvChannelManager.PROGRAM_ATTRIBUTE_LOCK,
												currentProInfo.number,
												currentProInfo.serviceType,
												0x00, currentProInfo.isLock);
								} else {
									//add by hz 20170826 for mantis:34492
									int[] ret = TvCommonManager.getInstance().setTvosCommonCommand("queryChannelLockStatus");
									if (ret[0] == 1 ) {
										TvParentalControlManager.getInstance().unlockChannel();
									}
									//end hz
								}
									
								}
							}
							// end
							
							 /** for system lock operation with immediate process
							 * on parental guidance.lxk 20150228*/
							 
							int CurrentRate = TvParentalControlManager
									.getInstance().getParentalControlRating();
							if ((currentProInfo.serviceType == TvChannelManager.SERVICE_TYPE_DTV)
									&& (currentProInfo.isLock == false)
									&& (CurrentRate != 0)) {
								PresentFollowingEventInfo pfEvtInfo = new PresentFollowingEventInfo();
								DTVSpecificProgInfo dvtSpecProgInfo = null;
								DtvEitInfo mDtveitinfo = null;
								try {
									pfEvtInfo = TvEpgManager
											.getInstance()
											.getEpgPresentFollowingEventInfo(
													currentProInfo.serviceType,
													currentProInfo.number,
													true,
													TvEpgManager.EPG_DETAIL_DESCRIPTION);
									dvtSpecProgInfo = TvChannelManager
											.getInstance()
											.getCurrentProgramSpecificInfo();
									if (null == dvtSpecProgInfo) {
										Log.v(TAG, "dvtSpecProgInfo is NULL!!");
										pfEvtInfo = null;
									}
									if (null != pfEvtInfo) {
										mDtveitinfo = TvEpgManager
												.getInstance().getEitInfo(true);
									} else {
										mDtveitinfo = null;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (null != mDtveitinfo) {
									Log.i(TAG,
											"for Parental Guidance(num:"
													+ currentProInfo.number
													+ ",ser:"
													+ currentProInfo.serviceType
													+ ",age of current:"
													+ mDtveitinfo.eitCurrentEventPf.parentalControl
													+ ",CurrentRate:"
													+ CurrentRate + ")");
									if (mDtveitinfo.eitCurrentEventPf.parentalControl >= CurrentRate)
										TvParentalControlManager.getInstance()
												.setParentalControlRating(
														CurrentRate);
								}
							}
							/* end */
							updateUi();
						}
					}
					focusedid = currentid;
					}
					break;
				default:
					break;
				}

				return false;
				}
		});
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener FocuschangesListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switch(v.getId()) {
                    case R.id.linearlayout_lock_system:
                        LinearLayout container = (LinearLayout) v;
                        if (hasFocus && mIsControlPermitted)
                        {
	                        container.getChildAt(0).setVisibility(View.VISIBLE);
	                        container.getChildAt(3).setVisibility(View.VISIBLE);
                        }else
                        {
							container.getChildAt(0).setVisibility(View.GONE);
                        	container.getChildAt(3).setVisibility(View.GONE);
                        }
                        MainMenuActivity.getInstance().setParentalControlSelectStatus(0x00000000);
                        break;
                    case R.id.linearlayout_parental_guidance:
                        if (TvCountry.SINGAPORE == mCountry) {
                            if (true == hasFocus) {
                                text_parental_guidance_value.setVisibility(View.VISIBLE);
                            } else {
                                text_parental_guidance_value.setVisibility(View.GONE);
                            }
                        }
                        break;
                    default:
                        break;

                }
            }
        };
        linear_lock_system.setOnFocusChangeListener(FocuschangesListener);
        linear_parental_guidance.setOnFocusChangeListener(FocuschangesListener);
    }

    private void setOnTouchListeners() {
        setMyOntouchListener(R.id.linearlayout_lock_system, 0x00000001, linear_lock_system);
        setMyOntouchListener(R.id.linearlayout_set_pwd, 0x00000001, linear_set_pwd);
        setMyOntouchListener(R.id.linearlayout_block_program, 0x00000001, linear_block_program);
    }
    
    private void setMyonClickListener(final int resID, final int status, LinearLayout lay){
    	  lay.requestFocus();
          MainMenuActivity.getInstance().setParentalControlSelectStatus(status);
          focusedid = resID;
          //onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
    }

    private void setMyOntouchListener(final int resID, final int status, LinearLayout lay) {
    	lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("fujia", "===dd=="+TvParentalControlManager.getInstance().isSystemLock());
				if(mPasswordLock.isShowing()){
				// TODO Auto-generated method stub
				  v.requestFocus();
                  MainMenuActivity.getInstance().setParentalControlSelectStatus(status);
                  focusedid = resID;
                  //onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
				}else {
                                  //setOnClickLisenters_frist();
					setOnClickLisenters();
					sendKeyEvent(KeyEvent.KEYCODE_ENTER);
				}
				ProgramInfo currentProInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
				//Log.d("fujia", "====="+currentProInfo.isLock);
				if(currentProInfo.isLock==true){
					 v.requestFocus();
	                  MainMenuActivity.getInstance().setParentalControlSelectStatus(status);
	                  focusedid = resID;
	                  //onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
				}
			}
		});
        lay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    v.requestFocus();
                    MainMenuActivity.getInstance().setParentalControlSelectStatus(status);
                    focusedid = resID;
                     Intent intent = new Intent();
                    switch (resID) {
                        case R.id.linearlayout_lock_system:
                        	 sendKeyEvent(KeyEvent.KEYCODE_ENTER);
                            break;
                        case R.id.linearlayout_set_pwd:
                        	if(!linear_set_pwd.isFocusable())break;
                        	linear_set_pwd.setFocusable(true);
                        	linear_set_pwd.requestFocus();
                            mPasswordChange.show();
                            break;
                        case R.id.linearlayout_block_program:
                        	if(!linear_block_program.isFocusable())break;
                        	linear_block_program.requestFocus();
                            intent.setClass(mainMenuActivity, BlockProgramActivity.class);
                            mainMenuActivity.startActivity(intent);
                            mainMenuActivity.finish();
                            break;
                        default:
                            break;
                    }
                   
                }
                return true;
            }
        });
    }

    private void findViewCiPinCode() {
        mEditPin1 = (EditText) mainMenuActivity.findViewById(R.id.ci_pin1);
        mEditPin2 = (EditText) mainMenuActivity.findViewById(R.id.ci_pin2);
        mEditPin3 = (EditText) mainMenuActivity.findViewById(R.id.ci_pin3);
        mEditPin4 = (EditText) mainMenuActivity.findViewById(R.id.ci_pin4);
        setMyEditListener(mEditPin1, mEditPin2);
        setMyEditListener(mEditPin2, mEditPin3);
        setMyEditListener(mEditPin3, mEditPin4);
        setMyEditListener(mEditPin4, linear_parental_cipincode);
    }

    private void ShowEditTextDialog(boolean isShow) {
        mEditPin1.setText("");
        mEditPin2.setText("");
        mEditPin3.setText("");
        mEditPin4.setText("");
        int visible = isShow ? View.VISIBLE : View.GONE;
        mEditPin1.setVisibility(visible);
        mEditPin2.setVisibility(visible);
        mEditPin3.setVisibility(visible);
        mEditPin4.setVisibility(visible);
        if (isShow) {
            mEditPin1.requestFocus();
        } else {
            linear_parental_cipincode.requestFocus();
        }
    }

    private void setMyEditListener(EditText edittext, final View view) {
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (R.id.linearlayout_parental_cipincode == mainMenuActivity.getCurrentFocus().getId()) {
                    return;
                }
                if (null != view) {
                    view.requestFocus();
                    if (linear_parental_cipincode == view) {
                        setCiPinCode();
                    }
                }
            }
        });
    }

    private void setCiPinCode() {
        int pin1, pin2, pin3, pin4, ciCamPinCode;
        pin1 = pin2 = pin3 = pin4 = 0;
        boolean isValid = true;
        if ((mEditPin1.getText().toString().equals("")) && (mEditPin2.getText().toString().equals(""))
            && (mEditPin3.getText().toString().equals("")) && (mEditPin4.getText().toString().equals(""))) {
            return;
        }
        try {
            pin1 = Integer.parseInt(mEditPin1.getText().toString());
            pin2 = Integer.parseInt(mEditPin2.getText().toString());
            pin3 = Integer.parseInt(mEditPin3.getText().toString());
            pin4 = Integer.parseInt(mEditPin4.getText().toString());
        } catch (Exception e) {
            isValid = false;
        }
        if (false == isValid)
            return;
        ciCamPinCode = pin1 * 1000 + pin2 * 100 + pin3 * 10 + pin4;
        Log.d(TAG, "ciCamPinCode:"+ciCamPinCode);
        TvCiManager.getInstance().setCiCamPinCode(ciCamPinCode);
        Toast.makeText(mainMenuActivity, R.string.str_ci_set_cam_pincode_done, Toast.LENGTH_SHORT).show();
        ShowEditTextDialog(false);
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
    	Log.i(TAG, "onKeyDown:"+keyCode);
        int currentid = -1;
        if (false == isControlPermitted()) {
            Log.e(TAG, "onKeyDown: isControlPermitted == false!!!");
            return;
        }
        if (mainMenuActivity.getCurrentFocus() != null) {
            currentid = mainMenuActivity.getCurrentFocus().getId();
        }
        if (focusedid != currentid) {
            MainMenuActivity.getInstance().setParentalControlSelectStatus(0x00000000);
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (currentid == R.id.linearlayout_lock_system) {
                    /*if (MainMenuActivity.getInstance().getParentalControlSelectStatus() == 0x00000001)*/ {
                        boolean lockstatus = TvParentalControlManager.getInstance().isSystemLock();
                        lockstatus = !lockstatus;
                        TvParentalControlManager.getInstance().setSystemLock(lockstatus);
                        updateUi();
                    }
                }
                focusedid = currentid;
                break;
            default:
                break;
        }
    }
}
