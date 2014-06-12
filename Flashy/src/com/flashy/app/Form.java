package com.flashy.app;

import com.example.flashy.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;

public class Form extends Activity {

    private EditText word;
    private EditText meaning;
    private View view;
    private DBHelper helper;

    int mode = 0;
    long card_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_card);

        word = (EditText) findViewById(R.id.word);
        meaning = (EditText) findViewById(R.id.meaning);
        view = (View) findViewById(R.id.card_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) { // !null--->> edit

            mode = extras.getInt(CardEntry.MODE);

            if (mode == CardEntry.EDIT) {

                String wordString = extras
                        .getString(CardEntry.COLUMN_NAME_WORD);
                String meaningString = extras
                        .getString(CardEntry.COLUMN_NAME_MEANING);
                Log.d("Formedit:", meaningString);
                card_id = extras.getLong(CardEntry.COLUMN_NAME_CARD_ID);
                Log.d("ID", String.valueOf(card_id));
                word.setText(wordString);
                if (meaningString != null)
                    meaning.setText(meaningString);

            }

        }

        helper = new DBHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        getMenuInflater().inflate(R.menu.add_menu, menu);
        /*
         * if (mode == CardEntry.EDIT) { MenuItem item =
         * menu.findItem(R.id.delete); item.setVisible(false); MenuItem item2 =
         * menu.findItem(R.id.add_cancel); item2.setVisible(false); } else{
         * MenuItem item = menu.findItem(R.id.delete); item.setVisible(false);
         * MenuItem item2 = menu.findItem(R.id.add_cancel);
         * item2.setVisible(true);
         * 
         * }
         */
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle extras = getIntent().getExtras();
        card_id = extras.getLong(CardEntry.COLUMN_NAME_CARD_ID);

        switch (item.getItemId()) {
        case R.id.add_ok:
            String wordTxt = word.getEditableText().toString();
            String meanTxt = meaning.getEditableText().toString();
            if (!wordTxt.equals("")) {
                if (mode == CardEntry.EDIT) {
                    helper.updateCard(card_id, wordTxt, meanTxt);
                } else {
                    helper.insertCard(wordTxt, meanTxt);
                }
            } else {
                Log.d("Form:", " Term cannot be null");
                // /pop up
            }

            startActivity(new Intent(Form.this, MainActivity.class));
            break;

        case R.id.add_cancel:
            startActivity(new Intent(Form.this, MainActivity.class));
            break;

        case R.id.delete:
            Bundle extra = getIntent().getExtras();
            long id = extra.getLong(CardEntry.COLUMN_NAME_CARD_ID);
            helper.open();
            helper.deleteCard(id);

            MainActivity.mCardArrayAdapter.notifyDataSetChanged();
            startActivity(new Intent(Form.this, MainActivity.class));

            break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.close();
    }

}
