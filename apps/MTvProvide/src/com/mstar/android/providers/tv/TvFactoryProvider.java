//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.android.providers.tv;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class TvFactoryProvider extends ContentProvider {
    private static final String TAG = "TvFactoryProvider";

    private SQLiteDatabase factoryDB;

    private FactorySQLiteDatabase tvFactoryDB;

    private HandlerThread factoryThread = new HandlerThread("com.mstar.android.tv.factory.handler");

    private Handler factoryHandler = null;

    public static final String AUTHORITY = "mstar.tv.factory";

    private static final UriMatcher s_urlMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int dbRefreshCNT = 30;

    private static final int URL_ADCAdjust = 0;

    private static final int URL_ADCAdjust_ID = 1;

    private static final int URL_ATVOverscanSetting = 2;

    private static final int URL_ATVOverscanSetting_ID = 3;

    private static final int URL_DTVOverscanSetting = 5;

    private static final int URL_DTVOverscanSetting_ID = 6;

    private static final int URL_FactoryAudioSetting = 10;

    private static final int URL_FactoryColorTemp = 15;

    private static final int URL_FactoryColorTemp_ID = 16;

    private static final int URL_FactoryColorTempEx = 20;

    private static final int URL_FactoryColorTempEx_ID = 21;

    private static final int URL_FactoryExtern = 25;

    private static final int URL_Factory_DB_VERSION = 30;

    private static final int URL_HDMIOverscanSetting = 35;

    private static final int URL_HDMIOverscanSetting_ID = 36;

    private static final int URL_NonLinearAdjust = 40;

    private static final int URL_NonLinearAdjust_ID = 41;

    private static final int URL_NonStandardAdjust = 50;

    private static final int URL_OverscanAdjust = 55;

    private static final int URL_OverscanAdjust_ID = 56;

    private static final int URL_PEQAdjust = 60;

    private static final int URL_PEQAdjust_ID = 61;

    private static final int URL_SSCAdjust = 65;

    private static final int URL_CIAdjust = 66;

    private static final int URL_YPbPrOverscanSetting = 70;

    private static final int URL_YPbPrOverscanSetting_ID = 71;

    private static final int URL_FactoryInfo = 80;

    private static final int URL_ReopenDB = 81;

    private String AdcAdjust = "tbl_ADCAdjust";

    private String AtvOverscanSetting = "tbl_ATVOverscanSetting";

    private String DtvOverscanSetting = "tbl_DTVOverscanSetting";

    private String FactoryAudioSetting = "tbl_FactoryAudioSetting";

    private String FactoryColorTemp = "tbl_FactoryColorTemp";

    private String FactoryColorTempEx = "tbl_FactoryColorTempEx";

    private String FactoryExtern = "tbl_FactoryExtern";

    private String FactoryDBVersion = "tbl_Factory_DB_VERSION";

    private String HDMIOverScanSetting = "tbl_HDMIOverscanSetting";

    private String NonLinearAdjust = "tbl_NonLinearAdjust";

    private String NonStandardAdjust = "tbl_NonStandardAdjust";

    private String OverScanAdjust = "tbl_OverscanAdjust";

    private String PEQAdjust = "tbl_PEQAdjust";

    private String SSCAdjust = "tbl_SSCAdjust";

    private String CIAdjust = "tbl_CIAdjust";

    private String YPBPROverScanSetting = "tbl_YPbPrOverscanSetting";

    private String FactoryInfo = "tbl_FactoryInfo";

    static {
        s_urlMatcher.addURI(AUTHORITY, "adcadjust", URL_ADCAdjust);
        s_urlMatcher.addURI(AUTHORITY, "adcadjust/sourceid/#", URL_ADCAdjust_ID);

        s_urlMatcher.addURI(AUTHORITY, "atvoverscansetting", URL_ATVOverscanSetting);
        s_urlMatcher.addURI(AUTHORITY, "atvoverscansetting/resolutiontypenum/#/_id/#",
                URL_ATVOverscanSetting_ID);

        s_urlMatcher.addURI(AUTHORITY, "dtvoverscansetting", URL_DTVOverscanSetting);
        s_urlMatcher.addURI(AUTHORITY, "dtvoverscansetting/resolutiontypenum/#/_id/#",
                URL_DTVOverscanSetting_ID);

        s_urlMatcher.addURI(AUTHORITY, "factoryaudiosetting", URL_FactoryAudioSetting);

        s_urlMatcher.addURI(AUTHORITY, "factorycolortemp", URL_FactoryColorTemp);
        s_urlMatcher.addURI(AUTHORITY, "factorycolortemp/colortemperatureid/#",
                URL_FactoryColorTemp_ID);

        s_urlMatcher.addURI(AUTHORITY, "factorycolortempex", URL_FactoryColorTempEx);
        s_urlMatcher.addURI(AUTHORITY, "factorycolortempex/inputsourceid/#/colortemperatureid/#",
                URL_FactoryColorTempEx_ID);

        s_urlMatcher.addURI(AUTHORITY, "factoryextern", URL_FactoryExtern);

        s_urlMatcher.addURI(AUTHORITY, "factory_db_version", URL_Factory_DB_VERSION);

        s_urlMatcher.addURI(AUTHORITY, "hdmioverscansetting", URL_HDMIOverscanSetting);
        s_urlMatcher.addURI(AUTHORITY, "hdmioverscansetting/resolutiontypenum/#/_id/#",
                URL_HDMIOverscanSetting_ID);

        s_urlMatcher.addURI(AUTHORITY, "nonlinearadjust", URL_NonLinearAdjust);
        s_urlMatcher.addURI(AUTHORITY, "nonlinearadjust/inputsrctype/#/curvetypeindex/#",
                URL_NonLinearAdjust_ID);

        s_urlMatcher.addURI(AUTHORITY, "nonstandardadjust", URL_NonStandardAdjust);

        s_urlMatcher.addURI(AUTHORITY, "overscanadjust", URL_OverscanAdjust);
        s_urlMatcher.addURI(AUTHORITY, "overscanadjust/factoryoverscantype/#/_id/#",
                URL_OverscanAdjust_ID);

        s_urlMatcher.addURI(AUTHORITY, "peqadjust", URL_PEQAdjust);
        s_urlMatcher.addURI(AUTHORITY, "peqadjust/#", URL_PEQAdjust_ID);

        s_urlMatcher.addURI(AUTHORITY, "sscadjust", URL_SSCAdjust);

        s_urlMatcher.addURI(AUTHORITY, "ciadjust", URL_CIAdjust);

        s_urlMatcher.addURI(AUTHORITY, "ypbproverscansetting", URL_YPbPrOverscanSetting);
        s_urlMatcher.addURI(AUTHORITY, "ypbproverscansetting/resolutiontypenum/#/_id/#",
                URL_YPbPrOverscanSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "factoryinfo", URL_FactoryInfo);
        s_urlMatcher.addURI(AUTHORITY, "reopendatabase", URL_ReopenDB);
    }

    @Override
    public boolean onCreate() {
        openDB();
        factoryThread.start();
        factoryHandler = new Handler(factoryThread.getLooper());
        tvFactoryDB = new FactorySQLiteDatabase(factoryDB);
        return (factoryDB == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int match = s_urlMatcher.match(uri);
        switch (match) {
            case URL_ADCAdjust: {
                qb.setTables(AdcAdjust);
                break;
            }
            case URL_ADCAdjust_ID: {
                qb.setTables(AdcAdjust);
                qb.appendWhere("SourceID = " + uri.getLastPathSegment());
                break;
            }
            case URL_ATVOverscanSetting: {
                qb.setTables(AtvOverscanSetting);
                break;
            }
            case URL_ATVOverscanSetting_ID: {
                qb.setTables(AtvOverscanSetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment() + " and ResolutionTypeNum = "
                        + uri.getPathSegments().get(2));
                break;
            }
            case URL_DTVOverscanSetting: {
                qb.setTables(DtvOverscanSetting);
                break;
            }
            case URL_DTVOverscanSetting_ID: {
                qb.setTables(DtvOverscanSetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment() + " and ResolutionTypeNum = "
                        + uri.getPathSegments().get(2));
                break;
            }
            case URL_FactoryAudioSetting: {
                qb.setTables(FactoryAudioSetting);
                break;
            }
            case URL_FactoryColorTemp: {
                qb.setTables(FactoryColorTemp);
                break;
            }
            case URL_FactoryColorTemp_ID: {
                qb.setTables(FactoryColorTemp);
                qb.appendWhere("ColorTemperatureID = " + uri.getLastPathSegment());
                break;
            }
            case URL_FactoryColorTempEx: {
                qb.setTables(FactoryColorTempEx);
                break;
            }
            case URL_FactoryColorTempEx_ID: {
                qb.setTables(FactoryColorTempEx);
                qb.appendWhere("ColorTemperatureID = " + uri.getLastPathSegment()
                        + " and InputSourceID = " + uri.getPathSegments().get(2));
                break;
            }
            case URL_FactoryExtern: {
                qb.setTables(FactoryExtern);
                break;
            }
            case URL_Factory_DB_VERSION: {
                qb.setTables(FactoryDBVersion);
                break;
            }
            case URL_HDMIOverscanSetting: {
                qb.setTables(HDMIOverScanSetting);
                break;
            }
            case URL_HDMIOverscanSetting_ID: {
                qb.setTables(HDMIOverScanSetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment() + " and ResolutionTypeNum = "
                        + uri.getPathSegments().get(2));
                break;
            }
            case URL_NonLinearAdjust: {
                qb.setTables(NonLinearAdjust);
                break;
            }
            case URL_NonLinearAdjust_ID: {
                qb.setTables(NonLinearAdjust);
                qb.appendWhere("CurveTypeIndex = " + uri.getLastPathSegment()
                        + " and InputSrcType = " + uri.getPathSegments().get(2));
                break;
            }
            case URL_NonStandardAdjust: {
                qb.setTables(NonStandardAdjust);
                break;
            }
            case URL_OverscanAdjust: {
                qb.setTables(OverScanAdjust);
                break;
            }
            case URL_OverscanAdjust_ID: {
                qb.setTables(OverScanAdjust);
                qb.appendWhere("_id = " + uri.getLastPathSegment() + " and FactoryOverScanType = "
                        + uri.getPathSegments().get(2));
                break;
            }
            case URL_PEQAdjust: {
                qb.setTables(PEQAdjust);
                break;
            }
            case URL_PEQAdjust_ID: {
                qb.setTables(PEQAdjust);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            }
            case URL_SSCAdjust: {
                qb.setTables(SSCAdjust);
                break;
            }
            case URL_CIAdjust: {
                qb.setTables(CIAdjust);
                break;
            }
            case URL_YPbPrOverscanSetting: {
                qb.setTables(YPBPROverScanSetting);
                break;
            }
            case URL_YPbPrOverscanSetting_ID: {
                qb.setTables(YPBPROverScanSetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment() + " and ResolutionTypeNum = "
                        + uri.getPathSegments().get(2));
                break;
            }
            case URL_FactoryInfo: {
                qb.setTables(FactoryInfo);
                break;
            }
        }

        Cursor c = qb.query(factoryDB, projection, selection, null, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        return -1;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        int count = 0;
        int match = s_urlMatcher.match(uri);

        switch (match) {
            case URL_ADCAdjust: {
                count = tvFactoryDB.update(uri, AdcAdjust, values, where, whereArgs);
                break;
            }
            case URL_ADCAdjust_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, AdcAdjust, values, "SourceID=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            }
            case URL_ATVOverscanSetting: {
                count = tvFactoryDB.update(uri, AtvOverscanSetting, values, where, whereArgs);
                break;
            }
            case URL_ATVOverscanSetting_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, AtvOverscanSetting, values,
                        "ResolutionTypeNum=? and _id=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            }
            case URL_DTVOverscanSetting: {
                count = tvFactoryDB.update(uri, DtvOverscanSetting, values, where, whereArgs);
                break;
            }
            case URL_DTVOverscanSetting_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, DtvOverscanSetting, values,
                        "ResolutionTypeNum=? and _id=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            }
            case URL_FactoryAudioSetting: {
                count = tvFactoryDB.update(uri, FactoryAudioSetting, values, where, whereArgs);
                break;
            }
            case URL_FactoryColorTemp: {
                count = tvFactoryDB.update(uri, FactoryColorTemp, values, where, whereArgs);
                break;
            }
            case URL_FactoryColorTemp_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, FactoryColorTemp, values, "ColorTemperatureID=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            }
            case URL_FactoryColorTempEx: {
                count = tvFactoryDB.update(uri, FactoryColorTempEx, values, where, whereArgs);
                break;
            }
            case URL_FactoryColorTempEx_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, FactoryColorTempEx, values,
                        "InputSourceID=? and ColorTemperatureID=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            }
            case URL_FactoryExtern: {
                count = tvFactoryDB.update(uri, FactoryExtern, values, where, whereArgs);
                break;
            }
            case URL_Factory_DB_VERSION: {
                count = tvFactoryDB.update(uri, FactoryDBVersion, values, where, whereArgs);
                break;
            }
            case URL_HDMIOverscanSetting: {
                count = tvFactoryDB.update(uri, HDMIOverScanSetting, values, where, whereArgs);
                break;
            }
            case URL_HDMIOverscanSetting_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, HDMIOverScanSetting, values,
                        "ResolutionTypeNum=? and _id=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            }
            case URL_NonLinearAdjust: {
                count = tvFactoryDB.update(uri, NonLinearAdjust, values, where, whereArgs);
                break;
            }
            case URL_NonLinearAdjust_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, NonLinearAdjust, values,
                        "InputSrcType=? and CurveTypeIndex=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            }
            case URL_NonStandardAdjust: {
                count = tvFactoryDB.update(uri, NonStandardAdjust, values, where, whereArgs);
                break;
            }
            case URL_OverscanAdjust: {
                count = tvFactoryDB.update(uri, OverScanAdjust, values, where, whereArgs);
                break;
            }
            case URL_OverscanAdjust_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, OverScanAdjust, values,
                        "FactoryOverScanType=? and _id=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            }
            case URL_PEQAdjust: {
                count = tvFactoryDB.update(uri, PEQAdjust, values, where, whereArgs);
                break;
            }
            case URL_PEQAdjust_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, PEQAdjust, values, "_id=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            }
            case URL_SSCAdjust: {
                count = tvFactoryDB.update(uri, SSCAdjust, values, where, whereArgs);
                break;
            }
            case URL_CIAdjust: {
                count = tvFactoryDB.update(uri, CIAdjust, values, where, whereArgs);
                break;
            }
            case URL_YPbPrOverscanSetting: {
                count = tvFactoryDB.update(uri, YPBPROverScanSetting, values, where, whereArgs);
                break;
            }
            case URL_YPbPrOverscanSetting_ID: {
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvFactoryDB.update(uri, YPBPROverScanSetting, values,
                        "ResolutionTypeNum=? and _id=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            }
            case URL_FactoryInfo: {
                count = tvFactoryDB.update(uri, FactoryInfo, values, where, whereArgs);
                break;
            }
            case URL_ReopenDB: {
                reopen();
                break;
            }

        }

        return count;
    }

    private void reopen() {
        closeDB();
        openDB();
        tvFactoryDB = null;
        tvFactoryDB = new FactorySQLiteDatabase(factoryDB);
    }

    private void openDB() {
        int retry = 0;
        if ((factoryDB == null) || (!factoryDB.isOpen())) {
            Log.e(TAG, "open db start");
            while (retry < 20) {
                try {
                    factoryDB = SQLiteDatabase.openDatabase("/tvdatabase/Database/factory.db",
                            null, SQLiteDatabase.OPEN_READWRITE
                                    | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
                } catch (SQLiteException e) {
                    Log.e(TAG, "open db fail,retry ...");
                    retry++;
                    factoryDB = null;
                    if (retry >= 20) {
                        Log.e(TAG, "open db fail,Please check sw bug!");
                    }
                } finally {
                    if (factoryDB != null) {
                        Log.e(TAG, "open db success");
                        retry = 20;
                    }
                }
            }
        }
    }

    private void closeDB() {
        factoryDB.close();
    }

    @Override
    public void shutdown() {
        Log.e(TAG, "now shutdown");
        super.shutdown();
        closeDB();
    }

    private class FactorySQLiteDatabase {
        private SQLiteDatabase factoryDB = null;

        long ret = -1;

        FactorySQLiteDatabase(SQLiteDatabase db) {
            factoryDB = db;
        }

        public int update(final Uri uri, final String table, final ContentValues values,
                final String whereClause, final String[] whereArgs) {
            try {
                ret = factoryDB.update(table, values, whereClause, whereArgs);
            } catch (SQLException e) {
                ret = -1;
            }
            if (ret < 0) {
                FactroySQLiteThread userSQLiteThread = new FactroySQLiteThread(factoryDB, uri,
                        table, values, whereClause, whereArgs);
                userSQLiteThread.start();
            } else {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return 1;
        }
    }

    private class FactroySQLiteThread extends Thread {
        long ret = -1;

        int redetect = 0;

        SQLiteDatabase factoryDBDB = null;

        Uri factoryDBURI = null;

        String factoryDBTable = null;

        ContentValues factoryDBValues = null;

        String factoryDBWhereClause = null;

        String[] factoryDBWhereArgs = null;

        public FactroySQLiteThread(SQLiteDatabase db, Uri uri, String table, ContentValues values,
                String whereClause, String[] whereArgs) {
            factoryDBDB = db;
            factoryDBURI = uri;
            factoryDBTable = table;
            factoryDBValues = values;
            factoryDBWhereClause = whereClause;
            factoryDBWhereArgs = whereArgs;
        }

        public void run() {

            while (ret < 0 && redetect < dbRefreshCNT) {
                try {
                    Thread.sleep(50);
                    ret = factoryDBDB.update(factoryDBTable, factoryDBValues, factoryDBWhereClause,
                            factoryDBWhereArgs);
                    redetect++;
                } catch (InterruptedException e) {
                } catch (SQLException e) {
                    ret = -1;
                }
            }

            if (ret < 0) {
                Log.e(TAG, "update factoryDB db fail !!!");
            } else {
                getContext().getContentResolver().notifyChange(factoryDBURI, null);
            }
        }
    }
}
