package com.ndroidme;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible to manage information about
 * each article
 * @author Abner M.
 */
public class Article implements Parcelable {
	
	private static final int SRC_URL_OFFSET = 4;
	private static final String DIV_END = "</div>";
	private static final String DIV_STYLE = "<div class=\"phone\">";
	private static final String DIV_TABLET_STYLE="<div class=\"tablet\">";
    private static final String ARTICLE_STYLE="<style type=\"text/css\">"
    +"div.tablet {text-align:justify; font-size:20px;}"
    +"div.phone {text-align: justify;}"
    +"iframe.c1 {max-width: 105%; margin-left: -20px;margin-right: -20px;}"
   +"</style>";
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mContent == null) ? 0 : mContent.hashCode());
		result = prime * result + mCountComments;
		result = prime * result + mCountLikes;
		result = prime * result + ((mDate == null) ? 0 : mDate.hashCode());
		result = prime * result + ((mJsonFrom == null) ? 0 : mJsonFrom.hashCode());
		result = prime * result + mId;
		result = prime * result
				+ ((mPhotoUrl == null) ? 0 : mPhotoUrl.hashCode());
		result = prime * result + ((mResume == null) ? 0 : mResume.hashCode());
		result = prime * result + ((mTags == null) ? 0 : mTags.hashCode());
		result = prime * result + ((mTitle == null) ? 0 : mTitle.hashCode());
		result = prime * result + ((mWriter == null) ? 0 : mWriter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		if (mContent == null) {
			if (other.mContent != null)
				return false;
		} else if (!mContent.equals(other.mContent))
			return false;
		if (mCountComments != other.mCountComments)
			return false;
		if (mCountLikes != other.mCountLikes)
			return false;
		if (mDate == null) {
			if (other.mDate != null)
				return false;
		} else if (!mDate.equals(other.mDate))
			return false;
		if (mJsonFrom == null) {
			if (other.mJsonFrom != null)
				return false;
		} else if (!mJsonFrom.equals(other.mJsonFrom))
			return false;
		if (mId != other.mId)
			return false;
		if (mPhotoUrl == null) {
			if (other.mPhotoUrl != null)
				return false;
		} else if (!mPhotoUrl.equals(other.mPhotoUrl))
			return false;
		if (mResume == null) {
			if (other.mResume != null)
				return false;
		} else if (!mResume.equals(other.mResume))
			return false;
		if (mTags == null) {
			if (other.mTags != null)
				return false;
		} else if (!mTags.equals(other.mTags))
			return false;
		if (mTitle == null) {
			if (other.mTitle != null)
				return false;
		} else if (!mTitle.equals(other.mTitle))
			return false;
		if (mWriter == null) {
			if (other.mWriter != null)
				return false;
		} else if (!mWriter.equals(other.mWriter))
			return false;
		return true;
	}

	private JSONArray mJsonFrom, mJsonTags;
	List<String> from;
	private String mTitle, mResume, mContent, mDate, mWriter, mPhotoUrl,mVideoUrl;
	private int mId, mCountLikes, mCountComments;
	private List<String> mFrom;
	private List<String> mTags;
//	private static String DOCTYPE="<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
	private static String DOCTYPE="<!DOCTYPE html>";
	private static String HEAD="<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"utf-8\" /><title> Article"+"</title>"+ARTICLE_STYLE+"</head><body>";
	private static String END="</body></html>";
	private static String DATAURL="data:text/html; charset=UTF8,";
	private static String ARTICLE_URL="http://ndroidme.com/news.php?article=";
	public static final Parcelable.Creator<Article> CREATOR =
			new Parcelable.Creator<Article>(){

		@Override
		public Article createFromParcel(Parcel in) {
			return new Article(in);
		}

		@Override
		public Article[] newArray(int size) {
			return new Article[size];
		}
	};
	
	/**
	 * Main constructor
	 * @param id
	 * 		Article unique ID
	 * @param from
	 * 		A duple indicating the name and url from where the article was extracted
	 * @param tags
	 * 		Keywords indicating to what the article reffers
	 * @param title
	 * 		Title of the article
	 * @param resume
	 * 		Resume of the article
	 * @param content
	 * 		Content of the article in plain text
	 * @param date
	 * 		Publication date of the article
	 * @param writer
	 * 		Writer of the article
	 * @param photoUrl
	 * 		Url of the main image of the article
	 * @param countComments
	 * 		Quantify of comments of the article
	 * @param countLikes
	 * 		Quantify of likes of the article
	 */
	public Article(int id, List<String>  listOfFrom, JSONArray jsonTags, String title, String resume, 
			String content, String date, String writer, String photoUrl, int countComments, 
			int countLikes) { 
		this.mId = id;
		this.mTitle = title;
		this.mResume = resume;
		this.mContent = content;
		this.mDate = date;
		this.mWriter = writer;
		this.mPhotoUrl = photoUrl;
		this.mCountComments = countComments;
		this.mCountLikes = countLikes;
		this.from = listOfFrom;
		this.mJsonTags = jsonTags;
		this.mVideoUrl=null;
	}

	public String getArticleUrl()
	{
		return ARTICLE_URL+mId;
	}
	public String getPhotoUrl() {
		return mPhotoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.mPhotoUrl = photoUrl;
	}

	public List<String> getFrom() {
		return mFrom;
	}

	public void setFrom(List<String> from) {
		this.mFrom = from;
	}

	public List<String> getTags() {
		return mTags;
	}

	public void setTags(List<String> tags) {
		this.mTags = tags;
	}

	public String getResume() {
		return mResume;
	}

	public void setResume(String resume) {
		this.mResume = resume;
	}

	public String getWriter() {
		return mWriter;
	}

	public void setWriter(String writer) {
		this.mWriter = writer;
	}

	public int getCountLikes() {
		return mCountLikes;
	}

	public void setCountLikes(int countLikes) {
		this.mCountLikes = countLikes;
	}

	public int getCountComments() {
		return mCountComments;
	}

	public void setCountComments(int countComments) {
		this.mCountComments = countComments;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getContent() {
		return mContent;
	}
	
	public String getContentUrl(boolean isTablet)
	{
		if(isTablet==false)
		{
	      return DATAURL+ DOCTYPE+HEAD+DIV_STYLE + mContent + DIV_END+END;
		}
		else
		{
			return DATAURL+DOCTYPE+HEAD+DIV_TABLET_STYLE+mContent+DIV_END  +END;
		}
		
		//return /*DATAURL+*/DOCTYPE+HEAD++mContent


	}

	public void setContent(String content) {
		this.mContent = content;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		this.mDate = date;
	}

	public int getId() { 
		return mId;
	}
	
	public void setId(int id) {
		this.mId = id;
	}
	public void extractVideoURL()
	{
		   mVideoUrl=null;
           if(mContent==null)
           {
        	   throw new NullPointerException("mContent is null Cannot extract VideoURL");
           }
           String[] partsOfContent=mContent.split("<center><br>");
           mContent= partsOfContent[0]+"</body></html>";
           if(partsOfContent.length==2)
           {
             String iFrameStr=partsOfContent[1];
             mVideoUrl=iFrameStr.replace("<center><br>", "");
             mVideoUrl=mVideoUrl.replace("</center>", "");
           
            /* int srcPlace= iFrameStr.indexOf("src=");
             mVideoUrl=iFrameStr.substring(srcPlace+SRC_URL_OFFSET,iFrameStr.indexOf(' ',srcPlace));
             mVideoUrl= "http:"+mVideoUrl.replace("\"", "");*/
             //mVideoUrl="http:"+mVideoUrl.replace("embed/", "");
           }
           
	}
	public void getMoreInfo()
	{
		
	}
	// Parcelling part
    public Article(Parcel in){
    	int id = in.readInt();//
    	List<String> from = new ArrayList<String>();//
    	in.readStringList(from);//
    	List<String> tags = new ArrayList<String>();//
    	in.readStringList(tags);//
    	String title = in.readString();//
    	String resume = in.readString();//
    	String content = in.readString();//
    	String date = in.readString();//
    	String writer = in.readString();//
    	String photoUrl = in.readString();//
    	String videoUrl = in.readString();
    	int countComments = in.readInt();//
    	int countLikes = in.readInt();//
    	
    	this.setId(id);
    	this.setFrom(from);
    	this.setTags(tags);
        this.setTitle(title);
        this.setResume(resume);
        this.setContent(content);
        this.setDate(date);
        this.setWriter(writer);
        this.setPhotoUrl(photoUrl);
        this.setVideoUrl(videoUrl);
        this.setCountComments(countComments);
        this.setCountLikes(countLikes);
    }
    
	public void setVideoUrl(String videoUrl) {
		// TODO Auto-generated method stub
		mVideoUrl=videoUrl;
	}
    public String getVideoUrl()
    {
    	return mVideoUrl;
    }
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getId());//
		dest.writeStringList(getFrom());//
		try {
			dest.writeStringList(getJsonTags());
		} catch (JSONException e) {
			
			dest.writeStringList(null);
			e.printStackTrace();
		}//
		dest.writeString(getTitle());//
		dest.writeString(getResume());//
		dest.writeString(getContent());//
		dest.writeString(getDate());//
		dest.writeString(getWriter());//
		dest.writeString(getPhotoUrl());//
		dest.writeString(mVideoUrl);
		dest.writeInt(getCountComments());//
		dest.writeInt(getCountLikes());//
	}

	private List<String> getJsonTags() throws JSONException{
		// TODO Auto-generated method stub
		
			List<String> listOfTags = new ArrayList<String>();
			if(mJsonTags!=null)
			{
			for (int i = 0; i < mJsonTags.length(); i++) {
				String tag = mJsonTags.getString(i);
				listOfTags.add(tag);
			}
			return listOfTags;
			}
			return null;
		
	}

	

}
