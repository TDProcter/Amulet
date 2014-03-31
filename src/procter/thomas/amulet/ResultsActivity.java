package procter.thomas.amulet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.app.Activity;
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
		String scoreUnit = intent.getStringExtra("unit");
		TextView textView = (TextView) findViewById(R.id.txtInspectionScore);
		textView.setText("Score: " + score + scoreUnit);
	    
	
	}
	
	@Override
	public void onBackPressed() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this, "Result Not Saved!", duration);
		toast.show();
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}
	
	public void menuButton(View v){
		postToServer();
		
	}
	
	private void postToServer(){
		TextView unitsTextView = (TextView) findViewById(R.id.txtUnitsConsumed);
		String unitsConsumed = unitsTextView.getText().toString();
		Log.i("units", unitsConsumed+"");
		JSONObject obj = taskObject(unitsConsumed);
		String HTTPString = obj.toString();
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		retrieveData.execute("POST", "http://08309.net.dcs.hull.ac.uk/api/admin/task", HTTPString);
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
		
		
	}
	
}
