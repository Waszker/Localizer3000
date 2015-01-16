package pl.papistudio.localizer3000;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable {	
	/* http://alvinalexander.com/java/jwarehouse/android/core/java/android/widget/TimePicker.java.shtml */	
	/******************/
	/*   VARIABLES    */
	/******************/
	 private int mHour = 0; // 0-23
	 private int mMinute = 0; // 0-59		 
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	 
	public Time() {
		mHour = 0;
		mMinute = 0;
	}

	public Time(int hour, int minute) {
		this.mHour = hour;
		this.mMinute = minute;
	}

	public int getHour() {
		return mHour;
	}

	public void setHour(int hour) {
		this.mHour = hour;
	}

	public int getMinute() {
		return mMinute;
	}

	public void setMinute(int minute) {
		this.mMinute = minute;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(6);
		
		if(mHour < 10)
		{
			stringBuilder.append("0" + mHour+":");
		}
		else
		{
			stringBuilder.append(mHour+":");
		}
		
		if(mMinute < 10)
		{
			stringBuilder.append("0" + mMinute);
		}
		else
		{
			stringBuilder.append(mMinute);
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * @param refTime
	 * @return 1 if time you compare to is smaller (before your time)
	 * 		   0 if both are equal
	 * 		  -1 if time you compare to is bigger
	 */
	public int compareTo(Time refTime) {
		int result;
		
		if(mHour == refTime.getHour() && mMinute == refTime.getMinute())
		{
			result = 0;
		}
		else
		{
			if(mHour > refTime.getHour() || (mHour == refTime.getHour() && mMinute > refTime.mMinute))
			{
				result = 1;
			}
			else
			{
				result = -1;
			}
		}
		
		return result;
	}
	
	/*
	 * Parcelable part
	 * @see http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-be-parcelable
	 * @see android.os.Parcelable#describeContents()
	 * @see http://blog.logicexception.com/2012/09/a-parcelable-tutorial-for-android.html
	 */
	public Time(Parcel in)
	{
		mHour=in.readInt();
		mMinute=in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mHour);
		dest.writeInt(mMinute);		
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Time createFromParcel(Parcel in) {
            return new Time(in); 
        }

        public Time[] newArray(int size) {
            return new Time[size];
        }
    }; 
}