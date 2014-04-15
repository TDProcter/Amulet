package procter.thomas.amulet;

import org.json.JSONObject;

import procter.thomas.amulet.OnExchangeHTTPData.OnExchangeHttpData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnExchangeHttpData{
	
	private int syncCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		syncCount = 0;
		String lastSync = SharedPreferencesWrapper.getFromPrefs(this,
				"lastsync", "Default");
		if(!(lastSync.equals("Default"))){
			Time lastSyncTime = new Time();
			Time syncTime = new Time();
			syncTime.setToNow();
			Log.i("parse", "parse");
			Log.i("sync time", lastSync);
			long millis = Long.parseLong(lastSync.trim());
			Log.i("millis", millis + "");
			lastSyncTime.set(millis);
			Log.i("set time", "set");
			Log.i("last load time day", lastSyncTime.yearDay+"");
			Log.i("last load time", lastSyncTime.year+"");
			Log.i("time day", syncTime.yearDay+"");
			Log.i("time", syncTime.year+"");
			if(lastSyncTime.year < syncTime.year){
				fullSync();
			}
			else if(lastSyncTime.yearDay < syncTime.yearDay || (lastSyncTime.yearDay != 0 && syncTime.yearDay ==0)){
				fullSync();
			}
			
		}
		else{
			fullSync();
		}
		
		
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
		if(SharedPreferencesWrapper.getFromPrefs(this, "InspectionBaseLine", "noBaseLine").equals("noBaseLine")){
			calibrationConfirmation(intent);
		}
		else{
			intent.putExtra("mode", "normal");
			startActivity(intent);
		}
	}
	
	public void pilotButton(View view){
		
		Intent intent = new Intent(this, PilotActivity.class);
		if(SharedPreferencesWrapper.getFromPrefs(this, "PilotBaseLine", "noBaseLine").equals("noBaseLine")){
			calibrationConfirmation(intent);
		}
		else{
			intent.putExtra("mode", "normal");
			startActivity(intent);
		}
	}
	
	public void sequenceButton(View view){
		Intent intent = new Intent(this, SequenceActivity.class);
		if(SharedPreferencesWrapper.getFromPrefs(this, "SequenceBaseLine", "noBaseLine").equals("noBaseLine")){
			calibrationConfirmation(intent);
		}
		else{
			intent.putExtra("mode", "normal");
			startActivity(intent);
		}
		
		
	}
	
	public void drinkDiaryButton(View view){
		Intent intent = new Intent(this, DrinkDiaryActivity.class);
		startActivity(intent);
	}
	
	private void logOutCheck(){
		
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "Default Username");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Log Out");
	    builder.setMessage("Are you sure you would like to Log Out, " + username + "?\n\nAll unsynced data will be deleted!");
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
	
private void calibrationConfirmation(final Intent intent){
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Sobriety is an issue!");
	    builder.setMessage("You haven't yet calibrated this task!\n" +
	    		"You must be sobre to calculate a baseline accurately.\n" +
	    		"Are you sobre enough to continue?");
	    builder.setPositiveButton("I haven't had a drink yet!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	intent.putExtra("mode", "calibration");
	        	startActivity(intent);
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
	
	private void fullSync() {
		ContentResolver cr = getContentResolver();

		StorageMethods storageMethods = new StorageMethods();

		String username = SharedPreferencesWrapper.getFromPrefs(this,
				"username", "Default");
		String password = SharedPreferencesWrapper.getFromPrefs(this,
				"password", "Default");

		Cursor taskCursor = StorageMethods.getUnsyncedTaskHistory(cr);
		if (taskCursor.getCount() > 0) {

			JSONObject taskObj = storageMethods
					.packTaskCursor(this, taskCursor);
			ExchangeHTTPDataAsync retrieveData = new ExchangeHTTPDataAsync(
					this, cr);
			retrieveData.execute("POST&UPDATETASK",
					"http://08309.net.dcs.hull.ac.uk/api/admin/task",
					taskObj.toString());

		}
		else{
			syncCount++;
		}
		taskCursor.close();

		Cursor diaryCursor = StorageMethods.getUnsyncedDrinkDiary(cr);
		if (diaryCursor.getCount() > 0) {
			JSONObject diaryObj = storageMethods.packDiaryCursor(this,
					diaryCursor);
			ExchangeHTTPDataAsync retrieveData = new ExchangeHTTPDataAsync(
					this, cr);
			retrieveData.execute("POST&UPDATEDIARY",
					"http://08309.net.dcs.hull.ac.uk/api/admin/drink",
					diaryObj.toString());
		}
		else{
			syncCount++;
		}
		diaryCursor.close();

		{
			ExchangeHTTPDataAsync retrieveData = new ExchangeHTTPDataAsync(
					this, cr);
			retrieveData.execute("GET&SAVETASK",
					"http://08309.net.dcs.hull.ac.uk/api/admin/taskhistory"
							+ "?username=" + username + "&password=" + password
							+ "&tasktype=all");
			
		}

		{
			ExchangeHTTPDataAsync retrieveData = new ExchangeHTTPDataAsync(
					this, cr);
			retrieveData
					.execute("GET&SAVEDIARY",
							"http://08309.net.dcs.hull.ac.uk/api/admin/diary"
									+ "?username=" + username + "&password="
									+ password);
		}
		
	}

	
	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		
		if(httpData.contains("tasks received")){
			syncCount++;
		}
		else if(httpData.contains("entries received")){
			syncCount++;
		}
		else if(httpData != null){
			
				
					if (httpData.contains("drinktype")) {
						syncCount++;
					}
				
				
					if (httpData.contains("tasktype")) {
						syncCount++;
						
				}
			
		}
		
		if(syncCount > 3){
			Time time = new Time();
			time.setToNow();
			SharedPreferencesWrapper.saveToPrefs(this, "lastsync",
					time.toMillis(false) + "");
			Log.i("saved time", time.toMillis(false) + "");
			
			String text = "All Data Synced with Server";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(this, text, duration);
			toast.show();
		}
			
		
	}
}

