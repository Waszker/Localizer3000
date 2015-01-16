package pl.papistudio.localizer3000;

import android.provider.BaseColumns;

public class DatabaseContract {

	public DatabaseContract() {}
	
	public static abstract class TableLocationDefinition implements BaseColumns {
        public static final String TABLE_NAME = "LocationsTable";
        public static final String COLUMN_NAME_LOCATION_NAME = "Name";
        public static final String COLUMN_NAME_LONGITUDE = "Longitude";
        public static final String COLUMN_NAME_LATITUDE = "Latitude";
        public static final String COLUMN_NAME_ISMONDAY = "isMonday";
        public static final String COLUMN_NAME_ISTUESDAY = "isTuesday";
        public static final String COLUMN_NAME_ISWEDNESDAY = "isWednesday";
        public static final String COLUMN_NAME_ISTHURSDAY = "isThursday";
        public static final String COLUMN_NAME_ISFRIDAY = "isFriday";
        public static final String COLUMN_NAME_ISSATURDAY = "isSaturday";
        public static final String COLUMN_NAME_ISSUNDAY = "isSunday";
        public static final String COLUMN_NAME_TIMEFROM_HOURS = "timeFromHours";
        public static final String COLUMN_NAME_TIMEFROM_MINUTES = "timeFromMinutes";
        public static final String COLUMN_NAME_TIMETO_HOURS = "timeToHours";
        public static final String COLUMN_NAME_TIMETO_MINUTES = "timeToMinutes";
        public static final String COLUMN_NAME_RADIUS = "Radius";
        public static final String COLUMN_NAME_PRIORITY = "Priority";
        public static final String COLUMN_NAME_ISSOUND = "isSound";
        public static final String COLUMN_NAME_ISVIBRATION = "isVibratiom";
        public static final String COLUMN_NAME_ISWIFI = "isWifi";
        public static final String COLUMN_NAME_ISBLUETOOTH = "isBluetooth";
        public static final String COLUMN_NAME_ISNFC = "isNfc";
        public static final String COLUMN_NAME_ISMOBILEDATA = "isMobileData";
        public static final String COLUMN_NAME_ISSMS = "isSMS";
        public static final String COLUMN_NAME_SHOULD_TURN_OFF = "shouldTurnOff";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String REAL_TYPE = " REAL";
        private static final String COMMA_SEP = ", ";
        
        public static final String SQL_CREATE_TABLE  =
            "CREATE TABLE " 
            + TableLocationDefinition.TABLE_NAME + " (" +
            TableLocationDefinition._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            TableLocationDefinition.COLUMN_NAME_LOCATION_NAME + TEXT_TYPE + COMMA_SEP + 
            TableLocationDefinition.COLUMN_NAME_LONGITUDE + REAL_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISMONDAY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISTUESDAY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISWEDNESDAY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISTHURSDAY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISFRIDAY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISSATURDAY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISSUNDAY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_TIMEFROM_HOURS + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_TIMEFROM_MINUTES + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_TIMETO_HOURS + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_TIMETO_MINUTES + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_RADIUS + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_PRIORITY + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISSOUND + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISVIBRATION + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISWIFI + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISBLUETOOTH + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISNFC + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISMOBILEDATA + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_ISSMS + INTEGER_TYPE + COMMA_SEP +
	        TableLocationDefinition.COLUMN_NAME_SHOULD_TURN_OFF + INTEGER_TYPE +         
            " )";

        public static final String SQL_DELETE_LOCATION_ENTRIES =
            "DROP TABLE IF EXISTS " + TableLocationDefinition.TABLE_NAME;
               
    }
	
	public static abstract class TableSMSDefinition implements BaseColumns {
        public static final String TABLE_NAME = "SMSTable";
        public static final String COLUMN_NAME_RECEIVER_NUMBER = "Number";
        public static final String COLUMN_NAME_MESSAGE_TEXT = "Text";
        public static final String COLUMN_NAME_LOCATION_NAME = "Location";
        public static final String COLUMN_NAME_IS_ONE_TIME = "IsOneTime";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ", ";
        
        public static final String SQL_CREATE_TABLE  =
            "CREATE TABLE " 
            + TableSMSDefinition.TABLE_NAME + "(" +
            TableSMSDefinition._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            TableSMSDefinition.COLUMN_NAME_RECEIVER_NUMBER + TEXT_TYPE + COMMA_SEP + 
            TableSMSDefinition.COLUMN_NAME_MESSAGE_TEXT + TEXT_TYPE + COMMA_SEP +
	        TableSMSDefinition.COLUMN_NAME_LOCATION_NAME + TEXT_TYPE + COMMA_SEP +
	        TableSMSDefinition.COLUMN_NAME_IS_ONE_TIME + INTEGER_TYPE +           
            ")";       
        
        public static final String SQL_DELETE_SMS_ENTRIES =
            "DROP TABLE IF EXISTS " + TableSMSDefinition.TABLE_NAME;
    }
}




