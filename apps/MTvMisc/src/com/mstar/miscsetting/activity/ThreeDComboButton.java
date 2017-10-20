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

package com.mstar.miscsetting.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mstar.miscsetting.R;

public class ThreeDComboButton implements IUpdateSysData {
    private static final String TAG = "ThreeDComboButton";

    LinearLayout container;

    TextView textViewName;

    TextView textViewIndicator;

    private String[] items;

    private boolean[] itemsAbleFlag;

    private int idx;

    private Activity actContext;

    private int leftKeyCode = KeyEvent.KEYCODE_DPAD_LEFT;

    private int rightKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT;

    private boolean isSelectedDifferent = false;

    private int delaytime = 800;

    private boolean isdelay = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                isdelay = false;
            }
        }
    };

    // con is current context,items are the items of choices,
    // resId is the linearlayout of combobutton(contain 2 textview)
    public ThreeDComboButton(Activity con, String[] items, int resId, boolean isSelectedDiff) {
        this.isSelectedDifferent = isSelectedDiff;
        actContext = con;
        container = (LinearLayout) actContext.findViewById(resId);
        textViewName = (TextView) container.getChildAt(0);
        textViewIndicator = (TextView) container.getChildAt(1);
        setLRListener();
        idx = 0;
        this.items = items;
        if (items != null)
            textViewIndicator.setText(items[0]);
        else
            textViewIndicator.setText("" + idx);
        initItemsFlag();
    }

    public ThreeDComboButton(Dialog dialogContext, String[] items, int resId) {
        container = (LinearLayout) dialogContext.findViewById(resId);
        textViewName = (TextView) container.getChildAt(0);
        textViewIndicator = (TextView) container.getChildAt(1);
        setLRListener();
        idx = 0;
        this.items = items;
        if (items != null)
            textViewIndicator.setText(items[0]);
        else
            textViewIndicator.setText("" + idx);
        initItemsFlag();
    }

    public ThreeDComboButton(Activity con, String[] items, int resId, int first, int second,
            boolean isSelectedDiff) {
        this.isSelectedDifferent = isSelectedDiff;
        actContext = con;
        container = (LinearLayout) actContext.findViewById(resId);
        textViewName = (TextView) container.getChildAt(first);
        textViewIndicator = (TextView) container.getChildAt(second);
        setLRListener();
        idx = 0;
        this.items = items;
        if (items != null)
            textViewIndicator.setText(items[0]);
        else
            textViewIndicator.setText("" + idx);
        initItemsFlag();
    }

    public ThreeDComboButton(Dialog dialogContext, String[] items, int resId, int first, int second) {
        container = (LinearLayout) dialogContext.findViewById(resId);
        textViewName = (TextView) container.getChildAt(first);
        textViewIndicator = (TextView) container.getChildAt(second);
        setLRListener();
        idx = 0;
        this.items = items;
        if (items != null)
            textViewIndicator.setText(items[0]);
        else
            textViewIndicator.setText("" + idx);
        initItemsFlag();
    }

    private void initItemsFlag() {
        if (items != null) {
            int len = items.length;
            itemsAbleFlag = new boolean[len];
            for (int i = 0; i < len; i++) {
                itemsAbleFlag[i] = true;
            }
        }
    }

    public void setSeletedDifferent(boolean b) {
        isSelectedDifferent = b;
    }

    public void setLRKeyCode(int ltKeyCode, int rtKeyCode) {
        this.leftKeyCode = ltKeyCode;
        this.rightKeyCode = rtKeyCode;
    }

    private void setLRListener() {
        container.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (container.isSelected()) {
                        container.setSelected(false);
                    } else {
                        container.setSelected(true);
                    }
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    container.setSelected(false);
                }
                if (keyCode == leftKeyCode && event.getAction() == KeyEvent.ACTION_DOWN
                        && (container.isSelected() || !isSelectedDifferent)) {
                    decreaseIdx();
                    doUpdate();
                    return true;
                } else if (keyCode == rightKeyCode && event.getAction() == KeyEvent.ACTION_DOWN
                        && (container.isSelected() || !isSelectedDifferent)) {
                    increaseIdx();
                    doUpdate();
                    return true;
                } else
                    return false;
            }
        });
        container.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    ThreeDComboButton.this.setFocused();
                    increaseIdx();
                    doUpdate();
                    return true;
                }
                return false;
            }
        });

    }

    public void setIdx(int i) {
        idx = i;
        if (items != null) {
            if (idx >= items.length) {
                idx = 0;
            }
            textViewIndicator.setText(items[idx]);
        } else {
            textViewIndicator.setText("" + idx);
        }
    }

    public int getItemLength() {
        return items.length;
    }

    public short getIdx() {
        return (short) idx;
    }

    public void setItemEnable(int itemIdx, boolean b) {
        itemsAbleFlag[itemIdx] = b;
    }

    public boolean getIsItemEnable(int itemIdx) {
        return itemsAbleFlag[itemIdx];
    }

    protected void increaseIdx() {
        if (items != null) {
            while (true) {
                idx++;
                if (idx >= items.length)
                    idx = 0;
                if (itemsAbleFlag[idx])
                    break;
            }
            textViewIndicator.setText(items[idx]);
        } else {
            textViewIndicator.setText("" + idx);
        }
    }

    protected void decreaseIdx() {
        if (items != null) {
            while (true) {
                idx--;
                if (idx <= -1)
                    idx = items.length - 1;
                if (itemsAbleFlag[idx])
                    break;
            }
            textViewIndicator.setText(items[idx]);
        } else
            textViewIndicator.setText("" + idx);
    }

    public void doUpdate() {
    }

    public void setFocused() {
        container.setFocusable(true);
        container.setFocusableInTouchMode(true);
        container.requestFocus();
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        container.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        container.setOnClickListener(onClickListener);
    }

    public void setTextInChild(int idx, String str) {
        TextView textView = (TextView) container.getChildAt(idx);
        textView.setText(str);
    }

    public void setFocusable(boolean b) {
        if (b) {
            textViewName.setTextColor(Color.WHITE);
            textViewIndicator.setTextColor(Color.WHITE);
        } else {
            textViewName.setTextColor(Color.GRAY);
            textViewIndicator.setTextColor(Color.GRAY);
        }
        container.setFocusable(b);
        container.setEnabled(b);
    }

    public void setEnable(boolean b) {
        container.setEnabled(b);
    }

    public void setVisibility(boolean bVisible) {
        if (bVisible) {
            container.setVisibility(View.VISIBLE);
        } else {
            container.setVisibility(View.GONE);
        }
    }

    public void setContainerGray(int childone, int childtwo, boolean b) {
        if (b) {
            ((TextView) (container.getChildAt(childone))).setTextColor(Color.GRAY);
            ((TextView) (container.getChildAt(childtwo))).setTextColor(Color.GRAY);
        } else {
            ((TextView) (container.getChildAt(childone))).setTextColor(Color.WHITE);
            ((TextView) (container.getChildAt(childtwo))).setTextColor(Color.WHITE);
        }
    }

    public void setTextViewIndicatorColor(int color) {
        textViewIndicator.setTextColor(color);
    }
}
