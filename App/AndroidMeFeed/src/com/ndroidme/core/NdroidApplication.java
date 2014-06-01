package com.ndroidme.core;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class NdroidApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .resetViewBeforeLoading(true)
	        .delayBeforeLoading(50)
	        .build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		    .defaultDisplayImageOptions(options)
		    .build();
		ImageLoader.getInstance().init(config);
	}

}
