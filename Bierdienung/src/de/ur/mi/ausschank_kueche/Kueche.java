package de.ur.mi.ausschank_kueche;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import de.ur.bierdienung.R;

public class Kueche extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kueche);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
