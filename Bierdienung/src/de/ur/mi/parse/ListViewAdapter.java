package de.ur.mi.parse;

import java.util.ArrayList;
import java.util.List;

import de.ur.bierdienung.R;
import de.ur.mi.bierdienung.Bedienung;
import de.ur.mi.bierdienung.LoginSignupActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<ParselistdownloadClass> parselistdownloadList = null;
	private ArrayList<ParselistdownloadClass> arraylist;

	public ListViewAdapter(Context context,
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
		holder.listviewPreis.setText(parselistdownloadList.get(position)
				.getPreis());

		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String key = parselistdownloadList.get(position).getName()
						+ " bestellt";
						
				 ParseObject objectToSave = new
				 ParseObject(LoginSignupActivity.getParseUser()+"_Bestellung");
				 objectToSave.put("Name",
				 parselistdownloadList.get(position).getName());
				 objectToSave.put("Preis",
				 parselistdownloadList.get(position).getPreis());
				 objectToSave.put("Tisch", Bedienung.getTNR());
				 objectToSave.put("Art", parselistdownloadList.get(position).getArt());
				 objectToSave.saveInBackground();
				
				Toast.makeText(v.getContext(), key, Toast.LENGTH_SHORT).show();

			}
		});

		return view;
	}
}