package de.ur.mi.bierdienung;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.ur.bierdienung.R;

public class Bedienung extends Activity {

	private EditText tischNummer;
	private Button bEnterBedienung;
	private static String tNr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bedienung);

		bEnterBedienung = (Button) findViewById(R.id.bEnterBedienung);
		tischNummer = (EditText) findViewById(R.id.nr);

		bEnterBedienung.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Bedienung.this, TischActivity.class);
				tNr = tischNummer.getText().toString();

				startActivity(i);
			}
		});
	}

	public static String getTNR() {
		return tNr;
	}

	@Override
	protected void onResume() {
		tischNummer.setText("");
		tischNummer.requestFocus();
		super.onResume();
	}

}
