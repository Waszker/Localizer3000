package pl.papistudio.localizer3000;

import android.app.Activity;
import android.os.Bundle;

public class SavedLocalizationsActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	private Location mCurrentlyUsedLocation;

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
			mCurrentlyUsedLocation = (Location)savedInstanceState.getParcelable("location");
		}
	}
	
	@Override
	 public void onSaveInstanceState(Bundle outState) {
	     super.onSaveInstanceState(outState);
	     outState.putParcelable("location", mCurrentlyUsedLocation);
	 }

	public Location getCurrentlyUsedLocation() {
		return mCurrentlyUsedLocation;
	}

	public void setCurrentlyUsedLocation(Location currentlyUsedLocation) {
		this.mCurrentlyUsedLocation = currentlyUsedLocation;
	}
}
