package com.yunshitu.activitystudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.net.Socket;
import java.net.URI;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Messenger messenger;

    private static class ClientHandler extends Handler{
        @Override
        public void handleMessage( @NonNull Message msg ) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Log.i("info","server response msg == "+msg.getData().getString("key"));
                    break;
                case 1:
                    break;
                    default:
                        break;
            }
        }
    }

    private Messenger mClientMsger = new Messenger(new ClientHandler());

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected( ComponentName name, IBinder service ) {
            messenger = new Messenger(service);
            Message msg = new Message();
            msg.what = 0;
            Bundle data = new Bundle();
            data.putString("key","hello ，this is client");
            msg.setData(data);
            try {
                msg.replyTo = mClientMsger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected( ComponentName name ) {

        }
    };


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,TAG+"--onCreate");
        final Intent intent = new Intent(this,MessegeService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        findViewById(R.id.btn_socket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent1 = new Intent(MainActivity.this, SocketActivity.class);
                startActivity(intent1);
            }
        });
        findViewById(R.id.btn_bindPoll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent1 = new Intent(MainActivity.this, TwoActivity.class);
//                Intent intent1 = new Intent(MainActivity.this, BinderPoolActivity.class);
                startActivity(intent1);
            }
        });

    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,TAG+"--onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,TAG+"--onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,TAG+"--onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,TAG+"--onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,TAG+"--onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,TAG+"--onDestroy");
        unbindService(connection);

    }

    public void startTwo( View view ) {
        Intent intent = new Intent(this,TwoActivity.class);
        startActivity(intent);
    }
}
