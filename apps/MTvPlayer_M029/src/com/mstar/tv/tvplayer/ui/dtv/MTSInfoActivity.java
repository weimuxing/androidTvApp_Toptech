
package com.mstar.tv.tvplayer.ui.dtv;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.MKeyEvent;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.SwitchPageHelper;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tvframework.MstarBaseActivity;

public class MTSInfoActivity extends MstarBaseActivity {

    private final String TAG = "MTSInfoActivity";

    private TextView mtsInfo = null;

    private ArrayList<String> audioInfo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.mtsinfo);
        mtsInfo = (TextView) findViewById(R.id.mtsType);
        TVRootApp app = (TVRootApp) getApplication();
        mtsInfo.setText(getSoundFormat());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
            finish();
            return true;
        }
        switch (keyCode) {
            case MKeyEvent.KEYCODE_MTS:
                TvCommonManager.getInstance().setToNextATVMtsMode();
                mtsInfo.setText(getSoundFormat());
                break;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    private String getSoundFormat() {
       String str = "";
        switch (TvCommonManager.getInstance().getATVMtsMode()) {
            case TvCommonManager.ATV_AUDIOMODE_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_A:
                str = getResources().getString(R.string.str_sound_format_dual_a);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_AB:
                str = getResources().getString(R.string.str_sound_format_dual_ab);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_B:
                str = getResources().getString(R.string.str_sound_format_dual_b);
                break;
            case TvCommonManager.ATV_AUDIOMODE_FORCED_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_G_STEREO:
                str = getResources().getString(R.string.str_sound_format_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_HIDEV_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_K_STEREO:
                str = getResources().getString(R.string.str_sound_format_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_LEFT_LEFT:
                str = getResources().getString(R.string.str_sound_format_left_left);
                break;
            case TvCommonManager.ATV_AUDIOMODE_LEFT_RIGHT:
                str = getResources().getString(R.string.str_sound_format_left_right);
                break;
            case TvCommonManager.ATV_AUDIOMODE_MONO_SAP:
                str = getResources().getString(R.string.str_sound_format_mono_sap);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_A:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_a);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_AB:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_ab);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_B:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_b);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_MONO:
                str = getResources().getString(R.string.str_sound_format_nicam_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_STEREO:
                str = getResources().getString(R.string.str_sound_format_nicam_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_RIGHT_RIGHT:
                str = getResources().getString(R.string.str_sound_format_right_right);
                break;
            case TvCommonManager.ATV_AUDIOMODE_STEREO_SAP:
                str = getResources().getString(R.string.str_sound_format_stereo_sap);
                break;
            case TvCommonManager.ATV_AUDIOMODE_INVALID:
            default:
                str = getResources().getString(R.string.str_sound_format_unknown);
                break;
        }
        return str;
    }

    @Override
    public void onTimeOut() {
        super.onTimeOut();
        finish();
    }
}
