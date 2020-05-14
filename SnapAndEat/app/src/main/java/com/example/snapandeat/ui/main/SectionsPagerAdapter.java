package com.example.snapandeat.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.snapandeat.ListViewFragment;
import com.example.snapandeat.MapViewFragment;
import com.example.snapandeat.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private String jsonPlaces;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String jsonPlaces) {
        super(fm);
        mContext = context;
        this.jsonPlaces = jsonPlaces;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MapViewFragment();
                break;
            case 1:
                fragment = ListViewFragment.newInstance(jsonPlaces);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    public void setData(String jsonPlaces) {
        this.jsonPlaces = jsonPlaces;
    }

    @Override
    public int getItemPosition(Object object) {

        Fragment oFragment=(Fragment)object;

        if(oFragment instanceof ListViewFragment)
            return POSITION_NONE;
        else
            return POSITION_UNCHANGED;
    }
}