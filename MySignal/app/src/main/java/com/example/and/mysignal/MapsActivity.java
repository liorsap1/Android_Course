package com.example.and.mysignal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
        SQLiteDatabase db = mapDataBase.getWritableDatabase();

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
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(myLocation).title("YOU ARE HERE!"));
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(31.781600, 35.208700))
//                .title("[Usr-Gershon] "+ DistanceFromPoint(31.781600,35.208700,31.780600,35.207700)));

        double zoomLevel = 17.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, (float) zoomLevel));
    }

    public void putPinsOnMap(){
        List itemIds = new ArrayList<>();
        while(runner.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();
    }

    public void pinThePlace(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Measured!"));
        Context context = getApplicationContext();
        CharSequence text = "Measured Successfully!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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







/*
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    public static final String TAG = MapsActivity.class.getSimpleName();

    final long MIN_TIME_FOR_UPDATE = 1000;
    final float MIN_DIS_FOR_UPFATE = 0.01f;
    Button goBack;
    Location location;
    Double locationX = 0.0;
    Double locationY = 0.0;
    LatLng mevaseret;

    LocationManager locationManager;
    String mprovider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        goBack = (Button) findViewById(R.id.back_home);

        final Context context = this;

        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_ASK_PERMISSIONS);
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainScreen.class);
                startActivity(intent);
                finish();
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(mevaseret).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mevaseret));
    }
}
*/
