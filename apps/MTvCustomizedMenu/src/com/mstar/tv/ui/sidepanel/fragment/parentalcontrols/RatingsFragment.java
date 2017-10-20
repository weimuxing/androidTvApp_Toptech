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

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.mstar.android.tvapi.dtv.atsc.vo.Regin5DimensionInformation;
import com.mstar.android.tvapi.dtv.atsc.vo.RR5RatingPair;
import com.mstar.android.tv.TvAtscChannelManager;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.parental.ContentRatingSystem;
import com.mstar.tv.parental.ContentRatingSystem.Rating;
import com.mstar.tv.parental.ContentRatingSystem.SubRating;
import com.mstar.tv.parental.ParentalControlSettings;
import com.mstar.tv.ui.dialog.OptionDialogFragment;
import com.mstar.tv.ui.sidepanel.item.CheckBoxItem;
import com.mstar.tv.ui.sidepanel.item.DividerItem;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RatingsFragment extends SideFragment {
    private static final String TAG = "RatingsFragment";
    private static TvAtscChannelManager mTvAtscChannelManager = TvAtscChannelManager.getInstance();
    private int mItemsSize;

    @Override
    protected String getTitle() {
        return getString(R.string.option_ratings);
    }

    /**
     * Shows the Reset RRT5 dialog.
     */
    public static class ResetRRT5ActionItem extends ActionItem {
        public final static String DIALOG_TAG = ResetRRT5ActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public ResetRRT5ActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.option_reset_rrt5));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            OptionDialogFragment dialog = new OptionDialogFragment(
                    mMainActivity.getString(R.string.option_reset_rrt5),
                    mMainActivity
                            .getString(R.string.option_reset_rrt5_confirm),
                    mMainActivity.getString(R.string.str_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTvAtscChannelManager.resetRRTSetting();
                            mMainActivity.getSideFragmentManager().popSideFragment();
                        }
                    }, mMainActivity.getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        items.add(new ResetRRT5ActionItem((MainActivity) getActivity()));
        int noDimension = mTvAtscChannelManager.getRRT5NoDimension();
        Log.d(TAG, "noDimension = "+noDimension);
        if (noDimension > 0) {
            String region5Name = mTvAtscChannelManager.getRRT5Region5Name();
            items.add(new DividerItem(region5Name));
            List<Regin5DimensionInformation> ratingInfo = mTvAtscChannelManager.getRRT5Dimension();
            ContentRatingSystem ratingSystem = getContentRatingSystem(region5Name, ratingInfo);
            for (Rating rating : ratingSystem.getRatings()) {
                RatingItem item = rating.getSubRatings().size() == 0 ?
                        new RatingItem(ratingSystem, rating) :
                        new RatingWithSubItem(ratingSystem, rating);
                items.add(item);
            }
        }
        mItemsSize = items.size();
        return items;
    }

    private Rating.Builder parseRating(Regin5DimensionInformation dimensionInfo, List<RR5RatingPair> ratingPairList) {
        Rating.Builder builder = new Rating.Builder();
        builder.setName(dimensionInfo.dimensionName);
        builder.setTitle(dimensionInfo.dimensionName);
        builder.setContentAgeHint(dimensionInfo.values_Defined);
        for (RR5RatingPair pair : ratingPairList) {
            if (!TextUtils.isEmpty(pair.abbRatingText)) {
                builder.addSubRatingName(pair.abbRatingText);
            }
        }
        return builder;
    }

    private SubRating.Builder parseSubRating(Regin5DimensionInformation dimensionInfo, RR5RatingPair pair) {
        SubRating.Builder builder = new SubRating.Builder();
        if (!TextUtils.isEmpty(pair.abbRatingText)) {
            builder.setName(pair.abbRatingText);
            builder.setTitle(pair.abbRatingText);
        }
        return builder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMainActivity().getParentalControlSettings().loadRatings();
    }

    private ContentRatingSystem getContentRatingSystem(String region5Name, List<Regin5DimensionInformation> ratingInfo) {
        ContentRatingSystem.Builder builder = new ContentRatingSystem.Builder(getActivity().getApplicationContext());
        builder.setDomain("com.mstar.android.tv.rrt5rating");
        builder.setName(region5Name);
        builder.setTitle(region5Name);
        for (Regin5DimensionInformation dimensionInfo : ratingInfo) {
            List<RR5RatingPair> ratingPairList = mTvAtscChannelManager.getRR5RatingPair(dimensionInfo.index, dimensionInfo.values_Defined);
            builder.addRatingBuilder(parseRating(dimensionInfo, ratingPairList));
            for (RR5RatingPair pair : ratingPairList) {
                builder.addSubRatingBuilder(parseSubRating(dimensionInfo, pair));
            }
        }
        return builder.build();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Although we set the attribution item at the end of the item list non-focusable, we do get
        // its position when the fragment is resumed. This ensures that we do not select the
        // non-focusable item at the end of the list. See b/17387103.
        if (getSelectedPosition() >= mItemsSize) {
            setSelectedPosition(mItemsSize - 1);
        }
    }

    private class RatingItem extends CheckBoxItem {
        protected final ContentRatingSystem mContentRatingSystem;
        protected final Rating mRating;
        private final Drawable mIcon;
        private CompoundButton mCompoundButton;

        private RatingItem(ContentRatingSystem contentRatingSystem, Rating rating) {
            super(rating.getTitle(), rating.getDescription());
            mContentRatingSystem = contentRatingSystem;
            mRating = rating;
            mIcon = rating.getIcon();
        }

        @Override
        public void onBind(View view) {
            super.onBind(view);

            mCompoundButton = (CompoundButton) view.findViewById(getCompoundButtonId());
            mCompoundButton.setVisibility(View.VISIBLE);

            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            if (mIcon != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(mIcon);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onUnbind() {
            super.onUnbind();
            mCompoundButton = null;
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            mCompoundButton.setButtonDrawable(getButtonDrawable());
            setChecked(getMainActivity().getParentalControlSettings().isRatingBlocked(
                    mContentRatingSystem, mRating));
        }

        @Override
        public void onSelected() {
            super.onSelected();
        }

        @Override
        public int getResourceId() {
            return R.layout.option_item_rating;
        }

        protected int getButtonDrawable() {
            return R.drawable.btn_lock_material_anim;
        }
    }

    private class RatingWithSubItem extends RatingItem {
        private RatingWithSubItem(ContentRatingSystem contentRatingSystem, Rating rating) {
            super(contentRatingSystem, rating);
        }

        @Override
        public void onSelected() {
            getMainActivity().getSideFragmentManager().show(new SubRatingsFragment(mContentRatingSystem, mRating));
        }

        @Override
        protected int getButtonDrawable() {
            int blockedStatus = getMainActivity().getParentalControlSettings().getBlockedStatus(mContentRatingSystem, mRating);
            if (blockedStatus == ParentalControlSettings.RATING_BLOCKED) {
                return R.drawable.btn_lock_material;
            } else if (blockedStatus == ParentalControlSettings.RATING_BLOCKED_PARTIAL) {
                return R.drawable.btn_partial_lock_material;
            }
            return R.drawable.btn_unlock_material;
        }
    }
}
