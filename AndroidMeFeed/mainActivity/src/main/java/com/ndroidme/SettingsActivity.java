package com.ndroidme;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends ActionBarActivity {
	
	private CheckBox cbNotifications, cbCache;
    private ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		cbNotifications = (CheckBox)findViewById(R.id.settings_cbNotifications);
		cbCache = (CheckBox)findViewById(R.id.settings_cbCache);
		
		cbNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				savePreferences("Notifications_Value", isChecked);
			}
		});
		cbCache.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				savePreferences("Cache_Value", isChecked);
			}
		});
		
		loadSavedPreferences();
		
		Drawable mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ic_action_bar_gray);
		mActionBarBackgroundDrawable.setAlpha(255);
        mActionBar= getSupportActionBar();
		mActionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			// Holo light action bar color is #DDDDDD
			int actionBarColor = Color.parseColor("#000000");
			tintManager.setStatusBarTintColor(actionBarColor);
			int padding = MainActivity.dpToPx(this, 75);
			int leftRightPadding=MainActivity.dpToPx(this, 20);
			View layout = findViewById(R.id.settings_layout);
			layout.setPadding(leftRightPadding, padding,leftRightPadding, 0);
		}
		
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
	
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean notificationsValue = sharedPreferences.getBoolean("Notifications_Value", false);
		boolean cacheValue = sharedPreferences.getBoolean("Cache_Value", false);
		cbNotifications.setChecked(notificationsValue);
		cbCache.setChecked(cacheValue);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        }
	    return super.onOptionsItemSelected(item);
	}
	
}
