package pl.papistudio.localizer3000;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Locations.db";

	// Create a helper object to create, open, and/or manage a database.
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseContract.TableDefinition.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public boolean DeleteLocationAt(Location locationToDelete) {
		
		SQLiteDatabase db = this.getWritableDatabase();		
		return db.delete(DatabaseContract.TableDefinition.TABLE_NAME, DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME + "=" + locationToDelete.getName(), null)>0;
	}

	public List<Location> getAllLocations() {

		List<Location> locationList = new ArrayList<>();
		String selectQuery = "SELECT  * FROM "
				+ DatabaseContract.TableDefinition.TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				Location newLocation = new Location(c.getString(1));
				android.location.Location GeoLocation = new android.location.Location(
						"");
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
				newLocation.setNfcOn(c.getInt(22) == 0 ? false : true);

				locationList.add(newLocation);
			} while (c.moveToNext());
		}

		return locationList;
	}

	public Location GetLocationAt(String locationName) {
		SQLiteDatabase db = this.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
				DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME,
				DatabaseContract.TableDefinition.COLUMN_NAME_LONGITUDE,
				DatabaseContract.TableDefinition.COLUMN_NAME_LATITUDE,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISMONDAY,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISTUESDAY,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISWEDNESDAY,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISTHURSDAY,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISFRIDAY,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISSATURDAY,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISSUNDAY,
				DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_HOURS,
				DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_MINUTES,
				DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_HOURS,
				DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_MINUTES,
				DatabaseContract.TableDefinition.COLUMN_NAME_RADIUS,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISSOUND,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISVIBRATION,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISWIFI,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISBLUETOOTH,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISNFC,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISMOBILEDATA,
				DatabaseContract.TableDefinition.COLUMN_NAME_ISSMS };

		String selection = DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME
				+ "=?";

		String[] selectionArgs = { locationName };

		Cursor c = db.query(DatabaseContract.TableDefinition.TABLE_NAME, // The
																			// table
																			// to
																			// query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		if (c != null)
			c.moveToFirst();

		Location newLocation = new Location(c.getString(0));
		android.location.Location GeoLocation = new android.location.Location(
				"");
		GeoLocation.setLongitude(c.getDouble(1));
		GeoLocation.setLatitude(c.getDouble(2));
		newLocation.setLocation(GeoLocation);
		newLocation.setMon(c.getInt(3) == 0 ? false : true);
		newLocation.setTue(c.getInt(4) == 0 ? false : true);
		newLocation.setWed(c.getInt(5) == 0 ? false : true);
		newLocation.setThu(c.getInt(6) == 0 ? false : true);
		newLocation.setFri(c.getInt(7) == 0 ? false : true);
		newLocation.setSat(c.getInt(8) == 0 ? false : true);
		newLocation.setSun(c.getInt(9) == 0 ? false : true);
		newLocation.setTimeFrom(new Time(c.getInt(10), c.getInt(11)));
		newLocation.setTimeTo(new Time(c.getInt(12), c.getInt(13)));
		newLocation.setRadius(c.getInt(14));
		newLocation.setSoundOn(c.getInt(15) == 0 ? false : true);
		newLocation.setVibrationOn(c.getInt(16) == 0 ? false : true);
		newLocation.setWifiOn(c.getInt(17) == 0 ? false : true);
		newLocation.setBluetoothOn(c.getInt(18) == 0 ? false : true);
		newLocation.setNfcOn(c.getInt(19) == 0 ? false : true);
		newLocation.setMobileData(c.getInt(20) == 0 ? false : true);
		newLocation.setNfcOn(c.getInt(21) == 0 ? false : true);

		return newLocation;
	}

	public long AddLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();

		// Create a new map of values, where column names are the keys
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

		// Insert the new row, returning the primary key value of the new row
		long newRowId = db.insert(DatabaseContract.TableDefinition.TABLE_NAME,
				null, values);

		return newRowId;
	}

}
