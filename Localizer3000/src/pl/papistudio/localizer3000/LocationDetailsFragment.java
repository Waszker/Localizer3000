package pl.papistudio.localizer3000;

import org.honorato.multistatetogglebutton.MultiStateToggleButton.ToggleStates;

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
 * <p>Fragment displaying all location details.</p>
 * <p>It allows to delete or edit location.</p>
 * <p>In case of deletion fragment ends its life and user comes back to previous screen.</p>
 * <p>In case of edition user is moved further to next fragment.</p>
 * 
 * <p>LocationDetailsFragment displays detailed information
 * about currently selected location. It takes info from
 * SavedLocalizationsActivity class where reference to location is stored.</p> 
 * 
 * @author PapiTeam
 *
 */
public class LocationDetailsFragment extends Fragment implements OnClickListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	private Location mLocation;
	
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
		{
			showDeleteConfirmationDialog();
		}
		if(v.getId() == R.id.details_edit_location_button)
		{
			showEditDetailsFragment(mLocation);
		}
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
		mLocation = ((SavedLocalizationsActivity)getActivity()).getCurrentlyUsedLocation();
	}
	
	private void fillLocationDetails(View v) {
		((TextView)v.findViewById(R.id.details_location_name))
					.setText(mLocation.getName());
		
		((TextView)v.findViewById(R.id.details_location_days))
					.setText(createActiveDaysString());
		
		((TextView)v.findViewById(R.id.details_location_hours))
					.setText(createActiveHoursString());
		
		setTextAndColorForStateValues(
				(TextView)v.findViewById(R.id.details_location_sounds), mLocation.isSoundOn());
		setTextAndColorForStateValues(
				(TextView)v.findViewById(R.id.details_location_vibrations), mLocation.isVibrationOn());
		setTextAndColorForStateValues(
				(TextView)v.findViewById(R.id.details_location_wifi), mLocation.isWifiOn());
		setTextAndColorForStateValues(
				(TextView)v.findViewById(R.id.details_location_bluetooth), mLocation.isBluetoothOn());
		setTextAndColorForStateValues(
				(TextView)v.findViewById(R.id.details_location_nfc), mLocation.isNfcOn());
		setTextAndColorForStateValues(
				(TextView)v.findViewById(R.id.details_location_mobile_data), mLocation.isMobileData());
		setTextAndColorForStateValues(
				(TextView)v.findViewById(R.id.details_location_nfc), mLocation.isNfcOn());
		
		setTextAndColorForBooleanValues(
				(TextView)v.findViewById(R.id.details_location_SMS), isSMSForLocationProvided(mLocation));
		setTextAndColorForBooleanValues(
				(TextView)v.findViewById(R.id.details_location_turn_off), mLocation.shouldTurnOff());		
	}
	
	private String createActiveDaysString() {
		StringBuilder stringBuilder = new StringBuilder();
		if(mLocation.isMon()==true)
		{
			stringBuilder.append("Mon ");
		}
		if(mLocation.isTue()==true)
		{
			stringBuilder.append("Tue ");
		}
		if(mLocation.isWed()==true)
		{
			stringBuilder.append("Wed ");
		}
		if(mLocation.isThu()==true)
		{
			stringBuilder.append("Thu ");
		}
		if(mLocation.isFri()==true)
		{
			stringBuilder.append("Fri ");
		}
		if(mLocation.isSat()==true)
		{
			stringBuilder.append("Sat ");
		}
		if(mLocation.isSun()==true)
		{
			stringBuilder.append("Sun ");
		}
		return stringBuilder.toString();
	}
	
	private String createActiveHoursString() {
		StringBuilder stringBuilderHours = new StringBuilder();
		stringBuilderHours.append(mLocation.getTimeFrom().toString()+"  -  ");
		stringBuilderHours.append(mLocation.getTimeTo().toString());
		return stringBuilderHours.toString();
	}
	
	private void setTextAndColorForStateValues(TextView view, ToggleStates state) {
		if(state == ToggleStates.On)
		{
			view.setText("ON");
			view.setTextColor(getResources().getColor(R.color.material_blue_toggle_unchecked));
		}
		else if(state == ToggleStates.Off)
		{
			view.setText("OFF");
			view.setTextColor(getResources().getColor(R.color.offOption));		
		}
		else 
		{
			view.setText("Unchanged");
			view.setTextColor(getResources().getColor(R.color.offOption));				
		}
	}
	
	private void setTextAndColorForBooleanValues(TextView view, boolean isOn) {
		if(isOn)
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
	        	DatabaseHelper.getInstance(getActivity()).deleteLocationAt(mLocation);
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
	
	private boolean isSMSForLocationProvided(Location location) {
		boolean isProvided = false;
		for(Sms s : DatabaseHelper.getInstance(getActivity()).getAllSMS())
			if(s.getLocationNameToSend().contentEquals(location.getName()))
			{
				isProvided = true;
				break;
			}
		
		return isProvided;
	}
}
