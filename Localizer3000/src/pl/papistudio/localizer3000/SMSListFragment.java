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

import com.melnykov.fab.FloatingActionButton;
import com.mobeta.android.dslv.DragSortListView;

public class SMSListFragment extends Fragment implements OnClickListener {
	/******************/
	/*   VARIABLES    */
	/******************/
	private List<SMS> smsList;
	private SMSListAdapter adapter;
	
	/**
	 * Internal list adapter class. 
	 * 
	 * @author PapiTeam
	 *
	 */
	private class SMSListAdapter extends BaseAdapter {
		/******************/
		/*   VARIABLES    */
		/******************/
		private LayoutInflater inflater;
		private List<SMS> list;
		private TextView smsName;
		private Activity activity;

		/******************/
		/*   FUNCTIONS    */
		/******************/
		public SMSListAdapter(Activity context, List<SMS> list)
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
		    smsName = (TextView)rowView.findViewById(R.id.list_item_location_name);
		    smsName.setText(list.get(position).getName());
		    
			/* Setting color */
			if (position % 2 == 0)
				((RelativeLayout) rowView.findViewById(R.id.list_item_location))
						.setBackgroundColor(activity.getResources()
								.getColor(R.color.material_blue));
			else
				((RelativeLayout) rowView.findViewById(R.id.list_item_location))
						.setBackgroundColor(activity.getResources()
								.getColor(R.color.material_blue_lighter));


			((ImageButton)rowView.findViewById(R.id.delete_item_button)).setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Log.d("Item delete", "Deleted item number: " + position);
					DatabaseHelper dbHelper = DatabaseHelper.getInstance(getActivity());
					dbHelper.deleteSMSAt(list.get(position));
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sms_list, container, false);
		fillListWithLocations(rootView);
		((FloatingActionButton)rootView.findViewById(R.id.sms_add_button)).setOnClickListener(this);
		
		return rootView;
	}
	
	/**
	 * Reaction to button click.
	 * Shows fragment to create new SMS message.
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		addNewSMS();		
	}	
	
	private void fillListWithLocations(View v) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());	
		DragSortListView listView = (DragSortListView)v.findViewById(R.id.list_of_sms);		
		
		smsList = dbHelper.getAllSMS();
		adapter = new SMSListAdapter(getActivity(), smsList);
		listView.setAdapter(adapter);	
		addListViewClickListener(listView, adapter);
	}
	
	private void addListViewClickListener(ListView listView, final SMSListAdapter adapter) {
		listView.setItemsCanFocus(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					SMS i = (SMS)adapter.getItem(position);
					((SMSActivity)SMSListFragment.this
							.getActivity()).setCurrentlyUsedSMS(i);
					showDetailsFragment();					
			}			
		});
	}
	
	private void addNewSMS() {
		((SMSActivity)SMSListFragment.this.getActivity()).setCurrentlyUsedSMS(null);
		showDetailsFragment();
	}
	
	private void showDetailsFragment() {
		FragmentTransaction ft = SMSListFragment.this.getFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
					android.R.animator.fade_in, android.R.animator.fade_out);
		ft.replace(SMSListFragment.this.getId(), new SMSDetailsFragment(), "SMSDetails");
		ft.addToBackStack(null);
		ft.commit();
	}
}
