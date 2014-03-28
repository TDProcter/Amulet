package procter.thomas.amulet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class SequenceActivity extends Activity{
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_sequence);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		String[] numbers = {"1", "2", "3","4","5","6","7","8","9", "10", "11", "12","13","14","15","16"};
		//int[] numbers = {1, 2, 3,4,5,6,7,8,9};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sequence_text_view, numbers);
	    gridview.setAdapter(adapter);
	    
	    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(SequenceActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            TextView test = (TextView) v;
	            
	            test.setBackgroundColor(256);
	        }
	    });
	    
		
	}
	
	
	
	
}
