package procter.thomas.amulet;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DrinkDiaryActivity extends Activity{

	ArrayList<String> addedDrinks;
	ArrayList<String> existingDrinks;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drink_diary);
		addedDrinks = new ArrayList<String>();
		existingDrinks = new ArrayList<String>();
		existingDrinks.add("Stella");
		existingDrinks.add("Guiness");
		setupList();
	}
	
	private void setupList(){
		
		String[] drinks = new String[addedDrinks.size()];
		addedDrinks.toArray(drinks);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.custom_list_view, drinks);
			 
			  ListView listView = (ListView) findViewById(R.id.lstAddedDrinks);
			  // Assign adapter to ListView
			  
			  listView.setAdapter(dataAdapter);
			  
			  listView.setOnItemClickListener(new OnItemClickListener() {
				   @Override
				   public void onItemClick(AdapterView<?> listView, View view, 
				     int position, long id) {
				   
					   
					   confirmation(position);
					   
				   }
				  });
	}
	
	private void confirmation(final int rowId){
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete entry");
	    builder.setMessage("Are you sure you want to delete this entry?");
	    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	addedDrinks.remove(rowId);
	        	setupList();
	        }
	     });
	    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     });
	    builder.setIcon(R.drawable.ic_launcher);
	     builder.show();
		
		
	}
	
private void addDrink(){
		
	String[] drinks = new String[existingDrinks.size()];
	existingDrinks.toArray(drinks);
	
	 LayoutInflater li = LayoutInflater.from(this);

     View promptsView = li.inflate(R.layout.dialog_unit_calculator, null);

     AlertDialog.Builder builder = new AlertDialog.Builder(this);

     builder.setView(promptsView);

     // set dialog message

     builder.setTitle("Add a Drink");
     builder.setIcon(R.drawable.ic_launcher);
     // create alert dialog
     
     final Spinner mSpinner= (Spinner) promptsView
             .findViewById(R.id.spnDrinks);
     final TextView mTextView = (TextView) promptsView
    		 .findViewById(R.id.txtDialogUnits);
     
     mSpinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, drinks));
     
     builder.setPositiveButton("Add Drink", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	addedDrinks.add(mSpinner.getSelectedItem().toString());
	        	setupList();
	        }
	     });
	    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     });
	    

     // reference UI elements from my_dialog_layout in similar fashion

     // show it
     builder.show();
     
		
	}
	
	public void addButton(View view){
		addDrink();
		setupList();
	}
}
