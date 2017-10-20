
package mstar.tvsetting.factory.ui.factorymenu;

import mstar.factorymenu.ui.R;
import android.widget.LinearLayout;

public class FactoryMenuViewHolder {
    private FactoryMenuActivity mstarActivity;

    protected LinearLayout adcAdjustLinerLayout;

    protected LinearLayout wbLinerLayout;

    protected LinearLayout overScanLinerLayout;

    protected LinearLayout otherLinerLayout;

    protected LinearLayout infoLinerLayout;

    public FactoryMenuViewHolder(FactoryMenuActivity activity) {
        this.mstarActivity = activity;
    }

    void findView() {
        adcAdjustLinerLayout = (LinearLayout) mstarActivity
                .findViewById(R.id.linearlayout_factory_adc);
        wbLinerLayout = (LinearLayout) mstarActivity
                .findViewById(R.id.linearlayout_factory_whitebalance);
        overScanLinerLayout = (LinearLayout) mstarActivity
                .findViewById(R.id.linearlayout_factory_overscan);
        otherLinerLayout = (LinearLayout) mstarActivity
                .findViewById(R.id.linearlayout_factory_otheroption);
        infoLinerLayout = (LinearLayout) mstarActivity.findViewById(R.id.linearlayout_factory_info);
    }
}
