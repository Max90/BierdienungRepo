package de.ur.mi.bierdienung;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.CashUpAdapter;

public class WaiterCashUpActivity extends ListActivity {

    private List<ParseObject> orders;
    private ProgressDialog mProgressDialog;
    private double amount = 0;
    private TextView textViewAmount;
    private Button buttonCashUp;
    private Button buttonCashUpMarked;
    private ArrayList<ParseObject> list = new ArrayList<ParseObject>();
    final Context context = this;
    private CashUpAdapter adapterCashUp;
    private ArrayList<String> adapterList = new ArrayList<String>();
    private ArrayList<String> adapterListBackground = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_up);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setHomeButtonEnabled(true);
        }

        setUpUi();

        setTitle("Tisch " + WaiterTableSelectActivity.getTNR());
        registerForContextMenu(getListView());

        cashUp();

        new RemoteDataTask().execute();

    }

    private void setUpUi() {
        buttonCashUp = (Button) findViewById(R.id.cash_up_table_button);
        buttonCashUpMarked = (Button) findViewById(R.id.sum_up_marked_button);
        textViewAmount = (TextView) findViewById(R.id.text_view_sum);
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

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        // Override this method to do custom remote calls
        protected Void doInBackground(Void... params) {
            // Gets the current list of orders
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    LoginSignupActivity.getParseUser() + "_Bestellung");
            query.whereEqualTo("Tisch", WaiterTableSelectActivity.getTNR());
            query.whereEqualTo("Status", "fertig");
            query.orderByDescending("Art");
            query.orderByAscending("Name");

            try {
                orders = query.find();
            } catch (ParseException e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(WaiterCashUpActivity.this);
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
            // Put the list of orders into the list view
            int i = 0;

            for (ParseObject order : orders) {
                orders.get(i).put("Background", "unmarked");
                orders.get(i).saveInBackground();
                adapterList.add((String) order.get("Name"));
                adapterListBackground.add((String) order.get("Background"));
                i++;
            }
            adapterCashUp = new CashUpAdapter(context, adapterList,
                    adapterListBackground);

            setListAdapter(adapterCashUp);
            adapterCashUp.notifyDataSetChanged();
            WaiterCashUpActivity.this.mProgressDialog.dismiss();
        }
    }

    private void cashUp() {
        buttonCashUpMarked.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = "Alles auf der Rechnung? Betrag: "
                        + String.format("%.2f", amount) + " Euro!";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Tisch abrechnen?");

                // set dialog message
                alertDialogBuilder
                        .setMessage(text)
                        .setCancelable(false)
                        .setPositiveButton("Ja",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // Execute RemoteDataTask AsyncTask
                                        new RemoteDataTask() {
                                            protected Void doInBackground(
                                                    Void... params) {
                                                // Delete the remote object
                                                for (int i = 0; i < list.size(); i++) {

                                                    // set the todoObject to the
                                                    // item in
                                                    // list
                                                    final ParseObject paidItem = list
                                                            .get(i);

                                                    paidItem.put("Status",
                                                            "abgerechnet");
                                                    try {
                                                        paidItem.save();
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    // normal doinbackground

                                                    // Gets the current list of
                                                    // orders
                                                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                                                            LoginSignupActivity
                                                                    .getParseUser()
                                                                    + "_Bestellung");
                                                    query.whereEqualTo("Tisch",
                                                            WaiterTableSelectActivity
                                                                    .getTNR());
                                                    query.whereEqualTo(
                                                            "Status", "fertig");
                                                    query.orderByDescending("Art");
                                                    query.orderByAscending("Name");

                                                    try {
                                                        orders = query.find();
                                                    } catch (ParseException e) {

                                                    }
                                                    // end normal

                                                    adapterList.clear();
                                                    adapterListBackground
                                                            .clear();
                                                    amount = 0;
                                                }
                                                return null;
                                            }
                                        }.execute();

                                        textViewAmount
                                                .setText("Betrag insgesamt: ");
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
        });

        buttonCashUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < orders.size(); i++) {
                    double preis = Double.parseDouble(orders.get(i)
                            .get("Preis").toString());
                    amount = amount + preis;
                }

                String text = "Alles auf der Rechnung? Betrag: "
                        + String.format("%.2f", amount) + " Euro!";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Tisch abrechnen?");

                // set dialog message
                alertDialogBuilder
                        .setMessage(text)
                        .setCancelable(false)
                        .setPositiveButton("Ja",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // Execute RemoteDataTask AsyncTask
                                        new RemoteDataTask() {
                                            protected Void doInBackground(
                                                    Void... params) {
                                                // Delete the remote object
                                                for (int i = 0; i < orders
                                                        .size(); i++) {

                                                    // set the todoObject to the
                                                    // item in
                                                    // list
                                                    final ParseObject paidItem = orders
                                                            .get(i);

                                                    paidItem.put("Status",
                                                            "abgerechnet");
                                                    try {
                                                        paidItem.save();
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                adapterList.clear();
                                                adapterListBackground.clear();
                                                amount = 0;

                                                // Gets the current list of
                                                // orders
                                                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                                                        LoginSignupActivity
                                                                .getParseUser()
                                                                + "_Bestellung");
                                                query.whereEqualTo("Tisch",
                                                        WaiterTableSelectActivity
                                                                .getTNR());
                                                query.whereEqualTo("Status",
                                                        "fertig");
                                                query.orderByDescending("Art");
                                                query.orderByAscending("Name");

                                                try {
                                                    orders = query.find();
                                                } catch (ParseException e) {

                                                }
                                                // end normal

                                                return null;
                                            }
                                        }.execute();
                                        textViewAmount
                                                .setText("Betrag insgesamt: ");
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
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        double preis = Double.parseDouble(orders.get(position).get("Preis")
                .toString());

        // unmarks an item if clicked twice
        // computes the amount to pay
        if (orders.get(position).get("Background").toString().equals("marked")) {
            amount = amount - preis;

            orders.get(position).put("Background", "unmarked");
            orders.get(position).saveInBackground();

            adapterListBackground.set(position,
                    orders.get(position).getString("Background"));
            adapterCashUp.notifyDataSetChanged();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == orders.get(position)) {
                    list.remove(i);
                }
            }
        } else {
            //marks an item if clicked
            //computes the amount to pay
            orders.get(position).put("Background", "marked");
            orders.get(position).saveInBackground();

            adapterListBackground.set(position,
                    orders.get(position).getString("Background"));
            adapterCashUp.notifyDataSetChanged();

            amount = amount + preis;
            list.add(orders.get(position));
        }

        textViewAmount.setText("Betrag insgesamt: "
                + String.format("%.2f", amount));

    }
}