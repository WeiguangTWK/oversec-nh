package io.oversec.one.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import io.oversec.one.R;
import io.oversec.one.iab.IabUtil;

public class AboutActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private boolean mIsIabAvailable;

    public static void show(Context ctx) {
        Intent i = new Intent();
        i.setClass(ctx, AboutActivity.class);
        ctx.startActivity(i);
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager2 mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mIsIabAvailable = IabUtil.getInstance(this).isIabAvailable();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager2) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mSectionsPagerAdapter.getPageTitle(position));
            }
        }).attach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter() {
            super(AboutActivity.this);
        }

        @NonNull
        private Fragment getFragmentForPosition(int position) {
            switch (position) {
                case 0:
                    return new AboutFragment();
                case 1:
                    return new ChangelogFragment();
                case 2:
                    return mIsIabAvailable ? new PurchasesFragment() : new DonationFragment();
            }
            throw new IllegalArgumentException("Unsupported page index: " + position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return getFragmentForPosition(position);
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.about_tab_about);
                case 1:
                    return getString(R.string.about_tab_changelog);
                case 2:
                    return getString(mIsIabAvailable ? R.string.about_tab_purchases : R.string.about_tab_donations);
            }
            throw new IllegalArgumentException("Unsupported page index: " + position);
        }
    }
}
