package de.ur.mi.bierdienung.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import de.ur.bierdienung.R;

public class CreateProdukt extends Activity {

	private EditText nameText;
	private EditText preisText;
	private int position;

	private RadioButton radio0;
	private RadioButton radio1;
	private RadioButton radio2;
	private RadioButton radio3;
	private Button confirmButton;
	private String kat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_createprodukt);
		setTitle("Produkt ändern");

		setUI();

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				if (nameText.getText().length() < 1
						|| preisText.getText().length() < 1) {
					Toast.makeText(view.getContext(),
							"Bitte Eingaben vervollständigen",
							Toast.LENGTH_SHORT).show();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putString("name", nameText.getText().toString());
				bundle.putString("preis", preisText.getText().toString());
				getkat();
				bundle.putString("kategorie", kat);
				bundle.putInt("position", position);

				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}

			private void getkat() {
				if (radio0.isChecked()) {
					kat = radio0.getText().toString();
				} else if (radio1.isChecked()) {
					kat = radio1.getText().toString();
				} else if (radio2.isChecked()) {
					kat = radio2.getText().toString();
				} else if (radio3.isChecked()) {
					kat = radio3.getText().toString();
				}
			}
		});

	}

	private void setUI() {

		confirmButton = (Button) findViewById(R.id.buttonuebernehmen);
		preisText = (EditText) findViewById(R.id.editpreis);
		nameText = (EditText) findViewById(R.id.editname);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		radio3 = (RadioButton) findViewById(R.id.radio3);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String name = extras.getString("name");
			position = extras.getInt("position");
			String preis = extras.getString("preis");
			kat = extras.getString("kategorie");
			if (name != null) {
				nameText.setText(name);
				preisText.setText(preis);

				if (kat.equals("Alkoholfrei")) {
					radio0.setChecked(true);
				} else if (kat.equals("Bier")) {
					radio1.setChecked(true);
				} else if (kat.equals("Schnaps")) {
					radio2.setChecked(true);
				} else if (kat.equals("Sonstiges")) {
					radio3.setChecked(true);
				} else if (kat.equals("Vorspeise")) {
					radio0.setChecked(true);
				} else if (kat.equals("Hauptspeise")) {
					radio1.setChecked(true);
				} else if (kat.equals("Nachspeise")) {
					radio2.setChecked(true);
				}

			}
		}

		if (EinstellungenActivity.getKarte().length() > 6) {
			radio0.setText("Alkoholfrei");
			radio1.setText("Bier");
			radio2.setText("Schnaps");
			radio3.setText("Sonstiges");
		} else {
			radio0.setText("Vorspeise");
			radio1.setText("Hauptspeise");
			radio2.setText("Nachspeise");
			radio3.setVisibility(View.INVISIBLE);
		}

	}
}
