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

package com.mstar.android.tv.tunersetup.tuning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mstar.android.tv.inputservice.R;
import com.mstar.android.tv.framework.MstarBaseActivity;
import com.mstar.android.tv.util.Constant;

public class ChooseCityForAutoTuneActivity extends MstarBaseActivity {
    /** Called when the activity is first created. */
    private Button button0;

    private Button button1;

    private Button button2;

    private Button button3;

    private Button button4;

    private Button button5;

    private Button button6;

    private Button button7;

    private Button button8;

    private static Button[] buttonArray = new Button[9];

    private int currentPage = 0;

    private final int ONEPINDEXS = 9;

    private int MAXINDEXS;

    private static int pagesCount = 0;

    private String[] OptionCities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecityforautotuning);
        findViews();
        goToPage(0);
        registerListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void findViews() {
        button0 = (Button) findViewById(R.id.button_cha_choose_city_1);
        button1 = (Button) findViewById(R.id.button_cha_choose_city_2);
        button2 = (Button) findViewById(R.id.button_cha_choose_city_3);
        button3 = (Button) findViewById(R.id.button_cha_choose_city_4);
        button4 = (Button) findViewById(R.id.button_cha_choose_city_5);
        button5 = (Button) findViewById(R.id.button_cha_choose_city_6);
        button6 = (Button) findViewById(R.id.button_cha_choose_city_7);
        button7 = (Button) findViewById(R.id.button_cha_choose_city_8);
        button8 = (Button) findViewById(R.id.button_cha_choose_city_9);
        buttonArray[0] = button0;
        buttonArray[1] = button1;
        buttonArray[2] = button2;
        buttonArray[3] = button3;
        buttonArray[4] = button4;
        buttonArray[5] = button5;
        buttonArray[6] = button6;
        buttonArray[7] = button7;
        buttonArray[8] = button8;
        buttonArray[1] = button0;
        OptionCities = getResources().getStringArray(R.array.cities_option_array);
        pagesCount = OptionCities.length / ONEPINDEXS;

    }

    protected void goToPage(int pageNo) {
        if (pageNo < pagesCount && pageNo >= 0) {
            button0.setText(OptionCities[pageNo * 9 + 0]);
            button1.setText(OptionCities[pageNo * 9 + 1]);
            button2.setText(OptionCities[pageNo * 9 + 2]);
            button3.setText(OptionCities[pageNo * 9 + 3]);
            button4.setText(OptionCities[pageNo * 9 + 4]);
            button5.setText(OptionCities[pageNo * 9 + 5]);
            button6.setText(OptionCities[pageNo * 9 + 6]);
            button7.setText(OptionCities[pageNo * 9 + 7]);
            button8.setText(OptionCities[pageNo * 9 + 8]);
            button0.setVisibility(View.VISIBLE);
            button0.setFocusable(true);
            button1.setVisibility(View.VISIBLE);
            button1.setFocusable(true);
            button2.setVisibility(View.VISIBLE);
            button2.setFocusable(true);
            button3.setVisibility(View.VISIBLE);
            button3.setFocusable(true);
            button4.setVisibility(View.VISIBLE);
            button4.setFocusable(true);
            button5.setVisibility(View.VISIBLE);
            button5.setFocusable(true);
            button6.setVisibility(View.VISIBLE);
            button6.setFocusable(true);
            button7.setVisibility(View.VISIBLE);
            button7.setFocusable(true);
            button8.setVisibility(View.VISIBLE);
            button8.setFocusable(true);

        } else if (pageNo == pagesCount) {
            for (int index = 0; index < OptionCities.length % 9; index++) {
                Log.i("ChooseCity", "@@@@@@@@~~~~~index is " + index);
                buttonArray[index].setText(OptionCities[pageNo * 9 + index]);
            }
            for (int indx = OptionCities.length % 9; indx < 9; indx++) {
                Log.i("ChooseCity", "@@@@@@@@~~~~~indx is " + indx);
                buttonArray[indx].setVisibility(View.INVISIBLE);
                buttonArray[indx].setFocusable(false);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int currentid = getCurrentFocus().getId();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.button_cha_choose_city_7:
                    case R.id.button_cha_choose_city_8:
                    case R.id.button_cha_choose_city_9: {
                        if (currentPage < pagesCount) {
                            currentPage++;
                            Log.i("ChooseCity", "@@@@@@@____currentPage is " + currentPage);
                            goToPage(currentPage);
                        }

                    }
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.button_cha_choose_city_1:
                    case R.id.button_cha_choose_city_2:
                    case R.id.button_cha_choose_city_3: {
                        if (currentPage >= 0) {
                            currentPage--;
                            goToPage(currentPage);
                        }
                    }
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                Intent intent = new Intent();
                intent.setClass(ChooseCityForAutoTuneActivity.this, AutoTuneOptionActivity.class);
                intent.putExtra(Constant.AUTO_TUNING_OPTION_FROM_SUBPAGE, true);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerListeners() {
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button0.setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.button_cha_choose_city_1:

                    // break;
                case R.id.button_cha_choose_city_2:

                    // break;
                case R.id.button_cha_choose_city_3:

                    // break;
                case R.id.button_cha_choose_city_4:

                    // break;
                case R.id.button_cha_choose_city_5:

                    // break;
                case R.id.button_cha_choose_city_6:

                    // break;
                case R.id.button_cha_choose_city_7:

                    // break;
                case R.id.button_cha_choose_city_8:

                    // break;
                case R.id.button_cha_choose_city_9:
                    Intent intent = new Intent();
                    intent.setClass(ChooseCityForAutoTuneActivity.this,
                            DtvAutoTuneOptionActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

}
