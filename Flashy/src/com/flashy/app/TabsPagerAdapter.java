package com.flashy.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    public TabsPagerAdapter(
            android.support.v4.app.FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
        case 0:
            // All fragment activity
            return new AllCardsFragment();
        case 1:
            // Last week fragment activity
            return new LastWeekFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // return num of tab
        return 2;
    }
}
