package de.ur.mi.bierdienung;

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
import com.parse.ParseQuery;
import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.AppSingleton;
import de.ur.mi.parse.WaiterCurrentOrderListViewAdapter;
import de.ur.mi.parse.ParselistdownloadClass;

public class WaiterCurrentOrderActivity extends ListActivity {
	// Declare Variables
	private ListView listview;
	private List<ParseObject> ob;
	private ProgressDialog mProgressDialog;
	private WaiterCurrentOrderListViewAdapter adapter;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	private Button buttonDeleteMarked;
	private Button buttonSendCurrentOrder;

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
		setContentView(R.layout.activity_waiter_current_order);
		buttonDeleteMarked = (Button) findViewById(R.id.delete_marked_button);
		buttonSendCurrentOrder = (Button) findViewById(R.id.send_current_order_button);

		deleteMarkedOrder();
		sendCurrentOrder();

		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
	}

	private void sendCurrentOrder() {
		buttonSendCurrentOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i = 0; i < appsingleton.deleteObjectList.size(); i++) {

					// set the todoObject to the item in list
					final ParseObject paidItem = appsingleton.deleteObjectList
							.get(i);

					paidItem.put("Status", "in Bearbeitung");
					paidItem.saveInBackground();


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


	private void deleteMarkedOrder() {
		buttonDeleteMarked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Execute RemoteDataTask AsyncTask

				for (int i = 0; i < appsingleton.deleteObjectList.size(); i++) {

					// set the todoObject to the item in list
					final ParseObject paidItem = appsingleton.deleteObjectList
							.get(i);

					paidItem.deleteInBackground();

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
			mProgressDialog = new ProgressDialog(
					WaiterCurrentOrderActivity.this);
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
			query.whereEqualTo("Status", "offen");

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
			adapter = new WaiterCurrentOrderListViewAdapter(
					WaiterCurrentOrderActivity.this, adapterListBestellung,
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

			adapterListBackground.set(position,
					ob.get(position).getString("Background"));
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