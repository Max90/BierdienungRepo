package de.ur.mi.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;
import de.ur.bierdienung.R;
import de.ur.mi.ausschank_kueche.AusschankKuecheActivity;
import de.ur.mi.bierdienung.WaiterTableSelectActivity;
import de.ur.mi.bierdienung.config.ManagementActivity;

public class LoginSignupActivity extends Activity {
	// Declare Variables
	private Button loginbutton;
	private Button signup;
	static String usernametxt;
	static String passwordtxt;
	static String kellnername;
	private EditText waiter;
	private EditText password;
	private EditText username;
	private ProgressDialog mProgressDialog;
	private static Context ctx;
	private RadioButton radioWaiter;
	private RadioButton radioKueche;
	private RadioButton radioAusschank;
	private RadioButton radioVerwalten;
	private RadioGroup radioCheck;
	public Context context = this;

	/**
	 * Called when the activity is first created.
	 */

	public static Context getContext() {
		return ctx;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from main.xml
		setContentView(R.layout.activity_login);
		// connection check
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null) {
			boolean isConnected = activeNetwork.isConnectedOrConnecting();
			if (!isConnected) {
				setDialogNoInternet();
			}
		} else
			setDialogNoInternet();

		// Parse -------------
		setParse();
		ctx = this.getApplicationContext();

		// TextViews
		setUI();
		// Login Button Click Listener
		setClickLoginButton();
		// Sign up Button Click Listener
		setClickSignUpButton();
		setRadioCheckListener();
	}

	private void setUI() {
		// Locate EditText
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		waiter = (EditText) findViewById(R.id.kellnernamen);
		// Locate Buttons in main.xml
		loginbutton = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);
		// Locate RadioButton
		radioCheck = (RadioGroup) findViewById(R.id.radioCheck);
		radioWaiter = (RadioButton) findViewById(R.id.radioKellner);
		radioKueche = (RadioButton) findViewById(R.id.radioKueche);
		radioAusschank = (RadioButton) findViewById(R.id.radioAusschank);
		radioVerwalten = (RadioButton) findViewById(R.id.radioVerwalten);
	}

	private void setRadioCheckListener() {
		radioCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				radioVerwalten.isChecked();
				if (radioWaiter.isChecked()) {
					waiter.setEnabled(true);
				} else {
					waiter.setEnabled(false);
				}
			}
		});
	}

	private void setClickSignUpButton() {
		signup.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				new RemoteDataTask() {
					protected Void doInBackground(Void... params) {
						// Retrieve the text entered from the EditText
						usernametxt = username.getText().toString();
						passwordtxt = password.getText().toString();
						// Force user to fill up the form
						if (usernametxt.equals("") && passwordtxt.equals("")) {
							Toast.makeText(getApplicationContext(),
									"Please complete the sign up form",
									Toast.LENGTH_LONG).show();
						} else {
							// Save new user data into Parse.com Data Storage
							ParseUser user = new ParseUser();
							user.setUsername(usernametxt);
							user.setPassword(passwordtxt);
							user.signUpInBackground(new SignUpCallback() {
								public void done(ParseException e) {
									if (e == null) {
										// Show a simple Toast message upon
										// successful
										// registration
										Toast.makeText(
												getApplicationContext(),
												"Successfully Signed up, please log in.",
												Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(getApplicationContext(),
												"Sign up Error",
												Toast.LENGTH_LONG).show();
									}
								}
							});
						}
						super.doInBackground();
						return null;
					}
				}.execute();
			}
		});
	}

	private void setClickLoginButton() {
		loginbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				kellnername = waiter.getText().toString();
				if (radioWaiter.isChecked()) {
					if (kellnername.equals("")) {
						Toast.makeText(getApplicationContext(),
								"Please type in Waiter", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				}
				// Push Push Push
				PushService.subscribe(ctx, kellnername,
						WaiterTableSelectActivity.class);
				new RemoteDataTask() {
					protected Void doInBackground(Void... params) {
						// Retrieve the text entered from the EditText
						usernametxt = username.getText().toString();
						passwordtxt = password.getText().toString();
						// Send data to Parse.com for verification
						ParseUser.logInInBackground(usernametxt, passwordtxt,
								new LogInCallback() {
									public void done(ParseUser user,
											ParseException e) {
										if (user != null) {
											// If user exist and authenticated,
											if (radioAusschank.isChecked()) {
												Intent i = new Intent(
														LoginSignupActivity.this,
														AusschankKuecheActivity.class);
												i.putExtra("name", "Getraenke");
												i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(i);
											} else if (radioWaiter.isChecked()) {
												Intent i = new Intent(
														LoginSignupActivity.this,
														WaiterTableSelectActivity.class);
												i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(i);
											} else if (radioKueche.isChecked()) {
												Intent i = new Intent(
														LoginSignupActivity.this,
														AusschankKuecheActivity.class);
												i.putExtra("name", "Essen");
												i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(i);
											} else {
												Intent i = new Intent(
														LoginSignupActivity.this,
														ManagementActivity.class);
												i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(i);
											}
											Toast.makeText(
													getApplicationContext(),
													R.string.successfully_logged_in_toast_string,
													Toast.LENGTH_LONG).show();
											finish();
										} else {
											Toast.makeText(
													getApplicationContext(),
													R.string.no_such_user_exist_toast_string,
													Toast.LENGTH_LONG).show();
										}
									}
								});
						super.doInBackground();
						return null;
					}
				}.execute();
			}
		});
	}

	private void setParse() {
		Parse.initialize(this, "8H5vDxr2paOyJbbKm0pnAw1JuriXdI1kmb0EtBTu",
				"FTLtxlrn9TM2ZIl7KuTcg0FBVFkOjJipBu11o7tW");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		// Push Push Push
		PushService.setDefaultPushCallback(this,
				WaiterTableSelectActivity.class);
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(LoginSignupActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Ueberpruefe Logindaten");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
		}
	}

	public static String getParseUser() {
		return usernametxt;
	}

	public static String getKellner() {
		return kellnername;
	}

	// sets a dialog with a link to internetSettings if no internet available
	public void setDialogNoInternet() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog message
		alertDialogBuilder
				.setMessage(R.string.no_connection_string)
				.setCancelable(false)
				.setPositiveButton(R.string.connect,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Link to InternetSettings
								Intent intent = new Intent(
										Settings.ACTION_SETTINGS);
								startActivity(intent);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}