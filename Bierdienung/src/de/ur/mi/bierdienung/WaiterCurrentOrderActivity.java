package de.ur.mi.bierdienung;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import de.ur.mi.parse.ParselistdownloadClass;
import de.ur.mi.parse.WaiterCurrentOrderListViewAdapter;

public class WaiterCurrentOrderActivity extends ListActivity {
	// Declare Variables
	private ListView listview;
	private List<ParseObject> orders;
	private ProgressDialog mProgressDialog;
	private WaiterCurrentOrderListViewAdapter adapter;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	private Button buttonDeleteMarked;
	private Button buttonSendCurrentOrder;

	public ArrayList<ParseObject> objectList = new ArrayList<ParseObject>();
	public ArrayList<ParseObject> deleteObjectList = new ArrayList<ParseObject>();

	private ArrayList<ParseObject> deleteList = new ArrayList<ParseObject>();
	private ArrayList<String> adapterListBestellung = new ArrayList<String>();
	private ArrayList<String> adapterListTisch = new ArrayList<String>();
	private ArrayList<String> adapterListBackground = new ArrayList<String>();
	private ArrayList<String> listArt = new ArrayList<String>();
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiter_current_order);

		buttonDeleteMarked = (Button) findViewById(R.id.delete_marked_button);
		buttonSendCurrentOrder = (Button) findViewById(R.id.send_current_order_button);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}

		deleteMarkedOrder();
		sendCurrentOrder();

		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
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

	private void sendCurrentOrder() {
		buttonSendCurrentOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < orders.size(); i++) {

					// set the todoObject to the item in list
					final ParseObject paidItem = orders.get(i);
					paidItem.put("Status", "aufgegeben");
					paidItem.saveInBackground();
				}

				adapterListBackground.clear();
				adapterListBestellung.clear();
				adapterListTisch.clear();
				listArt.clear();

				// Link to WaiterTableSelectActivity
				Intent waiterTableSelectActivity = new Intent(
						WaiterCurrentOrderActivity.this,
						WaiterTableSelectActivity.class);
				startActivity(waiterTableSelectActivity);
				waiterTableSelectActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
			}
		});
	}

	private void deleteMarkedOrder() {
		buttonDeleteMarked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Execute RemoteDataTask AsyncTask

				for (int i = 0; i < deleteObjectList.size(); i++) {

					// set the todoObject to the item in list
					final ParseObject paidItem = deleteObjectList.get(i);

					paidItem.deleteInBackground();

				}

				adapterListBackground.clear();
				adapterListBestellung.clear();
				adapterListTisch.clear();
				listArt.clear();
				adapter.notifyDataSetChanged();
				// Execute RemoteDataTask AsyncTask
				new RemoteDataTask().execute();

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			// set title
			alertDialogBuilder
					.setTitle("Wollen Sie Ihre Bestellung abbrechen?");
			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Ihre Bestellung wird nicht an die K�che/Ausschank gesendet. Trotzdem zur�ck gehen?")
					.setCancelable(false)
					.setPositiveButton("Ja",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							})
					.setNegativeButton("Nein",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
		return true;
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
			mProgressDialog.setTitle("Lade Liste");
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
			for (ParseObject Name : orders) {
				ParselistdownloadClass map = new ParselistdownloadClass();
				map.setName((String) Name.get("Name"));
				map.setTisch((String) Name.get("Tisch"));
				map.setArt((String) Name.get("Art"));
				map.setKellner((String) Name.get("Kellner"));
				map.setBackground((String) Name.get("Background"));

				parselistdownloadList.add(map);
				parselistdownloadList.add(map);

				orders.get(i).put("Background", "unmarked");
				orders.get(i).saveInBackground();
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