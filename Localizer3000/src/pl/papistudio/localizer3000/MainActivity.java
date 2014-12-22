package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
	public static final String INTERVAL_PREFERENCE = "INTERVAL_PREFERENCE";
	public static final int EDIT_EXISTING_LOCATION = 0x1;
	private AlertDialog alertDialog;
	private boolean isServiceBinded;
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("MainActivity", "Binded to service!");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("MainActivity", "Lost bind to service!");
			isServiceBinded = false;
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
	protected void onStart() {
		super.onStart();
        EventBus.getDefault().register(this);
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (isServiceBinded) 
        {
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
	 * Opens activity for managing SMS.
	 * @param v
	 */
	public void manageSMS(View v) {
		Intent intent = new Intent(this, SMSActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Sent via EventBus exception holds
	 * important information to show to the user.
	 * @param error
	 */
	public void onEvent(Exception error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        
        if(alertDialog == null || !(alertDialog.isShowing()) )
        {
	        alertDialog = new AlertDialog.Builder(this).create();
	        alertDialog.setTitle(error.getMessage());
	        alertDialog.setMessage("Please enable network or GPS connection.");
	        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ok", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
	        alertDialog.show();
        }
	}
	
	/**
	 * Method invoked by EventBus when location becomes updated.
	 * @param location - updated location
	 */
	public void onEvent(android.location.Location location) {
		updateCurrentLocation(location);
	}
	
	/**
	 * Updates textview by inserting current location details.
	 * @param location
	 */
	public void updateCurrentLocation(final android.location.Location location) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				((TextView) findViewById(R.id.current_location_textview))
						.setText(location.getLatitude() + "\n"
								+ location.getLongitude() + "\nwith accuracy: "
								+ location.getAccuracy());				
			}
		});
	}
	
	private void startLocationServiceAndBindToIt() {
		startLocationService();
		bindIfLocationServiceRunning();
	}
	
	private void startLocationService() {
		Intent intent = new Intent(this, LocationService.class);
		intent.putExtra("interval", 60*1000*getSharedPreferences(MainActivity.SHARED_PREFERENCES, 
										Context.MODE_PRIVATE).getInt(INTERVAL_PREFERENCE, 5));
		startService(intent);
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
