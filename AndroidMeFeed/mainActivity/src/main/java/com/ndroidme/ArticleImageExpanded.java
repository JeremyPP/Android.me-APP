package com.ndroidme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ArticleImageExpanded extends ActionBarActivity  {
	private ZoomView mArticleImageExpanded;


	//private WebView articleImageExpanded;
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
			//Bitmap fullScreen= ScaleBitmap.scaleToView(this,photoUrl);
			//articleImageExpanded.setAdjustViewBounds(true);
			mArticleImageExpanded.setBitmap(loadedImage);
			
			
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
		mArticleImageExpanded= (ZoomView)findViewById(R.id.image_expanded);
		//articleImageExpanded.setOnTouchListener(this);
		Intent intentSent= getIntent();
		String photoUrl= intentSent.getStringExtra("imageUrl");
		if(!photoUrl.equals(""))
		{
			/*DisplayMetrics metrics= new DisplayMetrics();
		    this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		    ImageLoader imageLoader =ImageLoader.getInstance();
		    imageLoader.getMemoryCache().remove(photoUrl);
		    imageLoader.getDiskCache().remove(photoUrl);*/
			ImageLoader.getInstance().loadImage(photoUrl,new ImageListener());
			
			//articleImageExpanded.loadUrl(photoUrl);
		}
		getSupportActionBar().hide();
		
	}
	
  

	
	

  


}
