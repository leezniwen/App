package com.example.msiqlab;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class checkin extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button btnGetLastLocation;
    TextView textLastLocation;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;

    private boolean getService = false;     //是否已開啟定位服務

    private LocationManager status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin);
        btnGetLastLocation =  findViewById(R.id.getlastlocation);
        btnGetLastLocation.setOnClickListener(btnGetLastLocationOnClickListener);
        btnGetLastLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent =new Intent(checkin.this,checkin_cheat_map.class);
                startActivity(intent);
                return true;
            }
        });
        textLastLocation =  findViewById(R.id.lastlocation);

        status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果沒有授權使用定位就會跳出來這個
            requestLocationPermission(); // 詢問使用者開啟權限
            return;
        }
    }

    private void requestLocationPermission() {
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    android.Manifest.permission.ACCESS_FINE_LOCATION);

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

    View.OnClickListener btnGetLastLocationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) && status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
                if (mGoogleApiClient != null) {
                    if (mGoogleApiClient.isConnected()) {
                        getMyLocation();
                    } else {
                        Toast.makeText(checkin.this,
                                "!mGoogleApiClient.isConnected()", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(checkin.this,
                            "mGoogleApiClient == null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(checkin.this, "請開啟定位", Toast.LENGTH_SHORT).show();
                getService = true; //確認開啟定位服務
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //開啟設定頁面
            }
        }
    };

    private void getMyLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                String latitude = String.valueOf(mLastLocation.getLatitude());
                String longitude =String.valueOf(mLastLocation.getLongitude());
                double getlatitude =Double.parseDouble(latitude);
                double getlongitude =Double.parseDouble(longitude);
                if(getlatitude<=25.007924 && getlatitude>=25.006590 && getlongitude<=121.488402 && getlongitude>=121.486828){
                    textLastLocation.setText("最後定位位置\n" +latitude+ "\n"+longitude+"\n成功" );
                    Toast.makeText(checkin.this,
                            "成功\n" +latitude+ "\n"+longitude ,
                            Toast.LENGTH_SHORT).show();
                }else {
                    textLastLocation.setText("最後定位位置\n" +latitude+ "\n"+longitude+"\n失敗" );
                    Toast.makeText(checkin.this,
                            "失敗\n" +latitude+ "\n"+longitude ,
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(checkin.this,
                        "mLastLocation == null",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(checkin.this,
                    "SecurityException:\n" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(checkin.this,
                "onConnectionSuspended: " + String.valueOf(i),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(checkin.this,
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_SHORT).show();
    }
}