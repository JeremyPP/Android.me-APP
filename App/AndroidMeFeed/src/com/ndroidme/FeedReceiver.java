package com.ndroidme;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is intended to start a service after system boot.
 * @author Abner M.
 */
public class FeedReceiver extends BroadcastReceiver {

	private void startService(Context context, Class<? extends Service> service) {
		Intent intent = new Intent(context, service);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(intent);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
    	String action = intent.getAction();
    	if (action.equals("android.intent.action.BOOT_COMPLETED")) {
    		startService(context, FeedService.class);
    	} 
	}

}
