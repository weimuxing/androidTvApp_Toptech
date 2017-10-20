//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2016 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.ui.sidepanel.fragment.parentalcontrols;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.mstar.tv.R;
import com.mstar.tv.parental.ContentRatingSystem;
import com.mstar.tv.parental.ContentRatingSystem.Rating;
import com.mstar.tv.parental.ContentRatingSystem.SubRating;
import com.mstar.tv.ui.sidepanel.item.CheckBoxItem;
import com.mstar.tv.ui.sidepanel.item.DividerItem;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;

import java.util.ArrayList;
import java.util.List;

public class SubRatingsFragment extends SideFragment {
    private final ContentRatingSystem mContentRatingSystem;
    private final Rating mRating;
    private final List<SubRatingItem> mSubRatingItems = new ArrayList<>();

    public SubRatingsFragment(ContentRatingSystem contentRatingSystem, Rating rating) {
        mContentRatingSystem = contentRatingSystem;
        mRating = rating;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.option_subrating_title, mRating.getTitle());
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        items.add(new RatingItem());
        items.add(new DividerItem(getString(R.string.option_subrating_header)));
        mSubRatingItems.clear();
        for (SubRating subRating : mRating.getSubRatings()) {
            mSubRatingItems.add(new SubRatingItem(subRating));
        }
        items.addAll(mSubRatingItems);
        return items;
    }

    private class RatingItem extends CheckBoxItem {
        private RatingItem() {
            super(mRating.getTitle(), mRating.getDescription());
        }

        @Override
        public void onBind(View view) {
            super.onBind(view);

            CompoundButton button = (CompoundButton) view.findViewById(getCompoundButtonId());
            button.setButtonDrawable(R.drawable.btn_lock_material_anim);
            button.setVisibility(View.VISIBLE);

            Drawable icon = mRating.getIcon();
            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            if (icon != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(icon);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            setChecked(isRatingEnabled());
        }

        @Override
        public void onSelected() {
            super.onSelected();
            boolean checked = isChecked();
            setRatingEnabled(checked);
            if (checked) {
                // If the rating is checked, check and disable all the sub rating items.
                for (SubRating subRating : mRating.getSubRatings()) {
                    setSubRatingEnabled(subRating, true);
                }
                for (SubRatingItem item : mSubRatingItems) {
                    item.setChecked(true);
                    item.setEnabled(false);
                }
            } else {
                // If the rating is unchecked, just enable all the sub rating items and do not
                // change the check state.
                for (SubRatingItem item : mSubRatingItems) {
                    item.setEnabled(true);
                }
            }
        }

        @Override
        public int getResourceId() {
            return R.layout.option_item_rating;
        }
    }

    private class SubRatingItem extends CheckBoxItem {
        private final SubRating mSubRating;

        private SubRatingItem(SubRating subRating) {
            super(subRating.getTitle(), subRating.getDescription());
            mSubRating = subRating;
        }

        @Override
        public void onBind(View view) {
            super.onBind(view);

            CompoundButton button = (CompoundButton) view.findViewById(getCompoundButtonId());
            button.setButtonDrawable(R.drawable.btn_lock_material_anim);
            button.setVisibility(View.VISIBLE);

            Drawable icon = mSubRating.getIcon();
            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            if (icon != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(icon);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            setChecked(isSubRatingEnabled(mSubRating));
            setEnabled(!isRatingEnabled());
        }

        @Override
        public void onSelected() {
            super.onSelected();
            setSubRatingEnabled(mSubRating, isChecked());
        }

        @Override
        public int getResourceId() {
            return R.layout.option_item_rating;
        }
    }

    private boolean isRatingEnabled() {
        return getMainActivity().getParentalControlSettings()
                .isRatingBlocked(mContentRatingSystem, mRating);
    }

    private boolean isSubRatingEnabled(SubRating subRating) {
        return getMainActivity().getParentalControlSettings()
                .isSubRatingEnabled(mContentRatingSystem, mRating, subRating);
    }

    private void setRatingEnabled(boolean enabled) {
        getMainActivity().getParentalControlSettings()
                .setRatingBlocked(mContentRatingSystem, mRating, enabled);
    }

    private void setSubRatingEnabled(SubRating subRating, boolean enabled) {
        getMainActivity().getParentalControlSettings()
                .setSubRatingBlocked(mContentRatingSystem, mRating, subRating, enabled);
    }
}
