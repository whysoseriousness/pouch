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
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public MemoryService service; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new MemoryService();
        
        List<Map<String, String>> planetsList = initFeed();
        ListView lv = (ListView) findViewById(R.id.listView);
        SimpleAdapter simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
        lv.setAdapter(simpleAdpt);
    }
    
    private List<Map<String, String>> initFeed(){
        List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
    	planetsList.add(createPlanet("planet", "Mercury"));
        planetsList.add(createPlanet("planet", "Venus"));
        planetsList.add(createPlanet("planet", "Mars"));
        planetsList.add(createPlanet("planet", "Jupiter"));
        planetsList.add(createPlanet("planet", "Saturn"));
        planetsList.add(createPlanet("planet", "Uranus"));
        planetsList.add(createPlanet("planet", "Neptune"));
        return planetsList;
    }
    
    private HashMap<String, String> createPlanet(String key, String name){
    	HashMap<String, String> planet = new HashMap<String, String>();
    	planet.put(key, name);

    	return planet;
    }
 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void renderHtml(View view){
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	
    	String read;
		StringBuilder message = new StringBuilder("");
    	try{
    		AssetManager am =getAssets();
        	InputStream is = am.open("verge_page_output.html");
    		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
    		while((read = bufferedReader.readLine()) != null){
    			message.append(read);
    			}
    		bufferedReader.close();
    	} catch (Exception e){
    		message.append(getFilesDir());
    	}

    	//String html = "<b>Testing some html</b>";
    	//Html.fromHtml(html).toString()
    	String messageOut = Html.fromHtml(message.toString()).toString();
    	intent.putExtra(EXTRA_MESSAGE, messageOut);
    	startActivity(intent);
    }
    
    
    public static void main(String[] args) throws Exception{
    	
    	throw new Exception();
    }

}
