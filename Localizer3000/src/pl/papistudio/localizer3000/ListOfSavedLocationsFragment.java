package pl.papistudio.localizer3000;

import java.util.Collections;
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
import com.mobeta.android.dslv.DragSortListView.DropListener;

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
	private List<Location> mLocations;
	private LocationListAdapter mAdapter;
	
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
		private LayoutInflater mInflater;
		private List<Location> mList;
		private TextView mLocationName;
		private Activity mActivity;

		/******************/
		/*   FUNCTIONS    */
		/******************/
		public LocationListAdapter(Activity context, List<Location> list) {
			super();
			mActivity = context;
			this.mInflater = LayoutInflater.from(context);
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
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
		        rowView = mInflater.inflate(R.layout.saved_location_list_item, parent, false);
		    }
		    mLocationName = (TextView)rowView.findViewById(R.id.list_item_location_name);
		    mLocationName.setText(mList.get(position).getName());
		    
			/* Setting color */
			if (position % 2 == 0)
			{
				((RelativeLayout) rowView.findViewById(R.id.list_item_location))
						.setBackgroundColor(mActivity.getResources()
								.getColor(R.color.material_blue));
			}
			else
			{
				((RelativeLayout) rowView.findViewById(R.id.list_item_location))
						.setBackgroundColor(mActivity.getResources()
								.getColor(R.color.material_blue_lighter));
			}

		    /* Adding listener to onclick */
		    ((ImageButton)rowView.findViewById(R.id.delete_item_button)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("Item delete", "Deleted item number: " + position);
					DatabaseHelper dbHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
					dbHelper.deleteLocationAt(mList.get(position));
					mList.remove(position);
					mAdapter.notifyDataSetChanged();
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
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());	
		DragSortListView listView = (DragSortListView)v.findViewById(R.id.list_of_localizations);		
		
		mLocations = dbHelper.getAllLocations();	
		Collections.sort(mLocations);
		mAdapter = new LocationListAdapter(getActivity(), mLocations);
		listView.setAdapter(mAdapter);		
		addDropListener(listView);
		addListViewClickListener(listView, mAdapter);
	}
	
	private void addListViewClickListener(ListView listView, final LocationListAdapter adapter) {
		listView.setItemsCanFocus(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Location i = (Location) adapter.getItem(position);
					((SavedLocalizationsActivity)ListOfSavedLocationsFragment.this
							.getActivity()).setCurrentlyUsedLocation(i);
					showDetailsFragment();					
			}
			
			private void showDetailsFragment() {
				FragmentTransaction ft = ListOfSavedLocationsFragment.this.getFragmentManager().beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
							android.R.animator.fade_in, android.R.animator.fade_out);
				ft.replace(ListOfSavedLocationsFragment.this.getId(), new LocationDetailsFragment());
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
	
	private void addDropListener(DragSortListView listView) {
		listView.setDropListener(new DropListener() {

			@Override
			public void drop(int from, int to) {
				if(from > to)
				{
					Location l = mLocations.get(from);
					mLocations.add(to, l);
					mLocations.remove(from+1);					
				}
				if(from < to)
				{
					Location l = mLocations.get(from);
					mLocations.add(to+1, l);
					mLocations.remove(from);					
				}
				
				mAdapter.notifyDataSetChanged();
				updateLocationPriorities();
			}
		});

	}
	
	private void updateLocationPriorities() {
		int priority = 0;
		for(Location l : mLocations)
		{
			l.setPriority(priority++);
			DatabaseHelper.getInstance(getActivity()).updateLocation(l, l.getName());
		}
	}
}
