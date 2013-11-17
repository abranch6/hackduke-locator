package deo.locater;

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
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class InfoActivity extends FragmentActivity {
	
	LocationManager service;
	private String provider;
	LocationListener locationListener;
	Location currentLocation;
	Marker currentMarker;
	GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra(MainActivity.LATITUDE,0.0);
		double longitude = intent.getDoubleExtra(MainActivity.LONGITUDE,0.0);
		
		// Get a handle to the Map Fragment
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        LatLng car = new LatLng(latitude,longitude);

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
	public void findCar(View view)
	{
service = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
	    provider = service.getBestProvider(criteria, false);
	    currentLocation = service.getLastKnownLocation(provider);
			    	    
		locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	currentLocation = location;		  
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
}
