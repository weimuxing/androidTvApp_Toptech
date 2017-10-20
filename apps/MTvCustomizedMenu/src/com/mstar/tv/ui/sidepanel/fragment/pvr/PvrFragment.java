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

package com.mstar.tv.ui.sidepanel.fragment.pvr;

import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.FileOutputStream;

import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.CheckBoxItem;
import com.mstar.tv.ui.sidepanel.item.CompoundButtonItem;
import com.mstar.tv.ui.sidepanel.item.DividerItem;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.RadioButtonItem;
import com.mstar.tv.ui.sidepanel.item.SubMenuItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.tv.ui.dialog.UsbDiskDialogFragment;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.storage.MStorageManager;
import com.mstar.tv.util.Constant;

import android.os.Handler;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.os.RemoteException;
import java.lang.Exception;

/**
 * Shows the pvrpower music setting
 */
public class PvrFragment extends SideFragment {
    private final static String TAG = "PvrFragment";

    private MStorageManager storageManager;

    private TvPvrManager mPvrManager = null;

    private Handler mHandler = new Handler();

    private String[] mShiftSizes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageManager = MStorageManager.getInstance(getActivity());
        mShiftSizes = getActivity().getResources().getStringArray(
                R.array.str_arr_pvr_time_shift_size);
    }

    /**
     * Set pvr select disk.
     */
    public class SelectDiskItem extends ActionItem {
        public final String DIALOG_TAG = SelectDiskItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private String mDiskPath;

        private String mDiskLabel;

        private int mPosition;

        public SelectDiskItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_pvr_select_disk));
            mMainActivity = mainActivity;
            setDescription(getChooseDiskPath());
        }

        @Override
        public void onSelected() {
            UsbDiskDialogFragment dialog = new UsbDiskDialogFragment(
                    new UsbDiskDialogFragment.ResultListener() {
                        @Override
                        public void onItemChosen(int position, String diskLabel, String diskPath) {

                            if (diskPath.isEmpty()) {
                                return;
                            }

                            mDiskPath = diskPath;
                            mDiskLabel = diskLabel;
                            mPosition = position;
                            Log.d(TAG, "diskPath :" + diskPath);
                            setDescription(diskPath);
                            SetDiskPath(diskPath, diskLabel);
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }

        private boolean SetDiskPath(String path, String label) {
            boolean bRet = false;
            String fat = "FAT";
            String ntfs = "NTFS";
            if (label.regionMatches(6, fat, 0, 3)) {
                bRet = TvPvrManager.getInstance().setPvrParams(path, (short) 2);
            } else if (label.regionMatches(6, ntfs, 0, 4) || !label.contains(fat)) {
                bRet = TvPvrManager.getInstance().setPvrParams(path, (short) 6);
            }
            saveChooseDiskSettings(true, path, label);
            return bRet;
        }

        private void saveChooseDiskSettings(boolean flag, String path, String label) {
            SharedPreferences sp = getActivity().getSharedPreferences(
                    new String("save_setting_select"), Context.MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putBoolean("IS_ALREADY_CHOOSE_DISK", flag);
            editor.putString("DISK_PATH", path);
            editor.putString("DISK_LABEL", label);
            editor.commit();
        }

        private String getChooseDiskPath() {
            SharedPreferences sp = getActivity().getSharedPreferences(
                    new String("save_setting_select"), Context.MODE_PRIVATE);
            return sp.getString("DISK_PATH", "");
        }
    }

    /**
     * Set pvr time shift size.
     */
    public class TimeShiftSizeItem extends ActionItem {
        public final String DIALOG_TAG = TimeShiftSizeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public TimeShiftSizeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_pvr_time_shift_size));
            mMainActivity = mainActivity;
            setDescription(getTimeShiftSizeText());
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new TimeShiftSizeFragment());
        }
    }

    /**
     * Set pvr format start
     */
    public class FormatStartItem extends ActionItem {
        public final String DIALOG_TAG = FormatStartItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private String mDiskPath;

        private String mDiskLabel;

        private int mPosition;

        private AlertDialog.Builder mBuilder = null;

        private AlertDialog mDialog = null;

        private ProgressDialog mpDialog = null;

        public FormatStartItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_pvr_format_start));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            UsbDiskDialogFragment dialog = new UsbDiskDialogFragment(
                    new UsbDiskDialogFragment.ResultListener() {
                        @Override
                        public void onItemChosen(int position, String diskLabel, String diskPath) {
                            if (diskPath.isEmpty()) {
                                return;
                            }
                            mDiskPath = diskPath;
                            mDiskLabel = diskLabel;
                            mPosition = position;
                            Log.d(TAG, "diskPath :" + diskPath);
                            setDescription(diskPath);
                            formatConfirm();
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }

        private void formatConfirm() {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mMainActivity);
            mBuilder.setTitle(getActivity().getResources().getString(R.string.str_pvr_format_usb));
            mBuilder.setPositiveButton(
                    getActivity().getResources().getString(R.string.str_program_edit_dialog_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startFormat();
                        }
                    });
            mBuilder.setNegativeButton(
                    getActivity().getResources().getString(R.string.str_program_edit_dialog_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            mDialog = mBuilder.create();
            mDialog.show();
        }

        private void stopRecordAndPlayback() {
            if (mPvrManager.isPlaybacking()) {
                mPvrManager.stopPlayback();
            }
            if (mPvrManager.isRecording()) {
                mPvrManager.stopRecord();
            }
        }

        private void startFormat() {
            Log.d(TAG, " startFormat, volume state:" + storageManager.getVolumeState(mDiskPath));
            stopRecordAndPlayback();
            if (storageManager.getVolumeState(mDiskPath).equals(Environment.MEDIA_MOUNTED)) {
                dounmount(mDiskPath);
            } else if (storageManager.getVolumeState(mDiskPath).equals(Environment.MEDIA_UNMOUNTED)) {
                startFormatDisk(mDiskPath);
            } else {
                Log.d(TAG, "Can not format " + mDiskPath);
            }
        }

        private void dounmount(String mPath) {
            Log.d(TAG, "dounmount, path:" + mPath);
            if (storageManager != null) {
                try {
                    storageManager.unmountVolume(mPath, true, false);
                } catch (Exception ex) {
                    Log.d(TAG, "dounmount, exception:" + ex);
                }
            }
        }

        private void startFormatDisk(final String path) {
            if (mpDialog == null) {
                mpDialog = new ProgressDialog(mMainActivity);
            }
            mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpDialog.setMessage(getResources().getString(R.string.str_pvr_format_usb_progressing));
            mpDialog.setIndeterminate(false);
            mpDialog.setCancelable(false);
            mpDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean result = storageManager.formatVolume(path);
                    Log.d(TAG, "formatVolume result: " + result);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result) {
                                if (storageManager.mountVolume(path)) {
                                    Log.d(TAG, "Success to mount" + path + " again");
                                } else {
                                    Log.d(TAG, "Fail to mount" + path + " again");
                                }
                            } else {
                                Log.d(TAG, "Fail to format" + path);
                            }
                            mpDialog.dismiss();
                            Toast toast = Toast.makeText(getActivity(), new String("formating..."),
                                    10);
                            toast.show();
                            mpDialog.dismiss();
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * Set pvr speed check.
     */
    public class SpeedCheckItem extends ActionItem {
        public final String DIALOG_TAG = SpeedCheckItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private String mDiskPath;

        private String mDiskLabel;

        private int mPosition;

        public SpeedCheckItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_pvr_speed_check));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            UsbDiskDialogFragment dialog = new UsbDiskDialogFragment(
                    new UsbDiskDialogFragment.ResultListener() {
                        @Override
                        public void onItemChosen(int position, String diskLabel, String diskPath) {
                            if (diskPath.isEmpty()) {
                                return;
                            }
                            mDiskPath = diskPath;
                            mDiskLabel = diskLabel;
                            mPosition = position;
                            Log.d(TAG, "diskPath :" + diskPath);
                            setDescription(diskPath);
                            startSpeedTest();
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }

        private void startSpeedTest() {
            final ProgressDialog mpDialog = new ProgressDialog(mMainActivity);
            mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpDialog.setMessage(getResources().getString(
                    R.string.str_pvr_usb_speed_test_progressing));
            mpDialog.setIndeterminate(false);
            mpDialog.setCancelable(false);
            mpDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String strMsg = checkSpeed(mDiskPath) + "KB/S";
                    Log.d(TAG, "check speed result: " + strMsg);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getActivity(), strMsg, 10);
                            toast.show();
                            mpDialog.dismiss();
                        }
                    });
                }
            }).start();
        }

        private int checkSpeed(String path) {
            int setSize = 100 * 1024 * 1024; // for file size
            long size = 0;
            byte[] data = new byte[512]; // for block size each time we write
            long duration = 0;
            String filePath = path + "/usbspeedtest.txt"; // file name for test

            // Create file
            FileOutputStream output = null;
            duration = System.currentTimeMillis();
            try {
                output = new FileOutputStream(filePath);

                // Get file size in bytes
                size = getFileSize(filePath);

                // Write file whilst the size is smaller than setSize
                while (size < setSize) {
                    output.write(data);
                    output.flush();
                    size = getFileSize(filePath);
                }

                duration = System.currentTimeMillis() - duration;
                output.close();
            } catch (IOException e) {
                Log.e(TAG, "create file fail");
                e.printStackTrace();
            }

            return mPvrManager.checkUsbSpeed();
        }
    }

    /**
     * Set pvr always time shift.
     */
    public class AlwaysTimeShiftItem extends SwitchItem {
        public final String DIALOG_TAG = AlwaysTimeShiftItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public AlwaysTimeShiftItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_pvr_always_timeshift));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    boolean isOn = mPvrManager.isAlwaysTimeShift();
                    Log.d(TAG, "always time shift :" + isOn);
                    if (isOn) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            mPvrManager.setAlwaysTimeShift(isChecked());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPvrManager == null) {
            mPvrManager = TvPvrManager.getInstance();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_pvr_title);
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        items.add(new SelectDiskItem((MainActivity) getActivity()));
        items.add(new TimeShiftSizeItem((MainActivity) getActivity()));
        items.add(new FormatStartItem((MainActivity) getActivity()));
        items.add(new SpeedCheckItem((MainActivity) getActivity()));
        items.add(new AlwaysTimeShiftItem((MainActivity) getActivity()));
        return items;
    }

    private long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            Log.d(TAG, "File does not exist");
            return -1;
        }
        return file.length();
    }

    private String getTimeShiftSizeText() {
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.SAVE_SETTING_SELECT,
                Context.MODE_PRIVATE);
        int idx = sp.getInt("LAST_SHIFT_SIZE", 0);
        if (idx <= mShiftSizes.length) {
            return mShiftSizes[idx];
        }
        return "";
    }
}
