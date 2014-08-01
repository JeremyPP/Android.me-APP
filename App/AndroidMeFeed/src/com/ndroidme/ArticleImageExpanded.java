package com.ndroidme;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ArticleImageExpanded extends Activity {
	private ImageView articleImageExpanded;
	private class ImageListener implements ImageLoadingListener
	{

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}
		
	}
	protected void onCreate(Bundle instance)
	{
		super.onCreate(instance);
		setContentView(R.layout.article_image_expanded);
		articleImageExpanded= (ImageView)findViewById(R.id.image_expanded);
		Intent intentSent= getIntent();
		String photoUrl= intentSent.getStringExtra("imageUrl");
		if(photoUrl!="")
		{
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(photoUrl, articleImageExpanded);
		}
		
	}

}
