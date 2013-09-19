package de.ur.mi.parse;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.ur.bierdienung.R;

public class AdapterAbrechnung extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;
	private final ArrayList<String> adapterListBackground;

	public AdapterAbrechnung(Context context, ArrayList<String> values,
			ArrayList<String> adapterListBackground) {
		super(context, R.layout.listview_item, values);
		this.context = context;
		this.values = values;
		this.adapterListBackground = adapterListBackground;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.listview_item, parent,
				false);
		TextView textView = (TextView) rowView.findViewById(R.id.listviewName);

		textView.setText(values.get(position));

		if (adapterListBackground.get(position).equals("unmarked")) {
			rowView.setBackgroundColor(Color.TRANSPARENT);
		} else {
			rowView.setBackgroundColor(Color.RED);
		}

		return rowView;
	}
}