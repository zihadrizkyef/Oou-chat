package com.zihadrizkyef.oou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zihadrizkyef.oou.adapter.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class Activity_Main extends AppCompatActivity {
    List<String> titles;
    List<Fragment> fragments;
    private int profileId = 0;
    private ViewPager mViewPager;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String shrPrfName = getString(R.string.shared_pref_name);
        SharedPreferences sharedPreferences = getSharedPreferences(shrPrfName, MODE_PRIVATE);
        profileId = sharedPreferences.getInt("id", -1);

        fragments = Arrays.asList(
                new Fragment_ContactList(),
                new Fragment_ChatRoomList(),
                new Fragment_Setting());
        titles = Arrays.asList(
                "Contact",
                "Chat",
                "Setting"
        );
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (titles.get(position).equals("Contact")) {
                    getMenuInflater().inflate(R.menu.menu_contact, menu);
                } else {
                    menu.clear();
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (titles.get(mViewPager.getCurrentItem()).equals("Contact")) {
            getMenuInflater().inflate(R.menu.menu_contact, menu);
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_friend) {
            startActivity(new Intent(Activity_Main.this, Activity_AddFriend.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
