package com.example.bet.opendata;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView ;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity
{
    ListView listView;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text= (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView1);
        //XML直接網路下載，網路操作一定要在新的執行序
        new Thread(new Runnable()
        {

            @Override
           public void run()
            {
                // TODO 自動產生的方法 Stub
                try
                {
                    //XML讀取完會得到一個ArrayList
                    final ArrayList<HashMap<String, Object>> arrayList = parse();
                    //LISTVIEW是繼承VIEW的，所以相關操作一定要在原來的UI主執行序
                    //可以呼叫ACTIVITY下的runOnUiThread方法便可以
                   runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), arrayList, R.layout.activity_main, new String[]
                                    { "County", "UVI" }, new int[]
                                    { R.id.textView, R.id.textView2 });
                            listView.setAdapter(adapter);
                        }
                    });
                }
                catch (URISyntaxException e)
                {
                    // TODO 自動產生的 catch 區塊
                    //e.printStackTrace();
                    text.setText("y");
                }
            }
        }).start();
    }

    public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse res;
        // client.getParams().setIntParameter("http.socket.timeout", 5000);
        HttpGet httpRequest = new HttpGet(url);
        res = client.execute(httpRequest);
        return res.getEntity().getContent();
    }

    // 解析空氣品質的OPEN DATA返回一個ArrayList集合
    public ArrayList<HashMap<String, Object>> parse() throws URISyntaxException
    {
        String tagName = null;
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        // 記錄出現次數
       int findCount = 0;
        try
        {
            //定義工廠 XmlPullParserFactory
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //定義解析器 XmlPullParser
            XmlPullParser parser = factory.newPullParser();
            //獲取xml輸入數據
            parser.setInput(new InputStreamReader(getUrlData("http://opendata.epa.gov.tw/ws/Data/UV/?format=xml")));
            //開始解析事件
            int eventType = parser.getEventType();
            //處理事件，不碰到文檔結束就一直處理
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                //因為XmlPullParser預先定義了一堆靜態常量，所以這裡可以用switch
                switch (eventType)
                {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //給當前標籤起個名字
                        tagName = parser.getName();
                        //看到感興趣的標籤個計數
                        if (findCount == 0 && tagName.equals("Data"))
                        {
                            findCount++;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (tagName.equals("County") && hashMap.containsKey("County") == false)
                        {
                            String d;
                           d=parser.getText() ;
                            try{
                            //text.setText(d);
                            }
                            catch (Exception e)
                            {
                                text.setText("FF");
                            }
                            hashMap.put("County", parser.getText());
                        }
                        else if (tagName.equals("UVI") && hashMap.containsKey("UVI") == false)
                        {
                            hashMap.put("UVI", parser.getText());
                        }
                        else if (tagName.equals("SiteName") && hashMap.containsKey("SiteName") == false)
                        {
                            hashMap.put("SiteName", parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //嘗試取得當前標籤名稱，若是Data才可以增加到arrayList，並且重置
                        String trytagName = parser.getName();
                        if (trytagName.equals("Data"))
                        {
                            tagName = parser.getName();
                            findCount = 0;
                            arrayList.add(hashMap);
                            hashMap = new HashMap<String, Object>();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //別忘了一定要用next方法處理下一個事件，忘了的結果就成無窮環圈#_#
                eventType = parser.next();
            }
            return arrayList;
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
            text.setText("TT");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            text.setText("K");
        }
       return arrayList;
    }

}
