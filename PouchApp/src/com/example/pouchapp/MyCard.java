package com.example.pouchapp;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class MyCard extends Card {
	private OnClickListener mListener;
	public MyCard(String title){
		super(title);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);

		((TextView) view.findViewById(R.id.title)).setText(title);

		
		return view;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		mListener = onClickListener;
	}

	
	
	
}
