package com.flashy.app;

import java.util.ArrayList;

import com.example.flashy.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnLongCardClickListener;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ActionBarActivity {

    // private Button add;
    private static DBHelper helper;
    public SimpleCursorAdapter mAdapter;
    public static CardGridArrayAdapter mCardArrayAdapter;
    public static CardGridView gridView;
    
    // private String[] columns;
    // private int[] to;
    public static Cursor cursor;
    private static ArrayList<Card> cards;
    private static Translation translator;
    
    // clipboard service and its listener
    private static ClipboardManager clipboard;
    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            performClipboardCheck();
        }

        private void performClipboardCheck() {
            if (clipboard.hasPrimaryClip()) {
                ClipData cd = clipboard.getPrimaryClip();
                if (cd.getDescription().hasMimeType(
                        ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.i("performClipboardCheck",
                            "item count = " + cd.getItemCount());
                    Log.i("performClipboardCheck",
                            "item = "
                                    + cd.getItemAt(0).coerceToText(
                                            getApplicationContext()));

                    String word = cd.getItemAt(0)
                            .coerceToText(getApplicationContext()).toString();
                    try {
                        // Translate a collected word and write to db.
                        // new Translation(new DBHelper(getApplicationContext()), word);
                        if(translator != null) {
                            translator.doTranslate(word);
                            AllCardsFragment.mCardArrayAdapter.notifyDataSetChanged();
                            LastWeekFragment.mCardArrayAdapter.notifyDataSetChanged();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Log.i("MainActivity::onCreate", "savedInstanceState == null");
            /*
             * getSupportFragmentManager().beginTransaction()
             * .add(R.id.container, new PlaceholderFragment()).commit();
             */
            if(helper == null)
                helper = new DBHelper(this);
            
            if(translator == null && helper != null)
                translator = new Translation(helper);
            
            // Listen to clipboard changes
            if (clipboard == null) {
                Log.i("MainActivity::onCreate", "clipboard == null");
                clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.addPrimaryClipChangedListener(listener);
            }
        }

        //TabFragment tab = new TabFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.container, new TabFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // call this before oncreate , pass tab option! here

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_adding) {
            Intent intent = new Intent(MainActivity.this, Form.class);
            intent.putExtra(CardEntry.MODE, CardEntry.NEW);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //helper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity::", "onResume");
        
    }

}
