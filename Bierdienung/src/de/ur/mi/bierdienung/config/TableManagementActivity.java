package de.ur.mi.bierdienung.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

// gets input of max table number in restaurant and saves that number in appSingleton
public class TableManagementActivity extends Activity {
	Button tableNumberFinishButton;
	EditText tableNumberEditText;
	private String tableNumber = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table_management);
		setupUI();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupUI() {
		tableNumberEditText = (EditText) findViewById(R.id.table_number_edit_text);
		tableNumberFinishButton = (Button) findViewById(R.id.set_table_number_button);
		tableNumberFinishButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// saves number of tables
				// links back to PrefereancesActivity
				tableNumber = tableNumberEditText.getText().toString();
				ParseObject objectToSave = new ParseObject(LoginSignupActivity
						.getParseUser() + "_Table");
				objectToSave.put("TableNumber", tableNumber);
				objectToSave.saveInBackground();

				Intent preferencesIntent = new Intent(
						TableManagementActivity.this,
						ManagementActivity.class);
				startActivity(preferencesIntent);
				finish();
			}
		});
	}
}