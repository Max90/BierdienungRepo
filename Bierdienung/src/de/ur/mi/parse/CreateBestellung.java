package de.ur.mi.parse;

import de.ur.bierdienung.R;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateBestellung extends Activity {

	private EditText nameText;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_bestellung);
		nameText = (EditText) findViewById(R.id.name);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String name = extras.getString("name");
			position = extras.getInt("position");

			if (name != null) {
				nameText.setText(name);
			}
		}

		Button confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString("name", nameText.getText().toString());
				bundle.putInt("position", position);

				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_bestellung, menu);
		return true;
	}

}
