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

package com.mstar.tv.tvplayer.ui.component;

import android.widget.ScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;

public class CycleScrollView extends ScrollView {
    private static final String TAG = "CycleScrollView";

    public CycleScrollView(Context context) {
        this(context, null);
    }

    public CycleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.scrollViewStyle);
    }

    public CycleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * This function has been override to provide cycle scroll ability
     *
     * @param event The key event to execute.
     * @return Return true if the event was handled, else false.
     */
    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        if (!canScroll()) {
            if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    View currentFocused = findFocus();
                    View nextFocused = null;
                    boolean ret = false;
                    if (currentFocused != this) {
                        switch (event.getKeyCode()) {
                            case KeyEvent.KEYCODE_DPAD_UP:
                                nextFocused = currentFocused.focusSearch(View.FOCUS_BACKWARD);
                                ret = nextFocused != null
                                        && nextFocused != this
                                        && nextFocused.requestFocus(View.FOCUS_UP);
                                if (true == ret) {
                                    if (nextFocused.isSoundEffectsEnabled()) {
                                        nextFocused.playSoundEffect(SoundEffectConstants.NAVIGATION_UP);
                                    }
                                }
                                return ret;
                            case KeyEvent.KEYCODE_DPAD_DOWN:
                                nextFocused = currentFocused.focusSearch(View.FOCUS_FORWARD);
                                ret = nextFocused != null
                                        && nextFocused != this
                                        && nextFocused.requestFocus(View.FOCUS_DOWN);
                                if (true == ret) {
                                    if (nextFocused.isSoundEffectsEnabled()) {
                                        nextFocused.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
                                    }
                                }
                                return ret;
                            default:
                                break;
                        }
                    }
                }
            }
            return false;
        }
        return super.executeKeyEvent(event);
    }

    /**
     * Handle scrolling in response to an up or down arrow click.
     * This function has been override to provide cycle scroll ability and sound effect
     *
     * @param direction The direction corresponding to the arrow key that was
     *                  pressed
     * @return True if we consumed the event, false otherwise
     */
    @Override
    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();

        if (currentFocused == this) {
            currentFocused = null;
        }

        if (null != currentFocused) {
            View viewNextFocus = null;
            // Reset to avoid timing issue
            currentFocused.setNextFocusUpId(View.NO_ID);
            currentFocused.setNextFocusDownId(View.NO_ID);
            viewNextFocus = currentFocused.focusSearch(View.FOCUS_BACKWARD);
            if (null != viewNextFocus) {
                currentFocused.setNextFocusUpId(viewNextFocus.getId());
            }
            viewNextFocus = currentFocused.focusSearch(View.FOCUS_FORWARD);
            if (null != viewNextFocus) {
                currentFocused.setNextFocusDownId(viewNextFocus.getId());
            }
        }

        boolean eventHandled = super.arrowScroll(direction);
        View nextFocused = findFocus();
        if (nextFocused == this) {
            nextFocused = null;
        }

        if (true == eventHandled) {
            if ((currentFocused != nextFocused) && (nextFocused != null)) {
                if (nextFocused.isSoundEffectsEnabled()) {
                    if (direction == View.FOCUS_UP) {
                        nextFocused.playSoundEffect(SoundEffectConstants.NAVIGATION_UP);
                    } else if (direction == View.FOCUS_DOWN) {
                        nextFocused.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
                    }
                }
            }
        }

        return eventHandled;
    }

    /**
     * @return Returns true this ScrollView can be scrolled
     */
    private boolean canScroll() {
        View child = getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return getHeight() < childHeight + mPaddingTop + mPaddingBottom;
        }
        return false;
    }
}
