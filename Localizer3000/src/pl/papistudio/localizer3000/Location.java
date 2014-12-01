package pl.papistudio.localizer3000;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
	/******************/
	/*   VARIABLES    */
	/******************/
	private String name;
	private boolean isWifiOn, isBluetoothOn, isNfcOn, isMobData;
	private boolean isMon, isTue, isWed, isThu, isFri, isSat, isSun;
	private android.location.Location location;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	/**
	 * Creates location with specified name.
	 * Location by default has all options enabled and is active
	 * on each day of the week.
	 * @param name
	 */
	public Location(String name) {
		this.name = name;
		this.isWifiOn = this.isBluetoothOn = this.isNfcOn = isMobData = true;
		setDaysOfWeek(true, true, true, true, true, true, true);
	}
	
	/**
	 * Creates location with specified options enabled.
	 * Location is active on all days of the week.
	 * @param name
	 * @param isWifiOn
	 * @param isBluetoothOn
	 * @param isNfcOn
	 */
	public Location(String name, boolean isWifiOn, boolean isBluetoothOn, boolean isNfcOn, boolean isMobData) {
		this.name = name;
		this.isWifiOn = isWifiOn;
		this.isBluetoothOn = isBluetoothOn;
		this.isNfcOn = isNfcOn;
		this.isMobData = isMobData;
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
	public void setDaysOfWeek(boolean Mon, boolean Tue, boolean Wed, boolean Thu,
			boolean Fri, boolean Sat, boolean Sun) {
		this.isMon = Mon;
		this.isTue = Tue;
		this.isWed = Wed;
		this.isThu = Thu;
		this.isFri = Fri;
		this.isSat = Sat;
		this.isSun = Sun;
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

	public boolean isMobData() {
		return isMobData;
	}

	public void setMobData(boolean isMobData) {
		this.isMobData = isMobData;
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
		isWifiOn = (in.readByte() != 0 ? true : false);
		isBluetoothOn = (in.readByte() != 0 ? true : false);
		isNfcOn = (in.readByte() != 0 ? true : false);
		isMobData = (in.readByte() != 0 ? true : false);
		location = (in.readParcelable(null));
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeByte((byte)(isWifiOn ? 1 : 0));
		dest.writeByte((byte)(isBluetoothOn ? 1 : 0));
		dest.writeByte((byte)(isNfcOn ? 1 : 0));
		dest.writeByte((byte)(isMobData ? 1 : 0));
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
