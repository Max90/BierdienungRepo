/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ur.mi.bierdienung;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.ListViewAdapter;
import de.ur.mi.parse.ParselistdownloadClass;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class DrinksMenuActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	ViewPager mViewPager;
	// Declare Variables

	public static final int INSERT_ID = Menu.FIRST;
	public static final int TISCH_WECHSELN_ID = Menu.FIRST + 1;

	private static String karte;

	private static Button buttonWaiterCurrentOrder;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drinks_menu);

		// Create the adapter that will return a fragment
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		setTitle("Tisch " + WaiterTableSelectActivity.getTNR());

		Bundle extras = getIntent().getExtras();
		karte = extras.getString("name");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setHomeButtonEnabled(true);
		}

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:

				return new DrinksMenuFragmentAlkfrei();

			case 1:

				return new DrinksMenuFragmentBier();
			case 2:

				return new DrinksMenuFragmentWein();
			case 3:

				return new DrinksMenuFragmentSchnaps();
			case 4:

				return new DrinksMenuFragmentSonstiges();

			}
			return null;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Alkfrei";
			case 1:
				return "Bier";
			case 2:
				return "Wein";
			case 3:
				return "Schnaps";
			case 4:
				return "Sonstiges";
			}
			return null;
		}
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class DrinksMenuFragmentAlkfrei extends Fragment {
		View rootView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_section_launchpad,
					container, false);

			// Execute RemoteDataTask AsyncTask
			new RemoteDataTask().execute();

			buttonWaiterCurrentOrder = (Button) rootView
					.findViewById(R.id.send_or_change_order_button);

			buttonWaiterCurrentOrder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(),
							WaiterCurrentOrderActivity.class);
					startActivity(i);
				}
			});

			return rootView;
		}

		// RemoteDataTask AsyncTask
		private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

			private ListView listview;
			private List<ParseObject> orders;
			private ListViewAdapter adapter;
			private List<ParselistdownloadClass> parselistdownloadList = null;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected Void doInBackground(Void... params) {
				// Create the array
				parselistdownloadList = new ArrayList<ParselistdownloadClass>();
				try {
					// Locate the class table named "Country" in Parse.com
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							LoginSignupActivity.getParseUser() + "_" + karte);
					query.whereEqualTo("Kategorie", "Alkoholfrei");
					query.orderByAscending("Name");
					
					orders = query.find();
					for (ParseObject order : orders) {
						ParselistdownloadClass map = new ParselistdownloadClass();
						map.setName((String) order.get("Name"));
						map.setPreis((String) order.get("Preis"));
						map.setArt((String) order.get("Art"));
						map.setKategorie((String) order.get("Kategorie"));

						parselistdownloadList.add(map);
					}
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// Locate the listview in listview_main.xml
				listview = (ListView) rootView.findViewById(R.id.list);
				// Pass the results into ListViewAdapter.java
				adapter = new ListViewAdapter(getActivity(),
						parselistdownloadList);
				// Binds the Adapter to the ListView
				listview.setAdapter(adapter);
			}
		}
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class DrinksMenuFragmentBier extends Fragment {

		View rootView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_section_launchpad,
					container, false);

			// Execute RemoteDataTask AsyncTask
			new RemoteDataTask().execute();

			buttonWaiterCurrentOrder = (Button) rootView
					.findViewById(R.id.send_or_change_order_button);

			buttonWaiterCurrentOrder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(),
							WaiterCurrentOrderActivity.class);
					startActivity(i);
				}
			});

			return rootView;
		}

		// RemoteDataTask AsyncTask
		private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

			private ListView listview;
			private List<ParseObject> orders;
			private ListViewAdapter adapter;
			private List<ParselistdownloadClass> parselistdownloadList = null;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected Void doInBackground(Void... params) {
				// Create the array
				parselistdownloadList = new ArrayList<ParselistdownloadClass>();
				try {
					// Locate the class table named "Country" in Parse.com
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							LoginSignupActivity.getParseUser() + "_" + karte);
					query.whereEqualTo("Kategorie", "Bier");
					query.orderByAscending("Name");
				
					orders = query.find();
					for (ParseObject order : orders) {
						ParselistdownloadClass map = new ParselistdownloadClass();
						map.setName((String) order.get("Name"));
						map.setPreis((String) order.get("Preis"));
						map.setArt((String) order.get("Art"));
						map.setKategorie((String) order.get("Kategorie"));

						parselistdownloadList.add(map);
					}
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// Locate the listview in listview_main.xml
				listview = (ListView) rootView.findViewById(R.id.list);
				// Pass the results into ListViewAdapter.java
				adapter = new ListViewAdapter(getActivity(),
						parselistdownloadList);
				// Binds the Adapter to the ListView
				listview.setAdapter(adapter);

			}
		}
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class DrinksMenuFragmentWein extends Fragment {

		View rootView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_section_launchpad,
					container, false);

			// Execute RemoteDataTask AsyncTask
			new RemoteDataTask().execute();

			buttonWaiterCurrentOrder = (Button) rootView
					.findViewById(R.id.send_or_change_order_button);

			buttonWaiterCurrentOrder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(),
							WaiterCurrentOrderActivity.class);
					startActivity(i);
				}
			});

			return rootView;
		}

		// RemoteDataTask AsyncTask
		private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

			private ListView listview;
			private List<ParseObject> orders;
			private ListViewAdapter adapter;
			private List<ParselistdownloadClass> parselistdownloadList = null;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
			}

			@Override
			protected Void doInBackground(Void... params) {
				// Create the array
				parselistdownloadList = new ArrayList<ParselistdownloadClass>();
				try {
					// Locate the class table named "Country" in Parse.com
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							LoginSignupActivity.getParseUser() + "_" + karte);
					query.whereEqualTo("Kategorie", "Wein");
					query.orderByAscending("Name");
					// if (karte.length() > 6) {
					// query.orderByAscending("Kategorie");
					// } else {
					// query.orderByDescending("Kategorie");
					// }
					orders = query.find();
					for (ParseObject order : orders) {
						ParselistdownloadClass map = new ParselistdownloadClass();
						map.setName((String) order.get("Name"));
						map.setPreis((String) order.get("Preis"));
						map.setArt((String) order.get("Art"));
						map.setKategorie((String) order.get("Kategorie"));

						parselistdownloadList.add(map);
					}
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// Locate the listview in listview_main.xml
				listview = (ListView) rootView.findViewById(R.id.list);
				// Pass the results into ListViewAdapter.java
				adapter = new ListViewAdapter(getActivity(),
						parselistdownloadList);
				// Binds the Adapter to the ListView
				listview.setAdapter(adapter);
				
			}
		}

	}

	public static class DrinksMenuFragmentSchnaps extends Fragment {

		View rootView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_section_launchpad,
					container, false);

			// Execute RemoteDataTask AsyncTask
			new RemoteDataTask().execute();

			buttonWaiterCurrentOrder = (Button) rootView
					.findViewById(R.id.send_or_change_order_button);

			buttonWaiterCurrentOrder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(),
							WaiterCurrentOrderActivity.class);
					startActivity(i);
				}
			});

			return rootView;
		}

		// RemoteDataTask AsyncTask
		private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

			private ListView listview;
			private List<ParseObject> orders;
			private ListViewAdapter adapter;
			private List<ParselistdownloadClass> parselistdownloadList = null;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			
			}

			@Override
			protected Void doInBackground(Void... params) {
				// Create the array
				parselistdownloadList = new ArrayList<ParselistdownloadClass>();
				try {
					// Locate the class table named "Country" in Parse.com
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							LoginSignupActivity.getParseUser() + "_" + karte);
					query.whereEqualTo("Kategorie", "Schnaps");
					query.orderByAscending("Name");

					orders = query.find();
					for (ParseObject order : orders) {
						ParselistdownloadClass map = new ParselistdownloadClass();
						map.setName((String) order.get("Name"));
						map.setPreis((String) order.get("Preis"));
						map.setArt((String) order.get("Art"));
						map.setKategorie((String) order.get("Kategorie"));

						parselistdownloadList.add(map);
					}
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// Locate the listview in listview_main.xml
				listview = (ListView) rootView.findViewById(R.id.list);
				// Pass the results into ListViewAdapter.java
				adapter = new ListViewAdapter(getActivity(),
						parselistdownloadList);
				// Binds the Adapter to the ListView
				listview.setAdapter(adapter);

			}
		}
	}

	public static class DrinksMenuFragmentSonstiges extends Fragment {

		View rootView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_section_launchpad,
					container, false);

			// Execute RemoteDataTask AsyncTask
			new RemoteDataTask().execute();

			buttonWaiterCurrentOrder = (Button) rootView
					.findViewById(R.id.send_or_change_order_button);

			buttonWaiterCurrentOrder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(),
							WaiterCurrentOrderActivity.class);
					startActivity(i);
				}
			});

			return rootView;
		}

		// RemoteDataTask AsyncTask
		private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

			private ListView listview;
			private List<ParseObject> orders;
			private ListViewAdapter adapter;
			private List<ParselistdownloadClass> parselistdownloadList = null;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected Void doInBackground(Void... params) {
				// Create the array
				parselistdownloadList = new ArrayList<ParselistdownloadClass>();
				try {
					// Locate the class table named "Country" in Parse.com
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							LoginSignupActivity.getParseUser() + "_" + karte);
					query.whereEqualTo("Kategorie", "Sonstiges");
					query.orderByAscending("Name");

					orders = query.find();
					for (ParseObject order : orders) {
						ParselistdownloadClass map = new ParselistdownloadClass();
						map.setName((String) order.get("Name"));
						map.setPreis((String) order.get("Preis"));
						map.setArt((String) order.get("Art"));
						map.setKategorie((String) order.get("Kategorie"));

						parselistdownloadList.add(map);
					}
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// Locate the listview in listview_main.xml
				listview = (ListView) rootView.findViewById(R.id.list);
				// Pass the results into ListViewAdapter.java
				adapter = new ListViewAdapter(getActivity(),
						parselistdownloadList);
				// Binds the Adapter to the ListView
				listview.setAdapter(adapter);

			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.tisch:
			Intent iTisch = new Intent(DrinksMenuActivity.this,
					WaiterTableOverviewActivity.class);
			startActivity(iTisch);
			finish();
			return true;

		case R.id.speisekarte:
			Intent iEssen = new Intent(DrinksMenuActivity.this,
					FoodMenuActivity.class);
			iEssen.putExtra("name", "Essen");
			startActivity(iEssen);
			finish();
			return true;

		case R.id.getraenkekarte:
			Intent iGetraenke = new Intent(DrinksMenuActivity.this,
					DrinksMenuActivity.class);
			iGetraenke.putExtra("name", "Getraenke");
			startActivity(iGetraenke);
			finish();
			return true;

		case R.id.change_table:
			finish();
			return true;

		case R.id.compute_table:
			Intent computeTableIntent = new Intent(DrinksMenuActivity.this,
					WaiterCashUpActivity.class);
			startActivity(computeTableIntent);
			return true;

		case android.R.id.home:
			finish();
			return true;
		default:
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return result;
	}

}
