package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
 * SavedLocalizationsActivity class where reference to location is stored. 
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	  if (requestCode == MainActivity.EDIT_EXISTING_LOCATION && resultCode == Activity.RESULT_OK)
	  {
		  Location location = data.getParcelableExtra("location");
		  if(location != null)
		  {
			  ((SavedLocalizationsActivity)getActivity()).setCurrentlyUsedLocation(location);
			  getLocationReference();
			  fillLocationDetails(getView());
		  }
	  }
	}
	
	private void getLocationReference() {
		location = ((SavedLocalizationsActivity)getActivity()).getCurrentlyUsedLocation();
	}
	
	private void fillLocationDetails(View v) {
		((TextView)v.findViewById(R.id.details_location_name)).setText(location.getName());
		((TextView)v.findViewById(R.id.details_location_days)).setText(createActiveDaysString());
		((TextView)v.findViewById(R.id.details_location_hours)).setText(createActiveHoursString());
		((TextView)v.findViewById(R.id.details_location_radius)).setText(location.getRadius());
		
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_sounds), location.isSoundOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_vibrations), location.isVibrationOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_wifi), location.isWifiOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_bluetooth), location.isBluetoothOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_nfc), location.isNfcOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_mobile_data), location.isMobileData());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_nfc), location.isNfcOn());
		setTextAndColorForBooleanValues((TextView)v.findViewById(R.id.details_location_SMS), location.isSMSsendOn());
		
	}
	
	private String createActiveDaysString() {
		StringBuilder stringBuilder = new StringBuilder();
		if(location.isMon()==true)
			stringBuilder.append("Mon ");
		if(location.isTue()==true)
			stringBuilder.append("Tue ");
		if(location.isWed()==true)
			stringBuilder.append("Wed ");
		if(location.isThu()==true)
			stringBuilder.append("Thu ");
		if(location.isFri()==true)
			stringBuilder.append("Fri ");
		if(location.isSat()==true)
			stringBuilder.append("Sat ");
		if(location.isSun()==true)
			stringBuilder.append("Sun ");
		return stringBuilder.toString();
	}
	
	private String createActiveHoursString() {
		StringBuilder stringBuilderHours = new StringBuilder();
		stringBuilderHours.append(location.getTimeFrom().toString()+"  -  ");
		stringBuilderHours.append(location.getTimeTo().toString());
		return stringBuilderHours.toString();
	}
	
	private void setTextAndColorForBooleanValues(TextView view, boolean booleanValue) {
		if(booleanValue)
		{
			view.setText("ON");
			view.setTextColor(getResources().getColor(R.color.material_blue_toggle_unchecked));
		}
		else
		{
			view.setText("OFF");
			view.setTextColor(getResources().getColor(R.color.offOption));		
		}
	}
	
	private void addOnClickActionsToButtons(View v) {
		((Button)v.findViewById(R.id.details_delete_location_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.details_edit_location_button)).setOnClickListener(this);
	}
	
	private void showEditDetailsFragment(Location location) {
		Intent intent = new Intent(getActivity(), EditLocationActivity.class);
		intent.putExtra("location", location);
		startActivityForResult(intent, MainActivity.EDIT_EXISTING_LOCATION);
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
	        	DatabaseHelper.getInstance(getActivity()).deleteLocationAt(location);
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
