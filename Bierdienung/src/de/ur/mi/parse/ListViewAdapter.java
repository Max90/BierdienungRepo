package de.ur.mi.parse;

import java.util.ArrayList;
import java.util.List;

import de.ur.bierdienung.R;
import de.ur.mi.bierdienung.BedienungTischAuswahlActivity;
import de.ur.mi.login.LoginSignupActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.NumberPicker;
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
			public void onClick(final View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						v.getContext());

				// set title
				alertDialogBuilder.setTitle("Bestellung aufnehmen");
				String text = parselistdownloadList.get(position).getName()
						+ " bestellen";

				final NumberPicker np = new NumberPicker(v.getContext());
				String[] nums = new String[100];
				for (int i = 0; i < nums.length; i++)
					nums[i] = Integer.toString(i);

				np.setMinValue(1);
				np.setMaxValue(nums.length - 1);
				np.setWrapSelectorWheel(false);
				np.setDisplayedValues(nums);
				np.setValue(1);
				alertDialogBuilder.setView(np);

				// set dialog message
				alertDialogBuilder
						.setMessage(text)
						.setCancelable(false)
						.setPositiveButton("Ja",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										for (int i = 1; i < np.getValue(); i++) {

											String key = String.valueOf(np
													.getValue()-1)
													+ "x "
													+ parselistdownloadList
															.get(position)
															.getName()
													+ " bestellt";

											ParseObject objectToSave = new ParseObject(
													LoginSignupActivity
															.getParseUser()
															+ "_Bestellung");
											objectToSave
													.put("Name",
															parselistdownloadList
																	.get(position)
																	.getName());
											objectToSave.put(
													"Preis",
													parselistdownloadList.get(
															position)
															.getPreis());
											objectToSave.put("Tisch",
													BedienungTischAuswahlActivity
															.getTNR());
											objectToSave.put(
													"Art",
													parselistdownloadList.get(
															position).getArt());
											objectToSave.put(
													"Kategorie",
													parselistdownloadList.get(
															position)
															.getKategorie());
											objectToSave.put("Kellner",
													LoginSignupActivity
															.getKellner());
											objectToSave.put("Used", "unused");
											objectToSave.saveInBackground();
											Toast.makeText(v.getContext(), key,
													Toast.LENGTH_SHORT).show();
										}

									}
								})
						.setNegativeButton("Nein",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			}
		});

		return view;
	}
}