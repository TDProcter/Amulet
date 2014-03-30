package procter.thomas.amulet;

import org.json.JSONException;
import org.json.JSONObject;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnRetrieveHttpData{
	
	private boolean debugMode = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkIfLoggedIn();
	}

	@Override
	public void onBackPressed() {
		if(debugMode){
			startMenu();//testing only
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void createAccountButton(View view){
		Intent intent = new Intent(this, CreateAccountActivity.class);
		startActivity(intent);
	}
	
	public void loginButton(View view){
		
		
		TextView usernameTextView = (TextView) findViewById(R.id.txtUsernameLogin);
		TextView passwordTextView = (TextView) findViewById(R.id.txtPasswordLogin);
		
		String username  = usernameTextView.getText().toString();
		String password = passwordTextView.getText().toString();
		Login(username, password);
		
	}

	private void checkIfLoggedIn(){
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "Default");
		String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "Default");
		if(!(username.equals("Default")))
		{
			Log.i(username, password);
			removeFromPrefs();
			Login(username, password);
		}
	}
	private void Login(String username, String password){
		
			
			SharedPreferencesWrapper.saveToPrefs(this, "username", username);
			SharedPreferencesWrapper.saveToPrefs(this, "password", password);
		
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		Log.i("test", username + " " + password);
		 retrieveData.execute("GET", "http://08309.net.dcs.hull.ac.uk/api/admin/details?" +
				"username=" + username +
				"&password=" + password); 
		Log.i("tag", "preexecute");
		/*
		retrieveData.execute("POST", "http://08309.net.dcs.hull.ac.uk/api/admin/task", "{\"username\":\"jeff@alan.com\",\"password\":\"no\",\"tasks\":[{\"tasktype\":\"inspection\",\"timestamp\":\"2010-03-08 14:59:30.252\",\"taskvalue\":\"133\",\"unitsconsumed\":\"5\"},{\"tasktype\":\"inspection\",\"timestamp\":\"2010-03-08 18:59:30.252\",\"taskvalue\":\"404\",\"unitsconsumed\":\"15\"}]}");
		retrievesData.execute("GET", "http://08309.net.dcs.hull.ac.uk/api/admin/taskhistory?username=jeff@alan.com&password=no&tasktype=inspection");
		*/
	}
	private void processObject(JSONObject result){
		Log.i("test", "process object");
		try {
			if(result.has("FullName")){
			
				startMenu();
			
				String text = "Login Succesful";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
				Log.i("json", result.getString("FullName"));
			}
			else if (result.has("Error")){
				String text = "User or password unknown";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
				removeFromPrefs();
			}
			else{
				String text = "Unknown Error";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
				removeFromPrefs();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startMenu(){
		Intent intent = new Intent(this, MenuActivity.class);////test only
		startActivity(intent);
	}
	
	private void removeFromPrefs(){
		SharedPreferencesWrapper.removeFromPrefs(this, "username");
		SharedPreferencesWrapper.removeFromPrefs(this, "password");
	}
	@Override
	public void onTaskCompleted(String httpData) {
		try {
			Log.i("ontaskcompleted", httpData);
			JSONObject result = new JSONObject(httpData);
			processObject(result);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
