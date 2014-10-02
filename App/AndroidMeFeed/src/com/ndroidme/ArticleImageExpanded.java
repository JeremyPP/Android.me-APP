package com.ndroidme;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class ArticleImageExpanded extends Activity implements OnTouchListener {
	private ImageView articleImageExpanded;
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
			articleImageExpanded.setAdjustViewBounds(true);
			articleImageExpanded.setImageBitmap(loadedImage);
			
			
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
			/*DisplayMetrics metrics= new DisplayMetrics();
		    this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		    ImageLoader imageLoader =ImageLoader.getInstance();
		    imageLoader.getMemoryCache().remove(photoUrl);
		    imageLoader.getDiskCache().remove(photoUrl);*/
			ImageLoader.getInstance().loadImage(photoUrl,new ImageListener());
			
			//articleImageExpanded.loadUrl(photoUrl);
		}
		getActionBar().hide();
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
