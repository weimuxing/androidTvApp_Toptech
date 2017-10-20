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

package com.mstar.tv.ui.sidepanel.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.RadioButtonItem;

import java.util.List;

public abstract class SideFragment extends Fragment {
    public static final int INVALID_POSITION = -1;

    private static final int RECYCLED_VIEW_POOL_SIZE = 7;
    private static final int[] PRELOADED_VIEW_IDS = {
        R.layout.option_item_radio_button,
        R.layout.option_item_channel_lock,
        R.layout.option_item_check_box,
        R.layout.option_item_channel_check
    };

    private static RecyclerView.RecycledViewPool sRecycledViewPool;

    private VerticalGridView mListView;
    private ItemAdapter mAdapter;
    private SideFragmentListener mListener;

    private final int mHideKey;
    private final int mDebugHideKey;
    private final boolean debugKeysEnabled = false;

    public SideFragment() {
        this(KeyEvent.KEYCODE_UNKNOWN, KeyEvent.KEYCODE_UNKNOWN);
    }

    /**
     * @param hideKey the KeyCode used to hide the fragment
     * @param debugHideKey the KeyCode used to hide the fragment if
     *            {@link SystemProperties#USE_DEBUG_KEYS}.
     */
    public SideFragment(int hideKey, int debugHideKey) {
        mHideKey = hideKey;
        mDebugHideKey = debugHideKey;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (sRecycledViewPool == null) {
            // sRecycledViewPool should be initialized by calling preloadRecycledViews()
            // before the entering animation of this fragment starts,
            // because it takes long time and if it is called after the animation starts (e.g. here)
            // it can affect the animation.
            throw new IllegalStateException("The RecyclerView pool has not been initialized.");
        }
        View view = inflater.inflate(getFragmentLayoutResourceId(), container, false);

        TextView textView = (TextView) view.findViewById(R.id.side_panel_title);
        textView.setText(getTitle());

        mListView = (VerticalGridView) view.findViewById(R.id.side_panel_list);
        mListView.setRecycledViewPool(sRecycledViewPool);

        mAdapter = new ItemAdapter(inflater, getItemList());
        mListView.setAdapter(mAdapter);
        mListView.requestFocus();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public final boolean isHideKeyForThisPanel(int keyCode) {
        return mHideKey != KeyEvent.KEYCODE_UNKNOWN &&
                (mHideKey == keyCode || (debugKeysEnabled && mDebugHideKey == keyCode));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListView.swapAdapter(null, true);
        if (mListener != null) {
            mListener.onSideFragmentViewDestroyed();
        }
    }

    public final void setListener(SideFragmentListener listener) {
        mListener = listener;
    }

    protected void setSelectedPosition(int position) {
        mListView.setSelectedPosition(position);
    }

    protected int getSelectedPosition() {
        return mListView.getSelectedPosition();
    }

    public void setItems(List<Item> items) {
        mAdapter.reset(items);
    }

    protected void closeFragment() {
        getMainActivity().getSideFragmentManager().popSideFragment();
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /*
     * HACK: The following methods bypass the updating mechanism of RecyclerView.Adapter and
     * directly updates each item. This works around a bug in the base libraries where calling
     * Adapter.notifyItemsChanged() causes the VerticalGridView to lose track of displayed item
     * position.
     */

    protected void notifyItemChanged(int position) {
        notifyItemChanged(mAdapter.getItem(position));
    }

    protected void notifyItemChanged(Item item) {
        item.notifyUpdated();
    }

    /**
     * Notifies all items of ItemAdapter has changed without structural changes.
     */
    protected void notifyItemsChanged() {
        notifyItemsChanged(0, mAdapter.getItemCount());
    }

    /**
     * Notifies some items of ItemAdapter has changed starting from position
     * <code>positionStart</code> to the end without structural changes.
     */
    protected void notifyItemsChanged(int positionStart) {
        notifyItemsChanged(positionStart, mAdapter.getItemCount() - positionStart);
    }

    protected void notifyItemsChanged(int positionStart, int itemCount) {
        while (itemCount-- != 0) {
            notifyItemChanged(positionStart++);
        }
    }

    /*
     * END HACK
     */

    protected int getFragmentLayoutResourceId() {
        return R.layout.option_fragment;
    }

    protected abstract String getTitle();

    protected abstract List<Item> getItemList();

    public interface SideFragmentListener {
        void onSideFragmentViewDestroyed();
    }

    public static void preloadRecycledViews(Context context) {
        if (sRecycledViewPool != null) {
            return;
        }
        sRecycledViewPool = new RecyclerView.RecycledViewPool();
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int id : PRELOADED_VIEW_IDS) {
            sRecycledViewPool.setMaxRecycledViews(id, RECYCLED_VIEW_POOL_SIZE);
            for (int j = 0; j < RECYCLED_VIEW_POOL_SIZE; ++j) {
                ItemAdapter.ViewHolder viewHolder = new ItemAdapter.ViewHolder(
                        inflater.inflate(id, null, false));
                sRecycledViewPool.putRecycledView(viewHolder);
            }
        }
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private final LayoutInflater mLayoutInflater;
        private List<Item> mItems;

        private ItemAdapter(LayoutInflater layoutInflater, List<Item> items) {
            mLayoutInflater = layoutInflater;
            mItems = items;
        }

        private void reset(List<Item> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.onBind(this, getItem(position));
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            holder.onUnbind();
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getResourceId();
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        private Item getItem(int position) {
            return mItems.get(position);
        }

        private void clearRadioGroup(Item item) {
            int position = mItems.indexOf(item);
            for (int i = position - 1; i >= 0; --i) {
                if ((item = mItems.get(i)) instanceof RadioButtonItem) {
                    ((RadioButtonItem) item).setChecked(false);
                } else {
                    break;
                }
            }
            for (int i = position + 1; i < mItems.size(); ++i) {
                if ((item = mItems.get(i)) instanceof RadioButtonItem) {
                    ((RadioButtonItem) item).setChecked(false);
                } else {
                    break;
                }
            }
        }

        private static class ViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener, View.OnFocusChangeListener {
            private ItemAdapter mAdapter;
            public Item mItem;

            private ViewHolder(View view) {
                super(view);
                itemView.setOnClickListener(this);
                itemView.setOnFocusChangeListener(this);
            }

            public void onBind(ItemAdapter adapter, Item item) {
                mAdapter = adapter;
                mItem = item;
                mItem.onBind(itemView);
                mItem.onUpdate();
            }

            public void onUnbind() {
                mItem.onUnbind();
                mItem = null;
                mAdapter = null;
            }

            @Override
            public void onClick(View view) {
                if (mItem instanceof RadioButtonItem) {
                    mAdapter.clearRadioGroup(mItem);
                }
                if (view.getBackground() instanceof RippleDrawable) {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mItem != null) {
                                mItem.onSelected();
                            }
                        }
                    }, view.getResources().getInteger(R.integer.side_panel_ripple_anim_duration));
                } else {
                    mItem.onSelected();
                }
            }

            @Override
            public void onFocusChange(View view, boolean focusGained) {
                if (focusGained) {
                    mItem.onFocused();
                }
            }
        }
    }
}
