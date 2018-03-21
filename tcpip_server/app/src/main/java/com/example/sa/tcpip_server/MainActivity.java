package com.example.sa.tcpip_server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;

public class MainActivity extends AppCompatActivity {
    private Button button;
    public TextView textView;
    // Public variables
    public ServerSocket m_serverSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Open a server socket
                    int nServerPort = 8888;
                    m_serverSocket = new ServerSocket(nServerPort);

                    // Start a server thread to do socket-accept tasks
                    MyServerThread serverThread = new MyServerThread(MainActivity.this);
                    serverThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
