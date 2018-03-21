package com.example.bet.blue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.bet.blue.R.layout.activity_blue;

public class blue extends Activity {
    private Button button;
    private EditText a1,a2;
    private TextView t;

    String s1,s2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_blue);
        button=(Button)findViewById(R.id.button);
        a1=(EditText)findViewById(R.id.editText7);
        a2=(EditText)findViewById(R.id.editText8);
        t=(TextView) findViewById(R.id.textView6);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=a1.getText().toString();
                s2=a2.getText().toString();
                if(s1.equals("admin") && s2.equals("admin")) {
                    Intent intent = new Intent();
                    intent.setClass(blue.this, LoginpageActivity.class);
                    startActivity(intent);
                }
                else
                    t.setText("Error!");
            }
        });
    }
}