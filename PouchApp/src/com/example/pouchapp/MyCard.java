package com.example.pouchapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class MyCard extends Card {
	public MyCard(String title, String desc){
		super(title);
		this.desc = desc;
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);
		
		TextView tv = (TextView) view.findViewById(R.id.title);
		tv.setText(title);
		tv.setTextColor(Color.parseColor("#CC0099"));
		
		TextView tvdesc = (TextView) view.findViewById(R.id.description);
		tvdesc.setText(desc);
		
		return view;
	}
}
