package com.yunshitu.activitystudy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.yunshitu.activitystudy.aidldemo.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * @author : liudouliang
 * @date : 2020/3/11 14:58
 * @ des   :
 */
public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;
    private Context mContext;
    private IBinderPool iBinderPool;
    private static volatile BinderPool sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private IBinderPool mBinderPool;

    private BinderPool(Context context){
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context){
        if (sInstance==null){
            synchronized (BinderPool.class){
                if (sInstance==null){
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext,AidlService.class);
        mContext.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE);

        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        if (mBinderPool!=null){
            try {
                binder = mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;

    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected( ComponentName name, IBinder service ) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();

        }

        @Override
        public void onServiceDisconnected( ComponentName name ) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    public static class BinderPoolImp extends IBinderPool.Stub{

        @Override
        public IBinder queryBinder( int binderCode ) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_NONE:
                    binder = null;
                    break;
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImp();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImp();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }

}
