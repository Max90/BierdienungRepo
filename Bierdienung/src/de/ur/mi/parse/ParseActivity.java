package de.ur.mi.parse;

import de.ur.bierdienung.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.parse.Parse;


public class ParseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parse);
		
		 Parse.initialize(this, "8H5vDxr2paOyJbbKm0pnAw1JuriXdI1kmb0EtBTu", "FTLtxlrn9TM2ZIl7KuTcg0FBVFkOjJipBu11o7tW"); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parse, menu);
		return true;
	}

}
