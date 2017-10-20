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

package com.mstar.android.setupwizard;

import java.util.ArrayList;
import java.util.Locale;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

public class DefaultActivity extends Activity {
    private static final String TAG = "DefaultActivity";
    private TextView mTitle;
    private NumberPicker mLanguagePicker;
    private Button mStartButton;
    private Locale[] mSupportedLocales = {Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE};
    private ArrayList<Locale> mLocaleList = new ArrayList<Locale>();
    private ArrayList<String> mLocaleDisplayNames = new ArrayList<String>();
    private TvCommonManager mTvCommonManager;
    private TvChannelManager mTvChannelManager;
    private int  currentInputSource;
    private int currentChannelNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ProportionalOuterFrame frame = (ProportionalOuterFrame) getLayoutInflater().inflate(R.layout.welcome_activity, null);
        setContentView(frame);
        mTvCommonManager = TvCommonManager.getInstance();
        mTvChannelManager = TvChannelManager.getInstance();
        currentInputSource = mTvCommonManager.getCurrentInputSource().ordinal();
        
        if (currentInputSource == TvCommonManager.INPUT_SOURCE_ATV
        	|| currentInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
        	currentChannelNumber = mTvChannelManager.getCurrentChannelNumber();
		}
        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
        mTitle = (TextView) findViewById(R.id.welcome_title);
        String[] locales = Resources.getSystem().getAssets().getLocales();
        //mSupportedLocales = Locale.getAvailableLocales();
        for (String stringLocales : locales) {
        	if (stringLocales.matches("[a-z]{2}-[A-Z]{2}")) {
				String[] split = stringLocales.split("-");
				Locale locale = new Locale(split[0], split[1]);
				mLocaleList.add(locale);
				mLocaleDisplayNames.add(locale.getDisplayName());
			}     
        }

        mLanguagePicker = (NumberPicker) findViewById(R.id.language_picker);
        mLanguagePicker.setMaxValue(mLocaleList.size()-1);
        mLanguagePicker.setMinValue(0);
        mLanguagePicker.setDisplayedValues(mLocaleDisplayNames.toArray(new String[mLocaleDisplayNames.size()]));
        mLanguagePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mLanguagePicker.setWrapSelectorWheel(false);
        mLanguagePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener (){
            public void onValueChange(NumberPicker view, int oldValue, int newValue) {
                changeApplicationLocale();
            }
        });
        Locale current = getResources().getConfiguration().locale;
        mLanguagePicker.setValue(mLocaleList.indexOf(current));

        mStartButton = (Button) findViewById(R.id.start);
        mStartButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                changeSystemLocale();
                finishSetupWizard();
            }
        });
    }

    private void changeApplicationLocale(){
        Configuration config = new Configuration();
        config.locale = mLocaleList.get(mLanguagePicker.getValue());
        Locale.setDefault(config.locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        mStartButton.setText(R.string.start);
        mTitle.setText(R.string.welcome_message);
        mLocaleDisplayNames.clear();
        for (Locale locale : mLocaleList) {
            mLocaleDisplayNames.add(locale.getDisplayName());
        }
        mLanguagePicker.setDisplayedValues(mLocaleDisplayNames.toArray(new String[mLocaleDisplayNames.size()]));
    }

    private void changeSystemLocale() {
        try {
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            config.locale = mLocaleList.get(mLanguagePicker.getValue());
            // indicate this isn't some passing default - the user wants this
            // remembered
            config.userSetLocale = true;
            am.updateConfiguration(config);
        } catch (RemoteException e) {
        }
    }

    private void finishSetupWizard() {
        // Add a persistent setting to allow other apps to know the device has been provisioned.
        Settings.Global.putInt(getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 1);
        Settings.Secure.putInt(getContentResolver(), Settings.Secure.USER_SETUP_COMPLETE, 1);

        // remove this activity from the package manager.
        PackageManager pm = getPackageManager();
        ComponentName name = new ComponentName(this, DefaultActivity.class);
        pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        mTvCommonManager.setInputSource(currentInputSource);
        if (currentInputSource == TvCommonManager.INPUT_SOURCE_ATV) {         
            	 mTvChannelManager.setAtvChannel(currentChannelNumber);
    		}else if (currentInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
				mTvChannelManager.selectProgram(currentChannelNumber, TvChannelManager.SERVICE_TYPE_DTV);
			}
        finish();
    }

    @Override
    public void onBackPressed() {
        // disable back key
    }

    /**
     * Used as the outer frame of all setup wizard pages that need to adjust their margins based
     * on the total size of the available display. (e.g. side margins set to 10% of total width.)
     */
    public static class ProportionalOuterFrame extends RelativeLayout {
        public ProportionalOuterFrame(Context context) {
            super(context);
        }
        public ProportionalOuterFrame(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public ProportionalOuterFrame(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
        /**
         * Set our margins and title area height proportionally to the available display size
         */
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            final Resources resources = getContext().getResources();
            float titleHeight = resources.getFraction(R.dimen.main_title_height, 1, 1);
            float sideMargin = resources.getFraction(R.dimen.main_border_width, 1, 1);
            int bottom = resources.getDimensionPixelSize(R.dimen.welcome_screen_margin_bottom);
            setPaddingRelative(
                    (int) (parentWidth * sideMargin),
                    0,
                    (int) (parentWidth * sideMargin),
                    bottom);
            View title = findViewById(R.id.title_area);
            if (title != null) {
                title.setMinimumHeight((int) (parentHeight * titleHeight));
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
