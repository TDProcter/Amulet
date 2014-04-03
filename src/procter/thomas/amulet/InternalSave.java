package procter.thomas.amulet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class InternalSave{
	private String taskHistoryFilename = "taskHistory_file";
	private String drinkDiaryFilename = "drinkDiary_file";
	
	public void saveTaskHistory(Context context, JSONObject obj){
		
		save(context, obj, taskHistoryFilename);
		
	}
	
	public void saveDrinkDiary(Context context, JSONObject obj){

		save(context, obj, drinkDiaryFilename);
		
	}
	
	private void save(Context context, JSONObject obj, String FILENAME){
		String saveString = obj.toString();
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(saveString.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*public JSONObject readTaskHistory(Context context){
		JSONObject returnObj = readFromFile(context, taskHistoryFilename);
		return returnObj;
	}*/
	
	public JSONObject readDrinkDiary(Context context){
		String string = readFromFile(context, drinkDiaryFilename);
		JSONObject returnObj = new JSONObject();
		try {
			returnObj = new JSONObject(string);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnObj;
	}
	
	private String readFromFile(Context context, String FILENAME){
		String returnString = null;
		
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			fis.read();
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			returnString = reader.readLine();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return returnString;
	}
}
