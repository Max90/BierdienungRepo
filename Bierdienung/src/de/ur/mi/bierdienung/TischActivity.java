package de.ur.mi.bierdienung;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

public class TischActivity extends ListActivity {

    public static final int INSERT_ID = Menu.FIRST;
    public static final int TISCH_WECHSELN_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private List<ParseObject> todos;
    private ProgressDialog mProgressDialog;

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        // Override this method to do custom remote calls
        protected Void doInBackground(Void... params) {
            // Gets the current list of todos in sorted order
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    LoginSignupActivity.getParseUser() + "_Bestellung");
            query.whereEqualTo("Tisch", BedienungTischAuswahlActivity.getTNR());
            query.orderByDescending("_created_at");

            try {
                todos = query.find();
            } catch (ParseException e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(TischActivity.this);
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
            // Put the list of todos into the list view
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    TischActivity.this, R.layout.activity_todo_only_textview);
            for (ParseObject todo : todos) {
                adapter.add((String) todo.get("Name"));
            }
            setListAdapter(adapter);
            TischActivity.this.mProgressDialog.dismiss();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tisch);

        setTitle("Tisch " + BedienungTischAuswahlActivity.getTNR());

        new RemoteDataTask().execute();
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return result;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();

                // Delete the remote object
                final ParseObject todo = todos.get(info.position);

                new RemoteDataTask() {
                    protected Void doInBackground(Void... params) {
                        try {
                            todo.delete();
                        } catch (ParseException e) {
                        }
                        super.doInBackground();
                        return null;
                    }
                }.execute();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.speisekarte:
                Intent iEssen = new Intent(TischActivity.this,
                        SpeiseUndGetraenkeKartenActivity.class);
                iEssen.putExtra("name", "Essen");
                startActivity(iEssen);
                finish();
                return true;

            case R.id.getraenkekarte:
                Intent iGetraenke = new Intent(TischActivity.this,
                        SpeiseUndGetraenkeKartenActivity.class);
                iGetraenke.putExtra("name", "Getraenke");
                startActivity(iGetraenke);
                finish();
                return true;

            case R.id.change_table:
                finish();
                return true;

            case R.id.compute_table:
                Intent computeTableIntent = new Intent(TischActivity.this, AbrechnungsActivity.class);
                startActivity(computeTableIntent);
        }

        return super.onOptionsItemSelected(item);
    }

}