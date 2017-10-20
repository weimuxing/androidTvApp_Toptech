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
package com.jrm.localmm.ui.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.jrm.localmm.R;
import com.jrm.localmm.business.adapter.MusicListDataAdapter;
import com.jrm.localmm.business.data.BaseData;
import android.util.DisplayMetrics;
import android.content.DialogInterface;

/**
 * Custom audio playlist Dialog
 * @author Ebric
 */
public class MusicListDialog extends Dialog {
    private static final String TAG = "MusicListDialog";
    private Activity activity;
    // Music list
    private ListView musicList;
    private Handler handler;
    private MusicListDataAdapter simpleAdapter;
    private List<BaseData> list;
    // The selected video
    private int selected = 0;
    private int mPosition;
    private Toast mToast = null;

    private boolean isListTop = false;
    private boolean isListLast = false;

    public MusicListDialog(Activity activity, Handler handler, int style) {

        super(activity, style);
        this.activity = activity;
        this.handler = handler;
    }

    public MusicListDialog(Activity activity, Handler handler) {
        super(activity);
        this.activity = activity;
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_list);
        Window w = getWindow();
        w.setTitle(null);
        Display display = w.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        Log.d(TAG, "x : " + point.x + " y : " + point.y);
        int width = (int) (point.x * 0.4);
        int height = (int) (point.y * 0.56);

        WindowManager.LayoutParams wl = w.getAttributes();

        if (activity != null) {
            DisplayMetrics metrics = activity.getResources()
                    .getDisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.i(TAG, "metrics.density:" + metrics.density);
            if (metrics.density == 1.5) {
                w.setBackgroundDrawableResource(color.transparent);
                wl.y = -(int) (point.y * 0.2);
                height = (int) (point.y * 0.7);
            }
        }

        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);

        w.setAttributes(wl);
        // w.setBackgroundDrawableResource(color.transparent);
        mPosition = MusicPlayerActivity.currentPosition;
        // control initialization
        findView();
        musicList.setClickable(true);
        Log.d(TAG, "listview is focused : " + musicList.isFocused());
        // ListView focus monitoring
        addListener();
    }

    /**
     * update ListView current focus position.
     * @param position the current focus index.
     */
    protected void setSelection(int position) {
        musicList.setSelection(position);
        simpleAdapter.notifyDataSetChanged();
    }

    /*
     * Initialization module
     */
    private void findView() {
        musicList = (ListView) findViewById(R.id.MusicFilename);
        getMusicName();
        musicList.setDividerHeight(0);
        musicList.requestFocus();
        musicList.setEnabled(true);
        musicList.setFocusable(true);
        musicList.setFocusableInTouchMode(true);
        setSelection(mPosition);
    }

    /*
     * registering listeners
     */
    private void addListener() {
        // add click monitor
        musicList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position >= 0) {
                    selected = position;
                    MusicPlayerActivity.currentPosition = selected;
                    new Thread(new Runnable() {
                        public void run() {
                            handler.sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_SERVICE_START);
                        }
                    }).start();
                    dismiss();
                }
            }
        });
        // Add selected monitoring
        musicList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setOnDismissListener(listener);
    }

    /**
     * For video data.
     */
    private void getMusicName() {
        list = MusicPlayerActivity.musicList;
        int size = list.size();
        String listData[] = new String[size];
        for (int i = 0; i < size; i++) {
            listData[i] = list.get(i).getName();
        }
        simpleAdapter = new MusicListDataAdapter(activity, listData);
        musicList.setAdapter(simpleAdapter);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode
                || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode
                || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }

        // Selected songs began to play
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            MusicPlayerActivity.currentPosition = selected;
            new Thread(new Runnable() {
                public void run() {
                    handler.sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_SERVICE_START);
                }
            }).start();
            dismiss();
            // Custom playlists, the user can delete play in the list of songs
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.exit_title);
            builder.setMessage(R.string.exit_confirm);
            builder.setPositiveButton(activity.getString(R.string.exit_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                            if (list.size() > 0 && selected < list.size()) {
                                list.remove(selected);
                                getMusicName();
                                setSelection(selected);
                                simpleAdapter.notifyDataSetChanged();
                                mDialog.dismiss();
                            }
                        }
                    });
            builder.setNeutralButton(activity.getString(R.string.exit_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                            mDialog.dismiss();
                        }
                    });
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int arg1,
                        KeyEvent arg2) {
                    if (KeyEvent.KEYCODE_MEDIA_PLAY == arg1
                            || KeyEvent.KEYCODE_MEDIA_PAUSE == arg1
                            || KeyEvent.KEYCODE_MEDIA_NEXT == arg1
                            || KeyEvent.KEYCODE_MEDIA_PREVIOUS == arg1) {
                        return true;
                    }
                    return false;
                }
            });
            builder.show();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (selected == 0) {
                if (isListTop) {
                    showToast(R.string.the_first_file);
                } else {
                    isListTop = true;
                }
            }
            isListLast = false;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (selected == (list.size() - 1)) {
                if (isListLast) {
                    showToast(R.string.the_last_file);
                } else {
                    isListLast = true;
                }
            }
            isListTop = false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * Tip is currently play list of the first or the last term.
     */
    private void showToast(int id) {
        if (mToast != null) {
            mToast.setText(id);
            mToast.show();
            return;
        }
        mToast = Toast.makeText(activity,
                activity.getResources().getString(id), Toast.LENGTH_SHORT);
        mToast.show();

    }

    public DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MusicPlayerActivity)activity).pauseOrPlayPresentMusicNameMarquee();
        }
    };
}
