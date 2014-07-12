package com.cardgame.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cardgame.R;
import com.cardgame.gameengine.Card;

public class HandAdapter extends BaseAdapter{

	private List<Card> mCards;
	private Context mContext;
	
	public HandAdapter(Context context) {
		super();
		mContext = context;
		mCards = new ArrayList<Card>();
	}
	
	public void addCard(Card card) {
		mCards.add(card);
		notifyDataSetChanged();
	}

	public void addCards(List<Card> cards) {
		for (Card card : cards) {
			mCards.add(card);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mCards.size();
	}

	@Override
	public Object getItem(int position) {
		return mCards.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.cardName = (TextView) convertView.findViewById(R.id.card_name);
			viewHolder.cardCB = (CheckBox) convertView.findViewById(R.id.card_selected);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.cardName.setText(mCards.get(position).toString());
		viewHolder.cardCB.setChecked(false);
		
	    viewHolder.cardCB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (viewHolder.cardCB.isChecked()) {
					mCards.get(position).setSelected(true);
				}
			    else {
			    	mCards.get(position).setSelected(false);
			    }
			}
		});
	    
		return convertView;
	}

	class ViewHolder {
		TextView cardName;
		CheckBox cardCB;
	}
	
}
