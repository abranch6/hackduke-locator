package deo.locater;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra(MainActivity.LATITUDE,0.0);
		double longitude = intent.getDoubleExtra(MainActivity.LONGITUDE,0.0);

		EditText latitudeTxt = (EditText) findViewById(R.id.latitudeTxt);
		EditText longitudeTxt = (EditText) findViewById(R.id.longitudeTxt);
		
		latitudeTxt.setText(Double.toString(latitude));
		longitudeTxt.setText(Double.toString(longitude));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
