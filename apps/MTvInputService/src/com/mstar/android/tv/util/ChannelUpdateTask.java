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

package com.mstar.android.tv.util;

import android.content.Context;
import android.content.Intent;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.media.tv.TvContract;
import android.net.Uri;
import android.util.Log;

import com.mstar.android.tv.util.TunerUtility;
import com.mstar.android.tv.util.TifChannelInfo;
import com.mstar.android.tv.tunersetup.TvIntent;
import com.mstar.android.tv.inputservice.R;

import java.util.ArrayList;
import java.lang.ref.WeakReference;

/**
 * ChannelUpdateTask
 */
public class ChannelUpdateTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "ChannelUpdateTask";
    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);
    private WeakReference<Context> mContext;
    private String mInputId;
    private boolean mIsScan;  // true: the update caused by scanning. false: the update is caused by TS_CHANGE event or others.

    public ChannelUpdateTask(Context context, String inputId, boolean isScan) {
        mContext = new WeakReference<>(context);
        mInputId = inputId;
        mIsScan = isScan;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (isCancelled()) {
            if (DEBUG) Log.d(TAG, "ChannelUpdateTask: ChannelUpdateTask has been canceled!!!");
            return null;
        }
        ContentResolver resolver = mContext.get().getContentResolver();
        Uri channelUri = TvContract.buildChannelsUriForInput(mInputId);
        ArrayList<TifChannelInfo> tifChannelList = BroadcastType.getInstance().buildTifChannelInfoList(mInputId);
        if (tifChannelList != null && tifChannelList.size() > 0) {
            if (!mIsScan) {
                ArrayList<TifChannelInfo> oldChannelList = getOldChannelList(channelUri);
                if (compareChannelLists(oldChannelList, tifChannelList)) {
                    // If channel counts and display number of every channel of
                    // two channel lists are the same then update channels only.
                    Log.d(TAG, "ChannelUpdateTask: only update channels");
                    ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                    for (int i = 0; i < tifChannelList.size(); i++) {
                        if (isCancelled()) {
                            if (DEBUG) Log.d(TAG, "ChannelUpdateTask: ChannelUpdateTask has been canceled!!!");
                            return null;
                        }
                        ops.add(ContentProviderOperation
                                .newUpdate(TvContract.buildChannelUri(oldChannelList.get(i).id))
                                .withValue(TvContract.Channels.COLUMN_DISPLAY_NAME, tifChannelList.get(i).displayName)
                                .withValue(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA, tifChannelList.get(i).internalProviderData)
                                .build());
                    }
                    try {
                        if (isCancelled()) {
                            if (DEBUG) Log.d(TAG, "ChannelUpdateTask: ChannelUpdateTask has been canceled!!!");
                            return null;
                        }
                        resolver.applyBatch(TvContract.AUTHORITY, ops);
                    } catch (RemoteException | OperationApplicationException e) {
                        Log.e(TAG, "error in applyBatch", e);
                    }
                    return null;
                }
            }
            if (isCancelled()) {
                if (DEBUG) Log.d(TAG, "ChannelUpdateTask: ChannelUpdateTask has been canceled!!!");
                return null;
            }
            // If channel update due to scanning or channel count/display number
            // changes then delete old programs/channels and insert new channels.
            TunerUtility.deleteProgramsByChannel(mContext.get(), channelUri);
            resolver.delete(channelUri, null, null);
            ContentValues[] contentArray = new ContentValues[tifChannelList.size()];
            int index = 0;
            for (TifChannelInfo channelInfo : tifChannelList) {
                if (isCancelled()) {
                    if (DEBUG) Log.d(TAG, "ChannelUpdateTask: ChannelUpdateTask has been canceled!!!");
                    return null;
                }
                if (!channelInfo.isValid()) {
                    continue;
                }
                contentArray[index++] = TunerUtility.putChannelInfoToContentValues(channelInfo);
                if (DEBUG) {
                    Log.d(TAG, "***********************************");
                    Log.d(TAG, "displayNumber = " + channelInfo.displayNumber);
                    Log.d(TAG, "displayName = " + channelInfo.displayName);
                    Log.d(TAG, "type = " + channelInfo.type);
                    Log.d(TAG, "serviceType = " + channelInfo.serviceType);
                    Log.d(TAG, "transportStreamId = " + channelInfo.transportStreamId);
                    Log.d(TAG, "serviceId = " + channelInfo.serviceId);
                    Log.d(TAG, "isLocked = " + channelInfo.isLocked);
                    Log.d(TAG, "***********************************");
                }
            }
            if (isCancelled()) {
                if (DEBUG) Log.d(TAG, "ChannelUpdateTask: ChannelUpdateTask has been canceled!!!");
                return null;
            }
            resolver.bulkInsert(TvContract.Channels.CONTENT_URI, contentArray);
            Intent intent = new Intent(TvIntent.ACTION_CHANNEL_HAS_UPDATED);
            mContext.get().sendBroadcast(intent);
        } else {
            TunerUtility.deleteProgramsByChannel(mContext.get(), channelUri);
            resolver.delete(channelUri, null, null);
        }
        return null;
    }

    private ArrayList<TifChannelInfo> getOldChannelList(Uri uri) {
        ArrayList<TifChannelInfo> oldChannelList = new ArrayList<TifChannelInfo>();
        String[] projection = { TvContract.Channels._ID,
                TvContract.Channels.COLUMN_TYPE,
                TvContract.Channels.COLUMN_SERVICE_TYPE,
                TvContract.Channels.COLUMN_SERVICE_ID,
                TvContract.Channels.COLUMN_DISPLAY_NUMBER };
        Cursor cursor = mContext.get().getContentResolver().query(
                uri, projection, null, null, null);
        try {
            while (cursor != null && cursor.moveToNext()) {
                TifChannelInfo info = new TifChannelInfo();
                info.id = cursor.getLong(cursor.getColumnIndex(TvContract.Channels._ID));
                info.type = cursor.getString(cursor.getColumnIndex(TvContract.Channels.COLUMN_TYPE));
                info.serviceType = cursor.getString(cursor.getColumnIndex(TvContract.Channels.COLUMN_SERVICE_TYPE));
                info.serviceId = cursor.getInt(cursor.getColumnIndex(TvContract.Channels.COLUMN_SERVICE_ID));
                info.displayNumber = cursor.getString(cursor.getColumnIndex(TvContract.Channels.COLUMN_DISPLAY_NUMBER));
                oldChannelList.add(info);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return oldChannelList;
    }

    private boolean compareChannelLists(ArrayList<TifChannelInfo> list1, ArrayList<TifChannelInfo> list2) {
        // Check two channel lists if their channel counts and display number of every channel are the same.
        if (list1 == null || list2 == null ||
                list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).type.equals(list2.get(i).type) ||
                    !list1.get(i).serviceType.equals(list2.get(i).serviceType) ||
                    list1.get(i).serviceId != list2.get(i).serviceId ||
                    !list1.get(i).displayNumber.equals(list2.get(i).displayNumber)) {
                return false;
            }
        }
        return true;
    }
}
