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

package com.mstar.tv.ui.dialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mstar.android.storage.MStorageManager;
import com.mstar.tv.R;

public class UsbDiskDialogFragment extends SafeDismissDialogFragment {
    public static final String DIALOG_TAG = UsbDiskDialogFragment.class.getName();

    public interface ResultListener {
        void onItemChosen(int position, String diskLabel, String diskPath);
    }

    private final int MAX_DISK_PER_PAGE = 4;
    private ResultListener mListener;
    private int mUsbDriverCount = 0;
    private ArrayList<String> mUsbDriverLabel = new ArrayList<String>();
    private ArrayList<String> mUsbDriverPath = new ArrayList<String>();
    private MStorageManager mStorageManager;

    public UsbDiskDialogFragment(ResultListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageManager = MStorageManager.getInstance(getActivity());
        updateUSBDriverInfo();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dlg = super.onCreateDialog(savedInstanceState);
        dlg.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dlg.setTitle(getActivity().getString(R.string.str_pvr_file_system_select_disk));
        return dlg;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dlg = getDialog();
        if (dlg != null) {
            dlg.getWindow().setLayout(
                    getResources().getDimensionPixelSize(R.dimen.slider_dialog_width),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.usb_driver_selecter, container, false);
        TextView noExternalStorageView = (TextView) view.findViewById(R.id.no_external_storage_text);
        GridView usbListView = (GridView) view.findViewById(R.id.usb_driver_selecter);
        if (mUsbDriverCount == 0) {
            noExternalStorageView.setVisibility(View.VISIBLE);
            usbListView.setVisibility(View.GONE);
        }
        else {
            usbListView.setNumColumns(mUsbDriverCount);
            int totalWidth = convDpToPx(200);
            int itemGap = convDpToPx(5);
            if (mUsbDriverCount <= MAX_DISK_PER_PAGE) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(totalWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                usbListView.setLayoutParams(params);
                usbListView.setColumnWidth((totalWidth - mUsbDriverCount * itemGap) / mUsbDriverCount);
            } else {
                LinearLayout.LayoutParams gridparams = new LinearLayout.LayoutParams(convDpToPx(65) * mUsbDriverCount,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                usbListView.setLayoutParams(gridparams);
                usbListView.setColumnWidth(convDpToPx(60));
            }
            usbListView.setAdapter(new UsbListAdapter(mUsbDriverCount));
            usbListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (mListener != null) {
                        mListener.onItemChosen(arg2, mUsbDriverLabel.get(arg2), mUsbDriverPath.get(arg2));
                    }
                    getDialog().dismiss();
                }
            });
            usbListView.setOnItemSelectedListener(new UsbDiskSelectedListener(
                    (HorizontalScrollView) view.findViewById(R.id.usb_driver_scroller)));
        }
        Button cancelButton = (Button) view.findViewById(R.id.usb_driver_selecter_cancel);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    public int convDpToPx(float dp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    private String getFileSystem(File file, String path) {
        if (file == null) {
            return "";
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                String[] info = line.split(" ");
                if (info[1].equals(path)) {
                    if (info[2].equals("ntfs3g"))
                        return "NTFS";
                    if (info[2].equals("vfat"))
                        return "FAT";
                    else
                        return info[2];
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private void updateUSBDriverInfo() {
        String[] volumes = mStorageManager.getVolumePaths();
        mUsbDriverCount = 0;
        mUsbDriverLabel.clear();
        mUsbDriverPath.clear();
        if (volumes == null) {
            return;
        }

        File file = new File("proc/mounts");
        if (!file.exists() || file.isDirectory()) {
            file = null;
        }

        for (int i = 0; i < volumes.length; ++i) {
            String state = mStorageManager.getVolumeState(volumes[i]);
            if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                continue;
            }
            String path = volumes[i];
            String[] pathPartition = path.split("/");
            String label = pathPartition[pathPartition.length - 1];
            String volumeLabel = mStorageManager.getVolumeLabel(path);
            if (volumeLabel != null) {
                // get rid of the long space in the Label word
                String[] tempVolumeLabel = volumeLabel.split(" ");
                volumeLabel = "";
                for (int j = 0; j < tempVolumeLabel.length; j++) {
                    if (j != tempVolumeLabel.length - 1) {
                        volumeLabel += tempVolumeLabel[j] + " ";
                        continue;
                    }
                    volumeLabel += tempVolumeLabel[j];
                }
            }
            label += ": " + getFileSystem(file, path) + "\n" + volumeLabel;
            mUsbDriverLabel.add(mUsbDriverCount, label);
            mUsbDriverPath.add(mUsbDriverCount, path);
            mUsbDriverCount++;
        }
        return;
    }

    private class UsbListAdapter extends BaseAdapter {

        private int itemCount = 0;

        public UsbListAdapter(int count) {
            itemCount = count;
        }

        @Override
        public int getCount() {
            return itemCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater layout = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layout.inflate(R.layout.usb_driver_item, parent, false);
            }
            TextView itemName = (TextView) view.findViewById(R.id.usbItemName);
            itemName.setText(mUsbDriverLabel.get(position));

            ProgressBar diskInfo = (ProgressBar) view.findViewById(R.id.usbItemSpace);
            ProgressBar tip = (ProgressBar) view.findViewById(R.id.tip);

            diskInfo.setMax(100);
            tip.setVisibility(View.VISIBLE);

            final UpdateHandler handler = new UpdateHandler(diskInfo, tip);
            new Thread() {
                @Override
                public void run() {
                    StatFs sf = new StatFs(mUsbDriverPath.get(position));
                    Message msg = Message.obtain();
                    msg.arg1 = (int) (100 - ((sf.getFreeBlocksLong()*100) / sf.getBlockCountLong()));
                    handler.sendMessage(msg);
                }
            }.start();
            return view;
        }

    }

    private class UsbDiskSelectedListener implements OnItemSelectedListener {

        private HorizontalScrollView scroller = null;

        private GridView itemParent = null;

        private int pageLength = 0;

        private final int Forward = 1;

        private final int Backward = 2;

        public UsbDiskSelectedListener(HorizontalScrollView view) {
            scroller = view;
            itemParent = (GridView) scroller.findViewById(R.id.usb_driver_selecter);
            pageLength = convDpToPx(520);
        }

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            switch (getScrollType(arg2)) {
                case Backward:
                    scroller.scrollTo(itemParent.getChildAt(arg2).getLeft() - convDpToPx(20), 0);
                    break;
                case Forward:
                    scroller.scrollTo(itemParent.getChildAt(arg2).getRight() - pageLength, 0);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }

        private int getScrollType(int position) {
            int itemPositionLeft = itemParent.getChildAt(position).getLeft();
            int itemPositionRight = itemParent.getChildAt(position).getRight();
            int scrollerPosition = scroller.getScrollX();
            if (itemPositionLeft > scrollerPosition
                    && itemPositionRight < pageLength + scrollerPosition) {
                return 0;
            }
            if (itemPositionLeft < scrollerPosition) {
                return Backward;
            }
            if (itemPositionRight > scrollerPosition + pageLength) {
                return Forward;
            }
            return 0;
        }
    }

    private static class UpdateHandler extends Handler {

        ProgressBar pb;

        ProgressBar tip;

        public UpdateHandler(ProgressBar pb, ProgressBar tip) {
            this.pb = pb;
            this.tip = tip;
        }

        @Override
        public void handleMessage(Message msg) {
            final int percentage = msg.arg1;
            pb.setProgress(percentage);
            tip.setVisibility(View.INVISIBLE);
        }

    }

    public void setResultListener(ResultListener listener) {
        mListener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
