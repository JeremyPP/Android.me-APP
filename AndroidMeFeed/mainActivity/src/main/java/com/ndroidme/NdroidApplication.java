package com.ndroidme;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class NdroidApplication extends Application {
    private static ArticleManager articleManager = new ArticleManager(FeedConfig.FM_URL);
    public static int getMoreInfo(Article article)
    {
        try {
            return articleManager.getMoreInfo(article);
        } catch (Exception e) {
            e.printStackTrace();
            return ArticleManager.FAIL;
        }
    }
    public static Article[] getArticles(int numOfArticles, List<String> tags)
    {   Article[] articles=null;
        try {
            articles=articleManager.getArticles(numOfArticles,tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;





    }
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
