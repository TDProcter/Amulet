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

public class CreateAccountActivity extends Activity implements OnRetrieveHttpData{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void registerUserButton(View view){
		TextView firstnameTextView = (TextView) findViewById(R.id.txtFirstname);
		TextView surnameTextView = (TextView) findViewById(R.id.txtSurname);
		TextView usernameTextView = (TextView) findViewById(R.id.txtEmail);
		TextView passwordTextView = (TextView) findViewById(R.id.txtPassword);
		TextView confirmPasswordTextView = (TextView) findViewById(R.id.txtConfirmPassword);
		String firstname = firstnameTextView.getText().toString();
		String surname = surnameTextView.getText().toString();
		String username  = usernameTextView.getText().toString();
		String password = passwordTextView.getText().toString();
		String confirmPassword = confirmPasswordTextView.getText().toString();
		if(password.equals(confirmPassword))
		{
			
		
		
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		retrieveData.execute("GET", "http://08309.net.dcs.hull.ac.uk/api/admin/register?" +
				"firstname=" + firstname +
				"&Surname=" + surname +
				"&username=" + username +
				"&password=" + password);
		}
		else{
			String text = "Error: Passwords do not match";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
	}

	private void processObject(JSONObject result){
		try {
			if(result.has("Result")){
			
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			
				String text = "Account Successfuly Created";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
				Log.i("json", result.getString("Result"));
			}
			else if (result.has("Error")){
				String text = result.getString("Error");
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
			
			String text = "JSON Error";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
			
			e.printStackTrace();
		}
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

