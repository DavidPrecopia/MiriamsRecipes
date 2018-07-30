package com.example.miriamsrecipes.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class StepItem implements Parcelable {
	
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
	
	private StepItem(Parcel in) {
		id = in.readInt();
		shortDescription = in.readString();
		description = in.readString();
		videoURL = in.readString();
		thumbnailURL = in.readString();
	}
	
	public static final Creator<StepItem> CREATOR = new Creator<StepItem>() {
		@Override
		public StepItem createFromParcel(Parcel in) {
			return new StepItem(in);
		}
		
		@Override
		public StepItem[] newArray(int size) {
			return new StepItem[size];
		}
	};
}