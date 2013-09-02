package de.ur.mi.parse;

import java.util.ArrayList;
import java.util.List;
import com.parse.ParsePush;

import de.ur.bierdienung.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter_Kueche_Ausschank extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	private ArrayList<ParselistdownloadClass> arraylist;

	public ListViewAdapter_Kueche_Ausschank(Context context,
			List<ParselistdownloadClass> parselistdownloadList) {
		mContext = context;
		this.parselistdownloadList = parselistdownloadList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<ParselistdownloadClass>();
		this.arraylist.addAll(parselistdownloadList);
	}

	public class ViewHolder {
		TextView listviewName;
		TextView listviewPreis;
	}

	@Override
	public int getCount() {
		return parselistdownloadList.size();
	}

	@Override
	public ParselistdownloadClass getItem(int position) {
		return parselistdownloadList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextViews in listview_item.xml
			holder.listviewName = (TextView) view
					.findViewById(R.id.listviewName);
			holder.listviewPreis = (TextView) view
					.findViewById(R.id.listviewPreis);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.listviewName.setText(parselistdownloadList.get(position)
				.getName());
		holder.listviewPreis.setText(String.valueOf(parselistdownloadList.get(position)
				.getPreis()));
		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				v.setBackgroundColor(Color.RED);
				String key = parselistdownloadList.get(position).getName()
						+ " Bestellung fertig";
				Toast.makeText(v.getContext(), key, Toast.LENGTH_SHORT).show();

				// PUSH PUSH PUSH

				// so soll notification zu richtigem Kellner kommen... gehört
				// dann anStelle von "sepp" hin
				// String kellnerPush = parselistdownloadList.get(position)
				// .getUser();
				ParsePush push = new ParsePush();
				push.setChannel("sepp");
				push.setMessage(key);
				push.sendInBackground();
			}
		});

		return view;
	}
}