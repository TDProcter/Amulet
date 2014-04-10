package procter.thomas.amulet;

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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity implements OnRetrieveHttpData{

	int score;
	String task;
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
			TextView textView = (TextView) findViewById(R.id.txtInspectionScore);
			textView.setText("Score: " + score + scoreUnit);
		}
	
	}
	
	@Override
	public void onBackPressed() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this, "Result Not Saved!", duration);
		toast.show();
		startMenu();
	}
	
	public void menuButton(View v){
		TextView unitsTextView = (TextView) findViewById(R.id.txtUnitsConsumed);
		String unitsConsumed = unitsTextView.getText().toString();
		int convertUnits = Integer.parseInt(unitsConsumed);
		unitsConsumed = convertUnits+"";
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
	
	private void postToServer(String unitsConsumed){
		
		Log.i("units", unitsConsumed+"");
		JSONObject obj = taskObject(unitsConsumed);
		String HTTPString = obj.toString();
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		retrieveData.execute("POST", "http://08309.net.dcs.hull.ac.uk/api/admin/task", HTTPString);
		startMenu();
	}
	
	private void startMenu(){
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}
	
	private JSONObject taskObject(String unitsConsumed){
		JSONObject obj = new JSONObject();
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "default");
		String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "default");
		JSONObject tasks = new JSONObject();
		
		Time time = new Time();
		time.setToNow();
		String timeStamp = time.format("%Y-%m-%d %H:%M:%S");
		
		JSONArray taskArray = new JSONArray();
		
		Log.i("time: ", timeStamp);
		String taskValue = score + "";
		
		try {
			tasks.put("tasktype",  task);
			tasks.put("timestamp",  timeStamp);
			tasks.put("taskvalue",  taskValue);
			tasks.put("unitsconsumed",  unitsConsumed);
			
			taskArray.put(tasks);
			obj.put("username", username);
			obj.put("password", password);
			obj.put("tasks", taskArray);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}

	@Override
	public void onTaskCompleted(String httpData) {
		
		Log.i("http", httpData);
		
	}
	
}
