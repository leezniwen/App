package com.example.msiqlab;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

public class qlab_Fragment extends Fragment {
    private ViewPager viewPager;
    public static TabLayout tabLayout;
    FragmentManager fm;
    int lastposition = -1;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qlab, container, false);
        viewPager = view.findViewById(R.id.qlab_vp);
        fm = getFragmentManager();
        tabLayout = view.findViewById(R.id.qlab_tabl);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.lab_button).setText("實驗室"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.device_button_unchick).setText("設備"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cert_button_unchick).setText("認證"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.inquire_button_unchick).setText("查詢"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onTabSelected(TabLayout.Tab tab) {
                //選擇時觸發
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setTint(Color.WHITE);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onTabUnselected(TabLayout.Tab tab) {
                //未選擇時觸發
                tab.getIcon().setTint(0xFFFFcccc);
            }

            public void onTabReselected(TabLayout.Tab tab) {
                //選中之後再次點擊即複選時觸發
            }
        });
        viewPager.setAdapter(new PageAdapter(fm, tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }

    public class PageAdapter extends FragmentStatePagerAdapter {

        private int num;
        private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

        public PageAdapter(FragmentManager fm, int num) {
            super(fm);
            this.num = num;
        }

        public Fragment getItem(int position) {
            return createFragment(position);
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            Fragment fragment = createFragment(position);
            fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }

        public Fragment instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container,
                    position);
            fm.beginTransaction().show(fragment).commitAllowingStateLoss();
            return fragment;
        }

        public int getCount() {
            return num;
        }

        private Fragment createFragment(int pos) {
            Fragment fragment = mFragmentHashMap.get(pos);
            if (fragment == null) {
                switch (pos) {
                    case 0:
                        fragment = new qlab_laboratory_Fragment();
                        lastposition = 0;
                        break;
                    case 1:
                        fragment = new qlab_lab_Fragment();
                        lastposition = 1;
                        break;
                    case 2:
                        fragment = new qlab_certification_Fragment();
                        lastposition = 2;
                        break;
                    case 3:
                        fragment = new qlab_inquire_Fragment();
                        lastposition = 3;
                        break;
                }
                mFragmentHashMap.put(pos, fragment);
            }
            return fragment;
        }

    }
}
