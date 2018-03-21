package com.example.bet.blue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.example.bet.blue.R.layout.activity_blue;

public class blue extends Activity  {
    private ImageView img;
    private Button button2,button3,button4,button5;
    int r=0,l=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_blue);
        img = (ImageView) findViewById(R.id.imageView);
        img.setOnTouchListener(imgListener);
        button2=(Button) findViewById(R.id.button2);
        button3=(Button) findViewById(R.id.button3);
        button4=(Button) findViewById(R.id.button4);
        button5=(Button) findViewById(R.id.button5);

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                      img.setY(img.getY()-10);
                }catch (Exception e)
                {}
            }
        });
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    img.setY(img.getY()+10);
                }catch (Exception e)
                {}
            }
        });
        button4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(img.getScaleX()<10 && img.getScaleY()<10) {
                        img.setScaleX(img.getScaleX() + (float)0.5);
                        img.setScaleY(img.getScaleY() + (float)0.5);
                    }
                }catch (Exception e)
                {}
            }
        });
        button5.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(img.getScaleX()>=1 && img.getScaleY()>=1) {
                        img.setScaleX(img.getScaleX() - (float)0.5);
                        img.setScaleY(img.getScaleY() - (float)0.5);
                    }
                }catch (Exception e)
                {}
            }
        });
    }

   private View.OnTouchListener imgListener = new View.OnTouchListener() {
        private float x, y;    // 原本圖片存在的X,Y軸位置
        private int mx, my; // 圖片被拖曳的X ,Y軸距離長度

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
            switch (event.getAction()) {          //判斷觸控的動作

                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY()+50;                  //觸控的Y軸位置  依圖片大小50可改

                case MotionEvent.ACTION_MOVE:// 移動圖片時

                    //getX()：是獲取當前控件(View)的座標

                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                    break;
            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
            return true;
        }
    };


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}