package com.flashy.app;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Intent;
import android.util.Log;

public class WordCollectService extends IntentService {
	private DBHelper db;
	private String word;
	private String meaning;
	private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener(){
        public void onPrimaryClipChanged() {performClipboardCheck();}

	private void performClipboardCheck() {
		
		if(db == null) db = new DBHelper(getApplicationContext());
		ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
        	ClipData cd = cb.getPrimaryClip();
        	if(cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            	Log.i("performClipboardCheck", "item count = " + cd.getItemCount());
            	Log.i("performClipboardCheck", "item = " + cd.getItemAt(0).coerceToText(getApplicationContext()));
            	
        		word = cd.getItemAt(0).coerceToText(getApplicationContext()).toString();
        		meaning = "";
        		db.insertCard(word, meaning);
        	}
        } 
	}};
	
	public WordCollectService() {
		super("WordCollectService");
		Log.i("WordCollectService", "WordCollectService");
		   
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("WordCollectService", "onCreate");
		db = new DBHelper(this);
		((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
	}
	
	/**	
	   * The IntentService calls this method from the default worker thread with
	   * the intent that started the service. When this method returns, IntentService
	   * stops the service, as appropriate.
	   */
	  @Override
	  protected void onHandleIntent(Intent intent) {
		  Log.i("onHandleIntent", "Listening to clipboard changes....");
		  
	  }
}
