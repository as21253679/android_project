package com.example.sa.tcpip_server;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by SA on 2017/10/27.
 */

public class MyServerThread extends Thread{
    private static final String LOG_TAG = "MyServerThread";
    private MainActivity  m_activityMain;
    private Handler mHandler;
    char c=' ';
    public MyServerThread(MainActivity activityMain)
    {
        super();

        // Save the activity
        m_activityMain = activityMain;
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                        m_activityMain.textView.setText(String.valueOf(c));
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                // Wait for new client connection
                Log.i(LOG_TAG, "Waiting for client connection…");
                Socket socketClient = m_activityMain.m_serverSocket.accept();

                Log.i(LOG_TAG, "Accepted connection from " + socketClient.getInetAddress().getHostAddress());

                // Read input from client socket
                InputStream is = socketClient.getInputStream();
                OutputStream os = socketClient.getOutputStream();
                while (!socketClient.isClosed())
                {
                    try
                    {
                        while (is.available() > 0) {
                            c = (char) is.read();
                            Log.i(LOG_TAG, "Read client socket=[ " + String.valueOf(c) + "]");
                            try {
                                Message msg = new Message();
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                                Thread.sleep(100);
                            }
                            catch (Exception e)
                            {}
                        }
                        c=' ';
                    }
                    catch(Exception e){}
                }

                // Close streams
                //dis.close();
                os.close();
                is.close();

                // Close client socket
                Log.i(LOG_TAG, "Read data from client ok. Close connection from " + socketClient.getInetAddress().getHostAddress());
                socketClient.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            // Stop loop when server socket is closed
            if (m_activityMain.m_serverSocket.isClosed())
            {
                break;
            }
        }
    }
}
