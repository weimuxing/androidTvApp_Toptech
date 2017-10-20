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

package com.mstar.miscsetting.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
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

public class ComboButton implements IUpdateSysData {
    private LinearLayout mLayout;

    TextView textViewName;

    TextView textViewIndicator;

    private String[] items;

    private boolean[] itemsAbleFlag;

    private int idx;

    private Activity actContext;

    private int leftKeyCode = KeyEvent.KEYCODE_DPAD_LEFT;

    private int rightKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT;

    private boolean isSelectedDifferent = false;

    // con is current context,items are the items of choices,
    // resId is the linearlayout of combobutton(contain 2 textview)
    public ComboButton(Activity con, String[] items, int resId, boolean isSelectedDiff) {
        this.isSelectedDifferent = isSelectedDiff;
        actContext = con;
        mLayout = (LinearLayout) actContext.findViewById(resId);
        textViewName = (TextView) mLayout.getChildAt(0);
        textViewIndicator = (TextView) mLayout.getChildAt(1);
        Log.i("------combobutton----------", "-----------01------------------");
        setLRListener();
        Log.i("------combobutton----------", "-----------02------------------");
        // setClickListenerForActivity();
        idx = 0;
        this.items = items;
        if (items != null)
            textViewIndicator.setText(items[0]);
        else
            textViewIndicator.setText("" + idx);
        initItemsFlag();
    }

    public ComboButton(Dialog dialogContext, String[] items, int resId) {
        mLayout = (LinearLayout) dialogContext.findViewById(resId);
        textViewName = (TextView) mLayout.getChildAt(0);
        textViewIndicator = (TextView) mLayout.getChildAt(1);
        setLRListener();
        idx = 0;
        this.items = items;
        if (items != null)
            textViewIndicator.setText(items[0]);
        else
            textViewIndicator.setText("" + idx);
        initItemsFlag();
    }

    public ComboButton(Activity con, String[] items, int resId, int first, int second,
            boolean isSelectedDiff) {
        this.isSelectedDifferent = isSelectedDiff;
        actContext = con;
        mLayout = (LinearLayout) actContext.findViewById(resId);
        textViewName = (TextView) mLayout.getChildAt(first);
        textViewIndicator = (TextView) mLayout.getChildAt(second);
        setLRListener();
        idx = 0;
        this.items = items;
        if (items != null)
            textViewIndicator.setText(items[0]);
        else
            textViewIndicator.setText("" + idx);
        initItemsFlag();
    }

    public ComboButton(Dialog dialogContext, String[] items, int resId, int first, int second) {
        mLayout = (LinearLayout) dialogContext.findViewById(resId);
        textViewName = (TextView) mLayout.getChildAt(first);
        textViewIndicator = (TextView) mLayout.getChildAt(second);
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
        Log.i("------combobutton----------", "-----------1------------------");
        mLayout.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mLayout.isSelected()) {
                        mLayout.setSelected(false);
                    } else {
                        mLayout.setSelected(true);
                    }
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    mLayout.setSelected(false);
                }
                Log.i("------combobutton----------", "-----------111------------------");
                if (keyCode == leftKeyCode && event.getAction() == KeyEvent.ACTION_DOWN
                        && (mLayout.isSelected() || !isSelectedDifferent)) {
                    Log.i("------combobutton----------", "-----------2------------------");
                    decreaseIdx();
                    doUpdate();
                    Log.i("------combobutton----------", "-----------3------------------");
                    return true;
                } else if (keyCode == rightKeyCode && event.getAction() == KeyEvent.ACTION_DOWN
                        && (mLayout.isSelected() || !isSelectedDifferent)) {
                    increaseIdx();
                    doUpdate();
                    return true;
                } else
                    return false;
            }
        });
        mLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // if(event.getAction() == KeyEvent.ACTION_UP){
                // System.out.println("combo on touch up!");
                // ComboButton.this.setFocused();
                // increaseIdx();
                // doUpdate();
                // return true;
                // }
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    ComboButton.this.setFocused();
                    increaseIdx();
                    doUpdate();
                    return true;
                }
                return false;
            }
        });

    }

    // private void setClickListenerForActivity(){
    // mLayout.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View arg0) {
    // // TODO Auto-generated method stub
    // Dialog dialog = new AlertDialog.Builder(actContext)
    // .setTitle(textViewName.getText())
    // .setSingleChoiceItems(items, idx, new DialogInterface.OnClickListener(){
    // @Override
    // public void onClick(DialogInterface dialog,
    // int which) {
    // // TODO Auto-generated method stub
    // idx = which;
    // textViewIndicator.setText(items[idx]);
    // doUpdate();
    // dialog.dismiss();
    // }
    // })
    // .show();
    // setDialogWindowSize(dialog);
    //
    // }
    // });
    // }
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
        } else
            textViewIndicator.setText("" + idx);
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
        // TODO Auto-generated method stub
    }

    public void setFocused() {
        mLayout.setFocusable(true);
        mLayout.setFocusableInTouchMode(true);
        mLayout.requestFocus();
    }

    // private void setDialogWindowSize(Dialog dialog) {
    // // TODO Auto-generated method stub
    // LayoutParams params = getWindow().getAttributes();
    // params.height = 500;
    // params.width = 600;
    // dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams)
    // params);
    // }
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mLayout.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mLayout.setOnClickListener(onClickListener);
    }

    public void setTextInChild(int idx, String str) {
        TextView textView = (TextView) mLayout.getChildAt(idx);
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
        mLayout.setFocusable(b);
        mLayout.setEnabled(b);
    }

    public void setEnable(boolean b) {
        mLayout.setEnabled(b);
    }

    public void setVisibility(boolean b) {
        if (b) {
            mLayout.setVisibility(View.VISIBLE);
        } else {
            mLayout.setVisibility(View.GONE);
        }
    }

    public void requestFocus() {
        mLayout.requestFocus();
    }

    public boolean isFocused() {
        return mLayout.isFocused();
    }

    public void setContainerGray(int childone, int childtwo, boolean b) {
        if (b) {
            ((TextView) (mLayout.getChildAt(childone))).setTextColor(Color.GRAY);
            ((TextView) (mLayout.getChildAt(childtwo))).setTextColor(Color.GRAY);
        } else {
            ((TextView) (mLayout.getChildAt(childone))).setTextColor(Color.WHITE);
            ((TextView) (mLayout.getChildAt(childtwo))).setTextColor(Color.WHITE);
        }
    }
}
