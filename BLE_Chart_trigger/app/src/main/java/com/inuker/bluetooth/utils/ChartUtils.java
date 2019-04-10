package com.inuker.bluetooth.utils;

import android.graphics.Color;
import android.graphics.Matrix;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * 图表工具
 * Created by yangle on 2016/11/29.
 */
public class ChartUtils {

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart chart,float zoom_value) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(true);
        // 不可以缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);
        chart.setExtraBottomOffset(10);

        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(-12);

        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setYOffset(-3);
        yAxis.setAxisMinimum(-10);
        yAxis.setAxisMaximum(255);

        Matrix matrix = new Matrix();
        // x轴缩放倍率
        matrix.postScale(zoom_value, 1f);
        // 在图表动画显示之前进行缩放
        chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        //chart.animateX(2000);

        chart.invalidate();
        return chart;
    }

    private static void initLineDataSet(LineDataSet lineDataSet, int color) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        // 设置平滑曲线
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // 不显示坐标点的小圆点
        lineDataSet.setDrawCircles(false);
        // 不显示坐标点的数据
        lineDataSet.setDrawValues(false);
        // 不显示定位线
        lineDataSet.setHighlightEnabled(false);
    }


    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values1 数据
     */
    public static void setChartData1(LineChart chart, List<Entry> values1 , List<Entry> values2, List<Entry> values3) {
        LineDataSet lineDataSet1,lineDataSet2,lineDataSet3;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            /////////////////////////////////////////
            lineDataSet1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet1.setValues(values1);
            /////////////////////////////////////////
            lineDataSet2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            lineDataSet2.setValues(values2);
            /////////////////////////////////////////
            lineDataSet3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            lineDataSet3.setValues(values3);
            /////////////////////////////////////////
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet1 = new LineDataSet(values1,"1");
            lineDataSet2 = new LineDataSet(values2,"2");
            lineDataSet3 = new LineDataSet(values3,"3");
            // 设置曲线颜色
            lineDataSet1.setColor(Color.parseColor("#ff0000"));
            lineDataSet2.setColor(Color.parseColor("#00db00"));
            lineDataSet3.setColor(Color.parseColor("#00caca"));
            // 設置關閉圓點
            lineDataSet1.setDrawCircles(false);
            lineDataSet2.setDrawCircles(false);
            lineDataSet3.setDrawCircles(false);
            // 设置平滑曲线
            lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // 设置線寬
            lineDataSet1.setLineWidth(2);
            lineDataSet2.setLineWidth(2);
            lineDataSet3.setLineWidth(2);

            LineData data = new LineData(lineDataSet1);
            chart.setData(data);
            chart.invalidate();
            chart.getLineData().addDataSet(lineDataSet2);
            chart.getLineData().addDataSet(lineDataSet3);

        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     * @param values1,values2    数据
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values1 , List<Entry> values2,  List<Entry> values3,final int datasize,final int max_ref) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValuesProcess(datasize,max_ref)[(int) value];
            }
        });

        chart.invalidate();
        setChartData1(chart, values1,values2,values3);
    }

    /**
     * x轴数据处理
     *
     * @return x轴数据
     */
    private static String[] xValuesProcess(int datasize,int max_ref) {
        String[] dayValues = new String[50000];

        for (int i = (datasize-1); i >= datasize-max_ref; i--) {
            dayValues[i-(datasize-max_ref)] = Integer.toString(i);
        }
        return dayValues;
    }
}
