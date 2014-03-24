package procter.thomas.amulet;


import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class InspectionActivity extends Activity{
	
	private static ImageButton[] bottleButtons;
	private static int blueBottlePos;
	private int timeElapse = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("test", "test");
		super.onCreate(savedInstanceState);
		Log.i("test", "test");
		setContentView(R.layout.placeholder);
		Random random = new Random();
		blueBottlePos = random.nextInt(4);
		
		
	}
	
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
		
		
		final long startTime = System.currentTimeMillis();
		boolean exit = false;
		
		
		
	    
		/*
		while(!exit){
			long currentTime = System.currentTimeMillis();
			Log.i("time", currentTime + "  " + startTime + "  " + (currentTime-startTime)+"");
			if((currentTime - startTime) > timeElapse){
				exit=true;
			}
		}
		
		
		for(int i = 0; i <bottleButtons.length; i++){
			
				bottleButtons[i].setImageResource(R.drawable.blank_bottle);
			
		}*/
	}
	
	public void bottleLoad(){
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
	}
	
	public void bottleButton0(View view){
		if(blueBottlePos == 0){
			bottleLoad();
		}
	}
	public void bottleButton1(View view){
		if(blueBottlePos == 1){
			bottleLoad();
		}
	}
	public void bottleButton2(View view){
		if(blueBottlePos == 2){
			bottleLoad();
		}
	}
	public void bottleButton3(View view){
		if(blueBottlePos == 3){
			bottleLoad();
		}
	}
	public void bottleButton4(View view){
		if(blueBottlePos == 4){
			bottleLoad();
		}
	}

}
