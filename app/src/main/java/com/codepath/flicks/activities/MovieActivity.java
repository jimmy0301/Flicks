package com.codepath.flicks.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.flicks.Fragment.MovieFavoriteFragment;
import com.codepath.flicks.R;
import com.codepath.flicks.adapter.SampleFragmentPagerAdapter;

public class MovieActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_movie);

      final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
      final SampleFragmentPagerAdapter sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
      viewPager.setAdapter(sampleFragmentPagerAdapter);

      // Give the PagerSlidingTabStrip the ViewPager
      final PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
      // Attach the view pager to the tab strip
      tabsStrip.setViewPager(viewPager);
      viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         @Override
         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

         }

         @Override
         public void onPageSelected(int position) {

            if (position == 1) {
               MovieFavoriteFragment f = (MovieFavoriteFragment) sampleFragmentPagerAdapter.getItem(position);
               f.update(getApplicationContext());
            }
         }

         @Override
         public void onPageScrollStateChanged(int state) {

         }
      });
   }
}

