package deo.locater;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.EditText;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	LocationManager service;
	private String provider;
	LocationListener locationListener;
	Location currentLocation;
	
	//double for location
	public final static String LONGITUDE = "deo.locator.LONGITUDE";
	public final static String LATITUDE = "deo.locator.LATITUDE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		service = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
	    provider = service.getBestProvider(criteria, false);
	    currentLocation = service.getLastKnownLocation(provider);
		
	    Log.i("Text","Test");
	    
	    final Context temp = this;
	    
		locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	currentLocation = location;
		    	Toast.makeText(temp,"Updated GPS", Toast.LENGTH_SHORT).show();
		    }

		
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
		};
		service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, locationListener);
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
	    //Location location = service.getLastKnownLocation(provider);
	    Log.i("latitude",Double.toString(currentLocation.getLatitude()));
	    Log.i("Longitude",Double.toString(currentLocation.getLongitude()));
	    
	    Intent intent = new Intent(this, InfoActivity.class);
	    intent.putExtra(LATITUDE, currentLocation.getLatitude());
	    intent.putExtra(LONGITUDE, currentLocation.getLongitude());
	    startActivity(intent);
	} 

}
