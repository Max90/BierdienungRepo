package de.ur.mi.bierdienung;

import de.ur.bierdienung.R;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FoodMenuActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	ViewPager mViewPager;
	// Declare Variables

	public static final int INSERT_ID = Menu.FIRST;
	public static final int TISCH_WECHSELN_ID = Menu.FIRST + 1;

	private static String karte;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drinks_menu);

		// Create the adapter that will return a fragment
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		
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
				return new MenuSwipeFragment("Vorspeise", karte);

			case 1:

				return new MenuSwipeFragment("Hauptspeise", karte);
			case 2:

				return new MenuSwipeFragment("Nachspeise", karte);

			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Vorspeise";
			case 1:
				return "Hauptspeise";
			case 2:
				return "Nachspeise";

			}
			return null;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		switch (item.getItemId()) {

		case R.id.tisch:
			
			Intent iTisch = new Intent(FoodMenuActivity.this,
					WaiterTableOverviewActivity.class);
			startActivity(iTisch);
			finish();
			return true;

		case R.id.speisekarte:
			Intent iEssen = new Intent(FoodMenuActivity.this,
					FoodMenuActivity.class);
			iEssen.putExtra("name", "Essen");
			startActivity(iEssen);
			finish();
			return true;

		case R.id.getraenkekarte:
			Intent iGetraenke = new Intent(FoodMenuActivity.this,
					DrinksMenuActivity.class);
			iGetraenke.putExtra("name", "Getraenke");
			startActivity(iGetraenke);
			finish();
			return true;

		case R.id.change_table:
			finish();
			return true;

		case R.id.compute_table:
			Intent computeTableIntent = new Intent(FoodMenuActivity.this,
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
		
		menu.findItem(R.id.tisch).setTitle("Tisch " + WaiterTableSelectActivity.getTNR());
		return result;
	}

}
