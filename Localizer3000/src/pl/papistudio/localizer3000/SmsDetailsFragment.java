package pl.papistudio.localizer3000;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SmsDetailsFragment extends Fragment implements OnClickListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	private static final int REQUEST_CONTACT_NUMBER = 123456789;
	private Sms mSms;
	private List<Location> mListOfLocations;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sms_details, container, false);
		mListOfLocations = DatabaseHelper.getInstance(getActivity()).getAllLocations();
		mSms = ((SmsActivity)getActivity()).getCurrentlyUsedSMS();
		
		setSpinnerItems(rootView);
		addListenersToButtons(rootView);
		fillSMSDetails(mSms, rootView);				
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.add_sms_receiver_button)
		{
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
	        startActivityForResult(contactPickerIntent, REQUEST_CONTACT_NUMBER);
		}
		
		if(v.getId() == R.id.edit_sms_cancel_button)
		{
			showCancelConfirmationDialog();
		}
		
		if(v.getId() == R.id.edit_sms_save_button)
		{
			saveSMS();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == Activity.RESULT_OK) 
	        {
	            if(data != null && requestCode == REQUEST_CONTACT_NUMBER) // we just came back form contact picking 
	            {  
	                Uri uriOfPhoneNumberRecord = data.getData();
	                String idOfPhoneRecord = uriOfPhoneNumberRecord.getLastPathSegment();
	                Cursor cursor = getActivity().getContentResolver().query(Phone.CONTENT_URI, 
	                														 new String[]{Phone.NUMBER}, 
	                														 Phone._ID + "=?", 
	                														 new String[]{idOfPhoneRecord}, 
	                														 null);
	                if(cursor != null) 
	                {
	                        if(cursor.getCount() > 0) 
	                        {
	                            cursor.moveToFirst();
	                            String formattedPhoneNumber = cursor.getString( cursor.getColumnIndex(Phone.NUMBER) );
	                            formattedPhoneNumber = formattedPhoneNumber.replaceAll("\\+..|\\s", "");
	                            try {
	                            	Integer.parseInt(formattedPhoneNumber);
		                    		((EditText)getView().findViewById(R.id.sms_receiver_number)).setText(formattedPhoneNumber);
	                            } catch(NumberFormatException e) {}
	                        }
	                        cursor.close();
	                }
	            }
	        }
	}
	
	/**
	 * Shows exit confirmation dialog.
	 */
	public void reactToBackPress() {
		showCancelConfirmationDialog();
	}
	
	private void setSpinnerItems(View v) {
		Spinner spinner = (Spinner)v.findViewById(R.id.sms_location_chooser);
		ArrayAdapter<Location> spinnerArrayAdapter = new ArrayAdapter<Location>(getActivity(), 
															android.R.layout.simple_spinner_item, mListOfLocations);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);
	}
	
	private void addListenersToButtons(View v) {
		((Button)v.findViewById(R.id.edit_sms_save_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_sms_cancel_button)).setOnClickListener(this);
		((ImageButton)v.findViewById(R.id.add_sms_receiver_button)).setOnClickListener(this);
	}
	
	private void fillSMSDetails(Sms sms, View v) {
		if(sms != null)
		{
			((EditText)v.findViewById(R.id.sms_receiver_number)).setText(String.valueOf(sms.getReceiverNumber()));
			((EditText)v.findViewById(R.id.sms_message_text)).setText(sms.getMessageText());
			setSpinnerSelectedLocation(((Spinner)v.findViewById(R.id.sms_location_chooser)), sms);	
			((Switch)v.findViewById(R.id.sms_one_time_checkbox)).setChecked(sms.isOneTimeUse());
		}
	}
	
	private void setSpinnerSelectedLocation(Spinner spinner, Sms sms) {
		for(int i=0; i<mListOfLocations.size(); i++)
		{
			if(mListOfLocations.get(i).getName().contentEquals(sms.getLocationNameToSend()))
			{
				spinner.setSelection(i);
				break;
			}
		}
	}
	
	private void showCancelConfirmationDialog() {
		new AlertDialog.Builder(getActivity())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Cancel SMS edition")
        .setMessage("Are you sure you want to stop editing this sms?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	getActivity().setResult(Activity.RESULT_CANCELED, null);
	        	SmsDetailsFragment.this.getFragmentManager().popBackStack();
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
	
	private boolean parseAndCheckSMSDataValidity() {
		boolean isValid = false;		
		if(((EditText)getView().findViewById(R.id.sms_receiver_number)).getText().length() > 0 // validity is set by checking receiver number
			&& (Location)((Spinner)getView().findViewById(R.id.sms_location_chooser)).getSelectedItem() != null)
		{
			isValid = true;
		}
		
		return isValid;
	}
	
	private void saveSMS() {
		if(parseAndCheckSMSDataValidity())
		{
			fillSMSBeforeSaving();
			saveOrUpdateSMS();
			getFragmentManager().popBackStack();
		}
		else
		{
			Toast.makeText(getActivity(), "Fill all SMS details before saving.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void fillSMSBeforeSaving() {
		if(mSms == null)
		{
			mSms = new Sms("", 0, "", null, true);
		}
		mSms.setReceiverNumber(Integer.valueOf(
				((EditText)getView().findViewById(R.id.sms_receiver_number)).getText().toString()));
		mSms.setLocationNameToSend(
				((Location)((Spinner)getView().findViewById(R.id.sms_location_chooser)).getSelectedItem()).getName());
		mSms.setMessageText(
				((EditText)getView().findViewById(R.id.sms_message_text)).getText().toString());
		mSms.setIsOneTimeUse(
				((Switch)getView().findViewById(R.id.sms_one_time_checkbox)).isChecked());
	}
	
	private void saveOrUpdateSMS() {
		if(mSms.getUniqueIdNumber() == -1)
		{
			DatabaseHelper.getInstance(getActivity()).addSMS(mSms);
		}
		else
		{
			DatabaseHelper.getInstance(getActivity()).updateSMS(mSms);
		}
	}
}
