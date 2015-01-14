package pl.papistudio.localizer3000;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseHelper extends SQLiteOpenHelper {
	/******************/
	/*   VARIABLES    */
	/******************/
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "Locations.db";
	private static DatabaseHelper dbInstance;
	private static int currentPriorityNumber;

	/******************/
	/*   VARIABLES    */
	/******************/
	/**
	 * Returns instance of DatabaseHelper class.
	 * This class is a singleton.
	 * 
	 * @see http://stackoverflow.com/questions/6905524/using-singleton-design-pattern-for-sqlitedatabase
	 * @param context
	 * @return instance of class
	 */
	public static DatabaseHelper getInstance(Context context) {	
		if(dbInstance == null)
		{
			dbInstance = new DatabaseHelper(context);
		}
		return dbInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseContract.TableLocationDefinition.SQL_CREATE_TABLE);
		db.execSQL(DatabaseContract.TableSMSDefinition.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// load all object from database to lists
		// drop old tables
		// create new tables and fill 'em with updated values
		List<Location> locations = getAllLocations(db);
		List<SMS> smses = getAllSMS(db);
		
		if(oldVersion == 1)
		{
			for(SMS s : smses)
				s.setIsOneTimeUse(true);
		}
		
		db.execSQL(DatabaseContract.TableLocationDefinition.SQL_DELETE_LOCATION_ENTRIES);
		db.execSQL(DatabaseContract.TableSMSDefinition.SQL_DELETE_SMS_ENTRIES);
		db.execSQL(DatabaseContract.TableLocationDefinition.SQL_CREATE_TABLE);
		db.execSQL(DatabaseContract.TableSMSDefinition.SQL_CREATE_TABLE);
		
		// fill new tables
		for(Location l : locations)
		{
			ContentValues values = createValuesFromLocation(l);
			db.insert(DatabaseContract.TableLocationDefinition.TABLE_NAME,
					null, values);
		}
		for(SMS s : smses)
		{
			ContentValues values = createValuesFromSMS(s);
			db.insert(DatabaseContract.TableSMSDefinition.TABLE_NAME,
					null, values);
		}
	}

	/**
	 * Deletes all rows in database with location name identical
	 * to passed location's name.
	 * @param locationToDelete
	 * @return boolean if deletion completed succesfully
	 */
	public boolean deleteLocationAt(Location locationToDelete) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(DatabaseContract.TableLocationDefinition.TABLE_NAME,
				DatabaseContract.TableLocationDefinition.COLUMN_NAME_LOCATION_NAME
						+ "='" + locationToDelete.getName() + "'", null) > 0;
	}

	/**
	 * Function returns list of all location objects
	 * stored in database.
	 * @return list of locations
	 */
	public List<Location> getAllLocations() {
		String selectQuery = "SELECT  * FROM "
				+ DatabaseContract.TableLocationDefinition.TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		List<Location> locationList = new ArrayList<>();
		if (c.moveToFirst()) {
			do {
				locationList.add(createLocationFromCursor(c));
			} while (c.moveToNext());
		}
		c.close();

		return locationList;
	}

	/**
	 * Returns location with the chosen name.
	 * @param locationName
	 * @return location object found in database or null if object was not found
	 */
	public Location getLocation(String locationName) {
		String selectQuery = "SELECT  * FROM "
				+ DatabaseContract.TableLocationDefinition.TABLE_NAME
				+ " WHERE " + DatabaseContract.TableLocationDefinition.COLUMN_NAME_LOCATION_NAME 
				+ "=\"" + locationName + "\"";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		Location newLocation = null;
		if (c.moveToFirst())
		{			
			newLocation = createLocationFromCursor(c);
			c.close();
		}
		
		return newLocation;
	}

	/**
	 * Adds location object to database
	 * @param location
	 * @return id of the put object
	 */
	public long addLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();
		location.setPriority(currentPriorityNumber++);
		ContentValues values = createValuesFromLocation(location);
		long newRowId = db.insert(DatabaseContract.TableLocationDefinition.TABLE_NAME,
				null, values);

		return newRowId;
	}
	
	/**
	 * Updates database record. Original location name is needed because
	 * it is also primary key.
	 * @param location
	 * @param originalName
	 */
	public void updateLocation(Location location, String originalName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createValuesFromLocation(location);
		db.update(DatabaseContract.TableLocationDefinition.TABLE_NAME, values, 
				  DatabaseContract.TableLocationDefinition.COLUMN_NAME_LOCATION_NAME + 
				  "=\"" + originalName + "\"", null);
	}
	
	/**
	 * Adds SMS to database. When adding database
	 * assigns unique id to SMS.
	 * @param sms
	 */
	public void addSMS(SMS sms) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createValuesFromSMS(sms);
		db.insert(DatabaseContract.TableSMSDefinition.TABLE_NAME, null, values);	
	}
	
	/**
	 * Updating SMS with specific unique id.
	 * @param sms
	 */
	public void updateSMS(SMS sms) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createValuesFromSMS(sms);
		db.update(DatabaseContract.TableSMSDefinition.TABLE_NAME, values, 
				  DatabaseContract.TableSMSDefinition._ID + 
				  "=\"" + sms.getUniqueIdNumber() + "\"", null);
	}
	
	/**
	 * Deletes SMS with specific unique id.
	 * @param smsToDelete
	 * @return has SMS been deleted.
	 */
	public boolean deleteSMSAt(SMS smsToDelete) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(DatabaseContract.TableSMSDefinition.TABLE_NAME,
		DatabaseContract.TableSMSDefinition._ID
		+ "=" + smsToDelete.getUniqueIdNumber() , null) > 0;
	}
	
	/**
	 * Returns list of all SMS objects in database.
	 * @return list of SMS
	 */
	public List<SMS> getAllSMS() {
		String selectQuery = "SELECT  * FROM "
				+ DatabaseContract.TableSMSDefinition.TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		List<SMS> smsList = new ArrayList<>();
		if (c.moveToFirst()) {
			do {
				smsList.add(createSMSFromCursor(c));
			} while (c.moveToNext());
		}
		c.close();

		return smsList;
	}
	
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		getBiggestPriorityNumber();
	}
	
	private void getBiggestPriorityNumber() {
		currentPriorityNumber = getAllLocations().size();
	}
	
	private Location createLocationFromCursor(Cursor c) {
		Location newLocation = new Location(c.getString(1));
		android.location.Location geoLocation = new android.location.Location("");
		geoLocation.setLongitude(c.getDouble(2));
		geoLocation.setLatitude(c.getDouble(3));
		newLocation.setLocation(geoLocation);
		newLocation.setMon(c.getInt(4) == 0 ? false : true);
		newLocation.setTue(c.getInt(5) == 0 ? false : true);
		newLocation.setWed(c.getInt(6) == 0 ? false : true);
		newLocation.setThu(c.getInt(7) == 0 ? false : true);
		newLocation.setFri(c.getInt(8) == 0 ? false : true);
		newLocation.setSat(c.getInt(9) == 0 ? false : true);
		newLocation.setSun(c.getInt(10) == 0 ? false : true);
		newLocation.setTimeFrom(new Time(c.getInt(11), c.getInt(12)));
		newLocation.setTimeTo(new Time(c.getInt(13), c.getInt(14)));
		newLocation.setRadius(c.getInt(15));
		newLocation.setPriority(c.getInt(16));
		newLocation.setSoundOn(c.getInt(17) == 0 ? false : true);
		newLocation.setVibrationOn(c.getInt(18) == 0 ? false : true);
		newLocation.setWifiOn(c.getInt(19) == 0 ? false : true);
		newLocation.setBluetoothOn(c.getInt(20) == 0 ? false : true);
		newLocation.setNfcOn(c.getInt(21) == 0 ? false : true);
		newLocation.setMobileData(c.getInt(22) == 0 ? false : true);
		newLocation.setSMSsendOn(c.getInt(23) == 0 ? false : true);
		
		return newLocation;
	}
	
	private ContentValues createValuesFromLocation(Location location) {
		ContentValues values = new ContentValues();

		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_LOCATION_NAME,
				location.getName());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_LATITUDE,
				location.getLocation().getLatitude());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_LONGITUDE,
				location.getLocation().getLongitude());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISMONDAY,
				location.isMon() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISTUESDAY,
				location.isTue() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISWEDNESDAY,
				location.isWed() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISTHURSDAY,
				location.isThu() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISFRIDAY,
				location.isFri() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISSATURDAY,
				location.isSat() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISSUNDAY,
				location.isSun() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_TIMEFROM_HOURS,
				location.getTimeFrom().getHour());
		values.put(
				DatabaseContract.TableLocationDefinition.COLUMN_NAME_TIMEFROM_MINUTES,
				location.getTimeFrom().getMinute());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_TIMETO_HOURS,
				location.getTimeTo().getHour());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_TIMETO_MINUTES,
				location.getTimeTo().getMinute());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_RADIUS,
				location.getRadius());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_PRIORITY,
				location.getPriority());
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISSOUND,
				location.isSoundOn() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISVIBRATION,
				location.isVibrationOn() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISWIFI,
				location.isWifiOn() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISBLUETOOTH,
				location.isBluetoothOn() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISNFC,
				location.isNfcOn() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISMOBILEDATA,
				location.isMobileData() ? 1 : 0);
		values.put(DatabaseContract.TableLocationDefinition.COLUMN_NAME_ISSMS,
				location.isSMSsendOn() ? 1 : 0);
		
		return values;
	}
	
	private SMS createSMSFromCursor(Cursor c) {
		SMS newSMS = new SMS();
		
		newSMS.setUniqueIdNumber(c.getInt(0));
		newSMS.setReceiverNumber(Integer.parseInt(c.getString(1)));
		newSMS.setMessageText(c.getString(2));
		newSMS.setLocationToSend(new Location(c.getString(3)));
		newSMS.setIsOneTimeUse((c.getInt(4)) == 1);
		
		return newSMS;
	}
	
	private ContentValues createValuesFromSMS(SMS sms) {
		ContentValues values = new ContentValues();

		values.put(DatabaseContract.TableSMSDefinition.COLUMN_NAME_RECEIVER_NUMBER,
				String.valueOf(sms.getReceiverNumber()));
		values.put(DatabaseContract.TableSMSDefinition.COLUMN_NAME_MESSAGE_TEXT,
				sms.getMessageText());
		values.put(DatabaseContract.TableSMSDefinition.COLUMN_NAME_LOCATION_NAME,
				sms.getLocationToSend().getName());
		values.put(DatabaseContract.TableSMSDefinition.COLUMN_NAME_IS_ONE_TIME,
				(sms.isOneTimeUse() ? 1 : 0));
		
		return values;
	}
	
	private List<Location> getAllLocations(SQLiteDatabase db) {
		// This function is used only during database upgrades
		// as it uses previous database reference
		String selectQuery = "SELECT  * FROM "
				+ DatabaseContract.TableLocationDefinition.TABLE_NAME;
		Cursor c = db.rawQuery(selectQuery, null);

		List<Location> locationList = new ArrayList<>();
		if (c.moveToFirst()) {
			do {
				locationList.add(createLocationFromCursor(c));
			} while (c.moveToNext());
		}
		c.close();

		return locationList;
	}
	
	private List<SMS> getAllSMS(SQLiteDatabase db) {
		// This function is used only during database upgrades
		// as it uses previous database reference
		String selectQuery = "SELECT  * FROM "
				+ DatabaseContract.TableSMSDefinition.TABLE_NAME;
		Cursor c = db.rawQuery(selectQuery, null);

		List<SMS> smsList = new ArrayList<>();
		if (c.moveToFirst()) {
			do {
				smsList.add(createSMSFromCursor(c));
			} while (c.moveToNext());
		}
		c.close();

		return smsList;
	}
}
