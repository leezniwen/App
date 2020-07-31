package com.example.msiqlab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class welcome extends AppCompatActivity {
    private Context Context;
    public static boolean isForeground = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Context = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                UserDB UserDB = new UserDB(welcome.this);
                // 如果進來程式有資料的話就不用再登入
                if (UserDB.getCount() > 0) {
                    UserData UserData = new UserData();
                    UserData = UserDB.getAll().get(0);
                    Intent intent = new Intent();
                    intent.setClass(welcome.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(welcome.this, login_main.class);
                    startActivity(intent);
                    welcome.this.finish();
                }
            }
        }, 2000);//两秒后跳转到另一个页面

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
}
