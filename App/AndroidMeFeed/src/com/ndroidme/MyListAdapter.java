package com.ndroidme;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<Article> {
	
	private static final int layoutId = R.layout.list_item;
	
	private final Context mContext;
	private final List<Article> mValues;
	private final Set<Integer> mRepository;
	
	public MyListAdapter(Context context, List<Article> objects) {
		super(context, layoutId, objects);
		this.mContext = context;
		this.mValues = objects;
		this.mRepository = ArticlesRepository.sRepository.getRepository();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(layoutId, parent, false);
		
		TextView tvTitle = (TextView)rowView.findViewById(R.id.list_tvTitle);
		ImageView imgPhoto = (ImageView)rowView.findViewById(R.id.list_imgPhoto);
		TextView tvResume = (TextView)rowView.findViewById(R.id.list_tvResume);
		TextView tvComments = (TextView)rowView.findViewById(R.id.list_tvComments);
		TextView tvLikes = (TextView)rowView.findViewById(R.id.list_tvLikes);
		final ProgressBar bar = (ProgressBar)rowView.findViewById(R.id.list_loading);
/*		
		if (mRepository.contains(mValues[position].getId())) {
			imgIcon.setImageResource(FeedConfig.FM_READ_ICON);
		} else {
			imgIcon.setImageResource(FeedConfig.FM_UNREAD_ICON);
		}
		*/
		tvTitle.setText(mValues.get(position).getTitle());
		tvResume.setText(mValues.get(position).getResume());
		tvComments.setText(String.format("%d %s", mValues.get(position).getCountComments(), parent.getResources().getString(R.string.article_comments)));
		tvLikes.setText(String.format("%d %s", mValues.get(position).getCountLikes(), parent.getResources().getString(R.string.article_likes)));
		
		ImageLoader.getInstance().displayImage(mValues.get(position).getPhotoUrl(), imgPhoto, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		    	bar.setVisibility(View.GONE);
		    }
		});

		
		return rowView;
	}
	
}
