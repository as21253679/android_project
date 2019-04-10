package com.yl.mpandroidchartdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.yl.mpandroidchartdemo.utils.AnimationUtils;
import com.yl.mpandroidchartdemo.utils.ChartUtils;
import com.yl.mpandroidchartdemo.utils.ShowUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineChartActivity extends Activity {

    private LineChart chart;
    private int maxsize=3;
    private int max_ref=500;

    private Chart mchart;

    String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String savePath = fullPath + File.separator + "/" + "dsp_data" + ".txt";
    File file = new File(savePath);
    String line;
    float mStartX = 0, mEndX = 0;
    int chart_view=0;
    int gesture_mode=0;
    float zoom_value=1;
    float oldDist1, oldDist2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        mchart = (Chart) findViewById(R.id.chart);
        mchart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        gesture_mode=1;
                        mStartX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(gesture_mode==1) {
                            mEndX = event.getX();
                            float dy = mEndX - mStartX;

                            if (Math.abs(dy) < 20)
                                break;  //低于20像素的滑动不算
                            if (mStartX < mEndX) {
                                chart_view -= ((int)Math.abs(dy))/(int)zoom_value;
                                if (chart_view < 0)
                                    chart_view = 0;
                            } else if (mStartX > mEndX) {
                                chart_view += ((int)Math.abs(dy))/(int)zoom_value;
                                if (chart_view > maxsize - max_ref)
                                    chart_view = maxsize - max_ref;
                            }
                            mStartX = mEndX;
                            initView();
                        }
                        else if(gesture_mode==2){
                            oldDist2 = spacing(event);//获得两触点距离
                            zoom_value+=(oldDist2-oldDist1)/300;
                            zoom_value=(zoom_value>50)?50:(zoom_value<1)?1:zoom_value;

                            initView();
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN://如果有了第二个接触点，则是缩放
                        oldDist1= spacing(event);//获得两触点距离
                        gesture_mode=2;
                        break;
                    case MotionEvent.ACTION_POINTER_UP://其余的一系列活动的终止
                        gesture_mode = 0;//终止
                        break;
                }
                return true;
            }

        });

        try {
            if (!file.exists()) {
                new AlertDialog.Builder(LineChartActivity.this)
                    .setTitle("警告")//設定視窗標題
                    .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                    .setMessage("檔案不存在")//設定顯示的文字
                    .setPositiveButton("關閉程式",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }).show();
                return;
            }
        }catch (Exception e)
        {}

        readfile();
        initView();
    }

    //两个触点的位置的距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) (Math.sqrt(x * x + y * y));
    }

    private void initView() {
        chart = (LineChart) findViewById(R.id.chart);

        ChartUtils.initChart(chart,zoom_value);
        ChartUtils.notifyDataSetChanged(chart, getData_1(),getData_2(),getData_3(),chart_view+max_ref,max_ref);
    }


    private void readfile()
    {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(savePath));
            line=br.readLine();
            maxsize=line.length()/6;
            br.close();
        } catch(Exception e)
        {}
    }

    private List<Entry> getData_1() {
        List<Entry> values = new ArrayList<>();

        String line_c;
        int value;

        for(int i=chart_view;i<chart_view+max_ref;i++) {
            line_c=line.substring(i*6,i*6+2).trim();
            value = Integer.parseInt(String.valueOf(line_c), 16);
            values.add(new Entry(i-chart_view, value));
        }
        return values;
    }

    private List<Entry> getData_2() {
        List<Entry> values = new ArrayList<>();
        String line_c;
        int value;

        for(int i=chart_view;i<chart_view+max_ref;i++) {
            line_c=line.substring(i*6+2,i*6+2+2).trim();
            value = Integer.parseInt(String.valueOf(line_c), 16);
            values.add(new Entry(i-chart_view, value));
        }
        return values;
    }

    private List<Entry> getData_3() {
        List<Entry> values = new ArrayList<>();
        String line_c;
        int value;

        for(int i=chart_view;i<chart_view+max_ref;i++) {
            line_c=line.substring(i*6+4,i*6+4+2).trim();
            value = Integer.parseInt(String.valueOf(line_c), 16);
            values.add(new Entry(i-chart_view, value));
        }
        return values;
    }
}
