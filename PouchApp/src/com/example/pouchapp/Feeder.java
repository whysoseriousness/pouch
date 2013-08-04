package com.example.pouchapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fima.cardsui.views.CardUI;
import com.fima.cardsui.objects.CardStack;

public class Feeder extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public MemoryService service;
	public SimpleAdapter simpleAdpt;
	private CardUI mCardView;

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder);
        service = new MemoryService();
        Intent i = new Intent(this, MemoryService.class);
        this.startService(i);
        
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.google");

        ListView lv = (ListView) findViewById(R.id.listView);
        List<Feed> feeds = getFeedOld();
        ListAdapter adapter = new FeedAdapter(this, feeds, android.R.layout.simple_list_item_1,
        									   new String[] {Feed.KEY_SOURCE, Feed.KEY_TITLE},
        									   new int[] {android.R.id.text1, android.R.id.text2});
        lv.setAdapter(adapter);
        
        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
           	 TextView clickedView = (TextView) view;
             renderHtml(clickedView);
            }
       });
    }
    
	*/
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder);
        service = new MemoryService();
        Intent i = new Intent(this, MemoryService.class);
        this.startService(i);
        
        mCardView = (CardUI) findViewById(R.id.cardsview);
        mCardView.setSwipeable(false);
        
        setCardFeed(mCardView);
        /*
        MyCard card = new MyCard("Fuck yeah bitches");
        mCardView.addCard(card);
		MyCard androidViewsCard = new MyCard("www.androidviews.net");
		androidViewsCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
		    	Uri uri = Uri.parse("http://example.com/");
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		    	startActivity(intent);		
			}
		});
		mCardView.addCard(androidViewsCard);
		*/
		
        mCardView.refresh();
        
	}
	
	private void setCardFeed(CardUI mCardView) {
    	List<Feed> feeds = new ArrayList<Feed>();
    	MemoryService ms = new MemoryService();
    	List<String> previews = ms.getAllPreviews();
    	for(int i = 0; i < previews.size(); i++){
    		try{
    			JSONObject json = new JSONObject(previews.get(i));
    			MyCard card = new MyCard(json.get("author").toString());
    			mCardView.addCard(card);
    		} catch(JSONException e) {
    			Log.w("TeamPenis", "Generating previews failed");
    		}	
    	}
	}
	
	/*
    private List<Feed> getFeed() {

    	for(int i = 0; i < previews.size(); i++) {
    		try{
    			JSONObject json = new JSONObject(previews.get(i));
        		feeds.add(new Feed(json.get("author").toString(), json.get("file_path").toString()));
    		} catch(JSONException e) {
    			Log.w("TeamPenis", "Generating previews failed");
    		}
    	}
    	return feeds;
    }*/
    
    private List<Feed> getFeedOld() {
    	List<Feed> feeds = new ArrayList<Feed>();
    	feeds.add(new Feed("Verge", "verge_page_output.html"));
    	feeds.add(new Feed("Verge", "Michelle"));
    	feeds.add(new Feed("Verge", "Android"));
    	feeds.add(new Feed("Verge", "Apple"));
    	feeds.add(new Feed("Verge", "Hackathon"));
    	return feeds;
    }
    
    // We want to create a context Menu when the user long click on an item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        HashMap map =  (HashMap) simpleAdpt.getItem(aInfo.position);

        menu.setHeaderTitle("Options for " + map.get("planet"));
        menu.add(1, 1, 1, "Details");
        menu.add(1, 2, 2, "Delete");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void renderHtml(TextView view){
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	
    	String read;
		StringBuilder message = new StringBuilder("");
    	try{
    		AssetManager am = this.getAssets();
        	InputStream is = am.open(view.getText().toString());
    		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
    		while((read = bufferedReader.readLine()) != null){
    			message.append(read);
    			}
    		bufferedReader.close();
    	} catch (Exception e){
    		message.append(getFilesDir());
    	}

    	String messageOut = Html.fromHtml(message.toString()).toString();
    	intent.putExtra(EXTRA_MESSAGE, messageOut);
    	startActivity(intent);
    }
      
    public void renderYuxiao(TextView view) {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	MemoryService ms = new MemoryService();
    	String messageOut = Html.fromHtml(ms.getHtml(view.getText().toString())).toString();
    	intent.putExtra(EXTRA_MESSAGE, messageOut);
    	startActivity(intent);
    }

    public void renderFromSite(TextView view) {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	startActivity(intent);
    }
    
    public void renderFeed(View view) {
    	Uri uri = Uri.parse("http://example.com/");
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    	startActivity(intent);
    }
}
