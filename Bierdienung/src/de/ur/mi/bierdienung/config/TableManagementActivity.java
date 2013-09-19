package de.ur.mi.bierdienung.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.ur.bierdienung.R;
import de.ur.mi.parse.AppSingleton;

// gets input of max table number in restaurant and saves that number in appSingleton
public class TableManagementActivity extends Activity {
    Button tableNumberFinishButton;
    EditText tableNumberEditText;
    AppSingleton appSingleton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appSingleton = AppSingleton.getInstance();
        setupUI();
    }

    private void setupUI() {
        tableNumberEditText = (EditText) findViewById(R.id.table_number_edit_text);
        tableNumberFinishButton = (Button) findViewById(R.id.set_table_number_button);
        tableNumberFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saves number of tables
                //links back to PrefereancesActivity
                appSingleton.tableNumber = tableNumberEditText.getText().toString();

                Intent preferencesIntent = new Intent(TableManagementActivity.this, EinstellungenActivity.class);
                startActivity(preferencesIntent);
            }
        });
    }
}