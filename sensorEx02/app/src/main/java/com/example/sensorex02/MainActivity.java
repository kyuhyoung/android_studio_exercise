package com.example.sensorex02;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    }
    protected void onPause()
    {
        super.onPause();
        if(map != null)
        {
            map.setMyLocationEnabled(false);
        }
    }
}
