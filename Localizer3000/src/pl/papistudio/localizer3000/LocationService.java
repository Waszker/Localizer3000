package pl.papistudio.localizer3000;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
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
	
	private final IBinder mBinder = new LocalBinder();
	private android.location.Location location;
	private LocationListener locationListener;
	private int interval;
	private int startMode = Service.START_NOT_STICKY; // TODO: change it!
	
	private boolean shouldThreadWork;
	private ServiceThread serviceThread;	
	private class ServiceThread extends Thread implements Runnable {
		/******************/
		/*   VARIABLES    */
		/******************/
		private android.location.Location lastKnowLocation;

		/******************/
		/*   FUNCTIONS    */
		/******************/
		@Override
		public void run() {
			while(shouldThreadWork)
			{
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(lastKnowLocation != location)
				{
					// TODO change!!!!
					((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
							.notify(1,createNotification(location.getLatitude()
											+ " " + location.getLongitude()));
					updateAndBroadcastNewLocation();
					takeActions();
				}
			}
			LocationService.this.stopSelf();
		}
		
		/**
		 * After location has changed broadcast it to all registered clients.
		 * This can be invoked when new client registers just to make it faster
		 * to get location info.
		 */
		public void updateAndBroadcastNewLocation() {
			// TODO: react to location changes!!!
	        Log.d("Location Service", "Location updated");
			lastKnowLocation = location;
			
			if(lastKnowLocation != null)
				broadcastNewLocation(lastKnowLocation);			
		}
		
		private void broadcastNewLocation(android.location.Location loc) {
			EventBus.getDefault().post(loc);
		}
		
		private void takeActions() {
			// TODO: this function should do all the stuff with location change!
		}
	}


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
    	createWorkingThread();   
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
    	shouldThreadWork = false;
        Toast.makeText(this, "Location service ending", Toast.LENGTH_SHORT).show();
		Log.d("Location Service", "Location service ending");
    }
    
    private void bringServiceToForeground() {
    	startForeground(1/* change it! */, createNotification("Location: unknown")); // change id!!
    }
    
	private Notification createNotification(String contentString) {
		return new Notification.Builder(getApplicationContext())
				.setContentTitle("Location service running")
				.setContentText(contentString)
				.setSmallIcon(R.drawable.powered_by_google_dark).build(); // TODO change!
	}
    
    private void createWorkingThread() {
    	if(serviceThread == null)
    	{
    		shouldThreadWork = true;
    		serviceThread = new ServiceThread();
    		serviceThread.start();
    	}     	
    }
        
    private void createLocationListener() {
		locationListener = new LocationListener() {
		    public void onLocationChanged(android.location.Location location) {
		    	LocationService.this.location = location;
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		};
		  
		
    }

    private void registerForLocationUpdates() {
		final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		locationManager.removeUpdates(locationListener);
    	// TODO: location updates should have different accuracy settings
		// TODO: Change interval here
		try {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 0, locationListener);
		} catch(IllegalArgumentException e) {
			Log.e("Location listener error: ", e.getMessage());			
		}
		
		try {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval, 0, locationListener);
		} catch(IllegalArgumentException e) {
			Log.e("Location listener error: ", e.getMessage());			
		}
    	
		// TODO: provide quick location fix
//		locationManager.getLastKnownLocation(provider); 
    }
}
