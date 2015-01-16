package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class SmsActivity extends Activity {
	/******************/
	/*   VARIABLES    */
	/******************/
	private Sms mCurrentlyUsedSMS; 
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		if (savedInstanceState == null) 
		{
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SmsListFragment()).commit();
		}
	}

	public Sms getCurrentlyUsedSMS() {
		return mCurrentlyUsedSMS;
	}

	public void setCurrentlyUsedSMS(Sms currentlyUsedSMS) {
		this.mCurrentlyUsedSMS = currentlyUsedSMS;
	}	
	
	@Override
	public void onBackPressed() {
    	Fragment fragment;
	    if ((fragment = getFragmentManager().findFragmentByTag("SMSDetails")) != null)
	    {
	    	if(((SmsDetailsFragment)fragment).isVisible())
	    	{
	    		((SmsDetailsFragment)fragment).reactToBackPress();
	    	}
	    	else
	    	{
	    		super.onBackPressed();
	    	}
	    }
	    else
	    {
	    	super.onBackPressed();
	    }
	}
}
