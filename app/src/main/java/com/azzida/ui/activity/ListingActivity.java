package com.azzida.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.azzida.R;
import com.azzida.adapter.SectionsPagerAdapter;
import com.azzida.custom.CustomViewPager;
import com.azzida.ui.fragment.ListingCanceledFragment;
import com.azzida.ui.fragment.ListingCompletedFragment;
import com.azzida.ui.fragment.ListingInProgressFragment;
import com.google.android.material.tabs.TabLayout;

public class ListingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView profile_back;
    TextView title;
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;
    private SectionsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);


        mViewPager = findViewById(R.id.view_pager);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setPagingEnabled(false);

        adapter = new SectionsPagerAdapter(this.getSupportFragmentManager());

        adapter.addFragment(new ListingInProgressFragment(), "IN PROGRESS");

        adapter.addFragment(new ListingCompletedFragment(), "COMPLETED");
        adapter.addFragment(new ListingCanceledFragment(), "CANCELED");

        mViewPager.setAdapter(adapter);

        title = findViewById(R.id.title);

        title.setText("My Listing");

        profile_back = findViewById(R.id.profile_back);


        profile_back.setOnClickListener(this);


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


    public void AddJob(View view) {

        Intent myIntent = new Intent(ListingActivity.this, AddFeedActivity.class);
        startActivity(myIntent);
    }
}
