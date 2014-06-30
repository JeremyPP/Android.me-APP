package com.ndroidme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
//import android.support.v4.view.MenuCompat;
//import android.support.v4.view.MenuItemCompat;
import android.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ArticleActivity extends Activity {

	private Article mArticle;
	private TextView tvTitle, tvDate, tvFrom, wvFrom;
	private ImageView imgPhoto;
	private WebView wvContent;
	private Drawable mActionBarBackgroundDrawable;
	private ArticlesRepository mArticleRepository;
	private ShareActionProvider mShareActionProvider;
	private class GetMoreInfo extends AsyncTask<String, Void, Void> {
		Article article;

	    public GetMoreInfo(Article article) {
	    	this.article = article;
	    }

	    protected Void doInBackground(String... urls) {
	    	try {
	    		new ArticleManager(FeedConfig.FM_URL).getMoreInfo(article);
	    	} catch(Exception e) {
	    		Log.e(MainActivity.TAG, e.getMessage());
	    		finish();
	    	}
	    	return null;
	    }
	    
	    protected void onPostExecute(Void result) {
	    	ArticlesRepository.sRepository.saveArticle(article.getId(), article);
	    	showInfo();
	    }
	    
	} 
	
	private void getArticle() { 
		Intent intent = getIntent();
		if (intent != null) {
			Parcelable parcel = getIntent().getParcelableExtra("article");
			if (parcel != null) {
				mArticle = (Article)parcel;
			}
		}
	}
	
	private void showInfo() {
		ProgressBar bar = (ProgressBar)findViewById(R.id.article_loading);
		bar.setVisibility(ProgressBar.GONE);
		tvFrom.setVisibility(TextView.VISIBLE);
		wvFrom.setVisibility(TextView.VISIBLE);
		ArticlesRepository.sRepository.insert(mArticle);
		ImageLoader.getInstance().displayImage(mArticle.getPhotoUrl(), imgPhoto);

		tvTitle.setText(mArticle.getTitle());
		String writer = (getText(R.string.article_writer) + " " + "<b>" + mArticle.getWriter() + "</b>").toUpperCase();
		String date = (getText(R.string.article_date) + " " + "<b>" + mArticle.getDate() + "</b>").toUpperCase();
		tvDate.setText(Html.fromHtml(writer + " | " + date));
		wvContent.loadUrl(mArticle.getContentUrl()); 
		wvFrom.setText(mArticle.getFrom().get(0));
		wvFrom.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String url = mArticle.getFrom().get(1);
				if (!url.startsWith("https://") && !url.startsWith("http://")){
				    url = "http://" + url;
				}
				Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(openUrlIntent);
			}
		});
		//wvFrom.loadData("<a href='"+ mArticle.getFrom().get(1) + "' style='color: #535353; text-decoration: none;'>" + mArticle.getFrom().get(0) + "</a>", "text/html", "utf-8");
		//wvFrom.setBackgroundColor(Color.parseColor("#cecece"));
		//wvFrom.reload();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);

		getArticle();
		if (mArticle == null) {
			finish();
		}
		//mShareActionProvider = new ShareActionProvider(this);
		//mShareActionProvider.
		
		tvTitle = (TextView)findViewById(R.id.article_tvTitle);
		tvDate = (TextView)findViewById(R.id.article_tvDate);
		tvFrom = (TextView)findViewById(R.id.article_tvFrom);
		wvFrom = (TextView)findViewById(R.id.article_wvFrom);
		wvContent = (WebView)findViewById(R.id.article_wvContent);
		imgPhoto = (ImageView)findViewById(R.id.article_imgPhoto);
		mArticleRepository=new ArticlesRepository(this);
		Article article = ArticlesRepository.sRepository.getLoadedArticle(mArticle.getId());
		if (article != null) {
			mArticle = article;
			showInfo();
		} else {
			new GetMoreInfo(mArticle).execute();
		}

		mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ic_action_bar2);
		mActionBarBackgroundDrawable.setAlpha(0);

		getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);

		((NotifyingScrollView) findViewById(R.id.article_scroll)).setOnScrollChangedListener(mOnScrollChangedListener);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			// Holo light action bar color is #DDDDDD
			int actionBarColor = Color.parseColor("#000000");
			tintManager.setStatusBarTintColor(actionBarColor);
			int padding = MainActivity.dpToPx(this, 25);
			ScrollView scroll = (ScrollView)findViewById(R.id.article_scroll);
			scroll.setPadding(0, padding, 0, 0);
		}
		
		String s;
		if (mArticle.getTitle().length() > 20) {
			s = mArticle.getTitle().substring(0, 20) + "...";
		} else {
			s = mArticle.getTitle();
		}
		setTitle(mArticle.getTitle());
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setIcon(android.R.color.transparent);
		getActionBar().setDisplayShowTitleEnabled(true);
		//getActionBar().setHomeButtonEnabled(false);
		//getActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = findViewById(R.id.article_imgPhoto).getHeight() - getActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }
    };
	
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem item = menu.findItem(R.id.action_likes);
		item.setTitle(String.valueOf(mArticle.getCountLikes()));
		//item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		 MenuItem shareItem = menu.findItem(R.id.menu_share);
		
	     mShareActionProvider=(ShareActionProvider)shareItem.getActionProvider();
	     mShareActionProvider.setShareIntent(getDefaultIntent()); 
		 //MenuItemCompat.setActionProvider(shareItem, mShareActionProvider);
        // Fetch and store ShareActionProvider
        
		
		return true;
	}
	public void doShare(Intent shareIntent) {
	      // When you want to share set the share intent.
	    shareIntent= new Intent(Intent.ACTION_SEND);
    	shareIntent.setType("text/plain");
    	shareIntent.putExtra(Intent.EXTRA_TEXT,mArticle.getArticleUrl());
	    mShareActionProvider.setShareIntent(shareIntent);
	  }
	Intent getDefaultIntent()
	{
		Intent intent= new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,mArticle.getArticleUrl());
		return intent;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        case R.id.action_view_in_browser:
	        	Intent intent= new Intent(Intent.ACTION_VIEW);
	            intent.setData(Uri.parse(mArticle.getArticleUrl()));
	            startActivity(intent);
	            return true;
	        	
	        	
	        }
	    return super.onOptionsItemSelected(item);
	}

}
