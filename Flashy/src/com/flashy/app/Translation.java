package com.flashy.app;

import android.os.AsyncTask;
import android.util.Log;

import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Translation {
    private DBHelper helper;
    private String word;

    public Translation(DBHelper dbHelper, String theWord) {
        this.helper = dbHelper;
        this.word = theWord;
        new TranslateTask().execute(word);
    }

    public String translateText(String text) throws Exception {
        // Set the Client ID / Client Secret once per JVM. It is set statically
        // and applies to all services
        Translate.setClientId("Flashy");
        Translate
                .setClientSecret("lCsQEAZsgQLuoSOkwMeRedPgmSFo5jYz0W3PGnh9d9Y=");

        Language detectedLanguage = Detect.execute(text);
        // String detectedLang = detectedLanguage.getName(Language.ENGLISH);
        return Translate.execute(text, detectedLanguage, Language.THAI);
    }

    class TranslateTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... texts) {
            // TODO Auto-generated method stub
            String meaning;
            try {
                meaning = translateText(texts[0]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                meaning = "";
            }
            return meaning;
        }

        @Override
        protected void onPostExecute(String translatedText) {
            // TODO Auto-generated method stub
            helper.insertCard(word, translatedText);
            // helper.close();
        }
    }
}
