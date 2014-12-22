package pl.papistudio.localizer3000;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable {	
	/* http://alvinalexander.com/java/jwarehouse/android/core/java/android/widget/TimePicker.java.shtml */	
	/******************/
	/*   VARIABLES    */
	/******************/
	 private int hour = 0; // 0-23
	 private int minute = 0; // 0-59		 
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	 
	public Time() {
		hour = 0;
		minute = 0;
	}

	public Time(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(6);
		
		if(hour < 10)
			stringBuilder.append("0" + hour+":");
		else
			stringBuilder.append(hour+":");
		
		if(minute < 10)
			stringBuilder.append("0" + minute);
		else
			stringBuilder.append(minute);
		
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * @param refTime
	 * @return 1 if first time you compare to is smaller (before your time)
	 * 		   0 if both are equal
	 * 		  -1 if time you compare to is bigger
	 */
	public int compareTo(Time refTime) {
		int result;
		
		if(hour == refTime.getHour() && minute == refTime.getMinute())
			result = 0;
		else
		{
			if(hour > refTime.getHour() || (hour == refTime.getHour() && minute > refTime.minute))
				result = 1;
			else
				result = -1;
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
		hour=in.readInt();
		minute=in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(hour);
		dest.writeInt(minute);		
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