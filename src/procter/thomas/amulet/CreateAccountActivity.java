package procter.thomas.amulet;

import java.util.concurrent.ExecutionException;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.app.Activity;
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
		retrieveData.execute("http://08309.net.dcs.hull.ac.uk/api/admin/register?" +
				"firstname=" + firstname +
				"&Surname=" + surname +
				"&username=" + username +
				"&password=" + password);

		String httpData = "";
		
			try {
				httpData = retrieveData.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		//int openBracePos = httpData.indexOf('{');
		//httpData = httpData.substring(openBracePos+2, httpData.length()-2);
		Log.i("tag", httpData);
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this, httpData, duration);
		toast.show();
		}
		else{
			String text = "Error: Passwords do not match";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
	}

	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		
	}

}

