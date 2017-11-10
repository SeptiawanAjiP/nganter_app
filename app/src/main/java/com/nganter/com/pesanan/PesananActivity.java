package com.nganter.com.pesanan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import com.nganter.com.R;

/**
 * Created by aji on 11/10/2017.
 */

public class PesananActivity extends AppCompatActivity {

    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.add(new DalamProsesFragment(), "Dalam Proses");
        pagerAdapter.add(new SelesaiFragment(), "Selesai");
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
