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
	
	private String user ="test@test.com"; 
	private List<Feed> feeds;
	private static final int POLL_INTERVAL = 1000; //10 minutes
	
	public MemoryService(){
		this("Noname");
	}
	public MemoryService(String name) {
		super(name);
		feeds = new ArrayList<Feed>();
	}
	
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
		File mediaDir = new File(getFilesDir(),FILENAME);
		File time = new File(getFilesDir(),TIMENAME);
		
		//Creates file
		if (!mediaDir.exists()){
		 
			int input = 0;

			FileOutputStream fos;
			
			try {
				
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(Integer.toString(input).getBytes());
				fos.close();
				mediaDir = new File(getFilesDir(),FILENAME);
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
		File time = new File(getFilesDir(),TIMENAME);
		
		//Writes to time
		if(time.exists()){
			
			time.delete();
			time = new File(getFilesDir(),TIMENAME);
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
	
	//total file
	public int getNumFiles(){
				
		String FILENAME = "global";
		File mediaDir = new File(getFilesDir(),FILENAME);
		
		//Reads from file
		if (mediaDir.exists()){
		  
			FileInputStream input;
			
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
		
		File mediaDir = new File(getFilesDir(),FILENAME);
		int input = getNumFiles()+1;
		mediaDir.delete();
		mediaDir = new File(getFilesDir(),FILENAME);
		
		
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
	public List<Feed> getFeeds(Context context){
		return feeds;
	}
	
	// TODO: connect to web service
	public void loadFeeds(Context context){
		Feed f1 = new Feed("Test Title 1", "Author 1", "Preview 1", "Path_1");
		Feed f2 = new Feed("Test Title 2", "Author 2", "Preview 2", "Path_2");
		
		feeds.add(f1);
		feeds.add(f2);
		
		
		String filepath = "Path_1";
		String content = "<p>test content 1</p><p>work</p>";
		
		try {
			FileOutputStream fos = context.openFileOutput(filepath, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		filepath = "Path_2";
		content = "<p>test content 2</p><p>work</p>";
		
		try {
			FileOutputStream fos = context.openFileOutput(filepath, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
		
	/**
	 * Return the html content of an article given its path.
	 * @param path The memory path to the article
	 * @return the html content
	 */
	public static String getContent(Context context, String path){
		FileInputStream in;
		try {
			in = context.openFileInput(path);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			
			String line;
			while ((line = br.readLine()) != null){
				sb.append(line);
			}
			
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "File not found.";
		} catch (IOException e){
			e.printStackTrace();
			return "File could not be read.";
		}
		
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
		File file = new File(getFilesDir(),numCode+"");
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
			file = new File(getFilesDir(),htmlPath);
			
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
				"http://serene-ridge-8390.herokuapp.com/articles?user[email]=" + user);

		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");

		Log.e("Request", request.toString());
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

		//boolean result = createGlobal();

		Log.w("TeamChyr", "Vitchyr Monitor");
		
		//writeToFile(grabFromServer());
		// pull from server
		// write to local memory
	}


	@Override
	public void onCreate(){
		
		super.onCreate();
		createGlobal();
		writeToFile(grabFromServer());
		
		AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.google");
        user = accounts[0].name;
        
		//setServiceAlarm(this, true);
	}
}