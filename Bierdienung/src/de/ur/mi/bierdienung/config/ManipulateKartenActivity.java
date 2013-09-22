package de.ur.mi.bierdienung.config;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

public class ManipulateKartenActivity extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    public static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private List<ParseObject> todos;
    private Dialog progressDialog;

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        protected Void doInBackground(Void... params) {
            // Gets the current list of todos in sorted order
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    LoginSignupActivity.getParseUser() + "_"
                            + EinstellungenActivity.getKarte());
            query.orderByDescending("_created_at");

            try {
                todos = query.find();
            } catch (ParseException e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            ManipulateKartenActivity.this.progressDialog = ProgressDialog.show(
                    ManipulateKartenActivity.this, "", "Loading...", true);
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
                    ManipulateKartenActivity.this,
                    R.layout.activity_todo_only_textview);
            for (ParseObject todo : todos) {
                adapter.add((String) todo.get("Name"));
            }
            setListAdapter(adapter);
            ManipulateKartenActivity.this.progressDialog.dismiss();

        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tisch);

        if (EinstellungenActivity.getKarte().equals("Getraenke")) {
            setTitle(R.string.drink_menu_string);
        } else {
            setTitle(R.string.meal_menu_string);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setHomeButtonEnabled(true);
        }

        new RemoteDataTask().execute();
        registerForContextMenu(getListView());
    }

    private void createTodo() {
        Intent i = new Intent(this, CreateProdukt.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null) {
            return;
        }
        final Bundle extras = intent.getExtras();

        switch (requestCode) {
            case ACTIVITY_CREATE:
                new RemoteDataTask() {
                    protected Void doInBackground(Void... params) {
                        String name = extras.getString("name");
                        String preis = extras.getString("preis");
                        String kategorie = extras.getString("kategorie");
                        ParseObject ob = new ParseObject(
                                LoginSignupActivity.getParseUser() + "_"
                                        + EinstellungenActivity.getKarte());
                        ob.put("Name", name);
                        ob.put("Preis", preis);
                        ob.put("Kategorie", kategorie);
                        ob.put("Art", EinstellungenActivity.getKarte());

                        try {
                            ob.save();
                        } catch (ParseException e) {
                        }

                        super.doInBackground();
                        return null;
                    }
                }.execute();
                break;
            case ACTIVITY_EDIT:
                // Edit the remote object
                final ParseObject ob;
                ob = todos.get(extras.getInt("position"));
                ob.put("Name", extras.getString("name"));
                ob.put("Preis", extras.getString("preis"));
                ob.put("Kategorie", extras.getString("kategorie"));
                ob.put("Art", EinstellungenActivity.getKarte());

                new RemoteDataTask() {
                    protected Void doInBackground(Void... params) {
                        try {
                            ob.save();
                        } catch (ParseException e) {
                        }
                        super.doInBackground();
                        return null;
                    }
                }.execute();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_bestellung, menu);
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
            case R.id.action_add:
                createTodo();
                return true;

            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, CreateProdukt.class);
        i.putExtra("name", todos.get(position).getString("Name").toString());
        i.putExtra("preis", todos.get(position).getString("Preis").toString());
        i.putExtra("kategorie", todos.get(position).getString("Kategorie")
                .toString());
        i.putExtra("position", position);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

}