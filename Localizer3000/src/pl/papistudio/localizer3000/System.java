package pl.papistudio.localizer3000;

import java.util.Calendar;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.util.Log;

/**
 * Class reacting to entering new location area
 * and changing system parameters. 
 * 
 * @author PapiTeam
 *
 */
public class System {
	/******************/
	/*   VARIABLES    */
	/******************/
	private static LocationService service;
	private static String TAG = "Location Service System module";
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	public static void reactToLocationChange(android.location.Location location, Context context, LocationService service) {
		System.service = service;
		Location nearestLocation = findNearestLocation(location, context);		
		if(nearestLocation != null)
		{
			setWifi(nearestLocation.isWifiOn());
			setBluetooth(nearestLocation.isBluetoothOn());
//			setMobileData(nearestLocation.isMobileData());
			setSound(nearestLocation.isSoundOn(), nearestLocation.isVibrationOn());
//			sendSMS();
			
			
			// TODO: Delete this (it is only for debugging purposes!)
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);
		}
		else {
			Log.d("System Location", "There is no good location to apply...");			
		}
	}	
	
	private static Location findNearestLocation(android.location.Location location, Context context) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
		List<Location> list = dbHelper.getAllLocations();
		double minDistance = Double.MAX_VALUE;
		Location bestSuitedLocation = null;
		
		for(Location l : list)
		{
			android.location.Location loc = l.getLocation();			
			int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			Time time = new Time(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 
								 Calendar.getInstance().get(Calendar.MINUTE));
			
			if(l.isLocationEnabled(day, time) 						// checks if location is active during this time (day + hours)
					&& minDistance > (loc.distanceTo(location))		// checks if newer location is better than prevoius one
					&& isLocationValidToApplySettings(l, location))	// check if we are inside "location circle"
			{
				Log.d("System Location", "Good location is: " + l.getName());
				minDistance = (loc.distanceTo(location));
				bestSuitedLocation = l;
			}
		}
		
		return bestSuitedLocation;
	}
	
	private static boolean isLocationValidToApplySettings(Location nearestLocation, android.location.Location location) {
		return nearestLocation != null
				&& nearestLocation.getLocation().distanceTo(location) <= nearestLocation.getRadius();
	}
	
	private static void setWifi(boolean isEnabled) {
		try {
			((WifiManager)service.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(isEnabled);
		} catch(Exception e) {
			Log.e(TAG, "Probably no WiFi module\n" + e.getLocalizedMessage());
		}
	}
	
	private static void setBluetooth(boolean isEnabled) {
		try {
			if(isEnabled)
				 BluetoothAdapter.getDefaultAdapter().enable();
			else
				BluetoothAdapter.getDefaultAdapter().disable();
		} catch (Exception e) {
			Log.e(TAG, "Probably no bluetooth module\n" + e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void setSound(boolean isSoundEnabled, boolean isVibrationEnabled) {
		AudioManager aManager=(AudioManager)service.getSystemService(Context.AUDIO_SERVICE);
		if(!isSoundEnabled && !isVibrationEnabled) 
			aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		if(isVibrationEnabled && !isSoundEnabled)
			aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		if(!isVibrationEnabled && isSoundEnabled)
		{
			aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			aManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
		            AudioManager.VIBRATE_SETTING_OFF);
		}
		if(isVibrationEnabled && isSoundEnabled)
			aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
	
//	private static void setMobileData(boolean isEnabled) {
//		Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//		ComponentName cn = new ComponentName("com.android.phone","com.android.phone.Settings");
//		intent.setComponent(cn);
//		service.getApplication().startActivity(intent);
//	}
	
//	private static void sendSMS() {
//		SmsManager.getDefault().sendTextMessage("506743135", null, "Yoł!", null, null);		
//	}
}
