package com.example.miriamsrecipes.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class StepsItem implements Parcelable {
	
	private int id;
	
	private String shortDescription;
	private String description;
	
	private String videoURL;
	private String thumbnailURL;
	
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public String getVideoURL() {
		return videoURL;
	}
	
	public String getThumbnailURL() {
		return thumbnailURL;
	}
	
	
	/**
	 * Implementation of Parcelable
	 */
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int i) {
		
		parcel.writeInt(id);
		parcel.writeString(shortDescription);
		parcel.writeString(description);
		parcel.writeString(videoURL);
		parcel.writeString(thumbnailURL);
	}
	
	private StepsItem(Parcel in) {
		id = in.readInt();
		shortDescription = in.readString();
		description = in.readString();
		videoURL = in.readString();
		thumbnailURL = in.readString();
	}
	
	public static final Creator<StepsItem> CREATOR = new Creator<StepsItem>() {
		@Override
		public StepsItem createFromParcel(Parcel in) {
			return new StepsItem(in);
		}
		
		@Override
		public StepsItem[] newArray(int size) {
			return new StepsItem[size];
		}
	};
}