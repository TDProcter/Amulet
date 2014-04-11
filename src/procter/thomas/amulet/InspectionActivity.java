package procter.thomas.amulet;


import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
		getActionBar().hide();
		setContentView(R.layout.placeholder);
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
		//intent results
		Intent intent = new Intent(this, ResultsActivity.class);
		intent.putExtra("mode",getIntent().getStringExtra("mode"));
		intent.putExtra("score", gameSpeed[gameSpeedPos]);
		intent.putExtra("task", "Inspection");
		intent.putExtra("unit", "ms");
		startActivity(intent);
	}
	
	
}
