package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EditLocationActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	public Location currentlyEditedLocation;
	
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
		}
	}
	
	@Override
	 public void onSaveInstanceState(Bundle outState) {
	     super.onSaveInstanceState(outState);
	     outState.putParcelable("location", currentlyEditedLocation);
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    @Override
	public void onBackPressed() {
    	Fragment fragment;
	    if ((fragment = getFragmentManager().findFragmentByTag("EditPart1")) != null)
	    	((LocationEditFirstFragment)fragment).reactToUserLeavingEdition();
	    else
	    	super.onBackPressed();
	}
}
