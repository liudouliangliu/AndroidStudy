package com.yunshitu.activitystudy;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.yunshitu.activitystudy.aidldemo.Book;
import com.yunshitu.activitystudy.aidldemo.IBookManager;
import com.yunshitu.activitystudy.aidldemo.ICompute;
import com.yunshitu.activitystudy.aidldemo.ISecurityCenter;
import com.yunshitu.activitystudy.aidldemo.OnBookArriveListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author : liudouliang
 * @date : 2020/3/4 18:25
 * @ des   :
 */
public class AidlService extends Service {
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<OnBookArriveListener> listeners = new RemoteCallbackList<>();
    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public boolean onTransact( int code, Parcel data, Parcel reply, int flags ) throws RemoteException {

            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook( Book book ) throws RemoteException {
            mBookList.add(book);
            int N = listeners.beginBroadcast();
            for (int i=0;i<N;i++){
                OnBookArriveListener onBookArriveListener = listeners.getBroadcastItem(i);
                onBookArriveListener.onNewBookArrive(book);
            }
            listeners.finishBroadcast();
        }

        @Override
        public void registerLestener( OnBookArriveListener listener ) throws RemoteException {
            listeners.register(listener);
        }

        @Override
        public void unregisterLestener( OnBookArriveListener listener ) throws RemoteException {
            listeners.unregister(listener);
        }
    };

    private IBinder mBinderPool = new BinderPool.BinderPoolImp();
    private String Tag = "AidlService";

    @Nullable
    @Override
    public IBinder onBind( Intent intent ) {
        Log.i(Tag,"onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Tag,"onCreate");
        mBookList.add(new Book(1,"书籍1"));
        mBookList.add(new Book(2,"书籍2"));
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        Log.i(Tag,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
