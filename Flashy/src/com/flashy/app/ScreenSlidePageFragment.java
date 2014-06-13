package com.flashy.app;

import com.example.flashy.R;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_TXT = "display_text";
    public static final String ARG_IS_FRONT = "is_front_card";
    private static final int FRONT_COLOR = Color.GRAY;
    private static final int BACK_COLOR = Color.CYAN;
    private String mText;
    private boolean mIsFront;

    public static ScreenSlidePageFragment create(String txt, boolean isFront) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TXT, txt);
        args.putBoolean(ARG_IS_FRONT, isFront);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mText = getArguments().getString(ARG_TXT);
        mIsFront = getArguments().getBoolean(ARG_IS_FRONT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i("ScreenSlidePageFragment::onCreateView", "...........");
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        Log.i("ScreenSlidePageFragment::onCreateView", "mText = " + mText);
        // Set the title view to show the page number.
        TextView text = ((TextView) rootView.findViewById(R.id.text1));
        text.setText(mText);
        text.setTextColor(Color.WHITE);
        text.setTypeface(null, Typeface.BOLD);
        //((TextView) rootView.findViewById(R.id.text1)).setText(mText);
        // Set background color
        if (mIsFront)
            rootView.setBackgroundResource(R.drawable.front_card);
        else
            rootView.setBackgroundResource(R.drawable.back_card);

        Log.i("ScreenSlidePageFragment::onCreateView", "mText = " + mText);
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    /*
     * public int getPageNumber() { return mPageNumber; }
     */
}
