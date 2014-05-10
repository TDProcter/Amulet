package procter.thomas.amulet;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewTaskHistoryActivity extends Activity{

	private Cursor taskHistoryCursor;
	SimpleCursorAdapter dataAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task_history);
		setupList();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		taskHistoryCursor.close();
	}
	
	private void setupList() {

		ContentResolver cr = getContentResolver();
		StorageMethods storageMethods = new StorageMethods();
		taskHistoryCursor = storageMethods.getTaskHistoryComplete(cr, AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN);
		
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
	
}
