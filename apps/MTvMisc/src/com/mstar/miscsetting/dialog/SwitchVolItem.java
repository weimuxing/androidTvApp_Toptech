package com.mstar.miscsetting.dialog;

public class SwitchVolItem {

    private String volName = null;
    private boolean signalFlag = true;
    private boolean selectFlag = false;
    private int typeFlag = 0; // 0 means not tv signal ,1means tv signal,other
    // number obligate for later.
    private int positon = 0;

    protected String [] switch_vol_name = new String [] {"Speak/Main","Speak/Sub","BT HeadSet/Main","BT HeadSet/Sub"};

    public String getVolName() {
        return volName;
    }

    public void setVolName(String volName) {
        this.volName = volName;
    }

    public boolean isSignalFlag() {
        return signalFlag;
    }

    public void setSignalFlag(boolean signalFlag) {
        this.signalFlag = signalFlag;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    public int getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(int typeFlag) {
        this.typeFlag = typeFlag;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    public String[] getSwitch_vol_name() {
        return switch_vol_name;
    }

    public void setSwitch_vol_name(String[] switch_vol_name) {
        this.switch_vol_name = switch_vol_name;
    }




}
