package pl.papistudio.localizer3000;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import de.greenrobot.event.EventBus;

public class PreferencesFragment extends PreferenceFragment 
								implements OnPreferenceClickListener, OnPreferenceChangeListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	private int mInterval;
	private String mLocationType;
	private SharedPreferences mSharedPref;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        addOnClickListeners();
        initializePreferenceVariables();
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
		{
			showPickerDialog();
		}
		else if(preference.getKey().contentEquals("modules"))
		{
			ModuleDialog.showModuleDialog(getActivity());
		}
		
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference.getKey().contentEquals("location_coords"))
		{
			mLocationType = (getResources()
					.getStringArray(R.array.coordinateSystem))[Integer.parseInt((String)newValue)-1];
			saveCoordinatesSystemToSharedPreferences(mLocationType);
			changePreferenceTexts();
		}
		
		return true;
	}
	
	private void addOnClickListeners() {
        ((Preference)findPreference("interval")).setOnPreferenceClickListener(this);
        ((Preference)findPreference("modules")).setOnPreferenceClickListener(this);	
        ((ListPreference)findPreference("location_coords")).setOnPreferenceChangeListener(this);		
	}
	
	private void initializePreferenceVariables() {
		mSharedPref = getActivity().getSharedPreferences(MainActivity.SHARED_PREFERENCES, 
													 	Context.MODE_PRIVATE);
        mInterval = mSharedPref.getInt(MainActivity.INTERVAL_PREFERENCE, 5);
        mLocationType = mSharedPref.getString(MainActivity.COORDINATES_TYPE, "Degrees");
        changePreferenceTexts();
	}
	
	private void showPickerDialog() {
		// taken from
		// http://stackoverflow.com/questions/15536908/preference-activity-on-preference-click-listener
		final NumberPicker picker = new NumberPicker(getActivity());
		RelativeLayout layout = getNumberPickerLayout(picker);
		prepareNumberPicker(picker);		
		getPickerDialog(picker, layout).show();
	}
	
	private void prepareNumberPicker(NumberPicker picker) {
		picker.setMaxValue(60);
		picker.setMinValue(1);
		picker.setValue(mInterval);		
	}
	
	private RelativeLayout getNumberPickerLayout(NumberPicker picker) {
		RelativeLayout relativeLayout = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				50, 50);
		RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		relativeLayout.setLayoutParams(params);
		relativeLayout.addView(picker, numPicerParams);
		
		return relativeLayout;
	}
	
	private AlertDialog getPickerDialog(final NumberPicker picker, RelativeLayout layout) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("Select the number");
		alertDialogBuilder.setView(layout);
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {						
						mInterval = picker.getValue();
						saveIntervalToSharedPreferences();
						changePreferenceTexts();
						EventBus.getDefault().post(Integer.valueOf(mInterval*1000));
						Log.d("Preference Fragment", "New interval value : " + picker.getValue());
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		return alertDialogBuilder.create();
	}
	
	private void saveIntervalToSharedPreferences() {
		SharedPreferences.Editor editor = mSharedPref.edit();
		editor.putInt(MainActivity.INTERVAL_PREFERENCE, mInterval);
		editor.commit();
	}
	
	private void saveCoordinatesSystemToSharedPreferences(String type) {
		SharedPreferences.Editor editor = mSharedPref.edit();
		editor.putString(MainActivity.COORDINATES_TYPE, type);
		editor.commit();		
	}
	
	private void changePreferenceTexts() {
		((Preference)findPreference("interval")).setSummary(mInterval + " min.");
		((ListPreference)findPreference("location_coords")).setSummary(mLocationType);
	}
}