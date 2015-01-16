package pl.papistudio.localizer3000;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragment displaying editable location details. User can set location on map.
 * There is also option to change radius.
 * help with map fragment: http://ucla.jamesyxu.com/?p=287
 * 
 * @author PapiTeam
 * 
 */
public class LocationEditSecondFragment extends Fragment implements OnClickListener, OnMapLongClickListener {
	/******************/
	/* VARIABLES 	  */
	/******************/
	private View mRootView;
	private MapView mMapView;

	/******************/
	/* FUNCTIONS 	  */
	/******************/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_location_edit2, container, false);
		mMapView = (MapView) mRootView.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		addOnClickActionsToButtons(mRootView);
		addMapListeners();
		changeMyLocationButtonPosition();
		
		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}
	
	@Override
	public void onClick(View v) {
		final Location location = ((EditLocationActivity)getActivity()).currentlyEditedLocation;
		
		if(v.getId() == R.id.edit_location_back_button)
			getFragmentManager().popBackStack();
		if(v.getId() == R.id.edit_location_save_button)
		{
			if(location.getLocation() != null)
				saveLocation();
			else
				Toast.makeText(getActivity(), "Location has to be set before saving", Toast.LENGTH_SHORT).show();				
		}
		if(v.getId() == R.id.radius_minus_button)
			updateRadius(location, -1);
		if(v.getId() == R.id.radius_plus_button)
			updateRadius(location, +1);
		if(v.getId() == R.id.radius_minus_ten_button)
			updateRadius(location, -10);
		if(v.getId() == R.id.radius_plus_ten_button)
			updateRadius(location, +10);
	}
	
	@Override
	public void onMapLongClick(LatLng point) {
		final Location location = ((EditLocationActivity)getActivity()).currentlyEditedLocation;
		
		android.location.Location loc = new android.location.Location("");
		loc.setLatitude(point.latitude);
		loc.setLongitude(point.longitude);
		location.setLocation(loc);
		
		drawMarkerWithCircle();
	}
	
	private void addOnClickActionsToButtons(View v) {
		((Button)v.findViewById(R.id.edit_location_back_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.edit_location_save_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.radius_minus_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.radius_plus_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.radius_minus_ten_button)).setOnClickListener(this);
		((Button)v.findViewById(R.id.radius_plus_ten_button)).setOnClickListener(this);
	}
	
	private void addMapListeners() {
		final GoogleMap map = mMapView.getMap();
		final Location location = ((EditLocationActivity)getActivity()).currentlyEditedLocation;
		MapsInitializer.initialize(getActivity());
		map.setMyLocationEnabled(true);	
		map.setOnMapLongClickListener(this);
		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(android.location.Location arg0) {
				if(location.getLocation() == null)
					location.setLocation(arg0);
				drawMarkerWithCircle();
				setMapCamera();				
				map.setOnMyLocationChangeListener(null);
			}
		});	
	}
	
	private void changeMyLocationButtonPosition() {
		// taken from:
		// http://stackoverflow.com/questions/14489880/how-to-change-the-position-of-maps-apis-get-my-location-button
		View locationButton = ((View) mMapView.findViewById(1).getParent()).findViewById(2);
		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
		rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
		rlp.setMargins(0, 30, 0, 30);
	}
	
	private void setMapCamera() {
		final GoogleMap map = mMapView.getMap();
		final Location location = ((EditLocationActivity)getActivity()).currentlyEditedLocation;
		MapsInitializer.initialize(getActivity());
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
	    .target(new LatLng(location.getLocation().getLatitude(), location.getLocation().getLongitude()))
	    .zoom(17)                  // Sets the zoom
	    .bearing(0)                // Sets the orientation of the camera to east
	    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
	    .build();                  // Creates a CameraPosition from the builder
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	private void updateRadius(Location location, int change) {
		location.setRadius(location.getRadius()+change);
		drawMarkerWithCircle();		
	}
	
	private void drawMarkerWithCircle(){
		final GoogleMap map = mMapView.getMap();
		final Location location = ((EditLocationActivity)getActivity()).currentlyEditedLocation;
		final LatLng position = new LatLng(location.getLocation().getLatitude(), 
											location.getLocation().getLongitude());
	    double radiusInMeters = location.getRadius();
	    int strokeColor = 0xffff0000; //red outline
	    int shadeColor = 0x44ff0000;  //opaque red fill

	    map.clear();
		map.addMarker(new MarkerOptions().position(position).title(location.getName()));
	    CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters)
	    							.fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
	    map.addCircle(circleOptions);
	}
	
	private void saveLocation() {
		final Location location = ((EditLocationActivity)getActivity()).currentlyEditedLocation;		
		final DatabaseHelper dbHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		final String originalName = ((EditLocationActivity)getActivity()).originalLocationName;
		final String locationName = (originalName.contentEquals("") ? location.getName() : originalName);
		boolean shouldFinish = true;
		
		if(dbHelper.getLocation(locationName) == null)
		{
			dbHelper.addLocation(location);
			Toast.makeText(getActivity(), "Location has been saved", Toast.LENGTH_SHORT).show();
		}
		else if(!originalName.contentEquals(""))
		{
			dbHelper.updateLocation(location, originalName);
			Toast.makeText(getActivity(), "Location has been updated", Toast.LENGTH_SHORT).show();
			Intent data = new Intent();
			data.putExtra("location", location);
			getActivity().setResult(Activity.RESULT_OK, data);
		}
		else
		{
			Toast.makeText(getActivity(), "Location with such name already exists!", Toast.LENGTH_SHORT).show();	
			shouldFinish = false;
		}
		
		if(shouldFinish)
			getActivity().finish();		
	}
}
