package pl.papistudio.localizer3000;



public class SMS {
	/******************/
	/*   VARIABLES    */
	/******************/
	private int mUniqueIdNumber;
	private String mReceiverName;
	private int mReceiverNumber;
	private String mMessageText;
	private Location mLocationToSend;
	private boolean mIsOneTimeUse;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	public SMS() {
		
	}
	
	public SMS(String name, int number, String text, Location location, boolean isOneTimeUse) {
		this.mUniqueIdNumber = -1;
		this.mReceiverName = name;
		this.mReceiverNumber = number;
		this.mMessageText = text;
		this.mLocationToSend = location;
		this.mIsOneTimeUse = isOneTimeUse; 
	}
	
	public String getName() {
		return mLocationToSend.getName() + " : " + mReceiverNumber;
	}

	public String getReceiverName() {
		return mReceiverName;
	}

	public void setReceiverName(String receiverName) {
		this.mReceiverName = receiverName;
	}

	public int getReceiverNumber() {
		return mReceiverNumber;
	}

	public void setReceiverNumber(int receiverNumber) {
		this.mReceiverNumber = receiverNumber;
	}

	public String getMessageText() {
		return mMessageText;
	}

	public void setMessageText(String messageText) {
		this.mMessageText = messageText;
	}

	public Location getLocationToSend() {
		return mLocationToSend;
	}

	public void setLocationToSend(Location locationToSend) {
		this.mLocationToSend = locationToSend;
	}

	public int getUniqueIdNumber() {
		return mUniqueIdNumber;
	}

	public void setUniqueIdNumber(int uniqueIdNumber) {
		this.mUniqueIdNumber = uniqueIdNumber;
	}

	public boolean isOneTimeUse() {
		return mIsOneTimeUse;
	}

	public void setIsOneTimeUse(boolean mIsOneTimeUse) {
		this.mIsOneTimeUse = mIsOneTimeUse;
	}
}
