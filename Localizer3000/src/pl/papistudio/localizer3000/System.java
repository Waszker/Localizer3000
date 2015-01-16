package pl.papistudio.localizer3000;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.honorato.multistatetogglebutton.MultiStateToggleButton.ToggleStates;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import de.greenrobot.event.EventBus;

/**
 * It is a static class that has two main duties:
 * <ul>
 * 		<li>It reacts to location changes: searches 
 * 			and changes system settings</li>
 * 		<li>It returns availability of certain phone/tablet
 * 			functionalities like GPS, Bluetooth etc.</li>
 * </ul>
 *  
 * @author PapiTeam
 *
 */
public class System {
	/******************/
	/*   VARIABLES    */
	/******************/
	private static final String TAG = "Location Service System module";
	private static Context mContext;
	private static Location mCurrentlyActiveLocation;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	/**
	 * <p>Function searches for saved location that can be applied to current user location.</p>
	 * <p>It selects saved location that covers current user position.</p>
	 * <p>Date and hours are also taken into account.</p>
	 * <p>If there are multiple such locations, the one with <b>lowest priority</b> is taken.</p>
	 *  
	 * @param location
	 * @param context asking for reaction
	 */
	public static void reactToLocationChange(android.location.Location location, Context context) {
		System.mContext = context;
		Location nearestLocation = findBestSuitedLocation(location, context);
		if(nearestLocation != null)
		{
			updatePhoneStatusForFoundLocation(nearestLocation);			
			mCurrentlyActiveLocation = nearestLocation;
		}
		else
			Log.d("System Location", "There is no good location to apply...");
	}
	
	/**
	 * Function checks for several phone functionalities:
	 * <ul>
	 * 		<li>Location support</li>
	 * 		<li>Accurate location support</li>
	 * 		<li>WiFi support</li>
	 * 		<li>Bluetooth support</li>
	 * 		<li>GPS support</li>
	 * 		<li>Mobile connection support</li>
	 * 		<li>Vibration support</li>
	 * 		<li>Root privileges support</li>
	 * </ul> <br/>
	 * and returns array of bools indicating what is supported or not.
	 * @param context
	 * @return array of bools indicating what is supported or not
	 */
	public static boolean[] checkPhoneModules(Context context) {
		boolean[] availability = new boolean[8];

		availability[0] = context.getPackageManager()
								 .hasSystemFeature(PackageManager.FEATURE_LOCATION);
		availability[1] = context.getPackageManager()
								 .hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
		availability[2] = context.getPackageManager()
								 .hasSystemFeature(PackageManager.FEATURE_WIFI);
		availability[3] = context.getPackageManager()
								 .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
		availability[4] = ((LocationManager)context
								.getSystemService(Context.LOCATION_SERVICE))
								.isProviderEnabled(LocationManager.GPS_PROVIDER);
		availability[5] = ((ConnectivityManager)context
								.getSystemService(Context.CONNECTIVITY_SERVICE))
								.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null;
		availability[6] = (((Vibrator)context
								.getSystemService(Context.VIBRATOR_SERVICE)).hasVibrator());
		availability[7] = findBinary("su");
		
		return availability;
	}
	
	private System() {
	}
	
	private static void updatePhoneStatusForFoundLocation(Location nearestLocation) {
		EventBus.getDefault().post(nearestLocation);
		boolean hasLocationChanged = mCurrentlyActiveLocation == null || 
									 !(mCurrentlyActiveLocation.getName()
											 .contentEquals(nearestLocation.getName()));
		
		setWifi(nearestLocation.isWifiOn());
		setBluetooth(nearestLocation.isBluetoothOn());
		setMobileData(nearestLocation.isMobileData());
		setSound(nearestLocation.isSoundOn(), nearestLocation.isVibrationOn());
		sendSMSes(nearestLocation, hasLocationChanged);
		turnServiceOffIfRequested(mContext, nearestLocation.shouldTurnOff());
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
	
	private static boolean isLocationValidToApplySettings(Location nearestLocation, 
														  android.location.Location location) {
		return nearestLocation != null && location != null
				&& nearestLocation.getLocation().distanceTo(location) <= nearestLocation.getRadius();
	}
	
	private static void setWifi(ToggleStates state) {
		if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))
		{
			if(state == ToggleStates.On || state == ToggleStates.Off)
			((WifiManager)mContext.getSystemService(Context.WIFI_SERVICE))
								 .setWifiEnabled(state == ToggleStates.On);
		}
	}
	
	private static void setBluetooth(ToggleStates state) {
		if(BluetoothAdapter.getDefaultAdapter() != null)
		{
			if(state == ToggleStates.On)
				 BluetoothAdapter.getDefaultAdapter().enable();
			else if(state == ToggleStates.Off)
				BluetoothAdapter.getDefaultAdapter().disable();
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void setSound(ToggleStates stateSound, ToggleStates stateVibration) {
		AudioManager aManager=(AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
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
		{ 
			// if we are here, problably we are rooted ;)
			try {
				if(state == ToggleStates.On)
					Runtime.getRuntime().exec("su -c svc data enable");
				else if(state == ToggleStates.Off)
					Runtime.getRuntime().exec("su -c svc data disable");
			} catch (IOException e) {
				Log.e(TAG, "Error getting root priviledges");
			}
		}
		else if(state != ToggleStates.Not_specified)
			Log.e(TAG, "No root...");
			
	}
	
	private static void sendSMSes(Location location, boolean hasLocationChanged) {
		List<SMS> smsList = DatabaseHelper.getInstance(mContext).getAllSMS();
		
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
					{
						DatabaseHelper.getInstance(mContext).deleteSMSAt(s);
					}
				}
			}
		}	
	}
	
	private static void turnServiceOffIfRequested(Context context, boolean shouldTurnOff) {
		if(shouldTurnOff)
			context.stopService(new Intent(context, LocationService.class));
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
