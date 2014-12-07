package pl.papistudio.localizer3000;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable{
	
	/* http://alvinalexander.com/java/jwarehouse/android/core/java/android/widget/TimePicker.java.shtml */
	
	/******************/
	/*   VARIABLES    */
	/******************/
	
	 private int Hour = 0; // 0-23
	 private int Minute = 0; // 0-59
	
	 
	 /******************/
	 /*   FUNCTIONS    */
	 /******************/
	 
	 public Time() {
			Hour = 0;
			Minute = 0;
		}
	 
	 public Time(int hour, int minute) {
			Hour = hour;
			Minute = minute;
		}

	 
	public int getHour() {
		return Hour;
	}

	public void setHour(int hour) {
		Hour = hour;
	}

	public int getMinute() {
		return Minute;
	}

	public void setMinute(int minute) {
		Minute = minute;
	}

	/*
	 * Parcelable part
	 * @see http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-be-parcelable
	 * @see android.os.Parcelable#describeContents()
	 * @see http://blog.logicexception.com/2012/09/a-parcelable-tutorial-for-android.html
	 */
	public Time(Parcel in)
	{
		Hour=in.readInt();
		Minute=in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Hour);
		dest.writeInt(Minute);		
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