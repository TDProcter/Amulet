package procter.thomas.amulet;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity implements OnRetrieveHttpData{

	int score;
	String task;
	ArrayList<String> genericSizes;
	ArrayList<String> genericDrinks;
	ArrayList<int[]> genericSizesMl;//the amount of liquid in mililetres
	ArrayList<double[]> genericDrinksAbv;//The alcoholic volume of the drink (%)
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		
		setContentView(R.layout.activity_results);
		Intent intent = getIntent();
		score = intent.getIntExtra("score", -1);
		task = intent.getStringExtra("task");
		if(intent.getStringExtra("mode").equals("calibration")){
			calibrate();
		}
		else{
			String scoreUnit = intent.getStringExtra("unit");
			TextView textView = (TextView) findViewById(R.id.txtResultsScore);
			textView.setText("Score: " + score + scoreUnit);
		}
		setupDrinkList();
	}
	
	@Override
	public void onBackPressed() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this, "Result Not Saved!", duration);
		toast.show();
		startMenu();
	}
	
	public void menuButton(View v){
		TextView unitsTextView = (TextView) findViewById(R.id.txtResultsUnitsConsumed);
		String unitsConsumed = unitsTextView.getText().toString();
		double convertUnits = Double.parseDouble(unitsConsumed);
		unitsConsumed = String.format(Locale.UK, "%.2f", convertUnits);
		if(unitsConsumed.equals("0")){
			calibrationConfirmation();
		}
		else{
			postToServer(unitsConsumed);
		}
		
		
	}
	private void calibrate(){
		SharedPreferencesWrapper.saveToPrefs(this, task+"BaseLine", score+"");
		postToServer("0");
	}
private void calibrationConfirmation(){
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Sobre i see...");
	    builder.setMessage("Have you not been Drinking? Would you like to overwrite your calibration score?");
	    builder.setPositiveButton("Yeah, I did much better this time!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	calibrate();
	        }
	     });
	    builder.setNegativeButton("I lied, I have been drinking, I'll go back and type it in. :(", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     });
	    builder.setNeutralButton("Nah, that was rubbish..", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	startMenu();
	        }
	     });
	    builder.setIcon(R.drawable.ic_launcher);
	     builder.show();
		
		
	}
public void unitCalcButton(View view){
	final EditText txtUnits = (EditText) findViewById(R.id.txtResultsUnitsConsumed);
	if(txtUnits.getText().toString().equals("")){
		addDrink(0);
	}
	else{
	addDrink(Double.parseDouble(txtUnits.getText().toString()));
	
	}
}

private void setupEditTextUnit(double units){
	final EditText txtUnits = (EditText) findViewById(R.id.txtResultsUnitsConsumed);
    txtUnits.setText(String.format(Locale.UK, "%.2f", units));
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

private void addDrink(final double currentUnits){
	
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
     lblUnits.setText(String.format("%.2f", currentUnits));
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
	            units = currentUnits + units;
	            addDrink(units);
	        }
	     });
	    builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	setupEditTextUnit(currentUnits);
	        }
	     });
	 
     builder.show();
     
	}
	
	private void postToServer(String unitsConsumed){
		
		Log.i("units", unitsConsumed+"");
		ContentResolver cr = getContentResolver();
		StorageMethods storageMethods = new StorageMethods();
		
		
		Time time = new Time();
		time.setToNow();
		String timeStamp = time.format("%Y-%m-%d %H:%M:%S");
		
		StorageMethods.addNewTask(cr, task, timeStamp, score+"", unitsConsumed, false);
		Cursor taskCursor = StorageMethods.getUnsyncedTaskHistory(cr);
		Log.i("count", taskCursor.getCount()+"");
		
		JSONObject obj = storageMethods.packTaskCursor(this, taskCursor);
		Log.i("obj", obj.toString());
		String HTTPString = obj.toString();
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		retrieveData.execute("POST", "http://08309.net.dcs.hull.ac.uk/api/admin/task", HTTPString);
		
		taskCursor.close();
		startMenu();
	}
	
	private void startMenu(){
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}
	
	

	@Override
	public void onTaskCompleted(String httpData) {
		
		Log.i("http", httpData);
		
	}
	
}
