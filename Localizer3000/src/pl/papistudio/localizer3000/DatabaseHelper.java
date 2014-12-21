package pl.papistudio.localizer3000;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	/******************/
	/*   VARIABLES    */
	/******************/
	private static DatabaseHelper dbInstance;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Locations.db";

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
		if(dbInstance == null) {
			dbInstance = new DatabaseHelper(context);
		}
		return dbInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseContract.TableDefinition.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * Deletes all rows in database with location name identical
	 * to passed location's name.
	 * @param locationToDelete
	 * @return boolean if deletion completed succesfully
	 */
	public boolean deleteLocationAt(Location locationToDelete) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(DatabaseContract.TableDefinition.TABLE_NAME,
				DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME
						+ "='" + locationToDelete.getName() + "'", null) > 0;
	}

	/**
	 * Function returns list of all location objects
	 * stored in database.
	 * @return list of locations
	 */
	public List<Location> getAllLocations() {
		String selectQuery = "SELECT  * FROM "
				+ DatabaseContract.TableDefinition.TABLE_NAME;
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
				+ DatabaseContract.TableDefinition.TABLE_NAME
				+ " WHERE " + DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME 
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
		ContentValues values = createValuesFromLocation(location);
		long newRowId = db.insert(DatabaseContract.TableDefinition.TABLE_NAME,
				null, values);

		return newRowId;
	}
	
	public void updateLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createValuesFromLocation(location);
		db.update(DatabaseContract.TableDefinition.TABLE_NAME, values, 
				  DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME + 
				  "=\"" + location.getName() + "\"", null);
	}
	
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	private Location createLocationFromCursor(Cursor c) {
		Location newLocation = new Location(c.getString(1));
		android.location.Location GeoLocation = new android.location.Location("");
		GeoLocation.setLongitude(c.getDouble(2));
		GeoLocation.setLatitude(c.getDouble(3));
		newLocation.setLocation(GeoLocation);
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
		newLocation.setSoundOn(c.getInt(16) == 0 ? false : true);
		newLocation.setVibrationOn(c.getInt(17) == 0 ? false : true);
		newLocation.setWifiOn(c.getInt(18) == 0 ? false : true);
		newLocation.setBluetoothOn(c.getInt(19) == 0 ? false : true);
		newLocation.setNfcOn(c.getInt(20) == 0 ? false : true);
		newLocation.setMobileData(c.getInt(21) == 0 ? false : true);
		newLocation.setSMSsendOn(c.getInt(22) == 0 ? false : true);
		
		return newLocation;
	}
	
	private ContentValues createValuesFromLocation(Location location) {
		ContentValues values = new ContentValues();

		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME,
				location.getName());
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_LATITUDE,
				location.getLocation().getLatitude());
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_LONGITUDE,
				location.getLocation().getLongitude());
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISMONDAY,
				location.isMon() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISTUESDAY,
				location.isTue() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISWEDNESDAY,
				location.isWed() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISTHURSDAY,
				location.isThu() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISFRIDAY,
				location.isFri() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSATURDAY,
				location.isSat() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSUNDAY,
				location.isSun() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_HOURS,
				location.getTimeFrom().getHour());
		values.put(
				DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_MINUTES,
				location.getTimeFrom().getMinute());
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_HOURS,
				location.getTimeTo().getHour());
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_MINUTES,
				location.getTimeTo().getMinute());
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_RADIUS,
				location.getRadius());
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSOUND,
				location.isSoundOn() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISVIBRATION,
				location.isVibrationOn() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISWIFI,
				location.isWifiOn() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISBLUETOOTH,
				location.isBluetoothOn() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISNFC,
				location.isNfcOn() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISMOBILEDATA,
				location.isMobileData() == true ? 1 : 0);
		values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSMS,
				location.isSMSsendOn() == true ? 1 : 0);
		
		return values;
	}
}
