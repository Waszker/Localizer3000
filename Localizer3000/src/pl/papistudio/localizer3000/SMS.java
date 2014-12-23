package pl.papistudio.localizer3000;



public class SMS {
	/******************/
	/*   VARIABLES    */
	/******************/
	private int uniqueIdNumber;
	private String receiverName;
	private int receiverNumber;
	private String messageText;
	private Location locationToSend;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	public SMS() {
		
	}
	
	public SMS(String name, int number, String text, Location location) {
		uniqueIdNumber = -1;
		receiverName = name;
		receiverNumber = number;
		messageText = text;
		locationToSend = location;
	}
	
	public String getName() {
		return locationToSend.getName() + " : " + receiverNumber;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public int getReceiverNumber() {
		return receiverNumber;
	}

	public void setReceiverNumber(int receiverNumber) {
		this.receiverNumber = receiverNumber;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Location getLocationToSend() {
		return locationToSend;
	}

	public void setLocationToSend(Location locationToSend) {
		this.locationToSend = locationToSend;
	}

	public int getUniqueIdNumber() {
		return uniqueIdNumber;
	}

	public void setUniqueIdNumber(int uniqueIdNumber) {
		this.uniqueIdNumber = uniqueIdNumber;
	}

}
