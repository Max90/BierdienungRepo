package de.ur.mi.parse;

import java.util.ArrayList;
import java.util.List;

import de.ur.bierdienung.R;
import de.ur.mi.bierdienung.WaiterTableSelectActivity;
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
    LayoutInflater inflater;
    private List<ParselistdownloadClass> parselistdownloadList = null;
    private ArrayList<ParselistdownloadClass> arraylist;

    public ListViewAdapter(Context context,
                           List<ParselistdownloadClass> parselistdownloadList) {
        try {
            inflater = LayoutInflater.from(context);
            this.parselistdownloadList = parselistdownloadList;
            this.arraylist = new ArrayList<ParselistdownloadClass>();
            this.arraylist.addAll(parselistdownloadList);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        view = inflater.inflate(R.layout.listview_item, null);
        // Locate the TextViews in listview_item.xml
        TextView listviewName = (TextView) view.findViewById(R.id.text_view_name);
        TextView listviewPreis = (TextView) view.findViewById(R.id.text_view_price);

        listviewName.setText(parselistdownloadList.get(position).getName());

        listviewPreis.setText(parselistdownloadList.get(position).getPreis());

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
                String[] nums = new String[20];
                for (int i = 0; i < nums.length; i++)
                    nums[i] = Integer.toString(i + 1);

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
                                        for (int i = 1; i < np.getValue() + 1; i++) {

                                            ParseObject objectToSave = new ParseObject(
                                                    LoginSignupActivity
                                                            .getParseUser()
                                                            + "_Bestellung");
                                            objectToSave
                                                    .put("Name",
                                                            parselistdownloadList
                                                                    .get(position)
                                                                    .getName());

                                            objectToSave.put("Background",
                                                    "unmarked");
                                            objectToSave.put(
                                                    "Preis",
                                                    parselistdownloadList.get(
                                                            position)
                                                            .getPreis());
                                            objectToSave.put("Tisch",
                                                    WaiterTableSelectActivity
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
                                            objectToSave.put("Status", "offen");
                                            objectToSave.saveInBackground();

                                        }
                                        String toastString = String.valueOf(np
                                                .getValue())
                                                + " x "
                                                + parselistdownloadList.get(
                                                position).getName()
                                                + R.string.add_to_order_toast_string;
                                        Toast.makeText(v.getContext(), toastString,
                                                Toast.LENGTH_SHORT).show();
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