package com.flashy.app;

import java.util.ArrayList;

import com.example.flashy.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.NavUtils;
//import android.support.v4.view.GestureDetectorCompat;
//import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class ScreenSlideActivity extends FragmentActivity {
    private float x, y = 0;
    Context context;
    private DBHelper helper;
    // Static variables shared between activity, fragment and adapter.
    private static ArrayList<Boolean> stateList;
    private static Cursor cursor;
    private static int currentPage;

    public ScreenSlideActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing
            // the
            // front of the card to this activity. If there is saved instance
            // state,
            // this fragment will have already been added to the activity.
            currentPage = getIntent().getIntExtra("POSITION_KEY", 0);
            Log.i("ScreenSlideActivity::onCreate", "currentPage = "
                    + currentPage);

            helper = new DBHelper(this);
            // cursor = helper.getAllByLastWeekCard();
            cursor = helper.getAllByDefault();
            stateList = new ArrayList<Boolean>();
            for (int i = 0; i < cursor.getCount(); i++)
                stateList.add(true);

            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CardFragment()).commit();

        } else {
            // mShowingBack = (getFragmentManager().getBackStackEntryCount() >
            // 0);
        }

        // mDetector = new GestureDetector(this, new MyGestureListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewConfiguration vc = ViewConfiguration.get(getApplicationContext());
        int mTouchSlop = vc.getScaledTouchSlop();

        switch (ev.getAction()) {
        /*
         * case MotionEvent.ACTION_MOVE: case MotionEvent.ACTION_CANCEL:
         */
        case MotionEvent.ACTION_DOWN: {
            x = ev.getX();
            y = ev.getY();

        }
        case MotionEvent.ACTION_UP: {
            int historySize = ev.getHistorySize();
            Log.i("ACTION_UP", "historySize = " + historySize);
            /*
             * if(historySize > 0) { float latestY =
             * ev.getHistoricalY(historySize - 1); if(Math.abs(latestY - y) >
             * mTouchSlop) flipCard(); }
             */

            float lastX = ev.getX();
            float lastY = ev.getY();
            float diffY = Math.abs(lastY - y);
            float diffX = Math.abs(lastX - x);
            if (diffY > diffX && diffY > mTouchSlop)
                flipCard();
            // return true;
            break;
        }
        }
        // return false;
        return super.dispatchTouchEvent(ev);
    }

    /*
     * @Override public boolean onTouchEvent(MotionEvent event){
     * this.mDetector.onTouchEvent(event); return super.onTouchEvent(event); }
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void flipCard() {

        Boolean state = (stateList.get(currentPage) == true) ? false : true;
        stateList.set(currentPage, state);

        /*
         * if (mShowingBack) { getFragmentManager().popBackStack(); return; }
         * 
         * // Flip to the back.
         * 
         * mShowingBack = true;
         */

        // Create and commit a new fragment transaction that adds the fragment
        // for the back of
        // the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        getFragmentManager().beginTransaction()

        // Replace the default fragment animations with animator resources
        // representing
        // rotations when switching to the back of the card, as well as animator
        // resources representing rotations when flipping back to the front
        // (e.g. when
        // the system Back button is pressed).
                .setCustomAnimations(R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a
                // fragment
                // representing the next page (indicated by the just-incremented
                // currentPage
                // variable).
                .replace(R.id.container, new CardFragment())

                // Add this transaction to the back stack, allowing users to
                // press Back
                // to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();

        // Defer an invalidation of the options menu (on modern devices, the
        // action bar). This
        // can't be done immediately because the transaction may not yet be
        // committed. Commits
        // are asynchronous in that they are posted to the main thread's message
        // loop.
        /*
         * mHandler.post(new Runnable() {
         * 
         * @Override public void run() { invalidateOptionsMenu(); } });
         */

    }

    public static class ScreenSlidePagerAdapter extends
            FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            Log.i("ScreenSlidePagerAdapter", "constructor");
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("ScreenSlidePagerAdapter::getItem", "position = " + position);
            cursor.moveToPosition(position);
            // Front card
            if (stateList.get(position) == true) {
                String word = cursor.getString(1);
                return ScreenSlidePageFragment.create(word, true);
            } else {
                String meaning = cursor.getString(2);
                return ScreenSlidePageFragment.create(meaning, false);
            }
            // return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return stateList.size();
        }
    }

    /**
     * A fragment representing the front of the card.
     */
    public static class CardFragment extends Fragment {

        private ViewPager mPager;
        public static final String ARG_PAGE = "current_page";
        public static final String ARG_TOTAL_PAGE = "total_page";

        public CardFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_card_front,
                    container, false);
            mPager = (ViewPager) view.findViewById(R.id.pager);
            mPager.setAdapter(new ScreenSlidePagerAdapter(getFragmentManager()));

            Log.i("CardFrontFragment::onCreateView", "currentPage = "
                    + currentPage);
            mPager.setCurrentItem(currentPage);
            mPager.setPageMargin(15);
            // mPager.setPageMarginDrawable(R.color.common_action_bar_splitter);
            mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                    Log.i("onPageSelected", "currentPage = " + currentPage);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // TODO Auto-generated method stub
                    super.onPageScrollStateChanged(state);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset,
                        int positionOffsetPixels) {
                    // TODO Auto-generated method stub
                    super.onPageScrolled(position, positionOffset,
                            positionOffsetPixels);

                }
            });

            return view;
        }
    }
}
