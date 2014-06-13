package com.flashy.app;

import com.example.flashy.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragment extends Fragment implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mTabAdapter;
    private ActionBar actionBar;
    private String[] tabs = { "All", "Last Week" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_viewpager, container,
                false);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mTabAdapter = new TabsPagerAdapter(getActivity()
                .getSupportFragmentManager());
        
        viewPager.setAdapter(mTabAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (savedInstanceState == null) {
            for (String tab_name : tabs) {
                actionBar.addTab(actionBar.newTab().setText(tab_name)
                        .setTabListener(TabFragment.this));

            }
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        return rootView;
    }
    
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction arg1) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

    }
}
