package com.yunshitu.activitystudy;

import android.os.RemoteException;

import com.yunshitu.activitystudy.aidldemo.ICompute;

/**
 * @author : liudouliang
 * @date : 2020/3/10 10:15
 * @ des   :
 */
public class ComputeImp extends ICompute.Stub {
    @Override
    public int add( int a, int b ) throws RemoteException {
        return a+b;
    }
}
