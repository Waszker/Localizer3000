package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class SMSActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	private SMS mCurrentlyUsedSMS; 
	
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

	public SMS getCurrentlyUsedSMS() {
		return mCurrentlyUsedSMS;
	}

	public void setCurrentlyUsedSMS(SMS currentlyUsedSMS) {
		this.mCurrentlyUsedSMS = currentlyUsedSMS;
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
