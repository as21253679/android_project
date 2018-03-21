package com.example.bet.a8_17;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity2 extends AppCompatActivity {
    private Button button3,back;
    private ListView listView;
    private ListAdapter listAdapter;
    private ByteArrayOutputStream stream=new ByteArrayOutputStream();
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView=(ListView)findViewById(R.id.listView);
        button3 =(Button)findViewById(R.id.button4);
        back =(Button)findViewById(R.id.button5);
        final String FILENAME = "date.txt";
        final byte[] byteBuf = new byte[1024];
        listView = (ListView) findViewById(R.id.listView);

        back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity2.this , MainActivity.class);
                startActivity(intent);
                MainActivity2.this.finish();
            }
        });

        String s=null;
        try {
            FileInputStream fos;
            fos = openFileInput(FILENAME);

            int length = -1;
            while ((length = fos.read(byteBuf)) != -1) {
                stream.write(byteBuf, 0, length);
            }
            s = stream.toString();
            fos.close();
            stream.close();
        } catch (Exception e) {}

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{s});
        listView.setAdapter(listAdapter);



        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write("".getBytes());
                    fos.close();
                } catch (Exception e) {}
                listView.setAdapter(null);
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        });
        mHandler = new Handler(){
            int i = 0;
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        stream=new ByteArrayOutputStream();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
}
