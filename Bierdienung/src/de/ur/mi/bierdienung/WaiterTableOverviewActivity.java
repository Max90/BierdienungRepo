package de.ur.mi.bierdienung;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

public class WaiterTableOverviewActivity extends ListActivity {

	public static final int INSERT_ID = Menu.FIRST;
	public static final int TISCH_WECHSELN_ID = Menu.FIRST + 1;

	private List<ParseObject> orders;
	private ProgressDialog mProgressDialog;

	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		// Override this method to do custom remote calls
		protected Void doInBackground(Void... params) {
			// Gets the current list of orders
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					LoginSignupActivity.getParseUser() + "_Bestellung");
			query.whereEqualTo("Tisch", WaiterTableSelectActivity.getTNR());
			query.orderByDescending("_created_at");

			try {
				orders = query.find();
			} catch (ParseException e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(
					WaiterTableOverviewActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Lade Tischliste");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// Put the list of orders into the list view
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					WaiterTableOverviewActivity.this,
					R.layout.activity_todo_only_textview);
			for (ParseObject order : orders) {
				adapter.add((String) order.get("Name"));
			}
			setListAdapter(adapter);
			WaiterTableOverviewActivity.this.mProgressDialog.dismiss();
		}
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tisch);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}

		new RemoteDataTask().execute();
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		menu.findItem(R.id.tisch).setTitle(
				"Tisch " + WaiterTableSelectActivity.getTNR());
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.speisekarte:
			Intent iEssen = new Intent(WaiterTableOverviewActivity.this,
					FoodMenuActivity.class);
			iEssen.putExtra("name", "Essen");
			startActivity(iEssen);
			finish();
			return true;

		case R.id.getraenkekarte:
			Intent iGetraenke = new Intent(WaiterTableOverviewActivity.this,
					DrinksMenuActivity.class);
			iGetraenke.putExtra("name", "Getraenke");
			startActivity(iGetraenke);
			finish();
			return true;

		case R.id.change_table:
			finish();
			return true;

		case R.id.compute_table:
			Intent computeTableIntent = new Intent(
					WaiterTableOverviewActivity.this,
					WaiterCashUpActivity.class);
			startActivity(computeTableIntent);

		case android.R.id.home:
			finish();
			return true;
		default:
		}

		return super.onOptionsItemSelected(item);
	}

}