package de.ur.mi.ausschank_kueche;

import java.util.ArrayList;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;

import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.ListViewAdapter_Kueche_Ausschank;
import de.ur.mi.parse.ParselistdownloadClass;

public class AusschankKuecheActivity extends Activity {
	// Declare Variables

	ListView listview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ListViewAdapter_Kueche_Ausschank adapter;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	private Button refresh;
	private String karte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kueche_ausschank);

		refresh = (Button) findViewById(R.id.refresh);

		Bundle extras = getIntent().getExtras();
		karte = extras.getString("name");

		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RemoteDataTask().execute();
				

			}
		});

		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
	}
	


	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(AusschankKuecheActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Lade Ausschankliste");
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
						LoginSignupActivity.getParseUser() + "_Bestellung");
				query.whereEqualTo("Art", karte);
				query.orderByAscending("Name");
				ob = query.find();
				for (ParseObject Name : ob) {
					ParselistdownloadClass map = new ParselistdownloadClass();
					map.setName((String) Name.get("Name"));
					map.setPreis((String) Name.get("Tisch"));
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
			listview = (ListView) findViewById(R.id.list);
			// Pass the results into ListViewAdapter.java
			adapter = new ListViewAdapter_Kueche_Ausschank(
					AusschankKuecheActivity.this, parselistdownloadList);
			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}

}
