package de.ur.mi.bierdienung.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.ur.bierdienung.R;

public class EinstellungenActivity extends Activity {

	private Button speiseKarten;
	private Button getraenkeKarten;
    private Button tableManagementButton;
    private static String karte;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_einstellungen);
		setupUI();

		speiseKarten.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				karte = "Essen";
				Intent i = new Intent(EinstellungenActivity.this,
						ManipulateKartenActivity.class);
				i.putExtra("name", "Essen");
				startActivity(i);
			}
		});

		getraenkeKarten.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				karte = "Getraenke";
				Intent i = new Intent(EinstellungenActivity.this,
						ManipulateKartenActivity.class);
				i.putExtra("name", "Getraenke");
				startActivity(i);
			}
		});

        tableManagementButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tableManagementActivity = new Intent(EinstellungenActivity.this, TableManagementActivity.class);
                startActivity(tableManagementActivity);
            }
        });

    }

	public static String getKarte() {
		return karte;
	}

	private void setupUI() {
		speiseKarten = (Button) findViewById(R.id.speisekarteeingeben);
		getraenkeKarten = (Button) findViewById(R.id.getraenkekarteeingeben);
        tableManagementButton = (Button) findViewById(R.id.button_table_management);
    }

}