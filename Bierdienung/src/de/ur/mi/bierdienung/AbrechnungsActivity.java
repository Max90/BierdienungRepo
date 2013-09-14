package de.ur.mi.bierdienung;

import java.util.ArrayList;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

public class AbrechnungsActivity extends ListActivity {

    private static final int DELETE_ID = Menu.FIRST + 1;

    private List<ParseObject> orderedItems;
    private ProgressDialog mProgressDialog;
    private double betrag = 0;
    private TextView Betrag;
    private Button bAbrechnen;
    private List<ParseObject> parselist;
    private ArrayList<ParseObject> list = new ArrayList<ParseObject>();
    final Context context = this;

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        // Override this method to do custom remote calls
        protected Void doInBackground(Void... params) {
            // Gets the current list of orderedItems in sorted order
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    LoginSignupActivity.getParseUser() + "_Bestellung");
            query.whereEqualTo("Tisch", BedienungTischAuswahlActivity.getTNR());
            query.orderByDescending("_created_at");

            try {
                orderedItems = query.find();
            } catch (ParseException e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(AbrechnungsActivity.this);
            mProgressDialog.setTitle("Lade Tischliste");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // Put the list of orderedItems into the list view
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    AbrechnungsActivity.this, R.layout.activity_todo_only_textview);
            for (ParseObject todo : orderedItems) {
                adapter.add((String) todo.get("Name"));
            }
            setListAdapter(adapter);
            AbrechnungsActivity.this.mProgressDialog.dismiss();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrechnung);
        Betrag = (TextView) findViewById(R.id.textview);

        setTitle("Tisch " + BedienungTischAuswahlActivity.getTNR());

        new RemoteDataTask().execute();
        registerForContextMenu(getListView());

        bAbrechnen = (Button) findViewById(R.id.bTischAbrechnen);

        bAbrechnen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Tisch abrechnen?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Alles auf der Rechnung?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Delete the remote object
                                for (int i = 0; i < list.size(); i++) {

                                    //set the todoObject to the item in list
                                    final ParseObject paidItem = list.get(i);

                                    new RemoteDataTask() {
                                        protected Void doInBackground(Void... params) {
                                            try {
                                                paidItem.delete();
                                            } catch (ParseException e) {
                                            }
                                            super.doInBackground();
                                            return null;
                                        }
                                    }.execute();
                                    mProgressDialog.dismiss();
                                }

                                Betrag.setText("Betrag insgesamt: ");
                            }
                        })
                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
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
                final ParseObject todo = orderedItems.get(info.position);

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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (v.getTag() == "red") {
            betrag = betrag - orderedItems.get(position).getDouble("Preis");
            v.setBackgroundColor(Color.TRANSPARENT);
            v.setTag("white");
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == orderedItems.get(position)) {
                    list.remove(i);
                }
            }
        } else {
            v.setBackgroundColor(Color.RED);
            v.setTag("red");
            betrag = betrag + orderedItems.get(position).getDouble("Preis");
            list.add(orderedItems.get(position));
        }

        Betrag.setText("Betrag insgesamt: " + betrag);

    }

}