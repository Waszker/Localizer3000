package pl.papistudio.localizer3000;

import java.util.ArrayList;
import java.util.List;

import com.mobeta.android.dslv.DragSortListView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Fragment displaying list of saved locations.
 * 
 * @author PapiTeam
 *
 */
public class ListOfSavedLocationsFragment extends Fragment {
	/******************/
	/*   VARIABLES    */
	/******************/
	private List<Location> locations;
	
	/**
	 * Internal list adapter class. 
	 * 
	 * @author PapiTeam
	 *
	 */
	private static class LocationListAdapter extends BaseAdapter {
		/******************/
		/*   VARIABLES    */
		/******************/
		private LayoutInflater inflater;
		private List<Location> list;
		private TextView locationName;

		/******************/
		/*   FUNCTIONS    */
		/******************/
		public LocationListAdapter(Activity context, List<Location> list)
		{
			super();
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;

		    if(rowView == null)
		    {
		        rowView = inflater.inflate(R.layout.saved_location_list_item, parent, false);
		    }
		    locationName = (TextView)rowView.findViewById(R.id.list_item_location_name);
		    locationName.setText(list.get(position).getName());
		    
		    /* Setting color */
		    if(position % 2 == 0)
		    	locationName.setBackgroundColor(Color.BLACK);
		    else
		    	locationName.setBackgroundColor(Color.DKGRAY);

		    return rowView;
		}
		
	}

	/******************/
	/*   FUNCTIONS    */
	/******************/
	public ListOfSavedLocationsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_saved_localizations, container, false);
		fillListWithLocations(rootView);
		return rootView;
	}
	
	private void fillListWithLocations(View v) {
		// TODO change way of declaring and filling list
		// TODO: add reactions to deleting items by fling!
		// TODO: check: https://github.com/bauerca/drag-sort-listview/blob/master/demo/res/layout/checkable_main.xml
		locations = new ArrayList<>();
		locations.add(new Location("DOM", true, false, false, true));
		locations.add(new Location("Babcia", true, true, true, true));
		locations.add(new Location("Uczelnia", true, false, true, false));
		locations.add(new Location("Miasto", false, false, true, true));
		
		DragSortListView listView = (DragSortListView)v.findViewById(R.id.list_of_localizations);
		LocationListAdapter adapter = new LocationListAdapter(getActivity(), locations);
		listView.setAdapter(adapter);		
		addListViewClickListener(listView, adapter);
	}
	
	private void addListViewClickListener(ListView listView, final LocationListAdapter adapter) {
		listView.setItemsCanFocus(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					Location i = (Location) adapter.getItem(position);
					Log.d("Click", "Clicked "+i.getName());
					((SavedLocalizationsActivity)ListOfSavedLocationsFragment.this.getActivity()).currentlyUsedLocation = i;
					showDetailsFragment(i);					
			}
			
			private void showDetailsFragment(Location location) {
				FragmentTransaction ft = ListOfSavedLocationsFragment.this.getFragmentManager().beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
							android.R.animator.fade_in, android.R.animator.fade_out);
				ft.replace(ListOfSavedLocationsFragment.this.getId(), new LocationDetailsFragment());
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
}
