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
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.parse.ListViewAdapter;
import de.ur.mi.parse.ParselistdownloadClass;

public class GetraenkekarteActivity extends Activity {
	
	// Declare Variables
	
	ListView listview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ListViewAdapter adapter;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	
	public static final int INSERT_ID = Menu.FIRST;
	public static final int TISCH_WECHSELN_ID = Menu.FIRST + 1;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from listview_main.xml
		setContentView(R.layout.listview_main);
		
		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	
		case R.id.tisch:
			Intent iTisch = new Intent(GetraenkekarteActivity.this,
					TischActivity.class);
			startActivity(iTisch);
			finish();
			return true;
			
		case R.id.speisekarte:
			Intent iEssen = new Intent(GetraenkekarteActivity.this,
					EssenkarteActivity.class);
			startActivity(iEssen);
			finish();
			return true;
			
		case R.id.action_settings:
			finish();
			return true;
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
			mProgressDialog = new ProgressDialog(GetraenkekarteActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Lade Getraenkeliste");
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
						"Getraenke");

				ob = query.find();
				for (ParseObject Name : ob) {
					ParselistdownloadClass map = new ParselistdownloadClass();
					map.setName((String) Name.get("Name"));
					map.setPreis((String) Name.get("Preis"));
					map.setArt((String) Name.get("Art"));
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
			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into ListViewAdapter.java
			adapter = new ListViewAdapter(GetraenkekarteActivity.this,
					parselistdownloadList);
			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}
}