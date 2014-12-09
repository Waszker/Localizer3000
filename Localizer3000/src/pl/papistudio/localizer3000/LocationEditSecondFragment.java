package pl.papistudio.localizer3000;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

/**
 * Fragment displaying editable location details. User can set location on map.
 * help with map fragment: http://ucla.jamesyxu.com/?p=287
 * 
 * @author PapiTeam
 * 
 */
public class LocationEditSecondFragment extends Fragment implements OnClickListener {
	/******************/
	/* VARIABLES 	  */
	/******************/
	private View rootView;
	MapView mapView;

	/******************/
	/* FUNCTIONS 	  */
	/******************/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_location_edit2, container, false);
		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		addOnClickActionsToButtons(rootView);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.edit_location_back_button)
			getFragmentManager().popBackStack();
		if(v.getId() == R.id.edit_location_save_button)
		{
			// TODO: enter here saving to database
			DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
			dbHelper.AddLocation(((EditLocationActivity)getActivity()).currentlyEditedLocation);
			Toast.makeText(getActivity(), "Location has been saved", Toast.LENGTH_SHORT).show();
			getActivity().finish();
		}
	}
	
	private void addOnClickActionsToButtons(View v) {
		((Button)v.findViewById(R.id.edit_location_back_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_location_save_button)).setOnClickListener(this);
	}
}
