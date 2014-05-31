package com.ndroidme;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

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
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		int id;
	    ImageView bmImage;
	    ProgressBar bar;

	    public DownloadImageTask(int id, ImageView bmImage, ProgressBar bar) {
	    	this.id = id;
	        this.bmImage = bmImage;
	        this.bar = bar;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e(MainActivity.TAG, e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	bar.setVisibility(ProgressBar.GONE);
	        bmImage.setImageBitmap(result);
	        ArticlesRepository.sRepository.saveImage(id, result);
	    }
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
		ProgressBar bar = (ProgressBar)rowView.findViewById(R.id.list_loading);
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
		
		Bitmap img = ArticlesRepository.sRepository.getLoadedImage(mValues.get(position).getId());
		if (img != null) {
			imgPhoto.setImageBitmap(img);
			bar.setVisibility(ProgressBar.GONE);
		} else {
			new DownloadImageTask(mValues.get(position).getId(), imgPhoto, bar).execute(mValues.get(position).getPhotoUrl());
		}
		
		return rowView;
	}
	
}
