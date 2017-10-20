//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

package mstar.tvsetting.factory.ui.designmenu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.lang.Thread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.factorymenu.BaseActivity;

import com.mstar.android.storage.MStorageManager;
import com.mstar.android.tv.TvCommonManager;

public class ExecuteShellActivity extends BaseActivity {
    private static final String TAG = "ExecuteShellActivity";

    private final String FACTORY_FOLDER_NAME = "_MSTFactory";

    private final String SOURCE_USB_FOLDER = "/mnt/usb/sda1/" + FACTORY_FOLDER_NAME;

    private final String TARGET_TMP_FOLDER = "/data/" + FACTORY_FOLDER_NAME;

    private final String SHELL_SCRIPT_FILE = "MScript.sh";

    private final int UPDATE_INFO = 100;

    protected Thread mRunShThread = null;

    private TextView mInfo = null;

    private String mStrInfo = "";

    private String mUsbStoragePath = "";

    private Runnable runSh = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Thread - Start!");
            mStrInfo += "Removing " + TARGET_TMP_FOLDER + "...";
            mHandler.sendEmptyMessage(UPDATE_INFO);
            Process p;
            try {
                p = Runtime.getRuntime().exec("rm -rf " + TARGET_TMP_FOLDER);
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                mStrInfo += "\nError: Failed to remove " + TARGET_TMP_FOLDER;
                mHandler.sendEmptyMessage(UPDATE_INFO);
                return;
            }
            mStrInfo += "done\n";
            mHandler.sendEmptyMessage(UPDATE_INFO);

            if (mUsbStoragePath.equals("")) {
                mStrInfo += "\nError: No any external stroage exists!\n";
                mHandler.sendEmptyMessage(UPDATE_INFO);
                return;
            }

            File tmpFolder = new File(mUsbStoragePath + "/");
            if (false == tmpFolder.isDirectory()) {
                mStrInfo += "\nError: Can not find " + mUsbStoragePath + " folder";
                mHandler.sendEmptyMessage(UPDATE_INFO);
                return;
            }

            mStrInfo += "Copying files to " + TARGET_TMP_FOLDER + "...";
            mHandler.sendEmptyMessage(UPDATE_INFO);
            try {
                p = Runtime.getRuntime().exec("cp -R " + mUsbStoragePath + "/ " + TARGET_TMP_FOLDER);
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                mStrInfo += "\nError: Failed to copy " + mUsbStoragePath + "/ to " + TARGET_TMP_FOLDER;
                mHandler.sendEmptyMessage(UPDATE_INFO);
                return;
            }
            mStrInfo += "done\n";
            mHandler.sendEmptyMessage(UPDATE_INFO);

            mStrInfo += "Changing 777 for " + TARGET_TMP_FOLDER + "...";
            mHandler.sendEmptyMessage(UPDATE_INFO);
            try {
                p = Runtime.getRuntime().exec("chmod 777 -R " + TARGET_TMP_FOLDER + "/");
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                mStrInfo += "\nError: Failed to change folder attributes to 777";
                mHandler.sendEmptyMessage(UPDATE_INFO);
                return;
            }
            mStrInfo += "done\n";
            mHandler.sendEmptyMessage(UPDATE_INFO);

            Log.d(TAG, "Executing sh...");
            mStrInfo += "Executing " + TARGET_TMP_FOLDER + "/" + SHELL_SCRIPT_FILE + "...";
            mHandler.sendEmptyMessage(UPDATE_INFO);

            File shFile = new File(TARGET_TMP_FOLDER + "/" + SHELL_SCRIPT_FILE);
            if (false == shFile.exists()) {
                mStrInfo += "\nError: Can not find " + TARGET_TMP_FOLDER + "/" + SHELL_SCRIPT_FILE;
                mHandler.sendEmptyMessage(UPDATE_INFO);
                return;
            }

            try {
                p = Runtime.getRuntime().exec("sh " + TARGET_TMP_FOLDER + "/" + SHELL_SCRIPT_FILE);
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                mStrInfo += "\nError: Failed to execute shell \"" + TARGET_TMP_FOLDER +"/" + SHELL_SCRIPT_FILE + "\"";
                mHandler.sendEmptyMessage(UPDATE_INFO);
                return;
            }
            mStrInfo += "done\n";
            Log.d(TAG, "Thread - End!");

            mStrInfo += "\n\nPlease press Exit key to leave this screen\n";
            mHandler.sendEmptyMessage(UPDATE_INFO);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.executeshell);
        super.onCreate(savedInstanceState);
        mInfo = (TextView) findViewById(R.id.textview_executeshell_info);

        if (VERSION.SDK_INT <= TvCommonManager.API_LEVEL_LOLLIPOP_MR1) {
            mUsbStoragePath = SOURCE_USB_FOLDER;
        } else {
            MStorageManager storageMgr = MStorageManager.getInstance(this);
            String[] volumes = storageMgr.getVolumePaths();
            boolean first = false;
            if (volumes != null) {
                for (int i = 0; i < volumes.length; ++i) {
                    String state = storageMgr.getVolumeState(volumes[i]);
                    if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                        continue;
                    }
                    String path = volumes[i] + "/" + FACTORY_FOLDER_NAME;
                    if (false == first) {
                        first = true;
                        mUsbStoragePath = path;
                    }
                    File tmpFolder = new File(volumes[i] + "/" + FACTORY_FOLDER_NAME);
                    if (true == tmpFolder.isDirectory()) {
                        mUsbStoragePath = path;
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        if ((mRunShThread == null)
                || (mRunShThread.getState() == Thread.State.TERMINATED)) {
            mRunShThread = new Thread(runSh);
            mRunShThread.start();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        if ((mRunShThread != null)
                && (mRunShThread.getState() != Thread.State.TERMINATED)) {
            mRunShThread.interrupted();
            mRunShThread = null;
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                finish();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_INFO) {
                mInfo.setText(mStrInfo);
            }
        };
    };
}
