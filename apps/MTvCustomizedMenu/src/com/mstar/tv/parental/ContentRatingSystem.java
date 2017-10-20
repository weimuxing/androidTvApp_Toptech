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
import android.graphics.drawable.Drawable;
import android.media.tv.TvContentRating;
import android.text.TextUtils;
import android.util.Log;

import com.mstar.tv.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ContentRatingSystem {
    private static final String TAG = "ContentRatingSystem";
    /*
     * A comparator that implements the display order of a group of content rating systems.
     */
    public static final Comparator<ContentRatingSystem> DISPLAY_NAME_COMPARATOR =
            new Comparator<ContentRatingSystem>() {
                @Override
                public int compare(ContentRatingSystem s1, ContentRatingSystem s2) {
                    String name1 = s1.getDisplayName();
                    String name2 = s2.getDisplayName();
                    return name1.compareTo(name2);
                }
            };

    private static final String DELIMITER = "/";

    // Name of this content rating system. It should be unique in an XML file.
    private final String mName;

    // Domain of this content rating system. It's package name now.
    private final String mDomain;

    // Title of this content rating system. (e.g. TV-PG)
    private final String mTitle;

    // Description of this content rating system.
    private final String mDescription;

    // Country code of this content rating system.
    private final List<String> mCountries;

    // Display name of this content rating system consisting of the associated country
    // and its title. For example, "Canada (French)"
    private final String mDisplayName;

    // Ordered list of main content ratings. UX should respect the order.
    private final List<Rating> mRatings;

    // Ordered list of sub content ratings. UX should respect the order.
    private final List<SubRating> mSubRatings;

    // List of orders. This describes the automatic lock/unlock relationship between ratings.
    // For example, let say we have following order.
    //    <order>
    //        <rating android:name="US_TVPG_Y" />
    //        <rating android:name="US_TVPG_Y7" />
    //    </order>
    // This means that locking US_TVPG_Y7 automatically locks US_TVPG_Y and
    // unlocking US_TVPG_Y automatically unlocks US_TVPG_Y7 from the UX.
    // An user can still unlock US_TVPG_Y while US_TVPG_Y7 is locked by manually.
    private final List<Order> mOrders;

    private final boolean mIsCustom;

    public String getId() {
        return mDomain + DELIMITER + mName;
    }

    public String getName(){
        return mName;
    }

    public String getDomain() {
        return mDomain;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getDescription(){
        return mDescription;
    }

    public List<String> getCountries(){
        return mCountries;
    }

    public List<Rating> getRatings(){
        return mRatings;
    }

    public List<SubRating> getSubRatings(){
        return mSubRatings;
    }

    public List<Order> getOrders(){
        return mOrders;
    }

    /**
     * Returns the display name of the content rating system consisting of the associated country
     * and its title. For example, "Canada (French)".
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    public boolean isCustom() {
        return mIsCustom;
    }

    /**
     * Returns true if the ratings is owned by this content rating system.
     */
    public boolean ownsRating(TvContentRating rating) {
        return mDomain.equals(rating.getDomain()) && mName.equals(rating.getRatingSystem());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContentRatingSystem) {
            ContentRatingSystem other = (ContentRatingSystem) obj;
            return this.mName.equals(other.mName) && this.mDomain.equals(other.mDomain);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * mName.hashCode() + mDomain.hashCode();
    }

    private ContentRatingSystem(
            String name, String domain, String title, String description, List<String> countries,
            String displayName, List<Rating> ratings, List<SubRating> subRatings,
            List<Order> orders, boolean isCustom) {
        mName = name;
        mDomain = domain;
        mTitle = title;
        mDescription = description;
        mCountries = countries;
        mDisplayName = displayName;
        mRatings = ratings;
        mSubRatings = subRatings;
        mOrders = orders;
        mIsCustom = isCustom;
    }

    public static class Builder {
        private final Context mContext;
        private String mName;
        private String mDomain;
        private String mTitle;
        private String mDescription;
        private List<String> mCountries;
        private final List<Rating.Builder> mRatingBuilders = new ArrayList<>();
        private final List<SubRating.Builder> mSubRatingBuilders = new ArrayList<>();
        private final List<Order.Builder> mOrderBuilders = new ArrayList<>();
        private boolean mIsCustom;

        public Builder(Context context) {
            mContext = context;
        }

        public void setName(String name) {
            mName = name;
        }

        public void setDomain(String domain) {
            mDomain = domain;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public void addCountry(String country) {
            if (mCountries == null) {
                mCountries = new ArrayList<>();
            }
            mCountries.add(new Locale("", country).getCountry());
        }

        public void addRatingBuilder(Rating.Builder ratingBuilder) {
            // To provide easy access to the SubRatings in it,
            // Rating has reference to SubRating, not Name of it.
            // (Note that Rating/SubRating is ordered list so we cannot use Map)
            // To do so, we need to have list of all SubRatings which might not be available
            // at this moment. Keep builders here and build it with SubRatings later.
            mRatingBuilders.add(ratingBuilder);
        }

        public void addSubRatingBuilder(SubRating.Builder subRatingBuilder) {
            // SubRatings would be built rather to keep consistency with other fields.
            mSubRatingBuilders.add(subRatingBuilder);
        }

        public void addOrderBuilder(Order.Builder orderBuilder) {
            // To provide easy access to the Ratings in it,
            // Order has reference to Rating, not Name of it.
            // (Note that Rating/SubRating is ordered list so we cannot use Map)
            // To do so, we need to have list of all Rating which might not be available
            // at this moment. Keep builders here and build it with Ratings later.
            mOrderBuilders.add(orderBuilder);
        }

        public void setIsCustom(boolean isCustom) {
            mIsCustom = isCustom;
        }

        public ContentRatingSystem build() {
            if (TextUtils.isEmpty(mName)) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (TextUtils.isEmpty(mDomain)) {
                throw new IllegalArgumentException("Domain cannot be empty");
            }

            StringBuilder sb = new StringBuilder();
            if (mCountries != null) {
                if (mCountries.size() == 1) {
                    sb.append(new Locale("", mCountries.get(0)).getDisplayCountry());
                } else if (mCountries.size() > 1) {
                    Locale locale = Locale.getDefault();
                    if (mCountries.contains(locale.getCountry())) {
                        // Shows the country name instead of "Other countries" if the current
                        // country is one of the countries this rating system applies to.
                        sb.append(locale.getDisplayCountry());
                    } else {
                        sb.append(mContext.getString(R.string.other_countries));
                    }
                }
            }
            if (!TextUtils.isEmpty(mTitle)) {
                sb.append(" (");
                sb.append(mTitle);
                sb.append(")");
            }
            String displayName = sb.toString();

            List<SubRating> subRatings = new ArrayList<>();
            if (mSubRatingBuilders != null) {
                for (SubRating.Builder builder : mSubRatingBuilders) {
                    subRatings.add(builder.build());
                }
            }

            if (mRatingBuilders.size() <= 0) {
                throw new IllegalArgumentException("Rating isn't available.");
            }
            List<Rating> ratings = new ArrayList<>();
            // Map string ID to object.
            for (Rating.Builder builder : mRatingBuilders) {
                ratings.add(builder.build(subRatings));
            }

            // Sanity check.
            for (SubRating subRating : subRatings) {
                boolean used = false;
                for (Rating rating : ratings) {
                    if (rating.getSubRatings().contains(subRating)) {
                        used = true;
                        break;
                    }
                }
                if (!used) {
                    throw new IllegalArgumentException("Subrating " + subRating.getName() +
                        " isn't used by any rating");
                }
            }

            List<Order> orders = new ArrayList<>();
            if (mOrderBuilders != null) {
                for (Order.Builder builder : mOrderBuilders) {
                    orders.add(builder.build(ratings));
                }
            }

            return new ContentRatingSystem(mName, mDomain, mTitle, mDescription, mCountries,
                    displayName, ratings, subRatings, orders, mIsCustom);
        }
    }

    public static class Rating {
        private final String mName;
        private final String mTitle;
        private final String mDescription;
        private final Drawable mIcon;
        private final int mContentAgeHint;
        private final List<SubRating> mSubRatings;

        public String getName() {
            return mName;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getDescription() {
            return mDescription;
        }

        public Drawable getIcon() {
            return mIcon;
        }

        public int getAgeHint() {
            return mContentAgeHint;
        }

        public List<SubRating> getSubRatings() {
            return mSubRatings;
        }

        private Rating(String name, String title, String description, Drawable icon,
                int contentAgeHint, List<SubRating> subRatings) {
            mName = name;
            mTitle = title;
            mDescription = description;
            mIcon = icon;
            mContentAgeHint = contentAgeHint;
            mSubRatings = subRatings;
        }

        public static class Builder {
            private String mName;
            private String mTitle;
            private String mDescription;
            private Drawable mIcon;
            private int mContentAgeHint = -1;
            private final List<String> mSubRatingNames = new ArrayList<>();

            public Builder() {
            }

            public void setName(String name) {
                mName = name;
            }

            public void setTitle(String title) {
                mTitle = title;
            }

            public void setDescription(String description) {
                mDescription = description;
            }

            public void setIcon(Drawable icon) {
                mIcon = icon;
            }

            public void setContentAgeHint(int contentAgeHint) {
                mContentAgeHint = contentAgeHint;
            }

            public void addSubRatingName(String subRatingName) {
                mSubRatingNames.add(subRatingName);
            }

            private Rating build(List<SubRating> allDefinedSubRatings) {
                if (TextUtils.isEmpty(mName)) {
                    throw new IllegalArgumentException("A rating should have non-empty name");
                }
                if (allDefinedSubRatings == null && mSubRatingNames.size() > 0) {
                    throw new IllegalArgumentException("Invalid subrating for rating " + mName);
                }
                if (mContentAgeHint < 0) {
                    throw new IllegalArgumentException("Rating " + mName + " should define " +
                        "non-negative contentAgeHint");
                }

                List<SubRating> subRatings = new ArrayList<>();
                for (String subRatingId : mSubRatingNames) {
                    boolean found = false;
                    for (SubRating subRating : allDefinedSubRatings) {
                        if (subRatingId.equals(subRating.getName())) {
                            found = true;
                            subRatings.add(subRating);
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException("Unknown subrating name " + subRatingId +
                                " in rating " + mName);
                    }
                }
                return new Rating(
                        mName, mTitle, mDescription, mIcon, mContentAgeHint, subRatings);
            }
        }
    }

    public static class SubRating {
        private final String mName;
        private final String mTitle;
        private final String mDescription;
        private final Drawable mIcon;

        public String getName() {
            return mName;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getDescription() {
            return mDescription;
        }

        public Drawable getIcon() {
            return mIcon;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!SubRating.class.isAssignableFrom(obj.getClass())) {
                return false;
            }
            final SubRating other = (SubRating) obj;
            if ((this.mName == null) ? (other.mName != null) : !this.mName.equals(other.mName)) {
                return false;
            }
            if ((this.mTitle == null) ? (other.mTitle != null) : !this.mTitle.equals(other.mTitle)) {
                return false;
            }
            if ((this.mDescription == null) ? (other.mDescription != null) : !this.mDescription.equals(other.mDescription)) {
                return false;
            }
            if ((this.mIcon == null) ? (other.mIcon != null) : !this.mIcon.equals(other.mIcon)) {
                return false;
            }
            return true;
        }

        private SubRating(String name, String title, String description, Drawable icon) {
            mName = name;
            mTitle = title;
            mDescription = description;
            mIcon = icon;
        }

        public static class Builder {
            private String mName;
            private String mTitle;
            private String mDescription;
            private Drawable mIcon;

            public Builder() {
            }

            public void setName(String name) {
                mName = name;
            }

            public void setTitle(String title) {
                mTitle = title;
            }

            public void setDescription(String description) {
                mDescription = description;
            }

            public void setIcon(Drawable icon) {
                mIcon = icon;
            }

            private SubRating build() {
                if (TextUtils.isEmpty(mName)) {
                    throw new IllegalArgumentException("A subrating should have non-empty name");
                }
                return new SubRating(mName, mTitle, mDescription, mIcon);
            }
        }
    }

    public static class Order {
        private final List<Rating> mRatingOrder;

        public List<Rating> getRatingOrder() {
            return mRatingOrder;
        }

        private Order(List<Rating> ratingOrder) {
            mRatingOrder = ratingOrder;
        }

        public static class Builder {
            private final List<String> mRatingNames = new ArrayList<>();

            public Builder() {
            }

            private Order build(List<Rating> ratings) {
                List<Rating> ratingOrder = new ArrayList<>();
                for (String ratingName : mRatingNames) {
                    boolean found = false;
                    for (Rating rating : ratings) {
                        if (ratingName.equals(rating.getName())) {
                            found = true;
                            ratingOrder.add(rating);
                            break;
                        }
                    }

                    if (!found) {
                        throw new IllegalArgumentException("Unknown rating " + ratingName +
                                " in rating-order tag");
                    }
                }

                return new Order(ratingOrder);
            }

            public void addRatingName(String name) {
                mRatingNames.add(name);
            }
        }
    }
}
