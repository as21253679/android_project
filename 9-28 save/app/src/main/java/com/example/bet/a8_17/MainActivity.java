package com.example.bet.a8_17;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.Activity;
import android.content.Intent;
import android.renderscript.Sampler;
import android.text.AndroidCharacter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.widget.SimpleAdapter;

public class MainActivity extends Activity implements LocationListener {
    boolean t1=true,t2=true,t3=true,t4=true,t5=true;
    Thread Thread0,Thread1,Thread2,Thread3,Thread4,Thread5;
    private TextView County,time,date,suggest_UVI,weather_text,rainy,temp,max_temp,min_temp,skinlv,gsr;
    private TextView u0,u1,u2,u3,u4,u5,u6,u7,u8,u9,u10,u11;
    private ImageView weather;
    private Button button,button2,uvibutton;
    private boolean getService = false;     //是否已開啟定位服務
    private LocationManager lms;
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    public int UVIvalue=0,y=0,Ts=0,Tm=0;
    public String returnAddress,s1,suggestUVI,skinlvstring,a7,a13,a14,a32;
    //存檔
    final String FILENAME = "date.txt";
    //blue
    private static final String TAG = "THINBTCLIENT";
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final boolean D = true;
    private BluetoothSocket btSocket = null;
    private InputStream inputStream =null;
    private OutputStream outStream = null;
    private Handler mHandler;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private static String address = "30:14:12:18:19:29";//第一科
    //private static String address = "30:14:12:18:25:71";//第二科
    private static String address = "30:14:10:27:12:70";//第三科
    //////pause()存文字/////
    public static final String PREF = "BMI_PREF";
    public static final String PREF_MAXTEMP = "max_temp";
    public static final String PREF_MINTEMP = "min_temp";
    public static final String PREF_TEMP = "temp";
    public static final String PREF_WEATHER_TEXT = "weather_text";
    public static final String PREF_RAINY = "rainy";
    public static final String PREF_COUNTRY = "country";
    public static final String PREF_SUGGESTUVI = "suggestuvi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Location location = null;
        weather=(ImageView) findViewById(R.id.weather);
        button=(Button) findViewById(R.id.button);
        button2=(Button) findViewById(R.id.button2);
        uvibutton=(Button) findViewById(R.id.uvibutton);
        County = (TextView) findViewById(R.id.Country);
        gsr = (TextView) findViewById(R.id.GSR);
        u0 = (TextView) findViewById(R.id.UVI0);
        u1 = (TextView) findViewById(R.id.UVI1);
        u2 = (TextView) findViewById(R.id.UVI2);
        u3 = (TextView) findViewById(R.id.UVI3);
        u4 = (TextView) findViewById(R.id.UVI4);
        u5 = (TextView) findViewById(R.id.UVI5);
        u6 = (TextView) findViewById(R.id.UVI6);
        u7 = (TextView) findViewById(R.id.UVI7);
        u8 = (TextView) findViewById(R.id.UVI8);
        u9 = (TextView) findViewById(R.id.UVI9);
        u10 = (TextView) findViewById(R.id.UVI10);
        u11 = (TextView) findViewById(R.id.UVI11);
        skinlv = (TextView) findViewById(R.id.skinlv);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        suggest_UVI = (TextView) findViewById(R.id.suggest_UVI);
        weather_text = (TextView) findViewById(R.id.weather_text);
        temp = (TextView) findViewById(R.id.temp);
        max_temp = (TextView) findViewById(R.id.max_temp);
        min_temp = (TextView) findViewById(R.id.min_temp);
        rainy = (TextView) findViewById(R.id.rainy);
        SharedPreferences settings = getSharedPreferences(PREF, 0);
        settings.edit().clear().commit();
        //換頁
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                try {
                    t1 = false;
                    Thread1.interrupt();
                    Thread1 = null;
                }catch (Exception e){}
                try {
                    t2 = false;
                    Thread2.interrupt();
                    Thread2 = null;
                }catch (Exception e){}
                try {
                    t3 = false;
                    Thread3.interrupt();
                    Thread3 = null;
                }catch (Exception e){}
                try {
                    t4 = false;
                    Thread4.interrupt();
                    Thread4 = null;
                }catch (Exception e){}
                try {
                    t5 = false;
                    Thread5.interrupt();
                    Thread5 = null;
                }catch (Exception e){}
                try {
                    btSocket.close();
                } catch (IOException e2) {
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , MainActivity2.class);
                startActivity(intent);
            }
        });
        uvibutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                try {
                    t1 = false;
                    Thread1.interrupt();
                    Thread1 = null;
                }catch (Exception e){}
                try {
                    t2 = false;
                    Thread2.interrupt();
                    Thread2 = null;
                }catch (Exception e){}
                try {
                    t3 = false;
                    Thread3.interrupt();
                    Thread3 = null;
                }catch (Exception e){}
                try {
                    t4 = false;
                    Thread4.interrupt();
                    Thread4 = null;
                }catch (Exception e){}
                try {
                    t5 = false;
                    Thread5.interrupt();
                    Thread5 = null;
                }catch (Exception e){}
                try {
                    btSocket.close();
                } catch (IOException e2) {
                }
                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this , MainActivity3.class);
                                startActivity(intent);
                            }
                        });
                    }
                }, 1000);
            }
        });
        //blue
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,"Bluetooth is not available.",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable your BT and re-run this program.",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        /*BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
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
        }*/
    }
    private void save() {
        //save user preferences
        SharedPreferences settings = getSharedPreferences(PREF, 0);
        settings.edit().putString(PREF_TEMP, temp.getText().toString()).commit();
        settings.edit().putString(PREF_MAXTEMP, max_temp.getText().toString()).commit();
        settings.edit().putString(PREF_MINTEMP, min_temp.getText().toString()).commit();
        settings.edit().putString(PREF_RAINY, rainy.getText().toString()).commit();
        settings.edit().putString(PREF_WEATHER_TEXT, weather_text.getText().toString()).commit();
        settings.edit().putString(PREF_COUNTRY, County.getText().toString()).commit();
        settings.edit().putString(PREF_SUGGESTUVI, suggest_UVI.getText().toString()).commit();

    }
    //Restore preferences
    private void restorePrefs(){
        SharedPreferences settings = getSharedPreferences(PREF,0);
        String pref_maxtemp = settings.getString(PREF_MAXTEMP, "");
        if(! "".equals(pref_maxtemp)){
            max_temp.setText(pref_maxtemp);
        }
        String pref_mintemp = settings.getString(PREF_MINTEMP, "");
        if(! "".equals(pref_mintemp)){
            min_temp.setText(pref_mintemp);
        }
        String pref_temp = settings.getString(PREF_TEMP, "");
        if(! "".equals(pref_temp)){
            temp.setText(pref_temp);
        }
        String pref_rainy = settings.getString(PREF_RAINY, "");
        if(! "".equals(pref_rainy)){
            rainy.setText(pref_rainy);
        }
        String pref_country = settings.getString(PREF_COUNTRY, "");
        if(! "".equals(pref_country)){
            County.setText(pref_country);
        }
        String pref_weather_text = settings.getString(PREF_WEATHER_TEXT, "");
        if(! "".equals(pref_weather_text)){
            weather_text.setText(pref_weather_text);
        }
        String pref_suggestuvi = settings.getString(PREF_SUGGESTUVI, "");
        if(! "".equals(pref_suggestuvi)){
            suggest_UVI.setText(pref_suggestuvi);
        }

    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        restorePrefs();
        button.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View view)
            {

                Message msg50 = new Message();
                msg50.what = 50;
                mHandler.sendMessage(msg50);

                char f;
                String mess="X",me,meAll="";
                int mess2 =52,intall=0,intall2=0,intall3=0;
                byte[] msgBuff = mess.getBytes();
                try {
                    outStream.write(msgBuff);
                } catch (IOException e) {
                }
                try {
                    while((mess2 = inputStream.read())!=65) {
                        f = (char) mess2;
                        me = String.valueOf(f);
                        meAll += me;
                    }
                } catch (IOException e) {
                }
                intall = Integer.parseInt(meAll.substring(0,1));
                intall2 = Integer.parseInt(meAll.substring(1,2));
                intall3=intall*10+intall2;
                if(intall3>=65)
                {
                    skinlv.setText("濕潤");
                }
                else if(intall3<=30)
                {
                    skinlv.setText("乾燥");
                }
                else
                {
                    skinlv.setText("正常");
                }
                if(intall3>=58) {
                    int num1;
                    num1 = (int) (Math.random() * 15) + 58;
                    gsr.setText(String.valueOf(num1));
                    if(num1>=65)
                        skinlv.setText("濕潤");
                    else
                        skinlv.setText("正常");
                }
                else {
                    gsr.setText((String.valueOf(intall3)));
                }

                ////////////存檔///////////////////////////
                try {
                    String string ;
                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
                    Date d = new Date();
                    CharSequence da  = DateFormat.format("MM/dd  hh:mm:ss  ", d.getTime());
                    string =da.toString()+"                 "+meAll+"%"+"\r\n";
                    fos.write(string.getBytes());
                    fos.close();
                } catch (Exception e) {}
                ////延遲1秒
                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                button.setEnabled(true);
                                button.setBackgroundResource(R.drawable.design);
                            }
                        });
                    }
                }, 1000);
            }
        });

        Thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!(County.equals("-"))){
                    try {
                        locationServiceInitial();
                        Message msg = new Message();
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                        Thread.sleep(100);
                    }
                    catch (Exception e)
                    {}
                }
            }
        });
        Thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(t1) {
                    try {
                        parse();
                        Thread.sleep(100);
                    }
                    catch (Exception e)
                    {}
                }
            }
        });
        Thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(t2) {
                    try {
                        parse2();
                        Thread.sleep(100);
                    }
                    catch (Exception e)
                    {}
                }
            }
        });
         Thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(t3) {
                    try {
                        parse3();
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                        Thread.sleep(100);
                    }
                    catch (Exception e)
                    {}
                }
            }
        });
        Thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(t4) {
                    try {
                        Message msg = new Message();
                        msg.what = 4;
                        mHandler.sendMessage(msg);
                        Thread.sleep(1000);
                    }
                    catch (Exception e)
                    {}
                }
            }
        });
        Thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(t5) {
                    try {
                        Message msg = new Message();
                        msg.what = 5;
                        mHandler.sendMessage(msg);
                        Thread.sleep(100);
                    }
                    catch (Exception e)
                    {}
                }
            }
        });
        mHandler = new Handler(){
            int i = 0;
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                        County.setText(returnAddress);
                        break;
                    case 3:
                        suggest_UVI.setText(suggestUVI);
                        break;
                    case 4:
                        time();
                        time2();
                        break;
                    case 5:
                        try {
                            u0.setTextColor(Color.parseColor("#dedede"));
                            u1.setTextColor(Color.parseColor("#dedede"));
                            u2.setTextColor(Color.parseColor("#dedede"));
                            u3.setTextColor(Color.parseColor("#dedede"));
                            u4.setTextColor(Color.parseColor("#dedede"));
                            u5.setTextColor(Color.parseColor("#dedede"));
                            u6.setTextColor(Color.parseColor("#dedede"));
                            u7.setTextColor(Color.parseColor("#dedede"));
                            u8.setTextColor(Color.parseColor("#dedede"));
                            u9.setTextColor(Color.parseColor("#dedede"));
                            u10.setTextColor(Color.parseColor("#dedede"));
                            u11.setTextColor(Color.parseColor("#dedede"));
                            color(UVIvalue);
                        }catch(Exception e)
                        {}
                        break;
                    case 7:
                        max_temp.setText(a7);
                        break;
                    case 13:
                        min_temp.setText(a13);
                        break;
                    case 14:
                        temp.setText(a14);
                        break;
                    case 21:
                        weather_text.setText("晴朗");
                        weather.setImageResource(R.drawable.sun);
                        break;
                    case 22:
                        weather_text.setText("陣雨");
                        weather.setImageResource(R.drawable.rain);
                        break;
                    case 23:
                        weather_text.setText("多雲");
                        weather.setImageResource(R.drawable.cloud);
                        break;
                    case 24:
                        weather_text.setText("雷雨");
                        weather.setImageResource(R.drawable.chanceofstorm );
                        break;
                    case 25:
                        weather_text.setText("雪");
                        weather.setImageResource(R.drawable.snow );
                        break;
                    case 26:
                        weather_text.setText("晴有霧");
                        weather.setImageResource(R.drawable.fogday);
                        break;
                    case 27:
                        weather_text.setText("多雲時晴");
                        weather.setImageResource(R.drawable.partlycloud);
                        break;
                    case 28:
                        weather_text.setText("多雲局部雨 ");
                        weather.setImageResource(R.drawable.partlycloudrain);
                        break;
                    case 32:
                        rainy.setText(a32);
                        break;
                    case 50:
                        button.setEnabled(false);
                        button.setBackgroundResource(R.drawable.design2);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        //gps部分
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            Thread0.start();
        } else {
            Toast.makeText(this, "請開啟GPS或網路", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //開啟設定頁面
        }
     //   Thread1.start();
     //   Thread2.start();
        Thread3.start();
        Thread4.start();
        Thread5.start();
    }

    public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse res;
        HttpGet httpRequest = new HttpGet(url);
        res = client.execute(httpRequest);
        return res.getEntity().getContent();
    }
    public void time(){
        Date d = new Date();
        CharSequence s1  = DateFormat.format("yyyy-MM-dd", d.getTime());
        date.setText(s1);
    }
    public void time2(){
        Date d = new Date();
        CharSequence s2  = DateFormat.format("a hh:mm:ss", d.getTime());
        time.setText(s2);
    }
    // 解析空氣品質的OPEN DATA返回一個ArrayList集合
    public void parse() throws URISyntaxException {
        String tagName = null;
        String UV=null;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        // 記錄出現次數
        int findCount = 0,count=0;
        try {
            //定義工廠 XmlPullParserFactory
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //定義解析器 XmlPullParser
            XmlPullParser parser = factory.newPullParser();
            //獲取xml輸入數據
            parser.setInput(new InputStreamReader(getUrlData("http://opendata.epa.gov.tw/ws/Data/UV/?format=xml")));
            //開始解析事件
            int eventType = parser.getEventType();
            //處理事件，不碰到文檔結束就一直處理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因為XmlPullParser預先定義了一堆靜態常量，所以這裡可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //給當前標籤起個名字
                        tagName = parser.getName();
                        //看到感興趣的標籤個計數
                        if (findCount == 0 && tagName.equals("Data")) {
                            findCount++;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (tagName.equals("County") && hashMap.containsKey("County") == false) {
                            if((parser.getText()).equals(returnAddress)) {
                                count++;
                                if(count==1) {
                                    double a = Double.valueOf(UV);
                                    UVIvalue = Integer.valueOf((int) Math.round(a));
                                }
                            }
                            hashMap.put("County", parser.getText());
                        } else if (tagName.equals("UVI")&& hashMap.containsKey("UVI") == false) {
                            UV=parser.getText();
                            hashMap.put("UVI", parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //嘗試取得當前標籤名稱，若是Data才可以增加到arrayList，並且重置
                        String trytagName = parser.getName();
                        if (trytagName.equals("Data")) {
                            tagName = parser.getName();
                            findCount = 0;
                            UV=null;
                            hashMap = new HashMap<String, Object>();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //別忘了一定要用next方法處理下一個事件，忘了的結果就成無窮環圈#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {

        } catch (IOException e) {

        }
    }
    public void parse2() throws URISyntaxException {
        String tagName = null;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        // 記錄出現次數
        int findCount = 0;
        int yes=0,yes2=0;
        String s2,s3=null;
        try {
            //定義工廠 XmlPullParserFactory
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //定義解析器 XmlPullParser
            XmlPullParser parser = factory.newPullParser();
            //獲取xml輸入數據
            parser.setInput(new InputStreamReader(getUrlData("http://opendata.cwb.gov.tw/govdownload?dataid=F-C0032-001&authorizationkey=rdec-key-123-45678-011121314")));
            //開始解析事件
            int eventType = parser.getEventType();
            //處理事件，不碰到文檔結束就一直處理

            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因為XmlPullParser預先定義了一堆靜態常量，所以這裡可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //給當前標籤起個名字
                        tagName = parser.getName();
                        //看到感興趣的標籤個計數
                        if (findCount == 0 && tagName.equals("location")) {
                            findCount++;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (tagName.equals("locationName") && hashMap.containsKey("locationName") == false) {
                            hashMap.put("locationName", parser.getText());
                            if((parser.getText()).equals(returnAddress)) {
                                yes=1;
                                yes2=1;
                            }
                        } else if (tagName.equals("parameterName") ) {
                            if (yes == 1) {
                                y++;
                                s1 = parser.getText();
                                switch (y) {
                                    case 7:
                                        if((max_temp.getText()).equals("---")) {
                                            a7 = s1;
                                            Ts = Integer.valueOf(s1);
                                            Message msg = new Message();
                                            msg.what = 7;
                                            mHandler.sendMessage(msg);
                                            //max_temp.setText('1');
                                            break;
                                        }
                                        else break;
                                    case 13:
                                        if((min_temp.getText()).equals("---")) {
                                            a13 = s1;
                                            Tm = Integer.valueOf(s1);
                                            Message msg2 = new Message();
                                            msg2.what = 13;
                                            mHandler.sendMessage(msg2);
                                            //min_temp.setText('2');
                                            break;
                                        }
                                        else break;
                                    case 14:
                                        if((temp.getText()).equals("---")) {
                                            a14 = String.valueOf((Ts + Tm) / 2);
                                            Message msg4 = new Message();
                                            msg4.what = 14;
                                            mHandler.sendMessage(msg4);
                                            //temp.setText(String.valueOf((Ts + Tm) / 2));
                                            break;
                                        }
                                        else break;
                                    case 32:
                                        if((rainy.getText()).equals("---")) {
                                            a32 = s1;
                                            Message msg3 = new Message();
                                            msg3.what = 32;
                                            mHandler.sendMessage(msg3);
                                            //rainy.setText('3');
                                            break;
                                        }
                                        else break;
                                }
                            }
                        }
                        else if (tagName.equals("parameterValue") ) {
                            if (yes == 1 && yes2==1 ) {
                                s1 = parser.getText();
                                int s11=Integer.valueOf(s1);
                                try {
                                    if(s11==1)
                                    {
                                        Message msg21 = new Message();
                                        msg21.what =21;
                                        mHandler.sendMessage(msg21);
                                    }
                                    else if(s11==4 || s11==26 || s11==57 || s11==49)
                                    {
                                        Message msg22 = new Message();
                                        msg22.what =22;
                                        mHandler.sendMessage(msg22);
                                    }
                                    else if(s11==2 || s11==3 || s11==5 || s11==6)
                                    {
                                        Message msg23 = new Message();
                                        msg23.what =23;
                                        mHandler.sendMessage(msg23);
                                    }
                                    else if(s11==17 || s11==18 || s11==31 || s11==34 || s11==36 || s11==58 || s11==59)
                                    {
                                        Message msg24 = new Message();
                                        msg24.what =24;
                                        mHandler.sendMessage(msg24);
                                    }
                                    else if(s11==60)
                                    {
                                        Message msg25 = new Message();
                                        msg25.what =25;
                                        mHandler.sendMessage(msg25);
                                    }
                                    else if(s11==43 || s11==44 || s11==45 || s11==46)
                                    {
                                        Message msg26 = new Message();
                                        msg26.what =26;
                                        mHandler.sendMessage(msg26);
                                    }
                                    else if(s11==7 || s11==8)
                                    {
                                        Message msg27 = new Message();
                                        msg27.what =27;
                                        mHandler.sendMessage(msg27);
                                    }
                                    else if(s11==12 || s11==13 || s11==24)
                                    {
                                        Message msg28 = new Message();
                                        msg28.what =28;
                                        mHandler.sendMessage(msg28);
                                    }
                                } catch (Exception e) {
                                    //weather_text.setText(s1);
                                }
                                yes2 =0;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //嘗試取得當前標籤名稱，若是Data才可以增加到arrayList，並且重置
                        String trytagName = parser.getName();
                        if (trytagName.equals("location")) {
                            tagName = parser.getName();
                            findCount = 0;
                            //parameterName = 0;
                            yes = 0;
                            y=0;
                            hashMap = new HashMap<String, Object>();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //別忘了一定要用next方法處理下一個事件，忘了的結果就成無窮環圈#_#
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {

        } catch (IOException e) {

        }
    }

    public void parse3() throws URISyntaxException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://140.130.35.236/skin/%E7%B4%AB%E5%A4%96%E7%B7%9A");
        try {
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();
            String result = EntityUtils.toString(resEntity);
            JSONArray result5 = new JSONArray(result);
            JSONObject stock_data = result5.getJSONObject(UVIvalue);
            suggestUVI=stock_data.getString("防護措施");
        }catch (Exception e)
        {
        }
    }
    public void color(int c) {
        switch(c) {
            case 0:
                u0.setTextColor(Color.parseColor("#00ff2f"));
                break;
            case 1:
                u1.setTextColor(Color.parseColor("#00ff2f"));
                break;
            case 2:
                u2.setTextColor(Color.parseColor("#1aff00"));
                break;
            case 3:
                u3.setTextColor(Color.parseColor("#5eff00"));
                break;
            case 4:
                u4.setTextColor(Color.parseColor("#a2ff00"));
                break;
            case 5:
                u5.setTextColor(Color.parseColor("#eeff00"));
                break;
            case 6:
                u6.setTextColor(Color.parseColor("#ffd500"));
                break;
            case 7:
                u7.setTextColor(Color.parseColor("#ffa600"));
                break;
            case 8:
                u8.setTextColor(Color.parseColor("#ff6f00"));
                break;
            case 9:
                u9.setTextColor(Color.parseColor("#ff5100"));
                break;
            case 10:
                u10.setTextColor(Color.parseColor("#ff2b00"));
                break;
            default:
                u11.setTextColor(Color.parseColor("#ff0000"));
                break;
        }
    }

    public void parse4() throws URISyntaxException {
        DefaultHttpClient client = new DefaultHttpClient();
        String result=null;
        HttpGet get = new HttpGet("http://140.130.35.236/skin/%E5%90%AB%E6%B0%B4%E9%87%8F");
        try {
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();
            result = EntityUtils.toString(resEntity);
            JSONArray result5 = new JSONArray(result);
            JSONObject stock_data = result5.getJSONObject(10);
            skinlvstring=(stock_data.getString("水分"));
            skinlv.setText(skinlvstring);
            //sugskin.setText(stock_data.getString("建議"));
        }
        catch (Exception e) {
        }
    }

    private void locationServiceInitial() {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE); //取得系統定位服務
        if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //使用GPS定位座標｛
        }
        if (location == null) {
            if (lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //使用網路定位座標
            }
        }
        getLocation(location);
    }
    private void getLocation(Location location) { //將定位資訊顯示在畫面中
        if(location != null) {
            Double longitude = location.getLongitude();   //取得經度
            Double latitude = location.getLatitude();     //取得緯度

            Geocoder gc = new Geocoder(this) ;//經緯度轉地址
            try {
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
                String allAddress=lstAddress.get(0).getAddressLine(0);//完整地址
                returnAddress= lstAddress.get(0).getAdminArea();//縣市

            }
            catch (Exception e)
            {
            }
        }
        else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {  //當地點改變時
        // TODO 自動產生的方法 Stub
        getLocation(location);
    }
    @Override
    public void onProviderDisabled(String arg0) {//當GPS或網路定位功能關閉時
        // TODO 自動產生的方法 Stub
        Toast.makeText(this, "請開啟gps或3G網路", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProviderEnabled(String arg0) { //當GPS或網路定位功能開啟
        // TODO 自動產生的方法 Stub
    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) { //定位狀態改變
        // TODO 自動產生的方法 Stub
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(getService) {
            lms.requestLocationUpdates(bestProvider, 1000, 1, this);
            //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {// TODO不要開
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
        // TODO Auto-generated method stub
        super.onPause();
        if(getService) {
            lms.removeUpdates(this);   //離開頁面時停止更新
        }
        try {
            t1 = false;
            Thread1.interrupt();
            Thread1 = null;
        }catch (Exception e){}
        try {
            t2 = false;
            Thread2.interrupt();
            Thread2 = null;
        }catch (Exception e){}
        try {
            t3 = false;
            Thread3.interrupt();
            Thread3 = null;
        }catch (Exception e){}
        try {
            t4 = false;
            Thread4.interrupt();
            Thread4 = null;
        }catch (Exception e){}
        try {
            t5 = false;
            Thread5.interrupt();
            Thread5 = null;
        }catch (Exception e){}

        try {
            btSocket.close();
        } catch (IOException e2) {
        }
        save();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            t1 = false;
            Thread1.interrupt();
            Thread1 = null;
        }catch (Exception e){}
        try {
            t2 = false;
            Thread2.interrupt();
            Thread2 = null;
        }catch (Exception e){}
        try {
            t3 = false;
            Thread3.interrupt();
            Thread3 = null;
        }catch (Exception e){}
        try {
            t4 = false;
            Thread4.interrupt();
            Thread4 = null;
        }catch (Exception e){}
        try {
            t5 = false;
            Thread5.interrupt();
            Thread5 = null;
        }catch (Exception e){}
        try {
            btSocket.close();
        } catch (IOException e2) {
        }
        try {
            btSocket.close();
        } catch (IOException e2) {
        }
        //android.os.Process.killProcess(android.os.Process.myPid());
    }
}
