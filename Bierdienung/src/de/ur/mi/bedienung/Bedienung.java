package de.ur.mi.bedienung;

import de.ur.bierdienung.R;

import de.ur.mi.parse.ToDoListActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Bedienung extends Activity{
	
	private EditText tischNummer;
	private Button getraenkeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bedienung);
		getraenkeButton = (Button) findViewById(R.id.getraenke);
		tischNummer = (EditText) findViewById(R.id.nr);
		getraenkeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(Bedienung.this,
						ToDoListActivity.class);
				i.putExtra("tisch", Integer.parseInt(tischNummer.getText().toString()));
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
