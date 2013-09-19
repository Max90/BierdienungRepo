package de.ur.mi.bierdienung;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

public class BedienungTischAuswahlActivity extends Activity {

    private EditText tischNummer;
    private Button bEnterBedienung;
    private Button bAbrechnung;
    private static String tNr;
    private List<ParseObject> parseListTableNumber;
    private String maxTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_tableselection);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                LoginSignupActivity.getParseUser() + "_Table");
        try {
            parseListTableNumber = query.find();
            maxTables = parseListTableNumber.get(parseListTableNumber.size() - 1).getString("TableNumber");
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        bEnterBedienung = (Button) findViewById(R.id.bEnterBedienung);
        bAbrechnung = (Button) findViewById(R.id.bAbrechnung);
        tischNummer = (EditText) findViewById(R.id.nr);

        bEnterBedienung.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(BedienungTischAuswahlActivity.this,
                        WaiterTableOverviewActivity.class);
                tNr = tischNummer.getText().toString();
                if (checkForInvalidInput(v))
                    return;
                startActivity(i);
            }
        });

        bAbrechnung.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(BedienungTischAuswahlActivity.this, AbrechnungsActivity.class);
                tNr = tischNummer.getText().toString();
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
            Toast.makeText(v.getContext(), " Es sind leider noch keine Tische angelegt. Bitte wenden Sie sich an Ihren Restaurantleiter", Toast.LENGTH_LONG).show();
            return true;
        } else if (Integer.parseInt(tischNummer.getText().toString()) > Integer.parseInt(maxTables)) {
            Toast.makeText(v.getContext(), "Tischnummer zu hoch. Wir haben nur " + maxTables + " Tische", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public static String getTNR() {
        return tNr;
        //dummyComment
    }

    @Override
    protected void onResume() {
        tischNummer.setText("");
        tischNummer.requestFocus();
        super.onResume();
    }

}
