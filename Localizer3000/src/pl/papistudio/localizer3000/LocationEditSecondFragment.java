package pl.papistudio.localizer3000;

import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Fragment displaying editable location
 * details. User can set location on map.
 * WARNING!!! Highy bad code! Don't look here for now :P
 * 
 * @author PapiTeam
 *
 */
public class LocationEditSecondFragment extends Fragment {
	/******************/
	/*   VARIABLES    */
	/******************/
	private View rootView;
	
	/******************/
	/*   FUNCTIONS    */
	/******************/	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(rootView != null) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
	        if (parent != null)
	            parent.removeView(rootView);
		}
		try 
		{
			rootView = inflater.inflate(R.layout.fragment_location_edit2, container, false);
		}
		catch(InflateException e) {
			 /* map is already there, just return view as it is */
			// TODO: show error?
			e.printStackTrace();
		}
		
		
		GoogleMap map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		
		return rootView;
	}
	
	@Override
	public void onDestroyView() {
		// taken from: http://stackoverflow.com/questions/14083950/duplicate-id-tag-null-or-parent-id-with-another-fragment-for-com-google-androi
	    super.onDestroyView();
	    MapFragment m = (MapFragment) getFragmentManager()
	                                         .findFragmentById(R.id.map);
	    if (m != null) 
	        getFragmentManager().beginTransaction().remove(m).commit();
	}
}
