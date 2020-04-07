package com.yunshitu.activitystudy;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yunshitu.activitystudy.aidldemo.ICompute;
import com.yunshitu.activitystudy.aidldemo.ISecurityCenter;

/**
 * @author : liudouliang
 * @date : 2020/3/11 15:28
 * @ des   : binder连接池activity
 */
public class BinderPoolActivity extends AppCompatActivity {
    private ISecurityCenter mSecurityCenter;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        new Thread(){
            @Override
            public void run() {
                super.run();
                doWork();
            }
        }.start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = SecurityCenterImp.asInterface(securityBinder);
        String msg = "helloword--安卓";
        Log.i("info","context=="+msg);
        try {
            String password = mSecurityCenter.decrypt(msg);
            Log.i("info","password=="+password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute iCompute = ComputeImp.asInterface(computeBinder);
        try {
            int sum = iCompute.add(1,1);
            Log.i("info","iCompute result:"+sum);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
