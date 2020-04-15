package com.yr.unique.code.utils.imp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.lang.reflect.Method;

/**
 * Vivo手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:29
 */
public class VivoDeviceIDHelper {

    private Context mConetxt;

    public VivoDeviceIDHelper(Context ctx) {
        mConetxt = ctx;
    }

    private HandlerThread handlerThread;
    private Handler handler;
    private boolean isSupportIds = false;
    String oaid = null;

    public String getOaid() {
        String res = null;
        Uri uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID");
        Cursor cursor = mConetxt.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                res = cursor.getString(cursor.getColumnIndex("value"));
            }
            cursor.close();
        } else {
        }
        return res;
    }

    public String loge() {
        String result = null;

        f();
        isSupportIds();

        if (!isSupportIds) {
            return null;
        }
        if (oaid != null) {
            return null;
        }
        timeCheck(0, null);

        return null;
    }

    private void timeCheck(int i, String str) {
        Message message = handler.obtainMessage();
        message.what = 11;
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        if (i == 1 || i == 2) {
            bundle.putString("appid", str);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private String sysProperty(String v1, String v2) {
        String res = null;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", new Class[]{String.class, String.class});
            res = (String) method.invoke(clazz, new Object[]{v1, "unknown"});
        } catch (Exception e) {
            e.printStackTrace();
            return v2;
        }
        return res;
    }

    private boolean isSupportIds() {
        String isSupId = sysProperty("persist.sys.identifierid.supported", "0");
        isSupportIds = isSupId.equals("1");
        return isSupportIds;
    }

    private void f() {
        handlerThread = new HandlerThread("SqlWorkThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 11) {
                    int tag = msg.getData().getInt("type");
                    String name = msg.getData().getString("appid");
                    String id = getContentResolver(tag, name);
                } else {

                }
            }
        };
    }

    private String getContentResolver(int tag, String name) {
        String result = null;
        Uri uri;
        switch (tag) {
            case 0:
                uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID");
                break;
            case 1:
                uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/VAID_" + name);
                break;
            case 2:
                uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/AAID_" + name);
                break;
            default:
                uri = null;
                break;
        }
        Cursor cursor = mConetxt.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex("value"));
            }
            cursor.close();
        } else {

        }
        return result;
    }
}
