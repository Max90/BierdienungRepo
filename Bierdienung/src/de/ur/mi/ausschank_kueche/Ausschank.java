package de.ur.mi.ausschank_kueche;

import de.ur.bierdienung.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class Ausschank extends Activity{
	

	private String string;
	private int test;
	private int max;
	private int jonema;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ausschank);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

}
