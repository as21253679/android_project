package com.example.bet.a8_17;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity3 extends AppCompatActivity {
    Thread Thread0;
    private Button back2,testuvi;
    private Handler mHandler;
    private InputStream inputStream =null;
    private OutputStream outStream = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    public int a;
    public Boolean RUN_THREAD=true;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private static String address = "30:14:12:18:19:29";//第一科
    //private static String address = "30:14:12:18:25:71";//第二科
    private static String address = "30:14:10:27:12:70";//第三科
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        back2=(Button) findViewById(R.id.button3);
        testuvi=(Button) findViewById(R.id.button6);

        back2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RUN_THREAD = false;
                    Thread0.interrupt();
                    Thread0 = null;
                }catch (Exception e){}
                try {
                    btSocket.close();
                } catch (IOException e2) {
                }
                /////延遲1秒
                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity3.this , MainActivity.class);
                                startActivity(intent);
                                MainActivity3.this.finish();
                            }
                        });
                    }
                }, 1000);
            }
        });
//////////////UVI
           Thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(RUN_THREAD) {
                    try {
                        char f;
                        String mess = "C", me, meAll = "";
                        int mess2 = 52, intall = 0, intall2 = 0, intall3 = 0;
                        byte[] msgBuff = mess.getBytes();
                        try {
                            outStream.write(msgBuff);
                        } catch (IOException e) {
                        }
                        try {
                            while ((mess2 = inputStream.read()) != 65) {

                                f = (char) mess2;
                                me = String.valueOf(f);
                                meAll += me;
                            }
                        } catch (IOException e) {

                        }
                        intall = Integer.parseInt(meAll.substring(0, 1));
                        intall2 = Integer.parseInt(meAll.substring(1, 2));
                        intall3 = intall * 10 + intall2;
                        if (intall3 == 0) {
                            Message msg = new Message();
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 1) {
                            Message msg = new Message();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 2) {
                            Message msg = new Message();
                            msg.what = 2;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 3) {
                            Message msg = new Message();
                            msg.what = 3;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 4) {
                            Message msg = new Message();
                            msg.what = 4;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 5) {
                            Message msg = new Message();
                            msg.what = 5;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 6) {
                            Message msg = new Message();
                            msg.what = 6;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 7) {
                            Message msg = new Message();
                            msg.what = 7;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 8) {
                            Message msg = new Message();
                            msg.what = 8;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 9) {
                            Message msg = new Message();
                            msg.what = 9;
                            mHandler.sendMessage(msg);
                        } else if (intall3 == 10) {
                            Message msg = new Message();
                            msg.what = 10;
                            mHandler.sendMessage(msg);
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                        }
                    }catch(Exception e){}
                }
            }
        });


        mHandler = new Handler(){
            int i = 0;
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                        testuvi.setText("LV0");
                        break;
                    case 1:
                        testuvi.setText("LV1");
                        break;
                    case 2:
                        testuvi.setText("LV2");
                        break;
                    case 3:
                        testuvi.setText("LV3");
                        break;
                    case 4:
                        testuvi.setText("LV4");
                        break;
                    case 5:
                        testuvi.setText("LV5");
                        break;
                    case 6:
                        testuvi.setText("LV6");
                        break;
                    case 7:
                        testuvi.setText("LV7");
                        break;
                    case 8:
                        testuvi.setText("LV8");
                        break;
                    case 9:
                        testuvi.setText("LV9");
                        break;
                    case 10:
                        testuvi.setText("LV10+");
                        break;
                }
                super.handleMessage(msg);
            }
        };
        testuvi.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {

               testuvi.setEnabled(false);
                Thread0.start();
               /*char f;
                String mess = "C", me, meAll = "";
                int mess2 = 52, intall = 0, intall2 = 0, intall3 = 0;
                byte[] msgBuff = mess.getBytes();
                try {
                    outStream.write(msgBuff);
                } catch (IOException e) {
                }
                try {
                    while ((mess2 = inputStream.read()) != 65) {

                            f = (char) mess2;
                            me = String.valueOf(f);
                            meAll += me;
                    }
                } catch (IOException e) {

                }
                intall = Integer.parseInt(meAll.substring(0, 1));
                intall2 = Integer.parseInt(meAll.substring(1, 2));
                intall3 = intall * 10 + intall2;
                if (intall3 == 0) {
                    testuvi.setText("LV0");
                } else if (intall3 == 1) {
                    testuvi.setText("LV1");
                } else if (intall3 == 2) {
                    testuvi.setText("LV2");
                }else if (intall3 == 3) {
                    testuvi.setText("LV3");
                }else if (intall3 == 4) {
                    testuvi.setText("LV4");
                }else if (intall3 == 5) {
                    testuvi.setText("LV5");
                }else if (intall3 == 6) {
                    testuvi.setText("LV6");
                }else if (intall3 == 7) {
                    testuvi.setText("LV7");
                }else if (intall3 == 8) {
                    testuvi.setText("LV8");
                }else if (intall3 == 9) {
                    testuvi.setText("LV9");
                }else if (intall3 == 10) {
                    testuvi.setText("LV10+");
                }
                testuvi.setTextSize(85);
                /////延遲1秒
                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                testuvi.setEnabled(true);
                            }
                        });
                    }
                }, 1000);*/
            }
        });
    }

    protected void onResume() {
        super.onResume();
        //blue
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
        }
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
            }
        }
        try {
            outStream = btSocket.getOutputStream();
            inputStream = btSocket.getInputStream();
        } catch (IOException e) {
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            RUN_THREAD = false;
            Thread0.interrupt();
            Thread0 = null;
        }catch (Exception e){}
        try {
            btSocket.close();
        } catch (IOException e2) {
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            RUN_THREAD = false;
            Thread0.interrupt();
            Thread0 = null;
        }catch (Exception e){}
        try {
            btSocket.close();
        } catch (IOException e2) {
        }

    }

}
