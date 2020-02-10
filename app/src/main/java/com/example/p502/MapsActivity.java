package com.example.p502;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    int marker_no;
    double marker_lat;
    double marker_lon;
    String marker_name;
    ArrayList<corona_location> loc = null;
    private GoogleMap mMap;
    LocationManager manager;
    Location locationA,locationB;
    Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String [] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this,permissions,101);

        Intent intent = getIntent();
        loc = (ArrayList<corona_location>) intent.getSerializableExtra("loc");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocation myLocation = new MyLocation();
        long minTime = 7000;
        float minDistance = 0;
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, myLocation);

        LatLng latlng_default = new LatLng(37.5012,127.0395);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng_default,13));

    }

    class MyLocation implements LocationListener {
        double lon;
        double lat;


        // Location 받아오기 서비스를 한번 실행하면 계속 위치 정보를 받아오도록 설정한다. //
        @Override
        public void onLocationChanged(Location location) {

            // 진동 설정 및 반경 만들기 //
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            int radiusM = 1000; // set radius in meters
            int d = 500; // diameter
            Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            Paint p = new Paint();
            p.setColor(Color.GREEN);
            c.drawCircle(d / 2, d / 2, d / 2, p);

            BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

            // 마커 만들기 //

            List<Marker> markerList = new ArrayList<Marker>();
            Marker marker;
            mMap.clear();
            markerList.clear();

            lat = location.getLatitude();
            lon = location.getLongitude();

            double lon_sum = 0;
            double lat_sum = 0;


            locationA = new Location("mypoint");

            locationA.setLatitude(lat);
            locationA.setLongitude(lon);

            locationB = new Location("markerpoint");

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker_red);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);


            for(int i =0;i<loc.size();i++){
                marker_no = loc.get(i).getNo();
                marker_lat = loc.get(i).getLat();
                marker_lon = loc.get(i).getLon();
                marker_name = loc.get(i).getName();

                lat_sum += marker_lat;
                lon_sum += marker_lon;

                locationB.setLatitude(marker_lat);
                locationB.setLongitude(marker_lon);
                float distance = locationA.distanceTo(locationB);

                if (distance <= 1000) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, 255));
                        Toast.makeText(MapsActivity.this, "위험해위험해", Toast.LENGTH_SHORT).show();
                        MediaPlayer player =MediaPlayer.create(getApplicationContext(),R.raw.r);
                        player.start();
                    } else {
                        Log.d("---", "ha");
                        Toast.makeText(MapsActivity.this, "위험해위험해", Toast.LENGTH_SHORT).show();
                        vibrator.vibrate(1000);
                        MediaPlayer player =MediaPlayer.create(getApplicationContext(),R.raw.r);
                        player.start();
                    }
                } else {
                    Log.d("---", "no");
                }

                MarkerOptions myMarkerOptions = new MarkerOptions()
                        .position(new LatLng(marker_lat, marker_lon))
                        .title(marker_no+"."+marker_name)
                        .snippet("거리:"+distance/1000+"km")
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                Log.d("---distance",distance+"");
                marker = mMap.addMarker(myMarkerOptions);
                markerList.add(marker);
                mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(bmD)
                        .position(new LatLng(marker_lat, marker_lon), radiusM * 2, radiusM * 2)
                        .transparency(0.4f));

            }
            double lat_avg = lat_sum/loc.size();
            double lon_avg = lon_sum/loc.size();
            LatLng avg = new LatLng(lat_avg,lon_avg);

            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Log.d("---test",lat+" "+lon);
            LatLng latLng = new LatLng(lat,lon);

            CircleOptions circle200M = new CircleOptions().center(latLng) //원점
                    .radius(200)      //반지름 단위 : m
                    .strokeWidth(1f)  //선너비 0f : 선없음
                    .fillColor(Color.parseColor("#80EFBAC0")); //배경색

            mMap.addCircle(circle200M);


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
    }

}
