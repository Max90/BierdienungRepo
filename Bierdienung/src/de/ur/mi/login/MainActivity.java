package de.ur.mi.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import de.ur.bierdienung.R;
import de.ur.mi.ausschank_kueche.AusschankKuecheActivity;
import de.ur.mi.bierdienung.BedienungTischAuswahlActivity;
import de.ur.mi.bierdienung.config.EinstellungenActivity;

public class MainActivity extends Activity {

    // context of this activity
    private Context context = this;
    private Button einstellungenButton;
    private Button bedienungButton;
    private Button ausschankButton;
    private Button kuecheButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }


    private void setupUI() {
        einstellungenButton = (Button) findViewById(R.id.einstellungen);
        bedienungButton = (Button) findViewById(R.id.bedienung);
        ausschankButton = (Button) findViewById(R.id.ausschank);
        kuecheButton = (Button) findViewById(R.id.kueche);

        einstellungenButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EinstellungenActivity.class);
                startActivity(i);
            }
        });

        bedienungButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        BedienungTischAuswahlActivity.class);
                startActivity(i);
            }
        });

        ausschankButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        AusschankKuecheActivity.class);
                i.putExtra("name", "Getraenke");
                startActivity(i);
            }
        });

        kuecheButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        AusschankKuecheActivity.class);
                i.putExtra("name", "Essen");
                startActivity(i);
            }
        });
    }
}

