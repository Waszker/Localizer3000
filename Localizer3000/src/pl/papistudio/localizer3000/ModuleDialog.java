package pl.papistudio.localizer3000;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * <p>Static class that provides method to
 * show dialog with system checkup.</p>
 * 
 * @author PapiTeam
 *
 */
public class ModuleDialog {
	/******************/
	/*   VARIABLES    */
	/******************/
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	public static void showModuleDialog(final Context context) {
		boolean[] available = System.checkPhoneModules(context);
	
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.module_availability_dialog);
		dialog.setTitle(R.string.dialog_modules_main_title);
		dialog.setCancelable(false);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.location_availability), available[0]);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.gps_location_availability), available[1]);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.wifi_availability), available[2]);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.bluetooth_availability), available[3]);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.gps_availability), available[4]);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.vibrations_availability), available[5]);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.mobile_availability), available[6]);
		setTextAndColorForModuleDialog((TextView)dialog.findViewById(R.id.root_availability), available[7]);
		((Button)dialog.findViewById(R.id.ok_button)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		((Button)dialog.findViewById(R.id.never_again_button)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
					.edit().putBoolean(MainActivity.CHECK_FOR_MODULES_PREFERENCE, false).commit();
				dialog.dismiss();				
			}
		});
		dialog.show();			
	}

	private static void setTextAndColorForModuleDialog(TextView text, boolean isAvailable) {
		if (isAvailable) 
		{
			text.setText(R.string.dialog_modules_supported);
			text.setTextColor((Color.GREEN));
		} 
		else 
		{
			text.setText(R.string.dialog_modules_unsupported);
			text.setTextColor((Color.RED));
		}
	}
	
	private ModuleDialog() {
	}

}
