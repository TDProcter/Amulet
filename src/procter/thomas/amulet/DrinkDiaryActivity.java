package procter.thomas.amulet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DrinkDiaryActivity extends Activity implements OnRetrieveHttpData{

	ArrayList<String> addedDrinks;
	ArrayList<String> existingDrinks;
	ArrayList<String> addedQuantities;
	ArrayAdapter<String> dataAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drink_diary);
		addedDrinks = new ArrayList<String>();
		existingDrinks = new ArrayList<String>();
		addedQuantities = new ArrayList<String>();
		existingDrinks.add("Stella");
		existingDrinks.add("Guiness");
		setupList();
	}
	
	private void setupList(){
		
		String[] drinks = new String[addedDrinks.size()];
		addedDrinks.toArray(drinks);
		
		for(int i = 0; i < addedDrinks.size(); i++){
			drinks[i] = addedQuantities.get(i) + " x "+ drinks[i];
		}
		
		dataAdapter = new ArrayAdapter<String>(this,
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
	        	addedQuantities.add(mTextView.getText().toString());
	        	setupList();
	        }
	     });
	    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     });
	 
     builder.show();
     
	}
	
	public void addButton(View view){
		addDrink();
		setupList();
		
	}
	
	public void submitButton(View view){
		postToServer();
		addedDrinks.clear();
		addedQuantities.clear();
		setupList();
		
	}
	
	private void postToServer(){
		JSONObject obj = entryObject();
		String HTTPString = obj.toString();
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		retrieveData.execute("POST", "http://08309.net.dcs.hull.ac.uk/api/admin/drink", HTTPString);
		saveToFile(obj, "posted");
	}
	
	private void saveToFile(JSONObject obj, String posted){
		InternalSave internalSave = new InternalSave();
		JSONObject currentObj = internalSave.readDrinkDiary(this);
		try {
			currentObj.putOpt(posted, obj);
			Log.i("tags", currentObj.toString());
			internalSave.saveDrinkDiary(this, currentObj);
			Log.i("tags", currentObj.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private JSONObject entryObject(){
		JSONObject obj = new JSONObject();
		JSONObject entries = new JSONObject();
		JSONArray entryArray = new JSONArray();
		
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "default");
		String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "default");
		
		
		Time time = new Time();
		time.setToNow();
		String timeStamp = time.format("%Y-%m-%d %H:%M:%S");
		
		try {
			for(int i = 0; i < addedDrinks.size(); i++){
				entries.put("timestamp",  timeStamp);
				entries.put("drinktype",  addedDrinks.get(i));
				entries.put("unitsconsumed",  addedQuantities.get(i));
				entryArray.put(entries);
			}
			
			obj.put("username", username);
			obj.put("password", password);
			obj.put("entries", entryArray);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("tag", obj.toString());
		
		return obj;
	}

	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		Log.i("onTaskCompleted", httpData);
		
	}
	
	
}
