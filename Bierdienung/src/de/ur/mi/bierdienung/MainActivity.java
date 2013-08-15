package de.ur.mi.bierdienung;


import de.ur.bierdienung.R;
import de.ur.mi.ausschank_kueche.Ausschank;
import de.ur.mi.ausschank_kueche.Kueche;
import de.ur.mi.bedienung.Bedienung;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button einstellungenButton;
	private Button bedienungButton;
	private Button ausschankButton;
	private Button kuecheButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
	}

	private void setupUI() {
		einstellungenButton = (Button) findViewById(R.id.einstellungen);
		bedienungButton = (Button) findViewById(R.id.bedienung);
		ausschankButton = (Button) findViewById(R.id.ausschank);
		kuecheButton = (Button) findViewById(R.id.kueche);

		einstellungenButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						Einstellungen.class);
				startActivity(i);

			}
		});

		bedienungButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						Bedienung.class);
				startActivity(i);

			}
		});

		ausschankButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						Ausschank.class);
				startActivity(i);

			}
		});

		kuecheButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						Kueche.class);
				startActivity(i);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
