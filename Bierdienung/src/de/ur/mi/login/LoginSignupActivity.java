package de.ur.mi.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import de.ur.bierdienung.R;

public class LoginSignupActivity extends Activity {
	// Declare Variables
	Button loginbutton;
	Button signup;
	static String usernametxt;
	static String passwordtxt;
	static String kellnername;
	EditText kellner;
	EditText password;
	EditText username;
	ProgressDialog mProgressDialog;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from main.xml
		setContentView(R.layout.activity_signup);
		// Locate EditTexts in main.xml

		// Parse -------------
		Parse.initialize(this, "8H5vDxr2paOyJbbKm0pnAw1JuriXdI1kmb0EtBTu",
				"FTLtxlrn9TM2ZIl7KuTcg0FBVFkOjJipBu11o7tW");
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		// ----------------------------------------

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		kellner = (EditText) findViewById(R.id.kellnernamen);

		// Locate Buttons in main.xml
		loginbutton = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);

		// Login Button Click Listener
		loginbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				kellnername = kellner.getText().toString();

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
											// send
											// user to Welcome.class
											Intent intent = new Intent(
													LoginSignupActivity.this,
													MainActivity.class);
											startActivity(intent);
											Toast.makeText(
													getApplicationContext(),
													"Successfully Logged in",
													Toast.LENGTH_LONG).show();
											finish();
										} else {
											Toast.makeText(
													getApplicationContext(),
													"No such user exist, please signup",
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
		// Sign up Button Click Listener
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

}