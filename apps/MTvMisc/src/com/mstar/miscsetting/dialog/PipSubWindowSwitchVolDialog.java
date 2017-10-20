
package com.mstar.miscsetting.dialog;

import java.util.ArrayList;

import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureDeviceType;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureSource;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.miscsetting.R;
import com.mstar.miscsetting.dialog.PipSubWindowInputSourceDialog.PIP_MODE_ENABLED;
import com.mstar.util.Tools;
import com.mstar.util.Utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class PipSubWindowSwitchVolDialog extends Dialog {

    public enum PIP_MODE_ENABLED {
        // / Pip mode
        PIP_ENABLED,
        // / Pop mode
        POP_ENABLED,
        // / dual mode
        DUAL_ENABLED,
        // / mode num
        NUM_EANBLED
    }

    public enum SWITCH_VOL {
        // spidf main
        SPIDF_MAIN,
        // spidf sub
        SPIDF_SUB,
        // BT headset main
        BT_HEADSET_MAIN,
        // BT headset sub
        BT_HEADSET_SUB

    }

    // add by ken.bi [25/4/2013]
    TvAudioManager tvAudioManager;

    private static final String TAG = "PipSubWindowSwitchVolDialog";

    private Context mActivity;

    private ArrayList<SwitchVolItem> data = new ArrayList<SwitchVolItem>();

    private ListView switchVolListView;

    private SwitchVolAdapter adapter = null;

    private TvPipPopManager tvPipPopManager; // TODO

    private int[] mIntArrayList;

    private int mIntArrayListSize;

    private int position = 0;

    public static SWITCH_VOL subVol = SWITCH_VOL.SPIDF_MAIN;

    public static SWITCH_VOL mainVol = null;

    public static SWITCH_VOL lastVol = null;

    public static VideoWindowType dispWin = null;

    public static VideoWindowType mainWin = null;

    public static VideoWindowType subWin = null;

    public static PIP_MODE_ENABLED pipModeEnabled = PIP_MODE_ENABLED.NUM_EANBLED;

    protected String[] switch_vol_name = new SwitchVolItem().getSwitch_vol_name();

    public PipSubWindowSwitchVolDialog(Context context, int style, int[] intArrayList) {
        super(context, style);
        mActivity = context;

        tvPipPopManager = TvPipPopManager.getInstance();
        tvAudioManager = TvAudioManager.getInstance();
        mIntArrayList = intArrayList;

        Log.i(TAG, "mIntArrayList length is " + mIntArrayList.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("-----SwitchVol Dialog OnCreate--------1----------");
        setContentView(R.layout.subwindow_inputsource_list);
        dispWin = new VideoWindowType();
        mainWin = new VideoWindowType();
        subWin = new VideoWindowType();

        dispWin.x = 0;
        dispWin.y = 540;
        dispWin.width = 680;
        dispWin.height = 540;
        mainWin.x = subWin.x = 0;
        mainWin.y = subWin.y = 0;
        mainWin.width = subWin.width = 1920;
        mainWin.height = subWin.height = 1080;
        System.out.println("-------SwitchVol Dialog OnCreate--------2----------");
        Window w = getWindow();

        // Get the default display the data
        Display display = w.getWindowManager().getDefaultDisplay();
        // The window's title is empty
        w.setTitle(null);

        // Definition of window width and height
        Point point = new Point();
        display.getSize(point);
        int width = (int) (point.x * 0.2);//(display.getWidth() * 0.2);
        int height = (int) (point.y * 0.9);//(display.getHeight() * 0.9);

        // Settings window properties
        w.setLayout(width, height);

        w.setGravity(Gravity.CENTER_HORIZONTAL);

        // Settings window properties
        WindowManager.LayoutParams wl = w.getAttributes();
        wl.x = 100;
        w.setAttributes(wl);
        Init();
    }

    public void Init() {
        switchVolListView = (ListView) findViewById(R.id.ListView_subwindow_inputsource);
        for (int i = 0; i < mIntArrayList.length; i++) {
            setItemText(mIntArrayList[i]);
        }
        adapter = new SwitchVolAdapter(this, data);
        switchVolListView.setAdapter(adapter);
        switchVolListView.setDividerHeight(0);
        setListener();

    }

    private void setListener() {
        switchVolListView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        switchVolListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long arg3) {

                int vol = mIntArrayList[data.get(position).getPositon()];

                subVol = SWITCH_VOL.values()[vol];

                if (subVol != null) {

                    // no change
                    if (subVol == lastVol && Utility.getCurrentPipMode() == ChoosePipOrPopDialog.miscMode.ordinal()) {
                        Log.d(TAG, "PIP no change just return, don't switch volum");
                        return;
                    }

                    switch (ChoosePipOrPopDialog.miscMode) {

                        case PIP_MODE:
                            swithVol(subVol);
                            break;
                        case POP_MODE:
                            swithVol(subVol);
                            break;
                        case DUAL_MODE:
                            swithVol(subVol);
                            break;
                        default:
                            break;
                    }

                }
            }

        });

    }

    private void swithVol(SWITCH_VOL subVol) {
        switch (subVol) {
            case SPIDF_MAIN: {
                tvAudioManager.setAudioCaptureSource(
                        TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE0,
                        TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
                Tools.toastShow(R.string.switch_main, this.mActivity);
            }

                break;
            case SPIDF_SUB: {
                tvAudioManager.setAudioCaptureSource(
                        TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                        TvAudioManager.AUDIO_CAPTURE_SOURCE_SUB_SOUND);
                Tools.toastShow(R.string.switch_sub, this.mActivity);
            }

                break;
            case BT_HEADSET_MAIN: {
                tvAudioManager.setAudioCaptureSource(
                        TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                        TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
                Tools.toastShow(R.string.switch_main, this.mActivity);
            }

                break;
            case BT_HEADSET_SUB: {
                tvAudioManager.setAudioCaptureSource(
                        TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                        TvAudioManager.AUDIO_CAPTURE_SOURCE_SUB_SOUND);
                Tools.toastShow(R.string.switch_sub, this.mActivity);
            }

                break;

            default:
                break;

        }

    }

    private void setItemText(int i) {

        SwitchVolItem item = new SwitchVolItem();

        item.setVolName(switch_vol_name[i]);

        item.setPositon(position);

        data.add(item);

        position++;

    }

}
