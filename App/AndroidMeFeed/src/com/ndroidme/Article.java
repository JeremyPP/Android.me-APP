package com.ndroidme;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is responsible to manage information about
 * each article
 * @author Abner M.
 */
public class Article implements Parcelable {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mContent == null) ? 0 : mContent.hashCode());
		result = prime * result + mCountComments;
		result = prime * result + mCountLikes;
		result = prime * result + ((mDate == null) ? 0 : mDate.hashCode());
		result = prime * result + ((mFrom == null) ? 0 : mFrom.hashCode());
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
		if (mFrom == null) {
			if (other.mFrom != null)
				return false;
		} else if (!mFrom.equals(other.mFrom))
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

	private List<String> mFrom, mTags;
	private String mTitle, mResume, mContent, mDate, mWriter, mPhotoUrl;
	private int mId, mCountLikes, mCountComments;
	
	
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
	public Article(int id, List<String> from, List<String> tags, String title, String resume, 
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
		this.mFrom = from;
		this.mTags = tags;
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
        this.setCountComments(countComments);
        this.setCountLikes(countLikes);
    }
    
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getId());//
		dest.writeStringList(getFrom());//
		dest.writeStringList(getTags());//
		dest.writeString(getTitle());//
		dest.writeString(getResume());//
		dest.writeString(getContent());//
		dest.writeString(getDate());//
		dest.writeString(getWriter());//
		dest.writeString(getPhotoUrl());//
		dest.writeInt(getCountComments());//
		dest.writeInt(getCountLikes());//
	}

}
