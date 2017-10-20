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

package com.mstar.tv.ui.sidepanel.fragment.misc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.dialog.SliderDialogFragment;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;

import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.common.vo.DtvProgramSignalInfo;
import com.mstar.android.tvapi.common.vo.DtvProgramSignalInfo.EnumProgramDemodType;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.OadCustomerInfo;
import com.mstar.android.tvapi.common.exception.TvOutOfBoundException;
import com.mstar.android.tvapi.dtv.vo.RfInfo;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvOadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows version and optional license information.
 */
public class SignalInfoFragment extends SideFragment {
    private final static String TAG = "SignalInfoFragment";

    private final static int DTV_REFRESH_UI = 0x01;

    private final static int REFRESH_UI_DELAY_TIME = 1000;

    private int mTvSystem = -1;

    private TvChannelManager mTvChannelManager = null;

    private DtvProgramSignalInfo mCurrSignalInfo = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private TvIsdbChannelManager mTvIsdbChannelManager = null;

    private String[] mCabQamTable;

    private String[] mDvbtQamTable;

    private SignalStrengthItem mSignalStrength;

    private SignalQualityItem mSignalQuality;

    private int mStrength;

    private int mQuality;

    private Handler mHandler = new Handler();

    private Runnable mUpdateSignalRunnable;

    private Handler mMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG, "mMessageHandler, msg.what = " + msg.what);
            switch (msg.what) {
                case DTV_REFRESH_UI:

                    mCurrSignalInfo = mTvChannelManager.getCurrentSignalInformation();
                    if (null != mCurrSignalInfo) {
                        mQuality = mCurrSignalInfo.quality;
                        mStrength = mCurrSignalInfo.strength;
                    }
                    Log.d(TAG, "setProgressBarValue(), quality = " + mQuality + ", strengthVal = "
                            + mStrength);

                    mMessageHandler.sendEmptyMessageDelayed(DTV_REFRESH_UI, REFRESH_UI_DELAY_TIME);

                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mTvChannelManager = TvChannelManager.getInstance();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mTvIsdbChannelManager = TvIsdbChannelManager.getInstance();
        }
        mCurrSignalInfo = mTvChannelManager.getCurrentSignalInformation();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMessageHandler.sendEmptyMessage(DTV_REFRESH_UI);
        if (mUpdateSignalRunnable != null) {
            mHandler.removeCallbacks(mUpdateSignalRunnable);
            mHandler.postDelayed(mUpdateSignalRunnable, 1000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mMessageHandler.removeMessages(DTV_REFRESH_UI);
        mHandler.removeCallbacks(mUpdateSignalRunnable);
    }

    /**
     * Shows the Software Version setting fragment.
     */
    public class SoftwareVersionItem extends ActionItem {
        public final String DIALOG_TAG = SoftwareVersionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSoftwareVersion;

        public SoftwareVersionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_software_version));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mSoftwareVersion = TvOadManager.getInstance()
                            .getOadVersion(TvOadManager.OAD_VERSION_TYPE_TV);
                    setDescription(Integer.toString(mSoftwareVersion));
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Antenna Type setting fragment.
     */
    public class AntennaTypeItem extends ActionItem {
        public final String DIALOG_TAG = AntennaTypeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mAntennaType;

        public AntennaTypeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_antannatype));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mTvAtscChannelManager
                            .getDtvAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                        setDescription(getResources().getString(R.string.str_misc_antannatype_air));
                    } else {
                        setDescription(
                                getResources().getString(R.string.str_misc_antannatype_cable));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Channel setting fragment.
     */
    public class ChannelItem extends ActionItem {
        public final String DIALOG_TAG = ChannelItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mChannel;

        public ChannelItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_channel));
            mMainActivity = mainActivity;

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mChannel = mCurrSignalInfo.rfNumber;
                    setDescription(Integer.toString(mChannel));

                    RfInfo rfInfo = mTvChannelManager.getRfInfo(TvChannelManager.RF_INFO,
                            mCurrSignalInfo.rfNumber);

                    if (true != isISDB()) {
                        int dvbtRouteIndex = TvChannelManager.TV_ROUTE_NONE;
                        int dvbt2RouteIndex = TvChannelManager.TV_ROUTE_NONE;
                        int dtmbRouteIndex = TvChannelManager.TV_ROUTE_NONE;

                        /*
                         * Only DTMB and DVBT shows RF Name instead rfPhyNum
                         * (e.g 24-1 / K30)
                         */
                        dvbtRouteIndex = mTvChannelManager
                                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT);
                        dvbt2RouteIndex = mTvChannelManager
                                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT2);
                        dtmbRouteIndex = mTvChannelManager
                                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);
                        if ((mTvChannelManager.getCurrentDtvRouteIndex() == dvbtRouteIndex)
                                || (mTvChannelManager.getCurrentDtvRouteIndex() == dvbt2RouteIndex)
                                || (mTvChannelManager
                                        .getCurrentDtvRouteIndex() == dtmbRouteIndex)) {
                            mChannel = mCurrSignalInfo.rfNumber;
                            setDescription(Integer.toString(mChannel));
                        }
                    }
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Frequency setting fragment.
     */
    public class FrequencyItem extends ActionItem {
        public final String DIALOG_TAG = FrequencyItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mFrequency;

        public FrequencyItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_frequency));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    RfInfo rfInfo = mTvChannelManager.getRfInfo(TvChannelManager.RF_INFO,
                            mCurrSignalInfo.rfNumber);

                    if (true != isISDB()) {
                        int dvbsRouteIndex = TvChannelManager.TV_ROUTE_NONE;
                        int dvbs2RouteIndex = TvChannelManager.TV_ROUTE_NONE;
                        int dvbtRouteIndex = TvChannelManager.TV_ROUTE_NONE;
                        int dvbt2RouteIndex = TvChannelManager.TV_ROUTE_NONE;
                        int dtmbRouteIndex = TvChannelManager.TV_ROUTE_NONE;

                        dvbsRouteIndex = mTvChannelManager
                                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS);
                        dvbs2RouteIndex = mTvChannelManager
                                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS2);
                        if ((mTvChannelManager.getCurrentDtvRouteIndex() == dvbsRouteIndex)
                                || (mTvChannelManager
                                        .getCurrentDtvRouteIndex() == dvbs2RouteIndex)) {
                            /* There is no channel info in DVBS/DVBS2 system */

                            ProgramInfo cpi = mTvChannelManager.getCurrentProgramInfo();
                            mFrequency = cpi.frequency;
                            setDescription(Integer.toString(mFrequency));

                            return;
                        }
                    }

                    mFrequency = rfInfo.frequency / 1000;
                    setDescription(Integer.toString(mFrequency));
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Network setting fragment.
     */
    public class NetworkItem extends ActionItem {
        public final String DIALOG_TAG = NetworkItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private String mNetwork;

        public NetworkItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_network));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setDescription(mCurrSignalInfo.networkName);
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Transport stream setting fragment.
     */
    public class TransportStreamItem extends ActionItem {
        public final String DIALOG_TAG = TransportStreamItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mTransportStream;

        public TransportStreamItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_transportstream));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ProgramInfo cpi = mTvChannelManager.getCurrentProgramInfo();
                    mTransportStream = cpi.transportStreamId;
                    setDescription(Integer.toString(mTransportStream));
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Service setting fragment.
     */
    public class ServiceItem extends ActionItem {
        public final String DIALOG_TAG = ServiceItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mService;

        public ServiceItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_service));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ProgramInfo cpi;
                    cpi = mTvChannelManager.getCurrentProgramInfo();
                    mService = cpi.serviceId;
                    setDescription(Integer.toString(mService));
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Signal Strength setting fragment.
     */
    public class SignalStrengthItem extends ActionItem {
        public final String DIALOG_TAG = SignalStrengthItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSignalStrength;

        public SignalStrengthItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_signalstrength));
            mMainActivity = mainActivity;
            mUpdateSignalRunnable = new Runnable() {
                @Override
                public void run() {
                    mSignalStrength = mStrength;
                    setDescription(Integer.toString(mSignalStrength));
                    mHandler.postDelayed(this, 1000);
                }
            };
            mHandler.postDelayed(mUpdateSignalRunnable, 1000);
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Signal Quality setting fragment.
     */
    public class SignalQualityItem extends ActionItem {
        public final String DIALOG_TAG = SignalQualityItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSignalQuality;

        public SignalQualityItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_signalquality));
            mMainActivity = mainActivity;
            mUpdateSignalRunnable = new Runnable() {
                @Override
                public void run() {
                    mSignalQuality = mQuality;
                    setDescription(Integer.toString(mSignalQuality));
                    mHandler.postDelayed(this, 1000);
                }
            };
            mHandler.postDelayed(mUpdateSignalRunnable, 1000);
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Modulation setting fragment.
     */
    public class ModulationItem extends ActionItem {
        public final String DIALOG_TAG = ModulationItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private String mModulation;

        public ModulationItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_modulation));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    mCabQamTable = getResources()
                            .getStringArray(R.array.str_arr_signalinfo_qam_cab);
                    mDvbtQamTable = getResources()
                            .getStringArray(R.array.str_arr_signalinfo_qam_dvbt);

                    EnumProgramDemodType mode;
                    try {
                        mode = mCurrSignalInfo.getModulationMode();
                        if (mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_T
                                || mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_T2) {
                            if (mCurrSignalInfo.amMode < mDvbtQamTable.length) {
                                mModulation = mDvbtQamTable[mCurrSignalInfo.amMode];
                                setDescription(mModulation);
                            }
                        } else
                            if (mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_C) {
                            if (mCurrSignalInfo.amMode < mCabQamTable.length) {
                                mModulation = mCabQamTable[mCurrSignalInfo.amMode];
                                setDescription(mModulation);
                            }
                        } else if (mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_S) {
                            setDescription(Integer.toString(R.string.str_misc_dvbs));
                        } else {
                            mModulation = "";
                            setDescription(mModulation);
                        }
                    } catch (TvOutOfBoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    /**
     * Shows the Symbol Rate setting fragment.
     */
    public class SymbolRateItem extends ActionItem {
        public final String DIALOG_TAG = SymbolRateItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSymbolRate;

        public SymbolRateItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_misc_symbol));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mTvAtscChannelManager
                            .getDtvAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                        setDescription(getResources().getString(R.string.str_misc_antannatype_air));
                    } else {
                        setDescription(
                                getResources().getString(R.string.str_misc_antannatype_cable));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
        }
    }

    private int getCurrentTvSystem() {
        if (mTvSystem < 0) {
            mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        }
        return mTvSystem;
    }

    private boolean isISDB() {
        return TvCommonManager.TV_SYSTEM_ISDB == getCurrentTvSystem();
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_misc_signalinfo);
    }

    @Override
    protected List<Item> getItemList() {

        mSignalStrength = new SignalStrengthItem((MainActivity) getActivity());

        mSignalQuality = new SignalQualityItem((MainActivity) getActivity());

        List<Item> items = new ArrayList<>();

        int currentSystem = getCurrentTvSystem();
        if ((TvCommonManager.TV_SYSTEM_ATSC != currentSystem)) {
            items.add(new SoftwareVersionItem((MainActivity) getActivity()));
        }

        if ((TvCommonManager.TV_SYSTEM_ATSC == currentSystem)) {
            items.add(new AntennaTypeItem((MainActivity) getActivity()));
        }

        int dvbsRouteIndex = TvChannelManager.getInstance()
                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS);
        int dvbs2RouteIndex = TvChannelManager.getInstance()
                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS2);

        if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() != dvbsRouteIndex)
                && (TvChannelManager.getInstance().getCurrentDtvRouteIndex() != dvbs2RouteIndex)) {
            /* There is no channel info in DVBS/DVBS2 system */
            items.add(new ChannelItem((MainActivity) getActivity()));
        }

        if ((TvCommonManager.TV_SYSTEM_ATSC != currentSystem)) {
            items.add(new FrequencyItem((MainActivity) getActivity()));
            items.add(new NetworkItem((MainActivity) getActivity()));

            if ((TvCommonManager.TV_SYSTEM_ISDB != currentSystem)) {
                items.add(new TransportStreamItem((MainActivity) getActivity()));
                items.add(new ServiceItem((MainActivity) getActivity()));
            }
        }

        items.add(mSignalStrength);
        items.add(mSignalQuality);

        EnumProgramDemodType mode;
        try {
            mode = mCurrSignalInfo.getModulationMode();
            if ((mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_T)
                    || (mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_T2)
                    || (mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_C)
                    || (mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_S)) {
                items.add(new ModulationItem((MainActivity) getActivity()));
            }

            if (mode == DtvProgramSignalInfo.EnumProgramDemodType.E_PROGRAM_DEMOD_DVB_C) {
                items.add(new SymbolRateItem((MainActivity) getActivity()));
            }
        } catch (TvOutOfBoundException e) {
            e.printStackTrace();
        }

        return items;
    }
}
