package com.flashy.app;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.Card.OnLongCardClickListener;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.util.ArrayList;

import com.example.flashy.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AllCardsFragment extends Fragment {

    private static DBHelper helper;
    private static Cursor cursor;
    private static ArrayList<Card> cards;
    protected static CardGridArrayAdapter mCardArrayAdapter;
    private static CardGridView gridView;
    private static View theRootView;

    public AllCardsFragment() {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_list_all, container, false);
          return setupFragmentUI(rootView); 
        /*theRootView = inflater.inflate(R.layout.grid_list_all, container,false);
        return setupFragmentUI(theRootView);*/
    }
    
    @Override
    public void onPause() {
        //Log.i("AllCardsFragment", "AllCardsFragment::onPause");
        super.onPause();
        helper.close();
    }
    
    @Override
    public void onResume() {
        //Log.i("AllCardsFragment", "AllCardsFragment::onResume");
        super.onResume();
        
        //setupFragmentUI(theRootView);
    }
    
    private View setupFragmentUI(View rootView) {
        helper = new DBHelper(getActivity());
        cursor = helper.getAllByDefault();
        
        cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardGridArrayAdapter(rootView.getContext(),
                cards);
        gridView = (CardGridView) rootView.findViewById(R.id.my_grid_list_all);

        if (gridView != null) {
            gridView.setAdapter(mCardArrayAdapter);
        }
        
        int position = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            FlashCard card = new FlashCard(rootView.getContext());
            final Long id = cursor.getLong(0);
            card.setId(id);

            final String term = cursor.getString(1);
            card.setTerm(term);

            final String meaning = cursor.getString(2);
            card.setMeaning(meaning);

            final int cardPosition = position;
            card.setOnClickListener(new OnCardClickListener() {

                @Override
                public void onClick(Card card, View view) {
                    // TODO Auto-generated method stub
                    Intent n = null;
                    n = new Intent(getActivity(), ScreenSlideActivity.class);
                    // Need - 1 for correct positions of cards.
                    // n.putExtra("POSITION_KEY",
                    // Integer.parseInt(card.getId()) - 1);
                    n.putExtra("POSITION_KEY", cardPosition);
                    // n.putExtra("DATA", data);
                    startActivity(n);
                }

            });
            card.setOnLongClickListener(new OnLongCardClickListener() {

                @Override
                public boolean onLongClick(Card card, View view) {

                    card.setId(String.valueOf(id));
                    Intent intent = new Intent(getActivity(), Form.class);
                    intent.putExtra(CardEntry.COLUMN_NAME_CARD_ID, id);
                    intent.putExtra(CardEntry.COLUMN_NAME_WORD, term);
                    intent.putExtra(CardEntry.COLUMN_NAME_MEANING, meaning);
                    intent.putExtra(CardEntry.MODE, CardEntry.EDIT);
                    startActivity(intent);

                    return false;
                }
            });

            if (cursor.getString(2).equals("")) {
                card.setBackgroundResourceId(R.drawable.card_background_color1);

            } else
                card.setBackgroundResourceId(R.drawable.card_background_color2);

            cards.add(card);
            position++;
        }

        /*mCardArrayAdapter = new CardGridArrayAdapter(rootView.getContext(),
                cards);
        gridView = (CardGridView) rootView.findViewById(R.id.my_grid_list_all);

        if (gridView != null) {
            gridView.setAdapter(mCardArrayAdapter);
        }*/
        mCardArrayAdapter.notifyDataSetChanged();
        //this.theRootView = rootView;
        return rootView;
    }

}