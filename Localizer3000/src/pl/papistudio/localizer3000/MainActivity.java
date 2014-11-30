package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	private android.location.Location location;

	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startLocationService();
		createLocationListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
    public void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, LocationService.class));
    }
	
	public void showSavedLocationsClick(View v) {
		Intent intent = new Intent(this, SavedLocalizationsActivity.class);
		startActivity(intent);
	}
	
	public void addNewLocationClick(View v) {
		Intent intent = new Intent(this, EditLocationActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Starts or updates location service
	 * with certain interval value.
	 */
	private void startLocationService() {
		Intent intent = new Intent(this, LocationService.class);
		// TODO: define interval in settings!!!!
		intent.putExtra("interval", 5000);
		startService(intent);
	}
	
	/**
	 * This location listener is necessary in order to show current location
	 * on the screen.
	 */
	private void createLocationListener() {
		final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(android.location.Location location) {
		    	MainActivity.this.location = location;
		    	updateCurrentLocation(MainActivity.this.location);
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };
		  
		// TODO: location updates should have different accuracy settings
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);    	
    }
	
	/**
	 * Updates textview by inserting current location details.
	 * @param location
	 */
	private void updateCurrentLocation(android.location.Location location) {
		((TextView)findViewById(R.id.current_location_textview)).setText(location.getLatitude() + " " + location.getLongitude() + " with accu: " + location.getAccuracy());
	}
}
