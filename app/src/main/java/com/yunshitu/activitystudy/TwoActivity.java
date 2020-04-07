package com.yunshitu.activitystudy;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.yunshitu.activitystudy.aidldemo.Book;
import com.yunshitu.activitystudy.aidldemo.IBookManager;
import com.yunshitu.activitystudy.aidldemo.OnBookArriveListener;

import java.net.CookieManager;
import java.util.List;
import java.util.Random;

/**
 * @author liudouliang_liu
 */
public class TwoActivity extends AppCompatActivity {
    private static final String TAG = "TwoActivity";

    private OnBookArriveListener listener = new OnBookArriveListener.Stub() {
        @Override
        public void onNewBookArrive( Book newBook ) throws RemoteException {
            Log.i("AidlService","收到了消息了 == "+newBook.getBookName());
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected( ComponentName name, IBinder service ) {
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> bookList = bookManager.getBookList();
                Log.i("info","书籍列表 == "+bookList.toString());
                Book book = new Book(3,"Android开发艺术探索");
                bookManager.addBook(book);
                List<Book> newkList = bookManager.getBookList();
                bookManager.registerLestener(listener);
                Log.i("AidlService","书籍列表:"+new Gson().toJson(newkList));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected( ComponentName name ) {

        }
    };
    private IBookManager bookManager;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Log.i(TAG, TAG + "--onCreate");
        Intent intent = new Intent(this,AidlService.class);
//        intent.setComponent(new ComponentName("",""));
        bindService(intent,connection,BIND_AUTO_CREATE);
        findViewById(R.id.addBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                addBook();
            }
        });
        ///////////////////////////////
        int scaledDoubleTapSlop = ViewConfiguration.get(this).getScaledDoubleTapSlop();
        Log.i("AidlService","touchSlop value is: "+scaledDoubleTapSlop);
        ///////////////////


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, TAG + "--onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, TAG + "--onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, TAG + "--onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, TAG + "--onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, TAG + "--onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, TAG + "--onDestroy");
        if (bookManager!=null&&bookManager.asBinder().isBinderAlive()){
            try {
                bookManager.unregisterLestener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);

    }

    public void addBook() {
        if (bookManager!=null&&bookManager.asBinder().isBinderAlive()){
            int id = new Random().nextInt(1000);
            String name = "测试书籍--"+id;
            try {
                bookManager.addBook(new Book(id,name));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
