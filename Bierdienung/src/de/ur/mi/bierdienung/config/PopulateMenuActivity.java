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

public class PopulateMenuActivity extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private static final int DELETE_ID = Menu.FIRST + 1;

    private List<ParseObject> orders;
    private Dialog progressDialog;

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            // Gets the current list of orders in sorted order
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    LoginSignupActivity.getParseUser() + "_"
                            + ManagementActivity.getMenu());
            query.orderByDescending("_created_at");

            try {
                orders = query.find();
            } catch (ParseException e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            PopulateMenuActivity.this.progressDialog = ProgressDialog.show(
                    PopulateMenuActivity.this, "", "Loading...", true);
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
                    PopulateMenuActivity.this,
                    R.layout.menu_list_item);
            for (ParseObject order : orders) {
                adapter.add((String) order.get("Name"));
            }
            setListAdapter(adapter);
            PopulateMenuActivity.this.progressDialog.dismiss();

        }
    }


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        if (ManagementActivity.getMenu().equals("Getraenke")) {
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

    private void createProduct() {
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
                        String price = extras.getString("preis");
                        String category = extras.getString("kategorie");
                        ParseObject ob = new ParseObject(
                                LoginSignupActivity.getParseUser() + "_"
                                        + ManagementActivity.getMenu());
                        ob.put("Name", name);
                        ob.put("Preis", price);
                        ob.put("Kategorie", category);
                        ob.put("Art", ManagementActivity.getMenu());

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
                ob = orders.get(extras.getInt("position"));
                ob.put("Name", extras.getString("name"));
                ob.put("Preis", extras.getString("preis"));
                ob.put("Kategorie", extras.getString("kategorie"));
                ob.put("Art", ManagementActivity.getMenu());

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
        inflater.inflate(R.menu.create_order, menu);
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
                final ParseObject order = orders.get(info.position);

                new RemoteDataTask() {
                    protected Void doInBackground(Void... params) {
                        try {
                            order.delete();
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
                createProduct();
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
        i.putExtra("name", orders.get(position).getString("Name"));
        i.putExtra("preis", orders.get(position).getString("Preis"));
        i.putExtra("kategorie", orders.get(position).getString("Kategorie"));
        i.putExtra("position", position);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

}