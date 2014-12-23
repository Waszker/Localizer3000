package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

public class SMSActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	private SMS currentlyUsedSMS; 
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SMSListFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public SMS getCurrentlyUsedSMS() {
		return currentlyUsedSMS;
	}

	public void setCurrentlyUsedSMS(SMS currentlyUsedSMS) {
		this.currentlyUsedSMS = currentlyUsedSMS;
	}	
	
	@Override
	public void onBackPressed() {
    	Fragment fragment;
	    if ((fragment = getFragmentManager().findFragmentByTag("SMSDetails")) != null)
	    {
	    	if(((SMSDetailsFragment)fragment).isVisible())
	    		((SMSDetailsFragment)fragment).reactToBackPress();
	    	else
	    		super.onBackPressed();
	    }
	    else
	    	super.onBackPressed();
	}
}
