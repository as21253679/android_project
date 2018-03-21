package com.example.sa.app_stt_nodialog;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecognitionListener {
    private Button btnStart,tcpbutton;
    public TextView textView1;
    private SpeechRecognizer recognizer;
    String s="";

    private Thread thread;                //執行緒
    private Socket clientSocket;        //客戶端的socket
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存
    private JSONObject json_write,json_read;        //從java伺服器傳遞與接收資料的json
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) this.findViewById(R.id.button1);
        tcpbutton=(Button) this.findViewById(R.id.tcpbutton);
        textView1 = (TextView) this.findViewById(R.id.textView1);

        recognizer = SpeechRecognizer.createSpeechRecognizer(this);

        recognizer.setRecognitionListener(new MainActivity());

        //按 Button 時，呼叫 SpeechRecognizer 的 startListening()
        //Intent 為傳遞給 SpeechRecognizer 的參數
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("請開始說話");
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                recognizer.startListening(intent);
            }
        });
        thread=new Thread(Connection);                //賦予執行緒工作
        thread.start();                    //讓執行緒開始執行
    }

    private Runnable Connection=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try{
                // IP為Server端
                InetAddress serverIp = InetAddress.getByName("192.168.137.1");
                int serverPort = 8888;
                clientSocket = new Socket(serverIp, serverPort);
                //取得網路輸出串流
                bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
                // 取得網路輸入串流
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // 當連線後
                bw.write("d");
                while (clientSocket.isConnected()) {
                    // 取得網路訊息
                    tmp = br.readLine();    //宣告一個緩衝,從br串流讀取值
                    Log.d("RECOGNIZER", "onResults: " + tmp);
                }
            }catch(Exception e){
                //當斷線時會跳到catch,可以在這裡寫上斷開連線後的處理
                e.printStackTrace();
                Log.e("text","Socket連線="+e.toString());
                finish();    //當斷線時自動關閉房間
            }
        }
    };
    @Override
    public void onResults(Bundle results) {
        List resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        s=resList.toString();
        Log.d("RECOGNIZER", "onResults: " + s);

    }

    @Override
    public void onError(int error) {
        Log.d("RECOGNIZER", "Error Code: " + error);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
    @Override
    protected void onDestroy() {
        recognizer.stopListening();
        recognizer.destroy();
        super.onDestroy();

        try {
            json_write=new JSONObject();
            json_write.put("action","離線");    //傳送離線動作給伺服器
            Log.i("text","onDestroy()="+json_write+"\n");
            //寫入後送出
            bw.write(json_write+"\n");
            bw.flush();
            //關閉輸出入串流後,關閉Socket
            //最近在小作品有發現close()這3個時,導致while (clientSocket.isConnected())這個迴圈內的區域錯誤
            //會跳出java.net.SocketException:Socket is closed錯誤,讓catch內的處理再重複執行,如有同樣問題的可以將下面這3行註解掉
            bw.close();
            br.close();
            clientSocket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("text","onDestroy()="+e.toString());
        }
    }
}
