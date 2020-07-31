package com.example.msiqlab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class checkin2 extends AppCompatActivity {

    Button btnGetLastLocation;
    TextView lastlocation_tv;
    private LocationManager locationManager;
    public static final int MY_PERMISSION_ACCESS_COARSE_LOCATION =11;
    String commad;

    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;

    double getLatitude;
    double getLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin);
        btnGetLastLocation = (Button) findViewById(R.id.getlastlocation);

        lastlocation_tv = (TextView) findViewById(R.id.lastlocation);
        commad =LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(checkin2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(checkin2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果沒有授權使用定位就會跳出來這個
            requestLocationPermission(); // 詢問使用者開啟權限
            return;
        }
        locationManager.requestLocationUpdates(commad,1000,0,locationListener);
        Location location = locationManager.getLastKnownLocation(commad);
        if(location != null){
            lastlocation_tv.setText("最後定位位置\n"+"緯度"+location.getLatitude()+"\n"+"經度"+location.getLongitude());
            getLatitude =location.getLatitude();
            getLongitude =location.getLongitude();
        }else{
            lastlocation_tv.setText("定位失敗，請移至網路收訊較好的位置");
        }
        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getLatitude<=25.007924 && getLatitude>=25.006590 && getLongitude<=121.488402 && getLongitude>=121.486828){
                    Toast.makeText(checkin2.this,"成功" ,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(checkin2.this,"失敗" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnGetLastLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent =new Intent(checkin2.this,checkin_cheat_map.class);
                startActivity(intent);
                return true;
            }
        });
    }
    private void requestLocationPermission() {
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);
            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_PERMISSION);
            }
            else {
                // 啟動地圖與定位元件
            }
        }
    }
    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lastlocation_tv.setText("緯度"+location.getLatitude()+"\n"+"經度"+location.getLongitude());
            getLatitude =location.getLatitude();
            getLongitude =location.getLongitude();
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
    };

}