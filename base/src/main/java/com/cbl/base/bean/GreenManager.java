package com.cbl.base.bean;

import android.content.Context;
import android.os.IBinder;

import com.eebbk.ovumserver.IGreenManager;

import java.lang.reflect.Method;

public class GreenManager {
    private static IGreenManager mGreenManager;
    private static Context mContext;

    public static void initGreenManager(Context context) {
        mContext = context;
    }

    public static IGreenManager getIGreenManager() {
        try {
            //加载servicemanager的字节码
            Class clazz = mContext.getApplicationContext().getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder ibinder = (IBinder) method.invoke(null, "ovum_green");
            mGreenManager = IGreenManager.Stub.asInterface(ibinder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mGreenManager;
    }
}
