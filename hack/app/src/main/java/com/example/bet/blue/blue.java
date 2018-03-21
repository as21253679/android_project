package com.example.bet.blue;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.example.bet.blue.R.layout.activity_blue;

public class blue extends Activity {

    private static final String TAG = "THINBTCLIENT";
    private static final boolean D = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private InputStream inputStream =null;
    private OutputStream outStream = null;
    private TextView a;
    private Button button;
    private  Handler mHandler;
    private boolean t1=false;
    Thread Thread0;
    private String roll="",pitch="",yaw="";
    // Well known SPP UUID (will *probably* map to
    // RFCOMM channel 1 (default) if not in use);
    // see comments in onResume().
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // ==> hardcode your server's MAC address here <==
   //private static String address = "30:14:12:18:19:29";
    private static String address = "30:14:12:18:25:71";//第二科

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_blue);
        a=(TextView)findViewById(R.id.a);
        button=(Button)findViewById(R.id.button);
        if (D)
            Log.e(TAG, "+++ ON CREATE +++");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth is not available.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this,
                    "Please enable your BT and re-run this program.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (D)
            Log.e(TAG, "+++ DONE IN ON CREATE, GOT LOCAL BT ADAPTER +++");
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    byte[] msgBuffer = "1".getBytes();
                    outStream.write(msgBuffer);
                } catch (IOException e) {
                    Log.e(TAG, "ON RESUME: Exception during write.", e);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                //String mess = (edit.getText()).toString() ;
                String mess = "a999";
                char c;
                byte[] msgBuff = mess.getBytes();
                while (t1) {
                    try {
                        while (inputStream.available() > 0) {
                            c = (char) inputStream.read();
                            if(c=='2'){
                            try {
                                Message msg = new Message();
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                                Thread.sleep(100);
                            }
                            catch (Exception e)
                            {}
                            }
                            else
                            {
                                try {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);
                                    Thread.sleep(100);
                                }
                                catch (Exception e)
                                {}
                            }
                            //roll += String.valueOf(c);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "ON RESUME: Exception during write.", e);
                    }
                    // float t = Float.parseFloat(roll);

                }
            }
        });
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                        a.setText("有人入侵!!");
                        break;
                    case 1:
                        a.setText("正常");
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();


        String message = "Hello message from client to server.";
        byte[] msgBuffer = message.getBytes();
        if (D) {
            Log.e(TAG, "+ ON RESUME +");
            Log.e(TAG, "+ ABOUT TO ATTEMPT CLIENT CONNECT +");
        }

        // When this returns, it will 'know' about the server,
        // via it's MAC address.
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);



        // We need two things before we can successfully connect
        // (authentication issues aside): a MAC address, which we
        // already have, and an RFCOMM channel.
        // Because RFCOMM channels (aka ports) are limited in
        // number, Android doesn't allow you to use them directly;
        // instead you request a RFCOMM mapping based on a service
        // ID. In our case, we will use the well-known SPP Service
        // ID. This ID is in UUID (GUID to you Microsofties)
        // format. Given the UUID, Android will handle the
        // mapping for you. Generally, this will return RFCOMM 1,
        // but not always; it depends what other BlueTooth services
        // are in use on your Android device.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
        }

        // Discovery may be going on, e.g., if you're running a
        // 'scan for devices' search from your handset's Bluetooth
        // settings, so we call cancelDiscovery(). It doesn't hurt
        // to call it, but it might hurt not to... discovery is a
        // heavyweight process; you don't want it in progress when
        // a connection attempt is made.
        mBluetoothAdapter.cancelDiscovery();

        // Blocking connect, for a simple client nothing else can
        // happen until a successful connection is made, so we
        // don't care if it blocks.
        try {
            btSocket.connect();
            Log.e(TAG, "ON RESUME: BT connection established, data transfer link open.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e(TAG,
                        "ON RESUME: Unable to close socket during connection failure", e2);
            }
        }

        // Create a data stream so we can talk to server.
        if (D)
            Log.e(TAG, "+ ABOUT TO SAY SOMETHING TO SERVER +");

        try {
            outStream = btSocket.getOutputStream();
            inputStream = btSocket.getInputStream() ;
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
        }

        try {
            outStream.write(msgBuffer);
            t1=true;
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Exception during write.", e);
        }
        Thread0.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (D)
            Log.e(TAG, "- ON PAUSE -");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }
}