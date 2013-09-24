package de.ur.mi.bierdienung;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.ur.bierdienung.R;
import de.ur.mi.login.LoginSignupActivity;
import de.ur.mi.parse.ListViewAdapter;
import de.ur.mi.parse.ParselistdownloadClass;

public class MenuSwipeFragment extends Fragment {

    private String karte;
    private String category;

    View rootView;

    public MenuSwipeFragment(String category, String karte) {
		this.karte = karte;
		this.category = category;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.swipe_fragement, container, false);

        List<ParselistdownloadClass> parselistdownloadList = new ArrayList<ParselistdownloadClass>();
        try {
			// Locate the class table named "Country" in Parse.com
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					LoginSignupActivity.getParseUser() + "_" + karte);
			query.whereEqualTo("Kategorie", category);
			query.orderByAscending("Name");

            List<ParseObject> orders = query.find();
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

		// Pass the results into ListViewAdapter.java
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), parselistdownloadList);

        // Locate the listview in listview_main.xml
        ListView listview = (ListView) rootView.findViewById(R.id.list);
        // Binds the Adapter to the ListView
		listview.setAdapter(adapter);

        Button buttonWaiterCurrentOrder = (Button) rootView
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
}
