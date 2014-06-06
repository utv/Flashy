package com.flashy.app;

import java.util.ArrayList;

import com.example.flashy.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnLongCardClickListener;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	//private Button add;
	private static DBHelper helper;
	public SimpleCursorAdapter mAdapter;
	public static CardGridArrayAdapter mCardArrayAdapter;
	public static CardGridView gridView;
	private String[] columns;
	private int[] to;
	public static Cursor cursor;
	static ArrayList<Card> cards;
	private static int count;
	
	public MainActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        if(savedInstanceState == null)
        	getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//call this before oncreate , pass tab option! here
		
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
		/*else if(id == R.id.action_importing){
			
		}*/
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		helper.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		helper = new DBHelper(this);
		cursor = helper.getAllByDefault();/****/
		cards = new ArrayList<Card>();
		mCardArrayAdapter = new CardGridArrayAdapter(this,cards);
		
		  
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
        	FlashCard card = new FlashCard(this);
        	final Long id = cursor.getLong(0);
        	card.setId(id);
        	
        	final String term = cursor.getString(1);
        	//Log.d("ID", String.valueOf(cursor.getInt(0)));
        	card.setTerm(term);
        	
        	final String meaning = cursor.getString(2);
        	//Log.d("meaning", meaning);
        	card.setMeaning(meaning);
        	
        	if(cursor.getString(2).equals("")){
        		card.setBackgroundResourceId(R.drawable.card_background_color1);
        		
        	}
        	else
        		card.setBackgroundResourceId(R.drawable.card_background_color2);
    		
        		
        	cards.add(card);
        }
		mCardArrayAdapter.notifyDataSetChanged();

	}
	public int getCount(){
		return count;
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
			
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.grid_list, container,
					false);
			helper = new DBHelper(rootView.getContext());
			
			
			cursor = helper.getAllByDefault();
			//cursor = helper.getAllByLastWeekCard();/****/
			//cursor = helper.getAllByAlphabet();
			cards = new ArrayList<Card>();
			  
	        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
	        	FlashCard card = new FlashCard(rootView.getContext());
	        	final Long id = cursor.getLong(0);
	        	card.setId(id);
	        	
	        	final String term = cursor.getString(1);
	        	//Log.d("ID", String.valueOf(cursor.getInt(0)));
	        	card.setTerm(term);
	        	
	        	final String meaning = cursor.getString(2);
	        	//Log.d("meaning", meaning);
	        	card.setMeaning(meaning);
	        	card.setOnClickListener(new OnCardClickListener() {

					@Override
					public void onClick(Card card, View view) {
						// TODO Auto-generated method stub
						Intent n = null;
						n = new Intent(getActivity(), ScreenSlideActivity.class);
						// Need - 1 for correct positions of cards.
						n.putExtra("POSITION_KEY", Integer.parseInt(card.getId()) - 1);
						n.putExtra("COUNT", count);
						//n.putExtra("DATA", data);
						startActivity(n);
					}
	        		
	        	});
	        	card.setOnLongClickListener(new OnLongCardClickListener() {
	              
					@Override
					public boolean onLongClick(Card card, View view) {
					
						card.setId(String.valueOf(id));
						//SQLiteCursor data = (SQLiteCursor) mCardArrayAdapter.getItem(card.getId());

						//data.moveToPosition(position);
						
						Toast.makeText(getActivity(),"Clickable card: pos "+card.getId(), Toast.LENGTH_LONG).show();
						Intent intent = new Intent(getActivity(), Form.class);
						
						intent.putExtra(CardEntry.COLUMN_NAME_CARD_ID, id);
						
						intent.putExtra(CardEntry.COLUMN_NAME_WORD, term);
						intent.putExtra(CardEntry.COLUMN_NAME_MEANING, meaning);
						intent.putExtra(CardEntry.MODE, CardEntry.EDIT);
						startActivity(intent);

						return false;
					}
	            });
	        	
	        	//set bg color to differentiate between card with both meaning and term or card with only term
	        	if(cursor.getString(2).equals("")){
	        		card.setBackgroundResourceId(R.drawable.card_background_color1);
	        		
	        	}
	        	else
	        		card.setBackgroundResourceId(R.drawable.card_background_color2);
        		
	        		
	        	cards.add(card);
	        	
	        }
            mCardArrayAdapter = new CardGridArrayAdapter(rootView.getContext(),cards);
            gridView = (CardGridView)rootView.findViewById(R.id.my_grid_list);
            /*gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View v, int position, long id) {
					// TODO Auto-generated method stub
					//card.setId(String.valueOf(id));
					//SQLiteCursor data = (SQLiteCursor) mCardArrayAdapter.getItem(position);
					
					data.moveToPosition(position);
					
					Toast.makeText(getActivity(),"Clickable card: pos "+card.getId(), Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getActivity(), Form.class);
					
					intent.putExtra(CardEntry.COLUMN_NAME_CARD_ID, id);
					
					intent.putExtra(CardEntry.COLUMN_NAME_WORD, term);
					intent.putExtra(CardEntry.COLUMN_NAME_MEANING, meaning);
					intent.putExtra(CardEntry.MODE, CardEntry.EDIT);
					startActivity(intent);
					return true;
				
				}
            	
            });*/
            if (gridView!=null){
                gridView.setAdapter(mCardArrayAdapter);
            }
            
			return rootView;
		}
		
		
	}
	/*
	
	*/
}
