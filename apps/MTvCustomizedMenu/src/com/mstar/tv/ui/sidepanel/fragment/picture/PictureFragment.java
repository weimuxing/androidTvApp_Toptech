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

package com.mstar.tv.ui.sidepanel.fragment.picture;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.mstar.android.tv.TvPictureManager;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.dialog.SliderDialogFragment;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.android.tvapi.common.vo.ColorTemperatureExData;
import com.mstar.android.tvapi.common.vo.HdrAttribute;
import android.database.Cursor;
import android.net.Uri;
import com.mstar.android.tv.TvCommonManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows version and optional license information.
 */
public class PictureFragment extends SideFragment {
    private final static String TAG = "PictureFragment";

    private static Handler mHandler = new Handler();

    // Picture mode related items
    private PictureModeItem mPictureModeItem;

    private ContrastItem mContrastItem;

    private BrightnessItem mBrightnessItem;

    private SharpnessItem mSharpnessItem;

    private HueItem mHueItem;

    private SaturationItem mSaturationItem;

    private BackLightItem mBackLightItem;

    // Color temperature related items
    private ColorTemperatureItem mColorTemperatureItem;

    private ColorRedItem mColorRedItem;

    private ColorGreenItem mColorGreenItem;

    private ColorBlueItem mColorBlueItem;

    private OpenHdrItem mOpenHdrItem;

    private DolbyHdrItem mDolbyHdrItem;

    // String arrays
    private String mStrPictureModes[];

    private String mStrColorTemperature[];

    private String mStrZoomModes[];

    private String mStrOpenHdr[];

    private String mStrDolbyHdr[];

    private String mStrImageNoiseReduction[];

    private String mStrMpegNoiseReduction[];

    private static final int XVYCC_NORMAL_MAIN = 0;

    private static final int XVYCC_ON_XVYCC_MAIN = 1;

    private static final int XVYCC_ON_SRGB_MAIN = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrPictureModes = getActivity().getResources()
                .getStringArray(R.array.str_arr_pic_picturemode_vals);
        mStrColorTemperature = getActivity().getResources()
                .getStringArray(R.array.str_arr_pic_color_temperature_vals);
        mStrZoomModes = getActivity().getResources()
                .getStringArray(R.array.str_arr_pic_zoom_mode_vals);
        mStrOpenHdr = getActivity().getResources()
                .getStringArray(R.array.str_arr_pic_open_hdr_vals);
        mStrDolbyHdr = getActivity().getResources()
                .getStringArray(R.array.str_arr_pic_dolby_hdr_vals);
        mStrImageNoiseReduction = getActivity().getResources()
                .getStringArray(R.array.str_arr_pic_image_noise_reduction_vals);
        mStrMpegNoiseReduction = getActivity().getResources()
                .getStringArray(R.array.str_arr_pic_mpeg_noise_reduction_vals);
    }

    /**
     * Shows the picture mode setting fragment.
     */
    public class PictureModeItem extends ActionItem {
        public final String DIALOG_TAG = PictureModeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mPictureMode;

        public PictureModeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.picture_mode));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mPictureMode = TvPictureManager.getInstance().getPictureMode();
                        setDescription(mStrPictureModes[mPictureMode]);
                        if (mPictureMode == TvPictureManager.PICTURE_MODE_USER) {
                            setFocusableForUserMode(true);
                        } else {
                            setFocusableForUserMode(false);
                        }
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new PictureModeFragment(mPictureMode));
        }
    }

    /**
     * Shows the contrast setting fragment.
     */
    public static class ContrastItem extends ActionItem {
        public final static String DIALOG_TAG = ContrastItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mContrast;

        public ContrastItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.contrast));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mContrast = TvPictureManager.getInstance()
                                .getVideoItem(TvPictureManager.PICTURE_CONTRAST);
                        setDescription(Integer.toString(mContrast));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.contrast), 0, 100, mContrast,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                TvPictureManager.getInstance()
                                        .setVideoItem(TvPictureManager.PICTURE_CONTRAST, value);
                                mContrast = value;
                                setDescription(Integer.toString(mContrast));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the brightness setting fragment.
     */
    public static class BrightnessItem extends ActionItem {
        public final static String DIALOG_TAG = BrightnessItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mBrightness;

        public BrightnessItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.brightness));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mBrightness = TvPictureManager.getInstance()
                                .getVideoItem(TvPictureManager.PICTURE_BRIGHTNESS);
                        setDescription(Integer.toString(mBrightness));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.brightness), 0, 100, mBrightness,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                TvPictureManager.getInstance()
                                        .setVideoItem(TvPictureManager.PICTURE_BRIGHTNESS, value);
                                mBrightness = value;
                                setDescription(Integer.toString(mBrightness));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the hue setting fragment.
     */
    public static class HueItem extends ActionItem {
        public final static String DIALOG_TAG = HueItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mHue;

        public HueItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.hue));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mHue = TvPictureManager.getInstance()
                                .getVideoItem(TvPictureManager.PICTURE_HUE);
                        setDescription(Integer.toString(mHue));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.hue), 0, 100, mHue,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                TvPictureManager.getInstance()
                                        .setVideoItem(TvPictureManager.PICTURE_HUE, value);
                                mHue = value;
                                setDescription(Integer.toString(mHue));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the sharpness setting fragment.
     */
    public static class SharpnessItem extends ActionItem {
        public final static String DIALOG_TAG = SharpnessItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSharpness;

        public SharpnessItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.sharpness));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mSharpness = TvPictureManager.getInstance()
                                .getVideoItem(TvPictureManager.PICTURE_SHARPNESS);
                        setDescription(Integer.toString(mSharpness));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.sharpness), 0, 100, mSharpness,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                TvPictureManager.getInstance()
                                        .setVideoItem(TvPictureManager.PICTURE_SHARPNESS, value);
                                mSharpness = value;
                                setDescription(Integer.toString(mSharpness));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the saturation setting fragment.
     */
    public static class SaturationItem extends ActionItem {
        public final static String DIALOG_TAG = SaturationItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSaturation;

        public SaturationItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.saturation));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mSaturation = TvPictureManager.getInstance()
                                .getVideoItem(TvPictureManager.PICTURE_SATURATION);
                        setDescription(Integer.toString(mSaturation));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.saturation), 0, 100, mSaturation,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                TvPictureManager.getInstance()
                                        .setVideoItem(TvPictureManager.PICTURE_SATURATION, value);
                                mSaturation = value;
                                setDescription(Integer.toString(mSaturation));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the backlight setting fragment.
     */
    public static class BackLightItem extends ActionItem {
        public final static String DIALOG_TAG = BackLightItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mBackLight;

        public BackLightItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.backlight));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mBackLight = TvPictureManager.getInstance()
                                .getBacklight();
                        setDescription(Integer.toString(mBackLight));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.backlight), 0, 100, mBackLight,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                TvPictureManager.getInstance()
                                        .setBacklight(value);
                                mBackLight = value;
                                setDescription(Integer.toString(mBackLight));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the PC Image mode setting fragment.
     */
    public static class PCImageModeActionItem extends ActionItem {
        public final static String DIALOG_TAG = PCImageModeActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public PCImageModeActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.PC_image_mode));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new PCImageModeFragment());
        }
    }

    /**
     * Shows the color temperature setting fragment.
     */
    public class ColorTemperatureItem extends ActionItem {
        public final String DIALOG_TAG = ColorTemperatureItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mColorTemperature;

        public ColorTemperatureItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.color_temperature));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        ColorTemperatureExData colorTemperatureExData = TvPictureManager
                                .getInstance().getColorTempratureEx();

                        mColorTemperature = getColorTemperatureSetting();
                        setDescription(mStrColorTemperature[mColorTemperature]);
                        if (mColorTemperature == TvPictureManager.COLOR_TEMP_USER1) {
                            setFocusableForColorTmpUserMode(true);
                        } else {
                            setFocusableForColorTmpUserMode(false);
                        }
                    }
                }
            });

        }

        private short getColorTemperatureSetting() {
            short colorTempIdx = 0;

            if (TvPictureManager.getInstance() != null) {
                int inputSrcType = TvCommonManager.getInstance().getCurrentTvInputSource();
                int pictureMode = TvPictureManager.getInstance().getPictureMode();
                Cursor cursor = mMainActivity.getApplicationContext().getContentResolver()
                        .query(Uri.parse("content://mstar.tv.usersetting/picmode_setting/inputsrc/"
                                + inputSrcType + "/picmode/" + pictureMode), null, null, null,
                        "PictureModeType");
                if (cursor.moveToFirst()) {
                    colorTempIdx = (short) cursor.getInt(cursor.getColumnIndex("eColorTemp"));
                }
                cursor.close();
            }

            return colorTempIdx;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager()
                    .show(new ColorTemperatureFragment(mColorTemperature));
        }
    }

    /**
     * Shows the color red setting fragment.
     */
    public static class ColorRedItem extends ActionItem {
        public final static String DIALOG_TAG = ColorRedItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mColoRed;

        public ColorRedItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.colorred));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        ColorTemperatureExData colorTemperatureExData = TvPictureManager
                                .getInstance().getColorTempratureEx();

                        mColoRed = colorTemperatureExData.redGain;
                        setDescription(Integer.toString(mColoRed));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.colorred), 0, 2048, mColoRed,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                ColorTemperatureExData colorTemperatureExData = TvPictureManager
                                        .getInstance().getColorTempratureEx();
                                colorTemperatureExData.redGain = value;
                                TvPictureManager.getInstance()
                                        .setColorTempratureEx(colorTemperatureExData);

                                mColoRed = value;
                                setDescription(Integer.toString(mColoRed));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the color green setting fragment.
     */
    public static class ColorGreenItem extends ActionItem {
        public final static String DIALOG_TAG = ColorGreenItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mColorGreen;

        public ColorGreenItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.colorgreen));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        ColorTemperatureExData colorTemperatureExData = TvPictureManager
                                .getInstance().getColorTempratureEx();

                        mColorGreen = colorTemperatureExData.greenGain;
                        setDescription(Integer.toString(mColorGreen));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.colorgreen), 0, 2048, mColorGreen,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                ColorTemperatureExData colorTemperatureExData = TvPictureManager
                                        .getInstance().getColorTempratureEx();
                                colorTemperatureExData.greenGain = value;
                                TvPictureManager.getInstance()
                                        .setColorTempratureEx(colorTemperatureExData);

                                mColorGreen = value;
                                setDescription(Integer.toString(mColorGreen));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the color blue setting fragment.
     */
    public static class ColorBlueItem extends ActionItem {
        public final static String DIALOG_TAG = ColorBlueItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mColorBlue;

        public ColorBlueItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.colorblue));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        ColorTemperatureExData colorTemperatureExData = TvPictureManager
                                .getInstance().getColorTempratureEx();

                        mColorBlue = colorTemperatureExData.blueGain;
                        setDescription(Integer.toString(mColorBlue));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.colorblue), 0, 2048, mColorBlue,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvPictureManager.getInstance() != null) {
                                ColorTemperatureExData colorTemperatureExData = TvPictureManager
                                        .getInstance().getColorTempratureEx();
                                colorTemperatureExData.blueGain = value;
                                TvPictureManager.getInstance()
                                        .setColorTempratureEx(colorTemperatureExData);

                                mColorBlue = value;
                                setDescription(Integer.toString(mColorBlue));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the zoom mode setting fragment.
     */
    public class ZoomModeItem extends ActionItem {
        public final String DIALOG_TAG = ZoomModeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mZoomMode;

        public ZoomModeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.zoom_mode));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mZoomMode = TvPictureManager.getInstance().getVideoArcType();
                        setDescription(mStrZoomModes[mZoomMode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new ZoomModeFragment(mZoomMode));
        }
    }

    /**
     * Shows the open hdr setting fragment.
     */
    public class OpenHdrItem extends ActionItem {
        public final String DIALOG_TAG = OpenHdrItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int level = TvPictureManager.HDR_OPEN_LEVEL_OFF;

        public OpenHdrItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.open_hdr));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        HdrAttribute openHdrAtt = TvPictureManager.getInstance().getHdrAttributes(TvPictureManager.HDR_OPEN_ATTRIBUTES
                            , TvPictureManager.VIDEO_MAIN_WINDOW);
                        if (null != openHdrAtt) {
                            if ((TvPictureManager.HDR_ERROR_CODE_SUCCESS == openHdrAtt.result)
                                || (TvPictureManager.HDR_ERROR_CODE_STILL_WORK == openHdrAtt.result)) {
                                level = openHdrAtt.level;
                            }
                        }
                        setDescription(mStrOpenHdr[level]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager()
                    .show(new OpenHdrFragment(level));
        }
    }

    /**
     * Shows the dolby hdr setting fragment.
     */
    public class DolbyHdrItem extends ActionItem {
        public final String DIALOG_TAG = DolbyHdrItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int level = TvPictureManager.HDR_DOLBY_LEVEL_OFF;

        public DolbyHdrItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.dolby_hdr));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        HdrAttribute dolbyHdrAtt = TvPictureManager.getInstance().getHdrAttributes(TvPictureManager.HDR_DOLBY_ATTRIBUTES
                            , TvPictureManager.VIDEO_MAIN_WINDOW);
                        if (null != dolbyHdrAtt) {
                            if ((TvPictureManager.HDR_ERROR_CODE_SUCCESS == dolbyHdrAtt.result)
                                || (TvPictureManager.HDR_ERROR_CODE_STILL_WORK == dolbyHdrAtt.result)) {
                                level = dolbyHdrAtt.level;
                            }
                        }
                        setDescription(mStrDolbyHdr[level]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager()
                    .show(new DolbyHdrFragment(level));
        }
    }

    /**
     * Shows the image noise reduction setting fragment.
     */
    public class ImageNoiseReductionItem extends ActionItem {
        public final String DIALOG_TAG = ImageNoiseReductionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mImageNoiseReduction;

        public ImageNoiseReductionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.image_noise_reduction));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mImageNoiseReduction = TvPictureManager.getInstance().getNoiseReduction();
                        setDescription(mStrImageNoiseReduction[mImageNoiseReduction]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager()
                    .show(new ImageNoiseReductionFragment(mImageNoiseReduction));
        }
    }
    /**
     * Shows the mpeg noise reduction setting fragment.
     */
    public class MpegNoiseReductionItem extends ActionItem {
        public final String DIALOG_TAG = MpegNoiseReductionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mMpegNoiseReduction;

        public MpegNoiseReductionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.mpeg_noise_reduction));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        mMpegNoiseReduction = TvPictureManager.getInstance()
                                .getMpegNoiseReduction();
                        setDescription(mStrMpegNoiseReduction[mMpegNoiseReduction]);
                    }
                }
            });

        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager()
                    .show(new MpegNoiseReductionFragment(mMpegNoiseReduction));
        }
    }

    /**
     * Shows the xvYCC setting fragment.
     */
    public static class XvYCCItem extends SwitchItem {
        public final static String DIALOG_TAG = XvYCCItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public XvYCCItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.xvycc));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() == null) {
                        return;
                    }
                    // Check if xvYCC is enabled and then set the ui
                    if (TvPictureManager.getInstance().getxvYCCEnable()) {
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
            if (TvPictureManager.getInstance() == null) {
                return;
            }
            // Enable/Disable xvYCC
            if (isChecked()) {
                TvPictureManager.getInstance().setxvYCCEnable(true, XVYCC_NORMAL_MAIN);
            } else {
                TvPictureManager.getInstance().setxvYCCEnable(false, XVYCC_ON_SRGB_MAIN);
            }
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_menu_picture);
    }

    @Override
    protected List<Item> getItemList() {
        mContrastItem = new ContrastItem((MainActivity) getActivity());
        mBrightnessItem = new BrightnessItem((MainActivity) getActivity());
        mHueItem = new HueItem((MainActivity) getActivity());
        mSharpnessItem = new SharpnessItem((MainActivity) getActivity());
        mSaturationItem = new SaturationItem((MainActivity) getActivity());
        mBackLightItem = new BackLightItem((MainActivity) getActivity());
        mPictureModeItem = new PictureModeItem((MainActivity) getActivity());
        mColorRedItem = new ColorRedItem((MainActivity) getActivity());
        mColorGreenItem = new ColorGreenItem((MainActivity) getActivity());
        mColorBlueItem = new ColorBlueItem((MainActivity) getActivity());
        mColorTemperatureItem = new ColorTemperatureItem((MainActivity) getActivity());
        List<Item> items = new ArrayList<>();
        items.add(mPictureModeItem);
        items.add(mContrastItem);
        items.add(mBrightnessItem);
        items.add(mHueItem);
        items.add(mSharpnessItem);
        items.add(mSaturationItem);
        items.add(mBackLightItem);

        int inputSrc = TvCommonManager.getInstance().getCurrentTvInputSource();
        if ((inputSrc == TvCommonManager.INPUT_SOURCE_VGA)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA2)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA3)) {
            if ((TvCommonManager.getInstance().isSignalStable(inputSrc))) {
                items.add(new PCImageModeActionItem((MainActivity) getActivity()));
            }
        }

        items.add(mColorTemperatureItem);
        items.add(mColorRedItem);
        items.add(mColorGreenItem);
        items.add(mColorBlueItem);
        items.add(new ZoomModeItem((MainActivity) getActivity()));
        if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OPEN_HDR)) {
            mOpenHdrItem = new OpenHdrItem((MainActivity) getActivity());
            mOpenHdrItem.setEnabled(false);
            items.add(mOpenHdrItem);
            HdrAttribute openHdrAtt = TvPictureManager.getInstance().getHdrAttributes(TvPictureManager.HDR_OPEN_ATTRIBUTES
                , TvPictureManager.VIDEO_MAIN_WINDOW);
            if (null != openHdrAtt) {
                if ((TvPictureManager.HDR_ERROR_CODE_SUCCESS == openHdrAtt.result)
                    || (TvPictureManager.HDR_ERROR_CODE_STILL_WORK == openHdrAtt.result)) {
                    mOpenHdrItem.setEnabled(true);
                }
            }
        }
        if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_DOLBY_HDR)) {
            mDolbyHdrItem = new DolbyHdrItem((MainActivity) getActivity());
            mDolbyHdrItem.setEnabled(false);
            items.add(mDolbyHdrItem);
            HdrAttribute dolbyHdrAtt = TvPictureManager.getInstance().getHdrAttributes(TvPictureManager.HDR_DOLBY_ATTRIBUTES
                , TvPictureManager.VIDEO_MAIN_WINDOW);
            if (null != dolbyHdrAtt) {
                if ((TvPictureManager.HDR_ERROR_CODE_SUCCESS == dolbyHdrAtt.result)
                    || (TvPictureManager.HDR_ERROR_CODE_STILL_WORK == dolbyHdrAtt.result)) {
                    mDolbyHdrItem.setEnabled(true);
                }
            }
        }
        items.add(new ImageNoiseReductionItem((MainActivity) getActivity()));
        items.add(new MpegNoiseReductionItem((MainActivity) getActivity()));
        items.add(new XvYCCItem((MainActivity) getActivity()));

        return items;
    }

    private void setFocusableForUserMode(boolean isUserMode) {
        if (isUserMode) {
            mContrastItem.setEnabled(true);
            mBrightnessItem.setEnabled(true);
            mHueItem.setEnabled(true);
            mSharpnessItem.setEnabled(true);
            mSaturationItem.setEnabled(true);
            mBackLightItem.setEnabled(true);
        } else {
            mContrastItem.setEnabled(false);
            mBrightnessItem.setEnabled(false);
            mHueItem.setEnabled(false);
            mSharpnessItem.setEnabled(false);
            mSaturationItem.setEnabled(false);
            mBackLightItem.setEnabled(false);
        }
    }

    private void setFocusableForColorTmpUserMode(boolean isUserMode) {
        if (isUserMode) {
            mColorRedItem.setEnabled(true);
            mColorGreenItem.setEnabled(true);
            mColorBlueItem.setEnabled(true);
        } else {
            mColorRedItem.setEnabled(false);
            mColorGreenItem.setEnabled(false);
            mColorBlueItem.setEnabled(false);
        }
    }
}
