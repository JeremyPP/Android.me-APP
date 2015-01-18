package com.ndroidme;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.SparseArray;

import java.util.Set;
import java.util.TreeSet;

/**
 * This class is responsible to manage a database 
 * that stores each read article, by id.
 * @author Abner M.
 */
public class ArticlesRepository {

	private static final String FM_DATABASE_COLUMN_READ = "READ_ARTICLES_ME";
	public static ArticlesRepository sRepository;
	private SQLiteDatabase mDatabase;
	private SparseArray<Bitmap> mLoadedImages;
	private SparseArray<Article> mLoadedArticles;
	
	public ArticlesRepository(Context context) {
		if (sRepository == null || sRepository.isClosed()) {
			open(context);
			mLoadedImages = new SparseArray<Bitmap>();
			mLoadedArticles = new SparseArray<Article>();
			sRepository = this;
		}
	}
	
	/**
	 * Opens connection to database
	 */
	public void open(Context context) { 
		mDatabase = context.openOrCreateDatabase(String.valueOf(FeedConfig.FM_URL.hashCode()), Context.MODE_PRIVATE, null);
		String sqlCmd = "CREATE TABLE IF NOT EXISTS " + FM_DATABASE_COLUMN_READ + 
				" (ID INTEGER PRIMARY KEY, HASHCODE INTEGER)";
		mDatabase.execSQL(sqlCmd);
	}
	
	/**
	 * Closes connection to database
	 */
	public void close() { 
		mDatabase.close();
	}
	
	/**
	 * Verify if database is closed
	 */
	public boolean isClosed() { 
		return !mDatabase.isOpen();
	}
	
	/**
	 * Inserts a new read article on database
	 * @param article
	 * 		Read article
	 */
	public void insert(Article article) {
		String sqlCmd = "INSERT INTO " + FM_DATABASE_COLUMN_READ  
			+ " (HASHCODE) values (" + article.getId() + ")";
		mDatabase.execSQL(sqlCmd);
	}
	
	/**
	 * Gets a list of integers of all read articles on repository
	 */
	public Set<Integer> getRepository() { 
		Set<Integer> tree = new TreeSet<Integer>();
		Cursor cursor = mDatabase.query(FM_DATABASE_COLUMN_READ, new String[]{"HASHCODE"}, null, null, null, null, null);
		if (cursor.getCount() > 0) { 
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int id = cursor.getInt(cursor.getColumnIndex("HASHCODE"));
				tree.add(id);
				cursor.moveToNext();
			}
		}
        cursor.close();
		return tree;
	}

	/**
	 * Count number of read articles in a vector
	 * @param articles
	 * 		Vector of articles
	 * @return
	 * 		Number of read articles
	 */
	public int countRead(Article[] articles) {
		Set<Integer> repository = getRepository();
		int count = 0;
		for (Article article : articles) {
			if (repository.contains(article.getId())) {
				count++;
			}
		}
		return count;
	}
	
	public Bitmap getLoadedImage(int id) {
		if (mLoadedImages.indexOfKey(id) != -1) {
			return mLoadedImages.get(id);
		}
		return null;
	}
	
	public void saveImage(int id, Bitmap image) {
		mLoadedImages.put(id, image);
	}
	
	public Article getLoadedArticle(int id) {
		if (mLoadedArticles.indexOfKey(id) != -1) {
			return mLoadedArticles.get(id);
		}
		return null;
	}
	
	public void saveArticle(int id, Article article) {
		mLoadedArticles.put(id, article);
	}
	
}