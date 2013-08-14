package com.example.pouchapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public MemoryService service;
	public SimpleAdapter simpleAdpt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	//service = new MemoryService();
    	//Intent i = new Intent(this, MemoryService.class);
        //this.startService(i);
    }
    
    public void renderFeeder(View view){
    	Intent intent = new Intent(this, Feeder.class);
    	startActivity(intent);
    }

    public static void main(String[] args) throws Exception{
    	throw new Exception();
    }
}
