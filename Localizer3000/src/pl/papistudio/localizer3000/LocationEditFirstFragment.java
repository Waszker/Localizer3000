package pl.papistudio.localizer3000;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

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
		
		// TODO: change that!!!
		if(v.getId() == R.id.edit_location_time_from_button)
			location.setTimeFrom(showTimePicker(location.getTimeFrom()));
		if(v.getId() == R.id.edit_location_time_to_button)
			location.setTimeTo(showTimePicker(location.getTimeTo()));
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
		((Button)v.findViewById(R.id.edit_location_time_from_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_location_time_to_button)).setOnClickListener(this);
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
		((Switch)v.findViewById(R.id.edit_location_mobile_data_switch)).setChecked(location.isMobileData());
		((Switch)v.findViewById(R.id.edit_location_sound_switch)).setChecked(location.isSoundOn());
		((Switch)v.findViewById(R.id.edit_location_vibration_switch)).setChecked(location.isVibrationOn());
		
		((ToggleButton)v.findViewById(R.id.toggle_monday)).setChecked(location.isMon());
		((ToggleButton)v.findViewById(R.id.toggle_tuesday)).setChecked(location.isTue());
		((ToggleButton)v.findViewById(R.id.toggle_wednesday)).setChecked(location.isWed());
		((ToggleButton)v.findViewById(R.id.toggle_thursday)).setChecked(location.isThu());
		((ToggleButton)v.findViewById(R.id.toggle_friday)).setChecked(location.isFri());
		((ToggleButton)v.findViewById(R.id.toggle_saturday)).setChecked(location.isSat());
		((ToggleButton)v.findViewById(R.id.toggle_sunday)).setChecked(location.isSun());
		
		setActiveTimeStrings(v, location.getTimeFrom(), location.getTimeTo());
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
			 location = new Location("", false, false, false, false, false, false, 100);
			 ((EditLocationActivity)getActivity()).currentlyEditedLocation = location;
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
		fillEditedLocationDetails();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out, android.R.animator.fade_in,
				android.R.animator.fade_out);
		ft.replace(getId(), new LocationEditSecondFragment());
		ft.addToBackStack(null);
		ft.commit();
	 }
	 
	 private Time showTimePicker(Time locationTime) {
		final Time selectedTime = new Time();

		TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker,int selectedHour, int selectedMinute) {
						selectedTime.setHour(selectedHour);
						selectedTime.setMinute(selectedMinute);
						setActiveTimeStrings(getView(), location.getTimeFrom(), location.getTimeTo());
					}
				}, locationTime.getHour(), locationTime.getMinute(), true /* 24 hour time */);
		mTimePicker.setTitle("Select starting hour");
		mTimePicker.setCancelable(false);
		mTimePicker.show();		
		
		return selectedTime;
	 }
	 
	 private void setActiveTimeStrings(View v, Time from, Time to) {
		 ((Button)v.findViewById(R.id.edit_location_time_from_button)).setText("From: " + from.toString());
		 ((Button)v.findViewById(R.id.edit_location_time_to_button)).setText("To: " + to.toString());		 
	 }
	 
	 private void fillEditedLocationDetails() {
			location.setName(String.valueOf(((TextView) getView().findViewById(R.id.edit_location_name)).getText()));
			location.setWifiOn(((Switch)getView().findViewById(R.id.edit_location_wifi_switch)).isChecked());
			location.setBluetoothOn(((Switch)getView().findViewById(R.id.edit_location_bluetooth_switch)).isChecked());
			location.setNfcOn(((Switch)getView().findViewById(R.id.edit_location_nfc_switch)).isChecked());
			location.setMobileData(((Switch)getView().findViewById(R.id.edit_location_mobile_data_switch)).isChecked());
			location.setSoundOn(((Switch)getView().findViewById(R.id.edit_location_sound_switch)).isChecked());
			location.setVibrationOn(((Switch)getView().findViewById(R.id.edit_location_vibration_switch)).isChecked());
			location.setLocation(new android.location.Location("PLAY")); // TODO: get location!

			location.setMon(((ToggleButton)getView().findViewById(R.id.toggle_monday)).isChecked());
			location.setTue(((ToggleButton)getView().findViewById(R.id.toggle_tuesday)).isChecked());
			location.setWed(((ToggleButton)getView().findViewById(R.id.toggle_wednesday)).isChecked());
			location.setThu(((ToggleButton)getView().findViewById(R.id.toggle_thursday)).isChecked());
			location.setFri(((ToggleButton)getView().findViewById(R.id.toggle_friday)).isChecked());
			location.setSat(((ToggleButton)getView().findViewById(R.id.toggle_saturday)).isChecked());
			location.setSun(((ToggleButton)getView().findViewById(R.id.toggle_sunday)).isChecked());		 
	 }
}
