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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.Html;

public class MemoryService extends Service {
	public MemoryService() {
	}

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
	
	

}
