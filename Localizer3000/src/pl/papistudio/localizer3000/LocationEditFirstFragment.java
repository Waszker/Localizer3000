package pl.papistudio.localizer3000;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment displaying editable location
 * details. User can set all parameters of location
 * except for location on map.
 * 
 * @author PapiTeam
 *
 */
public class LocationEditFirstFragment extends Fragment implements OnClickListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	private Location location;
	private boolean shouldRequestExitConfirmation;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		shouldRequestExitConfirmation = false;	
		View rootView = inflater.inflate(R.layout.fragment_location_edit1, container, false);
		getLocationReference();
		fillNullLocation();
		addOnClickActionsToButtons(rootView);
		fillLocationDetails(rootView);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.edit_location_cancel_button)
			showCancelConfirmationDialog();
		if(v.getId() == R.id.edit_location_next_button)
			showSecondEditFragment();
	}
	
	/**
	 * Method invoked when user leaves edition of location.
	 * In case of editing non-existing location confirmation dialog
	 * is shown that warns about possible loss of work.
	 */
	public void reactToUserLeavingEdition() {
		// TODO: Distinguish between user leaving from newly created
		//		 location or from editing existing one!
		if(shouldRequestExitConfirmation)
			showCancelConfirmationDialog();
		else
			LocationEditFirstFragment.this.getActivity().finish();
	}
	
	/**
	 * Get location reference assigned to activity.
	 */
	private void getLocationReference() {
		location = ((EditLocationActivity)getActivity()).currentlyEditedLocation;
	}
	
	private void addOnClickActionsToButtons(View v) {
		((Button)v.findViewById(R.id.edit_location_cancel_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_location_next_button)).setOnClickListener(this);
	}
	
	/**
	 * Fills view with location settings.
	 * @param view to fill
	 */
	private void fillLocationDetails(View v) {
		((TextView)v.findViewById(R.id.edit_location_name)).setText(location.getName());
		((Switch)v.findViewById(R.id.edit_location_wifi_switch)).setChecked(location.isWifiOn());
		((Switch)v.findViewById(R.id.edit_location_bluetooth_switch)).setChecked(location.isBluetoothOn());
		((Switch)v.findViewById(R.id.edit_location_nfc_switch)).setChecked(location.isNfcOn());
		((Switch)v.findViewById(R.id.edit_location_mobile_data_switch)).setChecked(location.isMobData());
	}
	 
	 /**
	  * Creates new location object if none was assigned
	  * to activity.
	  * (it usually means that user wants to create new 
	  * location).
	  */
	 private void fillNullLocation() {
		 if(location == null)
		 {
			 location = new Location("", false, false, false, false);
			 shouldRequestExitConfirmation = true;
		 }
	 }
	 
	 private void showCancelConfirmationDialog() {
			new AlertDialog.Builder(getActivity())
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Cancel location")
	        .setMessage("Are you sure you want to lose this location?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	/* Move to previous screen */
		        	Toast.makeText(LocationEditFirstFragment.this.getActivity(), 
		        			"Location has not been saved", Toast.LENGTH_SHORT).show();
		        	LocationEditFirstFragment.this.getActivity().finish(); 
		        }
		
		    })
		    .setNegativeButton("No", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        }
		
		    })
		    .show();
	}
	 
	 private void showSecondEditFragment() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out, android.R.animator.fade_in,
				android.R.animator.fade_out);
		ft.replace(getId(),	new LocationEditSecondFragment());
		ft.addToBackStack(null);
		ft.commit();
	 }
}
