package pl.papistudio.localizer3000;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {
	/******************/
	/*   VARIABLES    */
	/******************/
	private int interval;
	private int startMode = Service.START_NOT_STICKY;
	private android.location.Location location;
	private boolean shouldThreadWork = true;
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(lastKnowLocation != location)
					checkAndUpdateMainActivityLocation();
			}
			LocationService.this.stopSelf();
		}
		
		private void checkAndUpdateMainActivityLocation() {
			// TODO: react to location changes!!!
			lastKnowLocation = location;
		}
	}

	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
    public void onCreate() {
		createLocationListener();
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
        // A client is binding to the service with bindService()
        return null;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return false;
    }
    
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    
    @Override
    public void onDestroy() {
    	shouldThreadWork = false;
        Toast.makeText(this, "Location service ending", Toast.LENGTH_SHORT).show();
		Log.d("Service", "Location service ending");
    }
    
    /**
     * Starts working thread if there is none.
     */
    private void createWorkingThread() {
    	if(serviceThread == null)
    	{
    		serviceThread = new ServiceThread();
    		serviceThread.start();
    	}     	
    }
    
    /**
     * Creates location listener that updates the location
     * from time to time.
     */
    private void createLocationListener() {
		final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(android.location.Location location) {
		    	LocationService.this.location = location;
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };
		  
		// TODO: location updates should have different accuracy settings
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);    	
    }

}
