package pl.papistudio.localizer3000;

import java.util.Calendar;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.MultiStateToggleButton.ToggleStates;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>Location class represents object created by user
 * containing all information about place and phone status 
 * in this specified area.</p>
 * 
 * <p>This class is huge due to many, many, MANY getters and setters.
 * We decided to use them because of reasons stated here:
 * http://stackoverflow.com/questions/1568091/why-use-getters-and-setters </p>
 *  
 * @author PapiTeam
 *
 */
public class Location implements Parcelable, Comparable<Location> {
	/******************/
	/*   VARIABLES    */
	/******************/
	private String mName;
	private MultiStateToggleButton.ToggleStates mWifiState, mBluetoothState, mNfcState, 
												mMobileState, mSoundState, mVibrationState;
	private boolean mIsSMSsendOn, mShouldTurnOff;
	private int mRadius;
	private int mPriority;
	private Time mTimeFrom, mTimeTo;
	private boolean mIsMon, mIsTue, mIsWed, mIsThu, mIsFri, mIsSat, mIsSun;
	private android.location.Location mLocation;
	
	

	/******************/
	/*   FUNCTIONS    */
	/******************/
	/**
	 * Creates location with specified name.
	 * Location by default has all options except isSMSsendOn enabled and is active
	 * on each day of the week from 00:00 to 23:59 with Radius=100m.
	 * @param name
	 */
	public Location(String name) {
		this.mName = name;
		this.mWifiState = this.mBluetoothState = this.mNfcState = this.mMobileState = 
				this.mSoundState = this.mVibrationState = ToggleStates.Off;
		this.mIsSMSsendOn=false;
		this.mShouldTurnOff = false;
		this.mRadius=100;
		setTimeFrom(new Time(0,0));
		setTimeTo(new Time(23,59));
		setDaysOfWeek(true, true, true, true, true, true, true);
	}
	
	/**
	 * Creates location with specified options enabled.
	 * Location is active on all days of the week from 00:00 to 23:59.
	 * @param name
	 * @param isWifiOn
	 * @param isBluetoothOn
	 * @param isNfcOn
	 * @param isMobData
	 * @param isSoundOn
	 * @param isVibrationOn
	 * @param radius
	 */
	public Location(String name, ToggleStates isWifiOn, ToggleStates isBluetoothOn,
			ToggleStates isNfcOn, ToggleStates isMobData, ToggleStates isSoundOn,
			ToggleStates isVibrationOn, int radius) {
		this.mName = name;
		this.mWifiState = isWifiOn;
		this.mBluetoothState = isBluetoothOn;
		this.mNfcState = isNfcOn;
		this.mMobileState = isMobData;
		this.mSoundState = isSoundOn;
		this.mVibrationState = isVibrationOn;
		this.mIsSMSsendOn = false;
		this.mShouldTurnOff = false;
		this.mRadius = radius;
		setTimeFrom(new Time(0, 0));
		setTimeTo(new Time(23, 59));
		setDaysOfWeek(true, true, true, true, true, true, true);
	}
	
	/**
	 * Sets on which days of week the location profile should be active.
	 * @param Mon
	 * @param Tue
	 * @param Wed
	 * @param Thu
	 * @param Fri
	 * @param Sat
	 * @param Sun
	 */
	public void setDaysOfWeek(boolean mon, boolean tue, boolean wed, boolean thu,
			boolean fri, boolean sat, boolean sun) {
		this.mIsMon = mon;
		this.mIsTue = tue;
		this.mIsWed = wed;
		this.mIsThu = thu;
		this.mIsFri = fri;
		this.mIsSat = sat;
		this.mIsSun = sun;
	}
	
	/**
	 * Checks if location is applicable during this day
	 * of the week and within given hours limit.
	 * @param day of the week (taken from Calendar.get())
	 * @return is location enabled
	 */
	public boolean isLocationEnabled(final int day, final Time time) {
		boolean isEnabled = false;
		
		switch (day) {
			case Calendar.MONDAY:
				isEnabled = mIsMon;
				break;
				
			case Calendar.TUESDAY:
				isEnabled = mIsTue;
				break;
				
			case Calendar.WEDNESDAY:
				isEnabled = mIsWed;
				break;
				
			case Calendar.THURSDAY:
				isEnabled = mIsThu;
				break;
				
			case Calendar.FRIDAY:
				isEnabled = mIsFri;
				break;
				
			case Calendar.SATURDAY:
				isEnabled = mIsSat;
				break;
				
			case Calendar.SUNDAY:
				isEnabled = mIsSun;
				break;
	
			default:
				break;
		}
		
		if(mTimeFrom.compareTo(mTimeTo) > 0)
		{
			if(time.compareTo(mTimeFrom) < 0 && time.compareTo(mTimeTo) > 0)
			{
				isEnabled = false;
			}
		}
		else
		{
			if(time.compareTo(mTimeFrom) < 0 || time.compareTo(mTimeTo) > 0)
			{
				isEnabled = false;
			}
		}
		
		return isEnabled;
	}
	

	@Override
	public String toString() {
		return mName;
	}
	
	/**
	 * Compares two location classes.
	 * One class is bigger than the other when it has higher priority value.
	 * @param location
	 * @return 1 if first location has higher priority
	 * 		   0 if two locations have the same priority
	 * 		  -1 if second is bigger
	 */
	public int compareTo(final Location location) {
		int result = 0;
		
		if(this.mPriority > location.mPriority)
		{
			result = 1;
		}
		
		if(this.mPriority < location.mPriority)
		{
			result = -1;
		}
		return result;
	}

	/************************/
	/*   GETTERS&SETTERS    */
	/************************/
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public android.location.Location getLocation() {
		return mLocation;
	}

	public void setLocation(android.location.Location location) {
		this.mLocation = location;
	}
	
	public ToggleStates isWifiOn() {
		return mWifiState;
	}

	public void setWifiOn(ToggleStates isWifiOn) {
		this.mWifiState = isWifiOn;
	}

	public ToggleStates isBluetoothOn() {
		return mBluetoothState;
	}

	public void setBluetoothOn(ToggleStates isBluetoothOn) {
		this.mBluetoothState = isBluetoothOn;
	}

	public ToggleStates isNfcOn() {
		return mNfcState;
	}

	public void setNfcOn(ToggleStates isNfcOn) {
		this.mNfcState = isNfcOn;
	}

	public ToggleStates isMobileData() {
		return mMobileState;
	}

	public void setMobileData(ToggleStates isMobData) {
		this.mMobileState = isMobData;
	}
	
	public ToggleStates isSoundOn() {
		return mSoundState;
	}

	public void setSoundOn(ToggleStates isSoundOn) {
		this.mSoundState = isSoundOn;
	}

	public ToggleStates isVibrationOn() {
		return mVibrationState;
	}

	public void setVibrationOn(ToggleStates isVibrationOn) {
		this.mVibrationState = isVibrationOn;
	}

	public boolean isSMSsendOn() {
		return mIsSMSsendOn;
	}

	public void setSMSsendOn(boolean isSMSsendOn) {
		this.mIsSMSsendOn = isSMSsendOn;
	}
	
	public boolean shouldTurnOff() {
		return mShouldTurnOff;
	}

	public void setShouldTurnOff(boolean mShouldTurnOff) {
		this.mShouldTurnOff = mShouldTurnOff;
	}

	public int getRadius() {
		return mRadius;
	}

	public void setRadius(int radius) {
		if(radius > 0 && radius < Integer.MAX_VALUE)
			this.mRadius = radius;
	}

	public int getPriority() {
		return mPriority;
	}

	public void setPriority(int priority) {
		this.mPriority = priority;
	}

	public Time getTimeFrom() {
		return mTimeFrom;
	}

	public void setTimeFrom(Time timeFrom) {
		this.mTimeFrom = timeFrom;
	}

	public Time getTimeTo() {
		return mTimeTo;
	}

	public void setTimeTo(Time timeTo) {
		this.mTimeTo = timeTo;
	}

	public boolean isMon() {
		return mIsMon;
	}

	public void setMon(boolean isMon) {
		this.mIsMon = isMon;
	}

	public boolean isTue() {
		return mIsTue;
	}

	public void setTue(boolean isTue) {
		this.mIsTue = isTue;
	}

	public boolean isWed() {
		return mIsWed;
	}

	public void setWed(boolean isWed) {
		this.mIsWed = isWed;
	}

	public boolean isThu() {
		return mIsThu;
	}

	public void setThu(boolean isThu) {
		this.mIsThu = isThu;
	}

	public boolean isFri() {
		return mIsFri;
	}

	public void setFri(boolean isFri) {
		this.mIsFri = isFri;
	}

	public boolean isSat() {
		return mIsSat;
	}

	public void setSat(boolean isSat) {
		this.mIsSat = isSat;
	}

	public boolean isSun() {
		return mIsSun;
	}

	public void setSun(boolean isSun) {
		this.mIsSun = isSun;
	}

	/*
	 * Parcelable part
	 * @see http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-be-parcelable
	 * @see android.os.Parcelable#describeContents()
	 * @see http://blog.logicexception.com/2012/09/a-parcelable-tutorial-for-android.html
	 */
	public Location(Parcel in) {
		mName = in.readString();
		mIsMon = in.readByte() != 0;
		mIsTue = in.readByte() != 0;
		mIsWed = in.readByte() != 0;
		mIsThu = in.readByte() != 0;
		mIsFri = in.readByte() != 0;
		mIsSat = in.readByte() != 0;
		mIsSun = in.readByte() != 0;
		mWifiState = readState(in.readByte());
		mBluetoothState = readState(in.readByte());
		mNfcState = readState(in.readByte());
		mMobileState = readState(in.readByte());
		mSoundState= readState(in.readByte());
		mVibrationState = readState(in.readByte());
		mIsSMSsendOn = in.readByte() != 0;
		mShouldTurnOff = in.readByte() != 0;
		mRadius = in.readInt();
		mTimeFrom=in.readParcelable(Time.class.getClassLoader());
		mTimeTo=in.readParcelable(Time.class.getClassLoader());
		mLocation = in.readParcelable(null);
		mPriority = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeByte((byte)(mIsMon ? 1 : 0));
		dest.writeByte((byte)(mIsTue ? 1 : 0));
		dest.writeByte((byte)(mIsWed ? 1 : 0));
		dest.writeByte((byte)(mIsThu ? 1 : 0));
		dest.writeByte((byte)(mIsFri ? 1 : 0));
		dest.writeByte((byte)(mIsSat ? 1 : 0));
		dest.writeByte((byte)(mIsSun ? 1 : 0));
		dest.writeByte((byte)(mWifiState.getIdentifier()));
		dest.writeByte((byte)(mBluetoothState.getIdentifier()));
		dest.writeByte((byte)(mNfcState.getIdentifier()));
		dest.writeByte((byte)(mMobileState.getIdentifier()));
		dest.writeByte((byte)(mSoundState.getIdentifier()));
		dest.writeByte((byte)(mVibrationState.getIdentifier()));
		dest.writeByte((byte)(mIsSMSsendOn ? 1 : 0));
		dest.writeByte((byte)(mShouldTurnOff ? 1 : 0));
		dest.writeInt(mRadius);
		dest.writeParcelable(mTimeFrom, flags);
		dest.writeParcelable(mTimeTo, flags);
		dest.writeParcelable(mLocation, flags);
		dest.writeInt(mPriority);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Location createFromParcel(Parcel in) { // NOPMD by waszka on 1/16/15 6:48 PM        											  
            return new Location(in); 				  // It was taken from official Google site.
        }											  // I think this variable's name is ok.

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    
    private ToggleStates readState(byte b) {
    	return (b == 0 ? ToggleStates.Off : 
    			b == 1 ? ToggleStates.On : ToggleStates.Not_specified);
    }
}