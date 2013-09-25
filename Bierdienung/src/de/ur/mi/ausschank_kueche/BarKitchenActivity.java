package de.ur.mi.ausschank_kueche;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.ListViewAdapterKitchenBar;
import de.ur.mi.parse.ParselistdownloadClass;

public class BarKitchenActivity extends ListActivity {


    private static final int DELETE_ID = Menu.FIRST + 1;
    //
    // Declare Variables
    private List<ParseObject> ordersList;
    private ProgressDialog mProgressDialog;
    private ListViewAdapterKitchenBar adapter;
    private List<ParselistdownloadClass> parselistdownloadList = null;
    private Button refreshButton;
    private String menuName;
    private ArrayList<ParseObject> deleteList = new ArrayList<ParseObject>();
    private ArrayList<String> adapterListOrder = new ArrayList<String>();
    private ArrayList<String> adapterListTable = new ArrayList<String>();
    private ArrayList<String> adapterListBackground = new ArrayList<String>();
    private ArrayList<String> listKind = new ArrayList<String>();

    private ArrayList<ParseObject> objectList = new ArrayList<ParseObject>();
    private ArrayList<ParseObject> deleteObjectList = new ArrayList<ParseObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_bar);
        Parse.initialize(this, "8H5vDxr2paOyJbbKm0pnAw1JuriXdI1kmb0EtBTu",
                "FTLtxlrn9TM2ZIl7KuTcg0FBVFkOjJipBu11o7tW");

        refreshButton = (Button) findViewById(R.id.refresh_and_notify_waiter_button);
        Bundle extras = getIntent().getExtras();
        menuName = extras.getString("name");
        if (menuName.equals("Getraenke")) {
            setTitle("Ausschank");
        } else {
            setTitle("Küche");
        }

        refreshButton();
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
        registerForContextMenu(getListView());
    }

    private void refreshButton() {
        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Execute RemoteDataTask AsyncTask
                new RemoteDataTask() {
                    protected Void doInBackground(Void... params) {
                        for (int i = 0; i < deleteObjectList.size(); i++) {

                            // set the parseObject to the item in list
                            final ParseObject paidItem = deleteObjectList
                                    .get(i);

                            paidItem.put("Status", "fertig");
                            try {
                                paidItem.save();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }


                        // PushNotification for Waiter who accepted order when
                        // meal is cooked
                        ArrayList<String> listTable = new ArrayList<String>();
                        ArrayList<String> listOrder = new ArrayList<String>();
                        ArrayList<String> listWaiter = new ArrayList<String>();
                        for (int i = 0; i < adapterListBackground.size(); i++) {
                            if (adapterListBackground.get(i).equals("marked")
                                    && listKind.get(i).equals("Essen")) {
                                boolean check = true;
                                if (listTable.size() > 0) {
                                    for (int p = 0; p < listTable.size(); p++) {
                                        if (listTable.get(p).equals(
                                                adapterListTable.get(i))) {
                                            String temp = listOrder.get(p)
                                                    + ", "
                                                    + adapterListOrder.get(i);
                                            listOrder.set(p, temp);
                                            listWaiter.set(p,
                                                    parselistdownloadList
                                                            .get(i)
                                                            .getKellner());
                                            check = false;
                                        }
                                    }
                                }
                                if (check) {
                                    listTable.add(adapterListTable.get(i));
                                    listOrder.add(adapterListOrder.get(i));
                                    listWaiter.add(parselistdownloadList.get(i)
                                            .getKellner());
                                }
                            }
                        }
                        if (listTable.size() > 0) {
                            for (int i = 0; i < listTable.size(); i++) {
                                String key = listOrder.get(i) + " fertig!"
                                        + " Tisch " + listTable.get(i);
                                String waiterName = listWaiter.get(i);
                                ParsePush push = new ParsePush();
                                push.setChannel(waiterName);
                                push.setMessage(key);
                                push.sendInBackground();
                            }
                        }
                        adapterListBackground.clear();
                        adapterListOrder.clear();
                        adapterListTable.clear();
                        listKind.clear();

                        // create new list

                        parselistdownloadList = new ArrayList<ParselistdownloadClass>();

                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                                LoginSignupActivity.getParseUser()
                                        + "_Bestellung");
                        query.whereEqualTo("Status", "aufgegeben");
                        query.whereEqualTo("Art", menuName);
                        query.orderByAscending("Name");
                        try {
                            ordersList = query.find();

                        } catch (ParseException e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }

                        return null;
                    }
                }.execute();
            }
        });
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(BarKitchenActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Lade Liste");
            // Set progressdialog message
            mProgressDialog.setMessage("Laden...");
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
            query.whereEqualTo("Art", menuName);
            query.orderByAscending("Name");
            try {
                ordersList = query.find();

            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            int i = 0;
            objectList = (ArrayList<ParseObject>) ordersList;
            for (ParseObject order : ordersList) {
                ParselistdownloadClass map = new ParselistdownloadClass();
                map.setBackground((String) order.get("Background"));
                map.setName((String) order.get("Name"));
                map.setTisch((String) order.get("Tisch"));
                map.setArt((String) order.get("Art"));
                map.setKellner((String) order.get("Kellner"));

                parselistdownloadList.add(map);

                ordersList.get(i).put("Background", "unmarked");
                ordersList.get(i).saveInBackground();

                adapterListBackground.add((String) order.get("Background"));
                adapterListOrder.add((String) order.get("Name"));
                adapterListTable.add((String) order.get("Tisch"));
                listKind.add((String) order.get("Art"));

                i++;
            }

            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapterKitchenBar(BarKitchenActivity.this,
                    adapterListBackground, adapterListOrder, adapterListTable);

            setListAdapter(adapter);
            adapter.notifyDataSetChanged();

            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (ordersList.get(position).get("Background").toString()
                .equals("marked")) {

            ordersList.get(position).put("Background", "unmarked");
            ordersList.get(position).saveInBackground();

            adapterListBackground.set(position, ordersList.get(position)
                    .getString("Background"));
            adapter.notifyDataSetChanged();

            for (int i = 0; i < deleteList.size(); i++) {
                if (deleteList.get(i) == objectList.get(position)) {
                    deleteList.remove(i);
                }
                deleteObjectList = deleteList;
            }
        } else {
            ordersList.get(position).put("Background", "marked");
            ordersList.get(position).saveInBackground();

            adapterListBackground.set(position, ordersList.get(position)
                    .getString("Background"));
            adapter.notifyDataSetChanged();

            deleteList.add(objectList.get(position));
            deleteObjectList = deleteList;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_kitchen_menu, menu);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refreh:
                adapterListBackground.clear();
                adapterListOrder.clear();
                adapterListTable.clear();
                listKind.clear();
                // Execute RemoteDataTask AsyncTask
                new RemoteDataTask().execute();
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_cancel);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();

                // Delete the remote object
                final ParseObject order = ordersList.get(info.position);


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
                adapterListBackground.clear();
                adapterListOrder.clear();
                adapterListTable.clear();
                listKind.clear();

                return true;


        }

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
        return super.onContextItemSelected(item);
    }
}