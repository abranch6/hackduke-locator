package deo.locater;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

}
