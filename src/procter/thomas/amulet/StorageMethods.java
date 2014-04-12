package procter.thomas.amulet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class StorageMethods {

	public static Uri addNewTask(ContentResolver cr, String taskType,
			String timeStamp, String taskValue,
			String unitsConsumed, boolean synced) {
		
		// Create a new row of values to insert.
		ContentValues newValues = new ContentValues();

		// Assign values for each row.
		newValues.put(AmuletContentProvider.KEY_TASKS_TASKTYPE_COLUMN, taskType);
		newValues.put(AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN, timeStamp);
		newValues.put(AmuletContentProvider.KEY_TASKS_TASKVALUE_COLUMN, taskValue);
		newValues.put(AmuletContentProvider.KEY_TASKS_UNITSCONSUMED_COLUMN, unitsConsumed);
		newValues.put(AmuletContentProvider.KEY_TASKS_SYNCED_COLUMN, synced);
		
		

		// Insert the row into your table
		Uri myRowUri = cr.insert(AmuletContentProvider.CONTENT_URI_TASKS,
				newValues);
		
		//
		return myRowUri;
	}
	
	public static int deleteTask(ContentResolver cr, long rowId){
		Uri rowAddress = ContentUris.withAppendedId(
				AmuletContentProvider.CONTENT_URI_TASKS, rowId);
		int numberDeleted = cr.delete(rowAddress, null, null);
		return numberDeleted;
	}
	
	public static Cursor getTaskHistoryByTimeStamp(ContentResolver cr, String timeStamp) {

		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = new String[] { 
				AmuletContentProvider.KEY_ID,
				AmuletContentProvider.KEY_TASKS_TASKTYPE_COLUMN, 
				AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN, 
				AmuletContentProvider.KEY_TASKS_TASKVALUE_COLUMN, 
				AmuletContentProvider.KEY_TASKS_UNITSCONSUMED_COLUMN, 
				AmuletContentProvider.KEY_TASKS_SYNCED_COLUMN };
		
		

		// Append a row ID to the URI to address a specific row.
		Uri rowAddress = AmuletContentProvider.CONTENT_URI_TASKS;

		// Specify the where clause that will limit your results.
		String where = AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN
				+ "=?";
		String whereArgs[] = {timeStamp};
		String order = null;

		Cursor resultCursor = cr.query(rowAddress, result_columns, where,
				whereArgs, order);

		return resultCursor;
	}
	
	public static Cursor getTaskHistoryComplete(ContentResolver cr) {

		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = new String[] { 
				AmuletContentProvider.KEY_ID,
				AmuletContentProvider.KEY_TASKS_TASKTYPE_COLUMN, 
				AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN, 
				AmuletContentProvider.KEY_TASKS_TASKVALUE_COLUMN, 
				AmuletContentProvider.KEY_TASKS_UNITSCONSUMED_COLUMN, 
				AmuletContentProvider.KEY_TASKS_SYNCED_COLUMN };
		
		

		// Append a row ID to the URI to address a specific row.
		Uri rowAddress = AmuletContentProvider.CONTENT_URI_TASKS;

		// Specify the where clause that will limit your results.
		String where = null;
		String whereArgs[] = null;
		String order = null;

		Cursor resultCursor = cr.query(rowAddress, result_columns, where,
				whereArgs, order);

		return resultCursor;
	}
	
	public static Cursor getUnsyncedTaskHistory(ContentResolver cr) {

		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = new String[] { 
				AmuletContentProvider.KEY_ID,
				AmuletContentProvider.KEY_TASKS_TASKTYPE_COLUMN, 
				AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN, 
				AmuletContentProvider.KEY_TASKS_TASKVALUE_COLUMN, 
				AmuletContentProvider.KEY_TASKS_UNITSCONSUMED_COLUMN, 
				AmuletContentProvider.KEY_TASKS_SYNCED_COLUMN };
		
		

		// Append a row ID to the URI to address a specific row.
		Uri rowAddress = AmuletContentProvider.CONTENT_URI_TASKS;

		// Specify the where clause that will limit your results.
		String where = AmuletContentProvider.KEY_TASKS_SYNCED_COLUMN
				+ "=?";
		String whereArgs[] = {"0"};
		String order = null;

		Cursor resultCursor = cr.query(rowAddress, result_columns, where,
				whereArgs, order);

		return resultCursor;
	}
	
	public static void updateTask(ContentResolver cr, long rowId, String taskType,
			String timeStamp, String taskValue,
			String unitsConsumed, boolean synced) {
		
		// Create a new row of values to insert.
		ContentValues newValues = new ContentValues();

		// Assign values for each row.
		newValues.put(AmuletContentProvider.KEY_TASKS_TASKTYPE_COLUMN, taskType);
		newValues.put(AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN, timeStamp);
		newValues.put(AmuletContentProvider.KEY_TASKS_TASKVALUE_COLUMN, taskValue);
		newValues.put(AmuletContentProvider.KEY_TASKS_UNITSCONSUMED_COLUMN, unitsConsumed);
		newValues.put(AmuletContentProvider.KEY_TASKS_SYNCED_COLUMN, synced);
		
		

		Uri rowAddress = ContentUris.withAppendedId(
				AmuletContentProvider.CONTENT_URI_TASKS, rowId);
		
		// Insert the row into your table
		cr.update(rowAddress, newValues, null, null);
		
	}
	
	public JSONObject taskObject(Context context, String unitsConsumed, int score, String task, String timeStamp){
		JSONObject obj = new JSONObject();
		String username = SharedPreferencesWrapper.getFromPrefs(context, "username", "default");
		String password = SharedPreferencesWrapper.getFromPrefs(context, "password", "default");
		JSONObject tasks = new JSONObject();
		
		
		
		JSONArray taskArray = new JSONArray();
		
		Log.i("time: ", timeStamp);
		String taskValue = score + "";
		
		try {
			tasks.put("tasktype",  task);
			tasks.put("timestamp",  timeStamp);
			tasks.put("taskvalue",  taskValue);
			tasks.put("unitsconsumed",  unitsConsumed);
			
			taskArray.put(tasks);
			obj.put("username", username);
			obj.put("password", password);
			obj.put("tasks", taskArray);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public JSONObject packTaskCursor(Context context, Cursor taskCursor){
		
		
		
		JSONObject obj = new JSONObject();
		String username = SharedPreferencesWrapper.getFromPrefs(context, "username", "default");
		String password = SharedPreferencesWrapper.getFromPrefs(context, "password", "default");
		
		
		
		
		JSONArray taskArray = new JSONArray();
		
		
		
		
		try {
			while (taskCursor.moveToNext()) {
				JSONObject entries = new JSONObject();
				String unitsConsumed = taskCursor
						.getString(taskCursor
								.getColumnIndex(AmuletContentProvider.KEY_TASKS_UNITSCONSUMED_COLUMN));
				String taskValue = taskCursor
						.getString(taskCursor
								.getColumnIndex(AmuletContentProvider.KEY_TASKS_TASKVALUE_COLUMN));
				String task = taskCursor
						.getString(taskCursor
								.getColumnIndex(AmuletContentProvider.KEY_TASKS_TASKTYPE_COLUMN));
				String timeStamp = taskCursor
						.getString(taskCursor
								.getColumnIndex(AmuletContentProvider.KEY_TASKS_TIMESTAMP_COLUMN));
				entries.put("tasktype", task);
				entries.put("timestamp", timeStamp);
				entries.put("taskvalue", taskValue);
				entries.put("unitsconsumed", unitsConsumed);
				taskArray.put(entries);
			}
			
			obj.put("username", username);
			obj.put("password", password);
			obj.put("tasks", taskArray);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public boolean syncTasksFromServer(ContentResolver cr, String httpData){
		
		Log.i("synctask", "try:");
		try {
			
				JSONArray jsonArray = new JSONArray(httpData);
				//here query the database for the full set of synced data, this way we can check if we're up to data.
				Cursor taskHistory = getTaskHistoryComplete(cr);
				//if(!(taskHistory.getCount() == jsonArray.length())){//only works if data is posted first
				
				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject innerObj = jsonArray.getJSONObject(i);
					String unitsConsumed = innerObj.getString("unitsconsumed");
					String taskValue = innerObj.getString("taskvalue");
					String taskType = innerObj.getString("tasktype");
					String timeStamp = innerObj.getString("timestamp");
					timeStamp = timeStamp.replace("T", " ");
					
					Cursor taskCursor = getTaskHistoryByTimeStamp(cr, timeStamp);
					if(!(taskCursor.getCount() > 0)){
						
						addNewTask(cr, taskType, timeStamp, taskValue, unitsConsumed, true);
					}
					taskCursor.close();
				}
				taskHistory.close();
			//}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public boolean updateSyncedTasks(ContentResolver cr, String httpData){
		Log.i("synctask", httpData);
		
		try {
			JSONObject obj = new JSONObject(httpData);
			if(obj.has("tasks")){
				
				JSONArray jsonArray = obj.getJSONArray("tasks");
				//here query the database for the full set of synced data, this way we can check if we're up to data.
				//Cursor taskHistory = getTaskHistoryComplete(cr);
				//if(!(taskHistory.getCount() == jsonArray.length())){//only works if data is posted first
				
				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject innerObj = jsonArray.getJSONObject(i);
					String unitsConsumed = innerObj.getString("unitsconsumed");
					String taskValue = innerObj.getString("taskvalue");
					String taskType = innerObj.getString("tasktype");
					String timeStamp = innerObj.getString("timestamp");
					
					Cursor current = getTaskHistoryByTimeStamp(cr, timeStamp);
					//Log.i("count", current.getCount()+"");
					if(current.getCount() > 0){
					current.moveToFirst();
					long rowId = current.getLong(current.getColumnIndex(AmuletContentProvider.KEY_ID));
					Log.i("rowid", rowId+"");
					updateTask(cr, rowId, taskType, timeStamp, taskValue, unitsConsumed, true);
					}
					current.close();
				}
				//taskHistory.close();
			//}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
}
