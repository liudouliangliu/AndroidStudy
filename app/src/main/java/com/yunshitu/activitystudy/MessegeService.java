package com.yunshitu.activitystudy;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author : liudouliang
 * @date : 2020/3/4 17:29
 * @ des   :
 */
public class MessegeService extends Service {
    private static final String  TAG = "MessegeService";

    private static class MessegeHandler extends Handler{
        @Override
        public void handleMessage( @NonNull Message msg ) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Log.i("info","receive from client messege == "+msg.getData().getString("key"));
                    Messenger clientMessenger = msg.replyTo;
                    Bundle bundle = new Bundle();
                    bundle.putString("key","嗯，收到你的消息了，稍后回复你");
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 0;
                    try {
                        clientMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();

                    }
                    break;
                case 1:
                    break;
                    default:
                        break;
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessegeHandler());


    @Nullable
    @Override
    public IBinder onBind( Intent intent ) {
        return mMessenger.getBinder();
    }
}
