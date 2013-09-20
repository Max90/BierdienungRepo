package de.ur.mi.parse;

import java.util.ArrayList;
import de.ur.bierdienung.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WaiterCurrentOrderListViewAdapter extends ArrayAdapter<String> {

	private final ArrayList<String> adapterListBestellung;
	private final ArrayList<String> adapterListTisch;
	private final ArrayList<String> adapterListBackground;
	private final Context context;

	public WaiterCurrentOrderListViewAdapter(Context context,
			ArrayList<String> adapterListBestellung,
			ArrayList<String> adapterListTisch,
			ArrayList<String> adapterListBackground) {
		super(context, R.layout.listview_item, adapterListBestellung);
		this.context = context;
		this.adapterListBestellung = adapterListBestellung;
		this.adapterListTisch = adapterListTisch;
		this.adapterListBackground = adapterListBackground;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.listview_item, parent,
				false);
		TextView listviewName = (TextView) rowView
				.findViewById(R.id.listviewName);
		TextView listviewTisch = (TextView) rowView
				.findViewById(R.id.listviewPreis);

		listviewName.setText(adapterListBestellung.get(position));
		listviewTisch.setText(adapterListTisch.get(position));

		if (adapterListBackground.get(position).equals("unmarked")) {
			rowView.setBackgroundColor(Color.TRANSPARENT);
		} else {
			rowView.setBackgroundColor(Color.RED);
		}

		return rowView;
	}
}
