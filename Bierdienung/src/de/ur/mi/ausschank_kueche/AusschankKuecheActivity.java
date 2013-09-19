package de.ur.mi.ausschank_kueche;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
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
import com.parse.ParsePush;
import com.parse.ParseQuery;
import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.AppSingleton;
import de.ur.mi.parse.ListViewAdapter_Kueche_Ausschank;
import de.ur.mi.parse.ParselistdownloadClass;

public class AusschankKuecheActivity extends ListActivity {
	// Declare Variables
	ListView listview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ListViewAdapter_Kueche_Ausschank adapter;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	private Button refresh;
	private String karte;
	AppSingleton appsingleton;
	private ArrayList<ParseObject> deleteList = new ArrayList<ParseObject>();
	private ArrayList<String> adapterListBestellung = new ArrayList<String>();
	private ArrayList<String> adapterListTisch = new ArrayList<String>();
	private ArrayList<String> adapterListBackground = new ArrayList<String>();
	private ArrayList<String> listArt = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appsingleton = AppSingleton.getInstance();
		setContentView(R.layout.activity_kueche_ausschank);
		refresh = (Button) findViewById(R.id.refresh);
		Bundle extras = getIntent().getExtras();
		karte = extras.getString("name");
		refreshButton();

		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
	}

	private void refreshButton() {
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Execute RemoteDataTask AsyncTask

				for (int i = 0; i < appsingleton.deleteObjectList.size(); i++) {

					// set the todoObject to the item in list
					final ParseObject paidItem = appsingleton.deleteObjectList
							.get(i);

					paidItem.put("Used", "used");

					try {
						paidItem.save();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				// PushNotification for Waiter who accepted order when meal is
				// cooked
				for (int i = 0; i < adapterListBackground.size(); i++) {

					if (adapterListBackground.get(i).equals("marked")
							&& listArt.get(i).equals("Essen")) {
						String key = adapterListBestellung.get(i) + " fertig!"
								+ " Tisch " + adapterListTisch.get(i);
						String kellnerName = parselistdownloadList.get(i)
								.getKellner();
						ParsePush push = new ParsePush();
						push.setChannel(kellnerName);
						push.setMessage(key);
						push.sendInBackground();
					}

				}

				adapterListBackground.clear();
				adapterListBestellung.clear();
				adapterListTisch.clear();
				listArt.clear();

				// Execute RemoteDataTask AsyncTask
				new RemoteDataTask().execute();

			}
		});
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
			// Locate the class table named "Country" in Parse.com
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					LoginSignupActivity.getParseUser() + "_Bestellung");
			query.whereEqualTo("Used", "unused");
			query.whereEqualTo("Art", karte);
			query.orderByAscending("Name");
			try {
				ob = query.find();

			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			int i = 0;
			appsingleton.objectList = (ArrayList<ParseObject>) ob;
			for (ParseObject Name : ob) {
				ParselistdownloadClass map = new ParselistdownloadClass();
				map.setName((String) Name.get("Name"));
				map.setTisch((String) Name.get("Tisch"));
				map.setArt((String) Name.get("Art"));
				map.setKellner((String) Name.get("Kellner"));
				map.setBackground((String) Name.get("Background"));

				parselistdownloadList.add(map);

				ob.get(i).put("Background", "unmarked");
				ob.get(i).saveInBackground();
				adapterListBestellung.add((String) Name.get("Name"));
				adapterListTisch.add((String) Name.get("Tisch"));
				adapterListBackground.add((String) Name.get("Background"));
				listArt.add((String) Name.get("Art"));

				i++;

			}

			// Locate the listview in listview_main.xml
			listview = (ListView) findViewById(R.id.list);
			// Pass the results into ListViewAdapter.java
			adapter = new ListViewAdapter_Kueche_Ausschank(
					AusschankKuecheActivity.this, adapterListBestellung,
					adapterListTisch, adapterListBackground);

			setListAdapter(adapter);
			adapter.notifyDataSetChanged();

			// Close the progressdialog
			mProgressDialog.dismiss();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (ob.get(position).get("Background").toString().equals("marked")) {

			ob.get(position).put("Background", "unmarked");
			ob.get(position).saveInBackground();

            adapterListBackground.set(position, ob.get(position).getString("Background"));
            adapter.notifyDataSetChanged();

            for (int i = 0; i < deleteList.size(); i++) {
				if (deleteList.get(i) == appsingleton.objectList.get(position)) {
					deleteList.remove(i);

				}
				appsingleton.deleteObjectList = deleteList;

			}

		} else {

			ob.get(position).put("Background", "marked");
			ob.get(position).saveInBackground();

			adapterListBackground.set(position,
					ob.get(position).getString("Background"));
			adapter.notifyDataSetChanged();

			deleteList.add(appsingleton.objectList.get(position));
			appsingleton.deleteObjectList = deleteList;

		}

	}

}