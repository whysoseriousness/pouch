package com.example.pouchapp;

import java.util.HashMap;

public class Feed extends HashMap<String, String> {
	public String title;
	public String source;
	public static String KEY_TITLE = "title";
	public static String KEY_SOURCE = "source";
	
	public Feed(String title, String source){
		this.title = title;
		this.source = source;
	}
	
	@Override
	public String get(Object k){
		String key = (String) k;
		if (KEY_TITLE.equals(key)){
			return title;
		} else if (KEY_SOURCE.equals(key)) {
			return source;
		}
		return null;
	}
}