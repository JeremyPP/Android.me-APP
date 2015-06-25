package com.ndroidme;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
	private HttpReader httpReader;
	public final static int SUCCESS = 0;
    public final static int FAIL=-1;
    private final static String TAG="ArticleManager";
	/**
	 * Main constructor
	 * @param url
	 * 		Source of articles
	 */
	public ArticleManager(String url) {
		this.mUrl = url;
		httpReader = new HttpReader();
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
	private static String readAll(BufferedReader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int bufferSize=1024;
	    char[] buffer=new char[bufferSize];
	    
	    while (true)
	    {
	      int charsRead= rd.read(buffer);
	      if(charsRead!=bufferSize)
	      {
	    	  if(charsRead>0)
	    	  {
	    		  sb.append(buffer,0,charsRead);
	    	  }
	    	  break;
	      }
	      sb.append(buffer);
	    	
	    }
	    return sb.toString();
	}	
	/*private String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");
	    
	    int result=500;
	    StringBuilder webString= new StringBuilder();
	    while(true)
	    {
	    	int buffer=-41;
	    	 buffer=reader.read();
	    	 if(buffer==-1)
	    	 {
	    		 break;
	    	 
	    	 }
	    	 webString.append((char)buffer);
	    	
	    }
	    
	    return webString.toString();
	    
	}*/
	private String getTags(List<String> tags) {
		if (tags == null) return "";
		StringBuilder s = new StringBuilder();
		for (String tag : tags) {
			s.append(tag);
            s.append(',');
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
	public int getMoreInfo(Article article)
    {
		String url=String.format(mUrl + "post=%d", article.getId());
		//HttpReader reader= new HttpReader();
		

		try {
			/*bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream, Charset.forName("UTF-8")));*/
			String jsonText = httpReader.downloadUrl(url);
			//String jsonText= readIt(inputStream);
			JSONObject allContent = new JSONObject(jsonText);
			int sucess = allContent.getInt("sucess");
			if (sucess == 0)
				throw new Exception("Your request returned null");
		
			
			String writer = allContent.getString("writer");
			String content = allContent.getString("content");
			List<String> listOfFrom = new ArrayList<>();
			JSONObject from = allContent.getJSONObject("from");
			listOfFrom.add(from.getString("name"));
			listOfFrom.add(from.getString("link"));
			
			//article.setTags(listOfTags);
			article.setWriter(writer);
			article.setContent(content);
			article.setFrom(listOfFrom);
			article.extractVideoURL();
            return SUCCESS;
		}
        catch (Exception e)
        {
            Log.d(TAG, e.getMessage());
            return FAIL;
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
		List<Article> list = new LinkedList<>();
		String url;
		if (tags == null) {
			url = String.format(mUrl + "limit=9&skip=%d", startingIndex);
		} else {
			url = String.format(mUrl + "limit=20&skip=%d&tags=%s", startingIndex, getTags(tags));
		}
		//InputStream inputStream = new URL(url).openStream();
		BufferedReader bufferedReader=null;
	    try
        {
            String jsonText = httpReader.downloadUrl(url);
            JSONObject allContent = new JSONObject(jsonText);
            int sucess = allContent.getInt("sucess");
            if (sucess == 0)
                return new Article[]{};
            JSONArray posts = allContent.getJSONArray("posts");
            for (int i = 0; i < posts.length(); i++)
            {
                try
                {
                    JSONObject article = posts.getJSONObject(i);
                    int id = article.getInt("id");
                    String title = article.getString("title");
                    String date = convertDate(article.getInt("date"));
                    String photoUrl = article.getString("photo_url");
                    int countLikes = article.getInt("countLikes");
                    int countComments = article.optInt("countComments",0);


                    String resume = article.getString("resume");
                  /* String content= article.getString("content");
                    String writer= article.getString("writer");
                    List<String> listOfFrom = new ArrayList<String>();
                    JSONObject from = allContent.getJSONObject("from");
                    listOfFrom.add(from.getString("name"));
                    listOfFrom.add(from.getString("link"));*/

                    list.add(new Article(id, null, null, title, resume, null, date, null, photoUrl, countComments, countLikes));
                }
                catch(Exception ex)
                {
                    Log.d("ArticleManager", ex.getMessage());
                }
            }
        }
          catch(Exception e)
            {
                Log.d("ArticleManager",e.getMessage());
            }
	     finally {
	      //bufferedReader.close();
	    }
	    Article[] array = new Article[list.size()];
	    list.toArray(array);
	    return array;
	}
	
}
