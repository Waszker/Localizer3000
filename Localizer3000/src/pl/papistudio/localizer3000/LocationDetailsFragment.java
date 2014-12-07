package pl.papistudio.localizer3000;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment displaying all location details.
 * It allows to delete or edit location.
 * In case of deletion fragment ends its life and user comes back to previous screen.
 * In case of edition user is moved further to next fragment.
 * 
 * LocationDetailsFragment displays detailed information
 * about currently selected location. It takes info from
 * main activity class where reference to location is stored. 
 * 
 * @author PapiTeam
 *
 */
public class LocationDetailsFragment extends Fragment implements OnClickListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	private Location location;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_location_details, container, false);
		getLocationReference();
		fillLocationDetails(rootView);
		addOnClickActionsToButtons(rootView);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.details_delete_location_button)
			showDeleteConfirmationDialog();
		if(v.getId() == R.id.details_edit_location_button)
			showEditDetailsFragment(location);
	}
	

	/**
	 * Get location reference assigned to activity.
	 * WARNING: location should not be null! Otherwise
	 * it means that something went wrong!
	 */
	private void getLocationReference() {
		location = ((SavedLocalizationsActivity)getActivity()).currentlyUsedLocation;		
	}
	
	private void fillLocationDetails(View v) {
		((TextView)v.findViewById(R.id.details_location_name)).setText(location.getName());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_wifi), location.isWifiOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_bluetooth), location.isBluetoothOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_nfc), location.isNfcOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_mobile_data), location.isMobileData());
	}
	
	private void setTextAndColorForBooleanValues(TextView view, boolean booleanValue) {
		view.setText(String.valueOf(booleanValue));
		if(booleanValue)
			view.setTextColor(Color.GREEN);
		else
			view.setTextColor(Color.RED);
	}
	
	private void addOnClickActionsToButtons(View v) {
		((Button)v.findViewById(R.id.details_delete_location_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.details_edit_location_button)).setOnClickListener(this);
	}
	
	private void showEditDetailsFragment(Location location) {
		Intent intent = new Intent(getActivity(), EditLocationActivity.class);
		intent.putExtra("location", location);
		startActivity(intent);
	}
	
	private void showDeleteConfirmationDialog() {
		new AlertDialog.Builder(getActivity())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Delete location")
        .setMessage("Are you sure you want to delete this location?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	/* Move to previous screen */
	        	Toast.makeText(LocationDetailsFragment.this.getActivity(), 
	        			"Location has been deleted", Toast.LENGTH_SHORT).show();
	        	LocationDetailsFragment.this.getFragmentManager().popBackStack(); 
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
}
