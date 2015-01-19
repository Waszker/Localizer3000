package pl.papistudio.localizer3000;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.MultiStateToggleButton.ToggleStates;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

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
	private Location mLocation;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_location_edit1, container, false);
		checkForGooglePlayServicesAndExitIfNone();
		getCurrentlyEditedLocationReference();
		fillNullLocation();
		saveLocationOriginalName();
		addOnClickActionsToButtons(rootView);
		fillLocationDetails(rootView);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.edit_location_cancel_button)
		{
			showCancelConfirmationDialog();
		}
		if(v.getId() == R.id.edit_location_next_button)
		{
			checkLocationNameAndShowSecondFragment();
		}
		if(v.getId() == R.id.edit_location_time_from_button)
		{
			mLocation.setTimeFrom(showTimePicker(mLocation.getTimeFrom(), "Select starting hour"));
		}
		if(v.getId() == R.id.edit_location_time_to_button)
		{
			mLocation.setTimeTo(showTimePicker(mLocation.getTimeTo(), "Select ending hour"));
		}
	}
	
	/**
	 * Method invoked when user leaves edition of location.
	 * In case of editing non-existing location confirmation dialog
	 * is shown that warns about possible loss of work.
	 */
	public void reactToUserLeavingEdition() {
		showCancelConfirmationDialog();
	}
	
	private void checkForGooglePlayServicesAndExitIfNone() {
		if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) != ConnectionResult.SUCCESS)
		{
			Toast.makeText(LocationEditFirstFragment.this.getActivity(), 
        			"Problems with Google Services!", Toast.LENGTH_SHORT).show();
        	getActivity().setResult(Activity.RESULT_CANCELED, null);
        	LocationEditFirstFragment.this.getActivity().finish(); 
		}
	}
	
	private void getCurrentlyEditedLocationReference() {
		mLocation = ((EditLocationActivity)getActivity()).currentlyEditedLocation;
	}
	
	private void addOnClickActionsToButtons(View v) {
		((Button)v.findViewById(R.id.edit_location_cancel_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_location_next_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_location_time_from_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_location_time_to_button)).setOnClickListener(this);
	}
	
	private void fillLocationDetails(View v) {
		((TextView)v.findViewById(R.id.edit_location_name)).setText(mLocation.getName());
		((MultiStateToggleButton)v.findViewById(R.id.edit_location_wifi_switch)).setSelection(mLocation.isWifiOn());
		((MultiStateToggleButton)v.findViewById(R.id.edit_location_bluetooth_switch)).setSelection(mLocation.isBluetoothOn());
		((MultiStateToggleButton)v.findViewById(R.id.edit_location_nfc_switch)).setSelection(mLocation.isNfcOn());
		((MultiStateToggleButton)v.findViewById(R.id.edit_location_mobile_data_switch)).setSelection(mLocation.isMobileData());
		((MultiStateToggleButton)v.findViewById(R.id.edit_location_sound_switch)).setSelection(mLocation.isSoundOn());
		((MultiStateToggleButton)v.findViewById(R.id.edit_location_vibration_switch)).setSelection(mLocation.isVibrationOn());
		
		((ToggleButton)v.findViewById(R.id.toggle_monday)).setChecked(mLocation.isMon());
		((ToggleButton)v.findViewById(R.id.toggle_tuesday)).setChecked(mLocation.isTue());
		((ToggleButton)v.findViewById(R.id.toggle_wednesday)).setChecked(mLocation.isWed());
		((ToggleButton)v.findViewById(R.id.toggle_thursday)).setChecked(mLocation.isThu());
		((ToggleButton)v.findViewById(R.id.toggle_friday)).setChecked(mLocation.isFri());
		((ToggleButton)v.findViewById(R.id.toggle_saturday)).setChecked(mLocation.isSat());
		((ToggleButton)v.findViewById(R.id.toggle_sunday)).setChecked(mLocation.isSun());
		((Switch)v.findViewById(R.id.edit_location_turn_off_switch)).setChecked(mLocation.shouldTurnOff());
						
		setActiveTimeStrings(v, mLocation.getTimeFrom(), mLocation.getTimeTo());
	}
	 
	private void fillNullLocation() {
		if (mLocation == null) 
		{
			mLocation = new Location("", ToggleStates.Off, ToggleStates.Off, 
									ToggleStates.Off, ToggleStates.Off, ToggleStates.Off, 
									ToggleStates.Off, 100);
			((EditLocationActivity) getActivity()).currentlyEditedLocation = mLocation;
		}
	}
	
	private void saveLocationOriginalName() {
		((EditLocationActivity)getActivity()).originalLocationName = mLocation.getName();
	}
	 
	private void showCancelConfirmationDialog() {
		if(!((EditLocationActivity)getActivity()).originalLocationName.contentEquals(""))
		{
			showCancelEditionDialog();
		}
		else
		{
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
			        	getActivity().setResult(Activity.RESULT_CANCELED, null);
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
	}
	
	private void showCancelEditionDialog() {
		new AlertDialog.Builder(getActivity())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Save location")
        .setMessage("Do you want to save changes to this location?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	fillEditedLocationDetails();
	        	DatabaseHelper.getInstance(getActivity())
	        		.updateLocation(((EditLocationActivity)getActivity()).currentlyEditedLocation, 
	        					((EditLocationActivity)getActivity()).originalLocationName);
				Toast.makeText(getActivity(), "Location has been updated", Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				data.putExtra("location", ((EditLocationActivity)getActivity()).currentlyEditedLocation);
				getActivity().setResult(Activity.RESULT_OK, data);
				getActivity().finish();
	        }	
	    })
	    .setNegativeButton("No", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {Toast.makeText(LocationEditFirstFragment.this.getActivity(), 
        		"Location has not been edited", Toast.LENGTH_SHORT).show();
	        	getActivity().setResult(Activity.RESULT_CANCELED, null);
	        	LocationEditFirstFragment.this.getActivity().finish(); 
	        }
	
	    })
	    .show();
	}
	 
	private void checkLocationNameAndShowSecondFragment() {
		if(((TextView)getView().findViewById(R.id.edit_location_name)).getText().length() != 0)
		{
			showSecondEditFragment();
		}
		else
		{
			Toast.makeText(getActivity(), "Please provide at least location name", Toast.LENGTH_SHORT).show();	
		}
	}
	 
	private void showSecondEditFragment() {
		fillEditedLocationDetails();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out, android.R.animator.fade_in,
				android.R.animator.fade_out);
		ft.replace(getId(), new LocationEditSecondFragment());
		ft.addToBackStack("EditPart1");
		ft.commit();
	 }
	 
	 private Time showTimePicker(Time locationTime, String title) {
		final Time selectedTime = new Time(locationTime.getHour(), locationTime.getMinute());

		TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker,int selectedHour, int selectedMinute) {
						selectedTime.setHour(selectedHour);
						selectedTime.setMinute(selectedMinute);
						setActiveTimeStrings(getView(), mLocation.getTimeFrom(), mLocation.getTimeTo());
					}
				}, locationTime.getHour(), locationTime.getMinute(), true /* 24 hour time */);
		mTimePicker.setTitle(title);
		mTimePicker.setCancelable(false);
		mTimePicker.show();		
		
		return selectedTime;
	 }
	 
	 
	 private void setActiveTimeStrings(View v, Time from, Time to) {
		 ((Button)v.findViewById(R.id.edit_location_time_from_button)).setText("From: " + from.toString());
		 ((Button)v.findViewById(R.id.edit_location_time_to_button)).setText("To: " + to.toString());		 
	 }
	 
	 private void fillEditedLocationDetails() {
			mLocation.setName(String.valueOf(((TextView) getView().findViewById(R.id.edit_location_name)).getText()));
			mLocation.setWifiOn(((MultiStateToggleButton)getView().findViewById(R.id.edit_location_wifi_switch)).getStateValue());
			mLocation.setBluetoothOn(((MultiStateToggleButton)getView().findViewById(R.id.edit_location_bluetooth_switch)).getStateValue());
			mLocation.setNfcOn(((MultiStateToggleButton)getView().findViewById(R.id.edit_location_nfc_switch)).getStateValue());
			mLocation.setMobileData(((MultiStateToggleButton)getView().findViewById(R.id.edit_location_mobile_data_switch)).getStateValue());
			mLocation.setSoundOn(((MultiStateToggleButton)getView().findViewById(R.id.edit_location_sound_switch)).getStateValue());
			mLocation.setVibrationOn(((MultiStateToggleButton)getView().findViewById(R.id.edit_location_vibration_switch)).getStateValue());
			mLocation.setLocation(mLocation.getLocation());
			mLocation.setMon(((ToggleButton)getView().findViewById(R.id.toggle_monday)).isChecked());
			mLocation.setTue(((ToggleButton)getView().findViewById(R.id.toggle_tuesday)).isChecked());
			mLocation.setWed(((ToggleButton)getView().findViewById(R.id.toggle_wednesday)).isChecked());
			mLocation.setThu(((ToggleButton)getView().findViewById(R.id.toggle_thursday)).isChecked());
			mLocation.setFri(((ToggleButton)getView().findViewById(R.id.toggle_friday)).isChecked());
			mLocation.setSat(((ToggleButton)getView().findViewById(R.id.toggle_saturday)).isChecked());
			mLocation.setSun(((ToggleButton)getView().findViewById(R.id.toggle_sunday)).isChecked());
			mLocation.setShouldTurnOff(((Switch)getView().findViewById(R.id.edit_location_turn_off_switch)).isChecked());
	 }
}
