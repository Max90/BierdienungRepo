package de.ur.mi.bierdienung;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;

//import android.widget.AdapterView.AdapterContextMenuInfo;

public class GetraenkekarteActivity extends ListActivity {

	private List<ParseObject> getraenkeList;
	private Dialog progressDialog;
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		// Override this method to do custom remote calls
		protected Void doInBackground(Void... params) {
			// Gets the current list of todos in sorted order
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					"Getraenke");
			query.orderByDescending("_created_at");
			try {
				getraenkeList = query.find();
			} catch (ParseException e) {
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			GetraenkekarteActivity.this.progressDialog = ProgressDialog.show(GetraenkekarteActivity.this,
					"", "Loading...", true);
			super.onPreExecute();
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(Void result) {
			// Put the list of todos into the list view
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					GetraenkekarteActivity.this, R.layout.todo_row);
			for (ParseObject todo : getraenkeList) {
				adapter.add((String) todo.get("Name"));
			}
			setListAdapter(adapter);
			GetraenkekarteActivity.this.progressDialog.dismiss();
		}
	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		new RemoteDataTask().execute();
		registerForContextMenu(getListView());
	}

}