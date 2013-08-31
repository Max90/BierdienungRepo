package de.ur.mi.bierdienung;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.ur.bierdienung.R;

public class BedienungTischAuswahlActivity extends Activity {

	private EditText tischNummer;
	private Button bEnterBedienung;
	private Button bAbrechnung;
	private static String tNr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bedienung);

		bEnterBedienung = (Button) findViewById(R.id.bEnterBedienung);
		bAbrechnung = (Button) findViewById(R.id.bAbrechnung);
		tischNummer = (EditText) findViewById(R.id.nr);

		bEnterBedienung.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(BedienungTischAuswahlActivity.this,
						TischActivity.class);
				tNr = tischNummer.getText().toString();
				if (tNr.equals("")) {
					Toast.makeText(v.getContext(), "Bitte Tisch eingeben",
							Toast.LENGTH_SHORT).show();
					return;
				}
				startActivity(i);
			}
		});

		bAbrechnung.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(BedienungTischAuswahlActivity.this,
						AbrechnungsActivity.class);
				tNr = tischNummer.getText().toString();
				if (tNr.equals("")) {
					Toast.makeText(v.getContext(), "Bitte Tisch eingeben",
							Toast.LENGTH_SHORT).show();
					return;
				}
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
