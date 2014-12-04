package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SavedLocalizationsActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	public Location currentlyUsedLocation;

	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_localizations);
		if (savedInstanceState == null) 
		{
			getFragmentManager().beginTransaction()
					.add(R.id.container, new ListOfSavedLocationsFragment()).commit();
		}
		else
		{
			currentlyUsedLocation = (Location)savedInstanceState.getParcelable("location");
		}
	}
	
	@Override
	 public void onSaveInstanceState(Bundle outState) {
	     super.onSaveInstanceState(outState);
	     outState.putParcelable("location", currentlyUsedLocation);
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_localizations, menu);
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
}
