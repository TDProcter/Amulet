package procter.thomas.amulet;

import java.util.ArrayList;

import org.json.JSONObject;

import procter.thomas.amulet.OnExchangeHTTPData.OnExchangeHttpData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class DrinkDiaryActivity extends Activity implements OnExchangeHttpData{

	ArrayAdapter<String> dataAdapter;
	ArrayList<String> addedDrinks;
	ArrayList<String> addedUnits;
	ArrayList<String> addedQuantities;
	ArrayList<String> addedAdapter;
	ArrayList<String> genericDrinks;
	ArrayList<String> genericSizes;
	ArrayList<int[]> genericSizesMl;//the amount of liquid in mililetres
	ArrayList<double[]> genericDrinksAbv;//The alcoholic volume of the drink (%)
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drink_diary);
		
		addedDrinks = new ArrayList<String>();
		addedUnits = new ArrayList<String>();
		addedQuantities = new ArrayList<String>();
		addedAdapter = new ArrayList<String>();
		
		setupDrinkList();
		setupList();
	}
	
	private void setupDrinksArray(){
		addedAdapter.clear();
		
		
		for(int i = 0; i < addedDrinks.size(); i++){
			addedAdapter.add(addedQuantities.get(i) + " ml of "+ addedDrinks.get(i));
		}
	}
	
	private void setupList(){
		
		setupDrinksArray();
		
		dataAdapter = new ArrayAdapter<String>(this,
				R.layout.custom_list_view, addedAdapter);
			 
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
	        	addedUnits.remove(rowId);
	        	addedQuantities.remove(rowId);
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
	
	private void setupDrinkList(){
		
		genericDrinks = new ArrayList<String>();
		genericDrinksAbv = new ArrayList<double[]>();
		
		genericDrinks.add("Stella");
		genericDrinksAbv.add(new double[] {5.2});
		
		genericDrinks.add("Guiness");
		genericDrinksAbv.add(new double[] {4.1});
		
		genericSizes = new ArrayList<String>();
		genericSizesMl = new ArrayList<int[]>();
		
		genericSizes.add("Pint");
		genericSizesMl.add(new int[] {568});
		
		genericSizes.add("Half Pint");
		genericSizesMl.add(new int[] {284});
	}
	
	private void addDrink(){
		
		String[] drinks = new String[genericDrinks.size()];
		genericDrinks.toArray(drinks);
		
		String[] sizes = new String[genericSizes.size()];
		genericSizes.toArray(sizes);
		
		 LayoutInflater li = LayoutInflater.from(this);

	     View promptsView = li.inflate(R.layout.dialog_unit_calculator, null);

	     AlertDialog.Builder builder = new AlertDialog.Builder(this);

	     builder.setView(promptsView);

	     // set dialog message

	     builder.setTitle("Add a Drink");
	     builder.setIcon(R.drawable.ic_launcher);
	     // create alert dialog
	     
	     final Spinner spnDrinks= (Spinner) promptsView
	             .findViewById(R.id.spnCalcDrinks);
	     final Spinner spnSizes= (Spinner) promptsView
	             .findViewById(R.id.spnCalcSize);
	     final NumberPicker nbrQuantity = (NumberPicker) promptsView
	             .findViewById(R.id.nbrCalcQuantity);
	     final TextView lblUnits = (TextView) promptsView
	    		 .findViewById(R.id.lblCalcUnits);
	     lblUnits.setText("");
	     nbrQuantity.setMaxValue(100);
	     nbrQuantity.setMinValue(1);
	     
	     
	     spnDrinks.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_dropdown_item, drinks));
	     spnSizes.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_dropdown_item, sizes));
	     
	     builder.setPositiveButton("Add Drink", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            double vol = genericSizesMl.get(spnSizes.getSelectedItemPosition())[0];
		            double abv = genericDrinksAbv.get(spnDrinks.getSelectedItemPosition())[0];
		            double units = ((nbrQuantity.getValue() * vol) * abv)/1000;
		            
		            addedDrinks.add(spnDrinks.getSelectedItem().toString());
		            addedQuantities.add(String.format("%.0f", vol));
		            addedUnits.add(String.format("%.2f", units));
		            
		            setupDrinksArray();
		    		dataAdapter.notifyDataSetChanged();
		        }
		     });
		    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	temp();
		        	
		        }
		     });
		 
	     builder.show();
	     
		}
	
	private void temp(){
		Intent intent = new Intent(this, ViewDrinkDiaryActivity.class);
		startActivity(intent);
	}
	public void addButton(View view){
		addDrink();
		
		
	}
	
	public void submitButton(View view){
		postToServer();
		addedDrinks.clear();
		addedUnits.clear();
		addedQuantities.clear();
		setupDrinksArray();
		dataAdapter.notifyDataSetChanged();
		
	}
	
	private void postToServer(){

		ContentResolver cr = getContentResolver();
		StorageMethods storageMethods = new StorageMethods();
		
		
		Time time = new Time();
		time.setToNow();
		String timeStamp = time.format("%Y-%m-%d %H:%M:%S");
		
		for(int i = 0; i < addedDrinks.size(); i++){
		StorageMethods.addNewDiaryEntry(cr, timeStamp, addedDrinks.get(i), addedUnits.get(i), false);
		}
		Cursor diaryCursor = StorageMethods.getUnsyncedDrinkDiary(cr);
		Log.i("count", diaryCursor.getCount()+"");
		
		JSONObject obj = storageMethods.packDiaryCursor(this, diaryCursor);
		Log.i("obj", obj.toString());
		String HTTPString = obj.toString();
		ExchangeHTTPDataAsync retrieveData = new ExchangeHTTPDataAsync(this, cr);
		retrieveData.execute("POST&UPDATEDIARY", "http://08309.net.dcs.hull.ac.uk/api/admin/drink", HTTPString);
		
		diaryCursor.close();
		
	}

	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		Log.i("onTaskCompleted", httpData);
		
	}
	
	
}
