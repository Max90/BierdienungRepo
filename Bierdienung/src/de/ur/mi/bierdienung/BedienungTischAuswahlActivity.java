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
import de.ur.mi.parse.AppSingleton;

public class BedienungTischAuswahlActivity extends Activity {

	private EditText tischNummer;
	private Button bEnterBedienung;
	private Button bAbrechnung;
	private static String tNr;
    AppSingleton appSingleton;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bedienung);

        appSingleton = AppSingleton.getInstance();

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
                Intent i = new Intent(BedienungTischAuswahlActivity.this, AbrechnungsActivity.class);
                tNr = tischNummer.getText().toString();
				if (tNr.equals("")) {
                    Toast.makeText(v.getContext(), "Bitte Tisch eingeben", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(tischNummer.getText().toString()) > Integer.parseInt(appSingleton.tableNumber))
                    Toast.makeText(v.getContext(), "Tischnummer zu hoch. Wir haben nur " + appSingleton.tableNumber + " Tische", Toast.LENGTH_LONG).show();
                startActivity(i);
			}
		});
	}

	public static String getTNR() {
		return tNr;
        //dummyComment
    }

	@Override
	protected void onResume() {
		tischNummer.setText("");
		tischNummer.requestFocus();
		super.onResume();
	}

}
