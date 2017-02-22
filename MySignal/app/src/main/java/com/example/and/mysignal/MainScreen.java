package com.example.and.mysignal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainScreen extends AppCompatActivity {

    ImageView wifi;
    ImageView cellular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        wifi = (ImageView) findViewById(R.id.wifi_button);
        cellular = (ImageView)findViewById(R.id.cellular_button);

        final Context context = this;

        wifi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, wifiData.class);
                startActivity(intent);
                finish();
            }
        });
        cellular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, CellularData.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
