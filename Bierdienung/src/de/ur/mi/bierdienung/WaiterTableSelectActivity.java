package de.ur.mi.bierdienung;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

public class WaiterTableSelectActivity extends Activity {

    private EditText editTextTableNum;
    private Button buttonEnterBedienung;
    private Button buttonCashUp;
    private static String tNr;
    private List<ParseObject> parseListTableNumber;
    private String maxTables = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_tableselection);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                LoginSignupActivity.getParseUser() + "_Table");
        try {
            parseListTableNumber = query.find();
            if (parseListTableNumber.size() > 0) {
                maxTables = parseListTableNumber.get(
                        parseListTableNumber.size() - 1).getString(
                        "TableNumber");
            }

        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        buttonEnterBedienung = (Button) findViewById(R.id.accept_order_button);
        buttonCashUp = (Button) findViewById(R.id.billing_button);
        editTextTableNum = (EditText) findViewById(R.id.edit_text_table_number);

        buttonEnterBedienung.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(WaiterTableSelectActivity.this,
                        WaiterTableOverviewActivity.class);
                tNr = editTextTableNum.getText().toString();
                if (checkForInvalidInput(v))
                    return;
                startActivity(i);
            }
        });

        buttonCashUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(WaiterTableSelectActivity.this,
                        WaiterCashUpActivity.class);
                tNr = editTextTableNum.getText().toString();
                if (checkForInvalidInput(v))
                    return;
                startActivity(i);
            }
        });
    }

    private boolean checkForInvalidInput(View v) {
        if (tNr.equals("")) {
            Toast.makeText(v.getContext(), "Bitte Tisch eingeben",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (maxTables.equals("")) {
            Toast.makeText(
                    v.getContext(),
                    R.string.no_tables_toast_string,
                    Toast.LENGTH_LONG).show();
            return true;
        } else if (Integer.parseInt(editTextTableNum.getText().toString()) > Integer
                .parseInt(maxTables)) {
            Toast.makeText(
                    v.getContext(),
                    "Tischnummer zu hoch. Wir haben nur " + maxTables
                            + " Tische", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public static String getTNR() {
        return tNr;
        // dummyComment
    }

    @Override
    protected void onResume() {
        editTextTableNum.setText("");
        editTextTableNum.requestFocus();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_logout:
                setUserLogout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUserLogout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Kellner ausloggen?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Sind Sie sich sicher?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ParseUser.logOut();
                        PushService.unsubscribe(
                                LoginSignupActivity.getContext(),
                                LoginSignupActivity.getKellner());

                        Intent loginIntent = new Intent(
                                WaiterTableSelectActivity.this,
                                LoginSignupActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                })
                .setNegativeButton("Nein",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.waiterlogoutmenu, menu);

        return result;
    }

}
