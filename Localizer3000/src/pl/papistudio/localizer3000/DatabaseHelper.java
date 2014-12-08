package pl.papistudio.localizer3000;

import android.content.Context;
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

	// public void Get(String locationName)
	// {
	// SQLiteDatabase db = dbHelper.getReadableDatabase();
	//
	// // Define a projection that specifies which columns from the database
	// // you will actually use after this query.
	// String[] projection = {
	// DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME,
	// DatabaseContract.TableDefinition.COLUMN_NAME_LONGITUDE,
	// DatabaseContract.TableDefinition.COLUMN_NAME_LATITUDE,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISMONDAY,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISTUESDAY,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISWEDNESDAY,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISTHURSDAY,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISFRIDAY,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISSATURDAY,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISSUNDAY,
	// DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_HOURS,
	// DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_MINUTES,
	// DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_HOURS,
	// DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_MINUTES,
	// DatabaseContract.TableDefinition.COLUMN_NAME_RADIUS,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISSOUND,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISVIBRATION,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISWIFI,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISBLUETOOTH,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISNFC,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISMOBILEDATA,
	// DatabaseContract.TableDefinition.COLUMN_NAME_ISSMS
	// };
	//
	// String selection =
	// DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME;
	//
	// String[] selectionArgs = {
	// locationName
	// };
	//
	// Cursor c = db.query(
	// DatabaseContract.TableDefinition.TABLE_NAME, // The table to query
	// projection, // The columns to return
	// selection, // The columns for the WHERE clause
	// selectionArgs, // The values for the WHERE clause
	// null, // don't group the rows
	// null, // don't filter by row groups
	// null // The sort order
	// );
	// }
	//
	// public void Add(String name, double Longitude, double Latitude,
	// boolean isMon, boolean isTue, boolean isWed, boolean isThu,
	// boolean isFri, boolean isSat, boolean isSun,
	// int timeFromHours, int timeFromMinutes,
	// int timeToHours, int timeToMinutes,
	// int radius,
	// boolean isSoundOn, boolean isVibrationOn, boolean isWifiOn, boolean
	// isBluetoothOn,
	// boolean isNfcOn, boolean isMobileData, boolean isSMSsendOn)
	// {
	// SQLiteDatabase db = dbHelper.getWritableDatabase();
	//
	// //Create a new map of values, where column names are the keys
	// ContentValues values = new ContentValues();
	//
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_LOCATION_NAME,
	// name);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_LATITUDE,
	// Latitude);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_LONGITUDE,
	// Longitude);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISMONDAY,
	// isMon==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISTUESDAY,
	// isTue==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISWEDNESDAY,
	// isWed==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISTHURSDAY,
	// isThu==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISFRIDAY,
	// isFri==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSATURDAY,
	// isSat==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSUNDAY,
	// isSun==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_HOURS,
	// timeFromHours);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_TIMEFROM_MINUTES,
	// timeFromMinutes);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_HOURS,
	// timeToHours);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_TIMETO_MINUTES,
	// timeToMinutes);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_RADIUS, radius);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSOUND,
	// isSoundOn==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISVIBRATION,
	// isVibrationOn==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISWIFI,
	// isWifiOn==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISBLUETOOTH,
	// isBluetoothOn==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISNFC,
	// isNfcOn==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISMOBILEDATA,
	// isMobileData==true?1:0);
	// values.put(DatabaseContract.TableDefinition.COLUMN_NAME_ISSMS,
	// isSMSsendOn==true?1:0);
	//
	//
	//
	// //Insert the new row, returning the primary key value of the new row
	// long newRowId;
	// newRowId = db.insert(
	// DatabaseContract.TableDefinition.TABLE_NAME,
	// null,
	// values);
	// }
	//
}
