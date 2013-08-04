package com.example.pouchapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;

public class MemoryService extends IntentService {
	
	public MemoryService(){
		
		this("Noname");
	}
	public MemoryService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private static final int POLL_INTERVAL = 1000; //10 minutes
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	//creates global file cache
	public boolean createGlobal(){

		String FILENAME = "global";
		File mediaDir = new File(FILENAME);
		
		//Creates file
		if (!mediaDir.exists()){
		  
			int input = 0;

			FileOutputStream fos;
			
			try {
				
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(Integer.toString(input).getBytes());
				fos.close();
				
			} catch (Exception e) {
				return false;
			}
			
		}
		
		return true;
	}

	//retrieves number of articles
	//0 = no log file, or no main
	//-1 = error
	public int getNumFiles(){
				
		String FILENAME = "global";
		File mediaDir = new File(FILENAME);
		
		//Reads from file
		if (mediaDir.exists()){
		  
			FileInputStream input = null;
			
			try {
				
				 input = openFileInput(FILENAME);
				 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				 return Integer.parseInt(reader.readLine());
				 
			} catch (Exception e) {
				
				return -1;
			}

		}
		
		return 0;
	}
	
	//retrieves all previews 
	public List<String> getAllPreviews(){
		
		List<String> Return = new ArrayList<String>();
		
		int numFiles = getNumFiles();
		
		//grabs file
		if(numFiles != -1){
			
			//loops through all file
			for(int i=0;i<numFiles;i++){
				
				String path = i+"";
				String toAdd = "";
				
				File file = getFile(path);
				
				//if we have said file
				if(file != null){
					
					String line = "";
					try {
						
						BufferedReader reader = new BufferedReader(new FileReader(file));
						
						try {
							
							while( (line = reader.readLine()) != null){
								toAdd+=line;
							}
							
						} catch (IOException e) {
							
							return null;
						}
					} catch (FileNotFoundException e) {
						
						return null;
					}
					
				}
				
				Return.add(toAdd);
			}
		}
		
		return Return;
	}
	
	//Get a file from path
	public File getFile(String path){
		
		File dir = new File(path);
		
		//Reads from file
		if (dir.exists()){
		  
			return dir;
		}
		
		return null;
	}
	
	//starts repeating 
	public static void setServiceAlarm(Context context, boolean on){
		
		Intent i = new Intent(context, MemoryService.class);
		PendingIntent pi = PendingIntent.getService(context, 0,i,0);
		
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		if(on){
			
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
			
		}else{
			
			alarmManager.cancel(pi);
			pi.cancel();
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		boolean result = createGlobal();
		
		
		//pull from server
		//write to local memory
	}

	@Override
	public void onCreate(){
		
		super.onCreate();
		
		setServiceAlarm(this, true);
	}
}