package com.example.and.mysignal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {

    Button map;
    ImageView wifi;
    ImageView cellular;
    ImageView map_button;
    Boolean isWifiConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        wifi = (ImageView) findViewById(R.id.wifi_button);
        cellular = (ImageView)findViewById(R.id.cellular_button);
        map_button = (ImageView)findViewById(R.id.map_button);

        final Context context = this;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConnected = true;
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Context contextDebug = getApplicationContext();
            CharSequence text = "Please Check Connection To WIFI And Cellular!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(contextDebug, text, duration);
            toast.show();
        }

        wifi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isWifiConnected) {
                    Intent intent = new Intent(context, wifiData.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        cellular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, CellularData.class);
                startActivity(intent);
                finish();
            }
        });
        map_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
