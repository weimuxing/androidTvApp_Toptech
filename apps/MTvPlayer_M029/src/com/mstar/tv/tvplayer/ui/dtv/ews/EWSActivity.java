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

package com.mstar.tv.tvplayer.ui.dtv.ews;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Gravity;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.mstar.tv.tvplayer.ui.R;
import com.mstar.android.tvapi.dtv.dvb.vo.EwsInfo;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvChannelManager.OnEwsEventListener;
import com.mstar.util.Constant;
import com.mstar.util.Utility;

public class EWSActivity extends Activity {
    private static final String TAG = "EWSActivity";

    private final int EWSACTIVITY_FINISH_ACTIVITY = 1001;

    private final int[] mDisasterType = {
            /* 0x01 */
            R.drawable.ews_earthquake
            /* 0x02 */
            , R.drawable.ews_tsunami
            /* 0x03 */
            , R.drawable.ews_volcanic_eruptions
            /* 0x04 */
            , R.drawable.ews_land_movement
            /* 0x05 */
            , R.drawable.ews_flooding
            /* 0x06 */
            , R.drawable.ews_drought
            /* 0x07 */
            , R.drawable.ews_land_and_forest_fire
            /* 0x08 */
            , R.drawable.ews_erosion
            /* 0x09 */
            , R.drawable.ews_fire_building_and_housing
            /* 0x0A */
            , R.drawable.ews_extreme_waves_and_abrasion
            /* 0x0B */
            , R.drawable.ews_extreme_weather
            /* 0x0C */
            , R.drawable.ews_technology_failure
            /* 0x0D */
            , R.drawable.ews_epidemic_and_outbreak
            /* 0x0E */
            , R.drawable.ews_social_conflict
            /* 0x0F */
            , R.drawable.ews_proposal
    };

    private final int WASPADA_DISPLAY_WEIGHT = 4;

    private ImageView mType;

    private ImageView mAuthority;

    private ImageView mLevel;

    private TextView mLocation;

    private TextView mCode;

    private TextView mEventData;

    private TextView mPosition;

    private TextView mCharacteristic;

    private TextView mInformation;

    private EwsInfo mEwsInfo = null;

    private OnEwsEventListener mEwsEventListener = null;

    private class EwsEventListener implements OnEwsEventListener {
        @Override
        public boolean onEwsEvent(int what, int arg1, int arg2, Object obj) {
            /*
             * EWS sends two kinds of event to AN:
             * TVPLAYER_DISPLAY_EMERGENCY_SYSTEM: AN needs to start the EWS UI
             * TVPLAYER_CLOSE_EMERGENCY_SYSTEM: AN needs to close the EWS UI
             */
            Log.d(TAG, "onEwsEvent arg1 = " + arg1 + " ,arg2 = " + arg2);
            if (TvChannelManager.TVPLAYER_CLOSE_EMERGENCY_SYSTEM == what) {
                mHandler.sendEmptyMessage(EWSACTIVITY_FINISH_ACTIVITY);
            }
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEwsInfo = TvDvbChannelManager.getInstance().getEWSInfo();
        if (null == mEwsInfo) {
            Log.e(TAG, "No EWS infomation!");
            finish();
            return;
        }

        loadView();

        mEwsEventListener = new EwsEventListener();
        TvChannelManager.getInstance().registerOnEwsEventListener(mEwsEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInfo();
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        TvChannelManager.getInstance().unregisterOnEwsEventListener(mEwsEventListener);
        mEwsEventListener = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != mEwsInfo) {
            if (EwsInfo.LEVEL_WASPADA != mEwsInfo.level) {
                /*
                 * Only WASPADA level can leave activity when user press the
                 * Exit key. AWAS and SIAGA levels are not allowed user to input
                 * any keys
                 */
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (EWSACTIVITY_FINISH_ACTIVITY == msg.what) {
                finish();
            }
        };
    };

    private void loadView() {
        LinearLayout linearlayout;
        LinearLayout.LayoutParams params;

        setContentView(R.layout.ews);

        mType = (ImageView) findViewById(R.id.disaster_type);
        mAuthority = (ImageView) findViewById(R.id.disaster_authority);
        mLocation = (TextView) findViewById(R.id.location_text);
        mCode = (TextView) findViewById(R.id.ews_code_value);
        mEventData = (TextView) findViewById(R.id.ews_event_date_value);
        mPosition = (TextView) findViewById(R.id.ews_position_value);
        mCharacteristic = (TextView) findViewById(R.id.ews_characteristic_value);
        mInformation = (TextView) findViewById(R.id.ews_informatio_value);

        /* apply scrolling */
        mInformation.setMovementMethod(new ScrollingMovementMethod());

        if (null != mEwsInfo) {
            /*
             * Please be careful to modify following codes. R.layout.ews is
             * defines for AWAS, SIAGA and WASPADA. In different levels, we just
             * have to adjust the screen layout to fit the EWS displaying
             * requirement.
             */
            if (EwsInfo.LEVEL_AWAS == mEwsInfo.level) {
                /* full screen */
                linearlayout = (LinearLayout) findViewById(R.id.linearlayout_main);
                params = (LinearLayout.LayoutParams) linearlayout.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                linearlayout.setLayoutParams(params);
            } else if (EwsInfo.LEVEL_SIAGA == mEwsInfo.level) {
                /* pop-up window like screen, ues XML defualt configuration */
                TextView status = (TextView) findViewById(R.id.status_text);
                status.setTextColor(Color.rgb(240, 210, 90));
                status.setText(getResources().getString(R.string.str_ews_status_siaga));
            } else if (EwsInfo.LEVEL_WASPADA == mEwsInfo.level) {
                /* display the information in bottom area */
                float weight = 1.0f;
                RelativeLayout relativelayout;

                TextView status = (TextView) findViewById(R.id.status_text);
                status.setTextColor(Color.YELLOW);
                status.setText(getResources().getString(R.string.str_ews_status_waspada));

                /* hide middle area */
                relativelayout = (RelativeLayout) findViewById(R.id.linearlayout_info);
                relativelayout.setVisibility(View.GONE);

                /* layout adjustation */
                linearlayout = (LinearLayout) findViewById(R.id.linearlayout_main);
                params = (LinearLayout.LayoutParams) linearlayout.getLayoutParams();
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                /* caculate the displaying height and top margin */
                params.height = size.y * WASPADA_DISPLAY_WEIGHT / 10;
                params.setMargins(0, size.y - params.height, 0, 0);
                linearlayout.setLayoutParams(params);

                /* layout adjustation */
                linearlayout = (LinearLayout) findViewById(R.id.linearlayout_title);
                linearlayout.setOrientation(LinearLayout.HORIZONTAL);

                /* set the weight to 1:1 */
                linearlayout = (LinearLayout) findViewById(R.id.linearlayout_area_u);
                params = (LinearLayout.LayoutParams) linearlayout.getLayoutParams();
                weight = params.weight;
                linearlayout = (LinearLayout) findViewById(R.id.linearlayout_area_d);
                params = (LinearLayout.LayoutParams) linearlayout.getLayoutParams();
                params.weight = weight;
                linearlayout.setLayoutParams(params);
            }
        }
    }

    private void updateInfo() {
        if (null != mEwsInfo) {
            mType = (ImageView) findViewById(R.id.disaster_type);
            if ((0 < mEwsInfo.type) && (mEwsInfo.type <= mDisasterType.length)) {
                /* valid type: 0x01 ~ 0x0F */
                mType.setImageResource(mDisasterType[mEwsInfo.type - 1]);
            }
            mAuthority = (ImageView) findViewById(R.id.disaster_authority);
            if (EwsInfo.AUTHORITY_BMKG == mEwsInfo.authority) {
                mAuthority.setImageResource(R.drawable.ews_bmkg_logo);
            } else {
                mAuthority.setImageResource(R.drawable.ews_bnpb_logo);
            }
            mLocation.setText(mEwsInfo.location.toString());
            mCode.setText(mEwsInfo.code.toString());
            mEventData.setText(mEwsInfo.eventData.toString());
            mPosition.setText(mEwsInfo.position.toString());
            mLocation.setText(mEwsInfo.location.toString());
            mCharacteristic.setText(mEwsInfo.characteristic.toString());
            mInformation.setText(mEwsInfo.information.toString());
        }
    }
}
