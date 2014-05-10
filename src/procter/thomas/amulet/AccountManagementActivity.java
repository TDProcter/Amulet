package procter.thomas.amulet;

import org.json.JSONException;
import org.json.JSONObject;

import procter.thomas.amulet.OnExchangeHTTPData.OnExchangeHttpData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountManagementActivity extends Activity implements OnExchangeHttpData{

	int mode = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_management);
		setAccountDetails();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_management, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_delete_account:
	        	mode = 2;
	    		confirmDelete();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void setAccountDetails(){
		TextView username = (TextView) findViewById(R.id.lblMngUsernameView);
		TextView fullname = (TextView) findViewById(R.id.lblMngFullNameView);
		username.setText(SharedPreferencesWrapper.getFromPrefs(this, "username", "defaultUserName"));
		fullname.setText(SharedPreferencesWrapper.getFromPrefs(this, "fullName", "defaultUserName"));
		
	}
	
	public void changePassword(View view){
		mode = 1;
		setContentView(R.layout.activity_change_password);
	}
	public void taskHistory(View view){
		Intent intent = new Intent(this, ViewTaskHistoryActivity.class);
		startActivity(intent);
	}
	
	public void drinkDiary(View view){
		Intent intent = new Intent(this, ViewDrinkDiaryActivity.class);
		startActivity(intent);
	}
	
	private void deleteAccountConfirmed(){
		ExchangeHTTPDataAsync retrieveData = new ExchangeHTTPDataAsync(this);
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "defaultUserName");
		String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "defaultPassword");
		
		retrieveData.execute("GET", "http://08309.net.dcs.hull.ac.uk/api/admin/unregister?" +
				"username=" + username +
				"&password=" + password);
	}
	
	public void changePasswordComplete(View view){
		TextView txtNewPassword = (TextView) findViewById(R.id.txtMngNewPassword);
		TextView txtConfirmPassword = (TextView) findViewById(R.id.txtMngConfirmPassword);
		if(txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
			TextView txtOldPassword = (TextView) findViewById(R.id.txtMngCurrentPassword);
			String newPassword = txtNewPassword.getText().toString();
			String oldPassword = txtOldPassword.getText().toString();
			String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "defaultUserName");
			ExchangeHTTPDataAsync retrieveData = new ExchangeHTTPDataAsync(this);
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
				if(mode == 1)
				{
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
				else if(mode == 2){
					String text = "Account Deleted";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(this, text, duration);
					toast.show();
					logOut();
				}
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
	
	private void confirmDelete(){
		
		 LayoutInflater li = LayoutInflater.from(this);

	     View promptsView = li.inflate(R.layout.confirm_password, null);

	     AlertDialog.Builder builder = new AlertDialog.Builder(this);

	     builder.setView(promptsView);

	     // set dialog message

	     builder.setTitle("Are you sure you want to delete your account?");
	     builder.setIcon(R.drawable.ic_launcher);
	     // create alert dialog
	     
	     final EditText editText = (EditText) promptsView.findViewById(R.id.txtAccConfirmPassword);
	     final String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "defaultPassword");
	     builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            
		            String confirm = editText.getText().toString();
		            if(confirm.equals(password)){
		            	deleteAccountConfirmed();
		            }
		            else{
		            	incorrectPassword();
		            }
		            
		        }
		     });
		    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	
		        }
		     });
		 
	     builder.show();
	     
		}
	
	private void incorrectPassword(){
		String text = "Incorrect Password";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	public void logOut(){
		if(!(SharedPreferencesWrapper.getFromPrefs(this, "SequenceBaseLine", "noBaseLine").equals("noBaseLine"))){
			SharedPreferencesWrapper.removeFromPrefs(this, "SequenceBaseLine");
		}
		if(!(SharedPreferencesWrapper.getFromPrefs(this, "PilotBaseLine", "noBaseLine").equals("noBaseLine"))){
			SharedPreferencesWrapper.removeFromPrefs(this, "PilotBaseLine");
		}
		if(!(SharedPreferencesWrapper.getFromPrefs(this, "InspectionBaseLine", "noBaseLine").equals("noBaseLine"))){
			SharedPreferencesWrapper.removeFromPrefs(this, "InspectionBaseLine");
		}
		if(!(SharedPreferencesWrapper.getFromPrefs(this, "lastsync", "def").equals("def"))){
			SharedPreferencesWrapper.removeFromPrefs(this, "lastsync");
		}
		SharedPreferencesWrapper.removeFromPrefs(this, "username");
		SharedPreferencesWrapper.removeFromPrefs(this, "password");
		SharedPreferencesWrapper.removeFromPrefs(this, "fullName");
		
		ContentResolver cr = getContentResolver();
		int deleted = StorageMethods.deleteAll(cr);
		Log.i("deleted", deleted+"");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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
