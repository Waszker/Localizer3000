package pl.papistudio.localizer3000;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.honorato.multistatetogglebutton.MultiStateToggleButton.ToggleStates;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.telephony.SmsManager;
import android.util.Log;
import de.greenrobot.event.EventBus;

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
	private static Location currentlyActiveLocation;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	/**
	 * Function searches for saved location that can be applied to current user location.
	 * It selects saved location that covers current user position.
	 * Date and hours are also taken into account.
	 * If there are multiple such locations, the one with lowest priority is taken.
	 *  
	 * @param location
	 * @param context
	 * @param service asking for reaction
	 */
	public static void reactToLocationChange(android.location.Location location, Context context, LocationService service) {
		System.service = service;
		Location nearestLocation = findBestSuitedLocation(location, context);
		if(nearestLocation != null)
		{
			updatePhoneStatusForFoundLocation(nearestLocation);			
			currentlyActiveLocation = nearestLocation;
		}
		else
			Log.d("System Location", "There is no good location to apply...");
	}
	
	private static void updatePhoneStatusForFoundLocation(Location nearestLocation) {
		EventBus.getDefault().post(nearestLocation);
		boolean hasLocationChanged = currentlyActiveLocation == null || 
									 !(currentlyActiveLocation.getName().contentEquals(nearestLocation.getName()));
		
		setWifi(nearestLocation.isWifiOn());
		setBluetooth(nearestLocation.isBluetoothOn());
		setMobileData(nearestLocation.isMobileData());
		setSound(nearestLocation.isSoundOn(), nearestLocation.isVibrationOn());
		sendSMSes(nearestLocation, hasLocationChanged);
	}
	
	private static Location findBestSuitedLocation(android.location.Location location, Context context) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
		List<Location> list = dbHelper.getAllLocations();
		Location bestSuitedLocation = null;
		
		for(Location l : list)
		{		
			int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			Time time = new Time(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 
								 Calendar.getInstance().get(Calendar.MINUTE));
			
			if(l.isLocationEnabled(day, time) 								// checks if location is active during this time (day + hours)
					&& isLocationValidToApplySettings(l, location)			// check if we are inside "location circle"
					&& (bestSuitedLocation == null 							// check if new location has lower priority
						|| l.getPriority() < bestSuitedLocation.getPriority()))
			{
				Log.d("System Location", "Good location is: " + l.getName());
				bestSuitedLocation = l;
			}
		}
		
		return bestSuitedLocation;
	}
	
	private static boolean isLocationValidToApplySettings(Location nearestLocation, android.location.Location location) {
		return nearestLocation != null && location != null
				&& nearestLocation.getLocation().distanceTo(location) <= nearestLocation.getRadius();
	}
	
	private static void setWifi(ToggleStates state) {
		try {
			if(state == ToggleStates.On || state == ToggleStates.Off)
			((WifiManager)service.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(state == ToggleStates.On);
		} catch(Exception e) {
			Log.e(TAG, "Probably no WiFi module\n" + e.getLocalizedMessage());
		}
	}
	
	private static void setBluetooth(ToggleStates state) {
		try {
			if(state == ToggleStates.On)
				 BluetoothAdapter.getDefaultAdapter().enable();
			else if(state == ToggleStates.Off)
				BluetoothAdapter.getDefaultAdapter().disable();
		} catch (Exception e) {
			Log.e(TAG, "Probably no bluetooth module\n" + e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void setSound(ToggleStates stateSound, ToggleStates stateVibration) {
		AudioManager aManager=(AudioManager)service.getSystemService(Context.AUDIO_SERVICE);
		if(stateSound == ToggleStates.Off && stateVibration == ToggleStates.Off) 
			aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		if(stateSound == ToggleStates.Off && stateVibration == ToggleStates.On)
			aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		if(stateSound == ToggleStates.On && stateVibration == ToggleStates.Off)
		{
			aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			aManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, // this doesn't work on newer API
		            AudioManager.VIBRATE_SETTING_OFF);
		}
		if(stateSound == ToggleStates.On && stateVibration == ToggleStates.On)
			aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
	
	private static void setMobileData(ToggleStates state) {
		if(state != ToggleStates.Not_specified && findBinary("su"))
		{ // if we are here, problably we are rooted ;)
			try {
				if(state == ToggleStates.On)
					Runtime.getRuntime().exec("su -c svc data enable");
				else if(state == ToggleStates.Off)
					Runtime.getRuntime().exec("su -c svc data disable");
			} catch (IOException e) {
				Log.e(TAG, "Error getting root priviledges");
			}
		}
		else
			Log.e(TAG, "No root...");
			
	}
	
	private static void sendSMSes(Location location, boolean hasLocationChanged) {
		List<SMS> smsList = DatabaseHelper.getInstance(service).getAllSMS();
		
		for(SMS s : smsList)
		{
			if(s.getLocationToSend().getName().contentEquals(location.getName()))
			{
				if(hasLocationChanged)
				{
					SmsManager.getDefault().sendTextMessage(
													String.valueOf(s.getReceiverNumber()), 
													null, s.getMessageText(), null, null
													);

					if(s.isOneTimeUse())
						DatabaseHelper.getInstance(service).deleteSMSAt(s);
				}
			}
		}	
	}
	
	private static boolean findBinary(String binaryName) {
	    boolean found = false;
	    String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
	    
        for (String where : places) 
        {
            if ( new File( where + binaryName ).exists() ) 
            {
                found = true;
                break;
            }
        }
	        
	    return found;
	}
}
