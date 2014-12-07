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