package com.ndroidme;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * This class is responsible to notify on system task bar
 * when new posts are available. The verification must be
 * continuous, that's why a service is required.
 * @author Abner M.
 */
public class FeedService extends Service {

	private static final int FM_NOTIFICATION_ID = 102121; 
	public static boolean sDontShow = false;
	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotificationManager;
	private ArticleManager mArticleManager;
	private ScheduledThreadPoolExecutor mTimer;
	private Runnable mCheckForNotifications = new Runnable() {

		@Override
		public void run() {
			checkOperation();
			
		}
    	
    };
	private int lastNumberOfUnread = 0;
	
	/**
	 * Adds a new notification on task bar
	 */
    private void addNotification(Article[] articles, int numberOfRead) {
    	//Creates a new notification
    	mBuilder = new NotificationCompat.Builder(this);
    	mBuilder.setSmallIcon(FeedConfig.FM_NOTIFICATION_ICON);
    	mBuilder.setContentTitle(getText(R.string.notification_title));
    	mBuilder.setContentText(getText(R.string.notification_text));
    	mBuilder.setTicker(getText(R.string.notification_ticker));
    	mBuilder.setNumber(numberOfRead);
    	
    	//Performs an action when clicked
    	Intent resultIntent = new Intent(this, MainActivity.class);
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
           stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
       
        //Attaches the notification to the system
    	mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(FM_NOTIFICATION_ID, mBuilder.build());
    }  

    /**
     * Removes the notification from task bar
     */
    private void removeNotification() {   
    	if (mNotificationManager != null) {
    		mNotificationManager.cancel(FM_NOTIFICATION_ID);
    		mNotificationManager = null;
    	}
    }  
    private void checkOperation(){
    	try {
			Article[] articles = mArticleManager.getArticles(0, null);
			int numberOfRead = ArticlesRepository.sRepository.countRead(articles);
			int numberOfUnread = articles.length - numberOfRead;
			if (numberOfUnread > lastNumberOfUnread) {
				removeNotification();
			}
			lastNumberOfUnread = numberOfUnread;
			if (numberOfUnread > 0 && !sDontShow) {
				addNotification(articles, numberOfUnread);
			} else {
				removeNotification();
			}
		} catch(Exception e) {
			stopSelf();
			e.printStackTrace();
		}
    }
    /**
     * Periodically check for new articles on feed
     */
    
    /*private void checkForNotifications() { 
    	final Handler handler = new Handler();
    	Runnable runnable = new Runnable() {
    		   @Override
    		   public void run() {
    		       do what you need to do 
    			   checkOperation();
    		       and here comes the "trick" 
    		      handler.postDelayed(this, FeedConfig.FM_SEARCH_INTERVAL);
    		   }
    		};
    	handler.postDelayed(runnable, FeedConfig.FM_SEARCH_INTERVAL);
    	
    	
    	
    	TimerTask timerTask = new TimerTask() {
    		@Override
    		public void run() {
    			checkOperation();
    		}
    	};
    	mTimer = new Timer(true);
    	mTimer.scheduleAtFixedRate(timerTask, 0, FeedConfig.FM_SEARCH_INTERVAL);
    }*/
    
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean notificationsValue = sharedPreferences.getBoolean("Notifications_Value", false);
		if (notificationsValue) {
			new ArticlesRepository(this);
			mArticleManager = new ArticleManager(FeedConfig.FM_URL);
			mTimer= new ScheduledThreadPoolExecutor(2);
			mTimer.scheduleAtFixedRate(mCheckForNotifications, FeedConfig.FM_SEARCH_DELAY,FeedConfig.FM_SEARCH_INTERVAL, TimeUnit.MILLISECONDS);
			
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		removeNotification();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() { 
		super.onDestroy();
		removeNotification();
//		mTimer.cancel();
		ArticlesRepository.sRepository.close();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
