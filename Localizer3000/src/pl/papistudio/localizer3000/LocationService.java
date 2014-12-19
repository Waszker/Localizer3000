package pl.papistudio.localizer3000;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
	
	private static int NOTIFICATION_ID = 1;
	private final IBinder mBinder = new LocalBinder();
	private android.location.Location location;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private int interval;
	private int startMode = Service.START_STICKY;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
    public void onCreate() {
		bringServiceToForeground();
		createLocationListener();
		registerForLocationUpdates();
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Location service starting", Toast.LENGTH_SHORT).show();
        Log.d("Location Service", "Started");
    	interval = intent.getIntExtra("interval", 5*1000);  
        return startMode;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
    	Log.d("Location Service", "Client binded");
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
    	Log.d("Location Service", "Client unbinded");
        return true;
    }
    
    @Override
    public void onRebind(Intent intent) {
    	Log.d("Location Service", "Client rebinded");
    }
    
    @Override
    public void onDestroy() {
		locationManager.removeUpdates(locationListener);
        Toast.makeText(this, "Location service ending", Toast.LENGTH_SHORT).show();
		Log.d("Location Service", "Location service ending");
    }
    
    private void bringServiceToForeground() {
    	startForeground(NOTIFICATION_ID, createNotification("Location: unknown"));
    }
    
	private Notification createNotification(String contentString) {
		return new Notification.Builder(getApplicationContext())
				.setContentTitle("Location service running")
				.setContentText(contentString)
				.setSmallIcon(R.drawable.powered_by_google_dark).build(); // TODO change!
	}
        
    private void createLocationListener() {
		locationListener = new LocationListener() {
		    public void onLocationChanged(android.location.Location location) {
		    	LocationService.this.location = location;
		    	updateNotification();
		    	broadcastNewLocation(location);
		    	Log.d("Location Service", "Location updated");
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		    
		    private void broadcastNewLocation(android.location.Location loc) {
				EventBus.getDefault().post(loc);
			}
		    
		    private void updateNotification() {
		    	((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
				.notify(NOTIFICATION_ID, createNotification(location.getLatitude()
								+ " " + location.getLongitude()));
		    }
		};		  
    }

    private void registerForLocationUpdates() {
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(locationListener);

		// TODO: Change interval here
		if( !(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && isNetworkLocationProviderEnabled()) )
			EventBus.getDefault().post("No network or GPS!");
		
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 0, locationListener);

		if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval, 0, locationListener);
    	
		// TODO: provide quick location fix
//		location = locationManager.getLastKnownLocation(provider); 
    }
    
    private boolean isNetworkLocationProviderEnabled() {
		ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile, wifi;
		mobile = fillState(con, ConnectivityManager.TYPE_MOBILE);
    	wifi = fillState(con, ConnectivityManager.TYPE_WIFI);
    	
    	return (mobile == NetworkInfo.State.CONNECTED 
    			|| mobile == NetworkInfo.State.CONNECTING
    			|| wifi == NetworkInfo.State.CONNECTING
    			|| wifi == NetworkInfo.State.CONNECTING);
    }
    
    private State fillState(ConnectivityManager manager, int networkType) {
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
