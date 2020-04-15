package com.yr.unique.code.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.yr.unique.code.utils.imp.ASUSDeviceIDHelper;
import com.yr.unique.code.utils.imp.HWDeviceIDHelper;
import com.yr.unique.code.utils.imp.LenovoDeviceIDHelper;
import com.yr.unique.code.utils.imp.MeizuDeviceIDHelper;
import com.yr.unique.code.utils.imp.NubiaDeviceIDHelper;
import com.yr.unique.code.utils.imp.OnePlusDeviceIDHelper;
import com.yr.unique.code.utils.imp.OppoDeviceIDHelper;
import com.yr.unique.code.utils.imp.SamsungDeviceIDHelper;
import com.yr.unique.code.utils.imp.VivoDeviceIDHelper;
import com.yr.unique.code.utils.imp.XiaomiDeviceIDHelper;
import com.yr.unique.code.utils.imp.ZTEDeviceIDHelper;

import java.lang.reflect.Method;

/**
 * 把各大厂商获取OADI的工具类统一封装成一个类
 *
 * @author AF
 * @time 2020/4/14 17:11
 */
public class OAIDHelper {

    private AppIdsUpdater mAppIdUpdateListener;

    public OAIDHelper(AppIdsUpdater callback) {
        mAppIdUpdateListener = callback;
    }


    /**
     * 获取各大平台的OAID
     *
     * @param context
     */
    public void getOAid(Context context) {

        String oaid = null;
        String manufacturer = getManufacturer().toUpperCase();
        Log.d("DevicesIDsHelper", "manufacturer===> " + manufacturer);
        if (mAppIdUpdateListener == null) {
            return;
        }

        if (isFreeMeOS() || isSSUIOS()) {
            getOAIDByNewThread(context);
        }
        DeviceTypeEnum deviceType = DeviceTypeEnum.getInstance(manufacturer);
        switch (deviceType) {
            case HuaShuo:
            case HuaWei:
            case Oppo:
            case OnePlus:
            case ZTE:
                getOAIDByNewThread(context);
                break;
            case Lianxiang:
            case Motolora:
                new LenovoDeviceIDHelper(context).getIdRun(mAppIdUpdateListener);
                break;
            case Nubia:
                oaid = new NubiaDeviceIDHelper(context).getNubiaID();
                mAppIdUpdateListener.OnIdsAvalid(oaid);
                break;
            case Meizu:
                new MeizuDeviceIDHelper(context).getMeizuID(mAppIdUpdateListener);
                break;
            case Samsung:
                new SamsungDeviceIDHelper(context).getSumsungID(mAppIdUpdateListener);
                break;
            case Vivo:
                oaid = new VivoDeviceIDHelper(context).getOaid();
                mAppIdUpdateListener.OnIdsAvalid(oaid);
                break;
            case XiaoMi:
            case BlackShark:
                oaid = new XiaomiDeviceIDHelper(context).getOAID();
                mAppIdUpdateListener.OnIdsAvalid(oaid);
                break;
        }

    }


    /**
     * 这些平台获取OAID是耗时操作，需要放在异步线程中
     *
     * @param context
     */
    private void getOAIDByNewThread(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isFreeMeOS() || isSSUIOS()) {
                    new ZTEDeviceIDHelper(context).getID(mAppIdUpdateListener);
                    return;
                }
                String manufacturer = getManufacturer().toUpperCase();
                DeviceTypeEnum deviceType = DeviceTypeEnum.getInstance(manufacturer);
                switch (deviceType) {
                    case HuaShuo:
                        new ASUSDeviceIDHelper(context).getID(mAppIdUpdateListener);
                        break;
                    case HuaWei:
                        new HWDeviceIDHelper(context).getHWID(mAppIdUpdateListener);
                        break;
                    case Oppo:
                        new OppoDeviceIDHelper(context).getID(mAppIdUpdateListener);
                        break;
                    case OnePlus:
                        new OnePlusDeviceIDHelper(context).getID(mAppIdUpdateListener);
                        break;
                    case ZTE:
                        new ZTEDeviceIDHelper(context).getID(mAppIdUpdateListener);
                        break;
                }
            }
        }).start();
    }


    /**
     * 获取当前设备硬件制造商（MANUFACTURER）
     *
     * @return
     */
    private String getManufacturer() {
        return Build.MANUFACTURER.toUpperCase();
    }

    private String getProperty(String property) {
        String res = null;
        if (property == null) {
            return null;
        }
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", new Class[]{String.class, String.class});
            res = (String) method.invoke(clazz, new Object[]{property, "unknown"});
        } catch (Exception e) {
            // ignore
        }
        return res;
    }


    /**
     * 是否是freeMe操作系统，该系统是卓易科技深度定制的Android操作系统
     *
     * @return
     */
    public boolean isFreeMeOS() {
        String pro = getProperty("ro.build.freeme.label");      // "ro.build.freeme.label"
        if ((!TextUtils.isEmpty(pro)) && pro.equalsIgnoreCase("FREEMEOS")) {      // "FreemeOS"  FREEMEOS
            return true;
        }
        return false;
    }


    public boolean isSSUIOS() {
        String pro = getProperty("ro.ssui.product");    // "ro.ssui.product"
        if ((!TextUtils.isEmpty(pro)) && (!pro.equalsIgnoreCase("unknown"))) {
            return true;
        }
        return false;
    }


}
