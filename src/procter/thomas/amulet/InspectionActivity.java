package procter.thomas.amulet;


import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class InspectionActivity extends Activity{
	
	private static ImageButton[] bottleButtons;
	private static int blueBottlePos;
	private int[] gameSpeed = {1000, 750, 562, 422, 316, 237, 178, 133, 56, 42, 32};
	private int gameSpeedPos;
	private int lastGameSpeed = 0;
	private Handler handler = new Handler();
	private boolean allowClick = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.placeholder);
		Random random = new Random();
		blueBottlePos = random.nextInt(4);
		gameSpeedPos = 0;
		
		
	}
	
	private Runnable runnable = new Runnable() {
		   @Override
		   public void run() {
		      /* do what you need to do */
			   for(int i = 0; i <bottleButtons.length; i++){
					
					bottleButtons[i].setImageResource(R.drawable.blank_bottle);
				
			}
		      /* and here comes the "trick" */
		      allowClick = true;
		      
		    	  
		      
		      Log.i("gamespeedpos: ", gameSpeedPos +"");
		      Log.i("gamespeed: ", gameSpeed[gameSpeedPos]+"");
		      
		   }
		};
		
		private Runnable bottleLoadRunnable = new Runnable() {
			   @Override
			   public void run() {
			      /* do what you need to do */

				   Random random = new Random();
					blueBottlePos = random.nextInt(5);
					
					for(int i = 0; i <bottleButtons.length; i++){
						if(i == blueBottlePos){
							bottleButtons[i].setImageResource(R.drawable.blue_bottle);
						}
						else{
							bottleButtons[i].setImageResource(R.drawable.green_bottle);
						}
					}
					
					handler.postDelayed(runnable, gameSpeed[gameSpeedPos]);
					
			      
			   }
			};
	
	public void portraitLoad(){
		setContentView(R.layout.activity_inspection_portrait);
		
		bottleButtons = new ImageButton[5];
		bottleButtons[0] = (ImageButton) findViewById(R.id.btnBottleInspectionPortrait0);
		bottleButtons[1] = (ImageButton) findViewById(R.id.btnBottleInspectionPortrait1);
		bottleButtons[2] = (ImageButton) findViewById(R.id.btnBottleInspectionPortrait2);
		bottleButtons[3] = (ImageButton) findViewById(R.id.btnBottleInspectionPortrait3);
		bottleButtons[4] = (ImageButton) findViewById(R.id.btnBottleInspectionPortrait4);
	}
	
	public void startButton(View view){
		
		
		portraitLoad();
		bottleLoad();
		
	}
	
	public void bottleLoad(){
		allowClick = false;
		handler.postDelayed(bottleLoadRunnable, 2000);
		
	}
	
	public void bottleButton(View view){
		if(allowClick ==true)
		{
			
			int buttonPos = -1;
			if(view.getId() == R.id.btnBottleInspectionPortrait0){
				buttonPos = 0;
			}
			else if(view.getId() == R.id.btnBottleInspectionPortrait1){
				buttonPos = 1;
			}
			else if(view.getId() == R.id.btnBottleInspectionPortrait2){
				buttonPos = 2;
			}
			else if(view.getId() == R.id.btnBottleInspectionPortrait3){
				buttonPos = 3;
			}
			else if(view.getId() == R.id.btnBottleInspectionPortrait4){
				buttonPos = 4;
			}
			
			if(blueBottlePos == buttonPos){
				if(gameSpeedPos  < gameSpeed.length - 1)
		    	  {
		    		  if(lastGameSpeed == gameSpeed[gameSpeedPos])
			    	  {
		    			  gameSpeedPos++;
		    	  
		    			  
			    	  }
		    		  lastGameSpeed = gameSpeed[gameSpeedPos];
		    	  }
		    	  else{
		    		  if(lastGameSpeed == gameSpeed[gameSpeedPos])
			    	  {
		    			  Log.i("end clause", "end");
		    			  endClause();
			    	  }
		    	  }
				
				bottleLoad();
			}
			else{
				endClause();
			}
		}
	}
	
	
	private void endClause(){
		setContentView(R.layout.activity_inspection_results);
		TextView textView = (TextView) findViewById(R.id.txtInspectionScore);
		textView.setText("Score: " + gameSpeed[gameSpeedPos] + "ms");
	}
	
	public void postToServer(View v){
		TextView unitsTextView = (TextView) findViewById(R.id.txtUnitsConsumed);
		String unitsConsumed = unitsTextView.getText().toString();
		Log.i("units", unitsConsumed+"");
		JSONObject HTTPString = taskObject(unitsConsumed);
		Log.i("JSON", HTTPString.toString());
	}
	
	private JSONObject taskObject(String unitsConsumed){
		JSONObject obj = new JSONObject();
		String username = SharedPreferencesWrapper.getFromPrefs(this, "username", "default");
		String password = SharedPreferencesWrapper.getFromPrefs(this, "password", "default");
		JSONObject tasks = new JSONObject();
		String taskType = "inspection";
		Time time = new Time();
		time.setToNow();
		String timeStamp = time.format("%Y-%m-%d %H:%M:%S");
		
		JSONArray taskArray = new JSONArray();
		
		Log.i("time: ", timeStamp);
		String taskValue = gameSpeed[gameSpeedPos] + "";
		
		try {
			tasks.put("tasktype",  taskType);
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
}
