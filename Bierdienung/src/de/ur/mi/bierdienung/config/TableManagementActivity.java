package de.ur.mi.bierdienung.config;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;

// gets input of max table number in restaurant and saves that number
public class TableManagementActivity extends Activity {
    private Button tableNumberFinishButton;
    private EditText tableNumberTextEdit;
    private TextView currentTableNumTextView;
    private String tableNumber = "";
    private List<ParseObject> parseListTableNumber;
    private String maxTables = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_management);
        setTitle("Tische Verwalten");

        // downloads the items ordered for this table
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                LoginSignupActivity.getParseUser() + "_Table");
        try {
            parseListTableNumber = query.find();

        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        if (parseListTableNumber.size() > 0) {
            maxTables = parseListTableNumber.get(
                    parseListTableNumber.size() - 1).getString("TableNumber");
        }
        setupUI();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setHomeButtonEnabled(true);
        }
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

    private void setupUI() {
        tableNumberTextEdit = (EditText) findViewById(R.id.table_number_edit_text);
        currentTableNumTextView = (TextView) findViewById(R.id.text_view_table_num);
        currentTableNumTextView.setText("Aktuelle Tischanzahl: " + maxTables);
        tableNumberFinishButton = (Button) findViewById(R.id.set_table_number_button);
        tableNumberFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // saves number of tables
                // links back to PrefereancesActivity
                tableNumber = tableNumberTextEdit.getText().toString();
                ParseObject objectToSave = new ParseObject(LoginSignupActivity
                        .getParseUser() + "_Table");
                objectToSave.put("TableNumber", tableNumber);

                objectToSave.saveInBackground();

                Intent preferencesIntent = new Intent(
                        TableManagementActivity.this, ManagementActivity.class);
                startActivity(preferencesIntent);
                finish();
            }
        });
    }
}