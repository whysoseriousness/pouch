package com.example.pouchapp;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
public class FeedAdapter extends SimpleAdapter {
		private List<Feed> feeds;
		
		private int[] colors = new int[] {0x30ffffff, 0x30808080};
		
		@SuppressWarnings("unchecked")
		public FeedAdapter(Context context, 
						   List<? extends Map<String, String>> feeds, 
						   int resource, String[] from, int[] to){
			super(context, feeds, resource, from, to);
			this.feeds = (List<Feed>) feeds;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View view = super.getView(position, convertView, parent);
			int colorPos = position % colors.length;
			view.setBackgroundColor(colors[colorPos]);
			return view;
		}
}

