package com.mstar.miscsetting.dialog;

import java.lang.reflect.Method;
import com.mstar.android.MDisplay;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.Parcel;

public class PictureSkin {
    private IBinder surfaceFlinger = null;
    private boolean connected = false;
    private static PictureSkin mPictureSkin = null;

    private PictureSkin() {
        surfaceFlinger = null;
        connected = false;
    }

    public static PictureSkin getInstance() {
        if (mPictureSkin != null) {
            return mPictureSkin;
        } else {
            return new PictureSkin();
        }
    }

    public void Connect() {
        if (connected == false) {

            Class<?> servicManager = null;
            Method method = null;
            try{
                servicManager = Class.forName("android.os.ServiceManager");
                method = servicManager.getMethod("getService", String.class);
                surfaceFlinger = (IBinder)method.invoke(null, "SurfaceFlinger");
            }catch(Exception e){
                e.printStackTrace();
            }

            if (surfaceFlinger != null) {
                connected = true;
            } else {
                connected = false;
            }
        }

    }

    public void setSurfaceResolutionMode(int width, int height, int hstart, int interleave, int orientation, long value) {
        MDisplay.setSurfaceResolutionMode(width, height, hstart, interleave, orientation, value);
    }

}
