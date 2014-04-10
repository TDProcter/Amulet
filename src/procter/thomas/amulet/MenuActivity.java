package procter.thomas.amulet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	@Override
	public void onBackPressed() {
		logOutCheck();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	        	logOutCheck();
	            return true;
	        case R.id.action_account_management:
	        	Intent intent = new Intent(this, AccountManagementActivity.class);
	        	startActivity(intent);
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void inspectionButton(View view){
		
		Intent intent = new Intent(this, InspectionActivity.class);
		startActivity(intent);
	}
	
	public void pilotButton(View view){
		
		Intent intent = new Intent(this, PilotActivity.class);
		startActivity(intent);
	}
	
	public void sequenceButton(View view){
		Intent intent = new Intent(this, SequenceActivity.class);
		startActivity(intent);
	}
	
	public void drinkDiaryButton(View view){
		Intent intent = new Intent(this, DrinkDiaryActivity.class);
		startActivity(intent);
	}
	
	private void logOutCheck(){
		
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "Default Username");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Log Out");
	    builder.setMessage("Are you sure you would like to Log Out, " + username + "?");
	    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            //log out
	        	logOut();
	        	
	        }
	     });
	    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     });
	    builder.setIcon(R.drawable.ic_launcher);
	     builder.show();
		
		
	}
	
	private void logOut(){
		SharedPreferencesWrapper.removeFromPrefs(this, "username");
		SharedPreferencesWrapper.removeFromPrefs(this, "password");
		SharedPreferencesWrapper.removeFromPrefs(this, "fullName");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}

