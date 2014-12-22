package pl.papistudio.localizer3000;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
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

public class SMSDetailsFragment extends Fragment implements OnClickListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	private static final int REQUEST_CONTACT_NUMBER = 123456789;
	private SMS sms;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sms_details, container, false);
		sms = ((SMSActivity)getActivity()).getCurrentlyUsedSMS();
		
		if(sms != null)
			fillSMSDetails(sms, rootView);
		else
		{
			sms = new SMS(0, "", 0, "", null);
			((SMSActivity)getActivity()).setCurrentlyUsedSMS(sms);
		}			
		setSpinnerItems(rootView);
		addListenersToButtons(rootView);
		
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
			getFragmentManager().popBackStack();
		if(v.getId() == R.id.edit_sms_save_button)
		{
			sms.setLocationToSend((Location)((Spinner)getView().findViewById(R.id.sms_location_chooser)).getSelectedItem());
			sms.setMessageText(((EditText)getView().findViewById(R.id.sms_message_text)).getText().toString());
			getFragmentManager().popBackStack(); // TODO: change to save!
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == Activity.RESULT_OK) 
	        {
	            if(data != null && requestCode == REQUEST_CONTACT_NUMBER) 
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
		                            sms.setReceiverNumber(Integer.parseInt(formattedPhoneNumber));
		                    		((EditText)getView().findViewById(R.id.sms_receiver_number)).setText(formattedPhoneNumber);
	                            } catch(NumberFormatException e) {}
	                        }
	                        cursor.close();
	                }
	            }
	        }
	}
	
	private void fillSMSDetails(SMS sms, View v) {
		((EditText)v.findViewById(R.id.sms_receiver_number)).setText(sms.getReceiverNumber());
		((EditText)v.findViewById(R.id.sms_message_text)).setText(sms.getMessageText());
	}
	
	private void setSpinnerItems(View v) {
		Spinner spinner = (Spinner)v.findViewById(R.id.sms_location_chooser);
		List<Location> list = DatabaseHelper.getInstance(getActivity()).getAllLocations();
		ArrayAdapter<Location> spinnerArrayAdapter = new ArrayAdapter<Location>(getActivity(), 
															android.R.layout.simple_spinner_item, list);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);
	}
	
	private void addListenersToButtons(View v) {
		((Button)v.findViewById(R.id.edit_sms_save_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_sms_cancel_button)).setOnClickListener(this);
		((ImageButton)v.findViewById(R.id.add_sms_receiver_button)).setOnClickListener(this);
	}
}
