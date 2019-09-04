package com.example.sensorex02;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SupportMapFragment mapFragment;
    GoogleMap map;
    private CompassView mCV;
    private SensorManager mSM;
    private boolean mCE;
    MarkerOptions myLocationMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback()
        {
            public void onMapReady(GoogleMap googleMap)
            {
                Log.d(TAG, "GoogleMap is ready.");
                map = googleMap;
                //map.setMyLocationEnabled(true);
                //map.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });
        try
        {
            MapsInitializer.initialize(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Button button = (Button)findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                requestCurrentLocation();
            }
        });

        mSM = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        mCV = new CompassView(this);
        mCV.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        ((ViewGroup)mapFragment.getView()).addView(mCV, params);
        mCE = true;
    }


    private void requestCurrentLocation()
    {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try
        {
            long minTime = 10000;
            float minDist = 0;
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDist, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    showCurrentLocation(location);

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(null != lastLocation)
            {
                showCurrentLocation(lastLocation);
            }
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDist, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    showCurrentLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }

    }
    private void showCurrentLocation(Location location)
    {
        LatLng currentPoint = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 15));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        showMyLocationMarker(location);
    }

    private void showMyLocationMarker(Location location)
    {
        if(null == myLocationMarker)
        {
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myLocationMarker.title("현재 나의 위치");
            myLocationMarker.snippet(" : 테크노 밸리");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pill_icon));
            map.addMarker(myLocationMarker);
        }
    }

    protected void onResume()
    {
        super.onResume();
        if(map != null)
        {
            map.setMyLocationEnabled(true);
        }

        if(mCE)
        {
            Sensor os = mSM.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            mSM.registerListener(mL, os, SensorManager.SENSOR_DELAY_UI);
        }
    }
    protected void onPause()
    {
        super.onPause();
        if(map != null)
        {
            map.setMyLocationEnabled(false);
        }

        if(mCE)
        {
            mSM.unregisterListener(mL);
        }
    }

    private final SensorEventListener mL = new SensorEventListener() {

        private int orientationValue = -1;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void onSensorChanged(SensorEvent event) {
            if(orientationValue < 0)
            {
                orientationValue = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            }
            mCV.setAzimuth(90 + orientationValue);
            mCV.invalidate();
        }


    };
}
