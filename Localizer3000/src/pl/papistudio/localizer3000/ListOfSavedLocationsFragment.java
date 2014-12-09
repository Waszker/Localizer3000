package pl.papistudio.localizer3000;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortListView;

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
	private LocationListAdapter adapter;
	
	/**
	 * Internal list adapter class. 
	 * 
	 * @author PapiTeam
	 *
	 */
	private class LocationListAdapter extends BaseAdapter {
		/******************/
		/*   VARIABLES    */
		/******************/
		private LayoutInflater inflater;
		private List<Location> list;
		private TextView locationName;
		private Activity activity;

		/******************/
		/*   FUNCTIONS    */
		/******************/
		public LocationListAdapter(Activity context, List<Location> list)
		{
			super();
			activity = context;
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
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View rowView = convertView;

		    if(rowView == null)
		    {
		        rowView = inflater.inflate(R.layout.saved_location_list_item, parent, false);
		    }
		    locationName = (TextView)rowView.findViewById(R.id.list_item_location_name);
		    locationName.setText(list.get(position).getName());
		    
		    /* Setting color */
		    // TODO: change!
		    if(position % 2 == 0)
		    	((RelativeLayout)rowView.findViewById(R.id.list_item_location)).setBackgroundColor(activity.getResources().getColor(R.color.material_blue));
		    else
		    	((RelativeLayout)rowView.findViewById(R.id.list_item_location)).setBackgroundColor(activity.getResources().getColor(R.color.material_blue_lighter));
		    
		    /* Adding listener to onclick */
		    ((ImageButton)rowView.findViewById(R.id.delete_item_button)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO: wrap it in a method?
					Log.d("Item delete", "Deleted item number: " + position);
					DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
					dbHelper.deleteLocationAt(list.get(position));
					dbHelper.close();
					list.remove(position);
					adapter.notifyDataSetChanged();
				}
			});

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
		DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
		locations = dbHelper.getAllLocations(); // here put database connection! :D
		dbHelper.close();
		
		DragSortListView listView = (DragSortListView)v.findViewById(R.id.list_of_localizations);
		adapter = new LocationListAdapter(getActivity(), locations);
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
