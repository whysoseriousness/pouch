package com.example.pouchapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;

public class MemoryService extends IntentService {
	
	private String url = "http://www.google.com";
	private String user =""; 
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
	
	//Initiates global file
	//creates global file cache
	public boolean createGlobal(){

		
		String FILENAME = "global";
		String TIMENAME = "time";
		File mediaDir = new File(FILENAME);
		File time = new File(TIMENAME);
		
		//Creates file
		if (!mediaDir.exists()){
		 
			int input = 0;

			FileOutputStream fos;
			
			try {
				
				fos = openFileOutput(FILENAME, Context.MODE_WORLD_WRITEABLE);
				fos.write(Integer.toString(input).getBytes());
				fos.close();
				Log.e("CreateGlobal", mediaDir.exists()+"");
			} catch (Exception e) {
				return false;
			}
			
		}
		
		//Writes to time
		if(!time.exists()){
			
		
			FileOutputStream fos;
			
			try {
				
				fos = openFileOutput(TIMENAME, Context.MODE_WORLD_WRITEABLE);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String date = dateFormat.format(cal.getTime())+"|"+cal.ZONE_OFFSET;
				
				writer.write(date);
				writer.close();
				fos.close();
				
			} catch (Exception e) {
				return false;
			}
		}
		
		return true;
	}

	//updates time table to most recent time
	public void updateTime(){
		
		String TIMENAME = "time";
		File time = new File(TIMENAME);
		
		//Writes to time
		if(time.exists()){
			
			time.delete();
			time = new File(TIMENAME);
		}
		
			FileOutputStream fos;
			
			try {
				
				fos = openFileOutput(TIMENAME, Context.MODE_PRIVATE);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String date = dateFormat.format(cal.getTime())+"|"+cal.ZONE_OFFSET;
				writer.write(date);
				writer.close();
				fos.close();
				
			} catch (Exception e) {
				
			}
		
	}

	//retrives time table
	public String getTime(){
		
		return getStringFromFile("time");
	}
	
	//total file
	public int getNumFiles(){
				
		String FILENAME = "global";
		File mediaDir = new File(FILENAME);
		
		//Reads from file
		if (mediaDir.exists()){
		  
			FileInputStream input = null;
			
			try {
				
				 input = openFileInput(FILENAME);
				 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				 int ret =  Integer.parseInt(reader.readLine());
				 reader.close();
				 return ret;
				 
			} catch (Exception e) {
				
				return -1;
			}

		}
		
		return 0;
	}
	
	//retrieves all previews 
	
	//global files +1
	public int increaseFiles(){
	
		String FILENAME = "global";
		
		File mediaDir = new File(FILENAME);
		int input = getNumFiles()+1;
		mediaDir.delete();
		mediaDir = new File(FILENAME);
		
		
			FileOutputStream fos;
			
			try {
				
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(Integer.toString(input).getBytes());
				fos.close();
				
			} catch (Exception e) {
				return -1;
			}
			
	
		
		return input;
	}
	
	//Get all previews for this user
	public List<String> getAllPreviews(){
		
		List<String> Return = new ArrayList<String>();
		Log.w("DOYOULIKEDICKS","8=====D");
		int numFiles = getNumFiles();
		
		Log.w("adad", getTime()+ "jjjjjjjjjj");
		//grabs file
		if(numFiles != -1){
			
			//loops through all file
			for(int i=0;i<=numFiles;i++){
				
				String path = i+"";
				String toAdd = getStringFromFile(path);
				
				if(toAdd!=null)
					Return.add(getStringFromFile(path));
				
				
			}
		}
		
		return Return;
	}
	
	//Get file on path
	public File getFile(String path){
		
		File dir = new File(path);
		
		//Reads from file
		if (dir.exists()){
		  
			return dir;
		}
		
		return null;
	}
	
	//Converts file path to string
	public String getStringFromFile(String path){
		
		File file = getFile(path);
		String all = "";
		
		//if file exists
		if(file!=null){
			
			String line = "";
			FileInputStream input;
			try {
				
				//opens stream and reader
				input = openFileInput(path);
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				String toAdd = "";
				
				//add all lines
				try {
					while((toAdd = reader.readLine())!=null){
						
						all+=toAdd;
					}
				} catch (IOException e) {
					
					return null;
				}
			} catch (FileNotFoundException e) {
				
				return null;
			}
			
			return all;
		}
		
		return null;
	}
	
	//Grabs all HTML files
	public String getHtml(String path){
		
		File file = getFile(path);
		String toAdd = "";
		
		if(file!=null){
			
			return getStringFromFile(path);
		}
		
		return null;
	}

	//Repeatedly calls this service
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

	
	//HTML part UNTESTED
	//creates a ID - Preview - Html link
	public boolean createFile(String preview, String html){
		
		int numCode = increaseFiles();
		
		//Writer the preview file
		File file = new File(numCode+"");
		try {
			
			file.createNewFile();

			FileOutputStream fos = openFileOutput(numCode+"", Context.MODE_PRIVATE);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
			
			
			writer.write(preview);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			
			return false;
		}
		
		try {
			
			//grabs json name
			JSONObject json = new JSONObject(preview);
			String htmlPath = (String)json.get("file_path");
			
			//creates html file
			file = new File(htmlPath);
			
			try {
				
				file.createNewFile();

				FileOutputStream fos = openFileOutput(htmlPath, Context.MODE_PRIVATE);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
				
				writer.write(html);
				writer.flush();
				writer.close();
				
			} catch (IOException e) {
				
				return false;
			}
		} catch (JSONException e) {
			
			return false;
		}
		return true;
	}

	// Tested
	public String grabFromServer() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime()) + "|" + cal.ZONE_OFFSET;

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"http://serene-ridge-8390.herokuapp.com/articles?user[email]=test@test.com");

		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");

		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String textView = "";
		// Get the response
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line = "";
		try {
			while ((line = rd.readLine()) != null) {
				textView += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return textView;
	}

	// Write data to file
	public void writeToFile(String data) {

		String server = grabFromServer();
		try {

			JSONArray json = new JSONArray(server);

			// runs through to create feeds-array
			for (int i = 0; i < json.length(); i++) {

				JSONArray feeds = json.getJSONObject(i).getJSONArray("feed");

				//
				for (int j = 0; j < feeds.length(); j++) {

					JSONObject feed = feeds.getJSONObject(j);
					String html = feed.getString("page_content");
					feed.remove("page_content");
					String preview = feed.toString();
					createFile(preview, html);
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// What is called during this service
	@Override
	protected void onHandleIntent(Intent intent) {

		boolean result = createGlobal();

		Log.w("TeamDick", "Vitchyr Monitor");
		
		//writeToFile(grabFromServer());
		// pull from server
		// write to local memory
	}

	@Override
	public void onCreate(){
		
		super.onCreate();
		createGlobal();
		
		AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.google");
        user = accounts[0].name;
        
		setServiceAlarm(this, true);
	}
}