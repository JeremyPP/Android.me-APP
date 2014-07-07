package com.ndroidme;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.etsy.android.grid.StaggeredGridView;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity  {
	
	public static final String TAG = "com.ndroidme";
	private List<Article> mArticles = new ArrayList<Article>();
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private SwipeRefreshLayout mRefreshLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private StaggeredGridView mList;
    private ArrayAdapter<Article> mAdapter;
    private int mIndex;
    private boolean mFirstRun;
    private ArticlesRepository mArticleRepository;
	private ArticleManager mArticleManager=new ArticleManager(FeedConfig.FM_URL);
	private boolean mHasRequestedMore;
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

	public static int dpToPx(Context context, int dp) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	private void addToArticleList(int start) throws Exception
	{
		Article[] articles = mArticleManager.getArticles(start, null);
		for (Article article : articles) {
			if (!mArticles.contains(article))
			{
				mArticles.add(article);
			}
		}
		
	}
	private void setUpList() { 
		mList = (StaggeredGridView)findViewById(R.id.main_listView);
		if (isTablet(this)) {
			mList.setColumnCount(2);
			mList.setColumnCountLandscape(3);
		} else {
			mList.setColumnCountLandscape(2);
		}
		
		mAdapter = new MyListAdapter(this, mArticles);
		final View footer = ((LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar, null, false);
		mList.addFooterView(footer);
		mList.setAdapter(mAdapter);
		footer.setVisibility(View.GONE);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
		            int position, long id) {
				RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_error);
				if (layout.getVisibility() != LinearLayout.VISIBLE) {
					Intent itemIntent = new Intent(MainActivity.this, ArticleActivity.class);
					itemIntent.putExtra("article", mArticles.get(position));
					startActivity(itemIntent);
				}
			}
		});
		mList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) { }
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				final int lastInScreen = firstVisibleItem + visibleItemCount;
				if (lastInScreen >= totalItemCount) {
					if (!mHasRequestedMore) {
						
						mHasRequestedMore = true;
						if(mFirstRun==false){
						  footer.setVisibility(View.VISIBLE);
						}
						new Thread(new Runnable() {
							 
							@Override
							public void run() {
								try
								{
									if(mFirstRun==false)
									{
										mIndex++;
									  addToArticleList(mIndex*9);
									}
									else
									{
										addToArticleList(0);
										
									}
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											try {
												mAdapter.notifyDataSetChanged();
												mHasRequestedMore = false;
												if(mFirstRun==true)
												{
												  ProgressBar bar = (ProgressBar)findViewById(R.id.main_loading);
												  bar.setVisibility(ProgressBar.GONE);
												  mFirstRun=false;
												}
												RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_error);
												layout.setVisibility(LinearLayout.INVISIBLE);
											} catch (Exception e) {
												e.fillInStackTrace();
											}
										}
									});
								} catch(Exception e) {
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											footer.setVisibility(View.VISIBLE);
											RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_error);
											layout.setVisibility(LinearLayout.VISIBLE);
											mHasRequestedMore = false;
											mList.setSelection(0);
											mIndex--;
										}
									});
								}
							}
						}).start();
					}
				}
			}
		});
		mFirstRun = true;
		mHasRequestedMore=false;
	}

    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (FeedService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	private void fillDrawerLayout() { 
		List<DrawerItem> list = new ArrayList<DrawerItem>();
		list.add(new DrawerItem("Home", true, 0));
		list.add(new DrawerItem("Contact", false, 0));
		list.add(new DrawerItem("Profile", false, 0));
		list.add(new DrawerItem("Settings", false, R.drawable.ic_settings));
		
		ArrayAdapter<DrawerItem> adapter = new MyDrawerAdapter(this, list);
		mDrawerList.setAdapter(adapter);
	}  
	private void loadRefreshLayout()
	{
		mRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
		mRefreshLayout.setColorSchemeColors(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				//mRefreshLayout.setRefreshing(false);
				updateList();
				
			}});
		
	
	}
	private void loadDrawerLayout() { 
		mDrawerList = (ListView) findViewById(R.id.main_left_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_navigation_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(getString(R.string.site_name));
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(getString(R.string.site_name));
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
		            int position, long id) {
				if (position == 1) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri data = Uri.parse("mailto:?subject=" + "contact@ndroidme.com" + "&body=" + "");
					intent.setData(data);
					startActivity(intent);
				} else if (position == 2) {
					 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					 builder.setTitle("Oops...");
					 builder.setMessage("This feature is still under development, check again soon.");
					 builder.setNeutralButton("Ok", null);
					 AlertDialog alert = builder.create();
					 alert.show();
				} else if (position == 3) {
					Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
					startActivity(intent);
				}
				mDrawerLayout.closeDrawers();
			}
		});
	}
	
	private void updateList() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
//					Article[] articles = mArticleManager.getArticles(0, null);
//					for (Article article : articles) {
//						mArticles.add(article);
//					}
					addToArticleList(0);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								
								//setUpList();
								mAdapter.notifyDataSetChanged();
								
								mRefreshLayout.setRefreshing(false);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} catch(Exception e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setContentView(R.layout.activity_noconnection);
							TextView tv = (TextView)findViewById(R.id.noconnection_tvRetry);
							tv.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Intent intent = getIntent();
									startActivity(intent);
								}
							});
						}
					});
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (!sharedPreferences.contains("Notifications_Value")) { 
			savePreferences("Notifications_Value", true);
		}
		if (!sharedPreferences.contains("Cache_Value")) { 
			savePreferences("Cache_Value", false);
		}
 
		mArticleRepository=new ArticlesRepository(MainActivity.this);
		mIndex = 0;
		loadRefreshLayout();
		setUpList();
		//updateList();
		
		FeedService.sDontShow = true;
        
		loadDrawerLayout();
		fillDrawerLayout();
        //loadRefreshLayout();
		Drawable mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ic_action_bar);
		mActionBarBackgroundDrawable.setAlpha(255);

		getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			// Holo light action bar color is #DDDDDD
			int actionBarColor = Color.parseColor("#6472CD");
			tintManager.setStatusBarTintColor(actionBarColor);
		}
	}
    
	@Override
	protected void onResume() { 
		super.onResume();

		if(mArticleRepository==null)
		{
			mArticleRepository= new ArticlesRepository(this);
		}
		//update the list of read articles
//		try {
//			if (mArticles != null)
//				fillList();
//		} catch (Exception e) {
//			e.fillInStackTrace();
//		}
		
		//verify is notifications preference is on
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean notificationsValue = sharedPreferences.getBoolean("Notifications_Value", false);
		if (notificationsValue && !isServiceRunning() ) {
			Intent serviceIntent = new Intent(this, FeedService.class);
			startService(serviceIntent);
		}/* else {
			if (isServiceRunning()) {
				Intent serviceIntent = new Intent(this, FeedService.class);
				stopService(serviceIntent);
			}*/
		
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);  */
		// Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
	} 
	
	@Override
	protected void onDestroy() { 
		super.onDestroy();
		//new ArticlesRepository(this);
		//mark all articles as unread when leave the main activity
		if (ArticlesRepository.sRepository != null && mArticles != null) {
			Set<Integer> repository = ArticlesRepository.sRepository.getRepository();
			for (Article article : mArticles) {
				if (!repository.contains(article.getId())) {
					ArticlesRepository.sRepository.insert(article);
				}
			}
		}
		FeedService.sDontShow = false;
		//close the database
		if (!isServiceRunning()) {
			ArticlesRepository.sRepository.close();
		}
	}

}
