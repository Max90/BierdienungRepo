package de.ur.mi.login;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.ur.bierdienung.R;
import de.ur.mi.ausschank_kueche.AusschankKuecheActivity;
import de.ur.mi.bierdienung.BedienungTischAuswahlActivity;
import de.ur.mi.bierdienung.config.EinstellungenActivity;
public class MainActivity extends Activity {
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
        setEinstellungenClick();
        setBedienungClick();
        setAusschankClick();
        setKuecheClick();

        einstellungenButton.setClickable(false);
        bedienungButton.setClickable(false);
        ausschankButton.setClickable(false);
        kuecheButton.setClickable(false);

        if (LoginSignupActivity.getUserStatus().equals("Kellner")) {
            bedienungButton.setClickable(true);
        } else if (LoginSignupActivity.getUserStatus().equals("Kueche")) {
            kuecheButton.setClickable(true);
        } else if (LoginSignupActivity.getUserStatus().equals("Verwalten")) {
            einstellungenButton.setClickable(true);
        } else if (LoginSignupActivity.getUserStatus().equals("Ausschank")) {
            ausschankButton.setClickable(true);
        }
    }

    private void setKuecheClick() {
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

    private void setAusschankClick() {
        ausschankButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        AusschankKuecheActivity.class);
                i.putExtra("name", "Getraenke");
                startActivity(i);
            }
        });
    }

    private void setBedienungClick() {
        bedienungButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        BedienungTischAuswahlActivity.class);
                startActivity(i);
            }
        });
    }

    private void setEinstellungenClick() {
        einstellungenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        EinstellungenActivity.class);
                startActivity(i);
            }
        });
    }
}