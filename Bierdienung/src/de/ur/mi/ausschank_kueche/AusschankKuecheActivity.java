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
	private ListView listview;
	private List<ParseObject> orders;
	private ProgressDialog mProgressDialog;
	private ListViewAdapter_Kueche_Ausschank adapter;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	private Button refresh;
	private String karte;
	private ArrayList<ParseObject> deleteList = new ArrayList<ParseObject>();
	private ArrayList<String> adapterListBestellung = new ArrayList<String>();
	private ArrayList<String> adapterListTisch = new ArrayList<String>();
	private ArrayList<String> adapterListBackground = new ArrayList<String>();
	private ArrayList<String> listArt = new ArrayList<String>();

    public ArrayList<ParseObject> objectList = new ArrayList<ParseObject>();
    public ArrayList<ParseObject> deleteObjectList = new ArrayList<ParseObject>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kueche_ausschank);
		refresh = (Button) findViewById(R.id.refresh);
		Bundle extras = getIntent().getExtras();
		karte = extras.getString("name");
		refreshButton();
		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
        //dummyComment
    }

	private void refreshButton() {
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Execute RemoteDataTask AsyncTask

                for (int i = 0; i < deleteObjectList.size(); i++) {

                    // set the todoObject to the item in list
                    final ParseObject paidItem = deleteObjectList
                            .get(i);

					paidItem.put("Status", "fertig");
					paidItem.saveInBackground();
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
			query.whereEqualTo("Status", "aufgegeben");
			query.whereEqualTo("Art", karte);
			query.orderByAscending("Name");
			try {
				orders = query.find();

			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			int i = 0;
            objectList = (ArrayList<ParseObject>) orders;
            for (ParseObject order : orders) {
				ParselistdownloadClass map = new ParselistdownloadClass();
				map.setBackground((String) order.get("Background"));
				map.setName((String) order.get("Name"));
				map.setTisch((String) order.get("Tisch"));
				map.setArt((String) order.get("Art"));
				map.setKellner((String) order.get("Kellner"));
				

				parselistdownloadList.add(map);

				orders.get(i).put("Background", "unmarked");
				orders.get(i).saveInBackground();

				adapterListBackground.add((String) order.get("Background"));
				adapterListBestellung.add((String) order.get("Name"));
				adapterListTisch.add((String) order.get("Tisch"));
				listArt.add((String) order.get("Art"));

				i++;

			}

			// Locate the listview in listview_main.xml
			listview = (ListView) findViewById(R.id.list);
			// Pass the results into ListViewAdapter.java
			adapter = new ListViewAdapter_Kueche_Ausschank(
					AusschankKuecheActivity.this, adapterListBackground,
					adapterListBestellung, adapterListTisch);

			setListAdapter(adapter);
			adapter.notifyDataSetChanged();

			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (orders.get(position).get("Background").toString().equals("marked")) {

			orders.get(position).put("Background", "unmarked");
			orders.get(position).saveInBackground();

			adapterListBackground.set(position,
					orders.get(position).getString("Background"));
			adapter.notifyDataSetChanged();

			for (int i = 0; i < deleteList.size(); i++) {
                if (deleteList.get(i) == objectList.get(position)) {
                    deleteList.remove(i);
				}
                deleteObjectList = deleteList;
            }
		} else {
			orders.get(position).put("Background", "marked");
			orders.get(position).saveInBackground();

			adapterListBackground.set(position,
					orders.get(position).getString("Background"));
			adapter.notifyDataSetChanged();

            deleteList.add(objectList.get(position));
            deleteObjectList = deleteList;
        }
	}
}