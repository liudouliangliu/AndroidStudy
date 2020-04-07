package com.yunshitu.activitystudy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : liudouliang
 * @date : 2020/3/9 10:27
 * @ des   : socket 测试activity
 */
public class SocketActivity extends AppCompatActivity {
    private static final int MESSEGE_RECEIVE_NEW = 1;
    private static final int MESSEGE_SOCKET_CONNECT = 2;
    private MessegeAdapter adapter;
    private EditText mEtMsg;
    private Socket mClientSocket;
    private PrintWriter mPrintWriter;
    private boolean isDestroy = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage( @NonNull Message msg ) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSEGE_RECEIVE_NEW:
                    String newMsg = (String) msg.obj;
                    adapter.addMsg(newMsg);
                    mRc.smoothScrollToPosition(adapter.getItemCount()-1);
                    break;
                case MESSEGE_SOCKET_CONNECT:
                    break;
                default:
                    break;
            }
        }
    };
    private RecyclerView mRc;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        initView();
    }

    private void initView() {
        adapter = new MessegeAdapter();
        mRc = findViewById(R.id.ry);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRc.setLayoutManager(manager);
        mRc.setAdapter(adapter);
//        manager.setStackFromEnd(true);
//        manager.scrollToPositionWithOffset(adapter.getItemCount() - 1, Integer.MIN_VALUE);
        mEtMsg = findViewById(R.id.etMsg);
        Button mBtnSend = findViewById(R.id.btnSend);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if (TextUtils.isEmpty(mEtMsg.getText())) {
                    Toast.makeText(SocketActivity.this, "发送内容不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                sendMsg(mEtMsg.getText().toString());
            }
        });
        Intent intent = new Intent(this, TcpServerService.class);
        startService(intent);
        new Thread() {
            @Override
            public void run() {
                super.run();
                connectTcpService();
            }
        }.start();
    }

    private void connectTcpService() {
        Socket socket = null;
        while (socket == null && !isDestroy) {
            try {
                socket = new Socket("localhost", 8688);
                if (socket != null) {
                    Log.e("error", "连接服务器成功");
                    mClientSocket = socket;
                    mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    mHandler.sendEmptyMessage(MESSEGE_SOCKET_CONNECT);
                } else {
                    Log.e("error", "连接服务器失败");
                }
            } catch (Exception e) {
                SystemClock.sleep(1000);
                Log.e("error", "connectTcpService连接服务器异常==" + e.toString());
            }
        }

        try {
            //接收服务器消息
            if (socket != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!SocketActivity.this.isFinishing()) {
                    String msg = br.readLine();
                    msg = "server-" + getNowTimeStr() + ":" + msg;
                    Message message = mHandler.obtainMessage();
                    message.what = MESSEGE_RECEIVE_NEW;
                    message.obj = msg;
                    mHandler.sendMessage(message);
                }
                MyUtils.close(mPrintWriter);
                MyUtils.close(br);
                socket.close();
            }
        } catch (Exception e) {
            Log.e("error", "connectTcpService连接服务器异常==" + e.toString());
        }
    }

    private void sendMsg( final String toString ) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (mPrintWriter != null) {
                    mEtMsg.setText("");
                    String time = getNowTimeStr();
                    String msg = "self-" + time + ":" + toString;
                    Log.e("error", "发送内容==" + msg);
                    Message message = mHandler.obtainMessage();
                    message.obj = msg;
                    message.what = MESSEGE_RECEIVE_NEW;
                    mHandler.sendMessage(message);
                    mPrintWriter.println(toString);
                }
            }
        }.start();

    }

    private String getNowTimeStr() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (Exception e) {
                Log.e("error", "关闭socket连接异常==" + e.toString());
            }
        }
    }
}
