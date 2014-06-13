package com.flashy.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.flashy.model.Card;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + CardEntry.TABLE_NAME + " (" + CardEntry.COLUMN_NAME_CARD_ID
            + " INTEGER PRIMARY KEY," + CardEntry.COLUMN_NAME_WORD + TEXT_TYPE
            + COMMA_SEP + CardEntry.COLUMN_NAME_MEANING + TEXT_TYPE + COMMA_SEP
            + CardEntry.COLUMN_NAME_DATE + " DATE" + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + CardEntry.TABLE_NAME;

    private Context context;
    private CardDBHelper mDbHelper;
    private SQLiteDatabase db;

    public DBHelper() {
        super();

    }

    public DBHelper(Context context) {
        this.context = context;
        mDbHelper = new CardDBHelper(context);
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public void open() throws SQLException {

        db = mDbHelper.getWritableDatabase();

    }

    public void updateCard(long card_id, String word, String mean) {
        db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CardEntry.COLUMN_NAME_WORD, word);
        cv.put(CardEntry.COLUMN_NAME_MEANING, mean);
        String[] args = { String.valueOf(card_id) };

        db.update(CardEntry.TABLE_NAME, cv, "_id=?", args);

    }

    public long insertCard(String word, String mean) {
        db = mDbHelper.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        Cursor check = getAllByDefault();
        check.moveToFirst();
        if (check != null && check.getCount() > 0) {
            for (int i = 0; i < check.getCount(); i++)
                if (check.getString(1).equals(word)) {
                    // already exist
                }
            //Log.d("1 col", check.getString(1));
            //Log.d("CUrsor", Integer.toString(check.getCount()));
        }
        ContentValues cv = new ContentValues();
        cv.put(CardEntry.COLUMN_NAME_WORD, word.substring(0, 1).toUpperCase()
                + word.substring(1)); // first letter to Capital
        cv.put(CardEntry.COLUMN_NAME_MEANING, mean);

        //cv.put(CardEntry.COLUMN_NAME_DATE, dateFormat.format(date));
        cv.put(CardEntry.COLUMN_NAME_DATE, "2014-06-06 04:23:33");

        return db.insert(CardEntry.TABLE_NAME, null, cv);
    }

    public void deleteCard(long id) {

        String[] args = { String.valueOf(id) };
        //Log.d("card id delete", String.valueOf(id));
        db.delete(CardEntry.TABLE_NAME, "_id=?", args);
    }

    public Card getSingleCard(long id) {
        db = mDbHelper.getReadableDatabase();

        String[] projection = { "*" };

        String[] args = { String.valueOf(id) };

        Cursor c = db.query(CardEntry.TABLE_NAME, projection, "where "
                + CardEntry.COLUMN_NAME_CARD_ID + "=?", args, null, null, null);

        c.moveToFirst();
        String word = c.getString(c.getColumnIndex(CardEntry.COLUMN_NAME_WORD));
        String meaning = c.getString(c
                .getColumnIndex(CardEntry.COLUMN_NAME_MEANING));
        Card result = new Card(id, word, meaning, null);
        return result;
    }

    public Cursor getAllByDefault() {

        db = mDbHelper.getReadableDatabase();

        String[] projection = { CardEntry.COLUMN_NAME_CARD_ID,
                CardEntry.COLUMN_NAME_WORD, CardEntry.COLUMN_NAME_MEANING,
                CardEntry.COLUMN_NAME_DATE };

        Cursor c = db.query(CardEntry.TABLE_NAME, projection, null, null, null,
                null, CardEntry.COLUMN_NAME_DATE + " DESC");

        return c;
    }

    public Cursor getAllByLastWeekCard() {

        db = mDbHelper.getReadableDatabase();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String d = dateFormat.format(cal.getTime());
        //Log.d("DATE", d);

        String[] projection = { CardEntry.COLUMN_NAME_CARD_ID,
                CardEntry.COLUMN_NAME_WORD, CardEntry.COLUMN_NAME_MEANING,
                CardEntry.COLUMN_NAME_DATE };
        String[] args = { dateFormat.format(cal.getTime()) };
        Cursor c = db.query(CardEntry.TABLE_NAME, projection, "date("
                + CardEntry.COLUMN_NAME_DATE + ") >=? ", args, null, null,
                "date(" + CardEntry.COLUMN_NAME_DATE + ") DESC");

        return c;
    }

    public Cursor getAllByAlphabet() {
        db = mDbHelper.getReadableDatabase();

        String[] projection = { CardEntry.COLUMN_NAME_CARD_ID,
                CardEntry.COLUMN_NAME_WORD, CardEntry.COLUMN_NAME_MEANING,
                CardEntry.COLUMN_NAME_DATE };
        String order = CardEntry.COLUMN_NAME_WORD + " COLLATE NOCASE ASC";
        Cursor c = db.query(CardEntry.TABLE_NAME, projection, null, null, null,
                null, order);

        return c;
    }

    public class CardDBHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FlashCard.db";

        public CardDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Log.i("myTag", SQL_CREATE_ENTRIES);
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);

        }
    }

}
