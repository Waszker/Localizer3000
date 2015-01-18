package pl.papistudio.localizer3000;

import java.text.DecimalFormat;

import android.content.Context;

/**
 * <p>Static class that takes care of
 * converting location coordinates
 * to system chosen by the user.</p>
 * 
 * @author PapiTeam
 *
 */
public class CoordinatesConverter {
	/******************/
	/*   VARIABLES    */
	/******************/
	
	/******************/
	/*   FUNCTIONS    */
	/******************/
	public static String convertLocationToString(Context context, 
								double latitude, double longitude, float accuracy) {
		
		StringBuilder text = new StringBuilder();
		
		switch(context.getSharedPreferences(
				MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
				.getString(MainActivity.COORDINATES_TYPE, "Degrees"))
		{
			case "Degrees":
				DecimalFormat f = new DecimalFormat("0.#####");
				text.append(f.format(latitude) + "\u00B0 \n" + 
				 f.format(longitude) + "\u00B0 \n");
				break;
				
			case "Minutes":
				int sec = (int)Math.round(latitude*3600);
				int deg = sec/3600;
				sec = Math.abs(sec % 3600);
				int min = sec / 60;
				sec %= 60;
				
				int sec2 = (int)Math.round(longitude*3600);
				int deg2 = sec2/3600;
				sec2 = Math.abs(sec2 % 3600);
				int min2 = sec2 / 60;
				sec2 %= 60;
				
				text.append(deg + "\u00B0 " + min + "' " + sec + "\"\n" +
							deg2 + "\u00B0 " + min2 + "' " + sec2 + "\"\n");
				break;
		}
		text.append("with accuracy: " +
				 	accuracy + "m.");
		
		return text.toString();
		
	}
	
	private CoordinatesConverter() {
		
	}

}
