package de.ur.mi.parse;

import java.util.ArrayList;
import java.util.List;

import de.ur.bierdienung.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<parselistdownload> parselistdownloadList = null;
	private ArrayList<parselistdownload> arraylist;

	public ListViewAdapter(Context context,
			List<parselistdownload> parselistdownloadList) {
		mContext = context;
		this.parselistdownloadList = parselistdownloadList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<parselistdownload>();
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
	public parselistdownload getItem(int position) {
		return parselistdownloadList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// private void showToast() {
	// int d = Toast.LENGTH_SHORT;
	// Toast toast = Toast.makeText(v.getContext(), "Test", d);
	// toast.setGravity(Gravity.BOTTOM, 0, 0);
	// toast.show();
	// }

	public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.listviewName = (TextView) view.findViewById(R.id.listviewName);
            holder.listviewPreis = (TextView) view.findViewById(R.id.listviewPreis);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.listviewName.setText(parselistdownloadList.get(position).getName());
        holder.listviewPreis.setText(parselistdownloadList.get(position).getPreis());
        
        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // Send single item click data to SingleItemView Class
            	
            	//  Intent intent = new Intent(mContext, SingleItemView.class);
                
                //get name
                //parselistdownloadList.get(position).getName();
                //get preis
                //parselistdownloadList.get(position).getPreis()
                
                String key = parselistdownloadList.get(position).getName() + " wurde bestellt";
                
                Toast.makeText(v.getContext(), key, Toast.LENGTH_SHORT).show();


            }
        });
 
        return view;
    }
}