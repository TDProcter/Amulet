package procter.thomas.amulet;

import java.util.concurrent.ExecutionException;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkIfLoggedIn();
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
		String password = SharedPreferencesWrapper.getFromPrefs(this, "username", "Default");
		if(!(username.equals("Default")))
		{
			Login(username, password);
		}
	}
	private void Login(String username, String password){
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		
		 retrieveData.execute("GET", "http://08309.net.dcs.hull.ac.uk/api/admin/details?" +
				"username=" + username +
				"&password=" + password); 
		Log.i("tag", "preexecute");/*
		retrieveData.execute("POST", "http://08309.net.dcs.hull.ac.uk/api/admin/task", "{\"username\":\"jeff@alan.com\",\"password\":\"no\",\"tasks\":[{\"tasktype\":\"inspection\",\"timestamp\":\"2010-03-08 14:59:30.252\",\"taskvalue\":\"133\",\"unitsconsumed\":\"5\"},{\"tasktype\":\"inspection\",\"timestamp\":\"2010-03-08 18:59:30.252\",\"taskvalue\":\"404\",\"unitsconsumed\":\"15\"}]}");
		retrievesData.execute("GET", "http://08309.net.dcs.hull.ac.uk/api/admin/taskhistory?username=jeff@alan.com&password=no&tasktype=inspection");
		*/
		
		
		//int openBracePos = httpData.indexOf('{');
		//httpData = httpData.substring(openBracePos+2, httpData.length()-2);
			
		
		
		
		
	}
	private void processObject(JSONObject result){
		try {
		if(result.has("FullName")){
			if(!(SharedPreferencesWrapper.getFromPrefs(this, "username", "Default").equals("Default")))
			{
				SharedPreferencesWrapper.saveToPrefs(this, "username", result.getString("FullName"));
				SharedPreferencesWrapper.saveToPrefs(this, "password", result.getString("UserName"));
			}
			//stuffs gone wrong here 
			Intent intent = new Intent(this, MenuActivity.class);////test only
			startActivity(intent);
			
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
		}
		else{
			String text = "Unknown Error";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onTaskCompleted(String httpData) {

		try {
			JSONObject result = new JSONObject(httpData);
			processObject(result);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
