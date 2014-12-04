package pl.papistudio.localizer3000;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
	private int interval;
	private int startMode = Service.START_NOT_STICKY; // TODO: change it!
	
	private List<Object> receivers;
	private List<Method> methods;
	
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
					// TODO Auto-generated catch block
					// TODO: change it!
					e.printStackTrace();
				}
				
				if(lastKnowLocation != location)
				{
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
	        Log.d("Location Service", "Location updated " + receivers.size());
			lastKnowLocation = location;
			
			if(lastKnowLocation != null)
				broadcastNewLocation(lastKnowLocation);			
		}
		
		private void broadcastNewLocation(android.location.Location loc) {
			Object[] params = new Object[1];
			params[0] = loc;
			for(int i=0; i<receivers.size(); i++)
				try {
					methods.get(i).invoke(receivers.get(i), params);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | IndexOutOfBoundsException e) {
					// TODO: Do something productive?
					e.printStackTrace();
				}
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
		createLocationListener();
		receivers = new ArrayList<>();
		methods = new ArrayList<>();
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
    
    /**
     * Function registering for location updates from service.
     * @param receiver an object we should call method on
     * @param method function that takes android.location.Location as the only argument
     */
    public void requestLocationUpdates(Object receiver, Method method) {
    	receivers.add(receiver);
    	methods.add(method);
    	serviceThread.updateAndBroadcastNewLocation();
    }
    
    /**
     * Unregisters object from location updates.
     * @See requestLocationUpdates method for more information.
     * @param receiver
     * @param method
     */
    public void unregisterFromLocationUpdates(Object receiver, Method method) {
    	receivers.remove(receiver);
    	methods.remove(method);
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
		try {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		} catch(IllegalArgumentException e) {
			Log.d("Location listener error: ", e.getMessage());
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);			
		}
    }

}
