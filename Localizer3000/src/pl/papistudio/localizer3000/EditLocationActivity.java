package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EditLocationActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	public Location currentlyEditedLocation;
	public String originalLocationName;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_location);
		if (savedInstanceState == null) 
		{
			Intent i = getIntent();
			currentlyEditedLocation = (Location)i.getParcelableExtra("location");
			
			getFragmentManager().beginTransaction()
					.add(R.id.container, new LocationEditFirstFragment(), "EditPart1").commit();
		}
		else 
		{
			currentlyEditedLocation = (Location)savedInstanceState.getParcelable("location");
			originalLocationName = savedInstanceState.getString("LocationName");
		}
	}
	
	@Override
	 public void onSaveInstanceState(Bundle outState) {
	     super.onSaveInstanceState(outState);
	     outState.putParcelable("location", currentlyEditedLocation);
	     outState.putString("LocationName", originalLocationName);
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_location, menu);
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
	public void onBackPressed() {
    	Fragment fragment;
	    if ((fragment = getFragmentManager().findFragmentByTag("EditPart1")) != null)
	    {
	    	if(((LocationEditFirstFragment)fragment).isVisible())
	    		((LocationEditFirstFragment)fragment).reactToUserLeavingEdition();
	    	else
	    		super.onBackPressed();
	    }
	    else
	    	super.onBackPressed();
	}
}
