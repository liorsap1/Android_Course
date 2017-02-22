package com.example.and.mysignal;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class wifiData extends AppCompatActivity {

    WifiManager wifi;
    String results[] = new String[20];
    TextView signal;
    TextView signalStrength;
    Button back;
    Button details;
    Boolean clicked = false;


    int lastPoint = 0;

    private final String TAG = getClass().getSimpleName();

    LongOperation runner = new LongOperation();

    GraphView graph;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_data);

        signal = (TextView) findViewById(R.id.title);
        back = (Button) findViewById(R.id.back_home);
        details = (Button) findViewById(R.id.details);
        signalStrength = (TextView) findViewById(R.id.signal);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();

        final Context context = this;

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(true);

        details.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                runner.execute("infoButton");
                clicked = !clicked;
                runIndices();
            }
        });
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainScreen.class);
                startActivity(intent);
                clicked = false;
                finish();
            }
        });
    }

    public int getWifiStrength() {
        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            int rssi = wifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(rssi, 100);
            return (int) (level);
        } catch (Exception e) {
            return -1;
        }
    }

    public void runIndices() {
        final Handler ha = new Handler();
        final int WAIT_TIME = 1000;
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!clicked) {
                    ha.removeCallbacksAndMessages(null);
                } else {
                    int force = getWifiStrength();
                    if (force != -1) {
                        signalStrength.setText("the strength is: "+force+ "%\n");
                        series.appendData(new DataPoint(lastPoint,force), true, 100);
                        graph.addSeries(series);
                        lastPoint++;
                    }
                }
                ha.postDelayed(this, WAIT_TIME);
            }
        }, WAIT_TIME);
    }

    private class LongOperation extends AsyncTask<String, String, String> {

        String send;
        int numberSend;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... params) {
            try {
                String data = "SSID: " + wifi.getConnectionInfo().getSSID() + "\n" +
                        "MAC Addresses: " + wifi.getConnectionInfo().getMacAddress() + "\n" +
                        // "IP Addresses: " + wifi.getConnectionInfo().getIpAddress() + "\n" +
                          "IP Addresses: "+     Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress())+"\n"+
                        "Link Speed: " + wifi.getConnectionInfo().getLinkSpeed() + "\n" +
                        "Router MAC: " + wifi.getConnectionInfo().getBSSID() + "\n" +
                        "Frequency: " + wifi.getConnectionInfo().getFrequency() + "\n";
                return data;
            } catch (Exception e) {
                Log.d(TAG, "doInBackground problem");
                return null;
            }
        }


        @Override
        protected void onPostExecute(String last) {
            try {
                signal.setText(last);
            } catch (Exception e) {
                Log.d(TAG, "onPostExecute problem");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... middle) {
        }
    }
}
