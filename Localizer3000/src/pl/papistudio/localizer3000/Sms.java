package pl.papistudio.localizer3000;



public class Sms {
	/******************/
	/*   VARIABLES    */
	/******************/
	private int mUniqueIdNumber;
	private String mReceiverName;
	private int mReceiverNumber;
	private String mMessageText;
	private String mLocationNameToSend;
	private boolean mIsOneTimeUse;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	public Sms() {
		
	}
	
	public Sms(String name, int number, String text, String locationName, boolean isOneTimeUse) {
		this.mUniqueIdNumber = -1;
		this.mReceiverName = name;
		this.mReceiverNumber = number;
		this.mMessageText = text;
		this.mLocationNameToSend = locationName;
		this.mIsOneTimeUse = isOneTimeUse; 
	}
	
	public String getName() {
		return mLocationNameToSend + " : " + mReceiverNumber;
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

	public String getLocationNameToSend() {
		return mLocationNameToSend;
	}

	public void setLocationNameToSend(String locationToSend) {
		this.mLocationNameToSend = locationToSend;
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
