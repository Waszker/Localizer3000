package pl.papistudio.localizer3000;

import pl.papistudio.localizer3000.LocationService.LocalBinder;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
	public static final String INTERVAL_PREFERENCE = "INTERVAL_PREFERENCE";
	private LocationService locationService;
	private boolean isServiceBinded;
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("MainActivity", "Binded to service!");
			LocalBinder binder = (LocalBinder)service;
			locationService = binder.getService();
			registerOrUnregisterForLocationUpdates(true);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("MainActivity", "Lost bind to service!");
			isServiceBinded = false;
			registerOrUnregisterForLocationUpdates(false);
		}

		private void registerOrUnregisterForLocationUpdates(boolean shouldRegister) {			
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = android.location.Location.class;
			try {
				if(shouldRegister)
					locationService.requestLocationUpdates(MainActivity.this, 
														   MainActivity.class.getMethod(
																   "updateCurrentLocation", 
																   parameterTypes));
				else
					locationService.unregisterFromLocationUpdates(MainActivity.this, 
																  MainActivity.class.getMethod(
																	"updateCurrentLocation", 
																	parameterTypes));					
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	};

	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setServiceButtonTextAccordingToServiceState();
		bindIfLocationServiceRunning();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
					android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(android.R.id.content, new PreferencesFragment());
			ft.addToBackStack(null);
            ft.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        if (isServiceBinded) 
        {
        	serviceConnection.onServiceDisconnected(null); // TODO: change!
            unbindService(serviceConnection);
            isServiceBinded = false;
        }
    }
	
	@Override
    public void onDestroy() {
		super.onDestroy();
    }
	
	/**
	 * Starts or stops location service upon click.
	 * @param v
	 */
	public void startStopServiceOnClick(View v) {
		if(((ToggleButton)v).isChecked())
			startLocationServiceAndBindToIt();
		else
		{
			if(isServiceBinded)
				unbindService(serviceConnection);
            isServiceBinded = false;
            stopService(new Intent(this, LocationService.class));
		}

		setServiceButtonTextAccordingToServiceState();
	}
	
	/**
	 * Opens activity listing all saved locations.
	 * @param v
	 */
	public void showSavedLocationsClick(View v) {
		Intent intent = new Intent(this, SavedLocalizationsActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Opens activity for creating new location object.
	 * @param v
	 */
	public void addNewLocationClick(View v) {
		Intent intent = new Intent(this, EditLocationActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Updates textview by inserting current location details.
	 * @param location
	 */
	public void updateCurrentLocation(final android.location.Location location) {
		// TODO: change it somehow!
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				((TextView) findViewById(R.id.current_location_textview))
						.setText(location.getLatitude() + "\n"
								+ location.getLongitude() + "\nwith accu: "
								+ location.getAccuracy());
			}
		});
	}
	
	private void startLocationServiceAndBindToIt() {
		Intent intent = new Intent(this, LocationService.class);
		
		if(isLocationServiceNotRunning())
		{
			// TODO: change interval sending!
			intent.putExtra("interval", 1000*getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(INTERVAL_PREFERENCE, 5));
			startService(intent);			
		}
		
		bindIfLocationServiceRunning();
	}

	private void setServiceButtonTextAccordingToServiceState() {
		ToggleButton b = (ToggleButton)findViewById(R.id.service_start_stop_button);
		boolean isServiceNotRunning = isLocationServiceNotRunning();
		
		if(isServiceNotRunning)
		{
			b.setText("Not running");
			b.setChecked(false);
		}
		else
		{
			b.setText("Running");
			b.setChecked(true);
		}
	}
	
	private boolean isLocationServiceNotRunning() {
		boolean isNotRunning = true;
		ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
			if (LocationService.class.getName().equals(service.service.getClassName()))
			{
				isNotRunning = false;
				break;
			}
		
		return isNotRunning;
	}
	
	private void bindIfLocationServiceRunning() {
		if(!isLocationServiceNotRunning())
			isServiceBinded = bindService(new Intent(this, LocationService.class), serviceConnection, BIND_AUTO_CREATE);
	}
}
