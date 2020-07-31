package com.example.msiqlab;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    //創建Fragment和Fragment[]存取資料
    private Fragment news;
    private Fragment user;
    private Fragment qlab;
    public Fragment[] fragmentlist;
    private int lastFragment;
    private String user_name = "";
    public  boolean updata = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        initFragment();
        /*FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String m_token = instanceIdResult.getToken();
                Log.d("Token" , m_token);
            }
        }); */

    }

    private void initFragment() {
        navigationView = (BottomNavigationView) findViewById(R.id.bottom);
        navigationView.setItemIconTintList(null);
        //底部菜單
        news = new news_Fragment();
        user = new user_Fragment();
        qlab = new qlab_Fragment();
        fragmentlist = new Fragment[]{news, user, qlab};
        lastFragment = 0;
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //設定首頁為NEWS
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, news)
                .show(news).commit();
        navigationView.setSelectedItemId(R.id.news);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.news:
                    //判斷要跳轉的頁面是否是當前頁面
                    if (lastFragment != 0) {
                        switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    return true;
                case R.id.user:
                    if (lastFragment != 1) {
                        switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    return true;
                case R.id.qlab:
                    if (lastFragment != 2) {
                        switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    return true;
            }
            return false;
        }
    };

    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏前一個Fragment
        transaction.hide(fragmentlist[lastFragment]);
        //判斷transaction中是否加載過index對應的頁面，若沒加載過則加載
        if (fragmentlist[index].isAdded() == false) {
            transaction.add(R.id.frame, fragmentlist[index]);
        }
        transaction.show(fragmentlist[index]).commitAllowingStateLoss();
    }

    private void resetToDefaultIcon() {
        navigationView.getMenu().findItem(R.id.news).setIcon(R.drawable.home_button);
        navigationView.getMenu().findItem(R.id.user).setIcon(R.drawable.user_button);
        navigationView.getMenu().findItem(R.id.qlab).setIcon(R.drawable.q_button);
    }


}





