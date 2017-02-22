package com.example.and.mysignal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class CellularData extends AppCompatActivity {


    TelephonyManager telephone;
   // Button networkType;
    ImageView backHome;
    TextView textType;
    TextView signalStrength;
    Boolean clicked = false;
    int lastPoint = 0;

    GraphView graph;
    LineGraphSeries<DataPoint> series;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellular_data);

       // networkType = (Button) findViewById(R.id.getType);
        backHome = (ImageView) findViewById(R.id.back_button);
        textType = (TextView) findViewById(R.id.typeText);
        //signalStrength = (TextView) findViewById(R.id.signal);
        telephone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        final Context context = this;


        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();


        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-134);
        viewport.setMaxY(-50);
        viewport.setScrollable(true);


        String type = "Generation of phone: " + getNetworkClass(context) + "\n";
        String info = getCellularInfo();
        textType.setText(type + info);
        clicked = !clicked;
        runStrength();


//        networkType.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//            public void onClick(View v) {
//                String type = "Generation of phone: " + getNetworkClass(context) + "\n";
//                String info = getCellularInfo();
//                textType.setText(type + info);
//                clicked = !clicked;
//                runStrength();
//            }
//        });
        backHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainScreen.class);
                clicked = false;
                startActivity(intent);
                finish();
            }
        });
    }


    public static String getNetworkClass(Context mContext) {

        TelephonyManager mTelephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "?? problem with network network";
        }
    }

    // Returns 3G or 4G signal strength
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getSignalStrength() {
        String strength = "";
        List<CellInfo> cellInfos = telephone.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if (cellInfos != null) {
            for (int i = 0; i < cellInfos.size(); i++) {
                if (cellInfos.get(i).isRegistered()) {
                    if (cellInfos.get(i) instanceof CellInfoWcdma) {
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephone.getAllCellInfo().get(0);
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthWcdma.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                        CellInfoGsm cellInfogsm = (CellInfoGsm) telephone.getAllCellInfo().get(0);
                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) telephone.getAllCellInfo().get(0);
                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthLte.getDbm());
                    }
                }
            }
            return Integer.parseInt(strength);
        }
        return 0;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public String getCellularInfo() {
        String information = "";
        information += "Subscriber ID: " + telephone.getDeviceId() + "\n";
        information += "SIM Serial Number: " + telephone.getSimSerialNumber() + "\n";
        if (telephone.getNetworkCountryIso().equals("il")) {
            information += "Network Country ISO Code: " + "Israel" + "\n";
        }else {information += "Network Country ISO Code: " +
                telephone.getNetworkCountryIso() + "\n";}
        information += "SIM Country ISO Code: " + telephone.getSimCountryIso() + "\n";
        information += "Device software version: " + telephone.getDeviceSoftwareVersion() + "\n";
        information += "Voice mail number: " + telephone.getVoiceMailNumber() + "\n";

        information += "SIM State: ";
        int SIMState = telephone.getSimState();

        switch (SIMState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                information += "ABSENT \n"; break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                information += "NETWORK LOCKED \n"; break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                information += "PIN REQUIRED \n"; break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                information += "PUK REQUIRED \n"; break;
            case TelephonyManager.SIM_STATE_READY:
                information += "READY \n"; break;
            default: information += "UNKNOWN \n";
        }


        //information += "Network strength: " + getSignalStrength() + " dBm\n";
        return information;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void runStrength() {
        final Handler ha = new Handler();
        final int WAIT_TIME = 1000;
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!clicked) {
                    ha.removeCallbacksAndMessages(null);
                } else {
                    int force = getSignalStrength();
                    if (force != -1) {
                        series.appendData(new DataPoint(lastPoint, force), true, 84);
                        graph.addSeries(series);
                        lastPoint++;
                    }
                }
                ha.postDelayed(this, WAIT_TIME);
            }
        }, WAIT_TIME);
    }


}
