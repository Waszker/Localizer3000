package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

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
