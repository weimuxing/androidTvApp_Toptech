package mstar.tvsetting.factory.ui.pqsetting;

import android.util.Log;

import com.mstar.android.tv.TvFactoryManager;

import mstar.tvsetting.factory.until.Constant;

/**
 * Created by wymzz on 2017/7/10 0010.
 */

public final class PqViewHolder {
    private static String TAG = "PqViewHolder";
    private String ipName;
    private String[] tableNames;
    private int tableCount;
    private int currentIndex;
    public boolean bValid = true;
    public boolean bLoaded = false;

    // 0, big table
    // 1, little table
    private int pqType;
    private int pqIpIndex;

    private TvFactoryManager mTvFactoryManager = null;

    PqViewHolder(int type, int ipIdx) {
        pqType = type;
        pqIpIndex = ipIdx;
        bValid = true;
        bLoaded = false;
        Log.d(TAG, "new ............... PqViewHolder");
    }

    void init() {
        mTvFactoryManager = TvFactoryManager.getInstance();
        if (pqType == Constant.PQ_TABLE_TYPE_BIG) { // big table
            ipName = mTvFactoryManager.getQmapIpName(pqIpIndex);
            tableCount = mTvFactoryManager.getQmapTableNum(pqIpIndex);
            tableNames = new String[tableCount];
            for (int i = 0; i < tableCount; i++) {
                tableNames[i] = mTvFactoryManager.getQmapTableName(pqIpIndex, i);
            }
            currentIndex = mTvFactoryManager.getQmapCurrentTableIdx(pqIpIndex);
            bLoaded = true;
        } else { // little table
            ipName = mTvFactoryManager.getQmapCustomerIpName(pqIpIndex);
            tableCount = mTvFactoryManager.getQmapCustomerTableNum(pqIpIndex);
            tableNames = new String[tableCount];
            for (int i = 0; i < tableCount; i++) {
                tableNames[i] = mTvFactoryManager.getQmapCustomerTableName(pqIpIndex, i);
            }
            currentIndex = mTvFactoryManager.getQmapCustomerCurrentTableIdx(pqIpIndex);
            bLoaded = true;
        }
        Log.d(TAG, "pqType is " + pqType);
        Log.d(TAG, "pqIpIndex is " + pqIpIndex);
        Log.d(TAG, "ipName is " + ipName);
        Log.d(TAG, "tableCount is " + tableCount);
        Log.d(TAG, "currentIndex is " + currentIndex);

        check_and_reset();
    }

    void check_and_reset() {
        if (tableNames == null || tableNames.length <= 0) {
            tableNames = new String[1];
            tableNames[0] = "dummy";
            bValid = false;
        }

        if (tableNames.length < tableCount)
            tableCount = tableNames.length;
        if (currentIndex >= tableCount)
            currentIndex = 0;

        if (ipName == null || ipName.isEmpty())
            ipName = "dummy";
    }

    String getIpName() {
        return ipName;
    }

    int getIpIndex() {
        return pqIpIndex;
    }

    void setPqIpIndex (int ip) {
        pqIpIndex = ip;
    }

    int getCurrentIndex() {
        return currentIndex;
    }

    int getTableCount() {
        return tableCount;
    }

    String getCurrentTableName() {
        if (currentIndex >= tableCount)
            currentIndex = 0;

        return tableNames[currentIndex];
    }

    String getTableName(int idx) {
        if (idx >= tableCount)
            currentIndex = 0;

        return tableNames[idx];
    }

    String[] getTableNames() {
        return tableNames;
    }
}
