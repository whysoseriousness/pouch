package com.example.pouchapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public MemoryService service; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new MemoryService();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void sendMessage(View view){
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.edit_message);
    	//String message = editText.getText().toString();
    	
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
