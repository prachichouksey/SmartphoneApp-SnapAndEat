package com.example.snapandeat.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.snapandeat.BarFragment;
import com.example.snapandeat.ListViewFragment;
import com.example.snapandeat.MapViewFragment;
import com.example.snapandeat.PieFragment;
import com.example.snapandeat.R;
import com.example.snapandeat.Report;
import com.example.snapandeat.ReportListFragment;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ReportsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.report_tab_1, R.string.report_tab_2, R.string.report_tab_3};
    private final Context mContext;
    ArrayList<Report> reports = new ArrayList<>();
    int totalItems = 0;
    float calories = 0.0f;
    float carbs = 0.0f;
    float fats = 0.0f;
    float proteins = 0.0f;

    public ReportsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = ReportListFragment.newInstance(reports, totalItems, calories, carbs, fats, proteins);
                break;
            case 1:
                fragment = PieFragment.newInstance(calories, carbs, fats, proteins);
                break;
            case 2:
                fragment = BarFragment.newInstance(calories, carbs, fats, proteins);
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
        return 3;
    }

    public void setData(ArrayList<Report> reports, int totalItems, float calories, float carbs, float fats, float proteins) {
        this.reports = reports;
        this.totalItems = totalItems;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
    }

    @Override
    public int getItemPosition(Object object) {

        Fragment oFragment=(Fragment)object;

//        if(oFragment instanceof ListViewFragment)
//            return POSITION_NONE;
//        else
//            return POSITION_UNCHANGED;
        return POSITION_NONE;
    }
}