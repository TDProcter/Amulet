package procter.thomas.amulet;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewTaskHistoryActivity extends Activity implements OnRetrieveHttpData{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
	}
	
private void setupList(){
		
		
			 
			  ListView listView = (ListView) findViewById(R.id.lstAddedDrinks);
			  // Assign adapter to ListView
			  
			 // listView.setAdapter(dataAdapter);
			  
	}

	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		
	}
}
