package com.azzida.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.azzida.R;
import com.azzida.adapter.SectionsPagerAdapter;
import com.azzida.custom.CustomViewPager;
import com.azzida.ui.fragment.MyJobCanceledFragment;
import com.azzida.ui.fragment.MyJobCompletedFragment;
import com.azzida.ui.fragment.MyJobInProgressFragment;
import com.google.android.material.tabs.TabLayout;

public class MyJobActivity extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    ImageView profile_back;
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;
    private SectionsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myjob);


        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setPagingEnabled(false);

        adapter = new SectionsPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new MyJobInProgressFragment(), "IN PROGRESS");
        adapter.addFragment(new MyJobCompletedFragment(), "COMPLETED");
        adapter.addFragment(new MyJobCanceledFragment(), "CANCELED");
        mViewPager.setAdapter(adapter);

        title = findViewById(R.id.title);

        title.setText("My Jobs");

        profile_back = findViewById(R.id.profile_back);


        profile_back.setOnClickListener(this);

/*
        getString(R.string.nav_hotgame)
*/


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (mViewPager.getAdapter() != null) {
                    setTitle(mViewPager.getAdapter().getPageTitle(position));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.profile_back:

                finish();

                break;


        }
    }

}
