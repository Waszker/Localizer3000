package pl.papistudio.localizer3000;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Location class represents object created by user
 * containing all information about place and phone status there.
 * 
 * @author PapiTeam
 *
 */
public class Location implements Parcelable, Comparable<Location> {
	/******************/
	/*   VARIABLES    */
	/******************/
	private String name;
	private boolean isWifiOn, isBluetoothOn, isNfcOn, isMobileData, isSoundOn, isVibrationOn, isSMSsendOn;
	private int radius; //in meters
	private int priority;
	private Time timeFrom, timeTo;
	private boolean isMon, isTue, isWed, isThu, isFri, isSat, isSun;
	private android.location.Location location;
	
	

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
		this.name = name;
		this.isWifiOn = this.isBluetoothOn = this.isNfcOn = this.isMobileData = this.isSoundOn = this.isVibrationOn =true;
		this.isSMSsendOn=false;
		this.radius=100;
		setTimeFrom(new Time(0,0));
		setTimeTo(new Time(23,59));
		setDaysOfWeek(true, true, true, true, true, true, true);
	}
	
	/**
	 * Creates location with specified options enabled.
	 * Location is active on all days of the week.
	 * @param name
	 * @param isWifiOn
	 * @param isBluetoothOn
	 * @param isNfcOn
	 * @param isMobData
	 * @param isSoundOn
	 * @param isVibrationOn
	 * @param radius
	 */
	public Location(String name, boolean isWifiOn, boolean isBluetoothOn,
			boolean isNfcOn, boolean isMobData, boolean isSoundOn,
			boolean isVibrationOn, int radius) {
		this.name = name;
		this.isWifiOn = isWifiOn;
		this.isBluetoothOn = isBluetoothOn;
		this.isNfcOn = isNfcOn;
		this.isMobileData = isMobData;
		this.isSoundOn = isSoundOn;
		this.isVibrationOn = isVibrationOn;
		this.isSMSsendOn = false;
		this.radius = radius;
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
		this.isMon = mon;
		this.isTue = tue;
		this.isWed = wed;
		this.isThu = thu;
		this.isFri = fri;
		this.isSat = sat;
		this.isSun = sun;
	}
	
	/**
	 * Checks if location is applicable during this day
	 * of the week.
	 * @param day of the week (taken from Calendar.get())
	 * @return is location enabled
	 */
	public boolean isLocationEnabled(int day, Time time) {
		boolean isEnabled = false;
		
		switch (day) {
			case Calendar.MONDAY:
				isEnabled = isMon;
				break;
				
			case Calendar.TUESDAY:
				isEnabled = isTue;
				break;
				
			case Calendar.WEDNESDAY:
				isEnabled = isWed;
				break;
				
			case Calendar.THURSDAY:
				isEnabled = isThu;
				break;
				
			case Calendar.FRIDAY:
				isEnabled = isFri;
				break;
				
			case Calendar.SATURDAY:
				isEnabled = isSat;
				break;
				
			case Calendar.SUNDAY:
				isEnabled = isSun;
				break;
	
			default:
				break;
		}
		
		if(timeFrom.compareTo(timeTo) > 0)
		{
			if(time.compareTo(timeFrom) < 0 && time.compareTo(timeTo) > 0)
				isEnabled = false;
		}
		else
		{
			if(time.compareTo(timeFrom) < 0 || time.compareTo(timeTo) > 0)
				isEnabled = false;
		}
		
		return isEnabled;
	}
	
	/**
	 * Compares two location classes.
	 * One class is bigger than the other when it has higher priority value.
	 * @param location
	 * @return 1 if first location has higher priority
	 * 		   0 if two locations have the same priority
	 * 		  -1 if second is bigger
	 */
	public int compareTo(Location location) {
		int result = 0;
		if(this.priority > location.priority)
			result = 1;
		if(this.priority < location.priority)
			result = -1;
		return result;
	}

	/************************/
	/*   GETTERS&SETTERS    */
	/************************/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public android.location.Location getLocation() {
		return location;
	}

	public void setLocation(android.location.Location location) {
		this.location = location;
	}
	
	public boolean isWifiOn() {
		return isWifiOn;
	}

	public void setWifiOn(boolean isWifiOn) {
		this.isWifiOn = isWifiOn;
	}

	public boolean isBluetoothOn() {
		return isBluetoothOn;
	}

	public void setBluetoothOn(boolean isBluetoothOn) {
		this.isBluetoothOn = isBluetoothOn;
	}

	public boolean isNfcOn() {
		return isNfcOn;
	}

	public void setNfcOn(boolean isNfcOn) {
		this.isNfcOn = isNfcOn;
	}

	public boolean isMobileData() {
		return isMobileData;
	}

	public void setMobileData(boolean isMobData) {
		this.isMobileData = isMobData;
	}
	
	public boolean isSoundOn() {
		return isSoundOn;
	}

	public void setSoundOn(boolean isSoundOn) {
		this.isSoundOn = isSoundOn;
	}

	public boolean isVibrationOn() {
		return isVibrationOn;
	}

	public void setVibrationOn(boolean isVibrationOn) {
		this.isVibrationOn = isVibrationOn;
	}

	public boolean isSMSsendOn() {
		return isSMSsendOn;
	}

	public void setSMSsendOn(boolean isSMSsendOn) {
		this.isSMSsendOn = isSMSsendOn;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		if(radius > 0)
			this.radius = radius;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Time getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Time timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Time getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Time timeTo) {
		this.timeTo = timeTo;
	}

	public boolean isMon() {
		return isMon;
	}

	public void setMon(boolean isMon) {
		this.isMon = isMon;
	}

	public boolean isTue() {
		return isTue;
	}

	public void setTue(boolean isTue) {
		this.isTue = isTue;
	}

	public boolean isWed() {
		return isWed;
	}

	public void setWed(boolean isWed) {
		this.isWed = isWed;
	}

	public boolean isThu() {
		return isThu;
	}

	public void setThu(boolean isThu) {
		this.isThu = isThu;
	}

	public boolean isFri() {
		return isFri;
	}

	public void setFri(boolean isFri) {
		this.isFri = isFri;
	}

	public boolean isSat() {
		return isSat;
	}

	public void setSat(boolean isSat) {
		this.isSat = isSat;
	}

	public boolean isSun() {
		return isSun;
	}

	public void setSun(boolean isSun) {
		this.isSun = isSun;
	}

	/*
	 * Parcelable part
	 * @see http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-be-parcelable
	 * @see android.os.Parcelable#describeContents()
	 * @see http://blog.logicexception.com/2012/09/a-parcelable-tutorial-for-android.html
	 */
	public Location(Parcel in) {
		name = in.readString();
		isMon = (in.readByte() != 0 ? true : false);
		isTue = (in.readByte() != 0 ? true : false);
		isWed = (in.readByte() != 0 ? true : false);
		isThu = (in.readByte() != 0 ? true : false);
		isFri = (in.readByte() != 0 ? true : false);
		isSat = (in.readByte() != 0 ? true : false);
		isSun = (in.readByte() != 0 ? true : false);
		isWifiOn = (in.readByte() != 0 ? true : false);
		isBluetoothOn = (in.readByte() != 0 ? true : false);
		isNfcOn = (in.readByte() != 0 ? true : false);
		isMobileData = (in.readByte() != 0 ? true : false);
		isSoundOn= (in.readByte() != 0 ? true : false);
		isVibrationOn = (in.readByte() != 0 ? true : false);
		isSMSsendOn = (in.readByte() != 0 ? true : false);
		radius = (in.readInt());
		timeFrom=(in.readParcelable(Time.class.getClassLoader()));
		timeTo=(in.readParcelable(Time.class.getClassLoader()));
		location = (in.readParcelable(null));
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeByte((byte)(isMon ? 1 : 0));
		dest.writeByte((byte)(isTue ? 1 : 0));
		dest.writeByte((byte)(isWed ? 1 : 0));
		dest.writeByte((byte)(isThu ? 1 : 0));
		dest.writeByte((byte)(isFri ? 1 : 0));
		dest.writeByte((byte)(isSat ? 1 : 0));
		dest.writeByte((byte)(isSun ? 1 : 0));
		dest.writeByte((byte)(isWifiOn ? 1 : 0));
		dest.writeByte((byte)(isBluetoothOn ? 1 : 0));
		dest.writeByte((byte)(isNfcOn ? 1 : 0));
		dest.writeByte((byte)(isMobileData ? 1 : 0));
		dest.writeByte((byte)(isSoundOn ? 1 : 0));
		dest.writeByte((byte)(isVibrationOn ? 1 : 0));
		dest.writeByte((byte)(isSMSsendOn ? 1 : 0));
		dest.writeInt(radius);
		dest.writeParcelable(timeFrom, flags);
		dest.writeParcelable(timeTo, flags);
		dest.writeParcelable(location, flags);

	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Location createFromParcel(Parcel in) {
            return new Location(in); 
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}