package procter.thomas.amulet;

import org.json.JSONObject;

import procter.thomas.amulet.OnRetrieveHTTPData.OnRetrieveHttpData;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewTaskHistoryActivity extends Activity implements OnRetrieveHttpData{

	private Cursor taskHistoryCursor;
	SimpleCursorAdapter dataAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task_history);
		sync();
		setupList();
	}
	
	@Override
	protected void onStop(){
		
		super.onStop();
		taskHistoryCursor.close();
	}
	
	private void setupList() {

		ContentResolver cr = getContentResolver();
		taskHistoryCursor = StorageMethods.getTaskHistoryComplete(cr, AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN);
		
		if(taskHistoryCursor.getCount()>0){
			
			
			// The desired columns to be bound
			  String[] columns = new String[] {
					  AmuletContentProvider.KEY_TASKS_TASKTYPE_COLUMN, 
					  AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN,
					  AmuletContentProvider.KEY_TASKS_TASKVALUE_COLUMN,
					  AmuletContentProvider.KEY_TASKS_UNITSCONSUMED_COLUMN
			  };
			 
			  // the XML defined views which the data will be bound to
			  int[] to = new int[] { 
			    R.id.lblTskLstTaskType,
			    R.id.lblTskLstTimeStamp,
			    R.id.lblTskLstTaskValue,
			    R.id.lblTskLstUnitsConsumed
			  };
			 
			  
			  
			  // create the adapter using the cursor pointing to the desired data 
			  //as well as the layout information
			  dataAdapter = new SimpleCursorAdapter(
			    this, R.layout.task_history_list_view, 
			    taskHistoryCursor, 
			    columns, 
			    to,
			    0);
			  
	          
			  ListView listView = (ListView) findViewById(R.id.lstTaskHistory);
			  // Assign adapter to ListView
			  listView.setAdapter(dataAdapter);
			  
			}  
		}
	
	private void sync(){
		ContentResolver cr = getContentResolver();
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this, cr);	
		StorageMethods meth = new StorageMethods();
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "Default");
		String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "Default");
		
		Cursor unsyncedTaskCursor = StorageMethods.getUnsyncedTaskHistory(cr);
		Log.i("unsyncedcount", unsyncedTaskCursor.getCount()+"");
		if(unsyncedTaskCursor.getCount()>0){
		JSONObject jsonObject = meth.packTaskCursor(this, unsyncedTaskCursor);
		String HTTPString = jsonObject.toString();
		retrieveData.execute("POST&UPDATETASK", "http://08309.net.dcs.hull.ac.uk/api/admin/task", HTTPString);
		}
		unsyncedTaskCursor.close();
		
	}
	
	@Override
	public void onTaskCompleted(String httpData) {
		ContentResolver cr = getContentResolver();
		RetrieveHTTPDataAsync retrieveData = new RetrieveHTTPDataAsync(this, cr);	
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "Default");
		String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "Default");
		
		if(httpData.contains("tasks received")){
			retrieveData.execute("GET&SAVETASK", "http://08309.net.dcs.hull.ac.uk/api/admin/taskhistory" +
					"?username=" + username +
					"&password=" + password +
					"&tasktype=all"); 
		}
	}
}
