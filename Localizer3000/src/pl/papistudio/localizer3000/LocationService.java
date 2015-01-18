package pl.papistudio.localizer3000;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class LocationService extends Service {
	/******************/
	/*   VARIABLES    */
	/******************/
	public class LocalBinder extends Binder {
		LocationService getService() {
			return LocationService.this;
		}
	}
	
	private static final String TAG = "Location Service";
	private static final int NOTIFICATION_ID = 1;
	private final IBinder mBinder = new LocalBinder();
	private android.location.Location mLocation;
	private Location mActiveLocation;
	private BroadcastReceiver mNetworkChangeReceiver;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private int interval;
	final private int mStartMode = Service.START_STICKY;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
    public void onCreate() {
		bringServiceToForeground();
		createLocationListener();
		EventBus.getDefault().register(this);
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Location service starting", Toast.LENGTH_SHORT).show();
        Log.d("Location Service", "Started");
    	interval = intent.getIntExtra("interval", 5*60*1000);
    	registerForLocationUpdates();
    	registerForConnectivityChanges();
        return mStartMode;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
    	Log.d(TAG, "Client binded");
    	sendLocationInfo();
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
    	Log.d(TAG, "Client unbinded");
        return true;
    }
    
    @Override
    public void onRebind(Intent intent) {
    	Log.d(TAG, "Client rebinded");
    	sendLocationInfo();
    }
    
	/**
	 * Invoked by EventBus. Integer holds the value
	 * of new interval value. Method updates interval.
	 * @param interval value
	 */
	public void onEvent(Integer i) {
		if(i != interval)
		{
			interval = i;
			registerForLocationUpdates();
		}
	}
	
	/**
	 * Invoked when new saved location becomes
	 * active. Method updates notification.
	 * @param l
	 */
	public void onEvent(Location l) {
		mActiveLocation = l;
		updateNotification("Currently active profile:\n"+l.getName());
	}
    
    @Override
    public void onDestroy() {
		mLocationManager.removeUpdates(mLocationListener);
		unregisterReceivers();
        Toast.makeText(this, "Location service ending", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "Location service ending");
    }
    
    private void bringServiceToForeground() {
    	startForeground(NOTIFICATION_ID, createNotification("Location: unknown"));
    }
	
    private void updateNotification(String text) {
    	((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
		.notify(NOTIFICATION_ID, createNotification(text));
    }
    
    private void sendLocationInfo() {
    	if(mLocation != null)
    	{
    		EventBus.getDefault().post(mLocation);
    	}
    	
    	if(mActiveLocation != null)
    	{
    		EventBus.getDefault().post(mActiveLocation);
    	}
    }
    
	private Notification createNotification(String contentString) {
		 Intent notificationIntent = new Intent(this, MainActivity.class);
		 notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
				 					 Intent.FLAG_ACTIVITY_SINGLE_TOP);
		 PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, 
				 									notificationIntent, 0);
		 
		return new Notification.Builder(getApplicationContext())
				.setContentTitle("Location service running")
				.setContentText(contentString)
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_launcher).build(); // TODO change!
	}
        
    private void createLocationListener() {
		mLocationListener = new LocationListener() {
		    public void onLocationChanged(android.location.Location location) {
		    	if(location != null && isNewLocationBetter(location))
		    	{
			    	LocationService.this.mLocation = location;
			    	broadcastNewLocation(location);
			    	System.reactToLocationChange(location, LocationService.this);
			    	Log.d(TAG, "Location updated");
		    	}
		    	
	    		if(location != null && location.getAccuracy() > 150.0)
	    		{
	    			reactToPoorLocationAccuracy();
	    		}
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		    
		    private boolean isNewLocationBetter(android.location.Location location) {
		    	android.location.Location oldLocation = LocationService.this.mLocation;
		    	
		    	return ((oldLocation.distanceTo(location) > location.getAccuracy() + oldLocation.getAccuracy() 	// we want new location when it points to definately new position
		    			|| oldLocation.getAccuracy() >= location.getAccuracy())									// we want new location always if it provides better accuracy
		    			&& oldLocation.getElapsedRealtimeNanos() <= location.getElapsedRealtimeNanos());		// we reject new location if it is "older" one than we have right now
		    																									// (for example GPS last known location compared to network location)
		    }
		    
		    private void reactToPoorLocationAccuracy() {
    			mLocationManager = (LocationManager)LocationService.this.getSystemService(Context.LOCATION_SERVICE);
    			
    			if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    			{
    				mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);	
    			}
		    }
		    
		    private void broadcastNewLocation(android.location.Location loc) {
				EventBus.getDefault().post(loc);
			}
		};		  
    }
    
    private void registerForConnectivityChanges() {
    	mNetworkChangeReceiver = new BroadcastReceiver() {
   		 @Override
		    public void onReceive(Context context, Intent intent) {
		        Log.w(TAG, "Network Type Changed");
		        registerForLocationUpdates();
		    }
    	};
    	registerReceiver(mNetworkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    
    private void unregisterReceivers() {
    	unregisterReceiver(mNetworkChangeReceiver);
		EventBus.getDefault().unregister(this);
    }

    private void registerForLocationUpdates() {
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.removeUpdates(mLocationListener);

		if( !(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || isNetworkConnectionEnabled()) ) 
		{
			EventBus.getDefault().post(new Exception("No network or GPS!"));
			updateNotification("No network or GPS connection! Location cannot be obtained!");
		}

		if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && isNetworkConnectionEnabled()) // we prefer NETWORK OVER GPS
		{
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval, 0, mLocationListener);
			mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			mLocationListener.onLocationChanged(mLocation);
			Log.d(TAG, "Network obtained");
		}
		else
		{
			
			if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 0, mLocationListener);
				mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//				locationListener.onLocationChanged(location);
				Log.d(TAG, "GPS obtained");
			}
		}
    }
    
    private boolean isNetworkConnectionEnabled() {
		final ConnectivityManager con = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile, wifi;
		mobile = fillState(con, ConnectivityManager.TYPE_MOBILE);
    	wifi = fillState(con, ConnectivityManager.TYPE_WIFI);
    	
    	return (mobile == NetworkInfo.State.CONNECTED 
    			|| mobile == NetworkInfo.State.CONNECTING
    			|| wifi == NetworkInfo.State.CONNECTED
    			|| wifi == NetworkInfo.State.CONNECTING);
    }
    
    private State fillState(final ConnectivityManager manager, final int networkType) {
    	State state;
    	try {
    		state = manager.getNetworkInfo(networkType).getState();
    	}
    	catch(NullPointerException e) {
    		state = NetworkInfo.State.UNKNOWN;
    	}
    	
    	return state;
    }
}