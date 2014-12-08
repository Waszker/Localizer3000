package pl.papistudio.localizer3000;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

public class PreferencesFragment extends PreferenceFragment implements OnPreferenceClickListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        ((Preference)findPreference("interval")).setOnPreferenceClickListener(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(android.R.color.black));

        return view;
    }
    
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if(preference.getKey().contentEquals("interval"))
			showPickerDialog();
		
		return false;
	}
	
	private void showPickerDialog() {
		// taken from
		// http://stackoverflow.com/questions/15536908/preference-activity-on-preference-click-listener
		RelativeLayout linearLayout = new RelativeLayout(getActivity());
		final NumberPicker aNumberPicker = new NumberPicker(getActivity());
		aNumberPicker.setMaxValue(60);
		aNumberPicker.setMinValue(1);
//		aNumberPicker.setValue(getActivity().getSharedPreferences(name, mode))

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				50, 50);
		RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
		numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		linearLayout.setLayoutParams(params);
		linearLayout.addView(aNumberPicker, numPicerParams);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setTitle("Select the number");
		alertDialogBuilder.setView(linearLayout);
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.e("",
								"New Quantity Value : "
										+ aNumberPicker.getValue());

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
