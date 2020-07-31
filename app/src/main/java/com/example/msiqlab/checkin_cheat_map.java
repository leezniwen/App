package com.example.msiqlab;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

public class checkin_cheat_map extends FragmentActivity {

    private GoogleMap mMap;
    MarkerOptions markerOptions;
    TextView checkin;
    TextView title_tv;

    double getLatitude =0;
    double getLongitude=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_cheat_map);
        checkin = findViewById(R.id.checkin_but_tv);
        title_tv =findViewById(R.id.title_tv);
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLatitude <= 25.007924 && getLatitude >= 25.006590 && getLongitude <= 121.488402 && getLongitude >= 121.486828) {
                    Toast.makeText(checkin_cheat_map.this, "成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(checkin_cheat_map.this, "失敗", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(onMapReadyCallback);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            // Add a marker in Sydney and move the camera
            final LatLng sydney = new LatLng(25.007257, 121.487615);
            mMap.addMarker(new MarkerOptions().position(sydney).title("微星"));
            getLatitude = 25.007257;
            getLongitude = 121.487615;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
            title_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(sydney).title("微星"));
                    getLatitude = 25.007257;
                    getLongitude = 121.487615;
                    LatLng nkut = new LatLng(getLatitude, getLongitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nkut,18));
                }
            });
            mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
                @Override
                public void onPoiClick(PointOfInterest poi) {
                    mMap.clear();
                    markerOptions = new MarkerOptions()
                            .position(new LatLng(poi.latLng.latitude, poi.latLng.longitude))
                            .title(poi.name);
                    mMap.addMarker(markerOptions);
                    Toast.makeText(getApplicationContext(), "Clicked: " +
                                    poi.name + "\nPlace ID:" + poi.placeId +
                                    "\nLatitude:" + poi.latLng.latitude +
                                    " Longitude:" + poi.latLng.longitude,
                            Toast.LENGTH_SHORT).show();
                    getLatitude = poi.latLng.latitude;
                    getLongitude = poi.latLng.longitude;
                }
            });

        }
    };
}
