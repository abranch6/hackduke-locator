package deo.locater;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.EditText;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	LocationManager service;
	private String provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		service = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onPause() 
	{
	    super.onPause();
	    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
	    SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI

	    editor.putFloat("Latitude", (float)0.0);
	    editor.putFloat("Longitude", (float)0.0);
	    editor.putBoolean("Location Stored", true);    
	    editor.commit();
	}
	
	@Override
    	protected void onResume() 
    	{	
        	super.onResume();
        	SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        
        	preferences.getBoolean("Location Stored", false);
        	preferences.getFloat("Latitude", (float)0.0);
        	preferences.getFloat("Longitude", (float)0.0);
    	}

	public void parkCar(View view)
	{
		boolean gpsEnable = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		if(!gpsEnable)
		{
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		Criteria criteria = new Criteria();
	    provider = service.getBestProvider(criteria, false);
	    Location location = service.getLastKnownLocation(provider);
	    Log.i("latitude",Double.toString(location.getLatitude()));
	    Log.i("Longitude",Double.toString(location.getLongitude()));
	}

}
