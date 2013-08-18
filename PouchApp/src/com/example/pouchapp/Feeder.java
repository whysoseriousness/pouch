package com.example.pouchapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fima.cardsui.views.CardUI;

public class Feeder extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public MemoryService service;
	public SimpleAdapter simpleAdpt;
	private CardUI mCardView;

	/*
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_feeder); service = new MemoryService();
	 * Intent i = new Intent(this, MemoryService.class); this.startService(i);
	 * 
	 * AccountManager am = AccountManager.get(this); Account[] accounts =
	 * am.getAccountsByType("com.google");
	 * 
	 * ListView lv = (ListView) findViewById(R.id.listView); List<Feed> feeds =
	 * getFeedOld(); ListAdapter adapter = new FeedAdapter(this, feeds,
	 * android.R.layout.simple_list_item_1, new String[] {Feed.KEY_SOURCE,
	 * Feed.KEY_TITLE}, new int[] {android.R.id.text1, android.R.id.text2});
	 * lv.setAdapter(adapter);
	 * 
	 * // React to user clicks on item lv.setOnItemClickListener(new
	 * AdapterView.OnItemClickListener() {
	 * 
	 * public void onItemClick(AdapterView<?> parentAdapter, View view, int
	 * position, long id) { TextView clickedView = (TextView) view;
	 * renderHtml(clickedView); } }); }
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feeder);
		service = new MemoryService();
		// Intent i = new Intent(this, MemoryService.class);
		// this.startService(i);

		Button syncButton = (Button) findViewById(R.id.syncbutton);
		syncButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				service.loadFeeds(v.getContext());
				refreshCards();
			}
		});
		
		refreshCards();
	}
	
	private void refreshCards(){
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);

		setCardFeed(mCardView);
		mCardView.refresh();
	}

	private void setCardFeed(CardUI mCardView) {
		List<Feed> feeds = service.getFeeds(this);
		for (int i = 0; i < feeds.size(); i++) {
			final Feed f = feeds.get(i);
			MyCard card = new MyCard(f.getTitle(), String.format(
					"Author: %s\n%s", f.getSource(), f.getPreview()));
			card.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openArticle(MemoryService.getContent(v.getContext(),
							f.getPath()));
				}
			});

			mCardView.addCard(card);
		}
	}

	// We want to create a context Menu when the user long click on an item
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		HashMap map = (HashMap) simpleAdpt.getItem(aInfo.position);

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

	private void openArticle(String html) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		String messageOut = Html.fromHtml(html).toString();
		intent.putExtra(EXTRA_MESSAGE, messageOut);
		startActivity(intent);
	}
}
