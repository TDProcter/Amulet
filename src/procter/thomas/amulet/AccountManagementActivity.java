package procter.thomas.amulet;

import org.json.JSONException;
import org.json.JSONObject;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AccountManagementActivity extends Activity implements OnRetrieveHttpData{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_management);
		setAccountDetails();
	}
	
	private void setAccountDetails(){
		TextView username = (TextView) findViewById(R.id.lblMngUsernameView);
		TextView fullname = (TextView) findViewById(R.id.lblMngFullNameView);
		username.setText(SharedPreferencesWrapper.getFromPrefs(this, "username", "defaultUserName"));
		fullname.setText(SharedPreferencesWrapper.getFromPrefs(this, "fullName", "defaultUserName"));
		
	}
	
	public void changePassword(View view){
		setContentView(R.layout.activity_change_password);
	}
	
	public void changePasswordComplete(View view){
		TextView txtNewPassword = (TextView) findViewById(R.id.txtMngNewPassword);
		TextView txtConfirmPassword = (TextView) findViewById(R.id.txtMngConfirmPassword);
		if(txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
			TextView txtOldPassword = (TextView) findViewById(R.id.txtMngCurrentPassword);
			String newPassword = txtNewPassword.getText().toString();
			String oldPassword = txtOldPassword.getText().toString();
			String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "defaultUserName");
			RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this);
			retrieveData.execute("GET", "http://08309.net.dcs.hull.ac.uk/api/admin/change?" +
					"username=" + username +
					"&oldpassword=" + oldPassword +
					"&newpassword=" + newPassword);
		
		}
		else{
			String text = "New Passwords do not match!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
	}
	
	private void processObject(JSONObject result){
		try {
			if(result.has("Result")){
			
				String text = "Password Changed Successfuly";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
				SharedPreferencesWrapper.removeFromPrefs(this, "password");
				TextView txtNewPassword = (TextView) findViewById(R.id.txtMngNewPassword);
				SharedPreferencesWrapper.saveToPrefs(this, "password", txtNewPassword.getText().toString());
				setContentView(R.layout.activity_account_management);
				setAccountDetails();
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
			e.printStackTrace();
		}
	}
	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		Log.i("Amulet", httpData);
		JSONObject result;
		try {
			result = new JSONObject(httpData);
			processObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
