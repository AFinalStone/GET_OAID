package com.yr.unique.code.utils.imp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

import com.yr.unique.code.utils.AppIdsUpdater;

/**
 * 魅族手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:25
 */
public class MeizuDeviceIDHelper {

    private Context mContext;

    public MeizuDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }


    private boolean isMeizuSupport() {
        try {
            PackageManager pm = mContext.getPackageManager();
            if (pm != null) {
                ProviderInfo pi = pm.resolveContentProvider("com.meizu.flyme.openidsdk", 0);        // "com.meizu.flyme.openidsdk"
                if (pi != null) {
                    return true;
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return false;
    }

    public void getMeizuID(AppIdsUpdater _listener) {
        try {
            mContext.getPackageManager().getPackageInfo("com.meizu.flyme.openidsdk", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse("content://com.meizu.flyme.openidsdk/");

        Cursor cursor;
        ContentResolver contentResolver = mContext.getContentResolver();
        try {
            cursor = contentResolver.query(uri, null, null, new String[]{"oaid"}, null);
            String oaid = getOaid(cursor);

            if (_listener != null) {
                _listener.OnIdsAvalid(oaid);
            }
            cursor.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 获取 OAID
     *
     * @param cursor
     * @return
     */
    private String getOaid(Cursor cursor) {
        String oaid = null;
        if (cursor == null) {
            return null;
        }
        if (cursor.isClosed()) {
            return null;
        }
        cursor.moveToFirst();
        int valueIdx = cursor.getColumnIndex("value");
        if (valueIdx > 0) {
            oaid = cursor.getString(valueIdx);
        }
        valueIdx = cursor.getColumnIndex("code");
        if (valueIdx > 0) {
            int codeID = cursor.getInt(valueIdx);
        }
        valueIdx = cursor.getColumnIndex("expired");
        if (valueIdx > 0) {
            long timeC = cursor.getLong(valueIdx);
        }
        return oaid;
    }
}
