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

package mstar.tvsetting.factory.ui.designmenu;

import java.io.File;
import java.util.ArrayList;

import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tv.TvCommonManager;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.FactoryDeskImpl;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.factorymenu.BaseActivity;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvFactoryManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.storage.MStorageManager;

public class FactoryProgramPresetActivity extends BaseActivity {
    private static final String TAG = "FactoryProgramPresetActivity";

    private final int DTV_ROUTE_TYPE_DEFAULT_VAL = 0x000001;

    private IFactoryDesk mFactoryManager;

    private MStorageManager mStorageManager;

    protected LinearLayout mLayoutProgramPreset;

    protected LinearLayout mLayoutProgramPresetAtvOrDtv;

    protected LinearLayout mLayoutProgramReset;

    protected LinearLayout mLayoutBackupDbtoUsb;

    protected LinearLayout mLayoutRestoreDbfromUsb;

    protected TextView mTextProgramPresetState;

    protected Button mAtvPreset;

    protected Button mDtvPreset;

    protected TextView mTextProgramResetState;

    protected TextView mTextBackupDbtoUsbState;

    protected TextView mTextRestoreDbfromUsbState;

    protected Boolean mClickOpenState = true;

    protected ProgressDialog mProgressDialogAtv = null;

    protected ProgressDialog mProgressDialogDtv = null;

    private factoryProgramFinishedEventListener mFactoryProgramFinishedEventListener = null;

    private ArrayList<String> mSupportDtvRouteType = new ArrayList<String>();

    private ArrayList<Integer> mSupportDtvRouteTypeData = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mFactoryManager = FactoryDeskImpl.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factoryprogrampreset);
        findView();
        TvTypeInfo tvinfo = TvCommonManager.getInstance().getTvInfo();
        String[] dtvRouteTypeStr = this.getResources().getStringArray(R.array.str_arr_dtv_route_type);
        mFactoryProgramFinishedEventListener = new factoryProgramFinishedEventListener();
        TvFactoryManager.getInstance().registerOnFactoryProgramFinishedEventListener(mFactoryProgramFinishedEventListener);
        int dtvRouteTypeVal = (int) tvinfo.dtvType;
        if (TvChannelManager.TV_ROUTE_NONE == dtvRouteTypeVal) {
            Toast.makeText(FactoryProgramPresetActivity.this,
                           this.getResources().getString(R.string.str_factoryprogrampreset_no_support_dtv_route),Toast.LENGTH_SHORT).show();
        } else {
            int dtvRouteTypeLength = dtvRouteTypeStr.length;
            for (int i = 1; i <= dtvRouteTypeLength - 1; i++) {
                int tempType = DTV_ROUTE_TYPE_DEFAULT_VAL;
                tempType = tempType << (i - 1);
                if ((dtvRouteTypeVal & tempType) != 0 ? true : false) {
                    mSupportDtvRouteType.add(dtvRouteTypeStr[i]);
                    mSupportDtvRouteTypeData.add(new Integer(tempType));
                }
            }
            final String[] arr = (String[]) mSupportDtvRouteType
                    .toArray(new String[mSupportDtvRouteType.size()]);
            mAtvPreset.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    TvFactoryManager.getInstance().restoreFactoryAtvProgramTable(TvFactoryManager.CITY_NUM_ONE);
                    mProgressDialogAtv = ProgressDialog.show(FactoryProgramPresetActivity.this,
                            FactoryProgramPresetActivity.this.getResources().getString(R.string.str_factoryprogrampreset_waiting),
                            FactoryProgramPresetActivity.this.getResources().getString(R.string.str_factoryprogrampreset_being_preseted_atv), true, true,
                            mCancelListener);
                }
            });
            mDtvPreset.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    new AlertDialog.Builder(FactoryProgramPresetActivity.this)
                            .setTitle(FactoryProgramPresetActivity.this.getResources().getString(R.string.str_factoryprogrampreset_choose_type))
                            .setItems(arr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TvFactoryManager.getInstance().restoreFactoryDtvProgramTableByRoute(TvFactoryManager.CITY_NUM_ONE,
                                            mSupportDtvRouteTypeData.get(which).intValue());
                                    mProgressDialogDtv = ProgressDialog.show(
                                            FactoryProgramPresetActivity.this, FactoryProgramPresetActivity.this.getResources().getString(R.string.str_factoryprogrampreset_waiting),
                                            FactoryProgramPresetActivity.this.getResources().getString(R.string.str_factoryprogrampreset_being_preseted_dtv), true, true, mCancelListener);
                                }
                            }).setNegativeButton(FactoryProgramPresetActivity.this.getResources().getString(R.string.str_factoryprogrampreset_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        TvFactoryManager.getInstance().unregisterOnFactoryProgramFinishedEventListener(
                mFactoryProgramFinishedEventListener);
        mFactoryProgramFinishedEventListener = null;
        super.onDestroy();
    }

    private OnCancelListener mCancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            Toast.makeText(FactoryProgramPresetActivity.this,
                    FactoryProgramPresetActivity.this.getResources().getString(R.string.str_factoryprogrampreset_done),
                    Toast.LENGTH_SHORT).show();
        }
    };

    private class factoryProgramFinishedEventListener implements
            TvFactoryManager.OnFactoryProgramFinishedEventListener {
        @Override
        public boolean onFactoryProgramFinishedEvent(int what, int arg1, int arg2, Object obj) {
            Log.i(TAG, "onFactoryProgramFinishedEvent(), what:" + what);
            switch (what) {
                case TvFactoryManager.EV_TV_FACTORYPROGRAM_FINISHED:
                    if (null != mProgressDialogDtv) {
                        Log.i(TAG,"DTV program finished!");
                        mProgressDialogDtv.dismiss();
                        mProgressDialogDtv = null;
                    }
                    if (null != mProgressDialogAtv) {
                        Log.i(TAG,"ATV program finished!");
                        mProgressDialogAtv.dismiss();
                        mProgressDialogAtv = null;
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    }

    public void findView() {
        mLayoutProgramPreset = (LinearLayout) findViewById(R.id.linearlayout_factory_otheroption_factoryprogrampreset);
        mLayoutProgramReset = (LinearLayout) findViewById(R.id.linearlayout_factory_otheroption_factoryprogramreset);
        mLayoutBackupDbtoUsb = (LinearLayout) findViewById(R.id.linearlayout_factory_otheroption_backupdbtousb);
        mLayoutRestoreDbfromUsb = (LinearLayout) findViewById(R.id.linearlayout_factory_otheroption_restoredbfromusb);
        mLayoutProgramPresetAtvOrDtv = (LinearLayout) findViewById(R.id.linearlayout_factory_otheroption_factoryprogrampreset_atv_dtv);
        mAtvPreset = (Button) findViewById(R.id.button_atv_preset);
        mDtvPreset = (Button) findViewById(R.id.button_dtv_preset);
        mTextProgramPresetState = (TextView) findViewById(R.id.textview_factory_otheroption_factoryprogrampreset_val);
        mTextProgramResetState = (TextView) findViewById(R.id.textview_factory_otheroption_factoryprogramreset_val);
        mTextBackupDbtoUsbState = (TextView) findViewById(R.id.textview_factory_otheroption_backupdbtousb_val);
        mTextRestoreDbfromUsbState = (TextView) findViewById(R.id.textview_factory_otheroption_restoredbfromusb_val);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bRet = true;
        int currentid = this.getCurrentFocus().getId();
        boolean bValue;
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                switch (currentid) {
                    case R.id.linearlayout_factory_otheroption_factoryprogrampreset:
                        if (true == mClickOpenState) {
                            mLayoutProgramPresetAtvOrDtv.setVisibility(View.VISIBLE);
                            mClickOpenState = false;
                        } else {
                            mLayoutProgramPresetAtvOrDtv.setVisibility(View.GONE);
                            mClickOpenState = true;
                        }
                        break;
                    case R.id.linearlayout_factory_otheroption_factoryprogramreset:
                        if (mFactoryManager.resetToFactoryDefault() == true) {
                            mTextProgramResetState
                                    .setText(R.string.str_textview_factory_otheroption_text_pass);
                        } else {
                            mTextProgramResetState
                                    .setText(R.string.str_textview_factory_otheroption_text_fail);
                        }
                        break;
                    case R.id.linearlayout_factory_otheroption_backupdbtousb:
                        if (BackupDBtoUSB() == 0) {
                            mTextBackupDbtoUsbState
                                    .setText(R.string.str_textview_factory_otheroption_text_pass);
                        } else {
                            mTextBackupDbtoUsbState
                                    .setText(R.string.str_textview_factory_otheroption_text_fail);
                        }
                        break;
                    case R.id.linearlayout_factory_otheroption_restoredbfromusb:
                        if (RestoreDBfromUSB() == 0) {
                            mTextRestoreDbfromUsbState
                                    .setText(R.string.str_textview_factory_otheroption_text_pass);
                        } else {
                            mTextRestoreDbfromUsbState
                                    .setText(R.string.str_textview_factory_otheroption_text_fail);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                finish();
                break;
            default:
                bRet = false;
                break;
        }
        return bRet;
    }

    private int BackupDBtoUSB() {
        int ret = -1;
        String DBPATH = getResources().getString(R.string.str_factory_db_path);
        String DBFILE = getResources().getString(R.string.str_factory_db_file);
        String srcFile;
        String destPath;

        mStorageManager = MStorageManager.getInstance(this);
        String[] volumes = mStorageManager.getVolumePaths();
        if (volumes == null) {
            return ret;
        }

        for (int i = 0; i < volumes.length; ++i) {
            String state = mStorageManager.getVolumeState(volumes[i]);
            if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                continue;
            }
            Log.i(TAG, "usb path = " + volumes[i]);

            if (!isFileExit(DBPATH, "cmdb")) {
                Log.i(TAG, "Can't find *Cmdb*.bin ");
                return ret;
            }

            srcFile = DBPATH + DBFILE;
            destPath = volumes[i] + "/";

            ret = mFactoryManager.copyCmDb(srcFile, destPath);
            break;
        }
        return ret;
    }

    private int RestoreDBfromUSB() {
        int ret = -1;
        String DBPATH = getResources().getString(R.string.str_factory_db_path);
        String DBFILE = getResources().getString(R.string.str_factory_db_file);
        String srcFile;
        String destPath;

        mStorageManager = MStorageManager.getInstance(this);
        String[] volumes = mStorageManager.getVolumePaths();

        if (volumes == null) {
            return ret;
        }

        for (int i = 0; i < volumes.length; ++i) {
            String state = mStorageManager.getVolumeState(volumes[i]);
            if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                continue;
            }
            Log.i(TAG, "usb path = " + volumes[i]);

            String srcPath = volumes[i] + "/";
            if (!isFileExit(srcPath, "cmdb")) {
                Log.i(TAG, "Can't find *Cmdb*.bin ");
                return ret;
            }

            srcFile = volumes[i] + "/" + DBFILE;
            destPath = DBPATH;

            ret = mFactoryManager.copyCmDb(srcFile, destPath);
            break;
        }
        return ret;
    }

    private boolean isFileExit(String path, String keyword) {
        File[] files = new File(path).listFiles();
        for (File f : files) {
            if (f.getName().indexOf(keyword) >= 0) {
                return true;
            }
        }
        return false;
    }
}
