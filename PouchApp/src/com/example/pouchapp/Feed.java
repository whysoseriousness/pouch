package com.example.pouchapp;

public class Feed {
	public String title;
	public String source;
	public String preview;
	public String path;
	public static String KEY_TITLE = "title";
	public static String KEY_SOURCE = "source";
	
	public Feed(String title, String source, String preview, String path){
		this.title = title;
		this.source = source;
		this.preview = preview;
		this.path = path;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getSource(){
		return source;
	}
	
	public String getPreview(){
		return preview;
	}
	
	public String getPath(){
		return path;
	}
}
