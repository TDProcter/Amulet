package procter.thomas.amulet;

import java.util.concurrent.ExecutionException;

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
		
			
		
		
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
		retrieveData.execute("http://08309.net.dcs.hull.ac.uk/api/admin/details?" +
				"username=" + username +
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
		
		
		if(httpData.contains("{\"FullName\":")){
			Intent intent = new Intent(this, MenuActivity.class);////test only
			startActivity(intent);
			
			String text = "Login Succesful";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
		else if(httpData.contains("{\"Error\":\"User or password unknown\"}")){
			
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
		
	}

	
	
	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		
	}
}
