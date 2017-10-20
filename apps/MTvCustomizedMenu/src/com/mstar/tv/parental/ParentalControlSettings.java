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

package com.mstar.tv.parental;

import android.content.Context;
import android.media.tv.TvContentRating;
import android.media.tv.TvInputManager;

import com.mstar.tv.parental.ContentRatingSystem.Rating;
import com.mstar.tv.parental.ContentRatingSystem.SubRating;
import com.mstar.tv.parental.ContentRatingSystem.Order;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class ParentalControlSettings {
    /**
     * The rating and all of its sub-ratings are blocked.
     */
    public static final int RATING_BLOCKED = 0;

    /**
     * The rating is blocked but not all of its sub-ratings are blocked.
     */
    public static final int RATING_BLOCKED_PARTIAL = 1;

    /**
     * The rating is not blocked.
     */
    public static final int RATING_NOT_BLOCKED = 2;

    private final Context mContext;
    private final TvInputManager mTvInputManager;

    // mRatings is expected to be synchronized with mTvInputManager.getBlockedRatings().
    private Set<TvContentRating> mRatings;
    private Set<TvContentRating> mCustomRatings;

    public ParentalControlSettings(Context context) {
        mContext = context;
        mTvInputManager = (TvInputManager) mContext.getSystemService(Context.TV_INPUT_SERVICE);
    }

    public boolean isParentalControlsEnabled() {
        return mTvInputManager.isParentalControlsEnabled();
    }

    public void setParentalControlsEnabled(boolean enabled) {
        mTvInputManager.setParentalControlsEnabled(enabled);
    }

    public void loadRatings() {
        mRatings = new HashSet<>(mTvInputManager.getBlockedRatings());
    }

    /**
     * Sets the blocked status of a given content rating.
     * <p>
     * Note that a call to this method automatically changes the current rating level to
     * {@code TvSettings.CONTENT_RATING_LEVEL_CUSTOM} if needed.
     * </p>
     *
     * @param contentRatingSystem The content rating system where the given rating belongs.
     * @param rating The content rating to set.
     * @return {@code true} if changed, {@code false} otherwise.
     * @see #setSubRatingBlocked
     */
    public boolean setRatingBlocked(ContentRatingSystem contentRatingSystem, Rating rating,
            boolean blocked) {
        List<Order> orders = contentRatingSystem.getOrders();
        if (blocked) {
            for (Order order : orders) {
                List<Rating> ratings = order.getRatingOrder();
                if (ratings.contains(rating)) {
                    boolean found = false;
                    for (Rating ratingInOrder : ratings) {
                        if (found) {
                            setRatingBlockedInternal(contentRatingSystem, ratingInOrder, null, blocked);
                        }
                        if (ratingInOrder.equals(rating)) {
                            found = true;
                        }
                    }
                }
            }
        }
        else {
            for (Order order : orders) {
                List<Rating> ratings = order.getRatingOrder();
                if (ratings.contains(rating)) {
                    boolean found = false;
                    for (Rating ratingInOrder : ratings) {
                        if (ratingInOrder.equals(rating)) {
                            found = true;
                        }
                        if (!found) {
                            setRatingBlockedInternal(contentRatingSystem, ratingInOrder, null, blocked);
                        }
                    }
                }
            }
        }
        return setRatingBlockedInternal(contentRatingSystem, rating, null, blocked);
    }

    /**
     * Checks whether any of given ratings is blocked.
     *
     * @param ratings The array of ratings to check
     * @return {@code true} if a rating is blocked, {@code false} otherwise.
     */
    public boolean isRatingBlocked(TvContentRating[] ratings) {
        return getBlockedRating(ratings) != null;
    }

    /**
     * Checks whether any of given ratings is blocked and returns the first blocked rating.
     *
     * @param ratings The array of ratings to check
     * @return The {@link TvContentRating} that is blocked.
     */
    public TvContentRating getBlockedRating(TvContentRating[] ratings) {
        if (ratings == null) {
            return null;
        }
        for (TvContentRating rating : ratings) {
            if (mTvInputManager.isRatingBlocked(rating)) {
                return rating;
            }
        }
        return null;
    }

    /**
     * Checks whether a given rating is blocked by the user or not.
     *
     * @param contentRatingSystem The content rating system where the given rating belongs.
     * @param rating The content rating to check.
     * @return {@code true} if blocked, {@code false} otherwise.
     */
    public boolean isRatingBlocked(ContentRatingSystem contentRatingSystem, Rating rating) {
        return mRatings.contains(toTvContentRating(contentRatingSystem, rating));
    }

    /**
     * Sets the blocked status of a given content sub-rating.
     * <p>
     * Note that a call to this method automatically changes the current rating level to
     * {@code TvSettings.CONTENT_RATING_LEVEL_CUSTOM} if needed.
     * </p>
     *
     * @param contentRatingSystem The content rating system where the given rating belongs.
     * @param rating The content rating associated with the given sub-rating.
     * @param subRating The content sub-rating to set.
     * @return {@code true} if changed, {@code false} otherwise.
     * @see #setRatingBlocked
     */
    public boolean setSubRatingBlocked(ContentRatingSystem contentRatingSystem, Rating rating,
            SubRating subRating, boolean blocked) {
        return setRatingBlockedInternal(contentRatingSystem, rating, subRating, blocked);
    }

    /**
     * Checks whether a given content sub-rating is blocked by the user or not.
     *
     * @param contentRatingSystem The content rating system where the given rating belongs.
     * @param rating The content rating associated with the given sub-rating.
     * @param subRating The content sub-rating to check.
     * @return {@code true} if blocked, {@code false} otherwise.
     */
    public boolean isSubRatingEnabled(ContentRatingSystem contentRatingSystem, Rating rating,
            SubRating subRating) {
        return mRatings.contains(toTvContentRating(contentRatingSystem, rating, subRating));
    }

    private boolean setRatingBlockedInternal(ContentRatingSystem contentRatingSystem, Rating rating,
            SubRating subRating, boolean blocked) {
        TvContentRating tvContentRating = (subRating == null)
                ? toTvContentRating(contentRatingSystem, rating)
                : toTvContentRating(contentRatingSystem, rating, subRating);
        boolean changed;
        if (blocked) {
            changed = mRatings.add(tvContentRating);
            mTvInputManager.addBlockedRating(tvContentRating);
        } else {
            changed = mRatings.remove(tvContentRating);
            mTvInputManager.removeBlockedRating(tvContentRating);
        }
        return changed;
    }

    /**
     * Returns the blocked status of a given rating. The status can be one of the followings:
     * {@link #RATING_BLOCKED}, {@link #RATING_BLOCKED_PARTIAL} and {@link #RATING_NOT_BLOCKED}
     */
    public int getBlockedStatus(ContentRatingSystem contentRatingSystem, Rating rating) {
        if (isRatingBlocked(contentRatingSystem, rating)) {
            return RATING_BLOCKED;
        }
        for (SubRating subRating : rating.getSubRatings()) {
            if (isSubRatingEnabled(contentRatingSystem, rating, subRating)) {
                return RATING_BLOCKED_PARTIAL;
            }
        }
        return RATING_NOT_BLOCKED;
    }

    private TvContentRating toTvContentRating(ContentRatingSystem contentRatingSystem,
            Rating rating) {
        return TvContentRating.createRating(contentRatingSystem.getDomain(),
                contentRatingSystem.getName(), rating.getName());
    }

    private TvContentRating toTvContentRating(ContentRatingSystem contentRatingSystem,
            Rating rating, SubRating subRating) {
        return TvContentRating.createRating(contentRatingSystem.getDomain(),
                contentRatingSystem.getName(), rating.getName(), subRating.getName());
    }
}
