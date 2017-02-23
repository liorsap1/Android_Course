package com.example.and.mysignal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button back_to_menu;
    private Button pinPlace;
    MapDB mapDataBase;
    SQLiteDatabase db;
    String[] projection = {Points.MapPointsLocation._ID,
            Points.MapPointsLocation.Longtitude,
            Points.MapPointsLocation.Altitude,
            Points.MapPointsLocation.Strength,
            Points.MapPointsLocation.TYPE,
    };

    Cursor runner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        back_to_menu = (Button) findViewById(R.id.back_home);
        pinPlace = (Button)findViewById(R.id.pinMap);


        mapDataBase = new MapDB(this);
        db = mapDataBase.getWritableDatabase();

        runner = db.query(Points.MapPointsLocation.TABLE_NAME,null,null,null,null,null,null);

        back_to_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, MainScreen.class));
                finish();
            }
        });
        pinPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pinThePlace();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        Double lontit = location.getLongitude();
        Double altit = location.getLatitude();

        LatLng myLocation = new LatLng(altit, lontit);
        InsrtToDataBse(lontit,altit);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("YOU ARE HERE!"));
        pinAllPoints();
        double zoomLevel = 17.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, (float) zoomLevel));
    }


    public void pinThePlace(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        Double longtit = location.getLongitude();
        Double altit = location.getLatitude();
        LatLng myLocation = new LatLng(altit ,longtit);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Measured!"));

        InsrtToDataBse(longtit,altit);

        Context context = getApplicationContext();
        CharSequence text = "Measured Successfully!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    class GeoPoint {
        int id;
        double x;
        double y;
        String strength ="";
        String type= "";

        public GeoPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public GeoPoint(){}
        public void setLong(Double x){this.x=x;}
        public void setAlt(Double y){this.y=y;}
        public void setid(int y){this.id=y;}
        public void setStrength(String y){this.strength=y;}
        public void setType(String y){this.type=y;}
    }


    public List<GeoPoint> pinAllPoints() {
        List<GeoPoint> contactList = new ArrayList<GeoPoint>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Points.MapPointsLocation.TABLE_NAME;

        db = mapDataBase.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GeoPoint Points = new GeoPoint();
                Points.setid(Integer.parseInt(cursor.getString(0)));
                Points.setLong(Double.parseDouble(cursor.getString(1)));
                Points.setAlt(Double.parseDouble(cursor.getString(2)));
                Points.setStrength(cursor.getString(3));
                Points.setType(cursor.getString(4));
                // Adding contact to list
                contactList.add(Points);
            } while (cursor.moveToNext());
        }
        cursor.close();
        for (int  i=0;i < contactList.size(); i++){
            System.out.println("contactList.get(i).y ,contactList.get(i).x) : "+contactList.get(i).y+" "+contactList.get(i).x);
            LatLng myLocation = new LatLng(contactList.get(i).y ,contactList.get(i).x);
            mMap.addMarker(new MarkerOptions().position(myLocation).title("Measured!"));
        }
        // return contact list
        return contactList;
    }


    public void InsrtToDataBse(Double longtit,Double altit){
        db= mapDataBase.getReadableDatabase();
        String x = ""+longtit;
        String y = ""+altit;
        if (!x.equals("")) {
            ContentValues values =new ContentValues();
            values.put(Points.MapPointsLocation.Longtitude,x);
            values.put(Points.MapPointsLocation.Altitude,y);
            db.insert(Points.MapPointsLocation.TABLE_NAME,null,values);
        }
        ContentValues values2 =new ContentValues();

        values2.put(Points.MapPointsLocation.Longtitude,"35.193710");
        values2.put(Points.MapPointsLocation.Altitude,"31.769159");
        db.insert(Points.MapPointsLocation.TABLE_NAME,null,values2);
        System.out.println("comes here");
    }


    ///Return distance from the point in meter
    private String DistanceFromPoint(double latA, double lngA, double latB, double lngB) {
        Location locationA = new Location("");
        Location locationB = new Location("");
        locationA.setLatitude(latA);
        locationA.setLongitude(lngA); //
        locationB.setLatitude(latB);
        locationB.setLongitude(lngB); //
        float distance = locationA.distanceTo(locationB);
        return ((int) distance + "m From you");
    }
}
