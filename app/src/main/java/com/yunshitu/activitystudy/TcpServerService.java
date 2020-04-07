package com.yunshitu.activitystudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * @author : liudouliang
 * @date : 2020/3/9 10:29
 * @ des   :
 */
public class TcpServerService extends Service {
    private boolean mIsServiceDestroyed = false;
    private String[] mDefaultMessages = {"你好啊，哈哈",
            "请问你叫什么名字啊", "今天北京天气不错啊，" +
            "shy", "你知道吗？我可是可以和多个人一起聊天的哦",
            "给你讲个笑话吧，据说爱笑的人运气不会太差，不知道真假"};

    @Nullable
    @Override
    public IBinder onBind( Intent intent ) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestroyed = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServer()).start();
    }

    private class TcpServer implements Runnable{

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                //监听本地8688端口
                serverSocket = new ServerSocket(8688);
                Log.e("error","启动服务器");
            }catch (Exception e){
                Log.e("error","服务器初始化异常=="+e.toString());
                return;
            }


            while (!mIsServiceDestroyed){
                try {
                    final Socket client = serverSocket.accept();
                    Log.e("error","accept");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }catch (Exception e){
                    Log.e("error","服务器接收消息异常=="+e.toString());

                }
            }

        }

    }

    private void responseClient(Socket client) throws IOException {
        //接收客户端的消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
        out.println("欢迎来到聊天室");
        Log.e("error","mIsServiceDestroyed=="+mIsServiceDestroyed);
        while (!mIsServiceDestroyed){
            Log.e("error","服务正常"+in);
            String str = in.readLine();
            Log.e("error","服务正常11");
            if (str==null){
                Log.e("error","服务断开");
                //断开连接
                break;
            }
            int i = new Random().nextInt(mDefaultMessages.length);
            String msg = mDefaultMessages[i];
            Log.e("error","服务返回内容=="+msg);
            out.println(msg);
        }
        //关闭流
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }

}
