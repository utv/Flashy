package com.flashy.app;


import java.util.Date;

import com.example.flashy.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.Card.OnLongCardClickListener;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FlashCard extends Card {

	protected TextView mTitle;
	protected Long id;
	//protected Date date;
    protected String term;
    protected String meaning;
    
    Context context;
    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public FlashCard(Context context) {
        this(context, R.layout.my_card);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public FlashCard(Context context, int innerLayout) {
        super(context, innerLayout);
        //init();
    }

    /**
     * Init
     */
    public void init(){

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.my_term);
        //mTitle = (TextView) parent.findViewById(R.id.my_term);
        if (mTitle != null)
        	mTitle.setText(term);
    }
    
    public void setTerm(String word) {
    	this.term = word;
    }
    
	public String getTerm() {
		return term;
	}
	
	public String getId() {
		return String.valueOf(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}


}
