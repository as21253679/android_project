package com.inuker.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.ByteUtils;

import static com.inuker.bluetooth.library.Constants.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

/**
 * Created by dingjikerbo on 2016/9/6.
 */
public class CharacterActivity extends Activity implements View.OnClickListener {

    private String mMac;
    private UUID mService;
    private UUID mCharacter;

    private TextView mTvTitle;
    private TextView mDataView;

    private Button mBtnNotify;
    private Button mBtnUnnotify;
    private Button mReadFile;
    private Button mEraseFile;
    private Button mWrite;

    private String data="";
    private String data_string="";

    private String uuid_write= "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    private String uuid_read = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";

    String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String savePath = fullPath + File.separator + "/" + "dsp_data" + ".txt";
    File file = new File(savePath);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.character_activity);

        Intent intent = getIntent();
        mMac = intent.getStringExtra("mac");
        mService = (UUID) intent.getSerializableExtra("service");
        mCharacter = (UUID) intent.getSerializableExtra("character");

        mTvTitle = (TextView) findViewById(R.id.title);
        mTvTitle.setText(String.format("%s", mMac));

        mDataView= (TextView) findViewById(R.id.dataview);
        mDataView.setMovementMethod(new ScrollingMovementMethod());

        mBtnNotify = (Button) findViewById(R.id.notify);
        mBtnUnnotify = (Button) findViewById(R.id.unnotify);
        mReadFile = (Button) findViewById(R.id.read_file);
        mEraseFile = (Button) findViewById(R.id.erase_file);
        mWrite = (Button) findViewById(R.id.write);

        mBtnNotify.setOnClickListener(this);
        mBtnNotify.setEnabled(true);

        mBtnUnnotify.setOnClickListener(this);
        mBtnUnnotify.setEnabled(false);

        mWrite.setOnClickListener(this);
        mWrite.setEnabled(true);

        mReadFile.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                BufferedReader br = null;

                try {
                    StringBuffer output = new StringBuffer();

                    br = new BufferedReader(new FileReader(savePath));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        output.append(line +"\n");
                    }
                    mDataView.setText(output);
                    br.close();

                } catch(Exception e)
                {}
            }
        });

        mEraseFile.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile(),false);
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write("");
                    bw.close();
                }
                catch (Exception e)
                {}
            }
        });

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch (Exception e)
        {}
    }

    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                CommonUtils.toast("success");
            } else {
                CommonUtils.toast("failed");
            }
        }
    };

    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(mService) && character.equals(mCharacter)) {
                data=ByteUtils.byteToString(value);
                //mBtnNotify.setText(String.format("%s", data));

                if(data.equals("FE")) {
                    ClientManager.getClient().unnotify(mMac, mService, mCharacter, mUnnotifyRsp);
                    mDataView.setText("END");
                    return;
                }
                else if(data.equals("FD")) {
                    mDataView.setText("START");
                    return;
                }

                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write(data);
                    bw.close();
                }
                catch (Exception e)
                {}
            }
        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                mBtnNotify.setEnabled(false);
                mBtnUnnotify.setEnabled(true);
                CommonUtils.toast("success");
            } else {
                CommonUtils.toast("failed");
            }
        }
    };

    private final BleUnnotifyResponse mUnnotifyRsp = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                CommonUtils.toast("success");
                mBtnNotify.setEnabled(true);
                mBtnUnnotify.setEnabled(false);
            } else {
                CommonUtils.toast("failed");
            }
        }
    };

    @Override
    public void onClick(View v) {
        byte[] b=new byte[1];

        mDataView.setText("");
        switch (v.getId()) {
            case R.id.write:
                b[0]=0x01;
                mCharacter=UUID.fromString(uuid_write);
                ClientManager.getClient().write(mMac, mService, mCharacter,b, mWriteRsp);

                mCharacter=UUID.fromString(uuid_read);
                ClientManager.getClient().unnotify(mMac, mService, mCharacter, mUnnotifyRsp);
                break;
            case R.id.notify:
                //erase file
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile(),false);
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write("");
                    bw.close();
                }
                catch (Exception e)
                {}
                //send read command
                b[0]=0x02;
                mCharacter=UUID.fromString(uuid_write);
                ClientManager.getClient().write(mMac, mService, mCharacter,b, mWriteRsp);
                //read
                mCharacter=UUID.fromString(uuid_read);
                ClientManager.getClient().notify(mMac, mService, mCharacter, mNotifyRsp);
                break;
            case R.id.unnotify:
                b[0]=0x01;
                mCharacter=UUID.fromString(uuid_write);
                ClientManager.getClient().write(mMac, mService, mCharacter,b, mWriteRsp);

                mCharacter=UUID.fromString(uuid_read);
                ClientManager.getClient().unnotify(mMac, mService, mCharacter, mUnnotifyRsp);
                break;
        }
    }

    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            BluetoothLog.v(String.format("CharacterActivity.onConnectStatusChanged status = %d", status));

            if (status == STATUS_DISCONNECTED) {
                CommonUtils.toast("disconnected");
                mBtnNotify.setEnabled(false);
                mBtnUnnotify.setEnabled(false);

                mTvTitle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                    }
                }, 300);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        ClientManager.getClient().registerConnectStatusListener(mMac, mConnectStatusListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ClientManager.getClient().unregisterConnectStatusListener(mMac, mConnectStatusListener);
    }
}
