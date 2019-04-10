package com.inuker.bluetooth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPage extends AppCompatActivity {

    private Button ble_bt,chart_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        ble_bt=(Button)findViewById(R.id.ble_bt);
        chart_bt=(Button)findViewById(R.id.chart_bt);

        ble_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPage.this ,MainActivity.class);
                startActivity(intent);
            }

        });
        chart_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPage.this ,LineChartActivity.class);
                startActivity(intent);
            }

        });
    }
}
