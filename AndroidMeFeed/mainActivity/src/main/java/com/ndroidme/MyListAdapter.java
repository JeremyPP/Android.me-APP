package com.ndroidme;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;
import java.util.Set;

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
		final ViewHolder viewHolder;

        if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutId, parent, false);
			
			viewHolder = new ViewHolder();
			
			viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.list_tvTitle);
			viewHolder.imgPhoto = (ImageView)convertView.findViewById(R.id.list_imgPhoto);
			viewHolder.tvResume = (TextView)convertView.findViewById(R.id.list_tvResume);
			viewHolder.tvComments = (TextView)convertView.findViewById(R.id.list_tvComments);
			viewHolder.tvLikes = (TextView)convertView.findViewById(R.id.list_tvLikes);
			viewHolder.bar = (ProgressBar)convertView.findViewById(R.id.list_loading);
			convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(mValues.get(position).getTitle());
        viewHolder.tvResume.setText(mValues.get(position).getResume());
        viewHolder.tvComments.setText(String.format("%d %s", mValues.get(position).getCountComments(), parent.getResources().getString(R.string.article_comments)));
        viewHolder.tvLikes.setText(String.format("%d %s", mValues.get(position).getCountLikes(), parent.getResources().getString(R.string.article_likes)));
		
		ImageLoader.getInstance().displayImage(mValues.get(position).getPhotoUrl(), viewHolder.imgPhoto, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		    	viewHolder.bar.setVisibility(View.GONE);
		    }
		});

		return convertView;
	}
	
	static class ViewHolder {
		TextView tvTitle;
		ImageView imgPhoto;
		TextView tvResume;
		TextView tvComments;
		TextView tvLikes;
		ProgressBar bar;
    }
	
}
