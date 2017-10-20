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

package com.mstar.tv.tvplayer.ui.dtv.ewbs;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.tv.tvplayer.ui.R;

import java.util.HashMap;
import java.util.Map;

public class AreaNameList {

    public static final int NO_RESOURCE_ID = 0;

    private final Map<Integer, Integer> mDataMap = new HashMap<Integer, Integer>();

    public AreaNameList() {
        if (TvCountry.PHILIPPINES == TvChannelManager.getInstance().getSystemCountryId()) {
            loadPhData();
        }
    }

    private void loadPhData() {
        mDataMap.put(0x0000, R.string.str_ewbs_ph_root_menu);
        mDataMap.put(0x0100, R.string.str_ewbs_ph_nw_luzon);
        mDataMap.put(0x0101, R.string.str_ewbs_ph_nw_visayas);
        mDataMap.put(0x0102, R.string.str_ewbs_ph_nw_mindanao);
        mDataMap.put(0x0200, R.string.str_ewbs_ph_region_ncr);
        mDataMap.put(0x0201, R.string.str_ewbs_ph_region_car);
        mDataMap.put(0x0202, R.string.str_ewbs_ph_region_region_1);
        mDataMap.put(0x0203, R.string.str_ewbs_ph_region_region_2);
        mDataMap.put(0x0204, R.string.str_ewbs_ph_region_region_3);
        mDataMap.put(0x0205, R.string.str_ewbs_ph_region_region_4a);
        mDataMap.put(0x0206, R.string.str_ewbs_ph_region_region_4b);
        mDataMap.put(0x0207, R.string.str_ewbs_ph_region_region_5);
        mDataMap.put(0x0220, R.string.str_ewbs_ph_region_region_6);
        mDataMap.put(0x0221, R.string.str_ewbs_ph_region_region_7);
        mDataMap.put(0x0222, R.string.str_ewbs_ph_region_region_8);
        mDataMap.put(0x0240, R.string.str_ewbs_ph_region_region_9);
        mDataMap.put(0x0241, R.string.str_ewbs_ph_region_region_10);
        mDataMap.put(0x0242, R.string.str_ewbs_ph_region_region_11);
        mDataMap.put(0x0243, R.string.str_ewbs_ph_region_region_12);
        mDataMap.put(0x0244, R.string.str_ewbs_ph_region_region_13);
        mDataMap.put(0x0245, R.string.str_ewbs_ph_region_armm);
        mDataMap.put(0x0300, R.string.str_ewbs_ph_city_caloocan);
        mDataMap.put(0x0301, R.string.str_ewbs_ph_city_laspinas);
        mDataMap.put(0x0302, R.string.str_ewbs_ph_city_makati);
        mDataMap.put(0x0303, R.string.str_ewbs_ph_city_malabon);
        mDataMap.put(0x0304, R.string.str_ewbs_ph_city_mandaluyong);
        mDataMap.put(0x0305, R.string.str_ewbs_ph_city_manila);
        mDataMap.put(0x0306, R.string.str_ewbs_ph_city_marikina);
        mDataMap.put(0x0307, R.string.str_ewbs_ph_city_muntinlupa);
        mDataMap.put(0x0308, R.string.str_ewbs_ph_city_navotas);
        mDataMap.put(0x0309, R.string.str_ewbs_ph_city_paranaque);
        mDataMap.put(0x030A, R.string.str_ewbs_ph_city_pasay);
        mDataMap.put(0x030B, R.string.str_ewbs_ph_city_pasig);
        mDataMap.put(0x030C, R.string.str_ewbs_ph_city_pateros);
        mDataMap.put(0x030D, R.string.str_ewbs_ph_city_quezoncity);
        mDataMap.put(0x030E, R.string.str_ewbs_ph_city_sanjuan);
        mDataMap.put(0x030F, R.string.str_ewbs_ph_city_taguig);
        mDataMap.put(0x0310, R.string.str_ewbs_ph_city_valenzuela);
        mDataMap.put(0x0330, R.string.str_ewbs_ph_city_abra);
        mDataMap.put(0x0331, R.string.str_ewbs_ph_city_apayao);
        mDataMap.put(0x0332, R.string.str_ewbs_ph_city_baguio);
        mDataMap.put(0x0333, R.string.str_ewbs_ph_city_benguet);
        mDataMap.put(0x0334, R.string.str_ewbs_ph_city_ifugao);
        mDataMap.put(0x0335, R.string.str_ewbs_ph_city_kalinga);
        mDataMap.put(0X0336, R.string.str_ewbs_ph_city_mtprovince);
        mDataMap.put(0x0350, R.string.str_ewbs_ph_city_ilocosnorte);
        mDataMap.put(0x0351, R.string.str_ewbs_ph_city_ilocossur);
        mDataMap.put(0x0352, R.string.str_ewbs_ph_city_launion);
        mDataMap.put(0x0353, R.string.str_ewbs_ph_city_pagasinan);
        mDataMap.put(0x0370, R.string.str_ewbs_ph_city_batanes);
        mDataMap.put(0x0371, R.string.str_ewbs_ph_city_cagayan);
        mDataMap.put(0x0372, R.string.str_ewbs_ph_city_isabela);
        mDataMap.put(0x0373, R.string.str_ewbs_ph_city_nuevavizcaya);
        mDataMap.put(0x0374, R.string.str_ewbs_ph_city_quirino);
        mDataMap.put(0x0375, R.string.str_ewbs_ph_city_santiago);
        mDataMap.put(0x0390, R.string.str_ewbs_ph_city_angeles);
        mDataMap.put(0x0391, R.string.str_ewbs_ph_city_aurora);
        mDataMap.put(0x0392, R.string.str_ewbs_ph_city_bataan);
        mDataMap.put(0x0393, R.string.str_ewbs_ph_city_bulacan);
        mDataMap.put(0x0394, R.string.str_ewbs_ph_city_nuevaecija);
        mDataMap.put(0x0395, R.string.str_ewbs_ph_city_olongapo);
        mDataMap.put(0x0396, R.string.str_ewbs_ph_city_pampanga);
        mDataMap.put(0x0397, R.string.str_ewbs_ph_city_tarlac);
        mDataMap.put(0x0398, R.string.str_ewbs_ph_city_zambales);
        mDataMap.put(0x03B0, R.string.str_ewbs_ph_city_batangas);
        mDataMap.put(0x03B1, R.string.str_ewbs_ph_city_cavite);
        mDataMap.put(0x03B2, R.string.str_ewbs_ph_city_laguna);
        mDataMap.put(0x03B3, R.string.str_ewbs_ph_city_lucena);
        mDataMap.put(0x03B4, R.string.str_ewbs_ph_city_quezon);
        mDataMap.put(0x03B5, R.string.str_ewbs_ph_city_rizal);
        mDataMap.put(0x03D0, R.string.str_ewbs_ph_city_marinduque);
        mDataMap.put(0x03D1, R.string.str_ewbs_ph_city_occidentalmindoro);
        mDataMap.put(0x03D2, R.string.str_ewbs_ph_city_orientalmindoro);
        mDataMap.put(0x03D3, R.string.str_ewbs_ph_city_palawan);
        mDataMap.put(0x03D4, R.string.str_ewbs_ph_city_puertoprincesa);
        mDataMap.put(0x03D5, R.string.str_ewbs_ph_city_romblon);
        mDataMap.put(0x03F0, R.string.str_ewbs_ph_city_albay);
        mDataMap.put(0x03F1, R.string.str_ewbs_ph_city_camarinessur);
        mDataMap.put(0x03F2, R.string.str_ewbs_ph_city_camarinesnorte);
        mDataMap.put(0x03F3, R.string.str_ewbs_ph_city_catanduanes);
        mDataMap.put(0x03F4, R.string.str_ewbs_ph_city_masbata);
        mDataMap.put(0x03F5, R.string.str_ewbs_ph_city_naga);
        mDataMap.put(0x03F6, R.string.str_ewbs_ph_city_sorsogon);
        mDataMap.put(0x0410, R.string.str_ewbs_ph_city_aklan);
        mDataMap.put(0x0411, R.string.str_ewbs_ph_city_antique);
        mDataMap.put(0x0412, R.string.str_ewbs_ph_city_bacolod);
        mDataMap.put(0x0413, R.string.str_ewbs_ph_city_capiz);
        mDataMap.put(0x0414, R.string.str_ewbs_ph_city_guimaras);
        mDataMap.put(0x0415, R.string.str_ewbs_ph_city_iloilo);
        mDataMap.put(0x0416, R.string.str_ewbs_ph_city_iloilocity);
        mDataMap.put(0x0417, R.string.str_ewbs_ph_city_negrosoccidental);
        mDataMap.put(0x0430, R.string.str_ewbs_ph_city_bohol);
        mDataMap.put(0x0431, R.string.str_ewbs_ph_city_cebu);
        mDataMap.put(0x0432, R.string.str_ewbs_ph_city_cebucity);
        mDataMap.put(0x0433, R.string.str_ewbs_ph_city_lapulapu);
        mDataMap.put(0x0434, R.string.str_ewbs_ph_city_negrosoriental);
        mDataMap.put(0x0435, R.string.str_ewbs_ph_city_siquijor);
        mDataMap.put(0x0450, R.string.str_ewbs_ph_city_biliran);
        mDataMap.put(0x0451, R.string.str_ewbs_ph_city_easternsamar);
        mDataMap.put(0x0452, R.string.str_ewbs_ph_city_leyte);
        mDataMap.put(0x0453, R.string.str_ewbs_ph_city_northernsamar);
        mDataMap.put(0x0454, R.string.str_ewbs_ph_city_ormoc);
        mDataMap.put(0x0455, R.string.str_ewbs_ph_city_samar);
        mDataMap.put(0x0456, R.string.str_ewbs_ph_city_southernleyte);
        mDataMap.put(0x0457, R.string.str_ewbs_ph_city_tacloban);
        mDataMap.put(0x0470, R.string.str_ewbs_ph_city_isabelacity);
        mDataMap.put(0x0471, R.string.str_ewbs_ph_city_zamboangacity);
        mDataMap.put(0x0472, R.string.str_ewbs_ph_city_zamboangadelnorte);
        mDataMap.put(0x0473, R.string.str_ewbs_ph_city_zamboangadelsur);
        mDataMap.put(0x0474, R.string.str_ewbs_ph_city_zamboangasibugay);
        mDataMap.put(0x0490, R.string.str_ewbs_ph_city_bukidnon);
        mDataMap.put(0x0491, R.string.str_ewbs_ph_city_cagayandeoro);
        mDataMap.put(0x0492, R.string.str_ewbs_ph_city_camiguin);
        mDataMap.put(0x0493, R.string.str_ewbs_ph_city_iligan);
        mDataMap.put(0x0494, R.string.str_ewbs_ph_city_lanaodelnorte);
        mDataMap.put(0x0495, R.string.str_ewbs_ph_city_misamisoccidental);
        mDataMap.put(0x0496, R.string.str_ewbs_ph_city_misamisoriental);
        mDataMap.put(0x04B0, R.string.str_ewbs_ph_city_compostelavalley);
        mDataMap.put(0x04B1, R.string.str_ewbs_ph_city_davaocity);
        mDataMap.put(0x04B2, R.string.str_ewbs_ph_city_davaodelnorte);
        mDataMap.put(0x04B3, R.string.str_ewbs_ph_city_davaodelsur);
        mDataMap.put(0x04B4, R.string.str_ewbs_ph_city_davaooriental);
        mDataMap.put(0x04D0, R.string.str_ewbs_ph_city_cotabato);
        mDataMap.put(0x04D1, R.string.str_ewbs_ph_city_cotabatocity);
        mDataMap.put(0x04D2, R.string.str_ewbs_ph_city_generalsantos);
        mDataMap.put(0x04D3, R.string.str_ewbs_ph_city_saranggani);
        mDataMap.put(0x04D4, R.string.str_ewbs_ph_city_southcotabato);
        mDataMap.put(0x04D5, R.string.str_ewbs_ph_city_sultankudarat);
        mDataMap.put(0x04F0, R.string.str_ewbs_ph_city_agusandelnorte);
        mDataMap.put(0x04F1, R.string.str_ewbs_ph_city_agusandelsur);
        mDataMap.put(0x04F2, R.string.str_ewbs_ph_city_butuan);
        mDataMap.put(0x04F3, R.string.str_ewbs_ph_city_dinagatislands);
        mDataMap.put(0x04F4, R.string.str_ewbs_ph_city_surigaodelnorte);
        mDataMap.put(0x04F5, R.string.str_ewbs_ph_city_surigaodelsur);
        mDataMap.put(0x0510, R.string.str_ewbs_ph_city_basilan);
        mDataMap.put(0x0511, R.string.str_ewbs_ph_city_lanaodelsur);
        mDataMap.put(0x0512, R.string.str_ewbs_ph_city_maguindanao);
        mDataMap.put(0x0513, R.string.str_ewbs_ph_city_sulu);
        mDataMap.put(0x0514, R.string.str_ewbs_ph_city_tawitawi);
    }

    public int getNameResId(int cityCode) {
        int ret = NO_RESOURCE_ID;
        if (true == mDataMap.containsKey(cityCode)) {
            ret = mDataMap.get(cityCode);
        }
        return ret;
    }
}
