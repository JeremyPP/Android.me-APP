package com.ndroidme;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyDrawerAdapter extends ArrayAdapter<DrawerItem> {
	
	private static final int layoutId = R.layout.drawer_list_item;
	
	private final Context mContext;
	private final List<DrawerItem> mValues;

	public MyDrawerAdapter(Context context, List<DrawerItem> objects) {
		super(context, layoutId, objects);
		this.mContext = context;
		this.mValues = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(layoutId, parent, false);

		RelativeLayout layout = (RelativeLayout)rowView.findViewById(R.id.drawer_layout);
		ImageView imgIcon = (ImageView)rowView.findViewById(R.id.drawer_imgIcon);
		TextView tvText = (TextView)rowView.findViewById(R.id.drawer_tvText);
	
		if (mValues.get(position).getIcon() != 0) {
			layout.setBackgroundResource(R.drawable.drawer_button_down);
			imgIcon.setImageResource(mValues.get(position).getIcon());
			tvText.setText(mValues.get(position).getText().toUpperCase());
			//tvText.setTextSize(MainActivity.dpToPx(mContext, 8));
			tvText.setTextColor(parent.getResources().getColor(R.color.drawer_gray));
			//tvText.setTextAppearance(mContext, android.R.attr.textAppearanceListItemSmall);
			layout.setPadding(MainActivity.dpToPx(mContext, 15), MainActivity.dpToPx(mContext, 15), MainActivity.dpToPx(mContext, 10), MainActivity.dpToPx(mContext, 15));
			tvText.setPadding(MainActivity.dpToPx(mContext, 10), 0, 0, 0);
		} else {
			imgIcon.setImageResource(R.drawable.ic_dummy);
			imgIcon.setPadding(0, 0, 0, 0);
			layout.setBackgroundResource(R.drawable.drawer_button);
			tvText.setText(mValues.get(position).getText());
			tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			if(position == 2)
				layout.setPadding(MainActivity.dpToPx(mContext, 10), MainActivity.dpToPx(mContext, 15), MainActivity.dpToPx(mContext, 10), MainActivity.dpToPx(mContext, 30));
			else
				layout.setPadding(MainActivity.dpToPx(mContext, 10), MainActivity.dpToPx(mContext, 15), MainActivity.dpToPx(mContext, 10), MainActivity.dpToPx(mContext, 15));
			//tvText.setTextAppearance(mContext, android.R.attr.textAppearanceListItem);
		}
		
		if (mValues.get(position).isBold()) {
			tvText.setTypeface(null, Typeface.BOLD);
			
		}else{
			tvText.setTextColor(Color.GRAY);
		}
		
		return rowView;
	}
	
}
