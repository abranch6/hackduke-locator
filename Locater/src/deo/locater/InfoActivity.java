package deo.locater;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


public class InfoActivity extends FragmentActivity {
	
	LocationManager service;
	private String provider;
	LocationListener locationListener;
	Location currentLocation;
	Marker currentMarker;
	GoogleMap map;
	boolean direction = false;
	LatLng car, me;
	double latitude, longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		Intent intent = getIntent();
		latitude = intent.getDoubleExtra(MainActivity.LATITUDE,0.0);
		longitude = intent.getDoubleExtra(MainActivity.LONGITUDE,0.0);
		
		SharedPreferences preferences = getSharedPreferences(MainActivity.PREF, MODE_PRIVATE);
	    
    	if(preferences.getBoolean("Location Stored", false))
    	{
    		latitude = preferences.getFloat("Latitude", (float)0.0);
        	longitude = preferences.getFloat("Longitude", (float)0.0);
    	}
		
		// Get a handle to the Map Fragment
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        car = new LatLng(latitude,longitude);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(car, 13));

        map.addMarker(new MarkerOptions()
                .title("My Car")
                .snippet("Bitch")
                .position(car));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	
	@Override
	protected void onResume() 
	{	
    	super.onResume();
    	SharedPreferences preferences = getSharedPreferences(MainActivity.PREF, MODE_PRIVATE);
    	
    	if(!preferences.getBoolean("Location Stored", false)){
    		SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI

    	    editor.putFloat("Latitude", (float)latitude);
    	    editor.putFloat("Longitude", (float)longitude);
    	    editor.putBoolean("Location Stored", true);    
    	    editor.commit();
    	}
    		
    	else
    	{
    		latitude = preferences.getFloat("Latitude", (float)0.0);
        	longitude = preferences.getFloat("Longitude", (float)0.0);
    	}
    	
	}

	public void findCar(View view)
	{
		service = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		SharedPreferences preferences = getSharedPreferences(MainActivity.PREF, MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI

		editor.putBoolean("Location Stored", false);    
		editor.commit();
		//Log.i("CarLatitude", Double.toString(car.latitude));
		//Log.i("CarLongitude", Double.toString(car.longitude));

		Criteria criteria = new Criteria();
	    provider = service.getBestProvider(criteria, false);
	    currentLocation = service.getLastKnownLocation(provider);
			    	    
		locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	currentLocation = location;		
		    	
		    	if(!direction)
		    	{
		    		me = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
		    		Log.i("Testing our input",Double.toString(me.latitude));
		    		Log.i("Testing our input",Double.toString(me.longitude));
		    		//me = new LatLng(36.0014,-78.9388);	    		
		    		new DirectionAPI(map).execute(getURL(me,car));
		    		direction = true;
		    	}
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
	
	 public String getURL(LatLng start, LatLng end) {
	        String url = "http://maps.googleapis.com/maps/api/directions/xml?" 
	                + "origin=" + start.latitude + "," + start.longitude  
	                + "&destination=" + end.latitude + "," + end.longitude 
	                + "&sensor=false&units=metric&mode=walking";

	        return url;
	    }
	}
