package com.yr.unique.code.utils.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.yr.unique.code.utils.AppIdsUpdater;
import com.yr.unique.code.utils.interfaces.ZTEIDInterface;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * 中兴手机获取 OAid
 *
 * @author AF
 * @time 2020/4/14 18:29
 */
public class ZTEDeviceIDHelper {
    Context mContext;
    String idPkgName = "com.mdid.msa";

    public ZTEDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    private int checkService() {
        int s = 0;
        try {
            mContext.getPackageManager().getPackageInfo(idPkgName, 0);
            s = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private void startMsaklServer(String pkgName) {
        if (checkService() > 0) {   // 这里等于虚设
            //
        }
        Intent intent = new Intent();
        intent.setClassName(idPkgName, "com.mdid.msa.service.MsaKlService");
        intent.setAction("com.bun.msa.action.start.service");
        intent.putExtra("com.bun.msa.param.pkgname", pkgName);
        try {
            intent.putExtra("com.bun.msa.param.runinset", true);
            if (mContext.startService(intent) != null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getID(AppIdsUpdater _listener) {
        try {
            mContext.getPackageManager().getPackageInfo(idPkgName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String pkgName = mContext.getPackageName();
        startMsaklServer(pkgName);

        Intent v0 = new Intent();
        v0.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaIdService");
        v0.setAction("com.bun.msa.action.bindto.service");
        v0.putExtra("com.bun.msa.param.pkgname", pkgName);
        boolean isBin = mContext.bindService(v0, serviceConnection, Context.BIND_AUTO_CREATE);
        if (isBin) {
            try {
                IBinder iBinder = linkedBlockingQueue.take();
                ZTEIDInterface zteidInterface = new ZTEIDInterface.Proxy(iBinder);
                String oaid = zteidInterface.getOAID();

                if (_listener != null) {
                    _listener.OnIdsAvalid(oaid);
                }

                mContext.unbindService(serviceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mContext.unbindService(serviceConnection);
            }
        }
    }

    public final LinkedBlockingQueue<IBinder> linkedBlockingQueue = new LinkedBlockingQueue(1);
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                linkedBlockingQueue.put(service);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
