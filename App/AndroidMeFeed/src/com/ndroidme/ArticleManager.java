package com.ndroidme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class establishes a connection between this application 
 * and your website. You must refer to a URL built in JSONArray 
 * format, with each interior JSONObject containing the following 
 * attributes: 
 * - "id":
 * 		A unique ID for each article
 * - "title":
 * 		Title of the article 
 * - "date":
 * 		Publication date of the article
 * - "tags":
 * 		A array of tags
 * - "writer":
 * 		Writer of the article
 * - "photo_url":
 * 		Url of the main photo of the article
 * - "countLikes":
 * 		Quantify of likes of the article
 * - "countComments":
 * 		Quantify of comments of the article
 * - "content":
 * 		Content of the article in plain text
 * - "from":
 * 		A array containing information from where the article was extracted


 * @author Abner M.
 */
public class ArticleManager {

	private String mUrl;
	
	/**
	 * Main constructor
	 * @param url
	 * 		Source of articles
	 */
	public ArticleManager(String url) {
		this.mUrl = url;
	}
	
	/**
	 * Reads all the content from the url and puts it in a string
	 * @param rd
	 * 		Stream that will be used
	 * @return
	 * 		Content in form of string
	 * @throws IOException
	 * 		Case the url isn't valid
	 */
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	}	

	private String getTags(List<String> tags) {
		if (tags == null) return "";
		StringBuilder s = new StringBuilder();
		for (String tag : tags) {
			s.append(tag + ",");
		}
		return s.toString();
	}
	
	private String convertDate(int seconds) {
		java.util.Date date = new java.util.Date((long)seconds*1000);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
	/**
	 * Get more information about an article in particular
	 * @param article
	 * 		Article you want to infer 
	 */
	public void getMoreInfo(Article article) throws Exception {
		InputStream inputStream = new URL(String.format(mUrl + "post=%d", article.getId())).openStream();
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String jsonText = readAll(bufferedReader);
			JSONObject allContent = new JSONObject(jsonText);
			int sucess = allContent.getInt("sucess");
			if (sucess == 0)
				throw new Exception("Your request returned null");
		
			List<String> listOfTags = new ArrayList<String>();
			JSONArray tags = allContent.getJSONArray("tags");
			for (int i = 0; i < tags.length(); i++) {
				String tag = tags.getString(i);
				listOfTags.add(tag);
			}
			String writer = allContent.getString("writer");
			String content = allContent.getString("content");
			List<String> listOfFrom = new ArrayList<String>();
			JSONObject from = allContent.getJSONObject("from");
			listOfFrom.add(from.getString("name"));
			listOfFrom.add(from.getString("link"));
			
			article.setTags(listOfTags);
			article.setWriter(writer);
			article.setContent(content);
			article.setFrom(listOfFrom);
		} finally { 
			inputStream.close();
		}
	}

	/**
	 * Read all articles from the url and returns it as a list of 
	 * NewsArticle
	 * @return
	 * 		A list of all articles available 
	 * @throws IOException 
	 */
	public Article[] getArticles(int startingIndex, List<String> tags) 
			throws Exception { 
		List<Article> list = new LinkedList<Article>();
		String url;
		if (tags == null) {
			url = String.format(mUrl + "limit=9&skip=%d", startingIndex);
		} else {
			url = String.format(mUrl + "limit=20&skip=%d&tags=%s", startingIndex, getTags(tags));
		}
		InputStream inputStream = new URL(url).openStream();
	    try {
	      BufferedReader bufferedReader = new BufferedReader(
	    		  new InputStreamReader(inputStream, Charset.forName("UTF-8")));
	      String jsonText = readAll(bufferedReader);
	      JSONObject allContent = new JSONObject(jsonText);
	      int sucess = allContent.getInt("sucess");
	      if (sucess == 0)
	    	  return new Article[]{};
	      JSONArray posts = allContent.getJSONArray("posts");
	      for (int i = 0; i < posts.length(); i++) {
	    	  JSONObject article = posts.getJSONObject(i);
	    	  int id = article.getInt("id");
	    	  String title = article.getString("title");
	    	  String date = convertDate(article.getInt("date"));
	    	  String photoUrl = article.getString("photo_url");
	    	  int countLikes = article.getInt("countLikes");
	    	  int countComments = article.getInt("countComments");
	    	  String resume = article.getString("resume");	  
	    	  list.add(new Article(id, null, null, title, resume, null, date, null, photoUrl, countComments, countLikes));
	      }
	    } finally {
	      inputStream.close();
	    }
	    Article[] array = new Article[list.size()];
	    list.toArray(array);
	    return array;
	}
	
}
