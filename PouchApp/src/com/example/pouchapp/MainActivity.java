package com.example.pouchapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public MemoryService service;
	public SimpleAdapter simpleAdpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new MemoryService();
        Intent i = new Intent(this, MemoryService.class);
        this.startService(i);

        ListView lv = (ListView) findViewById(R.id.listView);
        List<Feed> feeds = getFeed();
        ListAdapter adapter = new FeedAdapter(this, feeds, android.R.layout.simple_list_item_1,
        									   new String[] {Feed.KEY_SOURCE, Feed.KEY_TITLE},
        									   new int[] {android.R.id.text1, android.R.id.text2});
        lv.setAdapter(adapter);
        
        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
           	 TextView clickedView = (TextView) view;
             renderFromSite(clickedView);
            }
       });
    }
    
    private List<Map<String, String>> initFeed(){
        List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
    	planetsList.add(createPlanet("planet", "verge_page_output.html"));
        planetsList.add(createPlanet("planet", "verge_page_output.html"));
        planetsList.add(createPlanet("planet", "Mars"));
        planetsList.add(createPlanet("planet", "Jupiter"));
        planetsList.add(createPlanet("planet", "Saturn"));
        planetsList.add(createPlanet("planet", "Uranus"));
        planetsList.add(createPlanet("planet", "Neptune"));
        return planetsList;
    }
    
    private List<Feed> getFeed() {
    	List<Feed> feeds = new ArrayList<Feed>();
    	feeds.add(new Feed("Verge", "Obama"));
    	feeds.add(new Feed("Verge", "Michelle"));
    	feeds.add(new Feed("Verge", "Android"));
    	feeds.add(new Feed("Verge", "Apple"));
    	feeds.add(new Feed("Verge", "Hackathon"));
    	return feeds;
    }
    
    private HashMap<String, String> createPlanet(String key, String name){
    	HashMap<String, String> planet = new HashMap<String, String>();
    	planet.put(key, name);

    	return planet;
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
        	InputStream is = am.open("verge_page_output.html");
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
    
    public void renderFromSite(TextView view) {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	startActivity(intent);
    }
    
    public void renderFeed(View view) {
    	Uri uri = Uri.parse("http://example.com/");
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    	startActivity(intent);
    }
    
    public static void main(String[] args) throws Exception{
    	
    	throw new Exception();
    }
}
