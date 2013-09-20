package de.ur.mi.bierdienung;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.ListViewAdapter;
import de.ur.mi.parse.ParselistdownloadClass;

public class MenuActivity extends Activity {

	// Declare Variables
	private ListView listview;
	private List<ParseObject> orders;
	private ProgressDialog mProgressDialog;
	private ListViewAdapter adapter;
	private List<ParselistdownloadClass> parselistdownloadList = null;

	public static final int INSERT_ID = Menu.FIRST;
	public static final int TISCH_WECHSELN_ID = Menu.FIRST + 1;

	private String karte;
	private Button buttonWaiterCurrentOrder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from listview_main.xml
		setContentView(R.layout.activity_speisekarte);

		Bundle extras = getIntent().getExtras();
		karte = extras.getString("name");

		buttonWaiterCurrentOrder = (Button) findViewById(R.id.send_or_change_order_button);

		buttonWaiterCurrentOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuActivity.this,
						WaiterCurrentOrderActivity.class);
				startActivity(i);

			}
		});

		setTitle("Tisch " + WaiterTableSelectActivity.getTNR());

		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.tisch:
			Intent iTisch = new Intent(MenuActivity.this,
					WaiterTableOverviewActivity.class);
			startActivity(iTisch);
			finish();
			return true;

		case R.id.speisekarte:
			Intent iEssen = new Intent(MenuActivity.this, MenuActivity.class);
			iEssen.putExtra("name", "Essen");
			startActivity(iEssen);
			finish();
			return true;

		case R.id.getraenkekarte:
			Intent iGetraenke = new Intent(MenuActivity.this,
					MenuActivity.class);
			iGetraenke.putExtra("name", "Getraenke");
			startActivity(iGetraenke);
			finish();
			return true;

		case R.id.change_table:
			finish();
			return true;

		case R.id.compute_table:
			Intent computeTableIntent = new Intent(MenuActivity.this,
					WaiterCashUpActivity.class);
			startActivity(computeTableIntent);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return result;
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(MenuActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Lade " + karte + "liste");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create the array
			parselistdownloadList = new ArrayList<ParselistdownloadClass>();
			try {
				// Locate the class table named "Country" in Parse.com
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						LoginSignupActivity.getParseUser() + "_" + karte);
				query.orderByAscending("Name");
				if (karte.length() > 6) {
					query.orderByAscending("Kategorie");
				} else {
					query.orderByDescending("Kategorie");
				}
				orders = query.find();
				for (ParseObject order : orders) {
					ParselistdownloadClass map = new ParselistdownloadClass();
					map.setName((String) order.get("Name"));
					map.setPreis((String) order.get("Preis"));
					map.setArt((String) order.get("Art"));
					map.setKategorie((String) order.get("Kategorie"));

					parselistdownloadList.add(map);
				}
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Locate the listview in listview_main.xml
			listview = (ListView) findViewById(R.id.list);
			// Pass the results into ListViewAdapter.java
			adapter = new ListViewAdapter(MenuActivity.this,
					parselistdownloadList);
			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}
}