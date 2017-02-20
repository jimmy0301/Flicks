package com.codepath.flicks.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.codepath.flicks.Fragment.MovieAllFragment;
import com.codepath.flicks.Fragment.MovieFavoriteFragment;

/**
 * Created by keyulun on 2017/2/18.
 */

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
   private String tabTitles[] = new String[] { "All", "Favorite"};

   public SampleFragmentPagerAdapter(FragmentManager fm) {
      super(fm);
   }

   @Override
   public int getCount() {
      return tabTitles.length;
   }

   @Override
   public Fragment getItem(int position) {
      switch (position) {
         case 0:
            return MovieAllFragment.newInstance();
         case 1:
            return MovieFavoriteFragment.newInstance();
         default:
            return null;
      }
   }

   @Override
   public Object instantiateItem(ViewGroup container, int position) {
      return super.instantiateItem(container, position);
   }

   @Override
   public int getItemPosition(Object object) {
      return POSITION_NONE;
   }

   @Override
   public CharSequence getPageTitle(int position) {
      // Generate title based on item position
      return tabTitles[position];
   }
}
