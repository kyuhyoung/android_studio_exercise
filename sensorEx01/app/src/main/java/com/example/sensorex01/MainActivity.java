package com.example.sensorex01;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn01 = (Button) findViewById(R.id.btn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationSVC();
            }
        });
        checkDangerousPermissions();
    }


    private void startLocationSVC() {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        GPSLinstener gpsLinstener = new GPSLinstener();
        long minTime = 10000;
        float minDistance = 0;
        try
        {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsLinstener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsLinstener);
            Location lastLoc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLoc != null)
            {
                Double latitude = lastLoc.getLatitude();
                Double longitude = lastLoc.getLongitude();
                Toast.makeText(getApplicationContext(), "Last known location " + "Latitude : " + latitude + "'\nLongitude : " + longitude, Toast.LENGTH_LONG).show();
            }
        }
        catch (SecurityException ex)
        {
            ex.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "위치 확인이 시작되었습니다.  로그 확인", Toast.LENGTH_SHORT).show();
    }

    private void checkDangerousPermissions()
    {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
                };
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for(int i = 0; i < permissions.length; i++)
        {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if(permissionCheck == PackageManager.PERMISSION_DENIED)
            {
                break;
            }
        }
        if(permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]))
            {
                Toast.makeText(this, "권한 설명 필요함", Toast.LENGTH_LONG).show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 1)
        {
            for(int i = 0; i < permissions.length; i++)
            {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, permissions[i] + "권한이 승인됨", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, permissions[i] + "권한이 승인 되지 않음", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class GPSLinstener implements LocationListener {
        public void onLocationChanged(Location location)
        {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
            Log.i("GPSLinstener", msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}