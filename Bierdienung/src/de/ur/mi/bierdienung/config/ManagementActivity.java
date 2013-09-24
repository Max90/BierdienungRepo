package de.ur.mi.bierdienung.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import de.ur.bierdienung.R;

public class ManagementActivity extends Activity {

    private Button foodMenuButton;
    private Button drinkMenuButton;
    private Button tableManagementButton;
    private static String whichMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        setTitle(R.string.management_title_string);
        setupUI();
        setButtonsOnClick();


    }

    private void setupUI() {
        foodMenuButton = (Button) findViewById(R.id.button_food_menu);
        drinkMenuButton = (Button) findViewById(R.id.button_drink_menu);
        tableManagementButton = (Button) findViewById(R.id.button_table_management);
    }

    private void setButtonsOnClick() {
        foodMenuButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                whichMenu = "Essen";
                Intent i = new Intent(ManagementActivity.this,
                        PopulateMenuActivity.class);
                i.putExtra("name", "Essen");
                startActivity(i);
            }
        });

        drinkMenuButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                whichMenu = "Getraenke";
                Intent i = new Intent(ManagementActivity.this,
                        PopulateMenuActivity.class);
                i.putExtra("name", "Getraenke");
                startActivity(i);
            }
        });

        tableManagementButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tableManagementActivity = new Intent(
                        ManagementActivity.this, TableManagementActivity.class);
                startActivity(tableManagementActivity);
            }
        });

    }

    public static String getMenu() {
        return whichMenu;
    }


}