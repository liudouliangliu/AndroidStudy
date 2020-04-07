package com.yunshitu.activitystudy;

import android.os.RemoteException;

import com.yunshitu.activitystudy.aidldemo.ISecurityCenter;

/**
 * @author : liudouliang
 * @date : 2020/3/10 10:15
 * @ des   :
 */
public class SecurityCenterImp extends ISecurityCenter.Stub {
    private static final char SECURITYCODE = '^';

    @Override
    public String encrypt( String content ) throws RemoteException {

        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECURITYCODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt( String password ) throws RemoteException {
        return encrypt(password);
    }
}
