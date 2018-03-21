package com.example.sa.camera;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "THINBTCLIENT";
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private InputStream inputStream = null;
    private OutputStream outStream = null;
    private Handler mHandler;
    private Button but;
    private TextView textview;
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "98:D3:34:91:13:03";
    FileOutputStream output;
    byte[] c = new byte[1];
    byte[] pkt = new byte[122];
    int total = 0;
    boolean flag = false, flag2 = false;
    int bytes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but = (Button) this.findViewById(R.id.button);
        textview = (TextView) this.findViewById(R.id.textView);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //建立FileOutputStream物件，路徑為SD卡中的123.jpg
                try {
                    output = new FileOutputStream("/sdcard/123.jpg");
                } catch (Exception e) {}
                try {
                    byte[] msgBuffer = "A".getBytes();
                    outStream.write(msgBuffer, 0, 1);
                } catch (Exception e) {
                }
                try {
                    while (true) {
                        if (flag) {
                            flag = false;
                            break;
                        }
                        while (inputStream.available() > 0) {
                            inputStream.read(c, 0, 1);
                            inputStream.read(c, 0, 1);
                            total += (c[0] - 48) * 100;
                            inputStream.read(c, 0, 1);
                            total += (c[0] - 48) * 10;
                            inputStream.read(c, 0, 1);
                            total += (c[0] - 48);
                            textview.setText(Integer.toString(total));
                            flag = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                }
                //收圖
                try {
                    for (int i = 0; i < total - 1; i++) {
                        while (true) {
                            if (flag2) {
                                flag2 = false;
                                break;
                            }
                            while (inputStream.available() > 122) {
                                inputStream.read(pkt, 0, 122);
                                for (int j = 0; j < 122; j++) {
                                    bytes=pkt[j] & 0xFF;
                                    output.write(bytes);
                                }
                                flag2 = true;
                                break;
                            }
                        }
                    }
                }catch(Exception e){
                }
                try {
                    output.close();
                } catch (Exception e) {
                }
                total = 0;
            }
        });
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
        if (true)
            Log.e(TAG, "+++ DONE IN ON CREATE, GOT LOCAL BT ADAPTER +++");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        String message = "Hello message from client to server.";
        byte[] msgBuffer = message.getBytes();
        if (true) {
            Log.e(TAG, "+ ON RESUME +");
            Log.e(TAG, "+ ABOUT TO ATTEMPT CLIENT CONNECT +");
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
        }

        mBluetoothAdapter.cancelDiscovery();

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

        if (true)
            Log.e(TAG, "+ ABOUT TO SAY SOMETHING TO SERVER +");

        try {
            outStream = btSocket.getOutputStream();
            inputStream = btSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
        }
        //Thread0.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
            }
        }
        try {
            btSocket.close();
        } catch (IOException e2) {
            Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
        }
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped <n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b <0) {
                        break;//we reached EOF
                    } else {
                        bytesSkipped = 1;//we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
