package com.example.sa.magnetic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tx1 = (TextView) findViewById(R.id.textview1);
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 從感測器管理器中獲得全部的感測器列表
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        // 顯示有多少個感測器
        tx1.setText("經檢測該手機有" + allSensors.size() + "個感測器，他們分別是：\n");
        // 顯示每個感測器的具體資訊
		/* s.getName()設備名稱
		 * s.getVersion()設備版號
		 * s.getVendor()供應商
		 */
        for (Sensor s : allSensors) {
            String tempString = "\n" + " 設備名稱：" + s.getName() + "\n" + " 設備版本："
                    + s.getVersion() + "\n" + " 供應商：" + s.getVendor() + "\n";
            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 加速度感測器 accelerometer" + tempString);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 陀螺儀感測器gyroscope" + tempString);
                    break;
                case Sensor.TYPE_LIGHT:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 環境光線感測器light" + tempString);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 電磁場感測器magnetic field" + tempString);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 方向感測器orientation" + tempString);
                    break;
                case Sensor.TYPE_PRESSURE:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 壓力感測器pressure" + tempString);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 距離感測器proximity" + tempString);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    tx1.setText(tx1.getText().toString() + s.getType()
                            + " 溫度感測器temperature" + tempString);
                    break;
                default:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 未知感測器"
                            + tempString);
                    break;
            }
        }
    }
}
